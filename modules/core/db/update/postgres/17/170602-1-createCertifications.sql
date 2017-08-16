create table GENESIS_CERTIFICATIONS (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CERT_NAME varchar(255),
    CERT_NUMBER varchar(255),
    CERT_DATE_END date,
    CERT_IMAGE_ID uuid,
    EMPLOYEE_ID uuid,
    --
    primary key (ID)
);
