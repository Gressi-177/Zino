package com.hnv.db.nso.vi;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.hnv.api.main.Hnv_CfgHibernate;
import com.hnv.db.EntityAbstract;
import com.hnv.db.EntityDAO;
import com.hnv.db.msg.TaMsgMessage;
import com.hnv.db.msg.TaMsgMessageHistory;
import com.hnv.db.nso.TaNsoGroup;
import com.hnv.db.nso.TaNsoGroupMember;
import com.hnv.def.DefDBExt;		

/**
* TaNsoGroup by H&V SAS
*/
@Entity
public class ViNsoGroupUnread extends EntityAbstract<ViNsoGroupUnread> {

	private static final long serialVersionUID 		= 1L;
	
	public static final int STAT_PUBLISH			= 1;
	public static final int STAT_PRIVATE			= 2;
	public static final int STAT_DEACTIVE			= 3;
	
	public static final int TYP_02_PRIVATE			= 401;
	public static final int TYP_02_PUBLIC			= 402;

	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                              =	"I_ID";
	public static final String	COL_T_NAME                            =	"T_Name";
	
	public static final String	COL_I_TYPE_01                         =	"I_Type_01";
	public static final String	COL_I_TYPE_02                         =	"I_Type_02";
	public static final String	COL_T_VAL_01                          =	"T_Val_01";
	public static final String	COL_T_VAL_02                          =	"T_Val_02";

	public static final String	COL_T_MSGFROM                         =	"T_MsgFrom";
	public static final String	COL_T_MSGBODY                        =	"T_MsgBody";
	public static final String	COL_I_MSGUSER                         =	"I_MsgUser";
	public static final String	COL_D_MSGDATE                         =	"D_MsgDate";
	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              =	"I_ID";
	public static final String	ATT_T_NAME                            =	"T_Name";
	
	public static final String	ATT_I_TYPE_01                         =	"I_Type_01";
	public static final String	ATT_I_TYPE_02                         =	"I_Type_02";
	
	public static final String	ATT_T_VAL_01                        =	"T_Val_01";
	public static final String	ATT_T_VAL_02                        =	"T_Val_02";
	
