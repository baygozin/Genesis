alter table GENESIS_EMPLOYEE add constraint FK_GENESIS_EMPLOYEE_UD_WORK_HI_IMAGE foreign key (UD_WORK_HI_IMAGE_ID) references SYS_FILE(ID);
create index IDX_GENESIS_EMPLOYEE_UD_WORK_HI_IMAGE on GENESIS_EMPLOYEE (UD_WORK_HI_IMAGE_ID);
