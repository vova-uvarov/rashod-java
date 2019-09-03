package com.vuvarov.rashod.sum;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.enums.OperationType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OperationSumResolver {

    public BigDecimal extractSum(Operation operation, Long accountId) {
        if (operation.getOperationType().equals(OperationType.INCOME)) {
            return operation.getCost();
        }

        if (operation.getOperationType().equals(OperationType.CONSUMPTION)) {
            return operation.getCost().negate(); // Расход или перевод со счета
        }

        if (operation.getOperationType().equals(OperationType.TRANSFER)) {
            if (operation.getAccountToTransferId().equals(accountId)){ // Перевод на этот счет
                return operation.getCurrencyCost();
            }
            return operation.getCurrencyCost().negate();

        }

        return BigDecimal.ZERO;
    }
}
