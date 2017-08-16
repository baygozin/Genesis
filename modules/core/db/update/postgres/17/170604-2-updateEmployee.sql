alter table GENESIS_EMPLOYEE add column POSITION_ varchar(255) ;
alter table GENESIS_EMPLOYEE drop column POSITION_ID cascade ;
