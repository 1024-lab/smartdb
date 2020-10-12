-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.20 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 smartdb 的数据库结构
DROP DATABASE IF EXISTS `smartdb`;
CREATE DATABASE IF NOT EXISTS `smartdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `smartdb`;

-- 导出  表 smartdb.t_delete_sql_builder 结构
DROP TABLE IF EXISTS `t_delete_sql_builder`;
CREATE TABLE IF NOT EXISTS `t_delete_sql_builder` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- 正在导出表  smartdb.t_delete_sql_builder 的数据：~0 rows (大约)
DELETE FROM `t_delete_sql_builder`;
/*!40000 ALTER TABLE `t_delete_sql_builder` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_delete_sql_builder` ENABLE KEYS */;

-- 导出  表 smartdb.t_enum 结构
DROP TABLE IF EXISTS `t_enum`;
CREATE TABLE IF NOT EXISTS `t_enum` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL DEFAULT '0',
  `sex` varchar(50) NOT NULL DEFAULT '0',
  `level` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 正在导出表  smartdb.t_enum 的数据：~0 rows (大约)
DELETE FROM `t_enum`;
/*!40000 ALTER TABLE `t_enum` DISABLE KEYS */;
INSERT INTO `t_enum` (`id`, `user_name`, `sex`, `level`) VALUES
	(1, 'world', 'GIRL', 2);
/*!40000 ALTER TABLE `t_enum` ENABLE KEYS */;

