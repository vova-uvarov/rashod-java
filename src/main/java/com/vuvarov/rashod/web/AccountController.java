package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.dto.AccountBalance;
import com.vuvarov.rashod.model.enums.AccountStatus;
import com.vuvarov.rashod.model.enums.AccountType;
import com.vuvarov.rashod.repository.AccountRepository;
import com.vuvarov.rashod.service.interfaces.IAccountService;
import com.vuvarov.rashod.service.interfaces.IOperationService;
import com.vuvarov.rashod.sum.IBalanceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController extends RestRepositoryController<Account, Long, AccountRepository> {

    private static final String ACTUAL_BALANCE_FIELD = "actualBalance";

    private final IAccountService accountService;
    private final IOperationService operationService;
    private final IBalanceCalculator<Account, AccountBalance> balanceCalculator;

    @GetMapping("/search")
    public Page<Account> search(@RequestParam String q, @PageableDefault Pageable pageable) {
        return repository.findAllByName("%" + q + "%", pageable);
    }

    @GetMapping("/{accountId}/balance")
    public AccountBalance balance(@PathVariable Long accountId) {
        return balanceCalculator.calculate(accountService.get(accountId));
    }

    @GetMapping("/balances/goal/byCurrency")
    public List<AccountBalance> balancesGoalByCurrency() {
        return balanceCalculator.calculate(accountService.goalAccountsByCurrency());
    }

    @GetMapping("/balances/goal")
    public List<AccountBalance> goalBalances() {
        return balanceCalculator.calculate(accountService.accounts(AccountType.GOAL, AccountStatus.ACTIVE));
    }

    @GetMapping("/balances")
    public List<AccountBalance> balance() {
        return balanceCalculator.calculate(accountService.accountsForBalances());
    }

    @GetMapping("/balances/debt/{status}")
    public List<AccountBalance> debts(@PathVariable AccountStatus status) {
        return balanceCalculator.calculate(accountService.accounts(AccountType.DEBT, status));
    }

    @GetMapping("/totalBalance")
    public BigDecimal totalBalance() {
        return balanceCalculator.totalBalance(
                balanceCalculator.calculate(accountService.accountsForBalances())
        );
    }

    @PatchMapping("{id}/equalization")
    public void equalization(@PathVariable Long id, @RequestBody Map<String, BigDecimal> params) {
        Account account = accountService.get(id);
        operationService.equalization(account, params.get(ACTUAL_BALANCE_FIELD), balanceCalculator.calculate(account).getBalance());
    }

    @Override
    public Page<Account> findAll(@PageableDefault(sort = "name") Pageable pageable) {
        return super.findAll(pageable);
    }
}
