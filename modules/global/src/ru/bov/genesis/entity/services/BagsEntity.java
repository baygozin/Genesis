package ru.bov.genesis.entity.services;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Listeners("genesis_BagsEntityEntityListener")
@NamePattern("%s %s %s %s|bugDateTime,bugField,bugStatus,bugAnswer")
@Table(name = "GENESIS_BAGS_ENTITY")
@Entity(name = "genesis$BagsEntity")
public class BagsEntity extends StandardEntity {
    private static final long serialVersionUID = -8442448780789502898L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BUG_DATE_TIME")
    protected Date bugDateTime;

    @Lob
    @Column(name = "BUG_FIELD")
    protected String bugField;

    @Column(name = "BUG_STATUS")
    protected Integer bugStatus;


    @Lob
    @Column(name = "BUG_ANSWER")
    protected String bugAnswer;

    @OnDelete(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_ID")
    protected FileDescriptor file;

    public void setFile(FileDescriptor file) {
        this.file = file;
    }

    public FileDescriptor getFile() {
        return file;
    }


    public void setBugDateTime(Date bugDateTime) {
        this.bugDateTime = bugDateTime;
    }

    public Date getBugDateTime() {
        return bugDateTime;
    }


    public void setBugAnswer(String bugAnswer) {
        this.bugAnswer = bugAnswer;
    }

    public String getBugAnswer() {
        return bugAnswer;
    }


    public BugStatusEnum getBugStatus() {
        return bugStatus == null ? null : BugStatusEnum.fromId(bugStatus);
    }

    public void setBugStatus(BugStatusEnum bugStatus) {
        this.bugStatus = bugStatus == null ? null : bugStatus.getId();
    }


    public void setBugField(String bugField) {
        this.bugField = bugField;
    }

    public String getBugField() {
        return bugField;
    }


}