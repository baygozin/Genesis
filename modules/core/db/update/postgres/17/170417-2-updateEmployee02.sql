alter table GENESIS_EMPLOYEE add constraint FK_GENESIS_EMPLOYEE_NAKS_IMAGE foreign key (NAKS_IMAGE_ID) references SYS_FILE(ID);
create index IDX_GENESIS_EMPLOYEE_NAKS_IMAGE on GENESIS_EMPLOYEE (NAKS_IMAGE_ID);
