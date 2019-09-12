package com.vuvarov.rashod.statistics;

import com.vuvarov.rashod.mapper.OperationFilterMapper;
import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.service.OperationService;
import com.vuvarov.rashod.service.interfaces.IStatisticsService;
import com.vuvarov.rashod.statistics.calculator.AverageByYearTrendCalculator;
import com.vuvarov.rashod.web.dto.statistics.StatisticDataSet;
import com.vuvarov.rashod.web.dto.statistics.Statistics;
import com.vuvarov.rashod.web.dto.statistics.StatisticsFilterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;

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
        List<Operation> operations = operationService.search(filterMapper.from(filter));

        Map<String, BigDecimal> sums = new HashMap<>();
        operations.forEach(op -> {
            String categoryName = op.getCategory().getName();
            BigDecimal sumForCategory = sums.getOrDefault(categoryName, BigDecimal.ZERO);
            sums.put(categoryName, sumForCategory.add(op.getCost()));
        });

        return sums.entrySet().stream()
                .sorted(reverseOrder(comparing(Map.Entry::getValue)))
                .filter(this::isNotNillValue)
                .map(this::buildData)
                .collect(Collectors.toList());
    }

    private StatisticDataSet buildData(Map.Entry<String, BigDecimal> entry) {
        return StatisticDataSet.builder()
                .name(entry.getKey())
                .data(singletonList(entry.getValue()))
                .build();
    }

    private boolean isNotNillValue(Map.Entry<String, BigDecimal> entry) {
        return entry.getValue().compareTo(BigDecimal.ZERO) > 0;
    }
}
