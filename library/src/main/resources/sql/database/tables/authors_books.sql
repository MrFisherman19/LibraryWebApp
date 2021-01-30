CREATE TABLE IF NOT EXISTS `books_authors`
(
    `book_id` BIGINT NOT NULL,
    `author_id`   BIGINT NOT NULL,
    FOREIGN KEY (`book_id`) REFERENCES 	`books` (`id`),
    FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`)
);