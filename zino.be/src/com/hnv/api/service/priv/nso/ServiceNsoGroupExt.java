package com.hnv.api.service.priv.nso;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.hnv.api.def.DefAPI;
import com.hnv.api.def.DefJS;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.api.service.common.APIAuth;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolDate;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.common.tool.ToolSet;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.aut.vi.ViAutUserDyn;
import com.hnv.db.aut.vi.ViAutUserMember;
import com.hnv.db.job.TaJobDayOffRequest;
import com.hnv.db.job.TaJobDayOffResume;
import com.hnv.db.job.TaJobHoliday;
import com.hnv.db.nso.TaNsoGroup;
import com.hnv.db.nso.TaNsoGroupMember;
import com.hnv.db.sor.TaSorOrder;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.db.tpy.TaTpyRelationship;
import com.hnv.def.DefDBExt;

/**
* ----- ServiceJobDayoffResume by H&V
* ----- Copyright 2017------------
*/
public class ServiceNsoGroupExt implements IService {
	private static	String 			filePath	= null; 
	private	static	String 			urlPath		= null; 
	

	public static final Integer 	NEW_CONTINUE 			= 1;	
	public static final Integer 	NEW_EXIT 				= 2;
	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 					= "EC_V3".toLowerCase();

	public static final String SV_CLASS 						= "ServiceNsoGroupExt".toLowerCase();	
	
	public static final String SV_DO_USER_PLANNING_GET 		    = "SVUserPlanningGet".toLowerCase();
	
	public static final String SV_DO_GROUP_PLANNING_GET 	    = "SVGroupPlanningGet".toLowerCase();
	
	public static final String SV_DO_GROUP_PLANNING_GET_APPOINTMENT_LST 	    = "SVGroupPlanningGetAppointmentList".toLowerCase();
	
	public static final String SV_DO_GROUP_PLANNING_GET_APPOINTMENT_LST_BY_DATE = "SVGroupPlanningGetAppointmentListByDate".toLowerCase();	
	
