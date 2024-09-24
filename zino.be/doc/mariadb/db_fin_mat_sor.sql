DROP TABLE IF EXISTS `TA_FIN_FINANCE`;
CREATE TABLE `TA_FIN_FINANCE` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Status` int(11) DEFAULT NULL,
  `I_Type_01` int(11) DEFAULT NULL,
  `I_Type_02` int(11) DEFAULT NULL,
  
  `D_Date_01` datetime DEFAULT NULL COMMENT 'dt new',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'dt mod',
  `D_Date_03` datetime DEFAULT NULL COMMENT 'dt begin',
  `D_Date_04` datetime DEFAULT NULL COMMENT 'dt end',
  
  `F_Val_01` double DEFAULT NULL,
  `F_Val_02` double DEFAULT NULL,
  `F_Val_03` double DEFAULT NULL,
  `F_Val_04` double DEFAULT NULL,
  `F_Val_05` double DEFAULT NULL,
  
  `T_Info_01` text  DEFAULT NULL,
  `T_Info_02` text  DEFAULT NULL,
  `T_Info_03` text  DEFAULT NULL,
  `T_Info_04` text  DEFAULT NULL,
  `T_Info_05` text  DEFAULT NULL,
  
  `I_Aut_User_01` int(11) DEFAULT NULL,
  `I_Aut_User_02` int(11) DEFAULT NULL,
  `I_Aut_User_03` int(11) DEFAULT NULL,
  
  `I_Per_Person_01` int(11) NOT NULL,
  `I_Per_Person_02` int(11) NOT NULL,
  
  `I_Per_Manager` int(11) DEFAULT NULL,
  
  PRIMARY KEY (`I_ID`),
  KEY `idx_FIFIN_01` (`I_Per_Person_01`),
  KEY `idx_FIFIN_02` (`I_Per_Person_02`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `TA_MAT_MATERIAL_DETAIL`;
DROP TABLE IF EXISTS `TA_MAT_MATERIAL`;
CREATE TABLE `TA_MAT_MATERIAL` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  
  `I_Status_01` int(11) DEFAULT NULL,
  `I_Status_02` int(11) DEFAULT NULL,
  `I_Status_03` int(11) DEFAULT NULL,
  `I_Status_04` int(11) DEFAULT NULL,
  `I_Status_05` int(11) DEFAULT NULL,
  
  `I_Type_01` int(11) DEFAULT NULL,
  `I_Type_02` int(11) DEFAULT NULL,
  `I_Type_03` int(11) DEFAULT NULL,
  `I_Type_04` int(11) DEFAULT NULL,
  `I_Type_05` int(11) DEFAULT NULL,
  
  `T_Name_01` varchar(1000)  DEFAULT NULL,
  `T_Name_02` varchar(1000)  DEFAULT NULL,
  `T_Name_03` varchar(1000)  DEFAULT NULL,
  
  `T_Code_01` varchar(500)  DEFAULT NULL,
  `T_Code_02` varchar(500)  DEFAULT NULL,
  `T_Code_03` varchar(500)  DEFAULT NULL,
  `T_Code_04` varchar(500)  DEFAULT NULL,
  `T_Code_05` varchar(500)  DEFAULT NULL,
  
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
  
  `D_Date_01` datetime DEFAULT NULL COMMENT 'dt new',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'dt mod',
  `D_Date_03` datetime DEFAULT NULL COMMENT 'dt begin',
  `D_Date_04` datetime DEFAULT NULL COMMENT 'dt end',
  
  `I_Aut_User_01` int(11) DEFAULT NULL,
  `I_Aut_User_02` int(11) DEFAULT NULL,
  
  `I_Per_Manager` int(11) DEFAULT NULL,
  
  `I_Per_Person_01` int(11) DEFAULT NULL COMMENT 'per production',
  `I_Per_Person_02` int(11) DEFAULT NULL COMMENT 'per other',
  
  PRIMARY KEY (`I_ID`),
  KEY `idx_TMMAT_02` (`T_Name_01`),
  KEY `idx_TMMAT_03` (`T_Name_02`),
  KEY `idx_TMMAT_04` (`T_Code_01`),
  KEY `idx_TMMAT_05` (`T_Code_02`),
  KEY `idx_TMMAT_06` (`T_Code_03`),
  KEY `idx_TMMAT_07` (`T_Code_04`),
  KEY `idx_TMMAT_08` (`T_Code_05`),
  KEY `idx_TMMAT_09` (`I_Per_Manager`),
  KEY `idx_TMMAT_10` (`I_Per_Person_01`),
  KEY `idx_TMMAT_11` (`I_Per_Person_02`),
  KEY `idx_TMMAT_13` (`I_Type_01`),
  KEY `idx_TMMAT_14` (`I_Type_02`),
  KEY `idx_TMMAT_15` (`I_Type_03`),
  KEY `idx_TMMAT_21` (`I_Status_01`),
  KEY `idx_TMMAT_22` (`I_Status_02`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- hnvNewLine.TA_MAT_MATERIAL_DETAIL definition

CREATE TABLE `TA_MAT_MATERIAL_DETAIL` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Mat_Material_01` int(11) DEFAULT NULL COMMENT 'Parent',
  `I_Mat_Material_02` int(11) DEFAULT NULL COMMENT 'Child',
  `T_Info_01` text DEFAULT NULL COMMENT 'mat unit label',
  `T_Info_02` text DEFAULT NULL,
  `T_Info_03` text DEFAULT NULL,
  `T_Info_04` text DEFAULT NULL,
  `T_Info_05` text DEFAULT NULL,
  `I_Type_01` int(11) DEFAULT NULL,
  `I_Type_02` int(11) DEFAULT NULL,
  `I_Status_01` int(11) DEFAULT NULL,
  `I_Status_02` int(11) DEFAULT NULL,
  `I_Val_01` int(11) DEFAULT NULL COMMENT 'Priority',
  `I_Val_02` int(11) DEFAULT NULL,
  `F_Val_01` double DEFAULT NULL COMMENT 'quant',
  `F_Val_02` double DEFAULT NULL COMMENT 'unit ratio',
  `F_Val_03` double DEFAULT NULL COMMENT 'unit price pref',
  `F_Val_04` double DEFAULT NULL,
  `F_Val_05` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`I_ID`),
  KEY `idx_TMMDET_02` (`I_Mat_Material_01`),
  KEY `idx_TMMDET_03` (`I_Mat_Material_02`),
  CONSTRAINT `FK_TMMDE_01` FOREIGN KEY (`I_Mat_Material_01`) REFERENCES `TA_MAT_MATERIAL` (`I_ID`),
  CONSTRAINT `FK_TMMDE_02` FOREIGN KEY (`I_Mat_Material_02`) REFERENCES `TA_MAT_MATERIAL` (`I_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `TA_MAT_UNIT`;
CREATE TABLE `TA_MAT_UNIT` (
  `I_ID` 		int(11) NOT NULL AUTO_INCREMENT,
  `I_Status` 	int(11) DEFAULT NULL,
  `T_Code` 		varchar(1000)  DEFAULT NULL,
  `T_Name` 		varchar(1000)  DEFAULT NULL,
  `I_Per_Manager` int(11) DEFAULT NULL COMMENT 'cong ty/ca nhan quan ly',
  PRIMARY KEY (`I_ID`),
  KEY `idx_TMUNIT_01` (`I_Per_Manager`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `TA_MAT_PRICE`;
CREATE TABLE `TA_MAT_PRICE` (
  `I_ID` 			int(11) NOT NULL AUTO_INCREMENT,
  `I_Priority` 		int(11) DEFAULT NULL,
  `I_Status` 		int(11) DEFAULT NULL,
  
  `I_Mat_Material` 	int(11) DEFAULT NULL,
  `I_Mat_Unit` 		int(11) DEFAULT NULL,
 
  `F_Val_00` 		double DEFAULT NULL COMMENT 'ratio',
  `F_Val_01`	 	double DEFAULT NULL COMMENT 'price 01',
  `F_Val_02` 		double DEFAULT NULL COMMENT 'price 02',
  `F_Val_03` 		double DEFAULT NULL COMMENT 'discount',
  `F_Val_04` 		double DEFAULT NULL,
  `F_Val_05` 		double DEFAULT NULL,
  `F_Val_06` 		double DEFAULT NULL,
  `F_Val_07`		double DEFAULT NULL,
  `F_Val_08`		double DEFAULT NULL,
  `F_Val_09`		double DEFAULT NULL,
  `F_Val_10`		double DEFAULT NULL,
  
  `T_Info_01` text  DEFAULT NULL COMMENT 'Unit',
  `T_Info_02` text  DEFAULT NULL COMMENT 'Currency',
  `T_Info_03` text  DEFAULT NULL,
  `T_Info_04` text  DEFAULT NULL,
  `T_Info_05` text  DEFAULT NULL,
  
  `D_Date_01` datetime DEFAULT NULL COMMENT 'dt new',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'dt mod',
  `D_Date_03` datetime DEFAULT NULL COMMENT 'dt begin',
  `D_Date_04` datetime DEFAULT NULL COMMENT 'dt end',
  
  `I_Aut_User_01` int(11) DEFAULT NULL,
  `I_Aut_User_02` int(11) DEFAULT NULL,

  PRIMARY KEY (`I_ID`),
  KEY `idx_TMMPRI_02` (`I_Mat_Material`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `TA_MAT_STOCK`;
CREATE TABLE `TA_MAT_STOCK` (
  `I_ID` 			int(11) NOT NULL AUTO_INCREMENT,
  `I_Per_Manager` 	int(11) DEFAULT NULL,
  `I_Mat_Warehouse` int(11) DEFAULT NULL,
  `I_Mat_Material` 	int(11) DEFAULT NULL,
  
  `I_Status` 		int(11) DEFAULT NULL,
  `F_Val_00` 		double DEFAULT NULL,
  `F_Val_01` 		double DEFAULT NULL,
  `F_Val_02` 		double DEFAULT NULL,
  `F_Val_03`	 	double DEFAULT NULL,
  `F_Val_04` 		double DEFAULT NULL,
  `F_Val_05` 		double DEFAULT NULL,
  
  `T_Info_01` 		text  DEFAULT NULL,
  `T_Info_02` 		text  DEFAULT NULL,
  `T_Info_03` 		text  DEFAULT NULL,
  `T_Info_04` 		text  DEFAULT NULL,
  `T_Info_05` 		text  DEFAULT NULL,
  
  `D_Date_01` 		datetime DEFAULT NULL,
  `D_Date_02` 		datetime DEFAULT NULL,
  `D_Date_03` 		datetime DEFAULT NULL,
  `D_Date_04` 		datetime DEFAULT NULL,
  
  PRIMARY KEY (`I_ID`),
  KEY `idx_TMMSTK_01` (`I_ID`),
  KEY `idx_TMMSTK_02` (`I_Mat_Material`),
  KEY `idx_TMMSTK_03` (`I_Per_Manager`),
  KEY `idx_TMMSTK_04` (`I_Mat_Warehouse`),
  KEY `idx_TMMSTK_05` (`I_Status`),
  KEY `idx_TMMSTK_06` (`D_Date_04`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `TA_MAT_STOCK_IO`;
CREATE TABLE `TA_MAT_STOCK_IO` (
  `I_ID` 				int(11) NOT NULL AUTO_INCREMENT,
  `I_Per_Manager` 		int(11) DEFAULT NULL,
  `I_Mat_Material` 		int(11) DEFAULT NULL,
  `I_Mat_Stock` 		int(11) DEFAULT NULL,
  `I_Mat_Warehouse` 	int(11) DEFAULT NULL,
  `I_Sor_Order` 		int(11) DEFAULT NULL,
  `I_Sor_Order_Detail` 	int(11) DEFAULT NULL,
  
  `I_Status` 			int(11) DEFAULT NULL,
  `I_Type` 				int(11) DEFAULT NULL,
  
  `F_Val_00` 			double DEFAULT NULL,
  `F_Val_01` 			double DEFAULT NULL,
  `F_Val_02` 			double DEFAULT NULL,
  `F_Val_03` 			double DEFAULT NULL,
  `F_Val_04` 			double DEFAULT NULL,
  `F_Val_05` 			double DEFAULT NULL,
  
  `D_Date_01` 			datetime DEFAULT NULL,
  `D_Date_02` 			datetime DEFAULT NULL,
  `D_Date_03` 			datetime DEFAULT NULL,
  `D_Date_04` 			datetime DEFAULT NULL,
  PRIMARY KEY (`I_ID`),
  KEY `idx_TMMSIO_02` (`I_Mat_Material`),
  KEY `idx_TMMSIO_03` (`I_Per_Manager`),
  KEY `idx_TMMSIO_04` (`I_Mat_Stock`),
  KEY `idx_TMMSIO_05` (`I_Sor_Order`),
  KEY `idx_TMMSTK_06` (`I_Sor_Order_Detail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `TA_MAT_STOCK_MONTH`;
CREATE TABLE `TA_MAT_STOCK_MONTH` (
  `I_ID` 			int(11) NOT NULL AUTO_INCREMENT,
  `I_Per_Manager` 	int(11) DEFAULT NULL,
  `I_Mat_Material` 	int(11) DEFAULT NULL,
  `I_Mat_Warehouse` int(11) DEFAULT NULL,
  `F_Val_00` 		double DEFAULT NULL,
  `F_Val_01` 		double DEFAULT NULL,
  `F_Val_02` 		double DEFAULT NULL,
  `F_Val_03` 		double DEFAULT NULL,
  `F_Val_04` 		double DEFAULT NULL,
  `F_Val_05` 		double DEFAULT NULL,
  `D_Date_01` 		datetime DEFAULT NULL,
  `D_Date_02` 		datetime DEFAULT NULL,
  `I_Type` 			int(11) DEFAULT NULL,
  `I_Aut_User` 		int(11) DEFAULT NULL,
  `T_Info_01` 		text  DEFAULT NULL,
  `T_Info_02` 		text  DEFAULT NULL,
  `T_Info_03` 		text  DEFAULT NULL,
  `T_Info_04` 		text  DEFAULT NULL,
  `T_Info_05` 		text  DEFAULT NULL,
  PRIMARY KEY (`I_ID`),
  KEY `idx_TMSMO_02` (`I_Mat_Material`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `TA_MAT_WAREHOUSE`;
CREATE TABLE `TA_MAT_WAREHOUSE` (
  `I_ID` 		int(11) NOT NULL AUTO_INCREMENT,
  `T_Name` 		varchar(1000)  DEFAULT NULL,
  
  `I_Status` 	int(11) DEFAULT NULL,
  `I_Type_01` 	int(11) DEFAULT NULL COMMENT '1: wh public/web sale, 2: wh intern',
  `I_Type_02` 	int(11) DEFAULT NULL,
  
  `T_Code_01` 	varchar(100)  DEFAULT NULL,
  `T_Code_02` 	varchar(100)  DEFAULT NULL,
  
  `T_Info_01` 	text  DEFAULT NULL,
  `T_Info_02` 	text  DEFAULT NULL,
  `T_Info_03` 	text  DEFAULT NULL,
  `T_Info_04` 	text  DEFAULT NULL,
  `T_Info_05` 	text  DEFAULT NULL,
  
  `I_Per_Manager` int(11) DEFAULT NULL,
 
  PRIMARY KEY (`I_ID`),
  KEY `idx_TMMWAR_01` (`T_Name`),
  KEY `idx_TMMWAR_02` (`T_Code_01`),
  KEY `idx_TMMWAR_03` (`T_Code_02`),
  KEY `idx_TMMWAR_04` (`I_Per_Manager`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `TA_SOR_DEAL`;
CREATE TABLE `TA_SOR_DEAL` (
  `I_ID` 		int(11) NOT NULL AUTO_INCREMENT,
  `I_Status` 	int(11) DEFAULT NULL,
  
  `T_Code_01`	varchar(1000)  DEFAULT NULL,
  `T_Code_02` 	varchar(1000)  DEFAULT NULL,
  
  `I_Type_01` 	int(11) DEFAULT NULL,
  `I_Type_02` 	int(11) DEFAULT NULL,
  `I_Type_03` 	int(11) DEFAULT NULL,
  
  `F_Val_00` 	double DEFAULT NULL,
  `F_Val_01` 	double DEFAULT NULL,
  `F_Val_02` 	double DEFAULT NULL,
  
  `T_Info_01` 	text  DEFAULT NULL,
  `T_Info_02` 	text  DEFAULT NULL,
  
  `D_Date_01` 	datetime DEFAULT NULL,
  `D_Date_02` 	datetime DEFAULT NULL,
  `D_Date_03` 	datetime DEFAULT NULL,
  `D_Date_04` 	datetime DEFAULT NULL,
  
  `I_Aut_User_01` int(11) DEFAULT NULL,
  `I_Aut_User_02` int(11) DEFAULT NULL,
  
  `I_Per_Manager` int(11) DEFAULT NULL,
  
  PRIMARY KEY (`I_ID`),
  KEY `idx_TSDEA_02` (`I_Per_Manager`),
  KEY `idx_TSDEA_06` (`I_Aut_User_01`),
  KEY `idx_TSDEA_08` (`D_Date_01`),
  KEY `idx_TSDEA_10` (`T_Code_01`),
  KEY `idx_TSDEA_11` (`T_Code_02`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `TA_SOR_ORDER`;
CREATE TABLE `TA_SOR_ORDER` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `T_Code_01` varchar(1000)  DEFAULT NULL,
  `T_Code_02` varchar(1000)  DEFAULT NULL,
  
  `I_Status` int(11) DEFAULT NULL,
  
  `I_Type_01` int(11) DEFAULT NULL,
  `I_Type_02` int(11) DEFAULT NULL,
  `I_Type_03` int(11) DEFAULT NULL,
  `I_Type_04` int(11) DEFAULT NULL,
  `I_Type_05` int(11) DEFAULT NULL,
  
  `F_Val_00` double DEFAULT NULL,
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
 
  `T_Info_01` longtext  DEFAULT NULL,
  `T_Info_02` text  DEFAULT NULL,
  `T_Info_03` text  DEFAULT NULL,
  `T_Info_04` text  DEFAULT NULL,
  `T_Info_05` text  DEFAULT NULL,
  
  `D_Date_01` datetime DEFAULT NULL,
  `D_Date_02` datetime DEFAULT NULL,
  `D_Date_03` datetime DEFAULT NULL,
  `D_Date_04` datetime DEFAULT NULL,
  
  `I_Aut_User_01` int(11) DEFAULT NULL,
  `I_Aut_User_02` int(11) DEFAULT NULL,
  
  `I_Entity_Type` 	int(11) DEFAULT NULL,
  `I_Entity_ID_01`   int(11) DEFAULT NULL COMMENT 'Suplier',
  `I_Entity_ID_02`   int(11) DEFAULT NULL COMMENT 'Client',
 
  
  `I_Mat_Val_01` int(11) DEFAULT NULL COMMENT 'Warehouse source',
  `I_Mat_Val_02` int(11) DEFAULT NULL COMMENT 'Warehouse destination',
  
  
  `I_Per_Person_01` int(11) DEFAULT NULL,
  `I_Per_Person_02` int(11) DEFAULT NULL,
  `I_Per_Person_03` int(11) DEFAULT NULL,
  `I_Per_Person_04` int(11) DEFAULT NULL,
  `I_Per_Person_05` int(11) DEFAULT NULL,
  
  `I_Parent` int(11) DEFAULT NULL COMMENT 'Sor Order source (ex: transfert)',
 
  `I_Per_Manager` int(11) DEFAULT NULL,
  PRIMARY KEY (`I_ID`),
  KEY `idx_TSORD_02` (`I_Per_Manager`),
  KEY `idx_TSORD_03` (`I_Per_Person_01`),
  KEY `idx_TSORD_04` (`I_Per_Person_02`),
  KEY `idx_TSORD_05` (`I_Per_Person_03`),
  KEY `idx_TSORD_06` (`I_Aut_User_01`),
  KEY `idx_TSORD_07` (`I_Aut_User_02`),
  KEY `idx_TSORD_08` (`D_Date_01`),
  KEY `idx_TSORD_09` (`D_Date_02`),
  KEY `idx_TSORD_10` (`T_Code_01`),
  KEY `idx_TSORD_11` (`I_Mat_Val_01`),
  KEY `idx_TSORD_12` (`I_Mat_Val_02`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `TA_SOR_ORDER_DETAIL`;
CREATE TABLE `TA_SOR_ORDER_DETAIL` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Sor_Order` int(11) DEFAULT NULL,
  
  `I_Status` int(11) DEFAULT NULL,
  `I_Priority` int(11) DEFAULT NULL,
  `I_Mat_Material` int(11) DEFAULT NULL,
  `I_Mat_Price` int(11) DEFAULT NULL,
  
  `F_Val_00` double DEFAULT NULL,
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
  
  `D_Date_01` datetime DEFAULT NULL COMMENT 'dt production',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'dt expiration',
  
  `I_Per_Person_01` int(11) DEFAULT NULL,
  `I_Per_Person_02` int(11) DEFAULT NULL,
  PRIMARY KEY (`I_ID`),
  KEY `idx_TSODE_02` (`I_Sor_Order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




DROP TABLE IF EXISTS `TA_MAT_MATERIAL_DATA`;
CREATE TABLE `TA_MAT_MATERIAL_DATA` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  
  `I_Mat_Material` int(11) DEFAULT NULL COMMENT 'Material',
   
  `I_Status_01` int(11) DEFAULT NULL,
  `I_Status_02` int(11) DEFAULT NULL,
  `I_Status_03` int(11) DEFAULT NULL,
  
  `I_Type_01` int(11) DEFAULT NULL,
  `I_Type_02` int(11) DEFAULT NULL,
  `I_Type_03` int(11) DEFAULT NULL,
  
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
  
  `T_Info_01` text  DEFAULT NULL,
  `T_Info_02` text  DEFAULT NULL,
  `T_Info_03` text  DEFAULT NULL,
    
  `D_Date_01` datetime DEFAULT NULL COMMENT 'dt new',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'dt mod',
  `D_Date_03` datetime DEFAULT NULL COMMENT 'dt begin',
  `D_Date_04` datetime DEFAULT NULL COMMENT 'dt end',
  
  `I_Aut_User_01` int(11) DEFAULT NULL,
  `I_Aut_User_02` int(11) DEFAULT NULL,
  
  PRIMARY KEY (`I_ID`),
  KEY `idx_TMDAT_01` (`D_Date_01`),
  KEY `idx_TMDAT_02` (`I_Type_01`),
  KEY `idx_TMDAT_03` (`I_Type_02`),
  KEY `idx_TMDAT_04` (`I_Status_01`),
  KEY `idx_TMDAT_05` (`I_Status_02`),
  KEY `idx_TMMDET_02` (`I_Mat_Material`),
  CONSTRAINT `FK_TMDAT_01` FOREIGN KEY (`I_Mat_Material`) REFERENCES `TA_MAT_MATERIAL` (`I_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;