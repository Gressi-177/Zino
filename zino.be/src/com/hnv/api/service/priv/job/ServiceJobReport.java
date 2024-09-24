package com.hnv.api.service.priv.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
import com.hnv.api.service.common.APIAuth;
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
import com.hnv.db.job.TaJobDayOffResume;
import com.hnv.db.job.TaJobHoliday;
import com.hnv.db.job.TaJobReport;
import com.hnv.db.job.TaJobReportDetail;
import com.hnv.db.job.TaJobReportResume;
import com.hnv.db.job.ViJobReportUser;
import com.hnv.db.mat.TaMatUnit;
import com.hnv.db.msg.TaMsgMessage;
import com.hnv.db.per.TaPerPerson;
import com.hnv.db.sys.TaSysLock;
import com.hnv.db.tpy.TaTpyCategory;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.def.DefDBExt;
import com.hnv.process.ThreadManager;

/**
 * ----- ServiceJobReport by H&V
 * ----- Copyright 2017------------
 */
public class ServiceJobReport implements IService {

	private static	String 			filePath	= null; 
	private	static	String 			urlPath		= null; 
	public static final Integer 	NEW_CONTINUE 			= 1;	
	public static final Integer 	NEW_EXIT 				= 2;
	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 					= "EC_V3".toLowerCase();

	public static final String SV_CLASS 					= "ServiceJobReport".toLowerCase();	



	public static final String SV_GET 			= "SVGet".toLowerCase();
	public static final String SV_LST 			= "SVLst".toLowerCase();
	public static final String SV_LST_BY_USER 	= "SVLstByUser".toLowerCase();
	public static final String SV_LST_DYN		= "SVLstDyn".toLowerCase();
	public static final String SV_LST_DYN_MAN	= "SVLstManDyn".toLowerCase();

	public static final String SV_INFO_USER		= "SVInfoUser".toLowerCase();	
	public static final String SV_LST_USER_DYN	= "SVLstUserDyn".toLowerCase();

	public static final String SV_NEW 			= "SVNew".toLowerCase();	
	public static final String SV_MOD 			= "SVMod".toLowerCase();	
	public static final String SV_DEL 			= "SVDel".toLowerCase();
	public static final String SV_MOD_DIRECT	= "SVModDirect".toLowerCase();//--for supervisor who modifies some info in CRA

	public static final String SV_LCK_REQ 		= "SVLckReq".toLowerCase(); //req or refresh	
	public static final String SV_LCK_SAV 		= "SVLckSav".toLowerCase(); //save and continue
	public static final String SV_LCK_END 		= "SVLckEnd".toLowerCase();
	public static final String SV_LCK_DEL 		= "SVLckDel".toLowerCase();

	public static final String SV_LST_CAT 		= "SVLstCat".toLowerCase();
	public static final String SV_WORKING_DATE	= "SVWorkingDate".toLowerCase();
	public static final String SV_SAVE_FILES 	= "SVSaveFiles".toLowerCase();

	public static final String SV_LST_BY_MONTH	= "SVLstByMonth".toLowerCase();
	//---------------------------------------------------------------------------------------------
	//-------------------------Default Constructor - Required -------------------------------------
	public ServiceJobReport(){

		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}
	public static final Integer	ENT_TYP				= DefDBExt.ID_TA_JOB_REPORT;
	//----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		//ServerLogTool.doLogInf("--------- "+ SV_CLASS+ ".doService --------------");

		if (filePath	==null) filePath		= API.reqContextParameter("JOB_REPORT_PATH_FILE");
		if (urlPath		==null) urlPath			= API.reqContextParameter("JOB_REPORT_PATH_URL");	

		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");

