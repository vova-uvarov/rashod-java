package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.enums.OperationType;
import com.vuvarov.rashod.repository.OperationRepository;
import com.vuvarov.rashod.statistics.GroupByDateCalculator;
import com.vuvarov.rashod.statistics.LabelFormatter;
import com.vuvarov.rashod.web.dto.StatisticItemDto;
import com.vuvarov.rashod.web.dto.Statistics;
import com.vuvarov.rashod.web.dto.statistics.StatisticsFilterDto;
import com.vuvarov.rashod.web.dto.statistics.StatisticsGroupBy;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.util.Pair;
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
    private final OperationRepository operationRepository;
    private final LabelFormatter labelFormatter;

    @GetMapping("/incomeConsumptionByGroup")
    public Statistics incomeConsumptionByMonthLastPrevious(StatisticsFilterDto filter) {
        GroupByDateCalculator calculator = new GroupByDateCalculator(filter.getFrom(), filter.getTo(), filter.getGroupBy());

        List<String> labels = new ArrayList<>();
        List<BigDecimal> incomeData = new ArrayList<>();
        List<BigDecimal> consumptionData = new ArrayList<>();

        Pair<LocalDateTime, LocalDateTime> interval = calculator.nextDate();
        while (interval != null) {
            labels.add(labelFormatter.format(interval.getFirst(), filter.getGroupBy()));
            List<Long> excludeCategoryIds = ObjectUtils.defaultIfNull(filter.getExcludeCategoryIds(), new ArrayList<>());

            List<Operation> operationForCurrentMonth = operationRepository.findAllByOperationDateBetween(interval.getFirst(), interval.getSecond())
                    .stream()
                    .filter(op -> !excludeCategoryIds.contains(op.getCategory().getId()))
                    .collect(Collectors.toList());

            incomeData.add(incomeSum(operationForCurrentMonth));
            consumptionData.add(consumptionSum(operationForCurrentMonth));

            interval = calculator.nextDate();
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
