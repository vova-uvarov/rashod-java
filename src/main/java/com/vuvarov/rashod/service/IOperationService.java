package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOperationService {

    public List<Operation> findAllOperations(Long accountId, boolean includePlans);

    Page<Operation> search(OperationFilterDto filter, Pageable pageable);
}
