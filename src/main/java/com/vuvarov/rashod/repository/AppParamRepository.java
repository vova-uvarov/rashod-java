package com.vuvarov.rashod.repository;

import com.vuvarov.rashod.model.param.AppParam;
import com.vuvarov.rashod.model.param.ParamGroup;
import com.vuvarov.rashod.model.param.ParamKey;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppParamRepository extends PagingAndSortingRepository<AppParam, Long> {
    AppParam findByGroupNameAndKeyName(ParamGroup group, ParamKey key);
}
