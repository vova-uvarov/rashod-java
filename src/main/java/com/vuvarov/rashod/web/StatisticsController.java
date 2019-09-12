package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Category;
import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.enums.OperationType;
import com.vuvarov.rashod.model.param.ParamGroup;
import com.vuvarov.rashod.model.param.ParamKey;
import com.vuvarov.rashod.repository.AppParamRepository;
import com.vuvarov.rashod.repository.CategoryRepository;
import com.vuvarov.rashod.service.OperationService;
import com.vuvarov.rashod.statistics.GroupByDateCalculator;
import com.vuvarov.rashod.statistics.LabelFormatter;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import com.vuvarov.rashod.web.dto.StatisticItemDto;
import com.vuvarov.rashod.web.dto.Statistics;
import com.vuvarov.rashod.web.dto.statistics.MonthPlanDto;
import com.vuvarov.rashod.web.dto.statistics.StatisticsFilterDto;
import com.vuvarov.rashod.web.dto.statistics.StatisticsGroupBy;
import com.vuvarov.rashod.web.dto.statistics.StatisticsPieData;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.math.RoundingMode.HALF_DOWN;
import static java.util.Collections.singletonList;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final AppParamRepository paramRepository;
    private final OperationService operationService;
    private final CategoryRepository categoryRepository;
    private final LabelFormatter labelFormatter;

    @GetMapping("/averageByYearTrend")
    public Statistics averageByYearTrend(StatisticsFilterDto filter) {
        GroupByDateCalculator calculator = new GroupByDateCalculator(operationService.minOperationDate().toLocalDate(), LocalDate.now(), StatisticsGroupBy.YEAR);
        List<String> labels = new ArrayList<>();
        LocalDate now = LocalDate.now();
        Pair<LocalDate, LocalDate> interval = calculator.nextDate();
        List<BigDecimal> data = new ArrayList<>();
        while (interval != null) {
            labels.add(labelFormatter.format(interval.getFirst(), StatisticsGroupBy.YEAR));
            List<Operation> operations = operationService.search(OperationFilterDto.builder()
                    .dateFrom(interval.getFirst())
                    .dateTo(interval.getSecond())
                    .categoryIds(filter.getIncludeCategoryIds())
                    .isPlan(false)
                    .build(), Pageable.unpaged()).getContent();
            BigDecimal sum = consumptionSum(operations);
            if (interval.getFirst().getYear() == now.getYear()) {
                data.add(sum.divide(BigDecimal.valueOf(now.getMonthValue()), HALF_DOWN));
            } else {
                data.add(sum.divide(BigDecimal.valueOf(12), HALF_DOWN));
            }
            interval = calculator.nextDate();
        }

        String categoryName = "Все";
        if (CollectionUtils.isNotEmpty(filter.getIncludeCategoryIds())) {
            Iterable<Category> categories = categoryRepository.findAllById(filter.getIncludeCategoryIds());

            categoryName = IterableUtils.toList(categories)
                    .stream()
                    .map(Category::getName)
                    .collect(Collectors.joining(","));
        }
        return Statistics.builder()
                .labels(labels)
                .datasets(singletonList(StatisticItemDto.builder()
                        .data(data)
                        .name(categoryName)
                        .build()))
                .build();
    }

    @GetMapping("/categoryTrend")
    public Statistics categoryTrend(StatisticsFilterDto filter) {
        normalizeFilter(filter);
        GroupByDateCalculator calculator = new GroupByDateCalculator(filter.getFrom(), filter.getTo(), StatisticsGroupBy.YEAR);

        List<String> labels = new ArrayList<>();

        Pair<LocalDate, LocalDate> interval = calculator.nextDate();
        List<Long> excludeCategoryIds = ObjectUtils.defaultIfNull(filter.getExcludeCategoryIds(), new ArrayList<>());
        List<StatisticItemDto> datasets = new ArrayList<>();
        while (interval != null) {
            GroupByDateCalculator monthCalculator = new GroupByDateCalculator(interval.getFirst(), interval.getSecond(), StatisticsGroupBy.MONTH);
            Pair<LocalDate, LocalDate> monthInterval = monthCalculator.nextDate();
            List<BigDecimal> yearData = new ArrayList<>();
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
            datasets.add(StatisticItemDto.builder()
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
    public List<StatisticsPieData> sumsByCategory(StatisticsFilterDto filter) {
        normalizeFilter(filter);
//        todo стоит сделать пагинацию
        List<Operation> operations = operationService.search(OperationFilterDto.builder()
                .dateFrom(filter.getFrom())
                .dateTo(filter.getTo())
                .operationTypes(filter.getOperationTypes())
                .isPlan(false)
                .build(), Pageable.unpaged()).getContent();
        Map<String, BigDecimal> sums = new HashMap<>();
        operations.forEach(op -> {
            String categoryName = op.getCategory().getName();
            BigDecimal sumForCategory = sums.getOrDefault(categoryName, BigDecimal.ZERO);
            sums.put(categoryName, sumForCategory.add(op.getCost()));
        });

        return sums.entrySet().stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Map.Entry::getValue)))
                .filter(entry -> entry.getValue().compareTo(BigDecimal.ZERO) > 0)
                .map(entry -> StatisticsPieData.builder()
                        .name(entry.getKey())
                        .sum(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }


    @GetMapping("/averageByDayInCurrMonth")
    public Statistics averageByDayInCurrMonth(StatisticsFilterDto filter) {
        normalizeFilter(filter);
        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();
        LocalDate currentCalcDate = LocalDate.now().withDayOfMonth(1);
        List<Long> excludeCategoryIds = ObjectUtils.defaultIfNull(filter.getExcludeCategoryIds(), new ArrayList<>());

        BigDecimal totalSum = BigDecimal.ZERO;
        while (currentCalcDate.isBefore(LocalDate.now().plusDays(1))) {
            labels.add(labelFormatter.format(currentCalcDate, StatisticsGroupBy.DAY));
            List<Operation> currentOperations = getOperations(currentCalcDate, currentCalcDate);

            currentOperations = currentOperations.stream()
                    .filter(op -> !excludeCategoryIds.contains(op.getCategory().getId()))// todo
                    .collect(Collectors.toList());
            BigDecimal consumptionSum = consumptionSum(currentOperations);
            totalSum = totalSum.add(consumptionSum);
            data.add(totalSum.divide(BigDecimal.valueOf(currentCalcDate.getDayOfMonth()), HALF_DOWN));
            currentCalcDate = currentCalcDate.plusDays(1);
        }

        List<StatisticItemDto> datasets = new ArrayList<>();
        datasets.add(StatisticItemDto.builder()
                .name("Динамика среднего расхода")
                .data(data)
                .build());

        return Statistics.builder()
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
//        private BigDecimal canSpend;
//        private BigDecimal canSpendFroPlan;
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
        List<BigDecimal> incomeData = new ArrayList<>();
        List<BigDecimal> consumptionData = new ArrayList<>();

        Pair<LocalDate, LocalDate> interval = calculator.nextDate();
        while (interval != null) {
            labels.add(labelFormatter.format(interval.getFirst(), filter.getGroupBy()));
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


    private void normalizeFilter(StatisticsFilterDto filter) {
        if (filter.getTo() == null) {
            filter.setTo(LocalDate.now());
        }

        if (filter.getFrom() == null) {
            filter.setFrom(operationService.minOperationDate().toLocalDate());
        }
    }
}
