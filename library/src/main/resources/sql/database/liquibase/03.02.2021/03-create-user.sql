-- liquibase formatted sql
-- changeset mrfisherman:6
CREATE TABLE IF NOT EXISTS `users`
(
    `id`                         BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `username`                   varchar(25)  not null UNIQUE,
    `password`                   varchar(100) not null,
    `email`                      varchar(50)  not null UNIQUE,
    `is_non_locked`              tinyint(1),
    `is_non_expired`             tinyint(1),
    `is_credentials_non_expired` tinyint(1),
    `is_enabled`                 tinyint(1)
);
-- changeset mrfisherman:7
CREATE TABLE IF NOT EXISTS `roles`
(
    `id`   BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL
);
-- changeset mrfisherman:8
CREATE TABLE IF NOT EXISTS `user_roles`
(
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    CONSTRAINT `fk_user_liked_books_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_user_liked_books_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
);
-- changeset mrfisherman:9
CREATE TABLE `user_liked_books`
(
    `user_id` BIGINT NOT NULL,
    `book_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `book_id`),
    CONSTRAINT `fk_user_liked_books_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_user_liked_books_book_id` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
);