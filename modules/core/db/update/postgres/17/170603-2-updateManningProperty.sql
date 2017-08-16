alter table GENESIS_MANNING_PROPERTY add column MAIN_CLASS varchar(255) ;
alter table GENESIS_MANNING_PROPERTY add column FIELD_CLASS varchar(255) ;
alter table GENESIS_MANNING_PROPERTY drop column FIELD_EMPLOYEE cascade ;
