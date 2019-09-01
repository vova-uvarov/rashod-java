package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.dto.AccountBalance;
import com.vuvarov.rashod.repository.AccountRepository;
import com.vuvarov.rashod.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin
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

    @GetMapping("/{id}/balances")
    public List<AccountBalance> balance() {
        return accountService.accountBalances();
    }
}
