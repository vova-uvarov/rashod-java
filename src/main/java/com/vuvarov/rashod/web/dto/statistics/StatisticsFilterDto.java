package com.vuvarov.rashod.web.dto.statistics;

import com.vuvarov.rashod.model.enums.OperationType;
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
    List<Long> includeCategoryIds;

    List<OperationType> operationTypes;

    StatisticsGroupBy groupBy;
    StatisticsSumCalculatorType calculatorType;

    Boolean isPlan;
}
