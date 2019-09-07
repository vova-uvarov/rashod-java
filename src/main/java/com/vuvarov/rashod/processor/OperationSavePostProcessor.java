package com.vuvarov.rashod.processor;

import com.vuvarov.rashod.model.Operation;
import org.springframework.stereotype.Component;

import static com.vuvarov.rashod.util.OperationUtil.isTransfer;

@Component
public class OperationSavePostProcessor implements IProcessor<Operation> {

    @Override
    public void process(Operation operation) {
        if (operation.getCurrencyCost() == null) { // todo нужно понять зачем это делалось в старом приложении
            operation.setCurrencyCost(operation.getCost());
        }

        if (!isTransfer(operation)) {
            operation.setAccountToTransfer(null);
        }

        operation.setAuthor("vuvarov"); // todo hack пока нет пользователей
    }
}
