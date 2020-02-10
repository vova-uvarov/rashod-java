package com.vuvarov.rashod.statistics;

import com.vuvarov.rashod.web.dto.statistics.StatisticDataSet;
import com.vuvarov.rashod.web.dto.statistics.Statistics;

import java.util.List;

public class StatisticsFactory {

    public static Statistics build(List<String> labels, List<StatisticDataSet> datasets) {
        return Statistics.builder()
                .labels(labels)
                .datasets(datasets)
                .build();
    }
}
