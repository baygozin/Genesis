package ru.bov.genesis.entity.services;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum StatusEmployeeEnum implements EnumClass<String> {

    Work("1"),
    Pause("2"),
    Free("3");

    private String id;

    StatusEmployeeEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static StatusEmployeeEnum fromId(String id) {
        for (StatusEmployeeEnum at : StatusEmployeeEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}