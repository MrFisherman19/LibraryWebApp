CREATE TABLE `verification_token`
(
    `id`         BIGINT NOT NULL auto_increment primary key,
    `token`      varchar(100),
    `expiryDate` datetime,
    `user_id`    BIGINT NOT NULL,
    foreign key (`user_id`) REFERENCES user (id)
);