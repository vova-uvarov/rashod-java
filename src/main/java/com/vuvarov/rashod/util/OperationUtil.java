package com.vuvarov.rashod.util;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.enums.OperationType;

import java.math.BigDecimal;
import java.util.Collection;

public class OperationUtil {
    public static boolean isTransfer(Operation operation) {
        return OperationType.TRANSFER.equals(operation.getOperationType());
    }

    public static boolean isConsumption(Operation operation) {
        return OperationType.CONSUMPTION.equals(operation.getOperationType());
    }

    public static BigDecimal sum(Collection<Operation> operations) {
        return operations.stream()
                .map(Operation::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
