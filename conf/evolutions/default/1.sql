# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "ADDRESS" ("ID" SERIAL NOT NULL PRIMARY KEY,"street" VARCHAR(254) NOT NULL,"streetNr" VARCHAR(254) NOT NULL,"cityZip" INTEGER NOT NULL,"city" VARCHAR(254) NOT NULL);
create table "ADMIN_SERVER" ("uuid" VARCHAR(254) NOT NULL PRIMARY KEY);
create table "ARTICLE" ("name" VARCHAR(254) NOT NULL PRIMARY KEY,"descr" VARCHAR(254) NOT NULL,"img" VARCHAR(254) NOT NULL,"playlist" VARCHAR(254) NOT NULL,"active" BOOLEAN NOT NULL);
create table "CLIENT" ("uuid" VARCHAR(254) NOT NULL PRIMARY KEY,"order" INTEGER NOT NULL,"playlist" VARCHAR(254) NOT NULL,"locationId" INTEGER NOT NULL);
create table "LOCATION" ("ID" SERIAL NOT NULL PRIMARY KEY,"uuid" VARCHAR(254) NOT NULL,"latitude" DOUBLE PRECISION NOT NULL,"longitude" DOUBLE PRECISION NOT NULL,"addressId" INTEGER NOT NULL);
alter table "CLIENT" add constraint "LOCATION" foreign key("locationId") references "LOCATION"("ID") on update NO ACTION on delete NO ACTION;
alter table "LOCATION" add constraint "ADDRESS" foreign key("addressId") references "ADDRESS"("ID") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "LOCATION" drop constraint "ADDRESS";
alter table "CLIENT" drop constraint "LOCATION";
drop table "LOCATION";
drop table "CLIENT";
drop table "ARTICLE";
drop table "ADMIN_SERVER";
drop table "ADDRESS";

