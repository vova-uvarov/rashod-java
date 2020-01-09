package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.enums.AccountStatus;
import com.vuvarov.rashod.model.enums.AccountType;
import com.vuvarov.rashod.model.enums.Currency;
import com.vuvarov.rashod.repository.AccountRepository;
import com.vuvarov.rashod.service.interfaces.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;

    @Override
    public Account get(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("not found account iwth id: " + id));
    }

    @Override
    public List<Account> accountsForBalances() {
        return accountRepository.findAllByIsBalanceAndAccountTypeAndStatus(true, AccountType.SIMPLE, AccountStatus.ACTIVE);
    }

    @Override
    public Map<Currency, List<Account>> goalAccountsByCurrency() {
        return accounts(AccountType.GOAL, AccountStatus.ACTIVE).stream()
                .filter(account -> !(account.getName().contains("Налог") || account.getName().equals("Должны НАМ") || account.getName().equals("Тинькоф Р/C"))) // todo какой-то жесткий хак. Думаю стоит сделать отдельный тип счета или что-то типа того
                .collect(Collectors.groupingBy(Account::getCurrency));
    }

    @Override
    public List<Account> accounts(AccountType accountType, AccountStatus status) {
        return accountRepository.findAllByAccountTypeAndStatus(accountType, status);
    }
}
