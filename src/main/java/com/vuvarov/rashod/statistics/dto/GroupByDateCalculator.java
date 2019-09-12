package com.vuvarov.rashod.statistics.dto;

import com.vuvarov.rashod.web.dto.statistics.StatisticsGroupBy;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class GroupByDateCalculator {
    private LocalDate firstDate;
    private LocalDate endDate;
    private LocalDate currentDate;
    private StatisticsGroupBy groupBy;

    public GroupByDateCalculator(LocalDate fromDate, LocalDate toDate, StatisticsGroupBy groupBy) {
        this.groupBy = groupBy;
        firstDate = firstDate(fromDate);
        endDate = endDate(toDate);
    }


    public Pair<LocalDate, LocalDate> nextDate() {
        if (currentDate == null) {
            currentDate = firstDate;
            return Pair.of(currentDate, endDate(currentDate));
        }
        if (currentDate.isAfter(endDate)) {
            return null;
        }

        if (StatisticsGroupBy.DAY.equals(groupBy)) {
            currentDate = currentDate.plusDays(1);
        } else if (StatisticsGroupBy.YEAR.equals(groupBy)) {
            currentDate = currentDate.plusYears(1);
        } else {
            currentDate = currentDate.plusMonths(1);
        }
        return Pair.of(currentDate, endDate(currentDate));
    }

    private LocalDate firstDate(LocalDate date) {
        if (StatisticsGroupBy.DAY.equals(groupBy)) {
            return date;
        }

        if (StatisticsGroupBy.YEAR.equals(groupBy)) {
            return date.withDayOfYear(1);
        }
        return date.withDayOfMonth(1);
    }

    private LocalDate endDate(LocalDate date) {
        if (StatisticsGroupBy.DAY.equals(groupBy)) {
            return date;
        }

        if (StatisticsGroupBy.YEAR.equals(groupBy)) {
            return date.with(TemporalAdjusters.lastDayOfYear());
        }
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }


}
