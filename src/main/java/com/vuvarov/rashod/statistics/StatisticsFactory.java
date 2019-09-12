package com.vuvarov.rashod.statistics;

import com.vuvarov.rashod.web.dto.StatisticDataSet;
import com.vuvarov.rashod.web.dto.Statistics;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.singletonList;

public class StatisticsFactory {
    public static Statistics build(List<String> labels, List<BigDecimal> data, String datasetName) {
        return Statistics.builder()
                .labels(labels)
                .datasets(singletonList(StatisticDataSet.builder()
                        .data(data)
                        .name(datasetName)
                        .build()))
                .build();
    }

    public static Statistics build(List<String> labels, List<StatisticDataSet> datasets) {
        return Statistics.builder()
                .labels(labels)
                .datasets(datasets)
                .build();
    }
}
