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
public class AccountBalance {
    Long accountId;
    String accountName;
    BigDecimal balance;
}
