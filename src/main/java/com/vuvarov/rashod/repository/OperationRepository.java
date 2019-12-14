package com.vuvarov.rashod.repository;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.enums.OperationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationRepository extends PagingAndSortingRepository<Operation, Long>, JpaSpecificationExecutor<Operation> {
    @Query("select distinct op.place from Operation op order by op.place")
    List<String> getAllPlaces();  // todo это можно кэшировать

    @Query("select min(op.operationDate) from Operation op")
    LocalDateTime minOperationDate(); // todo это можно кэшировать

    long countAllByOperationDateBeforeAndPlan(LocalDateTime date, boolean isPlan);
}
