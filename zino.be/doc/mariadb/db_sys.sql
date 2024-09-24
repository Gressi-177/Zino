--
-- Host: localhost    Database: hnvTrainning
-- ------------------------------------------------------
--
-- Table structure for table `TA_SYS_AUDIT`
--
DROP TABLE IF EXISTS `TA_SYS_LOCK`;
DROP TABLE IF EXISTS `TA_SYS_AUDIT`;
DROP TABLE IF EXISTS `TA_SYS_EXCEPTION`;

CREATE TABLE `TA_SYS_AUDIT` (
  `I_ID` 		int(11) 	NOT NULL AUTO_INCREMENT,
  `I_Aut_User` 	int(11) 	DEFAULT NULL,
  `I_Val_01` 	int(11) 	DEFAULT NULL COMMENT 'entity type',
  `I_Val_02` 	int(11) 	DEFAULT NULL COMMENT 'entity id',
  `I_Val_03` 	int(11) 	DEFAULT NULL COMMENT '1:new, 2:mod, 3:del',
  `D_Date` 	 	datetime 	DEFAULT NULL,
  `T_Info_01` 	longtext  	DEFAULT NULL COMMENT 'entity content',
  `T_Info_02` 	text  		DEFAULT NULL COMMENT 'extra info',
  `T_Info_03` 	text  		DEFAULT NULL,
  `T_Info_04` 	text  		DEFAULT NULL,
  PRIMARY KEY (`I_ID`),
  KEY `idx_TSAUD_01` (`I_Aut_User`),
  KEY `idx_TSAUD_02` (`I_Val_01`),
  KEY `idx_TSAUD_03` (`I_Val_02`),
  KEY `idx_TSAUD_04` (`I_Val_03`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



CREATE TABLE `TA_SYS_LOCK` (
  `I_ID` 		int(11) 		NOT NULL AUTO_INCREMENT,
  `I_Aut_User` 	int(11) 		DEFAULT NULL,
  `I_Val_01` 	int(11) 		DEFAULT NULL COMMENT 'object type, table reference',
  `I_Val_02` 	int(11) 		DEFAULT NULL COMMENT 'object key: line id of object',
  `I_Status` 	int(11) 		DEFAULT NULL,
  `D_Date_01` 	datetime 		DEFAULT NULL COMMENT 'date création of lock',
  `D_Date_02` 	datetime 		DEFAULT NULL COMMENT 'date refresh of lock',
  `T_Info_01` 	varchar(500)  	DEFAULT NULL COMMENT 'user info',
  `T_Info_02` 	varchar(500)  	DEFAULT NULL COMMENT 'other info',
  PRIMARY KEY (`I_ID`),
  KEY `idx_TSLOC_01` (`I_Val_01`),
  KEY `idx_TSLOC_02` (`I_Val_02`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



CREATE TABLE `TA_SYS_EXCEPTION` (
  `I_ID` 		int(11) 	 NOT NULL AUTO_INCREMENT,
  `I_Aut_User` 	int(11) 	 DEFAULT NULL,
  `D_Date` 		datetime 	 DEFAULT NULL,
  `T_Info_01` 	varchar(200) DEFAULT NULL COMMENT 'T_Module',
  `T_Info_02` 	varchar(200) DEFAULT NULL COMMENT 'T_Class',
  `T_Info_03` 	varchar(200) DEFAULT NULL COMMENT 'T_Function',
  `T_Info_04` 	text 		 DEFAULT NULL COMMENT 'T_Error',
  PRIMARY KEY (`I_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;



