package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
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
}
