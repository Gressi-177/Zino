package com.hnv.api.service.priv.msg;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.hnv.api.def.DefAPI;
import com.hnv.api.def.DefJS;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.common.tool.ToolDBLock;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.msg.TaMsgMessageHistory;
import com.hnv.db.sys.TaSysLock;
import com.hnv.def.DefDBExt;


/**
 * ----- ServiceMsgMessageHistory by H&V
 * ----- Copyright 2017------------
 */
public class ServiceMsgMessageHistory implements IService {

	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 					= "EC_V3".toLowerCase();

	public static final String SV_CLASS 					= "ServiceMsgMessageHistory".toLowerCase();	
	public static final Integer	ENT_TYP			= DefDBExt.ID_TA_MSG_MESSAGE_HISTORY;
	public static final String SV_DO_GET 		= "SVGet".toLowerCase();	
	public static final String SV_DO_LST 		= "SVLst".toLowerCase();

	public static final String SV_DO_NEW 		= "SVNew".toLowerCase();	
	public static final String SV_DO_MOD 		= "SVMod".toLowerCase();	
	public static final String SV_DO_DEL 		= "SVDel".toLowerCase();

	public static final String SV_DO_LCK_REQ 	= "SVLckReq".toLowerCase(); //req or refresh	
	public static final String SV_DO_LCK_SAV 	= "SVLckSav".toLowerCase(); //save and continue
	public static final String SV_DO_LCK_END 	= "SVLckEnd".toLowerCase();
	public static final String SV_DO_LCK_DEL 	= "SVLckDel".toLowerCase();

	//-----------------------------------------------------------------------------------------------
	//-------------------------Default Constructor - Required -------------------------------------
	public ServiceMsgMessageHistory(){
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {


			if(sv.equals(SV_DO_GET)){
				doGet(user, json, response);
			} else if(sv.equals(SV_DO_LST)){
				doLst(user, json, response);

			} else if(sv.equals(SV_DO_NEW)){
				doNew(user, json, response);
			} else if(sv.equals(SV_DO_MOD)){
				doMod(user, json, response);
			} else  if(sv.equals(SV_DO_DEL)){
				doDel(user, json, response);

			} else if(sv.equals(SV_DO_LCK_REQ)){
				doLckReq(user, json, response);
			} else if(sv.equals(SV_DO_LCK_SAV)){
				doLckSav(user, json, response);
			} else if(sv.equals(SV_DO_LCK_END)){
				doLckEnd(user, json, response);
			} else if(sv.equals(SV_DO_LCK_DEL)){
				doLckDel(user, json, response);

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

		Integer 			objId	= ToolData.reqInt	(json, "id"		, -1	);				
		Boolean				forced	= ToolData.reqBool	(json, "forced"	, false	);

		TaMsgMessageHistory 		ent 	= TaMsgMessageHistory.DAO.reqEntityByRef(objId, forced);

		if (ent==null){
			API.doResponse(response,ToolJSON.reqJSonString(DefJS.SESS_STAT	, 1, DefJS.SV_CODE	, DefAPI.SV_CODE_API_NO));
			return;
		}


		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, true, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, ent 
				));

	}

	//---------------------------------------------------------------------------------------------------------
	private static void doLst(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {

		List<TaMsgMessageHistory> 	list = doLst(user, json); //and other params if necessary
		if (list==null ){
			API.doResponse(response,ToolJSON.reqJSonString(DefJS.SESS_STAT	, 1, DefJS.SV_CODE	, DefAPI.SV_CODE_API_NO));
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, true, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, list 
				));
	}

	private static Criterion reqCriterion(Object...params) throws Exception{
		if (params==null || params.length==0) return null;

		Criterion cri = Restrictions.gt("I_ID", 0);		

		if (params!=null && params.length>0){
			//int type 	= (int) params[0];
			//cri 		= Restrictions.and(cri, Restrictions.eqOrIsNull(TaMsgMessageHistory.ATT_I_TYPE, type));
		}			

		if (params!=null && params.length>1){
			//do something
		}

		return cri;
	}

	private static List<TaMsgMessageHistory> doLst(TaAutUser user,  JSONObject json, Object...params) throws Exception {
		Integer 			objTyp	= ToolData.reqInt	(json, "typ"		, -1	);
		//other params here

		if (!canWorkWithObj(user, WORK_LST, objTyp)){ //other param after objTyp...
			return null;
		}

		Criterion 			cri		= reqCriterion (objTyp); //and other params	
		List<TaMsgMessageHistory> 	list 	= null;
		if (cri==null) 
			list =   TaMsgMessageHistory.DAO.reqList();
		else
			list =   TaMsgMessageHistory.DAO.reqList(cri);


		if (params!=null){
			//do something with list before return
		}	
		//do something else with request

		return list;
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doNew(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {

		TaMsgMessageHistory 			ent		= doNew		(user, json);
		if (ent==null){
			API.doResponse(response,ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, true, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_NO					
					));
		}else{				
			API.doResponse(response,ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, true, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA	, ent
					));				
		}
	}


	private static TaMsgMessageHistory doNew(TaAutUser user, JSONObject json) throws Exception {
		JSONObject				obj		= ToolData.reqJson 		(json, "obj", null);

		if (!canWorkWithObj(user, WORK_NEW, obj)){ //other param after obj...
			return null;
		}

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaMsgMessageHistory.class);
		TaMsgMessageHistory  		ent	 	= new TaMsgMessageHistory(attr);
		TaMsgMessageHistory.DAO.doPersist(ent);

		//build other objects from obj and request

		return ent;
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doMod(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doMod --------------");

		TaMsgMessageHistory		ent		= reqMod(user, json); 								
		if (ent==null){
			API.doResponse(response,ToolJSON.reqJSonString(DefJS.SESS_STAT	, 1, DefJS.SV_CODE	, DefAPI.SV_CODE_API_NO));
		} else {
			API.doResponse(response,ToolJSON.reqJSonString(DefJS.SESS_STAT	, 1, DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES));
		}				
	}

	private static TaMsgMessageHistory reqMod(TaAutUser user, JSONObject json) throws Exception {
		JSONObject			obj		= ToolData.reqJson 		(json, "obj", null);

		int 				objId	= (int)obj.get("id");	
		TaMsgMessageHistory  		ent	 	= TaMsgMessageHistory.DAO.reqEntityByRef(objId);
		if (ent==null){
			return ent;
		}


		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaMsgMessageHistory.class);		
		TaMsgMessageHistory.DAO.doMerge(ent, attr);	

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
		Integer 		objId	= ToolData.reqInt	(json, "id"		, -1	);	

		TaMsgMessageHistory  	ent	 	= TaMsgMessageHistory.DAO.reqEntityByRef(objId);
		if (ent==null){
			return false;
		}

		if (!canWorkWithObj(user, WORK_DEL, ent)){ //other param after ent...
			return false;
		}		

		//remove all other object connecting to this ent first-------

		TaMsgMessageHistory.DAO.doRemove(ent);		
		return true;
	}

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

		TaMsgMessageHistory  		ent	 	=  reqMod(user, json); 								
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


		TaMsgMessageHistory ent = reqMod(user, json);				
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

}
