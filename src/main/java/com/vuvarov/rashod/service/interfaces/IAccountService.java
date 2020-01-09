package com.vuvarov.rashod.service.interfaces;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.enums.AccountStatus;
import com.vuvarov.rashod.model.enums.AccountType;
import com.vuvarov.rashod.model.enums.Currency;

import java.util.List;
import java.util.Map;

public interface IAccountService {
    Account get(Long id);

    List<Account> accountsForBalances();

    Map<Currency, List<Account>> goalAccountsByCurrency();

    List<Account> accounts(AccountType accountType, AccountStatus status);
}