	public static final String	ATT_T_MSGFROM                         =	"T_MsgFrom";
	public static final String	ATT_T_MSGFBODY                        =	"T_MsgBody";
	public static final String	ATT_I_MSGUSER                         =	"I_MsgUser";
	public static final String	ATT_D_MSGDATE                         =	"D_MsgDate";

	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, false, false, false, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static	final boolean				API_CACHE 	= false;
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<ViNsoGroupUnread> 	DAO;
	static{
		DAO = new EntityDAO<ViNsoGroupUnread>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), ViNsoGroupUnread.class,RIGHTS);
	}

	//-----------------------Class Attributs-------------------------
	@Id
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;
	
	@Column(name=COL_T_NAME, nullable = true)
	private	String          T_Name;
     
	@Column(name=COL_I_TYPE_01, nullable = true)
	private	Integer         I_Type_01;
    
	@Column(name=COL_I_TYPE_02, nullable = true)
	private	Integer         I_Type_02;
    	
	@Column(name=COL_T_VAL_01, nullable = true)
	private	String         T_Val_01;
	
	@Column(name=COL_T_VAL_02, nullable = true)
	private	String         T_Val_02;


	@Column(name=COL_T_MSGFROM, nullable = true)
	private	String          T_MsgFrom;
       
	@Column(name=COL_T_MSGBODY, nullable = true)
	private	String          T_MsgBody;
       
	@Column(name=COL_I_MSGUSER, nullable = true)
	private	Integer         I_MsgUser;
   
	@Column(name=COL_D_MSGDATE, nullable = true)
	private	Date            D_MsgDate;	
	
	//-----------------------Transient Variables-------------------------
	//---------------------Constructeurs-----------------------
	private ViNsoGroupUnread(){}

	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}

	@Override
	public void doMergeWith(ViNsoGroupUnread ent) {
		if (ent == this) return;
		this.T_Name                 = ent.T_Name;
		this.I_Type_01              = ent.I_Type_01;
		this.I_Type_02              = ent.I_Type_02;
		this.T_Val_01          		= ent.T_Val_01;
		this.T_Val_02          		= ent.T_Val_02;


		//---------------------Merge Transient Variables if exist-----------------------
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;
		
		ok = (I_ID == ((ViNsoGroupUnread)o).I_ID);
		if (!ok) return ok;

				
		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}
	
	static String SQL = "select "
			+"gr." + TaNsoGroup.ATT_I_ID 			+ " " + COL_I_ID 			+ ", "
			+"gr." + TaNsoGroup.ATT_T_NAME 			+ " " + COL_T_NAME 			+ ", "
			+"gr." + TaNsoGroup.COL_I_TYPE_01 		+ " " + COL_I_TYPE_01 		+ ", "
			+"gr." + TaNsoGroup.COL_I_TYPE_02 		+ " " + COL_I_TYPE_02 		+ ", "
			+"gr." + TaNsoGroup.COL_T_VAL_01 		+ " " + COL_T_VAL_01 		+ ", "
			+"gr." + TaNsoGroup.COL_T_VAL_02 		+ " " + COL_T_VAL_02 		+ ", "
			
			+"msg." + TaMsgMessage.COL_I_AUT_USER 	+ " " + COL_I_MSGUSER 		+ ", "
			+"msg." + TaMsgMessage.COL_T_INFO_01 	+ " " + COL_T_MSGFROM 		+ ", "
			+"msg." + TaMsgMessage.COL_T_INFO_04 	+ " " + COL_T_MSGBODY 		+ ", "
			+"msg." + TaMsgMessage.COL_D_DATE_01 	+ " " + COL_D_MSGDATE 		+ " from " +
			"(" + 
				"select max(h.D_Date) " + TaNsoGroupMember.COL_D_DATE_01 + ", h." + TaMsgMessageHistory.COL_I_NSO_GROUP  
				+ " from "  		+ DefDBExt.TA_NSO_GROUP_MEMBER 			+ " m " 
				+ " inner join " 	+ DefDBExt.TA_MSG_MESSAGE_HISTORY 		+ " h " 
				+ " on  m." + TaNsoGroupMember.COL_I_AUT_USER 	+ " = %d " 
				+ " and m." + TaNsoGroupMember.COL_I_STATUS 	+ " = " 	+ TaNsoGroupMember.STAT_ACTIVE 
				+ " and m." + TaNsoGroupMember.COL_I_AUT_USER 	+ " = h." 	+ TaMsgMessageHistory.COL_I_AUT_USER 
				+ " and m." + TaNsoGroupMember.COL_I_NSO_GROUP	+ " = h." 	+ TaMsgMessageHistory.COL_I_NSO_GROUP 
				+ " group by h.I_Nso_Group " + 
			") g "  
			
			+ " inner join " + DefDBExt.TA_MSG_MESSAGE 		+ " msg " 
			+ " on  msg." 	 + TaMsgMessage.COL_I_ENTITY_ID + " = g."  + TaMsgMessageHistory.COL_I_NSO_GROUP 
			+ " and msg." 	 + TaMsgMessage.COL_D_DATE_01   + " > g."  + TaNsoGroupMember.COL_D_DATE_01 
			+ " and msg." 	 + TaMsgMessage.COL_I_AUT_USER  + " <> %d"  
			
			+ " inner join " + DefDBExt.TA_NSO_GROUP 		+ "  gr  " 
			+ " on  gr."	 + TaNsoGroup.COL_I_ID 			+ " = g."  + TaMsgMessageHistory.COL_I_NSO_GROUP 
			
			+ " order by  msg.I_ID desc";
	
	public static List<ViNsoGroupUnread> reqList(int uId) throws Exception{
		String s = String.format(SQL, uId, uId);
		List<ViNsoGroupUnread> lst = ViNsoGroupUnread.DAO.reqList(s);
		return lst;
	}
	
}
