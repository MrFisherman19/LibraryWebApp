-- liquibase formatted sql
-- changeset mrfisherman:13
ALTER TABLE `posts` MODIFY COLUMN book_id BIGINT NULL;