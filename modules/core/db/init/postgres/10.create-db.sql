-- begin GENESIS_PROFESSIONS
create table GENESIS_PROFESSIONS (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME_PROFESSION varchar(100),
    --
    primary key (ID)
)^
-- end GENESIS_PROFESSIONS
-- begin GENESIS_POSITION
create table GENESIS_POSITION (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME_POSITION varchar(100),
    --
    primary key (ID)
)^
-- end GENESIS_POSITION
-- begin GENESIS_DIRECTING
create table GENESIS_DIRECTING (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME_DIRECTING varchar(100),
    --
    primary key (ID)
)^
-- end GENESIS_DIRECTING
-- begin GENESIS_DEPARTMENT
create table GENESIS_DEPARTMENT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME_DEPARTMENT varchar(255),
    --
    primary key (ID)
)^
-- end GENESIS_DEPARTMENT


-- begin GENESIS_TYPE_DOCUMENTS
create table GENESIS_TYPE_DOCUMENTS (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME_DOCUMENT varchar(255),
    --
    primary key (ID)
)^
-- end GENESIS_TYPE_DOCUMENTS
-- begin GENESIS_EMPLOYEE
create table GENESIS_EMPLOYEE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    IMAGE_PHOTO_ID uuid,
    IMAGE_SIGN_ID uuid,
    FIRST_NAME varchar(255),
    MIDDLE_NAME varchar(255),
    LAST_NAME varchar(255),
    FULL_NAME text,
    POSITION_ varchar(255),
    DIRECTION_WORK_ID uuid,
    PROFESSION_ID uuid,
    PASS_ISSUED_DATE date,
    PASS_COMMON text,
    GENDER varchar(50),
    BIRTHDAY date,
    PLACE_OF_BIRTH varchar(255),
    ADDRESS_POSTAL varchar(255),
    ADDRESS_RESIDENTIAL varchar(255),
    ADDRESS_REGISTRATION varchar(255),
    DATE_ADDRES_REGISTRATION date,
    MARTIAL_STATUS integer,
    NUMBER_IFNS varchar(255),
    NUMBER_INN varchar(255),
    NUMBER_PFR varchar(255),
    PHONE_MOBILE varchar(255),
    PHONE_HOME varchar(255),
    PHONE_OFFICE varchar(255),
    EMAIL_PRIVATE varchar(255),
    EMAIL_OFFICE varchar(255),
    NAKS_NUMBER varchar(255),
    NAKS_DATE_EXPIRE date,
    NAKS_NGDO varchar(255),
    NAKS_OHNVP varchar(255),
    NAKS_GO varchar(255),
    NAKS_PTO varchar(255),
    NAKS_SK varchar(255),
    NAKS_KO varchar(255),
    NAKS_IMAGE_ID uuid,
    VIK_NUMBER varchar(255),
    VIK_DATE_EXPIRE date,
    VIK_VIK varchar(255),
    VIK_RK varchar(255),
    VIK_VK varchar(255),
    VIK_MK varchar(255),
    VIK_EK varchar(255),
    VIK_UK varchar(255),
    VIK_PVT varchar(255),
    VIK_VD varchar(255),
    VIK_PVK varchar(255),
    VIK_IMAGE_ID uuid,
    VUZ_NAME varchar(255),
    VUZ_SITY varchar(255),
    VUZ_NUMBER varchar(255),
    VUZ_DATE_BEGIN varchar(4),
    VUZ_DATE_END varchar(4),
    VUZ_SPEC varchar(255),
    VUZ_KVAL varchar(255),
    VUZ_IMAGE_ID uuid,
    MEDICAL_CHECK_BASE_BEGIN date,
    MEDICAL_CHECK_BASE_END date,
    MEDICAL_CHECK_PERIODIC_BEGIN date,
    MEDICAL_CHECK_PERIODIC_END date,
    BUILDING_ID uuid,
    FIELD_STATUS varchar(255),
    COMMENT_MAN text,
    DEPARTMENT_ID uuid,
    DEPARTMENT_TERRITORY varchar(255),
    CONTRACT_TYPE varchar(255),
    CONTRACT_DATE date,
    EMPLOYMENT_DATE date,
    UD_CK_NUMBER varchar(255),
    UD_CK_EXPIRE date,
    UD_CK_IMAGE_ID uuid,
    UD_FIRST_HELP_NUMBER varchar(255),
    UD_FIRST_HELP_EXPIRE date,
    UD_FIRST_HELP_IMAGE_ID uuid,
    UD_ECO_SAF_NUMBER varchar(255),
    UD_ECO_SAF_EXPIRE date,
    UD_ECO_SAF_IMAGE_ID uuid,
    UD_WORK_HI_NUMBER varchar(255),
    UD_WORK_HI_EXPIRE date,
    UD_WORK_HI_IMAGE_ID uuid,
    UD_DRIVE_SAF_NUMBER varchar(255),
    UD_DRIVE_SAF_EXPIRE date,
    UD_DRIVE_SAF_IMAGE_ID uuid,
    UD_EB_NUMBER varchar(255),
    UD_EB_EXPIRE date,
    UD_EB_IMAGE_ID uuid,
    UD_OT_NUMBER varchar(255),
    UD_OT_EXPIRE date,
    UD_OT_IMAGE_ID uuid,
    UD_PTM_NUMBER varchar(255),
    UD_PTM_EXPIRE date,
    UD_PTM_IMAGE_ID uuid,
    UD_PB_NUMBER varchar(255),
    UD_PB_EXPIRE date,
    UD_PB_IMAGE_ID uuid,
    EXPIRE_DATE_ALL varchar(255),
    ARHIV boolean,
    --
    primary key (ID)
)^
-- end GENESIS_EMPLOYEE
-- begin GENESIS_COURSES
create table GENESIS_COURSES (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    ORGANIZATION varchar(255),
    CITY varchar(255),
    DATE_END date,
    HOURS integer,
    IMAGE_ID uuid,
    EMPLOYEE_ID uuid,
    --
    primary key (ID)
)^
-- end GENESIS_COURSES
-- begin GENESIS_STORAGE_FILES
create table GENESIS_STORAGE_FILES (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    COMMENT_ varchar(255),
    FILE_ID uuid,
    EMPLOYEE_ID uuid,
    --
    primary key (ID)
)^
-- end GENESIS_STORAGE_FILES
-- begin GENESIS_BAGS_ENTITY
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
    FILE_ID uuid,
    --
    primary key (ID)
)^
-- end GENESIS_BAGS_ENTITY
-- begin GENESIS_BUILDING
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
    CONTRACT varchar(255),
    TYPE_WORK varchar(255),
    DATE_BEGIN date,
    DATE_END date,
    PLACE varchar(255),
    PLACE_TYPE varchar(50),
    PLACE_SCALE integer,
    IMAGE_CONTRACT_ID uuid,
    PERIOD_WORK varchar(255) not null,
    PERIOD_PAUSE varchar(255) not null,
    NUMBER_EMPLOYEE integer,
    --
    primary key (ID)
)^
-- end GENESIS_BUILDING
-- begin GENESIS_PERSONS
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
    BUILDING_ID uuid,
    --
    primary key (ID)
)^
-- end GENESIS_PERSONS

