CREATE DATABASE IF NOT EXISTS `media`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `media`;

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS `genre` (
    `id`       BIGINT NOT NULL AUTO_INCREMENT,
    `name`     VARCHAR(255) DEFAULT NULL,
    `deleted`  TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `image` (
    `id`         BIGINT NOT NULL AUTO_INCREMENT,
    `id_owner`   BIGINT DEFAULT NULL,
    `bucket`     VARCHAR(255) DEFAULT NULL,
    `object_key` VARCHAR(1024) DEFAULT NULL,
    `file_name`  VARCHAR(512) DEFAULT NULL,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `artist` (
    `id`           BIGINT NOT NULL AUTO_INCREMENT,
    `name`         VARCHAR(255) NOT NULL,
    `id_cover`     BIGINT DEFAULT NULL COMMENT 'FK -> image.id (обложка)',
    `description`  VARCHAR(4000) DEFAULT NULL,
    `is_deleted`   TINYINT(1) NOT NULL DEFAULT 0,
    `created_at`   DATETIME(6) DEFAULT NULL,
    `updated_at`   DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_artist_id_cover_image` FOREIGN KEY (`id_cover`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `track` (
    `id`         BIGINT NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(512) DEFAULT NULL,
    `duration`   BIGINT DEFAULT NULL,
    `id_cover`   BIGINT DEFAULT NULL COMMENT 'FK -> image.id (обложка)',
    `bucket`     VARCHAR(255) DEFAULT NULL,
    `object_key` VARCHAR(1024) DEFAULT NULL,
    `removed`    TINYINT(1) NOT NULL DEFAULT 0,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_track_id_cover_image` FOREIGN KEY (`id_cover`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `playlist` (
    `id`          BIGINT NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(512) DEFAULT NULL,
    `id_cover`    BIGINT DEFAULT NULL COMMENT 'FK -> image.id (обложка)',
    `id_creator`  BIGINT DEFAULT NULL,
    `removed`     TINYINT(1) NOT NULL DEFAULT 0,
    `created_at`  DATETIME(6) DEFAULT NULL,
    `updated_at`  DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_playlist_id_cover_image` FOREIGN KEY (`id_cover`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `track_genre` (
    `id`        BIGINT NOT NULL AUTO_INCREMENT,
    `id_track`  BIGINT DEFAULT NULL,
    `id_genre`  BIGINT DEFAULT NULL,
    `added_at`  DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_track_genre_track` FOREIGN KEY (`id_track`) REFERENCES `track` (`id`),
    CONSTRAINT `fk_track_genre_genre` FOREIGN KEY (`id_genre`) REFERENCES `genre` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `track_artist` (
    `id`        BIGINT NOT NULL AUTO_INCREMENT,
    `track_id`  BIGINT DEFAULT NULL,
    `artist_id` BIGINT DEFAULT NULL,
    `added_at`  DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_track_artist_track` FOREIGN KEY (`track_id`) REFERENCES `track` (`id`),
    CONSTRAINT `fk_track_artist_artist` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `track_playlist` (
    `id`          BIGINT NOT NULL AUTO_INCREMENT,
    `id_playlist` BIGINT DEFAULT NULL,
    `id_track`    BIGINT DEFAULT NULL,
    `removed`     TINYINT(1) NOT NULL DEFAULT 0,
    `added_at`    DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_track_playlist_playlist` FOREIGN KEY (`id_playlist`) REFERENCES `playlist` (`id`),
    CONSTRAINT `fk_track_playlist_track` FOREIGN KEY (`id_track`) REFERENCES `track` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `playlist_user_venue` (
    `id`          BIGINT NOT NULL AUTO_INCREMENT,
    `id_playlist` BIGINT DEFAULT NULL,
    `id_user`     BIGINT DEFAULT NULL,
    `venue_id`    BIGINT DEFAULT NULL,
    `added_at`    DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_playlist_user_venue_playlist` FOREIGN KEY (`id_playlist`) REFERENCES `playlist` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `artist_playlist` (
    `id`           BIGINT NOT NULL AUTO_INCREMENT,
    `artist_id`    BIGINT DEFAULT NULL,
    `playlist_id`  BIGINT DEFAULT NULL,
    `deleted`      TINYINT(1) NOT NULL DEFAULT 0,
    `created_at`   DATETIME(6) DEFAULT NULL,
    `deleted_at`   DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_artist_playlist_artist` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`),
    CONSTRAINT `fk_artist_playlist_playlist` FOREIGN KEY (`playlist_id`) REFERENCES `playlist` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `image_venue` (
    `id`       BIGINT NOT NULL AUTO_INCREMENT,
    `image_id` BIGINT DEFAULT NULL COMMENT 'FK -> image.id',
    `venue_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_image_venue_image` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `venue_documents` (
    `id`               BIGINT NOT NULL AUTO_INCREMENT,
    `venue_id`         BIGINT DEFAULT NULL,
    `venue_address_id` BIGINT DEFAULT NULL,
    `url`              VARCHAR(2048) DEFAULT NULL,
    `title`            VARCHAR(512) DEFAULT NULL,
    `bucket`           VARCHAR(255) DEFAULT NULL,
    `object_key`       VARCHAR(1024) DEFAULT NULL,
    `created_at`       DATETIME(6) DEFAULT NULL,
    `removed`          TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
