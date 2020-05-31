package com.vuvarov.rashod.mapper;

import com.vuvarov.rashod.model.ShoppingItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ShoppingItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "operationId", ignore = true)
    ShoppingItem copy(ShoppingItem item);

    List<ShoppingItem> copyList(List<ShoppingItem> source);
}

