CREATE DATABASE IF NOT EXISTS `users`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `users`;

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS `user` (
    `id`            BIGINT NOT NULL AUTO_INCREMENT,
    `email`         VARCHAR(255) NOT NULL,
    `password`      VARCHAR(255) NOT NULL,
    `name`          VARCHAR(255) DEFAULT NULL,
    `phone`         VARCHAR(255) DEFAULT NULL,
    `is_deleted`    TINYINT(1) NOT NULL DEFAULT 0,
    `created_at`    DATETIME(6) DEFAULT NULL,
    `updated_at`    DATETIME(6) DEFAULT NULL,
    `gender`        VARCHAR(32) DEFAULT NULL,
    `date_of_birth` DATE DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `role` (
    `id`   BIGINT NOT NULL AUTO_INCREMENT,
    `role` VARCHAR(64) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `venue` (
    `id`           BIGINT NOT NULL AUTO_INCREMENT,
    `name`         VARCHAR(255) NOT NULL,
    `email`        VARCHAR(255) DEFAULT NULL,
    `description`  VARCHAR(4000) DEFAULT NULL,
    `url_web_site` VARCHAR(512) DEFAULT NULL,
    `phone`        VARCHAR(255) DEFAULT NULL,
    `deleted`      TINYINT(1) NOT NULL DEFAULT 0,
    `created_at`   DATETIME(6) DEFAULT NULL,
    `updated_at`   DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_venue_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `venue_address` (
    `id`           BIGINT NOT NULL AUTO_INCREMENT,
    `country`      VARCHAR(255) DEFAULT NULL,
    `city`         VARCHAR(255) DEFAULT NULL,
    `address_city` VARCHAR(512) DEFAULT NULL,
    `venue_id`     BIGINT DEFAULT NULL,
    `status`       VARCHAR(32)  NOT NULL,
    `deleted`      TINYINT(1) NOT NULL DEFAULT 0,
    `created_at`   DATETIME(6) DEFAULT NULL,
    `updated_at`   DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_venue_address_venue` FOREIGN KEY (`venue_id`) REFERENCES `venue` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `user_role` (
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `venue_curator` (
    `id`             BIGINT NOT NULL AUTO_INCREMENT,
    `curator_id`     BIGINT DEFAULT NULL,
    `venue_id`       BIGINT DEFAULT NULL,
    `address_id`     BIGINT DEFAULT NULL,
    `is_user_admin`  TINYINT(1) DEFAULT NULL,
    `deleted`        TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_venue_curator_user` FOREIGN KEY (`curator_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_venue_curator_venue` FOREIGN KEY (`venue_id`) REFERENCES `venue` (`id`),
    CONSTRAINT `fk_venue_curator_venue_address` FOREIGN KEY (`address_id`) REFERENCES `venue_address` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
