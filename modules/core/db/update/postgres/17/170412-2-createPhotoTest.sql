alter table GENESIS_PHOTO_TEST add constraint FK_GENESIS_PHOTO_TEST_IMAGE foreign key (IMAGE_ID) references SYS_FILE(ID);
create index IDX_GENESIS_PHOTO_TEST_IMAGE on GENESIS_PHOTO_TEST (IMAGE_ID);
