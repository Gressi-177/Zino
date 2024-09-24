package com.hnv.api.service.priv.job;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.hnv.common.tool.ToolDBLock;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolDatatable;
import com.hnv.common.tool.ToolEmail;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.data.json.JSONArray;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.cfg.TaCfgValue;
import com.hnv.db.job.TaJobDayOffRequest;
import com.hnv.db.job.TaJobDayOffResume;
import com.hnv.db.job.TaJobReport;
import com.hnv.db.job.ViJobReportUser;
import com.hnv.db.msg.TaMsgMessage;
import com.hnv.db.per.TaPerPerson;
import com.hnv.db.sys.TaSysLock;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.def.DefDBExt;
import com.hnv.process.ThreadManager;

/**
* ----- ServiceJobDayoffRequest by H&V
* ----- Copyright 2017------------
*/
public class ServiceJobDayoffRequest implements IService {
	private static	String 			filePath	= null; 
	private	static	String 			urlPath		= null; 
	

	public static final Integer 	NEW_CONTINUE 				= 1;	
	public static final Integer 	NEW_EXIT 					= 2;
	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 						= "EC_V3".toLowerCase();

	public static final String SV_CLASS 						= "ServiceJobDayoffRequest".toLowerCase();	
	
	public static final String SV_GET 			= "SVGet".toLowerCase();	
	public static final String SV_LST 			= "SVLst".toLowerCase();
	public static final String SV_LST_DYN		= "SVLstDyn".toLowerCase(); 
	public static final String SV_LST_DYN_MAN	= "SVLstDynMan".toLowerCase(); 
	public static final String SV_LST_USER		= "SVLstUser".toLowerCase();
	public static final String SV_INFO_USER		= "SVInfoUser".toLowerCase();

	public static final String SV_NEW 			= "SVNew".toLowerCase();	
	public static final String SV_MOD 			= "SVMod".toLowerCase();	
	public static final String SV_DEL 			= "SVDel".toLowerCase();
	public static final String SV_MOD_DIRECT	= "SVModDirect".toLowerCase();

	public static final String SV_LCK_REQ 		= "SVLckReq".toLowerCase(); //req or refresh	
	public static final String SV_LCK_SAV 		= "SVLckSav".toLowerCase(); //save and continue
	public static final String SV_LCK_END 		= "SVLckEnd".toLowerCase();
	public static final String SV_LCK_DEL 		= "SVLckDel".toLowerCase();

	public static final String SV_SAVE_FILES 	= "SVSaveFiles".toLowerCase();
	
		
	//-----------------------------------------------------------------------------------------------
	//-------------------------Default Constructor - Required -------------------------------------
	public ServiceJobDayoffRequest(){
		
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}
	
	public static final Integer	ENT_TYP				= DefDBExt.ID_TA_JOB_DAYOFF_REQUEST;
	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		//ServerLogTool.doLogInf("--------- "+ SV_CLASS+ ".doService --------------");
		
		if (filePath	==null) filePath		= API.reqContextParameter("JOB_DAYOFF_REQUEST_PATH_FILE");
		if (urlPath		==null) urlPath			= API.reqContextParameter("JOB_DAYOFF_REQUEST_PATH_URL");	
						
		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		
		try {
			//---------------------------------------------------------------------------------
			//---------------------------------------------------------------------------------
		
			if(sv.equals(SV_GET) 				){
				doGet(user, json, response);
			} else if(sv.equals(SV_LST)		){
				doLst(user, json, response);
			} else if(sv.equals(SV_LST_DYN)	){
				doLstDyn(user, json, response);
			} else if(sv.equals(SV_LST_DYN_MAN)	){
				doLstDynMan(user, json, response);
			} else if(sv.equals(SV_LST_USER)		){
				doLstUser(user, json, response);
			} else if(sv.equals(SV_INFO_USER)		){
				doInfoUser(user, json, response);
			} else if(sv.equals(SV_NEW)		){
				doNew(user, json, response);
			} else if(sv.equals(SV_MOD)		){
				doMod(user, json, response, false);
			} else if(sv.equals(SV_MOD_DIRECT)		){
				doModDirect(user, json, response);
			}else  if(sv.equals(SV_DEL)		){
				doDel(user, json, response);
			} else if(sv.equals(SV_LCK_REQ)	){
				doLckReq(user, json, response);
			} else if(sv.equals(SV_LCK_SAV)	){
				doLckSav(user, json, response);
			} else if(sv.equals(SV_LCK_END)	){
				doLckEnd(user, json, response);
			} else if(sv.equals(SV_LCK_DEL)	){
				doLckDel(user, json, response);		
			} else if(sv.equals(SV_SAVE_FILES)	){
				doSaveFiles(user, json, response);	
			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}	
		
		}catch(Exception e){
			API.doResponse(response, DefAPI.API_MSG_ERR_API);
			e.printStackTrace();
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
	private static void doGet(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		
		Integer 			objId	= ToolData.reqInt	(json, "id"		, -1	);				
		Boolean				forced	= ToolData.reqBool	(json, "forced"	, false	);
																		
		TaJobDayOffRequest 		ent 	= reqGet(objId, forced);
		ent.doBuildPerCreateAndOwner(true);
		ent.doBuildPerValidate(true);
		ent.doBuildUserDayoffResume(true);
		
		if (ent==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
				
		if (!canWorkWithObj(user, WORK_GET, ent)){
			API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			return;
		}
								
		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent 
		));
	}
	
