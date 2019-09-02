package com.vuvarov.rashod.model.dto.migration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OperationDto {

    Long id;
    String author;
    String category;
    String comment;
    BigDecimal cost;
    LocalDateTime creationDate;
    String description;
    LocalDateTime operationDate;
    String place;
    String operationType;
    String accountName;
    String accountToTransferName;
    Boolean isPlan;
    LocalDateTime lastUpdated;
    String tags;
    Long parentId;
    BigDecimal currencyCost;

}
