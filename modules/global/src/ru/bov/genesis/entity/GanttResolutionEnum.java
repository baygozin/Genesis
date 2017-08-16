package ru.bov.genesis.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum GanttResolutionEnum implements EnumClass<String> {

    day("day"),
    week("week");

    private String id;

    GanttResolutionEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static GanttResolutionEnum fromId(String id) {
        for (GanttResolutionEnum at : GanttResolutionEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}