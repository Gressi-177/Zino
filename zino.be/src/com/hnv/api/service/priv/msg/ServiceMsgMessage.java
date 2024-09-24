package com.hnv.api.service.priv.msg;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hnv.api.def.DefAPI;
import com.hnv.api.def.DefJS;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.api.service.common.APIAuth;
import com.hnv.api.service.socket.ChatMessage;
import com.hnv.api.service.socket.ServiceChatEndpoint;
import com.hnv.common.tool.ToolDBLock;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolDatatable;
import com.hnv.common.tool.ToolDate;
import com.hnv.common.tool.ToolFile;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.common.tool.ToolSet;
import com.hnv.common.tool.ToolZip;
import com.hnv.data.json.JSONArray;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.aut.vi.ViAutUserMember;
import com.hnv.db.msg.TaMsgMessage;
import com.hnv.db.msg.TaMsgMessageHistory;
import com.hnv.db.msg.TaMsgMessageStore;
import com.hnv.db.msg.vi.ViMsgNotification;
import com.hnv.db.nso.TaNsoGroup;
import com.hnv.db.nso.TaNsoGroupMember;
import com.hnv.db.nso.vi.ViNsoGroupUnread;
import com.hnv.db.sys.TaSysLock;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.def.DefAPIExt;
import com.hnv.def.DefDBExt;


/**
 * ----- ServiceMsgMessage by H&V
 * ----- Copyright 2017------------
 */
public class ServiceMsgMessage implements IService {

	//--------------------------------Service Definition----------------------------------
	public static final String 	SV_MODULE 				= "EC_V3/msg".toLowerCase();

	public static final String 	SV_CLASS 				= "ServiceMsgMessage".toLowerCase();	
	public static final Integer	ENT_TYP					= DefDBExt.ID_TA_MSG_MESSAGE;

	public static final String SV_DO_GET 				= "SVGet".toLowerCase();	
	public static final String SV_DO_LST 				= "SVLst".toLowerCase();
	public static final String SV_DO_LST_CHAT_ROOM 		= "SVLstChatRoom".toLowerCase();
	public static final String SV_DO_LST_DYN 			= "SVLstDyn".toLowerCase();
	public static final String SV_DO_LST_NOTI_BY_ENT 	= "SVLstNotiByEnt".toLowerCase();
	public static final String SV_DO_LST_DYN_MANAGER 	= "SVLstDynManager".toLowerCase();

	public static final String SV_DO_MERGE_NOTI 		= "SVMergeNoti".toLowerCase();
	

	public static final String SV_DO_NEW 				= "SVNew".toLowerCase();
	public static final String SV_DO_MOD 				= "SVMod".toLowerCase();	
	public static final String SV_DO_DEL 				= "SVDel".toLowerCase();

	public static final String SV_DO_CHAT_NEW_SOCKET			= "SVNewWebsocket".toLowerCase();	
	public static final String SV_DO_CHAT_NEW 					= "SVChatNew".toLowerCase();	
	public static final String SV_DO_CHAT_NEW_NEW_WITH_IMG		= "SVChatNewWithImg".toLowerCase();
	public static final String SV_DO_CHAT_LST 					= "SVChatLst".toLowerCase();	
	public static final String SV_DO_MEMBER_LST 				= "SVMemberLst".toLowerCase();
	public static final String SV_DO_MEMBER_LST_WAITING			= "SVMemberLstWaiting".toLowerCase();
	public static final String SV_DO_CHAT_LST_BG 				= "SVChatLstBg".toLowerCase();	

	public static final String SV_DO_CHAT_LST_DEL_BY_ID			= "SVChatLstById".toLowerCase();	

	public static final String SV_DO_MEMBER_ROLE 				= "SVMemberRole".toLowerCase();
	public static final String SV_DO_MEMBER_JOIN 				= "SVMemberJoin".toLowerCase();
	public static final String SV_DO_MEMBER_JOIN_PUBLIC			= "SVMemberJoinPublic".toLowerCase();
	public static final String SV_DO_MEMBER_CANCEL_JOIN 		= "SVMemberCancelJoin".toLowerCase();
	
	public static final String SV_DO_CHAT_DEL 					= "SVChatDel"			.toLowerCase();
	public static final String SV_DO_CHAT_HIDE 					= "SVChatHide"			.toLowerCase();
	public static final String SV_DO_CHAT_HISTORY_NEW 			= "SVChatHistoryNew"	.toLowerCase();
	public static final String SV_DO_CHAT_HISTORY_LST			= "SVChatHistoryLst"	.toLowerCase();

	public static final String SV_DO_LCK_REQ 	= "SVLckReq".toLowerCase(); //req or refresh	
	public static final String SV_DO_LCK_SAV 	= "SVLckSav".toLowerCase(); //save and continue
	public static final String SV_DO_LCK_END 	= "SVLckEnd".toLowerCase();
	public static final String SV_DO_LCK_DEL 	= "SVLckDel".toLowerCase();

	public static final String SV_DO_SEND 			= "SVSend".toLowerCase();

	public static final String SV_DO_RESP_GET 		= "SVRespGet".toLowerCase();	

	public static final String SV_DO_COUNT_TOTAL	= "SVCountTotal".toLowerCase();

	public static final String SV_DO_LST_WAIT_READ	= "SVLstWaitRead".toLowerCase();	
	//-----------------------------------------------------------------------------------------------
	public static final String SV_NOTI_COUNT 				= "SVNotiCount"		.toLowerCase();	
	public static final String SV_NOTI_LIST 				= "SVNotiLst"		.toLowerCase();	
	public static final String SV_NOTI_DEL 					= "SVNotiDel"		.toLowerCase();	
	public static final String SV_NOTI_DEL_ALL 				= "SVNotiDelAll"	.toLowerCase();
	public static final String SV_NOTI_READ 				= "SVNotiRead"		.toLowerCase();


	//-----------------------------------------------------------------------------------------------

	//-------------------------Default Constructor - Required -------------------------------------
	public ServiceMsgMessage(){
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		// ToolLogServer.doLogInf("--------- "+ SV_CLASS+ ".doService --------------");

		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {
			if(sv.equals(SV_DO_GET)						){
				doGet(user, json, response);
			} else if(sv.equals(SV_DO_RESP_GET) 		){
				doRespGet(user, json, response);
			} else if(sv.equals(SV_DO_LST)				){
				doLst(user, json, response);
			} else if(sv.equals(SV_DO_LST_CHAT_ROOM)	){
				doLstChatRoom(user, json, response);
			} else if(sv.equals(SV_DO_LST_DYN) 			){
				doLstDyn(user, json, response);
			} else if(sv.equals(SV_DO_LST_NOTI_BY_ENT) ){
				doLstNotiByEnt(user, json, response);
			}
			else if(sv.equals(SV_DO_MERGE_NOTI) 		){
				doMergeNoti(user, json, response);

			} else if(sv.equals(SV_DO_LST_DYN_MANAGER) 	){
				doLstDynManager(user, json, response);

			} else if(sv.equals(SV_DO_NEW)				){
				doNew(user, json, response);
			} else if(sv.equals(SV_DO_MOD)				){
				doMod(user, json, response);
			} else  if(sv.equals(SV_DO_DEL)				){
				doDel(user, json, response);

				//--------------CHAT ROOM-------------------------

			} else if(sv.equals(SV_DO_CHAT_NEW_SOCKET)		)  {
				doMsgChatNew(user, json, response);
			} else if(sv.equals(SV_DO_CHAT_NEW)				)  {
				doMsgChatNew(user, json, response);
			} else if(sv.equals(SV_DO_CHAT_NEW_NEW_WITH_IMG)){
				doMsgChatNewWithImg(user, json, response);
			} else if(sv.equals(SV_DO_CHAT_LST)				)  {
				doMsgChatLst(user, json, response);
			} else if(sv.equals(SV_DO_CHAT_LST_BG)			)  {
				doMsgChatLstBg(user, json, response);
			
			} else if(sv.equals(SV_DO_MEMBER_LST)			)  {
				doMsgMemberLst(user, json, response);
			} else if(sv.equals(SV_DO_MEMBER_LST_WAITING)	)  {
				doMsgMemberLstWaiting(user, json, response);
			} else if(sv.equals(SV_DO_CHAT_LST_DEL_BY_ID)	)  {
				doMsgChatLstDelById(user, json, response);
			} else if(sv.equals(SV_DO_MEMBER_ROLE)			)  {
				doMsgMemberRole(user, json, response);	
			} else if(sv.equals(SV_DO_MEMBER_JOIN)			)  {
				doMsgMemberJoin(user, json, response);
			} else if(sv.equals(SV_DO_MEMBER_JOIN_PUBLIC)	)  {
				doMsgMemberJoinPublic(user, json, response);
			} else if(sv.equals(SV_DO_MEMBER_CANCEL_JOIN)	)  {
				doMsgMemberCancelJoin(user, json, response);
			} else if(sv.equals(SV_DO_CHAT_DEL)				)  {
				doMsgChatDel(user, json, response);
			} else if(sv.equals(SV_DO_CHAT_HIDE)			)  {
				doMsgChatHide(user, json, response);
				//---------------------------------------------------
			} else if(sv.equals(SV_DO_SEND) 		){
				doSend(user, json, response);

			} else if(sv.equals(SV_DO_LCK_REQ)		){
				doLckReq(user, json, response);
			} else if(sv.equals(SV_DO_LCK_SAV)		){
				doLckSav(user, json, response);
			} else if(sv.equals(SV_DO_LCK_END)		){
				doLckEnd(user, json, response);
			} else if(sv.equals(SV_DO_LCK_DEL)		){
				doLckDel(user, json, response);

			} else if(sv.equals(SV_DO_COUNT_TOTAL)	){
				doCountTotal(user, json, response);
			} else if(sv.equals(SV_DO_LST_WAIT_READ)	){
				doLstWaitRead(user, json, response);

			//----Service for notification-------------------------
			} else if(sv.equals(SV_NOTI_COUNT) 			){
				doNotiCount(user,  json, response);
			} else if(sv.equals(SV_NOTI_LIST) 			){
				doNotiLst(user,  json, response);
			} else if(sv.equals(SV_NOTI_DEL) 			){
				doNotiDel(user,  json, response);
			} else if(sv.equals(SV_NOTI_DEL_ALL) 		){
				doNotiDelAll(user,  json, response);
			} else if(sv.equals(SV_NOTI_READ) 			){
				doNotiRead(user,  json, response);

			//----Service for notification chat -------------------------
			} else if(sv.equals(SV_DO_CHAT_HISTORY_NEW) ){
				doMessageHistoryNew (user,  json, response);
				
			} else if(sv.equals(SV_DO_CHAT_HISTORY_LST) ){
				doMessageHistoryLst (user,  json, response);
				
			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}

		} catch (Exception e) {
			API.doResponse(response, DefAPI.API_MSG_ERR_API);
			e.printStackTrace();
		}
	}
	//---------------------------------------------------------------------------------------------------------

