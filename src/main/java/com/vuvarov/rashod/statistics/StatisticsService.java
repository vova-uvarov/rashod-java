package com.vuvarov.rashod.statistics;

import com.vuvarov.rashod.mapper.OperationFilterMapper;
import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.service.OperationService;
import com.vuvarov.rashod.service.interfaces.IStatisticsService;
import com.vuvarov.rashod.statistics.calculator.AverageByYearTrendCalculator;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import com.vuvarov.rashod.web.dto.statistics.StatisticDataSet;
import com.vuvarov.rashod.web.dto.statistics.Statistics;
import com.vuvarov.rashod.web.dto.statistics.StatisticsFilterDto;
import com.vuvarov.rashod.web.dto.statistics.StatisticsSumCalculatorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;
import static java.util.Collections.singletonList;

@Service
@RequiredArgsConstructor
public class StatisticsService implements IStatisticsService {
    private final AverageByYearTrendCalculator averageByYearTrendCalculator;
    private final OperationFilterMapper filterMapper;
    private final OperationService operationService;

    @Override
    public Statistics averageByYearTrend(StatisticsFilterDto filter) {
        return averageByYearTrendCalculator.calculate(filter);
    }

    @Override
    public List<StatisticDataSet> sumsByCategory(StatisticsFilterDto filter) {
////        todo стоит сделать пагинацию
        OperationFilterDto operationFilter = filterMapper.from(filter);
        List<Operation> operations = operationService.search(operationFilter);

        Map<String, BigDecimal> sums = new HashMap<>();
        operations.forEach(op -> {
            String categoryName = op.getCategory().getName();
            BigDecimal sumForCategory = sums.getOrDefault(categoryName, BigDecimal.ZERO);
            sums.put(categoryName, sumForCategory.add(op.getCost()));
        });
        if (StatisticsSumCalculatorType.MONTH_AVERAGE.equals(filter.getCalculatorType())) {
            LocalDate to = filter.getTo();
            if (to.isAfter(LocalDate.now())){ // todo похоже на хак. Как буд-то с фронта просто дожны корректно присылать
                to = LocalDate.now();
            }
            long months = ChronoUnit.MONTHS.between(filter.getFrom(), to);
            sums.keySet().forEach(key -> {
                BigDecimal averageSum = sums.get(key).divide(BigDecimal.valueOf(months+1), RoundingMode.HALF_DOWN);
                sums.put(key, averageSum);
            });
        }

        if (sums.size() == 0) {
            sums.put("Нет Данных", BigDecimal.ZERO);
        }

        return sums.entrySet().stream()
                .sorted(reverseOrder(Map.Entry.comparingByValue()))
                .map(this::buildData)
                .collect(Collectors.toList());
    }

    private StatisticDataSet buildData(Map.Entry<String, BigDecimal> entry) {
        return StatisticDataSet.builder()
                .name(entry.getKey())
                .data(singletonList(entry.getValue()))
                .build();
    }
}
