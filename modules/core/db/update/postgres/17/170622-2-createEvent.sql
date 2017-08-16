alter table GENESIS_EVENT add constraint FK_GENESIS_EVENT_OBJECT foreign key (OBJECT_ID) references GENESIS_BUILDING(ID);
alter table GENESIS_EVENT add constraint FK_GENESIS_EVENT_EMPLOYEE foreign key (EMPLOYEE_ID) references GENESIS_EMPLOYEE(ID);
create index IDX_GENESIS_EVENT_OBJECT on GENESIS_EVENT (OBJECT_ID);
create index IDX_GENESIS_EVENT_EMPLOYEE on GENESIS_EVENT (EMPLOYEE_ID);