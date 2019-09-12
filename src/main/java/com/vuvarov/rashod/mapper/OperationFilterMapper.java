package com.vuvarov.rashod.mapper;

import com.vuvarov.rashod.service.interfaces.IOperationService;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import com.vuvarov.rashod.web.dto.statistics.StatisticsFilterDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class OperationFilterMapper {

    private final IOperationService operationService;

    public OperationFilterDto from(StatisticsFilterDto source) {
        OperationFilterDto filter = new OperationFilterDto();
        filter.setDateFrom(ObjectUtils.defaultIfNull(source.getFrom(), operationService.minOperationDate()));
        filter.setDateTo(ObjectUtils.defaultIfNull(source.getFrom(), LocalDate.now()));
        filter.setCategoryIds(source.getIncludeCategoryIds());
        filter.setExcludeCategoryIds(source.getExcludeCategoryIds());
        filter.setIsPlan(BooleanUtils.isTrue(source.getIsPlan()));
        filter.setOperationTypes(source.getOperationTypes());

        return filter;
    }


}
