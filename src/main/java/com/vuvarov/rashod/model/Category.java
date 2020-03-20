package com.vuvarov.rashod.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;


@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Category extends LongModel {

    private String name;
    private String description;
}
