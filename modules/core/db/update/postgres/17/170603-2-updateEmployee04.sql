alter table GENESIS_EMPLOYEE add constraint FK_GENESIS_EMPLOYEE_UD_FIRST_HELP_IMAGE foreign key (UD_FIRST_HELP_IMAGE_ID) references SYS_FILE(ID);
create index IDX_GENESIS_EMPLOYEE_UD_FIRST_HELP_IMAGE on GENESIS_EMPLOYEE (UD_FIRST_HELP_IMAGE_ID);