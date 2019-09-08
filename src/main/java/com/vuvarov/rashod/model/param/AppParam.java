package com.vuvarov.rashod.model.param;

import com.vuvarov.rashod.model.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class AppParam extends Model {
    @Enumerated(EnumType.STRING)
    ParamGroup groupName;
    @Enumerated(EnumType.STRING)
    ParamKey keyName;
    String name;
    String comment;
    String stringValue;
    Long numberValue;
    BigDecimal decimalValue;
    Boolean boolValue;
    Date dateValue;
}
