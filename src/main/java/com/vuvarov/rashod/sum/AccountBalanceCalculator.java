package com.vuvarov.rashod.sum;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.dto.AccountBalance;
import com.vuvarov.rashod.service.interfaces.IOperationService;
import com.vuvarov.rashod.web.dto.OperationFilterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountBalanceCalculator implements ICalculator<Account, AccountBalance> {
    @Autowired
    private  IOperationService operationService; // todo циклическая зависимость

    private final OperationSumResolver sumResolver;

    @Override
    public AccountBalance calculate(Account account) {
        OperationFilterDto filterDto = new OperationFilterDto();
        filterDto.setAccountIds(Collections.singletonList(account.getId()));
        filterDto.setIsPlan(false);

        List<Operation> operations = operationService.search(filterDto, Pageable.unpaged()).getContent();
        BigDecimal result = operations.stream().
                filter(operation -> !operation.isPlan())
                .map(op -> sumResolver.extractSum(op, account.getId()))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return AccountBalance.builder()
                .accountId(account.getId())
                .accountName(account.getName())
                .color(account.getColor())
                .balance(result)
                .build();
    }
}
