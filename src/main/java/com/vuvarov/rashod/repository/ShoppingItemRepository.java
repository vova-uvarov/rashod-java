package com.vuvarov.rashod.repository;

import com.vuvarov.rashod.model.ShoppingItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingItemRepository extends PagingAndSortingRepository<ShoppingItem, Long> {
}
