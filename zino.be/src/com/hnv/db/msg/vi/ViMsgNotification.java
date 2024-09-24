package com.hnv.db.msg.vi;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hnv.api.main.Hnv_CfgHibernate;
import com.hnv.db.EntityAbstract;
import com.hnv.db.EntityDAO;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.def.DefDBExt;		

/**
* TaMsgMessage by H&V SAS
*/
@Entity
@Table(name = DefDBExt.TA_MSG_MESSAGE )
public class ViMsgNotification extends EntityAbstract<ViMsgNotification> {

	private static final long serialVersionUID 		= 1L;
	
	public static final int TYPE_02_OFFER			= 50;
	public static final int TYPE_02_COMMENT			= 50;

	public static final int STAT_NO_READ 			= 100;
	public static final int STAT_READED 			= 101;

	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                              	=	"I_ID";
	public static final String	COL_I_STATUS                          	=	"I_Status";
	public static final String	COL_I_TYPE_01                        	=	"I_Type_01";
	
	public static final String	COL_T_INFO_04                           =	"T_Info_04";
	public static final String	COL_T_INFO_05                           =	"T_Info_05";
	
	public static final String	COL_I_AUT_USER                        	=	"I_Aut_User";
	public static final String	COL_D_DATE                            	=	"D_Date";

	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              	=	"I_ID";
	public static final String	ATT_I_STATUS                          	=	"I_Status";
	public static final String	ATT_I_TYPE_01                        	=	"I_Type_01";
	public static final String	ATT_T_INFO_04                           =	"T_Info_04";
	public static final String	ATT_T_INFO_05                           =	"T_Info_05";
	public static final String	ATT_I_AUT_USER                        	=	"I_Aut_User";
	public static final String	ATT_D_DATE                            	=	"D_Date";
	

	//-----------------------------------------------------------------------------------------------
	

	

	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static	final boolean				API_CACHE 	= false;
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<ViMsgNotification> 	DAO;
	static{
		DAO = new EntityDAO<ViMsgNotification>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), ViMsgNotification.class,RIGHTS);
	}

	//-----------------------Class Attributs-------------------------
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;
         
	@Column(name=COL_I_STATUS, nullable = false)
	private	Integer         I_Status;
     
	@Column(name=COL_I_TYPE_01, nullable = false)
	private	Integer         I_Type_01;
      
	@Column(name=COL_T_INFO_04, nullable = true)
	private	String          T_Info_04;
	
	@Column(name=COL_T_INFO_05, nullable = true)
	private	String          T_Info_05;
       
	@Column(name=COL_I_AUT_USER, nullable = true)
	private	Integer         I_Aut_User;
   
	@Column(name=COL_D_DATE, nullable = true)
	private	Date            D_Date;
    
	//-----------------------Transient Variables-------------------------
	
	//---------------------Constructeurs-----------------------
	private ViMsgNotification(){}

	public ViMsgNotification(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		doInitDBFlag();
	}
	
	public ViMsgNotification(Integer I_Status, Integer I_Type_Msg) throws Exception {
		this.reqSetAttr(
			ATT_I_STATUS     , I_Status,
			ATT_I_TYPE_01   , I_Type_Msg
		);
		doInitDBFlag();
	}
	
	
	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}

	@Override
	public void doMergeWith(ViMsgNotification ent) {
		if (ent == this) return;
		this.I_Status       = ent.I_Status;
		this.I_Type_01      = ent.I_Type_01;
		this.T_Info_04      = ent.T_Info_04;
		this.I_Aut_User     = ent.I_Aut_User;
		this.D_Date         = ent.D_Date;



		//---------------------Merge Transient Variables if exist-----------------------
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;
		
		ok = (I_ID == ((ViMsgNotification)o).I_ID);
		if (!ok) return ok;

				
		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}
	
}