	private static TaJobDayOffRequest reqGet(Integer matId, Boolean forced) throws Exception{
		TaJobDayOffRequest 		ent 	= TaJobDayOffRequest.DAO.reqEntityByRef(matId, forced);
			
		//---do build something other of ent like details....
			
		return ent;
	}
	//---------------------------------------------------------------------------------------------------------
	private static void doLst(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");
			
		List<TaJobDayOffRequest> 	list = reqLst(user, json); //and other params if necessary
		if (list==null || list.size()==0){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
		
		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
			DefJS.SESS_STAT		, 1,
			DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
			DefJS.RES_DATA		, list 
		));				
	}

	private static List<TaJobDayOffRequest> reqLst(TaAutUser user, JSONObject json, Object...params) throws Exception {
		Integer 			objMan		= ToolData.reqInt	(json, "manId"	, null	);
		Integer 			objTyp		= ToolData.reqInt	(json, "typ"		, null	);
		Boolean 			objContBuild= ToolData.reqBool	(json, "withBuild"	, false	);
		
		//other params here
				
		if (!canWorkWithObj(user, WORK_LST, objTyp)){ //other param after objTyp...
			return null;
		}
				
		Criterion 			cri		= reqCriterion (objTyp); //and other params	
		List<TaJobDayOffRequest> 	list 	= null;
		if (cri==null) 
			list =   TaJobDayOffRequest.DAO.reqList();
		else
			list =   TaJobDayOffRequest.DAO.reqList(cri);
		
		
		if (params!=null){
			//do something with list before return
		}	
		
		//do something else with request
		if (objContBuild){
			
		}
		
		return list;
	}
	
	private static Criterion reqCriterion(Object...params) throws Exception{
		if (params==null || params.length==0) return null;
		
		Criterion cri = Restrictions.gt("I_ID", 0);	

		if (params!=null && params.length>0){
			//int type 	= (int) params[0];
			//cri 		= Restrictions.and(cri, Restrictions.eqOrIsNull(TaJobDayoffRequest.ATT_I_TYPE, type));
		}			
		
		if (params!=null && params.length>1){
			//do something
		}
		
		return cri;
	}
	//---------------------------------------------------------------------------------------------------------
	//JOB DAYOFF REQUEST USER INFO------JOB DAYOFF REQUEST USER INFO------JOB DAYOFF REQUEST USER INFO------JOB
	//---------------------------------------------------------------------------------------------------------
	
	private static void doInfoUser(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");
		Integer uId03 = ToolData.reqInt	(json, "uId03", null);			
		ViJobReportUser userInfo = ViJobReportUser.reqUserInfoForJobOff(uId03);
		if (userInfo==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
			
		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
			DefJS.SESS_STAT		, 1,
			DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
			DefJS.RES_DATA		, userInfo 
		));				
	}
	
	//---------------------------------------------------------------------------------------------------------
	//JOB DAYOFF REQUEST LIST USER------JOB DAYOFF REQUEST LIST USER------JOB DAYOFF REQUEST LIST USER------JOB
	//---------------------------------------------------------------------------------------------------------
	private static void doLstUser(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");
			
		List<TaAutUser> listUser = TaAutUser.DAO.reqList();
		if (listUser==null || listUser.size()==0){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
		for(TaAutUser u : listUser){
			u.doBuildPerson(false);
		}
		
		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
			DefJS.SESS_STAT		, 1,
			DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
			DefJS.RES_DATA		, listUser 
		));				
	}
	
	//---------------------------------------------------------------------------------------------------------
	//JOB DAYOFF REQUEST LIST DYNAMIC------JOB DAYOFF REQUEST LIST DYNAMIC------JOB DAYOFF REQUEST LIST DYNAMIC
	//---------------------------------------------------------------------------------------------------------

	private static Long countAllEle = null;
	private static void doLstDyn(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {	
		Object[]  			dataTableOption = ToolDatatable.reqDataTableOption (json, null);				
		Set<String>		searchKey			= (Set<String>)dataTableOption[0];	
		Integer				stat			= ToolData.reqInt	(json, "stat" , null);
		Integer				uId03			= ToolData.reqInt	(json, "uId03", null);

		if (!canWorkWithObj(user, WORK_LST, null, stat, uId03)){ //other param after objTyp...
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		Criterion 	cri 					= reqRestriction(searchKey, stat, uId03, null, null);
		List<TaJobDayOffRequest> list 		= reqListDyn(dataTableOption, cri);		
		if (list==null ){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}else {
			for (TaJobDayOffRequest rp : list){
				rp.doBuildUserDayoffResume(true);
				rp.doBuildPerCreateAndOwner(true);
				rp.doBuildPerValidate(true);
				rp.doBuildFiles(true);
			}
		}

		if (countAllEle==null)
			countAllEle = ((long)reqNbJobDayoffRequestListDyn());

		Integer iTotalRecords 			= (countAllEle.intValue());				
		Integer iTotalDisplayRecords 	= reqNbJobDayoffRequestListDyn(cri).intValue();

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,					
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, list
				));

	}
	
	private static void doLstDynMan(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {	
		Object[]  			dataTableOption = ToolDatatable.reqDataTableOption (json, null);				
		Set<String>		searchKey			= (Set<String>) dataTableOption[0];	
		Integer				stat			= ToolData.reqInt	(json, "stat", null);
		Integer				uId03			= ToolData.reqInt	(json, "uId03", null);

		if (!canWorkWithObj(user, WORK_LST, null, stat, uId03)){ //other param after objTyp...
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
		
		Integer manId           = user.reqPerManagerId();
		
		Set<Integer>	stats	= new HashSet<Integer>() ;
		
		if(stat == null) {
			stats.add(TaJobDayOffRequest.PR_REQUEST_STAT_PENDING);
			stats.add(TaJobDayOffRequest.PR_REQUEST_STAT_VALIDATE);
			stats.add(TaJobDayOffRequest.PR_REQUEST_STAT_DENIED);
		}

		Criterion 	cri 					= reqRestriction(searchKey, stat, uId03, stats, manId);
		List<TaJobDayOffRequest> list 		= reqListDyn(dataTableOption, cri);		
		if (list==null ){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}else {
			for (TaJobDayOffRequest rp : list){
				rp.doBuildUserDayoffResume(true);
				rp.doBuildPerCreateAndOwner(true);
				rp.doBuildPerValidate(true);
				rp.doBuildFiles(true);
			}
		}

		if (countAllEle==null)
			countAllEle = ((long)reqNbJobDayoffRequestListDyn());

		Integer iTotalRecords 			= (countAllEle.intValue());				
		Integer iTotalDisplayRecords 	= reqNbJobDayoffRequestListDyn(cri).intValue();

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,					
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, list
				));

	}
	
	
	private static Criterion reqRestriction(Set<String> searchKey, Integer stat, Integer uId03, Set<Integer> stats, Integer manId) throws Exception {		
		//Don't use SearchKey here.
		Criterion cri = Restrictions.gt(TaJobDayOffRequest.ATT_I_ID, 0);
		
		if(stat != null){
			cri = Restrictions.and(cri, Restrictions.eq(TaJobDayOffRequest.ATT_I_STATUS, stat));
		}
		
		if(stats != null && !stats.isEmpty()){
			cri = Restrictions.and(cri, Restrictions.in(TaJobDayOffRequest.ATT_I_STATUS, stats));
		} 
		
		if(uId03 != null){			
			cri = Restrictions.and(cri, Restrictions.eq(TaJobDayOffRequest.ATT_I_AUT_USER_03, uId03));			
		}
		
		if(manId != null){			
			cri = Restrictions.and(cri, Restrictions.eq(TaJobDayOffRequest.ATT_I_PER_MANAGER, manId));			
		}
		
		return cri;
	}
	
	private static List<TaJobDayOffRequest> reqListDyn(Object[] dataTableOption, Criterion 	cri) throws Exception {		
		int 		begin 		= (int)	dataTableOption[1];
		int 		number 		= (int)	dataTableOption[2]; 
		int 		sortCol 	= (int)	dataTableOption[3]; 
		int 		sortTyp 	= (int)	dataTableOption[4];	

		List<TaJobDayOffRequest> list 	= new ArrayList<TaJobDayOffRequest>();		

		Order 	order 	= null;			
		String 	colName = null;

		switch(sortCol){
//		case 0: colName = TaJobDayoffRequest.ATT_I_ID; break;		
		//case 1: colName = TaJobDayoffRequest.ATT_T_NAME; break;					
		}

		if (colName!=null){
			switch(sortTyp){
			case 0: order = Order.asc (colName); break;
			case 1: order = Order.desc(colName); break;								
			}
		}

		if (order==null)
			list	= TaJobDayOffRequest.DAO.reqList(begin, number,  Order.desc(TaJobDayOffRequest.ATT_D_DATE_03), cri);
		else
			list	= TaJobDayOffRequest.DAO.reqList(begin, number, order, cri);			

		return list;
	}

	private static Number reqNbJobDayoffRequestListDyn() throws Exception {						
		return TaJobDayOffRequest.DAO.reqCount();		
	}

	private static Number reqNbJobDayoffRequestListDyn(Criterion cri) throws Exception {			
		return TaJobDayOffRequest.DAO.reqCount(cri);
	}
	
	//---------------------------------------------------------------------------------------------------------
	//JOB DAYOFF REQUEST NEW------JOB DAYOFF REQUEST NEW------JOB DAYOFF REQUEST NEW------JOB DAYOFF REQUEST NE
	//---------------------------------------------------------------------------------------------------------
	
	private static void doNew(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doNew --------------");
		//--- in simple case, obj has only header , no details ----------------------
		//Map<String, Object> attr 	= API.reqMapParamsByClass(request, TaJobDayoffRequest.class);
		//TaJobDayoffRequest  ent	 = new TaJobDayoffRequest(attr);
		//TaJobDayoffRequest.DAO.doPersist(ent);
		//----------------------------------------------------------------------------------------------------------------------
			
		TaJobDayOffRequest 			ent		= reqNew		(user, json);
		
		if (ent==null){
			API.doResponse(response, ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO					
					));
			return;
		}

		TaSysLock 	lock 	= ToolDBLock.reqLock (json, "lock", DefDB.DB_LOCK_NEW, ENT_TYP, (Integer)ent.req(TaAutUser.ATT_I_ID), user.reqId(), user.reqStr(TaAutUser.ATT_T_LOGIN_01), null);
		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent,
				"lock"				, lock
				));
	}

	private static TaJobDayOffRequest reqNew(TaAutUser user,  JSONObject json) throws Exception {
		JSONObject			   obj		= ToolData.reqJson (json, "obj", null);
		Map<String, Object> 	attr 	= API.reqMapParamsByClass(obj, TaJobDayOffRequest.class);		
		TaJobDayOffRequest  	ent	 	= new TaJobDayOffRequest(attr);
				
		if (!canWorkWithObj(user, WORK_NEW, ent)){ //other param after obj...
			return null;
		}
		
		Integer manId = user.reqPerManagerId();
		
		ent.reqSet(TaJobDayOffRequest.ATT_D_DATE_01, new Date());
		ent.reqSet(TaJobDayOffRequest.ATT_I_PER_MANAGER, manId);
		
		TaJobDayOffRequest.DAO.doPersist(ent);
		Integer requestId = (Integer) ent.req(TaJobDayOffRequest.ATT_I_ID);
		
		//Update attach files
		JSONArray			files		= ToolData.reqJsonArr(json, "files", new JSONArray());
		List<TaTpyDocument> lstDoc = TaTpyDocument.reqListCheck(DefAPI.SV_MODE_NEW, user,DefDBExt.ID_TA_JOB_REPORT, ent.reqId(), files);
		ent.reqSet(TaJobReport.ATT_O_DOCUMENTS, lstDoc);
		
		ent.doBuildPerCreateAndOwner(true);
		ent.doBuildUserDayoffResume(true);
		
		return ent;
	}

	//---------------------------------------------------------------------------------------------------------
	//JOB DAYOFF REQUEST MOD------JOB DAYOFF REQUEST MOD------JOB DAYOFF REQUEST MOD------JOB DAYOFF REQUEST MO
	//---------------------------------------------------------------------------------------------------------

	private static void doMod(TaAutUser user, JSONObject json, HttpServletResponse response, boolean directSave) throws Exception  {
		TaJobDayOffRequest ent	= null;
		if(directSave){
			ent	= reqModDirect(user, json);
		} else {
			ent	= reqMod(user, json);
		}
									
		if (ent==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
		} else {
			ent.doBuildUserDayoffResume(true);
			ent.doBuildPerCreateAndOwner(true);
			ent.doBuildPerValidate(true);
			ent.doBuildFiles(true);
			
			doSendEmailManagerJobDayoff(user, ent);
			
			API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent
			));	
		}		
	}
	
	//-----------send-mai--------------
	private static String EMAIL_HOST 		= "";
	private static String EMAIL_PORT 		= "";
	private static String EMAIL_LOGIN 		= "";
	private static String EMAIL_PWD 		= "";

	private static String 	EMAIL_JOB_DAY_OFF_TITLE 	= "";
	private static String 	EMAIL_JOB_DAY_OFF_CONT 	= "";
	private static Integer 	EMAIL_JOB_DAY_OFF_CONT_NB 	= 1;

	private static final int TYP_RECEIVE		= 12;
	private static final String TAB_DAY_OFF 	= "off";
	
	private static JSONObject reqConfig (String code) throws Exception{
		TaCfgValue	config = TaCfgValue.DAO.reqEntityByValue(TaCfgValue.ATT_T_CODE, "TA_CFG_PRJ_EMAIL");
		if (config==null) return null;

		JSONObject 	cfg		= ToolJSON.reqJSonFromString(config.reqStr(TaCfgValue.ATT_T_INFO_01));

		EMAIL_HOST 			= (String) cfg.get("prj_email_host" );
		EMAIL_PORT 			= (String) cfg.get("prj_email_port" );
		EMAIL_LOGIN 		= (String) cfg.get("prj_email_login");
		EMAIL_PWD 			= (String) cfg.get("prj_email_pwd" 	);
		
		return cfg;
	}
	
	public static void doSendEmailManagerJobDayoff(TaAutUser user, TaJobDayOffRequest ent){
		Thread t = new Thread(){
			public void run(){
				try{
					JSONObject 	cfg  = reqConfig ("TA_CFG_PRJ_EMAIL");
					if (cfg==null) return;
					
					String EMAIL_JOB_DAY_OFF_TITLE 		= (String) cfg.get("prj_email_job_reqdayoff_title" 	);
					String EMAIL_JOB_DAY_OFF_CONT 		= (String) cfg.get("prj_email_job_reqdayoff_content"	);
					Integer EMAIL_JOB_DAY_OFF_CONT_NB 	= Integer.parseInt((String) cfg.get("prj_email_job_reqdayoff_content"	));
					
					
					String        	pCode   = ent.req(TaJobReport.ATT_T_CODE_01).toString();

					Integer       	perId  	= (Integer)user.req(TaAutUser.ATT_I_PER_PERSON);
					TaPerPerson	  	per		= TaPerPerson.DAO.reqEntityByValue(TaPerPerson.ATT_I_ID, perId);
					String			namePer	= (String) per.req(TaPerPerson.ATT_T_NAME_01) + " " + per.req(TaPerPerson.ATT_T_NAME_02) + " " + per.req(TaPerPerson.ATT_T_NAME_03);

					Integer 		uId 	= (Integer) user.reqId();
					TaAutUser 		u 		= TaAutUser.DAO.reqEntityByID(uId);
					String 			email 	= u.reqStr(TaAutUser.ATT_T_INFO_01);
					
					Integer       pId     = (Integer) ent.req(TaJobDayOffRequest.ATT_I_ID);
					String        pRef   = ent.req(TaJobDayOffRequest.ATT_T_CODE_01).toString();

					
					if (email!=null && email.length()>0) {
					
						String emailTitle 	= EMAIL_JOB_DAY_OFF_TITLE;
						String emailCont 	= reqFormattedContent(EMAIL_JOB_DAY_OFF_CONT, pRef, EMAIL_JOB_DAY_OFF_CONT_NB); 
		
						ToolEmail.canSendEmail(
								EMAIL_HOST, EMAIL_PORT, null, EMAIL_LOGIN, EMAIL_PWD, 
								EMAIL_LOGIN, 
								emailTitle, emailCont,
								email, null, null, null);	
					}
					
					doNewNotification(ent, user, TAB_DAY_OFF, TYP_RECEIVE);
				}catch(Exception e){
				}
			}
		};
		ThreadManager.doExecute(t, DefTime.TIME_00_00_05_000);
	}
	
	private static void doSendEmailJobOffRefuse(int clientId, String cliMsg) throws Exception{
		JSONObject 	cfg  = reqConfig ("TA_CFG_PRJ_EMAIL");
		if (cfg==null) return;

		String E_DESTINATION = doIdentifyClient(clientId);
		String E_TITLE		 = (String) cfg.get("prj_email_job_reqdayoff_KO_title" 	);
		String E_CONTENT	 = (String) cfg.get("prj_email_job_reqdayoff_KO_content" 	);

		if (E_DESTINATION 	!=null) {
			E_CONTENT 	= String.format(E_CONTENT,cliMsg,cliMsg,cliMsg);

			if (E_DESTINATION!=null)
				ToolEmail.canSendEmail(
						EMAIL_HOST, EMAIL_PORT, null, EMAIL_LOGIN, EMAIL_PWD, EMAIL_LOGIN, 
						E_TITLE, E_CONTENT, E_DESTINATION, 
						null, null, null);
		}
	}
	private static String doIdentifyClient(int clientId) throws Exception  {
		String 	emailClient 	= null;
		TaAutUser per = TaAutUser.DAO.reqEntityByRef(clientId);
		if(per != null) {
			String eU = (String) per.req(TaAutUser.ATT_T_INFO_01);
			if(eU != null && !eU.isEmpty()) {
				emailClient = eU;
			}
		}
		if(emailClient == null || emailClient.isEmpty()) {
			//			emailClient = "contact@wygo.com";
			emailClient = null;
		}

		return emailClient;
	}
	private static String reqFormattedContent(String template, String msg01, Integer count) {
		String content = "";
		switch(count) {
		case 0:
		case 1: content 	= String.format(template, msg01);break;
		case 2: content 	= String.format(template, msg01, msg01);break;
		case 3: content 	= String.format(template, msg01, msg01, msg01);break;
		case 4: content 	= String.format(template, msg01, msg01, msg01, msg01);break;
		case 5: content 	= String.format(template, msg01, msg01, msg01, msg01, msg01);break;
		default : content 	= template;
		}
		return content;
	}
	
	//----------notification------------
	private static void doNewNotification(TaJobDayOffRequest rep, TaAutUser user, String table_notif, int typ_notif) throws Exception {
		Integer 		uId 	= (Integer) user.req(TaAutUser.ATT_I_AUT_USER_03);
		if (uId==null) 	return;
		Integer 		manId 	= (Integer) user.req(TaAutUser.ATT_I_PER_MANAGER);
		Integer       	pId     = (Integer) rep.req(TaJobReport.ATT_I_ID);
		String        	pCode   = rep.req(TaJobReport.ATT_T_CODE_01).toString();

		Integer       	perId  	= (Integer)user.req(TaAutUser.ATT_I_PER_PERSON);
		TaPerPerson	  	per		= TaPerPerson.DAO.reqEntityByValue(TaPerPerson.ATT_I_ID, perId);
		String			namePer	= (String) per.req(TaPerPerson.ATT_T_NAME_01) + " " + per.req(TaPerPerson.ATT_T_NAME_02) + " " + per.req(TaPerPerson.ATT_T_NAME_03);


		JSONArray arr 			= new JSONArray();
		JSONObject notify 		= new JSONObject();

		notify.put("typ"		, typ_notif);
		notify.put("typTab"		, table_notif);
		notify.put("title"		, pCode + "_" + namePer.trim());
		notify.put("uID"		, uId);
		notify.put("manID"		, manId);
		notify.put("uAction"	, user.reqRef());
		notify.put("parTyp"		, DefDBExt.ID_TA_PRJ_PROJECT);
		notify.put("parID"		, rep.req(TaJobDayOffRequest.ATT_I_ID));

		arr.add(notify);
		reqNewNoti(uId, rep.reqId(), arr);
	}

	private static void reqNewNoti(int uIdDest, int rId, JSONArray arrNotify) throws Exception {
		List<TaMsgMessage> lst = new ArrayList<TaMsgMessage>();

		for(int i = 0; i < arrNotify.size(); i++) {
			JSONObject notifyContent = (JSONObject) arrNotify.get(i);
			TaMsgMessage notif = new TaMsgMessage();
			notif.reqSet(TaMsgMessage.ATT_I_TYPE_01			, TaMsgMessage.TYPE_01_NOTIFICATION);
			notif.reqSet(TaMsgMessage.ATT_I_STATUS			, TaMsgMessage.STAT_NOTI_NEW);
			notif.reqSet(TaMsgMessage.ATT_D_DATE_01			, new Date());
			notif.reqSet(TaMsgMessage.ATT_I_AUT_USER		, uIdDest);
			notif.reqSet(TaMsgMessage.ATT_T_INFO_01			, notifyContent.toString());
			notif.reqSet(TaMsgMessage.ATT_I_ENTITY_TYPE		, DefDBExt.ID_TA_JOB_REPORT);
			notif.reqSet(TaMsgMessage.ATT_I_ENTITY_ID		, rId);
			lst.add(notif);
		}

		TaMsgMessage.DAO.doPersist(lst);
	}

	//-------------------------------
	private static TaJobDayOffRequest reqMod(TaAutUser user,  JSONObject json) throws Exception {
		Map<String, Object> 	attr 	= API.reqMapParamsByClass(json, TaJobDayOffRequest.class);

		int requestId 					= (int) attr.get(TaJobDayOffRequest.ATT_I_ID);
		int newStatus 					= (int) attr.get(TaJobReport.ATT_I_STATUS);
		String comment					= (String) attr.get(TaJobDayOffRequest.ATT_T_INFO_01);
		TaJobDayOffRequest  ent	 		= TaJobDayOffRequest.DAO.reqEntityByRef(requestId);
			
		if (ent==null){
			return null;
		}
				
		if (!canWorkWithObj(user, WORK_MOD, ent)){ //other param after obj...
			return null;
		}
				
		if(newStatus != (int) ent.req(TaJobDayOffRequest.ATT_I_STATUS)){
			attr.put(TaJobDayOffRequest.ATT_D_DATE_02, new Date());
			attr.put(TaJobDayOffRequest.ATT_I_AUT_USER_02, user.req(TaAutUser.ATT_I_ID));
			attr.put(TaJobDayOffRequest.ATT_T_INFO_01, comment);
		}

		TaJobDayOffRequest.DAO.doMerge(ent, attr);	
		
		//Update attach files
		JSONArray			files		= ToolData.reqJsonArr(json, "files", new JSONArray());
		List<TaTpyDocument> lstDoc = TaTpyDocument.reqListCheck(DefAPI.SV_MODE_MOD, user,DefDBExt.ID_TA_JOB_REPORT, ent.reqId(), files);
		ent.reqSet(TaJobReport.ATT_O_DOCUMENTS, lstDoc);
				
		return ent;
	}
	
	private void doModDirect(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		Integer 		entId	= ToolData.reqInt	(json, "id", null);	
		TaSysLock 		lock 	= ToolDBLock.reqLock(ENT_TYP, entId, user.reqId(), user.reqStr(TaAutUser.ATT_T_LOGIN_01), null);
		if (lock==null || lock.reqStatus() == 0){
			API.doResponse(response, ToolJSON.reqJSonString(						
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO,
					DefJS.RES_DATA		, lock
					));	
		}else{
			doMod(user, json, response, true);
			ToolDBLock.canDeleteLock(lock);
			API.doResponse(response, ToolJSON.reqJSonString(						
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES
					));	
		}		
	}
	
	private static TaJobDayOffRequest reqModDirect(TaAutUser user,  JSONObject json) throws Exception {
		int reqId				= ToolData.reqInt	(json, 	"reqId"		, null);
		int newStatus 			= ToolData.reqInt	(json, 	"stat"		, null);
		String cmt				= ToolData.reqStr	(json, 	"cmt"		, null);
		String message			= ToolData.reqStr	(json, 	"message"	, null);
		TaJobDayOffRequest  ent	= TaJobDayOffRequest.DAO.reqEntityByRef(reqId);
		
		int oldStat  = (int) ent.req(TaJobDayOffRequest.ATT_I_STATUS);
		
		if (ent==null){
			return null;
		}
		
		if (!canWorkWithObj(user, WORK_MOD, ent)){ //other param after obj...
			return null;
		}
		
		ent.reqSet(TaJobReport.ATT_I_STATUS, newStatus);
		
		if(newStatus == TaJobDayOffRequest.PR_REQUEST_STAT_DENIED || newStatus == TaJobDayOffRequest.PR_REQUEST_STAT_VALIDATE){
			ent.reqSet(TaJobDayOffRequest.ATT_D_DATE_02, new Date());
			ent.reqSet(TaJobDayOffRequest.ATT_I_AUT_USER_02, user.req(TaAutUser.ATT_I_ID));
		}
	
		ent.reqSet(TaJobDayOffRequest.ATT_T_INFO_01, cmt);
		
		TaJobDayOffRequest.DAO.doMerge(ent);
		if(message != null && !message.isEmpty()) {
			doSendEmailJobOffRefuse((int)ent.req(TaJobDayOffRequest.ATT_I_AUT_USER_01),message);
		}
//		update day off total
		if(newStatus == TaJobDayOffRequest.PR_REQUEST_STAT_VALIDATE) {
			int idUser = (int) ent.req(TaJobDayOffRequest.ATT_I_AUT_USER_01);
			TaJobDayOffResume info = TaJobDayOffResume.DAO.reqEntityByValue(TaJobDayOffResume.ATT_I_AUT_USER, idUser);

			if(info == null){
				// Tao moi new tab Resume chua co user Data						
				info = new TaJobDayOffResume(idUser, null, null);
				TaJobDayOffResume.DAO.doPersist(info);
			}
			doUpdateDayOffResume(info, ent, oldStat);
		}
		
		return ent;
	}
	
	
	
	
	private static void doUpdateDayOffResume(TaJobDayOffResume info, TaJobDayOffRequest ent, int oldStat) throws Exception {
		// Lay so ngay nghi yeu cau
		double dayoff_use = (double) ent.req(TaJobDayOffRequest.COL_F_VAL_01);

		double hld_R  = 0.0;
		//----Cong ngay da nghi vao so ngay nghi:
		if(oldStat == TaJobDayOffRequest.PR_REQUEST_STAT_DENIED) {
			hld_R = (double) info.req(TaJobDayOffResume.ATT_F_VAL_03) - dayoff_use;
		}else {
			hld_R = (double) info.req(TaJobDayOffResume.ATT_F_VAL_03) + dayoff_use;
		}
		
		info.reqSet(TaJobDayOffResume.ATT_F_VAL_03,  	hld_R);
		TaJobDayOffResume.DAO.doMerge(info);
	}

	//---------------------------------------------------------------------------------------------------------
	//JOB DAYOFF REQUEST DEL------JOB DAYOFF REQUEST DEL------JOB DAYOFF REQUEST DEL------JOB DAYOFF REQUEST DE
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
			API.doResponse(response, ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_YES));
		}

		ToolDBLock.canDeleteLock(lock);
	}
	
	private static boolean canDel(TaAutUser user,  JSONObject json) throws Exception {
		Integer 		objId	= ToolData.reqInt	(json, "id"		, -1	);	
		
		TaJobDayOffRequest  	ent	 	= TaJobDayOffRequest.DAO.reqEntityByRef(objId);
		if (ent==null){
			return false;
		}
		
		if (!canWorkWithObj(user, WORK_DEL, ent)){ //other param after ent...
			return false;
		}
		
		Session sessMain 	= TaJobDayOffRequest		.DAO.reqSessionCurrent();
		try {
			TaTpyDocument		.doListDel	(sessMain, ENT_TYP, ent.reqId());
			TaJobDayOffRequest.DAO.doRemove(sessMain, ent);		
		}catch(Exception e){
			e.printStackTrace();
			TaJobDayOffRequest			.DAO.doSessionRollback(sessMain);
//			TaTpyDocument		.DAO.doSessionRollback(sessSub);
		}	
		
		return true;
	}
	
	//---------------------------------------------------------------------------------------------------------
	private void doLckReq(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLckReq --------------");		

		Integer 		entId	= ToolData.reqInt	(json, "id", null);	
		TaSysLock 		lock 	= ToolDBLock.reqLock(ENT_TYP, entId, user.reqId(), user.reqStr(TaAutUser.ATT_T_LOGIN_01), null);
		if (lock==null || lock.reqStatus() == 0){
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
		
		TaJobDayOffRequest  		ent	 	=  reqMod(user, json); 								
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
		
		
		TaJobDayOffRequest ent = reqMod(user, json);						
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
	
	private static void doSaveFiles(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		TaJobDayOffRequest 			ent		= reqSaveFiles		(user, json);
		if (ent==null){
			API.doResponse(response, ToolJSON.reqJSonString(		
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO
					));	
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent
				));		
	}
		
	private static TaJobDayOffRequest reqSaveFiles(TaAutUser user,  JSONObject json) throws Exception {
		JSONObject			obj		= ToolData.reqJson(json, "obj", new JSONObject());

		if (!canWorkWithObj(user, WORK_NEW, obj)){ //other param after obj...
			return null;
		}
		

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaJobDayOffRequest.class);

		Integer 			prjID 	= (Integer) attr.get(TaJobDayOffRequest.ATT_I_ID);
		if(prjID == null)	return null;

		TaJobDayOffRequest  		ent	 	= TaJobDayOffRequest.DAO.reqEntityByRef(prjID);
		if(ent == null)	return null;		


		JSONArray			files		= ToolData.reqJsonArr(json, "files", new JSONArray());
		List<TaTpyDocument> lstDoc = TaTpyDocument.reqListCheck(DefAPI.SV_MODE_NEW, user,DefDBExt.ID_TA_JOB_REPORT, ent.reqId(), files);
		ent.reqSet(TaJobReport.ATT_O_DOCUMENTS, lstDoc);

		return ent;

	}
}
