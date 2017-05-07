package ru.bov.genesis.entity.services;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|namePosition")
@Table(name = "GENESIS_POSITION")
@Entity(name = "genesis$Position")
public class Position extends StandardEntity {
    private static final long serialVersionUID = -7534365525129766485L;

    @Column(name = "NAME_POSITION", unique = true, length = 100)
    protected String namePosition;

    public void setNamePosition(String namePosition) {
        this.namePosition = namePosition;
    }

    public String getNamePosition() {
        return namePosition;
    }


}