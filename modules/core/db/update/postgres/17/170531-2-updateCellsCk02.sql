alter table GENESIS_CELLS_CK add constraint FK_GENESIS_CELLS_CK_BUILDING foreign key (BUILDING_ID) references GENESIS_BUILDING(ID);
create index IDX_GENESIS_CELLS_CK_BUILDING on GENESIS_CELLS_CK (BUILDING_ID);