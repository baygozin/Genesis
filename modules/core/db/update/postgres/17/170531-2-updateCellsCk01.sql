alter table GENESIS_CELLS_CK add constraint FK_GENESIS_CELLS_CK_POSITION_GROUP foreign key (POSITION_GROUP_ID) references GENESIS_POSITION(ID);
create index IDX_GENESIS_CELLS_CK_POSITION_GROUP on GENESIS_CELLS_CK (POSITION_GROUP_ID);
