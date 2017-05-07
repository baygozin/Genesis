package ru.bov.genesis.entity.services;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|nameDepartment")
@Table(name = "GENESIS_DEPARTMENT")
@Entity(name = "genesis$Department")
public class Department extends StandardEntity {
    private static final long serialVersionUID = 1580531970263204402L;

    @Column(name = "NAME_DEPARTMENT", unique = true)
    protected String nameDepartment;

    public void setNameDepartment(String nameDepartment) {
        this.nameDepartment = nameDepartment;
    }

    public String getNameDepartment() {
        return nameDepartment;
    }


}