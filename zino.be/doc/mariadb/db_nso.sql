DROP TABLE IF EXISTS `TA_NSO_OFFER`;
DROP TABLE IF EXISTS `TA_NSO_POST`;

DROP TABLE IF EXISTS `TA_NSO_GROUP_HISTORY`;
DROP TABLE IF EXISTS `TA_NSO_GROUP_MEMBER`;
DROP TABLE IF EXISTS `TA_NSO_GROUP`;

CREATE TABLE `TA_NSO_OFFER` (
  `I_ID` 		int(11) NOT NULL AUTO_INCREMENT,
  
  `T_Title` 	varchar(1000) DEFAULT NULL,
  
  `I_Status_01` int(11) DEFAULT NULL COMMENT 'status by admin',
  `I_Status_02` int(11) DEFAULT NULL COMMENT 'status by user: public or private',
  
  `I_Parent` 	int(11) DEFAULT NULL COMMENT 'id of main offer',
  
  `T_Code_01` 	varchar(100) DEFAULT NULL,
  `T_Code_02` 	varchar(100) DEFAULT NULL,
 
  `I_Type_01` 	int(11) DEFAULT NULL COMMENT 'type of offer like: work, candidate',
  `I_Type_02` 	int(11) DEFAULT NULL COMMENT 'type of lang',
  `I_Type_03` 	int(11) DEFAULT NULL,
  
  `T_Content_01` text DEFAULT NULL,
  `T_Content_02` text DEFAULT NULL,
  `T_Content_03` text DEFAULT NULL,
  `T_Content_04` text DEFAULT NULL,
  `T_Content_05` text DEFAULT NULL,
  `T_Content_06` text DEFAULT NULL,
  `T_Content_07` text DEFAULT NULL,
  `T_Content_08` text DEFAULT NULL,
  `T_Content_09` text DEFAULT NULL,
  `T_Content_10` text DEFAULT NULL COMMENT 'T_Comment use in adm mode',
 
  `T_Info_01` text DEFAULT NULL COMMENT 'Json for address: {lat:123, long:345, addr: "123 Hung Vuong, DA,VN"}',
  `T_Info_02` text DEFAULT NULL,
  `T_Info_03` text DEFAULT NULL,
  `T_Info_04` text DEFAULT NULL,
  `T_Info_05` text DEFAULT NULL,
  
  `D_Date_01` datetime DEFAULT NULL COMMENT 'ngày tạo',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'ngày sửa',
  `D_Date_03` datetime DEFAULT NULL COMMENT 'ngày bắt đầu',
  `D_Date_04` datetime DEFAULT NULL COMMENT 'ngày kết thúc',
  `D_Date_05` datetime DEFAULT NULL COMMENT 'ngày khác',
  
  `I_Aut_User_01` int(11) DEFAULT NULL COMMENT 'user created',
  `I_Aut_User_02` int(11) DEFAULT NULL COMMENT 'user modify/validate/delete',
  `I_Aut_User_03` int(11) DEFAULT NULL COMMENT 'other user',
  
  `I_Val_01` 		int(11) DEFAULT NULL COMMENT 'I_Entity_Type: type parent entity ...',
  `I_Val_02` 		int(11) DEFAULT NULL COMMENT 'I_Entity_ID: parent id...',
  `I_Val_03` 		int(11) DEFAULT NULL COMMENT 'I_Nb_Resp',
  `I_Val_04` 		int(11) DEFAULT NULL COMMENT 'khác',
  `I_Val_05` 		int(11) DEFAULT NULL COMMENT 'khác',
  
  `F_Val_01` double DEFAULT NULL COMMENT 'latitude for search',
  `F_Val_02` double DEFAULT NULL COMMENT 'long for search',
  `F_Val_03` double DEFAULT NULL,
  `F_Val_04` double DEFAULT NULL,
  `F_Val_05` double DEFAULT NULL COMMENT 'Rate count',
  `F_Val_06` double DEFAULT NULL COMMENT 'Rate 1',
  `F_Val_07` double DEFAULT NULL COMMENT 'Rate 2',
  `F_Val_08` double DEFAULT NULL COMMENT 'Rate 3',
  `F_Val_09` double DEFAULT NULL COMMENT 'Rate 4',
  `F_Val_10` double DEFAULT NULL COMMENT 'Rate 5',
  
  `I_Per_Manager` int(11) DEFAULT NULL COMMENT 'Per_Person moral manager',
  
  PRIMARY KEY (`I_ID`),
  
  KEY `idx_TNOFF_01` (`D_Date_01`),
  KEY `idx_TNOFF_02` (`D_Date_02`),
  KEY `idx_TNOFF_03` (`D_Date_03`),
  KEY `idx_TNOFF_04` (`D_Date_04`),
  
  KEY `idx_TNOFF_11` (`I_Aut_User_01`),
  KEY `idx_TNOFF_12` (`I_Aut_User_02`), 
  KEY `idx_TNOFF_13` (`I_Aut_User_03`),
    
  KEY `idx_TNOFF_20` (`T_Title`),
  KEY `idx_TNOFF_21` (`T_Code_01`),
  KEY `idx_TNOFF_22` (`T_Code_02`),
      
  KEY `idx_TNOFF_31` (`I_Parent`),
  KEY `idx_TNOFF_32` (`I_Val_01`,`I_Val_02`),
  KEY `idx_TNOFF_33` (`I_Per_Manager`),
  
  KEY `idx_TNOFF_41` (`I_Type_01`),
  KEY `idx_TNOFF_42` (`I_Type_02`),
  KEY `idx_TNOFF_43` (`I_Type_03`),
  KEY `idx_TNOFF_44` (`I_Status_01`),
  KEY `idx_TNOFF_45` (`I_Status_02`),
    
  KEY `idx_TNOFF_51` (`F_Val_01`),
  KEY `idx_TNOFF_52` (`F_Val_02`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------------
-- --------------------------------------------------------------------------
-- --------------------------------------------------------------------------

CREATE TABLE `TA_NSO_POST` (
  `I_ID` 		int(11) NOT NULL AUTO_INCREMENT,
  
  `T_Title` 	varchar(1000) DEFAULT NULL,
  
  `I_Status_01` int(11) DEFAULT NULL COMMENT 'status by admin',
  `I_Status_02` int(11) DEFAULT NULL COMMENT 'status by user: public or private',
  
  `I_Parent` 	int(11) DEFAULT NULL COMMENT 'id of main post for that this post response',
  
  `T_Code_01` 	varchar(100) DEFAULT NULL,
  `T_Code_02` 	varchar(100) DEFAULT NULL,
 
  `I_Type_01` 	int(11) DEFAULT NULL COMMENT 'type of post like: event, news, evaluation',
  `I_Type_02` 	int(11) DEFAULT NULL COMMENT 'type of lang',
  `I_Type_03` 	int(11) DEFAULT NULL,
  
  `T_Content_01` text DEFAULT NULL,
  `T_Content_02` text DEFAULT NULL,
  `T_Content_03` text DEFAULT NULL,
  `T_Content_04` text DEFAULT NULL,
  `T_Content_05` text DEFAULT NULL,
  `T_Content_06` text DEFAULT NULL,
  `T_Content_07` text DEFAULT NULL,
  `T_Content_08` text DEFAULT NULL,
  `T_Content_09` text DEFAULT NULL,
  `T_Content_10` text DEFAULT NULL COMMENT 'T_Comment use in adm mode',
 
  `T_Info_01` text DEFAULT NULL,
  `T_Info_02` text DEFAULT NULL,
  `T_Info_03` text DEFAULT NULL,
  `T_Info_04` text DEFAULT NULL,
  `T_Info_05` text DEFAULT NULL,
  
  `D_Date_01` datetime DEFAULT NULL COMMENT 'ngày tạo',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'ngày sửa',
  `D_Date_03` datetime DEFAULT NULL COMMENT 'ngày bắt đầu',
  `D_Date_04` datetime DEFAULT NULL COMMENT 'ngày kết thúc',
  `D_Date_05` datetime DEFAULT NULL COMMENT 'ngày khác',
  
  `I_Aut_User_01` int(11) DEFAULT NULL,
  `I_Aut_User_02` int(11) DEFAULT NULL,
  
  `I_Val_01` 		int(11) DEFAULT NULL COMMENT 'I_Entity_Type: type offer, area ...',
  `I_Val_02` 		int(11) DEFAULT NULL COMMENT 'I_Entity_ID: offer id, area id...',
  `I_Val_03` 		int(11) DEFAULT NULL COMMENT 'I_Nb_Resp',
  `I_Val_04` 		int(11) DEFAULT NULL COMMENT 'khác',
  `I_Val_05` 		int(11) DEFAULT NULL COMMENT 'khác',
  
  `F_Val_01` double DEFAULT NULL,
  `F_Val_02` double DEFAULT NULL,
  `F_Val_03` double DEFAULT NULL,
  `F_Val_04` double DEFAULT NULL,
  `F_Val_05` double DEFAULT NULL,
  
  
  PRIMARY KEY (`I_ID`),
  
  KEY `idx_TNPOS_01` (`D_Date_01`),
  KEY `idx_TNPOS_02` (`D_Date_02`),
  KEY `idx_TNPOS_03` (`D_Date_03`),
  KEY `idx_TNPOS_04` (`D_Date_04`),
  
  KEY `idx_TNPOS_11` (`I_Aut_User_01`),
  
  KEY `idx_TNPOS_20` (`T_Title`),
  KEY `idx_TNPOS_21` (`T_Code_01`),
  KEY `idx_TNPOS_22` (`T_Code_02`),
    
  KEY `idx_TNPOS_30` (`I_Val_01`,`I_Val_02`),
  KEY `idx_TNPOS_31` (`I_Parent`),
  
  KEY `idx_TNPOS_41` (`I_Type_01`),
  KEY `idx_TNPOS_42` (`I_Type_02`),
  KEY `idx_TNPOS_43` (`I_Type_03`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------------------------
-- --------------------------------------------------------------------------
-- --------------------------------------------------------------------------

CREATE TABLE `TA_NSO_GROUP` (
  `I_ID` 			int(11) NOT NULL AUTO_INCREMENT,
  `T_Ref` 			varchar(100) DEFAULT NULL,
  `T_Name` 			varchar(200) DEFAULT NULL,
  
  `T_Info_01` 		text DEFAULT NULL,
  `T_Info_02` 		text DEFAULT NULL,
  `T_Info_03` 		text DEFAULT NULL,
  `T_Info_04` 		text DEFAULT NULL,
  `T_Info_05` 		text DEFAULT NULL,
  
  `D_Date_01` 		datetime DEFAULT NULL COMMENT 'Date creation',
  `D_Date_02` 		datetime DEFAULT NULL COMMENT 'Date mod',
  
  `I_Status_01` 	int(11) DEFAULT NULL COMMENT 'status by admin',
  `I_Status_02` 	int(11) DEFAULT NULL COMMENT '1: Publish, 2: Private, 0: Desactivate',
  
  `I_Type_01` 		int(11) DEFAULT NULL,
  `I_Type_02` 		int(11) DEFAULT NULL,
  
  `I_Aut_User` 		int(11) DEFAULT NULL,
  `I_Per_Manager` 	int(11) DEFAULT NULL COMMENT 'Per_Person moral manager',
  
  `T_Val_01` 		text DEFAULT NULL,
  `T_Val_02` 		text DEFAULT NULL,
  
  PRIMARY KEY (`I_ID`),
  KEY `idx_TNGRO_04` (`T_Ref`),
  KEY `idx_TNGRO_01` (`I_Per_Manager`),
  KEY `idx_TNGRO_02` (`I_Type_01`),
  KEY `idx_TNGRO_03` (`I_Type_02`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `TA_NSO_GROUP_HISTORY` (
  `I_ID` 			int(11) NOT NULL AUTO_INCREMENT,
  `I_Nso_Group` 	int(11) NOT NULL,
  `I_Msg_Message` 	int(11) NOT NULL,
  `I_Aut_User` 		int(11) NOT NULL,
  `I_Status` 		int(11) NOT NULL,
  `D_Date` 			datetime DEFAULT NULL,
  `T_Info_01` 		text DEFAULT NULL,
  `T_Info_02` 		text DEFAULT NULL,
  PRIMARY KEY (`I_ID`),
  KEY `idx_TNGHI_01` (`I_Nso_Group`),
  KEY `idx_TNGHI_02` (`I_Aut_User`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `TA_NSO_GROUP_MEMBER` (
  `I_ID` 		int(11) NOT NULL AUTO_INCREMENT,
  `I_Nso_Group` int(11) DEFAULT NULL,
  `I_Aut_User` 	int(11) DEFAULT NULL,
  `I_Status` 	int(11) DEFAULT NULL COMMENT '1: waiting, 2: accept, 0: Desactivate',
  `I_Type` 		int(11) DEFAULT NULL COMMENT '1: adm, 2: member lev 1, 2: member lev 2',
  `D_Date_01` 	datetime DEFAULT NULL COMMENT 'Date creation',
  `D_Date_02` 	datetime DEFAULT NULL COMMENT 'Date mod',
  `T_Info_01` 	text DEFAULT NULL,
  `T_Info_02` 	text DEFAULT NULL,
  PRIMARY KEY (`I_ID`),
  KEY `idx_TNGME_01` (`I_Nso_Group`),
  KEY `idx_TNGME_02` (`I_Aut_User`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;