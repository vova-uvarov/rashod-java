package com.vuvarov.rashod.repository;

import com.vuvarov.rashod.model.Operation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends PagingAndSortingRepository<Operation, Long> {
    List<Operation> findAllByAccountIdOrAccountToTransferIdAndPlan(Long accountId, Long accotunToTransferId, boolean plan);
}
