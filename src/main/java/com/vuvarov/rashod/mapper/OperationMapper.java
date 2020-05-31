package com.vuvarov.rashod.mapper;

import com.vuvarov.rashod.model.Operation;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class OperationMapper {

    @Autowired
    ShoppingItemMapper shoppingItemMapper; // todo Mapper.uses instead of !Autowired

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingList", ignore = true)
    public abstract Operation copy(Operation operation);

    @AfterMapping
    public void afterCopy(Operation source, @MappingTarget Operation target) {
        target.setShoppingList(shoppingItemMapper.copyList(source.getShoppingList()));
    }
}

