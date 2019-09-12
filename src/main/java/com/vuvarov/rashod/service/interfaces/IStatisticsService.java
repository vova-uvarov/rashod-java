package com.vuvarov.rashod.service.interfaces;

import com.vuvarov.rashod.web.dto.statistics.StatisticDataSet;
import com.vuvarov.rashod.web.dto.statistics.Statistics;
import com.vuvarov.rashod.web.dto.statistics.StatisticsFilterDto;

import java.math.BigDecimal;
import java.util.List;

public interface IStatisticsService {

    Statistics averageByYearTrend(StatisticsFilterDto filter);

    List<StatisticDataSet> sumsByCategory(StatisticsFilterDto filter);
}
