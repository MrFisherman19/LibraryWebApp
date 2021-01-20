-- DROP TABLE IF EXISTS `posts`;
CREATE TABLE IF NOT EXISTS `posts`
(
    `id`              BIGINT             NOT NULL AUTO_INCREMENT,
    `title`           VARCHAR(300)       NOT NULL,
    `content`         MEDIUMTEXT,
    `created`         DATETIME,
    `updated`         DATETIME,
    `vote_up`          INT,
    `vote_down`        INT,
    `book_id`         BIGINT            NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`book_id`) REFERENCES books(`id`)
);