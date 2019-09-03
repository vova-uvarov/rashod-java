package com.vuvarov.rashod.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vuvarov.rashod.model.enums.OperationType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//todo возможно стоит сделать наследование (хотя и хранить в одной таблице)
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Operation extends Model {

    Long parentId;

    @OneToOne
    @JoinColumn(name = "accountId")
    Account account;

    @OneToOne
    @JoinColumn(name = "accountToTransferId")
    Account accountToTransfer;

    @OneToOne
    @JoinColumn(name = "categoryId")
    Category category;

    @OneToMany
    @JoinColumn(name = "operationId", updatable = false, insertable = false)
    List<ShoppingItem> shoppingList;
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
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDateTime operationDate;
}
