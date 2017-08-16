alter table GENESIS_CELLS_CK add column DIRECTING_GROUP_ID uuid ;
alter table GENESIS_CELLS_CK drop column POSITION_GROUP_ID cascade ;
