package com.hnv.db.fin;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hnv.api.main.Hnv_CfgHibernate;
import com.hnv.db.EntityAbstract;
import com.hnv.db.EntityDAO;
import com.hnv.def.DefDBExt;	


/**
 * TaMatMaterial by H&V SAS
 */
@Entity
@Table(name = DefDBExt.TA_FIN_FINANCE )
public class TaFinFinance extends EntityAbstract<TaFinFinance> {

	public static final int	TYPE_01_IN 			= 101;
	public static final int	TYPE_01_OUT 		= 201;
	
	public static final int	STAT_NEW 			= 0; 
	public static final int	STAT_ACTIVE 		= 1;  
	public static final int	STAT_REVIEW 		= 5; 
	public static final int	STAT_DELETED 		= 10;
	
	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                              =	"I_ID";
	public static final String	COL_I_STATUS                          =	"I_Status";
	public static final String	COL_I_TYPE_01                         =	"I_Type_01"; //101 for in, 201 for out
	public static final String	COL_I_TYPE_02                         =	"I_Type_02";
	
	public static final String	COL_T_INFO_01                         =	"T_Info_01"; //histo: {dt, typ, val, ordId, ordRef}
	public static final String	COL_T_INFO_02                         =	"T_Info_02";
	public static final String	COL_T_INFO_03                         =	"T_Info_03";
	public static final String	COL_T_INFO_04                         =	"T_Info_04";
	public static final String	COL_T_INFO_05                         =	"T_Info_05";
	
	public static final String	COL_F_VAL_01                          =	"F_Val_01";  //final sum
	public static final String	COL_F_VAL_02                          =	"F_Val_02";	 //last sum befor last sum
	public static final String	COL_F_VAL_03                          =	"F_Val_03";	 //last val i/o 
	public static final String	COL_F_VAL_04                          =	"F_Val_04";
	public static final String	COL_F_VAL_05                          =	"F_Val_05";
	
	public static final String  COL_D_DATE_01 						  = "D_Date_01"; 
	public static final String  COL_D_DATE_02 						  = "D_Date_02";//last update
	public static final String  COL_D_DATE_03 						  = "D_Date_03";//lat  dt in 
	public static final String	COL_D_DATE_04                         =	"D_Date_04";//last dt out
	
	public static final String 	COL_I_AUT_USER_01 					  = "I_Aut_User_01";
	public static final String 	COL_I_AUT_USER_02 					  = "I_Aut_User_02";
	public static final String	COL_I_AUT_USER_03                     =	"I_Aut_User_03";
	
