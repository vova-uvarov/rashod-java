package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.service.interfaces.IOperationService;
import com.vuvarov.rashod.web.dto.CreateOperationDto;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
public class OperationController {

    private final IOperationService service;

    @GetMapping("/search")
    Page<Operation> search(OperationFilterDto filterDto,
                           @PageableDefault(sort = {"operationDate", "insTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return service.search(filterDto, pageable);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public Operation get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public Operation createOperations(@RequestBody CreateOperationDto createOperationDto) {
        return service.save(createOperationDto);
    }
}
