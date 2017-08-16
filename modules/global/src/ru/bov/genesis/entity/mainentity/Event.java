package ru.bov.genesis.entity.mainentity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import ru.bov.genesis.entity.services.StatusEmployeeEnum;

import javax.persistence.*;
import java.util.Date;

@NamePattern("%s %s %s %s %s|status,startEvent,stopEvent,description,commentary")
@Table(name = "GENESIS_EVENT")
@Entity(name = "genesis$Event")
public class Event extends StandardEntity {
    private static final long serialVersionUID = 454907968727424659L;

    @Column(name = "DESCRIPTION")
    private String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_EVENT")
    private Date startEvent;

    @Temporal(TemporalType.DATE)
    @Column(name = "STOP_EVENT")
    private Date stopEvent;

    @Column(name = "STATUS")
    private String status;

    @Lob
    @Column(name = "COMMENTARY")
    private String commentary;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OBJECT_ID")
    private Building object;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    public StatusEmployeeEnum getStatus() {
        return status == null ? null : StatusEmployeeEnum.fromId(status);
    }

    public void setStatus(StatusEmployeeEnum status) {
        this.status = status == null ? null : status.getId();
    }




    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }


    public void setStartEvent(Date startEvent) {
        this.startEvent = startEvent;
    }

    public Date getStartEvent() {
        return startEvent;
    }

    public void setStopEvent(Date stopEvent) {
        this.stopEvent = stopEvent;
    }

    public Date getStopEvent() {
        return stopEvent;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setObject(Building object) {
        this.object = object;
    }

    public Building getObject() {
        return object;
    }


}