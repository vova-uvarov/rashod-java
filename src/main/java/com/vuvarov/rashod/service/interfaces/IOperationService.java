package com.vuvarov.rashod.service.interfaces;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.web.dto.CreateOperationDto;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IOperationService {

    LocalDateTime minOperationDateTime();

    LocalDate minOperationDate();

    List<Operation> search(OperationFilterDto filter);

    Page<Operation> search(OperationFilterDto filter, Pageable pageable);

    Operation save(CreateOperationDto request);

    void delete(Long id);

    Operation get(Long id);

    long countPlans();
}
