create table GENESIS_PHOTO_TEST (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME_PHOTO varchar(255),
    IMAGE_ID uuid,
    --
    primary key (ID)
);
