alter table GENESIS_BUILDING add column PERIOD_WORK varchar(255) ;
alter table GENESIS_BUILDING add column PERIOD_PAUSE varchar(255) ;
alter table GENESIS_BUILDING drop column VALUE_WORK cascade ;
alter table GENESIS_BUILDING drop column VALUE_PAUSE cascade ;
