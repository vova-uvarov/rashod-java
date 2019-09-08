package com.vuvarov.rashod.model.param;

import lombok.Getter;

public enum ParamKey {
    SUM_TO_MONTH("План на месяц");

    @Getter
    private final String name;

    ParamKey(String s) {
        this.name = s;
    }
}
