package com.hnv.api.service.priv.job;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hnv.api.def.DefAPI;
import com.hnv.api.def.DefDB;
import com.hnv.api.def.DefJS;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.common.tool.ToolDBLock;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolDatatable;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.job.TaJobDayOffResume;
import com.hnv.db.sys.TaSysLock;
import com.hnv.def.DefDBExt;

/**
* ----- ServiceJobDayoffResume by H&V
* ----- Copyright 2017------------
*/
public class ServiceJobDayoffResume implements IService {
	private static	String 			filePath	= null; 
	private	static	String 			urlPath		= null; 
	

	public static final Integer 	NEW_CONTINUE 			= 1;	
	public static final Integer 	NEW_EXIT 				= 2;
	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 					= "EC_V3".toLowerCase();

	public static final String SV_CLASS 					= "ServiceJobDayoffResume".toLowerCase();	
	
	public static final String SV_GET 		= "SVGet".toLowerCase();	
	public static final String SV_LST 		= "SVLst".toLowerCase();
	public static final String SV_LST_DYN	= "SVLstDyn".toLowerCase(); 
	
	public static final String SV_NEW 		= "SVNew".toLowerCase();	
	public static final String SV_MOD 		= "SVMod".toLowerCase();	
	public static final String SV_DEL 		= "SVDel".toLowerCase();

