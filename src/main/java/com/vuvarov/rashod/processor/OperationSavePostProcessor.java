package com.vuvarov.rashod.processor;

import com.vuvarov.rashod.configuration.OperationProperties;
import com.vuvarov.rashod.model.Account;
import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.service.AccountService;
import com.vuvarov.rashod.service.CategoryService;
import com.vuvarov.rashod.util.OperationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.vuvarov.rashod.util.OperationUtil.isTransfer;

@Component
@RequiredArgsConstructor
public class OperationSavePostProcessor implements IProcessor<Operation> {

    private final AccountService accountService;
    private final CategoryService categoryService;
    private final OperationProperties operationProperties;

    @Override
    public void process(Operation operation) {

        if (operation.getCurrencyCost() == null) {
            operation.setCurrencyCost(operation.getCost());
        }

        if (isTransfer(operation)) {
            operation.setCategory(categoryService.findByName(operationProperties.getTransferCategory()));
        } else {
            operation.setAccountToTransfer(null);
        }

        operation.setInputCost(operation.getCost());

        if (OperationUtil.isConsumption(operation)) {
            Account account = accountService.get(operation.getAccount().getId());
            if (operation.getId() == null && account.isRound()) {
                BigDecimal cost = operation.getCost();
                operation.setCost(cost);
                BigDecimal tenBD = BigDecimal.valueOf(10);
                BigDecimal remainder = cost.remainder(tenBD);

                if (remainder.intValue() != 0) {
                    operation.setInputCost(cost);
                    operation.setCost(cost.add(tenBD.subtract(remainder)));
                }
            }
        }

        operation.setAuthor(operationProperties.getAuthor());
    }
}
