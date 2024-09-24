package com.hnv.api.service.priv.nso;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hnv.api.def.DefAPI;
import com.hnv.api.def.DefDB;
import com.hnv.api.def.DefJS;
import com.hnv.api.def.DefTime;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.api.service.common.APIAuth;
import com.hnv.api.service.common.ResultPagination;
import com.hnv.common.tool.ToolDBLock;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolDatatable;
import com.hnv.common.tool.ToolDate;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.common.tool.ToolSet;
import com.hnv.common.util.CacheData;
import com.hnv.data.json.JSONArray;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.aut.vi.ViAutUserDyn;
import com.hnv.db.aut.vi.ViAutUserMember;
import com.hnv.db.nso.TaNsoGroup;
import com.hnv.db.nso.TaNsoGroupMember;
import com.hnv.db.nso.TaNsoOffer;
import com.hnv.db.nso.TaNsoPost;
import com.hnv.db.nso.vi.ViNsoGroupMember;
import com.hnv.db.prj.TaPrjProject;
import com.hnv.db.sys.TaSysLock;
import com.hnv.db.tpy.TaTpyCategoryEntity;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.db.tpy.TaTpyRelationship;
import com.hnv.def.DefDBExt;

/**
 * ----- ServiceNsoGroup by H&V
 * ----- Copyright 2017------------
 */
public class ServiceNsoGroup implements IService {
	private static	String 			filePath	= null; 
	private	static	String 			urlPath		= null; 


	public static final Integer 	NEW_CONTINUE 			= 1;	
	public static final Integer 	NEW_EXIT 				= 2;
	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 					= "EC_V3".toLowerCase();

	public static final String SV_CLASS 					= "ServiceNsoGroup".toLowerCase();	

	public static final String SV_GET 			= "SVGet".toLowerCase();	
	public static final String SV_LST 			= "SVLst".toLowerCase();
	public static final String SV_LST_SEARCH	= "SVLstSearch".toLowerCase(); 
	public static final String SV_LST_DYN		= "SVLstDyn".toLowerCase(); 

	public static final String SV_NEW 			= "SVNew".toLowerCase();	
	public static final String SV_MOD 			= "SVMod".toLowerCase();	
	public static final String SV_DEL 			= "SVDel".toLowerCase();

	public static final String SV_LCK_REQ 		= "SVLckReq".toLowerCase(); //req or refresh	
	public static final String SV_LCK_SAV 		= "SVLckSav".toLowerCase(); //save and continue
	public static final String SV_LCK_END 		= "SVLckEnd".toLowerCase();
	public static final String SV_LCK_DEL 		= "SVLckDel".toLowerCase();
	
	public static final String SV_GET_MEMBER 		= "SVGetMember".toLowerCase();
	public static final String SV_MOD_STAT_MEMBER 	= "SVModStatMember".toLowerCase();
	

	public static final String SV_NEW_WORK 	= "SVNewWork".toLowerCase();
	public static final String SV_MOD_WORK 	= "SVModWork".toLowerCase();
	public static final String SV_DEL_WORK 	= "SVDelWork".toLowerCase();

	public static final String SV_WORK_SAVE_MEMBER = "SVWorkSaveMember".toLowerCase();

