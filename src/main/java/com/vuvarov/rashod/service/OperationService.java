package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.repository.OperationRepository;
import com.vuvarov.rashod.repository.specification.OperationSpecification;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationService implements IOperationService {

    private final OperationRepository repository;

    @Override
    public List<Operation> findAllOperations(Long accountId, boolean includePlans) {
        return repository.findAllByAccountIdOrAccountToTransferIdAndPlan(accountId, accountId, includePlans);
    }

    @Override
    public Page<Operation> search(OperationFilterDto filter, Pageable pageable) {
        return repository.findAll(new OperationSpecification(filter), pageable);
    }
}
