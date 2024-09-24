package com.hnv.api.service.priv.prj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hnv.api.def.DefAPI;
import com.hnv.api.def.DefJS;
import com.hnv.api.def.DefTime;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.api.service.common.APIAuth;
import com.hnv.api.service.common.ResultPagination;
import com.hnv.common.tool.ToolDBEntity;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.common.tool.ToolSet;
import com.hnv.common.util.CacheData;
import com.hnv.data.json.JSONArray;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.aut.vi.ViAutUserMember;
import com.hnv.db.prj.TaPrjProject;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.db.tpy.TaTpyInformation;
import com.hnv.db.tpy.TaTpyRelationship;
import com.hnv.def.DefDBExt;	


public class ServicePrjProjectDyn implements IService {

	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 					= "EC_V3".toLowerCase();

	public static final String SV_CLASS 					= "ServicePrjProjectDyn".toLowerCase();

	public static final String SV_LST_PAGE					= "SVLstPage"			.toLowerCase(); 
	public static final String SV_LST_FILTER_DYN			= "SVLstFilterDyn"		.toLowerCase(); 

	public static final String SV_LST_LATE					= "SVLstLate"			.toLowerCase(); 
	public static final String SV_LST_ACTIVITY				= "SVLstActivity"		.toLowerCase();

