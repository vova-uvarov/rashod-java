package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.enums.OperationType;
import com.vuvarov.rashod.repository.OperationRepository;
import com.vuvarov.rashod.web.dto.StatisticItemDto;
import com.vuvarov.rashod.web.dto.Statistics;
import com.vuvarov.rashod.web.dto.StatisticsFilterDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MMM");
    private final OperationRepository operationRepository;

    @GetMapping("/incomeConsumptionByMonth")
    public Statistics incomeConsumptionByMonthLastPrevious(StatisticsFilterDto filter) {
        LocalDate firstDayInMonth = filter.getFrom().withDayOfMonth(1);
        LocalDate endDate = filter.getTo().with(TemporalAdjusters.lastDayOfMonth());
        List<String> labels = new ArrayList<>();
        List<BigDecimal> incomeData = new ArrayList<>();
        List<BigDecimal> consumptionData = new ArrayList<>();

        while (firstDayInMonth.isBefore(endDate)) {
            labels.add(DF.format(firstDayInMonth));

            LocalDateTime dateFrom = firstDayInMonth.atStartOfDay();
            LocalDateTime dateTo = firstDayInMonth.with(TemporalAdjusters.lastDayOfMonth()).atStartOfDay();
            List<Long> excludeCategoryIds = ObjectUtils.defaultIfNull(filter.getExcludeCategoryIds(), new ArrayList<>());

            List<Operation> operationForCurrentMonth = operationRepository.findAllByOperationDateBetween(dateFrom, dateTo)
                    .stream()
                    .filter(op -> !excludeCategoryIds.contains(op.getCategory().getId()))
                    .collect(Collectors.toList());

            incomeData.add(incomeSum(operationForCurrentMonth));
            consumptionData.add(consumptionSum(operationForCurrentMonth));

            firstDayInMonth = firstDayInMonth.plusMonths(1);
        }

        List<StatisticItemDto> datasets = new ArrayList<>();
        datasets.add(StatisticItemDto.builder()
                .name("Доход")
                .data(incomeData)
                .build());

        datasets.add(StatisticItemDto.builder()
                .name("Расход")
                .data(consumptionData)
                .build());

        return Statistics.builder()
                .labels(labels)
                .datasets(datasets)
                .build();
    }

    private BigDecimal consumptionSum(List<Operation> operations) {
        return sum(operations, OperationType.CONSUMPTION);
    }

    private BigDecimal incomeSum(List<Operation> operations) {
        return sum(operations, OperationType.INCOME);
    }

    private BigDecimal sum(List<Operation> operations, OperationType consumption) {
        return operations.stream()
                .filter(op -> op.getOperationType().equals(consumption))
                .filter(op -> !op.isPlan())
                .map(Operation::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
