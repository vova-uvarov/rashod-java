package com.vuvarov.rashod.model.factory;

import com.vuvarov.rashod.model.Operation;
import com.vuvarov.rashod.model.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

public class OperationFactory {

    //    todo попробывать сделать через mapper например mapstruct
    public static Operation copy(Operation source) {
        Operation result = new Operation();
        result.setParentId(source.getParentId());
        result.setAccount(source.getAccount());
        result.setAccountToTransfer(source.getAccountToTransfer());
        result.setCategory(source.getCategory());
        result.setComment(source.getComment());
        result.setTags(source.getTags());
        result.setCost(source.getCost());
        result.setCurrencyCost(source.getCurrencyCost());
        result.setPlan(source.isPlan());
        result.setPlace(source.getPlace());
        result.setAuthor(source.getAuthor());
        result.setOperationType(source.getOperationType());
        result.setOperationDate(source.getOperationDate());

        List<ShoppingItem> shoppingList = source.getShoppingList();
        List<ShoppingItem> resultShoppingList = new ArrayList<>();
        if (shoppingList != null) {
            for (ShoppingItem shoppingItem : shoppingList) {
                ShoppingItem resultShoppingItem = new ShoppingItem();
                resultShoppingItem.setName(shoppingItem.getName());
                resultShoppingItem.setMeasure(shoppingItem.getMeasure());
                resultShoppingItem.setMeasureValue(shoppingItem.getMeasureValue());
                resultShoppingList.add(resultShoppingItem);
            }
            result.setShoppingList(resultShoppingList);
        }
        return result;
    }
}
