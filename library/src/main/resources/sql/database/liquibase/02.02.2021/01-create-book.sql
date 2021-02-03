-- liquibase formatted sql
-- changeset mrfisherman:1

CREATE TABLE IF NOT EXISTS `books`
(
    `id`              BIGINT             NOT NULL AUTO_INCREMENT,
    `title`           VARCHAR(300)       NOT NULL,
    `description`     VARCHAR(2000)      NULL,
    `summary`         MEDIUMTEXT         NULL,
    `publish_year`    YEAR               NULL,
    `type`            VARCHAR(30)        NOT NULL,
    `number_of_pages` INT                NULL,
    `isbn`            VARCHAR(13) UNIQUE NOT NULL,
    `created`         DATETIME,
    `updated`         DATETIME,
    PRIMARY KEY (`id`)
);