create table GENESIS_BUILDING (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME_SHORT varchar(255),
    NAME_FULL varchar(255),
    CUSTOMER varchar(255),
    --
    primary key (ID)
);
