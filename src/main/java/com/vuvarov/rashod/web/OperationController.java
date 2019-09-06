package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.enums.OperationType;
import com.vuvarov.rashod.repository.OperationRepository;
import com.vuvarov.rashod.service.IOperationService;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
public class OperationController extends RestRepositoryController<Operation, Long, OperationRepository> {

    private final IOperationService operationService;

    @GetMapping("/search")
    Page<Operation> search(OperationFilterDto filterDto,
                           @PageableDefault(sort = "operationDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return operationService.search(filterDto, pageable);
    }

    @Override
    public Operation save(@RequestBody Operation entity) {
        if (entity.getCurrencyCost() == null) {
            entity.setCurrencyCost(entity.getCost());
        }

        if (!entity.getOperationType().equals(OperationType.TRANSFER)) {
            entity.setAccountToTransfer(null);
        }

        entity.setAuthor("vuvarov"); // todo hack пока нет пользователей
        return super.save(entity);
    }
}
