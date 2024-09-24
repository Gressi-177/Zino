package com.hnv.db.sor.vi;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.hnv.api.main.Hnv_CfgHibernate;
import com.hnv.db.EntityAbstract;
import com.hnv.db.EntityDAO;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.mat.TaMatMaterial;
import com.hnv.db.per.TaPerPerson;
import com.hnv.db.sor.TaSorOrder;
import com.hnv.db.sor.TaSorOrderDetail;
import com.hnv.def.DefDBExt;		

/**
* TaInvOrder by H&V SAS
*/
@Entity
public class ViSorOrder extends EntityAbstract<ViSorOrder> {

	private static final long serialVersionUID = 1L;

	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                              =	"I_ID";
	public static final String	COL_I_TYPE_01                         =	"I_Type_01";
	public static final String	COL_I_TYPE_02                         =	"I_Type_02";
	public static final String	COL_I_TYPE_03                         =	"I_Type_03";
	
	public static final String	COL_D_DATE_01                         =	"D_Date_01";
	public static final String	COL_D_DATE_02                         =	"D_Date_02";
	public static final String	COL_D_DATE_03                         =	"D_Date_03";
	public static final String	COL_D_DATE_04                         =	"D_Date_04";
	
	public static final String	COL_F_VAL_00                         =	"F_Val_00";
	public static final String	COL_F_VAL_01                         =	"F_Val_01";
	public static final String	COL_F_VAL_02                         =	"F_Val_02";
	public static final String	COL_F_VAL_03                         =	"F_Val_03";
	public static final String	COL_F_VAL_04                         =	"F_Val_04";
	public static final String	COL_F_VAL_05                         =	"F_Val_05";
	public static final String	COL_F_VAL_06                         =	"F_Val_06";
	public static final String	COL_F_VAL_07                         =	"F_Val_07";
	public static final String	COL_F_VAL_08                         =	"F_Val_08";
	public static final String	COL_F_VAL_09                         =	"F_Val_09";
	public static final String	COL_F_VAL_10                         =	"F_Val_10";
	
	public static final String	COL_T_CODE_01                         =	"T_Code_01";
	public static final String	COL_T_CODE_02                         =	"T_Code_02";	
	
	public static final String	COL_T_NAME_01                         =	"T_Name_01";
	public static final String	COL_T_NAME_02                         =	"T_Name_02";
	public static final String	COL_T_NAME_03                         =	"T_Name_03";
	
	public static final String	COL_T_INFO_05                         =	"T_Info_05";
	public static final String	COL_T_INFO_04                         =	"T_Info_04";
	public static final String	COL_T_INFO_03                         =	"T_Info_03";
	public static final String	COL_T_INFO_02                         =	"T_Info_02";
	public static final String	COL_T_INFO_01                         =	"T_Info_01";
	
	
	public static final String	COL_T_STATUS                          =	"T_Status";
	public static final String	COL_I_STATUS                          =	"I_Status";
	
	public static final String  COL_I_MAT_VAL_01					  = "I_Mat_Val_01";
	public static final String  COL_I_MAT_VAL_02					  = "I_Mat_Val_02";
	
	public static final String	COL_I_AUT_USER_01                     =	"I_Aut_User_01";
	public static final String	COL_I_AUT_USER_02                     =	"I_Aut_User_02";
	
	public static final String	COL_I_PER_MANAGER                     =	"I_Per_Manager";
	public static final String	COL_I_PER_PERSON_01                   =	"I_Per_Person_01";
	public static final String	COL_I_PER_PERSON_02                   =	"I_Per_Person_02";
	public static final String	COL_I_PER_PERSON_03                   =	"I_Per_Person_03";
	
	public static final String  COL_T_MAT_CODE_01					  = "T_Mat_Code_01";
	public static final String  COL_T_MAT_CODE_02					  = "T_Mat_Code_02";
	public static final String  COL_I_MAT_MATERIAL					  = "I_Mat_Material";
	
	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              =	"I_ID";
	public static final String	ATT_I_TYPE_01                         =	"I_Type_01";
	public static final String	ATT_I_TYPE_02                         =	"I_Type_02";
	public static final String	ATT_D_DATE_01                         =	"D_Date_01";
	public static final String	ATT_D_DATE_02                         =	"D_Date_02";
	public static final String	ATT_D_DATE_03                         =	"D_Date_03";
	public static final String	ATT_T_CODE_01                         =	"T_Code_01";
	public static final String	ATT_T_CODE_02                         =	"T_Code_02";
	public static final String	ATT_I_STATUS                          =	"I_Status";
	
	public static final String	ATT_T_NAME_01                         =	"T_Name_01";
	public static final String	ATT_T_NAME_02                         =	"T_Name_02";
	public static final String	ATT_T_NAME_03                         =	"T_Name_03";
	
	public static final String	ATT_I_PERMANAGER                      =	"I_Per_Manager";
	
	public static final String	ATT_I_PER_PERSON_01                   =	"I_Per_Person_01";
	public static final String	ATT_I_PER_PERSON_02                   =	"I_Per_Person_02";
	public static final String	ATT_I_PER_PERSON_03                   =	"I_Per_Person_03";
	
	public static final String	ATT_T_STATUS                          =	"T_Status";
	
	//--------------------------------------------------------------------------------------------------------------------------
	public static final String SQL_REQ_LIST_SELECT = "SELECT "
			+		" s."	+ TaSorOrder.COL_I_ID		+ " AS "	+ COL_I_ID		+ ","
			+		" s."	+ TaSorOrder.COL_T_CODE_01	+ " AS "	+ COL_T_CODE_01	+ ","
			+		" s."	+ TaSorOrder.COL_T_CODE_02	+ " AS "	+ COL_T_CODE_02	+ ","
			
