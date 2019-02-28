package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.Operation;

import java.util.List;

public interface IOperationService {

    public List<Operation> findAllOperations(Long accountId, boolean includePlans);
}
