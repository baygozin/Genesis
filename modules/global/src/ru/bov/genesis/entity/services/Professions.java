package ru.bov.genesis.entity.services;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|nameProfession")
@Table(name = "GENESIS_PROFESSIONS")
@Entity(name = "genesis$Professions")
public class Professions extends StandardEntity {
    private static final long serialVersionUID = 3707390705354108578L;

    @Column(name = "NAME_PROFESSION", unique = true, length = 100)
    protected String nameProfession;

    public void setNameProfession(String nameProfession) {
        this.nameProfession = nameProfession;
    }

    public String getNameProfession() {
        return nameProfession;
    }


}