			+		" s."	+ TaSorOrder.COL_I_TYPE_01	+ " AS "	+ COL_I_TYPE_01	+ ","
			+		" s."	+ TaSorOrder.COL_I_TYPE_02	+ " AS "	+ COL_I_TYPE_02	+ ","
			+		" s."	+ TaSorOrder.COL_I_TYPE_03	+ " AS "	+ COL_I_TYPE_03	+ ","
			
			+		" s."	+ TaSorOrder.COL_F_VAL_03	+ " AS "	+ COL_F_VAL_03	+ ","
			+		" s."	+ TaSorOrder.COL_F_VAL_04	+ " AS "	+ COL_F_VAL_04	+ ","
			+		" s."	+ TaSorOrder.COL_F_VAL_06	+ " AS "	+ COL_F_VAL_06	+ ","
			+		" s."	+ TaSorOrder.COL_F_VAL_07	+ " AS "	+ COL_F_VAL_07	+ ","
			
			+		" s."	+ TaSorOrder.COL_D_DATE_01	+ " AS "	+ COL_D_DATE_01	+ ","
			+		" s."	+ TaSorOrder.COL_D_DATE_02	+ " AS "	+ COL_D_DATE_02	+ ","
			+		" s."	+ TaSorOrder.COL_D_DATE_03	+ " AS "	+ COL_D_DATE_03	+ ","
			
			+		" s."	+ TaSorOrder.COL_I_STATUS	+ " AS "	+ COL_I_STATUS	+ ","
			
			////////////////////////////////////////////////////////
			+		" s."	+ TaSorOrder.COL_I_PER_MANAGER	+ " AS "	+ COL_I_PER_MANAGER	+ ","
			
			+		" s."	+ TaSorOrder.COL_I_MAT_VAL_01	+ " AS "	+ COL_I_MAT_VAL_01	+ ","
			+		" s."	+ TaSorOrder.COL_I_AUT_USER_01	+ " AS "	+ COL_I_AUT_USER_01	+ ","
			
			// null field
			+		"NULL AS "	+ COL_T_NAME_01		+ ","
			+		"NULL AS "	+ COL_T_NAME_02		+ ","
			+		"NULL AS "	+ COL_T_NAME_03		+ ","
			
			+		"NULL AS "	+ COL_I_PER_PERSON_01		+ ","
			+		"NULL AS "	+ COL_I_PER_PERSON_02		+ ","
			+		"NULL AS "	+ COL_I_PER_PERSON_03		+ ","
			
			+		"NULL AS "	+ COL_D_DATE_04				+ ","
			+		"NULL AS "	+ COL_F_VAL_02				+ ","
			+		"NULL AS "	+ COL_T_INFO_05				+ ","
			+		"NULL AS "	+ COL_T_MAT_CODE_01			+ ","
			+		"NULL AS "	+ COL_T_MAT_CODE_02			+ ","
			+		"NULL AS "	+ COL_I_MAT_MATERIAL		+ ","
			
			+		" NULL "		+ " AS "	+ COL_T_STATUS;
	
	public static final String SQL_REQ_LIST_SELECT_FILTER = "SELECT "
			+		" s."	+ TaSorOrder.COL_I_ID		+ " AS "	+ COL_I_ID		+ ","
			+		" s."	+ TaSorOrder.COL_T_CODE_01	+ " AS "	+ COL_T_CODE_01	+ ","
			+		" s."	+ TaSorOrder.COL_T_CODE_02	+ " AS "	+ COL_T_CODE_02	+ ","
			
			+		" s."	+ TaSorOrder.COL_I_TYPE_01	+ " AS "	+ COL_I_TYPE_01	+ ","
			+		" s."	+ TaSorOrder.COL_I_TYPE_02	+ " AS "	+ COL_I_TYPE_02	+ ","
			+		" s."	+ TaSorOrder.COL_I_TYPE_03	+ " AS "	+ COL_I_TYPE_03	+ ","
			
			+		" s."	+ TaSorOrder.COL_D_DATE_01	+ " AS "	+ COL_D_DATE_01	+ ","
			+		" s."	+ TaSorOrder.COL_D_DATE_02	+ " AS "	+ COL_D_DATE_02	+ ","
			+		" s."	+ TaSorOrder.COL_D_DATE_03	+ " AS "	+ COL_D_DATE_03	+ ","
			
			+		" p1."	+ TaPerPerson.ATT_T_NAME_01	+ " AS "	+ COL_T_NAME_01	+ ","
			+		" p2."	+ TaPerPerson.ATT_T_NAME_01	+ " AS "	+ COL_T_NAME_02 + ","
			+		" p3."	+ TaPerPerson.ATT_T_NAME_01	+ " AS "	+ COL_T_NAME_03 + ","
			
			+		" s."	+ TaSorOrder.COL_I_STATUS	+ " AS "	+ COL_I_STATUS	+ ","
			
			// null field
			+		"NULL AS "	+ COL_D_DATE_04				+ ","
			+		"NULL AS "	+ COL_I_PER_PERSON_01		+ ","
			+		"NULL AS "	+ COL_I_PER_PERSON_02		+ ","
			+		"NULL AS "	+ COL_I_PER_PERSON_03		+ ","
			+		"NULL AS "	+ COL_T_INFO_05				+ ","
			+		"NULL AS "	+ COL_T_MAT_CODE_01			+ ","
			+		"NULL AS "	+ COL_T_MAT_CODE_02			+ ","
			+		"NULL AS "	+ COL_I_MAT_MATERIAL		+ ","
			////////////////////////////////////////////////////////
			+		" s."	+ TaSorOrder.COL_I_PER_MANAGER	+ " AS "	+ COL_I_PER_MANAGER	+ ","
			
