-- liquibase formatted sql
-- changeset mrfisherman:10
CREATE TABLE IF NOT EXISTS `authors`
(
    `id`     BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`   VARCHAR(30) NOT NULL
);
-- changeset mrfisherman:11
CREATE TABLE IF NOT EXISTS `books_authors`
(
    `book_id`   BIGINT NOT NULL,
    `author_id` BIGINT NOT NULL,
    FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
    FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`)
);