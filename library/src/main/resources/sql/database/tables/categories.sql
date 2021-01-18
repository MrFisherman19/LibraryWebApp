DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`
(
    `id`   BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(30) UNIQUE,
    PRIMARY KEY (`id`)
);