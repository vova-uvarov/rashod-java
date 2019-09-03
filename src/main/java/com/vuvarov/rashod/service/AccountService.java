package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.dto.AccountBalance;
import com.vuvarov.rashod.model.enums.AccountType;
import com.vuvarov.rashod.repository.AccountRepository;
import com.vuvarov.rashod.sum.AccountBalanceCalculator;
import com.vuvarov.rashod.sum.AccountsBalanceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final AccountsBalanceCalculator accountsBalanceCalculator;
    private final AccountBalanceCalculator accountBalanceCalculator;

    @Override
    public AccountBalance balance(Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        return accountBalanceCalculator.calculate(account.get());
    }

    @Override
    public List<AccountBalance> accountBalances() {
        return accountsBalanceCalculator.calculate(accountRepository.findAllByIsBalanceTrueAndAccountType(AccountType.SIMPLE));
    }
}
