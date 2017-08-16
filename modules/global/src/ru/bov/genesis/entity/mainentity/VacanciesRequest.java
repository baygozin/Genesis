package ru.bov.genesis.entity.mainentity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import ru.bov.genesis.entity.services.Directing;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s %s %s|speciality,numberVacancy,note")
@Table(name = "GENESIS_VACANCIES_REQUEST")
@Entity(name = "genesis$VacanciesRequest")
public class VacanciesRequest extends StandardEntity {
    private static final long serialVersionUID = 5032014729873675658L;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SPECIALITY_ID")
    private Directing speciality;

    @Column(name = "NUMBER_VACANCY", nullable = false)
    private Integer numberVacancy;

    @Column(name = "NOTE")
    private String note;


    public void setSpeciality(Directing speciality) {
        this.speciality = speciality;
    }

    public Directing getSpeciality() {
        return speciality;
    }

    public void setNumberVacancy(Integer numberVacancy) {
        this.numberVacancy = numberVacancy;
    }

    public Integer getNumberVacancy() {
        return numberVacancy;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }


}