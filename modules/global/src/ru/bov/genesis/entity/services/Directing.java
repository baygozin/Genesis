package ru.bov.genesis.entity.services;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|nameDirecting")
@Table(name = "GENESIS_DIRECTING")
@Entity(name = "genesis$Directing")
public class Directing extends StandardEntity {
    private static final long serialVersionUID = -8233794723476650883L;

    @Column(name = "NAME_DIRECTING", unique = true, length = 100)
    protected String nameDirecting;

    public void setNameDirecting(String nameDirecting) {
        this.nameDirecting = nameDirecting;
    }

    public String getNameDirecting() {
        return nameDirecting;
    }


}