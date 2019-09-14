package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.enums.AccountStatus;
import com.vuvarov.rashod.model.enums.AccountType;
import com.vuvarov.rashod.model.enums.Currency;
import com.vuvarov.rashod.model.enums.OperationType;
import com.vuvarov.rashod.service.interfaces.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    private final IAccountService accountService;

    @GetMapping("/operationTypes")
    List<OperationType> operationTypes() {
        return Arrays.asList(OperationType.values());
    }

    @GetMapping("/accountTypes")
    List<AccountType> accountTypes() {
        return Arrays.asList(AccountType.values());
    }

    @GetMapping("/currencies")
    List<Currency> currencies() {
        return Arrays.asList(Currency.values());
    }

    @GetMapping("/accountStatuses")
    List<AccountStatus> accountStatuses() {
        return Arrays.asList(AccountStatus.values());
    }
}
