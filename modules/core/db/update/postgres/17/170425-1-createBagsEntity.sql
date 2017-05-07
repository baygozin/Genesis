create table GENESIS_BAGS_ENTITY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    BUG_DATE_TIME timestamp,
    BUG_FIELD text,
    BUG_STATUS integer,
    BUG_ANSWER text,
    --
    primary key (ID)
);
