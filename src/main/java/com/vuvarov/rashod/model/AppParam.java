package com.vuvarov.rashod.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
public class AppParam  extends Model{
    String group;
    String name;
    String comment;
    String charValue;
    Long numberValue;
    BigDecimal decimalValue;
    Boolean boolValue;
    Date dateValue;
}
