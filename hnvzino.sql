/*
 Navicat Premium Data Transfer

 Source Server         : myweb6
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:3306
 Source Schema         : hnvzino

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 14/03/2024 07:28:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ta_aut_auth_service
-- ----------------------------
DROP TABLE IF EXISTS `ta_aut_auth_service`;
CREATE TABLE `ta_aut_auth_service`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `T_Info_01` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'serviceClass.serviceName',
  `T_Info_02` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'service name',
  `I_Type_01` int(11) NULL DEFAULT NULL,
  `I_Type_02` int(11) NULL DEFAULT NULL,
  `I_Status` int(11) NULL DEFAULT NULL,
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'dt begin',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'dt end',
  `T_Aut_Role` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'id cac vai tro lien quan, vd: 100, 200 & 300, 102 & 400',
  `T_Aut_Right` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'id cac quyen lien quan, vd: 12001, 12003 & 12004, copy lai tu TA_AUT_ROLE.T_Aut_Right',
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TAASE_01`(`T_Info_01`) USING BTREE,
  INDEX `idx_TAASE_02`(`T_Info_02`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_aut_auth_service
-- ----------------------------

-- ----------------------------
-- Table structure for ta_aut_auth_user
-- ----------------------------
DROP TABLE IF EXISTS `ta_aut_auth_user`;
CREATE TABLE `ta_aut_auth_user`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Aut_User` int(11) NULL DEFAULT NULL,
  `I_Aut_Role` int(11) NULL DEFAULT NULL,
  `I_Status` int(11) NULL DEFAULT NULL,
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'dt begin',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'dt end',
  `T_Aut_Right` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'id cac quyen lien quan, vd: 12001, 12003, copy lai tu TA_AUT_ROLE.T_Aut_Right',
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TAAUS_01`(`I_Aut_User`) USING BTREE,
  INDEX `idx_TAAUS_02`(`I_Aut_Role`) USING BTREE,
  CONSTRAINT `FK_TAAUS_02` FOREIGN KEY (`I_Aut_Role`) REFERENCES `ta_aut_role` (`I_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_TAAUS_03` FOREIGN KEY (`I_Aut_User`) REFERENCES `ta_aut_user` (`I_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_aut_auth_user
-- ----------------------------
INSERT INTO `ta_aut_auth_user` VALUES (1, 1, 100, 1, '2024-03-09 18:56:45', '2024-05-03 18:56:46', '1000001,1000002,1000003,1000004,1000005,30000011');
INSERT INTO `ta_aut_auth_user` VALUES (2, 2, 100, 1, '2024-03-08 17:03:34', '2024-05-03 18:56:46', '30000011, 1000005, 1000004, 1000003, 1000002, 1000001');

-- ----------------------------
-- Table structure for ta_aut_history
-- ----------------------------
DROP TABLE IF EXISTS `ta_aut_history`;
CREATE TABLE `ta_aut_history`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Aut_User` int(11) NOT NULL,
  `I_Type` int(11) NOT NULL,
  `D_Date` datetime NOT NULL,
  `T_Val` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'IP/other info',
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TACHI_01`(`I_Aut_User`) USING BTREE,
  INDEX `idx_TACHI_02`(`I_Type`) USING BTREE,
  CONSTRAINT `fk_TACHI_01` FOREIGN KEY (`I_Aut_User`) REFERENCES `ta_aut_user` (`I_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 266 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_aut_history
-- ----------------------------
INSERT INTO `ta_aut_history` VALUES (1, 1, 1, '2024-02-02 09:49:19', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (2, 1, 1, '2024-02-03 17:25:00', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (3, 1, 1, '2024-02-03 20:02:00', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (4, 1, 1, '2024-02-04 14:57:50', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (5, 1, 1, '2024-02-04 17:23:48', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (6, 1, 1, '2024-02-04 18:24:54', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (7, 1, 1, '2024-02-04 18:41:19', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (8, 1, 1, '2024-02-04 18:49:22', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (9, 1, 1, '2024-02-04 22:46:14', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (10, 1, 1, '2024-02-04 22:48:57', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (11, 1, 1, '2024-02-04 22:53:02', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (12, 1, 1, '2024-02-19 19:04:53', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (13, 1, 1, '2024-02-21 21:56:12', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (14, 1, 1, '2024-02-21 22:57:07', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (15, 1, 1, '2024-02-22 17:20:28', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (16, 1, 1, '2024-02-22 17:31:16', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (17, 1, 1, '2024-02-22 17:37:44', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (18, 1, 1, '2024-02-27 18:08:02', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (19, 1, 1, '2024-02-27 18:35:50', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (20, 1, 1, '2024-02-27 18:38:03', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (21, 1, 1, '2024-02-27 18:44:36', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (22, 1, 1, '2024-02-27 18:55:14', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (23, 1, 1, '2024-02-27 18:55:34', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (24, 1, 1, '2024-02-27 19:13:35', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (25, 1, 1, '2024-03-01 14:13:18', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (26, 1, 1, '2024-03-01 20:23:06', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (27, 1, 1, '2024-03-01 20:31:27', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (28, 1, 1, '2024-03-02 18:20:53', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (29, 1, 1, '2024-03-02 18:23:07', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (30, 1, 1, '2024-03-02 19:07:16', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (31, 1, 1, '2024-03-02 23:25:03', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (32, 1, 1, '2024-03-03 12:12:15', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (33, 1, 1, '2024-03-03 13:19:58', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (34, 1, 1, '2024-03-04 09:18:03', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (35, 1, 1, '2024-03-04 09:20:51', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (36, 1, 1, '2024-03-04 09:31:36', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (37, 1, 1, '2024-03-04 09:38:39', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (38, 1, 1, '2024-03-04 09:42:02', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (39, 1, 1, '2024-03-04 09:45:16', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (40, 1, 1, '2024-03-04 09:51:18', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (41, 1, 1, '2024-03-04 09:57:17', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (42, 1, 1, '2024-03-04 09:59:25', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (43, 1, 1, '2024-03-04 10:14:35', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (44, 1, 1, '2024-03-04 10:54:24', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (45, 1, 1, '2024-03-04 11:36:33', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (46, 1, 1, '2024-03-04 14:59:14', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (47, 1, 1, '2024-03-04 15:09:05', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (48, 1, 1, '2024-03-04 16:23:50', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (49, 1, 1, '2024-03-05 16:04:28', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (50, 1, 1, '2024-03-05 17:06:33', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (51, 1, 1, '2024-03-05 18:59:29', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (52, 1, 1, '2024-03-05 19:22:50', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (53, 1, 1, '2024-03-05 19:24:01', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (54, 1, 1, '2024-03-05 19:54:14', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (55, 1, 1, '2024-03-05 20:29:35', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (56, 1, 1, '2024-03-05 20:30:36', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (57, 1, 1, '2024-03-05 20:33:26', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (58, 1, 1, '2024-03-05 20:51:09', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (59, 1, 1, '2024-03-05 20:53:14', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (60, 1, 1, '2024-03-05 20:58:25', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (61, 1, 1, '2024-03-05 21:01:33', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (62, 1, 1, '2024-03-05 21:09:26', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (63, 1, 1, '2024-03-05 22:11:30', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (64, 1, 1, '2024-03-05 22:43:33', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (65, 1, 1, '2024-03-05 22:53:00', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (66, 1, 1, '2024-03-05 23:04:06', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (67, 1, 1, '2024-03-06 11:55:18', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (68, 1, 1, '2024-03-06 11:57:47', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (69, 1, 1, '2024-03-06 12:24:57', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (70, 1, 1, '2024-03-06 12:27:16', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (71, 1, 1, '2024-03-06 12:33:12', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (72, 1, 1, '2024-03-06 12:35:37', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (73, 1, 1, '2024-03-06 12:37:13', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (74, 1, 1, '2024-03-06 12:39:12', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (75, 1, 1, '2024-03-06 12:41:57', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (76, 1, 1, '2024-03-06 12:42:53', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (77, 1, 1, '2024-03-06 12:46:29', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (78, 1, 1, '2024-03-06 12:52:54', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (79, 1, 1, '2024-03-06 12:55:35', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (80, 1, 1, '2024-03-06 13:07:29', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (81, 1, 1, '2024-03-06 13:12:56', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (82, 1, 1, '2024-03-06 13:17:31', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (83, 1, 1, '2024-03-06 13:18:29', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (84, 1, 1, '2024-03-06 13:28:56', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (85, 1, 1, '2024-03-06 13:30:33', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (86, 1, 1, '2024-03-06 13:40:15', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (87, 1, 1, '2024-03-06 13:42:05', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (88, 1, 1, '2024-03-06 13:42:08', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (89, 1, 1, '2024-03-06 13:46:09', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (90, 1, 1, '2024-03-06 13:49:25', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (91, 1, 1, '2024-03-06 13:59:34', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (92, 1, 1, '2024-03-06 14:50:58', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (93, 1, 1, '2024-03-06 14:53:25', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (94, 1, 1, '2024-03-06 14:56:59', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (95, 1, 1, '2024-03-06 18:38:00', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (96, 1, 1, '2024-03-06 18:46:01', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (97, 1, 1, '2024-03-06 18:57:03', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (98, 1, 1, '2024-03-06 19:00:20', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (99, 1, 1, '2024-03-06 20:10:08', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (100, 1, 1, '2024-03-06 20:17:55', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (101, 1, 1, '2024-03-06 20:24:48', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (102, 1, 1, '2024-03-06 20:32:08', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (103, 1, 1, '2024-03-06 20:48:06', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (104, 1, 1, '2024-03-06 20:49:59', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (105, 1, 1, '2024-03-07 16:54:04', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (106, 1, 1, '2024-03-07 22:49:47', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (107, 1, 1, '2024-03-07 23:02:13', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (108, 1, 1, '2024-03-07 23:03:46', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (109, 1, 1, '2024-03-07 23:17:40', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (110, 1, 1, '2024-03-08 00:19:07', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (111, 1, 1, '2024-03-08 15:12:26', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (112, 1, 1, '2024-03-08 16:43:39', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (113, 1, 1, '2024-03-08 17:20:41', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (114, 1, 1, '2024-03-08 17:21:57', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (115, 1, 1, '2024-03-08 17:25:05', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (116, 1, 1, '2024-03-08 17:27:29', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (117, 1, 1, '2024-03-08 17:31:43', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (118, 1, 1, '2024-03-08 17:32:49', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (119, 1, 1, '2024-03-08 17:33:48', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (120, 1, 1, '2024-03-08 17:35:32', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (121, 1, 1, '2024-03-08 17:37:39', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (122, 1, 1, '2024-03-08 18:32:44', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (123, 1, 1, '2024-03-08 18:35:12', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (124, 1, 1, '2024-03-08 19:12:47', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (125, 1, 1, '2024-03-08 19:14:48', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (126, 1, 1, '2024-03-08 19:17:09', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (127, 1, 1, '2024-03-08 19:23:54', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (128, 1, 1, '2024-03-08 19:25:04', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (129, 1, 1, '2024-03-08 19:33:00', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (130, 1, 1, '2024-03-08 19:34:40', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (131, 1, 1, '2024-03-08 19:37:23', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (132, 1, 1, '2024-03-08 19:48:16', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (133, 1, 1, '2024-03-08 19:55:55', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (134, 1, 1, '2024-03-09 10:40:41', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (135, 1, 1, '2024-03-09 11:25:24', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (136, 1, 1, '2024-03-09 12:24:12', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (137, 1, 1, '2024-03-09 12:29:35', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (138, 1, 1, '2024-03-09 13:34:52', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (139, 1, 1, '2024-03-09 18:46:04', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (140, 1, 1, '2024-03-13 17:14:47', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (141, 1, 1, '2024-03-13 17:23:00', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (142, 1, 1, '2024-03-13 17:23:30', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (143, 1, 1, '2024-03-13 17:25:05', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (144, 1, 1, '2024-03-13 17:25:29', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (145, 1, 1, '2024-03-13 17:26:03', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (146, 1, 1, '2024-03-13 17:26:32', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (147, 1, 1, '2024-03-13 17:26:38', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (148, 1, 1, '2024-03-13 17:30:51', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (149, 1, 1, '2024-03-13 17:30:55', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (150, 1, 1, '2024-03-13 17:30:58', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (151, 1, 1, '2024-03-13 17:31:48', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (152, 1, 1, '2024-03-13 17:41:37', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (153, 1, 1, '2024-03-13 17:43:46', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (154, 1, 1, '2024-03-13 17:46:27', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (155, 1, 1, '2024-03-13 17:48:40', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (156, 1, 1, '2024-03-13 17:56:21', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (157, 1, 1, '2024-03-13 18:06:17', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (158, 1, 1, '2024-03-13 18:06:21', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (159, 1, 1, '2024-03-13 18:06:22', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (160, 1, 1, '2024-03-13 18:06:23', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (161, 1, 1, '2024-03-13 18:06:52', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (162, 1, 1, '2024-03-13 18:07:20', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (163, 1, 1, '2024-03-13 18:07:26', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (164, 1, 1, '2024-03-13 18:07:38', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (165, 1, 1, '2024-03-13 18:09:40', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (166, 1, 1, '2024-03-13 18:17:29', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (167, 1, 1, '2024-03-13 18:17:57', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (168, 1, 1, '2024-03-13 18:18:13', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (169, 1, 1, '2024-03-13 18:19:17', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (170, 1, 1, '2024-03-13 18:21:23', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (171, 1, 1, '2024-03-13 18:21:30', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (172, 1, 1, '2024-03-13 18:25:57', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (173, 1, 1, '2024-03-13 18:26:55', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (174, 1, 1, '2024-03-13 18:35:07', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (175, 1, 1, '2024-03-13 18:35:22', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (176, 1, 1, '2024-03-13 18:36:59', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (177, 1, 1, '2024-03-13 18:46:10', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (178, 1, 1, '2024-03-13 18:46:56', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (179, 1, 1, '2024-03-13 19:28:19', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (180, 1, 1, '2024-03-13 19:29:58', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (181, 1, 1, '2024-03-13 19:31:11', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (182, 1, 1, '2024-03-13 19:33:23', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (183, 1, 1, '2024-03-13 19:33:55', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (184, 1, 1, '2024-03-13 19:34:17', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (185, 1, 1, '2024-03-13 19:34:57', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (186, 1, 1, '2024-03-13 19:36:21', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (187, 1, 1, '2024-03-13 19:38:22', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (188, 1, 1, '2024-03-13 19:38:41', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (189, 1, 1, '2024-03-13 19:41:50', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (190, 1, 1, '2024-03-13 19:48:32', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (191, 1, 1, '2024-03-13 19:49:10', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (192, 1, 1, '2024-03-13 19:49:27', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (193, 1, 1, '2024-03-13 20:04:27', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (194, 1, 1, '2024-03-13 20:17:55', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (195, 1, 1, '2024-03-13 20:18:11', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (196, 1, 1, '2024-03-13 20:21:10', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (197, 1, 1, '2024-03-13 20:21:49', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (198, 1, 1, '2024-03-13 20:25:39', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (199, 1, 1, '2024-03-13 20:25:47', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (200, 1, 1, '2024-03-13 20:25:51', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (201, 1, 1, '2024-03-13 20:25:57', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (202, 1, 1, '2024-03-13 20:26:01', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (203, 1, 1, '2024-03-13 20:27:55', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (204, 1, 1, '2024-03-13 20:28:06', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (205, 1, 1, '2024-03-13 20:28:15', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (206, 1, 1, '2024-03-13 20:31:20', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (207, 1, 1, '2024-03-13 20:31:41', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (208, 1, 1, '2024-03-13 20:36:52', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (209, 1, 1, '2024-03-13 20:38:12', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (210, 1, 1, '2024-03-13 20:38:28', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (211, 1, 1, '2024-03-13 20:38:50', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (212, 1, 1, '2024-03-13 20:43:05', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (213, 1, 1, '2024-03-13 20:44:57', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (214, 1, 1, '2024-03-13 20:45:23', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (215, 1, 1, '2024-03-13 20:46:29', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (216, 1, 1, '2024-03-13 20:49:38', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (217, 1, 1, '2024-03-13 20:50:45', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (218, 1, 1, '2024-03-13 20:52:48', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (219, 1, 1, '2024-03-13 21:00:47', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (220, 1, 1, '2024-03-13 21:06:29', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (221, 1, 1, '2024-03-13 21:08:28', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (222, 1, 1, '2024-03-13 21:11:02', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (223, 1, 1, '2024-03-13 21:15:31', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (224, 1, 1, '2024-03-13 21:17:33', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (225, 1, 1, '2024-03-13 21:18:23', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (226, 1, 1, '2024-03-13 21:20:02', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (227, 1, 1, '2024-03-13 21:23:27', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (228, 1, 1, '2024-03-13 21:27:48', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (229, 1, 1, '2024-03-13 21:35:33', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (230, 1, 1, '2024-03-13 21:41:25', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (231, 1, 1, '2024-03-13 21:42:26', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (232, 1, 1, '2024-03-13 21:42:55', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (233, 1, 1, '2024-03-13 21:44:48', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (234, 1, 1, '2024-03-13 21:47:10', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (235, 1, 1, '2024-03-13 21:50:37', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (236, 1, 1, '2024-03-13 21:51:55', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (237, 1, 1, '2024-03-13 21:52:53', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (238, 1, 1, '2024-03-13 21:55:52', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (239, 1, 1, '2024-03-13 22:00:21', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (240, 1, 1, '2024-03-13 22:05:47', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (241, 1, 1, '2024-03-13 22:11:28', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (242, 1, 1, '2024-03-13 22:12:34', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (243, 1, 1, '2024-03-13 22:14:47', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (244, 1, 1, '2024-03-13 22:16:38', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (245, 1, 1, '2024-03-13 22:18:23', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (246, 1, 1, '2024-03-13 22:19:28', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (247, 1, 1, '2024-03-13 22:20:54', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (248, 1, 1, '2024-03-13 22:25:00', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (249, 1, 1, '2024-03-13 22:27:33', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (250, 1, 1, '2024-03-13 22:32:26', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (251, 1, 1, '2024-03-13 22:36:41', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (252, 1, 1, '2024-03-13 22:40:17', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (253, 1, 1, '2024-03-13 22:45:50', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (254, 1, 1, '2024-03-13 22:48:09', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (255, 1, 1, '2024-03-13 22:52:18', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (256, 1, 1, '2024-03-13 22:52:44', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (257, 1, 1, '2024-03-13 22:53:38', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (258, 1, 1, '2024-03-13 22:55:00', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (259, 1, 1, '2024-03-13 22:56:18', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (260, 1, 1, '2024-03-13 23:01:06', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (261, 1, 1, '2024-03-13 23:02:23', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (262, 1, 1, '2024-03-13 23:05:48', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (263, 1, 1, '2024-03-13 23:09:38', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (264, 1, 1, '2024-03-13 23:10:19', '0:0:0:0:0:0:0:1');
INSERT INTO `ta_aut_history` VALUES (265, 1, 1, '2024-03-13 23:14:09', '0:0:0:0:0:0:0:1');

-- ----------------------------
-- Table structure for ta_aut_right
-- ----------------------------
DROP TABLE IF EXISTS `ta_aut_right`;
CREATE TABLE `ta_aut_right`  (
  `I_ID` int(11) NOT NULL COMMENT 'cac quyen co the: xem, xoa, sua, them moi, in an, xem bao cao...',
  `T_Name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `T_Info_01` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'description',
  `T_Info_02` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'group',
  PRIMARY KEY (`I_ID`) USING BTREE,
  UNIQUE INDEX `idx_TARIG_01`(`T_Name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_aut_right
-- ----------------------------
INSERT INTO `ta_aut_right` VALUES (100, 'R_AUT_ADMIN', 'Administration du Système', 'aut_adm');
INSERT INTO `ta_aut_right` VALUES (1000001, 'aut_right_aut_user_g_001', 'Xem thông tin người dùng', 'aut_right_aut_user');
INSERT INTO `ta_aut_right` VALUES (1000002, 'aut_right_aut_user_n_002', 'Tạo thông tin người dùng', 'aut_right_aut_user');
INSERT INTO `ta_aut_right` VALUES (1000003, 'aut_right_aut_user_m_003', 'Sửa thông tin người dùng', 'aut_right_aut_user');
INSERT INTO `ta_aut_right` VALUES (1000004, 'aut_right_aut_user_d_004', 'Xóa thông tin người dùng', 'aut_right_aut_user');
INSERT INTO `ta_aut_right` VALUES (1000005, 'aut_right_aut_user_r_005', 'Tổng kết báo cáo trên người dùng', 'aut_right_aut_user');
INSERT INTO `ta_aut_right` VALUES (2000001, 'aut_right_aut_position_g_001', 'Xem thông tin vị trí công việc', 'aut_right_aut_position');
INSERT INTO `ta_aut_right` VALUES (2000002, 'aut_right_aut_position_n_002', 'Tạo thông tin vị trí công việc', 'aut_right_aut_position');
INSERT INTO `ta_aut_right` VALUES (2000003, 'aut_right_aut_position_m_003', 'Sửa thông tin vị trí công việc', 'aut_right_aut_position');
INSERT INTO `ta_aut_right` VALUES (2000004, 'aut_right_aut_position_d_004', 'Xóa thông tin vị trí công việc', 'aut_right_aut_position');
INSERT INTO `ta_aut_right` VALUES (2000005, 'aut_right_aut_position_r_005', 'Tổng kết báo cáo trên vị trí công việc', 'aut_right_aut_position');
INSERT INTO `ta_aut_right` VALUES (2001001, 'aut_right_job_holiday_g_001', 'Xem thông tin ngày nghỉ lễ', 'aut_right_job_holiday');
INSERT INTO `ta_aut_right` VALUES (2001002, 'aut_right_job_holiday_n_002', 'Tạo thông tin ngày nghỉ lễ', 'aut_right_job_holiday');
INSERT INTO `ta_aut_right` VALUES (2001003, 'aut_right_job_holiday_m_003', 'Sửa thông tin ngày nghỉ lễ', 'aut_right_job_holiday');
INSERT INTO `ta_aut_right` VALUES (2001004, 'aut_right_job_holiday_d_004', 'Xóa thông tin ngày nghỉ lễ', 'aut_right_job_holiday');
INSERT INTO `ta_aut_right` VALUES (2001005, 'aut_right_job_holiday_r_005', 'Tổng kết báo cáo trên ngày nghỉ lễ', 'aut_right_job_holiday');
INSERT INTO `ta_aut_right` VALUES (2002001, 'aut_right_job_report_g_001', 'Xem thông tin chấm công', 'aut_right_job_report');
INSERT INTO `ta_aut_right` VALUES (2002002, 'aut_right_job_report_n_002', 'Tạo thông tin chấm công', 'aut_right_job_report');
INSERT INTO `ta_aut_right` VALUES (2002003, 'aut_right_job_report_m_003', 'Sửa thông tin chấm công', 'aut_right_job_report');
INSERT INTO `ta_aut_right` VALUES (2002004, 'aut_right_job_report_d_004', 'Xóa thông tin chấm công', 'aut_right_job_report');
INSERT INTO `ta_aut_right` VALUES (2002005, 'aut_right_job_report_r_005', 'Tổng kết báo cáo trên chấm công', 'aut_right_job_report');
INSERT INTO `ta_aut_right` VALUES (2002011, 'aut_right_job_report_adm_g', 'Xem danh sách chấm công nhân viên', 'aut_right_job_report_adm');
INSERT INTO `ta_aut_right` VALUES (2002012, 'aut_right_job_report_adm_n', 'Tạo cra cho nhân viên', 'aut_right_job_report_adm');
INSERT INTO `ta_aut_right` VALUES (2002013, 'aut_right_job_report_adm_m', 'Chỉnh sửa/Xác nhận/Từ chối cra', 'aut_right_job_report_adm');
INSERT INTO `ta_aut_right` VALUES (2002014, 'aut_right_job_report_adm_d', 'Xóa cra của nhân viên', 'aut_right_job_report_adm');
INSERT INTO `ta_aut_right` VALUES (2010001, 'aut_right_job_ctrl_g_001', 'Xem thông tin điểm kiểm xoát', 'aut_right_job_ctrl');
INSERT INTO `ta_aut_right` VALUES (2010002, 'aut_right_job_ctrl_n_002', 'Tạo thông tin điểm kiểm xoát', 'aut_right_job_ctrl');
INSERT INTO `ta_aut_right` VALUES (2010003, 'aut_right_job_ctrl_m_003', 'Sửa thông tin điểm kiểm xoát', 'aut_right_job_ctrl');
INSERT INTO `ta_aut_right` VALUES (2010004, 'aut_right_job_ctrl_d_004', 'Xóa thông tin điểm kiểm xoát', 'aut_right_job_ctrl');
INSERT INTO `ta_aut_right` VALUES (2010005, 'aut_right_job_ctrl_r_005', 'Tổng kết báo cáo trên điểm kiểm xoát', 'aut_right_job_ctrl');
INSERT INTO `ta_aut_right` VALUES (2020001, 'aut_right_job_off_g_001', 'Xem thông tin nghỉ phép', 'aut_right_job_off');
INSERT INTO `ta_aut_right` VALUES (2020002, 'aut_right_job_off_n_002', 'Tạo thông tin nghỉ phép', 'aut_right_job_off');
INSERT INTO `ta_aut_right` VALUES (2020003, 'aut_right_job_off_m_003', 'Sửa thông tin nghỉ phép', 'aut_right_job_off');
INSERT INTO `ta_aut_right` VALUES (2020004, 'aut_right_job_off_d_004', 'Xóa thông tin nghỉ phép', 'aut_right_job_off');
INSERT INTO `ta_aut_right` VALUES (2020005, 'aut_right_job_off_r_005', 'Tổng kết báo cáo trên nghỉ phép', 'aut_right_job_off');
INSERT INTO `ta_aut_right` VALUES (4000001, 'aut_right_aut_role_g_001', 'Xem vai trò', 'aut_right_aut_role');
INSERT INTO `ta_aut_right` VALUES (4000002, 'aut_right_aut_role_n_002', 'Tạo vai trò', 'aut_right_aut_role');
INSERT INTO `ta_aut_right` VALUES (4000003, 'aut_right_aut_role_m_003', 'Chỉnh sửa vai trò', 'aut_right_aut_role');
INSERT INTO `ta_aut_right` VALUES (4000004, 'aut_right_aut_role_d_004', 'Xóa vai trò', 'aut_right_aut_role');
INSERT INTO `ta_aut_right` VALUES (5000001, 'aut_right_sys_report_g_001', 'Xem báo cáo', 'aut_right_sys_report');
INSERT INTO `ta_aut_right` VALUES (5000002, 'aut_right_sys_report_n_002', 'Tạo báo cáo', 'aut_right_sys_report');
INSERT INTO `ta_aut_right` VALUES (5000003, 'aut_right_sys_report_m_003', 'Sửa báo cáo', 'aut_right_sys_report');
INSERT INTO `ta_aut_right` VALUES (5000004, 'aut_right_sys_report_d_004', 'Xóa báo cáo', 'aut_right_sys_report');
INSERT INTO `ta_aut_right` VALUES (5000005, 'aut_right_sys_report_r_005', 'Tổng kết báo cáo', 'aut_right_sys_report');
INSERT INTO `ta_aut_right` VALUES (7000001, 'aut_right_tpy_category_g_001', 'Xem thông tin nhóm định nghĩa', 'aut_right_tpy_category');
INSERT INTO `ta_aut_right` VALUES (7000002, 'aut_right_tpy_category_n_002', 'Tạo thông tin nhóm định nghĩa', 'aut_right_tpy_category');
INSERT INTO `ta_aut_right` VALUES (7000003, 'aut_right_tpy_category_m_003', 'Sửa thông tin nhóm định nghĩa', 'aut_right_tpy_category');
INSERT INTO `ta_aut_right` VALUES (7000004, 'aut_right_tpy_category_d_004', 'Xóa thông tin nhóm định nghĩa', 'aut_right_tpy_category');
INSERT INTO `ta_aut_right` VALUES (7000005, 'aut_right_tpy_category_r_005', 'Tổng kết trên thông tin nhóm định nghĩa', 'aut_right_tpy_category');
INSERT INTO `ta_aut_right` VALUES (8000001, 'aut_right_cfg_group_g_001', 'Xem cấu hình hệ thống', 'aut_right_cfg_group');
INSERT INTO `ta_aut_right` VALUES (8000002, 'aut_right_cfg_group_n_002', 'Tạo cấu hình hệ thống', 'aut_right_cfg_group');
INSERT INTO `ta_aut_right` VALUES (8000003, 'aut_right_cfg_group_m_003', 'Sửa cấu hình hệ thống', 'aut_right_cfg_group');
INSERT INTO `ta_aut_right` VALUES (8000004, 'aut_right_cfg_group_d_004', 'Xóa cấu hình hệ thống', 'aut_right_cfg_group');
INSERT INTO `ta_aut_right` VALUES (8000005, 'aut_right_cfg_group_r_005', 'Tổng kết trên cấu hình hệ thống', 'aut_right_cfg_group');
INSERT INTO `ta_aut_right` VALUES (10000001, 'aut_right_inv_invoice_i_g_001', 'Xem thông tin hóa đơn nhập', 'aut_right_inv_invoice_in');
INSERT INTO `ta_aut_right` VALUES (10000002, 'aut_right_inv_invoice_i_n_002', 'Tạo thông tin hóa đơn nhập', 'aut_right_inv_invoice_in');
INSERT INTO `ta_aut_right` VALUES (10000003, 'aut_right_inv_invoice_i_m_003', 'Sửa thông tin hóa đơn nhập', 'aut_right_inv_invoice_in');
INSERT INTO `ta_aut_right` VALUES (10000004, 'aut_right_inv_invoice_i_d_004', 'Xóa thông tin hóa đơn nhập', 'aut_right_inv_invoice_in');
INSERT INTO `ta_aut_right` VALUES (10000005, 'aut_right_inv_invoice_i_r_005', 'Tổng kết báo cáo hóa đơn nhập', 'aut_right_inv_invoice_in');
INSERT INTO `ta_aut_right` VALUES (10000006, 'aut_right_inv_invoice_i_v_006', 'Kiểm tra xác nhận hóa đơn mua hàng', 'aut_right_inv_invoice_in');
INSERT INTO `ta_aut_right` VALUES (10000011, 'aut_right_inv_invoice_o_g_011', 'Xem thông tin hóa đơn xuất', 'aut_right_inv_invoice_out');
INSERT INTO `ta_aut_right` VALUES (10000012, 'aut_right_inv_invoice_o_n_012', 'Tạo thông tin hóa đơn xuất', 'aut_right_inv_invoice_out');
INSERT INTO `ta_aut_right` VALUES (10000013, 'aut_right_inv_invoice_o_m_013', 'Sửa thông tin hóa đơn xuất', 'aut_right_inv_invoice_out');
INSERT INTO `ta_aut_right` VALUES (10000014, 'aut_right_inv_invoice_o_d_014', 'Xóa thông tin hóa đơn xuất', 'aut_right_inv_invoice_out');
INSERT INTO `ta_aut_right` VALUES (10000015, 'aut_right_inv_invoice_o_r_015', 'Tổng kết báo cáo hóa đơn xuất', 'aut_right_inv_invoice_out');
INSERT INTO `ta_aut_right` VALUES (10000016, 'aut_right_inv_invoice_o_v_016', 'Kiểm tra, xác nhận và phát gửi hóa đơn bán hàng', 'aut_right_inv_invoice_out');
INSERT INTO `ta_aut_right` VALUES (10010001, 'aut_right_inv_accounting_g_001', 'Xem thông tin kế toán', 'aut_right_inv_accounting');
INSERT INTO `ta_aut_right` VALUES (10010002, 'aut_right_inv_accounting_n_002', 'Tạo thông tin kế toán', 'aut_right_inv_accounting');
INSERT INTO `ta_aut_right` VALUES (10010003, 'aut_right_inv_accounting_m_003', 'Sửa thông tin kế toán', 'aut_right_inv_accounting');
INSERT INTO `ta_aut_right` VALUES (10010004, 'aut_right_inv_accounting_d_004', 'Xóa thông tin kế toán', 'aut_right_inv_accounting');
INSERT INTO `ta_aut_right` VALUES (10010005, 'aut_right_inv_accounting_r_005', 'Tổng kết báo cáo kế toán', 'aut_right_inv_accounting');
INSERT INTO `ta_aut_right` VALUES (10020001, 'aut_right_inv_tax_g_001', 'Xem thông tin hệ thống thuế', 'aut_right_inv_tax');
INSERT INTO `ta_aut_right` VALUES (10020002, 'aut_right_inv_tax_n_002', 'Tạo thông tin thuế', 'aut_right_inv_tax');
INSERT INTO `ta_aut_right` VALUES (10020003, 'aut_right_inv_tax_m_003', 'Sửa thông tin thuế', 'aut_right_inv_tax');
INSERT INTO `ta_aut_right` VALUES (10020004, 'aut_right_inv_tax_d_004', 'Xóa thông tin thuế', 'aut_right_inv_tax');
INSERT INTO `ta_aut_right` VALUES (10020005, 'aut_right_inv_tax_r_005', 'Tổng kết báo cáo trên thuế', 'aut_right_inv_tax');
INSERT INTO `ta_aut_right` VALUES (10100001, 'aut_right_inv_balance_g_001', 'Xem thu chi', 'aut_right_inv_balance');
INSERT INTO `ta_aut_right` VALUES (10100002, 'aut_right_inv_balance_n_002', 'Tạo thông tin thu chi', 'aut_right_inv_balance');
INSERT INTO `ta_aut_right` VALUES (10100003, 'aut_right_inv_balance_m_003', 'Chỉnh sửa thông tin thu chi', 'aut_right_inv_balance');
INSERT INTO `ta_aut_right` VALUES (10100004, 'aut_right_inv_balance_d_004', 'Xóa thông tin thu chi', 'aut_right_inv_balance');
INSERT INTO `ta_aut_right` VALUES (10100005, 'aut_right_inv_balance_r_005', 'Tổng kết báo cáo thu chi', 'aut_right_inv_balance');
INSERT INTO `ta_aut_right` VALUES (10100006, 'aut_right_inv_balance_o_r_006', 'Tổng hợp hóa đơn nhập để tạo chi', 'aut_right_inv_balance');
INSERT INTO `ta_aut_right` VALUES (10100007, 'aut_right_inv_balance_r_i_007', 'Tổng hợp hóa đơn xuât để tạo thu', 'aut_right_inv_balance');
INSERT INTO `ta_aut_right` VALUES (11000001, 'aut_right_sor_order_i_g_001', 'Xem các đơn hàng đến', 'aut_right_sor_order_in');
INSERT INTO `ta_aut_right` VALUES (11000002, 'aut_right_sor_order_i_n_002', 'Tạo đơn hàng đến', 'aut_right_sor_order_in');
INSERT INTO `ta_aut_right` VALUES (11000003, 'aut_right_sor_order_i_m_003', 'Sửa đơn bán đến', 'aut_right_sor_order_in');
INSERT INTO `ta_aut_right` VALUES (11000004, 'aut_right_sor_order_i_d_004', 'Xóa đơn hàng đến', 'aut_right_sor_order_in');
INSERT INTO `ta_aut_right` VALUES (11000005, 'aut_right_sor_order_i_r_005', 'Tổng kết các đơn hàng đến', 'aut_right_sor_order_in');
INSERT INTO `ta_aut_right` VALUES (11000010, 'aut_right_sor_order_o_s_010', 'Quyền bán hàng', 'aut_right_sor_order_out');
INSERT INTO `ta_aut_right` VALUES (11000011, 'aut_right_sor_order_o_g_011', 'Xem các đơn bán hàng', 'aut_right_sor_order_out');
INSERT INTO `ta_aut_right` VALUES (11000012, 'aut_right_sor_order_o_n_012', 'Tạo đơn bán hàng', 'aut_right_sor_order_out');
INSERT INTO `ta_aut_right` VALUES (11000013, 'aut_right_sor_order_o_m_013', 'Sửa đơn bán hàng', 'aut_right_sor_order_out');
INSERT INTO `ta_aut_right` VALUES (11000014, 'aut_right_sor_order_o_d_014', 'Xóa đơn bán hàng', 'aut_right_sor_order_out');
INSERT INTO `ta_aut_right` VALUES (11000015, 'aut_right_sor_order_o_r_015', 'Tổng kết các đơn bán hàng', 'aut_right_sor_order_out');
INSERT INTO `ta_aut_right` VALUES (11010001, 'aut_right_sor_deal_g_001', 'Xem khuyến mãi', 'aut_right_sor_deal');
INSERT INTO `ta_aut_right` VALUES (11010002, 'aut_right_sor_deal_n_002', 'Tạo khuyến mãi', 'aut_right_sor_deal');
INSERT INTO `ta_aut_right` VALUES (11010003, 'aut_right_sor_deal_m_003', 'Sửa khuyến mãi', 'aut_right_sor_deal');
INSERT INTO `ta_aut_right` VALUES (11010004, 'aut_right_sor_deal_d_004', 'Xóa khuyến mãi', 'aut_right_sor_deal');
INSERT INTO `ta_aut_right` VALUES (11010005, 'aut_right_sor_deal_r_005', 'Tổng kết khuyến mãi', 'aut_right_sor_deal');
INSERT INTO `ta_aut_right` VALUES (11020001, 'aut_right_sor_pricing_g_001', 'Xem đơn giá bán', 'aut_right_sor_pricing');
INSERT INTO `ta_aut_right` VALUES (11020002, 'aut_right_sor_pricing_n_002', 'Tạo đơn giá bán', 'aut_right_sor_pricing');
INSERT INTO `ta_aut_right` VALUES (11020003, 'aut_right_sor_pricing_m_003', 'Sửa đơn giá bán', 'aut_right_sor_pricing');
INSERT INTO `ta_aut_right` VALUES (11020004, 'aut_right_sor_pricing_d_004', 'Xóa đơn giá bán', 'aut_right_sor_pricing');
INSERT INTO `ta_aut_right` VALUES (11020005, 'aut_right_sor_pricing_r_005', 'Tổng kết báo cáo trên giá bán', 'aut_right_sor_pricing');
INSERT INTO `ta_aut_right` VALUES (14000001, 'aut_right_nso_area_g_001', 'Xem thông tin địa danh', 'aut_right_nso_area');
INSERT INTO `ta_aut_right` VALUES (14000002, 'aut_right_nso_area_n_002', 'Tạo thông tin địa danh', 'aut_right_nso_area');
INSERT INTO `ta_aut_right` VALUES (14000003, 'aut_right_nso_area_m_003', 'Sửa thông tin địa danh', 'aut_right_nso_area');
INSERT INTO `ta_aut_right` VALUES (14000004, 'aut_right_nso_area_d_004', 'Xóa thông tin địa danh', 'aut_right_nso_area');
INSERT INTO `ta_aut_right` VALUES (14000005, 'aut_right_nso_area_r_005', 'Tổng kết báo cáo trên địa danh', 'aut_right_nso_area');
INSERT INTO `ta_aut_right` VALUES (15000001, 'aut_right_nso_offer_g_001', 'Xem thông tin ưu đãi', 'aut_right_nso_offer');
INSERT INTO `ta_aut_right` VALUES (15000002, 'aut_right_nso_offer_n_002', 'Tạo thông tin ưu đãi', 'aut_right_nso_offer');
INSERT INTO `ta_aut_right` VALUES (15000003, 'aut_right_nso_offer_m_003', 'Sửa thông tin ưu đãi', 'aut_right_nso_offer');
INSERT INTO `ta_aut_right` VALUES (15000004, 'aut_right_nso_offer_d_004', 'Xóa thông tin ưu đãi', 'aut_right_nso_offer');
INSERT INTO `ta_aut_right` VALUES (15000005, 'aut_right_nso_offer_r_005', 'Tổng kết báo cáo trên ưu đãi', 'aut_right_nso_offer');
INSERT INTO `ta_aut_right` VALUES (16000001, 'aut_right_nso_plan_g_001', 'Xem thông tin lịch trình', 'aut_right_nso_plan');
INSERT INTO `ta_aut_right` VALUES (16000002, 'aut_right_nso_plan_n_002', 'Tạo thông tin lịch trình', 'aut_right_nso_plan');
INSERT INTO `ta_aut_right` VALUES (16000003, 'aut_right_nso_plan_m_003', 'Sửa thông tin lịch trình', 'aut_right_nso_plan');
INSERT INTO `ta_aut_right` VALUES (16000004, 'aut_right_nso_plan_d_004', 'Xóa thông tin lịch trình', 'aut_right_nso_plan');
INSERT INTO `ta_aut_right` VALUES (16000005, 'aut_right_nso_plan_r_005', 'Tổng kết báo cáo trên lịch trình', 'aut_right_nso_plan');
INSERT INTO `ta_aut_right` VALUES (17000001, 'aut_right_nso_post_g_001', 'Xem thông tin phản hồi', 'aut_right_nso_post');
INSERT INTO `ta_aut_right` VALUES (17000002, 'aut_right_nso_post_n_002', 'Tạo thông tin phản hồi', 'aut_right_nso_post');
INSERT INTO `ta_aut_right` VALUES (17000003, 'aut_right_nso_post_m_003', 'Sửa thông tin phản hồi', 'aut_right_nso_post');
INSERT INTO `ta_aut_right` VALUES (17000004, 'aut_right_nso_post_d_004', 'Xóa thông tin phản hồi', 'aut_right_nso_post');
INSERT INTO `ta_aut_right` VALUES (17000005, 'aut_right_nso_post_r_005', 'Tổng kết báo cáo trên phản hồi', 'aut_right_nso_post');
INSERT INTO `ta_aut_right` VALUES (20000001, 'aut_right_mat_material_g_001', 'Xem thông tin sản phẩm', 'aut_right_mat_material');
INSERT INTO `ta_aut_right` VALUES (20000002, 'aut_right_mat_material_n_002', 'Tạo thông tin sản phẩm', 'aut_right_mat_material');
INSERT INTO `ta_aut_right` VALUES (20000003, 'aut_right_mat_material_m_003', 'Sửa thông tin sản phẩm', 'aut_right_mat_material');
INSERT INTO `ta_aut_right` VALUES (20000004, 'aut_right_mat_material_d_004', 'Xóa thông tin sản phẩm', 'aut_right_mat_material');
INSERT INTO `ta_aut_right` VALUES (20000005, 'aut_right_mat_material_r_005', 'Tổng kết trên sản phẩm', 'aut_right_mat_material');
INSERT INTO `ta_aut_right` VALUES (20100001, 'aut_right_mat_warehouse_g_001', 'Xem thông tin nhà kho/trụ sở', 'aut_right_mat_warehouse');
INSERT INTO `ta_aut_right` VALUES (20100002, 'aut_right_mat_warehouse_n_002', 'Tạo thông tin nhà kho/trụ sở', 'aut_right_mat_warehouse');
INSERT INTO `ta_aut_right` VALUES (20100003, 'aut_right_mat_warehouse_m_003', 'Chỉnh sửa thông tin nhà kho/trụ sở', 'aut_right_mat_warehouse');
INSERT INTO `ta_aut_right` VALUES (20100004, 'aut_right_mat_warehouse_d_004', 'Xóa thông tin nhà kho/trụ sở', 'aut_right_mat_warehouse');
INSERT INTO `ta_aut_right` VALUES (20300001, 'aut_right_mat_stock_io_i_g_001', 'Xem phiếu nhập kho, G: get', 'aut_right_mat_stock_in_order');
INSERT INTO `ta_aut_right` VALUES (20300002, 'aut_right_mat_stock_io_i_n_002', 'Tạo phiếu nhập kho, N: new', 'aut_right_mat_stock_in_order');
INSERT INTO `ta_aut_right` VALUES (20300003, 'aut_right_mat_stock_io_i_m_003', 'Chỉnh sửa phiếu nhập kho, M: mod', 'aut_right_mat_stock_in_order');
INSERT INTO `ta_aut_right` VALUES (20300004, 'aut_right_mat_stock_io_i_d_004', 'Xóa phiếu nhập kho, D: del', 'aut_right_mat_stock_in_order');
INSERT INTO `ta_aut_right` VALUES (20300005, 'aut_right_mat_stock_io_i_r_005', 'Tổng kết phiếu nhập kho, R: report', 'aut_right_mat_stock_in_order');
INSERT INTO `ta_aut_right` VALUES (20300011, 'aut_right_mat_stock_io_o_g_011', 'Xem phiếu xuất kho, G: get', 'aut_right_mat_stock_out_order');
INSERT INTO `ta_aut_right` VALUES (20300012, 'aut_right_mat_stock_io_o_n_012', 'Tạo phiếu xuất kho, N: new', 'aut_right_mat_stock_out_order');
INSERT INTO `ta_aut_right` VALUES (20300013, 'aut_right_mat_stock_io_o_m_013', 'Chỉnh sửa phiếu xuất kho, M: mod', 'aut_right_mat_stock_out_order');
INSERT INTO `ta_aut_right` VALUES (20300014, 'aut_right_mat_stock_io_o_d_014', 'Xóa phiếu xuất kho, D: del', 'aut_right_mat_stock_out_order');
INSERT INTO `ta_aut_right` VALUES (20300015, 'aut_right_mat_stock_io_o_r_015', 'Tổng kết phiếu xuất kho, R: report', 'aut_right_mat_stock_out_order');
INSERT INTO `ta_aut_right` VALUES (20400001, 'aut_right_mat_unit_g_001', 'Xem thông tin đơn vị xuất nhập', 'aut_right_mat_unit');
INSERT INTO `ta_aut_right` VALUES (20400002, 'aut_right_mat_unit_n_002', 'Tạo thông tin đơn vị xuất nhập', 'aut_right_mat_unit');
INSERT INTO `ta_aut_right` VALUES (20400003, 'aut_right_mat_unit_m_003', 'Sửa thông tin đơn vị xuất nhập', 'aut_right_mat_unit');
INSERT INTO `ta_aut_right` VALUES (20400004, 'aut_right_mat_unit_d_004', 'Xóa thông tin đơn vị xuất nhập', 'aut_right_mat_unit');
INSERT INTO `ta_aut_right` VALUES (20400005, 'aut_right_mat_unit_r_005', 'Tổng kết trên đơn vị xuất nhập', 'aut_right_mat_unit');
INSERT INTO `ta_aut_right` VALUES (20500001, 'aut_right_mat_pr_pricing_g_001', 'Xem thông tin báo giá', 'aut_right_mat_provider_pricing');
INSERT INTO `ta_aut_right` VALUES (20500002, 'aut_right_mat_pr_pricing_n_002', 'Tạo thông tin báo giá', 'aut_right_mat_provider_pricing');
INSERT INTO `ta_aut_right` VALUES (20500003, 'aut_right_mat_pr_pricing_m_003', 'Sửa thông tin báo giá', 'aut_right_mat_provider_pricing');
INSERT INTO `ta_aut_right` VALUES (20500004, 'aut_right_mat_pr_pricing_d_004', 'Xóa thông tin báo giá', 'aut_right_mat_provider_pricing');
INSERT INTO `ta_aut_right` VALUES (20500005, 'aut_right_mat_pr_pricing_r_005', 'Tổng kết trên báo giá', 'aut_right_mat_provider_pricing');
INSERT INTO `ta_aut_right` VALUES (30000001, 'aut_right_per_self_g_001', 'Xem thông tin nội bộ', 'aut_right_per_self');
INSERT INTO `ta_aut_right` VALUES (30000002, 'aut_right_per_self_n_002', 'Tạo thông tin nội bộ', 'aut_right_per_self');
INSERT INTO `ta_aut_right` VALUES (30000003, 'aut_right_per_self_m_003', 'Sửa thông tin nội bộ', 'aut_right_per_self');
INSERT INTO `ta_aut_right` VALUES (30000004, 'aut_right_per_self_d_004', 'Xóa thông tin nội bộ', 'aut_right_per_self');
INSERT INTO `ta_aut_right` VALUES (30000005, 'aut_right_per_self_r_005', 'Tổng kết báo cáo trong nội bộ', 'aut_right_per_self');
INSERT INTO `ta_aut_right` VALUES (30000011, 'aut_right_per_client_g_011', 'Xem thông tin khách hàng', 'aut_right_per_client');
INSERT INTO `ta_aut_right` VALUES (30000012, 'aut_right_per_client_n_012', 'Tạo thông tin khách hàng', 'aut_right_per_client');
INSERT INTO `ta_aut_right` VALUES (30000013, 'aut_right_per_client_m_013', 'Sửa thông tin khách hàng', 'aut_right_per_client');
INSERT INTO `ta_aut_right` VALUES (30000014, 'aut_right_per_client_d_014', 'Xóa thông tin khách hàng', 'aut_right_per_client');
INSERT INTO `ta_aut_right` VALUES (30000015, 'aut_right_per_client_r_015', 'Tổng kết báo cáo trên khách hàng', 'aut_right_per_client');
INSERT INTO `ta_aut_right` VALUES (30000021, 'aut_right_per_supplier_g_021', 'Xem  thông tin nhà cung ứng', 'aut_right_per_supplier');
INSERT INTO `ta_aut_right` VALUES (30000022, 'aut_right_per_supplier_n_022', 'Tạo thông tin nhà cung ứng', 'aut_right_per_supplier');
INSERT INTO `ta_aut_right` VALUES (30000023, 'aut_right_per_supplier_m_023', 'Sửa thông tin nhà cung ứng', 'aut_right_per_supplier');
INSERT INTO `ta_aut_right` VALUES (30000024, 'aut_right_per_supplier_d_024', 'Xóa thông tin nhà cung ứng', 'aut_right_per_supplier');
INSERT INTO `ta_aut_right` VALUES (30000025, 'aut_right_per_supplier_r_025', 'Tổng kết báo cáo trên nhà cung ứng', 'aut_right_per_supplier');
INSERT INTO `ta_aut_right` VALUES (30000031, 'aut_right_per_producer_g_031', 'Xem thông tin nhà sản xuất', 'aut_right_per_producer');
INSERT INTO `ta_aut_right` VALUES (30000032, 'aut_right_per_producer_n_032', 'Tạo thông tin nhà sản xuất', 'aut_right_per_producer');
INSERT INTO `ta_aut_right` VALUES (30000033, 'aut_right_per_producer_m_033', 'Sửa thông tin nhà sản xuất', 'aut_right_per_producer');
INSERT INTO `ta_aut_right` VALUES (30000034, 'aut_right_per_producer_d_034', 'Xóa thông tin nhà sản xuất', 'aut_right_per_producer');
INSERT INTO `ta_aut_right` VALUES (30000035, 'aut_right_per_producer_r_035', 'Tổng kết báo cáo trên nhà sản xuất', 'aut_right_per_producer');
INSERT INTO `ta_aut_right` VALUES (30000041, 'aut_right_per_thirdparty_g_041', 'Xem thông tin đối tác', 'aut_right_per_thirdparty');
INSERT INTO `ta_aut_right` VALUES (30000042, 'aut_right_per_thirdparty_n_042', 'Tạo thông tin đối tác', 'aut_right_per_thirdparty');
INSERT INTO `ta_aut_right` VALUES (30000043, 'aut_right_per_thirdparty_m_043', 'Sửa thông tin đối tác', 'aut_right_per_thirdparty');
INSERT INTO `ta_aut_right` VALUES (30000044, 'aut_right_per_thirdparty_d_044', 'Xóa thông tin đối tác', 'aut_right_per_thirdparty');
INSERT INTO `ta_aut_right` VALUES (30000045, 'aut_right_per_thirdparty_r_045', 'Tổng kết báo cáo trên đối tác', 'aut_right_per_thirdparty');
INSERT INTO `ta_aut_right` VALUES (30001001, 'aut_right_per_contract_g_001', 'Xem thông tin hợp đồng', 'aut_right_per_contract');
INSERT INTO `ta_aut_right` VALUES (30001002, 'aut_right_per_contract_n_002', 'Tạo thông tin hợp đồng', 'aut_right_per_contract');
INSERT INTO `ta_aut_right` VALUES (30001003, 'aut_right_per_contract_m_003', 'Sửa thông tin hợp đồng', 'aut_right_per_contract');
INSERT INTO `ta_aut_right` VALUES (30001004, 'aut_right_per_contract_d_004', 'Xóa thông tin hợp đồng', 'aut_right_per_contract');
INSERT INTO `ta_aut_right` VALUES (30001005, 'aut_right_per_contract_r_005', 'Tổng kết báo cáo các hợp đồng', 'aut_right_per_contract');
INSERT INTO `ta_aut_right` VALUES (30002001, 'aut_right_per_child_g_001', 'Xem thông tin của chi nhánh', 'aut_right_per_child');
INSERT INTO `ta_aut_right` VALUES (30002002, 'aut_right_per_child_n_002', 'Tạo thông tin của chi nhánh', 'aut_right_per_child');
INSERT INTO `ta_aut_right` VALUES (30002003, 'aut_right_per_child_m_003', 'Sửa thông tin của chi nhánh', 'aut_right_per_child');
INSERT INTO `ta_aut_right` VALUES (30002004, 'aut_right_per_child_d_004', 'Xóa thông tin của chi nhánh', 'aut_right_per_child');
INSERT INTO `ta_aut_right` VALUES (30002005, 'aut_right_per_child_r_005', 'Tổng kết báo cáo của chi nhánh', 'aut_right_per_child');
INSERT INTO `ta_aut_right` VALUES (40000001, 'aut_right_prj_project_g_001', 'Xem thông tin của dự án', 'aut_right_prj_project');
INSERT INTO `ta_aut_right` VALUES (40000002, 'aut_right_prj_project_n_002', 'Tạo thông tin của dự án', 'aut_right_prj_project');
INSERT INTO `ta_aut_right` VALUES (40000003, 'aut_right_prj_project_m_003', 'Sửa thông tin của dự án', 'aut_right_prj_project');
INSERT INTO `ta_aut_right` VALUES (40000004, 'aut_right_prj_project_d_004', 'Xóa thông tin của dự án', 'aut_right_prj_project');
INSERT INTO `ta_aut_right` VALUES (40000005, 'aut_right_prj_project_r_005', 'Tổng kết báo cáo của dự án', 'aut_right_prj_project');
INSERT INTO `ta_aut_right` VALUES (50000001, 'aut_right_nso_email_grp_g_001', 'Xem thông tin của nhóm email', 'aut_right_nso_email');
INSERT INTO `ta_aut_right` VALUES (50000002, 'aut_right_nso_email_grp_n_002', 'Tạo thông tin của nhóm email', 'aut_right_nso_email');
INSERT INTO `ta_aut_right` VALUES (50000003, 'aut_right_nso_email_grp_m_003', 'Sửa thông tin của nhóm email', 'aut_right_nso_email');
INSERT INTO `ta_aut_right` VALUES (50000004, 'aut_right_nso_email_grp_d_004', 'Xóa thông tin của nhóm email', 'aut_right_nso_email');
INSERT INTO `ta_aut_right` VALUES (50000005, 'aut_right_nso_email_grp_r_005', 'Tổng kết báo cáo của nhóm email', 'aut_right_nso_email');
INSERT INTO `ta_aut_right` VALUES (50000101, 'aut_right_nso_email_camp_g_001', 'Xem thông tin của chương trình email', 'aut_right_nso_email_campaign');
INSERT INTO `ta_aut_right` VALUES (50000102, 'aut_right_nso_email_camp_n_002', 'Tạo thông tin của chương trình email', 'aut_right_nso_email_campaign');
INSERT INTO `ta_aut_right` VALUES (50000103, 'aut_right_nso_email_camp_m_003', 'Sửa thông tin của chương trình email', 'aut_right_nso_email_campaign');
INSERT INTO `ta_aut_right` VALUES (50000104, 'aut_right_nso_email_camp_d_004', 'Xóa thông tin của chương trình email', 'aut_right_nso_email_campaign');
INSERT INTO `ta_aut_right` VALUES (50000105, 'aut_right_nso_email_camp_r_005', 'Tổng kết báo cáo của chương trình email', 'aut_right_nso_email_campaign');

-- ----------------------------
-- Table structure for ta_aut_role
-- ----------------------------
DROP TABLE IF EXISTS `ta_aut_role`;
CREATE TABLE `ta_aut_role`  (
  `I_ID` int(11) NOT NULL,
  `I_Status` int(11) NOT NULL,
  `T_Name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `T_Info_01` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'description',
  `T_Info_02` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'group',
  `T_Aut_Right` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'id cac quyen lien quan, vd: 12001, 12003',
  PRIMARY KEY (`I_ID`) USING BTREE,
  UNIQUE INDEX `idx_TAROL_01`(`T_Name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_aut_role
-- ----------------------------
INSERT INTO `ta_aut_role` VALUES (100, 1, 'RO_ADM_SUPER', 'Super Admin', 'aut_adm', '[100,101,102,103,104,105]');
INSERT INTO `ta_aut_role` VALUES (101, 1, 'RO_ADM_SYS', 'Administrateur Système', 'aut_adm', '[101,102,103,104,105]');
INSERT INTO `ta_aut_role` VALUES (200, 1, 'RO_USR_ADM', 'Administrateur de Référentiel', 'aut_adm', '[101,102,103,104,105]');
INSERT INTO `ta_aut_role` VALUES (201, 1, 'RO_USR_STD', 'Utilisateur Standard', 'aut_user', '[101,105]');
INSERT INTO `ta_aut_role` VALUES (202, 1, 'RO_USR_EDIT', 'Éditeur de Données', 'aut_user', '[101,103,105]');
INSERT INTO `ta_aut_role` VALUES (203, 1, 'RO_USR_CONS', 'Consultant', 'aut_user', '[101,102,105]');
INSERT INTO `ta_aut_role` VALUES (204, 1, 'RO_USR_GET', 'Accès en Lecture Seule', 'aut_user', '[101]');
INSERT INTO `ta_aut_role` VALUES (205, 1, 'RO_USR_MOD', 'Accès en Écriture Limitée', 'aut_user', '[101,103]');
INSERT INTO `ta_aut_role` VALUES (206, 1, 'RO_USR_SPEC', 'Accès à des Fonctionnalités Spécifiques', 'aut_user', '[101,102,103,105]');

-- ----------------------------
-- Table structure for ta_aut_user
-- ----------------------------
DROP TABLE IF EXISTS `ta_aut_user`;
CREATE TABLE `ta_aut_user`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Status` int(11) NULL DEFAULT NULL,
  `I_Type_01` int(11) NULL DEFAULT NULL COMMENT 'kiểu adm, agent, visitor, member....',
  `I_Type_02` int(11) NULL DEFAULT NULL COMMENT 'kiểu thứ cấp, ví dụ 1 học sinh sẽ có tk cho cha mẹ',
  `T_Login_01` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Login_02` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Login_03` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Pass_01` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Pass_02` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Pass_03` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'email',
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'tel',
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_06` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_07` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_08` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_09` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_10` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'dt new',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'dt mod',
  `D_Date_03` datetime NULL DEFAULT NULL COMMENT 'dt bg limit if need validation',
  `D_Date_04` datetime NULL DEFAULT NULL COMMENT 'dt end limit if need validation',
  `I_Aut_User_01` int(11) NULL DEFAULT NULL COMMENT 'id user new ',
  `I_Aut_User_02` int(11) NULL DEFAULT NULL COMMENT 'id user mod ',
  `I_Aut_User_03` int(11) NULL DEFAULT NULL COMMENT 'id user sup ',
  `I_Per_Person` int(11) NULL DEFAULT NULL COMMENT 'cá nhân liên quan',
  `I_Per_Manager` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TUSER_01`(`T_Login_01`) USING BTREE,
  INDEX `idx_TUSER_02`(`T_Login_02`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_aut_user
-- ----------------------------
INSERT INTO `ta_aut_user` VALUES (1, 1, 2, NULL, 'adm', NULL, NULL, 'df0217a29bdf4b725d1e531d87075cf536120a38b9eb1ceaffc84e8c22cf33d3', NULL, NULL, 'adm@ratp.fr', '099', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-03-01 20:43:11', '2024-03-13 22:25:29', NULL, NULL, NULL, 1, 1, 2, 1);
INSERT INTO `ta_aut_user` VALUES (2, 1, 2, NULL, 'vu.hoang', NULL, NULL, 'df0217a29bdf4b725d1e531d87075cf536120a38b9eb1ceaffc84e8c22cf33d3', NULL, NULL, 'nguyenvu.hoag@yahoo.fr', '099', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-05-05 11:30:10', '2024-03-13 22:56:24', NULL, NULL, NULL, 1, 1, 2, 1);
INSERT INTO `ta_aut_user` VALUES (12, 1, 2, NULL, 'phan.sy', NULL, NULL, NULL, NULL, NULL, 'phan.sy@gmail.com', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-03-09 12:31:12', '2024-03-09 12:32:25', NULL, NULL, 1, 1, NULL, 7, 1);
INSERT INTO `ta_aut_user` VALUES (13, 1, 2, NULL, 'alan', NULL, NULL, NULL, NULL, NULL, 'alan@gmail.com', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-03-09 12:34:58', '2024-03-09 12:35:04', NULL, NULL, 1, 1, NULL, 8, 12);

-- ----------------------------
-- Table structure for ta_cfg_value
-- ----------------------------
DROP TABLE IF EXISTS `ta_cfg_value`;
CREATE TABLE `ta_cfg_value`  (
  `I_ID` int(11) NOT NULL,
  `I_Type_01` int(11) NULL DEFAULT NULL,
  `I_Type_02` int(11) NULL DEFAULT NULL,
  `I_Status_01` int(11) NULL DEFAULT NULL,
  `I_Status_02` int(11) NULL DEFAULT NULL,
  `T_Name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `T_Code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'description',
  `T_Info_02` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Table value',
  `T_Info_03` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'other value',
  `I_Parent` int(11) NULL DEFAULT NULL,
  `I_Per_Manager` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TCVAL_01`(`I_Parent`) USING BTREE,
  INDEX `idx_TCVAL_03`(`T_Code`) USING BTREE,
  INDEX `idx_TCVAL_04`(`I_Type_01`) USING BTREE,
  CONSTRAINT `fk_TCVAL_01` FOREIGN KEY (`I_Parent`) REFERENCES `ta_cfg_value` (`I_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_cfg_value
-- ----------------------------

-- ----------------------------
-- Table structure for ta_fin_finance
-- ----------------------------
DROP TABLE IF EXISTS `ta_fin_finance`;
CREATE TABLE `ta_fin_finance`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Status` int(11) NULL DEFAULT NULL,
  `I_Type_01` int(11) NULL DEFAULT NULL,
  `I_Type_02` int(11) NULL DEFAULT NULL,
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'dt new',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'dt mod',
  `D_Date_03` datetime NULL DEFAULT NULL COMMENT 'dt begin',
  `D_Date_04` datetime NULL DEFAULT NULL COMMENT 'dt end',
  `F_Val_01` double NULL DEFAULT NULL,
  `F_Val_02` double NULL DEFAULT NULL,
  `F_Val_03` double NULL DEFAULT NULL,
  `F_Val_04` double NULL DEFAULT NULL,
  `F_Val_05` double NULL DEFAULT NULL,
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `I_Aut_User_01` int(11) NULL DEFAULT NULL,
  `I_Aut_User_02` int(11) NULL DEFAULT NULL,
  `I_Aut_User_03` int(11) NULL DEFAULT NULL,
  `I_Per_Person_01` int(11) NOT NULL,
  `I_Per_Person_02` int(11) NOT NULL,
  `I_Per_Manager` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_FIFIN_01`(`I_Per_Person_01`) USING BTREE,
  INDEX `idx_FIFIN_02`(`I_Per_Person_02`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_fin_finance
-- ----------------------------

-- ----------------------------
-- Table structure for ta_mat_material
-- ----------------------------
DROP TABLE IF EXISTS `ta_mat_material`;
CREATE TABLE `ta_mat_material`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Status_01` int(11) NULL DEFAULT NULL,
  `I_Status_02` int(11) NULL DEFAULT NULL,
  `I_Status_03` int(11) NULL DEFAULT NULL,
  `I_Status_04` int(11) NULL DEFAULT NULL,
  `I_Status_05` int(11) NULL DEFAULT NULL,
  `I_Type_01` int(11) NULL DEFAULT NULL,
  `I_Type_02` int(11) NULL DEFAULT NULL,
  `I_Type_03` int(11) NULL DEFAULT NULL,
  `I_Type_04` int(11) NULL DEFAULT NULL,
  `I_Type_05` int(11) NULL DEFAULT NULL,
  `T_Name_01` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Name_02` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Name_03` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Code_01` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Code_02` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Code_03` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Code_04` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Code_05` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_06` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_07` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_08` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_09` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_10` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'dt new',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'dt mod',
  `D_Date_03` datetime NULL DEFAULT NULL COMMENT 'dt begin',
  `D_Date_04` datetime NULL DEFAULT NULL COMMENT 'dt end',
  `I_Aut_User_01` int(11) NULL DEFAULT NULL,
  `I_Aut_User_02` int(11) NULL DEFAULT NULL,
  `I_Per_Manager` int(11) NULL DEFAULT NULL,
  `I_Per_Person_01` int(11) NULL DEFAULT NULL COMMENT 'per production',
  `I_Per_Person_02` int(11) NULL DEFAULT NULL COMMENT 'per other',
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TMMAT_02`(`T_Name_01`) USING BTREE,
  INDEX `idx_TMMAT_03`(`T_Name_02`) USING BTREE,
  INDEX `idx_TMMAT_04`(`T_Code_01`) USING BTREE,
  INDEX `idx_TMMAT_05`(`T_Code_02`) USING BTREE,
  INDEX `idx_TMMAT_06`(`T_Code_03`) USING BTREE,
  INDEX `idx_TMMAT_07`(`T_Code_04`) USING BTREE,
  INDEX `idx_TMMAT_08`(`T_Code_05`) USING BTREE,
  INDEX `idx_TMMAT_09`(`I_Per_Manager`) USING BTREE,
  INDEX `idx_TMMAT_10`(`I_Per_Person_01`) USING BTREE,
  INDEX `idx_TMMAT_11`(`I_Per_Person_02`) USING BTREE,
  INDEX `idx_TMMAT_13`(`I_Type_01`) USING BTREE,
  INDEX `idx_TMMAT_14`(`I_Type_02`) USING BTREE,
  INDEX `idx_TMMAT_15`(`I_Type_03`) USING BTREE,
  INDEX `idx_TMMAT_21`(`I_Status_01`) USING BTREE,
  INDEX `idx_TMMAT_22`(`I_Status_02`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_mat_material
-- ----------------------------

-- ----------------------------
-- Table structure for ta_mat_material_detail
-- ----------------------------
DROP TABLE IF EXISTS `ta_mat_material_detail`;
CREATE TABLE `ta_mat_material_detail`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Mat_Material_01` int(11) NULL DEFAULT NULL COMMENT 'Parent',
  `I_Mat_Material_02` int(11) NULL DEFAULT NULL COMMENT 'Child',
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'mat unit label',
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `I_Type_01` int(11) NULL DEFAULT NULL,
  `I_Type_02` int(11) NULL DEFAULT NULL,
  `I_Status_01` int(11) NULL DEFAULT NULL,
  `I_Status_02` int(11) NULL DEFAULT NULL,
  `I_Val_01` int(11) NULL DEFAULT NULL COMMENT 'Priority',
  `I_Val_02` int(11) NULL DEFAULT NULL,
  `F_Val_01` double NULL DEFAULT NULL COMMENT 'quant',
  `F_Val_02` double NULL DEFAULT NULL COMMENT 'unit ratio',
  `F_Val_03` double NULL DEFAULT NULL COMMENT 'unit price pref',
  `F_Val_04` double NULL DEFAULT NULL,
  `F_Val_05` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TMMDET_02`(`I_Mat_Material_01`) USING BTREE,
  INDEX `idx_TMMDET_03`(`I_Mat_Material_02`) USING BTREE,
  CONSTRAINT `FK_TMMDE_01` FOREIGN KEY (`I_Mat_Material_01`) REFERENCES `ta_mat_material` (`I_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_TMMDE_02` FOREIGN KEY (`I_Mat_Material_02`) REFERENCES `ta_mat_material` (`I_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_mat_material_detail
-- ----------------------------

-- ----------------------------
-- Table structure for ta_mat_price
-- ----------------------------
DROP TABLE IF EXISTS `ta_mat_price`;
CREATE TABLE `ta_mat_price`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Priority` int(11) NULL DEFAULT NULL,
  `I_Status` int(11) NULL DEFAULT NULL,
  `I_Mat_Material` int(11) NULL DEFAULT NULL,
  `I_Mat_Unit` int(11) NULL DEFAULT NULL,
  `F_Val_00` double NULL DEFAULT NULL COMMENT 'ratio',
  `F_Val_01` double NULL DEFAULT NULL COMMENT 'price 01',
  `F_Val_02` double NULL DEFAULT NULL COMMENT 'price 02',
  `F_Val_03` double NULL DEFAULT NULL COMMENT 'discount',
  `F_Val_04` double NULL DEFAULT NULL,
  `F_Val_05` double NULL DEFAULT NULL,
  `F_Val_06` double NULL DEFAULT NULL,
  `F_Val_07` double NULL DEFAULT NULL,
  `F_Val_08` double NULL DEFAULT NULL,
  `F_Val_09` double NULL DEFAULT NULL,
  `F_Val_10` double NULL DEFAULT NULL,
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Unit',
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Currency',
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'dt new',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'dt mod',
  `D_Date_03` datetime NULL DEFAULT NULL COMMENT 'dt begin',
  `D_Date_04` datetime NULL DEFAULT NULL COMMENT 'dt end',
  `I_Aut_User_01` int(11) NULL DEFAULT NULL,
  `I_Aut_User_02` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TMMPRI_02`(`I_Mat_Material`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_mat_price
-- ----------------------------

-- ----------------------------
-- Table structure for ta_mat_stock
-- ----------------------------
DROP TABLE IF EXISTS `ta_mat_stock`;
CREATE TABLE `ta_mat_stock`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Per_Manager` int(11) NULL DEFAULT NULL,
  `I_Mat_Warehouse` int(11) NULL DEFAULT NULL,
  `I_Mat_Material` int(11) NULL DEFAULT NULL,
  `I_Status` int(11) NULL DEFAULT NULL,
  `F_Val_00` double NULL DEFAULT NULL,
  `F_Val_01` double NULL DEFAULT NULL,
  `F_Val_02` double NULL DEFAULT NULL,
  `F_Val_03` double NULL DEFAULT NULL,
  `F_Val_04` double NULL DEFAULT NULL,
  `F_Val_05` double NULL DEFAULT NULL,
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `D_Date_01` datetime NULL DEFAULT NULL,
  `D_Date_02` datetime NULL DEFAULT NULL,
  `D_Date_03` datetime NULL DEFAULT NULL,
  `D_Date_04` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TMMSTK_01`(`I_ID`) USING BTREE,
  INDEX `idx_TMMSTK_02`(`I_Mat_Material`) USING BTREE,
  INDEX `idx_TMMSTK_03`(`I_Per_Manager`) USING BTREE,
  INDEX `idx_TMMSTK_04`(`I_Mat_Warehouse`) USING BTREE,
  INDEX `idx_TMMSTK_05`(`I_Status`) USING BTREE,
  INDEX `idx_TMMSTK_06`(`D_Date_04`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_mat_stock
-- ----------------------------

-- ----------------------------
-- Table structure for ta_mat_stock_io
-- ----------------------------
DROP TABLE IF EXISTS `ta_mat_stock_io`;
CREATE TABLE `ta_mat_stock_io`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Per_Manager` int(11) NULL DEFAULT NULL,
  `I_Mat_Material` int(11) NULL DEFAULT NULL,
  `I_Mat_Stock` int(11) NULL DEFAULT NULL,
  `I_Mat_Warehouse` int(11) NULL DEFAULT NULL,
  `I_Sor_Order` int(11) NULL DEFAULT NULL,
  `I_Sor_Order_Detail` int(11) NULL DEFAULT NULL,
  `I_Status` int(11) NULL DEFAULT NULL,
  `I_Type` int(11) NULL DEFAULT NULL,
  `F_Val_00` double NULL DEFAULT NULL,
  `F_Val_01` double NULL DEFAULT NULL,
  `F_Val_02` double NULL DEFAULT NULL,
  `F_Val_03` double NULL DEFAULT NULL,
  `F_Val_04` double NULL DEFAULT NULL,
  `F_Val_05` double NULL DEFAULT NULL,
  `D_Date_01` datetime NULL DEFAULT NULL,
  `D_Date_02` datetime NULL DEFAULT NULL,
  `D_Date_03` datetime NULL DEFAULT NULL,
  `D_Date_04` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TMMSIO_02`(`I_Mat_Material`) USING BTREE,
  INDEX `idx_TMMSIO_03`(`I_Per_Manager`) USING BTREE,
  INDEX `idx_TMMSIO_04`(`I_Mat_Stock`) USING BTREE,
  INDEX `idx_TMMSIO_05`(`I_Sor_Order`) USING BTREE,
  INDEX `idx_TMMSTK_06`(`I_Sor_Order_Detail`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_mat_stock_io
-- ----------------------------

-- ----------------------------
-- Table structure for ta_mat_stock_month
-- ----------------------------
DROP TABLE IF EXISTS `ta_mat_stock_month`;
CREATE TABLE `ta_mat_stock_month`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Per_Manager` int(11) NULL DEFAULT NULL,
  `I_Mat_Material` int(11) NULL DEFAULT NULL,
  `I_Mat_Warehouse` int(11) NULL DEFAULT NULL,
  `F_Val_00` double NULL DEFAULT NULL,
  `F_Val_01` double NULL DEFAULT NULL,
  `F_Val_02` double NULL DEFAULT NULL,
  `F_Val_03` double NULL DEFAULT NULL,
  `F_Val_04` double NULL DEFAULT NULL,
  `F_Val_05` double NULL DEFAULT NULL,
  `D_Date_01` datetime NULL DEFAULT NULL,
  `D_Date_02` datetime NULL DEFAULT NULL,
  `I_Type` int(11) NULL DEFAULT NULL,
  `I_Aut_User` int(11) NULL DEFAULT NULL,
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TMSMO_02`(`I_Mat_Material`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_mat_stock_month
-- ----------------------------

-- ----------------------------
-- Table structure for ta_mat_unit
-- ----------------------------
DROP TABLE IF EXISTS `ta_mat_unit`;
CREATE TABLE `ta_mat_unit`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Status` int(11) NULL DEFAULT NULL,
  `T_Code` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Name` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `I_Per_Manager` int(11) NULL DEFAULT NULL COMMENT 'cong ty/ca nhan quan ly',
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TMUNIT_01`(`I_Per_Manager`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_mat_unit
-- ----------------------------

-- ----------------------------
-- Table structure for ta_mat_warehouse
-- ----------------------------
DROP TABLE IF EXISTS `ta_mat_warehouse`;
CREATE TABLE `ta_mat_warehouse`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `T_Name` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `I_Status` int(11) NULL DEFAULT NULL,
  `I_Type_01` int(11) NULL DEFAULT NULL COMMENT '1: wh public/web sale, 2: wh intern',
  `I_Type_02` int(11) NULL DEFAULT NULL,
  `T_Code_01` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Code_02` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `I_Per_Manager` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TMMWAR_01`(`T_Name`) USING BTREE,
  INDEX `idx_TMMWAR_02`(`T_Code_01`) USING BTREE,
  INDEX `idx_TMMWAR_03`(`T_Code_02`) USING BTREE,
  INDEX `idx_TMMWAR_04`(`I_Per_Manager`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_mat_warehouse
-- ----------------------------

-- ----------------------------
-- Table structure for ta_msg_message
-- ----------------------------
DROP TABLE IF EXISTS `ta_msg_message`;
CREATE TABLE `ta_msg_message`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Status` int(11) NOT NULL COMMENT 'mac dinh trong chuong trinh',
  `I_Type_01` int(11) NOT NULL COMMENT 'type Msg: 1:email, 2:chat',
  `I_Type_02` int(11) NULL DEFAULT NULL COMMENT 'type Noti: envoi sms, envoi email, in-app',
  `T_Info_01` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'T_From',
  `T_Info_02` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'T_To',
  `T_Info_03` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'T_Title',
  `T_Info_04` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'T_Body',
  `T_Info_05` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `I_Aut_User` int(11) NULL DEFAULT NULL COMMENT 'user creates',
  `D_Date` datetime NULL DEFAULT NULL,
  `I_Entity_Type` int(11) NULL DEFAULT NULL,
  `I_Entity_ID` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TMMES_01`(`I_Aut_User`) USING BTREE,
  INDEX `idx_TMMES_02`(`I_Entity_Type`, `I_Entity_ID`) USING BTREE,
  INDEX `idx_TMMES_03`(`D_Date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_msg_message
-- ----------------------------

-- ----------------------------
-- Table structure for ta_msg_message_history
-- ----------------------------
DROP TABLE IF EXISTS `ta_msg_message_history`;
CREATE TABLE `ta_msg_message_history`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Msg_Message` int(11) NOT NULL,
  `I_Status` int(11) NOT NULL,
  `I_Aut_User` int(11) NOT NULL COMMENT 'for what user/destination',
  `D_Date` datetime NULL DEFAULT NULL COMMENT 'date of status',
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TMMES_01`(`I_Aut_User`) USING BTREE,
  INDEX `idx_TMNOT_02`(`I_Msg_Message`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_msg_message_history
-- ----------------------------

-- ----------------------------
-- Table structure for ta_msg_message_store
-- ----------------------------
DROP TABLE IF EXISTS `ta_msg_message_store`;
CREATE TABLE `ta_msg_message_store`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Status` int(11) NULL DEFAULT NULL,
  `I_Type_01` int(11) NULL DEFAULT NULL,
  `I_Type_02` int(11) NULL DEFAULT NULL,
  `T_Info_01` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'T_From',
  `T_Info_02` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'T_To',
  `T_Info_03` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'T_Content => jsonArray: [{msg}]',
  `T_Info_04` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_05` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'dtBegin',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'dtEnd',
  `I_Entity_Type` int(11) NULL DEFAULT NULL,
  `I_Entity_ID_01` int(11) NULL DEFAULT NULL,
  `I_Entity_ID_02` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TMMST_01`(`I_Entity_Type`) USING BTREE,
  INDEX `idx_TMMST_02`(`I_Entity_ID_01`) USING BTREE,
  INDEX `idx_TMMST_03`(`I_Entity_ID_02`) USING BTREE,
  INDEX `idx_TMMST_10`(`I_Type_01`) USING BTREE,
  INDEX `idx_TMMST_11`(`I_Type_02`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_msg_message_store
-- ----------------------------

-- ----------------------------
-- Table structure for ta_nso_group
-- ----------------------------
DROP TABLE IF EXISTS `ta_nso_group`;
CREATE TABLE `ta_nso_group`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `T_Ref` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'Date creation',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'Date mod',
  `I_Status_01` int(11) NULL DEFAULT NULL COMMENT 'status by admin',
  `I_Status_02` int(11) NULL DEFAULT NULL COMMENT '1: Publish, 2: Private, 0: Desactivate',
  `I_Type_01` int(11) NULL DEFAULT NULL,
  `I_Type_02` int(11) NULL DEFAULT NULL,
  `I_Aut_User` int(11) NULL DEFAULT NULL,
  `I_Per_Manager` int(11) NULL DEFAULT NULL COMMENT 'Per_Person moral manager',
  `T_Val_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Val_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TNGRO_04`(`T_Ref`) USING BTREE,
  INDEX `idx_TNGRO_01`(`I_Per_Manager`) USING BTREE,
  INDEX `idx_TNGRO_02`(`I_Type_01`) USING BTREE,
  INDEX `idx_TNGRO_03`(`I_Type_02`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_nso_group
-- ----------------------------
INSERT INTO `ta_nso_group` VALUES (1, 'GRP_240227121049', 'asc', NULL, NULL, '2024-03-06 20:42:07', NULL, 1, NULL, 300, NULL, 1, 1, NULL, NULL);

-- ----------------------------
-- Table structure for ta_nso_group_history
-- ----------------------------
DROP TABLE IF EXISTS `ta_nso_group_history`;
CREATE TABLE `ta_nso_group_history`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Nso_Group` int(11) NOT NULL,
  `I_Msg_Message` int(11) NOT NULL,
  `I_Aut_User` int(11) NOT NULL,
  `I_Status` int(11) NOT NULL,
  `D_Date` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TNGHI_01`(`I_Nso_Group`) USING BTREE,
  INDEX `idx_TNGHI_02`(`I_Aut_User`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_nso_group_history
-- ----------------------------

-- ----------------------------
-- Table structure for ta_nso_group_member
-- ----------------------------
DROP TABLE IF EXISTS `ta_nso_group_member`;
CREATE TABLE `ta_nso_group_member`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Nso_Group` int(11) NULL DEFAULT NULL,
  `I_Aut_User` int(11) NULL DEFAULT NULL,
  `I_Status` int(11) NULL DEFAULT NULL COMMENT '1: waiting, 2: accept, 0: Desactivate',
  `I_Type` int(11) NULL DEFAULT NULL COMMENT '1: adm, 2: member lev 1, 2: member lev 2',
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'Date creation',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'Date mod',
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TNGME_01`(`I_Nso_Group`) USING BTREE,
  INDEX `idx_TNGME_02`(`I_Aut_User`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_nso_group_member
-- ----------------------------
INSERT INTO `ta_nso_group_member` VALUES (1, 1, 1, 1, 0, '2024-03-06 20:47:25', NULL);
INSERT INTO `ta_nso_group_member` VALUES (2, 2, 1, 1, 2, '2024-03-06 20:47:25', NULL);
INSERT INTO `ta_nso_group_member` VALUES (5, 1, 2, 1, 2, '2024-03-08 00:20:17', NULL);

-- ----------------------------
-- Table structure for ta_nso_offer
-- ----------------------------
DROP TABLE IF EXISTS `ta_nso_offer`;
CREATE TABLE `ta_nso_offer`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `T_Title` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `I_Status_01` int(11) NULL DEFAULT NULL COMMENT 'status by admin',
  `I_Status_02` int(11) NULL DEFAULT NULL COMMENT 'status by user: public or private',
  `I_Parent` int(11) NULL DEFAULT NULL COMMENT 'id of main offer',
  `T_Code_01` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Code_02` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `I_Type_01` int(11) NULL DEFAULT NULL COMMENT 'type of offer like: work, candidate',
  `I_Type_02` int(11) NULL DEFAULT NULL COMMENT 'type of lang',
  `I_Type_03` int(11) NULL DEFAULT NULL,
  `T_Content_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_06` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_07` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_08` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_09` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_10` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'T_Comment use in adm mode',
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Json for address: {lat:123, long:345, addr: \"123 Hung Vuong, DA,VN\"}',
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'ngày tạo',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'ngày sửa',
  `D_Date_03` datetime NULL DEFAULT NULL COMMENT 'ngày bắt đầu',
  `D_Date_04` datetime NULL DEFAULT NULL COMMENT 'ngày kết thúc',
  `D_Date_05` datetime NULL DEFAULT NULL COMMENT 'ngày khác',
  `I_Aut_User_01` int(11) NULL DEFAULT NULL COMMENT 'user created',
  `I_Aut_User_02` int(11) NULL DEFAULT NULL COMMENT 'user modify/validate/delete',
  `I_Aut_User_03` int(11) NULL DEFAULT NULL COMMENT 'other user',
  `I_Val_01` int(11) NULL DEFAULT NULL COMMENT 'I_Entity_Type: type parent entity ...',
  `I_Val_02` int(11) NULL DEFAULT NULL COMMENT 'I_Entity_ID: parent id...',
  `I_Val_03` int(11) NULL DEFAULT NULL COMMENT 'I_Nb_Resp',
  `I_Val_04` int(11) NULL DEFAULT NULL COMMENT 'khác',
  `I_Val_05` int(11) NULL DEFAULT NULL COMMENT 'khác',
  `F_Val_01` double NULL DEFAULT NULL COMMENT 'latitude for search',
  `F_Val_02` double NULL DEFAULT NULL COMMENT 'long for search',
  `F_Val_03` double NULL DEFAULT NULL,
  `F_Val_04` double NULL DEFAULT NULL,
  `F_Val_05` double NULL DEFAULT NULL,
  `I_Per_Manager` int(11) NULL DEFAULT NULL COMMENT 'Per_Person moral manager',
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TNOFF_01`(`D_Date_01`) USING BTREE,
  INDEX `idx_TNOFF_02`(`D_Date_02`) USING BTREE,
  INDEX `idx_TNOFF_03`(`D_Date_03`) USING BTREE,
  INDEX `idx_TNOFF_04`(`D_Date_04`) USING BTREE,
  INDEX `idx_TNOFF_11`(`I_Aut_User_01`) USING BTREE,
  INDEX `idx_TNOFF_12`(`I_Aut_User_02`) USING BTREE,
  INDEX `idx_TNOFF_13`(`I_Aut_User_03`) USING BTREE,
  INDEX `idx_TNOFF_20`(`T_Title`) USING BTREE,
  INDEX `idx_TNOFF_21`(`T_Code_01`) USING BTREE,
  INDEX `idx_TNOFF_22`(`T_Code_02`) USING BTREE,
  INDEX `idx_TNOFF_31`(`I_Parent`) USING BTREE,
  INDEX `idx_TNOFF_32`(`I_Val_01`, `I_Val_02`) USING BTREE,
  INDEX `idx_TNOFF_33`(`I_Per_Manager`) USING BTREE,
  INDEX `idx_TNOFF_41`(`I_Type_01`) USING BTREE,
  INDEX `idx_TNOFF_42`(`I_Type_02`) USING BTREE,
  INDEX `idx_TNOFF_43`(`I_Type_03`) USING BTREE,
  INDEX `idx_TNOFF_44`(`I_Status_01`) USING BTREE,
  INDEX `idx_TNOFF_45`(`I_Status_02`) USING BTREE,
  INDEX `idx_TNOFF_51`(`F_Val_01`) USING BTREE,
  INDEX `idx_TNOFF_52`(`F_Val_02`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_nso_offer
-- ----------------------------

-- ----------------------------
-- Table structure for ta_nso_post
-- ----------------------------
DROP TABLE IF EXISTS `ta_nso_post`;
CREATE TABLE `ta_nso_post`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `T_Title` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `I_Status_01` int(11) NULL DEFAULT NULL COMMENT 'status by admin',
  `I_Status_02` int(11) NULL DEFAULT NULL COMMENT 'status by user: public or private',
  `I_Parent` int(11) NULL DEFAULT NULL COMMENT 'id of main post for that this post response',
  `T_Code_01` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Code_02` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `I_Type_01` int(11) NULL DEFAULT NULL COMMENT 'type of post like: event, news, evaluation',
  `I_Type_02` int(11) NULL DEFAULT NULL COMMENT 'type of lang',
  `I_Type_03` int(11) NULL DEFAULT NULL,
  `T_Content_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_06` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_07` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_08` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_09` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Content_10` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'T_Comment use in adm mode',
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'ngày tạo',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'ngày sửa',
  `D_Date_03` datetime NULL DEFAULT NULL COMMENT 'ngày bắt đầu',
  `D_Date_04` datetime NULL DEFAULT NULL COMMENT 'ngày kết thúc',
  `D_Date_05` datetime NULL DEFAULT NULL COMMENT 'ngày khác',
  `I_Aut_User_01` int(11) NULL DEFAULT NULL,
  `I_Aut_User_02` int(11) NULL DEFAULT NULL,
  `I_Val_01` int(11) NULL DEFAULT NULL COMMENT 'I_Entity_Type: type offer, area ...',
  `I_Val_02` int(11) NULL DEFAULT NULL COMMENT 'I_Entity_ID: offer id, area id...',
  `I_Val_03` int(11) NULL DEFAULT NULL COMMENT 'I_Nb_Resp',
  `I_Val_04` int(11) NULL DEFAULT NULL COMMENT 'khác',
  `I_Val_05` int(11) NULL DEFAULT NULL COMMENT 'khác',
  `F_Val_01` double NULL DEFAULT NULL,
  `F_Val_02` double NULL DEFAULT NULL,
  `F_Val_03` double NULL DEFAULT NULL,
  `F_Val_04` double NULL DEFAULT NULL,
  `F_Val_05` double NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TNPOS_01`(`D_Date_01`) USING BTREE,
  INDEX `idx_TNPOS_02`(`D_Date_02`) USING BTREE,
  INDEX `idx_TNPOS_03`(`D_Date_03`) USING BTREE,
  INDEX `idx_TNPOS_04`(`D_Date_04`) USING BTREE,
  INDEX `idx_TNPOS_11`(`I_Aut_User_01`) USING BTREE,
  INDEX `idx_TNPOS_20`(`T_Title`) USING BTREE,
  INDEX `idx_TNPOS_21`(`T_Code_01`) USING BTREE,
  INDEX `idx_TNPOS_22`(`T_Code_02`) USING BTREE,
  INDEX `idx_TNPOS_30`(`I_Val_01`, `I_Val_02`) USING BTREE,
  INDEX `idx_TNPOS_31`(`I_Parent`) USING BTREE,
  INDEX `idx_TNPOS_41`(`I_Type_01`) USING BTREE,
  INDEX `idx_TNPOS_42`(`I_Type_02`) USING BTREE,
  INDEX `idx_TNPOS_43`(`I_Type_03`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_nso_post
-- ----------------------------

-- ----------------------------
-- Table structure for ta_per_person
-- ----------------------------
DROP TABLE IF EXISTS `ta_per_person`;
CREATE TABLE `ta_per_person`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `T_Name_01` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Họ / Tên doanh nghiệp',
  `T_Name_02` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Tên đệm',
  `T_Name_03` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Tên/ Tên gọi khác của doanh nghiệp',
  `T_Name_04` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Tên khác',
  `T_Name_05` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Tên khác',
  `I_Status_01` int(11) NULL DEFAULT NULL COMMENT '0: cần duyệt, 10: đã duyệt và đang hoạt động, 20:đã duyệt và tạm ngừng hoạt động, 100: không còn hoạt động',
  `I_Status_02` int(11) NULL DEFAULT NULL,
  `I_Type_01` int(11) NULL DEFAULT NULL COMMENT 'kiểu person:  100: kiểu cán bộ công nhân viên chức, 200: sinh viên, 750: doanh nghiệp,',
  `I_Type_02` int(11) NULL DEFAULT NULL COMMENT 'kiểu phân loại: doanh nghiệp: đối tác, cung ứng, khách hàng , cán bộ: giảng viên, chuyên viên..., sinh viên: ....',
  `I_Type_03` int(11) NULL DEFAULT NULL COMMENT '0: ko phân biệt, 1: nam, 2: nữ',
  `I_Type_04` int(11) NULL DEFAULT NULL COMMENT 'tình trạng tôn giáo	: không, phật giáo, công giáo, khác',
  `I_Type_05` int(11) NULL DEFAULT NULL COMMENT 'tình trạng đảng phái: Không, Đoàn, Đảng, Khác',
  `I_Type_06` int(11) NULL DEFAULT NULL COMMENT 'tình trạng gia đình 01: kết hôn, độc thân...',
  `I_Type_07` int(11) NULL DEFAULT NULL COMMENT 'tình trạng gia đình 02: đối tượng (hộ nghèo, thương binh, liệt sĩ...)',
  `I_Type_08` int(11) NULL DEFAULT NULL COMMENT 'tình trạng tuyển sinh/tuyển dụng 01: khu vực tuyển sinh',
  `I_Type_09` int(11) NULL DEFAULT NULL COMMENT 'tình trạng tuyển sinh/tuyển dụng 02: hình thức xét tuyển',
  `I_Type_10` int(11) NULL DEFAULT NULL COMMENT 'tình trạng khác',
  `F_Val_01` double NULL DEFAULT NULL COMMENT 'hệ số lương khởi động',
  `F_Val_02` double NULL DEFAULT NULL COMMENT 'hệ số lương hiện tại',
  `F_Val_03` double NULL DEFAULT NULL COMMENT 'other',
  `F_Val_04` double NULL DEFAULT NULL COMMENT 'other',
  `F_Val_05` double NULL DEFAULT NULL COMMENT 'other',
  `T_Code_01` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'CMND, đăng ký kinh doanh',
  `T_Code_02` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'CCCD, số đăng ký kinh doanh khác nếu có',
  `T_Code_03` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Số BHXH',
  `T_Code_04` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Mã QL nội bộ: số sinh viên, mã phòng ban',
  `T_Code_05` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Mã QL khác nếu có',
  `T_Code_06` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Mã QL khác nếu có',
  `T_Code_07` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Mã QL khác nếu có',
  `T_Code_08` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Mã QL khác nếu có',
  `T_Code_09` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Mã QL khác nếu có',
  `T_Code_10` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Mã QL khác nếu có',
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Json Thông tin cụ thể như địa chỉ tạm trú, thông tin cha mẹ....',
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Json Thông tin khác',
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Json khác',
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Json khác',
  `T_Info_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Json khác',
  `T_Info_06` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Json khác',
  `T_Info_07` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Json khác',
  `T_Info_08` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Json khác',
  `T_Info_09` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Json khác',
  `T_Info_10` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Json khác',
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'Ngày tạo',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'Ngày thay đổi',
  `D_Date_03` datetime NULL DEFAULT NULL COMMENT 'Ngày sinh',
  `D_Date_04` datetime NULL DEFAULT NULL COMMENT 'Ngày bắt đầu (bắt đầu làm việc, học)',
  `D_Date_05` datetime NULL DEFAULT NULL COMMENT 'Ngày kết thúc (nghỉ việc, nghỉ học)',
  `D_Date_06` datetime NULL DEFAULT NULL COMMENT 'Ngày ...',
  `D_Date_07` datetime NULL DEFAULT NULL COMMENT 'Ngày ...',
  `D_Date_08` datetime NULL DEFAULT NULL COMMENT 'Ngày ...',
  `D_Date_09` datetime NULL DEFAULT NULL COMMENT 'Ngày ...',
  `D_Date_10` datetime NULL DEFAULT NULL COMMENT 'Ngày ...',
  `I_Aut_User_01` int(11) NULL DEFAULT NULL COMMENT 'Người tạo',
  `I_Aut_User_02` int(11) NULL DEFAULT NULL COMMENT 'Người thay đổi',
  `I_Per_Manager` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TPERS_01`(`T_Name_01`) USING BTREE,
  INDEX `idx_TPERS_02`(`T_Name_02`) USING BTREE,
  INDEX `idx_TPERS_03`(`T_Name_03`) USING BTREE,
  INDEX `idx_TPERS_11`(`T_Code_01`) USING BTREE,
  INDEX `idx_TPERS_12`(`T_Code_02`) USING BTREE,
  INDEX `idx_TPERS_13`(`T_Code_03`) USING BTREE,
  INDEX `idx_TPERS_14`(`T_Code_04`) USING BTREE,
  INDEX `idx_TPERS_15`(`T_Code_05`) USING BTREE,
  INDEX `idx_TPERS_21`(`I_Type_01`) USING BTREE,
  INDEX `idx_TPERS_22`(`I_Type_02`) USING BTREE,
  INDEX `idx_TPERS_23`(`I_Type_03`) USING BTREE,
  INDEX `idx_TPERS_24`(`I_Type_04`) USING BTREE,
  INDEX `idx_TPERS_25`(`I_Type_05`) USING BTREE,
  INDEX `idx_TPERS_26`(`I_Type_06`) USING BTREE,
  INDEX `idx_TPERS_27`(`I_Type_07`) USING BTREE,
  INDEX `idx_TPERS_28`(`I_Type_08`) USING BTREE,
  INDEX `idx_TPERS_29`(`I_Type_09`) USING BTREE,
  INDEX `idx_TPERS_30`(`I_Type_10`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_per_person
-- ----------------------------
INSERT INTO `ta_per_person` VALUES (1, 'RATP', 'RATP', NULL, NULL, NULL, 1, NULL, 200, 2001000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'RATP', 'RATP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Paris', '(+33)', 'contact@ratp.fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `ta_per_person` VALUES (2, 'Adm', 'Adm', '', NULL, NULL, 1, NULL, 100, 1001000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Adm', 'Adm', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', 'asd', NULL, '{\"p\":\"\",\"d\":\"2024-03-09T04:37:42.845Z\",\"i\":\"asd\"}', '{\"f\":\"\",\"i\":\"\",\"l\":\"\"}', NULL, '', NULL, '', NULL, NULL, '2024-03-13 22:56:25', '2024-03-09 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, 1);
INSERT INTO `ta_per_person` VALUES (7, 'phan.sy', 'phan.sy', 'phan.sy', NULL, NULL, NULL, NULL, 100, 2000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', 'phan.sy1', NULL, '{\"i\":\"phan.sy\",\"d\":\"2024-03-09T05:30:50.600Z\",\"p\":\"phan.sy\"}', '{\"f\":\"\",\"i\":\"\",\"l\":\"\"}', NULL, '', NULL, '', NULL, '2024-03-09 12:31:12', '2024-03-09 12:32:26', '2024-03-09 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, NULL);
INSERT INTO `ta_per_person` VALUES (8, 'alan', 'alan', '', NULL, NULL, NULL, NULL, 100, 2000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', 'alan', NULL, '{\"p\":\"\",\"d\":\"2024-03-09T05:34:23.282Z\",\"i\":\"alan\"}', '{\"f\":\"\",\"i\":\"\",\"l\":\"\"}', NULL, '', NULL, '', NULL, '2024-03-09 12:34:58', '2024-03-09 12:35:05', '2024-03-09 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, NULL);

-- ----------------------------
-- Table structure for ta_prj_project
-- ----------------------------
DROP TABLE IF EXISTS `ta_prj_project`;
CREATE TABLE `ta_prj_project`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `T_Code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `T_Name` varchar(750) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `I_Parent` int(11) NULL DEFAULT NULL COMMENT 'Project - Epic (1 or N level) - Task (1 or N level) ',
  `I_Group` int(11) NOT NULL COMMENT 'I_Group = I_ID of first parent => for index and rapid search,\nAny epic, task have the same i_group',
  `I_Type_00` int(11) NULL DEFAULT NULL,
  `I_Type_01` int(11) NULL DEFAULT NULL COMMENT 'Business, software...',
  `I_Type_02` int(11) NULL DEFAULT NULL,
  `I_Type_03` int(11) NULL DEFAULT NULL,
  `T_Info_01` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `T_Info_02` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'lst of status:: 0: new, 1: todo, 2: in progress, 3: stand by, 4: done, 5: closed, 6: unresolved',
  `T_Info_03` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `T_Tag` varchar(750) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `I_Status` int(11) NULL DEFAULT NULL COMMENT '0: new, 1: todo, 2: in progress, 3: stand by, 4: done, 5: closed, 6: unresolved ',
  `I_Level` int(11) NULL DEFAULT NULL COMMENT 'level of priority',
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'D_Date_New',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'D_Date_Mod',
  `D_Date_03` datetime NULL DEFAULT NULL COMMENT 'D_Date_Begin',
  `D_Date_04` datetime NULL DEFAULT NULL COMMENT 'D_Date_End',
  `I_Per_Manager` int(11) NULL DEFAULT NULL COMMENT 'Company/person owns project',
  `I_Aut_User_01` int(11) NULL DEFAULT NULL COMMENT 'User who create the project',
  `I_Aut_User_02` int(11) NULL DEFAULT NULL COMMENT 'Last user modify content',
  `F_Val_01` double NULL DEFAULT NULL COMMENT 'Val used for evaluation something: % of project,  val of project, propability',
  `F_Val_02` double NULL DEFAULT NULL,
  `F_Val_03` double NULL DEFAULT NULL,
  `F_Val_04` double NULL DEFAULT NULL,
  `F_Val_05` double NULL DEFAULT NULL,
  `F_Val_00` double NULL DEFAULT NULL,
  `F_Val_06` double NULL DEFAULT NULL,
  `F_Val_07` double NULL DEFAULT NULL,
  `F_Val_08` double NULL DEFAULT NULL,
  `F_Val_09` double NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TPPRO_01`(`T_Code`) USING BTREE,
  INDEX `idx_TPPRO_02`(`T_Name`) USING BTREE,
  INDEX `idx_TPPRO_03`(`I_Type_01`) USING BTREE,
  INDEX `idx_TPPRO_04`(`T_Tag`) USING BTREE,
  INDEX `idx_TPPRO_05`(`I_Type_02`) USING BTREE,
  INDEX `idx_TPPRO_06`(`I_Group`) USING BTREE,
  INDEX `idx_TPPRO_07`(`I_Type_00`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1033 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_prj_project
-- ----------------------------
INSERT INTO `ta_prj_project` VALUES (939, 'STUWORK', 'ZenZobs', NULL, 939, 10, 11, 0, NULL, '<p>Market place for student looking for part time work</p>', '[]', NULL, 'STUWORK', 1, 2, '2023-07-10 08:58:47', '2023-11-06 08:46:17', '2023-07-01 07:00:00', '2023-08-31 19:00:00', 1, 1, 271, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (940, 'user-man_admin', 'Quản lý người dùng trang Admin', 939, 939, 10, 11, 1, NULL, 'Quản lý người dùng trang Admin', NULL, NULL, '', 4, 2, '2023-07-10 10:05:22', '2023-07-10 10:05:22', '2023-07-10 10:00:00', '2023-07-12 10:00:00', 1, 331, NULL, NULL, NULL, 150001, NULL, 100, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (941, 'PRJ-23071010-1486', 'Quản lý offer trang Admin', 939, 939, 10, 11, 1, NULL, '<p>Quản lý offer trang Admin</p>', '[]', NULL, '', 4, 2, '2023-07-10 10:08:42', '2023-07-10 10:08:42', '2023-07-10 10:05:00', '2023-07-12 10:05:00', 1, 331, NULL, NULL, NULL, 150001, NULL, 100, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (942, 'PRJ-23071010-4287', 'Auth - User', 939, 939, 10, 11, 1, NULL, '<p>- Sửa phần đăng nhập - đăng kí phía người dùng</p><p>- Sửa phần đăng nhập bằng Google, Facebook</p>', '[]', NULL, '', 4, 2, '2023-07-10 10:10:08', '2023-07-10 10:10:08', '2023-07-10 10:08:00', '2023-07-12 10:08:00', 1, 331, NULL, NULL, NULL, 150001, NULL, 100, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (943, 'PRJ-23071010-7745', 'Tìm kiếm - User', 939, 939, 10, 11, 1, NULL, '<p>Tìm kiếm theo các tiêu chí:</p><p>- Thời gian rảnh</p><p>- Giá theo giờ / theo danh sách công việc</p><p>- Điểm đánh giá</p><p>- Tiêu đề</p><p>- Lĩnh vực</p><p>- Địa chỉ</p><p>Sắp xếp theo:</p><p>- Địa chỉ</p><p>- Đánh giá</p><p>- Gần đây</p>', '[]', NULL, '', 1, 2, '2023-07-10 10:14:42', '2023-07-10 10:14:42', '2023-07-15 10:10:00', '2023-07-18 10:10:00', 1, 331, NULL, NULL, NULL, 150001, NULL, 50, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (944, 'PRJ-23071010-2109', 'Mapping', 939, 939, 10, 11, 1, NULL, '<p>- Người tìm việc: Mapping theo các tiêu chí như tìm kiếm</p><p>- Người đăng việc: Mapping theo từng bài đăng - theo các tiêu chí như tìm kiếm</p>', '[]', NULL, '', 1, 2, '2023-07-10 10:16:33', '2023-07-10 10:16:33', '2023-07-10 10:14:00', '2023-07-11 10:14:00', 1, 331, NULL, NULL, NULL, 150001, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (945, 'PRJ-23071010-9262', 'User Management - Admin: Sửa giao diện và thêm dữ liệu theo offer người tìm việc', 940, 939, 10, 11, 2, NULL, '<p>Sửa giao diện và thêm dữ liệu theo offer người tìm việc<br></p>', '[{\"item\":\"Thêm lĩnh vực\",\"stat\":2},{\"item\":\"Thêm thời gian rảnh\"},{\"item\":\"Thêm tiền\",\"stat\":2},{\"item\":\"Thêm vị trí\"}]', NULL, '', 4, 2, '2023-07-10 10:24:06', '2023-07-14 14:19:57', '2023-07-10 10:20:00', '2023-07-11 11:30:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 5995.85, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (946, 'PRJ-23071010-4858', 'Auth - User: Sửa đăng kí', 942, 939, 10, 11, 2, NULL, '<p>Sửa đăng kí<br></p>', '[]', NULL, '', 4, 2, '2023-07-10 10:29:54', '2023-07-20 22:54:28', '2023-07-10 10:25:00', '2023-07-10 17:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 15144.566666666668, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (947, 'PRJ-23071010-9365', 'Auth - User: Sửa đăng nhập', 942, 939, 10, 11, 2, NULL, '<p>Sửa đăng nhập<br></p>', '[{\"item\":\"Sửa logic đăng nhập bằng email\",\"stat\":2},{\"item\":\"Sửa logic đăng nhập bằng google, facebook\"}]', NULL, '', 4, 2, '2023-07-10 10:31:51', '2023-07-19 08:23:38', '2023-07-10 10:29:00', '2023-07-12 17:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 12831.783333333333, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (948, 'PRJ-23071010-3552', 'Offer - Admin', 941, 939, 10, 11, 2, NULL, '<p>Sửa giao diện + thêm dữ liệu</p>', '[{\"item\":\"Thêm thời gian rảnh\"},{\"item\":\"Thêm địa điểm\"}]', NULL, 'offer-admin', 4, 2, '2023-07-10 10:35:11', '2023-07-20 12:09:40', '2023-07-10 10:32:00', '2023-07-11 17:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 14494.483333333334, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (949, 'PRJ-23071010-9347', 'Search - User: Giao diện + Hiển thị dữ liệu', 943, 939, 10, 11, 2, NULL, '<p><br></p>', '[{\"item\":\"Hiển thị ô tìm kiếm\",\"stat\":2},{\"item\":\"Hiển thị các filter\",\"stat\":2},{\"item\":\"Hiển thị danh sách công việc\",\"stat\":2}]', NULL, '', 4, 2, '2023-07-10 10:39:52', '2023-08-04 22:46:20', '2023-07-10 10:37:00', '2023-07-22 17:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 36726.46666666667, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (950, 'PRJ-23071010-9349', 'Search - User: Người đăng việc', 943, 939, 10, 11, 2, NULL, '<p>Hiển thị giao diện cho người đăng việc</p>', '[{\"item\":\"Hiển thị ô tìm kiếm\",\"stat\":2},{\"item\":\"Hiển thị các filter\",\"stat\":2},{\"item\":\"Hiển thị danh sách người ứng viên\",\"stat\":2}]', NULL, '', 7, 2, '2023-07-10 10:41:06', '2023-07-20 12:10:56', '2023-07-10 10:40:00', '2023-07-11 17:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 0, NULL, 14489.833333333334, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (951, 'PRJ-23071010-9207', 'Dashboard - User', 939, 939, 10, 11, 1, NULL, '<p>Hiển thị và update dữ liệu profile</p>', '[]', NULL, '', 1, 2, '2023-07-10 10:42:50', '2023-07-20 12:28:30', '2023-07-10 10:41:00', '2023-07-15 17:00:00', 1, 331, 331, NULL, NULL, 150001, NULL, 66.66666666666667, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (952, 'PRJ-23071010-6395', 'Profile - User', 951, 939, 10, 11, 2, NULL, '<p>Hiển thị giao diện</p>', '[{\"item\":\"Hiển thị các thông tin cơ bản\"},{\"item\":\"Hiển thị thời gian rảnh theo Daypilot\"},{\"item\":\"Hiển thị các nút để update dữ liệu\"}]', NULL, '', 4, 2, '2023-07-10 10:44:43', '2023-07-21 00:21:48', '2023-07-10 10:42:00', '2023-07-20 23:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 6562.533333333334, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (953, 'PRJ-23071223-6281', 'Auth - User: Duy trì đăng nhập', 942, 939, 10, 11, 2, NULL, '<p>Auth - User: Duy trì đăng nhập<br></p>', '[]', NULL, '', 4, 2, '2023-07-12 23:06:18', '2023-07-14 17:39:59', '2023-07-13 08:05:00', '2023-07-13 16:45:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 2553.6833333333334, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (954, 'PRJ-23071817-8413', 'Offer - User', 939, 939, 10, 11, 1, NULL, '<p>Offer - User<br></p>', '[]', NULL, '', 1, 2, '2023-07-18 17:08:19', '2023-07-18 17:08:19', '2023-07-18 17:08:00', '2023-07-22 17:08:00', 1, 331, NULL, NULL, NULL, 150001, NULL, 95, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (955, 'PRJ-23071817-8852', 'Offer - User', 954, 939, 10, 11, 2, NULL, '<p>Offer - User<br></p>', '[]', NULL, '', 4, 2, '2023-07-18 17:10:00', '2023-07-22 15:27:36', '2023-07-18 17:09:00', '2023-07-20 17:09:00', 1, 331, NULL, NULL, NULL, 150001, 0, 100, NULL, 5657.6, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (956, 'PRJ-23071916-5641', 'HomeMain', 939, 939, 10, 11, 1, NULL, '<p><b>HomeMain</b><br></p>', '[]', NULL, '', 4, 2, '2023-07-19 16:47:49', '2023-07-19 16:47:49', '2023-07-19 16:47:00', '2023-07-21 16:47:00', 1, 331, NULL, NULL, NULL, 150001, NULL, 100, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (957, 'PRJ-23071916-4968', 'HomeMain: Show Candidate', 956, 939, 10, 11, 2, NULL, '<p>HomeMain: Show Candidate<br></p>', '[]', NULL, '', 4, 2, '2023-07-19 16:49:13', '2023-07-23 23:24:16', '2023-07-19 16:47:00', '2023-07-20 23:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 6155.033333333334, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (958, 'PRJ-23072012-7938', 'Offer Detail: Phát triển giao diện và hiển thị dữ liệu', 954, 939, 10, 11, 2, NULL, '<p>Offer Detail: Phát triển giao diện và hiển thị dữ liệu<br></p>', '[]', NULL, '', 4, 2, '2023-07-20 12:18:31', '2023-11-16 16:27:07', '2023-08-19 12:17:00', '2023-08-25 12:17:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 171668.6, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (959, 'PRJ-23072012-5937', 'Show Offer Jobs', 956, 939, 10, 11, 2, NULL, '<p>Show Offer Jobs<br></p>', '[]', NULL, '', 4, 2, '2023-07-20 12:23:46', '2023-07-23 23:24:09', '2023-07-20 12:21:00', '2023-07-21 17:00:00', 1, 331, NULL, NULL, NULL, 150001, 0, 100, NULL, 4980.383333333333, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (960, 'PRJ-23072012-3604', 'Translate', 939, 939, 10, 11, 2, NULL, '<p>Tìm và thay đổi các bản dịch</p>', '[]', NULL, '', 4, 2, '2023-07-20 12:27:02', '2023-08-29 13:34:20', '2023-07-20 12:26:00', '2023-07-22 12:26:00', 1, 331, NULL, NULL, NULL, 150001, 0, 100, NULL, 57667.3, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (961, 'PRJ-23072012-2007', 'Favorite Jobs', 951, 939, 10, 11, 2, NULL, '<p>Favorite Jobs<br></p>', '[{\"item\":\"Hiển thị icon lưu khi đăng nhập\",\"stat\":2},{\"item\":\"Viết service lưu thông tin\"},{\"item\":\"Hiển thị công việc yêu thích ở dashboard\",\"stat\":2}]', NULL, '', 4, 2, '2023-07-20 12:31:23', '2023-07-30 11:41:35', '2023-07-20 12:28:00', '2023-07-23 17:00:00', 1, 331, 332, NULL, NULL, 150001, 0, 100, NULL, 14350.2, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (962, 'PRJ-23072215-6845', 'Offer: Process', 939, 939, 10, 11, 2, NULL, '<p>Offer: Process<br></p>', '[]', NULL, '', 4, 2, '2023-07-22 15:29:12', '2023-08-19 08:43:22', '2023-07-22 15:28:00', '2023-07-23 15:28:00', 1, 331, NULL, NULL, NULL, 150001, 0, 100, NULL, 39914.166666666664, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (963, 'PRJ-23081908-6627', 'ChatBox', 939, 939, 10, 11, 1, NULL, '<p>Xây dựng ChatBox gồm chatroom và chat mini trên từng trang</p>', '[]', NULL, '', 1, 4, '2023-08-19 08:46:26', '2023-08-19 08:46:26', '2023-08-19 08:44:00', '2023-08-31 08:44:00', 1, 331, NULL, NULL, NULL, 150001, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (964, 'PRJ-23081908-3339', 'Dev FE - Chat mini', 963, 939, 10, 11, 2, NULL, '<p>Xây dựng front-end cho chat mini ở từng trang</p><p>*Lưu ý: Xây dựng component để thêm vào CMSController</p>', '[]', NULL, '', 1, 2, '2023-08-19 08:50:19', '2023-08-19 08:50:19', '2023-08-19 08:47:00', '2023-08-22 08:47:00', 1, 331, NULL, NULL, NULL, 150001, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (965, 'PRJ-23081908-3695', 'Dev BO', 963, 939, 10, 11, 2, NULL, '<p>Xây dựng service cho chatbox</p><p>*Lưu ý: Dùng chung cho chatmini và chatroom</p>', '[]', NULL, '', 1, 2, '2023-08-19 08:51:41', '2023-08-19 09:05:50', '2023-08-20 08:50:00', '2023-08-25 08:50:00', 1, 331, 331, NULL, NULL, 150001, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (966, 'PRJ-23081909-5532', 'Change format Freetimes', 941, 939, 10, 11, 2, NULL, '<p>Sửa định dạng thêm và hiển thị cho thời gian rảnh của offer</p><p>*Lưu ý: </p><p>   - Sửa theo Stuwork đã làm sẵn</p><p>   - Date cũ có dạng \"dd/MM/yyyy\" sang dạng ngày trong tuần. Ví dụ: 19/08/2023 -> 6 (Thứ 7 trong tuần)</p>', '[{\"item\":\"Sửa định dạng date ở thêm và cập nhật\",\"stat\":2},{\"item\":\"Xem các thay đổi ở StuWork để sửa phần hiển thị cho thời gian rảnh\"}]', NULL, '', 4, 2, '2023-08-19 09:00:03', '2023-08-28 09:25:43', '2023-08-19 08:55:00', '2023-08-21 16:55:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 12985.666666666666, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (967, 'PRJ-23082303-3601', 'Dev QL Post (bài viết)', 939, 939, 10, 11, 2, NULL, '<p>- Div các bài viết mới nhất trong trang home</p><p>-<span style=\"background-color: transparent; font-size: 0.8125rem;\">Trang hiển thị danh sách các bài viết (pagination)</span></p><p>- Trang hiển thị chi tiết 1 bài viết</p>', '[]', NULL, '', 4, 3, '2023-08-23 03:32:24', '2023-08-31 08:57:02', '2023-08-23 07:00:00', '2023-08-26 19:00:00', 1, 271, NULL, NULL, NULL, 150001, 0, 100, NULL, 11844.633333333333, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (968, 'PRJ-23082303-8977', 'QL Người dùng (man)', 939, 939, 10, 11, 2, NULL, '<p>- Thêm người dùng là admin&nbsp;</p><p>- Thêm QL người dùng là cộng tác viên</p>', '[]', NULL, '', 4, 3, '2023-08-23 03:34:23', '2023-08-23 14:02:00', '2023-08-23 07:00:00', '2023-08-23 19:00:00', 1, 271, NULL, NULL, NULL, 150001, 0, 100, NULL, 627.6166666666667, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (969, 'PRJ-23082313-5281', 'Menu các yêu thích của tôi', 939, 939, 10, 11, 2, NULL, '<p>Hiện các yêu thích dưới dạng pagination.</p>', '[]', NULL, '', 4, 2, '2023-08-23 13:57:19', '2023-08-25 00:28:41', '2023-08-23 07:00:00', '2023-08-24 19:00:00', 1, 271, NULL, NULL, NULL, 150001, 0, 100, NULL, 2071.366666666667, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (970, 'PRJ-23082315-0791', 'Quản lý bài viết - Man', 939, 939, 10, 11, 1, NULL, '<p>Quản lý bài viết - Man<br></p>', '[]', NULL, '', 1, 2, '2023-08-23 15:04:07', '2023-08-23 15:04:07', '2023-08-23 15:03:00', '2023-08-24 15:03:00', 1, 331, NULL, NULL, NULL, 150001, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (971, 'PRJ-23082913-2993', 'Order', 939, 939, 10, 11, 1, NULL, '<p>Order<br></p>', '[]', NULL, '', 4, 2, '2023-08-29 13:27:21', '2023-08-29 13:27:21', '2023-08-29 13:27:00', '2023-08-30 13:27:00', 1, 331, NULL, NULL, NULL, 150001, NULL, 100, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (972, 'PRJ-23082913-3057', 'Order Job', 971, 939, 10, 11, 2, NULL, '<p>Order Job<br></p>', '[{\"item\":\"Ứng tuyển công việc\"},{\"item\":\"Lưu trữ dữ liệu\"},{\"item\":\"Sửa trạng thái ở mỗi giai đoạn\"}]', NULL, '', 4, 2, '2023-08-29 13:29:07', '2023-10-24 16:27:03', '2023-08-29 13:27:00', '2023-08-31 13:27:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 80817.93333333333, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (973, 'PRJ-23082913-3125', 'Messages', 939, 939, 10, 11, 1, NULL, '<p>* Offer:</p><p>- Rating sau khi làm việc (đánh giá sao, chọn cats, messages)</p><p>- Comment là con của rating</p><p>* Post:</p><p>- Chỉ comment</p>', '[]', NULL, '', 1, 2, '2023-08-29 13:32:07', '2023-08-31 10:25:20', '2023-08-29 13:29:00', '2023-08-30 13:29:00', 1, 331, 331, NULL, NULL, 150001, NULL, 95, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (974, 'PRJ-23082913-8382', 'Comment: Post', 973, 939, 10, 11, 2, NULL, '<p>Comment: Post<br></p>', '[{\"item\":\"Comment được lưu trong CMS\",\"stat\":2},{\"item\":\"Tạo tmpl, controller, transl\",\"stat\":2},{\"item\":\"Lấy dữ liệu từ TaMessage và hiển thị (2 tầng)\",\"stat\":2},{\"item\":\"Các chức năng như: Thêm, xóa, sửa(đối với ng sở hữu), report\",\"stat\":2}]', NULL, '', 4, 2, '2023-08-29 13:33:58', '2023-10-29 22:15:45', '2023-08-29 13:33:00', '2023-09-02 13:33:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 88361.78333333334, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (975, 'PRJ-23083022-5540', 'Change format Favorite', 939, 939, 10, 11, 2, NULL, '<p>Change format Favorite</p><p>Ví dụ: [{typ: \'POST\', ids: [1,3,5]}, {typ: \'OFF\', ids: [4,5,6]}] (Với mỗi user)</p>', '[{\"item\":\"Thông tin được lưu dạng dạng json\"},{\"item\":\"Gồm status và list Ids\"},{\"item\":\"Tạo thêm các trường có thể tồn tại trong Favorite như: Post, Offer, Comment, Rating, Message,...\"}]', NULL, '', 4, 2, '2023-08-30 22:22:02', '2023-10-24 15:08:39', '2023-08-30 22:18:00', '2023-10-24 22:18:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 78766.61666666667, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (976, 'PRJ-23083110-2084', 'Notification', 973, 939, 10, 11, 2, NULL, '<p>Notification<br></p>', '[]', NULL, '', 4, 2, '2023-08-31 10:26:18', '2023-09-13 17:10:43', '2023-08-31 10:25:00', '2023-09-03 10:25:00', 1, 331, NULL, NULL, NULL, 150001, 0, 100, NULL, 19124.416666666668, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (977, 'PRJ-23092519-8976', 'Sửa giao diện Favorite', 951, 939, 10, 11, 2, NULL, '<p>Client: Sửa giao diện Favorite&nbsp;<br></p>', '[{\"item\":\"Viết sql join để lấy dữ liệu favorite đi kèm offer\"},{\"item\":\"Lấy dữ liệu và hiển thị với giao diện mới\"}]', NULL, '', 7, 2, '2023-09-25 19:46:14', '2023-10-24 16:26:23', '2023-09-25 19:44:00', '2023-09-30 19:44:00', 1, 331, NULL, NULL, NULL, 150001, 0, 0, NULL, 41560.15, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (978, 'PRJ-23092816-3379', 'Chỉnh sửa lại giao diện Public', 939, 939, 10, 11, 2, NULL, '<p>Cần chỉnh sửa lại home page</p>', '[{\"item\":\"1. Dịch thuật\"},{\"item\":\"2. Menu tiếng việt và href đến các trang (Việc tìm người / Người tìm Việc)\"},{\"item\":\"3. Footer của trang (lược giản, dựa theo 1001Cv.com)\"},{\"item\":\"Binhding lại list categories\"},{\"item\":\"Xem lại cấu trúc CMS + HomePage  => CMS chỉ có header + footer không gì khác\"}]', NULL, '', 7, 2, '2023-09-28 16:48:38', '2023-11-23 15:49:22', '2023-09-28 07:00:00', '2023-09-28 19:00:00', 1, 271, 271, NULL, NULL, 150001, 0, 0, NULL, 80640.73333333334, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (980, 'PRJ-23102113-4149', 'Chỉnh sửa trang Blog', 939, 939, 10, 11, 2, NULL, '<p>Chỉnh sửa trang Blog<br></p>', '[{\"item\":\"1. Nội dung:\",\"stat\":2},{\"item\":\"- Chỉnh sửa font chữ\",\"stat\":2},{\"item\":\"- Thêm phần mục lục: Có thể chuyển đến các mục chính\",\"stat\":2},{\"item\":\"2. Bình luận:\",\"stat\":2},{\"item\":\"- Phần hiển thị: Tối giản lại mỗi ô bình luận. Hiện vừa tốn diện tích, nhìn vào vừa trống trãi. Có thể đưa tiêu đề hiển thị hợp lí.\",\"stat\":2},{\"item\":\"- Phần gửi: Hiển thị nút gửi bình luận(đã đăng nhập hoặc sau đăng nhập trực tiếp ở trang).\",\"stat\":2},{\"item\":\"- Phần trả lời: Sửa phần trả lời bình luận. Hiện thị 2-3 trả lời bên dưới(có nút để hiển thị thêm)\",\"stat\":2},{\"item\":\"3. Yêu thích:\",\"stat\":2},{\"item\":\"- Thêm phần upvote - downvote cho bài viết.\",\"stat\":2},{\"item\":\"- Tham khảo trang viblo để hiển thị\",\"stat\":2},{\"item\":\"4. Xóa phần các số cuối bài viết(số yêu thích, bình luận,...). Không cần hiển thị tổng số bình luận.\",\"stat\":2}]', NULL, '', 4, 3, '2023-10-21 13:27:57', '2023-11-16 16:26:59', '2023-10-21 07:00:00', '2023-10-28 19:00:00', 1, 331, 335, NULL, NULL, 150001, 0, 100, NULL, 37679.03333333333, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (981, 'PRJ-23102113-2711', 'Sửa giao diện Trang chủ', 939, 939, 10, 11, 2, NULL, '<p>Sửa giao diện Trang chủ<br></p>', '[{\"item\":\"1. Phần danh sách công việc\"},{\"item\":\"- Hiển thị ngày bắt đầu làm việc\",\"stat\":2},{\"item\":\"- Hiển thị kiểu công việc là theo giờ hay danh sách công việc. Có thể dùng tag chéo ở góc và theo 2 màu để phân biệt\",\"stat\":1},{\"item\":\"- Format lại số tiền (theo giờ thì sẽ có /h). Tiền giá trị lớn sẽ được chia khoảng \".\" mỗi 3 chữ số\",\"stat\":2},{\"item\":\"- Đối với tiền việt thì sẽ thêm \"k\", \"tr\", \"t\" thay cho \"đ\". Các loại tiền tệ khác thì sẽ có kí hiệu đi trước hoặc sau tiền.\",\"stat\":1},{\"item\":\"- Nhấn vào lĩnh vực sẽ chuyển sang trang tìm kiếm và tìm các công việc/người làm việc theo lĩnh vực đó\",\"stat\":2},{\"item\":\"2. Phần danh sách người tìm việc: Giống 3 chi tiết sau ở phần công việc\",\"stat\":1},{\"item\":\"- Sửa phần hiển thị ảnh đại diện, vị trí làm việc. Có thể chỉ hiển thị từ quận trở lên\",\"stat\":1},{\"item\":\"3. Tối giản phần footer\",\"stat\":2}]', NULL, '', 4, 2, '2023-10-21 13:43:04', '2023-11-11 08:35:10', '2023-10-21 07:00:00', '2023-11-03 19:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 29992.1, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (982, 'PRJ-23102113-9747', 'Phát triển UI trang cá nhân', 939, 939, 10, 11, 2, NULL, '<p>Phát triển UI trang cá nhân khi người khác xem<br></p>', '[{\"item\":\"1. Hiển thị được chi tiết các thông tin công khai của người dùng\"},{\"item\":\"2. Thiết kế giao diện dựa vào các thông tin trong database\"},{\"item\":\"3. Sử dụng template phù hợp, màu sắc dễ nhìn, đưa layout phù hợp\"}]', NULL, '', 2, 2, '2023-10-21 13:47:27', '2023-10-30 22:52:50', '2023-10-21 07:00:00', '2023-11-03 19:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 10, NULL, 6429.25, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (983, 'PRJ-23102415-8937', 'Hiển thị danh sách Favorite', 939, 939, 10, 11, 2, NULL, '<p>Hiển thị danh sách Favorite<br></p>', '[{\"item\":\"1. Gửi danh sách ID thông qua typ\",\"stat\":2},{\"item\":\"2. Lấy danh sách các loại Favorite và hiển thị\",\"stat\":2},{\"item\":\"3. Hiển thị Pagination 5-10 / page (sử dụng PaginationTool như ở trang tìm kiếm)\",\"stat\":2}]', NULL, '', 4, 2, '2023-10-24 15:08:33', '2023-11-16 16:27:10', '2023-10-24 07:00:00', '2023-10-31 19:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 33258.61666666667, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (984, 'PRJ-23103118-5034', 'Favorite', 939, 939, 10, 11, 2, NULL, '<p>Favorite<br></p>', '[{\"item\":\"1. Lấy favorite từ FavoriteCtrl và lưu vào App.data\",\"stat\":2},{\"item\":\"2. Xử lí thêm, xóa favorite ở các trang:\",\"stat\":1},{\"item\":\"- Đặt class(thêm + xóa), data-typ và data-id cho nút trong trang(có thể nhiều nút)\",\"stat\":2},{\"item\":\"- Thực hiện binding nút và thay đổi giao diện khi nhấn\",\"stat\":1},{\"item\":\"- Sau khi nhấn sẽ update favorite trong App.data và gửi API merge vào DB\"}]', NULL, '', 4, 2, '2023-10-31 18:23:30', '2023-11-16 16:27:09', '2023-10-31 07:00:00', '2023-10-01 19:00:00', 1, 331, 340, NULL, NULL, 150001, 0, 100, NULL, 22923.65, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (986, 'PRJ-23111108-4056', 'Bug', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231111/1699686519037_image.png\"><br></p>', '[{\"item\":\"fix a\"},{\"item\":\"fix b\"}]', NULL, '', 7, 2, '2023-11-11 08:09:24', '2023-11-11 08:33:06', '2023-11-11 07:00:00', '2023-11-11 19:00:00', 1, 341, NULL, NULL, NULL, 150001, 0, 0, NULL, 23.7, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (987, 'PRJ-23111108-5558', 'Bug', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231111/1699688134420_image.png\"><br></p>', '[{\"item\":\"fix a\"},{\"item\":\"fix b\"}]', NULL, '', 7, 2, '2023-11-11 08:36:13', '2023-11-11 08:36:51', '2023-11-11 07:00:00', '2023-11-11 19:00:00', 1, 341, NULL, NULL, NULL, 150001, 0, 0, NULL, 0.6333333333333333, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (988, 'TES-23111716-3633', 'Footer: Tối giản', 939, 939, 30, 1, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231117/1700235084490_image.png\"><br></p>', NULL, NULL, NULL, 0, NULL, '2023-11-17 16:32:02', '2023-11-17 16:32:02', NULL, NULL, 1, 331, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (989, 'TES-23111717-8080', 'Update English language CMS', 939, 939, 30, 1, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231117/1700237106803_image.png\"><br></p>', NULL, NULL, NULL, 0, NULL, '2023-11-17 17:05:29', '2023-11-17 17:05:29', NULL, NULL, 1, 331, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (990, 'TES-23111813-0570', 'Update Favorite Main: Show count in header', 939, 939, 30, 1, 1, NULL, '<p><img src=\"/files/_unkown/1/-1/20231118/1700310756305_image.png\"><br></p>', NULL, NULL, NULL, 0, NULL, '2023-11-18 13:33:22', '2023-11-18 13:33:22', NULL, NULL, 1, 331, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (991, 'TES-23111813-5768', 'Update sex + monTyp when change CMSOfferWork', 939, 939, 30, 1, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231118/1700311343875_image.png\"><br></p>', NULL, NULL, NULL, 0, NULL, '2023-11-18 13:42:44', '2023-11-18 13:42:44', NULL, NULL, 1, 331, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (992, 'TES-23111813-8752', 'Update offer todos', 939, 939, 30, 1, 1, NULL, '<p><img src=\"/files/_unkown/1/-1/20231118/1700311730693_image.png\"><br></p>', NULL, NULL, NULL, 0, NULL, '2023-11-18 13:49:08', '2023-11-18 13:49:08', NULL, NULL, 1, 331, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (993, 'TES-23111813-5235', 'Update Time CMSOfferWork', 939, 939, 30, 1, 1, NULL, '<p><img src=\"/files/_unkown/1/-1/20231118/1700312026597_image.png\"><br></p>', NULL, NULL, NULL, 0, NULL, '2023-11-18 13:54:14', '2023-11-18 13:54:14', NULL, NULL, 1, 331, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (994, 'TES-23111814-1997', 'ád', 939, 939, 30, 1, 1, NULL, '<p>ád</p>', NULL, NULL, NULL, 6, NULL, '2023-11-18 14:54:54', '2023-11-18 14:55:01', NULL, NULL, 1, 331, NULL, NULL, NULL, NULL, 0, 0, NULL, 0, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (995, 'TES-23111814-3919', 'Dashboard: Revise the password update logic', 939, 939, 30, 1, 1, NULL, '<p>Mật khẩu cũ phải tự nhập vào.</p>', '[]', NULL, NULL, 0, NULL, '2023-11-18 14:59:18', '2023-11-19 04:46:43', NULL, NULL, 1, 340, 340, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (996, 'TES-23111815-7988', 'Home: Fix uName + image blog', 939, 939, 30, 1, 1, NULL, '<p><img src=\"/files/_unkown/1/-1/20231118/1700316002592_image.png\"><br></p>', NULL, NULL, NULL, 0, NULL, '2023-11-18 15:00:08', '2023-11-18 15:00:08', NULL, NULL, 1, 335, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (997, 'TES-23111815-8729', 'Home: Fix path offer', 939, 939, 30, 1, 1, NULL, '<p><img src=\"/files/_unkown/1/-1/20231118/1700318760005_image.png\"><br></p>', NULL, NULL, NULL, 0, NULL, '2023-11-18 15:46:06', '2023-11-18 15:46:06', NULL, NULL, 1, 335, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (998, 'TES-23111815-5049', 'Home: Fix path show all offer', 939, 939, 30, 1, 1, NULL, '<p><img src=\"/files/_unkown/1/-1/20231118/1700319486955_image.png\"><br></p>', NULL, NULL, NULL, 0, NULL, '2023-11-18 15:58:25', '2023-11-18 15:58:25', NULL, NULL, 1, 335, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (999, 'TES-23111909-6670', 'Recently Offers', 939, 939, 30, 1, 1, NULL, '<p><img src=\"/files/_unkown/1/-1/20231119/1700381697689_image.png\"><br></p>', NULL, NULL, NULL, 0, NULL, '2023-11-19 09:15:43', '2023-11-19 09:15:43', NULL, NULL, 1, 331, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1000, 'TES-23111909-1924', 'Fix image category in home page', 939, 939, 30, 1, 1, NULL, '<p><img src=\"/files/_unkown/1/-1/20231119/1700382606260_image.png\"><br></p>', NULL, NULL, NULL, 0, NULL, '2023-11-19 09:30:22', '2023-11-19 09:30:22', NULL, NULL, 1, 335, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1001, 'SPR-23111909-4014', 'Sprint 01', 939, 939, 100, NULL, NULL, NULL, '', NULL, NULL, NULL, 1, 2, '2023-11-19 09:39:07', '2023-11-19 09:39:07', '2023-11-20 07:00:00', '2023-11-26 19:00:00', 1, 331, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1002, 'PRJ-23111909-7815', 'Fix: Dashboard + OfferDetail', 939, 939, 10, 11, 2, NULL, '<p>Fix: Dashboard + OfferDetail<br></p>', '[{\"item\":\"Cập nhật mật khẩu\",\"stat\":2},{\"item\":\"Click vào job trong favorite chuyển hướng\",\"stat\":2},{\"item\":\"Click vào bài đăng trong favorite chuyển hướng\",\"stat\":2},{\"item\":\"Click vào pagination_noajax bị reload\",\"stat\":2},{\"item\":\"Fix moneyFormat handlebars helper\",\"stat\":2},{\"item\":\"Fix chuyển tab và show tab khi vào dashboard\",\"stat\":2},{\"item\":\"Thêm và hiển thị favorite\",\"stat\":2},{\"item\":\"OfferDetail: Sửa hiển thị giới tính và độ tuổi\",\"stat\":2},{\"item\":\"Thêm helper switch-case\",\"stat\":2},{\"item\":\"Cập nhật link mạng xã hội\",\"stat\":2},{\"item\":\"Hủy yêu thích ở dashboard\",\"stat\":2},{\"item\":\"Hủy apply công việc ở dashboard\",\"stat\":2}]', NULL, '', 3, 2, '2023-11-19 09:48:18', '2023-12-05 03:59:10', '2023-11-19 07:00:00', '2023-11-21 20:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 90, NULL, 22690.866666666665, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1003, 'PRJ-23111909-6635', 'Fix: Home + Blog List + Blog Detail', 939, 939, 10, 11, 2, NULL, '<p>Fix: Home + Blog List + Blog Detail<br></p>', '[{\"item\":\"Đường dẫn offer ở trang home\",\"stat\":2},{\"item\":\"Ảnh blog trang home\",\"stat\":2},{\"item\":\"Tên user đăng blog ở trang home\",\"stat\":2},{\"item\":\"Button hiên thị tất cả offer ở trang home\",\"stat\":2},{\"item\":\"Button hiển thị tất cả blog ở trang home\",\"stat\":2},{\"item\":\"Ảnh offer ở trang home\",\"stat\":2}]', NULL, '', 4, 2, '2023-11-19 09:48:56', '2024-01-15 15:32:25', '2023-11-19 07:00:00', '2023-11-21 20:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 82423.48333333334, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1004, 'TES-23111911-2674', 'Fix dyamic sorting search', 939, 939, 30, 1, 1, NULL, '<p><img src=\"/files/_unkown/1/-1/20231119/1700389736669_image.png\"><br></p>', NULL, NULL, NULL, 0, NULL, '2023-11-19 11:29:23', '2023-11-19 11:29:23', NULL, NULL, 1, 331, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1005, 'PRJ-23111911-6080', 'Fix CMS + Search', 939, 939, 10, 11, 2, NULL, '<p>Fix CMS + Search<br></p>', '[{\"item\":\"CMS: Tối giản Footer\",\"stat\":2},{\"item\":\"CMS: Cập nhập tiếng anh\",\"stat\":2},{\"item\":\"Search: Offer đã xem\",\"stat\":2},{\"item\":\"Search: Sửa dyamic sort \",\"stat\":2},{\"item\":\"CMS: Hiển thị số lượng favorite\",\"stat\":2},{\"item\":\"CMS: Cập nhật attr sex + monTyp cho OfferWork\",\"stat\":2},{\"item\":\"Search: Tạo thêm trang tìm kiếm theo người nhận việc\",\"stat\":2},{\"item\":\"Search: Mapping hiển thị nút show danh sách thời gian phù hợp\",\"stat\":2},{\"item\":\"Search: Cập nhật tiếng anh\",\"stat\":2},{\"item\":\"Cập nhật logo\",\"stat\":2}]', NULL, '', 4, 2, '2023-11-19 11:33:09', '2023-11-21 16:54:37', '2023-11-19 07:00:00', '2023-11-21 20:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 3201.4666666666667, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1006, 'TES-23111911-3575', 'Search + OfferDetail: Update translation', 939, 939, 30, 1, 1, NULL, '<p><img src=\"/files/_unkown/1/-1/20231119/1700390972986_image.png\">ar<br></p>', NULL, NULL, NULL, 0, NULL, '2023-11-19 11:50:11', '2023-11-19 11:50:11', NULL, NULL, 1, 331, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1007, 'PRJ-23112312-4602', 'Phát triển UI Popup Candidate', 939, 939, 10, 11, 2, NULL, 'Hiển thị UI khi hover vào mỗi candidate để hiển thị biểu đồ radar các đánh giá', '[{\"item\":\"Viết template khung hiển thị\",\"stat\":2},{\"item\":\"Khi hover vào mỗi candidate thì truyền dữ liệu vào tmpl và thêm vào html\",\"stat\":2},{\"item\":\"Khi hover từ lần 2 trở đi thì chỉ cần hiện ra tmpl của candidate đã được phân sẵn ở lần hiển thị đầu\",\"stat\":2},{\"item\":\"Lưu ý: Hiển thị tùy trái phải tùy theo vị trí của offer đó. Sao cho đủ chỗ hiển thị, dễ nhìn(Có thể sử dụng thư viện trợ giúp)\",\"stat\":2},{\"item\":\"Đưa vào tất cả mọi nơi có hiển thị offer candidate\",\"stat\":2},{\"item\":\"Thêm icon để biết đang hiển thị cho candidate nào. Có thể là dấu >\",\"stat\":2},{\"item\":\"Cập nhật thêm về giao diện, lớn nhất, nhỏ nhất, chart\",\"stat\":2},{\"item\":\"*Lưu ý: Đã sửa đổi cách đưa dữ liệu vào để hiển thị. Đưa danh sách offer vào hàm do_lc_show() rồi find\",\"stat\":2},{\"item\":\"Chỉnh sửa thêm các transl ở trang Home và các trang Blog\",\"stat\":1}]', NULL, '', 3, 2, '2023-11-23 12:14:38', '2023-12-04 10:49:54', '2023-11-23 07:00:00', '2023-11-29 23:00:00', 1, 331, 335, NULL, NULL, 150001, 0, 90, NULL, 15755.266666666666, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1008, 'PRJ-23112312-9408', 'Đưa ra các tiêu chí đánh giá cho offer candidate - Fake + Lấy dữ liệu - Hiển thị ra biểu đồ', 939, 939, 10, 11, 2, NULL, '<p>Đưa ra các tiêu chí đánh giá cho offer candidate - Fake + Lấy dữ liệu<br></p>', '[{\"item\":\"Đưa ra 5 tiêu chí để bên thuê đánh giá khi candidate làm xong việc\",\"stat\":2},{\"item\":\"Fake một số dữ liệu của candidate\",\"stat\":2},{\"item\":\"Lấy thêm các trường dữ liệu khi lấy dữ liệu về candidate\",\"stat\":2},{\"item\":\"Hiển thị dữ liệu ra biểu đồ radar\",\"stat\":2},{\"item\":\"*Lưu ý: Giao diện radar cần đẹp mắt. Các tiêu chí phải hợp lí, bổ trợ cho nhau để đánh giá được candidate\",\"stat\":2},{\"item\":\"Tự động cập nhật đánh giá cho offer mỗi tối\",\"stat\":2}]', NULL, '', 4, 2, '2023-11-23 12:18:03', '2023-11-28 16:04:35', '2023-11-23 07:00:00', '2023-11-25 19:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 7426.533333333334, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1009, 'PRJ-23112811-0856', 'Dashboard: Sửa tab tin tuyển dụng', 939, 939, 10, 11, 2, NULL, '<p>Đối với Người thuê: Tin tuyển dụng là danh sách các offer của họ</p><p>Đối với Người làm: Tin tuyển dụng là danh sách các offer họ apply</p><p>Tạo thêm tabOrder và kiểm tra nếu là Người làm thì hiển thị tabOrder để click còn nếu là Người thuê thì hiển thị tabOffer để click</p>', '[{\"item\":\"Tạo thêm tabOrder \",\"stat\":2},{\"item\":\"Kiểm tra nếu là Người làm thì hiển thị tabOrder để click còn nếu là Người thuê thì hiển thị tabOffer để click\",\"stat\":2},{\"item\":\"TabOrder: Lấy thông tin từ order và hiển thị\",\"stat\":2},{\"item\":\"TabOffer: Lấy thông tin danh sách offer được tạo của Người thuê và hiển thị\",\"stat\":2}]', NULL, '', 3, 2, '2023-11-28 11:58:10', '2023-12-04 13:17:33', '2023-11-29 07:00:00', '2023-12-01 19:00:00', 1, 331, 340, NULL, NULL, 150001, 0, 90, NULL, 8719.383333333333, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1010, 'PRJ-23112812-3197', 'Dashboard: Sửa khi đăng nhập và đăng xuất', 939, 939, 10, 11, 2, NULL, '<p>Dashboard: Sửa khi đăng nhập và đăng xuất<br></p>', '[{\"item\":\"Khi đăng nhập tại trang dashboard thì hiển thị giao diện\",\"stat\":2},{\"item\":\"Khi đăng xuất tại trang dashboard thì chuyển giao diện như lúc chưa đăng nhập\",\"stat\":2}]', NULL, '', 4, 2, '2023-11-28 12:01:09', '2023-12-04 13:39:16', '2023-12-02 07:00:00', '2023-12-03 07:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 100, NULL, 8738.116666666667, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1011, 'PRJ-23113016-6968', 'Test - Publ: CMS + Search(OfferWork + OfferCandidate)', 939, 939, 10, 11, 2, NULL, '<p>Test - Publ: CMS + Search(OfferWork + OfferCandidate)<br></p>', '[{\"item\":\"Test tất cả các chức năng, css, transl,... và ghi lại dù là những thứ nhỏ nhất\"},{\"item\":\"Lưu ý: Không nên chỉnh sửa\"}]', NULL, '', 2, 2, '2023-11-30 16:34:35', '2023-12-02 11:20:21', '2023-11-30 07:00:00', '2023-12-03 19:00:00', 1, 331, 331, NULL, NULL, 150001, 0, 10, NULL, 2565.766666666667, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1012, 'PRJ-23113016-1104', 'Test - Publ: HomeMain + Blog + OfferDetail', 939, 939, 10, 11, 2, NULL, '<p>Test - Publ: HomeMain + Blog + OfferDetail<br></p>', '[{\"item\":\"Test tất cả các chức năng, css, transl,... và ghi lại dù là những thứ nhỏ nhất\",\"stat\":2},{\"item\":\"Lưu ý: Không nên chỉnh sửa \",\"stat\":2}]', NULL, '', 4, 2, '2023-11-30 16:35:26', '2024-01-17 08:22:06', '2023-11-30 07:00:00', '2023-12-03 19:00:00', 1, 331, 342, NULL, NULL, 150001, 0, 100, NULL, 68626.66666666667, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1013, 'PRJ-23120308-4746', 'home page candidate', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231203/1701587306956_image.jpg\"><br></p>', '[]', NULL, '', 3, 2, '2023-12-03 08:09:15', '2023-12-04 16:06:42', '2023-12-03 07:00:00', '2023-12-03 19:00:00', 1, 341, NULL, NULL, NULL, 150001, 0, 90, NULL, 1917.45, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1014, 'PRJ-23120308-9759', 'blog list', 939, 939, 10, 11, 2, NULL, '<p><span id=\"docs-internal-guid-ccf0e4d2-7fff-d09b-de85-7d452b2600ef\"><span style=\"font-size: 11pt; font-family: Arial, sans-serif; color: rgb(0, 0, 0); background-color: transparent; font-variant-numeric: normal; font-variant-east-asian: normal; font-variant-alternates: normal; font-variant-position: normal; vertical-align: baseline; white-space-collapse: preserve;\"><span style=\"border:none;display:inline-block;overflow:hidden;width:602px;height:300px;\"><img src=\"https://lh7-us.googleusercontent.com/q4GrNzZpYt1CkUb9HBAJXXEvq8eRybeVM8Igcz98J5S9XzRSWcIPhUS6snOku-s5FAKh41lsujQX4HIrlmzQ701_Jlgdkckqz9WDrM5UMNFbz-N3dGyRAkmkE6EKZViQ5CiBHG-Ur2MdC6zS7jMRKl0\" width=\"602\" height=\"300\" style=\"margin-left:0px;margin-top:0px;\"></span></span></span><br></p>', '[]', NULL, '', 3, 2, '2023-12-03 08:10:32', '2023-12-04 10:38:47', '2023-12-03 07:00:00', '2023-12-03 19:00:00', 1, 341, NULL, NULL, NULL, 150001, 0, 90, NULL, 1588.25, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1015, 'PRJ-23120308-9288', 'error link + ui', 939, 939, 10, 11, 2, NULL, '<p><span id=\"docs-internal-guid-d6b05656-7fff-5a82-f986-e1abf8ba0a4f\"><span style=\"font-size: 11pt; font-family: Arial, sans-serif; color: rgb(0, 0, 0); background-color: transparent; font-variant-numeric: normal; font-variant-east-asian: normal; font-variant-alternates: normal; font-variant-position: normal; vertical-align: baseline; white-space-collapse: preserve;\"><span style=\"border:none;display:inline-block;overflow:hidden;width:602px;height:304px;\"><img src=\"https://lh7-us.googleusercontent.com/Xdhyh5KEBNZP3cTX5Wq1wf_iYLqyAunq3weXmB6jopa7ytxPo0v1igf-GOGGNxfqsDz4cd_BEkHSlnkKdZj6h7B6nBFaHY4wxrUuxp7DpV-U1FfjS6-xB9Qa_FfpzdForqWhLnBwewfcjQXGcl6GfKs\" width=\"602\" height=\"304\" style=\"margin-left:0px;margin-top:0px;\"></span></span></span><br></p>', '[]', NULL, '', 3, 2, '2023-12-03 08:12:27', '2023-12-04 10:37:46', '2023-12-03 07:00:00', '2023-12-03 19:00:00', 1, 341, NULL, NULL, NULL, 150001, 0, 90, NULL, 1585.3166666666666, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1016, 'PRJ-23120308-2795', 'error translate and link', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231203/1701587633737_image.jpg\"><br></p>', '[]', NULL, '', 7, 2, '2023-12-03 08:16:00', '2023-12-03 10:26:46', '2023-12-03 07:00:00', '2023-12-03 19:00:00', 1, 341, NULL, NULL, NULL, 150001, 0, 0, NULL, 130.76666666666668, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1017, 'PRJ-23120308-5915', 'can\'t comment', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231203/1701587993389_image.jpg\"><br></p>', '[]', NULL, '', 3, 2, '2023-12-03 08:20:22', '2023-12-04 16:02:47', '2023-12-03 07:00:00', '2023-12-03 19:00:00', 1, 341, NULL, NULL, NULL, 150001, 0, 90, NULL, 1902.4166666666667, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1018, 'PRJ-23120308-0263', 'publish post error translate', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231203/1701588128649_image.jpg\"><br></p>', '[]', NULL, '', 7, 2, '2023-12-03 08:23:05', '2023-12-03 10:27:36', '2023-12-03 07:00:00', '2023-12-03 19:00:00', 1, 341, NULL, NULL, NULL, 150001, 0, 0, NULL, 124.51666666666667, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1019, 'PRJ-23120308-4457', 'askew ui', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231203/1701588425087_image.jpg\"><br></p>', '[]', NULL, '', 7, 2, '2023-12-03 08:28:16', '2023-12-03 10:27:47', '2023-12-03 07:00:00', '2023-12-03 19:00:00', 1, 341, NULL, NULL, NULL, 150001, 0, 0, NULL, 119.51666666666667, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1020, 'PRJ-23120308-5796', 'it is cleared automatically after finishing input.', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231203/1701588618715_image.jpg\"><br></p>', '[]', NULL, '', 7, 2, '2023-12-03 08:31:30', '2023-12-05 18:28:35', '2023-12-03 07:00:00', '2023-12-03 19:00:00', 1, 341, NULL, NULL, NULL, 150001, 0, 0, NULL, 3477.0833333333335, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1021, 'PRJ-23120308-3411', 'error translate when using English', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231203/1701588908596_image.jpg\"><br></p>', '[]', NULL, '', 4, 2, '2023-12-03 08:35:28', '2023-12-04 13:03:41', '2023-12-03 07:00:00', '2023-12-03 19:00:00', 1, 341, NULL, NULL, NULL, 150001, 0, 100, NULL, 1708.2166666666667, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1022, 'PRJ-23120310-4608', 'offer work and candidate.', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231203/1701596569102_image.png\"><br></p>', '[{\"item\":\"xắp sếp ko chọn được\",\"stat\":2},{\"item\":\"tiếng việt đơn vị tiền VND\"}]', NULL, '', 4, 2, '2023-12-03 10:44:07', '2023-12-04 13:04:20', '2023-12-03 07:00:00', '2023-12-03 19:00:00', 1, 341, 341, NULL, NULL, 150001, 0, 100, NULL, 1580.2166666666667, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1023, 'PRJ-23120403-7012', 'Home: Chỉnh sửa thành giao diện mới', 939, 939, 10, 11, 2, NULL, '<p>Home: Chỉnh sửa thành giao diện mới<br></p>', '[{\"item\":\"- Chỉnh sửa danh sách jobs - với filter ở phía trên + pagination\"},{\"item\":\"- Chỉnh sửa danh sách người làm - hiển thị 2 hàng 3 cột với pagination\"},{\"item\":\"- Chỉnh sửa danh sách blog\"},{\"item\":\"* Lưu ý: Tất cả tham khảo theo tmpl Jobcy - với người làm và blog tìm các item phù hợp và tối ưu UI ngắn gọn nhất có thể\"}]', NULL, '', 4, 2, '2023-12-04 03:54:29', '2024-01-15 15:32:23', '2023-12-04 07:00:00', '2023-12-06 19:00:00', 1, 331, NULL, NULL, NULL, 150001, 0, 100, NULL, 61177.9, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1024, 'PRJ-23120403-5755', 'Dashboard: Chỉnh sửa thành giao diện mới', 939, 939, 10, 11, 2, NULL, '<p>Dashboard: Chỉnh sửa thành giao diện mới<br></p>', '[{\"item\":\"Chỉnh sửa theo Jobcy - Phần lớn về font chữ, size chữ\"},{\"item\":\"Các ô input - select - button\"},{\"item\":\"Các offer trong yêu thích và order\"}]', NULL, '', 1, 2, '2023-12-04 03:56:42', '2023-12-04 03:56:42', '2023-12-04 07:00:00', '2023-12-06 19:00:00', 1, 331, NULL, NULL, NULL, 150001, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1025, 'PRJ-23120518-2970', 'Header đè form đăng nhập', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231205/1701796068696_image.jpg\"><img src=\"/files/_unkown/1/-1/20231205/1701796069484_image.jpg\"><img src=\"/files/_unkown/1/-1/20231205/1701796069774_image.jpg\"><br></p>', '[]', NULL, '', 1, 2, '2023-12-05 18:08:11', '2023-12-05 18:08:11', '2023-12-06 07:00:00', '2023-12-06 19:00:00', 1, 341, NULL, NULL, NULL, 150001, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1026, 'PRJ-23120518-8712', 'nhấn vào thêm công việc hiển thị popup ko đúng và sau khi nhấn bị đứng mà hình ko tương tác đc nữa', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231205/1701796232008_image.jpg\"><br></p>', '[]', NULL, '', 1, 2, '2023-12-05 18:11:54', '2023-12-05 18:13:16', '2023-12-06 07:00:00', '2023-12-06 19:00:00', 1, 341, 341, NULL, NULL, 150001, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1027, 'PRJ-23120518-6203', 'chữ công khai bị đè và ô mức lương bị lệch giao diện', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231205/1701796456230_image.jpg\"><br></p>', '[]', NULL, '', 1, 2, '2023-12-05 18:15:04', '2023-12-05 18:15:04', '2023-12-06 07:00:00', '2023-12-06 19:00:00', 1, 341, NULL, NULL, NULL, 150001, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1028, 'PRJ-23120518-4317', 'vẫn còn lỗi tìm theo api', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231205/1701796635263_image.png\"><br></p>', '[]', NULL, '', 1, 2, '2023-12-05 18:17:41', '2023-12-05 18:17:41', '2023-12-06 07:00:00', '2023-12-06 19:00:00', 1, 341, NULL, NULL, NULL, 150001, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1029, 'PRJ-23120518-9991', 'chỉnh sang tiếng anh một số cái vẫn còn hiện tiếng Việt', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231205/1701796741677_image.jpg\" style=\"background-color: transparent; font-size: 0.8125rem;\"></p><p><br></p><p><img src=\"/files/_unkown/1/-1/20231205/1701796779082_image.jpg\"></p><p><br></p><p><br></p>', '[]', NULL, '', 1, 2, '2023-12-05 18:20:50', '2023-12-05 18:20:50', '2023-12-06 07:00:00', '2023-12-06 19:00:00', 1, 341, NULL, NULL, NULL, 150001, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1030, 'PRJ-23120518-7445', 'lỗi dịch thuật', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231205/1701796958962_image.jpg\"><br></p>', '[]', NULL, '', 1, 2, '2023-12-05 18:22:58', '2023-12-05 18:22:58', '2023-12-06 07:00:00', '2023-12-06 19:00:00', 1, 341, NULL, NULL, NULL, 150001, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1031, 'PRJ-23120518-5206', 'lỗi icon', 939, 939, 10, 11, 2, NULL, '<p><img src=\"/files/_unkown/1/-1/20231205/1701797181922_image.jpg\"><br></p>', '[]', NULL, '', 1, 2, '2023-12-05 18:27:10', '2023-12-05 18:27:10', '2023-12-06 07:00:00', '2023-12-06 19:00:00', 1, 341, NULL, NULL, NULL, 150001, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ta_prj_project` VALUES (1032, '123', '123', NULL, 1032, 10, 12, 0, NULL, '<p>123asd</p>', NULL, NULL, '123', 1, 2, '2024-02-27 18:08:19', '2024-02-27 18:08:19', '2024-02-22 07:00:00', '2024-02-24 19:00:00', 1, 1, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for ta_sor_deal
-- ----------------------------
DROP TABLE IF EXISTS `ta_sor_deal`;
CREATE TABLE `ta_sor_deal`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Status` int(11) NULL DEFAULT NULL,
  `T_Code_01` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Code_02` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `I_Type_01` int(11) NULL DEFAULT NULL,
  `I_Type_02` int(11) NULL DEFAULT NULL,
  `I_Type_03` int(11) NULL DEFAULT NULL,
  `F_Val_00` double NULL DEFAULT NULL,
  `F_Val_01` double NULL DEFAULT NULL,
  `F_Val_02` double NULL DEFAULT NULL,
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `D_Date_01` datetime NULL DEFAULT NULL,
  `D_Date_02` datetime NULL DEFAULT NULL,
  `D_Date_03` datetime NULL DEFAULT NULL,
  `D_Date_04` datetime NULL DEFAULT NULL,
  `I_Aut_User_01` int(11) NULL DEFAULT NULL,
  `I_Aut_User_02` int(11) NULL DEFAULT NULL,
  `I_Per_Manager` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TSDEA_02`(`I_Per_Manager`) USING BTREE,
  INDEX `idx_TSDEA_06`(`I_Aut_User_01`) USING BTREE,
  INDEX `idx_TSDEA_08`(`D_Date_01`) USING BTREE,
  INDEX `idx_TSDEA_10`(`T_Code_01`) USING BTREE,
  INDEX `idx_TSDEA_11`(`T_Code_02`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_sor_deal
-- ----------------------------

-- ----------------------------
-- Table structure for ta_sor_order
-- ----------------------------
DROP TABLE IF EXISTS `ta_sor_order`;
CREATE TABLE `ta_sor_order`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `T_Code_01` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Code_02` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `I_Status` int(11) NULL DEFAULT NULL,
  `I_Type_01` int(11) NULL DEFAULT NULL,
  `I_Type_02` int(11) NULL DEFAULT NULL,
  `I_Type_03` int(11) NULL DEFAULT NULL,
  `I_Type_04` int(11) NULL DEFAULT NULL,
  `I_Type_05` int(11) NULL DEFAULT NULL,
  `F_Val_00` double NULL DEFAULT NULL,
  `F_Val_01` double NULL DEFAULT NULL,
  `F_Val_02` double NULL DEFAULT NULL,
  `F_Val_03` double NULL DEFAULT NULL,
  `F_Val_04` double NULL DEFAULT NULL,
  `F_Val_05` double NULL DEFAULT NULL,
  `F_Val_06` double NULL DEFAULT NULL,
  `F_Val_07` double NULL DEFAULT NULL,
  `F_Val_08` double NULL DEFAULT NULL,
  `F_Val_09` double NULL DEFAULT NULL,
  `F_Val_10` double NULL DEFAULT NULL,
  `T_Info_01` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `D_Date_01` datetime NULL DEFAULT NULL,
  `D_Date_02` datetime NULL DEFAULT NULL,
  `D_Date_03` datetime NULL DEFAULT NULL,
  `D_Date_04` datetime NULL DEFAULT NULL,
  `I_Aut_User_01` int(11) NULL DEFAULT NULL,
  `I_Aut_User_02` int(11) NULL DEFAULT NULL,
  `I_Entity_Type` int(11) NULL DEFAULT NULL,
  `I_Entity_ID_01` int(11) NULL DEFAULT NULL COMMENT 'Suplier',
  `I_Entity_ID_02` int(11) NULL DEFAULT NULL COMMENT 'Client',
  `I_Mat_Val_01` int(11) NULL DEFAULT NULL COMMENT 'Warehouse source',
  `I_Mat_Val_02` int(11) NULL DEFAULT NULL COMMENT 'Warehouse destination',
  `I_Per_Person_01` int(11) NULL DEFAULT NULL,
  `I_Per_Person_02` int(11) NULL DEFAULT NULL,
  `I_Per_Person_03` int(11) NULL DEFAULT NULL,
  `I_Per_Person_04` int(11) NULL DEFAULT NULL,
  `I_Per_Person_05` int(11) NULL DEFAULT NULL,
  `I_Parent` int(11) NULL DEFAULT NULL COMMENT 'Sor Order source (ex: transfert)',
  `I_Per_Manager` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TSORD_02`(`I_Per_Manager`) USING BTREE,
  INDEX `idx_TSORD_03`(`I_Per_Person_01`) USING BTREE,
  INDEX `idx_TSORD_04`(`I_Per_Person_02`) USING BTREE,
  INDEX `idx_TSORD_05`(`I_Per_Person_03`) USING BTREE,
  INDEX `idx_TSORD_06`(`I_Aut_User_01`) USING BTREE,
  INDEX `idx_TSORD_07`(`I_Aut_User_02`) USING BTREE,
  INDEX `idx_TSORD_08`(`D_Date_01`) USING BTREE,
  INDEX `idx_TSORD_09`(`D_Date_02`) USING BTREE,
  INDEX `idx_TSORD_10`(`T_Code_01`) USING BTREE,
  INDEX `idx_TSORD_11`(`I_Mat_Val_01`) USING BTREE,
  INDEX `idx_TSORD_12`(`I_Mat_Val_02`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_sor_order
-- ----------------------------

-- ----------------------------
-- Table structure for ta_sor_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `ta_sor_order_detail`;
CREATE TABLE `ta_sor_order_detail`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Sor_Order` int(11) NULL DEFAULT NULL,
  `I_Status` int(11) NULL DEFAULT NULL,
  `I_Priority` int(11) NULL DEFAULT NULL,
  `I_Mat_Material` int(11) NULL DEFAULT NULL,
  `I_Mat_Price` int(11) NULL DEFAULT NULL,
  `F_Val_00` double NULL DEFAULT NULL,
  `F_Val_01` double NULL DEFAULT NULL,
  `F_Val_02` double NULL DEFAULT NULL,
  `F_Val_03` double NULL DEFAULT NULL,
  `F_Val_04` double NULL DEFAULT NULL,
  `F_Val_05` double NULL DEFAULT NULL,
  `F_Val_06` double NULL DEFAULT NULL,
  `F_Val_07` double NULL DEFAULT NULL,
  `F_Val_08` double NULL DEFAULT NULL,
  `F_Val_09` double NULL DEFAULT NULL,
  `F_Val_10` double NULL DEFAULT NULL,
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_06` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_07` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_08` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_09` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_10` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'dt production',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'dt expiration',
  `I_Per_Person_01` int(11) NULL DEFAULT NULL,
  `I_Per_Person_02` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TSODE_02`(`I_Sor_Order`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_sor_order_detail
-- ----------------------------

-- ----------------------------
-- Table structure for ta_sys_audit
-- ----------------------------
DROP TABLE IF EXISTS `ta_sys_audit`;
CREATE TABLE `ta_sys_audit`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Aut_User` int(11) NULL DEFAULT NULL,
  `I_Val_01` int(11) NULL DEFAULT NULL COMMENT 'entity type',
  `I_Val_02` int(11) NULL DEFAULT NULL COMMENT 'entity id',
  `I_Val_03` int(11) NULL DEFAULT NULL COMMENT '1:new, 2:mod, 3:del',
  `D_Date` datetime NULL DEFAULT NULL,
  `T_Info_01` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'entity content',
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'extra info',
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TSAUD_01`(`I_Aut_User`) USING BTREE,
  INDEX `idx_TSAUD_02`(`I_Val_01`) USING BTREE,
  INDEX `idx_TSAUD_03`(`I_Val_02`) USING BTREE,
  INDEX `idx_TSAUD_04`(`I_Val_03`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_sys_audit
-- ----------------------------

-- ----------------------------
-- Table structure for ta_sys_exception
-- ----------------------------
DROP TABLE IF EXISTS `ta_sys_exception`;
CREATE TABLE `ta_sys_exception`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Aut_User` int(11) NULL DEFAULT NULL,
  `D_Date` datetime NULL DEFAULT NULL,
  `T_Info_01` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'T_Module',
  `T_Info_02` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'T_Class',
  `T_Info_03` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'T_Function',
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'T_Error',
  PRIMARY KEY (`I_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_sys_exception
-- ----------------------------

-- ----------------------------
-- Table structure for ta_sys_lock
-- ----------------------------
DROP TABLE IF EXISTS `ta_sys_lock`;
CREATE TABLE `ta_sys_lock`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Aut_User` int(11) NULL DEFAULT NULL,
  `I_Val_01` int(11) NULL DEFAULT NULL COMMENT 'object type, table reference',
  `I_Val_02` int(11) NULL DEFAULT NULL COMMENT 'object key: line id of object',
  `I_Status` int(11) NULL DEFAULT NULL,
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'date création of lock',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'date refresh of lock',
  `T_Info_01` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'user info',
  `T_Info_02` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'other info',
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TSLOC_01`(`I_Val_01`) USING BTREE,
  INDEX `idx_TSLOC_02`(`I_Val_02`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_sys_lock
-- ----------------------------

-- ----------------------------
-- Table structure for ta_tpy_category
-- ----------------------------
DROP TABLE IF EXISTS `ta_tpy_category`;
CREATE TABLE `ta_tpy_category`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `T_Name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `T_Code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `I_Type_01` int(11) NOT NULL COMMENT 'what the table will use this category, ex: I_Type_00= ID_TA_MAT_MATERIAL ',
  `I_Type_02` int(11) NULL DEFAULT NULL,
  `I_Type_03` int(11) NULL DEFAULT NULL,
  `I_Status` int(11) NULL DEFAULT NULL,
  `I_Parent` int(11) NULL DEFAULT NULL COMMENT 'the cat parent',
  `I_Per_Manager` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TTCAT_01`(`T_Name`) USING BTREE,
  INDEX `idx_TTCAT_02`(`I_Parent`) USING BTREE,
  INDEX `idx_TTCAT_11`(`I_Type_01`) USING BTREE,
  INDEX `idx_TTCAT_12`(`I_Type_02`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_tpy_category
-- ----------------------------

-- ----------------------------
-- Table structure for ta_tpy_category_entity
-- ----------------------------
DROP TABLE IF EXISTS `ta_tpy_category_entity`;
CREATE TABLE `ta_tpy_category_entity`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Tpy_Category` int(11) NOT NULL,
  `I_Entity_Type` int(11) NOT NULL,
  `I_Entity_ID` int(11) NOT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TTCEN_01`(`I_Entity_Type`, `I_Entity_ID`) USING BTREE,
  INDEX `idx_TTCEN_02`(`I_Tpy_Category`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_tpy_category_entity
-- ----------------------------

-- ----------------------------
-- Table structure for ta_tpy_document
-- ----------------------------
DROP TABLE IF EXISTS `ta_tpy_document`;
CREATE TABLE `ta_tpy_document`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Entity_Type` int(11) NULL DEFAULT NULL,
  `I_Entity_ID` int(11) NULL DEFAULT NULL,
  `I_Status` int(11) NULL DEFAULT NULL COMMENT '0: new, 1: ok, 2: ngung hoat dong, 10: xóa ',
  `I_Priority` int(11) NULL DEFAULT NULL COMMENT 'order of file in list if needed',
  `I_Type_01` int(11) NULL DEFAULT NULL COMMENT '1: media, 2: other',
  `I_Type_02` int(11) NULL DEFAULT NULL COMMENT '1: avatar, 2: img, 3: video, 10: all',
  `I_Type_03` int(11) NULL DEFAULT NULL COMMENT '1: public, 2: private',
  `I_Type_04` int(11) NULL DEFAULT NULL COMMENT 'other',
  `I_Type_05` int(11) NULL DEFAULT NULL COMMENT 'other',
  `F_Val_01` double NULL DEFAULT NULL COMMENT 'file size',
  `F_Val_02` double NULL DEFAULT NULL COMMENT 'other',
  `F_Val_03` double NULL DEFAULT NULL COMMENT 'other',
  `F_Val_04` double NULL DEFAULT NULL COMMENT 'other',
  `F_Val_05` double NULL DEFAULT NULL COMMENT 'other',
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'filename',
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'path real in server',
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'path url',
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'path real preview',
  `T_Info_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'path url preview',
  `T_Info_06` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_07` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_08` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_09` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'comment',
  `T_Info_10` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'path tmp',
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'Date new',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'Date mod',
  `D_Date_03` datetime NULL DEFAULT NULL COMMENT 'Date begin',
  `D_Date_04` datetime NULL DEFAULT NULL COMMENT 'Date end',
  `D_Date_05` datetime NULL DEFAULT NULL COMMENT 'Date other',
  `I_Aut_User_01` int(11) NULL DEFAULT NULL COMMENT 'user new',
  `I_Aut_User_02` int(11) NULL DEFAULT NULL COMMENT 'user mod',
  `I_Parent` int(11) NULL DEFAULT NULL COMMENT 'tpyDocument origin id when this doc is duplicated',
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TTDOC_00`(`I_Entity_Type`, `I_Entity_ID`) USING BTREE,
  INDEX `idx_TTDOC_01`(`I_Type_01`) USING BTREE,
  INDEX `idx_TTDOC_02`(`I_Type_02`) USING BTREE,
  INDEX `idx_TTDOC_03`(`I_Type_03`) USING BTREE,
  INDEX `idx_TTDOC_10`(`I_Parent`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_tpy_document
-- ----------------------------
INSERT INTO `ta_tpy_document` VALUES (1, -1, -1, 1, NULL, 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for ta_tpy_favorite
-- ----------------------------
DROP TABLE IF EXISTS `ta_tpy_favorite`;
CREATE TABLE `ta_tpy_favorite`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Aut_User` int(11) NOT NULL,
  `I_Entity_Type` int(11) NOT NULL,
  `I_Entity_ID` int(11) NOT NULL,
  `I_Priority` int(11) NULL DEFAULT NULL COMMENT 'order of display',
  `D_Date` datetime NULL DEFAULT NULL COMMENT 'date creation',
  `T_Title` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `T_Description` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `I_Type` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TTFAV_01`(`I_Aut_User`) USING BTREE,
  INDEX `idx_TTFAV_02`(`I_Entity_Type`, `I_Entity_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_tpy_favorite
-- ----------------------------

-- ----------------------------
-- Table structure for ta_tpy_information
-- ----------------------------
DROP TABLE IF EXISTS `ta_tpy_information`;
CREATE TABLE `ta_tpy_information`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Entity_Type` int(11) NULL DEFAULT NULL,
  `I_Entity_ID` int(11) NULL DEFAULT NULL,
  `I_Status` int(11) NULL DEFAULT NULL,
  `I_Priority` int(11) NULL DEFAULT NULL COMMENT 'order of file in list if needed',
  `I_Type_01` int(11) NULL DEFAULT NULL,
  `I_Type_02` int(11) NULL DEFAULT NULL,
  `I_Type_03` int(11) NULL DEFAULT NULL,
  `I_Type_04` int(11) NULL DEFAULT NULL,
  `I_Type_05` int(11) NULL DEFAULT NULL,
  `T_Info_01` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_02` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_03` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_04` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_05` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_06` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_07` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_08` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_09` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_10` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `F_Val_01` double NULL DEFAULT NULL,
  `F_Val_02` double NULL DEFAULT NULL,
  `F_Val_03` double NULL DEFAULT NULL,
  `F_Val_04` double NULL DEFAULT NULL,
  `F_Val_05` double NULL DEFAULT NULL,
  `F_Val_06` double NULL DEFAULT NULL,
  `F_Val_07` double NULL DEFAULT NULL,
  `F_Val_08` double NULL DEFAULT NULL,
  `F_Val_09` double NULL DEFAULT NULL,
  `F_Val_10` double NULL DEFAULT NULL,
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'Date new',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'Date mod',
  `I_Aut_User_01` int(11) NULL DEFAULT NULL COMMENT 'user new',
  `I_Aut_User_02` int(11) NULL DEFAULT NULL COMMENT 'user mod',
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TTINF_00`(`I_Entity_Type`, `I_Entity_ID`) USING BTREE,
  INDEX `idx_TTINF_01`(`I_Type_01`, `I_Type_02`, `I_Type_03`, `I_Type_04`, `I_Type_05`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_tpy_information
-- ----------------------------
INSERT INTO `ta_tpy_information` VALUES (1, 250000, 1032, 0, NULL, 100, NULL, NULL, NULL, NULL, '[{\"dt\":\"2024-02-27 18:08:25\",\"uID\":1,\"statTo\":1,\"stat\":1,\"typTab\":\"prj\",\"uName\":\"adm\",\"typ\":1,\"statFrom\":null,\"lev\":2}]', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-02-27 18:08:25', NULL, 1, NULL);

-- ----------------------------
-- Table structure for ta_tpy_relationship
-- ----------------------------
DROP TABLE IF EXISTS `ta_tpy_relationship`;
CREATE TABLE `ta_tpy_relationship`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Entity_Type_01` int(11) NOT NULL,
  `I_Entity_Type_02` int(11) NOT NULL,
  `I_Entity_ID_01` int(11) NOT NULL,
  `I_Entity_ID_02` int(11) NOT NULL,
  `D_Date_01` datetime NULL DEFAULT NULL COMMENT 'Date of new',
  `D_Date_02` datetime NULL DEFAULT NULL COMMENT 'Date of mod',
  `D_Date_03` datetime NULL DEFAULT NULL COMMENT 'Date of begin',
  `D_Date_04` datetime NULL DEFAULT NULL COMMENT 'Date of end',
  `I_Type` int(11) NULL DEFAULT NULL,
  `I_Status` int(11) NULL DEFAULT NULL COMMENT '0: new/backlog, 1: todo, 2: in progress, 3: stand by, 4: done, 5: closed, 6: unresolved',
  `I_Level` int(11) NULL DEFAULT NULL COMMENT 'level 0: manager, level 1: reporter, level 2: worker',
  `T_Comment` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TTREL_01`(`I_Entity_Type_01`, `I_Entity_ID_01`, `I_Entity_Type_02`, `I_Entity_ID_02`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_tpy_relationship
-- ----------------------------
INSERT INTO `ta_tpy_relationship` VALUES (1, 250000, 1000, 1032, 1, '2024-02-27 18:08:19', NULL, NULL, NULL, 1, NULL, 2, NULL);
INSERT INTO `ta_tpy_relationship` VALUES (2, 250000, 1000, 1032, 331, '2024-02-27 18:08:19', NULL, NULL, NULL, 1, NULL, 0, NULL);

-- ----------------------------
-- Table structure for ta_tpy_translation
-- ----------------------------
DROP TABLE IF EXISTS `ta_tpy_translation`;
CREATE TABLE `ta_tpy_translation`  (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Entity_Type` int(11) NOT NULL,
  `I_Entity_ID` int(11) NOT NULL,
  `I_Val_01` int(11) NULL DEFAULT NULL COMMENT 'lang option',
  `I_Val_02` int(11) NULL DEFAULT NULL COMMENT 'other option',
  `T_Info_01` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `T_Info_02` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`I_ID`) USING BTREE,
  INDEX `idx_TTTRA_01`(`I_Entity_Type`, `I_Entity_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ta_tpy_translation
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