-- 导出  表 smartdb.t_insert_sql_builder 结构
DROP TABLE IF EXISTS `t_insert_sql_builder`;
CREATE TABLE IF NOT EXISTS `t_insert_sql_builder` (
  `id` bigint NOT NULL,
  `uuid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `key` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `balance` decimal(10,3) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- 正在导出表  smartdb.t_insert_sql_builder 的数据：~1 rows (大约)
DELETE FROM `t_insert_sql_builder`;
/*!40000 ALTER TABLE `t_insert_sql_builder` DISABLE KEYS */;
INSERT INTO `t_insert_sql_builder` (`id`, `uuid`, `create_time`, `key`, `balance`) VALUES
	(2, NULL, '2020-09-08 15:09:38', NULL, NULL);
/*!40000 ALTER TABLE `t_insert_sql_builder` ENABLE KEYS */;

-- 导出  表 smartdb.t_replace_sql_builder 结构
DROP TABLE IF EXISTS `t_replace_sql_builder`;
CREATE TABLE IF NOT EXISTS `t_replace_sql_builder` (
  `id` int unsigned NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `auto_increase_id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`auto_increase_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- 正在导出表  smartdb.t_replace_sql_builder 的数据：~1 rows (大约)
DELETE FROM `t_replace_sql_builder`;
/*!40000 ALTER TABLE `t_replace_sql_builder` DISABLE KEYS */;
INSERT INTO `t_replace_sql_builder` (`id`, `name`, `create_time`, `auto_increase_id`) VALUES
	(1, 'testReplaceBuilder-replace', '2019-07-11 16:28:15', 2);
/*!40000 ALTER TABLE `t_replace_sql_builder` ENABLE KEYS */;

-- 导出  表 smartdb.t_select_sql_builder 结构
DROP TABLE IF EXISTS `t_select_sql_builder`;
CREATE TABLE IF NOT EXISTS `t_select_sql_builder` (
  `id` bigint NOT NULL,
  `login_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nick_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `city` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `age` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- 正在导出表  smartdb.t_select_sql_builder 的数据：~1 rows (大约)
DELETE FROM `t_select_sql_builder`;
/*!40000 ALTER TABLE `t_select_sql_builder` DISABLE KEYS */;
INSERT INTO `t_select_sql_builder` (`id`, `login_name`, `name`, `nick_name`, `city`, `age`, `create_time`, `update_time`) VALUES
	(1, NULL, NULL, 'login-name 1', 'city1', 2, '2018-08-11 00:00:00', '2018-08-11 00:00:00'),
	(2, NULL, NULL, 'login-name 2', 'city2', 1, '2018-08-12 00:00:00', '2018-08-12 00:00:00'),
	(3, NULL, NULL, 'login-name 3', 'city3', 1, '2018-08-13 00:00:00', '2018-08-13 00:00:00'),
	(4, NULL, NULL, 'login-name 4', 'city4', 3, '2018-08-14 00:00:00', '2018-08-14 00:00:00'),
	(5, NULL, NULL, 'login-name 5', 'city5', 3, '2018-08-15 00:00:00', '2018-08-15 00:00:00'),
	(6, NULL, NULL, 'login-name 6', 'city6', 3, '2018-08-16 00:00:00', '2018-08-16 00:00:00'),
	(7, NULL, NULL, 'login-name 7', 'city7', 3, '2018-08-17 00:00:00', '2018-08-17 00:00:00'),
	(8, NULL, NULL, 'login-name 8', 'city8', 1, '2018-08-18 00:00:00', '2018-08-18 00:00:00'),
	(9, NULL, NULL, 'login-name 9', 'city9', 2, '2018-08-19 00:00:00', '2018-08-19 00:00:00'),
	(10, NULL, NULL, 'login-name 10', 'city10', 2, '2018-08-20 00:00:00', '2018-08-20 00:00:00'),
	(11, NULL, NULL, 'login-name 11', 'city11', 3, '2018-08-21 00:00:00', '2018-08-21 00:00:00'),
	(12, NULL, NULL, 'login-name 12', 'city12', 1, '2018-08-22 00:00:00', '2018-08-22 00:00:00'),
	(13, NULL, NULL, 'login-name 13', 'city13', 1, '2018-08-23 00:00:00', '2018-08-23 00:00:00'),
	(14, NULL, NULL, 'login-name 14', 'city14', 1, '2018-08-24 00:00:00', '2018-08-24 00:00:00'),
	(15, NULL, NULL, 'login-name 15', 'city15', 1, '2018-08-25 00:00:00', '2018-08-25 00:00:00');
/*!40000 ALTER TABLE `t_select_sql_builder` ENABLE KEYS */;

-- 导出  表 smartdb.t_transaction 结构
DROP TABLE IF EXISTS `t_transaction`;
CREATE TABLE IF NOT EXISTS `t_transaction` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- 正在导出表  smartdb.t_transaction 的数据：~0 rows (大约)
DELETE FROM `t_transaction`;
/*!40000 ALTER TABLE `t_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_transaction` ENABLE KEYS */;

-- 导出  表 smartdb.t_update_sql_builder 结构
DROP TABLE IF EXISTS `t_update_sql_builder`;
CREATE TABLE IF NOT EXISTS `t_update_sql_builder` (
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=588 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- 正在导出表  smartdb.t_update_sql_builder 的数据：~1 rows (大约)
DELETE FROM `t_update_sql_builder`;
/*!40000 ALTER TABLE `t_update_sql_builder` DISABLE KEYS */;
INSERT INTO `t_update_sql_builder` (`name`, `tag`, `create_time`, `id`) VALUES
	('name 0', 'tag0', '2020-09-08 15:11:32', 578),
	('name 1', 'tag1', '2020-09-08 15:11:32', 579),
	('hi', 'update-tag', '2020-09-08 15:11:32', 580),
	('name 3', 'tag3', '2020-09-08 15:11:32', 581),
	('name 4', 'tag4', '2020-09-08 15:11:32', 582),
	('name 5', 'tag5', '2020-09-08 15:11:32', 583),
	('name 6', 'tag6', '2020-09-08 15:11:32', 584),
	('name 7', 'tag7', '2020-09-08 15:11:32', 585),
	('name 8', 'tag8', '2020-09-08 15:11:32', 586),
	('name 9', 'tag9', '2020-09-08 15:11:32', 587);
/*!40000 ALTER TABLE `t_update_sql_builder` ENABLE KEYS */;

-- 导出  表 smartdb.t_user 结构
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  smartdb.t_user 的数据：~0 rows (大约)
DELETE FROM `t_user`;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
