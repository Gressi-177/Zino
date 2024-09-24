-- --------------------------------------------------------------------------
-- --------------------------------------------------------------------------
-- --------------------------------------------------------------------------
DROP TABLE IF EXISTS `TA_AUT_AUTH_USER`;
DROP TABLE IF EXISTS `TA_AUT_AUTH_SERVICE`;
DROP TABLE IF EXISTS `TA_AUT_HISTORY`;
DROP TABLE IF EXISTS `TA_AUT_RIGHT`;
DROP TABLE IF EXISTS `TA_AUT_ROLE`;
DROP TABLE IF EXISTS `TA_AUT_USER`;


CREATE TABLE `TA_AUT_RIGHT` (
  `I_ID` 		int(11) NOT NULL COMMENT 'cac quyen co the: xem, xoa, sua, them moi, in an, xem bao cao...',
  `T_Name` 		varchar(200) NOT NULL,  
  `T_Info_01` 	varchar(500) DEFAULT NULL  	COMMENT 'description',
  `T_Info_02` 	varchar(200) DEFAULT NULL	COMMENT 'group', 
  PRIMARY KEY (`I_ID`),
  UNIQUE KEY `idx_TARIG_01` (`T_Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `TA_AUT_ROLE` (
  `I_ID` 		int(11) NOT NULL,
  `I_Status` 	int(11) NOT NULL,  
  `T_Name` 		varchar(200) NOT NULL,   
  `T_Info_01` 	varchar(500) DEFAULT NULL  	COMMENT 'description',
  `T_Info_02` 	varchar(200) DEFAULT NULL	COMMENT 'group', 
  `T_Aut_Right` longtext DEFAULT NULL COMMENT 'id cac quyen lien quan, vd: 12001, 12003',
  PRIMARY KEY (`I_ID`),
  UNIQUE KEY `idx_TAROL_01` (`T_Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `TA_AUT_USER` (
  `I_ID` 	  int(11) NOT NULL AUTO_INCREMENT,
  `I_Status`  int(11) DEFAULT NULL,
  
  `I_Type_01` int(11) DEFAULT NULL COMMENT 'kiểu adm, agent, visitor, member....',
  `I_Type_02` int(11) DEFAULT NULL COMMENt 'kiểu thứ cấp, ví dụ 1 học sinh sẽ có tk cho cha mẹ',
   
  `T_Login_01` varchar(100)  DEFAULT NULL,
  `T_Login_02` varchar(100)  DEFAULT NULL,
  `T_Login_03` varchar(100)  DEFAULT NULL,
  
  `T_Pass_01` varchar(1000)  DEFAULT NULL,
  `T_Pass_02` varchar(1000)  DEFAULT NULL,
  `T_Pass_03` varchar(1000)  DEFAULT NULL,
  
  `T_Info_01` text  DEFAULT NULL COMMENT 'email',
  `T_Info_02` text  DEFAULT NULL COMMENT 'tel',
  `T_Info_03` text  DEFAULT NULL,
  `T_Info_04` text  DEFAULT NULL,
  `T_Info_05` text  DEFAULT NULL,
  
  `T_Info_06` text  DEFAULT NULL,
  `T_Info_07` text  DEFAULT NULL,
  `T_Info_08` text  DEFAULT NULL,
  `T_Info_09` text  DEFAULT NULL,
  `T_Info_10` text  DEFAULT NULL,
  
  `D_Date_01` datetime DEFAULT NULL COMMENT 'dt new',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'dt mod',
  `D_Date_03` datetime DEFAULT NULL	COMMENT 'dt bg limit if need validation',
  `D_Date_04` datetime DEFAULT NULL	COMMENT 'dt end limit if need validation',
  
  `I_Aut_User_01` int(11) DEFAULT NULL COMMENT 'id user new ',
  `I_Aut_User_02` int(11) DEFAULT NULL COMMENT 'id user mod ',
  `I_Aut_User_03` int(11) DEFAULT NULL COMMENT 'id user sup ',
  
  `I_Per_Person`  int(11) DEFAULT NULL COMMENT 'cá nhân liên quan',
  `I_Per_Manager` int(11) DEFAULT NULL,
  
  PRIMARY KEY (`I_ID`),
  KEY `idx_TUSER_01` (`T_Login_01`),
  KEY `idx_TUSER_02` (`T_Login_02`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;

CREATE TABLE `TA_AUT_AUTH_USER` (
  `I_ID` 		int(11) NOT NULL AUTO_INCREMENT,
  `I_Aut_User` 	int(11) DEFAULT NULL,
  `I_Aut_Role` 	int(11) DEFAULT NULL,
  `I_Status` 	int(11) DEFAULT NULL,
  `D_Date_01` datetime DEFAULT NULL COMMENT 'dt begin',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'dt end',
  `T_Aut_Right` longtext DEFAULT NULL COMMENT 'id cac quyen lien quan, vd: 12001, 12003, copy lai tu TA_AUT_ROLE.T_Aut_Right',
  PRIMARY KEY (`I_ID`),
  KEY `idx_TAAUS_01` (`I_Aut_User`),
  KEY `idx_TAAUS_02` (`I_Aut_Role`),
  CONSTRAINT `FK_TAAUS_02` FOREIGN KEY (`I_Aut_Role`) REFERENCES `TA_AUT_ROLE` (`I_ID`),
  CONSTRAINT `FK_TAAUS_03` FOREIGN KEY (`I_Aut_User`) REFERENCES `TA_AUT_USER` (`I_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `TA_AUT_AUTH_SERVICE` (
  `I_ID` 		int(11) NOT NULL AUTO_INCREMENT,
  `T_Info_01` 	varchar(200) NOT NULL COMMENT 'serviceClass.serviceName',
  `T_Info_02` 	varchar(200) NOT NULL COMMENT 'service name',
  `I_Type_01` 	int(11) DEFAULT NULL ,
  `I_Type_02` 	int(11) DEFAULT NULL ,
  `I_Status` 	int(11) DEFAULT NULL ,
  `D_Date_01` 	datetime DEFAULT NULL COMMENT 'dt begin',
  `D_Date_02` 	datetime DEFAULT NULL COMMENT 'dt end',
  `T_Aut_Role` 	text DEFAULT NULL COMMENT 'id cac vai tro lien quan, vd: 100, 200 & 300, 102 & 400',
  `T_Aut_Right` text DEFAULT NULL COMMENT 'id cac quyen lien quan, vd: 12001, 12003 & 12004, copy lai tu TA_AUT_ROLE.T_Aut_Right',
  PRIMARY KEY (`I_ID`),
  KEY `idx_TAASE_01` (`T_Info_01`),
  KEY `idx_TAASE_02` (`T_Info_02`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;





CREATE TABLE `TA_AUT_HISTORY` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  `I_Aut_User` int(11) NOT NULL,
  `I_Type` int(11) NOT NULL,
  `D_Date` datetime NOT NULL,
  `T_Val` text DEFAULT NULL COMMENT 'IP/other info',
  PRIMARY KEY (`I_ID`),
  KEY `idx_TACHI_01` (`I_Aut_User`),
  KEY `idx_TACHI_02` (`I_Type`),
  CONSTRAINT `fk_TACHI_01` FOREIGN KEY (`I_Aut_User`) REFERENCES `TA_AUT_USER` (`I_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;
-- --------------------------------------------------------------------------
-- --------------------------------------------------------------------------
-- --------------------------------------------------------------------------
--
-- Table structure for table `TA_PER_PERSON`
--

DROP TABLE IF EXISTS `TA_PER_PERSON`;
CREATE TABLE `TA_PER_PERSON` (
  `I_ID` int(11) NOT NULL AUTO_INCREMENT,
  
  `T_Name_01` varchar(200)  NOT 	NULL COMMENT 'Họ / Tên doanh nghiệp',
  `T_Name_02` varchar(200)  DEFAULT NULL COMMENT 'Tên đệm',
  `T_Name_03` varchar(200)  DEFAULT NULL COMMENT 'Tên/ Tên gọi khác của doanh nghiệp',
  `T_Name_04` varchar(200)  DEFAULT NULL COMMENT 'Tên khác',
  `T_Name_05` varchar(200)  DEFAULT NULL COMMENT 'Tên khác',
  
  `I_Status_01`  int(11) DEFAULT NULL COMMENT '0: cần duyệt, 10: đã duyệt và đang hoạt động, 20:đã duyệt và tạm ngừng hoạt động, 100: không còn hoạt động',
  `I_Status_02`  int(11) DEFAULT NULL ,
  
  `I_Type_01` int(11) DEFAULT NULL COMMENT 'kiểu person:  100: kiểu cán bộ công nhân viên chức, 200: sinh viên, 1000: doanh nghiệp,',
  `I_Type_02` int(11) DEFAULT NULL COMMENT 'kiểu phân loại: doanh nghiệp: đối tác, cung ứng, khách hàng , cán bộ: giảng viên, chuyên viên..., sinh viên: ....',
  `I_Type_03` int(11) DEFAULT NULL COMMENT '0: ko phân biệt, 1: nam, 2: nữ',
  `I_Type_04` int(11) DEFAULT NULL COMMENT 'tình trạng tôn giáo	: không, phật giáo, công giáo, khác',
  `I_Type_05` int(11) DEFAULT NULL COMMENT 'tình trạng đảng phái: Không, Đoàn, Đảng, Khác',
  `I_Type_06` int(11) DEFAULT NULL COMMENT 'tình trạng gia đình 01: kết hôn, độc thân...',
  `I_Type_07` int(11) DEFAULT NULL COMMENT 'tình trạng gia đình 02: đối tượng (hộ nghèo, thương binh, liệt sĩ...)',
  `I_Type_08` int(11) DEFAULT NULL COMMENT 'tình trạng tuyển sinh/tuyển dụng 01: khu vực tuyển sinh',
  `I_Type_09` int(11) DEFAULT NULL COMMENT 'tình trạng tuyển sinh/tuyển dụng 02: hình thức xét tuyển',
  `I_Type_10` int(11) DEFAULT NULL COMMENT 'tình trạng khác',
  
  `F_Val_01` double DEFAULT NULL COMMENT 'hệ số lương khởi động',
  `F_Val_02` double DEFAULT NULL COMMENT 'hệ số lương hiện tại',
  `F_Val_03` double DEFAULT NULL COMMENT 'other',
  `F_Val_04` double DEFAULT NULL COMMENT 'other',
  `F_Val_05` double DEFAULT NULL COMMENT 'other',
  
  `T_Code_01` varchar(200)  DEFAULT NULL COMMENT 'CMND, đăng ký kinh doanh',
  `T_Code_02` varchar(200)  DEFAULT NULL COMMENT 'CCCD, số đăng ký kinh doanh khác nếu có',
  `T_Code_03` varchar(200)  DEFAULT NULL COMMENT 'Số BHXH',
  `T_Code_04` varchar(200)  DEFAULT NULL COMMENT 'Mã QL nội bộ: số sinh viên, mã phòng ban',
  `T_Code_05` varchar(200)  DEFAULT NULL COMMENT 'Mã QL khác nếu có',
  `T_Code_06` varchar(200)  DEFAULT NULL COMMENT 'Mã QL khác nếu có',
  `T_Code_07` varchar(200)  DEFAULT NULL COMMENT 'Mã QL khác nếu có',
  `T_Code_08` varchar(200)  DEFAULT NULL COMMENT 'Mã QL khác nếu có',
  `T_Code_09` varchar(200)  DEFAULT NULL COMMENT 'Mã QL khác nếu có',
  `T_Code_10` varchar(200)  DEFAULT NULL COMMENT 'Mã QL khác nếu có',
  
  `T_Info_01` text  DEFAULT NULL COMMENT 'Json Thông tin cụ thể như địa chỉ tạm trú, thông tin cha mẹ....',
  `T_Info_02` text  DEFAULT NULL COMMENT 'Json Thông tin khác',
  `T_Info_03` text  DEFAULT NULL COMMENT 'Json khác',
  `T_Info_04` text  DEFAULT NULL COMMENT 'Json khác',
  `T_Info_05` text  DEFAULT NULL COMMENT 'Json khác',
  `T_Info_06` text  DEFAULT NULL COMMENT 'Json khác',
  `T_Info_07` text  DEFAULT NULL COMMENT 'Json khác',
  `T_Info_08` text  DEFAULT NULL COMMENT 'Json khác',
  `T_Info_09` text  DEFAULT NULL COMMENT 'Json khác',
  `T_Info_10` text  DEFAULT NULL COMMENT 'Json khác',
  
  `D_Date_01` datetime DEFAULT NULL COMMENT 'Ngày tạo',
  `D_Date_02` datetime DEFAULT NULL COMMENT 'Ngày thay đổi',
  `D_Date_03` datetime DEFAULT NULL COMMENT 'Ngày sinh',
  `D_Date_04` datetime DEFAULT NULL COMMENT 'Ngày bắt đầu (bắt đầu làm việc, học)',
  `D_Date_05` datetime DEFAULT NULL COMMENT 'Ngày kết thúc (nghỉ việc, nghỉ học)',
  `D_Date_06` datetime DEFAULT NULL COMMENT 'Ngày ...',
  `D_Date_07` datetime DEFAULT NULL COMMENT 'Ngày ...',
  `D_Date_08` datetime DEFAULT NULL COMMENT 'Ngày ...',
  `D_Date_09` datetime DEFAULT NULL COMMENT 'Ngày ...',
  `D_Date_10` datetime DEFAULT NULL COMMENT 'Ngày ...',
  
  `I_Aut_User_01` int(11) DEFAULT NULL COMMENT 'Người tạo',
  `I_Aut_User_02` int(11) DEFAULT NULL COMMENT 'Người thay đổi',
  `I_Per_Manager` int(11) DEFAULT NULL,
  
  PRIMARY KEY (`I_ID`),
  
  KEY `idx_TPERS_01` (`T_Name_01`),
  KEY `idx_TPERS_02` (`T_Name_02`),
  KEY `idx_TPERS_03` (`T_Name_03`),
  
  KEY `idx_TPERS_11` (`T_Code_01`),
  KEY `idx_TPERS_12` (`T_Code_02`),
  KEY `idx_TPERS_13` (`T_Code_03`),
  KEY `idx_TPERS_14` (`T_Code_04`),
  KEY `idx_TPERS_15` (`T_Code_05`),
  
  KEY `idx_TPERS_21` (`I_Type_01`),
  KEY `idx_TPERS_22` (`I_Type_02`),
  KEY `idx_TPERS_23` (`I_Type_03`),
  KEY `idx_TPERS_24` (`I_Type_04`),
  KEY `idx_TPERS_25` (`I_Type_05`),
  KEY `idx_TPERS_26` (`I_Type_06`),
  KEY `idx_TPERS_27` (`I_Type_07`),
  KEY `idx_TPERS_28` (`I_Type_08`),
  KEY `idx_TPERS_29` (`I_Type_09`),
  KEY `idx_TPERS_30` (`I_Type_10`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;





