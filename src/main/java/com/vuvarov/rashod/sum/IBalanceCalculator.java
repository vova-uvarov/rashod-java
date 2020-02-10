package com.vuvarov.rashod.sum;

import com.vuvarov.rashod.model.enums.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

//todo странноватый интерфейс
public interface IBalanceCalculator<I, R> {
    R calculate(I inputData);

    List<R> calculate(List<I> inputData);

    List<R> calculate(Map<Currency, List<I>> accounts);

    BigDecimal totalBalance(List<R> balances);
}
