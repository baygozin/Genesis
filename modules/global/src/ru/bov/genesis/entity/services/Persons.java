package ru.bov.genesis.entity.services;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Lob;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import ru.bov.genesis.entity.mainentity.Building;

@NamePattern("%s %s %s %s %s|namePerson,positionPerson,phonePerson,emailPerson,notePerson")
@Table(name = "GENESIS_PERSONS")
@Entity(name = "genesis$Persons")
public class Persons extends StandardEntity {
    private static final long serialVersionUID = 9066182096095391200L;

    @Column(name = "NAME_PERSON")
    protected String namePerson;

    @Column(name = "POSITION_PERSON")
    protected String positionPerson;

    @Column(name = "PHONE_PERSON")
    protected String phonePerson;

    @Column(name = "EMAIL_PERSON")
    protected String emailPerson;

    @Lob
    @Column(name = "NOTE_PERSON")
    protected String notePerson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUILDING_ID")
    protected Building building;

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Building getBuilding() {
        return building;
    }


    public void setNamePerson(String namePerson) {
        this.namePerson = namePerson;
    }

    public String getNamePerson() {
        return namePerson;
    }

    public void setPositionPerson(String positionPerson) {
        this.positionPerson = positionPerson;
    }

    public String getPositionPerson() {
        return positionPerson;
    }

    public void setPhonePerson(String phonePerson) {
        this.phonePerson = phonePerson;
    }

    public String getPhonePerson() {
        return phonePerson;
    }

    public void setEmailPerson(String emailPerson) {
        this.emailPerson = emailPerson;
    }

    public String getEmailPerson() {
        return emailPerson;
    }

    public void setNotePerson(String notePerson) {
        this.notePerson = notePerson;
    }

    public String getNotePerson() {
        return notePerson;
    }


}