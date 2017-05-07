package ru.bov.genesis.entity.mainentity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum TypeMapEnum implements EnumClass<String> {

    Roadmap("roadmap"),
    Satellite("satellite"),
    Hybrid("hybrid"),
    Terrain("terrain");

    private String id;

    TypeMapEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TypeMapEnum fromId(String id) {
        for (TypeMapEnum at : TypeMapEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}