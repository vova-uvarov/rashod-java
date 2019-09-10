package com.vuvarov.rashod.web.dto.statistics;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class StatisticsFilterDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate from;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate to;

    List<Long> excludeCategoryIds;

    StatisticsGroupBy groupBy;
}
