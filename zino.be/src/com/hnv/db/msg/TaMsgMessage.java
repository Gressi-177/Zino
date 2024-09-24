package com.hnv.db.msg;

import java.io.Serializable;
import java.util.ArrayList;
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

import org.checkerframework.common.returnsreceiver.qual.This;

import com.hnv.api.main.Hnv_CfgHibernate;
import com.hnv.common.tool.ToolDate;
import com.hnv.common.tool.ToolJSON;
import com.hnv.data.json.JSONArray;
import com.hnv.data.json.JSONObject;
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
public class TaMsgMessage extends EntityAbstract<TaMsgMessage> {

	private static final long serialVersionUID 	= 1L;
	private static final int  ENT_TYP			= DefDBExt.ID_TA_MSG_MESSAGE;
	
	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                              	=	"I_ID";
	public static final String	COL_I_STATUS                          	=	"I_Status";
	public static final String	COL_I_TYPE_01                        	=	"I_Type_01";//msg + //noti
	public static final String	COL_I_TYPE_02                       	=	"I_Type_02";
	
	public static final String	COL_T_INFO_01                           =	"T_Info_01";//from
	public static final String	COL_T_INFO_02                           =	"T_Info_02";//to
	public static final String	COL_T_INFO_03                           =	"T_Info_03";//title
	public static final String	COL_T_INFO_04                           =	"T_Info_04";//body
	public static final String	COL_T_INFO_05                           =	"T_Info_05";
	
	public static final String	COL_I_AUT_USER                        	=	"I_Aut_User";
	public static final String	COL_D_DATE_01                           =	"D_Date_01";
	public static final String	COL_D_DATE_02                           =	"D_Date_02";
	public static final String	COL_I_ENTITY_TYPE                     	=	"I_Entity_Type"; //possible user
	public static final String	COL_I_ENTITY_ID                       	=	"I_Entity_ID"; //possible user Destination

	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              	=	"I_ID";
	public static final String	ATT_I_STATUS                          	=	"I_Status";
	public static final String	ATT_I_TYPE_01                        	=	"I_Type_01";
	public static final String	ATT_I_TYPE_02                       	=	"I_Type_02";
	public static final String	ATT_T_INFO_01                           =	"T_Info_01";
	public static final String	ATT_T_INFO_02                           =	"T_Info_02";
	public static final String	ATT_T_INFO_03                           =	"T_Info_03";
	public static final String	ATT_T_INFO_04                           =	"T_Info_04";
	public static final String	ATT_T_INFO_05                           =	"T_Info_05";
	public static final String	ATT_I_AUT_USER                        	=	"I_Aut_User";
	public static final String	ATT_D_DATE_01                           =	"D_Date_01";
	public static final String	ATT_D_DATE_02                           =	"D_Date_02";
	public static final String	ATT_I_ENTITY_TYPE                     	=	"I_Entity_Type";
	public static final String	ATT_I_ENTITY_ID                       	=	"I_Entity_ID";

	public static final String	ATT_O_DOCUMENTS                       	=	"O_Documents";
	public static final String	ATT_O_USER                       	  	=	"O_User";
	public static final String	ATT_O_AVATAR                       	  	=	"O_Avatar";
	
	//---------------------------------------------------------------------------------
	
	public static final int TYPE_01_EMAIL				= 1;
	public static final int TYPE_01_CLAIM				= 2;
	public static final int TYPE_01_CONTACT				= 5;
	public static final int TYPE_01_CONTACT_CHAT		= 10;
	public static final int TYPE_01_CONTACT_CHAT_RESP	= 11;
	public static final int TYPE_01_SMS					= 100;
	
	public static final int TYPE_01_MESSAGE 			= 1000; 
	public static final int TYPE_01_NOTIFICATION 		= 1001;
	
	public static final int TYPE_01_CHAT_PRIVATE		= 200;
	public static final int TYPE_01_CHAT_PUBLIC			= 201;	
	
//	public static final int TYPE_01_CHAT_FILE			= 210;	
//	public static final int TYPE_01_CHAT_ROOM_TEXT		= 1000;
//	public static final int TYPE_01_CHAT_ROOM_FILE		= 1001;
			
