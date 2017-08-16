create table GENESIS_CLAIM_CK (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CLAIM_DATE date,
    SPECIALTY_ID uuid,
    NUMBER_SPECIALITY integer,
    PARENT_CLAIM_ID uuid,
    BUILDING_ID uuid,
    --
    primary key (ID)
);
