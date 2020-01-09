package com.vuvarov.rashod.processor;

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

    //    todo наверное стоит разделить на несколько процессоров
    @Override
    public void process(Operation operation) {

        if (isTransfer(operation)) {
            operation.setCategory(categoryService.findByName("Перевод")); // todo похоже на хак
        }
        if (!isTransfer(operation)) {
            operation.setAccountToTransfer(null);
        }

        operation.setInputCost(operation.getCost());

        if (OperationUtil.isConsumption(operation)) {
            if (operation.getId() == null && accountService.get(operation.getAccount().getId()).isRound()) {
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

        if (operation.getCurrencyCost() == null) { // todo нужно понять зачем это делалось в старом приложении
            operation.setCurrencyCost(operation.getCost());
        }

        operation.setAuthor("vuvarov"); // todo hack пока нет пользователей
    }
}
