package com.vuvarov.rashod.repository;

import com.vuvarov.rashod.model.AppParam;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppParamRepository extends PagingAndSortingRepository<AppParam, Long> {
}
