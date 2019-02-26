package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.dto.AccountBalance;
import com.vuvarov.rashod.model.enums.OperationType;
import com.vuvarov.rashod.repository.AccountRepository;
import com.vuvarov.rashod.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class BalanceService implements IBalanceService {
    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    @Override
    public AccountBalance calculate(Long accountId) {
        List<Operation> operations = operationRepository.findAllByAccountIdOrAccountToTransferIdAndPlanFalse(accountId, accountId);
        BigDecimal result =
                operations.stream().
                        filter(operation -> !operation.isPlan())
                        .map(operation -> {
                            if (Objects.equals(operation.getAccountId(), accountId)) {
                                if (operation.getOperationType().equals(OperationType.INCOME)) {
                                    return operation.getCost();
                                }
                                return operation.getCost().negate(); // Расход или перевед со счета
                            }

                            if (Objects.equals(operation.getAccountToTransferId(), accountId)) { // Перевод на этот счет
                                return operation.getCurrencyCost();
                            }
                            return BigDecimal.ZERO;
                        })
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
}
