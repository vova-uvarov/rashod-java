package com.vuvarov.rashod.model;

import com.vuvarov.rashod.model.enums.ShoppingItemMeasure;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingItem extends LongModel {
    Long operationId;
    String name;
    @Enumerated(EnumType.STRING)
    ShoppingItemMeasure measure;
    Long measureValue;
}
