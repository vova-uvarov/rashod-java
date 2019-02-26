package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.dto.AccountBalance;

import java.util.List;

public interface IBalanceService {
    AccountBalance calculate(Long accountId);

    List<AccountBalance> calculate();
}
