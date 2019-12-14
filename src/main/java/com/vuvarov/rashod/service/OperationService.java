package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.Model;
import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.ShoppingItem;
import com.vuvarov.rashod.model.factory.OperationFactory;
import com.vuvarov.rashod.processor.IProcessor;
import com.vuvarov.rashod.repository.OperationRepository;
import com.vuvarov.rashod.repository.ShoppingItemRepository;
import com.vuvarov.rashod.repository.specification.OperationSpecification;
import com.vuvarov.rashod.service.interfaces.IOperationService;
import com.vuvarov.rashod.web.dto.CreateOperationDto;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperationService implements IOperationService {

    private final OperationRepository repository;
    private final ShoppingItemRepository shoppingItemRepository;
    @Autowired
    private List<IProcessor<Operation>> processors;

    @Override
    public LocalDateTime minOperationDateTime() {
        return repository.minOperationDate();
    }

    @Override
    public LocalDate minOperationDate() {
        return repository.minOperationDate().toLocalDate();
    }

    @Override
    public List<Operation> search(OperationFilterDto filter) {
        return search(filter, Pageable.unpaged()).getContent();
    }

    @Override
    public Page<Operation> search(OperationFilterDto filter, Pageable pageable) {
        return repository.findAll(new OperationSpecification(filter), pageable);
    }

    @Override
    public Operation save(CreateOperationDto request) {
        Operation operation = request.getOperation();
        processors.forEach(p -> p.process(operation));

        boolean isCreate = isCreate(request);
        boolean isDivide = isDivide(request);
        boolean isRepeat = isRepeat(request);

        if (isCreate) {
            if (isDivide) {
//                todo наверное нужна валидация хотябы что оставшаяся сумма НЕ открицательная
                Operation parentOperation = get(operation.getParentId());
                BigDecimal newCost = parentOperation.getCost().subtract(operation.getCost());
                parentOperation.setCost(newCost);
                saveWithItems(parentOperation);
                return saveWithItems(operation);
            }

            Operation mainSavedOperation = saveWithItems(operation);
            if (isRepeat) {
                Long countRepeat = ObjectUtils.defaultIfNull(request.getCountRepeat(), 1L);
                saveRepeats(mainSavedOperation, countRepeat);
            }
            return mainSavedOperation;
        }

        return saveWithItems(operation);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Operation get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("operation not found with id: " + id));
    }

    @Override
    public long countPlans() {
        return repository.countAllByOperationDateBeforeAndPlan(LocalDate.now().atStartOfDay().with(LocalTime.MAX), true);
    }

    private void saveRepeats(Operation baseOperation, Long countRepeat) {
        LocalDateTime currentOperationDate = baseOperation.getOperationDate();
        for (int i = 1; i < countRepeat; i++) {
            Operation operationForSave = OperationFactory.copy(baseOperation);
            operationForSave.setId(null);
            operationForSave.setPlan(true);
            currentOperationDate = currentOperationDate.plusMonths(1);
            operationForSave.setOperationDate(currentOperationDate);
            saveWithItems(operationForSave);
        }
    }

    private Operation saveWithItems(Operation operation) {
        Operation savedOperation = repository.save(operation);
        List<ShoppingItem> shoppingList = operation.getShoppingList();
        if (shoppingList != null) {
            Set<Long> itemIds = shoppingList.stream()
                    .filter(op -> op.getId() != null)
                    .map(Model::getId)
                    .collect(Collectors.toSet());
            List<ShoppingItem> existsOperations = shoppingItemRepository.findAllByOperationId(savedOperation.getId());
            existsOperations.stream()
                    .filter(op -> !itemIds.contains(op.getId()))
                    .forEach(op -> shoppingItemRepository.deleteById(op.getId()));


            shoppingList.forEach(item -> item.setOperationId(savedOperation.getId()));
            savedOperation.setShoppingList(toList(shoppingList));
        }
        return savedOperation;
    }

    private List<ShoppingItem> toList(List<ShoppingItem> shoppingList) {
        return IterableUtils.toList(shoppingItemRepository.saveAll(shoppingList));
    }

    private boolean isCreate(CreateOperationDto request) {
        return request.getOperation().getId() == null;
    }

    private boolean isDivide(CreateOperationDto request) {
        return isCreate(request) && request.getOperation().getParentId() != null;
    }

    private boolean isRepeat(CreateOperationDto request) {
        return isCreate(request) && request.getCountRepeat() > 0;
    }
}
