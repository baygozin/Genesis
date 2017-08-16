create table GENESIS_REQUEST_ENTITY_COPY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DATE_IN date,
    SPECIALIST_ID uuid,
    NUMBER_SPEC integer,
    PARENT_ID uuid,
    --
    primary key (ID)
);
