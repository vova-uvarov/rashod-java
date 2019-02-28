package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.dto.AccountBalance;
import com.vuvarov.rashod.model.enums.OperationType;
import com.vuvarov.rashod.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class BalanceService implements IBalanceService {
    private final AccountRepository accountRepository;
    private final OperationService operationService;

    @Override
    public AccountBalance calculate(Long accountId) {
        List<Operation> operations = operationService.findAllOperations(accountId, false);
        BigDecimal result = operations.stream().
                filter(operation -> !operation.isPlan())
                .map(this::computeOperationSum)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return new AccountBalance(accountId, result);
    }

    @Override
    public List<AccountBalance> calculate() {
        Iterable<Account> accounts = accountRepository.findAll();
        return StreamSupport.stream(accounts.spliterator(), false)
                .map(account -> calculate(account.getId()))
                .collect(Collectors.toList());
    }

    private BigDecimal computeOperationSum(Operation operation) {
        if (operation.getOperationType().equals(OperationType.INCOME)) {
            return operation.getCost();
        }

        if (operation.getOperationType().equals(OperationType.CONSMPTION)) {
            return operation.getCost().negate(); // Расход или перевод со счета
        }

        if (operation.getOperationType().equals(OperationType.TRANSFER)) { // Перевод на этот счет
            return operation.getCurrencyCost();
        }
        return BigDecimal.ZERO;
    }
}
