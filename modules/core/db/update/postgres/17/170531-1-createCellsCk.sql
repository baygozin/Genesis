create table GENESIS_CELLS_CK (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    POSITION_CK_ID uuid,
    COUNT_VACANCY integer,
    --
    primary key (ID)
);
