-- ------------------------------------------------------------------------------------
-- ------------------------------------------------------------------------------------
/* insert main manager */
INSERT INTO TA_PER_PERSON
(I_ID, I_Status_01, I_Type_01, I_Type_02, T_Name_01, T_Name_02, T_Code_01, T_Code_02, T_Info_01, T_Info_02, T_Info_03, T_Info_04, T_Info_05, D_Date_01, D_Date_02, D_Date_03, I_Aut_User_01, I_Aut_User_02, I_Per_Manager)
VALUES(1, 1, 200, 2001000, 'RATP', 'RATP', 'RATP', 'RATP', 'Paris', '(+33)', 'contact@ratp.fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

INSERT INTO TA_PER_PERSON
(I_ID, I_Status_01, I_Type_01, I_Type_02, T_Name_01, T_Name_02, T_Code_01, T_Code_02, T_Info_01, T_Info_02, T_Info_03, T_Info_04, T_Info_05, D_Date_01, D_Date_02, D_Date_03, I_Aut_User_01, I_Aut_User_02, I_Per_Manager)
VALUES(2, 1, 100, 1001000, 'Adm', 'Adm', 'Adm', 'Adm', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, 1);

/* insert adm user */
INSERT INTO TA_AUT_USER (I_Status,I_Type_01,T_Login_01,T_Pass_01,T_Info_01,T_Info_02,T_Info_03,T_Info_04,T_Info_05,D_Date_01,D_Date_02,D_Date_03,I_Aut_User_01,I_Aut_User_02,I_Aut_User_03, I_Per_Manager, I_Per_Person) VALUES 
(1,2,'adm','86f65e28a754e1a71b2df9403615a6c436c32c42a75a10d02813961b86f1e428','adm@ratp.fr','099',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1, 1, 1, 2)
/*pass: adm*/