-- begin GENESIS_MANNING_PROPERTY
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
    MAIN_CLASS varchar(255),
    FIELD_CLASS varchar(255),
    TYPE_FIELD varchar(255),
    COMMENT_USER varchar(255),
    --
    primary key (ID)
)^
-- end GENESIS_MANNING_PROPERTY
-- begin GENESIS_EVENT
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
    STATUS varchar(255),
    COMMENTARY text,
    OBJECT_ID uuid,
    EMPLOYEE_ID uuid,
    --
    primary key (ID)
)^
-- end GENESIS_EVENT

-- begin GENESIS_VACANCIES_REQUEST
create table GENESIS_VACANCIES_REQUEST (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    SPECIALITY_ID uuid not null,
    NUMBER_VACANCY integer not null,
    NOTE varchar(255),
    --
    primary key (ID)
)^
-- end GENESIS_VACANCIES_REQUEST

-- begin GENESIS_REQUEST_ENTITY
create table GENESIS_REQUEST_ENTITY (
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
)^
-- end GENESIS_REQUEST_ENTITY
-- begin GENESIS_REQUEST_ENTITY_COPY
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
)^
-- end GENESIS_REQUEST_ENTITY_COPY
-- begin GENESIS_CLAIM_CK
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
)^
-- end GENESIS_CLAIM_CK