	public static final int STAT_MSG_WAITING			= 10;
	public static final int STAT_MSG_SENT				= 11;
	public static final int STAT_MSG_INBOX				= 12;
	public static final int STAT_MSG_INBOX_UNREAD		= 13;
	public static final int STAT_MSG_INBOX_READ			= 14;
	public static final int STAT_MSG_STARRED			= 15;
	public static final int STAT_MSG_DRAFT				= 16;
	public static final int STAT_MSG_TRASH				= 17;

	//-----------------------------------------------------------------------------------------------
	public static final int	STAT_NOTI_NEW 				= 100;
	public static final int	STAT_NOTI_READED 			= 101;
	public static final int	STAT_NOTI_DELETED			= 102;
	
	public static final int	STAT_REQ_NEW 				= 200;
	public static final int	STAT_REQ_VALIDATED 			= 201;
	public static final int	STAT_REQ_REFUSED 			= 202;
	
	public static final int STAT_EMAIL_WAITING			= 10;
	public static final int STAT_EMAIL_SENT				= 11;
	public static final int STAT_EMAIL_INBOX			= 12;
	public static final int STAT_EMAIL_INBOX_UNREAD		= 13;
	public static final int STAT_EMAIL_INBOX_READ		= 14;
	public static final int STAT_EMAIL_STARRED			= 15;
	public static final int STAT_EMAIL_DRAFT			= 16;
	public static final int STAT_EMAIL_TRASH			= 17;