			+		" s."	+ TaSorOrder.COL_F_VAL_02	+ " AS "	+ COL_F_VAL_02	+ ","
			+		" s."	+ TaSorOrder.COL_F_VAL_03	+ " AS "	+ COL_F_VAL_03	+ ","
			+		" s."	+ TaSorOrder.COL_F_VAL_04	+ " AS "	+ COL_F_VAL_04	+ ","
			+		" s."	+ TaSorOrder.COL_F_VAL_06	+ " AS "	+ COL_F_VAL_06	+ ","
			+		" s."	+ TaSorOrder.COL_F_VAL_07	+ " AS "	+ COL_F_VAL_07	+ ","
			
			+		" s."	+ TaSorOrder.COL_I_MAT_VAL_01	+ " AS "	+ COL_I_MAT_VAL_01	+ ","
			+		" s."	+ TaSorOrder.COL_I_AUT_USER_01	+ " AS "	+ COL_I_AUT_USER_01	+ ","
			
			+		" NULL "		+ " AS "	+ COL_T_STATUS;
	
	
	static final String SQL_REQ_LIST_SELECT_FILTER_BY_PERSON	=  "SELECT distinct "
			+		" s."	+ TaSorOrder.COL_I_ID					+ " AS "	+ COL_I_ID		+ ","
			+		" s."	+ TaSorOrder.COL_I_PER_PERSON_01		+ " AS "	+ ATT_I_PER_PERSON_01		+ ","
			+		" s."	+ TaSorOrder.COL_I_PER_PERSON_02		+ " AS "	+ ATT_I_PER_PERSON_02		+ ","
			+		" s."	+ TaSorOrder.COL_I_PER_PERSON_03		+ " AS "	+ ATT_I_PER_PERSON_03		+ ","
			
			+		" s."	+ TaSorOrder.COL_D_DATE_01	+ " AS "	+ COL_D_DATE_01	+ ","
			+		" s."	+ TaSorOrder.COL_D_DATE_02	+ " AS "	+ COL_D_DATE_02	+ ","
			+		" s."	+ TaSorOrder.COL_D_DATE_03	+ " AS "	+ COL_D_DATE_03	+ ","
			+		" s."	+ TaSorOrder.COL_D_DATE_04	+ " AS "	+ COL_D_DATE_04	+ ","
			
			// null field
			+		"NULL AS "	+ COL_T_NAME_01	+ ","
			+		"NULL AS "	+ COL_T_NAME_02	+ ","
			+		"NULL AS "	+ COL_T_NAME_03	+ ","
			+		"NULL AS "	+ COL_I_TYPE_02	+ ","
			+		"NULL AS "	+ COL_T_CODE_02	+ ","
			+		"NULL AS "	+ COL_T_STATUS	+ ","
			//////////////////////////////////////////////		
			
			+		" s."	+ TaSorOrder.COL_F_VAL_02	+ " AS "	+ COL_F_VAL_02	+ ","
			+		" s."	+ TaSorOrder.COL_F_VAL_03	+ " AS "	+ COL_F_VAL_03	+ ","
			+		" s."	+ TaSorOrder.COL_F_VAL_04	+ " AS "	+ COL_F_VAL_04	+ ","
			+		" s."	+ TaSorOrder.COL_F_VAL_06	+ " AS "	+ COL_F_VAL_06	+ ","
			+		" s."	+ TaSorOrder.COL_F_VAL_07	+ " AS "	+ COL_F_VAL_07	+ ","
			
			+		" s."	+ TaSorOrder.COL_T_CODE_01	+ " AS "	+ COL_T_CODE_01	+ ","
			+		" s."	+ TaSorOrder.COL_I_TYPE_01	+ " AS "	+ COL_I_TYPE_01	+ ","
			+		" s."	+ TaSorOrder.COL_I_TYPE_03	+ " AS "	+ COL_I_TYPE_03	+ ","
			+		" s."	+ TaSorOrder.COL_I_STATUS	+ " AS "	+ COL_I_STATUS	+ ","
			
			+		" s."	+ TaSorOrder.COL_T_INFO_05	+ " AS "	+ COL_T_INFO_05	+ ","
			
			//////////////////////////////////////////////		
			+		"NULL AS "	+ COL_I_MAT_MATERIAL	+ ","
			+		"NULL AS "	+ COL_T_MAT_CODE_01		+ ","
			+		"NULL AS "	+ COL_T_MAT_CODE_02;
	
	static final String SQL_REQ_LIST_DYN_COUNT 			= 	"SELECT count(DISTINCT(s."+ TaSorOrder.COL_I_ID		+"))";
	
	
	static final String SQL_REQ_LIST_DYN_FROM_O_DETAIL	= 	" FROM "		+ DefDBExt.TA_SOR_ORDER		+ " s" 
															+ 	" LEFT JOIN " 	+ DefDBExt.TA_SOR_ORDER_DETAIL 		+ " d"
															+	" ON s." 		+ TaSorOrder.ATT_I_ID	+ " = d." 	+ TaSorOrderDetail.ATT_I_SOR_ORDER;
	