	public static final String SV_LST_HISTORY_TASK			= "SVLstHistoryTask"		.toLowerCase();
	//-------------------------Default Constructor - Required -------------------------------------
	public ServicePrjProjectDyn(){
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {
			if (sv.equals(SV_LST_PAGE) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLstPage(user,  json, response);
			} else if (sv.equals(SV_LST_FILTER_DYN)){
				doLstFilterDyn(user,  json, response);
			} else if (sv.equals(SV_LST_LATE) 			&&  (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
														||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLstLate(user,  json, response);
			} else if (sv.equals(SV_LST_ACTIVITY)		&&  (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
														||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLstActivity(user,  json, response);
			} else if (sv.equals(SV_LST_HISTORY_TASK)){
				doLstHistoryTask(user,  json, response);
			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}

		}catch(Exception e){
			API.doResponse(response, DefAPI.API_MSG_ERR_API);
			e.printStackTrace();
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------
	private static Hashtable<String,Integer> mapCol = new Hashtable<String, Integer>(){
		{
			put("action"	, -1);
			put("id"		, 0 );
			put("code"		, 1 );
			put("name"		, 2 );
			put("tag"		, 3 );	
			put("lev"		, 4 );
			put("dtEnd"		, 5 );
		}
	};

	private static Object[] reqDataTableOption(String searchKey, int beginDisplay, int nbDisplay, String colN, Integer sortOption){
		Object[] res = new Object[10];


		List<String> keyword 	= new ArrayList<String>();
		if (searchKey!=null && searchKey.length()>0){				
			StringTokenizer token = new StringTokenizer(searchKey, " ");
			while (token.hasMoreTokens()){
				String s = "%" +token.nextToken()+ "%";
				s = s.replace("%+", "");
				s = s.replace("+%", "");
				keyword.add(s.toLowerCase());
			}			
		}else{
			keyword.add("%");
		}

		Integer colToSort= colN==null?null:mapCol.get(colN);
		res[0]		= keyword;
		res[1]		= (int)beginDisplay;
		res[2]		= (int)nbDisplay;
		res[3]		= colToSort;
		res[4]		= sortOption;
		res[5]		= -1;		
		return res;

	}

	//---------------------------------------------------------------------------------------------------------------------------
	private static CacheData<ResultPagination>		cache_rs 			= new CacheData<ResultPagination>	(100, DefTime.TIME_00_30_00_000); //30 minutes if project or epic
	private static CacheData<ResultPagination>		cache_rs_task 		= new CacheData<ResultPagination>	(100, DefTime.TIME_00_30_00_000); //30 s if task
	private static CacheData<ResultPagination>		cache_history 		= new CacheData<ResultPagination>	(100, DefTime.TIME_00_30_00_000); //30 minutes if project or epic
	private static CacheData<ResultPagination>		cache_history_task 	= new CacheData<ResultPagination>	(100, DefTime.TIME_00_30_00_000); //30 minutes if project or epic

	private static void doLstPage(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		ResultPagination  	res = reqLstPage(user, json, response); //and other params if necessary
		if (res.reqList()==null ){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, res
				));	
	}

	//-------------------------------------------------List dynamique filter mat--------------------------------------------------------------------------------------

	private static void doLstFilterDyn(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {	
		ResultPagination  	res = reqLstPage(user, json, response); //and other params if necessary

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, res
				));	
	}

	private static 	ResultPagination reqLstPage(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//		Integer			manId		= ToolData.reqInt	(json, "manId"		, null);
		Integer 		manId 		= user.reqPerManagerId();

		Integer 		uId			= user.reqId();

		//		if (uId.equals(user.reqId()))
		//			if (user.canBeAdmin()) uId	= null; // get all project late from manId if user is admin


		Integer 		begin		= ToolData.reqInt	(json, "begin"		, 0	);
		Integer 		number		= ToolData.reqInt	(json, "number"		, 20);
		Integer 		total		= ToolData.reqInt	(json, "total"		, 0	);
		String 			searchKey   = ToolData.reqStr	(json, "searchKey"	, null);

		String 			sortCol   	= ToolData.reqStr	(json, "sortCol"	, null);
		Integer 		sortDir   	= ToolData.reqInt	(json, "sortDir"	, null);

		Integer			typ00		= ToolData.reqInt	(json, "typ00"		, TaPrjProject.TYP_00_PRJ_SPRINT);
		Integer			typ01		= ToolData.reqInt	(json, "typ01"		, null); 
		Integer			typ02		= ToolData.reqInt	(json, "typ02"		, null); 

		Set<Integer>	stats		= ToolData.reqSetInt(json, "stats"		, null);

		Double			valMin		= ToolData.reqDouble(json, "valMin"		, null);
		Double			valMax		= ToolData.reqDouble(json, "valMax"		, null); 

		Integer			group		= ToolData.reqInt	(json, "group"		, null);

		String 			searchUser  = ToolData.reqStr	(json, "searchUser"	, null);

		Boolean 		wAva 		= ToolData.reqBool	(json, "wAva"	, true);
		Boolean 		wMem 		= ToolData.reqBool	(json, "wMem"	, true);
		Boolean 		wGrp 		= ToolData.reqBool	(json, "wGrp"	, false);
		Boolean 		wParent 	= ToolData.reqBool	(json, "wParent", false);
		
		Boolean 		forced 		= ToolData.reqBool	(json, "forced"	, false);

		Boolean 		isAll 		= ToolData.reqBool	(json, "isAll"		, false);

		if (uId==null) return null;

		if(isAll) uId = null;

		String keyWord 	= manId + "_" + uId + "_" + begin + "_" + number + "_" + total + "_" + searchKey + "_" + sortCol+ "_" + sortDir +"_" + typ00 + typ01+ typ02 +"_" + (stats)+ "_" + valMin+ "_" + valMax + "_" + group + "_" + searchUser + "_" + wAva + "_" + isAll;

		CacheData<ResultPagination> cacheUsed = null;
		if (typ00 == TaPrjProject.TYP_00_PRJ_PROJECT && typ02!=null) {
			switch (typ02) {
			case TaPrjProject.TYP_02_PRJ_MAIN	:
			case TaPrjProject.TYP_02_PRJ_SUB		: cacheUsed =	cache_rs; break;

			case TaPrjProject.TYP_02_PRJ_ELE		: cacheUsed =	cache_rs_task;   break;
			default: cacheUsed =	cache_rs; 
			}
		} else {
			cacheUsed =	cache_rs;
		}

		ResultPagination rs =	null; //cacheUsed.reqData(keyWord);

		if(Boolean.TRUE.equals(forced))	rs = null;

		if(rs==null) {
			Object[] 		dataTableOption 	= reqDataTableOption(searchKey, begin, number, sortCol, sortDir);
			List<String>	sKey				= (List<String>)dataTableOption[0];
			Criterion 		cri 				= TaPrjProject.reqCri(sKey, manId, uId, typ00, typ01, typ02, valMin, valMax, group, stats);
			List<TaPrjProject> prjList 			= reqListFilterDyn(user, dataTableOption, cri, wAva, wMem, wGrp, wParent);
			
			TaPrjProject.doBuildParentForList(prjList);

			if (prjList==null || prjList.isEmpty() ){
				rs								= new ResultPagination(prjList, 0, 0, 0);
			}else {
				if (total == 0 )	total		= reqListDynCount(dataTableOption, cri);
				rs								= new ResultPagination(prjList, total, begin, number);
			}

			cacheUsed.reqPut(keyWord, rs);			
		} else {
			ToolLogServer.doLogInf("---reqViPrjLst use cache-----");
			 
		}

		return rs;

	}
	private static List<TaPrjProject> reqListFilterDyn(TaAutUser user,  Object[] dataTableOption, Criterion cri, boolean wAVatar, boolean wMem, boolean wGrp, boolean wParent) throws Exception {
		int 		begin 		= (int)		dataTableOption[1];
		int 		number 		= (int)		dataTableOption[2]; 
		Integer 	sortCol 	= (Integer)	dataTableOption[3]; 
		Integer 	sortTyp 	= (Integer)	dataTableOption[4];

		String sortColName = null;
		String sortDir	   = null;

		if (sortCol!=null) {
			switch(sortCol) {
			case 0: sortColName = TaPrjProject.ATT_I_ID; break;		
			case 1: sortColName = TaPrjProject.ATT_T_CODE_01; break;
			case 2: sortColName = TaPrjProject.ATT_T_NAME; break;
			case 3: sortColName = TaPrjProject.ATT_T_CODE_02; break;
			case 4: sortColName = TaPrjProject.ATT_I_LEVEL; break;
			case 5: sortColName = TaPrjProject.ATT_D_DATE_04; break;
			default: sortColName = TaPrjProject.ATT_D_DATE_04; break;
			}
		}
		if (sortColName != null) {
			if (sortTyp==null) sortTyp = 0;
			switch(sortTyp) {
			case 0: sortDir = "ASC"; break;
			case 1: sortDir = "DESC"; break;								
			}
		}

		List<TaPrjProject> lst = TaPrjProject.reqListPrjFilter(begin, number,  sortColName, sortDir, cri);

		if (wAVatar) 	TaPrjProject.doBuildAvatarForList(lst);
		if (wMem) 		TaPrjProject.doBuildMemberForList(lst);
		if (wGrp)		TaPrjProject.doBuildGrpMemberForList(lst);
		//---build Project parent info---
		if (wParent)	TaPrjProject.doBuildParentForList(lst);
		
		return lst;
	}

	private static Integer reqListDynCount(Object[] dataTableOption, Criterion cri) throws Exception {
		Integer result = TaPrjProject.reqCountPrjFilter(cri).intValue();
		return result;
	}

	//-------------------------------------------------------------------------------------------------------------------------------------------------
	private static void doLstLate(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		ResultPagination  	res = reqLstLate(user, json, response); //and other params if necessary
		if (res.reqList()==null ){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, res
				));	
	}

	private static 	ResultPagination reqLstLate(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//		Integer			manId		= ToolData.reqInt	(json, "manId"		, null);
		Integer 		manId 		= user.reqPerManagerId();

		Integer 		uId			= ToolData.reqInt	(json, "user_id"		, user.reqId()); //to view by user
		//		if (uId.equals(user.reqId()))
		//			if (user.canBeAdmin()) uId	= null; // get all project late from manId if user is admin

		Integer 		begin		= ToolData.reqInt	(json, "begin"		, 0	);
		Integer 		number		= ToolData.reqInt	(json, "number"		, 20);
		Integer 		total		= ToolData.reqInt	(json, "total"		, 0	);
		String 			searchKey   = ToolData.reqStr	(json, "searchKey"	, null);

		String 			sortCol   	= ToolData.reqStr	(json, "sortCol"		, "id");
		Integer 		sortDir   	= ToolData.reqInt	(json, "sortDir"		, 1);

		Integer			typ00		= ToolData.reqInt	(json, "typ01"		, TaPrjProject.TYP_00_PRJ_PROJECT); 
		Integer			typ01		= ToolData.reqInt	(json, "typ01"		, null); 
		Integer			typ02		= ToolData.reqInt	(json, "typ02"		, null); 

		if (uId==null) return null;

		String keyWord 	= manId + "_" + uId + "_" + begin + "_" + number + "_" + total + "_" + searchKey + "_" + sortCol+ "_" + sortDir +"_" + typ00+"_"+typ01+ "_" + typ02 ;

		CacheData<ResultPagination> cacheUsed = null;		
		switch (typ02) {
		case TaPrjProject.TYP_02_PRJ_MAIN:
		case TaPrjProject.TYP_02_PRJ_SUB: cacheUsed =	cache_rs; break;
		case TaPrjProject.TYP_02_PRJ_ELE: cacheUsed =	cache_rs_task;   break;
		default: cacheUsed =	cache_rs; 
		}

		ResultPagination rs =	cacheUsed.reqData(keyWord);

		//for test;
		//		if(typ02 == TaPrjProject.TYP02_PRJ_TASK)	rs = null;

		if(rs==null) {
			Object[] dataTableOption 	= reqDataTableOption(searchKey, begin, number, sortCol, sortDir);
			List<TaPrjProject> prjList 	= reqListLate(dataTableOption, manId,  uId, typ00, typ01, typ02);


			if (prjList==null || prjList.size() ==0 ){
				rs								= new ResultPagination(prjList, 0, 0, 0);
			}else {
				TaPrjProject.doBuildAvatarForList(prjList);
				TaPrjProject.doBuildMemberForList(prjList);

				if (total == 0 )	total		= reqFilterListLateCount(dataTableOption, manId, uId, typ00, typ01, typ02);
				rs								= new ResultPagination(prjList, total, begin, number);
			}

			cacheUsed.reqPut(keyWord, rs);			
		} else {
			 
		}

		return rs;

	}


	private static List<TaPrjProject> reqListLate(Object[] dataTableOption, Integer manId, Integer uId, Integer typ00, Integer typ01, Integer typ02) throws Exception {
		List<String>			searchKey				= (List<String>)dataTableOption[0];
		int 		begin 		= (int)	dataTableOption[1];
		int 		number 		= (int)	dataTableOption[2]; 
		int 		sortCol 	= (int)	dataTableOption[3]; 
		int 		sortTyp 	= (int)	dataTableOption[4];

		String sortColName = null;
		String sortDir	   = null;

		switch(sortCol) {
		case 0: sortColName = TaPrjProject.ATT_I_ID; break;		
		case 1: sortColName = TaPrjProject.ATT_T_CODE_01; break;
		case 3: sortColName = TaPrjProject.ATT_T_NAME; break;
		case 4: sortColName = TaPrjProject.ATT_T_CODE_02; break;
		default: sortColName = TaPrjProject.ATT_I_ID; break;
		}

		if (sortColName != null) {
			switch(sortTyp) {
			case 0: sortDir = "ASC"; break;
			case 1: sortDir = "DESC"; break;								
			}
		}

		List<TaPrjProject> lst = TaPrjProject.reqListPrjLate(begin, number,  sortColName, sortDir, searchKey, manId, uId, typ00, typ01, typ02);
		return lst;
	}

	private static Integer reqFilterListLateCount(Object[] dataTableOption, Integer manId, Integer uId, Integer typ00, Integer typ01, Integer typ02) throws Exception {
		List<String>	searchKey				= (List<String>)dataTableOption[0];
		Integer result = TaPrjProject.reqCountPrjLate(searchKey, manId , uId, typ00, typ01, typ02).intValue();
		return result;
	}

	//-------------------------------------------------------------------------------------------------------------------------------------------------
	private static void doLstActivity(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		ResultPagination  	res = reqLstActivity(user, json, response); //and other params if necessary
		if (res.reqList()==null ){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, res
				));	
	}

