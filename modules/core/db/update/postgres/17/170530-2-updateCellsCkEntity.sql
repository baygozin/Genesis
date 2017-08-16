alter table GENESIS_CELLS_CK_ENTITY add column POSITION_ varchar(255) ;
alter table GENESIS_CELLS_CK_ENTITY drop column POSITION_ID cascade ;
