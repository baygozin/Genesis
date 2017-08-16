package ru.bov.genesis.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import ru.bov.genesis.entity.services.Directing;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s %s %s|dateIn,specialist,numberSpec")
@Table(name = "GENESIS_REQUEST_ENTITY")
@Entity(name = "genesis$RequestEntity")
public class RequestEntity extends StandardEntity {
    private static final long serialVersionUID = -6400994881371124086L;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_IN")
    private Date dateIn;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPECIALIST_ID")
    private Directing specialist;

    @Column(name = "NUMBER_SPEC")
    private Integer numberSpec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private RequestEntity parent;

    public void setParent(RequestEntity parent) {
        this.parent = parent;
    }

    public RequestEntity getParent() {
        return parent;
    }


    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setSpecialist(Directing specialist) {
        this.specialist = specialist;
    }

    public Directing getSpecialist() {
        return specialist;
    }

    public void setNumberSpec(Integer numberSpec) {
        this.numberSpec = numberSpec;
    }

    public Integer getNumberSpec() {
        return numberSpec;
    }


}