package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.dto.AccountBalance;
import com.vuvarov.rashod.repository.AccountRepository;
import com.vuvarov.rashod.sum.AccountsBalanceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final AccountsBalanceCalculator accountsBalanceCalculator;

    @Override
    public List<AccountBalance> accountBalances() {
        return accountsBalanceCalculator.calculate(accountRepository.findAllIds());
    }
}
