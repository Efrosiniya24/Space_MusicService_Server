CREATE DATABASE IF NOT EXISTS `preferences`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `preferences`;

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS `preference` (
    `id`               BIGINT NOT NULL AUTO_INCREMENT,
    `user_id`          BIGINT DEFAULT NULL,
    `deleted`          TINYINT(1) NOT NULL DEFAULT 0,
    `created_at`       DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`       DATETIME(6) DEFAULT NULL,
    `venue_id`         BIGINT DEFAULT NULL,
    `time_irrelevant`  TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_preference_venue_id` (`venue_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `preference_address` (
    `id`             BIGINT NOT NULL AUTO_INCREMENT,
    `address_id`     BIGINT NOT NULL,
    `preference_id`  BIGINT NOT NULL,
    `created_at`     DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`),
    KEY `idx_preference_address_preference_id` (`preference_id`),
    CONSTRAINT `fk_preference_address_preference` FOREIGN KEY (`preference_id`) REFERENCES `preference` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `preference_genre` (
    `id`             BIGINT NOT NULL AUTO_INCREMENT,
    `genre_id`       BIGINT NOT NULL,
    `preference_id`  BIGINT NOT NULL,
    `created_at`     DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`),
    KEY `idx_preference_genre_preference_id` (`preference_id`),
    CONSTRAINT `fk_preference_genre_preference` FOREIGN KEY (`preference_id`) REFERENCES `preference` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `preference_track` (
    `id`             BIGINT NOT NULL AUTO_INCREMENT,
    `track_id`       BIGINT NOT NULL,
    `preference_id`  BIGINT NOT NULL,
    `created_at`     DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`),
    KEY `idx_preference_track_preference_id` (`preference_id`),
    CONSTRAINT `fk_preference_track_preference` FOREIGN KEY (`preference_id`) REFERENCES `preference` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `preference_playlist` (
    `id`             BIGINT NOT NULL AUTO_INCREMENT,
    `playlist_id`    BIGINT NOT NULL,
    `preference_id`  BIGINT NOT NULL,
    `created_at`     DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`),
    KEY `idx_preference_playlist_preference_id` (`preference_id`),
    CONSTRAINT `fk_preference_playlist_preference` FOREIGN KEY (`preference_id`) REFERENCES `preference` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `preference_volume` (
    `id`             BIGINT NOT NULL AUTO_INCREMENT,
    `preference_id`  BIGINT NOT NULL,
    `volume_level`   VARCHAR(16) NOT NULL,
    `created_at`     DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_preference_volume_pref_level` (`preference_id`, `volume_level`),
    KEY `idx_preference_volume_preference_id` (`preference_id`),
    CONSTRAINT `fk_preference_volume_preference` FOREIGN KEY (`preference_id`) REFERENCES `preference` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `preference_schedule_block` (
    `id`             BIGINT NOT NULL AUTO_INCREMENT,
    `preference_id`  BIGINT NOT NULL,
    `sort_order`     INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_pref_sched_block_preference_id` (`preference_id`),
    CONSTRAINT `fk_pref_sched_block_preference` FOREIGN KEY (`preference_id`) REFERENCES `preference` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `preference_day` (
    `id`        BIGINT NOT NULL AUTO_INCREMENT,
    `block_id`  BIGINT NOT NULL,
    `weekday`   TINYINT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_preference_day_block_weekday` (`block_id`, `weekday`),
    KEY `idx_preference_day_block_id` (`block_id`),
    CONSTRAINT `fk_preference_day_block` FOREIGN KEY (`block_id`) REFERENCES `preference_schedule_block` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `preference_time` (
    `id`          BIGINT NOT NULL AUTO_INCREMENT,
    `block_id`    BIGINT NOT NULL,
    `time_kind`   VARCHAR(16) NOT NULL,
    `preset_code` VARCHAR(32) DEFAULT NULL COMMENT 'PRESET: MORNING|DAY|EVENING|NIGHT; CUSTOM: NULL',
    `time_from`   TIME DEFAULT NULL,
    `time_to`     TIME DEFAULT NULL,
    `sort_order`  INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_preference_time_block_id` (`block_id`),
    CONSTRAINT `fk_preference_time_block` FOREIGN KEY (`block_id`) REFERENCES `preference_schedule_block` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `preference_scheule_dated` (
    `id`            BIGINT NOT NULL AUTO_INCREMENT,
    `block_id`      BIGINT NOT NULL,
    `specific_date` DATE NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_pref_sched_date_block_date` (`block_id`, `specific_date`),
    KEY `idx_pref_sched_date_block_id` (`block_id`),
    CONSTRAINT `fk_pref_sched_date_block` FOREIGN KEY (`block_id`) REFERENCES `preference_schedule_block` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