	static final String SQL_REQ_LIST_DYN_FROM_MAT 		= 	" LEFT JOIN " 	+ DefDBExt.TA_MAT_MATERIAL	+ " m"
															+	" ON d." 		+ TaSorOrderDetail.ATT_I_MAT_MATERIAL	+ " = m." 	+ TaMatMaterial.ATT_I_ID;
	
	static final String SQL_REQ_LIST_DYN_FROM_CLI		=	" FROM "		+ DefDBExt.TA_SOR_ORDER				+ " s" 
														+ 	" LEFT JOIN " 	+ DefDBExt.TA_PER_PERSON 			+ " p1"
														+	" ON s." 		+ TaSorOrder.ATT_I_PER_PERSON_01	+ " = p1." 	+ TaPerPerson.ATT_I_ID;
	
	
	static final String SQL_REQ_LIST_DYN_FROM_SUPL		= 	" LEFT JOIN " 	+ DefDBExt.TA_PER_PERSON 			+ " p2"
														+	" ON s." 		+ TaSorOrder.ATT_I_PER_PERSON_02	+ " = p2." 	+ TaPerPerson.ATT_I_ID;
	
	
	static final String SQL_REQ_LIST_FROM_ORDER			= 	" FROM "		+ DefDBExt.TA_SOR_ORDER				+ " s" ;
	static final String SQL_REQ_LIST_JOIN_ORDER_DETAIL	= 	" INNER JOIN " 	+ DefDBExt.TA_SOR_ORDER_DETAIL 		+ " d" 	+	" ON s." 	+ TaSorOrder.ATT_I_ID				+ " = d." 	+ TaSorOrderDetail.ATT_I_SOR_ORDER;
	static final String SQL_REQ_LIST_JOIN_PER_SUPL		= 	" INNER JOIN " 	+ DefDBExt.TA_PER_PERSON 			+ " p1"	+	" ON s."	+ TaSorOrder.ATT_I_PER_PERSON_01	+ " = p1." 	+ TaPerPerson.ATT_I_ID;
	static final String SQL_REQ_LIST_JOIN_PER_CLI		= 	" INNER JOIN " 	+ DefDBExt.TA_PER_PERSON 			+ " p2"	+	" ON s."	+ TaSorOrder.ATT_I_PER_PERSON_02	+ " = p2." 	+ TaPerPerson.ATT_I_ID;
	static final String SQL_REQ_LIST_JOIN_PER_TP		= 	" INNER JOIN " 	+ DefDBExt.TA_PER_PERSON 			+ " p3"	+	" ON s."	+ TaSorOrder.ATT_I_PER_PERSON_03	+ " = p3." 	+ TaPerPerson.ATT_I_ID;
	
	//---------------------------------------------------------------------------------------------------------------------------------------
	static final String SQL_REQ_LIST_ORDER_SELECT	=  "SELECT * FROM " + DefDBExt.TA_SOR_ORDER + " s" ;
	
	static final String SQL_REQ_WHERE 				=  " WHERE true ";
	
	static final String SQL_REQ_WHERE_PER_MAN		=  " AND s."	+ TaSorOrder.ATT_I_PER_MANAGER	+ " = "	;    
	
	static final String SQL_REQ_WHERE_TYPE_01 		=  " AND s."	+ TaSorOrder.COL_I_TYPE_01		+ " = "	;
	static final String SQL_REQ_WHERE_TYPE_02 		=  " AND s."	+ TaSorOrder.COL_I_TYPE_02		+ " = "	;    
	static final String SQL_REQ_WHERE_TYPE_03 		=  " AND s."	+ TaSorOrder.COL_I_TYPE_03		+ " = "	;    
    static final String SQL_REQ_WHERE_STATUS 		=  " AND s."	+ TaSorOrder.COL_I_STATUS		+ " = " ;
    
    static final String SQL_REQ_WHERE_PER_01 		=  " AND s."	+ TaSorOrder.COL_I_PER_PERSON_01		+ " = "	;
    static final String SQL_REQ_WHERE_PER_02 		=  " AND s."	+ TaSorOrder.COL_I_PER_PERSON_02		+ " = "	;
    static final String SQL_REQ_WHERE_PER_03 		=  " AND s."	+ TaSorOrder.COL_I_PER_PERSON_03		+ " = "	;
    
    static final String SQL_REQ_WHERE_MAT 			=  " AND d."	+ TaSorOrderDetail.COL_I_MAT_MATERIAL	+ " = "	;
    
    
    static final String SQL_REQ_WHERE_WH			=  " AND s."	+ TaSorOrder.COL_I_MAT_VAL_01 + " = ";
    
    static final String SQL_REQ_WHERE_FROMID		=  " AND s."	+ TaSorOrder.COL_I_ID + " >= ";
    static final String SQL_REQ_WHERE_TOID			=  " AND s."	+ TaSorOrder.COL_I_ID + " <= ";
    
    static final String SQL_REQ_WHERE_USER_ID		=  " AND s."	+ TaSorOrder.ATT_I_AUT_USER_01 + " = ";
    
    static final String SQL_REQ_WHERE_SUP_ID		=  " AND s."	+ TaSorOrder.COL_I_PER_PERSON_01 + " = ";
    static final String SQL_REQ_WHERE_CLI_ID		=  " AND s."	+ TaSorOrder.ATT_I_PER_PERSON_02 + " = ";
    static final String SQL_REQ_WHERE_TP_ID			=  " AND s."	+ TaSorOrder.ATT_I_PER_PERSON_03 + " = ";
    
