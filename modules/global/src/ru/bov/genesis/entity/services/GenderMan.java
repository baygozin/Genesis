package ru.bov.genesis.entity.services;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum GenderMan implements EnumClass<String> {

    Male("1"),
    Female("2");

    private String id;

    GenderMan(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static GenderMan fromId(String id) {
        for (GenderMan at : GenderMan.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}