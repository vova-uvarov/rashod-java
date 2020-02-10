package com.vuvarov.rashod.sum;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.dto.AccountBalance;
import com.vuvarov.rashod.model.enums.Currency;
import com.vuvarov.rashod.service.interfaces.IOperationService;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountBalanceCalculator implements IBalanceCalculator<Account, AccountBalance> {
    private final IOperationService operationService;
    private final OperationSumResolver sumResolver;

    @Override
    public AccountBalance calculate(Account account) {
        OperationFilterDto filterDto = new OperationFilterDto();
        filterDto.setAccountIds(Collections.singletonList(account.getId()));
        filterDto.setIsPlan(false);

        List<Operation> operations = operationService.search(filterDto, Pageable.unpaged()).getContent();
        BigDecimal result = operations.stream().
                filter(operation -> !operation.isPlan())
                .map(op -> sumResolver.extractSum(op, account.getId()))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return AccountBalance.builder()
                .accountId(account.getId())
                .accountName(account.getName())
                .goalCost(account.getTargetCost())
                .color(account.getColor())
                .balance(result)
                .build();
    }

    @Override
    public List<AccountBalance> calculate(List<Account> accounts) {
        return accounts.stream()
                .map(this::calculate)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountBalance> calculate(Map<Currency, List<Account>> accounts) {
        return accounts.entrySet().stream().map(entry -> {
            AccountBalance accountBalance = new AccountBalance();
            accountBalance.setBalance(entry.getValue().stream()
                    .map(this::calculate)
                    .map(AccountBalance::getBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            accountBalance.setAccountName(entry.getKey().name());
            return accountBalance;
        }).collect(Collectors.toList());
    }

    @Override
    public BigDecimal totalBalance(List<AccountBalance> balances) {
        return balances.stream()
                .map(AccountBalance::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
