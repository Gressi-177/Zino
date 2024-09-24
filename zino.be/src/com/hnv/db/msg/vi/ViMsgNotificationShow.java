package com.hnv.db.msg.vi;

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
public class ViMsgNotificationShow extends EntityAbstract<ViMsgNotificationShow> {

	private static final long serialVersionUID 		= 1L;

	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                              	=	"I_ID";
	public static final String	COL_I_STATUS                          	=	"I_Status";
	public static final String	COL_I_TYPE                        		=	"I_Type";
	
	public static final String	COL_T_MESSAGE                           =	"T_Message";//from
	
	public static final String	COL_D_DATE                            	=	"D_Date";

	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              	=	"I_ID";
	public static final String	ATT_I_STATUS                          	=	"I_Status";
	public static final String	ATT_I_TYPE                        		=	"I_Type";
	public static final String	ATT_T_MESSAGE                           =	"T_Message";//from
	public static final String	ATT_D_DATE                            	=	"D_Date";
	

	//-----------------------------------------------------------------------------------------------
	

	

	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static	final boolean				API_CACHE 	= false;
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<ViMsgNotificationShow> 	DAO;
	static{
		DAO = new EntityDAO<ViMsgNotificationShow>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), ViMsgNotificationShow.class,RIGHTS);
	}

	//-----------------------Class Attributs-------------------------
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;
         
	@Column(name=COL_I_STATUS, nullable = false)
	private	Integer         I_Status;
     
	@Column(name=COL_I_TYPE, nullable = false)
	private	Integer         I_Type;
    
	@Column(name=COL_T_MESSAGE, nullable = false)
	private	Integer         T_Message;
   
	@Column(name=COL_D_DATE, nullable = true)
	private	Date            D_Date;
    
	//-----------------------Transient Variables-------------------------
	
	//---------------------Constructeurs-----------------------
	private ViMsgNotificationShow(){}

	public ViMsgNotificationShow(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		doInitDBFlag();
	}
	
	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}

	@Override
	public void doMergeWith(ViMsgNotificationShow ent) {
		if (ent == this) return;
		this.I_Status       = ent.I_Status;
		this.I_Type      	= ent.I_Type;
		this.T_Message      = ent.T_Message;
		this.D_Date         = ent.D_Date;



		//---------------------Merge Transient Variables if exist-----------------------
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;
		
		ok = (I_ID == ((ViMsgNotificationShow)o).I_ID);
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
				"I_ID:"+                      I_ID +"," + 
				"I_Status:"+                  I_Status +"," + 
				"I_Type_Msg:"+                I_Type +"," + 
				"T_From:"+                    T_Message +"," + 
				"D_Date:"+                    D_Date +"," + 
				 "}";

	}
	
}