	private static final int WORK_GET = 1;
	private static final int WORK_LST = 2;
	private static final int WORK_NEW = 3;
	private static final int WORK_MOD = 4;
	private static final int WORK_DEL = 5;

	private static boolean canWorkWithObj ( TaAutUser user, int typWork, Object...params){
		switch(typWork){
		case WORK_GET : 
			//check something with params
			return true;
		case WORK_LST : 
			//check something with params
			return true;
		case WORK_NEW : 
			//check something with params
			return true;
		case WORK_MOD : 
			//check something with params
			return true;	
		case WORK_DEL : 
			//check something with params
			return true;
		}
		return false;
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doGet(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	

		Integer 			objId	= ToolData.reqInt 	(json, "id"		, -1	);				
		Boolean				forced	= ToolData.reqBool 	(json, "forced"	, false	);
		TaMsgMessage 		ent 	= TaMsgMessage.DAO.reqEntityByRef(objId, forced);

		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, ent 
				));

	}
	//---------------------------------------------------------------------------------------------------------
	private static void doRespGet(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		Integer 			msgId	= ToolData.reqInt  (json, "msgId"	, -1	);			
		Date				dLast	= ToolData.reqDate (json, "dLast"	, ToolDate.reqDateByAdding(new Date(), 0 , 0, 0, 0, 0, -50));


		List<TaMsgMessage>  lst	 	= TaMsgMessage.DAO.reqList(
				Restrictions.eq(TaMsgMessage.ATT_I_TYPE_01	, TaMsgMessage.TYPE_01_CONTACT_CHAT_RESP), 
				Restrictions.eq(TaMsgMessage.ATT_I_AUT_USER	, msgId),
				Restrictions.ge(TaMsgMessage.ATT_D_DATE_01	, dLast)
				);

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, lst 
				));

	}
	//---------------------------------------------------------------------------------------------------------
	private static void doLst(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		List<TaMsgMessage> 	list = doLst(user, json); //and other params if necessary
		if (list==null ){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, list 
				));
		//----------------------------------------------------------------------------------------------------------------------
		//----------------------------------------------------------------------------------------------------------------------
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doLstChatRoom(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		List<TaMsgMessage> 	list = doLstChatRoom(user, json); //and other params if necessary
		if (list==null ){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, list 
				));
		//----------------------------------------------------------------------------------------------------------------------
		//----------------------------------------------------------------------------------------------------------------------
	}

	private static Criterion reqCriterion(Object...params) throws Exception{
		if (params==null || params.length==0) return null;

		Criterion cri = Restrictions.gt("I_ID", 0);		

		if (params!=null && params.length>0){
			String username= String.valueOf(params[1]);
			//			int typMsg	   = (int) params[2];
			if(!username.equals("visitor") && !username.equals("null")) {
				cri = Restrictions.and(cri, Restrictions.or(
						Restrictions.eq(TaMsgMessage.ATT_I_TYPE_01, TaMsgMessage.TYPE_01_CHAT_PRIVATE),
						Restrictions.eq(TaMsgMessage.ATT_I_TYPE_01, TaMsgMessage.TYPE_01_CHAT_PUBLIC)),
						Restrictions.or(Restrictions.eq(TaMsgMessage.ATT_T_INFO_01, username), Restrictions.eq(TaMsgMessage.ATT_T_INFO_02, username)));
			}

		}		

		if (params!=null && params.length>1){
			Integer userId= (Integer) params[2];
			//			int typMsg	   = (int) params[2];
			if(userId != 3 && userId != null) { //userId = 3:  visitor
				cri = Restrictions.and(cri, Restrictions.or(
						Restrictions.eq(TaMsgMessage.ATT_I_TYPE_01, TaMsgMessage.TYPE_01_CHAT_PRIVATE),
						Restrictions.eq(TaMsgMessage.ATT_I_TYPE_01, TaMsgMessage.TYPE_01_CHAT_PUBLIC)),
						Restrictions.or(Restrictions.eq(TaMsgMessage.ATT_I_AUT_USER, userId), 
										Restrictions.and(	Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_ID	, userId),	
															Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_NSO_GROUP_MEMBER))),
						Restrictions.and(	Restrictions.isNotNull((TaMsgMessage.ATT_I_ENTITY_ID)),
											Restrictions.isNotNull(TaMsgMessage.ATT_I_ENTITY_TYPE)));
			}
		}

		//		if (params!=null && params.length>2){
		//do something
		//		}

		return cri;
	}

	private static List<TaMsgMessage> doLst(TaAutUser user,  JSONObject json, Object...params) throws Exception {
		Integer 			objTyp	= ToolData.reqInt (json, "typ"		, -1);
		String 			  username	= ToolData.reqStr (json, "username"	, "");
		Integer 			typMsg  = ToolData.reqInt (json, "typMsg"   , -1);
		//other params here

		if (!canWorkWithObj(user, WORK_LST, objTyp, username, typMsg)){ //other param after objTyp...
			return null;
		}

		Criterion 			cri		= reqCriterion (objTyp, username); //and other params	
		List<TaMsgMessage> 	list 	= null;
		if (cri==null) 
			list =   TaMsgMessage.DAO.reqList();
		else
			list =   TaMsgMessage.DAO.reqList(cri);

		List<TaMsgMessage> groupMsg	= new ArrayList<TaMsgMessage>();

		List<TaNsoGroupMember> lstGr= TaNsoGroupMember.reqNsoGroupIDByUser(user.reqId());
		Set<Integer> 			ids = ToolSet.reqSetInt(lstGr, TaNsoGroupMember.ATT_I_NSO_GROUP);

		List<String> idLst = new ArrayList<>();
		for(Integer i: ids) {
			idLst.add(i.toString());
		}

		Criterion cri2 = Restrictions.ne(TaMsgMessage.ATT_T_INFO_01, username);

		groupMsg = TaMsgMessage.DAO.reqList_In(TaMsgMessage.ATT_T_INFO_02, idLst, cri2);

		Set<TaMsgMessage> set = new HashSet<TaMsgMessage>(list);
		set.addAll(groupMsg);



		List<TaMsgMessage> finalList= new ArrayList<>();
		finalList.addAll(set);
		//		finalList.removeAll(groupMsg);
		//		finalList.addAll(groupMsg);


		if (params!=null){
			//do something with list before return
		}	
		//do something else with request

		return finalList;
	}

	private static List<TaMsgMessage> doLstChatRoom(TaAutUser user, JSONObject json, Object...params) throws Exception {
		Integer 			objTyp	= ToolData.reqInt (json, "typ"		, -1);
		String 			  username	= ToolData.reqStr (json, "username"	, "");
		Integer 			typMsg  = ToolData.reqInt (json, "typMsg"   , -1);
		Integer 			userId  = user.reqId();
		//other params here

		if (!canWorkWithObj(user, WORK_LST, objTyp, username, typMsg)){ //other param after objTyp...
			return null;
		}

		Criterion 			cri		= reqCriterion (objTyp, null, userId); //and other params	
		List<TaMsgMessage> 	list 	= null;
		Order 				order 	= Order.asc (TaMsgMessage.ATT_D_DATE_01);			

		if (cri==null) 
			list =   TaMsgMessage.DAO.reqList(order);
		else
			list =   TaMsgMessage.DAO.reqList(order, cri);

		List<TaMsgMessage> groupMsg	= new ArrayList<TaMsgMessage>();

		List<TaNsoGroupMember> lstGr= TaNsoGroupMember.reqNsoGroupIDByUser(user.reqId());
		Set<Integer> 			ids = ToolSet.reqSetInt(lstGr, TaNsoGroupMember.ATT_I_NSO_GROUP);

		List<Integer> idLst = new ArrayList<>();
		for(Integer i: ids) {
			idLst.add(i);
		}

		Criterion cri2 = Restrictions.ne(TaMsgMessage.ATT_I_AUT_USER, userId);
		cri2 = Restrictions.and(cri2, Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_TYPE, DefDBExt.ID_TA_NSO_GROUP));

		groupMsg = TaMsgMessage.DAO.reqList_In(TaMsgMessage.ATT_I_ENTITY_ID, idLst, cri2);

		Set<TaMsgMessage> set = new HashSet<TaMsgMessage>(list);
		set.addAll(groupMsg);



		List<TaMsgMessage> finalList= new ArrayList<>();
		finalList.addAll(set);
		Collections.sort(finalList, new Comparator<TaMsgMessage>() {
			public int compare(TaMsgMessage o1, TaMsgMessage o2) {
				if (o1.req(TaMsgMessage.ATT_D_DATE_01) == null || o2.req(TaMsgMessage.ATT_D_DATE_01) == null)
					return 0;
				return ((Date) o1.req(TaMsgMessage.ATT_D_DATE_01)).compareTo((Date) o2.req(TaMsgMessage.ATT_D_DATE_01));
			}
		});
		//		finalList.removeAll(groupMsg);
		//		finalList.addAll(groupMsg);


		if (params!=null){
			//do something with list before return
		}	
		//do something else with request

		return finalList;
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doNew(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doNew --------------");

		TaMsgMessage 			ent		= doNew		(user, json);



		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}else{		
			Boolean	send			= ToolData.reqBool(json, "sendEmail" , false);
			if (send) ServiceMsgEmail.doSendEmail(ent);

			API.doResponse(response,ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA	, ent
					));				
		}
	}

	private static TaMsgMessage doNew(TaAutUser user, JSONObject json) throws Exception {
		JSONObject	obj			= ToolData.reqJson (json, "obj"			, null);
		Boolean		forManager	= ToolData.reqBool (json, "forManager"	, false);		
		Integer		msgId		= ToolData.reqInt (json, "msgId"		, 0);	

		//		if (!canWorkWithObj(user, WORK_NEW, obj)){ //other param after obj...
		//			return null;
		//		}

		//		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaMsgMessage.class);

		Integer typMsg	= obj.get("typMsg")	!=null	? Integer.parseInt(obj.get("typMsg").toString())	:-1;
		String 	email 	= null;
		String	to      = null;

		if(forManager) {
			//check validation msg type contact = 5 or msg type claim = 2;
			if(typMsg == TaMsgMessage.TYPE_01_CONTACT || typMsg == TaMsgMessage.TYPE_01_CLAIM){
				email	= obj.get("from")!=null ? obj.get("from").toString()	:null; //ToolData.reqStr	(json, "email");
				to		= obj.get("to")	 !=null ? obj.get("to").toString()		:null;
			}
		}else {
			if(typMsg == TaMsgMessage.TYPE_01_CONTACT || typMsg == TaMsgMessage.TYPE_01_CLAIM || typMsg == TaMsgMessage.TYPE_01_CONTACT_CHAT 
					|| typMsg == TaMsgMessage.TYPE_01_CHAT_PRIVATE || typMsg == TaMsgMessage.TYPE_01_CHAT_PUBLIC ){
				email	= obj.get("from")!=null ? obj.get("from").toString()	:null; //ToolData.reqStr	(json, "email");
				to		= obj.get("to")!=null ? obj.get("to").toString()	:"HNV TECH<contact@hnv-tech.com>";
			}
		}

		if (email == null) return null;

		Integer				userId	= user.reqId();

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaMsgMessage.class);
		if (typMsg == TaMsgMessage.TYPE_01_CONTACT_CHAT) {
			userId = msgId;
		}

		attr.put(TaMsgMessage.ATT_I_STATUS, 	TaMsgMessage.STAT_MSG_INBOX);
		attr.put(TaMsgMessage.ATT_I_TYPE_01, 	typMsg);
		attr.put(TaMsgMessage.ATT_T_INFO_01,	email);
		attr.put(TaMsgMessage.ATT_T_INFO_02,	to);
		attr.put(TaMsgMessage.ATT_I_AUT_USER, 	userId);
		attr.put(TaMsgMessage.ATT_D_DATE_01,	new Date());

		TaMsgMessage  		ent	 	= new TaMsgMessage(attr);

		if (!canWorkWithObj(user, WORK_NEW, ent)){ //other param after obj...
			return null;
		}

		TaMsgMessage.DAO.doPersist(ent);
		return ent;
	}

	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------
	private static List<TaMsgMessage> reqMsgChatNewWithImg(TaAutUser user, JSONObject json) throws Exception {
		JSONObject	obj			= ToolData.reqJson 	(json, "obj"	, null);
		String 		message 	= ToolData.reqStr 	(json, "msg"	, null);
		String 		info 		= ToolData.reqStr 	(json, "info"	, null);
		Integer 	typMsg 		= ToolData.reqInt 	(json, "typMsg"	, null);
		Integer 	entId 		= ToolData.reqInt 	(json, "entId"	, null);
		String 		split 		= ToolData.reqStr 	(json, "split"	, null);

		if(entId == null)		return null;

		Integer entTyp			= DefDBExt.ID_TA_NSO_GROUP;

		Integer				userId	= user.reqId();
		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaMsgMessage.class); 

		String randomString = UUID.randomUUID().toString(); //to split without removing text
		String[] lstMsg = message.replace(split, split + randomString ).split(randomString, -1);
		List<TaMsgMessage> list 	= new ArrayList<TaMsgMessage>();	

		for (String msg : lstMsg) {
			if (msg.length() > 0) {
				attr.put(TaMsgMessage.ATT_I_STATUS, 	TaMsgMessage.STAT_MSG_INBOX);
				attr.put(TaMsgMessage.ATT_I_TYPE_01, 	typMsg);
				attr.put(TaMsgMessage.ATT_T_INFO_04, 	msg);
				attr.put(TaMsgMessage.ATT_I_AUT_USER, 	userId);
				attr.put(TaMsgMessage.ATT_I_ENTITY_ID, 	entId);
				attr.put(TaMsgMessage.ATT_I_ENTITY_TYPE,entTyp);
				attr.put(TaMsgMessage.ATT_D_DATE_01, 	new Date());

				TaMsgMessage  		ent	 	= new TaMsgMessage(attr);

				if (!canWorkWithObj(user, WORK_NEW, ent)){ //other param after obj...
					return null;
				}

				TaMsgMessage.DAO.doPersist(ent);
				ServiceChatEndpoint.doSendMsg(ent, user);
				list.add(ent);
			}
		}


		return list;
	}

	private static TaMsgMessage reqMsgChatNew(TaAutUser user, JSONObject json) throws Exception {
		JSONObject	obj			= ToolData.reqJson 	(json, "obj"	, null);
		String 		message 	= ToolData.reqStr 	(json, "msg"	, null);
		JSONObject	info 		= ToolData.reqJson 	(json, "info"	, null);
		Integer 	typMsg 		= ToolData.reqInt	(json, "typMsg"	, null);
		Integer 	entId 		= ToolData.reqInt 	(json, "entId"	, null);

		if(entId == null)		return null;
		if(obj   == null)		obj = new JSONObject();

		Integer entTyp	= DefDBExt.ID_TA_NSO_GROUP;

		Integer				userId	= user.reqId();
		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaMsgMessage.class);
		attr.put(TaMsgMessage.ATT_I_STATUS, 	TaMsgMessage.STAT_MSG_INBOX);
		attr.put(TaMsgMessage.ATT_I_TYPE_01, 	typMsg);	
		attr.put(TaMsgMessage.ATT_T_INFO_01, 	user.req(TaAutUser.ATT_T_LOGIN_01));
		attr.put(TaMsgMessage.ATT_T_INFO_04, 	message);		
		attr.put(TaMsgMessage.ATT_I_AUT_USER, 	userId);
		attr.put(TaMsgMessage.ATT_I_ENTITY_ID, 	entId);
		attr.put(TaMsgMessage.ATT_I_ENTITY_TYPE,entTyp);
		attr.put(TaMsgMessage.ATT_D_DATE_01,	new Date());

		TaMsgMessage  		ent	 	= new TaMsgMessage(attr);
		if (!canWorkWithObj(user, WORK_NEW, ent)) return null;
		
		doHandleFileUpload(user, info, entId, ent);	
		
		TaMsgMessage.DAO.doPersist(ent);
		
		return ent;
	}
	
	/**
	 * Handle file upload information.
	*/
	private static void doHandleFileUpload(TaAutUser user, JSONObject info, Integer entId, TaMsgMessage ent) throws Exception {
		JSONArray filesArray   			= (JSONArray) info.get("files");
		if(filesArray != null) {
			//Get a set of document IDs from the filesArray
			Set<Integer> 		docIds 	= new HashSet<Integer>();
			for (Object obj : filesArray) {
			    JSONObject file = (JSONObject) obj;
			    Integer id = ToolData.reqInt(file, "id", null);
			    if (id != null) {
			    	docIds.add(id);
			    }
			}
			
			List<TaTpyDocument> lstDoc  = new ArrayList<TaTpyDocument>();
			if (!docIds.isEmpty()) {
				List<TaTpyDocument> docs = TaTpyDocument.DAO.reqList_In(TaTpyDocument.ATT_I_ID, docIds, Restrictions.eq(TaTpyDocument.ATT_I_STATUS, TaTpyDocument.STAT_NEW));
				TaTpyDocument.reqListSaveFromNewToValidated (user, DefDBExt.ID_TA_MSG_MESSAGE, entId, docs);
				lstDoc.addAll(docs);
			}

			Map<Integer, TaTpyDocument> documentMap = lstDoc.stream().collect(Collectors.toMap(TaTpyDocument::reqId, Function.identity()));

			// Update the "fUrl" field for each file in the filesArray
			for(int i = 0; i < filesArray.size();i++) {
				JSONObject file =  (JSONObject)filesArray.get(i);
				Integer fileId = 	ToolData.reqInt(file, "id", null);
				TaTpyDocument docExist = documentMap.get(fileId);
				if(docExist != null) {
					String fileUrl = docExist.reqStr(TaTpyDocument.ATT_T_INFO_05);
					if (fileUrl == null) {
					    fileUrl = docExist.reqStr(TaTpyDocument.ATT_T_INFO_03);
					}
					if (fileUrl != null) {
					    file.put("fUrl", fileUrl);
					}
				}
			}
			
			ent.reqSet(TaMsgMessage.ATT_T_INFO_05, info.toJSONString());	
		}
	}
	
	private static void doMsgChatNew(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {

		TaMsgMessage 			ent		= reqMsgChatNew		(user, json);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}else{				
			API.doResponse(response,ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA	, ent
					));		
			//lauch websocket end point with userId
			ServiceChatEndpoint.doSendMsg(ent, user);
		}
	}

	private static void doMsgChatNewWithImg(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {

		List<TaMsgMessage> 			list		= reqMsgChatNewWithImg		(user, json);
		if (list==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}else{				
			API.doResponse(response,ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA	, list
					));		
			//lauch websocket end point with userId

			/*
			 * for (TaMsgMessage ent : list) { ServiceChatEndpoint.doSendMsg(ent, user); }
			 */
		}
	}

	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	private static void doMsgChatLst(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		List<TaMsgMessage> 	lst		= reqMsgChatLst		(user, json);
		if (lst==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}else{				
			API.doResponse(response,ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA	, lst
					));		
		}
	}

	private static List<TaMsgMessage> reqMsgChatLst(TaAutUser user, JSONObject json) throws Exception {
		Integer		begin		= ToolData.reqInt (json, "begin"		, 0);
		Integer		nb			= ToolData.reqInt (json, "nb"			, 20);
		//		Integer		msgTyp		= ToolData.reqInt (json, "msgTyp"		, 0); //200: private, 201: public	
		Integer		entId		= ToolData.reqInt (json, "entId"		, 0); //id of group or person
		Date		dateLast	= ToolData.reqDate (json, "dateLast"	, null);
		//		Integer 	entTyp		= DefDBExt.ID_TA_AUT_USER;

		if (entId<=0) return null; //msgTyp<=0 || 

		Criterion 			cri = null;
		List<TaMsgMessage> 	lst	= null;
		//--private
		//		if (msgTyp.equals(TaMsgMessage.TYPE_01_CHAT_PRIVATE)) {
		//			entTyp	= DefDBExt.ID_TA_AUT_USER	;
		//			cri 	= Restrictions.and(	Restrictions.eq(TaMsgMessage.ATT_I_TYPE_MSG		, msgTyp), 
		//					Restrictions.or (
		//							Restrictions.and(	Restrictions.eq(TaMsgMessage.ATT_I_AUT_USER		, idUser), 
		//									Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_TYPE	, entTyp), Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_ID, entId)),
		//							Restrictions.and(	Restrictions.eq(TaMsgMessage.ATT_I_AUT_USER		, entId), 
		//									Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_TYPE	, entTyp), Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_ID, idUser))
		//							));
		//			lst	= TaMsgMessage.DAO.reqList(begin, nb, Order.desc(TaMsgMessage.ATT_I_ID), cri );		
		//			
		//			if(lst.size() == 0) {
		//				cri 	= Restrictions.and(	Restrictions.eq(TaMsgMessageStore.ATT_I_TYPE_01		, msgTyp), 
		//						Restrictions.or (
		//								Restrictions.and(	Restrictions.eq(TaMsgMessageStore.ATT_I_ENTITY_ID_01		, idUser), 
		//										Restrictions.eq(TaMsgMessageStore.ATT_I_ENTITY_TYPE	, entTyp), Restrictions.eq(TaMsgMessageStore.ATT_I_ENTITY_ID_02, entId)),
		//								Restrictions.and(	Restrictions.eq(TaMsgMessageStore.ATT_I_ENTITY_ID_01		, entId), 
		//										Restrictions.eq(TaMsgMessageStore.ATT_I_ENTITY_TYPE	, entTyp), Restrictions.eq(TaMsgMessageStore.ATT_I_ENTITY_ID_02, idUser))
		//								));
		//				
		//				if(dateLast != null) {
		//					cri = Restrictions.and(cri, Restrictions.le(TaMsgMessageStore.ATT_D_DATE_02, dateLast));
		//				}
		//				List<TaMsgMessageStore> storages = TaMsgMessageStore.DAO.reqList(0, 1, Order.desc(TaMsgMessageStore.ATT_D_DATE_01), cri);
		//				if(storages != null && storages.size() > 0) {
		//					TaMsgMessageStore storage = storages.get(0);
		//					JSONObject msg = ToolJSON.reqJSonObjectFromString((String) storage.req(TaMsgMessageStore.ATT_T_CONTENT));
		//					JSONArray arr = (JSONArray) msg.get("val");
		//					for(int i = 0 ; i < arr.size(); i++) {
		//						Map<String, Object> attrs = API.reqMapParamsByClass((JSONObject)arr.get(i), TaMsgMessage.class);
		//						lst.add(new TaMsgMessage(attrs));
		//					}
		//					Collections.reverse(lst);
		//				}
		//			}
		//			
		//		}else if (msgTyp.equals(TaMsgMessage.TYPE_01_CHAT_PUBLIC)) {
		cri 	= Restrictions.and(	Restrictions.eq(TaMsgMessage.ATT_I_TYPE_01		, TaMsgMessage.TYPE_01_CHAT_PUBLIC), 
				Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_NSO_GROUP), 
				Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_ID	, entId));
		lst	= TaMsgMessage.DAO.reqList(begin, nb, Order.desc(TaMsgMessage.ATT_I_ID), cri );


		if(lst.size() == 0) {
			cri 	= Restrictions.and(	
					Restrictions.eq(TaMsgMessageStore.ATT_I_TYPE_01			, TaMsgMessage.TYPE_01_CHAT_PUBLIC),
					Restrictions.eq(TaMsgMessageStore.ATT_I_ENTITY_ID_01	, entId), 
					Restrictions.eq(TaMsgMessageStore.ATT_I_ENTITY_TYPE		, DefDBExt.ID_TA_NSO_GROUP));

			if(dateLast != null) {
				cri = Restrictions.and(cri, Restrictions.le(TaMsgMessageStore.ATT_D_DATE_02, dateLast));
			}
			List<TaMsgMessageStore> storages = TaMsgMessageStore.DAO.reqList(0, 1, Order.desc(TaMsgMessageStore.ATT_D_DATE_01), cri);
			if(storages != null && storages.size() > 0) {
				TaMsgMessageStore 	storage = storages.get(0);
				String 				content = (String) storage.req(TaMsgMessageStore.ATT_T_CONTENT);
				Integer				stat	= (Integer)storage.req(TaMsgMessageStore.ATT_I_STATUS);
				if (stat.equals(TaMsgMessageStore.STAT_COMPRESS_LEV_02)) content = ToolZip.reqUnzip(content);

				JSONObject 			msg 	= ToolJSON.reqJSonObjectFromString(content);
				JSONArray 			arr 	= (JSONArray) msg.get("val");
				for(int i = 0 ; i < arr.size(); i++) {
					Map<String, Object> attrs = API.reqMapParamsByClass((JSONObject)arr.get(i), TaMsgMessage.class);
					lst.add(0, new TaMsgMessage(attrs));
				}
				//					Collections.reverse(lst);
			}
		}

		if(lst != null && lst.size() > 0) {
			Set<Integer> setUser = ToolSet.reqSetInt(lst, TaMsgMessage.ATT_I_AUT_USER);
			if(!setUser.isEmpty()) {
				List<TaAutUser> lstU = TaAutUser.DAO.reqList_In(TaAutUser.ATT_I_ID, setUser);
				if(lstU != null && lstU.size() > 0) {
					Map<Integer, TaAutUser> mapU = new HashMap<Integer, TaAutUser>();
					for(TaAutUser u: lstU) {
						u. doHidePwd();
						mapU.put((Integer) u.reqRef(), u);
					}
					for(TaMsgMessage m : lst) {
						m.reqSet(TaMsgMessage.ATT_O_USER, mapU.get(m.req(TaMsgMessage.ATT_I_AUT_USER)));
					}
				}
			}
		}
		//		}else return null;

		//		DocTool.doBuildTpyDocuments(lst, DefDBExt.ID_TA_MSG_MESSAGE, null, null, TaMsgMessage.ATT_O_DOCUMENTS, false);
		return lst;
	}
	
	private static void doMsgChatLstBg(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		List<TaMsgMessage> 	lst		= reqMsgChatLstBg		(user, json);
		if (lst==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}else{				
			API.doResponse(response,ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA	, lst
					));		
		}
	}
	
	private static List<TaMsgMessage> reqMsgChatLstBg(TaAutUser user, JSONObject json) throws Exception {
		Integer		entId	= ToolData.reqInt 	(json	, "entId"	, 0); 
		Date		dtFrom	= ToolData.reqDate 	(json	, "dtFrom"	, null);

		if (entId<=0) return null; //msgTyp<=0 || 

		Criterion 			cri = null;
		List<TaMsgMessage> 	lst	= null;
		
		cri 	= Restrictions.and(	Restrictions.eq(TaMsgMessage.ATT_I_TYPE_01		, TaMsgMessage.TYPE_01_CHAT_PUBLIC), 
									Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_NSO_GROUP), 
									Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_ID	, entId));
		
		if(dtFrom != null) {
			Date dtFromNew = ToolDate.reqDateByAdding(dtFrom, 0, 0, -1, 0, 0, 0);
			
			cri = Restrictions.and(cri, Restrictions.ge(TaMsgMessage.ATT_D_DATE_01, dtFromNew));
		}
		lst		= TaMsgMessage.DAO.reqList(Order.desc(TaMsgMessage.ATT_D_DATE_01), cri);


		if(lst.size() == 0) {
			cri 	= Restrictions.and(	
					Restrictions.eq(TaMsgMessageStore.ATT_I_TYPE_01			, TaMsgMessage.TYPE_01_CHAT_PUBLIC),
					Restrictions.eq(TaMsgMessageStore.ATT_I_ENTITY_ID_01	, entId), 
					Restrictions.eq(TaMsgMessageStore.ATT_I_ENTITY_TYPE		, DefDBExt.ID_TA_NSO_GROUP));

			if(dtFrom != null) {
				cri = Restrictions.and(cri, Restrictions.le(TaMsgMessageStore.ATT_D_DATE_02, dtFrom));
			}
			List<TaMsgMessageStore> storages = TaMsgMessageStore.DAO.reqList(0, 1, Order.desc(TaMsgMessageStore.ATT_D_DATE_01), cri);
			if(storages != null && storages.size() > 0) {
				TaMsgMessageStore 	storage = storages.get(0);
				String 				content = (String) storage.req(TaMsgMessageStore.ATT_T_CONTENT);
				Integer				stat	= (Integer)storage.req(TaMsgMessageStore.ATT_I_STATUS);
				if (stat.equals(TaMsgMessageStore.STAT_COMPRESS_LEV_02)) content = ToolZip.reqUnzip(content);

				JSONObject 			msg 	= ToolJSON.reqJSonObjectFromString(content);
				JSONArray 			arr 	= (JSONArray) msg.get("val");
				for(int i = 0 ; i < arr.size(); i++) {
					Map<String, Object> attrs = API.reqMapParamsByClass((JSONObject)arr.get(i), TaMsgMessage.class);
					lst.add(0, new TaMsgMessage(attrs));
				}
			}
		}

		if(lst != null && lst.size() > 0) {
			Set<Integer> setUser = ToolSet.reqSetInt(lst, TaMsgMessage.ATT_I_AUT_USER);
			if(!setUser.isEmpty()) {
				List<TaAutUser> lstU = TaAutUser.DAO.reqList_In(TaAutUser.ATT_I_ID, setUser);
				if(lstU != null && lstU.size() > 0) {
					Map<Integer, TaAutUser> mapU = new HashMap<Integer, TaAutUser>();
					for(TaAutUser u: lstU) {
						u. doHidePwd();
						mapU.put((Integer) u.reqRef(), u);
					}
					for(TaMsgMessage m : lst) {
						m.reqSet(TaMsgMessage.ATT_O_USER, mapU.get(m.req(TaMsgMessage.ATT_I_AUT_USER)));
					}
				}
			}
		}
		
		return lst;
	}
	

	private static List<TaMsgMessage> reqMsgChatLstNew(TaAutUser user, JSONObject json) throws Exception {
		Integer		begin		= ToolData.reqInt (json, "begin"		, 0);
		Integer		nb			= ToolData.reqInt (json, "nb"			, 20);
		Integer		msgTyp		= ToolData.reqInt (json, "msgTyp"		, 0); //200: private, 201: public	
		Integer		entId		= ToolData.reqInt (json, "entId"		, 0); //id of group or person
		Date		dateLast	= ToolData.reqDate (json, "dateLast"	, null);
		Integer 	uId 		= (int)user.reqRef();

		if (msgTyp<=0 || entId<=0) return null;

		Criterion 			cri = null;
		List<TaMsgMessage> 	lst	= null;
		//--private
		if (msgTyp.equals(TaMsgMessage.TYPE_01_CHAT_PRIVATE)) {
			String ref 	= uId < entId ? "GROUP_" + uId + "_" + entId : "GROUP_" + entId + "_" + uId;//key relation 2 user

			TaNsoGroup gr = TaNsoGroup.DAO.reqEntityByValue(TaNsoGroup.ATT_T_REF, ref);

			if(gr == null) {
				gr = new TaNsoGroup(ref, msgTyp);
				gr.reqSet(TaNsoGroup.ATT_I_TYPE_02, TaNsoGroup.TYP_02_PRIVATE);
				gr.reqSet(TaNsoGroup.ATT_I_STATUS_01, TaNsoGroup.STAT_01_ACTIVE);
				gr.reqSet(TaNsoGroup.ATT_D_DATE_01, new Date());

				TaNsoGroup.DAO.doPersist(gr);

				entId = gr.reqId();
			}
		}

		Integer entTyp 	= DefDBExt.ID_TA_NSO_GROUP	;
		cri 	= Restrictions.and(	Restrictions.eq(TaMsgMessage.ATT_I_TYPE_01		, msgTyp), 
				Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_TYPE	, entTyp), 
				Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_ID	, entId));
		lst	= TaMsgMessage.DAO.reqList(begin, nb, Order.desc(TaMsgMessage.ATT_I_ID), cri );

		if(lst != null && lst.size() > 0) {
			Set<Integer> setUser = ToolSet.reqSetInt(lst, TaMsgMessage.ATT_I_AUT_USER);
			if(!setUser.isEmpty()) {
				List<TaAutUser> lstU = TaAutUser.DAO.reqList_In(TaAutUser.ATT_I_ID, setUser);
				if(lstU != null && lstU.size() > 0) {
					Map<Integer, TaAutUser> mapU = new HashMap<Integer, TaAutUser>();
					for(TaAutUser u: lstU) {
						u. doHidePwd();
						mapU.put((Integer) u.reqRef(), u);
					}
					for(TaMsgMessage m : lst) {
						m.reqSet(TaMsgMessage.ATT_O_USER, mapU.get(m.req(TaMsgMessage.ATT_I_AUT_USER)));
					}
				}
			}
		}

		TaTpyDocument.doBuildTpyDocuments(lst, DefDBExt.ID_TA_MSG_MESSAGE, null, null, TaMsgMessage.ATT_O_DOCUMENTS, false);
		return lst;
	}

	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	private static void doMsgChatLstDelById(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		boolean		ok		= canDelMsgChatLstById		(user, json);
		if (!ok){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}else{				
			API.doResponse(response,ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES
					));		
		}
	}

	private static boolean canDelMsgChatLstById(TaAutUser user, JSONObject json) throws Exception {
		Integer		entId		= ToolData.reqInt (json, "entId"		, 0); //id of group or person

		if (entId<=0) return false; //msgTyp<=0 || 

		Criterion 			cri = null;
		List<TaMsgMessage> 	lst	= null;

		cri 	= Restrictions.and(	Restrictions.eq(TaMsgMessage.ATT_I_TYPE_01		, TaMsgMessage.TYPE_01_CHAT_PUBLIC), 
				Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_NSO_GROUP), 
				Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_ID	, entId));
		lst	= TaMsgMessage.DAO.reqList(cri);


		if(lst != null && lst.size() > 0) {
			TaMsgMessage.DAO.doRemove(lst);
		}

		cri 	= Restrictions.and(	
				Restrictions.eq(TaMsgMessageStore.ATT_I_TYPE_01			, TaMsgMessage.TYPE_01_CHAT_PUBLIC),
				Restrictions.eq(TaMsgMessageStore.ATT_I_ENTITY_ID_01	, entId), 
				Restrictions.eq(TaMsgMessageStore.ATT_I_ENTITY_TYPE		, DefDBExt.ID_TA_NSO_GROUP));

		List<TaMsgMessageStore> storages = TaMsgMessageStore.DAO.reqList(cri);

		if(storages != null && storages.size() > 0) {
			TaMsgMessageStore.DAO.doRemove(storages);
		}

		return true;
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doMsgMemberLst(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		List<TaNsoGroupMember> 	lst		= reqMsgMemberLst		(user, json);
		if (lst==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}			
		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, lst
				));		
	}

	private static void doMsgMemberLstWaiting(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		List<TaNsoGroupMember> 	lst		= reqMsgMemberLstWaiting		(user, json);
		if (lst==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}			
		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, lst
				));		
	}

	private static List<TaNsoGroupMember> reqMsgMemberLstWaiting(TaAutUser user, JSONObject json) throws Exception {
		Integer		groupId		= ToolData.reqInt (json, "groupId"		, null); //id of group or person	

		if(groupId == null)	return null;

		List<TaNsoGroupMember> list 		= TaNsoGroupMember.DAO.reqList(Restrictions.and(
				Restrictions.eq(TaNsoGroupMember.ATT_I_NSO_GROUP	, groupId), 
				Restrictions.eq(TaNsoGroupMember.ATT_I_STATUS	, TaNsoGroupMember.STAT_NEW)));

		List<TaAutUser> 		listUsers 	= new ArrayList<TaAutUser>();

		if(list != null && list.size() > 0) {
			//---build users + their avatars
			List<ViAutUserMember> lstUsers = TaAutUser		.reqBuildUserMember	(list, TaNsoGroupMember.ATT_I_AUT_USER, TaNsoGroupMember.ATT_O_MEMBER);
			TaTpyDocument	.reqBuildAvatar(lstUsers, DefDBExt.ID_TA_AUT_USER, ViAutUserMember.ATT_O_AVATAR);
		}

		return list;
	}

	private static List<TaNsoGroupMember> reqMsgMemberLst(TaAutUser user, JSONObject json) throws Exception {
		Integer		groupId		= ToolData.reqInt (json, "groupId"		, null); //id of group or person	

		if(groupId == null)	return null;

		List<TaNsoGroupMember> list 		= TaNsoGroupMember.DAO.reqList(Restrictions.and(
				Restrictions.eq(TaNsoGroupMember.ATT_I_NSO_GROUP	, groupId), 
				Restrictions.ne(TaNsoGroupMember.ATT_I_STATUS		, TaNsoGroupMember.STAT_DELETED)));

		if(list != null && list.size() > 0) {
			//---build users + their avatars
			List<ViAutUserMember> lstUsers = TaAutUser		.reqBuildUserMember	(list, TaNsoGroupMember.ATT_I_AUT_USER, TaNsoGroupMember.ATT_O_MEMBER);
			TaTpyDocument	.reqBuildAvatar(lstUsers, DefDBExt.ID_TA_AUT_USER, ViAutUserMember.ATT_O_AVATAR);
		}

		return list;
	}

	private static void doMsgMemberRole(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doNew --------------");
		TaNsoGroupMember 	ent		= reqMsgMemberRole		(user, json);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}			
		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, ent
				));		
	}

	private static TaNsoGroupMember reqMsgMemberRole(TaAutUser user, JSONObject json) throws Exception {
		Integer		groupId		= ToolData.reqInt (json, "groupId"		, null); //id of group or person	

		if(groupId == null)	return null;

		List<TaNsoGroupMember> list 		= TaNsoGroupMember.DAO.reqList(Restrictions.and(
				Restrictions.eq(TaNsoGroupMember.ATT_I_NSO_GROUP	, groupId),
				Restrictions.eq(TaNsoGroupMember.ATT_I_AUT_USER	, user.reqId())));

		if(list != null && list.size() > 0)	return list.get(0);
		return null;
	}

	private static void doMsgMemberJoin(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		TaNsoGroupMember 	ent		= reqMsgMemberJoin		(user, json);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}			
		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES
				));		
	}

	private static TaNsoGroupMember reqMsgMemberJoin(TaAutUser user, JSONObject json) throws Exception {
		Integer		groupId		= ToolData.reqInt (json, "groupId"		, null); //id of group or person	

		if(groupId == null)	return null;
		Map<String, Object> attr = new HashMap<String, Object>();
		TaNsoGroupMember ent = new TaNsoGroupMember(attr);
		ent.reqSet(TaNsoGroupMember.ATT_I_NSO_GROUP				, groupId);
		ent.reqSet(TaNsoGroupMember.ATT_I_AUT_USER			, user.reqId());
		ent.reqSet(TaNsoGroupMember.ATT_I_STATUS			, TaNsoGroupMember.STAT_NEW);
		ent.reqSet(TaNsoGroupMember.ATT_I_TYPE				, TaNsoGroupMember.TYP_WORKER);
		ent.reqSet(TaNsoGroupMember.ATT_D_DATE_01			, new Date());

		TaNsoGroupMember.DAO.doPersist(ent);
		return ent;
	}

	private static void doMsgMemberCancelJoin(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		boolean ok		= reqMsgMemberCancelJoin		(user, json);
		if(!ok) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES
				));

	}

	private static boolean reqMsgMemberCancelJoin(TaAutUser user, JSONObject json) throws Exception {
		Integer		groupId		= ToolData.reqInt (json, "groupId"		, null); //id of group or person	
		if(groupId == null)	return false;
		List<TaNsoGroupMember> members = TaNsoGroupMember.DAO.reqList(Restrictions.and(Restrictions.eq(TaNsoGroupMember.ATT_I_NSO_GROUP, groupId),
				Restrictions.eq(TaNsoGroupMember.ATT_I_STATUS, TaNsoGroupMember.STAT_NEW)));

		TaNsoGroupMember member 	= null;
		for(TaNsoGroupMember m : members) {
			if((Integer)m.req(TaNsoGroupMember.ATT_I_AUT_USER) == (int)user.reqId()) {
				member = m;
			}
		}

		TaNsoGroupMember.DAO.doRemove(member);
		return true;
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doSend(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {

		TaMsgMessage 			ent		= doReqMessageSend		(user, json);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}			
		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, ent
				));				
	}

	//---------------------------------------------------------------------------------------------------------
	private static TaMsgMessage doReqMessageSend(TaAutUser user, JSONObject json) throws Exception  {
		JSONObject obj	 = ToolData.reqJson(json, "obj", null);

		String emailUser   = (String) user.req(TaAutUser.ATT_T_INFO_01);//email

		Integer stat	= obj.get("stat")	!=null	? Integer.parseInt(obj.get("stat").toString())	    : -1; //ToolData.reqInt	(json, "typMsg", -1);
		Integer typMsg	= obj.get("typMsg")	!=null	? Integer.parseInt(obj.get("typMsg").toString())	: 1; //ToolData.reqInt	(json, "typMsg", -1);
		String 	title   = obj.get("title")	!=null 	? obj.get("title").toString()						:"NO_DATA"; //ToolData.reqStr	(json, "title");
		String 	body	= obj.get("body")	!=null 	? obj.get("body").toString()						:"NO_DATA";	//ToolData.reqStr	(json, "body");
		String 	from 	= obj.get("from")   !=null  ? obj.get("from").toString()	                    : emailUser ;
		String	to      = obj.get("to")     !=null  ? obj.get("to").toString()	                        :"NO_DATA"; //ToolData.reqStr	(json, "email");

		if(from == null || to == null) {
			return null; 
		}

		Map<String, Object> 	attr 	= API.reqMapParamsByClass(obj, TaMsgMessage.class);	

		attr.put(TaMsgMessage.ATT_D_DATE_01, 	new Date());
		attr.put(TaMsgMessage.ATT_I_AUT_USER, 	user.reqRef());
		attr.put(TaMsgMessage.ATT_I_STATUS, 	stat);
		attr.put(TaMsgMessage.COL_I_TYPE_01,	typMsg);
		attr.put(TaMsgMessage.ATT_T_INFO_01, 	from);
		attr.put(TaMsgMessage.ATT_T_INFO_02, 	to);
		attr.put(TaMsgMessage.ATT_T_INFO_04, 	body);

		TaMsgMessage  		ent	 	= new TaMsgMessage(attr);

		if (!canWorkWithObj(user, WORK_NEW, ent)){ //other param after obj...
			return null;
		}

		TaMsgMessage.DAO.doPersist(ent);

		return ent;
	}

	private static boolean canValidEmail(String email) { 
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."	+ 
				"[a-zA-Z0-9_+&*-]+)*@" 		+ 
				"(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
				"A-Z]{2,7}$"; 

		Pattern pat = Pattern.compile(emailRegex); 
		if (email == null) 
			return false; 
		return pat.matcher(email).matches(); 
	} 

	//---------------------------------------------------------------------------------------------------------
	private static void doMod(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		TaMsgMessage		ent		= reqMod(user, json); 								
		if (ent==null){
			API.doResponse(response,ToolJSON.reqJSonString(DefJS.SESS_STAT	, 1, DefJS.SV_CODE	, DefAPI.SV_CODE_API_NO));
		} else {
			API.doResponse(response,ToolJSON.reqJSonString(DefJS.SESS_STAT	, 1, DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES));
		}				
	}

	private static TaMsgMessage reqMod(TaAutUser user, JSONObject json) throws Exception {
		JSONObject			obj		= ToolData.reqJson(json, "obj", null);

		int 				objId	= (int)obj.get("id");	
		TaMsgMessage  		ent	 	= TaMsgMessage.DAO.reqEntityByRef(objId);
		if (ent==null){
			return null;
		}

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaMsgMessage.class);		
		TaMsgMessage.DAO.doMerge(ent, attr);	

		//merge other objects from obj and request

		return ent;
	}	

	//---------------------------------------------------------------------------------------------------------
	private static void doDel(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doDel --------------");

		int				entId 	= ToolData.reqInt(json, "id", null);
		TaSysLock 		lock 	= ToolDBLock.reqLock(ENT_TYP, entId, user.reqId(), user.reqStr(TaAutUser.ATT_T_LOGIN_01), null);
		if (lock==null || lock.reqStatus() == 0){
			API.doResponse(response, ToolJSON.reqJSonString(						
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO,
					DefJS.RES_DATA		, lock
					));	
			return;
		}

		if (!canDel(user, json)){
			API.doResponse(response,DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response,DefAPI.API_MSG_OK);
		}

		ToolDBLock.canDeleteLock(lock);
	}

	private static boolean canDel(TaAutUser user, JSONObject json) throws Exception {
		Integer 		objId	= ToolData.reqInt (json, "id"		, -1	);	

		TaMsgMessage  	ent	 	= TaMsgMessage.DAO.reqEntityByRef(objId);
		if (ent==null){
			return false;
		}

		if (!canWorkWithObj(user, WORK_DEL, ent)){ //other param after ent...
			return false;
		}		

		//remove all other object connecting to this ent first-------

		TaMsgMessage.DAO.doRemove(ent);		
		return true;
	}

	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	private void doLckReq(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLckReq --------------");		

		Integer 		entId	= ToolData.reqInt	(json, "id", null);	
		TaSysLock 		lock 	= ToolDBLock.reqLock(ENT_TYP, entId, user.reqId(), user.reqStr(TaAutUser.ATT_T_LOGIN_01), null);
		if (lock==null || lock.reqStatus() != TaSysLock.STAT_ACTIVE){
			API.doResponse(response, ToolJSON.reqJSonString(						
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO,
					DefJS.RES_DATA		, lock
					));	
		}else{
			API.doResponse(response, ToolJSON.reqJSonString(						
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, lock
					));	
		}			
	}
	private void doLckDel(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLckDel --------------");

		boolean isDeleted = ToolDBLock.canDeleteLock(json);		
		if (isDeleted)
			API.doResponse(response, DefAPI.API_MSG_OK);		
		else 
			API.doResponse(response, DefAPI.API_MSG_KO);
	}

	private void doLckSav(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLckSav --------------");	
		boolean isLocked 	= ToolDBLock.canExistLock(json);
		if(!isLocked){
			API.doResponse(response, DefAPI.API_MSG_ERR_LOCK);
			return;
		}

		TaMsgMessage  		ent	 	=  reqMod(user, json); 								
		if (ent==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
		} else {				
			API.doResponse(response, ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, ent
					));	
		}
	}


	//user when modify object with lock
	private void doLckEnd(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLckEnd --------------");	
		boolean isLocked 	= ToolDBLock.canExistLock(json);
		if(!isLocked){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}


		TaMsgMessage ent = reqMod(user, json);				
		if (ent==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
		} else {
			ToolDBLock.canDeleteLock(json);
			API.doResponse(response, ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, ent
					));	
		}	
	}


	//---------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	private void doLstDyn(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		Object[]  			dataTableOption = ToolDatatable.reqDataTableOption (json, mapCol);
		List<String> 	searchKey 		= (List<String>)dataTableOption[0];

		//if stat = inbox => get inbox unread and read
		//		if(msgStat.equals(TaMsgMessage.STAT_INT_MSG_INBOX)) {
		//			msgStat += TaMsgMessage.STAT_INT_MSG_INBOX_READ + "," + TaMsgMessage.STAT_INT_MSG_INBOX_UNREAD;
		//		} 

		Set<Integer> 		typs				= ToolData.reqSetInt(json, "typ"	, null); 
		Set<Integer> 		stats				= ToolData.reqSetInt(json, "stat"	, null);
		Criterion			cri 				= reqRestriction(searchKey, typs, stats , user.reqId()  );				
		List<TaMsgMessage> 	mesLst 				= reqMsgMessageListDyn( dataTableOption, cri );


		if(mesLst==null) {
			API.doResponse(response,ToolJSON.reqJSonString(DefJS.SESS_STAT	, 1, DefJS.SV_CODE	, DefAPI.SV_CODE_API_NO));
			return;
		}

		Integer iTotalDisplayRecords 	= reqMsgMessageListDynCount(cri);

		Criterion	cri_All				= reqRestriction(null, typs, stats, user.reqId() );	
		Integer iTotalRecords 			= reqMsgMessageListDynCount(cri_All);				

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,					
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, mesLst //mesLst 
				));
	}
	
	private void doLstNotiByEnt(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLstNotiByEnt --------------");

		ViMsgNotification  	res = reqLstNotiByEnt(user, json); //and other params if necessary
		
		if (res == null){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT				, 1, 
				DefJS.SV_CODE				, DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA				, res));
	}
	
	private ViMsgNotification reqLstNotiByEnt(TaAutUser user,  JSONObject json) throws Exception {
		Integer				begin			= ToolData.reqInt	(json, "begin"		, 0 );
		Integer				nbLine			= ToolData.reqInt	(json, "nbLine"		, 15 );
		Integer				total			= ToolData.reqInt	(json, "total"		, 0 );
		Set<Integer> 		typs			= ToolData.reqSetInt(json, "typs"		, new HashSet<>());
		
		if(typs == null) {
			typs.add(TaMsgMessage.TYPE_01_NOTIFICATION);
		}
		
		ViMsgNotification 	rs		= ViMsgNotification.DAO.reqEntityByValues(
				ViMsgNotification.ATT_I_AUT_USER, user.reqId(),
				ViMsgNotification.ATT_I_TYPE_01	, TaMsgMessage.TYPE_01_NOTIFICATION);
		
		return rs;
	}
	
	private void doMergeNoti(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		Integer			notiId	= ToolData.reqInt	(json, "id" 	, null);
		String			notis	= ToolData.reqStr	(json, "nots"	, null);
		Date			dt		= ToolData.reqDate	(json, "dt"		, null);
		
		if (notis==null || dt==null || notiId==null){
			API.doResponse(response,ToolJSON.reqJSonString(		//filter,
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_NO
					));
			return;
		};
		
		TaMsgMessage 	msg		= TaMsgMessage.DAO.reqEntityByValues(
												TaMsgMessage.ATT_I_ID		, notiId,
												TaMsgMessage.ATT_I_AUT_USER	, user.reqId(),
												TaMsgMessage.ATT_I_TYPE_01	, TaMsgMessage.TYPE_01_NOTIFICATION);
		if(msg == null) {
			API.doResponse(response,ToolJSON.reqJSonString(		//filter,
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_NO
					));
			return;
		};
		
		if (dt.compareTo(msg.reqDate(TaMsgMessage.ATT_D_DATE_01))>=0) {
			msg.reqSet(TaMsgMessage.ATT_T_INFO_05, null);
		}
		msg.reqSet(TaMsgMessage.ATT_T_INFO_04, notis);
		TaMsgMessage.DAO.doMerge(msg);
		
		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, msg
				));
	}

	private void doLstDynManager(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		Object[] 		dataTableOption = ToolDatatable.reqDataTableOption (json, mapCol);
		List<String> 	searchKey 		= (List<String>)dataTableOption[0];

		String 			msgStat			= ToolData.reqStr 	(json, "stat"	, "12");
		String 			msgType			= ToolData.reqStr 	(json, "typ"	, "1");

		//if stat = inbox => get inbox unread and read
		if(msgStat.equals(TaMsgMessage.STAT_MSG_INBOX)) {
			msgStat += TaMsgMessage.STAT_MSG_INBOX_READ + "," + TaMsgMessage.STAT_MSG_INBOX_UNREAD;
		}

		Set<Integer> 		typs				= ToolSet.reqSetInt(msgType);
		Set<Integer> 		stats				= ToolSet.reqSetInt(msgStat);
		Criterion			cri 				= reqRestriction(searchKey, typs, stats ,null );				
		List<TaMsgMessage> 	mesLst 				= reqMsgMessageListDyn( dataTableOption, cri );

		if(mesLst==null) {
			API.doResponse(response,ToolJSON.reqJSonString(DefJS.SESS_STAT	, 1, DefJS.SV_CODE	, DefAPI.SV_CODE_API_NO));
			return;
		}

		Integer iTotalDisplayRecords 	= reqMsgMessageListDynCount(cri);

		Criterion	cri_All				= reqRestriction(null, typs, stats, null );
		Integer iTotalRecords 			= reqMsgMessageListDynCount(cri_All);

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,					
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, mesLst //mesLst 
				));
	}


	private static List<TaMsgMessage> reqMsgMessageListDyn(Object[] dataTableOption, Criterion 	cri) throws Exception {		
		int 		begin 		= (int)(long)	dataTableOption[1];
		int 		number 		= (int)(long)	dataTableOption[2]; 
		int 		sortCol 	= (int)			dataTableOption[3]; 
		int 		sortTyp 	= (int)(long)	dataTableOption[4];	

		List<TaMsgMessage> list 	= new ArrayList<TaMsgMessage>();		

		Order 	order 	= null;			
		String 	colName = null;

		switch(sortCol){
		case 0: 	colName = TaMsgMessage.ATT_I_ID; 				break;
		case 1: 	colName = TaMsgMessage.ATT_I_STATUS; 			break;
		case 4: 												 	break;
		case 5: 												 	break;
		case 6: 	colName = TaMsgMessage.ATT_T_INFO_01; 			break;
		case 7: 	colName = TaMsgMessage.ATT_T_INFO_03; 			break;
		case 8: 	colName = TaMsgMessage.ATT_D_DATE_01; 			break;

		}

		if (colName!=null){
			switch(sortTyp){
			case 0: order = Order.asc (colName); break;
			case 1: order = Order.desc(colName); break;								
			}
		}

		if (order==null)
			list	= TaMsgMessage.DAO.reqList(begin, number, cri);
		else
			list	= TaMsgMessage.DAO.reqList(begin, number, order, cri);			

		return list;
	}


	private static Criterion reqRestriction(List<String> searchKey, Set<Integer> types, Set<Integer> stats, Integer userId) throws Exception {		
		Criterion cri = Restrictions.gt(TaMsgMessage.ATT_I_ID, 0);

		if(userId != null){
			cri =  Restrictions.and(cri, Restrictions.eq(TaMsgMessage.ATT_I_AUT_USER, userId));
		} 
		if(types != null){
			cri = Restrictions.and(cri,Restrictions.in(TaMsgMessage.ATT_I_TYPE_01, types));
		} 
		if(stats != null){
			cri = Restrictions.and(cri,Restrictions.in(TaMsgMessage.ATT_I_STATUS, stats));
		} 

		if (searchKey!=null) {
			for (String s : searchKey){
				if (cri==null)
					cri = 	Restrictions.or(
							Restrictions.ilike(TaMsgMessage.ATT_T_INFO_03		, s), 
							Restrictions.ilike(TaMsgMessage.ATT_T_INFO_04		, s));

				else
					cri = 	Restrictions.and(	cri, 
							Restrictions.or(
									Restrictions.ilike(TaMsgMessage.ATT_T_INFO_03		, s), 
									Restrictions.ilike(TaMsgMessage.ATT_T_INFO_04		, s))
							);
			}
		}

		return cri;
	}


	public static Integer reqMsgMessageListDynCount(Criterion cri) throws Exception {						
		Integer result = TaMsgMessage.DAO.reqCount(cri).intValue();
		return result;
	}

	//---------------------------------------------------------------------------------------------------------
	private static Hashtable<String,Integer> mapCol = new Hashtable<String, Integer>(){
		{
			put("action"	, -1);
			put("id"		, 0 );
			put("stat"		, 1 );

			put("statHistory", 2 );
			put("dtHistory"	 , 3 );

			put("from"		, 6 );
			put("to"		, 7 );
			put("dt"		, 8 );
		}
	};



	private static void doMsgChatDel(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		Integer 	entId 		= ToolData.reqInt (json, "id"	, null);

		if(entId == null) {
			API.doResponse(response,ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_NO					
					));
			return;
		}

		TaMsgMessage 	ent		= reqMsgChatDel		(user, entId);
		if (ent == null){
			API.doResponse(response,ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_NO					
					));
			return;
		}	

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES
				));		

		//lauch websocket end point			
		Integer 	grId		= ent.reqInt(ent, TaMsgMessage.ATT_I_ENTITY_ID);
		JSONObject 	object		= new JSONObject();
		object.put("type"		, ChatMessage.MSG_CHAT_MSG_DEL);
		object.put("msgId"		, entId);
		object.put("grId"		, grId);
		String msgString = object.toJSONString();

		ServiceChatEndpoint.doSendMsgToGroup(grId, msgString);

	}
	private static TaMsgMessage reqMsgChatDel(TaAutUser user, Integer entId) throws Exception {
		//----Integer grpId, grpType, msgDate

		TaMsgMessage msg = TaMsgMessage.DAO.reqEntityByRef(entId);

		if(msg == null)	return null;

		//----co the them buoc tim trong MsgStore
		//----Integer grpId, grpType, msgDate

		if((int)msg.req(TaMsgMessage.ATT_I_AUT_USER) != user.reqId())	return null;


		String body = (String) msg.req(TaMsgMessage.ATT_T_INFO_04);
		String key	= "data-path=\"" + DefAPIExt.API_PATH_URL_DOCBASE;
		if (body.indexOf(key)>0) {
			int 	begin 	= body.indexOf(key)+ key.length();
			String 	path 	= DefAPIExt.API_PATH_DOCUMENT  + body.substring(begin, body.indexOf("\">",begin));
			ToolFile.canDelFile(path);
		}

		TaTpyDocument.doListDel(DefDBExt.ID_TA_MSG_MESSAGE, entId);

		TaMsgMessage.DAO.doRemove(msg);

		return msg;
	}
	private static void doMsgChatHide(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		Integer 	entId 		= ToolData.reqInt (json, "id"	, null);

		if(entId == null) {
			API.doResponse(response,ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_NO					
					));
			return;
		}

		TaMsgMessage 	ent		= reqMsgChatHide		(user, entId);
		if (ent == null){
			API.doResponse(response,ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_NO					
					));
			return;
		}	

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES
				));		

		//lauch websocket end point			
		Integer 	grId		= ent.reqInt(ent, TaMsgMessage.ATT_I_ENTITY_ID);
		JSONObject 	object		= new JSONObject();
		object.put("type"		, ChatMessage.MSG_CHAT_MSG_HIDE);
		object.put("msgId"		, entId);
		object.put("grId"		, grId);
		String msgString = object.toJSONString();

		ServiceChatEndpoint.doSendMsgToUser(user.reqId(), msgString);

	}
	private static TaMsgMessage reqMsgChatHide(TaAutUser user, Integer entId) throws Exception {
		//----Integer grpId, grpType, msgDate

		TaMsgMessage msg = TaMsgMessage.DAO.reqEntityByRef(entId);

		if(msg == null)	return null;

		//----co the them buoc tim trong MsgStore
		//----Integer grpId, grpType, msgDate

		String inf = (String) msg.req(TaMsgMessage.ATT_T_INFO_05);
		//{"files": []}
		JSONObject js = ToolJSON.reqJSonFromString(inf);
		if (js!=null) {
			JSONObject hide = (JSONObject) js.get("hide");
			if (hide==null) {
				hide = new JSONObject();
			}
			hide.put(user.reqId(), 1);
			js.put("hide", hide);
		}
		msg.reqSet(TaMsgMessage.ATT_T_INFO_05, js.toJSONString());
		TaMsgMessage.DAO.doMerge(msg);

		return msg;
	}

	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	private static void doCountTotal(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		Integer 	total		= reqMsgCountTotal		(user, json);
		if (total==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
		}else{				
			API.doResponse(response,ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA	, total
					));		
		}
	}

	private static Integer reqMsgCountTotal(TaAutUser user, JSONObject json) throws Exception {
		Integer		entId		= ToolData.reqInt (json, "id"		, 0); //id of group or person
		Integer 	idUser 		= (int)user.reqRef();

		Criterion 			cri = null;
		//--private

		cri 	= Restrictions.and(	Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_ID	, entId), 
				Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_NSO_GROUP),
				Restrictions.eq(TaMsgMessage.ATT_I_STATUS	    , TaMsgMessage.STAT_MSG_INBOX));

		Integer count	= TaMsgMessage.DAO.reqCount(cri).intValue();


		return count;
	}

	private static void doMsgMemberJoinPublic(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		TaNsoGroupMember 	ent		= reqMsgMemberJoinPublic		(user, json);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
		}else{				
			API.doResponse(response,ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES
					));		
		}
	}

	private static TaNsoGroupMember reqMsgMemberJoinPublic(TaAutUser user, JSONObject json) throws Exception {
		Integer		groupId		= ToolData.reqInt (json, "groupId"		, null); //id of group or person	

		if(groupId == null)	return null;
		Map<String, Object> attr = new HashMap<String, Object>();
		TaNsoGroupMember ent = new TaNsoGroupMember(attr);
		ent.reqSet(TaNsoGroupMember.ATT_I_NSO_GROUP			, groupId);
		ent.reqSet(TaNsoGroupMember.ATT_I_AUT_USER			, user.reqId());
		ent.reqSet(TaNsoGroupMember.ATT_I_STATUS			, TaNsoGroupMember.STAT_ACTIVE);
		ent.reqSet(TaNsoGroupMember.ATT_I_TYPE				, TaNsoGroupMember.TYP_WORKER);
		ent.reqSet(TaNsoGroupMember.ATT_D_DATE_01			, new Date());

		TaNsoGroupMember.DAO.doPersist(ent);
		return ent;
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doLstWaitRead(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		List<ViNsoGroupUnread> 	list = reqLstGroupUnread(user, json); //and other params if necessary
		if (list==null ){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, list 
				));
	}

	private static List<ViNsoGroupUnread> reqLstGroupUnread(TaAutUser user,  JSONObject json, Object...params) throws Exception {
		Integer 			uId  = user.reqId();
		List<ViNsoGroupUnread> lstMsg= ViNsoGroupUnread.reqList(uId);
		return lstMsg;
	}
	
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	private void doNotiCount(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		Integer 	count 	= reqNotiCountNoRead(user);

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, count
				));	
	}

	private static Integer reqNotiCountNoRead(TaAutUser user) throws Exception {
		Integer 	uID 	= user.reqId();
		String 		forPrj 	= "\"parTyp\":" + DefDBExt.ID_TA_PRJ_PROJECT;
		Criterion 	cri 	= Restrictions.and(
				Restrictions.eq(TaMsgMessage.ATT_I_AUT_USER		, uID), 
				Restrictions.eq(TaMsgMessage.ATT_I_TYPE_01		, TaMsgMessage.TYPE_01_NOTIFICATION),
				Restrictions.eq(TaMsgMessage.ATT_I_STATUS		, TaMsgMessage.STAT_NOTI_NEW),
				Restrictions.like(TaMsgMessage.ATT_T_INFO_01	, "%" + forPrj + "%")
				);

		Integer 	count 	= TaMsgMessage.DAO.reqCount(cri).intValue();

		return count;
	}

	private void doNotiLst(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLckEnd --------------");
		Integer 	number 	= ToolData.reqInt	(json, "number"	, 5);
		Integer 	begin 	= ToolData.reqInt	(json, "begin"	, 0);

		Integer 	uID 	= user.reqId();
		
		Criterion 	cri 	= Restrictions.and(
				Restrictions.eq(TaMsgMessage.ATT_I_AUT_USER		, uID), 
				Restrictions.eq(TaMsgMessage.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_PRJ_PROJECT)
				);

		Order ord = Order.desc(TaMsgMessage.ATT_D_DATE_01);

		List<TaMsgMessage> 	lst 	= TaMsgMessage.DAO.reqList(begin, number, ord, cri);

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, lst
				));	
	}

	private void doNotiRead(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLckEnd --------------");
		Integer 	idNotify 	= ToolData.reqInt	(json, "id"			, null);
		boolean 	isReadAll 	= ToolData.reqBool	(json, "isReadAll"	, false);
		Integer 	count 		= 0;
		
		if(idNotify == null  && !isReadAll) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		
		if (isReadAll) {
			Criterion 	cri 	= Restrictions.and(
					Restrictions.eq(TaMsgMessage.ATT_I_AUT_USER		, user.reqId()), 
					Restrictions.eq(TaMsgMessage.ATT_I_TYPE_01		, TaMsgMessage.TYPE_01_NOTIFICATION),
					Restrictions.eq(TaMsgMessage.ATT_I_STATUS		, TaMsgMessage.STAT_NOTI_NEW)
					);
			List<TaMsgMessage> 	lst 	= TaMsgMessage.DAO.reqList(cri);
			if(lst != null && lst.size() > 0) {
				for(TaMsgMessage noti: lst) {
					noti.reqSet(TaMsgMessage.ATT_I_STATUS		, TaMsgMessage.STAT_NOTI_READED);
					noti.reqSet(TaMsgMessage.ATT_D_DATE_02		, new Date());
				}
				TaMsgMessage.DAO.doMerge(lst);
			}
		}else {
			TaMsgMessage noti = TaMsgMessage.DAO.reqEntityByRef(idNotify);
			if (noti==null ||noti.reqInt(TaMsgMessage.ATT_I_AUT_USER)!=user.reqId()) {
				API.doResponse(response, DefAPI.API_MSG_KO);
				return;
			}

			noti.reqSet(TaMsgMessage.ATT_I_STATUS	, TaMsgMessage.STAT_NOTI_READED );
			noti.reqSet(TaMsgMessage.ATT_D_DATE_02	, new Date());

			TaMsgMessage.DAO.doMerge(noti);
			count = reqNotiCountNoRead(user);
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, count
				));	
	}

	private void doNotiDel(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLckEnd --------------");
		JSONArray ids = ToolData.reqJsonArr	(json, "ids", null);
		if(ids == null || ids.size() == 0) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		Set<Integer> setId = new HashSet<Integer>();

		for(int i = 0 ; i< ids.size() ; i++) {
			setId.add((int)(long)ids.get(i));
		}

		List<TaMsgMessage> notifs = TaMsgMessage.DAO.reqList_In(TaMsgMessage.ATT_I_ID, setId, Restrictions.eq(TaMsgMessage.ATT_I_AUT_USER, user.reqId()));

		if(notifs == null || notifs.size() == 0) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		TaMsgMessage.DAO.doRemove(notifs);

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES
				));	
	}
	
	private void doNotiDelAll(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLckEnd --------------");
		try {
			String s = "delete from " + DefDBExt.TA_MSG_MESSAGE + 
					" where " + TaMsgMessage.ATT_I_AUT_USER + "= " + user.reqId() + 
					" and " + TaMsgMessage.ATT_I_TYPE_01 + " = " + TaMsgMessage.TYPE_01_NOTIFICATION;
			TaMsgMessage.DAO.doExecuteSQLs(s);
		}catch(Exception e) {
			e.printStackTrace();
		}
		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES
				));	
	}
	
	//----------------------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	private static void doMessageHistoryNew(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doNew --------------");
		//----------------------------------------------------------------------------------------------------------------------
		TaMsgMessageHistory 			ent		= reqMessageHistoryNew		(user, json);
		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES
				));	

	}

	private static TaMsgMessageHistory reqMessageHistoryNew(TaAutUser user, JSONObject json) throws Exception {
		JSONObject	obj			= ToolData.reqJson(json, "obj"		, null);
		Integer		grpId		= ToolData.reqInt (obj, "group"	, null); //id of group or person	
		Integer		msgId		= ToolData.reqInt (obj, "msg"		, null); //id of group or person	
		Integer		ursId		= user.reqId();
		
		TaMsgMessageHistory  		entDB	 	= TaMsgMessageHistory.DAO.reqEntityByValues(
				TaMsgMessageHistory.ATT_I_NSO_GROUP		, grpId, 
				TaMsgMessageHistory.ATT_I_MSG_MESSAGE	, msgId, 
				TaMsgMessageHistory.ATT_I_AUT_USER		, ursId);
		
		if (entDB != null) return null;

		TaMsgMessageHistory  		ent	 	= new TaMsgMessageHistory(ursId, grpId, msgId, TaMsgMessageHistory.STAT_READ);
		TaMsgMessageHistory.DAO.doPersist(ent);

		JSONObject userRead = new JSONObject();
		userRead.put("user" , ursId);
		userRead.put("idMsg", msgId);

		JSONObject object		= new JSONObject();
		object.put("type"		, ChatMessage.MSG_CHAT_USER_READ ); //"MSG_CHAT_USER_READ");
		object.put("payLoad"	, userRead.toJSONString()); //msg nay em them id user vào để các bên nhận được msg có thể thêm avatar của usẻ này vào
		String msgString = object.toJSONString();

		ServiceChatEndpoint.doSendMsgToGroup(grpId, msgString);

		return ent;
	}
	
	
	private static void doMessageHistoryLst(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");
			
		List<TaMsgMessageHistory> 	list = reqMessageHistoryLst(user, json); //and other params if necessary
		if (list==null || list.size()==0){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		
		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, list
				));	
		
	}
	
	
	private static List<TaMsgMessageHistory> reqMessageHistoryLst(TaAutUser user, JSONObject json) throws Exception {
		JSONObject	obj			= ToolData.reqJson(json, "obj"			, null);
		Integer		grpId		= ToolData.reqInt (obj, "group"		, null); //id of group or person	
				
		Criterion 	cri 		= Restrictions.eq(TaMsgMessageHistory.ATT_I_NSO_GROUP, grpId);	
		Order 		order		= Order.desc(TaMsgMessageHistory.ATT_D_DATE);
		List<TaMsgMessageHistory> 	list 	= TaMsgMessageHistory.DAO.reqList(0, 30, order, cri);;
		
		return list;
	}

}
