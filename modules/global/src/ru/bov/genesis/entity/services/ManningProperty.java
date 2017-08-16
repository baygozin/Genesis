package ru.bov.genesis.entity.services;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s %s %s|columnExcel,fieldClass,commentUser")
@Table(name = "GENESIS_MANNING_PROPERTY")
@Entity(name = "genesis$ManningProperty")
public class ManningProperty extends StandardEntity {
    private static final long serialVersionUID = 8279870152996028135L;

    @Column(name = "COLUMN_EXCEL")
    protected String columnExcel;

    @Column(name = "MAIN_CLASS")
    protected String mainClass;

    @Column(name = "FIELD_CLASS")
    protected String fieldClass;

    @Column(name = "TYPE_FIELD")
    protected String typeField;

    @Column(name = "COMMENT_USER")
    protected String commentUser;

    public void setTypeField(String typeField) {
        this.typeField = typeField;
    }

    public String getTypeField() {
        return typeField;
    }


    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setFieldClass(String fieldClass) {
        this.fieldClass = fieldClass;
    }

    public String getFieldClass() {
        return fieldClass;
    }


    public void setColumnExcel(String columnExcel) {
        this.columnExcel = columnExcel;
    }

    public String getColumnExcel() {
        return columnExcel;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser;
    }

    public String getCommentUser() {
        return commentUser;
    }


}