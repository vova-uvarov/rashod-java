package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.dto.AccountBalance;
import com.vuvarov.rashod.repository.AccountRepository;
import com.vuvarov.rashod.service.interfaces.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.JsonPath;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController extends RestRepositoryController<Account, Long, AccountRepository> {

    private final IAccountService accountService;

    @GetMapping("/search")
    public Page<Account> search(@RequestParam String q, @PageableDefault Pageable pageable) {
        return repository.findAllByName("%" + q + "%", pageable);
    }

    @GetMapping("/{id}/balance")
    public AccountBalance balance(@PathVariable Long accountId) {
        return accountService.balance(accountId);
    }

    @GetMapping("/balances/goal/byCurrency")
    public List<AccountBalance> balancesGoalByCurrency() {
        return accountService.balancesGoalByCurrency();
    }

    @GetMapping("/balances/goal")
    public List<AccountBalance> goalBalances() {
        return accountService.goalBalances();
    }

    @GetMapping("/balances")
    public List<AccountBalance> balance() {
        return accountService.accountBalances();
    }

    @GetMapping("/totalBalance")
    public BigDecimal totalBalance() {
        return accountService.totalBalance();
    }

    @PatchMapping("{id}/equalization")
    public void equalization(@PathVariable Long id, @RequestBody Map<String, BigDecimal> params) {
        accountService.equalization(id, params.get("actualBalance")); // todo
    }

    @Override
    public Page<Account> findAll(@PageableDefault(sort = "name") Pageable pageable) {
        return super.findAll(pageable);
    }
}
