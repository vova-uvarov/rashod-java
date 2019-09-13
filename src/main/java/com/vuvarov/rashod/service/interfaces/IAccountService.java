package com.vuvarov.rashod.service.interfaces;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.dto.AccountBalance;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountService {
    Account get(Long id);

    AccountBalance balance(Long accountId);

    List<AccountBalance> accountBalances();

    BigDecimal totalBalance();

    List<AccountBalance> balancesGoalByCurrency();

    void equalization(Long id, BigDecimal actualBalance);
}