	//-----------------------------------------------------------------------------------------------
	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	public		static 	final EntityDAO<TaMsgMessage> 	DAO;
	static{
		DAO = new EntityDAO<TaMsgMessage>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), TaMsgMessage.class,RIGHTS);
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
   
	@Column(name=COL_I_TYPE_02, nullable = true)
	private	Integer         I_Type_02;
  
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
       
	@Column(name=COL_I_AUT_USER, nullable = true)
	private	Integer         I_Aut_User;
   
	@Column(name=COL_D_DATE_01, nullable = true)
	private	Date            D_Date_01;
	@Column(name=COL_D_DATE_02, nullable = true)
	private	Date            D_Date_02;
       
	@Column(name=COL_I_ENTITY_TYPE, nullable = true)
	private	Integer         I_Entity_Type;

	@Column(name=COL_I_ENTITY_ID, nullable = true)
	private	Integer         I_Entity_ID;
    
	//-----------------------Transient Variables-------------------------
	@Transient
	private List<TaTpyDocument>			O_Documents;
	
	@Transient
	private TaAutUser					O_User;
	
	@Transient
	private Object						O_Avatar;
	//---------------------Constructeurs-----------------------
	public TaMsgMessage(){}

	public TaMsgMessage(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		doInitDBFlag();
	}
	
	public TaMsgMessage(Integer I_Status, Integer I_Type_Msg) throws Exception {
		this.reqSetAttr(
			ATT_I_STATUS     , I_Status,
			ATT_I_TYPE_01   , I_Type_Msg
		);
		doInitDBFlag();
	}
	public TaMsgMessage(Integer I_Status, Integer I_Type_Msg, Integer I_Type_Noti, String T_From, String T_To, String T_Title, String T_Body, Integer I_Aut_User, Date D_Date) throws Exception {
		this.reqSetAttr(
			ATT_I_STATUS               , I_Status,
			ATT_I_TYPE_01             , I_Type_Msg,
			ATT_I_TYPE_02            , I_Type_Noti,
			ATT_T_INFO_01                 , T_From,
			ATT_T_INFO_02                   , T_To,
			ATT_T_INFO_03                , T_Title,
			ATT_T_INFO_04                 , T_Body,
			ATT_I_AUT_USER             , I_Aut_User,
			ATT_D_DATE_01                 , D_Date
		);
		doInitDBFlag();
	}
	
	public TaMsgMessage(Integer I_Entity_Type, Integer I_Entity_ID, Integer I_Status, Integer I_Type_Msg, Integer I_Type_Noti, String T_From, String T_To, String T_Title, String T_Body, Integer I_Aut_User, Date D_Date) throws Exception {
		this.reqSetAttr(
			ATT_I_STATUS               , I_Status,
			ATT_I_TYPE_01             , I_Type_Msg,
			ATT_I_TYPE_02            , I_Type_Noti,
			ATT_T_INFO_01                 , T_From,
			ATT_T_INFO_02                   , T_To,
			ATT_T_INFO_03                , T_Title,
			ATT_T_INFO_04                 , T_Body,
			ATT_I_AUT_USER             , I_Aut_User,
			ATT_D_DATE_01                , D_Date,
			ATT_I_ENTITY_TYPE          , I_Entity_Type,
			ATT_I_ENTITY_ID            , I_Entity_ID
		);
		doInitDBFlag();
	}
	
	
	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}

	@Override
	public void doMergeWith(TaMsgMessage ent) {
		if (ent == this) return;
		this.I_Status             = ent.I_Status;
		this.I_Type_01            = ent.I_Type_01;
		this.I_Type_02            = ent.I_Type_02;
		this.T_Info_01            = ent.T_Info_01;
		this.T_Info_02            = ent.T_Info_02;
		this.T_Info_03            = ent.T_Info_03;
		this.T_Info_04            = ent.T_Info_04;
		this.I_Aut_User           = ent.I_Aut_User;
		this.D_Date_01            = ent.D_Date_01;
		this.D_Date_02            = ent.D_Date_02;



		//---------------------Merge Transient Variables if exist-----------------------
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;
		
		ok = (I_ID == ((TaMsgMessage)o).I_ID);
		if (!ok) return ok;

				
		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}

	
	public void doBuildDocuments(boolean forced) throws Exception {
		if (this.O_Documents != null && !forced) return;
		this.O_Documents = TaTpyDocument.reqTpyDocuments(ENT_TYP, I_ID, null, null);
	}
	
	public static void doNewNotification(Integer uId, String uName, String uAva, Integer stat, Integer entTyp01, Integer entId01, String entCod01, String entTit01, Integer entTyp02, Integer entId02, Integer entStat02) throws Exception {
		if(uId == null || entId01 == null || entTyp01 == null) return;
		
		String msg 	= ToolJSON.reqJSonString(
//						"id"		, System.currentTimeMillis(),
						"typ01"		, entTyp01, 
						"id01"		, entId01,
						"cod01"		, entCod01,
						"typ02"		, entTyp02,
						"id02"		, entId02,
						"uName"		, uName,
						"uAva"		, uAva,
						"stat"		, stat,
						"dt"		, System.currentTimeMillis() //ToolDate.reqString(new Date(), ToolDate.FORMAT_ISO)
						);
		
		if(entTit01 != null) {
			msg = msg.substring(0, msg.length() - 1) + ",\"tit01\":\"" + entTit01 + "\"}";
		}
		
		if(entStat02 != null) {
			msg = msg.substring(0, msg.length() - 1) + ",\"stat02\":" + entStat02 + "}";
		}
		
		TaMsgMessage 	noti 	= TaMsgMessage.DAO.reqEntityByValues(TaMsgMessage.ATT_I_AUT_USER, uId, TaMsgMessage.ATT_I_TYPE_01, TaMsgMessage.TYPE_01_NOTIFICATION);
		String 			lstNots = "";
		
		if(noti == null) {
			noti = new TaMsgMessage();
			noti.reqSet(TaMsgMessage.ATT_I_STATUS	, TaMsgMessage.STAT_NOTI_NEW);
			noti.reqSet(TaMsgMessage.ATT_I_TYPE_01	, TaMsgMessage.TYPE_01_NOTIFICATION);
			noti.reqSet(TaMsgMessage.ATT_I_AUT_USER	, uId);
			noti.reqSet(TaMsgMessage.ATT_D_DATE_01	, new Date());
			
			lstNots = "[" + msg + "]";
			
			noti.reqSet(TaMsgMessage.ATT_T_INFO_05, lstNots);
			TaMsgMessage.DAO.doPersist(noti);
		} else {
			lstNots =  noti.reqStr(TaMsgMessage.ATT_T_INFO_05);
				
			if(lstNots == null || lstNots.isEmpty()) {
				lstNots = "[" + msg + "]";
			} else {
				lstNots = lstNots.substring(0, lstNots.length() - 1) + "," + msg + "]";
			}
			
			noti.reqSet(TaMsgMessage.ATT_T_INFO_05	, lstNots);
			noti.reqSet(TaMsgMessage.ATT_D_DATE_01	, new Date());
			TaMsgMessage.DAO.doMerge(noti);
		}
	}
}
