package com.vuvarov.rashod.model;

import com.vuvarov.rashod.model.enums.OperationType;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

//todo возможно стоит сделать наследование (хотя и хранить в одной таблице)
@Data
@Entity
public class Operation extends Model {

    Long parentId;
    Long accountId;

    // todo Счет куда переводятся деньги, используется только в операциях типа перевод. Наверное нужно наследование
    Long accountToTransferId;

    @OneToOne
    @JoinColumn(name = "categoryId")
    Category category;

    /**
     * Список покупок через запятую в старом приложении это поле description
     */
    String shoppingList;
    /**
     * Просто комментарий в свободной форме
     */
    String comment;

    /**
     * Список тегов через запятую
     */
    String tags;

    BigDecimal cost;

    /**
     * Сумма в валюте зачисления. Используется только в переводах
     */
    BigDecimal currencyCost;

    boolean plan;

    String place;
    String author; // todo это будет пользователь

    @Enumerated(EnumType.STRING)
    OperationType operationType;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime operationDate;
}
