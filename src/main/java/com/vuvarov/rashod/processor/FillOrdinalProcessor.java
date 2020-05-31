package com.vuvarov.rashod.processor;

import com.vuvarov.rashod.model.Operation;
import org.springframework.stereotype.Component;

@Component
public class FillOrdinalProcessor implements IProcessor<Operation> {

    @Override
    public void process(Operation operation) {
        if (operation.getOrdinal() == null) {
            operation.setOrdinal(1);
        }
    }
}
