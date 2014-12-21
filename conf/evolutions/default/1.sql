# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "ARTICLE" ("name" VARCHAR NOT NULL PRIMARY KEY,"descr" VARCHAR NOT NULL,"img" VARCHAR NOT NULL,"play" VARCHAR NOT NULL);
create table "CLIENT" ("uuid" VARCHAR NOT NULL PRIMARY KEY,"order" INTEGER NOT NULL);

# --- !Downs

drop table "CLIENT";
drop table "ARTICLE";