	public static final String SV_SAVE_NEWSLETTER  = "SVSaveNewsletter".toLowerCase();
	
	
	public static final Integer ENT_TYP       = DefDBExt.ID_TA_NSO_GROUP;
	//-----------------------------------------------------------------------------------------------
	//-------------------------Default Constructor - Required -------------------------------------
	public ServiceNsoGroup() {
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		String    sv   = API.reqSVFunctName(json);
		TaAutUser user = (TaAutUser) json.get("userInfo");
		try {
			if(sv.equals(SV_GET) 				&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_AUT_USER_GET)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGet(user, json, response);
			} else if(sv.equals(SV_LST)			&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_AUT_USER_GET)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLst(user, json, response);
			} else if(sv.equals(SV_LST_SEARCH)	&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_AUT_USER_GET)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLstSearch(user, json, response);

			} else if(sv.equals(SV_LST_DYN)		&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_AUT_USER_GET)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLstDyn(user, json, response);

			} else if(sv.equals(SV_NEW)			&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_NEW, APIAuth.R_AUT_USER_NEW)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doNew(user, json, response);
			} else if(sv.equals(SV_MOD)			&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_AUT_USER_MOD)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doMod(user, json, response);
			} else if(sv.equals(SV_DEL)		&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_DEL, APIAuth.R_AUT_USER_DEL)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doDel(user, json, response);

			} else if(sv.equals(SV_LCK_REQ)	&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_AUT_USER_MOD)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLckReq(user, json, response);
			} else if(sv.equals(SV_LCK_SAV)	&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_AUT_USER_MOD)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLckSav(user, json, response);
			} else if(sv.equals(SV_LCK_END)	&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_AUT_USER_MOD)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLckEnd(user, json, response);
			} else if(sv.equals(SV_LCK_DEL)	&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_AUT_USER_MOD)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLckDel(user, json, response);		
			
			} else if(sv.equals(SV_GET_MEMBER)	&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_AUT_USER_GET)
						||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGetMembers(user, json, response);
			} else if(sv.equals(SV_MOD_STAT_MEMBER)	&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_AUT_USER_MOD)
					||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doModStatMember(user, json, response);		
			
				//----------------group for project---------------------------------------------------------------
			} else if(sv.equals(SV_NEW_WORK)	&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_NEW, APIAuth.R_AUT_USER_NEW)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doWorkNew(user, json, response);
			} else if(sv.equals(SV_MOD_WORK)	&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_AUT_USER_MOD)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doWorkMod(user, json, response);
			} else if(sv.equals(SV_DEL_WORK)	&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_DEL, APIAuth.R_AUT_USER_DEL)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doDelWork(user, json, response);				
			} else if(sv.equals(SV_WORK_SAVE_MEMBER)	&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_NEW, APIAuth.R_AUT_USER_NEW)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGroupWorkSaveMember(user, json, response);	

			} else if(sv.equals(SV_SAVE_NEWSLETTER)	&& (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_NEW, APIAuth.R_AUT_USER_NEW)
												||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGroupSaveNewsletter(user, json, response);	

			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}	

		}catch(Exception e){
			API.doResponse(response, DefAPI.API_MSG_ERR_API);
			e.printStackTrace();
			return;
		}			
	}
	//---------------------------------------------------------------------------------------------------------

	private static final int WORK_GET 	= 1;
	private static final int WORK_LST 	= 2;
	private static final int WORK_NEW 	= 3;
	private static final int WORK_MOD 	= 4;
	private static final int WORK_DEL 	= 5;
	private static final int WORK_UPL 	= 10; //upload

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
		case WORK_UPL : 
			//check something with params
			return true;
		}
		return false;
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doGet(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");

		Integer 			objId	= ToolData.reqInt	(json, "id"		, -1	);				
		Boolean				forced	= ToolData.reqBool	(json, "forced"	, false	);

		TaNsoGroup 		ent 	= reqGet(objId, forced);

		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		if (!canWorkWithObj(user, WORK_GET, ent)){
			API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			return;
		}

		API.doResponse(response	, ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, ent));
	}
	private static TaNsoGroup reqGet(Integer matId, Boolean forced) throws Exception{
		TaNsoGroup 		ent 	= TaNsoGroup.DAO.reqEntityByRef(matId, forced);

		ent.doBuildMembers	(false);
		ent.doBuildDocuments(false);

		//---do build something other of ent like details....

		return ent;
	}
	//---------------------------------------------------------------------------------------------------------
	private static void doLst(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");

		List<TaNsoGroup> 	list = reqLst(user, json); //and other params if necessary
		if (list==null ){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		API.doResponse(response	, ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, list));
	}

	private static List<TaNsoGroup> reqLst(TaAutUser user, JSONObject json, Object...params) throws Exception {
		Integer 			nbLine      = ToolData.reqInt		(json, "nbLine" 	, 10);
		String 				searchkey	= ToolData.reqStr		(json, "searchkey"	, null);
		Integer 			manId		= ToolData.reqInt		(json, "manId"		, null);
		Set<Integer> 		stat01		= ToolData.reqSetInt	(json, "stat01s"	, null);
		Set<Integer> 		typ01		= ToolData.reqSetInt	(json, "typ01s"		, null);
		Boolean				wAvatar		= ToolData.reqBool		(json, "wAvatar"	, false);
		Boolean 			objContBuild= ToolData.reqBool		(json, "withBuild"	, false);

		//other params here

		if (!canWorkWithObj(user, WORK_LST, typ01)){ //other param after objTyp...
			return null;
		}

		List<TaNsoGroup> 		list 	= null;
//		List<TaNsoGroupMember> 	lstGr	= ViNsoGroupMember.reqIDByUser(user.reqId());
//		Set<Integer> 			ids 	= ToolSet.reqSetInt(lstGr, TaNsoGroupMember.ATT_I_NSO_GROUP);
		

//		Criterion 				cri		= reqRestriction (searchkey, manId, stat01, typ01, ids);
		Criterion 				cri		= reqRestriction (searchkey, manId, stat01, typ01, null);
		
		list = TaNsoGroup.DAO.reqList(0, nbLine, cri);;
		if(list == null) return null;
		//		Hashtable<Integer, EntityAbstract> tabs = HashTool.reqHashtableKeyInt(lstGr, TaNsoGroupMember.ATT_I_NSO_GROUP);

		//		if (cri==null) 
		//			list =   TaNsoGroup.DAO.reqList();
		//		else
		//			list =   TaNsoGroup.DAO.reqList(cri);
		//		
		//		
		//		if (params!=null){
		//			//do something with list before return
		//		}	
		//		
		//		//do something else with request
		//		if (objContBuild){
		//			
		//		}
		
		if (wAvatar) 		TaTpyDocument	.reqBuildAvatar(list, DefDBExt.ID_TA_NSO_GROUP, TaNsoGroup.ATT_O_AVATAR);

		return list;
	}
	
	private static Criterion reqRestriction(String searchKey, Integer manId, Set<Integer> stats, Set<Integer> typ, Set<Integer> ids) throws Exception {	
		//--Pre-Check condition---------------------------------------------------
		if (stats == null){
			stats = new HashSet<>() ; 
			stats.add(TaNsoGroup.STAT_01_ACTIVE);
		}
				
		Criterion cri = Restrictions.in(TaNsoGroup.COL_I_STATUS_01, stats);
		
		if(typ!=null) {
			cri = Restrictions.and(	cri, Restrictions.in(TaNsoGroup.ATT_I_TYPE_01 , typ));
		}

		if (searchKey!=null) {
			searchKey = '%' + searchKey + '%';
			cri = Restrictions.and(	cri, Restrictions.or(
					Restrictions.ilike(TaNsoGroup.ATT_T_NAME, searchKey),
					Restrictions.ilike(TaNsoGroup.ATT_T_REF, searchKey),
					Restrictions.ilike(TaNsoGroup.ATT_T_INFO_01, searchKey),
					Restrictions.ilike(TaNsoGroup.ATT_T_INFO_02, searchKey))
			);
		}
		
		if(ids != null) {
			cri = Restrictions.and(cri , Restrictions.in(TaNsoGroup.ATT_I_ID, ids));
		}
		
		if (manId!=null) cri = Restrictions.and(cri, Restrictions.eq(ViAutUserDyn.ATT_I_PER_MANAGER, manId));
		return cri;
	}

	private static Criterion reqCriterion(Object...params) throws Exception{
		if (params==null || params.length==0) return null;

		Criterion cri = Restrictions.gt("I_ID", 0);	

		if (params!=null && params.length>0){
			//int type 	= (int) params[0];
			//cri 		= Restrictions.and(cri, Restrictions.eqOrIsNull(TaNsoGroup.ATT_I_TYPE, type));
		}			

		if (params!=null && params.length>1){
			//do something
		}

		return cri;
	}
	
	//--------------------------------------------------------------------------------------------
	private static CacheData<ResultPagination> cacheEnt_rs = new CacheData<ResultPagination>(100, DefTime.TIME_02_00_00_000);
	
	private static void doLstSearch(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {	
		ResultPagination  	res = reqLstSearch(user, json); //and other params if necessary
		
		if (res.reqList()==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT				, 1, 
				DefJS.SV_CODE				, DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA				, res));
	}
	
	public static ResultPagination reqLstSearch(TaAutUser user, JSONObject json) throws Exception {
		String				searchKey		= ToolData.reqStr		(json, "searchKey"	, null);
		Integer 			begin			= ToolData.reqInt		(json, "begin"		, 0	);
		Integer 			number			= ToolData.reqInt		(json, "number"		, 10); 
		Integer 			total			= ToolData.reqInt		(json, "total"		, 0	);
		
		Set<Integer>		stats			= ToolData.reqSetInt	(json, "stats"		, null);
		Set<Integer>		typ01s			= ToolData.reqSetInt	(json, "typ01s"		, null);
		Boolean				hardLoad		= ToolData.reqBool		(json, "hardLoad"	, false);
		
		String 				keyWord 		= stats.toString() + "_" + typ01s.toString();
		ResultPagination 	rs 				= null;
		boolean				addCache		= true;
		
		if (!canWorkWithObj(user, WORK_LST, null, stats)){ //other param after objTyp...
			return null;
		}
		
		if(stats == null || stats.size() <= 0 || typ01s == null) {
			rs			= cacheEnt_rs.reqData(keyWord);
		} else {
			addCache = false;
		}
		
		//-------------------------------------------------------------------
		if(rs == null || hardLoad) {
			Criterion 	cri 		= reqRestrictionSearch(user, searchKey, stats, typ01s);				

			List<TaNsoGroup> list 	= TaNsoGroup.DAO.reqList(begin, number, cri);
			
			if (total == 0 ) {
				total				= TaNsoGroup.DAO.reqCount().intValue();
			}
			rs						= new ResultPagination(list, total, begin, number);
			
			if (addCache) cacheEnt_rs.reqPut(keyWord, rs);
		} else {
			ToolLogServer.doLogInf("---reqListSearch use cache-----");
			cacheEnt_rs.reqCheckIfOld(keyWord); //cache in 2 hour
		}

		return rs;
	}
	
	private static Criterion reqRestrictionSearch(TaAutUser user, String searchKey, Set<Integer> stats, Set<Integer> typ01s) {
		if (stats == null){
			stats = new HashSet<Integer>() ; 
			stats.add(TaNsoGroup.STAT_01_ACTIVE);
		}
				
		Criterion cri 	= Restrictions.in(TaNsoGroup.ATT_I_STATUS_01, stats);
		cri 			= Restrictions.and(cri, 
							Restrictions.in(TaNsoGroup.ATT_I_TYPE_01, typ01s));

		if (searchKey!=null) {
			searchKey = '%' + searchKey + '%';
				cri = Restrictions.and(	cri, Restrictions.or(
						Restrictions.ilike(TaNsoGroup.ATT_T_NAME, searchKey), 
						Restrictions.ilike(TaNsoGroup.ATT_T_INFO_01, searchKey),
						Restrictions.ilike(TaNsoGroup.ATT_T_INFO_02, searchKey))
				);
		}
		
		cri = Restrictions.and(cri, Restrictions.eq(ViAutUserDyn.ATT_I_PER_MANAGER, user.reqPerManagerId()));
		return cri;
	}

	//---------------------------------------------------------------------------------------------------------		
	private static Hashtable<String, Integer> mapCol = new Hashtable<String, Integer>() {
		{
			put("action", -1);
			put("id", 0);
		}
	};
	private static Long countAllEle = null;
	private static void doLstDyn(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {	
		Object[]     		dataTableOption = ToolDatatable.reqDataTableOption(json, mapCol);		
		Set<String>			searchKey		= (Set<String>)dataTableOption[0];	
		Set<Integer>		typ01			= ToolData.reqSetInt(json, "typ01"	, null);
		Set<Integer>		typ02			= ToolData.reqSetInt(json, "typ02"	, null);
		Set<Integer>		stat01			= ToolData.reqSetInt(json, "stat01" , null);
		Set<Integer>		stat02			= ToolData.reqSetInt(json, "stat02" , null);

		if (!canWorkWithObj(user, WORK_LST, null, typ01, typ02)){ //other param after objTyp...
			API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			return;
		}

		Criterion 	cri 			= reqRestriction(searchKey, typ01, typ02, stat01, stat02);				


		List<TaNsoGroup> list 		= reqListDyn(dataTableOption, cri);		
		if (list==null ){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		if (countAllEle==null)
			countAllEle = ((long)reqNbNsoGroupListDyn());

		Integer iTotalRecords 			= (countAllEle.intValue());				
		Integer iTotalDisplayRecords 	= reqNbNsoGroupListDyn(cri).intValue();


		API.doResponse(response	, ToolJSON.reqJSonString(
				// filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES, 				
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, list
				));

	}

	private static Object[] reqDataTableOption(HttpServletRequest request){
		/*{"sEcho":3,"iColumns":2,"sColumns":",",
		"iDisplayStart":0,	"iDisplayLength":10,
		"mDataProp_0":"code","sSearch_0":"","bRegex_0":false,"bSearchable_0":true,"bSortable_0":true,
		"mDataProp_1":"name","sSearch_1":"","bRegex_1":false,"bSearchable_1":true,"bSortable_1":true,
		"sSearch":"f","bRegex":false,
		"iSortCol_0":0,"sSortDir_0":"desc","iSortingCols":1}]
		 */

		Object[] res = new Object[5];

		String dataTableParam	= request.getParameter("dataTableParam");				
		JSONObject dataTableParams = null;
		if (dataTableParam!=null){
			dataTableParams = ToolJSON.reqJSonFromString(dataTableParam);	
		}	

		List<String> keyword 	= new ArrayList<String>();
		String searchKey = (String) dataTableParams.get("sSearch");
		if (searchKey!=null && searchKey.length()>0){				
			StringTokenizer token = new StringTokenizer(searchKey, " ");
			while (token.hasMoreTokens()){
				String s = "%" +token.nextToken()+ "%";
				s = s.replace("%$", "");
				s = s.replace("$%", "");
				keyword.add(s.toLowerCase());
			}			
		}else{
			keyword.add("%");
		}

		long beginDisplay 	= (long)dataTableParams.get("iDisplayStart");
		long nbDisplay		= (long)dataTableParams.get("iDisplayLength");

		long  colToSort		= (long)dataTableParams.get("iSortCol_0");				
		long  sortOption	= 0;
		String tmp			= (String) dataTableParams.get("sSortDir_0");
		if(tmp.equals("desc")) sortOption = 1;

		res[0]		= keyword;
		res[1]		= beginDisplay;
		res[2]		= nbDisplay;
		res[3]		= colToSort;
		res[4]		= sortOption;
		return res;
	}

	private static Criterion reqRestriction(Set<String> searchKey, Set<Integer> typ01, Set<Integer> typ02, Set<Integer> stat01, Set<Integer> stat02) throws Exception {		
		Criterion cri = null;

		for (String s : searchKey){
			if (cri==null)
				cri = 	Restrictions.or(
						Restrictions.ilike(TaNsoGroup.ATT_T_NAME		, s), 
						Restrictions.ilike(TaNsoGroup.ATT_T_REF		    , s));

			else
				cri = 	Restrictions.and(	cri, 
						Restrictions.or(
								Restrictions.ilike(TaNsoGroup.ATT_T_NAME		, s), 
								Restrictions.ilike(TaNsoGroup.ATT_T_REF  		, s))
						);
		}	

		if (cri==null) {
			cri = Restrictions.isNotNull(TaNsoGroup.ATT_I_ID);
		}
		
		if(typ01 != null){
			cri = Restrictions.and(cri, Restrictions.in(TaNsoGroup.ATT_I_TYPE_01, typ01));
		} 
		if(typ02 != null){			
			cri = Restrictions.and(cri, Restrictions.in(TaNsoGroup.ATT_I_TYPE_02, typ02));			
		} 
		
		if(stat01 != null){
			cri = Restrictions.and(cri,Restrictions.in(TaNsoPost.ATT_I_STATUS_01, stat01));
		}
		
		if(stat02 != null){
			cri = Restrictions.and(cri,Restrictions.in(TaNsoPost.ATT_I_STATUS_02, stat02));
		}

		return cri;
	}
	private static List<TaNsoGroup> reqListDyn(Object[] dataTableOption, Criterion 	cri) throws Exception {		
		int 		begin 		= (int)	dataTableOption[1];
		int 		number 		= (int)	dataTableOption[2]; 
		int 		sortCol 	= (int)	dataTableOption[3]; 
		int 		sortTyp 	= (int)	dataTableOption[4];	

		List<TaNsoGroup> list 	= new ArrayList<TaNsoGroup>();		

		Order 	order 	= null;			
		String 	colName = null;

		switch(sortCol){
		case 0: colName = TaNsoGroup.ATT_I_ID; break;		
		//case 1: colName = TaNsoGroup.ATT_T_NAME; break;					
		}

		if (colName!=null){
			switch(sortTyp){
			case 0: order = Order.asc (colName); break;
			case 1: order = Order.desc(colName); break;								
			}
		}

		if (order==null)
			list	= TaNsoGroup.DAO.reqList(begin, number, cri);
		else
			list	= TaNsoGroup.DAO.reqList(begin, number, order, cri);			

		return list;
	}

	private static Number reqNbNsoGroupListDyn() throws Exception {						
		return TaNsoGroup.DAO.reqCount();		
	}

	private static Number reqNbNsoGroupListDyn(Criterion cri) throws Exception {			
		return TaNsoGroup.DAO.reqCount(cri);
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doNew(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		TaNsoGroup ent = reqNew(user, json);
		if (ent == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		TaSysLock lock = ToolDBLock.reqLock(json, 
				"lock"		, DefDB.DB_LOCK_NEW, 
				ENT_TYP		, ent.reqId(), 
				user.reqId(), user.reqStr(TaAutUser.ATT_T_LOGIN_01), null);
		
		API.doResponse(response	, ToolJSON.reqJSonString( // filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA	, ent, "lock", lock));
	}
	

	private static TaNsoGroup reqNew(TaAutUser user, JSONObject json) throws Exception {
		JSONObject          obj  = ToolData.reqJson(json, "obj", null);
		

		if (!canWorkWithObj(user, WORK_NEW, obj)){ //other param after obj...
			return null;
		}

		Map<String, Object> attr = API.reqMapParamsByClass(obj, TaNsoGroup.class);
		attr.put(TaNsoGroup.ATT_D_DATE_01		, new Date());
		attr.put(TaNsoGroup.ATT_I_PER_MANAGER	, user.reqPerManagerId());
		attr.put(TaNsoGroup.ATT_I_AUT_USER		, user.reqId());
		
		TaNsoGroup  		ent	 	= new TaNsoGroup(attr);
		TaNsoGroup.DAO.doPersist(ent);
		
		int					entId 	= ent.reqId();
		/*build list member if existing*/
		JSONArray		members		= ToolData.reqJsonArr(json, "member", null);
		ent.reqSet(TaNsoGroup.ATT_O_MEMBERS, TaNsoGroupMember.reqListNew(entId, members));

		
		JSONArray		docs		= (JSONArray) obj.get("files");
		ent.reqSet(TaNsoOffer.ATT_O_DOCUMENTS, TaTpyDocument.reqListCheck(DefAPI.SV_MODE_NEW, user, ENT_TYP, entId, docs));


		return ent;
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doMod(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doMod --------------");

		TaNsoGroup  		ent	 	=  reqMod(user, json); 								
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		
		API.doResponse(response	, ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, ent));
	}

	private static TaNsoGroup reqMod(TaAutUser user, JSONObject json) throws Exception {
		JSONObject			obj		= ToolData.reqJson	(json, "obj", null);

		int 				entId	= Integer.parseInt(obj.get("id").toString());	
		TaNsoGroup  		ent	 	= TaNsoGroup.DAO.reqEntityByRef(entId);
		if (ent==null){
			return null;
		}

		if (!canWorkWithObj(user, WORK_MOD, ent)){ //other param after obj...
			return null;
		}

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaNsoGroup.class);		
		TaNsoGroup.DAO.doMerge(ent, attr);	

		//merge other objects from obj and request
		JSONArray		members		= ToolData.reqJsonArr(json, "member", null);
		ent.reqSet(TaNsoGroup.ATT_O_MEMBERS, TaNsoGroupMember.reqListMod(entId, members));

		JSONArray 		docs 	= (JSONArray) obj.get("files");
		ent.reqSet(TaNsoOffer.ATT_O_DOCUMENTS, TaTpyDocument.reqListCheck(DefAPI.SV_MODE_MOD, user, ENT_TYP, entId, docs));
		
		return ent;
	}	

	//---------------------------------------------------------------------------------------------------------
	private static void doDel(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		// ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doDel --------------");

		int       entId = ToolData.reqInt(json, "id", null);
		TaSysLock lock  = ToolDBLock.reqLock(ENT_TYP, entId, user.reqId(), user.reqStr(TaAutUser.ATT_T_LOGIN_01), null);
		if (lock == null || lock.reqStatus() == 0) {
			API.doResponse(response	, ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_NO,
					DefJS.RES_DATA	, lock));
			return;
		}

		if (!canDel(user, json)) {
			API.doResponse(response, DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response, DefAPI.API_MSG_OK);
		}

		ToolDBLock.canDeleteLock(lock);
	}
	private static boolean canDel(TaAutUser user, JSONObject json) throws Exception {
		Integer    entId = ToolData.reqInt(json, "id", null);
		TaNsoGroup ent   = TaNsoGroup.DAO.reqEntityByRef(entId);
		if (ent == null) {
			return false;
		}

		Session sess = TaNsoGroup.DAO.reqSessionCurrent();
		try {
			TaTpyDocument		.doListDel(sess, ENT_TYP, ent.reqId());
			TaTpyCategoryEntity	.doListDel(sess, ENT_TYP, ent.reqId());
			
			TaNsoGroupMember	.doListDel(sess, entId);
			TaNsoGroup.DAO		.doRemove(sess, ent);
			
			TaNsoGroup.DAO		.doSessionCommit(sess);
		}catch (Exception e){
			if (sess!=null) TaNsoGroup.DAO.doSessionRollback(sess);
			return false;
		}

		return true;
	}
	
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	private void doLckReq(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		// ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLckReq --------------");

		Integer   entId = ToolData.reqInt(json, "id", null);
		TaSysLock lock  = ToolDBLock.reqLock(ENT_TYP, entId, user.reqId(), user.reqStr(TaAutUser.ATT_T_LOGIN_01), null);

		if (lock == null || lock.reqStatus() == 0) {
			API.doResponse(response	, ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_NO,
					DefJS.RES_DATA	, lock));
		} else {
			API.doResponse(response	, ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA	, lock));
		}
	}

	private void doLckDel(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		// ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLckDel --------------");

		if (ToolDBLock.canDeleteLock(json))
			API.doResponse(response, DefAPI.API_MSG_OK);
		else
			API.doResponse(response, DefAPI.API_MSG_KO);

	}

	private void doLckSav(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		// ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLckSav --------------");
		boolean isLocked = ToolDBLock.canExistLock(json);
		if (!isLocked) {
			API.doResponse(response, DefAPI.API_MSG_ERR_LOCK);
			return;
		}

		TaNsoGroup ent = reqMod(user, json);
		if (ent == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response	, ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA	, ent));
		}
	}

	// user when modify object with lock
	private void doLckEnd(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		// ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLckEnd --------------");
		boolean isLocked = ToolDBLock.canExistLock(json);
		if (!isLocked) {
			API.doResponse(response, DefAPI.API_MSG_ERR_LOCK);
			return;
		}

		TaNsoGroup ent = reqMod(user, json);
		if (ent == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
		} else {
			ToolDBLock.canDeleteLock(json);
			API.doResponse(response	, ToolJSON.reqJSonString(
					DefJS.SESS_STAT	, 1, 
					DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA	, ent));
		}
	}

	//---------------------------------------------------------------------------------------------------------
	private void doGetMembers(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		Integer grpId = ToolData.reqInt	(json, "id", null);
		
		if(grpId == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		
		List<TaNsoGroupMember> mem = TaNsoGroupMember.DAO.reqList(Restrictions.and(
				Restrictions.eq(TaNsoGroupMember.COL_I_NSO_GROUP, grpId)));
		
		if(mem.size() > 0) {
			List<ViAutUserMember> lstUsers = TaAutUser		.reqBuildUserMember	(mem, TaNsoGroupMember.ATT_I_AUT_USER, TaNsoGroupMember.ATT_O_MEMBER);
			TaTpyDocument	.reqBuildAvatar(lstUsers, DefDBExt.ID_TA_AUT_USER, ViAutUserMember.ATT_O_AVATAR);
		}

		API.doResponse(response	, ToolJSON.reqJSonString(
				// filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, mem
				));
	}
	
	private void doModStatMember(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		Integer grpId 	= ToolData.reqInt	(json, "id", null);
		Integer stat 	= ToolData.reqInt	(json, "stat", null);
		
		if(grpId == null || stat == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		
		TaNsoGroupMember mem = TaNsoGroupMember.DAO.reqEntityByValues(
				TaNsoGroupMember.COL_I_NSO_GROUP, grpId, TaNsoGroupMember.ATT_I_AUT_USER, user.reqId());
		
		if(mem == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		
		mem.reqSet(TaNsoGroupMember.ATT_I_STATUS, stat);
		mem.reqSet(TaNsoGroupMember.ATT_D_DATE_02, new Date());
		
		TaNsoGroupMember.DAO.doMerge(mem);
		
		API.doResponse(response	, ToolJSON.reqJSonString(
				// filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES
				));
	}
	
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	private static void doWorkNew(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {

		TaNsoGroup ent = reqNewGroupWork(user, json);

		if(ent == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response	, ToolJSON.reqJSonString(
				// filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, ent
				));

	}

	private static TaNsoGroup reqNewGroupWork(TaAutUser user, JSONObject json) throws Exception {
		JSONObject obj = ToolData.reqJson	(json, "obj", null);
		if(obj == null)	return null;

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaNsoGroup.class);

		TaNsoGroup ent 		= new TaNsoGroup(attr);
		Integer type01 		= ent.reqInt(TaNsoGroup.ATT_I_TYPE_01);
		Integer stat01 		= ent.reqInt(TaNsoGroup.ATT_I_STATUS_01);
		if(type01==null)
			ent.reqSet(TaNsoGroup.ATT_I_TYPE_01		, TaNsoGroup.TYP_01_WORK);
		if(stat01==null)
			ent.reqSet(TaNsoGroup.ATT_I_STATUS_01	, TaNsoGroup.STAT_01_ACTIVE);
		ent.reqSet(TaNsoGroup.ATT_D_DATE_01		, new Date());
		ent.reqSet(TaNsoGroup.ATT_I_PER_MANAGER	, user.reqPerManagerId());
		ent.reqSet(TaNsoGroup.ATT_T_REF	, "GRP_" + ToolDate.reqString(new Date(), "yyMMddHHmmss"));
		TaNsoGroup.DAO.doPersist(ent);
		
		int				entId 		= ent.reqId();
		//----add user creating to manager
		Map<String, Object> mem 	= new HashMap<String, Object>();
		TaNsoGroupMember 	memG 	= new TaNsoGroupMember(mem);
		memG.reqSet(TaNsoGroupMember.ATT_I_NSO_GROUP	, ent.reqRef());
		memG.reqSet(TaNsoGroupMember.ATT_I_AUT_USER		, user.reqId());
		memG.reqSet(TaNsoGroupMember.ATT_I_STATUS		, TaNsoGroupMember.STAT_ACTIVE);
		memG.reqSet(TaNsoGroupMember.ATT_I_TYPE			, TaNsoGroupMember.TYP_MANAGER);
		memG.reqSet(TaNsoGroupMember.ATT_D_DATE_01		, new Date());
		TaNsoGroupMember.DAO.doPersist(memG);

		JSONArray		docs		= (JSONArray) obj.get("files");
		ent.reqSet(TaNsoGroup.ATT_O_DOCUMENTS, TaTpyDocument.reqListCheck(DefAPI.SV_MODE_NEW, user, ENT_TYP, entId, docs));
		
		//----copy path avatar to T_Val_01
		ent.doBuilAvatarPath();
		TaNsoGroup.DAO.doMerge(ent);
		return ent;
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doWorkMod(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {

		TaNsoGroup ent = reqModGroupWork(user, json);

		if(ent == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response	, ToolJSON.reqJSonString(
				// filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, ent
				));

	}

	private static TaNsoGroup reqModGroupWork(TaAutUser user, JSONObject json) throws Exception {
		JSONObject obj = ToolData.reqJson	(json, "obj", null);
		if(obj == null)	return null;
		
		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaNsoGroup.class);
		attr.remove	(TaNsoGroup.ATT_T_VAL_01);
		attr.remove	(TaNsoGroup.ATT_T_REF);
		attr.remove	(TaNsoGroup.ATT_D_DATE_01);
		attr.put	(TaNsoGroup.ATT_D_DATE_02, new Date());
		
		int 				objId	= Integer.parseInt(obj.get("id").toString());
		TaNsoGroup  		ent	 	= TaNsoGroup.DAO.reqEntityByRef(objId);
		if (ent==null){
			return null;
		}
		
		TaNsoGroup.DAO.doMerge(ent, attr);	

		int				entId 		= ent.reqId();
		
		JSONArray		docs		= (JSONArray) obj.get("files");
		ent.reqSet(TaNsoGroup.ATT_O_DOCUMENTS, TaTpyDocument.reqListCheck(DefAPI.SV_MODE_MOD, user, ENT_TYP, entId, docs));
		
		//----copy path avatar to T_Val_01
		ent.doBuilAvatarPath();
		TaNsoGroup.DAO.doMerge(ent);			

		return ent;
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doDelWork(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {

		boolean ok = reqDelGroupWork(user, json);

		if(!ok) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response	, ToolJSON.reqJSonString(
				// filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES
				));

	}

	private static boolean reqDelGroupWork(TaAutUser user, JSONObject json) throws Exception {
		Integer grpId = ToolData.reqInt	(json, "id", null);
		if(grpId == null)	return false;

		org.hibernate.Session sess = TaNsoGroup.DAO.reqSessionCurrent();
		try {
			TaNsoGroup gr = TaNsoGroup.DAO.reqEntityByRef(sess, grpId);
			if(gr == null)	return false;

			//delete member
			List<TaNsoGroupMember> members = TaNsoGroupMember.DAO.reqList(sess, Restrictions.eq(TaNsoGroupMember.ATT_I_NSO_GROUP, grpId));
			if(members == null || members.size() == 0)	return false;

			boolean isOwner = false;
			for(TaNsoGroupMember m : members) {
				if((Integer)m.req(TaNsoGroupMember.ATT_I_AUT_USER) == (int)user.reqId() && (Integer)m.req(TaNsoGroupMember.ATT_I_TYPE) == TaNsoGroupMember.TYP_MANAGER) {
					isOwner = true; break;
				}
			}
			if(!isOwner)	return false;
			TaNsoGroupMember.DAO.doRemove(sess, members);

			//del doc of group and group
			List<TaTpyDocument> lstDoc =  TaTpyDocument.DAO.reqList(sess,
					Restrictions.eq(TaTpyDocument.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_NSO_GROUP),
					Restrictions.eq(TaTpyDocument.ATT_I_ENTITY_ID	, grpId)
					);
			TaTpyDocument.DAO.doRemove(sess, lstDoc);

			//delete relationship from all Prj
			List<TaTpyRelationship> lst 		= TaTpyRelationship.DAO.reqList(sess,
					Restrictions.eq(TaTpyRelationship.ATT_I_STATUS			, grpId), 
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT), 
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER));
			TaTpyRelationship.DAO.doRemove(sess, lst);

			lst 		= TaTpyRelationship.DAO.reqList(sess,
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02	, grpId), 
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT), 
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_NSO_GROUP));
			TaTpyRelationship.DAO.doRemove(sess, lst);

			//delete grp
			TaNsoGroup.DAO.doRemove(sess, gr);

			TaNsoGroup.DAO.doSessionCommit(sess);
		}catch(Exception e) {
			if (sess!=null) TaNsoGroup.DAO.doSessionRollback(sess);
			return false;
		}
		return true;
	}
	
	//---------------------------------------------------------------------------------------------------------
	private static void doGroupWorkSaveMember(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {	
		Integer 				groupId	= ToolData.reqInt		(json, "groupId", null);	
		JSONArray				member 	= ToolData.reqJsonArr	(json, "members", new JSONArray()) ;

		if(groupId == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		TaNsoGroup group = TaNsoGroup.DAO.reqEntityByRef(groupId);
		if( group == null || ! group.req(TaNsoGroup.ATT_I_PER_MANAGER).equals(user.reqPerManagerId())) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		if(member.size() == 0)	return;

		List<TaNsoGroupMember> mems = TaNsoGroupMember.DAO.reqList(Restrictions.eq(TaNsoGroupMember.ATT_I_NSO_GROUP		, groupId));

		Map<Integer, TaNsoGroupMember> mapExist = new HashMap<Integer, TaNsoGroupMember>();
		for(TaNsoGroupMember m: mems) {
			mapExist.put((Integer) m.req(TaNsoGroupMember.ATT_I_AUT_USER), m);
		}

		List<TaNsoGroupMember> lstRE 	= new ArrayList<TaNsoGroupMember>();
		List<TaNsoGroupMember> lstMOD 	= new ArrayList<TaNsoGroupMember>();
		List<TaNsoGroupMember> lstDEL 	= new ArrayList<TaNsoGroupMember>();

		for(int i = 0; i < member.size(); i++) {
			JSONObject mem = (JSONObject) member.get(i);
			Map<String, Object> attr 	= API.reqMapParamsByClass(mem, TaNsoGroupMember.class);

			Integer uId 				= (Integer) attr.get(TaNsoGroupMember.ATT_I_AUT_USER);
			TaNsoGroupMember re 		= new TaNsoGroupMember(attr);

			if(mapExist.containsKey(uId)) {
				TaNsoGroupMember memExist = mapExist.get(uId);
				memExist.reqSet(TaNsoGroupMember.ATT_I_TYPE	, attr.get(TaNsoGroupMember.ATT_I_TYPE));
				memExist.reqSet(TaNsoGroupMember.ATT_D_DATE_02, new Date());
				lstMOD.add(mapExist.get(uId));
				mapExist.remove(uId);
				continue;
			}
			
			re.reqSet(TaNsoGroupMember.ATT_I_NSO_GROUP	, groupId);
			re.reqSet(TaNsoGroupMember.ATT_I_STATUS		, TaNsoGroupMember.STAT_ACTIVE);
			re.reqSet(TaNsoGroupMember.ATT_D_DATE_01		, new Date());

			lstRE.add(re);
		}

		if(lstRE.size() > 0)	TaNsoGroupMember.DAO.doPersist(lstRE);
		if(lstMOD.size() > 0) 	TaNsoGroupMember.DAO.doMerge(lstMOD);

		if(!mapExist.isEmpty()) {
			for (Map.Entry<Integer, TaNsoGroupMember> entry : mapExist.entrySet()) {
				lstDEL.add(entry.getValue());
			}
			TaNsoGroupMember.DAO.doRemove(lstDEL);
		}

		API.doResponse(response	, ToolJSON.reqJSonString(
				// filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES
				));
	}
	//---------------------------------------------------------------------------------------------------------
	private static void doGroupSaveNewsletter(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {	
		JSONObject				obj 	= ToolData.reqJson	(json, "obj", new JSONObject());
		String 					email 	= (String) obj.get("email");

		if(email == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		String code                     = "NEWSLETTER";
		TaNsoGroup group = TaNsoGroup.DAO.reqEntityByValue(TaNsoGroup.ATT_T_REF, code);
		if( group == null) {
			TaNsoGroup grp 		= new TaNsoGroup(code, TaNsoGroup.TYP_01_EMAIL);
			grp.reqSet(TaNsoGroup.ATT_T_NAME  ,  code);
			grp.reqSet(TaNsoGroup.ATT_I_STATUS_01,  TaNsoGroup.STAT_EMAIL_VALIDATE);
			grp.reqSet(TaNsoGroup.ATT_D_DATE_01, new Date());
			grp.reqSet(TaNsoGroup.ATT_T_INFO_02,  email);
			TaNsoGroup.DAO.doPersist(grp);
		}else {
			String val01          = (String) group.req(TaNsoGroup.ATT_T_INFO_02);
			StringBuilder sbVal01 = new StringBuilder(val01);
			sbVal01.append("," + email);

			group.reqSet(TaNsoGroup.ATT_T_INFO_02,  sbVal01.toString());

			TaNsoGroup.DAO.doMerge(group);
		}
		
		API.doResponse(response	, ToolJSON.reqJSonString(
				// filter,
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES
				));
	}
}
