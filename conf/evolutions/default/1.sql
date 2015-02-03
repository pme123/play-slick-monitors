# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

CREATE TABLE "ADDRESS" (
  "ID"       INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
  "street"   VARCHAR NOT NULL,
  "streetNr" VARCHAR NOT NULL,
  "cityZip"  INTEGER NOT NULL,
  "city"     VARCHAR NOT NULL
);
create table "ADMIN_SERVER" ("uuid" VARCHAR NOT NULL PRIMARY KEY);
CREATE TABLE "ARTICLE" (
  "name"     VARCHAR NOT NULL PRIMARY KEY,
  "descr"    VARCHAR NOT NULL,
  "img"      VARCHAR NOT NULL,
  "playlist" VARCHAR NOT NULL,
  "active"   BOOLEAN NOT NULL
);
create table "CLIENT" ("uuid" VARCHAR NOT NULL PRIMARY KEY,"order" INTEGER NOT NULL,"playlist" VARCHAR NOT NULL);
CREATE TABLE "LOCATION" (
  "ID"         INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
  "uuid"       VARCHAR NOT NULL,
  "ADDRESS_ID" INTEGER NOT NULL
);
ALTER TABLE "LOCATION" ADD CONSTRAINT "ADDRESS" FOREIGN KEY ("ADDRESS_ID") REFERENCES "ADDRESS" ("ID") ON UPDATE NO ACTION ON DELETE NO ACTION;

# --- !Downs

ALTER TABLE "LOCATION" DROP CONSTRAINT "ADDRESS";
DROP TABLE "LOCATION";
drop table "CLIENT";
drop table "ARTICLE";
drop table "ADMIN_SERVER";
DROP TABLE "ADDRESS";

