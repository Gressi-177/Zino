package com.hnv.db.job;

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
* TaJobDayoffResume by H&V SAS
*/
@Entity
@Table(name = DefDBExt.TA_JOB_DAYOFF_RESUME )
public class TaJobDayOffResume extends EntityAbstract<TaJobDayOffResume> {

	private static final long serialVersionUID = 1L;

	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                              =	"I_ID";
	public static final String	COL_I_AUT_USER                        =	"I_Aut_User";
	
	public static final String	COL_I_VAL_01                      	  =	"I_Val_01"; //"I_Reset_Unit";
	public static final String	COL_I_VAL_02                       	  =	"I_Val_02"; //"I_Reset_Val";
	
	
	public static final String	COL_T_INFO_01                         =	"T_Info_01";//COL_T_CONFIG_01
	public static final String	COL_T_INFO_02                         =	"T_Info_02";//COL_T_CONFIG_02
	public static final String	COL_T_INFO_03                         =	"T_Info_03";//COL_T_COMMENT
	
	public static final String	COL_F_VAL_01                          =	"F_Val_01"; //COL_F_TOTAL
	public static final String	COL_F_VAL_02                          =	"F_Val_02"; //COL_F_AVAILABLE
	public static final String	COL_F_VAL_03                          =	"F_Val_03"; //F_Realized
	
	public static final String	COL_D_DATE_01                         =	"D_Date_01";//D_Date_New
	public static final String	COL_D_DATE_02                         =	"D_Date_02";//D_Date_Validation
	public static final String	COL_D_DATE_03                         =	"D_Date_03";//D_Date_Reset
	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              =	"I_ID";
	public static final String	ATT_I_AUT_USER                        =	"I_Aut_User";
	
	public static final String	ATT_I_VAL_01                      	  =	"I_Val_01"; //"I_Reset_Unit";
	public static final String	ATT_I_VAL_02                       	  =	"I_Val_02"; //"I_Reset_Val";
	
	
	public static final String	ATT_T_INFO_01                         =	"T_Info_01";//ATT_T_COMMENT
	public static final String	ATT_T_INFO_02                         =	"T_Info_02";//ATT_T_CONFIG_01
	public static final String	ATT_T_INFO_03                         =	"T_Info_03";//ATT_T_CONFIG_02
	
	public static final String	ATT_F_VAL_01                          =	"F_Val_01"; //ATT_F_TOTAL
	public static final String	ATT_F_VAL_02                          =	"F_Val_02"; //ATT_F_AVAILABLE
	public static final String	ATT_F_VAL_03                          =	"F_Val_03"; //ATT_F_AVAILABLE
	
	public static final String	ATT_D_DATE_01                         =	"D_Date_01";//D_Date_New
	public static final String	ATT_D_DATE_02                         =	"D_Date_02";//D_Date_Validation
	public static final String	ATT_D_DATE_03                         =	"D_Date_03";//D_Date_Reset

	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static	final boolean				API_CACHE 	= false;
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<TaJobDayOffResume> 	DAO;
	static{
		DAO = new EntityDAO<TaJobDayOffResume>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), TaJobDayOffResume.class,RIGHTS);
	}

	//-----------------------Class Attributs-------------------------
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;
         
	@Column(name=COL_I_AUT_USER, nullable = true)
	private	Integer         I_Aut_User;
   
  
	@Column(name=COL_T_INFO_01, nullable = true)
	private	String          T_Info_01;

	@Column(name=COL_T_INFO_02, nullable = true)
	private	String          T_Info_02;
	
	@Column(name=COL_T_INFO_03, nullable = true)
	private	String          T_Info_03;
	
	
	@Column(name=COL_F_VAL_01, nullable = true)
	private	Double          F_Val_01;
     
	@Column(name=COL_F_VAL_02, nullable = true)
	private	Double          F_Val_02;
	
	@Column(name=COL_F_VAL_03, nullable = true)
	private	Double          F_Val_03;
	
	
	@Column(name=COL_D_DATE_01, nullable = true)
	private	Date            D_Date_01;
    
	@Column(name=COL_D_DATE_02, nullable = true)
	private	Date            D_Date_02;
	
	@Column(name=COL_D_DATE_03, nullable = true)
	private	Date            D_Date_03;
	
	@Column(name=COL_I_VAL_01, nullable = true)
	private	Integer         I_Reset_Unit;
 
	@Column(name=COL_I_VAL_02, nullable = true)
	private	Integer         I_Reset_Val;
    
	//-----------------------Transient Variables-------------------------


	//---------------------Constructeurs-----------------------
	public TaJobDayOffResume(){}

	public TaJobDayOffResume(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		//doInitDBFlag();
	}
	
	public TaJobDayOffResume(Integer uId01, String wd, String wh) throws Exception{
		this.I_Aut_User 	= uId01;
		this.F_Val_01		= Double.parseDouble("0");
		this.F_Val_02 		= Double.parseDouble("0");
		this.F_Val_03  		= Double.parseDouble("0");
		if(wd != null && wd.length() > 0){
			this.T_Info_01 =	wd;
		}
		if(wh != null && wh.length() > 0){
			this.T_Info_02 =	wh;
		}
	}
	
	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;
	}

	@Override
	public void doMergeWith(TaJobDayOffResume ent) {
		if (ent == this) return;
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;
		
		ok = (I_ID == ((TaJobDayOffResume)o).I_ID);
		if (!ok) return ok;

				
		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}

	
}
