package ru.bov.genesis.entity.services;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum BugStatusEnum implements EnumClass<Integer> {

    newbug(1),
    ready(2),
    accepted(3),
    deferred(4),
    corrected(5);

    private Integer id;

    BugStatusEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static BugStatusEnum fromId(Integer id) {
        for (BugStatusEnum at : BugStatusEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}