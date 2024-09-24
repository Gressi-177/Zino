package com.hnv.db.msg;

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
* TaMsgMessage by H&V SAS
*/
@Entity
@Table(name = DefDBExt.TA_MSG_MESSAGE_STORE )
public class TaMsgMessageStore extends EntityAbstract<TaMsgMessageStore> {

	private static final long serialVersionUID = 1L;
	public static final int		STAT_NEW				= 0;
	public static final int		STAT_COMPRESS_LEV_01	= 1;
	public static final int		STAT_COMPRESS_LEV_02	= 2; //---zip
	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                             	=	"I_ID";
	public static final String	COL_I_STATUS                         	=	"I_Status";
	public static final String	COL_I_TYPE_01                        	=	"I_Type_01";
	public static final String	COL_I_TYPE_02                       	=	"I_Type_02";
	public static final String	COL_T_FROM                            	=	"T_From";
	public static final String	COL_T_TO                              	=	"T_To";
	public static final String	COL_T_CONTENT                           =	"T_Content";
	public static final String	COL_D_DATE_01                           =	"D_Date_01";
	public static final String	COL_D_DATE_02                           =	"D_Date_02";

	public static final String	COL_I_ENTITY_TYPE                     	=	"I_Entity_Type";
	public static final String	COL_I_ENTITY_ID_01                      =	"I_Entity_ID_01";
	public static final String	COL_I_ENTITY_ID_02                      =	"I_Entity_ID_02";

	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              	=	"I_ID";
	public static final String	ATT_I_STATUS                          	=	"I_Status";
	public static final String	ATT_I_TYPE_01                       	=	"I_Type_01";
	public static final String	ATT_I_TYPE_02                       	=	"I_Type_02";
	public static final String	ATT_T_FROM                            	=	"T_From";
	public static final String	ATT_T_TO                              	=	"T_To";
	public static final String	ATT_T_CONTENT                           =	"T_Content";
	public static final String	ATT_D_DATE_01                        	=	"D_Date_01";
	public static final String	ATT_D_DATE_02                           =	"D_Date_02";
	public static final String	ATT_I_ENTITY_TYPE                     	=	"I_Entity_Type";
	public static final String	ATT_I_ENTITY_ID_01                      =	"I_Entity_ID_01";
	public static final String	ATT_I_ENTITY_ID_02                      =	"I_Entity_ID_02";

	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static	final boolean				API_CACHE 	= false;
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<TaMsgMessageStore> 	DAO;
	static{
		DAO = new EntityDAO<TaMsgMessageStore>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), TaMsgMessageStore.class,RIGHTS); 
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
  
	@Column(name=COL_T_FROM, nullable = true)
	private	String          T_From;
       
	@Column(name=COL_T_TO, nullable = true)
	private	String          T_To;
         
	@Column(name=COL_T_CONTENT, nullable = true)
	private	String          T_Content;
      
	@Column(name=COL_D_DATE_01, nullable = true)
	private	Date          D_Date_01;
   
	@Column(name=COL_D_DATE_02, nullable = true)
	private	Date            D_Date_02;
       
	@Column(name=COL_I_ENTITY_TYPE, nullable = true)
	private	Integer         I_Entity_Type;

	@Column(name=COL_I_ENTITY_ID_01, nullable = true)
	private	Integer         I_Entity_ID_01;
    
	@Column(name=COL_I_ENTITY_ID_02, nullable = true)
	private	Integer         I_Entity_ID_02;
	//-----------------------Transient Variables-------------------------
	//---------------------Constructeurs-----------------------
	public TaMsgMessageStore(){}

	public TaMsgMessageStore(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		doInitDBFlag();
	}
	
	public TaMsgMessageStore(Integer I_Status, Integer I_Type_01) throws Exception {
		this.reqSetAttr(
			ATT_I_STATUS     , I_Status,
			ATT_I_TYPE_01   , I_Type_01
		);
		doInitDBFlag();
	}
	
	public TaMsgMessageStore(Integer I_Entity_Type, Integer I_Entity_ID_01, Integer I_Entity_ID_02, Integer I_Status, Integer I_Type_01, Integer I_Type_02, String T_From, String T_To, String T_Content, String D_Date_01, Integer I_Aut_User, Date D_Date_02) throws Exception {
		this.reqSetAttr(
			ATT_I_STATUS               	, I_Status,
			ATT_I_TYPE_01             	, I_Type_01,
			ATT_I_TYPE_02            	, I_Type_02,
			ATT_T_FROM                 	, T_From,
			ATT_T_TO                   	, T_To,
			ATT_T_CONTENT               , T_Content,
			ATT_D_DATE_01               , D_Date_01,
			ATT_D_DATE_02               , D_Date_02,
			ATT_I_ENTITY_TYPE          	, I_Entity_Type,
			ATT_I_ENTITY_ID_01          , I_Entity_ID_01,
			ATT_I_ENTITY_ID_02          , I_Entity_ID_02
		);
		doInitDBFlag();
	}
	
	
	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;
	}

	@Override
	public void doMergeWith(TaMsgMessageStore ent) {
		if (ent == this) return;
		this.I_Status               = ent.I_Status;
		this.I_Type_01             	= ent.I_Type_01;
		this.I_Type_02            	= ent.I_Type_02;
		this.T_From                 = ent.T_From;
		this.T_To                   = ent.T_To;
		this.T_Content              = ent.T_Content;
		this.D_Date_01              = ent.D_Date_01;
		this.D_Date_02              = ent.D_Date_02;
		this.I_Entity_Type          = ent.I_Entity_Type;
		this.I_Entity_ID_01         = ent.I_Entity_ID_01;
		this.I_Entity_ID_02         = ent.I_Entity_ID_02;
		//---------------------Merge Transient Variables if exist-----------------------
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;
		
		ok = (I_ID == ((TaMsgMessageStore)o).I_ID);
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
		return 	"TaMsgMessage { " +
				"I_ID:"+                      	I_ID 			+"," + 
				"I_Status:"+                  	I_Status 		+"," + 
				"I_Type_01:"+                	I_Type_01 		+"," + 
				"I_Type_02:"+               	I_Type_02 		+"," + 
				"T_From:"+                    	T_From 			+"," + 
				"T_To:"+                      	T_To 			+"," + 
				"T_Content:"+                   T_Content 		+"," + 
				"D_Date_01:"+                   D_Date_01 		+"," + 
				"D_Date_02:"+                   D_Date_02 		+"," + 
				"I_Entity_Type:"+               I_Entity_Type 	+"," + 
				"I_Entity_ID_01:"+              I_Entity_ID_01 	+"," + 
				"I_Entity_ID_02:"+              I_Entity_ID_02 	+"," + 
				 "}";

	}
	
}
