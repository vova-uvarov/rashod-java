package com.vuvarov.rashod.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vuvarov.rashod.model.enums.AccountType;
import com.vuvarov.rashod.model.enums.Currency;
import com.vuvarov.rashod.model.enums.OperationType;
import com.vuvarov.rashod.util.LocalDate2LocalDateTimeDeserializer;
import com.vuvarov.rashod.util.LocalDateTime2DateSerializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OperationFilterDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dateFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dateTo;

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
