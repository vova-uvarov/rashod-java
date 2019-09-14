package com.vuvarov.rashod.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//todo возможно стоит внутри просто передавать весь account
public class AccountBalance {
    Long accountId;
    String accountName;
    String color;
    BigDecimal balance;
    BigDecimal goalCost;
}
