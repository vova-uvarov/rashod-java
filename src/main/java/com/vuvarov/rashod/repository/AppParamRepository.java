package com.vuvarov.rashod.repository;

import com.vuvarov.rashod.model.AppParam;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppParamRepository extends CrudRepository<AppParam, Long> {
}
