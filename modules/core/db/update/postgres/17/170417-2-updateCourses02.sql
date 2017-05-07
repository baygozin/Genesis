alter table GENESIS_COURSES add column ORGANIZATION varchar(255) ;
alter table GENESIS_COURSES add column DATE_END date ;
alter table GENESIS_COURSES add column HOURS integer ;
alter table GENESIS_COURSES drop column ORGANIZACION cascade ;
alter table GENESIS_COURSES drop column DATA_END_CIRSES cascade ;
alter table GENESIS_COURSES drop column HOURS_NUMBER cascade ;
