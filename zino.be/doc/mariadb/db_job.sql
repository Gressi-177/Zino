CREATE TABLE `TA_JOB_HOLIDAY` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `D_Date` datetime DEFAULT NULL,
  `T_Code_01` varchar(45) DEFAULT NULL,
  `T_Code_02` varchar(45) DEFAULT NULL,
  `T_Info_01` text DEFAULT NULL,
  `T_Info_02` text DEFAULT NULL,
  `I_Per_Manager` int(11) DEFAULT NULL,
  PRIMARY KEY (`I_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `TA_JOB_REPORT` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `T_Code_01` varchar(45) NOT NULL COMMENT 'Code unique gen ',
  `T_Code_02` varchar(45) DEFAULT NULL COMMENT 'Code to define the month of report: ex:2017-08',
  `D_Date_01` datetime NOT NULL COMMENT 'D_Date_New',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'D_Date_Mod',
  `D_Date_03` datetime DEFAULT NULL COMMENT 'D_Date_Validation',
  `T_Info_01` text DEFAULT NULL,
  `T_Info_02` text DEFAULT NULL,
  `I_Status` int(11) NOT NULL COMMENT '0: create, 1: waiting for validation, 2: validation, 3: refused',
  `I_Aut_User_01` int(11) NOT NULL COMMENT 'I_Aut_Actor_New qui a cré le cra',
  `I_Aut_User_02` int(11) DEFAULT NULL COMMENT 'owner',
  `I_Aut_User_03` int(11) DEFAULT NULL COMMENT 'I_Aut_Actor_Validation qui a validé le cra',
  `I_Per_Manager` int(11) DEFAULT NULL COMMENT 'cra appartient à  quelle société',
  `F_Val_01` double DEFAULT NULL COMMENT 'nb of work-day',
  `F_Val_02` double DEFAULT NULL COMMENT 'nb of real work-day',
  `F_Val_03` double DEFAULT NULL COMMENT 'F_Holiday : nb of holidays earned for this report',
  `F_Val_04` double DEFAULT NULL COMMENT 'nb of dayOff in this month',
  PRIMARY KEY (`I_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `TA_JOB_REPORT_DETAIL` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Job_Report` int(11) DEFAULT NULL,
  `I_Tpy_Category` int(11) DEFAULT NULL,
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
  `F_Val_21` double DEFAULT NULL,
  `F_Val_22` double DEFAULT NULL,
  `F_Val_23` double DEFAULT NULL,
  `F_Val_24` double DEFAULT NULL,
  `F_Val_25` double DEFAULT NULL,
  `F_Val_26` double DEFAULT NULL,
  `F_Val_27` double DEFAULT NULL,
  `F_Val_28` double DEFAULT NULL,
  `F_Val_29` double DEFAULT NULL,
  `F_Val_30` double DEFAULT NULL,
  `F_Val_31` double DEFAULT NULL,
  PRIMARY KEY (`I_ID`),
  KEY `idx_TJAHI_02` (`I_Job_Report`),
  CONSTRAINT `fk_TA_AHI_TA_ARE` FOREIGN KEY (`I_Job_Report`) REFERENCES `TA_JOB_REPORT` (`I_ID`) ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `TA_JOB_REPORT_RESUME` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Job_Report` int(11) DEFAULT NULL,
  `T_Val_01` text DEFAULT NULL,
  `T_Val_02` text DEFAULT NULL,
  `T_Val_03` text DEFAULT NULL,
  `T_Val_04` text DEFAULT NULL,
  `T_Val_05` text DEFAULT NULL,
  `T_Val_06` text DEFAULT NULL,
  `T_Val_07` text DEFAULT NULL,
  `T_Val_08` text DEFAULT NULL,
  `T_Val_09` text DEFAULT NULL,
  `T_Val_10` text DEFAULT NULL,
  `T_Val_11` text DEFAULT NULL,
  `T_Val_12` text DEFAULT NULL,
  `T_Val_13` text DEFAULT NULL,
  `T_Val_14` text DEFAULT NULL,
  `T_Val_15` text DEFAULT NULL,
  `T_Val_16` text DEFAULT NULL,
  `T_Val_17` text DEFAULT NULL,
  `T_Val_18` text DEFAULT NULL,
  `T_Val_19` text DEFAULT NULL,
  `T_Val_20` text DEFAULT NULL,
  `T_Val_21` text DEFAULT NULL,
  `T_Val_22` text DEFAULT NULL,
  `T_Val_23` text DEFAULT NULL,
  `T_Val_24` text DEFAULT NULL,
  `T_Val_25` text DEFAULT NULL,
  `T_Val_26` text DEFAULT NULL,
  `T_Val_27` text DEFAULT NULL,
  `T_Val_28` text DEFAULT NULL,
  `T_Val_29` text DEFAULT NULL,
  `T_Val_30` text DEFAULT NULL,
  `T_Val_31` text DEFAULT NULL,
  PRIMARY KEY (`I_ID`),
  KEY `idx_TJRDR_01` (`I_Job_Report`),
  CONSTRAINT `fk_TA_JRD_TA_JRE` FOREIGN KEY (`I_Job_Report`) REFERENCES `TA_JOB_REPORT` (`I_ID`) ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



CREATE TABLE `TA_JOB_DAYOFF_REQUEST` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The user request the dayoff to his supervisor',
  `T_Code_01` varchar(45) NOT NULL,
  `T_Code_02` varchar(45) DEFAULT NULL,
  `D_Date_01` datetime NOT NULL COMMENT 'D_Date_New',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'D_Date_Validation',
  `D_Date_03` datetime DEFAULT NULL COMMENT 'D_Date_Begin',
  `D_Date_04` datetime DEFAULT NULL COMMENT 'D_Date_End',
  `I_Status` int(11) DEFAULT NULL,
  `I_Aut_User_01` int(11) NOT NULL COMMENT 'I_Aut_New',
  `I_Aut_User_02` int(11) DEFAULT NULL COMMENT 'I_Aut_Validation',
  `I_Aut_User_03` int(11) DEFAULT NULL COMMENT 'owner',
  `I_Per_Manager` varchar(45) NOT NULL,
  `T_Info_01` text DEFAULT NULL COMMENT 'T_Comment',
  `T_Info_02` text DEFAULT NULL  COMMENT 'T_Reason', 
  `F_Val_01` double DEFAULT NULL,
  `F_Val_02` double DEFAULT NULL,
  `F_Val_03` double DEFAULT NULL COMMENT '0: Nghỉ nguyên ngày S - E / 1: Nghỉ nửa ngày S / 2: Nghỉ nửa ngày E / 3: Nghỉ nửa ngày S và E',
  PRIMARY KEY (`I_ID`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `TA_JOB_DAYOFF_RESUME` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Aut_User` int(11) DEFAULT NULL COMMENT 'owner',
  
  `T_Info_01` text DEFAULT NULL COMMENT 'T_Config_01: Ngày làm việc (String 7 ngày bắt đầu từ thứ hai):  A: Nghỉ / F: 1 ngày / H: 1/2 ngày / Q: 1/4 ngày',
  `T_Info_02` text DEFAULT NULL  COMMENT 'T_Config_02', 
  `T_Info_03` text DEFAULT NULL  COMMENT 'T_Comment' , 
  
  `F_Val_01` double DEFAULT NULL COMMENT 'F_Total: reset to 0 each year',
  `F_Val_02` double DEFAULT NULL COMMENT 'F_Available:if >=0, reset to 0 each year. Else keep the negative value  ',
  `F_Val_03` double DEFAULT NULL COMMENT 'F_Realized: Reset to 0 at D_Date_Reset, for ex: each year, every 6 months....', 
  
  `I_Val_01` int(11) DEFAULT NULL COMMENT 'I_Reset_Unit: 1: day, 2: month, 3: trimestre, 4: semestre, 5: year ',
  `I_Val_02` int(11) DEFAULT NULL COMMENT 'I_Reset_Val: nb of unit (day, month, trimestre, semestre, year) ',
  
  `D_Date_01` datetime DEFAULT NULL COMMENT 'D_Date_New',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'D_Date_Validation',
  `D_Date_03` datetime DEFAULT NULL COMMENT 'D_Date_Reset:Date next reset, when reset, this date will be update by the next reset date. Depend on the policy of the company, this date can be different for each user ',
  PRIMARY KEY (`I_ID`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;