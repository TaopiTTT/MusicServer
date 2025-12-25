-- 创建数据库
CREATE DATABASE IF NOT EXISTS music_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE music_db;

-- 用户表 (自增ID)
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `phone` VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(100) NOT NULL COMMENT '昵称',
    `icon` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `followings_count` INT DEFAULT 0 COMMENT '关注数',
    `followers_count` INT DEFAULT 0 COMMENT '粉丝数',
    `tags` JSON COMMENT '标签数组',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_phone (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 歌手表 (自增ID)
CREATE TABLE IF NOT EXISTS `singer` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '歌手ID',
    `nickname` VARCHAR(100) NOT NULL COMMENT '艺名',
    `icon` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `followings_count` INT DEFAULT 0 COMMENT '关注数',
    `followers_count` INT DEFAULT 0 COMMENT '粉丝数',
    `tags` JSON COMMENT '标签数组',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 歌曲表 (自增ID)
CREATE TABLE IF NOT EXISTS `song` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '歌曲ID',
    `title` VARCHAR(200) NOT NULL COMMENT '歌曲标题',
    `icon` VARCHAR(255) DEFAULT NULL COMMENT '封面图',
    `uri` VARCHAR(255) DEFAULT NULL COMMENT '音频资源URL',
    `clicks_count` INT DEFAULT 0 COMMENT '点击数',
    `comments_count` INT DEFAULT 0 COMMENT '评论数',
    `user_id` BIGINT NOT NULL COMMENT '发布者ID',
    `singer_id` BIGINT NOT NULL COMMENT '歌手ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    FOREIGN KEY (`singer_id`) REFERENCES `singer`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 歌单表 (自增ID)
CREATE TABLE IF NOT EXISTS `playlist` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '歌单ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `icon` VARCHAR(255) DEFAULT NULL COMMENT '封面图',
    `detail` TEXT COMMENT '详情文本',
    `user_id` BIGINT NOT NULL COMMENT '创建者ID',
    `clicks_count` INT DEFAULT 0 COMMENT '点击数',
    `collects_count` INT DEFAULT 0 COMMENT '收藏数',
    `comments_count` INT DEFAULT 0 COMMENT '评论数',
    `songs_count` INT DEFAULT 0 COMMENT '歌曲数量',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 歌单-歌曲关联表
CREATE TABLE IF NOT EXISTS `playlist_song` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    `playlist_id` BIGINT NOT NULL COMMENT '歌单ID',
    `song_id` BIGINT NOT NULL COMMENT '歌曲ID',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`playlist_id`) REFERENCES `playlist`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`song_id`) REFERENCES `song`(`id`) ON DELETE CASCADE,
    INDEX idx_playlist_id (`playlist_id`),
    INDEX idx_song_id (`song_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 歌单-标签关联表
CREATE TABLE IF NOT EXISTS `playlist_tag` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    `playlist_id` BIGINT NOT NULL COMMENT '歌单ID',
    `tag_id` BIGINT NOT NULL COMMENT '标签ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`playlist_id`) REFERENCES `playlist`(`id`) ON DELETE CASCADE,
    INDEX idx_playlist_id (`playlist_id`),
    INDEX idx_tag_id (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 标签表
CREATE TABLE IF NOT EXISTS `tag` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标签ID',
    `title` VARCHAR(100) NOT NULL UNIQUE COMMENT '标签名称',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 广告表 (自增ID)
CREATE TABLE IF NOT EXISTS `ad` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '广告ID',
    `title` VARCHAR(200) NOT NULL COMMENT '广告标题',
    `icon` VARCHAR(255) DEFAULT NULL COMMENT '广告图片URL',
    `uri` VARCHAR(500) DEFAULT NULL COMMENT '跳转链接',
    `style` INT DEFAULT 0 COMMENT '样式类型',
    `position` INT DEFAULT 0 COMMENT '位置',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `user_id` BIGINT NOT NULL COMMENT '创建者ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    INDEX idx_position (`position`),
    INDEX idx_sort (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 会话表 (自增ID)
CREATE TABLE IF NOT EXISTS `session` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会话ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `session_token` VARCHAR(500) NOT NULL COMMENT '会话Token',
    `device` VARCHAR(100) DEFAULT NULL COMMENT '设备信息',
    `platform` INT DEFAULT NULL COMMENT '平台类型',
    `push_token` VARCHAR(255) DEFAULT NULL COMMENT '推送Token',
    `roles` VARCHAR(255) DEFAULT NULL COMMENT '用户角色',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_session_token (`session_token`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入测试数据
INSERT INTO `user` (`id`, `phone`, `password`, `nickname`, `icon`, `followings_count`, `followers_count`, `tags`) VALUES
(1417780635407372289, '13212312311', '123456', 'tao', 'assets/megumi.jpg', 0, 0, '["很有想法的", "Spring Boot", "川妹子", "iOS", "Vue3"]');

-- 插入广告测试数据
INSERT INTO `ad` (`id`, `title`, `icon`, `uri`, `style`, `position`, `sort`, `user_id`, `created_at`, `updated_at`) VALUES
(10, '这是广告', 'assets/g1.jpg', 'http://www.hut.edu.cn', 0, 0, 700, 1417780635407372289, '2021-09-04 17:34:05', '2024-08-19 21:59:07');

INSERT INTO `singer` (`id`, `nickname`, `icon`, `followings_count`, `followers_count`, `tags`) VALUES
(105, 'Rice Shower', 'assets/singer.jpg', 0, 0, '["米浴", "哥哥", "iOS"]');

INSERT INTO `song` (`id`, `title`, `icon`, `uri`, `clicks_count`, `comments_count`, `user_id`, `singer_id`, `created_at`, `updated_at`) VALUES
(13, 'Wind', 'assets/song.png', 'assets/Wind.mp3', 0, 0, 1417780635407372289, 105, '2019-09-17 05:56:50', '2022-03-03 10:12:20');

INSERT INTO `song` (`id`, `title`, `icon`, `uri`, `clicks_count`, `comments_count`, `user_id`, `singer_id`, `created_at`, `updated_at`) VALUES
(14, 'EndlessRoad', 'assets/song.png', 'assets/Endlessroad.mp3', 0, 0, 1417780635407372289, 105, '2019-09-17 05:56:50', '2022-03-03 10:12:20');


INSERT INTO `playlist` (`id`, `title`, `icon`, `detail`, `user_id`, `clicks_count`, `collects_count`, `comments_count`, `songs_count`, `created_at`, `updated_at`) VALUES
(1, '这是歌单1', 'assets/list1.jpg', '歌单1', 1417780635407372289, 21306, 22, 44, 2, '2200-04-10 00:28:49', '2025-12-24 20:11:49');

INSERT INTO `playlist` (`id`, `title`, `icon`, `detail`, `user_id`, `clicks_count`, `collects_count`, `comments_count`, `songs_count`, `created_at`, `updated_at`) VALUES
(2, '这是歌单2', 'assets/list2.png', '歌单2', 1417780635407372289, 21306, 22, 44, 0, '2200-04-10 00:28:49', '2025-12-24 20:11:49');

-- 插入标签数据
INSERT INTO `tag` (`id`, `title`) VALUES
(1, '流行'),
(2, '华语'),
(3, '摇滚'),
(4, '民谣'),
(5, '电子'),
(6, '说唱');

-- 插入歌单-标签关联数据
INSERT INTO `playlist_tag` (`playlist_id`, `tag_id`) VALUES
(1, 2),  -- 歌单1 - 华语
(1, 5);  -- 歌单1 - 电子

-- 插入歌单-歌曲关联数据
INSERT INTO `playlist_song` (`playlist_id`, `song_id`, `sort_order`) VALUES
(1, 13, 0),
(1, 14, 1);

