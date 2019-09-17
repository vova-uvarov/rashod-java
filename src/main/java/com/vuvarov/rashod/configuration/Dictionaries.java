package com.vuvarov.rashod.configuration;

import com.vuvarov.rashod.model.enums.AccountStatus;
import com.vuvarov.rashod.model.enums.AccountType;
import com.vuvarov.rashod.model.enums.Currency;
import com.vuvarov.rashod.model.enums.OperationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties("app.param.dictionaries")
public class Dictionaries {
    List<Record<OperationType>> operationTypes;
    List<Record<AccountType>> accountTypes;
    List<Record<AccountStatus>> accountStatuses;
    List<Record<Currency>> currencies;

    @Data
    private static class Record<T> {
        private T key;
        private String value;
    }
}
