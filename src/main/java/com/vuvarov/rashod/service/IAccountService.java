package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.dto.AccountBalance;

import java.util.List;

public interface IAccountService {
    AccountBalance balance(Long accountId);

    List<AccountBalance> accountBalances();
}
