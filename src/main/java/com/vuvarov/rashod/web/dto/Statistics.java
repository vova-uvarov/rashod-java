package com.vuvarov.rashod.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Statistics {
    List<String> labels;
    List<StatisticItemDto> datasets;
}