		try {
			//---------------------------------------------------------------------------------
			if(sv.equals(SV_INFO_USER) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_JOB_REPORT_GET)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doInfoUser(user, json, response);
			} else if(sv.equals(SV_LST_USER_DYN)		&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_JOB_REPORT_GET)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLstUserDyn(user, json, response);

			} else if(sv.equals(SV_GET)					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_JOB_REPORT_GET)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGet(user, json, response);

			} else if(sv.equals(SV_LST)					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_JOB_REPORT_GET)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLst(user, json, response);
			} else if(sv.equals(SV_LST_DYN)				&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_JOB_REPORT_GET)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLstDyn(user, json, response);
			} else if(sv.equals(SV_LST_DYN_MAN)			&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_JOB_REPORT_GET)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLstDynMan(user, json, response);	

			} else if(sv.equals(SV_LST_BY_USER)			&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_JOB_REPORT_GET)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLstByUser(user, json, response);

			} else if(sv.equals(SV_NEW)					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_NEW, APIAuth.R_JOB_REPORT_NEW)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doNew(user, json, response);
			} else if(sv.equals(SV_MOD)					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_JOB_REPORT_MOD)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doMod(user, json, response, false);
			} else if(sv.equals(SV_MOD_DIRECT)			&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_JOB_REPORT_MOD)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doModDirect(user, json, response);
			} else  if(sv.equals(SV_DEL)				&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_DEL, APIAuth.R_JOB_REPORT_DEL)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doDel(user, json, response);

			} else if(sv.equals(SV_LCK_REQ)				&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_NEW, APIAuth.R_JOB_REPORT_NEW)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLckReq(user, json, response);
			} else if(sv.equals(SV_LCK_SAV)				&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_JOB_REPORT_MOD)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLckSav(user, json, response);
			} else if(sv.equals(SV_LCK_END)				&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_JOB_REPORT_MOD)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLckEnd(user, json, response);
			} else if(sv.equals(SV_LCK_DEL)				&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_DEL, APIAuth.R_JOB_REPORT_DEL)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLckDel(user, json, response);	

			} else if(sv.equals(SV_LST_CAT)				&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_JOB_REPORT_GET)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGetCatValues(user, json, response);		
			} else if(sv.equals(SV_WORKING_DATE)		&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_JOB_REPORT_MOD)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doUpdateUserWd(user, json, response);		
			} else if(sv.equals(SV_SAVE_FILES)			&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_JOB_REPORT_MOD)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doSaveFiles(user, json, response);	
			} else if(sv.equals(SV_LST_BY_MONTH)		&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_JOB_REPORT_GET)
														||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doJobDReportLstByMonth(user, json, response);
			}  else {
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
	//JOB REPORT INFO USER------JOB REPORT INFO USER------JOB REPORT INFO USER------JOB REPORT INFO USER------J 								
	//---------------------------------------------------------------------------------------------------------

	private static void doInfoUser(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");

		Integer 			uId			= ToolData.reqInt	(json, "uId02", user.reqId());
		String				codeRp		= ToolData.reqStr	(json, "codeRp", null);
		ViJobReportUser 	infoUser 	= ViJobReportUser.reqActorInfo(uId, codeRp);

		if (infoUser==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		if (!canWorkWithObj(user, WORK_GET, infoUser)){
			API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, infoUser 
				));	
	}

	//---------------------------------------------------------------------------------------------------------
	//JOB REPORT LIST------JOB REPORT LIST------JOB REPORT LIST------JOB REPORT LIST------JOB REPORT LIST------ 								
	//---------------------------------------------------------------------------------------------------------

	private static void doGet(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");

		TaJobReport 	rp = reqGet (user, json); //and other params if necessary
		if (rp==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, rp 
				));				
	}

	private static TaJobReport reqGet(TaAutUser user,  JSONObject json) throws Exception{
		Integer 			entId	= ToolData.reqInt	(json, "id"	, null	);
		if (entId == null) return null;

		TaJobReport 		ent 	= TaJobReport.DAO.reqEntityByRef(entId);
		ent.doBuildListReportDetail	(true);
		ent.doBuildListReportResume	(true);
		ent.doBuildPerCreateAndOwner(true);
		ent.doBuildPerValidate		(true);
		ent.doBuildFiles			(true);
		return ent;
	}

	//---------------------------------------------------------------------------------------------------------
	//JOB REPORT LIST------JOB REPORT LIST------JOB REPORT LIST------JOB REPORT LIST------JOB REPORT LIST------ 								
	//---------------------------------------------------------------------------------------------------------

	private static void doLst(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");

		List<TaJobReport> 	list = reqLst(user, json); //and other params if necessary
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

	private static List<TaJobReport> reqLst(TaAutUser user, JSONObject json, Object...params) throws Exception {
		Integer 			objMan		= ToolData.reqInt	(json, "manId"	, null	);
		Integer 			objTyp		= ToolData.reqInt	(json, "typ"		, null	);
		Boolean 			objContBuild= ToolData.reqBool	(json, "withBuild"	, false	);

		//other params here
		if (!canWorkWithObj(user, WORK_LST, objTyp)){ //other param after objTyp...
			return null;
		}

		Criterion 			cri		= reqCriterion (objTyp); //and other params	
		List<TaJobReport> 	list 	= null;
		if (cri==null) 
			list =   TaJobReport.DAO.reqList();
		else
			list =   TaJobReport.DAO.reqList(cri);

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
			//cri 		= Restrictions.and(cri, Restrictions.eqOrIsNull(TaJobReport.ATT_I_TYPE, type));
		}			

		if (params!=null && params.length>1){
			//do something
		}

		return cri;
	}

	private static void doLstByUser(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");

		List<TaJobReport> 	list = reqLstByUser(user, json); //and other params if necessary
		if (list==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, list 
				));				
	}

	private static List<TaJobReport> reqLstByUser(TaAutUser user, JSONObject json, Object...params) throws Exception {
		Integer 			objMan		= ToolData.reqInt	(json, "manId"	, null	);
		Integer 			objTyp		= ToolData.reqInt	(json, "typ"		, null	);
		Boolean 			objContBuild= ToolData.reqBool	(json, "withBuild"	, false	);

		//other params here
		if (!canWorkWithObj(user, WORK_LST, objTyp)){ //other param after objTyp...
			return null;
		}

		Integer manId = user.reqPerManagerId();
		Integer uId   = user.reqId();

		Criterion 			cri		= reqCriterion (uId, manId); 
		List<TaJobReport> 	list 	= null;
		if (cri==null) 
			list =   TaJobReport.DAO.reqList();
		else
			list =   TaJobReport.DAO.reqList(cri);

		if (params!=null){
			//do something with list before return
		}	

		//do something else with request
		if (objContBuild){

		}
		return list;
	}

	private static Criterion reqCriterion(Integer uId, Integer manId) throws Exception{
		Criterion cri = Restrictions.gt("I_ID", 0);	

		if (manId!=null){
			cri 		= Restrictions.and(cri, Restrictions.eq(TaJobReport.ATT_I_PER_MANAGER, manId));
		}	

		if (uId!=null){
			cri 		= Restrictions.and(cri, Restrictions.eq(TaJobReport.ATT_I_AUT_USER_01, uId));
		}

		return cri;
	}

	//---------------------------------------------------------------------------------------------------------
	//JOB REPORT LIST DYNAMIC------JOB REPORT LIST DYNAMIC------JOB REPORT LIST DYNAMIC------JOB REPORT LIST DY
	//---------------------------------------------------------------------------------------------------------

	private static Long countAllEle = null;
	private static void doLstDyn(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {	
		Object[]  			dataTableOption = ToolDatatable.reqDataTableOption (json, null);				
		Set<String>			searchKey		= (Set<String>)dataTableOption[0];	

		Integer				uId02			= ToolData.reqInt	(json, "uId02", user.reqId());
		String				code			= ToolData.reqStr	(json, "code" , null);
		Integer				stat			= ToolData.reqInt	(json, "stat" , null);

		if (!canWorkWithObj(user, WORK_LST, uId02, code, stat)){ //other param after objTyp...
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		Criterion 	cri 			= reqRestriction(searchKey, user.reqPerManagerId(), null, uId02, code, stat,null);
		//CRI: SearchKey - userId03 (Cra Owner) - Code (Cra Date) - Stat (Cra status)

		List<TaJobReport> list 		= reqListDyn(dataTableOption, cri);	
		if (list==null ){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		} else {
			TaJobReport.doBuildInfo(list);

			//			for (TaJobReport rp : list){
			//				rp.doBuildListReportDetail(true);
			//				rp.doBuildPerCreateAndOwner(true);
			//				rp.doBuildPerValidate(true);
			//				rp.doBuildFiles(true);
			//			}
		}

		if (countAllEle==null)
			countAllEle = ((long)reqNbJobReportListDyn());

		Integer iTotalRecords 			= (countAllEle.intValue());				
		Integer iTotalDisplayRecords 	= reqNbJobReportListDyn(cri).intValue();

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,					
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, list
				));
	}

	private static List<TaJobReport> reqListDyn(Object[] dataTableOption, Criterion cri) throws Exception {		
		int 		begin 		= (int)	dataTableOption[1];
		int 		number 		= (int)	dataTableOption[2]; 
		int 		sortCol 	= (int)	dataTableOption[3]; 
		int 		sortTyp 	= (int)	dataTableOption[4];	

		List<TaJobReport> list 	= new ArrayList<TaJobReport>();		

		Order 	order 	= null;			
		String 	colCode = null;

		//		switch(sortCol){
		//		case 0: colCode = TaJobReport.ATT_T_CODE_01; break;		
		//		case 1: colCode = TaJobReport.ATT_T_CODE_01; break;					
		//		}

		if (colCode!=null){
			switch(sortTyp){
			case 0: order = Order.asc (colCode); break;
			case 1: order = Order.desc(colCode); break;								
			}
		}

		if(cri==null){
			cri = Restrictions.gt(TaJobHoliday.ATT_I_ID, 0);
		}

		if (order==null)
			list	= TaJobReport.DAO.reqList(begin, number, Order.desc(TaJobReport.ATT_T_INFO_01), cri);
		else
			list	= TaJobReport.DAO.reqList(begin, number, order, cri);			

		return list;
	}

	private static Criterion reqRestriction(Set<String> searchKey, Integer manId, Integer userManager, Integer uId02, String code, Integer stat,Set<Integer> stats) throws Exception {		
		Criterion cri = Restrictions.gt(TaJobReport.ATT_I_ID, 0);

		for (String s : searchKey){ //Search TCODE only
			if (s==null || s.length()==0 || s.equals("%")) continue;
			cri = 	Restrictions.and(	cri, Restrictions.ilike(TaJobReport.ATT_T_CODE_01	, s));
		}

		if(manId != null){
			cri = Restrictions.and(cri, Restrictions.eq(TaJobReport.ATT_I_PER_MANAGER, manId));
		} 

		if(userManager != null){
			cri = Restrictions.and(cri, Restrictions.eq(TaJobReport.ATT_I_AUT_USER_03, userManager));
		} 


		//CRI: SearchKey - uId02 (Cra Owner) - Code (Cra Date) - Stat (Cra status)
		if(uId02 != null){
			cri = Restrictions.and(cri, Restrictions.eq(TaJobReport.ATT_I_AUT_USER_02, uId02));
		} 
		if(code != null){			
			cri = Restrictions.and(cri, Restrictions.eq(TaJobReport.ATT_T_CODE_01, code));			
		}
		if(stat != null){
			cri = Restrictions.and(cri, Restrictions.eq(TaJobReport.ATT_I_STATUS, stat));
		}	
		if(stats != null && stats.size() > 0){
			cri = Restrictions.and(cri, Restrictions.in(TaJobReport.ATT_I_STATUS, stats));
		}	
		//			if(stat == TaJobReport.PR_REPORT_STAT_VALIDATE){
		//				cri = Restrictions.and(cri, Restrictions.ge(TaJobReport.ATT_I_STATUS, stat));
		//				//Get Validate, Denied and Resume Updated Status
		//			} else {
		//				
		//				//Get pending only
		//			}		
		//		else {
		//			cri = Restrictions.and(cri, Restrictions.eq(TaJobReport.ATT_I_STATUS, TaJobReport.STAT_DRAFT));
		//		}
		return cri;
	}

	private static Number reqNbJobReportListDyn() throws Exception {						
		return TaJobReport.DAO.reqCount();		
	}

	private static Number reqNbJobReportListDyn(Criterion cri) throws Exception {
		return TaJobReport.DAO.reqCount(cri);
	}



	private static void doLstDynMan(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {	
		Object[]  			dataTableOption = ToolDatatable.reqDataTableOption (json, null);				
		Set<String>			searchKey		= (Set<String>)dataTableOption[0];	

		Integer				manId			= user.reqPerManagerId();
		Integer				uId02			= ToolData.reqInt	(json, "uId02", null );
		String				code			= ToolData.reqStr	(json, "code" , null);
		Integer				stat			= ToolData.reqInt	(json, "stat" , null);

		if (!canWorkWithObj(user, WORK_LST, manId, uId02, code, stat)){ //other param after objTyp...
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		Set<Integer>	stats	= new HashSet<Integer>() ;

		if(stat == null) {
			stats.add(TaJobReport.STAT_VALIDATE);
			stats.add(TaJobReport.STAT_DENY);
		}

		Criterion 	cri 			= reqRestriction(searchKey, manId, user.reqId(), uId02, code, stat,stats);
		//CRI: SearchKey - userId03 (Cra Owner) - Code (Cra Date) - Stat (Cra status)

		List<TaJobReport> list 		= reqListDyn(dataTableOption, cri);	
		if (list==null ){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		} else {
			TaJobReport.doBuildInfo(list);

			//			for (TaJobReport rp : list){
			//				rp.doBuildListReportDetail(true);
			//				rp.doBuildPerCreateAndOwner(true);
			//				rp.doBuildPerValidate(true);
			//				rp.doBuildFiles(true);
			//			}
		}

		if (countAllEle==null)
			countAllEle = ((long)reqNbJobReportListDyn());

		Integer iTotalRecords 			= (countAllEle.intValue());				
		Integer iTotalDisplayRecords 	= reqNbJobReportListDyn(cri).intValue();

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,					
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, list
				));
	}

	//---------------------------------------------------------------------------------------------------------
	//JOB REPORT LIST USER DYNAMIC------JOB REPORT LIST USER DYNAMIC------JOB REPORT LIST USER DYNAMIC------JOB
	//---------------------------------------------------------------------------------------------------------

	private static void doLstUserDyn(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {	
		Object[]  			dataTableOption = ToolDatatable.reqDataTableOption (json, null);				

		Integer				uManager		= user.reqId();
		String				codeRp			= ToolData.reqStr	(json, "codeRp", null);

		//Temporary Get All PerNatural
		String idCompany = null;

		if (!canWorkWithObj(user, WORK_LST, idCompany)){ //other param after objTyp...
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		List<ViJobReportUser> listUserInfo 	= reqListUserDyn(dataTableOption, uManager, codeRp);	

		if (listUserInfo==null ){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		Integer iTotalRecords 			= listUserInfo.size();		
		int iTotalDisplayRecords 		= reqNbJobReportListUserDyn(dataTableOption, uManager,null);

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,					
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, listUserInfo
				));
	}

	private static List<ViJobReportUser> reqListUserDyn(Object[] dataTableOption, Integer userManager, String codeRp) throws Exception {
		Set<String>		searchKey		= (Set<String>)dataTableOption[0];
		int 			begin 			= (int)	dataTableOption[1];
		int 			number 			= (int)	dataTableOption[2];

		List<ViJobReportUser> list 		= new ArrayList<ViJobReportUser>();
		list	= ViJobReportUser.reqListActorInfo(userManager, codeRp, begin, number, searchKey, TaAutUser.STAT_ACTIVE);

		return list;
	}

	private static int reqNbJobReportListUserDyn(Object[] dataTableOption, Integer uManId, Integer userId) throws Exception {	
		Set<String>		searchKey		= (Set<String>)dataTableOption[0];
		return ViJobReportUser.reqCountLstUser(searchKey, uManId, userId, TaAutUser.STAT_ACTIVE);		
	}

	//---------------------------------------------------------------------------------------------------------
	//JOB REPORT NEW------JOB REPORT NEW------JOB REPORT NEW------JOB REPORT NEW------JOB REPORT NEW------JOB R
	//---------------------------------------------------------------------------------------------------------

	private static void doNew(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		TaJobReport 			ent		= reqNew		(user, json);
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

	private static TaJobReport reqNew(TaAutUser user,  JSONObject json) throws Exception {
		JSONObject				obj		= ToolData.reqJson (json, "obj", null);
		Map<String, Object> 	attr 	= API.reqMapParamsByClass(obj	, TaJobReport.class);
		TaJobReport  			ent	 	= new TaJobReport(attr);

		if (!canWorkWithObj(user, WORK_NEW, ent)){ //other param after obj...
			return null;
		}

		Integer uId	= user.reqId();
		TaAutUser ownerUser = user;

		ent.reqSet(TaJobReport.ATT_D_DATE_01	, new Date());
		ent.reqSet(TaJobReport.ATT_I_AUT_USER_01, uId);
		ent.reqSet(TaJobReport.ATT_I_PER_MANAGER, user.req(TaAutUser.ATT_I_PER_MANAGER));


		Integer owner 	  = (Integer) ent.req(TaJobReport.ATT_I_AUT_USER_02);
		if (owner ==null) owner = user.reqId(); //by default : adm
		ent.reqSet(TaJobReport.ATT_I_AUT_USER_02, owner);

		if (!owner.equals(uId)) {
			ownerUser = TaAutUser.DAO.reqEntityByRef(owner);
		}


		String code 	  = (String) ent.req(TaJobReport.ATT_T_CODE_01);

		List<TaJobReport> chk		= TaJobReport.DAO.reqList(
				Restrictions.and(
						Restrictions.eq(TaJobReport.ATT_I_AUT_USER_02, owner), 
						Restrictions.eq(TaJobReport.ATT_T_CODE_01, code)));
		if (chk!=null && chk.size()>0) return null; //each month only one

		Integer supervisor = (Integer) ownerUser.req(TaAutUser.ATT_I_AUT_USER_03);
		Integer manager    = (Integer) ownerUser.req(TaAutUser.ATT_I_PER_MANAGER);
		if (supervisor == null && manager.equals(1)) supervisor = 1; //by default : adm
		ent.reqSet(TaJobReport.ATT_I_AUT_USER_03, supervisor);

		TaJobReport.DAO.doPersist(ent);

		Integer        reportId    = (Integer) ent.req(TaJobReport.ATT_I_ID);

		//Update attach files
		JSONArray 		files 	   = ToolData.reqJsonArr (obj, "files", null); ;
		List<TaTpyDocument> lstDoc = TaTpyDocument.reqListCheck(DefAPI.SV_MODE_NEW, ownerUser,DefDBExt.ID_TA_JOB_REPORT, reportId, files);

		//build other objects from obj and request
		List<TaJobReportDetail> lstRpDetail = reqModReportDetail(reportId, ToolData.reqJsonArr(obj, "lstRp", null));
		List<TaJobReportResume> lstRpResume = reqModReportResume(reportId, ToolData.reqJsonArr(obj, "lstRpResume", null));

		ent.reqSet(TaJobReport.ATT_O_DOCUMENTS			, lstDoc);
		ent.reqSet(TaJobReport.ATT_O_LIST_REPORT_DETAIL	, lstRpDetail);
		ent.reqSet(TaJobReport.ATT_O_LIST_REPORT_RESUME	, lstRpResume);

		ent.doBuildPerCreateAndOwner(true);

		return ent;
	}

	//	private static TaJobReport reqNewReportDetail(TaJobReport ent, JSONArray lstRpJson) throws Exception{
	//		if (lstRpJson==null) 					lstRpJson 	= new JSONArray();
	//		List<TaJobReportDetail> lstRp = new ArrayList<TaJobReportDetail>();
	//		
	//		for(int i = 0; i < lstRpJson.size(); i++) {
	//			JSONObject 			o 		= (JSONObject) lstRpJson.get(i);
	//			if(o == null) continue;
	//			Map<String, Object> mapObj 	= API.reqMapParamsByClass(o, TaJobReportDetail.class);
	//			TaJobReportDetail newObj 	= new TaJobReportDetail(mapObj);
	//			newObj.reqSet(TaJobReportDetail.ATT_I_JOB_REPORT, ent.req(TaJobReport.ATT_I_ID));
	//			newObj.reqSet(TaJobReportDetail.ATT_I_ID, null);
	//			newObj.reqSet(TaJobReportDetail.ATT_D_DATE, new Date());
	//			lstRp.add(newObj);
	//		}
	//		
	//		if(lstRp.size()>0){
	//			TaJobReportDetail.DAO.doPersist(lstRp);
	//		}
	//		
	//		return ent;
	//	}

	//---------------------------------------------------------------------------------------------------------
	//JOB REPORT MOD------JOB REPORT MOD------JOB REPORT MOD------JOB REPORT MOD------JOB REPORT MOD------JOB R
	//---------------------------------------------------------------------------------------------------------

	private static void doMod(TaAutUser user, JSONObject json, HttpServletResponse response, boolean directSave) throws Exception {
		TaJobReport ent	= null;
		if(directSave){
			ent	= reqModDirect(user, json);
		} else {
			ent	= reqMod(user, json);
		}

		if (ent==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
		} else {
			ent.doBuildListReportDetail(false);
			ent.doBuildPerCreateAndOwner(false);
			ent.doBuildPerValidate(false);
			ent.doBuildFiles(false);

			API.doResponse(response,ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, 1,
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, ent
					));	
		}		
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
//			API.doResponse(response, ToolJSON.reqJSonString(						
//					DefJS.SESS_STAT		, 1, 
//					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES
//					));	
		}				
	}


	private static TaJobReport reqMod(TaAutUser user,  JSONObject json) throws Exception {
		JSONObject				obj		= ToolData.reqJson (json, "obj", null);
		Map<String, Object> 	attr 	= API.reqMapParamsByClass(json, TaJobReport.class);
		
		int reportId 			= ToolData.reqInt (obj, "id", null);
		TaJobReport  ent	 	= TaJobReport.DAO.reqEntityByRef(reportId);

		if (ent==null){
			return null;
		}

		if (!canWorkWithObj(user, WORK_MOD, ent)){ //other param after obj...
			return null;
		}

		attr.remove	(TaJobReport.ATT_I_AUT_USER_01);
		attr.remove	(TaJobReport.ATT_I_AUT_USER_02);
		attr.remove	(TaJobReport.ATT_I_AUT_USER_03);
		attr.remove	(TaJobReport.ATT_D_DATE_01);

		Integer supervisor = (Integer) user.req(TaAutUser.ATT_I_AUT_USER_03);
		Integer manager    = (Integer) user.req(TaAutUser.ATT_I_PER_MANAGER);
		if (supervisor == null && manager.equals(1)) supervisor = 1; //by default : adm

		attr.put(TaJobReport.ATT_I_AUT_USER_03, supervisor);
		attr.put(TaJobReport.ATT_D_DATE_02, new Date());

		TaJobReport.DAO.doMerge(ent, attr);	

		//Update attach files
		JSONArray 				files 	   	= ToolData.reqJsonArr (json, "files", new JSONArray());
		List<TaTpyDocument> 	lstDoc 		= TaTpyDocument.reqListCheck(DefAPI.SV_MODE_MOD, user, DefDBExt.ID_TA_JOB_REPORT, reportId, files); 

		//merge other objects from obj and request
		List<TaJobReportDetail> lstRpDetail = reqModReportDetail(reportId, ToolData.reqJsonArr(obj, "lstRp", null));
		List<TaJobReportResume> lstRpResume = reqModReportResume(reportId, ToolData.reqJsonArr(obj, "lstRpResume", null));

		ent.reqSet(TaJobReport.ATT_O_DOCUMENTS			, lstDoc);
		ent.reqSet(TaJobReport.ATT_O_LIST_REPORT_DETAIL	, lstRpDetail);
		ent.reqSet(TaJobReport.ATT_O_LIST_REPORT_RESUME	, lstRpResume);

		ent.doBuildPerCreateAndOwner(true);

		return ent;
	}

	private static TaJobReport reqModDirect(TaAutUser user,  JSONObject json) throws Exception {
		int 	reportId		= ToolData.reqInt	(json, "reportId"	, null);
		int 	newStatus 		= ToolData.reqInt	(json, "stat"		, null);
		String 	cmt				= ToolData.reqStr	(json, "cmt"		, null);
		String 	message			= ToolData.reqStr	(json, "message"	, null);
		TaJobReport  ent	 	= TaJobReport.DAO.reqEntityByRef(reportId);

		if (ent==null){
			return null;
		}

		if (!canWorkWithObj(user, WORK_MOD, ent)){ //other param after obj...
			return null;
		}

		ent.reqSet(TaJobReport.ATT_I_STATUS, newStatus);

		if(newStatus == TaJobReport.STAT_DRAFT){
			ent.reqSet(TaJobReport.ATT_D_DATE_02, null);
		}

		if(newStatus == TaJobReport.STAT_PENDING){
			doSendEmailManagerJobReport(user, ent);
		}

		if(newStatus == TaJobReport.STAT_VALIDATE){
			ent.reqSet(TaJobReport.ATT_D_DATE_02, new Date());
			ent.reqSet(TaJobReport.ATT_I_AUT_USER_03, user.req(TaAutUser.ATT_I_ID));
		}
		if(newStatus == TaJobReport.STAT_DENY){
			ent.reqSet(TaJobReport.ATT_D_DATE_02, new Date());
			ent.reqSet(TaJobReport.ATT_I_AUT_USER_03, user.req(TaAutUser.ATT_I_ID));
			doSendEmailJobReportRefuse((int)ent.req(TaJobReport.ATT_I_AUT_USER_01),message);
		}

		ent.reqSet(TaJobReport.ATT_T_INFO_01, cmt);

		TaJobReport.DAO.doMerge(ent);
		return ent;
	}

	public static List<TaJobReportDetail> reqModReportDetail(Integer parentID,  JSONArray lstRp) throws Exception {
		if (lstRp==null) 					lstRp 	= new JSONArray();

		List		<TaJobReportDetail> 			lstMod 		= new ArrayList<TaJobReportDetail >();
		List		<Map<String, Object>> 			lstModVal 	= new ArrayList<Map<String, Object>> 	();
		List		<TaJobReportDetail> 			lstNew 		= new ArrayList<TaJobReportDetail > ();
		Collection	<TaJobReportDetail> 			lstDel 		= null;
		Collection	<TaJobReportDetail>  			lstObj 		= TaJobReportDetail.DAO.reqList(Restrictions.eq(TaJobReportDetail.ATT_I_JOB_REPORT, parentID));		

		HashMap		<Integer,TaJobReportDetail> 	map 		= new HashMap<Integer,TaJobReportDetail>();
		if (lstObj!=null){
			for(TaJobReportDetail d:lstObj){
				Integer id = (Integer) d.req(TaJobReportDetail.ATT_I_ID);
				map.put(id, d);			 
			}
		}

		for(int i = 0; i < lstRp.size(); i++) {
			JSONObject 			o 		= (JSONObject) lstRp.get(i);
			if(o == null) continue;
			Map<String, Object> attr 	= API.reqMapParamsByClass(o, TaJobReportDetail.class);			

			Integer 			id		= (Integer) attr.get(TaJobReportDetail.ATT_I_ID);		
			if (id!=null && map.containsKey(id)){				
				attr.remove(TaJobReportDetail.ATT_I_JOB_REPORT);	
				lstMod		.add	(map.get(id));					
				lstModVal	.add	(attr);
				map			.remove	(id);
			}else{
				TaJobReportDetail poO	= new TaJobReportDetail(API.reqMapParamsByClass(o, TaJobReportDetail.class));		
				poO.reqSet(TaJobReportDetail.ATT_I_ID			, null);
				poO.reqSet(TaJobReportDetail.ATT_I_JOB_REPORT	, parentID);				
				lstNew		.add	(poO);
			}
		}

		if (map.size()>0){
			lstDel = map.values();
		}

		Session sess = TaMatUnit.DAO.reqSessionCurrent();
		try {
			TaJobReportDetail.DAO.doMerge		(sess, lstMod, lstModVal);
			TaJobReportDetail.DAO.doPersist		(sess, lstNew);
			TaJobReportDetail.DAO.doRemove		(sess, lstDel);
			TaJobReportDetail.DAO.doSessionCommit	(sess);

			lstMod.addAll(lstNew);
			return lstMod;
		}catch(Exception e){
			e.printStackTrace();
			TaMatUnit.DAO.doSessionRollback(sess);
			return null;
		}
	}

	public static List<TaJobReportResume> reqModReportResume(Integer parentID,  JSONArray lstRp) throws Exception {
		if (lstRp==null) 							lstRp 		= new JSONArray();

		List		<TaJobReportResume> 			lstMod 		= new ArrayList<TaJobReportResume >();
		List		<Map<String, Object>> 			lstModVal 	= new ArrayList<Map<String, Object>> 	();
		List		<TaJobReportResume> 			lstNew 		= new ArrayList<TaJobReportResume > ();
		Collection	<TaJobReportResume> 			lstDel 		= null;
		Collection	<TaJobReportResume>  			lstObj 		= TaJobReportResume.DAO.reqList(Restrictions.eq(TaJobReportResume.ATT_I_JOB_REPORT, parentID));		

		HashMap		<Integer,TaJobReportResume> 	map 		= new HashMap<Integer,TaJobReportResume>();
		if (lstObj!=null){
			for(TaJobReportResume d:lstObj){
				Integer id = (Integer) d.req(TaJobReportResume.ATT_I_ID);
				map.put(id, d);			 
			}
		}

		for(int i = 0; i < lstRp.size(); i++) {
			JSONObject 			o 		= (JSONObject) lstRp.get(i);
			if(o == null) continue;

			Map<String, Object> attr 	= API.reqMapParamsByClass(o, TaJobReportResume.class);					
			Integer 			id		= (Integer) attr.get(TaJobReportResume.ATT_I_ID);		
			if (id!=null && map.containsKey(id)){				
				attr.remove(TaJobReportResume.ATT_I_JOB_REPORT);	
				lstMod		.add	(map.get(id));					
				lstModVal	.add	(attr);
				map			.remove	(id);
			}else{
				TaJobReportResume poO	= new TaJobReportResume(API.reqMapParamsByClass(o, TaJobReportResume.class));						
				poO.reqSet(TaJobReportResume.ATT_I_JOB_REPORT	, parentID);
				poO.reqSet(TaJobReportResume.ATT_I_ID		, null);
				lstNew		.add	(poO);
			}
		}

		if (map.size()>0){
			lstDel = map.values();
		}

		Session sess = TaMatUnit.DAO.reqSessionCurrent();
		try {
			TaJobReportResume.DAO.doMerge			(sess, lstMod, lstModVal);
			TaJobReportResume.DAO.doPersist			(sess, lstNew);
			TaJobReportResume.DAO.doRemove			(sess, lstDel);
			TaJobReportResume.DAO.doSessionCommit	(sess);

			lstMod.addAll(lstNew);
			return lstMod;
		}catch(Exception e){
			e.printStackTrace();
			TaMatUnit.DAO.doSessionRollback(sess);
			return null;
		}
	}

	//---------------------------------------------------------------------------------------------------------
	//JOB REPORT DEL------JOB REPORT DEL------JOB REPORT DEL------JOB REPORT DEL------JOB REPORT DEL------JOB R
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

		TaJobReport  	ent	 	= TaJobReport.DAO.reqEntityByRef(objId);
		if (ent==null){
			return false;
		}

		if (!canWorkWithObj(user, WORK_DEL, ent)){ //other param after ent...
			return false;
		}		


		Session sessMain 	= TaJobReport		.DAO.reqSessionCurrent();
		try {
			TaTpyDocument		.doListDel	(sessMain, ENT_TYP, ent.reqId());
			TaJobReport.DAO.doRemove(sessMain, ent);	
			return true;
		}catch(Exception e){
			e.printStackTrace();
			TaJobReport			.DAO.doSessionRollback(sessMain);
			//			TaTpyDocument		.DAO.doSessionRollback(sessSub);
			return false;
		}	


	}
	//-----------send-mai--------------
	private static String EMAIL_HOST 		= "";
	private static String EMAIL_PORT 		= "";
	private static String EMAIL_LOGIN 		= "";
	private static String EMAIL_PWD 		= "";

	private static final String TAB_REPORT 		= "report";
	private static final int TYP_RECEIVE 		= 12;

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
	public static void doSendEmailManagerJobReport(TaAutUser user, TaJobReport ent){
		Thread t = new Thread(){
			public void run(){
				try{
					JSONObject 	cfg  = reqConfig ("TA_CFG_PRJ_EMAIL");
					if (cfg==null) return;
					
					String EMAIL_JOB_REPORT_TITLE 		= (String) cfg.get("prj_email_job_monthreport_title" 	);
					String EMAIL_JOB_REPORT_CONT 		= (String) cfg.get("prj_email_job_monthreport_content"	);
					Integer EMAIL_JOB_REPORT_CONT_NB 	= Integer.parseInt((String) cfg.get("prj_email_job_monthreport_content"	));


					String        	pCode   = ent.req(TaJobReport.ATT_T_CODE_01).toString();

					Integer       	perId  	= (Integer)user.req(TaAutUser.ATT_I_PER_PERSON);
					TaPerPerson	  	per		= TaPerPerson.DAO.reqEntityByValue(TaPerPerson.ATT_I_ID, perId);
					String			namePer	= (String) per.req(TaPerPerson.ATT_T_NAME_01) + " " + per.req(TaPerPerson.ATT_T_NAME_02) + " " + per.req(TaPerPerson.ATT_T_NAME_03);

					Integer 		uId 	= (Integer) user.reqId();
					TaAutUser 		u 		= TaAutUser.DAO.reqEntityByID(uId);
					String 			email 	= u.reqStr(TaAutUser.ATT_T_INFO_01);

					if (email!=null && email.length()>0) {
						String emailTitle 	= EMAIL_JOB_REPORT_TITLE;
						String emailCont 	= reqFormattedContent(EMAIL_JOB_REPORT_CONT, pCode + "_" + namePer.trim(), EMAIL_JOB_REPORT_CONT_NB); 

						ToolEmail.canSendEmail(
								EMAIL_HOST, EMAIL_PORT, null, EMAIL_LOGIN, EMAIL_PWD, 
								EMAIL_LOGIN, 
								emailTitle, emailCont,
								email, null, null,  null);	

					}

					doNewNotification(ent, user, TAB_REPORT, TYP_RECEIVE);
				}catch(Exception e){

				}
			}
		};
		ThreadManager.doExecute(t, DefTime.TIME_00_00_05_000);
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
	private static void doNewNotification(TaJobReport rep, TaAutUser user, String table_notif, int typ_notif) throws Exception {
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
		notify.put("parID"		, rep.req(TaJobReport.ATT_I_ID));

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
	private static void doSendEmailJobReportRefuse(int clientId, String cliMsg) throws Exception{
		JSONObject 	cfg  = reqConfig ("TA_CFG_PRJ_EMAIL");
		if (cfg==null) return;

		String E_DESTINATION = doIdentifyClient(clientId);
		String E_TITLE		 = (String) cfg.get("prj_email_job_monthreport_KO_title" 	);
		String E_CONTENT	 = (String) cfg.get("prj_email_job_monthreport_KO_title" 	);

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


	//---------------------------------------------------------------------------------------------------------
	//JOB REPORT LIST CATEGORY------JOB REPORT LIST CATEGORY------JOB REPORT LIST CATEGORY------JOB REPORT LIST								
	//---------------------------------------------------------------------------------------------------------

	private static void doGetCatValues(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		List<TaTpyCategory> listCat = new ArrayList<>();
		Integer manId = user.reqPerManagerId();
		listCat = TaTpyCategory.reqListByType(DefDBExt.ID_TA_JOB_REPORT, manId);
		if (listCat==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
		} 
		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, listCat
				));
	}

	//---------------------------------------------------------------------------------------------------------
	//UPDATE USER WORKING DATE------UPDATE USER WORKING DATE------UPDATE USER WORKING DATE------UPDATE USER WOR							
	//---------------------------------------------------------------------------------------------------------

	private static void doUpdateUserWd(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		Integer us 		= ToolData.reqInt	(json, "uId03", null);
		String  wD 		= ToolData.reqStr	(json, "wD", null);
		String  wH 		= ToolData.reqStr	(json, "wH", null);

		TaJobDayOffResume res 				= null;
		Criterion 					cri		= Restrictions.eq(TaJobDayOffResume.ATT_I_AUT_USER, us);
		List<TaJobDayOffResume> 	list 	=  TaJobDayOffResume.DAO.reqList(cri);

		if (list==null || list.size()==0){
			res = new TaJobDayOffResume(us, wD, wH);
			res.reqSet(TaJobDayOffResume.ATT_D_DATE_01, new Date());
			TaJobDayOffResume.DAO.doPersist(res);
		} else {
			res = list.get(0);
			res.reqSet(TaJobDayOffResume.ATT_T_INFO_01, wD);
			res.reqSet(TaJobDayOffResume.ATT_T_INFO_02, wH);
			TaJobDayOffResume.DAO.doMerge(res);
		}

		String			codeRp		= ToolData.reqStr	(json, "codeRp");
		ViJobReportUser infoUser	= ViJobReportUser.reqActorInfo(us, codeRp);

		if (infoUser==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, infoUser
				));
	}

	//---------------------------------------------------------------------------------------------------------
	//JOB REPORT LOCK------JOB REPORT LOCK------JOB REPORT LOCK------JOB REPORT LOCK------JOB REPORT LOCK------								
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

		TaJobReport  		ent	 	=  reqMod(user, json); 								
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

		TaJobReport ent = reqMod(user, json);						
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
		TaJobReport 			ent		= reqSaveFiles		(user, json);
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

	private static TaJobReport reqSaveFiles(TaAutUser user,  JSONObject json) throws Exception {
		JSONObject			obj		= ToolData.reqJson(json, "obj", new JSONObject());

		if (!canWorkWithObj(user, WORK_NEW, obj)){ //other param after obj...
			return null;
		}

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaJobReport.class);

		Integer 			prjID 	= (Integer) attr.get(TaJobReport.ATT_I_ID);
		if(prjID == null)	return null;

		TaJobReport  		ent	 	= TaJobReport.DAO.reqEntityByRef(prjID);
		if(ent == null)	return null;		

		JSONArray			files		= ToolData.reqJsonArr(json, "files", new JSONArray());
		List<TaTpyDocument> lstDoc = TaTpyDocument.reqListCheck(DefAPI.SV_MODE_MOD, user,DefDBExt.ID_TA_JOB_REPORT, ent.reqId(), files);
		ent.reqSet(TaJobReport.ATT_O_DOCUMENTS, lstDoc);

		return ent;
	}
	

	//---------------------------------------------------------------------------------------------------------
	private static void doJobDReportLstByMonth(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");
		List<TaJobReport> res = reqByMonth(user, json); //and other params if necessary

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, res
				));				
	}

	private static List<TaJobReport> reqByMonth(TaAutUser user, JSONObject json, Object...params) throws Exception {	
		String	code		= ToolData.reqStr	(json, "codeReport", null);
		Integer userId 		= user.reqId();		
		Integer manId  		= user.reqPerManagerId();

		List<TaJobReport> listReport = TaJobReport.DAO.reqList(
				Restrictions.eq(TaJobReport.ATT_T_CODE_01             , code),
				Restrictions.eq(TaJobReport.ATT_I_AUT_USER_03      , userId),
				Restrictions.eq(TaJobReport.ATT_I_PER_MANAGER      , manId));

		//		for(TaJobReport r : listReport) {
		//			r.doBuildListReportResume(true);
		//			r.doBuildPerCreateAndOwner(true);
		//		}

		TaJobReport.doBuildInfo(listReport);
		TaJobReport.doBuildReportResume(listReport);

		return listReport;
	}
}
