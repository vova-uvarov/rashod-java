package com.vuvarov.rashod.web.dto;

import com.vuvarov.rashod.model.enums.AccountType;
import com.vuvarov.rashod.model.enums.Currency;
import com.vuvarov.rashod.model.enums.OperationType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OperationFilterDto {
    LocalDateTime dateFrom;
    LocalDateTime dateTo;
    BigDecimal sumFrom;
    BigDecimal sumTo;
    List<OperationType> operationTypes;
    List<AccountType> accountTypes;
    List<Long> categoryIds;
    List<String> tags;
    Boolean isPlan;
    String place;
    Currency currency;
}