	private static 	ResultPagination reqLstActivity(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//			Integer			manId		= ToolData.reqInt	(json, "manId"		, null);
		Integer 		manId 		= user.reqPerManagerId();
		Integer 		uId			= ToolData.reqInt	(json, "user_id"		, user.reqId()); //to view by user
		//			if (uId.equals(user.reqId()))
		//				if (user.canBeAdmin()) uId	= null; // get all project late from manId if user is admin

		Integer 		begin		= ToolData.reqInt	(json, "begin"		, 0	);
		Integer 		number		= ToolData.reqInt	(json, "number"		, 10);
		Integer 		total		= ToolData.reqInt	(json, "total"		, 0	);
		String 			searchKey   = ToolData.reqStr	(json, "searchKey"	, null);

		String 			sortCol   	= ToolData.reqStr	(json, "sortCol"		, "id");
		Integer 		sortDir   	= ToolData.reqInt	(json, "sortDir"		, 1);


		if (uId==null) return null;

		String keyWord 	= manId + "_" + uId + "_" + begin + "_" + number + "_" + total + "_" + searchKey + "_" + sortCol+ "_" + sortDir;

		CacheData<ResultPagination> cacheUsed 	= cache_history;		
		ResultPagination 			rs 			= cacheUsed.reqData(keyWord);

		if(rs==null) {
			Object[] dataTableOption 	= reqDataTableOption(searchKey, begin, number, sortCol, sortDir);
			List<TaTpyInformation> prjList 	= reqListActivity(dataTableOption, manId,  uId);

			if (prjList==null || prjList.size() ==0 ){
				rs								= new ResultPagination(prjList, 0, 0, 0);
			}else {
				if (total == 0 )	total		= reqFilterListActivityCount(dataTableOption, manId, uId);
				rs								= new ResultPagination(prjList, total, begin, number);
			}

			cacheUsed.reqPut(keyWord, rs);			
		} else {
			 
		}

		return rs;
	}

