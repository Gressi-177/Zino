INSERT INTO TA_AUT_ROLE (I_ID, I_Status, T_Name, T_Info_01, T_Info_02, T_Aut_Right) VALUES(  0, 1, 'RO_FREE'		, 'Free'									    , 'aut_free', '');
INSERT INTO TA_AUT_ROLE (I_ID, I_Status, T_Name, T_Info_01, T_Info_02, T_Aut_Right) VALUES(100, 1, 'RO_ADM_SUPER'	, 'Super Admin'									, 'aut_adm'	, '[100,101,102,103,104,105]');
INSERT INTO TA_AUT_ROLE (I_ID, I_Status, T_Name, T_Info_01, T_Info_02, T_Aut_Right) VALUES(101, 1, 'RO_ADM_SYS'		, 'Administrateur Système'						, 'aut_adm'	, '[101,102,103,104,105]');

INSERT INTO TA_AUT_ROLE (I_ID, I_Status, T_Name, T_Info_01, T_Info_02, T_Aut_Right) VALUES(200, 1, 'RO_USR_ADM'		, 'Administrateur de Référentiel'				, 'aut_adm'	, '[101,102,103,104,105]');

INSERT INTO TA_AUT_ROLE (I_ID, I_Status, T_Name, T_Info_01, T_Info_02, T_Aut_Right) VALUES(201, 1, 'RO_USR_STD'		, 'Utilisateur Standard'						, 'aut_user', '[101,105]');
INSERT INTO TA_AUT_ROLE (I_ID, I_Status, T_Name, T_Info_01, T_Info_02, T_Aut_Right) VALUES(202, 1, 'RO_USR_EDIT'	, 'Éditeur de Données'							, 'aut_user', '[101,103,105]');
INSERT INTO TA_AUT_ROLE (I_ID, I_Status, T_Name, T_Info_01, T_Info_02, T_Aut_Right) VALUES(203, 1, 'RO_USR_CONS'	, 'Consultant'									, 'aut_user', '[101,102,105]');
INSERT INTO TA_AUT_ROLE (I_ID, I_Status, T_Name, T_Info_01, T_Info_02, T_Aut_Right) VALUES(204, 1, 'RO_USR_GET'		, 'Accès en Lecture Seule'						, 'aut_user', '[101]');
INSERT INTO TA_AUT_ROLE (I_ID, I_Status, T_Name, T_Info_01, T_Info_02, T_Aut_Right) VALUES(205, 1, 'RO_USR_MOD'		, 'Accès en Écriture Limitée'					, 'aut_user', '[101,103]');
INSERT INTO TA_AUT_ROLE (I_ID, I_Status, T_Name, T_Info_01, T_Info_02, T_Aut_Right) VALUES(206, 1, 'RO_USR_SPEC'	, 'Accès à des Fonctionnalités Spécifiques'		, 'aut_user', '[101,102,103,105]');
