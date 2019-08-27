package com.vuvarov.rashod.sum;

import com.vuvarov.rashod.model.dto.AccountBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountsBalanceCalculator implements ICalculator<List<Long>, List<AccountBalance>> {
    private final AccountBalanceCalculator accountBalanceCalculator;

    @Override
    public List<AccountBalance> calculate(List<Long> accountIds) {
        return accountIds.stream()
                .map(accountBalanceCalculator::calculate)
                .collect(Collectors.toList());
    }
}
