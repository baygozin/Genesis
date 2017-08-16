alter table GENESIS_EMPLOYEE add column IMAGE_PHOTO_ID uuid ;
alter table GENESIS_EMPLOYEE add column IMAGE_SIGN_ID uuid ;
alter table GENESIS_EMPLOYEE add column PASS_COMMON text ;
alter table GENESIS_EMPLOYEE add column NUMBER_IFNS varchar(255) ;
alter table GENESIS_EMPLOYEE add column COMMENT_MAN text ;
alter table GENESIS_EMPLOYEE add column DEPARTMENT_ID uuid ;
alter table GENESIS_EMPLOYEE add column DEPARTMENT_TERRITORY varchar(255) ;
alter table GENESIS_EMPLOYEE drop column IMAGE_ID cascade ;
