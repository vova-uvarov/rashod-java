package com.vuvarov.rashod.sum;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.enums.OperationType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OperationSumResolver {

    public BigDecimal extractSum(Operation operation, Long accountId) {

        if (operation.isPlan()) {
            return BigDecimal.ZERO;
        }
        if (operation.getOperationType().equals(OperationType.INCOME)) {
            return operation.getCost();
        }

        if (operation.getOperationType().equals(OperationType.CONSUMPTION)) {
            return operation.getCost().negate(); // Расход
        }

        if (operation.getOperationType().equals(OperationType.TRANSFER)) {
            if (operation.getAccountToTransfer().getId().equals(accountId)) { // Перевод на этот счет
                return operation.getCurrencyCost();
            }
            return operation.getCost().negate();
        }


        return BigDecimal.ZERO;
    }
}
