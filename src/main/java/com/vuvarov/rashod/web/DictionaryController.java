package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.dto.AccountBalance;
import com.vuvarov.rashod.model.enums.OperationType;
import com.vuvarov.rashod.repository.AccountRepository;
import com.vuvarov.rashod.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/dictionary")
@RequiredArgsConstructor
public class DictionaryController  {

    private final IAccountService accountService;

    @GetMapping("/operationTypes")
    List<OperationType> operationTypes(){
        return Arrays.asList(OperationType.values());
    }
}
