-- --------------------------------------------------------------------------
-- --------------------------------------------------------------------------
-- --------------------------------------------------------------------------
--
-- Table structure for table `TA_TPY_DOCUMENT`
--
DROP TABLE IF EXISTS `TA_TPY_DOCUMENT`;
DROP TABLE IF EXISTS `TA_TPY_INFORMATION`;
DROP TABLE IF EXISTS `TA_TPY_CATEGORY_ENTITY`;
DROP TABLE IF EXISTS `TA_TPY_CATEGORY`;
DROP TABLE IF EXISTS `TA_TPY_TRANSLATION`;
DROP TABLE IF EXISTS `TA_TPY_FAVORITE`;


CREATE TABLE `TA_TPY_DOCUMENT` (
  `I_ID` 			int(11) NOT NULL AUTO_INCREMENT,
  `I_Entity_Type` 	int(11) DEFAULT NULL,
  `I_Entity_ID` 	int(11) DEFAULT NULL,
 
  `I_Status` 	int(11) DEFAULT NULL COMMENT '0: new, 1: ok, 2: ngung hoat dong, 10: xóa ',
  `I_Priority` 	int(11) DEFAULT NULL COMMENT 'order of file in list if needed',
  
  `I_Type_01` int(11) DEFAULT NULL COMMENT '1: media, 2: other',
  `I_Type_02` int(11) DEFAULT NULL COMMENT '1: avatar, 2: img, 3: video, 10: all',
  `I_Type_03` int(11) DEFAULT NULL COMMENT '1: public, 2: private',
  `I_Type_04` int(11) DEFAULT NULL COMMENT 'other',
  `I_Type_05` int(11) DEFAULT NULL COMMENT 'other',
  
  `F_Val_01` double DEFAULT NULL COMMENT 'file size',
  `F_Val_02` double DEFAULT NULL COMMENT 'other',
  `F_Val_03` double DEFAULT NULL COMMENT 'other',
  `F_Val_04` double DEFAULT NULL COMMENT 'other',
  `F_Val_05` double DEFAULT NULL COMMENT 'other',
  
  `T_Info_01` text DEFAULT NULL COMMENT 'filename',
  `T_Info_02` text DEFAULT NULL COMMENT 'path real in server',
  `T_Info_03` text DEFAULT NULL COMMENT 'path url',
  `T_Info_04` text DEFAULT NULL COMMENT 'path real preview',
  `T_Info_05` text DEFAULT NULL COMMENT 'path url preview',
  `T_Info_06` text DEFAULT NULL,
  `T_Info_07` text DEFAULT NULL,
  `T_Info_08` text DEFAULT NULL,
  `T_Info_09` text DEFAULT NULL COMMENT 'comment',
  `T_Info_10` text DEFAULT NULL COMMENT 'path tmp',
 
  `D_Date_01` datetime DEFAULT NULL COMMENT 'Date new',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'Date mod',
  `D_Date_03` datetime DEFAULT NULL COMMENT 'Date begin',
  `D_Date_04` datetime DEFAULT NULL COMMENT 'Date end',
  `D_Date_05` datetime DEFAULT NULL COMMENT 'Date other',
  
  `I_Aut_User_01` int(11) DEFAULT NULL COMMENT 'user new',
  `I_Aut_User_02` int(11) DEFAULT NULL COMMENT 'user mod',
  
  `I_Parent` int(11) DEFAULT NULL COMMENT 'tpyDocument origin id when this doc is duplicated',
  
  PRIMARY KEY (`I_ID`),
  
  KEY `idx_TTDOC_00` (`I_Entity_Type`,`I_Entity_ID`),
  
  KEY `idx_TTDOC_01` (`I_Type_01`),
  KEY `idx_TTDOC_02` (`I_Type_02`),
  KEY `idx_TTDOC_03` (`I_Type_03`),
  
  KEY `idx_TTDOC_10` (`I_Parent`)
 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- Table structure for table `TA_TPY_INFORMATION`
--


CREATE TABLE `TA_TPY_INFORMATION` (
  `I_ID` 			int(11) NOT NULL AUTO_INCREMENT,
  
  `I_Entity_Type` 	int(11) DEFAULT NULL,
  `I_Entity_ID` 	int(11) DEFAULT NULL,
  
  `I_Status` 		int(11) DEFAULT NULL,
  `I_Priority` 		int(11) DEFAULT NULL COMMENT 'order of file in list if needed',
  
  `I_Type_01` int(11) DEFAULT NULL,
  `I_Type_02` int(11) DEFAULT NULL,
  `I_Type_03` int(11) DEFAULT NULL,
  `I_Type_04` int(11) DEFAULT NULL,
  `I_Type_05` int(11) DEFAULT NULL,
  
  `T_Info_01` text  DEFAULT NULL,
  `T_Info_02` text  DEFAULT NULL,
  `T_Info_03` text  DEFAULT NULL,
  `T_Info_04` text  DEFAULT NULL,
  `T_Info_05` text  DEFAULT NULL,
  `T_Info_06` text  DEFAULT NULL,
  `T_Info_07` text  DEFAULT NULL,
  `T_Info_08` text  DEFAULT NULL,
  `T_Info_09` text  DEFAULT NULL,
  `T_Info_10` text  DEFAULT NULL,
  `T_Info_11` text  DEFAULT NULL,
  `T_Info_12` text  DEFAULT NULL,
  `T_Info_13` text  DEFAULT NULL,
  `T_Info_14` text  DEFAULT NULL,
  `T_Info_15` text  DEFAULT NULL,
  `T_Info_16` text  DEFAULT NULL,
  `T_Info_17` text  DEFAULT NULL,
  `T_Info_18` text  DEFAULT NULL,
  `T_Info_19` text  DEFAULT NULL,
  `T_Info_20` text  DEFAULT NULL,
  
  `F_Val_01` double DEFAULT NULL,
  `F_Val_02` double DEFAULT NULL,
  `F_Val_03` double DEFAULT NULL,
  `F_Val_04` double DEFAULT NULL,
  `F_Val_05` double DEFAULT NULL,
  `F_Val_06` double DEFAULT NULL,
  `F_Val_07` double DEFAULT NULL,
  `F_Val_08` double DEFAULT NULL,
  `F_Val_09` double DEFAULT NULL,
  `F_Val_10` double DEFAULT NULL,
  `F_Val_11` double DEFAULT NULL,
  `F_Val_12` double DEFAULT NULL,
  `F_Val_13` double DEFAULT NULL,
  `F_Val_14` double DEFAULT NULL,
  `F_Val_15` double DEFAULT NULL,
  `F_Val_16` double DEFAULT NULL,
  `F_Val_17` double DEFAULT NULL,
  `F_Val_18` double DEFAULT NULL,
  `F_Val_19` double DEFAULT NULL,
  `F_Val_20` double DEFAULT NULL,
  
  `D_Date_01` datetime DEFAULT NULL COMMENT 'Date new',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'Date mod',
  
  `I_Aut_User_01` int(11) DEFAULT NULL COMMENT 'user new',
  `I_Aut_User_02` int(11) DEFAULT NULL COMMENT 'user mod',
  PRIMARY KEY (`I_ID`),
  
  KEY `idx_TTINF_00` (`I_Entity_Type`, `I_Entity_ID`),
  KEY `idx_TTINF_01` (`I_Type_01`	 , `I_Type_02`, `I_Type_03`, `I_Type_04`,`I_Type_05`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `TA_TPY_CATEGORY` (
  `I_ID` 			int(11) NOT NULL AUTO_INCREMENT, 
  
  `T_Name` 			varchar(200) NOT NULL,
  `T_Code` 			varchar(100) DEFAULT NULL,
  `T_Info` 			text DEFAULT NULL,
  
  `I_Type_01` 		int(11) NOT NULL COMMENT 'what the table will use this category, ex: I_Type_00= ID_TA_MAT_MATERIAL ',
  `I_Type_02` 		int(11) DEFAULT NULL,
  `I_Type_03` 		int(11) DEFAULT NULL,
  
  `I_Status` 		int(11) DEFAULT NULL,
  
  `I_Parent`  		int(11) DEFAULT NULL COMMENT 'the cat parent',
  `I_Per_Manager` 	int(11) DEFAULT NULL,
  
  PRIMARY KEY (`I_ID`),
  KEY `idx_TTCAT_01` (`T_Name`),
  KEY `idx_TTCAT_02` (`I_Parent`),
  
  KEY `idx_TTCAT_11` (`I_Type_01`),
  KEY `idx_TTCAT_12` (`I_Type_02`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



CREATE TABLE `TA_TPY_CATEGORY_ENTITY` (
  `I_ID` 			int(11) NOT NULL AUTO_INCREMENT,
  `I_Tpy_Category` 	int(11) NOT NULL,
  `I_Entity_Type` 	int(11) NOT NULL,
  `I_Entity_ID` 	int(11) NOT NULL, 
  PRIMARY KEY (`I_ID`),
  KEY `idx_TTCEN_01` (`I_Entity_Type`,`I_Entity_ID`),
  KEY `idx_TTCEN_02` (`I_Tpy_Category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `TA_TPY_TRANSLATION` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Entity_Type` int(11) NOT NULL,
  `I_Entity_ID` int(11) NOT NULL,
  
  `I_Val_01` int(11) DEFAULT NULL COMMENT 'lang option',
  `I_Val_02` int(11) DEFAULT NULL COMMENT 'other option',
   
  `T_Info_01` longtext DEFAULT NULL,
  `T_Info_02` longtext DEFAULT NULL,
  PRIMARY KEY (`I_ID`),
  KEY `idx_TTTRA_01` (`I_Entity_Type`,`I_Entity_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `TA_TPY_FAVORITE` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Aut_User` int(11) NOT NULL,
  `I_Entity_Type` int(11) NOT NULL,
  `I_Entity_ID` int(11) NOT NULL,
  `I_Priority` int(11) DEFAULT NULL COMMENT 'order of display',
  `D_Date` datetime DEFAULT NULL COMMENT 'date creation',
  `T_Title` varchar(1000) DEFAULT NULL,
  `T_Description` mediumtext DEFAULT NULL,
  `I_Type` int(11) DEFAULT NULL,
  PRIMARY KEY (`I_ID`),
  KEY `idx_TTFAV_01` (`I_Aut_User`),
  KEY `idx_TTFAV_02` (`I_Entity_Type`,`I_Entity_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `TA_TPY_RELATIONSHIP` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Entity_Type_01` int(11) NOT NULL,
  `I_Entity_Type_02` int(11) NOT NULL,
  `I_Entity_ID_01` int(11) NOT NULL,
  `I_Entity_ID_02` int(11) NOT NULL,
  `D_Date_01` datetime DEFAULT NULL COMMENT 'Date of new',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'Date of mod',
  `D_Date_03` datetime DEFAULT NULL COMMENT 'Date of begin',
  `D_Date_04` datetime DEFAULT NULL COMMENT 'Date of end',
  `I_Type` int(11) DEFAULT NULL,
  `I_Status` int(11) DEFAULT NULL COMMENT '0: new/backlog, 1: todo, 2: in progress, 3: stand by, 4: done, 5: closed, 6: unresolved',
  `I_Level` int(11) DEFAULT NULL COMMENT 'level 0: manager, level 1: reporter, level 2: worker',
  `T_Comment` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`I_ID`),
  KEY `idx_TTREL_01` (`I_Entity_Type_01`,`I_Entity_ID_01`,`I_Entity_Type_02`,`I_Entity_ID_02`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


