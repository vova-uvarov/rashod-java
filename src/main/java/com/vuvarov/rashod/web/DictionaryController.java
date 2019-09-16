package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.enums.AccountStatus;
import com.vuvarov.rashod.model.enums.AccountType;
import com.vuvarov.rashod.model.enums.Currency;
import com.vuvarov.rashod.model.enums.OperationType;
import com.vuvarov.rashod.service.interfaces.IAccountService;
import com.vuvarov.rashod.service.interfaces.IOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    private final IOperationService operationService;

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

    @GetMapping("/years")
    List<Integer> years() {
        return IntStream.rangeClosed(operationService.minOperationDate().getYear(), LocalDate.now().getYear())
                .boxed()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }
}
