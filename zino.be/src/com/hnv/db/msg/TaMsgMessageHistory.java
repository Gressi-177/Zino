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
* TaMsgMessageHistory by H&V SAS
*/
@Entity
@Table(name = DefDBExt.TA_MSG_MESSAGE_HISTORY )
public class TaMsgMessageHistory extends EntityAbstract<TaMsgMessageHistory> {

	private static final long serialVersionUID = 1L;

	public static final int		STAT_READ							  = 1;
	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                              =	"I_ID";
	public static final String	COL_I_MSG_MESSAGE                     =	"I_Msg_Message";
	public static final String	COL_I_NSO_GROUP                       =	"I_Nso_Group";
	public static final String	COL_I_STATUS                          =	"I_Status";
	public static final String	COL_I_AUT_USER                        =	"I_Aut_User";
	public static final String	COL_D_DATE                            =	"D_Date";


	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              =	"I_ID";
	public static final String	ATT_I_MSG_MESSAGE                     =	"I_Msg_Message";
	public static final String	ATT_I_NSO_GROUP                       =	"I_Nso_Group";
	public static final String	ATT_I_STATUS                          =	"I_Status";
	public static final String	ATT_I_AUT_USER                        =	"I_Aut_User";
	public static final String	ATT_D_DATE                            =	"D_Date";


	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 

	public		static 	final EntityDAO<TaMsgMessageHistory> 	DAO;
	static{
		DAO = new EntityDAO<TaMsgMessageHistory>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), TaMsgMessageHistory.class,RIGHTS); 
	}

	//-----------------------Class Attributs-------------------------
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;
         
	@Column(name=COL_I_MSG_MESSAGE, nullable = false)
	private	Integer         I_Msg_Message;
	
	@Column(name=COL_I_NSO_GROUP, nullable = true)
	private	Integer         I_Nso_Group;
    
	@Column(name=COL_I_STATUS, nullable = false)
	private	Integer         I_Status;
     
	@Column(name=COL_I_AUT_USER, nullable = false)
	private	Integer         I_Aut_User;
   
	@Column(name=COL_D_DATE, nullable = true)
	private	Date            D_Date;
	//-----------------------Transient Variables-------------------------


	//---------------------Constructeurs-----------------------
	private TaMsgMessageHistory(){}

	public TaMsgMessageHistory(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		doInitDBFlag();
	}
	
	public TaMsgMessageHistory(Integer I_Aut_User, Integer I_Group, Integer I_Message,  Integer I_Status) throws Exception {
		this.reqSetAttr(
				ATT_I_AUT_USER   	, I_Aut_User,
				ATT_I_NSO_GROUP   	, I_Group,
				ATT_I_MSG_MESSAGE   , I_Message,
				ATT_D_DATE  		, new Date(),
				ATT_I_STATUS     	, I_Status
				
				);
		doInitDBFlag();
	}
	
	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}

	@Override
	public void doMergeWith(TaMsgMessageHistory ent) {
		if (ent == this) return;
		this.I_Msg_Message              = ent.I_Msg_Message;
		this.I_Status               = ent.I_Status;
		this.I_Aut_User             = ent.I_Aut_User;
		this.D_Date                 = ent.D_Date;



		//---------------------Merge Transient Variables if exist-----------------------
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;
		
		ok = (I_ID == ((TaMsgMessageHistory)o).I_ID);
		if (!ok) return ok;

				
		if (!ok) return ok;
		return ok;
	}




}
