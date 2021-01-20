-- DROP TABLE IF EXISTS `books_categories`;
CREATE TABLE IF NOT EXISTS `books_categories`
(
    `book_id`     BIGINT NOT NULL,
    `category_id` BIGINT NOT NULL,
    FOREIGN KEY (`book_id`) REFERENCES books (`id`),
    FOREIGN KEY (`category_id`) REFERENCES categories (`id`)
);