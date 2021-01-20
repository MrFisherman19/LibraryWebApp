DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`        BIGINT      NOT NULL,
    `username`  VARCHAR(25) NOT NULL,
    `email`     VARCHAR(50) NOT NULL,
    `password`  CHAR(60)    NOT NULL,
    `user_role` VARCHAR(25) NOT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `user_details`;
CREATE TABLE `user_details`
(
    `id`       BIGINT NOT NULL,
    `sentence` VARCHAR(2000),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`id`) REFERENCES `user` (`id`)
);

DROP TABLE IF EXISTS 'user_liked_books';
CREATE TABLE `user_liked_books` (
    `user_id` BIGINT NOT NULL,
    `book_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `book_id`),
    CONSTRAINT `fk_user_liked_books_user_id` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    CONSTRAINT `fk_user_liked_books_book_id` FOREIGN KEY (`book_id`) REFERENCES `book`(`id`)
);