package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.web.dto.CreateOperationDto;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOperationService {

    Page<Operation> search(OperationFilterDto filter, Pageable pageable);

    Operation save(CreateOperationDto request);

    void delete(Long id);

    Operation get(Long id);
}
