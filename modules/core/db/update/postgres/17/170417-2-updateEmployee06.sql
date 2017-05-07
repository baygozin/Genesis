alter table GENESIS_EMPLOYEE alter column PASS_NUMBER set data type varchar(6) ;
alter table GENESIS_EMPLOYEE alter column PASS_SERIES set data type varchar(5) ;
alter table GENESIS_EMPLOYEE drop column VUZ_DATE_BEGIN cascade ;
alter table GENESIS_EMPLOYEE add column VUZ_DATE_BEGIN varchar(4) ;
alter table GENESIS_EMPLOYEE drop column VUZ_DATE_END cascade ;
alter table GENESIS_EMPLOYEE add column VUZ_DATE_END varchar(4) ;
