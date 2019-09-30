create table if not exists DB_INFO (
  DB_ID int not null primary key auto_increment,
  DB_NAME varchar(50),
  DB_URL varchar(100),
  DB_TYPE varchar(30),
  USER_NAME varchar(50),
  PASSWORD varchar(50),
  STATUS int(4) default(0),
  UNIQUE KEY "UNIQ_DB_NAME" ("DB_NAME")
);