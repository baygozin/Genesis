create table GENESIS_RETROSPECTION (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CURRENT_DAY date not null,
    CURRENT_OBJECT varchar(255),
    IS_SHIFT boolean,
    HOURS_WORKED time,
    COMMENT_DAY varchar(255),
    --
    primary key (ID)
);
