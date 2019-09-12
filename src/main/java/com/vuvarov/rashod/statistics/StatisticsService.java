package com.vuvarov.rashod.statistics;

import com.vuvarov.rashod.service.interfaces.IStatisticsService;
import com.vuvarov.rashod.statistics.calculator.AverageByYearTrendCalculator;
import com.vuvarov.rashod.web.dto.Statistics;
import com.vuvarov.rashod.web.dto.statistics.StatisticsFilterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService implements IStatisticsService {
    private final AverageByYearTrendCalculator averageByYearTrendCalculator;

    public Statistics averageByYearTrend(StatisticsFilterDto filter) {
        return averageByYearTrendCalculator.calculate(filter);
    }
}
