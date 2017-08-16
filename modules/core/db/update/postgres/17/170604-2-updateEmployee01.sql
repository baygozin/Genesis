alter table GENESIS_EMPLOYEE drop column PASS_NUMBER cascade ;
alter table GENESIS_EMPLOYEE drop column PASS_SERIES cascade ;
alter table GENESIS_EMPLOYEE drop column PASS_ISSUED cascade ;
alter table GENESIS_EMPLOYEE alter column NUMBER_INN set data type varchar(255) ;
alter table GENESIS_EMPLOYEE alter column NUMBER_PFR set data type varchar(255) ;
alter table GENESIS_EMPLOYEE alter column PHONE_MOBILE set data type varchar(255) ;
alter table GENESIS_EMPLOYEE alter column PHONE_HOME set data type varchar(255) ;
alter table GENESIS_EMPLOYEE alter column PHONE_OFFICE set data type varchar(255) ;
