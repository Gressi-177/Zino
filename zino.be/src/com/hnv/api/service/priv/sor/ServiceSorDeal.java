package com.hnv.api.service.priv.sor;


import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.hnv.api.def.DefAPI;
import com.hnv.api.def.DefDB;
import com.hnv.api.def.DefJS;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.api.service.common.APIAuth;
import com.hnv.common.tool.ToolDBLock;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolDatatable;
import com.hnv.common.tool.ToolDate;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.nso.TaNsoPost;
import com.hnv.db.sor.TaSorDeal;
import com.hnv.db.sor.TaSorOrder;
import com.hnv.db.sys.TaSysLock;
import com.hnv.def.DefDBExt;
import com.hnv.def.DefRight;


/**
 * ----- ServiceSorDeal by H&V
 * ----- Copyright 2017------------
 */
public class ServiceSorDeal implements IService {
	private static	String 			filePath		= null; 
	private	static	String 			urlPath			= null; 
	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 				= "HNV".toLowerCase();

	public static final String SV_CLASS 				= "ServiceTaSorDeal".toLowerCase();	

	//NEW CODE------------------------------------------------------------------------------------------------------------
	public static final String SV_GET					= "SVGet"	.toLowerCase();
	public static final String SV_LST					= "SVLst"	.toLowerCase();
	public static final String SV_LST_DYN				= "SVLstDyn"	.toLowerCase();
	
	public static final String SV_NEW					= "SVNew"	.toLowerCase();
	public static final String SV_MOD					= "SVMod"	.toLowerCase();
	public static final String SV_DEL					= "SVDel"	.toLowerCase();
	
	public static final String SV_GET_BY_CODE			= "SVGetByCode"	.toLowerCase();
	
	
	public static final String SV_LCK_REQ				= "SVLckReq"	.toLowerCase();
	public static final String SV_LCK_SAV				= "SVLckSav"	.toLowerCase();
	public static final String SV_LCK_END				= "SVLckEnd"	.toLowerCase();
	public static final String SV_LCK_DEL				= "SVLckDel"	.toLowerCase();
	
