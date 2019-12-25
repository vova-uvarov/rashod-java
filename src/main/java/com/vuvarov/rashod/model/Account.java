package com.vuvarov.rashod.model;

import com.vuvarov.rashod.model.enums.AccountStatus;
import com.vuvarov.rashod.model.enums.AccountType;
import com.vuvarov.rashod.model.enums.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

//todo возможно стоит разделить наследованием
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Account  extends Model{
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private BigDecimal targetCost;
    //    todo это как буд-то бы не часть аккаунта. Возможно нужна отдельная сущность настройки
    private String color;

    // округлять до 10-ти или нет. Например 103 будет превращено в 110
    private boolean round;

    /**
     * Учитывать или нет в балансе
     */
    boolean isBalance;
    @Enumerated(EnumType.STRING)
    AccountStatus status; // state из приложения gradle
    @Enumerated(EnumType.STRING)
    Currency currency;
}
