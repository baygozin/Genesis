alter table GENESIS_CELLS_CK add constraint FK_GENESIS_CELLS_CK_POSITION_CK foreign key (POSITION_CK_ID) references GENESIS_POSITION(ID);
