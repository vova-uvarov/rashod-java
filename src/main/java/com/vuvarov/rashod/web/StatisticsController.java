package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.enums.OperationType;
import com.vuvarov.rashod.model.param.ParamGroup;
import com.vuvarov.rashod.model.param.ParamKey;
import com.vuvarov.rashod.repository.AppParamRepository;
import com.vuvarov.rashod.service.OperationService;
import com.vuvarov.rashod.service.interfaces.IStatisticsService;
import com.vuvarov.rashod.statistics.LabelFormatter;
import com.vuvarov.rashod.statistics.dto.GroupByDateCalculator;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import com.vuvarov.rashod.web.dto.statistics.MonthPlanDto;
import com.vuvarov.rashod.web.dto.statistics.StatisticDataSet;
import com.vuvarov.rashod.web.dto.statistics.Statistics;
import com.vuvarov.rashod.web.dto.statistics.StatisticsFilterDto;
import com.vuvarov.rashod.web.dto.statistics.StatisticsGroupBy;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.RoundingMode.HALF_DOWN;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final AppParamRepository paramRepository;
    private final OperationService operationService;
    private final LabelFormatter labelFormatter;
    private final IStatisticsService statisticsService;

    @GetMapping("/averageByYearTrend")
    public Statistics averageByYearTrend(StatisticsFilterDto filter) {
        return statisticsService.averageByYearTrend(filter);
    }

    @GetMapping("/categoryTrend")
    public Statistics categoryTrend(StatisticsFilterDto filter) {
        normalizeFilter(filter);
        GroupByDateCalculator calculator = new GroupByDateCalculator(filter.getFrom(), filter.getTo(), StatisticsGroupBy.YEAR);

        List<String> labels = new ArrayList<>();

        Pair<LocalDate, LocalDate> interval = calculator.nextDate();
        List<Long> excludeCategoryIds = ObjectUtils.defaultIfNull(filter.getExcludeCategoryIds(), new ArrayList<>());
        List<StatisticDataSet> datasets = new ArrayList<>();
        while (interval != null) {
            GroupByDateCalculator monthCalculator = new GroupByDateCalculator(interval.getFirst(), interval.getSecond(), StatisticsGroupBy.MONTH);
            Pair<LocalDate, LocalDate> monthInterval = monthCalculator.nextDate();
            List<BigDecimal> yearData = new ArrayList<>();
//            todo странновая штука. Стоит убрать думаю
            labels = Arrays.asList("Янв", "Фер", "Март", "Апр", "Май", "Июнь", "Июль", "Авг", "Сен", "Окт", "Нояб", "Дек");
            while (monthInterval != null) {
                List<Operation> operationForCurrentMonth = operationService.search(OperationFilterDto.builder()
                        .dateFrom(monthInterval.getFirst())
                        .dateTo(monthInterval.getSecond())
                        .categoryIds(filter.getIncludeCategoryIds())
                        .isPlan(false)
                        .build(), Pageable.unpaged()).getContent();

                operationForCurrentMonth = operationForCurrentMonth
                        .stream()
                        .filter(op -> !excludeCategoryIds.contains(op.getCategory().getId()))
                        .collect(Collectors.toList());
                yearData.add(consumptionSum(operationForCurrentMonth));

                monthInterval = monthCalculator.nextDate();
            }
            datasets.add(StatisticDataSet.builder()
                    .name(String.valueOf(interval.getFirst().getYear()))
                    .data(yearData)
                    .build());

            interval = calculator.nextDate();
        }

        return Statistics.builder()
                .labels(labels)
                .datasets(datasets)
                .build();
    }

    @GetMapping("/sumsByCategory")
    public List<StatisticDataSet> sumsByCategory(StatisticsFilterDto filter) {
        return statisticsService.sumsByCategory(filter);
    }


    @GetMapping("/averageByDayInCurrMonth")
    public Statistics averageByDayInCurrMonth(StatisticsFilterDto filter) {
        normalizeFilter(filter);
        List<LocalDate> dates = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();
        LocalDate currentCalcDate = LocalDate.now().withDayOfMonth(1);
        List<Long> excludeCategoryIds = ObjectUtils.defaultIfNull(filter.getExcludeCategoryIds(), new ArrayList<>());

        BigDecimal totalSum = BigDecimal.ZERO;
        while (currentCalcDate.isBefore(LocalDate.now().plusDays(1))) {
            labels.add(labelFormatter.format(currentCalcDate, StatisticsGroupBy.DAY));
            dates.add(currentCalcDate);
            List<Operation> currentOperations = getOperations(currentCalcDate, currentCalcDate);

            currentOperations = currentOperations.stream()
                    .filter(op -> !excludeCategoryIds.contains(op.getCategory().getId()))// todo
                    .collect(Collectors.toList());
            BigDecimal consumptionSum = consumptionSum(currentOperations);
            totalSum = totalSum.add(consumptionSum);
            data.add(totalSum.divide(BigDecimal.valueOf(currentCalcDate.getDayOfMonth()), HALF_DOWN));
            currentCalcDate = currentCalcDate.plusDays(1);
        }

        List<StatisticDataSet> datasets = new ArrayList<>();
        datasets.add(StatisticDataSet.builder()
                .name("Динамика среднего расхода") // todo не должно этого тут быть
                .data(data)
                .build());

        return Statistics.builder()
                .dates(dates)
                .labels(labels)
                .datasets(datasets)
                .build();
    }

    private List<Operation> getOperations(LocalDate from, LocalDate to) { // todo Должен поидеи фильтр принимать
        return operationService.search(OperationFilterDto.builder()
                .dateFrom(from)
                .dateTo(to)
                .isPlan(false)
                .build(), Pageable.unpaged()).getContent();
    }

    @GetMapping("/plan/month")
    public MonthPlanDto monthPlan() {
        BigDecimal totalMonthPlan = paramRepository.findByGroupNameAndKeyName(ParamGroup.PLAN, ParamKey.SUM_TO_MONTH).getDecimalValue();
        YearMonth yearMonthObject = YearMonth.from(LocalDate.now());
        List<Operation> currentOperations = getOperations(LocalDate.now().withDayOfMonth(1), LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
        currentOperations = currentOperations.stream()
                .filter(op -> !op.getCategory().getId().equals(15L)) // todo
                .collect(Collectors.toList());
        BigDecimal consumptionSumInCurrentMonth = consumptionSum(currentOperations);
//        var canBeSpent = (((SUM_TO_MONTH / countDayInCurrentMonth) - averageInCurrentMonth) * dayOfMonth);
        BigDecimal dayOfMonth = BigDecimal.valueOf(LocalDate.now().getDayOfMonth());
        BigDecimal daysInMonth = BigDecimal.valueOf(yearMonthObject.lengthOfMonth());
        BigDecimal currentAverageSum = consumptionSumInCurrentMonth.divide(dayOfMonth, HALF_DOWN);
//        def spendNoMoreThan = (SUM_TO_MONTH - sumCurMonth) / (countDayInCurrentMonth - dayOfMonth + 1);
        BigDecimal planAverageSum = totalMonthPlan.divide(daysInMonth, HALF_DOWN);
        return MonthPlanDto.builder()
                .totalPlan(totalMonthPlan)
                .planForDay(planAverageSum)
                .currentAverageSum(currentAverageSum)
                .canSpent(planAverageSum.subtract(currentAverageSum).multiply(dayOfMonth))
                .canSpendForPlan(totalMonthPlan.subtract(consumptionSumInCurrentMonth).divide(daysInMonth.subtract(dayOfMonth).add(BigDecimal.ONE), HALF_DOWN))
                .build();
    }

    @GetMapping("/incomeConsumptionByGroup")
    public Statistics incomeConsumptionByGroup(StatisticsFilterDto filter) {
        normalizeFilter(filter);
        GroupByDateCalculator calculator = new GroupByDateCalculator(filter.getFrom(), filter.getTo(), filter.getGroupBy());

        List<String> labels = new ArrayList<>();
        List<LocalDate> dates = new ArrayList<>();
        List<BigDecimal> incomeData = new ArrayList<>();
        List<BigDecimal> consumptionData = new ArrayList<>();

        Pair<LocalDate, LocalDate> interval = calculator.nextDate();
        while (interval != null) {
            labels.add(labelFormatter.format(interval.getFirst(), filter.getGroupBy()));
            dates.add(interval.getFirst());
            List<Long> excludeCategoryIds = ObjectUtils.defaultIfNull(filter.getExcludeCategoryIds(), new ArrayList<>());

            List<Operation> operationForCurrentMonth = getOperations(interval.getFirst(), interval.getSecond());
            operationForCurrentMonth = operationForCurrentMonth
                    .stream()
                    .filter(op -> !excludeCategoryIds.contains(op.getCategory().getId()))
                    .collect(Collectors.toList());

            incomeData.add(incomeSum(operationForCurrentMonth));
            consumptionData.add(consumptionSum(operationForCurrentMonth));

            interval = calculator.nextDate();
        }

        List<StatisticDataSet> datasets = new ArrayList<>();
        datasets.add(StatisticDataSet.builder()
                .name("Доход")
                .data(incomeData)
                .build());

        datasets.add(StatisticDataSet.builder()
                .name("Расход")
                .data(consumptionData)
                .build());

        return Statistics.builder()
                .dates(dates)
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


    private void normalizeFilter(StatisticsFilterDto filter) {
        if (filter.getTo() == null) {
            filter.setTo(LocalDate.now());
        }

        if (filter.getFrom() == null) {
            filter.setFrom(operationService.minOperationDate());
        }
    }
}