    static final String SQL_REQ_WHERE_DT01_FROM		=  " AND s."	+ TaSorOrder.COL_D_DATE_01 + " >= "; //creation date
    static final String SQL_REQ_WHERE_DT01_TO		=  " AND s."	+ TaSorOrder.COL_D_DATE_01 + " <= ";
    static final String SQL_REQ_WHERE_DT02_FROM		=  " AND s."	+ TaSorOrder.COL_D_DATE_02 + " >= "; //modif date
    static final String SQL_REQ_WHERE_DT02_TO		=  " AND s."	+ TaSorOrder.COL_D_DATE_02 + " <= ";
    static final String SQL_REQ_WHERE_DT03_FROM		=  " AND s."	+ TaSorOrder.COL_D_DATE_03 + " >= "; //offical date
    static final String SQL_REQ_WHERE_DT03_TO		=  " AND s."	+ TaSorOrder.COL_D_DATE_03 + " <= ";
    
    static final String SQL_REQ_WHERE_ORD_CODE		= "  AND (lower(s."  + TaSorOrder.COL_T_CODE_01 + ") like lower('%s') or lower(s."  + TaSorOrder.COL_T_CODE_02 + ") like lower('%s'))";
    
    static final String SQL_REQ_WHERE_CLI_PHONE		= "  AND (lower(p2."  + TaPerPerson.ATT_T_INFO_02 + ") like lower('%s'))";
    
    static final String SQL_REQ_WHERE_AUT_USER_01	=  " AND s."	+ TaSorOrder.COL_I_AUT_USER_01 	+ " = %d";

	public static final String SQL_WHERE_SEARCH 	= " lower(s."	+ TaSorOrder.COL_T_CODE_01 + ") like lower('%s')";
	
//	public static final String SQL_WHERE_SEARCH_NB_ORD 	= " lower(s." + TaSorOrder.COL_T_NUMBER + ") like lower('%";
	public static final String SQL_WHERE_SEARCH_CODE 	= " (lower(s." + TaSorOrder.COL_T_CODE_01 + ") like lower('%";
	
	public static final String SQL_WHERE_SEARCH_DATE 	= " AND " + TaSorOrder.COL_D_DATE_01		+ " between '%s' and '%s' ";
	
