package com.vuvarov.rashod.sum;

import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.dto.AccountBalance;
import com.vuvarov.rashod.model.enums.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ICalculator<I, R> { // todo подумать над названием интерефейса и возможно просто использовать Function
    R calculate(I inputData);

    List<R> calculate(List<I> inputData);

    List<R> calculate(Map<Currency, List<I>> accounts);

    BigDecimal totalBalance(List<R> balances);
}
