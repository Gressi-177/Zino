--
-- Host: localhost    Database: hnvTrainning
-- ------------------------------------------------------
--
-- Table structure for table `TA_MSG_MESSAGE`
--
DROP TABLE IF EXISTS `TA_MSG_MESSAGE_HISTORY`;
DROP TABLE IF EXISTS `TA_MSG_MESSAGE_STORE`;
DROP TABLE IF EXISTS `TA_MSG_MESSAGE`;

CREATE TABLE `TA_MSG_MESSAGE` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Status` int(11) NOT NULL COMMENT 'mac dinh trong chuong trinh',
  `I_Type_01` int(11) NOT NULL COMMENT 'type Msg: 1:email, 2:chat',
  `I_Type_02` int(11) DEFAULT NULL COMMENT 'type Noti: envoi sms, envoi email, in-app',
  
  `T_Info_01` varchar(2000) DEFAULT NULL COMMENT 'T_From',
  `T_Info_02` varchar(2000) DEFAULT NULL COMMENT 'T_To',
  `T_Info_03` varchar(2000) DEFAULT NULL COMMENT 'T_Title',
  
  `T_Info_04` longtext DEFAULT NULL COMMENT 'T_Body',
  `T_Info_05` longtext DEFAULT NULL,
  
  `I_Aut_User` int(11) DEFAULT NULL COMMENT 'user creates',
  `D_Date_01` datetime DEFAULT NULL,
  `D_Date_02` datetime DEFAULT NULL,
  
  `I_Entity_Type` int(11) DEFAULT NULL,
  `I_Entity_ID` int(11) DEFAULT NULL,

  PRIMARY KEY (`I_ID`),
  KEY `idx_TMMES_01` (`I_Aut_User`),
  KEY `idx_TMMES_02` (`I_Entity_Type`, `I_Entity_ID`),
  KEY `idx_TMMES_03` (`D_Date_01`, `D_Date_02`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




CREATE TABLE `TA_MSG_MESSAGE_HISTORY` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Msg_Message` int(11) NOT NULL,
  `I_Nso_Group` int(11) NOT NULL,
  `I_Status` int(11) NOT NULL,
  `I_Aut_User` int(11) NOT NULL COMMENT 'for what user/destination',
  `D_Date` datetime DEFAULT NULL COMMENT 'date of status',
  PRIMARY KEY (`I_ID`),
  KEY `idx_TMMES_01` (`I_Aut_User`),
  KEY `idx_TMNOT_02` (`I_Msg_Message`),
  KEY `idx_TMNOT_03` (`I_Nso_Group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




CREATE TABLE `TA_MSG_MESSAGE_STORE` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Status` int(11) DEFAULT NULL,
  `I_Type_01` int(11) DEFAULT NULL,
  `I_Type_02` int(11) DEFAULT NULL,
  
  `T_Info_01` varchar(2000) DEFAULT NULL COMMENT 'T_From',
  `T_Info_02` varchar(2000) DEFAULT NULL COMMENT 'T_To',
  `T_Info_03` longtext DEFAULT NULL COMMENT 'T_Content => jsonArray: [{msg}]',
  `T_Info_04` longtext DEFAULT NULL,
  `T_Info_05` longtext DEFAULT NULL,
  
  `D_Date_01` datetime DEFAULT NULL COMMENT 'dtBegin',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'dtEnd',
  `I_Entity_Type` int(11) DEFAULT NULL,
  `I_Entity_ID_01` int(11) DEFAULT NULL,
  `I_Entity_ID_02` int(11) DEFAULT NULL,
  PRIMARY KEY (`I_ID`),
  KEY `idx_TMMST_01` (`I_Entity_Type`),
  KEY `idx_TMMST_02` (`I_Entity_ID_01`),
  KEY `idx_TMMST_03` (`I_Entity_ID_02`),
  KEY `idx_TMMST_10` (`I_Type_01`),
  KEY `idx_TMMST_11` (`I_Type_02`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;

