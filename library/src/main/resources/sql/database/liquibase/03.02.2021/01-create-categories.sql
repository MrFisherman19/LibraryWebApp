-- liquibase formatted sql
-- changeset mrfisherman:4

CREATE TABLE IF NOT EXISTS `categories`
(
    `name` VARCHAR(30) UNIQUE NOT NULL PRIMARY KEY
);