alter table GENESIS_CELLS_CK add column POSITION_GROUP_ID uuid ;
alter table GENESIS_CELLS_CK add column BUILDING_ID uuid ;
alter table GENESIS_CELLS_CK drop column POSITION_CK_ID cascade ;
