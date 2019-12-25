package com.vuvarov.rashod.web.dto.statistics;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticDataSet {
    String code;
    String name;
    List<BigDecimal> data; // todo для pie просто используем одно значение. Подумать над этим
    List<LocalDate> dates;
}

