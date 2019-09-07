package com.vuvarov.rashod.util;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.enums.OperationType;

public class OperationUtil {
    public static boolean isTransfer(Operation operation) {
        return OperationType.TRANSFER.equals(operation.getOperationType());
    }
}
