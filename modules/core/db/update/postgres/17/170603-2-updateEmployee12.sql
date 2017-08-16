alter table GENESIS_EMPLOYEE add column UD_IN_ECO_SAF_NUMBER varchar(255) ;
alter table GENESIS_EMPLOYEE add column UD_IN_ECO_SAF_EXPIRE date ;
alter table GENESIS_EMPLOYEE add column UD_IN_ECO_SAFE_IMAGE_ID uuid ;
alter table GENESIS_EMPLOYEE drop column UD_FIRST_HELP_EXPIRE cascade ;
alter table GENESIS_EMPLOYEE add column UD_FIRST_HELP_EXPIRE date ;
