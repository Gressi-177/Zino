-- hnvNewLine.TA_CFG_GROUP definition
DROP TABLE IF EXISTS `TA_CFG_VALUE`;
CREATE TABLE `TA_CFG_VALUE` (
  `I_ID` 			int(11) NOT NULL,
  `I_Type_01` 		int(11) DEFAULT NULL,
  `I_Type_02` 		int(11) DEFAULT NULL,
  `I_Status_01` 	int(11) DEFAULT NULL,
  `I_Status_02` 	int(11) DEFAULT NULL,
  `T_Name` 			varchar(200) NOT NULL,
  `T_Code` 			varchar(100) DEFAULT NULL,
  `T_Info_01` 		text DEFAULT NULL COMMENT 'description',
  `T_Info_02` 		longtext DEFAULT NULL COMMENT 'Table value',
  `T_Info_03` 		longtext DEFAULT NULL COMMENT 'other value',
  `I_Parent` 		int(11) DEFAULT NULL,
  `I_Per_Manager` 	int(11) DEFAULT NULL,  
  PRIMARY KEY (`I_ID`),
  KEY `idx_TCVAL_01` (`I_Parent`),
  KEY `idx_TCVAL_03` (`T_Code`),
  KEY `idx_TCVAL_04` (`I_Type_01`),
  CONSTRAINT `fk_TCVAL_01` FOREIGN KEY (`I_Parent`) REFERENCES `TA_CFG_VALUE` (`I_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