	public static final String 	COL_I_PER_MANAGER 					  = "I_Per_Manager";
	public static final String	COL_I_PER_PERSON_01                   =	"I_Per_Person_01";
	public static final String	COL_I_PER_PERSON_02                   =	"I_Per_Person_02";

	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              =	"I_ID";
	public static final String	ATT_I_STATUS                          =	"I_Status";
	public static final String	ATT_I_TYPE_01                         =	"I_Type_01";
	public static final String	ATT_I_TYPE_02                         =	"I_Type_02";
	public static final String	ATT_D_DATE_01                         =	"D_Date_01";
	public static final String	ATT_D_DATE_02                         =	"D_Date_02";
	public static final String	ATT_D_DATE_03                         =	"D_Date_03";
	public static final String	ATT_D_DATE_04                         =	"D_Date_04";
	public static final String	ATT_F_VAL_01                          =	"F_Val_01";
	public static final String	ATT_F_VAL_02                          =	"F_Val_02";
	public static final String	ATT_F_VAL_03                          =	"F_Val_03";
	public static final String	ATT_F_VAL_04                          =	"F_Val_04";
	public static final String	ATT_F_VAL_05                          =	"F_Val_05";
	public static final String	ATT_T_INFO_01                         =	"T_Info_01";
	public static final String	ATT_T_INFO_02                         =	"T_Info_02";
	public static final String	ATT_T_INFO_03                         =	"T_Info_03";
	public static final String	ATT_T_INFO_04                         =	"T_Info_04";
	public static final String	ATT_T_INFO_05                         =	"T_Info_05";
	public static final String	ATT_I_AUT_USER_01                     =	"I_Aut_User_01";
	public static final String	ATT_I_AUT_USER_02                     =	"I_Aut_User_02";
	public static final String	ATT_I_AUT_USER_03                     =	"I_Aut_User_03";
	public static final String	ATT_I_PER_PERSON_01                   =	"I_Per_Person_01";
	public static final String	ATT_I_PER_PERSON_02                   =	"I_Per_Person_02";
	public static final String	ATT_I_PER_MANAGER                     =	"I_Per_Manager";

	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<TaFinFinance> 	DAO;
	static{
		DAO = new EntityDAO<TaFinFinance>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), TaFinFinance.class,RIGHTS, HISTORY, DefDBExt.TA_FIN_FINANCE, DefDBExt.ID_TA_FIN_FINANCE);

	}
	//-----------------------Class Attributs-------------------------
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;
         
	@Column(name=COL_I_STATUS, nullable = true)
	private	Integer         I_Status;
     
	@Column(name=COL_I_TYPE_01, nullable = true)
	private	Integer         I_Type_01;
    
	@Column(name=COL_I_TYPE_02, nullable = true)
	private	Integer         I_Type_02;
    
	@Column(name=COL_D_DATE_01, nullable = true)
	private	Date            D_Date_01;
    
	@Column(name=COL_D_DATE_02, nullable = true)
	private	Date            D_Date_02;
    
	@Column(name=COL_D_DATE_03, nullable = true)
	private	Date            D_Date_03;
    
	@Column(name=COL_D_DATE_04, nullable = true)
	private	Date            D_Date_04;
    
	@Column(name=COL_F_VAL_01, nullable = true)
	private	Double          F_Val_01;
     
	@Column(name=COL_F_VAL_02, nullable = true)
	private	Double          F_Val_02;
     
	@Column(name=COL_F_VAL_03, nullable = true)
	private	Double          F_Val_03;
     
	@Column(name=COL_F_VAL_04, nullable = true)
	private	Double          F_Val_04;
     
	@Column(name=COL_F_VAL_05, nullable = true)
	private	Double          F_Val_05;
     
	@Column(name=COL_T_INFO_01, nullable = true)
	private	String          T_Info_01;
    
	@Column(name=COL_T_INFO_02, nullable = true)
	private	String          T_Info_02;
    
	@Column(name=COL_T_INFO_03, nullable = true)
	private	String          T_Info_03;
    
	@Column(name=COL_T_INFO_04, nullable = true)
	private	String          T_Info_04;
    
	@Column(name=COL_T_INFO_05, nullable = true)
	private	String          T_Info_05;
    
	@Column(name=COL_I_AUT_USER_01, nullable = true)
	private	Integer         I_Aut_User_01;

	@Column(name=COL_I_AUT_USER_02, nullable = true)
	private	Integer         I_Aut_User_02;

	@Column(name=COL_I_AUT_USER_03, nullable = true)
	private	Integer         I_Aut_User_03;

	@Column(name=COL_I_PER_PERSON_01, nullable = false)
	private	Integer         I_Per_Person_01;

	@Column(name=COL_I_PER_PERSON_02, nullable = false)
	private	Integer         I_Per_Person_02;

	@Column(name=COL_I_PER_MANAGER, nullable = true)
	private	Integer         I_Per_Manager;

	//-----------------------Transient Variables-------------------------
	
	
	//---------------------Constructeurs-----------------------
	public TaFinFinance(){}
	
	public TaFinFinance(int manId, int perId){
		this.I_Per_Manager		= manId;
		this.I_Per_Person_01 	= perId;
		this.D_Date_01 			= new Date();
		this.F_Val_01 			= 0.0;
		this.T_Info_01 			= "[]";
		this.I_Status			= STAT_ACTIVE;
	}

	public TaFinFinance(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		//doInitDBFlag();
	}


	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;

		ok = (I_ID == ((TaFinFinance)o).I_ID);
		if (!ok) return ok;


		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}


	public Integer reqID(){
		return this.I_ID;
	}


	@Override
	public void doMergeWith(TaFinFinance arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
