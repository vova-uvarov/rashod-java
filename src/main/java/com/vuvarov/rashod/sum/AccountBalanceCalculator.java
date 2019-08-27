package com.vuvarov.rashod.sum;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.dto.AccountBalance;
import com.vuvarov.rashod.service.IOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountBalanceCalculator implements ICalculator<Long, AccountBalance> {
    private final IOperationService operationService;
    private final OperationSumResolver sumResolver;

    @Override
    public AccountBalance calculate(Long accountId) {
        List<Operation> operations = operationService.findAllOperations(accountId, false);
        BigDecimal result = operations.stream().
                filter(operation -> !operation.isPlan())
                .map(sumResolver::extractSum)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return new AccountBalance(accountId, result);
    }
}
