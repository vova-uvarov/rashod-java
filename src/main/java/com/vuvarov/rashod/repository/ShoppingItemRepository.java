package com.vuvarov.rashod.repository;

import com.vuvarov.rashod.model.ShoppingItem;
import com.vuvarov.rashod.model.enums.OperationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingItemRepository extends PagingAndSortingRepository<ShoppingItem, Long> {
    @Query("SELECT c FROM ShoppingItem c WHERE LOWER(c.name) LIKE LOWER(?1)")
    List<ShoppingItem> findAllByName(String q, Pageable pageable);

    List<ShoppingItem> findAllByOperationId(Long operationId);

    @Query("SELECT distinct c.name FROM ShoppingItem c" +
            " join Operation o on o.id=c.operationId" +
            " where o.operationType=:operationType" +
            " order by c.name")
    List<String> findAllNamesByOperationType(@Param("operationType")OperationType operationType);
}
