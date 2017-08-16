package ru.bov.genesis.entity.mainentity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import ru.bov.genesis.entity.services.Directing;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import java.util.List;
import javax.persistence.OneToMany;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@NamePattern(" %s %s|specialty,numberSpeciality")
@Table(name = "GENESIS_CLAIM_CK")
@Entity(name = "genesis$ClaimCk")
public class ClaimCk extends StandardEntity {
    private static final long serialVersionUID = -4523747848340716969L;

    @Temporal(TemporalType.DATE)
    @Column(name = "CLAIM_DATE")
    private Date claimDate;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPECIALTY_ID")
    private Directing specialty;

    @Max(1000)
    @Min(1)
    @Column(name = "NUMBER_SPECIALITY")
    private Integer numberSpeciality;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_CLAIM_ID")
    private ClaimCk parentClaim;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUILDING_ID")
    private Building building;

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Building getBuilding() {
        return building;
    }


    public void setParentClaim(ClaimCk parentClaim) {
        this.parentClaim = parentClaim;
    }

    public ClaimCk getParentClaim() {
        return parentClaim;
    }


    public void setSpecialty(Directing specialty) {
        this.specialty = specialty;
    }

    public Directing getSpecialty() {
        return specialty;
    }

    public void setNumberSpeciality(Integer numberSpeciality) {
        this.numberSpeciality = numberSpeciality;
    }

    public Integer getNumberSpeciality() {
        return numberSpeciality;
    }


    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public Date getClaimDate() {
        return claimDate;
    }


}