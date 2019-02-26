package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController extends RestRepositoryController<Account, Long, AccountRepository> {

    @GetMapping("/search")
    public Page<Account> search(@RequestParam String q, @PageableDefault Pageable pageable) {
        return repository.findAllByName("%" + q + "%", pageable);
    }
}
