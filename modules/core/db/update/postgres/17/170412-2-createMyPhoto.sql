alter table GENESIS_MY_PHOTO add constraint FK_GENESIS_MY_PHOTO_IMAGE foreign key (IMAGE_ID) references SYS_FILE(ID);
create index IDX_GENESIS_MY_PHOTO_IMAGE on GENESIS_MY_PHOTO (IMAGE_ID);
