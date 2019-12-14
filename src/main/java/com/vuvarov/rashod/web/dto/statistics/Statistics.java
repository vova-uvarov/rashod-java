package com.vuvarov.rashod.web.dto.statistics;

import com.vuvarov.rashod.web.dto.statistics.StatisticDataSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Statistics {
    List<LocalDate> dates;
    List<String> labels;
    List<StatisticDataSet> datasets;
}
