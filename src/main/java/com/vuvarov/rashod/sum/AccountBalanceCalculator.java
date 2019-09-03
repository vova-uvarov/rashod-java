package com.vuvarov.rashod.sum;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.dto.AccountBalance;
import com.vuvarov.rashod.service.IOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountBalanceCalculator implements ICalculator<Account, AccountBalance> {
    private final IOperationService operationService;
    private final OperationSumResolver sumResolver;

    @Override
    public AccountBalance calculate(Account account) {
        List<Operation> operations = operationService.findAllOperations(account.getId(), false);
        BigDecimal result = operations.stream().
                filter(operation -> !operation.isPlan())
                .map(op->sumResolver.extractSum(op, account.getId()))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return AccountBalance.builder()
                .accountId(account.getId())
                .accountName(account.getName())
                .balance(result)
                .build();
    }
}
