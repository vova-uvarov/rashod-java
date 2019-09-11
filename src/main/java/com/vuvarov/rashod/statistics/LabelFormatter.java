package com.vuvarov.rashod.statistics;

import com.vuvarov.rashod.web.dto.statistics.StatisticsGroupBy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class LabelFormatter {
    private static final DateTimeFormatter DF_MONTH = DateTimeFormatter.ofPattern("yyyy-MMM");
    private static final DateTimeFormatter DF_YEAR = DateTimeFormatter.ofPattern("yyyy");
    private static final DateTimeFormatter DF_DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String format(LocalDate date, StatisticsGroupBy groupBy) {
        if (StatisticsGroupBy.YEAR.equals(groupBy)) {
            return DF_YEAR.format(date);
        }

        if (StatisticsGroupBy.DAY.equals(groupBy)) {
            return DF_DAY.format(date);
        }

        return DF_MONTH.format(date);
    }
}
