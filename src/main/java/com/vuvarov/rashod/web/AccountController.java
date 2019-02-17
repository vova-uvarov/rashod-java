package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController extends RestRepositoryController<Account, Long, AccountRepository> {

    @GetMapping("/search")
    public List<Account> search(@RequestParam String q) {
        return repository.findAllByName("%" + q + "%");
    }
}
