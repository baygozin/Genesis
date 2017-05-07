package ru.bov.genesis.entity.services;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum MaritalStatus implements EnumClass<Integer> {

    Married_m(1),
    Married_f(2),
    Unmarried_m(3),
    Unmarried_f(4),
    Marriage_civil(5);

    private Integer id;

    MaritalStatus(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static MaritalStatus fromId(Integer id) {
        for (MaritalStatus at : MaritalStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}