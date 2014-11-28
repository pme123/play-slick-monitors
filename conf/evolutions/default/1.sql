# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "ARTICLE" ("name" VARCHAR(254) NOT NULL PRIMARY KEY,"descr" VARCHAR(254) NOT NULL,"img" VARCHAR(254) NOT NULL);
create table "CLIENT" ("uuid" VARCHAR(254) NOT NULL PRIMARY KEY);

# --- !Downs

drop table "CLIENT";
drop table "ARTICLE";

