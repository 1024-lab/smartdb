-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.20-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  10.2.0.5670
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 smartdb 的数据库结构
DROP DATABASE IF EXISTS `smartdb`;
CREATE DATABASE IF NOT EXISTS `smartdb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `smartdb`;

-- 导出  表 smartdb.t_insert_sql_builder 结构
DROP TABLE IF EXISTS `t_insert_sql_builder`;
CREATE TABLE IF NOT EXISTS `t_insert_sql_builder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `balance` decimal(10,3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- 正在导出表  smartdb.t_insert_sql_builder 的数据：~1 rows (大约)
DELETE FROM `t_insert_sql_builder`;
/*!40000 ALTER TABLE `t_insert_sql_builder` DISABLE KEYS */;
INSERT INTO `t_insert_sql_builder` (`id`, `key`, `create_time`, `balance`) VALUES
	(2, NULL, '2019-09-28 11:26:57', NULL);
/*!40000 ALTER TABLE `t_insert_sql_builder` ENABLE KEYS */;

-- 导出  表 smartdb.t_select_sql_builder 结构
DROP TABLE IF EXISTS `t_select_sql_builder`;
CREATE TABLE IF NOT EXISTS `t_select_sql_builder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nick_name` text,
  `city` text,
  `age` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=181 DEFAULT CHARSET=utf8;

-- 正在导出表  smartdb.t_select_sql_builder 的数据：~15 rows (大约)
DELETE FROM `t_select_sql_builder`;
/*!40000 ALTER TABLE `t_select_sql_builder` DISABLE KEYS */;
INSERT INTO `t_select_sql_builder` (`id`, `nick_name`, `city`, `age`, `create_time`, `update_time`) VALUES
	(1, 'login-name 1', 'city1', 3, '2018-08-11 00:00:00', '2018-08-11 00:00:00'),
	(2, 'login-name 2', 'city2', 2, '2018-08-12 00:00:00', '2018-08-12 00:00:00'),
	(3, 'login-name 3', 'city3', 3, '2018-08-13 00:00:00', '2018-08-13 00:00:00'),
	(4, 'login-name 4', 'city4', 1, '2018-08-14 00:00:00', '2018-08-14 00:00:00'),
	(5, 'login-name 5', 'city5', 2, '2018-08-15 00:00:00', '2018-08-15 00:00:00'),
	(6, 'login-name 6', 'city6', 3, '2018-08-16 00:00:00', '2018-08-16 00:00:00'),
	(7, 'login-name 7', 'city7', 2, '2018-08-17 00:00:00', '2018-08-17 00:00:00'),
	(8, 'login-name 8', 'city8', 2, '2018-08-18 00:00:00', '2018-08-18 00:00:00'),
	(9, 'login-name 9', 'city9', 1, '2018-08-19 00:00:00', '2018-08-19 00:00:00'),
	(10, 'login-name 10', 'city10', 3, '2018-08-20 00:00:00', '2018-08-20 00:00:00'),
	(11, 'login-name 11', 'city11', 2, '2018-08-21 00:00:00', '2018-08-21 00:00:00'),
	(12, 'login-name 12', 'city12', 2, '2018-08-22 00:00:00', '2018-08-22 00:00:00'),
	(13, 'login-name 13', 'city13', 3, '2018-08-23 00:00:00', '2018-08-23 00:00:00'),
	(14, 'login-name 14', 'city14', 1, '2018-08-24 00:00:00', '2018-08-24 00:00:00'),
	(15, 'login-name 15', 'city15', 2, '2018-08-25 00:00:00', '2018-08-25 00:00:00');
/*!40000 ALTER TABLE `t_select_sql_builder` ENABLE KEYS */;

-- 导出  表 smartdb.t_update_sql_builder 结构
DROP TABLE IF EXISTS `t_update_sql_builder`;
CREATE TABLE IF NOT EXISTS `t_update_sql_builder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `tag` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=281 DEFAULT CHARSET=utf8;

-- 正在导出表  smartdb.t_update_sql_builder 的数据：~10 rows (大约)
DELETE FROM `t_update_sql_builder`;
/*!40000 ALTER TABLE `t_update_sql_builder` DISABLE KEYS */;
INSERT INTO `t_update_sql_builder` (`id`, `name`, `tag`, `create_time`) VALUES
	(271, 'name 0', 'tag0', '2019-09-25 09:49:31'),
	(272, 'name 1', 'tag1', '2019-09-25 09:49:31'),
	(273, 'hi', 'update-tag', '2019-09-25 09:49:31'),
	(274, 'name 3', 'tag3', '2019-09-25 09:49:31'),
	(275, 'name 4', 'tag4', '2019-09-25 09:49:31'),
	(276, 'name 5', 'tag5', '2019-09-25 09:49:31'),
	(277, 'name 6', 'tag6', '2019-09-25 09:49:31'),
	(278, 'name 7', 'tag7', '2019-09-25 09:49:31'),
	(279, 'name 8', 'tag8', '2019-09-25 09:49:31'),
	(280, 'name 9', 'tag9', '2019-09-25 09:49:31');
/*!40000 ALTER TABLE `t_update_sql_builder` ENABLE KEYS */;

-- 导出  表 smartdb.t_user 结构
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- 正在导出表  smartdb.t_user 的数据：~0 rows (大约)
DELETE FROM `t_user`;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
