package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.repository.OperationRepository;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
public class OperationController extends RestRepositoryController<Operation, Long, OperationRepository> {

    @GetMapping("/search")
    Page<Operation> search(OperationFilterDto filterDto, @PageableDefault(sort = "operationDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return repository.findAllByOperationTypeInAndPlan(filterDto.getOperationTypes(), filterDto.getIsPlan(), pageable);
    }
}
