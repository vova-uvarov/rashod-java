package com.vuvarov.rashod.web.dto;

import com.vuvarov.rashod.model.Operation;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOperationDto {
    Operation operation;
    Long countRepeat;
}
