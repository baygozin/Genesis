create table GENESIS_EVENT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DESCRIPTION varchar(255),
    START_EVENT date,
    STOP_EVENT date,
    COMMENTARY text,
    OBJECT_ID uuid,
    EMPLOYEE_ID uuid,
    --
    primary key (ID)
);
