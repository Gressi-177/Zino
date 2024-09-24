package com.hnv.api.service.priv.job;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.common.util.CacheData;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.job.TaJobHoliday;
import com.hnv.db.sys.TaSysLock;
import com.hnv.def.DefDBExt;

/**
* ----- ServiceJobHoliday by H&V
* ----- Copyright 2017------------
*/
public class ServiceJobHoliday implements IService {
	private static	String 			filePath	= null; 
	private	static	String 			urlPath		= null; 
	

	public static final Integer 	NEW_CONTINUE 			= 1;	
	public static final Integer 	NEW_EXIT 				= 2;
	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 						= "EC_V3".toLowerCase();

	public static final String SV_CLASS 						= "ServiceJobHoliday".toLowerCase();	
	
	public static final String SV_GET 					= "SVGet".toLowerCase();	
	public static final String SV_LST 					= "SVLst".toLowerCase();
	public static final String SV_LST_DYN				= "SVLstDyn".toLowerCase(); 
	public static final String SV_LST_DYN_ALL_PARTNER	= "SVLstDynAllPartner".toLowerCase(); 
	
	public static final String SV_NEW 					= "SVNew".toLowerCase();	
	public static final String SV_MOD 					= "SVMod".toLowerCase();	
	public static final String SV_DEL 					= "SVDel".toLowerCase();

	public static final String SV_LCK_REQ 				= "SVLckReq".toLowerCase(); //req or refresh	
	public static final String SV_LCK_SAV 				= "SVLckSav".toLowerCase(); //save and continue
	public static final String SV_LCK_END 				= "SVLckEnd".toLowerCase();
	public static final String SV_LCK_DEL 				= "SVLckDel".toLowerCase();
	