	public static final String SQL_WHERE_SEARCH_SUPPLIER 	= " AND s."	+ TaSorOrder.COL_I_PER_PERSON_02		+ " = ";
	public static final String SQL_WHERE_SEARCH_CLIENT 		= " AND s."	+ TaSorOrder.COL_I_PER_PERSON_01		+ " = ";
													  
	
	public static final String SQL_ORDER			= " ORDER BY %s %s";
	public static SimpleDateFormat changeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<ViSorOrder> 	DAO;
	static{
		DAO = new EntityDAO<ViSorOrder>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN) , ViSorOrder.class, RIGHTS,  HISTORY, DefDBExt.TA_SOR_ORDER, DefDBExt.ID_TA_SOR_ORDER);
	}

	//-----------------------Class Attributs-------------------------
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;
         
	@Column(name=COL_I_TYPE_01, nullable = true)
	private	Integer         I_Type_01;
	
	@Column(name=COL_I_TYPE_02, nullable = true)
	private	Integer         I_Type_02;
	
	@Column(name=COL_I_TYPE_03, nullable = true)
	private	Integer         I_Type_03;
       
	@Column(name=COL_D_DATE_01, nullable = true)
	private	Date            D_Date_01;
	
	@Column(name=COL_D_DATE_02, nullable = true)
	private	Date            D_Date_02;
	
	@Column(name=COL_D_DATE_03, nullable = true)
	private	Date            D_Date_03;
	
	@Column(name=COL_D_DATE_04, nullable = true)
	private	Date            D_Date_04;

	@Column(name=COL_T_CODE_01, nullable = true)
	private	String          T_Code_01;
	
	@Column(name=COL_T_CODE_02, nullable = true)
	private	String          T_Code_02;
    
	@Column(name=COL_I_STATUS, nullable = true)
	private	Integer         I_Status;

   
	@Column(name=COL_T_STATUS, nullable = true)
	private	String          T_Status;
	
	@Column(name=COL_T_NAME_01, nullable = true)
	private	String         	T_Name_01;
	@Column(name=COL_T_NAME_02, nullable = true)
	private	String         	T_Name_02;
	@Column(name=COL_T_NAME_03, nullable = true)
	private	String         	T_Name_03;
	
	// new field....
	@Column(name=ATT_I_PER_PERSON_01, nullable = true)
	private	String          I_Per_Person_01;
	@Column(name=ATT_I_PER_PERSON_02, nullable = true)
	private	String          I_Per_Person_02;
	@Column(name=ATT_I_PER_PERSON_03, nullable = true)
	private	String          I_Per_Person_03;
	
	@Column(name=COL_F_VAL_02, nullable = true)
	private	Double          F_Val_02;
	
	@Column(name=COL_F_VAL_03, nullable = true)
	private	Double          F_Val_03;
	@Column(name=COL_F_VAL_04, nullable = true)
	private	Double          F_Val_04;
	@Column(name=COL_F_VAL_06, nullable = true)
	private	Double          F_Val_06;
	@Column(name=COL_F_VAL_07, nullable = true)
	private	Double          F_Val_07;

	@Column(name=COL_T_INFO_05, nullable = true)
	private	String          T_Info_05;
	
	@Column(name=COL_T_MAT_CODE_01, nullable = true)
	private	String          T_Mat_Code_01;
	
	@Column(name=COL_T_MAT_CODE_02, nullable = true)
	private	String          T_Mat_Code_02;
	
	@Column(name=COL_I_MAT_MATERIAL, nullable = true)
	private	String          I_Mat_Material;

    
	//-----------------------Transient Variables-------------------------


	//---------------------Constructeurs-----------------------
	private ViSorOrder(){}

	public ViSorOrder(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		//doInitDBFlag();
	}
	
	
	
	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}

	@Override
	public void doMergeWith(ViSorOrder ent) {
		if (ent == this) return;
		this.I_Type_01              = ent.I_Type_01;
		this.I_Type_02              = ent.I_Type_02;
		this.D_Date_01              = ent.D_Date_01;
		this.T_Code_01                 = ent.T_Code_01;
		this.I_Status               = ent.I_Status;
		
		//---------------------Merge Transient Variables if exist-----------------------
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;
		
		ok = (I_ID == ((ViSorOrder)o).I_ID);
		if (!ok) return ok;

				
		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}

	@Override
	public String toString() {
		return 	"TaInvOrder { " +
				"I_ID:"+                      I_ID +"," + 
				"I_Type_01:"+                 I_Type_01 +"," + 
				"I_Type_02:"+                 I_Type_02 +"," + 
				"D_Date_01:"+                 D_Date_01 +"," + 
				"T_Code_01:"+                    T_Code_01 +"," + 
				"T_Code_02:"+                    T_Code_02 +"," + 
				"I_Status:"+                  I_Status +"," +
				
				 "}";
	}
	
	private static String reqRestriction(Set<String> searchKey) {	
		if (searchKey==null) return null;
		String restr = "";
		String orT	 =  "";	
		for (String s: searchKey){		
			if (s.equals("%")) continue;
			restr = restr + orT + String.format(SQL_WHERE_SEARCH, s, s);
			orT	  = " AND ";	
		}
		if (restr.length()==0) return null;
		else restr = "( " + restr + " )";
		return restr;
	}
	
	
	public static List<ViSorOrder> reqRestrictionFilterByPerson(Integer manId, Integer matId, String dt01, String dt02, Integer perId, Integer typPerson, String code) throws Exception {
		List<ViSorOrder> result = null;
		
		String sql = SQL_REQ_LIST_SELECT_FILTER_BY_PERSON;	
		
		sql += SQL_REQ_LIST_DYN_FROM_O_DETAIL + SQL_REQ_WHERE;
		
		if (perId != null && perId != -1) {
			if (typPerson.equals(TaPerPerson.TYP_02_SUPPLIER)) 	sql += SQL_REQ_WHERE_PER_01 	+ perId;
            if (typPerson.equals(TaPerPerson.TYP_02_CLIENT)) 	sql += SQL_REQ_WHERE_PER_02 	+ perId; 
            if (typPerson.equals(TaPerPerson.TYP_02_TPARTY)) 	sql += SQL_REQ_WHERE_PER_03 	+ perId; 
		}
		
		if (manId != null) {
        	sql += SQL_REQ_WHERE_PER_MAN		+ manId;
		}
		
		if (code != null) {
			sql += String.format(SQL_REQ_WHERE_ORD_CODE, code,code);
        }
        if (dt01 != null) {
        	sql += SQL_REQ_WHERE_DT01_FROM	+ "'" + dt01 + "' ";
        }
        if (dt02 != null) {
        	sql += SQL_REQ_WHERE_DT01_TO	+ "'" + dt02 + "' ";
        }
        if (matId != null) {
        	sql += SQL_REQ_WHERE_MAT	+ matId;
        }
        	
		result = ViSorOrder.DAO.reqList(sql);
		return result;
	}
	
	public static List<TaSorOrder> reqRestrictionSrc(int begin, int end, Integer perId, Integer typPerson, Set<String> searchKey, String orderCol, String orderDir, Integer searchOpt) throws Exception {
		List<TaSorOrder> result = null;
		String sql = SQL_REQ_LIST_ORDER_SELECT;
		sql += SQL_REQ_WHERE;
		
		if (perId != null && perId != -1) {
			if (typPerson == TaPerPerson.TYP_02_SUPPLIER) 	sql += SQL_REQ_WHERE_PER_01 	+ perId;
            if (typPerson == TaPerPerson.TYP_02_CLIENT) 	sql += SQL_REQ_WHERE_PER_02 	+ perId; 
            if (typPerson == TaPerPerson.TYP_02_TPARTY) 	sql += SQL_REQ_WHERE_PER_03 	+ perId; 
		}
		
		String 					restriction 			= reqRestriction(searchKey);
		if(restriction != null) {
			sql += " AND " + restriction;
		}
	
		sql += String.format(SQL_ORDER, orderCol, orderDir);
		result = TaSorOrder.DAO.reqList(begin, end, sql);
		return result;
	}
	
	public static int reqRestrictionSrc(Integer perId, Integer typPerson, Set<String> searchKey, Integer searchOpt) throws Exception {
		String sql = SQL_REQ_LIST_ORDER_SELECT;
		sql += SQL_REQ_WHERE;
		
		if (perId != null && perId != -1) {
			if (typPerson == TaPerPerson.TYP_02_SUPPLIER) 	sql += SQL_REQ_WHERE_PER_01 	+ perId;
            if (typPerson == TaPerPerson.TYP_02_CLIENT) 	sql += SQL_REQ_WHERE_PER_02 	+ perId; 
            if (typPerson == TaPerPerson.TYP_02_TPARTY) 	sql += SQL_REQ_WHERE_PER_03 	+ perId; 
		}
		
		String 					restriction 			= reqRestriction(searchKey);
		if(restriction != null) {
			sql += " AND " + restriction;
		}
	
		int count = TaSorOrder.DAO.reqCount(sql).intValue();		
		return count;
	}
	
		
	public static List<ViSorOrder> reqListByFilterCondition(
			TaAutUser user,  int begin, int end, 
			Set<String> searchKey, String orderCol, String orderDir, 
			
			Integer fromId, Integer toId,
			Integer type01, Integer type02, Integer type03,  Integer status, 
			
			String dt01_from, String dt01_to, //ngay tao
			String dt03_from, String dt03_to, //ngay so sach
			
			Integer idMan, Integer uId, 
			Integer warehouseId, Integer matId,		
			Integer supId, Integer cliId, Integer tpId,  
			String ordCode, String cliPhone
			
		) throws Exception {
		
		List<ViSorOrder> result = null;
		String sql = SQL_REQ_LIST_SELECT;	
		
		sql += SQL_REQ_LIST_FROM_ORDER;
		
		if (matId != null) {
	       	sql += SQL_REQ_LIST_JOIN_ORDER_DETAIL;
	    }
		if (supId != null && supId>0) {
			sql += SQL_REQ_LIST_JOIN_PER_SUPL	;
		}
		if (cliId != null && cliId>0) {
			sql += SQL_REQ_LIST_JOIN_PER_CLI	;
		}
		if (tpId != null && tpId>0) {
			sql += SQL_REQ_LIST_JOIN_PER_TP	;
		}
		
		sql += SQL_REQ_WHERE;

		if (fromId != null && toId==null) toId 	 = fromId;
		if (fromId == null && toId!=null) fromId = toId ;
		
		if (fromId != null)
			sql += " AND s."	+ TaSorOrder.ATT_I_ID	+ " >= " + fromId;
		if (toId != null)
			sql += " AND s."	+ TaSorOrder.ATT_I_ID	+ " <= " + toId; 
		
		
		if(type01 != null)
			sql += SQL_REQ_WHERE_TYPE_01 	+ type01;
		if(type02 != null)
			sql += SQL_REQ_WHERE_TYPE_02 	+ type02;
		if(type03 != null)
			sql += SQL_REQ_WHERE_TYPE_03 	+ type03;
		if(status != null)
			sql += SQL_REQ_WHERE_STATUS 	+ status;	
		
		if (dt01_from != null) 
			sql += SQL_REQ_WHERE_DT01_FROM	+ "'" + (dt01_from) + "'";
		if (dt01_to != null) 
			sql += SQL_REQ_WHERE_DT01_TO	+ "'" + (dt01_to) + "'";
		if (dt03_from != null) 
			sql += SQL_REQ_WHERE_DT03_FROM	+ "'" + (dt03_from) + "'";
		if (dt03_to != null) 
			sql += SQL_REQ_WHERE_DT03_TO	+ "'" + (dt03_to) + "'";
	        
		if(idMan != null)
			sql += SQL_REQ_WHERE_PER_MAN 	+ idMan;
		if(uId != null)
			sql += SQL_REQ_WHERE_USER_ID 	+ uId;
		if (matId != null) 
        	sql += SQL_REQ_WHERE_MAT	+ matId;
		
		if (supId != null && supId>0) 
			sql += SQL_REQ_WHERE_SUP_ID		+ supId;
		if (cliId != null && cliId>0) 
			sql += SQL_REQ_WHERE_CLI_ID		+ cliId;
		if (tpId != null && tpId>0) 
			sql += SQL_REQ_WHERE_TP_ID		+ tpId;
		
		
		
        if (ordCode != null) 
        	sql += String.format(SQL_REQ_WHERE_ORD_CODE, ordCode+ "%", ordCode+ "%");
        if (cliPhone != null) 
        	sql += String.format(SQL_REQ_WHERE_CLI_PHONE, cliPhone+ "%"); 	
        
		String 					restriction 			= reqRestriction(searchKey);
		if(restriction != null) {
			sql += " AND " + restriction;
		}
	
		sql += String.format(SQL_ORDER, orderCol, orderDir);
		result = ViSorOrder.DAO.reqList(begin, end, sql);		
		
		return result;
	}
	
	public static int reqCountInvOrder(TaAutUser user,  Integer type01, Integer type02, Set<String> searchKey, Integer status, Integer idMan, String code, String cliPhone, Integer cliId, Integer uId) throws Exception {
		String sql = SQL_REQ_LIST_DYN_COUNT;
		
//		if (type02 == TaSorOrder.TYPE_02_ORDER_IN) {
//			sql += SQL_REQ_LIST_DYN_FROM_SUPL + SQL_REQ_WHERE;
//		}else if (type02 == TaSorOrder.TYPE_02_ORDER_OUT) {
//			sql += SQL_REQ_LIST_DYN_FROM_CLI + SQL_REQ_WHERE;
//		}
//		
		sql += SQL_REQ_LIST_DYN_FROM_CLI + SQL_REQ_LIST_DYN_FROM_SUPL + SQL_REQ_WHERE;
		
		if(idMan != null)
			sql += SQL_REQ_WHERE_PER_MAN 	+ idMan;
		if(uId != null)
			sql += SQL_REQ_WHERE_USER_ID 	+ uId;
		
		if(type01 != null)
			sql += SQL_REQ_WHERE_TYPE_01 	+ type01;
		if(type02 != null)
			sql += SQL_REQ_WHERE_TYPE_02 	+ type02;
		if(status != null)
			sql += SQL_REQ_WHERE_STATUS 	+ status;
		
		if (cliId != null && cliId != -1) {
			sql += SQL_REQ_WHERE_CLI_ID		+ cliId;
		}
		if (cliPhone != null) {
			sql += String.format(SQL_REQ_WHERE_CLI_PHONE, cliPhone); 	
		}
		if (code != null) {
        	sql += String.format(SQL_REQ_WHERE_ORD_CODE, code,code);
        }
		
		String 					restriction 			= reqRestriction(searchKey);
		if(restriction != null) {
			sql += " AND " + restriction;
		}

		int count = ViSorOrder.DAO.reqCount(sql).intValue();		
		return count;
	}
	
	
	//--------------------------------------------------------------FILTER ORDER-------------------------------------------------------------------------------------------------------------------
	
	public static int reqCountFilterOrder(TaAutUser user, Integer type01, Integer type02, Set<String> searchKey, Integer idMan, Integer pId, String codeO, String dtB, String dtE) throws Exception {
		String dtBegin 	= "2000-01-01 00:01:00";
		String dtEnd 	= "3000-01-01 00:01:00";
		String sql = SQL_REQ_LIST_DYN_COUNT;
		
//		if (type02 == TaSorOrder.TYPE_02_ORDER_IN) {
//			sql += SQL_REQ_LIST_DYN_FROM_SUPL + SQL_REQ_WHERE;
//		}else if (type02 == TaSorOrder.TYPE_02_ORDER_OUT) {
//			sql += SQL_REQ_LIST_DYN_FROM_CLI + SQL_REQ_WHERE;
//		}
		
		if(idMan != null)
			sql += SQL_REQ_WHERE_PER_MAN 	+ idMan;
		
		
		if(type01 != null)
			sql += SQL_REQ_WHERE_TYPE_01 	+ type01;
		if(type02 != null)
			sql += SQL_REQ_WHERE_TYPE_02;
		
		String 					restriction 			= reqRestriction(searchKey);
		if(restriction != null) {
			sql += " AND " + restriction;
		}
		
//		if (!user.canBeAdmin() && !user.canBeSuperAdmin()) {
//			sql += String.format(SQL_REQ_WHERE_AUT_USER_01, user.reqId());
//		}
//		
//		if(pId != null && type02 != null){
//			if(type02 == TaSorOrder.TYPE_02_ORDER_IN){
//				sql += SQL_WHERE_SEARCH_SUPPLIER + pId;
//			} else if(type02 == TaSorOrder.TYPE_02_ORDER_OUT){
//				sql += SQL_WHERE_SEARCH_CLIENT + pId;
//			}
//		}
		
		if(codeO != null && codeO.length()>0){
//			sql += " AND " + SQL_WHERE_SEARCH_CODE + codeO + "%')" + " OR " + SQL_WHERE_SEARCH_NB_ORD + codeO + "%'))";
		}
		
		if (dtB	!=null) {
			dtBegin = dtB;
		}
		if (dtE!=null) {
			dtEnd 	= dtE;
		}
		
		sql += String.format(SQL_WHERE_SEARCH_DATE, dtBegin, dtEnd);

		int count = ViSorOrder.DAO.reqCount(sql).intValue();		
		return count;
	}
	
	public static int reqCountFilterOrder(TaAutUser user, Integer type01, Integer type02, Integer idMan, Integer pId, String codeO, String dtB, String dtE) throws Exception {
		String dtBegin 	= "2000-01-01 00:01:00";
		String dtEnd 	= "3000-01-01 00:01:00";
		
		String sql = SQL_REQ_LIST_DYN_COUNT;
		
//		if (type02 == TaSorOrder.TYPE_02_ORDER_IN) {
//			sql += SQL_REQ_LIST_DYN_FROM_SUPL + SQL_REQ_WHERE;
//		}else if (type02 == TaSorOrder.TYPE_02_ORDER_OUT) {
//			sql += SQL_REQ_LIST_DYN_FROM_CLI + SQL_REQ_WHERE;
//		}

		if(idMan != null)
			sql += SQL_REQ_WHERE_PER_MAN 	+ idMan;
		if(type01 != null)
			sql += SQL_REQ_WHERE_TYPE_01 	+ type01;
		if(type02 != null)
			sql += SQL_REQ_WHERE_TYPE_02;
		
//		if(pId != null && type02 != null){
//			if(type02 == TaSorOrder.TYPE_02_ORDER_IN){
//				sql += SQL_WHERE_SEARCH_SUPPLIER + pId;
//			} else if(type02 == TaSorOrder.TYPE_02_ORDER_OUT){
//				sql += SQL_WHERE_SEARCH_CLIENT + pId;
//			}
//		}
		
		if(codeO != null && codeO.length()>0){
//			sql += " AND " + SQL_WHERE_SEARCH_CODE + codeO + "%')" + " OR " + SQL_WHERE_SEARCH_NB_ORD + codeO + "%'))";
		}
		
//		if (!user.canBeAdmin() && !user.canBeSuperAdmin()) {
//			sql += String.format(SQL_REQ_WHERE_AUT_USER_01, user.reqId());
//		}
		
		if (dtB	!=null) {
			dtBegin = dtB;
		}
		if (dtE!=null) {
			dtEnd 	= dtE;
		}
		
		sql += String.format(SQL_WHERE_SEARCH_DATE, dtBegin, dtEnd);
		
		int count = ViSorOrder.DAO.reqCount(sql).intValue();		
		return count;
	}

}
