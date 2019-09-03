package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.dto.AccountBalance;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountService {
    AccountBalance balance(Long accountId);

    List<AccountBalance> accountBalances();

    BigDecimal totalBalance();

    List<AccountBalance> balancesGoalByCurrency();
}
