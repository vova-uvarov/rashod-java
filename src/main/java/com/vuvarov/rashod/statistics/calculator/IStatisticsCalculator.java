package com.vuvarov.rashod.statistics.calculator;

import com.vuvarov.rashod.web.dto.Statistics;
import com.vuvarov.rashod.web.dto.statistics.StatisticsFilterDto;

public interface IStatisticsCalculator {
    Statistics calculate(StatisticsFilterDto filter);
}
