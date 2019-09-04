package com.vuvarov.rashod.web.dto;

import com.vuvarov.rashod.model.enums.AccountType;
import com.vuvarov.rashod.model.enums.Currency;
import com.vuvarov.rashod.model.enums.OperationType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OperationFilterDto {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime dateFrom;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime dateTo;
    BigDecimal costFrom;
    BigDecimal costTo;
    List<OperationType> operationTypes;
    List<AccountType> accountTypes;
    List<Long> categoryIds;
    Long accountId;
    String tag;
    Boolean isPlan;
    String place;
    Currency currency;
}
