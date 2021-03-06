package ru.bov.genesis.entity.mainentity;

import javax.persistence.*;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import java.util.Date;
import java.util.List;

import com.haulmont.chile.core.annotations.Composition;
import java.util.Set;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import java.util.Collection;

import ru.bov.genesis.entity.services.Persons;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import javax.validation.constraints.Pattern;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import javax.validation.constraints.NotNull;
import ru.bov.genesis.entity.services.Position;

@NamePattern("%s|nameShort")
@Table(name = "GENESIS_BUILDING")
@Entity(name = "genesis$Building")
public class Building extends StandardEntity {
    private static final long serialVersionUID = -1019766510808553063L;

    @Column(name = "NAME_SHORT")
    protected String nameShort;

    @Column(name = "NAME_FULL")
    protected String nameFull;

    @Column(name = "CUSTOMER")
    protected String customer;

    @Column(name = "CONTRACT")
    protected String contract;

    @Column(name = "TYPE_WORK")
    protected String typeWork;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_BEGIN")
    protected Date dateBegin;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_END")
    protected Date dateEnd;

    @Column(name = "PLACE")
    protected String place;

    @Column(name = "PLACE_TYPE")
    protected String placeType;

    @Column(name = "PLACE_SCALE")
    protected Integer placeScale;

    @OneToMany(mappedBy = "building")
    protected List<Employee> employeeCk;

    @OneToMany(mappedBy = "building")
    protected List<Persons> persons;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMAGE_CONTRACT_ID")
    protected FileDescriptor imageContract;



    @NotNull(message = "{msg://ru.bov.genesis.entity.mainentity/periodWorkNot}")
    @Pattern(message = "{msg://ru.bov.genesis.entity.mainentity/patternMessage}", regexp = "^(\\d{1,2}[\u041C\u043C\u041D\u043D\u0414\u0434]{1}){1,3}$")
    @Column(name = "PERIOD_WORK", nullable = false)
    protected String periodWork;

    @NotNull(message = "{msg://ru.bov.genesis.entity.mainentity/periodPauseNot}")
    @Pattern(message = "{msg://ru.bov.genesis.entity.mainentity/patternMessage}", regexp = "^(\\d{1,2}[\u041C\u043C\u041D\u043D\u0414\u0434]{1}){1,3}$")
    @Column(name = "PERIOD_PAUSE", nullable = false)
    protected String periodPause;






    @Column(name = "NUMBER_EMPLOYEE")
    private Integer numberEmployee;




    @OneToMany(mappedBy = "building")
    private List<ClaimCk> claimCk;

    public void setClaimCk(List<ClaimCk> claimCk) {
        this.claimCk = claimCk;
    }

    public List<ClaimCk> getClaimCk() {
        return claimCk;
    }


    public void setNumberEmployee(Integer numberEmployee) {
        this.numberEmployee = numberEmployee;
    }

    public Integer getNumberEmployee() {
        return numberEmployee;
    }




    public void setPeriodWork(String periodWork) {
        this.periodWork = periodWork;
    }

    public String getPeriodWork() {
        return periodWork;
    }

    public void setPeriodPause(String periodPause) {
        this.periodPause = periodPause;
    }

    public String getPeriodPause() {
        return periodPause;
    }





    public void setImageContract(FileDescriptor imageContract) {
        this.imageContract = imageContract;
    }

    public FileDescriptor getImageContract() {
        return imageContract;
    }


    public void setPersons(List<Persons> persons) {
        this.persons = persons;
    }

    public List<Persons> getPersons() {
        return persons;
    }


    public void setEmployeeCk(List<Employee> employeeCk) {
        this.employeeCk = employeeCk;
    }

    public List<Employee> getEmployeeCk() {
        return employeeCk;
    }


    public void setPlaceScale(Integer placeScale) {
        this.placeScale = placeScale;
    }

    public Integer getPlaceScale() {
        return placeScale;
    }


    public void setPlaceType(TypeMapEnum placeType) {
        this.placeType = placeType == null ? null : placeType.getId();
    }

    public TypeMapEnum getPlaceType() {
        return placeType == null ? null : TypeMapEnum.fromId(placeType);
    }


    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getContract() {
        return contract;
    }

    public void setTypeWork(String typeWork) {
        this.typeWork = typeWork;
    }

    public String getTypeWork() {
        return typeWork;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }


    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameFull(String nameFull) {
        this.nameFull = nameFull;
    }

    public String getNameFull() {
        return nameFull;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomer() {
        return customer;
    }


}