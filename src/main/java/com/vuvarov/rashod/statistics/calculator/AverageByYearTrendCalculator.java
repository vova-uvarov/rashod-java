package com.vuvarov.rashod.statistics.calculator;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.statistics.dto.GroupByDateCalculator;
import com.vuvarov.rashod.util.OperationUtil;
import com.vuvarov.rashod.util.StatisticsUtil;
import com.vuvarov.rashod.web.dto.StatisticDataSet;
import com.vuvarov.rashod.web.dto.statistics.StatisticsFilterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vuvarov.rashod.web.dto.statistics.StatisticsGroupBy.YEAR;
import static java.math.RoundingMode.HALF_DOWN;

@Component
@RequiredArgsConstructor
public class AverageByYearTrendCalculator extends AbstractStatisticsCalculator {

    private static final String DATASET_CODE = "MAIN";
    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);
    private final StatisticsUtil statisticsUtil;

    @Override
    protected void calculateInner(Map<String, StatisticDataSet> dataSets, List<String> labels, List<Operation> operations, Pair<LocalDate, LocalDate> interval) {
        LocalDate now = now();
        labels.add(getLabelFormatter().format(interval.getFirst(), YEAR));

        BigDecimal sum = OperationUtil.sum(operations);
        if (interval.getFirst().getYear() == now.getYear()) {
            dataSets.get(DATASET_CODE).getData().add(sum.divide(BigDecimal.valueOf(now.getMonthValue()), HALF_DOWN));
        } else {
            dataSets.get(DATASET_CODE).getData().add(sum.divide(MONTHS_IN_YEAR, HALF_DOWN));
        }
    }

    @Override
    protected GroupByDateCalculator buildDateCalculator(StatisticsFilterDto filter) {
        return new GroupByDateCalculator(getOperationService().minOperationDate(), now(), YEAR);
    }

    @Override
    protected Map<String, StatisticDataSet> initDatasets(StatisticsFilterDto filter) {
        HashMap<String, StatisticDataSet> datasets = new HashMap<>();
        datasets.put(DATASET_CODE, StatisticDataSet.builder()
                .code(DATASET_CODE)
                .name(statisticsUtil.datasetName(filter.getIncludeCategoryIds()))
                .data(new ArrayList<>())
                .build());
        return datasets;
    }
}
