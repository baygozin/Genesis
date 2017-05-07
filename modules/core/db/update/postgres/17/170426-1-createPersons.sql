create table GENESIS_PERSONS (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME_PERSON varchar(255),
    POSITION_PERSON varchar(255),
    PHONE_PERSON varchar(255),
    EMAIL_PERSON varchar(255),
    NOTE_PERSON text,
    --
    primary key (ID)
);