	public static final Integer	ENT_TYP					= DefDBExt.ID_TA_SOR_DEAL;
	//-----------------------------------------------------------------------------------------------
	//-------------------------Default Constructor - Required -------------------------------------
	public ServiceSorDeal(){
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		//ToolLogServer.doLogInf("--------- "+ SV_CLASS+ ".doService --------------");
		if (filePath	==null) filePath		= API.reqContextParameter("INV_INVOICE_PATH_FILE");
		if (urlPath		==null) urlPath			= API.reqContextParameter("INV_INVOICE_PATH_URL");			


		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {
			
			if(sv.equals(SV_GET)						&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doGet(user, json, response);
			} else if(sv.equals(SV_GET_BY_CODE)			&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doGetByCode(user, json, response);
			
			} else if(sv.equals(SV_LST_DYN)				&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doLstDyn(user, json, response);
			} else if(sv.equals(SV_LST)					&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doLst(user, json, response);
			
			} else if(sv.equals(SV_NEW)					&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doNew(user, json, response);
			} else if(sv.equals(SV_MOD)					&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doMod(user, json, response);
			} else if(sv.equals(SV_DEL)					&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doDel(user, json, response);
			
			
			} else if(sv.equals(SV_LCK_REQ)				&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doLckReq(user, json, response);
			} else if(sv.equals(SV_LCK_SAV)				&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doLckSav(user, json, response);
			} else if(sv.equals(SV_LCK_END)				&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doLckEnd(user, json, response);
			} else if(sv.equals(SV_LCK_DEL)				&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doLckDel(user, json, response);
			
			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}	
		}catch(Exception e){
			try {
				API.doResponse(response, DefAPI.API_MSG_ERR_API);
				e.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}


	}
	
	//--------------------------------------------------------------------------------------------------------
	
	
	// done
	private void doGet(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		Integer 			objId	= Integer.parseInt(json.get("id").toString());
		
		TaSorDeal 			ent 	= TaSorDeal.DAO.reqEntityByValue(TaSorDeal.ATT_I_ID, objId);

		if (ent==null){
			API.doResponse(response, ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_NO));
			return;
		}

		ent.doBuildUsers();
		ent.doBuildManagerName();
		
		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent 
				));

	}
	
	
	private void doGetByCode(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		String 				code	= json.get("code").toString();
		
		TaSorDeal 			ent 	= TaSorDeal.reqDealByCode(code);

		if (ent==null){
			API.doResponse(response, ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_NO));
			return;
		}

		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent 
				));

	}
	
	// DONE
	private static void doLst(TaAutUser user, JSONObject json,  HttpServletResponse response) throws Exception  {
		List<TaSorDeal> 	list = TaSorDeal.DAO.reqList();
		
		if (list==null ){
			API.doResponse(response, ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_NO));
			return;
		}

		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, list 
				));
	}
	
	
	private static void doNew(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		TaSorDeal 			ent		= reqNew		(user, json);
		if (ent==null){
			API.doResponse(response, ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO					
					));
			return;
		}

		TaSysLock 	lock 	= ToolDBLock.reqLock (json, "lock", DefDB.DB_LOCK_NEW, ENT_TYP, (Integer)ent.req(TaNsoPost.ATT_I_ID), user.reqId(), user.reqStr(TaAutUser.ATT_T_LOGIN_01), null);
		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent,
				"lock"				, lock
				));
	}

	public static TaSorDeal reqNew(TaAutUser user, JSONObject json) throws Exception {
		String 			str 	= (String) json.get("obj");
		JSONObject 		obj 	= ToolJSON.reqJSonFromString(str);	
	

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaSorDeal.class);
		TaSorDeal  			deal	= new TaSorDeal(attr);
		
		Integer 			idMan 	= user.reqPerManagerId();	
		
		String 				code	= deal.reqStr(deal, TaSorDeal.ATT_T_CODE_01);
		TaSorDeal 			d		= TaSorDeal.reqDealByCode(code);
		if (d!=null) {
			code = code + "-" + ToolDate.reqString(new Date(), "HHmmss");
		}

		deal.reqSet(TaSorDeal.ATT_T_CODE_01, 			code);
		deal.reqSet(TaSorDeal.ATT_D_DATE_01, 			new Date());
		deal.reqSet(TaSorDeal.ATT_D_DATE_02, 			new Date());
		deal.reqSet(TaSorDeal.ATT_I_AUT_USER_01, 		user.req(TaAutUser.ATT_I_ID));
		deal.reqSet(TaSorDeal.ATT_I_AUT_USER_02, 		user.req(TaAutUser.ATT_I_ID));
		deal.reqSet(TaSorDeal.ATT_I_PER_MANAGER, 		idMan);
		
		if (deal.req(TaSorDeal.ATT_F_VAL_00) == null) deal.reqSet(TaSorDeal.ATT_F_VAL_00, 0.0);
		if (deal.req(TaSorDeal.ATT_F_VAL_01) == null) deal.reqSet(TaSorDeal.ATT_F_VAL_01, 0.0);
		if (deal.req(TaSorDeal.ATT_F_VAL_02) == null) deal.reqSet(TaSorDeal.ATT_F_VAL_02, 0.0);
		
		TaSorDeal.DAO.doPersist(deal);
		
		return deal;		
	}
	
	//---------------------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------------------
	private static void doMod(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doMod --------------");

		TaSorDeal  		ent	 	=  reqMod(user, json); 								
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
	private static TaSorDeal reqMod(TaAutUser user, JSONObject json) throws Exception {
		JSONObject			obj		= ToolData.reqJson (json, "obj"	, null);
		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaSorDeal.class);
		
		Integer				id		= (int) attr.get(TaSorOrder.ATT_I_ID);
		TaSorDeal  			deal	 = TaSorDeal.DAO.reqEntityByRef(id);

		if (deal==null) return null;
		
		
		attr.remove	(TaSorDeal.ATT_I_AUT_USER_01);
		attr.remove	(TaSorDeal.ATT_I_PER_MANAGER);
		attr.remove	(TaSorDeal.ATT_D_DATE_01);
		
		attr.put	(TaSorDeal.ATT_D_DATE_02, 			new Date());
		attr.put	(TaSorDeal.ATT_I_AUT_USER_02, 		user.reqId());
		
		if (attr.get(TaSorDeal.ATT_F_VAL_00) == null) attr.put(TaSorDeal.ATT_F_VAL_00, 0.0);
		if (attr.get(TaSorDeal.ATT_F_VAL_01) == null) attr.put(TaSorDeal.ATT_F_VAL_01, 0.0);
		if (attr.get(TaSorDeal.ATT_F_VAL_02) == null) attr.put(TaSorDeal.ATT_F_VAL_02, 0.0);
		
		TaSorDeal.DAO.doMerge(deal, attr);
		return deal;		
	}	
	//---------------------------------------------------------------------------------------------------------------------
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

		boolean					ok		= canDel(user, json);
		if (!ok){
			API.doResponse(response,DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response, ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_YES));
		}

		ToolDBLock.canDeleteLock(lock);
	}
	
	private static boolean canDel(TaAutUser user, JSONObject json) throws Exception {
		Integer			objId	= ToolData.reqInt	(json, "stat"	, null);
		TaSorDeal  		ent	 	= TaSorDeal.DAO.reqEntityByRef(objId);
		
		if (ent==null){
			return false;
		}

		TaSorDeal.DAO.doRemove(ent);	
		
		return true;
	}
	  // -------------------------------------------------------------------------------
	private static Hashtable<String,Integer> mapCol = new Hashtable<String, Integer>(){
	    {
	    	put("action", -1);
	    	put("code01", 0 );
	    	put("dt01", 1 );
	    	put("dt02", 2 );
	    	put("stat", 3 );
	    }
	};
	private void doLstDyn(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		Object[]  			dataTableOption = ToolDatatable.reqDataTableOption (json, mapCol);
		Set<String>		searchKey		= (Set<String>)dataTableOption[0];	
		Integer				stat			= ToolData.reqInt	(json, "stat"	, null);
		Integer				typ				= ToolData.reqInt	(json, "type"	, null);
		Set<Integer>		typMulti		= ToolData.reqSetInt(json, "typMulti", null);
		Integer 			manId			= (Integer)user.reqPerManagerId();

		List<TaSorDeal> 	stockIOOrderList = null;
		stockIOOrderList 	= reqListDyn(dataTableOption, manId, null, null, stat );
		// ----------------------------------

		if (stockIOOrderList==null ){
			API.doResponse(response, ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_NO));
			return;
		}

		Integer iTotalRecords 			= reqListDynCountTotal	(dataTableOption, manId, null, null, stat);				
		Integer iTotalDisplayRecords 	= reqListDynCount		(dataTableOption, manId, null, null, stat);

		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,					
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, stockIOOrderList
				));
	}
	
	private static Integer reqListDynCountTotal(Object[] dataTableOption, Integer manId, Integer typ01, Integer typ02, Integer stat) throws Exception {
		Integer result = TaSorDeal.reqListCount(manId, typ01, typ02, stat, null);
		return result;
	}
	private static Integer reqListDynCount(Object[] dataTableOption, Integer manId, Integer typ01, Integer typ02, Integer stat) throws Exception {
		Set<String>			searchKey				= (Set<String>)dataTableOption[0];
		Integer result = TaSorDeal.reqListCount(manId, typ01, typ02, stat, searchKey);
		return result;
	}
	private static List<TaSorDeal> reqListDyn(Object[] dataTableOption,  Integer idMan,  Integer typ01, Integer typ02, Integer stat ) throws Exception {
		Set<String>			searchKey				= (Set<String>)dataTableOption[0];
		
		int 		begin 		= (int)	dataTableOption[1];
		int 		number 		= (int)	dataTableOption[2]; 
		int 		sortCol 	= (int)	dataTableOption[3]; 
		int 		sortTyp 	= (int)	dataTableOption[4];

		String sortColName = TaSorDeal.ATT_T_CODE_01;
		String sortDir	   = null;

		switch(sortCol) {
			case 0: sortColName = TaSorDeal.ATT_T_CODE_01; break;
			case 1: sortColName = TaSorDeal.ATT_D_DATE_02; break;
			case 2: sortColName = TaSorDeal.ATT_D_DATE_03; break;
		}

		if (sortColName != null) {
			switch(sortTyp) {
			case 0: sortDir = " ASC"; break;
			case 1: sortDir = " DESC"; break;								
			}
		}
        
		List<TaSorDeal> lst = TaSorDeal.reqList(begin, number, idMan, typ01, typ02, stat, searchKey, sortColName, sortDir);
        return lst;
	}
	
	
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
				API.doResponse(response, ToolJSON.reqJSonString(		
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES
					));		
			else 
				API.doResponse(response, ToolJSON.reqJSonString(		
						DefJS.SESS_STAT		, 1, 
						DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO
						));
		}
		
		private void doLckSav(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
			//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLckSav --------------");	
			boolean isLocked 	= ToolDBLock.canExistLock(json);
			if(!isLocked){
				API.doResponse(response, DefAPI.API_MSG_ERR_LOCK);
				return;
			}
			
			TaSorDeal ent		=  reqMod(user, json); 								
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
				API.doResponse(response, DefAPI.API_MSG_ERR_LOCK);
				return;
			}
			
			TaSorDeal ent		= reqMod(user, json);						
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
	
	
	
	
	// 
	
	//---------------------------------------------------------------------------------------------------------

	private static final int WORK_GET 	= 1;
	private static final int WORK_LST 	= 2;
	private static final int WORK_NEW 	= 3;
	private static final int WORK_MOD 	= 4;
	private static final int WORK_DEL 	= 5;
	private static final int WORK_UPL 	= 10; //upload

	private static boolean canWorkWithObj ( TaAutUser user, int typWork, Object...params){
		if (params==null|| params.length==0 || user==null) return false;
		//int userType	= (int) user.req(TaAutUser.ATT_I_TYPE);
		//if (userType==TYPE_ADM_ALL) return true;

		switch(typWork){

		case WORK_MOD : 
			return true;
		case WORK_GET : 
			return true;
		case WORK_DEL :
			return true;
		case WORK_LST : 
			return true;
		case WORK_NEW : 
			//check something with params
			return true;

		}

		return false;
	}


}
