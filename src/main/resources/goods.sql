/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50143
Source Host           : localhost:3306
Source Database       : idea

Target Server Type    : MYSQL
Target Server Version : 50143
File Encoding         : 65001

Date: 2019-09-02 22:53:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `goods`
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `store` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES ('1', '华为', '0', '1');
