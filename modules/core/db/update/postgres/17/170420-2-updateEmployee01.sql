alter table GENESIS_EMPLOYEE add constraint FK_GENESIS_EMPLOYEE_NAKS_FILES foreign key (NAKS_FILES_ID) references GENESIS_STORAGE_FILES(ID);
create index IDX_GENESIS_EMPLOYEE_NAKS_FILES on GENESIS_EMPLOYEE (NAKS_FILES_ID);
