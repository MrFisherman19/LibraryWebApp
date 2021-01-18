DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments`
(
    `id`        BIGINT NOT NULL AUTO_INCREMENT,
    `content`   VARCHAR(3000),
    `created`   DATETIME,
    `updated`   DATETIME,
    `vote_up`   INT,
    `vote_down` INT,
    `post_id`   BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`post_id`) REFERENCES posts (`id`)
);