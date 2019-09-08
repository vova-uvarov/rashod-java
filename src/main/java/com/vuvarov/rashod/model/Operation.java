package com.vuvarov.rashod.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vuvarov.rashod.model.enums.OperationType;
import com.vuvarov.rashod.util.LocalDate2LocalDateTimeDeserializer;
import com.vuvarov.rashod.util.LocalDateTime2DateSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//todo возможно стоит сделать наследование (хотя и хранить в одной таблице)
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
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

    String place; // todo думаю стоит сделать отдельной сущностью
    String author; // todo это будет пользователь

    @Enumerated(EnumType.STRING)
    OperationType operationType;

    @Column(columnDefinition = "TIMESTAMP")
    @JsonSerialize(using = LocalDateTime2DateSerializer.class)
    @JsonDeserialize(using = LocalDate2LocalDateTimeDeserializer.class)
    LocalDateTime operationDate;
}
