package com.vuvarov.rashod.statistics.calculator;

import com.vuvarov.rashod.mapper.OperationFilterMapper;
import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.service.interfaces.IOperationService;
import com.vuvarov.rashod.statistics.LabelFormatter;
import com.vuvarov.rashod.statistics.StatisticsFactory;
import com.vuvarov.rashod.statistics.dto.GroupByDateCalculator;
import com.vuvarov.rashod.util.StatisticsUtil;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import com.vuvarov.rashod.web.dto.statistics.StatisticDataSet;
import com.vuvarov.rashod.web.dto.statistics.Statistics;
import com.vuvarov.rashod.web.dto.statistics.StatisticsFilterDto;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractStatisticsCalculator implements IStatisticsCalculator {
    @Getter
    @Autowired
    private IOperationService operationService;
    @Autowired
    private StatisticsUtil statisticsUtil;
    @Getter
    @Autowired
    private LabelFormatter labelFormatter;
    @Autowired
    private OperationFilterMapper filterMapper;

    @Override
    public Statistics calculate(StatisticsFilterDto filter) {
        GroupByDateCalculator calculator = buildDateCalculator(filter);

        Pair<LocalDate, LocalDate> interval = calculator.nextDate();

        Map<String, StatisticDataSet> dataSets = initDatasets(filter);
        List<String> labels = new ArrayList<>();
        OperationFilterDto operationFilter = filterMapper.from(filter);
        while (interval != null) {
            operationFilter.setDateFrom(interval.getFirst());
            operationFilter.setDateTo(interval.getSecond());
            List<Operation> operations = operationService.search(operationFilter);

            calculateInner(dataSets, labels, operations, interval);
            interval = calculator.nextDate();
        }

        return StatisticsFactory.build(labels, new ArrayList<>(dataSets.values()));
    }

    protected abstract void calculateInner(Map<String, StatisticDataSet> dataSets, List<String> labels, List<Operation> operations, Pair<LocalDate, LocalDate> interval);

    protected abstract GroupByDateCalculator buildDateCalculator(StatisticsFilterDto filter);

    protected abstract Map<String, StatisticDataSet> initDatasets(StatisticsFilterDto filter);

    public LocalDate now() {
        return LocalDate.now();
    }
}
