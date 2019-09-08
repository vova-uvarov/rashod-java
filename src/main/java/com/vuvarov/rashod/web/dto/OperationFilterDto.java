package com.vuvarov.rashod.web.dto;

import com.vuvarov.rashod.model.ShoppingItem;
import com.vuvarov.rashod.model.enums.AccountType;
import com.vuvarov.rashod.model.enums.Currency;
import com.vuvarov.rashod.model.enums.OperationType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    List<String> shoppingList;
    List<Long> categoryIds;
    List<Long> accountIds;
    String tag;
    Boolean isPlan;
    String place;
    String comment;
    Currency currency;
}
