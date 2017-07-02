/*
Navicat MySQL Data Transfer

Source Server         : mysql57
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : imgbox

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2017-07-02 21:04:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `storage`
-- ----------------------------
DROP TABLE IF EXISTS `storage`;
CREATE TABLE `storage` (
  `STORAGE_HASH` varchar(32) NOT NULL,
  `STORAGE_USER` varchar(32) DEFAULT NULL,
  `STORAGE_TIME` datetime DEFAULT NULL,
  `STORAGE_IP` varchar(128) DEFAULT NULL,
  `STORAGE_MIME` varchar(8) DEFAULT NULL,
  `STORAGE_SIZE` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`STORAGE_HASH`),
  KEY `STORAGE_USER` (`STORAGE_USER`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of storage
-- ----------------------------

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `USER_ID` varchar(32) NOT NULL,
  `USER_EMAIL` varchar(32) DEFAULT NULL,
  `USER_PASSWORD` varchar(32) DEFAULT NULL,
  `USER_STATUS` varchar(8) DEFAULT 'false' COMMENT 'true、false、dead',
  `USER_TIME` datetime DEFAULT NULL,
  `USER_BLACK` int(8) DEFAULT '0',
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `USER_EMAIL` (`USER_EMAIL`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------