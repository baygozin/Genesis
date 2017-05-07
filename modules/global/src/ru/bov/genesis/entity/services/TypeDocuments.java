package ru.bov.genesis.entity.services;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|nameDocument")
@Table(name = "GENESIS_TYPE_DOCUMENTS")
@Entity(name = "genesis$TypeDocuments")
public class TypeDocuments extends StandardEntity {
    private static final long serialVersionUID = -3980346330447192404L;

    @Column(name = "NAME_DOCUMENT", unique = true)
    protected String nameDocument;

    public void setNameDocument(String nameDocument) {
        this.nameDocument = nameDocument;
    }

    public String getNameDocument() {
        return nameDocument;
    }


}