	public static final String SV_LST_GRID				= "SVLstGrid".toLowerCase(); //pagination
	
		
	//-----------------------------------------------------------------------------------------------
	//-------------------------Default Constructor - Required -------------------------------------
	public ServiceJobHoliday(){
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	public static final Integer	ENT_TYP				= DefDBExt.ID_TA_JOB_HOLIDAY;
	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		//ServerLogTool.doLogInf("--------- "+ SV_CLASS+ ".doService --------------");
		
		if (filePath	==null) filePath		= API.reqContextParameter("JOB_HOLIDAY_PATH_FILE");
		if (urlPath		==null) urlPath			= API.reqContextParameter("JOB_HOLIDAY_PATH_URL");	
						
		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		
		try {
			//---------------------------------------------------------------------------------
			//---------------------------------------------------------------------------------
		
			if(sv.equals(SV_GET) 							&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_JOB_HOLIDAY_GET)
															||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGet(user, json, response);
			} else if(sv.equals(SV_LST)						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_JOB_HOLIDAY_GET)
															||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLst(user, json, response);
			} else if(sv.equals(SV_LST_DYN)					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_JOB_HOLIDAY_GET)
															||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLstDyn(user, json, response);
			} else if(sv.equals(SV_LST_DYN_ALL_PARTNER)		&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_JOB_HOLIDAY_GET)
															||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLstDynAllPartner(user, json, response);
				
			} else if(sv.equals(SV_NEW)						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_NEW, APIAuth.R_JOB_HOLIDAY_NEW)
															||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doNew(user, json, response);
			} else if(sv.equals(SV_MOD)						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_JOB_HOLIDAY_MOD)
															||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doMod(user, json, response);
			} else  if(sv.equals(SV_DEL)					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_DEL, APIAuth.R_JOB_HOLIDAY_DEL)
															||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doDel(user, json, response);
			
			} else if(sv.equals(SV_LCK_REQ)					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_JOB_HOLIDAY_GET)
															||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLckReq(user, json, response);
			} else if(sv.equals(SV_LCK_SAV)					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_JOB_HOLIDAY_MOD)
															||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLckSav(user, json, response);
			} else if(sv.equals(SV_LCK_END)					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_JOB_HOLIDAY_MOD)
															||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLckEnd(user, json, response);
			} else if(sv.equals(SV_LCK_DEL)					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_DEL, APIAuth.R_JOB_HOLIDAY_DEL)
															||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLckDel(user, json, response);		
			} else if(sv.equals(SV_LST_GRID)				&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_JOB_HOLIDAY_GET)
															||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLstGrid(user, json, response);
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
																		
		TaJobHoliday 		ent 	= reqGet(user, objId, forced);
			
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
	private static TaJobHoliday reqGet(TaAutUser user, Integer matId, Boolean forced) throws Exception{
		TaJobHoliday 		ent 	= TaJobHoliday.DAO.reqEntityByRef(matId, forced);
			
		if(ent == null)	return null;

		if(!user.reqPerManagerId().equals(ent.req(TaJobHoliday.ATT_I_PER_MANAGER))) {
			Integer			userType	= (Integer) user.req(TaAutUser.ATT_I_TYPE_01);

			if( userType != TaAutUser.TYPE_01_SUP_ADM ){
				return null;
			}
		}
		
		//---do build something other of ent like details....
		
		ent.doBuildManager(true);
			
		return ent;
	}
	//---------------------------------------------------------------------------------------------------------
	private static void doLst(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");
			
		List<TaJobHoliday> 	list = reqLst(user, json); //and other params if necessary
		
		if (list==null ){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
		
		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
			DefJS.SESS_STAT		, 1,
			DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
			DefJS.RES_DATA		, list 
		));				
	}
	
	private static List<TaJobHoliday> reqLst(TaAutUser user, JSONObject json, Object...params) throws Exception {
		
		Integer manId = user.reqPerManagerId();
		
		String 	codeRp	= ToolData.reqStr	(json, "code", null);
		
		if(codeRp == null) return null;
				
		if (!canWorkWithObj(user, WORK_LST, codeRp)){ //other param after objTyp...
			return null;
		}
				
		Criterion 			cri		= Restrictions.eq(TaJobHoliday.ATT_T_CODE_01, codeRp);
		
		if(manId != null)
			cri = Restrictions.and(	cri, Restrictions.eq(TaJobHoliday.ATT_I_PER_MANAGER, manId));
		
		List<TaJobHoliday> 	list 	=  TaJobHoliday.DAO.reqList(cri);

		return list;
	}
	
	//---------------------------------------------------------------------------------------------------------		
	
	private static Long countAllEle = null;
	private static void doLstDyn(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {	
		Object[]  			dataTableOption = ToolDatatable.reqDataTableOption (json, null);				
		Set<String>			searchKey		= (Set<String>)dataTableOption[0];	
		Integer				typ01			= ToolData.reqInt	(json, "typ01", null);
		Integer				typ02			= ToolData.reqInt	(json, "typ02", null);

		if (!canWorkWithObj(user, WORK_LST, null, typ01, typ02)){ //other param after objTyp...
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
		Integer			userType	= (Integer) user.req(TaAutUser.ATT_I_TYPE_01);
		Integer			manId		= user.reqPerManagerId();

		Criterion 	cri 			= reqRestriction(searchKey, typ01, typ02, manId);				

		List<TaJobHoliday> list 		= reqListDyn(dataTableOption, cri);		
		if (list==null ){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		if (countAllEle==null)
			countAllEle = ((long)reqNbJobHolidayListDyn());

		Integer iTotalRecords 			= (countAllEle.intValue());				
		Integer iTotalDisplayRecords 	= reqNbJobHolidayListDyn(cri).intValue();


		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,					
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, list
				));

	}
	
	private static void doLstDynAllPartner(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {	
		Integer mId = user.reqPerManagerId();
		if (!mId.equals(1) || !(user.canBeAdmin() || user.canBeSuperAdmin() )) {
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
		
		Object[]  			dataTableOption = ToolDatatable.reqDataTableOption (json, null);				
		List<String>		searchKey		= (List<String>)dataTableOption[0];	
		Integer				typ01			= ToolData.reqInt	(json, "typ01", null);
		Integer				typ02			= ToolData.reqInt	(json, "typ02", null);

		if (!canWorkWithObj(user, WORK_LST, null, typ01, typ02)){ //other param after objTyp...
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
		
		Criterion 	cri 				= reqRestrictionAllPartner(searchKey, typ01, typ02, mId);				


		List<TaJobHoliday> list 		= reqListDyn(dataTableOption, cri);		
		if (list==null ){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		if (countAllEle==null)
			countAllEle = ((long)reqNbJobHolidayListDyn());

		Integer iTotalRecords 			= (countAllEle.intValue());				
		Integer iTotalDisplayRecords 	= reqNbJobHolidayListDyn(cri).intValue();


		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,					
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, list
				));

	}
	
	
	private static Criterion reqRestriction(Set<String> searchKey, Integer typ01, Integer typ02, Integer manId) throws Exception {		
		Criterion cri = Restrictions.isNotNull(TaJobHoliday.ATT_I_ID);;
		
		for (String s : searchKey){
			if (cri==null)
				cri = 	Restrictions.or(
							Restrictions.ilike(TaJobHoliday.ATT_T_CODE_01		, s), 
							Restrictions.ilike(TaJobHoliday.ATT_T_CODE_02		, s),
							Restrictions.ilike(TaJobHoliday.COL_T_INFO_01		, s));

			else
				cri = 	Restrictions.and(	cri, 
							Restrictions.or(
								Restrictions.ilike(TaJobHoliday.ATT_T_CODE_01		, s), 
								Restrictions.ilike(TaJobHoliday.ATT_T_CODE_02		, s),
								Restrictions.ilike(TaJobHoliday.COL_T_INFO_01		, s)));
		}	
		
/*		if(typ01 != null){
			cri = Restrictions.and(cri, Restrictions.eq(TaJobHoliday.ATT_I_TYPE_01, typ01));
		} 
		if(typ02 != null){			
			cri = Restrictions.and(cri, Restrictions.eq(TaJobHoliday.ATT_I_TYPE_02, typ02));			
		} 
*/			
		
		if(manId != null)
			cri = Restrictions.and(	cri, Restrictions.eq(TaJobHoliday.ATT_I_PER_MANAGER, manId));
		
		return cri;
	}
	
	private static Criterion reqRestrictionAllPartner(List<String> searchKey, Integer typ01, Integer typ02, Integer manId) throws Exception {		
		Criterion cri = Restrictions.isNotNull(TaJobHoliday.ATT_I_ID);;
		
		for (String s : searchKey){
			if (cri==null)
				cri = 	Restrictions.or(
							Restrictions.ilike(TaJobHoliday.ATT_T_CODE_01		, s), 
							Restrictions.ilike(TaJobHoliday.ATT_T_INFO_01		, s));

			else
				cri = 	Restrictions.and(	cri, 
							Restrictions.or(
								Restrictions.ilike(TaJobHoliday.ATT_T_CODE_01	, s), 
								Restrictions.ilike(TaJobHoliday.ATT_T_INFO_01	, s))
						);
		}	
		
		if(manId != null)
			cri = Restrictions.and(	cri, Restrictions.ne(TaJobHoliday.ATT_I_PER_MANAGER, manId));
		
		return cri;
	}
	
	private static List<TaJobHoliday> reqListDyn(Object[] dataTableOption, Criterion 	cri) throws Exception {		
		int 		begin 		= (int)	dataTableOption[1];
		int 		number 		= (int)	dataTableOption[2]; 
		int 		sortCol 	= (int)	dataTableOption[3]; 
		int 		sortTyp 	= (int)	dataTableOption[4];	

		List<TaJobHoliday> list 	= new ArrayList<TaJobHoliday>();		

		Order 	order 	= null;			
		String 	colName = null;

		switch(sortCol){
		case 0: colName = TaJobHoliday.ATT_I_ID; break;		
		//case 1: colName = TaJobHoliday.ATT_T_NAME; break;					
		}

		if (colName!=null){
			switch(sortTyp){
			case 0: order = Order.asc (colName); break;
			case 1: order = Order.desc(colName); break;								
			}
		}
		
		if(cri==null){
			cri = Restrictions.gt(TaJobHoliday.ATT_I_ID, 0);
		}

		if (order==null)
			list	= TaJobHoliday.DAO.reqList(begin, number, cri);
		else
			list	= TaJobHoliday.DAO.reqList(begin, number, order, cri);			

		return list;
	}

	private static Number reqNbJobHolidayListDyn() throws Exception {
		
		return TaJobHoliday.DAO.reqCount();		
	}

	private static Number reqNbJobHolidayListDyn(Criterion cri) throws Exception {
		if(cri==null){
			cri = Restrictions.gt(TaJobHoliday.ATT_I_ID, 0);
		}
		return TaJobHoliday.DAO.reqCount(cri);
	}
	
	//---------------------------------------------------------------------------------------------------------
	private static void doNew(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doNew --------------");
		//--- in simple case, obj has only header , no details ----------------------
		//Map<String, Object> attr 	= API.reqMapParamsByClass(request, TaJobHoliday.class);
		//TaJobHoliday  ent	 = new TaJobHoliday(attr);
		//TaJobHoliday.DAO.doPersist(ent);
		//----------------------------------------------------------------------------------------------------------------------
			
		TaJobHoliday 			ent		= reqNew		(user, json);
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


	private static TaJobHoliday reqNew(TaAutUser user,  JSONObject json) throws Exception {
		JSONObject				obj		= ToolData.reqJson (json, "obj", null);
				
		if (!canWorkWithObj(user, WORK_NEW, obj)){ //other param after obj...
			return null;
		}
				
		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaJobHoliday.class);
		
		Integer manId = (Integer) user.req(TaAutUser.ATT_I_PER_MANAGER);
		if(manId != null) {
			attr.put(TaJobHoliday.ATT_I_PER_MANAGER, manId);
		}
			
		TaJobHoliday  		ent	 	= new TaJobHoliday(attr);
		TaJobHoliday.DAO.doPersist(ent);
		
		//build other objects from obj and request
		
		return ent;
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doMod(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doMod --------------");
		
		TaJobHoliday  		ent	 	=  reqMod(user, json); 								
		if (ent==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent
			));	
		}		
	}

	private static TaJobHoliday reqMod(TaAutUser user,  JSONObject json) throws Exception {
		JSONObject				obj		= ToolData.reqJson (json, "obj", null);
		Map<String, Object> 	attr 	= API.reqMapParamsByClass(json, TaJobHoliday.class);

		int objId 				= (int) attr.get(TaJobHoliday.ATT_I_ID);
		TaJobHoliday  ent	 	= TaJobHoliday.DAO.reqEntityByRef(objId);

		if (ent==null){
			return null;
		}

		if (!canWorkWithObj(user, WORK_MOD, ent)){ //other param after obj...
			return null;
		}

		//----some information can not be modified----------------------------
		attr.remove(TaJobHoliday.ATT_I_ID);
		attr.remove(TaJobHoliday.ATT_I_PER_MANAGER);

		TaJobHoliday.DAO.doMerge(ent, attr);	
		
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
			API.doResponse(response, ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_YES));
		}

		ToolDBLock.canDeleteLock(lock);
	}
	private static boolean canDel(TaAutUser user,  JSONObject json) throws Exception {
		Integer 		objId	= ToolData.reqInt	(json, "id"		, -1	);	
		
		TaJobHoliday  	ent	 	= TaJobHoliday.DAO.reqEntityByRef(objId);
		if (ent==null){
			return false;
		}
		
		if (!canWorkWithObj(user, WORK_DEL, ent)){ //other param after ent...
			return false;
		}		
		
		//remove all other object connecting to this ent first-------
		
		TaJobHoliday.DAO.doRemove(ent);		
		return true;
	}
	
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
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
		
		TaJobHoliday  		ent	 	=  reqMod(user, json); 								
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
		
		TaJobHoliday ent = reqMod(user, json);						
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
	
	private static CacheData<ResultPagination>		cache_rs 	= new CacheData<ResultPagination>	(100, DefTime.TIME_02_00_00_000);
	private static void doLstGrid(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");

		ResultPagination  	res = reqLstGrid(user, json); //and other params if necessary
		if (res.reqList()==null || res.reqList().size()==0){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, res 
				));	
	}

	private static ResultPagination reqLstGrid(TaAutUser user, JSONObject json, Object...params) throws Exception {

		Integer 		begin		= ToolData.reqInt	(json, "begin"		, 0	);
		Integer 		number		= ToolData.reqInt	(json, "number"		, 20);
		Integer 		total		= ToolData.reqInt	(json, "total"		, 0	);
		
		String keyWord 	= begin + "_" + number + "_" + total;

		ResultPagination rs =	cache_rs.reqData(keyWord);

		if(rs==null) {
			
			Criterion 	cri 			= null;				

			if(cri==null){
				cri = Restrictions.gt(TaJobHoliday.ATT_I_ID, 0);
			}
			
			List<TaJobHoliday> list 		= TaJobHoliday.DAO.reqList(begin, number, cri);
			if (total == 0 )	total		= reqNbJobHolidayListDyn().intValue();
			rs								= new ResultPagination(list, total, begin, number);
			cache_rs.reqPut(keyWord, rs);	
		} else {
		}
		return rs;
	}
	
}
