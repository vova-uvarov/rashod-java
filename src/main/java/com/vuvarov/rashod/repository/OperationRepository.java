package com.vuvarov.rashod.repository;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.enums.OperationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends PagingAndSortingRepository<Operation, Long> {
    List<Operation> findAllByAccountIdOrAccountToTransferIdAndPlan(Long accountId, Long accotunToTransferId, boolean plan);

    Page<Operation> findAllByOperationTypeInAndPlan(List<OperationType> operationTypes, boolean isPlan, Pageable pageable);
}
