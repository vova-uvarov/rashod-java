package com.vuvarov.rashod.repository;

import com.vuvarov.rashod.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(?1)")
        // todo параметр сделать именованым
    List<Category> findAllByName(String q, Pageable pageable); // todo надо возращать Page
}
