package com.vuvarov.rashod.web.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthPlanDto {
    private BigDecimal totalPlan;
    private BigDecimal planForDay;
    private BigDecimal currentAverageSum;
    private BigDecimal canSpent;
    private BigDecimal canSpendForPlan;
}
