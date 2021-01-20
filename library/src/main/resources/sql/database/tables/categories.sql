--     DROP TABLE IF EXISTS `categories`;
    CREATE TABLE IF NOT EXISTS `categories`
    (
        `id`   BIGINT NOT NULL AUTO_INCREMENT,
        `name` VARCHAR(30) UNIQUE,
        PRIMARY KEY (`id`)
    );