package com.vuvarov.rashod.mapper;

import com.vuvarov.rashod.model.Operation;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class OperationMapper {

    @Autowired
    ShoppingItemMapper shoppingItemMapper;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingList", ignore = true)
    public abstract Operation copy(Operation operation);

    @AfterMapping
    public void afterCopy(Operation source, Operation target) {
        source.setShoppingList(shoppingItemMapper.copyList(target.getShoppingList()));
    }
}