	private static List<TaTpyInformation> reqListActivity(Object[] dataTableOption, Integer manId, Integer uId) throws Exception {
		List<String>			searchKey				= (List<String>)dataTableOption[0];
		int 		begin 		= (int)	dataTableOption[1];
		int 		number 		= (int)	dataTableOption[2]; 
		int 		sortCol 	= (int)	dataTableOption[3]; 
		int 		sortTyp 	= (int)	dataTableOption[4];

		String sortColName = null;
		String sortDir	   = null;

		switch(sortCol) {
		case 0: sortColName = TaTpyInformation.ATT_D_DATE_01; break;		
		case 1: sortColName = TaTpyInformation.ATT_I_ID; break;
		case 2: sortColName = TaTpyInformation.ATT_T_INFO_01; break;
		default: sortColName = TaTpyInformation.ATT_D_DATE_01; break;
		}

		if (sortColName != null) {
			switch(sortTyp) {
			case 0: sortDir = "ASC"; break;
			case 1: sortDir = "DESC"; break;								
			}
		}

		Criterion cri = Restrictions.and(
				Restrictions.eq(TaTpyInformation.ATT_I_AUT_USER_01, uId),
				Restrictions.eq(TaTpyInformation.ATT_I_ENTITY_TYPE, DefDBExt.ID_TA_PRJ_PROJECT)
				);

		List<TaTpyInformation> result = null;

		Order order = null;
		if (sortColName!=null && sortDir!=null )
			if 		(sortDir.equals("DESC")) order = Order.desc(sortColName);
			else if (sortDir.equals("ASC"))  order = Order.asc(sortColName);

		//		if (order!=null)
		//			result = TaTpyInformation.DAO.reqList(begin, number, order, cri);
		//		else 
		//			result = TaTpyInformation.DAO.reqList(begin, number, cri);

		result = TaTpyInformation.DAO.reqList(begin, number, Order.desc(TaTpyInformation.ATT_D_DATE_01), cri);

		return result;
	}

