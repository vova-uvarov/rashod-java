package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.dto.AccountBalance;
import com.vuvarov.rashod.model.enums.AccountStatus;
import com.vuvarov.rashod.model.enums.AccountType;
import com.vuvarov.rashod.model.enums.Currency;
import com.vuvarov.rashod.model.enums.OperationType;
import com.vuvarov.rashod.repository.AccountRepository;
import com.vuvarov.rashod.repository.OperationRepository;
import com.vuvarov.rashod.service.interfaces.IAccountService;
import com.vuvarov.rashod.sum.AccountBalanceCalculator;
import com.vuvarov.rashod.sum.AccountsBalanceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final AccountsBalanceCalculator accountsBalanceCalculator;
    private final AccountBalanceCalculator accountBalanceCalculator;
    private final OperationRepository operationRepository;
    private final CategoryService categoryService;

    @Override
    public Account get(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("not found account iwth id: " + id));
    }

    @Override
    public AccountBalance balance(Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        return accountBalanceCalculator.calculate(account.get());
    }

    @Override
    public List<AccountBalance> accountBalances() {
        List<Account> accounts = accountRepository.findAllByIsBalanceAndAccountTypeAndStatus(true, AccountType.SIMPLE, AccountStatus.ACTIVE);
        return accountsBalanceCalculator.calculate(accounts);
    }

    @Override
    public BigDecimal totalBalance() {
        return accountBalances().stream()
                .map(AccountBalance::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<AccountBalance> balancesGoalByCurrency() {
        List<AccountBalance> result = new ArrayList<>();
        List<Account> accounts = accountRepository.findAllByAccountTypeAndStatus(AccountType.GOAL, AccountStatus.ACTIVE);
        Map<Currency, List<Account>> accountsByCurrency = accounts.stream()
//        "%Налог%",["Должны НАМ","Тинькоф Р/C"]
                .filter(account -> !(account.getName().contains("Налог") || account.getName().equals("Должны НАМ") || account.getName().equals("Тинькоф Р/C"))) // todo какой-то жесткий хак. Думаю стоит сделать отдельный тип счета или что-то типа того
                .collect(Collectors.groupingBy(Account::getCurrency));
        for (Map.Entry<Currency, List<Account>> currencyListEntry : accountsByCurrency.entrySet()) {
            AccountBalance accountBalance = new AccountBalance();
            accountBalance.setBalance(currencyListEntry.getValue().stream()
                    .map(accountBalanceCalculator::calculate)
                    .map(AccountBalance::getBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            accountBalance.setAccountName(currencyListEntry.getKey().name());
            result.add(accountBalance);
        }
        return result;
    }

    @Override
    public void equalization(Long id, BigDecimal actualBalance) {
        AccountBalance balance = balance(id);
        BigDecimal difference = actualBalance.subtract(balance.getBalance());
        Operation operation = new Operation();
        operation.setAccount(get(id));
        operation.setCategory(categoryService.findByName("Уравнивание").get(0)); // todo надо в настроках задавать id это категории
        operation.setCost(difference.abs());
        operation.setOperationDate(LocalDateTime.now());
        if (difference.compareTo(BigDecimal.ZERO) > 0) {
            operation.setOperationType(OperationType.INCOME);
        } else {
            operation.setOperationType(OperationType.CONSUMPTION);
        }
        operationRepository.save(operation);
    }

    @Override
    public List<AccountBalance> goalBalances() {
        List<Account> accounts = accountRepository.findAllByAccountTypeAndStatus(AccountType.GOAL, AccountStatus.ACTIVE);
        return accountsBalanceCalculator.calculate(accounts);
    }
}