	public static final String SV_LCK_REQ 	= "SVLckReq".toLowerCase(); //req or refresh	
	public static final String SV_LCK_SAV 	= "SVLckSav".toLowerCase(); //save and continue
	public static final String SV_LCK_END 	= "SVLckEnd".toLowerCase();
	public static final String SV_LCK_DEL 	= "SVLckDel".toLowerCase();
	
		
	//-----------------------------------------------------------------------------------------------
	//-------------------------Default Constructor - Required -------------------------------------
	public ServiceJobDayoffResume(){
		
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	public static final Integer	ENT_TYP				= DefDBExt.ID_TA_JOB_DAYOFF_RESUME;
	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		//ServerLogTool.doLogInf("--------- "+ SV_CLASS+ ".doService --------------");
		
		if (filePath	==null) filePath		= API.reqContextParameter("JOB_DAYOFF_RESUME_PATH_FILE");
		if (urlPath		==null) urlPath			= API.reqContextParameter("JOB_DAYOFF_RESUME_PATH_URL");	
						
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
				
			} else if(sv.equals(SV_NEW)		){
				doNew(user, json, response);
			} else if(sv.equals(SV_MOD)		){
				doMod(user, json, response);
			} else  if(sv.equals(SV_DEL)		){
				doDel(user, json, response);
			
			} else if(sv.equals(SV_LCK_REQ)	){
				doLckReq(user, json, response);
			} else if(sv.equals(SV_LCK_SAV)	){
				doLckSav(user, json, response);
			} else if(sv.equals(SV_LCK_END)	){
				doLckEnd(user, json, response);
			} else if(sv.equals(SV_LCK_DEL)	){
				doLckDel(user, json, response);		
						
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
																		
		TaJobDayOffResume 		ent 	= reqGet(objId, forced);
			
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
	
	private static TaJobDayOffResume reqGet(Integer matId, Boolean forced) throws Exception{
		TaJobDayOffResume 		ent 	= TaJobDayOffResume.DAO.reqEntityByRef(matId, forced);
			
		//---do build something other of ent like details....
			
		return ent;
	}
	//---------------------------------------------------------------------------------------------------------
	private static void doLst(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");
			
		TaJobDayOffResume res = req(user, json); //and other params if necessary
		
		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
			DefJS.SESS_STAT		, 1,
			DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
			DefJS.RES_DATA		, res
		));				
	}
	
	private static TaJobDayOffResume req(TaAutUser user, JSONObject json, Object...params) throws Exception {	
		Integer 	uId03	= ToolData.reqInt	(json, "uId03", null);		
		if(uId03 == null) return null;
				
		if (!canWorkWithObj(user, WORK_LST, uId03)){ //other param after objTyp...
			return null;
		}
		TaJobDayOffResume 			res 	= null;
		Criterion 					cri		= Restrictions.eq(TaJobDayOffResume.ATT_I_AUT_USER, uId03);
		List<TaJobDayOffResume> 	list 	=  TaJobDayOffResume.DAO.reqList(cri);
		
		if (list==null || list.size()==0){
			res = new TaJobDayOffResume(uId03, null, null);
		} else {
			res = list.get(0);
		}
		return res;
	}
	
	private static Criterion reqCriterion(Object...params) throws Exception{
		if (params==null || params.length==0) return null;
		
		Criterion cri = Restrictions.gt("I_ID", 0);	

		if (params!=null && params.length>0){
			//int type 	= (int) params[0];
			//cri 		= Restrictions.and(cri, Restrictions.eqOrIsNull(TaJobDayoffResume.ATT_I_TYPE, type));
		}			
		
		if (params!=null && params.length>1){
			//do something
		}
		
		return cri;
	}
	
	//---------------------------------------------------------------------------------------------------------		
	
	private static Long countAllEle = null;
	private static void doLstDyn(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {	
		Object[]  			dataTableOption = ToolDatatable.reqDataTableOption (json, null);				
		List<String>		searchKey		= (List<String>)dataTableOption[0];	
		Integer				typ01			= ToolData.reqInt	(json, "typ01", null);
		Integer				typ02			= ToolData.reqInt	(json, "typ02", null);

		if (!canWorkWithObj(user, WORK_LST, null, typ01, typ02)){ //other param after objTyp...
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		Criterion 	cri 			= reqRestriction(searchKey, typ01, typ02);

		List<TaJobDayOffResume> list 		= reqListDyn(dataTableOption, cri);		
		if (list==null ){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		if (countAllEle==null)
			countAllEle = ((long)reqNbJobDayoffResumeListDyn());

		Integer iTotalRecords 			= (countAllEle.intValue());				
		Integer iTotalDisplayRecords 	= reqNbJobDayoffResumeListDyn(cri).intValue();


		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,					
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, list
				));

	}
	
	
	private static Criterion reqRestriction(List<String> searchKey, Integer typ01, Integer typ02) throws Exception {		
		Criterion cri = null;
		
/*		for (String s : searchKey){
			if (cri==null)
				cri = 	Restrictions.or(
							Restrictions.ilike(TaJobDayoffResume.ATT_T_CODE_01		, s), 
							Restrictions.ilike(TaJobDayoffResume.ATT_T_NAME_01		, s));

			else
				cri = 	Restrictions.and(	cri, 
							Restrictions.or(
								Restrictions.ilike(TaJobDayoffResume.ATT_T_CODE_01		, s), 
								Restrictions.ilike(TaJobDayoffResume.ATT_T_NAME_01		, s))
						);
		}	
		
		if(typ01 != null){
			cri = Restrictions.and(cri, Restrictions.eq(TaJobDayoffResume.ATT_I_TYPE_01, typ01));
		} 
		if(typ02 != null){			
			cri = Restrictions.and(cri, Restrictions.eq(TaJobDayoffResume.ATT_I_TYPE_02, typ02));			
		} 
*/			
		return cri;
	}
	private static List<TaJobDayOffResume> reqListDyn(Object[] dataTableOption, Criterion 	cri) throws Exception {		
		int 		begin 		= (int)(long)	dataTableOption[1];
		int 		number 		= (int)(long)	dataTableOption[2]; 
		int 		sortCol 	= (int)(long)	dataTableOption[3]; 
		int 		sortTyp 	= (int)(long)	dataTableOption[4];	

		List<TaJobDayOffResume> list 	= new ArrayList<TaJobDayOffResume>();		

		Order 	order 	= null;			
		String 	colName = null;

		switch(sortCol){
		case 0: colName = TaJobDayOffResume.ATT_I_ID; break;		
		//case 1: colName = TaJobDayoffResume.ATT_T_NAME; break;					
		}

		if (colName!=null){
			switch(sortTyp){
			case 0: order = Order.asc (colName); break;
			case 1: order = Order.desc(colName); break;								
			}
		}

		if (order==null)
			list	= TaJobDayOffResume.DAO.reqList(begin, number, cri);
		else
			list	= TaJobDayOffResume.DAO.reqList(begin, number, order, cri);			

		return list;
	}

	private static Number reqNbJobDayoffResumeListDyn() throws Exception {						
		return TaJobDayOffResume.DAO.reqCount();		
	}

	private static Number reqNbJobDayoffResumeListDyn(Criterion cri) throws Exception {			
		return TaJobDayOffResume.DAO.reqCount(cri);
	}
	
	//---------------------------------------------------------------------------------------------------------
	private static void doNew(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doNew --------------");
		//--- in simple case, obj has only header , no details ----------------------
		//Map<String, Object> attr 	= API.reqMapParamsByClass(request, TaJobDayoffResume.class);
		//TaJobDayoffResume  ent	 = new TaJobDayoffResume(attr);
		//TaJobDayoffResume.DAO.doPersist(ent);
		//----------------------------------------------------------------------------------------------------------------------
			
		TaJobDayOffResume 			ent		= reqNew		(user, json);
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


	private static TaJobDayOffResume reqNew(TaAutUser user,  JSONObject json) throws Exception {
		JSONObject				obj		= ToolData.reqJson (json, "obj", null);
				
		if (!canWorkWithObj(user, WORK_NEW, obj)){ //other param after obj...
			return null;
		}
				
		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaJobDayOffResume.class);
		TaJobDayOffResume  		ent	 	= new TaJobDayOffResume(attr);
		TaJobDayOffResume.DAO.doPersist(ent);
		
		//build other objects from obj and request
		
		return ent;
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doMod(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doMod --------------");
		
		TaJobDayOffResume  		ent	 	=  reqMod(user, json); 								
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

	private static TaJobDayOffResume reqMod(TaAutUser user,  JSONObject json) throws Exception {
		JSONObject			obj		= ToolData.reqJson (json, "obj", null);
						
		int 				objId	= Integer.parseInt(obj.get("id").toString());	
		TaJobDayOffResume  		ent	 	= TaJobDayOffResume.DAO.reqEntityByRef(objId);
		if (ent==null){
			return null;
		}
		
		if (!canWorkWithObj(user, WORK_MOD, ent)){ //other param after obj...
			return null;
		}
		
		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaJobDayOffResume.class);		
		TaJobDayOffResume.DAO.doMerge(ent, attr);	
		
		//merge other objects from obj and request
			
		return ent;
	}	

	//---------------------------------------------------------------------------------------------------------
	private static void doDel(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
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
		
		TaJobDayOffResume  	ent	 	= TaJobDayOffResume.DAO.reqEntityByRef(objId);
		if (ent==null){
			return false;
		}
		
		if (!canWorkWithObj(user, WORK_DEL, ent)){ //other param after ent...
			return false;
		}		
		
		//remove all other object connecting to this ent first-------
		
		TaJobDayOffResume.DAO.doRemove(ent);		
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
		
		TaJobDayOffResume  		ent	 	=  reqMod(user, json); 								
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
		
		TaJobDayOffResume ent = reqMod(user, json);						
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
