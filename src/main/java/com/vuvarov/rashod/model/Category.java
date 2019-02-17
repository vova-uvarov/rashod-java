package com.vuvarov.rashod.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Category extends Model {

    private String name;
    private String description;
}