	//-----------------------------------------------------------------------------------------------
	//-------------------------Default Constructor - Required -------------------------------------
	public ServiceNsoGroupExt(){
		ToolLogServer.doLogInf("----"+ SV_CLASS+ " is loading -----");
	}


	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		//ServerLogTool.doLogInf("--------- "+ SV_CLASS+ ".doService --------------");
		String    sv   = API.reqSVFunctName(json);
		TaAutUser user = (TaAutUser) json.get("userInfo");
		try{
			if(sv.equals(SV_DO_USER_PLANNING_GET) 										){
				doUserPlanningGet(user, json, response);
			}else if(sv.equals(SV_DO_GROUP_PLANNING_GET) 								){
				doGroupPlanningGet(user, json, response);
			}else if(sv.equals(SV_DO_GROUP_PLANNING_GET_APPOINTMENT_LST) 				){
				doGroupPlanningGetAppointmentLst(user, json, response);	
			}else if(sv.equals(SV_DO_GROUP_PLANNING_GET_APPOINTMENT_LST_BY_DATE) 		){
				doGroupPlanningGetAppointmentLstByDate(user, json, response);	
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
		
	private static void doUserPlanningGet(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");

		Integer 			uId			= ToolData.reqInt	(json, "uId"		, user.reqId()	);
		Date 				dtBegin	    = ToolData.reqDate	(json, "dtBegin"	, ToolDate.reqDateByAdding(new Date(), 0, 0,  0, 0, 0, 0)	);
		Date 				dtEnd	    = ToolData.reqDate	(json, "dtEnd"		, ToolDate.reqDateByAdding(dtBegin	 , 0, 0,  30, 0, 0, 0));

		dtBegin			= ToolDate.reqDateByAdding(dtBegin, 0, 0, 0, - ToolDate.reqHour(dtBegin), 0, 0);	
		dtEnd			= ToolDate.reqDateByAdding(dtEnd, 0, 0, 0, 23 - ToolDate.reqHour(dtEnd), 0, 0);
		
		TaJobDayOffResume dayoff = TaJobDayOffResume.DAO.reqEntityByValue(TaJobDayOffResume.ATT_I_AUT_USER, uId);
		
		if (dayoff==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		// build dayoff
		Criterion cri = Restrictions.and(Restrictions.eq(TaJobDayOffRequest.ATT_I_AUT_USER_01, uId), 
										Restrictions.or(Restrictions.eq(TaJobDayOffRequest.ATT_I_STATUS, TaJobDayOffRequest.PR_REQUEST_STAT_PENDING ),
														Restrictions.eq(TaJobDayOffRequest.ATT_I_STATUS, TaJobDayOffRequest.PR_REQUEST_STAT_VALIDATE)),
														Restrictions.ge(TaJobDayOffRequest.ATT_D_DATE_03, dtBegin),
														Restrictions.le(TaJobDayOffRequest.ATT_D_DATE_04, dtEnd));
		List<TaJobDayOffRequest>  lstDayoffReq  = TaJobDayOffRequest.DAO.reqList(cri);
		if (lstDayoffReq==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		
		//build appointment
//		List<TaTpyRelationship>	lst = TaTpyRelationship.DAO.reqList( Restrictions.and(Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02, uId), 
//				 Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02, DefDBExt.ID_TA_AUT_USER),
//				 Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01, DefDBExt.ID_TA_PRJ_APPOINTMENT)));
//		
//		Set<Integer>  setIds = ToolSet.reqSetInt(lst, TaTpyRelationship.ATT_I_ENTITY_ID_01);
//		
//		List<TaSorOrder>  lstAppoint = new ArrayList<>();;
//		if(setIds != null && !setIds.isEmpty()) {
//			Criterion cri 	= Restrictions.and(Restrictions.ge(TaSorOrder.ATT_D_DATE_02, dtBegin),
//							  					Restrictions.le(TaSorOrder.ATT_D_DATE_03, dtEnd));
//				
//			lstAppoint = TaSorOrder.DAO.reqList_In(TaSorOrder.ATT_I_ID, setIds, cri);
//		}
//		
//		if (lstAppoint==null){
//			API.doResponse(response, DefAPI.API_MSG_KO);
//			return;
//		}
		
		//build holiday
		List<TaJobHoliday> lstHoliday = TaJobHoliday.DAO.reqList(Restrictions.and(  Restrictions.eq(TaJobHoliday.ATT_I_PER_MANAGER, user.reqPerManagerId()),
																					Restrictions.ge(TaJobHoliday.ATT_D_DATE, dtBegin),
																 					Restrictions.le(TaJobHoliday.ATT_D_DATE, dtEnd)
																 					));

		if (lstHoliday==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		
		JSONObject result = new JSONObject();
		
		result.put("config",  dayoff);
		result.put("joboff",  lstDayoffReq);
		result.put("holiday", lstHoliday);
//		result.put("appoint", lstAppoint);
		
		API.doResponse(response	, ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, result));
	}
	
	private static void doGroupPlanningGet(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");

		Integer 			grpId		= ToolData.reqInt	(json, "grpId"		, user.reqId()	);
		Date 				dtBegin	    = ToolData.reqDate	(json, "dtBegin"	, ToolDate.reqDateByAdding(new Date(), 0, 0,  0, 0, 0, 0));
		Date 				dtEnd	    = ToolData.reqDate	(json, "dtEnd"		, ToolDate.reqDateByAdding(dtBegin	 , 0, 0,  30, 0, 0, 0));

		dtBegin			= ToolDate.reqDateByAdding(dtBegin, 0, 0, 0, - ToolDate.reqHour(dtBegin), 0, 0);	
		dtEnd			= ToolDate.reqDateByAdding(dtEnd, 0, 0, 0, 23 - ToolDate.reqHour(dtEnd), 0, 0);
		
		if(grpId == null) return; 
		
		List<TaNsoGroupMember> lstMember = TaNsoGroupMember.DAO.reqList(Restrictions.eq(TaNsoGroupMember.ATT_I_NSO_GROUP, grpId));

		Set<Integer> setIds = ToolSet.reqSetInt(lstMember, TaNsoGroupMember.ATT_I_AUT_USER);
		
		List<TaJobDayOffResume> lstDayoff = new ArrayList<>();
		if(setIds != null && !setIds.isEmpty()) {
		    lstDayoff = TaJobDayOffResume.DAO.reqList_In(TaJobDayOffResume.ATT_I_AUT_USER, setIds);
		}
		
		if (lstDayoff==null || lstDayoff.size() == 0){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		
		//build user
		List<ViAutUserDyn> lstUser = ViAutUserDyn.DAO.reqList_In(TaAutUser.ATT_I_ID, setIds, Restrictions.eq(ViAutUserDyn.ATT_I_STATUS, ViAutUserDyn.STAT_ACTIVE));
		if (lstUser==null || lstUser.size() == 0){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		
		if(lstUser.size() > 0) {
			TaTpyDocument	.reqBuildAvatar(lstUser, DefDBExt.ID_TA_AUT_USER, ViAutUserDyn.ATT_O_AVATAR);
		}
		
		//build dayoff
		List<TaJobDayOffRequest> lstDayReq  = TaJobDayOffRequest.DAO.reqList_In(TaJobDayOffRequest.ATT_I_AUT_USER_01, setIds, 
																Restrictions.or(Restrictions.eq(TaJobDayOffRequest.ATT_I_STATUS, TaJobDayOffRequest.PR_REQUEST_STAT_PENDING ),
																				Restrictions.eq(TaJobDayOffRequest.ATT_I_STATUS, TaJobDayOffRequest.PR_REQUEST_STAT_VALIDATE)),
																Restrictions.ge(TaJobDayOffRequest.ATT_D_DATE_03, dtBegin),
															 	Restrictions.le(TaJobDayOffRequest.ATT_D_DATE_04, dtEnd));
		
		if (lstDayReq==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		
		JSONObject result = new JSONObject();
		result.put("configLst",  lstDayoff);
		result.put("joboffLst",  lstDayReq);
		result.put("userLst"  ,  lstUser);
	    
		API.doResponse(response	, ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, result));
	}

	private static void doGroupPlanningGetAppointmentLst(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");

		List<TaNsoGroup> 	list = doGetAppointmentLst(user, json); //and other params if necessary
		if (list==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response	, ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, list));

	}
	private static List<TaNsoGroup> doGetAppointmentLst(TaAutUser user, JSONObject json) throws Exception {
		Integer 			uId	    = ToolData.reqInt	(json, "uId"		, -1	);
		Date 				dtBegin	= ToolData.reqDate	(json, "dtBegin"	, ToolDate.reqDateByAdding(new Date(), 0, 0, 0, 0, 0, 0)	);
		Date 				dtEnd	= ToolData.reqDate	(json, "dtEnd"		, ToolDate.reqDateByAdding(dtBegin	 , 0, 0, 30, 0, 0, 0));
		//other params here
		
		dtBegin			= ToolDate.reqDateByAdding(dtBegin, 0, 0, 0, - ToolDate.reqHour(dtBegin), 0, 0);	
		dtEnd			= ToolDate.reqDateByAdding(dtEnd, 0, 0, 0, 23 - ToolDate.reqHour(dtEnd), 0, 0);

		//build list appointment owner
		Criterion 			cri		= reqCriterionPrjAppointment (uId, dtBegin, dtEnd); //and other params	
		List<TaNsoGroup> 	lstAppoint 	= TaNsoGroup.DAO.reqList(cri);

		//build list appointment member
//		List<TaTpyRelationship>	lst = TaTpyRelationship.DAO.reqList( Restrictions.and(Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02, uId), 
//				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02, DefDBExt.ID_TA_AUT_USER),
//				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01, DefDBExt.ID_TA_PRJ_APPOINTMENT)));
//
//		Set<Integer>  setIds = ToolSet.reqSetInt(lst, TaTpyRelationship.ATT_I_ENTITY_ID_01);

		List<TaNsoGroupMember>  lstMemberAppoint = new ArrayList<>();
		if(lstAppoint != null && !lstAppoint.isEmpty()) {
			for(TaNsoGroup g : lstAppoint) {
				g.doBuildMembersWithDate(dtBegin, dtEnd);
			}
//			lstMemberAppoint = TaNsoGroupMember.DAO.reqList_In(TaNsoGroupMember.ATT_I_NSO_GROUP, setIds,  Restrictions.and(Restrictions.ge(TaNsoGroupMember.ATT_D_DATE_01, dtBegin),
//																				                        Restrictions.le(TaNsoGroupMember.ATT_D_DATE_02, dtEnd)));
		}

//		if(lstMemberAppoint != null && lstMemberAppoint.size() > 0) {
//			lstAppoint.addAll(lstMemberAppoint);
//		}
		
		return lstAppoint;
	}
	private static Criterion reqCriterionPrjAppointment(Integer uId, Date dtBegin, Date dtEnd) throws Exception{

		Criterion cri = Restrictions.gt("I_ID", 0);		

		if (uId!=null){
			cri 		= Restrictions.and(cri, Restrictions.eq(TaNsoGroup.ATT_I_AUT_USER, uId));
		}	
		if (dtBegin!=null){
			cri 		= Restrictions.and(cri, Restrictions.ge(TaNsoGroup.ATT_D_DATE_01, dtBegin));
		}	
		if (dtEnd!=null){
			cri 		= Restrictions.and(cri, Restrictions.le(TaNsoGroup.ATT_D_DATE_02, dtEnd));
		}	


		return cri;
	}
	
	
	private static void doGroupPlanningGetAppointmentLstByDate(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");

		List<TaNsoGroup> 	list = doGetAppointmentLstByDate(user, json); //and other params if necessary
		if (list==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		
		API.doResponse(response	, ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, list));

	}
	private static List<TaNsoGroup> doGetAppointmentLstByDate(TaAutUser user, JSONObject json) throws Exception {
		Date 				date= ToolData.reqDate	(json, "date", null);
		Date dtBegin			= ToolDate.reqDateByAdding(date, 0, 0, 0, - ToolDate.reqHour(date), 0, 0);
		Date dtEnd				= ToolDate.reqDateByAdding(dtBegin, 0, 0, 0, +23, 0, 0);

		if(date == null) return null;

		List<TaNsoGroup> lst = TaNsoGroup.DAO.reqList( Restrictions.and(Restrictions.eq(TaNsoGroup.ATT_I_TYPE_01, TaNsoGroup.TYP_01_MEETING),
																		Restrictions.between(TaNsoGroup.ATT_D_DATE_02, dtBegin, dtEnd)));

		return lst;
	}
}
