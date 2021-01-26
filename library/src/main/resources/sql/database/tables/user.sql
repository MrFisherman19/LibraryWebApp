CREATE TABLE `users`
(
    `username` varchar(50) not null primary key,
    `password` varchar(50) not null,
    `enabled`  boolean     not null,
    `email`    VARCHAR(50) UNIQUE NULL
);

DROP TABLE IF EXISTS `user_details`;
CREATE TABLE `user_details`
(
    `username` VARCHAR(50) NOT NULL,
    `sentence` VARCHAR(2000),
    `created`  DATETIME,
    PRIMARY KEY (`username`),
    FOREIGN KEY (`username`) REFERENCES `users` (`username`)
);

DROP TABLE IF EXISTS 'user_liked_books';
CREATE TABLE `user_liked_books`
(
    `user_id` BIGINT NOT NULL,
    `book_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `book_id`),
    CONSTRAINT `fk_user_liked_books_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_user_liked_books_book_id` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
);