package com.vuvarov.rashod.sum;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.dto.AccountBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountsBalanceCalculator implements ICalculator<List<Account>, List<AccountBalance>> {
    private final AccountBalanceCalculator accountBalanceCalculator;

    @Override
    public List<AccountBalance> calculate(List<Account> accounts) {
        return accounts.stream()
                .map(accountBalanceCalculator::calculate)
                .collect(Collectors.toList());
    }
}
