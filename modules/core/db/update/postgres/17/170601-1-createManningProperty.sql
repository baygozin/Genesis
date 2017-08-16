create table GENESIS_MANNING_PROPERTY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    COLUMN_EXCEL varchar(255),
    FIELD_EMPLOYEE varchar(255),
    COMMENT_USER varchar(255),
    --
    primary key (ID)
);
