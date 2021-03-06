alter table GENESIS_CELLS_CK_ENTITY add constraint FK_GENESIS_CELLS_CK_ENTITY_POSITION foreign key (POSITION_ID) references GENESIS_POSITION(ID);
alter table GENESIS_CELLS_CK_ENTITY add constraint FK_GENESIS_CELLS_CK_ENTITY_BUILDING foreign key (BUILDING_ID) references GENESIS_BUILDING(ID);
create index IDX_GENESIS_CELLS_CK_ENTITY_POSITION on GENESIS_CELLS_CK_ENTITY (POSITION_ID);
create index IDX_GENESIS_CELLS_CK_ENTITY_BUILDING on GENESIS_CELLS_CK_ENTITY (BUILDING_ID);
