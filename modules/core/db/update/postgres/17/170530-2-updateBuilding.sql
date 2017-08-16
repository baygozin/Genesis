update GENESIS_BUILDING set PERIOD_WORK = '' where PERIOD_WORK is null ;
alter table GENESIS_BUILDING alter column PERIOD_WORK set not null ;
update GENESIS_BUILDING set PERIOD_PAUSE = '' where PERIOD_PAUSE is null ;
alter table GENESIS_BUILDING alter column PERIOD_PAUSE set not null ;