	private static Integer reqFilterListActivityCount(Object[] dataTableOption, Integer manId, Integer uId) throws Exception {
		List<String>	searchKey				= (List<String>)dataTableOption[0];
		Criterion cri = Restrictions.and(
				Restrictions.eq(TaTpyInformation.ATT_I_AUT_USER_01, uId),
				Restrictions.eq(TaTpyInformation.ATT_I_ENTITY_TYPE, DefDBExt.ID_TA_PRJ_PROJECT)
				);
		Integer result = TaTpyInformation.DAO.reqCount(cri).intValue();
		return result;
	}















	//-------------------------------------------------------------------------------------------------------------------------------------------------
	private static void doLstHistoryTask(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		ResultPagination  	res = reqViPrjLstHistoryTask(user, json, response); //and other params if necessary
		if (res.reqList()==null ){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, res
				));	
	}

	private static 	ResultPagination reqViPrjLstHistoryTask(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		Integer 		taskId		= ToolData.reqInt	(json, "taskId"		, null); //to view by user
		//			if (uId.equals(user.reqId()))
		//				if (user.canBeAdmin()) uId	= null; // get all project late from manId if user is admin

		Integer 		begin		= ToolData.reqInt	(json, "begin"		, 0	);
		Integer 		number		= ToolData.reqInt	(json, "number"		, 20);
		Integer 		total		= ToolData.reqInt	(json, "total"		, 0	);
		String 			searchKey   = ToolData.reqStr	(json, "searchKey"	, null);

		String 			sortCol   	= ToolData.reqStr	(json, "sortCol"		, "id");
		Integer 		sortDir   	= ToolData.reqInt	(json, "sortDir"		, 1);

		if (taskId==null) return null;

		String keyWord 	= taskId + "_" + begin + "_" + number + "_" + total + "_" + searchKey + "_" + sortCol+ "_" + sortDir;

		CacheData<ResultPagination> cacheUsed 	= cache_history_task;		
		ResultPagination 			rs 			= cacheUsed.reqData(keyWord);

		if(rs==null) {
			Object[] dataTableOption 		= reqDataTableOption(searchKey, begin, number, sortCol, sortDir);
			List<TaTpyInformation> his 		= reqPrjProjectListHistoryTask(dataTableOption, taskId);

			if (his==null || his.size() ==0 ){
				rs								= new ResultPagination(his, 0, 0, 0);
			}else {
				
				//---build users + their avatars
				List<ViAutUserMember> lstUsers = TaAutUser		.reqBuildUserMember	(his, TaTpyInformation.ATT_I_AUT_USER_01, TaTpyInformation.ATT_O_AUT_USER_01);
				TaTpyDocument	.reqBuildAvatar(lstUsers, DefDBExt.ID_TA_AUT_USER, ViAutUserMember.ATT_O_AVATAR);
				
				if (total == 0 )	total		= reqPrjFilterListHistoryTaskCount(dataTableOption, taskId);
				rs								= new ResultPagination(his, total, begin, number);
			}

			cacheUsed.reqPut(keyWord, rs);			
		} else {
			 
		}

		return rs;
	}

	private static List<TaTpyInformation> reqPrjProjectListHistoryTask(Object[] dataTableOption, Integer taskId) throws Exception {
		List<String>			searchKey				= (List<String>)dataTableOption[0];
		int 		begin 		= (int)	dataTableOption[1];
		int 		number 		= (int)	dataTableOption[2]; 
		int 		sortCol 	= (int)	dataTableOption[3]; 
		int 		sortTyp 	= (int)	dataTableOption[4];

		String sortColName = null;
		String sortDir	   = null;

		switch(sortCol) {
		case 0: sortColName = TaTpyInformation.ATT_I_ID; break;		
		case 1: sortColName = TaTpyInformation.ATT_D_DATE_01; break;
		case 2: sortColName = TaTpyInformation.ATT_T_INFO_01; break;
		default: sortColName = TaTpyInformation.ATT_I_ID; break;
		}

		if (sortColName != null) {
			switch(sortTyp) {
			case 0: sortDir = "DESC"; break;
			case 1: sortDir = "ASC"; break;								
			}
		}

		Criterion cri = Restrictions.and(
				Restrictions.eq(TaTpyInformation.ATT_I_ENTITY_ID, taskId),
				Restrictions.eq(TaTpyInformation.ATT_I_ENTITY_TYPE, DefDBExt.ID_TA_PRJ_PROJECT)
				);

		List<TaTpyInformation> result = null;

		Order order = null;
		if (sortColName!=null && sortDir!=null )
			if 		(sortDir.equals("DESC")) order = Order.desc(sortColName);
			else if (sortDir.equals("ASC"))  order = Order.asc(sortColName);

		if (order!=null)
			result = TaTpyInformation.DAO.reqList(begin, number, order, cri);
		else 
			result = TaTpyInformation.DAO.reqList(begin, number, cri);

		return result;
	}

	private static Integer reqPrjFilterListHistoryTaskCount(Object[] dataTableOption, Integer taskId) throws Exception {
		List<String>	searchKey				= (List<String>)dataTableOption[0];
		Criterion cri = Restrictions.and(
				Restrictions.eq(TaTpyInformation.ATT_I_ENTITY_ID, taskId),
				Restrictions.eq(TaTpyInformation.ATT_I_ENTITY_TYPE, DefDBExt.ID_TA_PRJ_PROJECT)
				);
		Integer result = TaTpyInformation.DAO.reqCount(cri).intValue();
		return result;
	}

}
