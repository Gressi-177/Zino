package com.hnv.api.service.priv.prj;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
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
import com.hnv.api.service.priv.aut.ServiceAutUser;
import com.hnv.common.tool.ToolDBEntity;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolDate;
import com.hnv.common.tool.ToolEmail;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.common.tool.ToolSet;
import com.hnv.common.util.CacheData;
import com.hnv.data.json.JSONArray;
import com.hnv.data.json.JSONObject;
import com.hnv.db.EntityAbstract;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.aut.vi.ViAutUserMember;
import com.hnv.db.cfg.TaCfgValue;
import com.hnv.db.msg.TaMsgMessage;
import com.hnv.db.nso.TaNsoGroup;
import com.hnv.db.nso.TaNsoGroupMember;
import com.hnv.db.nso.TaNsoPost;
import com.hnv.db.per.TaPerPerson;
import com.hnv.db.prj.TaPrjProject;
import com.hnv.db.prj.vi.ViPrjProjectSimple;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.db.tpy.TaTpyFavorite;
import com.hnv.db.tpy.TaTpyInformation;
import com.hnv.db.tpy.TaTpyPropertyQuant;
import com.hnv.db.tpy.TaTpyRelationship;
import com.hnv.def.DefDBExt;
import com.hnv.process.ThreadManager;

/**
 * ----- ServicePrjProject by H&V
 * ----- Copyright 2017------------
 */
public class ServicePrjProject implements IService {
	private static	String 			filePath	= null; 
	private	static	String 			urlPath		= null; 


	public static final Integer 	NEW_CONTINUE 			= 1;	
	public static final Integer 	NEW_EXIT 				= 2;
	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 									= "EM_V3"								.toLowerCase();

	public static final String SV_CLASS 									= "ServicePrjProject"					.toLowerCase();	

	public static final String SV_GET 						= "SVGet"					.toLowerCase();	
	public static final String SV_LST 						= "SVLst"					.toLowerCase();
	public static final String SV_LST_DYN					= "SVLstDyn"				.toLowerCase(); 

	public static final String SV_LST_SEARCH				= "SVLstSearch"				.toLowerCase();

	public static final String SV_NEW 						= "SVNew"					.toLowerCase();	
	public static final String SV_MOD 						= "SVMod"					.toLowerCase();	//---don't use
	public static final String SV_DEL 						= "SVDel"					.toLowerCase();
	public static final String SV_MAIN_DEL 					= "SVMainDel"				.toLowerCase();

	public static final String SV_WORKFLOW_DEL 				= "SVPrjWorkflowDel"		.toLowerCase();
	public static final String SV_SPRINT_DEL 				= "SVPrjSpintDel"			.toLowerCase();

	public static final String SV_GET_MEMBER 				= "SVGetMember"				.toLowerCase();	
	public static final String SV_GET_MEMBER_GROUP			= "SVGetMemberGroup"		.toLowerCase();	
	public static final String SV_GET_COMMENT				= "SVGetComment"			.toLowerCase();	


	public static final String SV_SAVE_MEMBER 				= "SVSaveMember"			.toLowerCase();
	public static final String SV_SAVE_MEMBER_GROUP 		= "SVSaveMemberGroup"		.toLowerCase();
	public static final String SV_SAVE_CONTENT 				= "SVSaveContent"			.toLowerCase();
	public static final String SV_SAVE_COMMENT				= "SVSaveComment"			.toLowerCase();	


	public static final String SV_TASK_GET 					= "SVTaskGet"				.toLowerCase();	
	public static final String SV_TASK_SAVE 				= "SVTaskSave"				.toLowerCase();
	public static final String SV_TASK_REFRESH 				= "SVTaskRefresh"			.toLowerCase();
	public static final String SV_TASK_MOVE 				= "SVTaskMove"				.toLowerCase();
	public static final String SV_TASK_LIST 				= "SVTaskList"				.toLowerCase();
	public static final String SV_TASK_LIST_SEARCH 			= "SVTaskListSearch"		.toLowerCase();

	public static final String SV_EPIC_GET 					= "SVEpicGet"				.toLowerCase();	
	public static final String SV_EPIC_SAVE 				= "SVEpicSave"				.toLowerCase();
	public static final String SV_EPIC_REFRESH 				= "SVEpicRefresh"			.toLowerCase();	
	public static final String SV_EPIC_ADD_TASK 			= "SVEpicAddTask"			.toLowerCase();

	public static final String SV_FILE_SAVE 				= "SVFileSave"				.toLowerCase();
	public static final String SV_FILE_ADD 					= "SVFileAdd"				.toLowerCase();
	public static final String SV_FILE_DEL 					= "SVFileDel"				.toLowerCase();
	public static final String SV_FILE_SEARCH 				= "SVFileSearch"			.toLowerCase();


	public static final String SV_REPORT_SAVE				= "SVReportSave"			.toLowerCase();	
	public static final String SV_REPORT_GET				= "SVReportGet"				.toLowerCase();	


	public static final String SV_TEST_UNIT_LIST_SEARCH 	= "SVTestUnitListSearch"	.toLowerCase();

	public static final String SV_COUNT_NB_PRJ 				= "SVCountNbPrj"			.toLowerCase();	
	public static final String SV_GET_RELATIONSHIP_ROLE 	= "SVGetRelationRole"		.toLowerCase();	
	public static final String SV_GET_ORDER 				= "SVGetOrder"				.toLowerCase();

	public static final String SV_GET_TASK_GLOBAL 			= "SVGetTaskGlobal"			.toLowerCase();
	public static final String SV_GET_TASK_KPI 				= "SVGetTaskKPI"			.toLowerCase();
	public static final String SV_GET_TASK_VAL 				= "SVGetTaskVal"			.toLowerCase();

	public static final String SV_GET_CUSTOMER 				= "SVGetCustomer"			.toLowerCase();	
	public static final String SV_SAVE_CUSTOMER 			= "SVSaveCustomer"			.toLowerCase();
	public static final String SV_INFO_WITH_CUSTOMER 		= "SVInfoWithCustomer"		.toLowerCase();	
	public static final String SV_UPDATE_INFO_WITH_CUSTOMER = "SVUpdateInfoWithCustomer".toLowerCase();	
	public static final String SV_DELETE_CUSTOMER 			= "SVDeleteCustomer"		.toLowerCase();	
	public static final String SV_CHANGE_TYP_CUSTOMER 		= "SVChangeTypCustomer"		.toLowerCase();	
	public static final String SV_NEW_CUSTOMER 				= "SVNewCustomer"			.toLowerCase();	

	public static final String SV_TREE_VIEW 				= "SVPrjTreeView"			.toLowerCase();	


	public static final String SV_SPRINT_REFRESH			= "SVSprintRefresh"			.toLowerCase();	
	public static final String SV_CONTENT_REFRESH 			= "SVContentRefresh"		.toLowerCase();
	public static final String SV_TEST_GROUP_REFRESH 		= "SVTestGroupRefresh"		.toLowerCase();

	public static final String SV_GET_PARENT 				= "SVGetParent"				.toLowerCase();
	public static final String SV_GET_DOC_COMPANY_USER 		= "SVGetDocCompanyUser"		.toLowerCase();
	public static final String SV_GET_EVALUATION 			= "SVGetEvaluation"			.toLowerCase();

	public static final String SV_INFO_MANAGER 				= "SVInfoManager"			.toLowerCase();


	public static final String SV_GET_HISTORY_TASK 			= "SVGetHistoryTask"		.toLowerCase();
	public static final String SV_SAVE_HISTORY 				= "SVSaveHistory"			.toLowerCase();
	public static final String SV_MOD_HISTORY 				= "SVModHistory"			.toLowerCase();

	public static final String SV_CALCUL_PERCENT_SPRINT		= "SVCalculPercentSprint"	.toLowerCase();

	public static final String SV_GEN_REPORT_CRA			= "SVGenReportCra"			.toLowerCase();
	public static final String SV_GET_WORKFLOW_BY_TASK		= "SVWorkflowByTask"		.toLowerCase();

	private static Hashtable<String,Long> 		lastUpd 	= new Hashtable<String, Long>();

	//-----------------------------------------------------------------------------------------------
	//-------------------------Default Constructor - Required -------------------------------------
	public ServicePrjProject(){
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {
			if(sv.equals(SV_GET) 									&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET, APIAuth.R_PRJ_DATA_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGet(user,  json, response);

			} else if(sv.equals(SV_GET_MEMBER) 						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET, APIAuth.R_PRJ_DATA_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGetMember(user,  json, response);

			} else if(sv.equals(SV_GET_MEMBER_GROUP) 				&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGetGroupMember(user,  json, response);

			} else if(sv.equals(SV_GET_COMMENT) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET, APIAuth.R_PRJ_DATA_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGetComments (user, json, response);



				//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
				//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
			} else if(sv.equals(SV_LST)								&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLst(user,  json, response);
			} else if(sv.equals(SV_LST_DYN)							&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLstDyn(user,  json, response);
			} else if(sv.equals(SV_LST_SEARCH)						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doLstSearch(user,  json, response);

				//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
				//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
			} else if(sv.equals(SV_NEW)							&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_NEW, APIAuth.R_PRJ_PROJECT_NEW, APIAuth.R_PRJ_DATA_NEW)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doNew(user,  json, response);

				//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
				//-------------------------------------------------------------------------------------------------------------------------------------------------------------------

			} else if(sv.equals(SV_MOD)								&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_PRJ_PROJECT_MOD)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
//				doMod(user,  json, response);

			} else if(sv.equals(SV_SAVE_CONTENT) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_PRJ_PROJECT_MOD)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doSaveContent(user,  json, response);//---same with doMod

			} else if(sv.equals(SV_SAVE_MEMBER) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_PRJ_PROJECT_MOD)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doSaveMember(user,  json, response);

			} else if(sv.equals(SV_SAVE_MEMBER_GROUP) 				&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_PRJ_PROJECT_MOD)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doSaveGroupMember(user,  json, response);

			} else if(sv.equals(SV_SAVE_COMMENT) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_PRJ_PROJECT_MOD)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doSaveComments (user, json, response);

				//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
				//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
			} else  if(sv.equals(SV_DEL)							&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_DEL, APIAuth.R_PRJ_PROJECT_DEL)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doDel(user,  json, response);
			} else  if(sv.equals(SV_MAIN_DEL)						&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doMainDel(user,  json, response);

			} else  if(sv.equals(SV_WORKFLOW_DEL)					&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doPrjWorkflowDel(user,  json, response);
			} else  if(sv.equals(SV_SPRINT_DEL)						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_DEL, APIAuth.R_PRJ_PROJECT_DEL)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doPrjSprintDel(user,  json, response);




				//-------------------------------------------------------------------------------------------------------------------------------
			} else if(sv.equals(SV_TASK_GET) 						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGetRelationship	(user, json, response, DefDBExt.ID_TA_PRJ_WORKFLOW, DefDBExt.ID_TA_PRJ_PROJECT_TASK);

			} else if(sv.equals(SV_TASK_SAVE) 						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_PRJ_PROJECT_MOD)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doSaveRelationship(user, json, response, DefDBExt.ID_TA_PRJ_WORKFLOW, DefDBExt.ID_TA_PRJ_PROJECT_TASK);

			} else if(sv.equals(SV_TASK_MOVE) 						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_PRJ_PROJECT_MOD)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doTaskMove(user,  json, response);

			} else if(sv.equals(SV_TASK_LIST) 						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doPrjGlobalList(user,  json, response);

			} else if(sv.equals(SV_TASK_LIST_SEARCH) 				&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doPrjTaskListSearch(user,  json, response);

			} else if(sv.equals(SV_TASK_REFRESH) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doTaskRefresh(user,  json, response);

				//-------------------------------------------------------------------------------------------------------------------------------

			} else if(sv.equals(SV_EPIC_GET) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGetRelationship (user, json, response, DefDBExt.ID_TA_PRJ_WORKFLOW, DefDBExt.ID_TA_PRJ_PROJECT);

			} else if(sv.equals(SV_EPIC_SAVE) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_PRJ_PROJECT_MOD)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doSaveRelationship(user, json, response, DefDBExt.ID_TA_PRJ_WORKFLOW, DefDBExt.ID_TA_PRJ_PROJECT);

			} else if(sv.equals(SV_EPIC_ADD_TASK) 				&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_PRJ_PROJECT_MOD)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doAddTaskToEpic(user,  json, response);

			} else if(sv.equals(SV_EPIC_REFRESH) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doEpicRefresh(user,  json, response);

				//-------------------------------------------------------------------------------------------------------------------------------
			} else if(sv.equals(SV_REPORT_GET) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGetReports (user, json, response);

			} else if(sv.equals(SV_REPORT_SAVE) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_PRJ_PROJECT_MOD)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doSaveReport (user, json, response);


				//-------------------------------------------------------------------------------------------------------------------------------

			} else if(sv.equals(SV_FILE_SAVE) 						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_PRJ_PROJECT_MOD)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doSaveFiles(user,  json, response);

			} else if(sv.equals(SV_FILE_ADD) 						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_NEW, APIAuth.R_PRJ_PROJECT_NEW)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doAddFiles(user,  json, response);

			} else if(sv.equals(SV_FILE_DEL) 						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_DEL, APIAuth.R_PRJ_PROJECT_DEL)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doDelFiles(user,  json, response);

			} else if(sv.equals(SV_FILE_SEARCH)						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_MOD, APIAuth.R_PRJ_PROJECT_MOD)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))) {
				doSearchFile(user,  json, response);

				//-------------------------------------------------------------------------------------------------------------------------------

			} else if(sv.equals(SV_TEST_UNIT_LIST_SEARCH) 			&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doPrjTestUnitListSearch(user,  json, response);


			} else if(sv.equals(SV_COUNT_NB_PRJ) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doCountNbPrj(user,  json, response);
			} else if(sv.equals(SV_GET_TASK_GLOBAL) 				&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doPrjGlobal(user,  json, response);
			} else if(sv.equals(SV_GET_TASK_KPI) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doPrjKPI(user,  json, response);

			} else if(sv.equals(SV_GET_TASK_VAL) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doPrjTaskVal(user,  json, response);

			} else if(sv.equals(SV_GET_CUSTOMER) 					&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doGetCustomer(user,  json, response);
			} else if(sv.equals(SV_SAVE_CUSTOMER) 					&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doSaveCustomer(user,  json, response);


			} else if(sv.equals(SV_GET_RELATIONSHIP_ROLE) 			&&  (APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_AUT_USER_GET)
					||	APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGetRelationshipRole(user,  json, response);
			} else if(sv.equals(SV_INFO_WITH_CUSTOMER) 				&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doGetInfoWithCustomer(user,  json, response);
			} else if(sv.equals(SV_UPDATE_INFO_WITH_CUSTOMER) 		&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doUpdateInfoWithCustomer(user,  json, response);
			} else if(sv.equals(SV_DELETE_CUSTOMER) 				&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doDeleteCustomer(user,  json, response);
			} else if(sv.equals(SV_CHANGE_TYP_CUSTOMER) 			&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doChangeTypCustomer(user,  json, response);
			} else if(sv.equals(SV_NEW_CUSTOMER) 					&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doNewCustomer(user,  json, response);
			} else if(sv.equals(SV_GET_ORDER) 						&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doGetOrder(user,  json, response);
			} else if(sv.equals(SV_TREE_VIEW) 						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doTreeView(user,  json, response);




			} else if(sv.equals(SV_SPRINT_REFRESH) 			&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doSprintRefresh(user,  json, response);

			} else if(sv.equals(SV_TEST_GROUP_REFRESH) 				&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doRefreshTestGroup(user,  json, response);

			} else if(sv.equals(SV_CONTENT_REFRESH) 				&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doContentRefresh(user,  json, response);
			} else if(sv.equals(SV_GET_PARENT) 						&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGetParent(user,  json, response);


			} else if(sv.equals(SV_GET_DOC_COMPANY_USER) 			&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doGetDocCompanyUser(user,  json, response);
			} else if(sv.equals(SV_GET_EVALUATION) 					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doEvaluation(user,  json, response);


				//			} else if(sv.equals(SV_INFO_MANAGER) 					&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				//				doGetInfoPartnerMan(user,  json, response);


			}else if(sv.equals(SV_GET_HISTORY_TASK)					&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGetHistoryTask(user,  json, response);
			}else if(sv.equals(SV_SAVE_HISTORY)						&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doSaveHistory(user,  json, response);	
			}else if(sv.equals(SV_MOD_HISTORY)						&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doModHistory(user,  json, response);

			}else if(sv.equals(SV_CALCUL_PERCENT_SPRINT)			&& APIAuth.canAuthorize(user, SV_CLASS, sv)){
				doCalculPercentSprint(user,  json, response);

			}else if(sv.equals(SV_GET_WORKFLOW_BY_TASK) 			&& ( APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_GET, APIAuth.R_PRJ_PROJECT_GET)
					||	 APIAuth.canAuthorize(user, SV_CLASS, sv))){
				doGetWorkflowByTask(user,  json, response);

			}else {
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
	//---------------------------------------------------------------------------------------------------------
	private static boolean canBeMemberOfPrj(TaPrjProject ent, TaAutUser user) throws Exception {
		if(user.reqId().equals(ent.req(TaPrjProject.ATT_I_AUT_USER_01))) {
			ent.doPutRole(user.reqId(), TaPrjProject.LEV_ROLE_PRJ_MANAGER);
		} else {
			if (ent.canHaveRole(user.reqId())) return true;

			Criterion cri = Restrictions.and(
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER), 
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02	, user.reqId()),

					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT), 
					Restrictions.or (
							Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, ent.reqId()),
							Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, ent.reqInt(TaPrjProject.ATT_I_GROUP))
							));

			List<TaTpyRelationship> rolePrj 		= TaTpyRelationship.DAO.reqList(Order.desc(TaTpyRelationship.ATT_I_ENTITY_ID_01), cri);

			if(rolePrj == null || rolePrj.size() == 0) {
				return false;
			} else {
				//---list ordered then the first element is the ent in question
				ent.doPutRole(user.reqId(), rolePrj.get(0).reqInt(TaTpyRelationship.ATT_I_LEVEL));
			}
		}
		return true;
	}

	private static boolean canBeMemberOfEntity(TaPrjProject ent, TaAutUser user) throws Exception {
		if(user.reqId().equals(ent.req(TaPrjProject.ATT_I_AUT_USER_01))) {
			ent.doPutRole(user.reqId(), TaPrjProject.LEV_ROLE_PRJ_MANAGER);
		} else {
			//-----if cache is ok--------------------------------------------------------
			if (ent.canHaveRole(user.reqId())) return true;

			//-----check db for member---------------------------------------------------
			Criterion cri = Restrictions.and(
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER), 
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02	, user.reqId()),

					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT), 
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, ent.reqId())
					);

			List<TaTpyRelationship> rolePrj 		= TaTpyRelationship.DAO.reqList(Order.desc(TaTpyRelationship.ATT_I_ENTITY_ID_01), cri);

			if(rolePrj == null || rolePrj.size() == 0) {
				
				//---test member with group	---------------------------------------------------------	
				List<TaTpyRelationship>	grps		= TaTpyRelationship.DAO.reqList(Restrictions.and(
						Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_NSO_GROUP), 
						Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT), 
						Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, ent.reqId())
						));
				if(grps == null || grps.size() == 0) 	return false;
				
				Hashtable				grpDict		= ToolDBEntity.reqTabKeyInt(grps, TaTpyRelationship.ATT_I_ENTITY_ID_02);
				List<TaNsoGroupMember> 	lstMember 	= TaNsoGroupMember.DAO.reqList_In(TaNsoGroupMember.ATT_I_NSO_GROUP, grpDict.keySet(), Restrictions.eq(TaNsoGroupMember.ATT_I_AUT_USER, user.reqId()));
				
				if (lstMember==null || lstMember.size() == 0) return false;
				
				TaNsoGroupMember 		mem 		= lstMember.get(0);
				Integer					grpId		= mem.reqInt(TaNsoGroupMember.ATT_I_NSO_GROUP);
				TaTpyRelationship		grp			= (TaTpyRelationship) grpDict.get(grpId);
				ent.doPutRole(user.reqId(), grp.reqInt(TaTpyRelationship.ATT_I_LEVEL));
				
			} else {
				//---list ordered then the first element is the ent in question
				ent.doPutRole(user.reqId(), rolePrj.get(0).reqInt(TaTpyRelationship.ATT_I_LEVEL));
			}
		}
		return true;
	}

	private static boolean canBeManagerOfEntity(TaPrjProject ent, TaAutUser user) throws Exception {
		if(user.reqId().equals(ent.req(TaPrjProject.ATT_I_AUT_USER_01))) {
			ent.doPutRole(user.reqId(), TaPrjProject.LEV_ROLE_PRJ_MANAGER);
			return true;
		} else {
			if (ent.canHaveRole(user.reqId())) return true;

			Criterion cri = Restrictions.and(
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER), 
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02	, user.reqId()),

					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT), 
					Restrictions.or (
							Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, ent.reqInt(TaPrjProject.ATT_I_PARENT)),
							Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, ent.reqInt(TaPrjProject.ATT_I_GROUP))
							));

			List<TaTpyRelationship> rolePrj 		= TaTpyRelationship.DAO.reqList(Order.desc(TaTpyRelationship.ATT_I_ENTITY_ID_01), cri);

			if(rolePrj == null || rolePrj.size() == 0) {
				return false;
			} else {
				//---list ordered then the first element is the ent in question
				int level = rolePrj.get(0).reqInt(TaTpyRelationship.ATT_I_LEVEL);
				if (level == TaPrjProject.LEV_ROLE_PRJ_MANAGER){
					ent.doPutRole(user.reqId(), TaPrjProject.LEV_ROLE_PRJ_MANAGER);
					return true;
				} else return false;
			}
		}
	}
	
	
	private static boolean canBeMemberOfSprint(TaPrjProject ent, TaAutUser user) throws Exception {
		//---get list sprint of project-----
		Integer 	grpPrjId 	= ent.reqInt(TaPrjProject.ATT_I_GROUP);
		Criterion 	cri 		= Restrictions.and(
				Restrictions.eq		(TaPrjProject.ATT_I_GROUP			, grpPrjId), 
				Restrictions.eq		(TaPrjProject.ATT_I_TYPE_00			, TaPrjProject.TYP_00_PRJ_SPRINT),

				Restrictions.like	(TaPrjProject.ATT_T_INFO_02			, "%" + ent.reqId()+"%") 
				);


		List<TaPrjProject> sprints = TaPrjProject.DAO.reqList(cri);
		if(sprints == null || sprints.size() == 0) {
			return false;
		}

		Set<Integer> spIds = ToolSet.reqSetInt(sprints, TaPrjProject.ATT_I_ID);

		//-----check db for member---------------------------------------------------
		List<TaTpyRelationship> rolePrj 		= TaTpyRelationship.DAO.reqList_In(TaTpyRelationship.ATT_I_ENTITY_ID_01, spIds, 
				Restrictions.and(
						Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT),
						Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER), 
						Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02	, user.reqId())
						));

		if(rolePrj == null || rolePrj.size() == 0) {

			//---test member with group	---------------------------------------------------------	
			List<TaTpyRelationship>	grps		= TaTpyRelationship.DAO.reqList_In(TaTpyRelationship.ATT_I_ENTITY_ID_01, spIds,
					Restrictions.and(
						Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_NSO_GROUP), 
						Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT), 
						Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, ent.reqId())
					));
			if(grps == null || grps.size() == 0) 	return false;

			Hashtable				grpDict		= ToolDBEntity.reqTabKeyInt(grps, TaTpyRelationship.ATT_I_ENTITY_ID_02);
			List<TaNsoGroupMember> 	lstMember 	= TaNsoGroupMember.DAO.reqList_In(TaNsoGroupMember.ATT_I_NSO_GROUP, grpDict.keySet(), Restrictions.eq(TaNsoGroupMember.ATT_I_AUT_USER, user.reqId()));

			if (lstMember==null || lstMember.size() == 0) return false;

			TaNsoGroupMember 		mem 		= lstMember.get(0);
			Integer					grpId		= mem.reqInt(TaNsoGroupMember.ATT_I_NSO_GROUP);
			TaTpyRelationship		grp			= (TaTpyRelationship) grpDict.get(grpId);
			ent.doPutRole(user.reqId(), grp.reqInt(TaTpyRelationship.ATT_I_LEVEL));

		} else {
			//---list ordered then the first element is the ent in question
			ent.doPutRole(user.reqId(), rolePrj.get(0).reqInt(TaTpyRelationship.ATT_I_LEVEL));
		}

		return true;
	}
	
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	private static Set<Integer> setTypeU = new HashSet<Integer>() {
		{
			add(TaAutUser.TYPE_01_ADM); 
			add(TaAutUser.TYPE_01_SUP_ADM);
			add(TaAutUser.TYPE_01_AGENT);
			//			add(TaAutUser.TYPE_01_CLIENT);
			//			add(TaAutUser.TYPE_01_MENTOR);
		}
	};
	//---------------------------------------------------------------------------------------------------------
	private static void doGet(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");

		Integer 			objId	= ToolData.reqInt	(json, "id"		, -1	);	
		String	 			objCode	= ToolData.reqStr	(json, "code"	, null	);
		Boolean				forced	= ToolData.reqBool	(json, "forced"	, false	);

		if (objCode==null||objId==null|objId<0) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		TaPrjProject		ent 	= reqGet(user, forced, objId, objCode );

		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
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

	private static TaPrjProject reqGet(TaAutUser user , boolean forced, Integer objId, String objCode) throws Exception{
		//---logic of GET: user must be member of entity (member direct/member of group in this entity)
		//---if entity is 
		//------------- Sprint	: user is member
		//------------- Project	: user is member
		//------------- Epic	: user is member/ user is manager in parent node
		//------------- Task	: user is member/ user is manager in parent node/ user is member of sprint containning this task
		TaPrjProject 		ent			= null;
		if (!forced) 		ent 		= TaPrjProject.reqPrjFromCache (user, objId, objCode);
		boolean 			userInPrj   = false;

		if(ent!= null)	{
			//---check if user is a member in ent or manager of all project/parent
			userInPrj 		= canBeMemberOfEntity(ent, user);
			if(!userInPrj)	
				userInPrj 	= canBeManagerOfEntity (ent, user);	

			if(!userInPrj)	{
				Integer typ02 = (Integer) ent.req(TaPrjProject.ATT_I_TYPE_02);
				if (!typ02.equals(TaPrjProject.TYP_02_PRJ_ELE)) return null;
				
				userInPrj 	= canBeMemberOfSprint(ent, user);	
			}
				
			if(!userInPrj)	return null;	
			return ent;
		}

		//----------------------------------------------------------------------------------------------------------------
		if (forced || ent == null) {
			if (!user.canBeSuperAdmin())
				ent 	= TaPrjProject.DAO.reqEntityByValues(TaPrjProject.ATT_I_ID, objId, TaPrjProject.ATT_T_CODE_01, objCode, TaPrjProject.ATT_I_PER_MANAGER, user.reqPerManagerId());
			else
				ent 	= TaPrjProject.DAO.reqEntityByValues(TaPrjProject.ATT_I_ID, objId, TaPrjProject.ATT_T_CODE_01, objCode);

			TaPrjProject.doPutPrjToCache (objId, objCode, ent);
		} 

		if(ent == null)	return null;

		//---check if user is a member in ent or manager of all project/parent
		userInPrj 		= canBeMemberOfEntity(ent, user);
		if(!userInPrj)	
			userInPrj 	= canBeManagerOfEntity (ent, user);	
		if(!userInPrj)	return null;	


		ent.doBuildDocuments(forced);//--build attached file + avatar
		//---do build something other of ent like details....

		//---build count info for prj/epic 
		Integer typ02 = (Integer) ent.req(TaPrjProject.ATT_I_TYPE_02);
		ent.doBuildEpicInfo	(forced);


		if (!typ02.equals(TaPrjProject.TYP_02_PRJ_MAIN)) ent.doBuildParent	(forced, user);

		//---build other data
		Integer typ00 = (Integer) ent.req(TaPrjProject.ATT_I_TYPE_00);
		switch (typ00){

		case TaPrjProject.TYP_00_PRJ_SPRINT	: 	
			ent.doBuildSprintTasks(forced); 
			break;

		case TaPrjProject.TYP_00_PRJ_TEST	: 	
			ent.doBuildTestGroup(forced);
			ent.doBuildTestGroupHistory(forced); 
			break;

		case TaPrjProject.TYP_00_PRJ_EMAIL	: 	
			List<TaTpyRelationship> list 		= TaTpyRelationship.reqList (	DefDBExt.ID_TA_PRJ_PROJECT, ent.reqId(), DefDBExt.ID_TA_NSO_GROUP, null);
			Set<Integer> 			setG 		= ToolSet.reqSetInt(list, TaTpyRelationship.ATT_I_ENTITY_ID_02);
			doBuildGroupMemberInfo (setG, list);

			ent.reqSet(TaPrjProject.ATT_O_GROUPS, list);
			break;

		default:
			ent.doBuildEpics(forced);
			ent.doBuildTasks(forced);
			ent.doCopyInf03FromPrjMain(forced);
		}

		return ent;
	}
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	private static void doGetMember(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		TaPrjProject 	prj		= TaPrjProject.reqPrjFromCache(user, json);

		//----prj must be in cache---
		if (prj==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		int objId = prj.reqId();

		List<TaTpyRelationship> list 	= TaTpyRelationship.reqList(DefDBExt.ID_TA_PRJ_PROJECT, objId, DefDBExt.ID_TA_AUT_USER, null);

		//---build users + their avatars
		List<ViAutUserMember> lstUsers = TaAutUser		.reqBuildUserMember	(list, TaTpyRelationship.ATT_I_ENTITY_ID_02, TaTpyRelationship.ATT_O_ENTITY_02);
		TaTpyDocument	.reqBuildAvatar(lstUsers, DefDBExt.ID_TA_AUT_USER, ViAutUserMember.ATT_O_AVATAR);

		//		Set<Integer> 			setU 	= ToolSet.reqSetInt(list, TaTpyRelationship.ATT_I_ENTITY_ID_02);
		//		doBuildMemberInfo (setU, list);
		prj.reqSet(TaPrjProject.ATT_O_MEMBERS, list);

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, list 
				));
	}

	private static void doSaveMember(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		TaPrjProject 	prj		= TaPrjProject.reqPrjFromCache(user, json);
		//----prj must be in cache---
		if (prj==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		JSONArray				members 	= ToolData.reqJsonArr	(json, "members"	, null);
		if(members==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		int objId = prj.reqId();

		List<TaTpyRelationship> list 	= TaTpyRelationship.reqListMod (DefDBExt.ID_TA_PRJ_PROJECT, objId, DefDBExt.ID_TA_AUT_USER, null, members);

		//---check owner in the list-----
		Set<Integer> 			setU 	= ToolSet.reqSetInt(list, TaTpyRelationship.ATT_I_ENTITY_ID_02);
		Integer					ownerId	= (Integer) prj.req(TaPrjProject.ATT_I_AUT_USER_01);
		if (!setU.contains(ownerId)) {
			TaTpyRelationship owner		= new TaTpyRelationship(DefDBExt.ID_TA_PRJ_PROJECT, DefDBExt.ID_TA_AUT_USER, objId, ownerId);
			owner.reqSet(TaTpyRelationship.ATT_I_LEVEL	, TaPrjProject.LEV_ROLE_PRJ_REPORTER);
			owner.reqSet(TaTpyRelationship.ATT_I_TYPE	, TaPrjProject.TYP_LEV_TOP);
			owner.reqSet(TaTpyRelationship.ATT_I_STATUS	, TaTpyRelationship.STAT_ACTIVE);

			TaTpyRelationship.DAO.doPersist(owner);

			list.add(owner);
			setU.add(ownerId);
		}

		//---build avatar and user info
		//		doBuildMemberInfo (setU, list);

		//---build users + their avatars
		List<ViAutUserMember> lstUsers = TaAutUser		.reqBuildUserMember	(list, TaTpyRelationship.ATT_I_ENTITY_ID_02, TaTpyRelationship.ATT_O_ENTITY_02);
		TaTpyDocument	.reqBuildAvatar( lstUsers, DefDBExt.ID_TA_AUT_USER, ViAutUserMember.ATT_O_AVATAR);

		prj.reqSet(TaPrjProject.ATT_O_MEMBERS, list);

		Integer stat =  (Integer) prj.req(TaPrjProject.ATT_I_STATUS_01);
		if (stat == TaPrjProject.STAT_01_PRJ_TODO || stat == TaPrjProject.STAT_01_PRJ_INPROGRESS ||  stat == TaPrjProject.STAT_01_PRJ_REVIEW || stat == TaPrjProject.STAT_01_PRJ_TEST ) {
			//req to Notification
			JSONArray arrNotify 		= new JSONArray();
			//			arrNotify.add(reqNotiJson(user, prj, TYP_HAS_CHANGE, TYP_MOD, (Integer) prj.req(TaPrjProject.ATT_I_AUT_USER_01)));

			doBuilNotiArray(list	, arrNotify, user, prj, TYP_JOIN	, TYP_ADD);
			reqNewNoti(prj.reqId(), arrNotify);
		}

		//--Save members to project main if dont exists
		if(prj.reqInt(TaPrjProject.ATT_I_TYPE_02) != TaPrjProject.TYP_02_PRJ_MAIN) {
			doSaveMemberToProject(prj, list);
		}


		//--send mail join or out project to user
		doSendEmailUserJoin (list , prj);
		//		doSendEmailUserLeave(lstDEL, prj);

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES
				));
	}

	private static void doSaveMemberToProject(TaPrjProject prj, List<TaTpyRelationship> listMod) throws Exception  {
		if(prj == null || listMod == null || listMod.size() <= 0) return;

		if(prj.reqInt(TaPrjProject.ATT_I_TYPE_02) != TaPrjProject.TYP_02_PRJ_MAIN) {
			prj 			= TaPrjProject.DAO.reqEntityByValue(TaPrjProject.ATT_I_ID, prj.reqInt(TaPrjProject.ATT_I_PARENT));
		}
		if(prj == null) return;

		if(prj.reqInt(TaPrjProject.ATT_I_TYPE_02) != TaPrjProject.TYP_02_PRJ_MAIN) {
			prj 			= TaPrjProject.DAO.reqEntityByValue(TaPrjProject.ATT_I_ID, prj.reqInt(TaPrjProject.ATT_I_PARENT));
		}
		if(prj == null) return;

		if(prj.reqInt(TaPrjProject.ATT_I_TYPE_02) != TaPrjProject.TYP_02_PRJ_MAIN) return;

		List<TaTpyRelationship> 		lstNew 		= new ArrayList<>();
		List<TaTpyRelationship> 		lstExists 	= new ArrayList<>();
		Map<Integer, TaTpyRelationship> mapExists 	= new HashMap<>();

		Criterion cri = Restrictions.and(
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, prj.reqId()),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER));
		lstExists = TaTpyRelationship.DAO.reqList(cri);

		if(lstExists.size() <= 0) return;

		for(TaTpyRelationship r : lstExists) {
			mapExists.put(r.reqInt(TaTpyRelationship.ATT_I_ENTITY_ID_02), r);
		}

		for(TaTpyRelationship r : listMod) {
			TaTpyRelationship rE = mapExists.get(r.reqInt(TaTpyRelationship.ATT_I_ENTITY_ID_02));

			if(rE != null) continue;

			rE = new TaTpyRelationship(
					DefDBExt.ID_TA_PRJ_PROJECT, 
					DefDBExt.ID_TA_AUT_USER, 
					prj.reqId(),
					r.reqInt(TaTpyRelationship.ATT_I_ENTITY_ID_02));
			rE.reqSet(TaTpyRelationship.ATT_I_TYPE, 1);
			rE.reqSet(TaTpyRelationship.ATT_I_LEVEL, TaPrjProject.LEV_ROLE_PRJ_WORKER);
			rE.reqSet(TaTpyRelationship.ATT_D_DATE_01, new Date());

			lstNew.add(rE);
		}

		if(lstNew.size() <= 0) return;

		TaTpyRelationship.DAO.doPersist(lstNew);
	}
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	public static void doBuildGroupMemberInfo (Set<Integer> setGroupIds, List<TaTpyRelationship> list) throws Exception {
		if(setGroupIds!=null && setGroupIds.size()>0) {
			List<TaNsoGroup> 		listGrps 	= TaNsoGroup.DAO.reqList_In(TaNsoGroup.ATT_I_ID, setGroupIds,
					Restrictions.eq(TaNsoGroup.ATT_I_STATUS_01	, TaNsoGroup.STAT_01_ACTIVE)
					);

			if(listGrps != null && listGrps.size() > 0) {
				TaTpyDocument.reqBuildAvatar (listGrps, DefDBExt.ID_TA_NSO_GROUP, TaNsoGroup.ATT_O_AVATAR);

				Map<Integer, TaNsoGroup> gMap = new HashMap<Integer, TaNsoGroup>();
				for(TaNsoGroup u : listGrps) {
					gMap.put((Integer) u.reqId(), u);
				}

				//---remove all user inactive------
				List<TaTpyRelationship> listDel 	= new ArrayList<TaTpyRelationship> ();
				for(TaTpyRelationship mem : list) {
					if(!gMap.containsKey(mem.req(TaTpyRelationship.ATT_I_ENTITY_ID_02))){							
						listDel.add(mem);
						continue;
					}
					mem.reqSet(TaTpyRelationship.ATT_O_ENTITY_02, gMap.get(mem.req(TaTpyRelationship.ATT_I_ENTITY_ID_02)));
				}
				if (listDel.size()>0) {
					list.removeAll(listDel);
					TaTpyRelationship.DAO.doRemove(listDel);
				}
				
				if (list.size()>0) {
					//--build lst members
					List<TaNsoGroupMember> 				lstMem = TaNsoGroupMember.DAO.reqList_In(TaNsoGroupMember.ATT_I_NSO_GROUP, gMap.keySet());
					
					//---build users + their avatars
					List<ViAutUserMember> lstUsers = TaAutUser		.reqBuildUserMember	(lstMem, TaNsoGroupMember.ATT_I_AUT_USER, TaNsoGroupMember.ATT_O_MEMBER);
					TaTpyDocument	.reqBuildAvatar( lstUsers, DefDBExt.ID_TA_AUT_USER, ViAutUserMember.ATT_O_AVATAR);
					
					for (TaNsoGroupMember m: lstMem) {
						TaNsoGroup  grp =  gMap.get(m.req(TaNsoGroupMember.ATT_I_NSO_GROUP));
						grp.doAddMember(m);
					}
				}
			}else {
				TaTpyRelationship.DAO.doRemove(list);
				list.clear();
			}
		}
	}

	private static void doGetGroupMember(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		TaPrjProject 	prj		= TaPrjProject.reqPrjFromCache(user, json);

		//----prj must be in cache---
		if (prj==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		int objId = prj.reqId();
		List<TaTpyRelationship> list 		= TaTpyRelationship.reqList (	DefDBExt.ID_TA_PRJ_PROJECT, objId, DefDBExt.ID_TA_NSO_GROUP, null);
		Set<Integer> 			setG 		= ToolSet.reqSetInt(list, TaTpyRelationship.ATT_I_ENTITY_ID_02);
		doBuildGroupMemberInfo (setG, list);//---add nsoGrp in ent02 of relationship

		prj.reqSet(TaPrjProject.ATT_O_GROUPS, list);

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, list 
				));
	}

	private static void doSaveGroupMember(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		TaPrjProject 	prj		= TaPrjProject.reqPrjFromCache(user, json);
		//----prj must be in cache---
		if (prj==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		int 			objId 	= prj.reqId();
		JSONArray		groups 	= ToolData.reqJsonArr	(json, "groups"	, null);
		if(groups==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}


		List<TaTpyRelationship> list 	= TaTpyRelationship.reqListMod (DefDBExt.ID_TA_PRJ_PROJECT, objId, DefDBExt.ID_TA_NSO_GROUP, null, groups);

		//---check owner in the list-----
		Set<Integer> 			setG 	= ToolSet.reqSetInt(list, TaTpyRelationship.ATT_I_ENTITY_ID_02);

		//---build avatar and group info
		doBuildGroupMemberInfo (setG, list);
		prj.reqSet(TaPrjProject.ATT_O_GROUPS, list);

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, list 
				));
	}
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------

	//---------------------------------------------------------------------------------------------------------
	private static void doGetComments(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		TaPrjProject 	prj		= TaPrjProject.reqPrjFromCache(user, json);

		//----prj must be in cache---
		if (prj==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		ResultPagination  	res = TaNsoPost.reqGetPost(user, json, DefDBExt.ID_TA_PRJ_PROJECT ,prj.reqId()); 
		if (res.reqList()==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, res 
				));
	}

	private static void doSaveComments(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		TaPrjProject 	prj		= TaPrjProject.reqPrjFromCache(user, json);

		//----prj must be in cache---
		if (prj==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		Integer cmtId	 = ToolData.reqInt(json	, "cmtId"	, null);
		String 	comment = ToolData.reqStr(json	, "comment"	, null);
		Integer parId 	= ToolData.reqInt(json	, "parId"	, null);

		if(comment == null || comment.length() == 0) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		TaNsoPost post = null;
		if (cmtId==null){
			post = new TaNsoPost();
			post.reqSet(TaNsoPost.ATT_I_VAL_01		, DefDBExt.ID_TA_PRJ_PROJECT);
			post.reqSet(TaNsoPost.ATT_I_VAL_02		, prj.reqId());
			post.reqSet(TaNsoPost.ATT_I_TYPE_01		, TaNsoPost.TYPE_01_CMT);
			post.reqSet(TaNsoPost.ATT_I_STATUS_01	, TaNsoPost.STAT_01_ACTIVE);
			post.reqSet(TaNsoPost.ATT_I_STATUS_02	, TaNsoPost.STAT_02_PUBLIC);
			post.reqSet(TaNsoPost.ATT_I_AUT_USER_01	, user.reqId());
			post.reqSet(TaNsoPost.ATT_D_DATE_01		, new Date());	
		}else {
			post = TaNsoPost.DAO.reqEntityByID(cmtId);
		}

		if (post==null||!post.reqInt(TaNsoPost.ATT_I_AUT_USER_01).equals(user.reqId())) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		post.reqSet(TaNsoPost.ATT_T_CONTENT_01	, comment);
		post.reqSet(TaNsoPost.ATT_D_DATE_02		, new Date());
		post.reqSet(TaNsoPost.ATT_I_PARENT		, parId);

		if (post.reqId()==null) 
			TaNsoPost.DAO.doPersist(post);
		else
			TaNsoPost.DAO.doMerge(post);

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, post 
				));
	}
	//---------------------------------------------------------------------------------------------------------
	private static void doGetReports(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		TaPrjProject 	prj		= TaPrjProject.reqPrjFromCache(user, json);

		//----prj must be in cache---
		if (prj==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		ResultPagination  	res = TaTpyInformation.reqGetInfo(user, json, DefDBExt.ID_TA_PRJ_PROJECT ,prj.reqId()); 
		if (res.reqList()==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, res 
				));
	}

	private static void doSaveReport(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		TaPrjProject 	prj		= TaPrjProject.reqPrjFromCache(user, json);

		//----prj must be in cache---
		if (prj==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		TaTpyInformation 			ent		= reqSaveReport		(user, json, prj	, response);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent
				));		
	}

	private static TaTpyInformation reqSaveReport(TaAutUser user,  JSONObject json, TaPrjProject prj, HttpServletResponse response) throws Exception {
		JSONObject			obj		= ToolData.reqJson 		(json, "obj", new JSONObject());

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaTpyInformation.class);
		TaTpyInformation 	inf		= null;

		//----some information can not be modified----------------------------
		Integer repID = (Integer) attr.get(TaTpyInformation.ATT_I_ID);

		attr.put(TaTpyInformation.ATT_I_STATUS		, TaTpyInformation.STAT_VALIDATED);
		attr.put(TaTpyInformation.ATT_D_DATE_02		, new Date());
		attr.put(TaTpyInformation.ATT_I_AUT_USER_02	, user.reqId());
		attr.put(TaTpyInformation.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_PRJ_PROJECT);
		attr.put(TaTpyInformation.ATT_I_ENTITY_ID	, prj.reqId());

		if (repID==null  || repID<0) {
			attr.put(TaTpyInformation.ATT_D_DATE_01		, new Date());
			attr.put(TaTpyInformation.ATT_I_AUT_USER_01	, user.reqId());
			inf = new TaTpyInformation(attr);
			TaTpyInformation.DAO.doPersist(inf);
		}else {
			attr.remove(TaTpyInformation.ATT_D_DATE_01		);
			attr.remove(TaTpyInformation.ATT_I_AUT_USER_01	);
			inf = TaTpyInformation.DAO.reqEntityByID(repID);
			Integer entId  = inf.reqInt(TaTpyInformation.ATT_I_ENTITY_ID);
			Integer entTyp = inf.reqInt(TaTpyInformation.ATT_I_ENTITY_TYPE);
			if (entId==null||entTyp==null||entTyp!=DefDBExt.ID_TA_PRJ_PROJECT||entId!=prj.reqId()) return null;
			TaTpyInformation.DAO.doMerge(inf, attr);
		}

		if(inf != null) {
			TaTpyInformation.doTpyReqSaveFile(user, obj, inf);
		}

		return inf;
	}	
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	private static void doBuilNotiArray(List<TaTpyRelationship> lst, JSONArray arr, TaAutUser user, TaPrjProject prj, Integer typMember, Integer typAction) {
		Set<Integer> setMem = new HashSet<Integer>();

		if(lst != null && lst.size() > 0) {
			setMem.addAll(ToolSet.reqSetInt(lst, TaTpyRelationship.ATT_I_ENTITY_ID_02));
		}

		if(!setMem.isEmpty()) {
			for(Integer mem: setMem) {
				JSONObject notify 		= reqNotiJson(user, prj, typMember, typAction, mem);
				arr.add(notify);
			}
		}
	}

	private static JSONObject reqNotiJson(TaAutUser user, TaPrjProject prj, Integer typMember, Integer typAction, Integer mem) {
		JSONObject notify 		= new JSONObject();
		notify.put("typ"		, typMember);
		notify.put("typTab"		, TAB_MEMBER);
		notify.put("title"		, prj.req(TaPrjProject.ATT_T_CODE_01));
		notify.put("uID"		, mem);
		notify.put("uAction"	, user.reqRef());
		notify.put("parTyp"		, DefDBExt.ID_TA_PRJ_PROJECT);
		notify.put("parID"		, prj.reqId());

		return notify;
	}

	//-------------------------------------------------------------------------------------------------------
	private static String EMAIL_PRJ_HOST 		= "";
	private static String EMAIL_PRJ_PORT 		= "";
	private static String EMAIL_PRJ_LOGIN 		= "";
	private static String EMAIL_PRJ_PWD 		= "";

	private static String 	EMAIL_PRJ_JOIN_TITLE 	= "";
	private static String 	EMAIL_PRJ_JOIN_CONT 	= "";
	private static Integer 	EMAIL_PRJ_JOIN_CONT_NB 	= 1;
	private static String 	EMAIL_PRJ_JOIN_LINK		= "";

	private static String 	EMAIL_PRJ_LEAVE_TITLE 	= "";
	private static String 	EMAIL_PRJ_LEAVE_CONT 	= "";
	private static Integer 	EMAIL_PRJ_LEAVE_CONT_NB 	= 1;

	public static void doSendEmailUserJoin(final List<TaTpyRelationship> lst, final TaPrjProject prj){
		if (lst==null || lst.size()==0) return;
		Thread t = new Thread(){
			public void run(){
				try{
					TaCfgValue  cfgVal = TaCfgValue.DAO.reqEntityByValue(TaCfgValue.ATT_T_CODE, "PRJ_EMAIL");
					if (cfgVal==null) return;

					JSONObject config = ToolJSON.reqJSonFromString(cfgVal.reqStr(TaCfgValue.ATT_T_INFO_01));

					EMAIL_PRJ_HOST 			= (String) config.get("prj_email_host" 				);
					EMAIL_PRJ_PORT 			= (String) config.get("prj_email_port" 				);
					EMAIL_PRJ_LOGIN 		= (String) config.get("prj_email_login" 			);
					EMAIL_PRJ_PWD 			= (String) config.get("prj_email_pwd" 				);
					EMAIL_PRJ_JOIN_TITLE 	= (String) config.get("prj_email_join_prj_title" 	);
					EMAIL_PRJ_JOIN_CONT 	= (String) config.get("prj_email_join_prj_content"	);
					EMAIL_PRJ_JOIN_CONT_NB 	= Integer.parseInt((String) config.get("prj_email_join_prj_content"	));
					EMAIL_PRJ_JOIN_LINK 	= (String) config.get("prj_email_join_prj_link" 	);


					Integer       pId     = (Integer) prj.req(TaPrjProject.ATT_I_ID);
					Integer       pTyp    = (Integer) prj.req(TaPrjProject.ATT_I_TYPE_02);
					String        pName   = prj.req(TaPrjProject.ATT_T_NAME).toString();
					String        pCode   = prj.req(TaPrjProject.ATT_T_CODE_01).toString();

					for(TaTpyRelationship re: lst) {
						Integer 		uId 	= (Integer) re.req(TaTpyRelationship.ATT_I_ENTITY_ID_02);
						TaAutUser 		u 		= TaAutUser.DAO.reqEntityByValue(TaAutUser.ATT_I_ID, uId);
						String 			email 	= u.req(TaAutUser.ATT_T_INFO_01).toString();
						if (email==null || email.length()<1) continue;

						String emailTitle 	= EMAIL_PRJ_JOIN_TITLE;
						String emailLink	= String.format(EMAIL_PRJ_JOIN_LINK, prj.reqRef());
						String emailCont 	= reqFormattedContent(EMAIL_PRJ_JOIN_CONT, pCode + " - " + pName, emailLink, EMAIL_PRJ_JOIN_CONT_NB); 

						ToolEmail.canSendEmail(
								EMAIL_PRJ_HOST, EMAIL_PRJ_PORT, null, EMAIL_PRJ_LOGIN, EMAIL_PRJ_PWD, 
								EMAIL_PRJ_LOGIN, 
								emailTitle, emailCont,
								email, null, null, null);	
					}

				}catch(Exception e){

				}
			}
		};
		ThreadManager.doExecute(t, DefTime.TIME_00_00_05_000);
	}

	public static void doSendEmailUserLeave(final List<TaTpyRelationship> lst, final TaPrjProject prj){
		if (lst==null || lst.size()==0) return;
		Thread t = new Thread(){
			public void run(){
				try{
					TaCfgValue  cfgVal = TaCfgValue.DAO.reqEntityByValue(TaCfgValue.ATT_T_CODE, "PRJ_EMAIL");
					if (cfgVal==null) return;
					JSONObject config = ToolJSON.reqJSonFromString(cfgVal.reqStr(TaCfgValue.ATT_T_INFO_01));

					EMAIL_PRJ_HOST 			= (String) config.get("prj_email_host" 				);
					EMAIL_PRJ_PORT 			= (String) config.get("prj_email_port" 				);
					EMAIL_PRJ_LOGIN 		= (String) config.get("prj_email_login" 			);
					EMAIL_PRJ_PWD 			= (String) config.get("prj_email_pwd" 				);
					EMAIL_PRJ_JOIN_TITLE 	= (String) config.get("prj_email_join_prj_title" 	);
					EMAIL_PRJ_JOIN_CONT 	= (String) config.get("prj_email_join_prj_content"	);
					EMAIL_PRJ_JOIN_CONT_NB 	= Integer.parseInt((String) config.get("prj_email_join_prj_content"	));
					EMAIL_PRJ_JOIN_LINK 	= (String) config.get("prj_email_join_prj_link" 	);

					String        pName   = prj.req(TaPrjProject.ATT_T_NAME).toString();
					String        pCode   = prj.req(TaPrjProject.ATT_T_CODE_01).toString();

					for(TaTpyRelationship re: lst) {
						Integer 		uId 	= (Integer) re.req(TaTpyRelationship.ATT_I_ENTITY_ID_02);
						TaAutUser 		u 		= TaAutUser.DAO.reqEntityByValue(TaAutUser.ATT_I_ID, uId);
						String 			email 	= u.req(TaAutUser.ATT_T_INFO_01).toString();
						if (email==null || email.length()<1) continue;

						String emailTitle 	= EMAIL_PRJ_LEAVE_TITLE;						
						String emailCont 	= reqFormattedContent(EMAIL_PRJ_LEAVE_CONT, pCode, pName, EMAIL_PRJ_LEAVE_CONT_NB); 

						ToolEmail.canSendEmail(
								EMAIL_PRJ_HOST, EMAIL_PRJ_PORT, null, EMAIL_PRJ_LOGIN, EMAIL_PRJ_PWD, 
								EMAIL_PRJ_LOGIN, 
								emailTitle, emailCont,
								email, null, null, null);	
					}

				}catch(Exception e){

				}
			}
		};
		ThreadManager.doExecute(t, DefTime.TIME_00_00_05_000);

	}

	private static String reqFormattedContent(String template, String msg01, String msg02, Integer count) {
		String content = "";
		switch(count) {
		case 0:
		case 1: content 	= String.format(template, msg01, msg02);break;
		case 2: content 	= String.format(template, msg01, msg02, msg01, msg02);break;
		case 3: content 	= String.format(template, msg01, msg02, msg01, msg02, msg01, msg02);break;
		case 4: content 	= String.format(template, msg01, msg02, msg01, msg02, msg01, msg02, msg01, msg02);break;
		case 5: content 	= String.format(template, msg01, msg02, msg01, msg02, msg01, msg02, msg01, msg02, msg01, msg02);break;
		default : content 	= template;
		}
		return content;
	}

	// ----------------------------------------------------------------------------------------------------------------------------------------
	private static void doGetRelationship(TaAutUser user,  JSONObject json, HttpServletResponse response,
			Integer typ01, //DefDBExt.ID_TA_PRJ_WORKFLOW
			Integer typ02  //DefDBExt.ID_TA_PRJ_PROJECT  ,  DefDBExt.ID_TA_PRJ_PROJECT_TASK

			) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		TaPrjProject 	prj		= TaPrjProject.reqPrjFromCache(user, json);
		//----prj must be in cache---
		if (prj==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		int objId = prj.reqId();

		List<TaTpyRelationship> list 		= TaTpyRelationship.DAO.reqList(Restrictions.and(
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, typ01), 
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, typ02), 
				Restrictions.or(Restrictions.eq(TaTpyRelationship.ATT_I_STATUS	, TaTpyRelationship.STAT_ACTIVE), 
						Restrictions.isNull(TaTpyRelationship.ATT_I_STATUS)),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, objId)));


		Set<Integer> 		setU 		= ToolSet.reqSetInt(list, TaTpyRelationship.ATT_I_ENTITY_ID_02);
		if(setU!=null && setU.size()>0) {
			List<TaPrjProject> 		listPrj 	= TaPrjProject.DAO.reqList_In(TaPrjProject.ATT_I_ID, setU);

			if(listPrj != null && listPrj.size() > 0) {
				Map<Integer, TaPrjProject> uMap = new HashMap<Integer, TaPrjProject>();
				for(TaPrjProject u : listPrj) {
					uMap.put((Integer) u.reqRef(), u);
				}

				//---remove all user inactive------
				List<TaTpyRelationship> listDel = new ArrayList<TaTpyRelationship> ();
				for(TaTpyRelationship mem : list) {
					if(!uMap.containsKey(mem.req(TaTpyRelationship.ATT_I_ENTITY_ID_02))){							
						listDel.add(mem);
						continue;
					}
					mem.reqSet(TaTpyRelationship.ATT_O_ENTITY_02, uMap.get(mem.req(TaTpyRelationship.ATT_I_ENTITY_ID_02)));
				}
				if (listDel.size()>0) {
					list.removeAll(listDel);
					TaTpyRelationship.DAO.doRemove(listDel);
				}					
			}else {
				TaTpyRelationship.DAO.doRemove(list);
				list.clear();
			}
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, list 
				));
	}

	private static void doSaveRelationship(TaAutUser user,  JSONObject json, HttpServletResponse response,
			Integer typ01, //DefDBExt.ID_TA_PRJ_WORKFLOW
			Integer typ02  //DefDBExt.ID_TA_PRJ_PROJECT   DefDBExt.ID_TA_PRJ_PROJECT_TASK
			) throws Exception  {

		TaPrjProject 	prj		= TaPrjProject.reqPrjFromCache(user, json);
		//----prj must be in cache---
		if (prj==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		int 			objId 	= prj.reqId();
		JSONArray		member 	= ToolData.reqJsonArr	(json, "members"	, new JSONArray()	);

		List<TaTpyRelationship> list 	= TaTpyRelationship.DAO.reqList(Restrictions.and(
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, typ01), 
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, typ02),
				Restrictions.or(Restrictions.eq(TaTpyRelationship.ATT_I_STATUS	, TaTpyRelationship.STAT_ACTIVE), 
						Restrictions.isNull(TaTpyRelationship.ATT_I_STATUS)),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, objId)));

		Map<Integer, TaTpyRelationship> mapExist = new HashMap<Integer, TaTpyRelationship>();
		for(TaTpyRelationship m: list) {
			mapExist.put((Integer) m.req(TaTpyRelationship.ATT_I_ENTITY_ID_02), m);
		}

		List<TaTpyRelationship> lstRE 		= new ArrayList<TaTpyRelationship>();
		List<TaTpyRelationship> lstMOD 		= new ArrayList<TaTpyRelationship>();
		List<TaTpyRelationship> lstDEL 		= new ArrayList<TaTpyRelationship>();

		Set<Integer>			idsToDel	= new HashSet<Integer>();
		for(int i = 0; i < member.size(); i++) {
			JSONObject mem = (JSONObject) member.get(i);
			Map<String, Object> attr 	= API.reqMapParamsByClass(mem, TaTpyRelationship.class);

			Integer pId 				= (Integer) attr.get(TaTpyRelationship.ATT_I_ENTITY_ID_02);
			idsToDel.add(pId);
			TaTpyRelationship re 		= new TaTpyRelationship(attr);

			if(mapExist.containsKey(pId)) {
				TaTpyRelationship memExist = mapExist.get(pId);
				memExist.reqSet(TaTpyRelationship.ATT_I_LEVEL	, attr.get(TaTpyRelationship.ATT_I_LEVEL));
				memExist.reqSet(TaTpyRelationship.ATT_I_TYPE	, attr.get(TaTpyRelationship.ATT_I_TYPE));
				memExist.reqSet(TaTpyRelationship.ATT_D_DATE_02, new Date());
				lstMOD.add(mapExist.get(pId));
				mapExist.remove(pId);
				continue;
			}

			re.reqSet(TaTpyRelationship.ATT_D_DATE_01		, new Date());
			re.reqSet(TaTpyRelationship.ATT_I_ENTITY_TYPE_01, typ01);
			re.reqSet(TaTpyRelationship.ATT_I_ENTITY_TYPE_02, typ02);
			re.reqSet(TaTpyRelationship.ATT_I_STATUS        , TaTpyRelationship.STAT_ACTIVE);
			lstRE.add(re);
		}

		Session sess = TaTpyRelationship.DAO.reqSessionCurrent();
		try {
			//---del old relationship first
			List<TaTpyRelationship> wfExist = TaTpyRelationship.DAO.reqList_In(sess,  TaTpyRelationship.ATT_I_ENTITY_ID_02,idsToDel, Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02, typ02));
			if (wfExist != null) {
				lstDEL.addAll(wfExist);
			}
			if(lstRE.size() > 0)	TaTpyRelationship.DAO.doPersist	(sess,lstRE);
			if(lstMOD.size() > 0) 	TaTpyRelationship.DAO.doMerge	(sess,lstMOD);

			if(!mapExist.isEmpty()) {
				for (Map.Entry<Integer, TaTpyRelationship> entry : mapExist.entrySet()) {
					lstDEL.add(entry.getValue());
				}
			}
			TaTpyRelationship.DAO.doRemove(sess,lstDEL);

			TaTpyRelationship.DAO.doSessionCommit(sess);
		}catch(Exception e) {
			if (sess!=null) TaTpyRelationship.DAO.doSessionRollback(sess);
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES
				));
	}

	//------------------------------------------------------------------------------------------------------------------------------------------
	private static void doSaveFiles(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		TaPrjProject 			ent		= reqPrjProjectSaveFiles		(user, json, response);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent
				));		
	}

	private static TaPrjProject reqPrjProjectSaveFiles(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		JSONObject			obj		= ToolData.reqJson	(json, "obj" ,	new JSONObject());

		if (!canWorkWithObj(user, WORK_NEW, obj)){ //other param after obj...
			return null;
		}

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaPrjProject.class);

		Integer 			prjID 	= (Integer) attr.get(TaPrjProject.ATT_I_ID);
		if(prjID == null)	return null;

		TaPrjProject  		ent	 	= TaPrjProject.DAO.reqEntityByRef(prjID);
		if(ent == null)	return null;		

		doPrjReqSaveFile(user, obj, ent);

		return ent;
	}

	private static void doAddFiles(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		TaPrjProject 			ent		= reqAddFiles		(user, json, response);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent
				));		
	}

	private static TaPrjProject reqAddFiles(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		TaPrjProject 	ent		= TaPrjProject.reqPrjFromCache(user, json);
		//----prj must be in cache---
		if (ent==null) {
			return null;
		}

		JSONObject			obj		= ToolData.reqJson	(json, "obj", new JSONObject());
		if (!canWorkWithObj(user, WORK_NEW, obj)){ //other param after obj...
			return null;
		}
		
		boolean canAdd = false;
		if (canBeMemberOfEntity (ent, user)) canAdd = true;

		if (canAdd) {
			doPrjReqAddFile(user, obj, ent);
			return ent;
		}
		return null;
	}

	private static void doDelFiles(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		TaPrjProject 			ent		= reqDelFiles		(user, json, response);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		} 

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES
				));		
	}

	private static TaPrjProject reqDelFiles(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		TaPrjProject 	ent		= TaPrjProject.reqPrjFromCache(user, json);
		//----prj must be in cache---
		if (ent==null) {
			return null;
		}

		Integer 			uId		= user.reqId();
		Integer 			fileId	= ToolData.reqInt	(json, "fileId", null);

		if (fileId==null  || fileId<=0 ) return null;

		boolean canDel = false;
		if (canBeManagerOfEntity (ent, user)) canDel = true;

		if (canDel) {
			TaTpyDocument.reqTpyDocumentDel(fileId);
			ent.doBuildDocuments(true);
			return ent;
		}
		return null;
	}
	//---------------------------------------------------------------------------------------------------------
	private static void doLst(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");

		List<TaPrjProject> 	list = reqLst(user, json, response); //and other params if necessary
		if (list==null ){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, list 
				));				
	}

	private static List<TaPrjProject> reqLst(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		Integer 			manId		= ToolData.reqInt	(json, "manId"		, null	);
		Integer 			parId		= ToolData.reqInt	(json, "parId"		, null	);
		Integer 			typ00		= ToolData.reqInt	(json, "typ00"		, null	);
		Integer 			typ01		= ToolData.reqInt	(json, "typ01"		, null	);
		Integer 			typ02		= ToolData.reqInt	(json, "typ02"		, null	);
		Boolean 			wBuild		= ToolData.reqBool	(json, "wBuild"		, false	);
		Boolean 			wAva		= ToolData.reqBool	(json, "wAva"		, false	);
		Boolean 			wMem		= ToolData.reqBool	(json, "wMem"		, false	);
		Boolean 			wGrp		= ToolData.reqBool	(json, "wGrp"		, false	);
		Integer 			uId			= user.reqId();

		//other params here
		if (!canWorkWithObj(user, WORK_LST, typ01)){ //other param after objTyp...
			return null;
		}

		Set<Integer> 		ids  	= reqRelationIds(uId);
		Criterion 			cri		= reqCriterion (manId, parId, typ00, typ01, typ02, ids); //and other params	
		List<TaPrjProject> 	list 	= null;

		if (cri!=null) 
			list							= TaPrjProject.DAO.reqList(cri);

		//do something else with request
		if (wBuild){	
			/*int i= 0;
			while(i<2) {
				List<TaPrjProject> nextLevelList	= reqSubProjectForListPrj(list, i);
				if (nextLevelList!=null) list.addAll(nextLevelList);
				i++;
			}*/
		}


		if (wAva) {
			TaPrjProject.doBuildAvatarForList(list);
		}

		if (wMem) {
			TaPrjProject.doBuildMemberForList(list);
		}

		if (wGrp) {
			TaPrjProject.doBuildGrpMemberForList(list);
		}

		return list;
	}

	private static Criterion reqCriterion(Integer manId, Integer parId, Integer typ00, Integer typ01, Integer typ02, Set<Integer> ids) throws Exception{
		Criterion cri = Restrictions.gt(TaPrjProject.ATT_I_ID, 0);	
		if (typ00!=null) cri = Restrictions.and(cri, Restrictions.eq(TaPrjProject.ATT_I_TYPE_00		, typ00));	
		if (typ01!=null) cri = Restrictions.and(cri, Restrictions.eq(TaPrjProject.ATT_I_TYPE_01		, typ01));	
		if (typ02!=null) cri = Restrictions.and(cri, Restrictions.eq(TaPrjProject.ATT_I_TYPE_02		, typ02));	
		if (manId!=null) cri = Restrictions.and(cri, Restrictions.eq(TaPrjProject.ATT_I_PER_MANAGER	, manId));	
		if (parId!=null) cri = Restrictions.and(cri, Restrictions.eq(TaPrjProject.ATT_I_PARENT		, parId));	
		if (ids  !=null) cri = Restrictions.and(cri, Restrictions.in(TaPrjProject.ATT_I_ID			, ids));	

		return cri;
	}

	private static Set<Integer> reqRelationIds (Integer uId) throws Exception {
		if (uId ==null) return null;
		Criterion cri = Restrictions.and(
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01		, DefDBExt.ID_TA_PRJ_PROJECT), 
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02		, DefDBExt.ID_TA_AUT_USER),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02		, uId));	
		List<TaTpyRelationship> lst = TaTpyRelationship.DAO.reqList(cri);
		Set<Integer> set = null;
		if (lst!=null && lst.size()>0) {
			set = ToolSet.reqSetInt(lst, TaTpyRelationship. ATT_I_ENTITY_ID_01);
		}
		return set;
	}

	//---------------------------------------------------------------------------------------------------------		
	private static Set<Integer> setStatP = new HashSet<Integer>() {
		{
			add(TaPrjProject.STAT_01_PRJ_NEW); 
			add(TaPrjProject.STAT_01_PRJ_TODO); 
			add(TaPrjProject.STAT_01_PRJ_INPROGRESS); 
			add(TaPrjProject.STAT_01_PRJ_REVIEW);
			add(TaPrjProject.STAT_01_PRJ_TEST);
		}
	};
	private static void doLstSearch(TaAutUser user,  JSONObject json, HttpServletResponse response)	throws Exception {

		Integer				idPerMan	= user.reqPerManagerId(); 
		String				searchkey	= ToolData.reqStr	(json, "searchkey"	, "%");// Integer.parseInt(request.getParameter("typ01")); 	
		Integer				nbLineMax	= ToolData.reqInt	(json, "nbLine"		, 10 );

		Criterion cri	= null;
		cri 			= Restrictions.like(TaPrjProject.ATT_T_NAME							, "%"+searchkey+"%");
		cri				= Restrictions.and(cri, 
				Restrictions.eq(TaPrjProject.ATT_I_PER_MANAGER	, idPerMan),
				Restrictions.eq(TaPrjProject.ATT_I_TYPE_02		, TaPrjProject.TYP_02_PRJ_MAIN)
				//				Restrictions.in(TaPrjProject.ATT_I_STATUS_01	, setStatP)
				);

		List<TaPrjProject> 	lst		= TaPrjProject.DAO.reqList(0, nbLineMax, Order.asc(TaPrjProject.ATT_T_NAME), cri);

		API.doResponse(response, ToolJSON.reqJSonStringWithNull(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA		, lst));

	}
	//---------------------------------------------------------------------------------------------------------		
	private static void doLstDyn(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {	
		String[] search				= ToolData.reqStr	(json	, "search").split(" ");//keyword;
		Integer begin				= ToolData.reqInt	(json	, "begin", 0);//beginDisplay;
		Integer nb					= ToolData.reqInt	(json	, "nb"	, 10);//nbDisplay;
		List<String> searchKey		= Arrays.asList(search);	

		if (!canWorkWithObj(user, WORK_LST)){ //other param after objTyp...
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		int 				manID 	= user.reqPerManagerId();
		Criterion 			cri 	= reqRestrictionPrj(searchKey, manID);				
		List<TaPrjProject> 	list 	= reqPrjProjectListDyn1(begin, nb, cri);		

		if (list==null||list.size() == 0 ){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		Long	countAllEle1 			= ((long)reqNbPrjProjectListDyn());

		Integer iTotalRecords 			= (countAllEle1.intValue());				
		Integer iTotalDisplayRecords 	= reqNbPrjProjectListDyn(cri).intValue();

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,					
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, list
				));

	}

	private static Criterion reqRestrictionPrj(List<String> searchKey, Integer manID) throws Exception {		
		Criterion cri = null;

		for (String s : searchKey){
			s = "%" + s + "%";
			s = s.replace("%$", "");
			s = s.replace("$%", "");
			s = s.toLowerCase();
			if (cri==null)
				cri = 	Restrictions.or(
						Restrictions.ilike(TaPrjProject.ATT_T_CODE_01				, s), 
						Restrictions.ilike(TaPrjProject.ATT_T_NAME				, s), 
						Restrictions.ilike(TaPrjProject.ATT_T_INFO_01	, s), 
						Restrictions.ilike(TaPrjProject.ATT_T_INFO_02	, s));

			else
				cri = 	Restrictions.and(	cri, 
						Restrictions.or(
								Restrictions.ilike(TaPrjProject.ATT_T_CODE_01				, s), 
								Restrictions.ilike(TaPrjProject.ATT_T_NAME				, s), 
								Restrictions.ilike(TaPrjProject.ATT_T_INFO_01	, s), 
								Restrictions.ilike(TaPrjProject.ATT_T_INFO_02	, s))
						);
		}	

		if(manID != null) {
			cri = Restrictions.and(cri, Restrictions.eq(TaPrjProject.ATT_I_PER_MANAGER, manID));
		}
		return cri;
	}

	private static List<TaPrjProject> reqPrjProjectListDyn1(Integer begin, Integer number, Criterion cri) throws Exception {		
		List<TaPrjProject> list = new ArrayList<TaPrjProject>();		

		list					= TaPrjProject.DAO.reqList(begin, number, Order.asc (TaPrjProject.ATT_I_ID), cri);

		if(list.size() == 0)	return list;

		TaPrjProject.doBuildAvatarForList(list);
		TaPrjProject.doBuildMemberForList(list);
		TaPrjProject.doBuildGrpMemberForList(list);
		return list;
	}

	public static List<TaPrjProject> reqSubProjectForListPrj(List<TaPrjProject> list, int type) throws Exception {
		List<TaPrjProject> sublist	= new ArrayList<TaPrjProject>();
		for(TaPrjProject p : list) {
			Integer type02			= (Integer) p.req(TaPrjProject.ATT_I_TYPE_02);
			if(type02!=null && type02.equals(type))
				sublist.add(p);
		}

		Set<Integer> ids 			= ToolSet.reqSetInt(sublist, TaPrjProject.ATT_I_ID);

		sublist						= TaPrjProject.DAO.reqList_In(TaPrjProject.ATT_I_PARENT, ids);

		return sublist;
	}

	private static Number reqNbPrjProjectListDyn() throws Exception {						
		return TaPrjProject.DAO.reqCount();		
	}

	private static Number reqNbPrjProjectListDyn(Criterion cri) throws Exception {			
		return TaPrjProject.DAO.reqCount(cri);
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doNew(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		TaPrjProject 			ent		= reqNew		(user, json, response);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		doPrjReqMemberAllForSendMail(ent);
		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent
				));		

	}


	private static TaPrjProject reqNew(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		JSONObject				obj		= ToolData.reqJson		(json, "obj"	, new JSONObject());
		JSONArray				member 	= ToolData.reqJsonArr	(json, "member"	, new JSONArray());
		JSONArray				group 	= ToolData.reqJsonArr	(json, "group"	, new JSONArray());
		Integer					frView	= ToolData.reqInt		(json, "frView"	, 0); //0: view project, 1: kanban view

		if (!canWorkWithObj(user, WORK_NEW, obj)){ //other param after obj...
			return null;
		}

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaPrjProject.class);

		TaPrjProject  		ent	 	= new TaPrjProject(attr);
		Integer stat 				= ent.reqInt(TaPrjProject.ATT_I_STATUS_01);
		Integer grpID 				= ent.reqInt(TaPrjProject.ATT_I_GROUP);
		Integer parId 				= ent.reqInt(TaPrjProject.ATT_I_PARENT);

		if (stat  == null) ent.reqSet(TaPrjProject.ATT_I_STATUS_01	, TaPrjProject.STAT_01_PRJ_NEW);

		Integer typ00 	= ent.reqInt(TaPrjProject.ATT_I_TYPE_00);
		Integer typ01 	= ent.reqInt(TaPrjProject.ATT_I_TYPE_01);
		Integer typ02 	= ent.reqInt(TaPrjProject.ATT_I_TYPE_02);


		if (typ00 == null)  typ00 = TaPrjProject.TYP_00_PRJ_PROJECT;
		if (typ01 == null)  typ01 = TaPrjProject.TYP_01_PRJ_IT;
		if (typ02 == null)  typ02 = TaPrjProject.TYP_02_PRJ_ELE;

		//---check right---
		switch(typ00) {
		case TaPrjProject.TYP_00_PRJ_PROJECT	: 
			if (typ02==TaPrjProject.TYP_02_PRJ_MAIN) {
				if (!APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_NEW, APIAuth.R_PRJ_PROJECT_NEW)) return null;
			} else {
				if (parId==null) return null;
			}

			break;
		case TaPrjProject.TYP_00_PRJ_DATACENTER	: 
			if (typ02==TaPrjProject.TYP_02_PRJ_MAIN) {
				if (!APIAuth.canAuthorizeWithOneRight(user, APIAuth.R_ADMIN, APIAuth.R_AUT_ALL_NEW, APIAuth.R_PRJ_DATA_NEW)) return null;
			} else {
				if (parId==null) return null;
			}
			break;
		case TaPrjProject.TYP_00_PRJ_TEST		: break;
		case TaPrjProject.TYP_00_PRJ_SPRINT		: if (parId==null) return null; break;
		case TaPrjProject.TYP_00_PRJ_WORKFLOW	: if (parId==null) return null; break;
		}


		//---check member---
		TaPrjProject 	parent 	= TaPrjProject.reqPrjFromCache(user, parId);
		if (frView!=0 && parId!=null && parent==null){
			parent 	= TaPrjProject.DAO.reqEntityByID(parId);
			if (parent!=null)
				TaPrjProject.doPutPrjToCache(parId, parent.reqStr(TaPrjProject.ATT_T_CODE_01), parent);
		}

		switch (typ00) {
		case TaPrjProject.TYP_00_PRJ_PROJECT	:
		case TaPrjProject.TYP_00_PRJ_DATACENTER	:
			if (typ02.equals(TaPrjProject.TYP_02_PRJ_MAIN)){
				ent.reqSet(TaPrjProject.ATT_I_PARENT	, null	);
				ent.reqSet(TaPrjProject.ATT_I_GROUP		, -1	);//cannot be null
				
			} else	if (!typ02.equals(TaPrjProject.TYP_02_PRJ_MAIN) && parent ==null){
				//---check from cache and db if view not from project
				return null;

			} else {
				//---check member: all member can create task
				boolean userInPrj = canBeMemberOfPrj(parent, user);
				if(!userInPrj)	return null;

				ent.reqSet(TaPrjProject.ATT_I_GROUP		, parent.reqInt(TaPrjProject.ATT_I_GROUP));
				ent.reqSet(TaPrjProject.ATT_O_USER_ROLE	, parent.req(TaPrjProject.ATT_O_USER_ROLE));
			}
			break;
		case TaPrjProject.TYP_00_PRJ_TEST		:
		case TaPrjProject.TYP_00_PRJ_WORKFLOW	:
		case TaPrjProject.TYP_00_PRJ_SPRINT		:
			if (parent ==null){
				//---check from cache and db if view not from project
				return null;
			}
			boolean userInPrj = canBeMemberOfPrj(parent, user);
			if(!userInPrj)	return null;

			ent.reqSet(TaPrjProject.ATT_I_GROUP	, parent.reqInt(TaPrjProject.ATT_I_GROUP));
			ent.reqSet(TaPrjProject.ATT_O_USER_ROLE, parent.req(TaPrjProject.ATT_O_USER_ROLE));
			break;
		}



		String preCode = "PRJ-";
		switch(typ00) {
		case TaPrjProject.TYP_00_PRJ_PROJECT	: preCode = "PRJ-"; break;
		case TaPrjProject.TYP_00_PRJ_DATACENTER	: preCode = "DAT-"; break;
		case TaPrjProject.TYP_00_PRJ_TEST		: preCode = "TES-"; break;
		case TaPrjProject.TYP_00_PRJ_SPRINT		: preCode = "SPR-"; break;
		case TaPrjProject.TYP_00_PRJ_WORKFLOW	: preCode = "WFL-"; break;
		}
		String	code01 = preCode + ToolDate.reqString(new Date(), "yyMMddHH")+"-"+ String.format("%04d", (int)(Math.random()*10000)) ;

		ent.reqSet(TaPrjProject.ATT_I_TYPE_00		, typ00);
		ent.reqSet(TaPrjProject.ATT_I_TYPE_01		, typ01);
		ent.reqSet(TaPrjProject.ATT_I_TYPE_02		, typ02);

		ent.reqSet(TaPrjProject.ATT_T_CODE_01		, code01);
		ent.reqSet(TaPrjProject.ATT_D_DATE_01		, new Date());
		ent.reqSet(TaPrjProject.ATT_D_DATE_02		, new Date());
		ent.reqSet(TaPrjProject.ATT_I_AUT_USER_01	, user.reqRef());
		ent.reqSet(TaPrjProject.ATT_I_PER_MANAGER	, user.reqPerManagerId());
		ent.reqSet(TaPrjProject.ATT_F_VAL_05		, 0.0);

		
		//----set inf03, stat cfg 
		TaPrjProject.DAO.doPersist(ent);
		
		
		
		//---update I_group + i_Parent
		switch (typ00) {
		case TaPrjProject.TYP_00_PRJ_PROJECT:
		case TaPrjProject.TYP_00_PRJ_DATACENTER:
			if (typ02==TaPrjProject.TYP_02_PRJ_MAIN) {
				ent.reqSet(TaPrjProject.ATT_I_PARENT	, null);
				ent.reqSet(TaPrjProject.ATT_I_GROUP		, ent.reqId());
				TaPrjProject.DAO.doMerge(ent);
			} else {
				parent.doBuildEpicInfo	(true);//force to rebuild epicInfo of group
				parent.doBuildEpics		(true);
				parent.doBuildTasks		(true);
			}
			break;
		case TaPrjProject.TYP_00_PRJ_TEST		: break;
		case TaPrjProject.TYP_00_PRJ_WORKFLOW	: break;
		case TaPrjProject.TYP_00_PRJ_SPRINT		: break;
		}


		doPrjReqSaveFile	(user, obj		, ent);
		doPrjReqAddMember	(user, member	, ent);
		doPrjReqAddGroup	(user, group	, ent);

		//req to history
		reqSaveHistory(ent.reqId(), ent.reqStr(TaPrjProject.ATT_T_CODE_01), parId, user, TYP_ADD, TAB_PRJ, null, ent.reqInt(TaPrjProject.ATT_I_STATUS_01), ent.reqInt(TaPrjProject.ATT_I_LEVEL));

		//---push to cache-----------
//		TaPrjProject.doPutPrjToCache(ent.reqId(), ent.reqStr(TaPrjProject.ATT_T_CODE_01), ent);
		//---bug, donot build parent of prj
		
		return ent;
	}

	private static void doPrjReqAddMember(TaAutUser user, JSONArray member, TaPrjProject ent) throws Exception {
		if(member.size() == 0)	return;
		List<TaTpyRelationship> lstRE 	= new ArrayList<TaTpyRelationship>();
		Set<Integer> chkUser			= new HashSet<Integer>();
		for(int i = 0; i < member.size(); i++) {
			JSONObject 	mem = (JSONObject) member.get(i);

			Integer 	uId = ToolData.reqInt(mem, "id", null);
			if (chkUser.contains(uId)) continue;

			chkUser.add(uId);
			Integer 	lev = ToolData.reqInt(mem, "lev", null);
			Integer 	typ = ToolData.reqInt(mem, "typ", null);

			if(uId == null || lev == null)	continue;

			TaTpyRelationship re = new TaTpyRelationship(DefDBExt.ID_TA_PRJ_PROJECT, DefDBExt.ID_TA_AUT_USER, (Integer) ent.reqRef(), uId);
			re.reqSet(TaTpyRelationship.ATT_I_LEVEL		, lev);
			re.reqSet(TaTpyRelationship.ATT_I_TYPE		, typ);
			re.reqSet(TaTpyRelationship.ATT_I_STATUS	, TaTpyRelationship.STAT_ACTIVE );
			re.reqSet(TaTpyRelationship.ATT_D_DATE_01	, new Date());
			lstRE.add(re);
		}
		if(lstRE.size() > 0) {
			TaTpyRelationship.DAO.doPersist(lstRE);
		}
	}

	private static void doPrjReqAddGroup(TaAutUser user, JSONArray group, TaPrjProject ent) throws Exception {
		if(group.size() == 0)	return;
		Map<Integer, TaTpyRelationship> mapRE 		= new HashMap<Integer, TaTpyRelationship>();
		List<TaTpyRelationship>      	lstGrpRE 	= new ArrayList<TaTpyRelationship>();
		Set<Integer> 					chkGrp		= new HashSet<Integer>();
		for(int i = 0; i < group.size(); i++) {
			JSONObject 	grp   = (JSONObject) group.get(i);
			Integer 	grpId = ToolData.reqInt(grp, "entId02", null);
			if (chkGrp.contains(grpId)) continue;

			chkGrp.add(grpId);
			Integer 	lev = ToolData.reqInt(grp, "lev", null);
			Integer 	typ = ToolData.reqInt(grp, "typ", null);

			if(grpId == null)	continue;

			TaTpyRelationship re = new TaTpyRelationship(DefDBExt.ID_TA_PRJ_PROJECT, DefDBExt.ID_TA_NSO_GROUP, (Integer) ent.reqRef(), grpId);
			re.reqSet(TaTpyRelationship.ATT_I_TYPE		, typ);
			re.reqSet(TaTpyRelationship.ATT_I_STATUS	, TaTpyRelationship.STAT_ACTIVE );
			re.reqSet(TaTpyRelationship.ATT_I_LEVEL		, lev);
			re.reqSet(TaTpyRelationship.ATT_D_DATE_01	, new Date());
			lstGrpRE.add(re);
		}
		if(lstGrpRE.size() > 0) {
			TaTpyRelationship.DAO.doPersist(lstGrpRE);

			for(TaTpyRelationship re : lstGrpRE) {
				mapRE.put((Integer) re.req(TaTpyRelationship.ATT_I_ENTITY_ID_02), re);
			}
		}

		/*
		List<TaNsoGroupMember> lstMember 	= TaNsoGroupMember.DAO.reqList_In(TaNsoGroupMember.ATT_I_NSO_GROUP, chkGrp, Restrictions.eq(TaNsoGroupMember.ATT_I_STATUS, TaNsoGroupMember.STAT_ACTIVE));

		if(lstMember != null) {
			List<TaTpyRelationship>      lstRE = new ArrayList<TaTpyRelationship>();
			for(TaNsoGroupMember mem : lstMember) {
				Integer 	uId = (Integer) mem.req(TaNsoGroupMember.ATT_I_AUT_USER);
				Integer 	lev = (Integer) mem.req(TaNsoGroupMember.ATT_I_TYPE);
				Integer 	typ = null;
				Integer   grpId = (Integer) mem.req(TaNsoGroupMember.ATT_I_NSO_GROUP);

				if(mapRE.containsKey(grpId)) {
					TaTpyRelationship groupM = mapRE.get(grpId);
					typ = (Integer) groupM.req(TaTpyRelationship.ATT_I_TYPE);
				}

				TaTpyRelationship re = new TaTpyRelationship(DefDBExt.ID_TA_PRJ_PROJECT, DefDBExt.ID_TA_AUT_USER, (Integer) ent.reqRef(), uId);
				re.reqSet(TaTpyRelationship.ATT_I_LEVEL, lev);
				re.reqSet(TaTpyRelationship.ATT_I_TYPE, typ);
				re.reqSet(TaTpyRelationship.ATT_I_STATUS, grpId);
				re.reqSet(TaTpyRelationship.ATT_D_DATE_01, new Date());
				lstRE.add(re);
			}
			if(lstRE.size() > 0) {
				TaTpyRelationship.DAO.doPersist(lstRE);
			}
		}*/
	}

	private static void doPrjReqSaveFile(TaAutUser user, JSONObject obj, TaPrjProject ent) throws Exception {
		JSONArray files   			= (JSONArray) obj.get("files");
		if(files!=null) {
			while(files.contains(null))
				files.remove(null);
			List<TaTpyDocument> lstDoc = TaTpyDocument.reqListCheck(DefAPI.SV_MODE_MOD, user, DefDBExt.ID_TA_PRJ_PROJECT, ent.reqId(), files) ;
//			ent.reqSet(TaPrjProject.ATT_O_DOCUMENTS, lstDoc);
		}
		ent.doBuildDocuments(true);
	}

	private static void doPrjReqAddFile(TaAutUser user, JSONObject obj, TaPrjProject ent) throws Exception {
		JSONArray files   			= (JSONArray) obj.get("files");
		if(files!=null) {
			while(files.contains(null))
				files.remove(null);
			List<TaTpyDocument> lstDoc = TaTpyDocument.reqListCheck(DefAPI.SV_MODE_NEW, user, DefDBExt.ID_TA_PRJ_PROJECT, ent.reqId(), files) ;
//			ent.reqSet(TaPrjProject.ATT_O_DOCUMENTS, lstDoc);
		}
		ent.doBuildDocuments(true);
	}

	private static void doPrjReqMemberAllForSendMail(TaPrjProject ent) throws Exception {
		Integer   grpId = (Integer) ent.req(TaPrjProject.ATT_I_ID);

		Map<Integer, TaTpyRelationship> mapRe = new HashMap<Integer, TaTpyRelationship>();
		List<TaTpyRelationship>      lstRE = TaTpyRelationship.DAO.reqList(Restrictions.and(Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01, grpId),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01, DefDBExt.ID_TA_PRJ_PROJECT),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02, DefDBExt.ID_TA_AUT_USER)));
		for(TaTpyRelationship re: lstRE) {
			mapRe.put((Integer) re.req(TaTpyRelationship.ATT_I_ENTITY_ID_02), re);
		}


		List<TaTpyRelationship>      lstNewRe = new ArrayList(mapRe.values());

		if(lstNewRe != null) doSendEmailUserJoin(lstNewRe , ent);
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doSaveContent(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		TaPrjProject 			ent		= reqSaveContent		(user, json, response);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent
				));		
	}

	private static TaPrjProject reqSaveContent(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		JSONObject			obj		= ToolData.reqJson 		(json, "obj", new JSONObject());

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaPrjProject.class);
		Integer 			prjID 	= (Integer) attr.get(TaPrjProject.ATT_I_ID);
		String				prjCode = (String ) attr.get(TaPrjProject.ATT_T_CODE_01);


		TaPrjProject  		ent	 	= TaPrjProject.reqPrjFromCache(user, prjID, prjCode);
		if(ent == null)		ent 	= TaPrjProject.reqPrjFromCache(user, prjID); //--in this case, code will be changed
		if(ent == null)		return null;

		if (!canWorkWithObj(user, WORK_MOD, ent)){ //other param after obj...
			return null;
		}

		//----some information can not be modified----------------------------
		attr.remove(TaPrjProject.ATT_I_ID);
		attr.remove(TaPrjProject.ATT_T_CODE_01);
		attr.remove(TaPrjProject.ATT_D_DATE_01);
		attr.remove(TaPrjProject.ATT_D_DATE_01);
		attr.remove(TaPrjProject.ATT_I_PER_MANAGER);
		attr.remove(TaPrjProject.ATT_I_AUT_USER_01);

		attr.put(TaPrjProject.ATT_D_DATE_02		, new Date());
		attr.put(TaPrjProject.ATT_I_AUT_USER_02	, user.reqId());

		//---get old stat
		Integer typ00 	= (Integer) attr.get(TaPrjProject.ATT_I_TYPE_00);
		Integer typ01 	= (Integer) attr.get(TaPrjProject.ATT_I_TYPE_01);
		Integer statOld = (Integer) ent.req(TaPrjProject.ATT_I_STATUS_01);
		Integer levOld 	= (Integer) ent.req(TaPrjProject.ATT_I_LEVEL);
		Integer parOld	= (Integer) ent.req(TaPrjProject.ATT_I_PARENT);

		if(typ00 ==TaPrjProject.TYP_00_PRJ_SPRINT) {
			ent.reqSet(TaPrjProject.ATT_F_VAL_05    	, ServicePrjProjectEval.reqPercentByStatus(ent));
		}

		Map<String, Object[]> mapChange = TaPrjProject.DAO.reqMerge(user.reqId(), ent, attr);

		if(mapChange.containsKey(TaPrjProject.ATT_I_STATUS_01) || mapChange.containsKey(TaPrjProject.ATT_I_LEVEL)) {
			//req to history
			reqSaveHistory(ent.reqId(), null, null, user, TYP_MOD, TAB_CONTENT, statOld, (Integer)ent.req(TaPrjProject.ATT_I_STATUS_01), (Integer)ent.req(TaPrjProject.ATT_I_LEVEL));
			doCalcKPI(ent);
			doCalcSLA(ent);
		}

//		doPrjReqSaveFile(user, obj, ent); //--file in other flush
//		doPrjReqSaveAvatar(obj, ent, ServiceTpyDocument.TYPE_01_FILE_AVATAR);
		
		Integer stat =  (Integer) ent.req(TaPrjProject.ATT_I_STATUS_01);
		if (stat == TaPrjProject.STAT_01_PRJ_TODO || stat == TaPrjProject.STAT_01_PRJ_INPROGRESS ||  stat == TaPrjProject.STAT_01_PRJ_REVIEW || stat == TaPrjProject.STAT_01_PRJ_TEST ) {
			reqSaveNotificationComment(ent, user, TAB_CONTENT, TYP_MOD);
		}

		TaPrjProject.doPutPrjToCache(prjID, prjCode, ent);

		//----rebuild some data for old parent in cache
		if(parOld != null && ent.reqInt(TaPrjProject.ATT_I_TYPE_02) != TaPrjProject.TYP_02_PRJ_MAIN) {
			Integer parNew	= (Integer) ent.req(TaPrjProject.ATT_I_PARENT);
			if (!parOld.equals(parNew)) {
				TaPrjProject 	par 	= TaPrjProject.reqPrjFromCache(user, parOld); 
				if (par!=null) {
					Integer 		parTyp	= par.reqInt(TaPrjProject.ATT_I_TYPE_00);
					if (parTyp.equals(TaPrjProject.TYP_00_PRJ_PROJECT)) {
						par.doBuildTasks(true);
						par.doBuildEpics(true);
						par.doBuildEpicInfo(true);
					}
				}
			}
		}

		//-----------------------------------------
		return ent;
	}	

	//---------------------------------------------------------------------------------------------------------
	private static void doMainDel(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		TaPrjProject  		ent	 	= TaPrjProject.reqPrjFromCache(user, json);
		if (ent==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		if (!canWorkWithObj(user, WORK_DEL, ent)){ //other param after ent...
			API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			return;
		}

		int objId = ent.reqId();
		// if not owner or manager, not del
		int userIDCreate = (Integer)ent.req(TaPrjProject.ATT_I_AUT_USER_01);
		if(userIDCreate != user.reqId()) {
			List<TaTpyRelationship>      lstRE = TaTpyRelationship.DAO.reqList(Restrictions.and(
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, objId),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02	, user.reqId()),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER),
					Restrictions.eq(TaTpyRelationship.ATT_I_LEVEL			, TaPrjProject.LEV_ROLE_PRJ_MANAGER))); //ServiceTpyRelationship.LEV_RS_MANAGER)
			if(lstRE==null || lstRE.size() ==0 ) {
				API.doResponse(response, DefAPI.API_MSG_KO);
				return;
			}
		}



		//if remove epic, il faut remove all task dedans
		List<TaPrjProject> 	prjs 	= TaPrjProject.DAO.reqList(Restrictions.eq(TaPrjProject.ATT_I_GROUP, objId));
		Set<Integer> 		prjIDs 	= ToolSet.reqSetInt(prjs, TaPrjProject.ATT_I_ID);

		Session sess = null;
		try {
			sess = TaPrjProject.DAO.reqSessionCurrent();

			//Remove member and Customer, document, post, history
			doRemoveAll(sess, prjIDs);

			TaPrjProject.DAO.doRemove(sess, prjs);
			TaPrjProject.DAO.doSessionCommit(sess);

			API.doResponse(response, DefAPI.API_MSG_OK);
		}catch(Exception e) {
			if (sess!=null) TaPrjProject.DAO.doSessionRollback(sess);
			API.doResponse(response, DefAPI.API_MSG_ERR_SESS);
		}
	}

	private static void doPrjWorkflowDel(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//----------------------------------------------------------------------------------------------------------------------
		boolean		ok		= canPrjWorkflowDel(user, json, response); 				
		if (!ok){
			API.doResponse(response, DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response, DefAPI.API_MSG_OK);
		}	
	}

	private static boolean canPrjWorkflowDel(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		TaPrjProject  		ent	 	= TaPrjProject.reqPrjFromCache(user, json);
		if (ent==null) {
			return false;
		}

		if (!canWorkWithObj(user, WORK_DEL, ent)){ //other param after ent...
			return false;
		}

		int objId = ent.reqId();
		// if not owner or manager, not del
		int userIDCreate = (Integer)ent.req(TaPrjProject.ATT_I_AUT_USER_01);
		if(userIDCreate != user.reqId()) {
			List<TaTpyRelationship>      lstRE = TaTpyRelationship.DAO.reqList(Restrictions.and(
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, objId),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02	, user.reqId()),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER),
					Restrictions.eq(TaTpyRelationship.ATT_I_LEVEL			, TaPrjProject.LEV_ROLE_PRJ_MANAGER))); //ServiceTpyRelationship.LEV_RS_MANAGER)
			if(lstRE==null || lstRE.size() ==0 ) {
				return false;
			}
		}

		Session sess = null;
		try {
			sess = TaPrjProject.DAO.reqSessionCurrent();

			//Remove member, task, list
			doRemoveWorkflowAll(sess, objId);

			TaPrjProject.DAO.doRemove(sess, ent);
			TaPrjProject.DAO.doSessionCommit(sess);

			return true;
		}catch(Exception e) {
			if (sess!=null) TaPrjProject.DAO.doSessionRollback(sess);
			return false;
		}

	}

	private static void doRemoveWorkflowAll (Session sess, Integer objId)throws Exception {
		// member
		List<TaTpyRelationship> lstMem = TaTpyRelationship.DAO.reqList(Restrictions.and(
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, objId)
				));

		if(lstMem != null && lstMem.size() > 0) {
			TaTpyRelationship.DAO.doRemove(lstMem);
		}

		// task, epic, prj
		List<TaTpyRelationship> lstTask = TaTpyRelationship.DAO.reqList(Restrictions.and(
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_WORKFLOW),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, objId)
				));

		if(lstTask != null && lstTask.size() > 0) {
			TaTpyRelationship.DAO.doRemove(lstTask);
		}

	}

	private static void doPrjSprintDel(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//----------------------------------------------------------------------------------------------------------------------
		boolean		ok		= canPrjSprintDel(user, json, response); 				
		if (!ok){
			API.doResponse(response, DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response, DefAPI.API_MSG_OK);
		}	
	}

	private static boolean canPrjSprintDel(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		TaPrjProject  		ent	 	= TaPrjProject.reqPrjFromCache(user, json);
		if (ent==null) {
			return false;
		}

		if (!canWorkWithObj(user, WORK_DEL, ent)){ //other param after ent...
			return false;
		}

		int objId = ent.reqId();
		// if not owner or manager, not del
		int userIDCreate = (Integer)ent.req(TaPrjProject.ATT_I_AUT_USER_01);
		if(userIDCreate != user.reqId()) {
			List<TaTpyRelationship>      lstRE = TaTpyRelationship.DAO.reqList(Restrictions.and(
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, objId),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02	, user.reqId()),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER),
					Restrictions.eq(TaTpyRelationship.ATT_I_LEVEL			, TaPrjProject.LEV_ROLE_PRJ_MANAGER))); //ServiceTpyRelationship.LEV_RS_MANAGER)
			if(lstRE==null || lstRE.size() ==0 ) {
				return false;
			}
		}

		//only stat new, closed
		Integer stat 	= (Integer)ent.req(TaPrjProject.ATT_I_STATUS_01);
		if(stat != TaPrjProject.STAT_01_PRJ_NEW && stat != TaPrjProject.STAT_01_PRJ_CLOSED && stat != TaPrjProject.STAT_01_PRJ_UNRESOLVED)	return false;

		//only type prj epic, task
		//		Integer typ02 	= (Integer)ent.req(TaPrjProject.ATT_I_TYPE_02);
		//		if(typ02 == TaPrjProject.TYP02_PRJ_MAIN)	return false;

		//if remove epic, il faut remove all task dedans // il faut corriger, dans le cas epic qui contiens epic child and task
		List<TaPrjProject> prjs = reqPrjProjectChildById(ent, true);

		Set<Integer> prjIDs = new HashSet<Integer>();
		if(prjs != null && prjs.size() > 0) {
			prjIDs = ToolSet.reqSetInt(prjs, TaPrjProject.ATT_I_ID);
			prjs.add(ent);
		}

		prjIDs.add(objId);

		Session sess = null;
		try {
			sess = TaPrjProject.DAO.reqSessionCurrent();

			//Remove member and Customer, document, post, history
			doRemoveAll(sess, prjIDs);

			TaPrjProject.DAO.doRemove(sess, prjs);

			if (prjs == null) TaPrjProject.DAO.doRemove(sess, ent);

			TaPrjProject.DAO.doSessionCommit(sess);

			return true;
		}catch(Exception e) {
			if (sess!=null) TaPrjProject.DAO.doSessionRollback(sess);
			return false;
		}

	}

	private static void doDel(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//----------------------------------------------------------------------------------------------------------------------
		boolean		ok		= canPrjProjectDel(user, json, response); 				
		if (!ok){
			API.doResponse(response, DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response, DefAPI.API_MSG_OK);
		}	
	}

	private static boolean canPrjProjectDel(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {

		TaPrjProject  		ent	 	= TaPrjProject.reqPrjFromCache(user, json);

		if(ent == null) {
			Integer 			objId	= ToolData.reqInt	(json, "id"		, -1	);	
			String	 			objCode	= ToolData.reqStr	(json, "code"	, null	);
			Boolean				forced	= ToolData.reqBool	(json, "forced"	, false	);
			if (objCode==null||objId<0) {
				return false;
			}

			ent 	= reqGet(user, forced, objId, objCode );
			if(ent == null) return false;
		}

		if (!canWorkWithObj(user, WORK_DEL, ent)){ //other param after ent...
			return false;
		}

		int objId = ent.reqId();


		// if not owner or manager, not del
		int userIDCreate = ent.reqInt(TaPrjProject.ATT_I_AUT_USER_01);
		if(userIDCreate != user.reqId()) {
			List<TaTpyRelationship>      lstRE = TaTpyRelationship.DAO.reqList(Restrictions.and(
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, objId),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02	, user.reqId()),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER),
					Restrictions.eq(TaTpyRelationship.ATT_I_LEVEL			, TaPrjProject.LEV_ROLE_PRJ_MANAGER))); //ServiceTpyRelationship.LEV_RS_MANAGER)
			if(lstRE==null || lstRE.size() ==0 ) {
				return false;
			}
		}


		//only stat new
		//		Integer stat 	= (Integer)ent.req(TaPrjProject.ATT_I_STATUS);
		//		if(stat != TaPrjProject.STAT_01_PRJ_NEW)	return false;

		//only type prj epic, task
		//		Integer typ02 	= (Integer)ent.req(TaPrjProject.ATT_I_TYPE_02);
		//		if(typ02 == TaPrjProject.TYP02_PRJ_MAIN)	return false;

		//if remove epic, il faut remove all task dedans // il faut corriger, dans le cas epic qui contiens epic child and task
		List<TaPrjProject> prjs = reqPrjProjectChildById(ent, true);

		Set<Integer> prjIDs = new HashSet<Integer>();
		if(prjs != null && prjs.size() > 0) {
			prjIDs = ToolSet.reqSetInt(prjs, TaPrjProject.ATT_I_ID);
		}

		prjs.add(ent);
		prjIDs.add(objId);

		Session sess = null;
		try {
			sess = TaPrjProject.DAO.reqSessionCurrent();

			//Remove member and Customer, document, post, history
			doRemoveAll(sess, prjIDs);

			TaPrjProject.DAO.doRemove(sess, prjs);
			TaPrjProject.DAO.doSessionCommit(sess);

			// Save history
			/*if (prjs != null && prjs.size() > 0) {
				for (TaPrjProject e : prjs) {
					if ((int)e.req(TaPrjProject.ATT_I_TYPE_02) == TaPrjProject.TYP02_PRJ_ELE) {
						reqSaveHistory(e.reqId(), (String) e.req(TaPrjProject.ATT_T_CODE_01), (Integer)e.req(TaPrjProject.ATT_I_PARENT), user, TYP_DEL, TAB_TASK, null, (Integer)e.req(TaPrjProject.ATT_I_STATUS_01));
					} else if ((int)e.req(TaPrjProject.ATT_I_TYPE_02) == TaPrjProject.TYP02_PRJ_SUB) {
						reqSaveHistory(e.reqId(), (String) e.req(TaPrjProject.ATT_T_CODE_01), (Integer)e.req(TaPrjProject.ATT_I_PARENT), user, TYP_DEL, TAB_EPIC, null, (Integer)e.req(TaPrjProject.ATT_I_STATUS_01));
					}
				}
			}*/
			if(ent.reqInt(TaPrjProject.ATT_I_TYPE_02) != TaPrjProject.TYP_02_PRJ_MAIN) {
				Integer 			prjId	= ToolData.reqInt	(json, "prjId"		, null	);	
				String	 			prjCode	= ToolData.reqStr	(json, "prjCode"	, null	);
				Integer				typ02	= ent.reqInt(TaPrjProject.ATT_I_TYPE_02);
				TaPrjProject 		oldPrjMain = TaPrjProject.cache_Ent.reqData(prjId + ":" + prjCode);

				if(oldPrjMain != null) {
					if(typ02 == TaPrjProject.TYP_02_PRJ_SUB) {
						oldPrjMain.doBuildEpics(true);
					} else if(typ02 == TaPrjProject.TYP_02_PRJ_ELE) {
						oldPrjMain.doBuildTasks(true);
					}
					TaPrjProject.doPutPrjToCache(prjId, prjCode, oldPrjMain);
				}
			}

			return true;
		}catch(Exception e) {
			if (sess!=null) TaPrjProject.DAO.doSessionRollback(sess);
			return false;
		}

	}

	private static void doRemoveAll (Session sess, Set<Integer> prjIDs)throws Exception {
		doRemoveRelationShipPrj	(sess, prjIDs);
		doRemoveDocumentPrj		(sess, prjIDs);
		doRemovePostPrj			(sess, prjIDs);
		doRemoveHistoryPrj		(sess, prjIDs);
		doRemoveFavoritePrj		(sess, prjIDs);
	}

	private static void doRemoveRelationShipPrj(Session sess, Set<Integer> prjIDs) throws Exception {
		List<TaTpyRelationship> lstMem = TaTpyRelationship.DAO.reqList(sess,
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT),
				Restrictions.in(TaTpyRelationship.ATT_I_ENTITY_ID_01	, prjIDs)
				);

		if(lstMem != null && lstMem.size() > 0) {
			TaTpyRelationship.DAO.doRemove(sess, lstMem);
		}
	}

	private static void doRemoveDocumentPrj(Session sess,Set<Integer> prjIDs) throws Exception {
		List<TaTpyDocument> 	lstDoc = TaTpyDocument.DAO.reqList(sess,
				Restrictions.eq(TaTpyDocument.ATT_I_ENTITY_TYPE			, DefDBExt.ID_TA_PRJ_PROJECT),
				Restrictions.in(TaTpyDocument.ATT_I_ENTITY_ID			, prjIDs)
				);

		if(lstDoc != null && lstDoc.size() > 0) {
			TaTpyDocument.DAO.doRemove(sess,  lstDoc);
		}
	}

	private static void doRemovePostPrj(Session sess, Set<Integer> prjIDs) throws Exception {
		List<TaNsoPost> 		lstPost = TaNsoPost.DAO.reqList(sess,
				Restrictions.eq(TaNsoPost.ATT_I_VAL_01				, DefDBExt.ID_TA_PRJ_PROJECT),
				Restrictions.in(TaNsoPost.ATT_I_VAL_02				, prjIDs)
				);

		if(lstPost != null && lstPost.size() > 0) {
			TaNsoPost.DAO.doRemove(sess, lstPost);
		}
	}

	private static void doRemoveHistoryPrj(Session sess, Set<Integer> prjIDs) throws Exception {
		List<TaTpyInformation> 		lstHis = TaTpyInformation.DAO.reqList(sess,
				Restrictions.eq(TaTpyInformation.ATT_I_ENTITY_TYPE				, DefDBExt.ID_TA_PRJ_PROJECT),
				Restrictions.in(TaTpyInformation.ATT_I_ENTITY_ID				, prjIDs)
				);

		if(lstHis != null && lstHis.size() > 0) {
			TaTpyInformation.DAO.doRemove(sess, lstHis);
		}
	}

	private static void doRemoveFavoritePrj(Session sess, Set<Integer> prjIDs) throws Exception {
		List<TaTpyFavorite> 		lst = TaTpyFavorite.DAO.reqList(sess,
				Restrictions.eq(TaTpyFavorite.ATT_I_ENTITY_TYPE				, DefDBExt.ID_TA_PRJ_PROJECT),
				Restrictions.in(TaTpyFavorite.ATT_I_ENTITY_ID				, prjIDs)
				);

		if(lst != null && lst.size() > 0) {
			TaTpyFavorite.DAO.doRemove(sess, lst);
		}
	}

	//---------------------------------------------------------------------------------------------------------
	private static void reqSaveNotificationComment(TaPrjProject prj, TaAutUser user, String table_notif, int typ_notif) throws Exception {
		Criterion cri = Restrictions.and(
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, prj.reqId()),
				Restrictions.ne(TaTpyRelationship.ATT_I_ENTITY_ID_02	, user.reqId())
				);
		List<TaTpyRelationship> lstMem = TaTpyRelationship.DAO.reqList(cri);

		Set<Integer> setMem = new HashSet<Integer>();
		if(lstMem != null && lstMem.size() > 0) {
			setMem = ToolSet.reqSetInt(lstMem, TaTpyRelationship.ATT_I_ENTITY_ID_02);
			if (setMem!=null && setMem.size()>0) {
				setMem.remove(user.reqId()); //---no need noti for actual user

				//---build content of noti
				JSONArray arr 		= new JSONArray();
				for(Integer mem: setMem) {
					JSONObject notify 		= new JSONObject();
					notify.put("typ"		, typ_notif);
					notify.put("typTab"		, table_notif);
					notify.put("title"		, prj.req(TaPrjProject.ATT_T_CODE_01));
					notify.put("uID"		, mem);
					notify.put("uAction"	, user.reqRef());
					notify.put("parTyp"		, DefDBExt.ID_TA_PRJ_PROJECT);
					notify.put("parID"		, prj.reqId());
					arr.add(notify);
				}

				//---insert into db
				reqNewNoti (prj.reqId(), arr);
			}
		}
	}


	private static void reqNewNoti(int prjId, JSONArray arrNotify) throws Exception {
		List<TaMsgMessage> lst = new ArrayList<TaMsgMessage>();

		for(int i = 0; i < arrNotify.size(); i++) {
			JSONObject notifyContent = (JSONObject) arrNotify.get(i);
			TaMsgMessage notif = new TaMsgMessage();
			notif.reqSet(TaMsgMessage.ATT_I_TYPE_01			, TaMsgMessage.TYPE_01_NOTIFICATION);
			notif.reqSet(TaMsgMessage.ATT_I_STATUS			, TaMsgMessage.STAT_NOTI_NEW);
			notif.reqSet(TaMsgMessage.ATT_D_DATE_01			, new Date());
			notif.reqSet(TaMsgMessage.ATT_I_AUT_USER		, (Integer)notifyContent.get("uID"));
			notif.reqSet(TaMsgMessage.ATT_T_INFO_01			, notifyContent.toString());
			notif.reqSet(TaMsgMessage.ATT_I_ENTITY_TYPE		, DefDBExt.ID_TA_PRJ_PROJECT);
			notif.reqSet(TaMsgMessage.ATT_I_ENTITY_ID		, prjId);
			lst.add(notif);
		}

		TaMsgMessage.DAO.doPersist(lst);
	}


	public static void reqSaveNotificationTaskLate(int prjId, List<TaTpyRelationship> lstMem, Hashtable tabTask) throws Exception {
		JSONArray arr 		= new JSONArray();

		if(lstMem != null && lstMem.size() > 0) {
			for(TaTpyRelationship mem: lstMem) {
				TaPrjProject 	prj 		= (TaPrjProject) tabTask.get(mem.req(TaTpyRelationship.ATT_I_ENTITY_ID_01));

				JSONObject notify 		= new JSONObject();
				notify.put("typ"		, TYP_LATE);
				notify.put("title"		, prj.req(TaPrjProject.ATT_T_CODE_01));
				notify.put("uID"		, mem.req(TaTpyRelationship.ATT_I_ENTITY_ID_02));
				notify.put("parTyp"		, DefDBExt.ID_TA_PRJ_PROJECT);
				notify.put("parID"		, prj.reqId());
				arr.add(notify);
			}
		}

		Set<Integer> setIdsPrj = (Set<Integer>)tabTask.keySet();
		for(Integer key: setIdsPrj){
			TaPrjProject 	prj 		= (TaPrjProject) tabTask.get(key);
			JSONObject notify 		= new JSONObject();
			notify.put("typ"		, TYP_LATE);
			notify.put("title"		, prj.req(TaPrjProject.ATT_T_CODE_01));
			notify.put("uID"		, prj.req(TaPrjProject.ATT_I_AUT_USER_01));
			notify.put("parTyp"		, DefDBExt.ID_TA_PRJ_PROJECT);
			notify.put("parID"		, prj.reqId());
			arr.add(notify);
		}

		reqNewNoti(prjId, arr);
	}

	private static void doTaskMove(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		Integer objId 			= ToolData.reqInt	(json	, "id"			, null);
		Integer statTo 			= ToolData.reqInt	(json	, "statTo"		, null);

		if(objId == null || statTo == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		TaPrjProject 		ent 	= TaPrjProject.DAO.reqEntityByRef(objId);

		if(ent == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		int statFrom = (Integer)ent.req(TaPrjProject.ATT_I_STATUS_01);
		if(statTo == statFrom) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		Double val05 = ServicePrjProjectEval.reqPercentByStatus(statTo);
		ent.reqSet(TaPrjProject.ATT_I_STATUS_01	, statTo);
		ent.reqSet(TaPrjProject.ATT_F_VAL_05	, val05);
		ent.reqSet(TaPrjProject.ATT_D_DATE_02	, new Date());
		TaPrjProject.DAO.doMerge(ent);

		//req to history
		reqSaveHistory(ent.reqId(), null, null, user, TYP_MOVE, TAB_CONTENT, statFrom, statTo, (Integer)ent.req(TaPrjProject.ATT_I_LEVEL));

		Integer stat =  (Integer) ent.req(TaPrjProject.ATT_I_STATUS_01);
		if (stat == TaPrjProject.STAT_01_PRJ_TODO || stat == TaPrjProject.STAT_01_PRJ_INPROGRESS ||  stat == TaPrjProject.STAT_01_PRJ_REVIEW || stat == TaPrjProject.STAT_01_PRJ_TEST ) {
			reqSaveNotificationComment(ent, user, TAB_CONTENT, TYP_MOD);
		}

		// Handle KPI
		doCalcKPI(ent);
		doCalcSLA(ent);
		////////////////////////////////////////

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA		, ent));
	}

	private static void doCalcKPI (TaPrjProject ent) throws Exception {
		Integer stat  =  (Integer) ent.req(TaPrjProject.ATT_I_STATUS_01);
		Integer entId =  (Integer) ent.req(TaPrjProject.ATT_I_ID);
		Double val04 = 0.0;
		if (stat == TaPrjProject.STAT_01_PRJ_CLOSED) {
			Double KPI = 1.0;
			Double ratio = 0.9;
			boolean isCloseBeforeDealine 	= false;
			boolean isDoneBeforeDealine 	= false;
			boolean isRedoTask 				= false;

			TaTpyInformation hist = TaTpyInformation.DAO.reqEntityByValues(
					TaTpyInformation.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_PRJ_PROJECT, 
					TaTpyInformation.ATT_I_ENTITY_ID	, entId,
					TaTpyInformation.ATT_I_TYPE_01		, TaTpyInformation.TYP_01_HISTO
					);
			String 			val 		= hist.reqStr(hist, TaTpyInformation.ATT_T_INFO_01);
			JSONArray		lstHistory 	= ToolJSON.reqJSonArrayFromString(val);

			for (int i = 0; i < lstHistory.size(); i++) {
				JSONObject cont = (JSONObject)lstHistory.get(i);
				if (cont.get("statTo") != null) {
					Integer statTo = ((Long) cont.get("statTo")).intValue();
					Date dt = ToolDate.reqDate((String) cont.get("dt"));
					if (statTo == TaPrjProject.STAT_01_PRJ_CLOSED) {						
						if (dt.before((Date)ent.req(TaPrjProject.ATT_D_DATE_04))) {
							isCloseBeforeDealine = true;
						}
					}

					if (statTo == TaPrjProject.STAT_01_PRJ_DONE) {
						if (dt.before((Date)ent.req(TaPrjProject.ATT_D_DATE_04))) {
							isDoneBeforeDealine = true;
						}
					}

					if (statTo == TaPrjProject.STAT_01_PRJ_UNRESOLVED) {
						isRedoTask = true;
					}
				}
			}

			//Check task close before deadline (KPI = Ratio * 100%, with Ratio >= 1)
			if (isCloseBeforeDealine) {
				// Todo with ratio
				ratio = 1.1;
			} else {
				// Check task close after deadline ---- 2 cases:
				if (!isRedoTask) {
					// + Case 1: Check history has INPROGRESS, DONE => KPI = Ratio * 100. (Ratio <1 )  , náº¿u hoÃ n thÃ nh trÆ°á»›c thÃ¬ ratio >=1
					if (isDoneBeforeDealine) {
						// Todo with ratio
						ratio = 1.2;
					} else {
						// Todo with ratio
						ratio = 0.9;
					}
				} else {
					// + Case 2: Check history has INPROGRESS, DONE, UNRESOLVED = > .....
					// find the last time finish task
					for (int i = lstHistory.size(); i > 0; i--) {
						JSONObject cont = (JSONObject)lstHistory.get(i);
						Date dt = ToolDate.reqDate((String) cont.get("dt"));
						if (cont.get("statTo") != null && ((Long) cont.get("statTo")).intValue() == TaPrjProject.STAT_01_PRJ_DONE) {
							if (dt.before((Date)ent.req(TaPrjProject.ATT_D_DATE_04))) {
								// => Náº¿u thá»�i gian hoÃ n thÃ nh cuá»‘i cÃ¹ng trÆ°á»›c deadline thÃ¬ thá»�i gian nÃ y Ä‘Æ°á»£c dÃ¹ng Ä‘á»ƒ tÃ­nh ratio
								ratio = 1.0; // todo
								break;
							} else {
								// => Náº¿u thá»�i gian hoÃ n thÃ nh cuá»‘i cÃ¹ng > deadline thÃ¬ 
								//    thá»�i gian nÃ y Ä‘Æ°á»£c trá»« bá»›t khoáº£ng thá»�i gian Ä‘á»£i tá»« hoÃ n thÃ nh cuá»‘i cÃ¹ng trÆ°á»›c deadline Ä‘áº¿n sáº¯p hoÃ n thÃ nh trÆ°á»›c khi dÃ¹ng Ä‘á»ƒ tÃ­nh ratio
								ratio = 0.8; // todo
								break;
							}
						}
					}
				}
			}
			// Calc KPI
			KPI = ratio * 100;
			val04 = KPI;
		}
		ent.reqSet(TaPrjProject.ATT_F_VAL_04, val04);
		TaPrjProject.DAO.doMerge(ent);
	}

	private static void doCountNbPrj(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		Integer uId 	= ToolData.reqInt	(json, "user_id"	, user.reqId());
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		Double valBring			= 0.0;
		int nbTask				= 0;
		int nbDone				= 0;
		Double percentFinish 	= 0.0;

		TaAutUser userInfo	= ServiceAutUser.reqGet(uId, false);
		userInfo.doHidePwd();

		Set<Integer> idPrjs = new HashSet<Integer>();
		List<TaTpyRelationship> lstRE = TaTpyRelationship.DAO.reqList(
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02	, uId)
				);
		if(lstRE != null && lstRE.size() > 0)			idPrjs.addAll(ToolSet.reqSetInt(lstRE, TaTpyRelationship.ATT_I_ENTITY_ID_01));

		List<TaPrjProject> lstCreate = TaPrjProject.DAO.reqList(Restrictions.eq(TaPrjProject.ATT_I_AUT_USER_01, uId));
		if(lstCreate != null && lstCreate.size() > 0)	idPrjs.addAll(ToolSet.reqSetInt(lstCreate, TaPrjProject.ATT_I_ID));

		List<TaPrjProject> lstTask = TaPrjProject.DAO.reqList_In(TaPrjProject.ATT_I_ID, idPrjs, Restrictions.eq(TaPrjProject.ATT_I_TYPE_02, TaPrjProject.TYP_02_PRJ_ELE));

		if(lstTask != null && lstTask.size() > 0) {
			//Map status
			Map<Integer, List<TaPrjProject>> mapStat = new HashMap<Integer, List<TaPrjProject>>();

			for(TaPrjProject p : lstTask) {
				Integer stat = (Integer) p.req(TaPrjProject.ATT_I_STATUS_01);
				if(mapStat.containsKey(stat)) {
					mapStat.get(stat).add(p);
				}else {
					List<TaPrjProject> lst = new ArrayList<TaPrjProject>();
					lst.add(p);
					mapStat.put(stat, lst);
				}
			}

			//calculat
			List<TaPrjProject> lstNew 			= mapStat.get(TaPrjProject.STAT_01_PRJ_NEW);
			List<TaPrjProject> lstTodo 			= mapStat.get(TaPrjProject.STAT_01_PRJ_TODO);
			List<TaPrjProject> lstInProgress 	= mapStat.get(TaPrjProject.STAT_01_PRJ_INPROGRESS);
			List<TaPrjProject> lstReview 		= mapStat.get(TaPrjProject.STAT_01_PRJ_REVIEW);
			List<TaPrjProject> lstDone 			= mapStat.get(TaPrjProject.STAT_01_PRJ_DONE);
			List<TaPrjProject> lstClose 		= mapStat.get(TaPrjProject.STAT_01_PRJ_CLOSED);
			List<TaPrjProject> lstFail 			= mapStat.get(TaPrjProject.STAT_01_PRJ_FAIL);
			List<TaPrjProject> lstUnresolved 	= mapStat.get(TaPrjProject.STAT_01_PRJ_UNRESOLVED);

			nbTask			= lstTask.size();
			nbDone			= (lstDone != null ? lstDone.size() : 0) + (lstClose != null ? lstClose.size() : 0);

			double	ratio	= (nbTask - (lstNew != null ? lstNew.size() : 0));
			if (ratio==0) 
				percentFinish = 0.0;
			else
				percentFinish 	= (double) (((lstDone != null ? lstDone.size() : 0) + (lstClose != null ? lstClose.size() : 0)) * 100 /ratio);

			if(lstClose != null) {
				for(TaPrjProject p: lstClose) {
					Double val02 = p.reqDouble(TaPrjProject.ATT_F_VAL_02);
					valBring 	+= (val02 == null ? 0 : val02);
				}
			}
		}

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES, 
				"valBring"					, valBring,
				"nbTask"					, nbTask,
				"nbDone"					, nbDone,
				"percentFinish"				, percentFinish,
				"userInfo"					, userInfo));
	}

	private static Set<Integer> reqPrjOfUserPaticiped (Integer userId, Set<Integer> ids) throws Exception{
		Set<Integer>	idPrjs 	= new HashSet<Integer>();
		Criterion 		cri 	=  Restrictions.and(
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02	, userId)
				);
		if (ids!=null) {
			cri = Restrictions.and(Restrictions.in(TaTpyRelationship.ATT_I_ENTITY_ID_01, ids), cri);
		}
		List<TaTpyRelationship> lstRE = TaTpyRelationship.DAO.reqList(cri);
		if(lstRE != null && lstRE.size() > 0)			idPrjs.addAll(ToolSet.reqSetInt(lstRE, TaTpyRelationship.ATT_I_ENTITY_ID_01));

		List<TaPrjProject> lstCreate = TaPrjProject.DAO.reqList(Restrictions.eq(TaPrjProject.ATT_I_AUT_USER_01, userId));
		if(lstCreate != null && lstCreate.size() > 0)	idPrjs.addAll(ToolSet.reqSetInt(lstCreate, TaPrjProject.ATT_I_ID));

		return idPrjs;
	}

	private static void doPrjGlobalList(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		List<TaPrjProject> lstPrj = reqPrjProjectPrjGlobalList(user, json, response);
		if(lstPrj == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, lstPrj));
	}

	private static List<TaPrjProject> reqPrjProjectPrjGlobalList(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		Set<Integer> 		idPrjs = reqPrjOfUserPaticiped(user.reqId(), null);
		List<TaPrjProject> 	lstPrj = TaPrjProject.DAO.reqList_In(TaPrjProject.ATT_I_ID, idPrjs, 
				Restrictions.eq(TaPrjProject.ATT_I_TYPE_00, TaPrjProject.TYP_00_PRJ_PROJECT),
				Restrictions.eq(TaPrjProject.ATT_I_TYPE_02, TaPrjProject.TYP_02_PRJ_MAIN));

		return lstPrj;
	}

	private static void doPrjGlobal(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		List<TaPrjProject> lstTask = reqPrjProjectPrjGlobal(user, json, response);
		if(lstTask == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, lstTask));
	}

	private static List<TaPrjProject> reqPrjProjectPrjGlobal(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		Integer uId 	= ToolData.reqInt	(json, "user_id"	, user.reqId());
		Date dtBegin 	= ToolData.reqDate	(json, "dtBegin"	, ToolDate.reqDateByAdding(new Date(), 0, 0, -30, 0, 0, 0));
		Date dtEnd 		= ToolData.reqDate	(json, "dtEnd"		, new Date());

		JSONArray arrStat 		= ToolData.reqJsonArr	(json, "stats", null);
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");

		Set<Integer> idPrjs = reqPrjOfUserPaticiped(uId, null);

		Criterion cri = Restrictions.and(
				Restrictions.eq(TaPrjProject.ATT_I_TYPE_02, TaPrjProject.TYP_02_PRJ_ELE),
				Restrictions.ge(TaPrjProject.ATT_D_DATE_04, dtBegin),
				Restrictions.le(TaPrjProject.ATT_D_DATE_04, dtEnd)
				);

		if(arrStat != null) {
			Set<Integer> setStat = new HashSet<Integer>();
			for(int i = 0; i < arrStat.size() ; i++) {
				setStat.add((int)(long) arrStat.get(i));
			}
			cri = Restrictions.and(cri, Restrictions.in(TaPrjProject.ATT_I_STATUS_01, setStat));
		}
		List<TaPrjProject> lstTask = TaPrjProject.DAO.reqList_In(TaPrjProject.ATT_I_ID, idPrjs, cri);

		if(lstTask != null && lstTask.size() > 0) {
			Set<Integer> 				ids 		= ToolSet.reqSetInt(lstTask, TaPrjProject.ATT_I_PARENT);
			List<TaPrjProject> 			lstParent 	= TaPrjProject.DAO.reqList_In(TaPrjProject.ATT_I_ID, ids);
			Map<Integer, TaPrjProject> 	mapPrj 		= new HashMap<Integer, TaPrjProject>();
			for(TaPrjProject p : lstParent) {
				mapPrj.put(p.reqId(), p);
			}

			for(TaPrjProject p : lstTask) {
				p.reqSet(TaPrjProject.ATT_O_PARENT, mapPrj.get(p.req(TaPrjProject.ATT_I_PARENT)));
			}

			Collections.sort(lstTask, new Comparator<TaPrjProject>() {
				public int compare(TaPrjProject o1, TaPrjProject o2) {
					Date dEnd01 = (Date) o1.req(TaPrjProject.ATT_D_DATE_04);
					Date dEnd02 = (Date) o2.req(TaPrjProject.ATT_D_DATE_04);
					if (dEnd01 == null || dEnd02  == null)	return 0;
					return dEnd01.compareTo(dEnd02);
				}
			});
		}

		return lstTask;
	}

	//---------------------------------------------------------------------------------------------------------		

	private static void doPrjTaskListSearch(TaAutUser user,  JSONObject json, HttpServletResponse response)	throws Exception {

		Integer				idPerMan	= user.reqPerManagerId(); 

		String				searchkey	= ToolData.reqStr	(json, "searchkey"	, "%");// Integer.parseInt(request.getParameter("typ01")); 	
		Integer				nbLineMax	= ToolData.reqInt	(json, "nbLine"		, 10 );

		Integer          	groupId     = ToolData.reqInt	(json, "grId"		, null);



		Set<Integer> setStatP = new HashSet<Integer>();
		setStatP.add(TaPrjProject.STAT_01_PRJ_NEW); setStatP.add(TaPrjProject.STAT_01_PRJ_TODO); 
		setStatP.add(TaPrjProject.STAT_01_PRJ_INPROGRESS); setStatP.add(TaPrjProject.STAT_01_PRJ_REVIEW);

		Criterion cri	= null;
		cri 			= Restrictions.or (
				Restrictions.ilike(TaPrjProject.ATT_T_NAME								, "%"+searchkey+"%"),
				Restrictions.ilike(TaPrjProject.ATT_T_CODE_02							, "%"+searchkey+"%"),
				Restrictions.ilike(TaPrjProject.ATT_T_CODE_01							, "%"+searchkey+"%")
				);

		cri				= Restrictions.and(cri, 
				Restrictions.eq(TaPrjProject.ATT_I_PER_MANAGER	, idPerMan),
				Restrictions.eq(TaPrjProject.ATT_I_TYPE_02		, TaPrjProject.TYP_02_PRJ_ELE),
				Restrictions.in(TaPrjProject.ATT_I_STATUS_01		, setStatP)
				);

		if (groupId!=null) cri = Restrictions.and(cri, Restrictions.eq(TaPrjProject.ATT_I_GROUP	, groupId));

		List<TaPrjProject> 	lst		= TaPrjProject.DAO.reqList(0, nbLineMax, Order.asc(TaPrjProject.ATT_T_NAME), cri);

		API.doResponse(response, ToolJSON.reqJSonStringWithNull(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA		, lst));

	}

	private static void doPrjTestUnitListSearch(TaAutUser user,  JSONObject json, HttpServletResponse response)	throws Exception {
		Integer				idPerMan	= user.reqPerManagerId(); 

		String				searchkey	= ToolData.reqStr	(json, "searchkey"	, "%");// Integer.parseInt(request.getParameter("typ01")); 	
		Integer				nbLineMax	= ToolData.reqInt	(json, "nbLine"		, 10 );

		Integer          	groupId     = ToolData.reqInt	(json, "grId"		, null);

		JSONObject          obj     	= ToolData.reqJson	(json, "obj"			, new JSONObject());
		if(groupId == null) {
			groupId							= ((Long) obj.get("grId")).intValue();
		}


		Set<Integer> setStatP = new HashSet<Integer>();
		setStatP.add(TaPrjProject.STAT_01_PRJ_NEW); setStatP.add(TaPrjProject.STAT_01_PRJ_TODO); 
		setStatP.add(TaPrjProject.STAT_01_PRJ_INPROGRESS); setStatP.add(TaPrjProject.STAT_01_PRJ_REVIEW);

		Criterion cri	= null;
		cri 			= Restrictions.or (
				Restrictions.ilike(TaPrjProject.ATT_T_NAME								, "%"+searchkey+"%"),
				Restrictions.ilike(TaPrjProject.ATT_T_CODE_02							, "%"+searchkey+"%"),
				Restrictions.ilike(TaPrjProject.ATT_T_CODE_01							, "%"+searchkey+"%")
				);

		cri				= Restrictions.and(cri, 
				Restrictions.eq(TaPrjProject.ATT_I_PER_MANAGER	, idPerMan),
				Restrictions.eq(TaPrjProject.ATT_I_TYPE_00		, TaPrjProject.TYP_00_PRJ_TEST),
				Restrictions.eq(TaPrjProject.ATT_I_TYPE_01		, TaPrjProject.TYP_01_TEST_UNIT)
				);

		if (groupId!=null) cri = Restrictions.and(cri, Restrictions.eq(TaPrjProject.ATT_I_GROUP	, groupId));

		List<TaPrjProject> 	lst		= TaPrjProject.DAO.reqList(0, nbLineMax, Order.asc(TaPrjProject.ATT_T_NAME), cri);

		API.doResponse(response, ToolJSON.reqJSonStringWithNull(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA		, lst));

	}

	private static void doPrjKPI(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		JSONObject lstTask = reqPrjProjectPrjKPI(user, json, response);
		if(lstTask == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, lstTask));
	}

	private static JSONObject reqPrjProjectPrjKPI(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	

		Integer uId 	= ToolData.reqInt	(json, "user_id"	, user.reqId());
		Date dtEnd 		= ToolData.reqDate	(json, "dtEnd"		, new Date());
		Date dtBegin 	= ToolData.reqDate	(json, "dtBegin"	, ToolDate.reqDateByAdding(dtEnd, 0, 0, -30, 0, 0, 0));


		Set<Integer> idPrjs = reqPrjOfUserPaticiped(uId, null);

		Criterion cri = Restrictions.and(
				Restrictions.eq(TaPrjProject.ATT_I_TYPE_02, TaPrjProject.TYP_02_PRJ_ELE),
				Restrictions.between(TaPrjProject.ATT_D_DATE_03, dtBegin, dtEnd),
				Restrictions.ne(TaPrjProject.ATT_I_STATUS_01, TaPrjProject.STAT_01_PRJ_NEW)
				);

		List<TaPrjProject> lstTask = TaPrjProject.DAO.reqList_In(TaPrjProject.ATT_I_ID, idPrjs, cri);

		JSONObject res  		= new JSONObject();
		double kpiTotal 		= 0;
		double workTimeTotal 	= 0;
		if(lstTask != null && lstTask.size() > 0) {
			// Calc total KPI
			for (TaPrjProject e : lstTask) {
				if (e.req(TaPrjProject.ATT_F_VAL_04) != null) {
					kpiTotal += (double)e.req(TaPrjProject.ATT_F_VAL_04);
				}
			}

			// Calc total work time
			Set<Integer> 		ids 		= ToolSet.reqSetInt(lstTask, TaPrjProject.ATT_I_ID);
			List<TaTpyInformation> 	lstHistory 	= TaTpyInformation.DAO.reqList_In(TaTpyInformation.ATT_I_ENTITY_ID, ids);
			for (TaTpyInformation e : lstHistory) {
				workTimeTotal += reqCalcWorkTimeOnTask(e);
			}
		}
		res.put("nbTask"		, lstTask.size());
		res.put("kpiTotal"		, kpiTotal);
		res.put("workTimeTotal"	, workTimeTotal);

		/*
		if(lstTask != null && lstTask.size() > 0) {
			Set<Integer> 				ids 		= ToolSet.reqSetInt(lstTask, TaPrjProject.ATT_I_PARENT);
			List<TaPrjProject> 			lstParent 	= TaPrjProject.DAO.reqList_In(TaPrjProject.ATT_I_ID, ids);
			Map<Integer, TaPrjProject> 	mapPrj 		= new HashMap<Integer, TaPrjProject>();
			for(TaPrjProject p : lstParent) {
				mapPrj.put(p.reqId(), p);
			}

			for(TaPrjProject p : lstTask) {
				p.reqSet(TaPrjProject.ATT_O_PARENT, mapPrj.get(p.req(TaPrjProject.ATT_I_PARENT)));
			}

			Collections.sort(lstTask, new Comparator<TaPrjProject>() {
				public int compare(TaPrjProject o1, TaPrjProject o2) {
					Date dEnd01 = (Date) o1.req(TaPrjProject.ATT_D_DATE_END);
					Date dEnd02 = (Date) o2.req(TaPrjProject.ATT_D_DATE_END);
					if (dEnd01 == null || dEnd02  == null)	return 0;
					return dEnd01.compareTo(dEnd02);
				}
			});
		}
		 */

		return res;
	}

	private static long reqCalcWorkTimeOnTask(TaTpyInformation e) {
		long res = 0;
		JSONArray lstHistory = ToolJSON.reqJSonArrayFromString((String) e.req(TaTpyInformation.ATT_T_INFO_01));
		for (int i = 1; i < lstHistory.size(); i++) {
			JSONObject e1 	= (JSONObject) lstHistory.get(i);
			JSONObject e2 	= (JSONObject) lstHistory.get(i-1);

			Date d1 = ToolDate.reqDate((String) e1.get("dt"));
			Date d2 = ToolDate.reqDate((String) e2.get("dt"));

			Integer stat1 = e1.get("stat") != null ? ((Long) e1.get("stat")).intValue() : null;
			Integer stat2 = e2.get("stat") != null ? ((Long) e2.get("stat")).intValue() : null;

			if (stat1 != null && stat2 != null) {			
				if (stat2 == TaPrjProject.STAT_01_PRJ_INPROGRESS && stat1 == TaPrjProject.STAT_01_PRJ_DONE) {
					res += d1.getTime() - d2.getTime();
				}
			}			
		}

		return res;
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doPrjTaskVal(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		JSONObject map = reqPrjProjectPrjTaskVal(user, json, response);

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, map));
	}

	private static JSONObject reqPrjProjectPrjTaskVal(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		Integer uId 	= ToolData.reqInt	(json, "user_id"	, user.reqId());
		Date dtEnd 		= ToolData.reqDate	(json, "dtEnd"		, new Date());
		Date dtBegin 	= ToolData.reqDate	(json, "dtBegin"	, ToolDate.reqDateByAdding(dtEnd, 0, 0, -30, 0, 0, 0));

		Map<Integer, Double> 	mapKPI 			= new HashMap<Integer, Double>();
		Map<Integer, Double> 	mapStat 		= new HashMap<Integer, Double>();
		Map<Integer, Double> 	mapLevel 		= new HashMap<Integer, Double>();

		Set<Integer> idPrjs = reqPrjOfUserPaticiped(uId, null);

		List<TaPrjProject> lstTask = TaPrjProject.DAO.reqList_In(TaPrjProject.ATT_I_ID, idPrjs, 
				Restrictions.eq(TaPrjProject.ATT_I_TYPE_02, TaPrjProject.TYP_02_PRJ_ELE),
				Restrictions.between(TaPrjProject.ATT_D_DATE_03, dtBegin, dtEnd)
				);

		if(lstTask != null && lstTask.size() > 0) {
			Double val = 0.0;
			for(TaPrjProject p : lstTask) {
				Integer stat 	= (Integer) p.req(TaPrjProject.ATT_I_STATUS_01);
				Integer lev		= (Integer)	p.req(TaPrjProject.ATT_I_LEVEL);
				Double valInit 	= p.reqDouble(p, TaPrjProject.ATT_F_VAL_01);
				Double valReal 	= p.reqDouble(p, TaPrjProject.ATT_F_VAL_02);

				if(valInit == null)	valInit = 0.0;
				if(valReal == null)	valReal = valInit;

				//----KPI--------
				val = 0.0;
				if(mapKPI.containsKey(stat)) {
					val = mapKPI.get(stat);
				}
				val+=valReal;
				mapKPI.put( stat, val);


				//----Nb by stat--------
				val = 0.0;
				if(mapStat.containsKey(stat)) {
					val = mapStat.get(stat);
				}
				val+=1.0;
				mapStat.put( stat, val);

				//----Nb by level--------
				val = 0.0;
				if(mapLevel.containsKey(lev)) {
					val = mapLevel.get(lev);
				}
				val+=1.0;
				mapLevel.put( lev, val);

			}
		}

		JSONObject o = new JSONObject();
		o.put("kpi"	, mapKPI);
		o.put("stat", mapStat);
		o.put("lev"	, mapLevel);

		return o;
	}

	private static void doGetOrder(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		Integer uId = ToolData.reqInt	(json, "user_id", user.reqId());

		List<TaTpyPropertyQuant> lstOrder = TaTpyPropertyQuant.DAO.reqList(
				Restrictions.eq(TaTpyPropertyQuant.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_AUT_USER),
				Restrictions.eq(TaTpyPropertyQuant.ATT_I_ENTITY_ID		, uId),
				Restrictions.eq(TaTpyPropertyQuant.ATT_I_PARENT_TYPE	, DefDBExt.ID_TA_TPY_RELATIONSHIP)
				);

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, lstOrder));
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doGetCustomer(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		Integer 			objId	= ToolData.reqInt	(json, "id"		, -1	);
		String	 			objCode	= ToolData.reqStr	(json, "code"	, null	);

		if (objCode==null||objId<0) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		TaPrjProject prj = TaPrjProject.DAO.reqEntityByRef(objId);
		int pMan = (int) prj.req(TaPrjProject.ATT_I_PER_MANAGER);
		int uMan = user.reqPerManagerId();
		if(prj == null || pMan != uMan) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		List<TaTpyRelationship> list 		= TaTpyRelationship.DAO.reqList(Restrictions.and(
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT), 
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_PER_PERSON), 
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, objId)));

		List<TaPerPerson> 		listPers 	= new ArrayList<TaPerPerson>();

		if(list != null && list.size() > 0) {
			Set<Integer> 		setP 		= ToolSet.reqSetInt(list, TaTpyRelationship.ATT_I_ENTITY_ID_02);
			if(!setP.isEmpty()) {
				listPers = TaPerPerson.DAO.reqList_In(TaPerPerson.ATT_I_ID, setP);

				List<TaPerPerson> 		listCus 	= new ArrayList<TaPerPerson>();
				if(listPers != null && listPers.size() > 0) {
					for(TaPerPerson p : listPers) {
						if((Integer)p.req(TaPerPerson.ATT_I_TYPE_02) == TaPerPerson.TYP_02_CLIENT) {
							listCus.add(p);
						}
					}
				}

				if(listCus.size() > 0) {
					Set<Integer> setPer = ToolSet.reqSetInt(listCus, TaPerPerson.ATT_I_ID);
					if(!setPer.isEmpty()) {
						Map<Integer, TaTpyDocument> mapAva = new HashMap<Integer, TaTpyDocument>();
						List<TaTpyDocument> avas = TaTpyDocument.reqTpyDocuments (DefDBExt.ID_TA_PER_PERSON, setPer, TaTpyDocument.TYPE_01_FILE_MEDIA, TaTpyDocument.TYPE_02_FILE_IMG_AVATAR, null, null);


						if(avas != null && avas.size() > 0) {
							for(TaTpyDocument a : avas) {
								mapAva.put((Integer) a.req(TaTpyDocument.ATT_I_ENTITY_ID), a);
							}

							for(TaPerPerson p : listCus) {
								if(mapAva.containsKey(p.req(TaPerPerson.ATT_I_ID))){
									p.reqSet(TaPerPerson.ATT_O_AVATAR, mapAva.get(p.req(TaAutUser.ATT_I_PER_PERSON)));
								}
							}
						}
					}

					Map<Integer, TaPerPerson> pMap = new HashMap<Integer, TaPerPerson>();
					for(TaPerPerson p : listCus) {
						pMap.put((Integer) p.reqRef(), p);
					}

					for(int i = 0; i< list.size(); i++) {
						TaTpyRelationship per = list.get(i);
						if(!setPer.contains(per.req(TaTpyRelationship.ATT_I_ENTITY_ID_02))) {
							list.remove(i);
						}
					}
					for(TaTpyRelationship per : list) {
						if(!pMap.containsKey(per.req(TaTpyRelationship.ATT_I_ENTITY_ID_02)))	continue;
						per.reqSet(TaTpyRelationship.ATT_O_ENTITY_02, pMap.get(per.req(TaTpyRelationship.ATT_I_ENTITY_ID_02)));
					}
				}
			}
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, list 
				));
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doSaveCustomer(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		Integer 			objId	= ToolData.reqInt		(json, "id"		, -1	);
		String	 			objCode	= ToolData.reqStr		(json, "code"	, null	);
		JSONArray			lstCus 	= ToolData.reqJsonArr	(json, "customers"	, new JSONArray()) ;

		if (objCode==null||objId<0) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		TaPrjProject prj = TaPrjProject.DAO.reqEntityByRef(objId);
		if(prj == null || prj.req(TaPrjProject.ATT_I_PER_MANAGER) != user.reqPerManagerId()) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		List<TaTpyRelationship> list 	= TaTpyRelationship.DAO.reqList(Restrictions.and(
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT), 
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_PER_PERSON), 
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, objId)));


		Map<Integer, TaTpyRelationship> mapExist = new HashMap<Integer, TaTpyRelationship>();
		if(list != null && list.size() > 0) {
			reqListRelationShipCustomer(list);
			for(TaTpyRelationship m: list) {
				mapExist.put((Integer) m.req(TaTpyRelationship.ATT_I_ENTITY_ID_02), m);
			}
		}

		List<TaTpyRelationship> lstRE 	= new ArrayList<TaTpyRelationship>();
		List<TaTpyRelationship> lstMOD 	= new ArrayList<TaTpyRelationship>();
		List<TaTpyRelationship> lstDEL 	= new ArrayList<TaTpyRelationship>();

		if(lstCus != null && lstCus.size() > 0) {
			for(int i = 0; i < lstCus.size(); i++) {
				JSONObject cus = (JSONObject) lstCus.get(i);
				Map<String, Object> attr 	= API.reqMapParamsByClass(cus, TaTpyRelationship.class);

				Integer cId 				= (Integer) attr.get(TaTpyRelationship.ATT_I_ENTITY_ID_02);
				TaTpyRelationship re 		= new TaTpyRelationship(attr);

				if(mapExist.containsKey(cId)) {
					TaTpyRelationship memExist = mapExist.get(cId);
					memExist.reqSet(TaTpyRelationship.ATT_I_TYPE	, attr.get(TaTpyRelationship.ATT_I_TYPE));
					memExist.reqSet(TaTpyRelationship.ATT_D_DATE_02, new Date());
					lstMOD.add(mapExist.get(cId));
					mapExist.remove(cId);
					continue;
				}

				re.reqSet(TaTpyRelationship.ATT_D_DATE_01		, new Date());
				re.reqSet(TaTpyRelationship.ATT_I_ENTITY_TYPE_01, DefDBExt.ID_TA_PRJ_PROJECT);
				re.reqSet(TaTpyRelationship.ATT_I_ENTITY_TYPE_02, DefDBExt.ID_TA_PER_PERSON);
				lstRE.add(re);
			}
		}

		if(lstRE.size() > 0)	TaTpyRelationship.DAO.doPersist(lstRE);
		if(lstMOD.size() > 0) 	TaTpyRelationship.DAO.doMerge(lstMOD);

		if(!mapExist.isEmpty()) {
			for (Map.Entry<Integer, TaTpyRelationship> entry : mapExist.entrySet()) {
				lstDEL.add(entry.getValue());
			}
			TaTpyRelationship.DAO.doRemove(lstDEL);
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES
				));
	}

	private static List<TaPerPerson> reqListRelationShipCustomer(List<TaTpyRelationship> list) throws Exception{
		List<TaPerPerson> 		listCus 	= new ArrayList<TaPerPerson>();
		List<TaPerPerson> 		listPers 	= new ArrayList<TaPerPerson>();

		if(list != null && list.size() > 0) {
			Set<Integer> 		setP 		= ToolSet.reqSetInt(list, TaTpyRelationship.ATT_I_ENTITY_ID_02);
			if(!setP.isEmpty()) {
				listPers = TaPerPerson.DAO.reqList_In(TaPerPerson.ATT_I_ID, setP);

				if(listPers != null && listPers.size() > 0) {
					for(TaPerPerson p : listPers) {
						if((Integer)p.req(TaPerPerson.ATT_I_TYPE_02) == TaPerPerson.TYP_02_CLIENT) {
							listCus.add(p);
						}
					}
				}

				if(listCus != null && listCus.size() > 0) {
					Set<Integer> setCus = ToolSet.reqSetInt(listCus, TaPerPerson.ATT_I_ID);
					List<TaTpyRelationship> listCustomer = new ArrayList<TaTpyRelationship>();
					for(TaTpyRelationship p: list) {
						if(setCus.contains(p.req(TaTpyRelationship.ATT_I_ENTITY_ID_02))) {
							listCustomer.add(p);
						}
					}
					list = listCustomer;
				}
			}
		}

		return listCus;
	}


	//--------------------------------------------------------------------------------------------------------------------
	private void doGetRelationshipRole(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLckEnd --------------");
		Integer 	id 	= ToolData.reqInt	(json, "id", null);

		boolean ok = reqRelationshipRole(user, id);

		if(!ok) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES
				));	
	}

	private static boolean reqRelationshipRole(TaAutUser user, Integer idUser) throws Exception {
		if(idUser == null)							return false;

		TaAutUser uInfo 	= TaAutUser.DAO.reqEntityByRef(idUser);
		if(uInfo == null)							return false;

		Integer 	iSup 	= (Integer) uInfo.req(TaAutUser.ATT_I_AUT_USER_03);
		if(iSup != null && iSup == user.reqId())	return true;

		Integer 	iPerMan = (Integer) uInfo.req(TaAutUser.ATT_I_PER_MANAGER);
		if(iPerMan != user.reqPerManagerId())		return false;

		boolean 	isAdmin = user.canBeAdmin() || user.canBeSuperAdmin();
		if(isAdmin)									return true;

		return false;
	}

	private void doGetInfoWithCustomer(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLckEnd --------------");
		Integer 	id 	= ToolData.reqInt	(json, "id", null);

		if(id == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		TaTpyRelationship re = TaTpyRelationship.DAO.reqEntityByRef(id);

		if(re == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		List<TaTpyPropertyQuant> infos = TaTpyPropertyQuant.DAO.reqList(
				Restrictions.eq(TaTpyPropertyQuant.ATT_I_PARENT_ID	, id),
				Restrictions.eq(TaTpyPropertyQuant.ATT_I_PARENT_TYPE, DefDBExt.ID_TA_TPY_RELATIONSHIP)
				);

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, infos
				));	
	}

	private void doUpdateInfoWithCustomer(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLckEnd --------------");
		boolean ok = reqUpdateInfoCustomer(user, json, response);

		if(!ok) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES
				));	
	}

	@SuppressWarnings("unchecked")
	private static boolean reqUpdateInfoCustomer(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		Integer 	id 	= ToolData.reqInt	(json, "id", null);
		JSONObject	obj = ToolData.reqJson	(json, "obj", null);

		if(id == null)	return false;

		TaTpyRelationship re = TaTpyRelationship.DAO.reqEntityByRef(id);

		if(re == null)	return false;

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaTpyPropertyQuant.class);

		List<TaTpyPropertyQuant> infos = TaTpyPropertyQuant.DAO.reqList(
				Restrictions.eq(TaTpyPropertyQuant.ATT_I_PARENT_ID	, id),
				Restrictions.eq(TaTpyPropertyQuant.ATT_I_PARENT_TYPE, DefDBExt.ID_TA_TPY_RELATIONSHIP)
				);

		if(infos != null && infos.size() > 0) {
			TaTpyPropertyQuant info = infos.get(0);

			TaTpyPropertyQuant.DAO.doMerge(info, attr);

		} else {
			TaTpyPropertyQuant 	ent 	= new TaTpyPropertyQuant(attr);
			ent.reqSet(TaTpyPropertyQuant.ATT_I_PARENT_ID	, id);
			ent.reqSet(TaTpyPropertyQuant.ATT_I_PARENT_TYPE	, DefDBExt.ID_TA_TPY_RELATIONSHIP);
			ent.reqSet(TaTpyPropertyQuant.ATT_I_ENTITY_ID	, user.reqId());
			ent.reqSet(TaTpyPropertyQuant.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_AUT_USER);

			TaTpyPropertyQuant.DAO.doPersist(ent);
		}

		JSONObject objCus = new JSONObject();
		objCus.put("dtBegin", obj.get("dtBegin"));
		objCus.put("dtEnd"	, obj.get("dtEnd"));

		Map<String, Object> attrCus 	= API.reqMapParamsByClass(objCus, TaTpyRelationship.class);
		TaTpyRelationship.DAO.doMerge(re, attrCus);

		return true;
	}

	private void doDeleteCustomer(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLckEnd --------------");
		boolean ok = reqDeleteCustomer(user, json, response);

		if(!ok) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES
				));	
	}

	private static boolean reqDeleteCustomer(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		Integer 		id 		= ToolData.reqInt	(json, "id", null);

		if(id == null)	return false;

		TaTpyRelationship re 	= TaTpyRelationship.DAO.reqEntityByRef(id);

		if(re == null)	return false;

		Integer 		idPrj 	= (Integer) re.req(TaTpyRelationship.ATT_I_ENTITY_ID_01);
		TaPrjProject 	prj 	= TaPrjProject.DAO.reqEntityByRef(idPrj);
		if(prj == null)	return false;

		List<TaTpyPropertyQuant> infos = TaTpyPropertyQuant.DAO.reqList(
				Restrictions.eq(TaTpyPropertyQuant.ATT_I_PARENT_ID	, id),
				Restrictions.eq(TaTpyPropertyQuant.ATT_I_PARENT_TYPE, DefDBExt.ID_TA_TPY_RELATIONSHIP)
				);

		if(infos != null && infos.size() > 0) {
			TaTpyPropertyQuant info = infos.get(0);
			TaTpyPropertyQuant.DAO.doRemove(info);
		}

		TaTpyRelationship.DAO.doRemove(re);

		reqSaveHistory(idPrj, null, null, user, TYP_DEL, TAB_CUSTOMER, null, null);

		return true;
	}

	private void doChangeTypCustomer(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLckEnd --------------");
		TaTpyRelationship re = reqChangeTypCustomer(user, json, response);

		if(re == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, re
				));	
	}

	private static TaTpyRelationship reqChangeTypCustomer(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		Integer 	id 			= ToolData.reqInt	(json, "id"			, null);
		Integer 	changeto 	= ToolData.reqInt	(json, "changeto"	, null);

		if(id == null || changeto == null)	return null;

		TaTpyRelationship re 	= TaTpyRelationship.DAO.reqEntityByRef(id);

		if(re == null)	return null;

		Integer 		idPrj 	= (Integer) re.req(TaTpyRelationship.ATT_I_ENTITY_ID_01);
		TaPrjProject 	prj 	= TaPrjProject.DAO.reqEntityByRef(idPrj);
		if(prj == null)	return null;

		re.reqSet(TaTpyRelationship.ATT_I_TYPE, changeto);
		TaTpyRelationship.DAO.doMerge(re);

		reqSaveHistory(idPrj, null,  null, user, TYP_MOD, TAB_CUSTOMER, null, null);

		return re;
	}

	private void doNewCustomer(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doLckEnd --------------");
		TaTpyRelationship re = reqNewCustomer(user, json, response);

		if(re == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, re
				));	
	}

	private static TaTpyRelationship reqNewCustomer(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		JSONObject 	obj 			= ToolData.reqJson	(json, "obj"			, null);

		if(obj == null)	return null;

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaTpyRelationship.class);


		Integer idPrj = (Integer) attr.get(TaTpyRelationship.ATT_I_ENTITY_ID_01);
		Integer idCus = (Integer) attr.get(TaTpyRelationship.ATT_I_ENTITY_ID_02);

		TaPrjProject prj = TaPrjProject.DAO.reqEntityByRef(idPrj);

		List<TaTpyRelationship> lst = TaTpyRelationship.DAO.reqList(Restrictions.and(
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT), 
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_PER_PERSON), 
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_01	, idPrj),
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02	, idCus)));

		if(lst != null && lst.size() > 0)	return null;

		TaTpyRelationship ent = new TaTpyRelationship(attr);

		ent.reqSet(TaTpyRelationship.ATT_D_DATE_01		, new Date());
		ent.reqSet(TaTpyRelationship.ATT_I_ENTITY_TYPE_01, DefDBExt.ID_TA_PRJ_PROJECT);
		ent.reqSet(TaTpyRelationship.ATT_I_ENTITY_TYPE_02, DefDBExt.ID_TA_PER_PERSON);

		TaTpyRelationship.DAO.doPersist(ent);

		reqSaveHistory(idPrj, null, null, user, TYP_ADD, TAB_CUSTOMER, null, null);

		return ent;
	}

	//--------------------------------------------------------------------------------------------------------
	private static final int TYP_ADD 		= 1;
	private static final int TYP_MOD 		= 2;
	private static final int TYP_DEL 		= 3;
	private static final int TYP_JOIN 		= 4;
	private static final int TYP_OUT 		= 6;
	private static final int TYP_MOVE 		= 9;
	private static final int TYP_LATE 		= 10;
	private static final int TYP_HAS_CHANGE = 11;

	private static final String TAB_CONTENT 	= "content";
	private static final String TAB_MEMBER 		= "member";
	private static final String TAB_PRJ 		= "prj";
	private static final String TAB_EPIC 		= "epic";
	private static final String TAB_TASK 		= "task";
	private static final String TAB_COMMENT 	= "comment";
	private static final String TAB_FILE 		= "file";
	private static final String TAB_CUSTOMER 	= "customer";
	private static final String TAB_DAY_OFF 	= "off";
	private static final String TAB_REPORT 		= "report";


	private static void reqSaveHistory(Integer entId, String entRef, Integer parentId, TaAutUser user, Integer typ, String typTab, Integer stat01, Integer stat02, Object...params) throws Exception {
		JSONObject his 		= new JSONObject();
		his.put("typ"		, typ);
		his.put("typTab"	, typTab);
		his.put("uID"		, user.reqRef());
		his.put("uName"		, user.req(TaAutUser.ATT_T_LOGIN_01));
		his.put("dt"		, ToolDate.reqString(new Date(), ToolDate.FORMAT_ISO));		
		his.put("statFrom"	, stat01);
		his.put("statTo"	, stat02);
		his.put("stat"		, stat02);
		if (params!=null && params.length>0) his.put("lev"		, params[0]);

		reqSaveHistory (entId, his);

		if (parentId!=null && !parentId.equals(entId)){
			his 	= new JSONObject();
			his.put("typ"		, typ);
			his.put("typTab"	, typTab);
			his.put("uID"		, user.reqRef());
			his.put("uName"		, user.req(TaAutUser.ATT_T_LOGIN_01));
			his.put("dt"		, ToolDate.reqString(new Date(), ToolDate.FORMAT_ISO));		

			his.put("entId"		, entId);
			his.put("entRef"	, entRef);

			reqSaveHistory (parentId, his);
		}
	}

	private static void reqSaveHistory(Integer entId, JSONObject js) throws Exception {
		TaTpyInformation hist = TaTpyInformation.DAO.reqEntityByValues(
				TaTpyInformation.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_PRJ_PROJECT, 
				TaTpyInformation.ATT_I_ENTITY_ID	, entId,
				TaTpyInformation.ATT_I_TYPE_01		, TaTpyInformation.TYP_01_HISTO
				);
		if (hist==null){
			hist = new TaTpyInformation ();
			hist.reqSet(TaTpyInformation.ATT_I_ENTITY_TYPE		, DefDBExt.ID_TA_PRJ_PROJECT);
			hist.reqSet(TaTpyInformation.ATT_I_ENTITY_ID		, entId);
			hist.reqSet(TaTpyInformation.ATT_T_INFO_01			, "[]");
			hist.reqSet(TaTpyInformation.ATT_I_STATUS		    , 0);
			hist.reqSet(TaTpyInformation.ATT_I_TYPE_01		    , TaTpyInformation.TYP_01_HISTO);
			hist.reqSet(TaTpyInformation.ATT_I_AUT_USER_01 		, js.get("uID"));
			TaTpyInformation.DAO.doPersist(hist);
		}

		String	 	val = hist.reqStr(hist, TaTpyInformation.ATT_T_INFO_01);
		JSONArray 	arr = ToolJSON.reqJSonArrayFromString(val);
		arr.add(js);

		hist.reqSet(TaTpyInformation.ATT_T_INFO_01		, arr.toJSONString());
		hist.reqSet(TaTpyInformation.ATT_D_DATE_01		, new Date());
		hist.reqSet(TaTpyInformation.ATT_I_AUT_USER_01 	, js.get("uID"));
		hist.reqSet(TaTpyInformation.ATT_I_TYPE_01		, TaTpyInformation.TYP_01_HISTO);
		TaTpyInformation.DAO.doMerge(hist);
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doTreeView(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		Integer 			prjID	= ToolData.reqInt	(json, "id"		, -1	);
		String	 			prjCode	= ToolData.reqStr	(json, "code"	, null	);

		TaPrjProject  		ent	 	= TaPrjProject.reqPrjFromCache(user, prjID, prjCode);
		if (ent==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		if (!canWorkWithObj(user, WORK_MOD, ent)){ //other param after obj...
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		TaPrjProject  tree = reqPrjTree(user, ent);
		if (tree == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response,ToolJSON.reqJSonString(		//filter,
					DefJS.SESS_STAT		, 1,  
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, tree 
					));
		}
	}

	private static TaPrjProject reqPrjTree (TaAutUser user, TaPrjProject prj) throws Exception{
		TaPrjProject 	tree 	= (TaPrjProject) prj.req(TaPrjProject.ATT_O_TREE); 	
		if (tree==null){
			Integer 			iGroup 	= (Integer) prj.reqInt(TaPrjProject.ATT_I_GROUP);

			List<TaPrjProject>	lstPrj 	= TaPrjProject.DAO.reqList(Restrictions.eq(TaPrjProject.ATT_I_GROUP, iGroup));
			Set<Integer> 		ids 	= ToolSet.reqSetInt(lstPrj, TaPrjProject.ATT_I_ID);

			List<TaTpyRelationship> lstMems	= TaTpyRelationship.DAO.reqList_In(TaTpyRelationship.ATT_I_ENTITY_ID_01, ids, 
					Restrictions.and(
							Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT), 
							Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER)
							));

			if(lstMems != null && lstMems.size() > 0) {
				//---build users + their avatars
				List<ViAutUserMember> lstUsers = TaAutUser		.reqBuildUserMember	(lstMems, TaTpyRelationship.ATT_I_ENTITY_ID_02, TaTpyRelationship.ATT_O_ENTITY_02);
				TaTpyDocument	.reqBuildAvatar(lstUsers, DefDBExt.ID_TA_AUT_USER, ViAutUserMember.ATT_O_AVATAR);

				Hashtable<Integer, EntityAbstract> map = ToolDBEntity.reqTabKeyInt(lstPrj, TaPrjProject.ATT_I_ID);

				for (TaTpyRelationship r: lstMems) {
					int pId = r.reqInt(TaTpyRelationship.ATT_I_ENTITY_ID_01);
					TaPrjProject p = (TaPrjProject) map.get(pId);
					if (p!=null) p.doAddMember(r);
				}

				//---build tree

				for (TaPrjProject p: lstPrj) {
					if (p.reqId().equals(prj.reqId())) tree = p;

					Integer pId = p.reqInt(TaPrjProject.ATT_I_PARENT);
					if (pId==null){
						continue;
					}
					TaPrjProject par = (TaPrjProject) map.get(pId);
					if (p!=null) par.doAddChild(p);
				}

			}
			prj.reqSet(TaPrjProject.ATT_O_TREE, tree);
		}

		return tree;
	}
	//------------------------------------------Evaluation---------------------------------------------------------------
	private static List<TaPrjProject> reqEvaluation(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		TaPrjProject 	prj		= TaPrjProject.reqPrjFromCache(user, json);

		//----prj must be in cache---
		if (prj==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return null;
		}

		return reqPrjProjectChildById(prj, false);
	}


	private static List<TaPrjProject> reqPrjProjectChildById(TaPrjProject prj, boolean withParent) throws Exception{
		Integer typ00 = (Integer) prj.req(TaPrjProject.ATT_I_TYPE_00);
		Integer typ01 = (Integer) prj.req(TaPrjProject.ATT_I_TYPE_01);
		Integer typ02 = (Integer) prj.req(TaPrjProject.ATT_I_TYPE_02);

		Integer objId = prj.reqId();	
		if(typ00 == TaPrjProject.TYP_00_PRJ_SPRINT) {
			String desc02    = (String) prj.req(TaPrjProject.ATT_T_INFO_02);
			if(desc02 == null)  return null;
			JSONObject objStr = ToolJSON.reqJSonFromString(desc02);
			if(objStr == null)  return null;
			JSONArray  arrIds = (JSONArray) objStr.get("task_ids");

			Set<Integer> setIds  = new HashSet<>();

			for(int i = 0; i< arrIds.size(); i++) {
				setIds.add((int)(long) arrIds.get(i));
			}

			if(setIds != null && !setIds.isEmpty()) {
				List<TaPrjProject> 	tasks 	= TaPrjProject.DAO.reqList_In(TaPrjProject.ATT_I_ID, setIds, Restrictions.eq(TaPrjProject.ATT_I_TYPE_02, TaPrjProject.TYP_02_PRJ_ELE));
				return tasks;
			}

		}else {
			Integer 			iGroup 	= (Integer) prj.req(TaPrjProject.ATT_I_GROUP);
			List<TaPrjProject>	lstPrj 	= TaPrjProject.DAO.reqList(Restrictions.eq(TaPrjProject.ATT_I_GROUP, iGroup));

			if(lstPrj != null && lstPrj.size() > 0) {
				Hashtable<Integer, TaPrjProject> 	mapPrj 		= new Hashtable<Integer,TaPrjProject> ();
				Hashtable<Integer, Set<Integer>>	mapParent 	= new Hashtable<Integer, Set<Integer>>();

				//--------build dictionary---------------------------------
				for(TaPrjProject p : lstPrj) {
					int pId = p.reqId();
					mapPrj.put(pId, p);

					Integer parent = (Integer) p.req(TaPrjProject.ATT_I_PARENT);					

					if(parent != null) {
						Set<Integer> set = mapParent.get(parent);
						if (set==null) set = new HashSet<Integer>();

						set.add(pId);
						mapParent.put(parent, set);
					}
				}

				//-------get children---------------------------------------
				List<TaPrjProject> 	lstAll 		= new ArrayList<TaPrjProject>();
				Set<Integer> 		setChild 	= mapParent.get(objId);
				if (setChild==null) return lstAll;

				int					maxDeep = 20;

				if(withParent) {
					reqListChildWithParent	(maxDeep, setChild, mapParent, lstAll, mapPrj);
				} else {
					for(Integer i : setChild) {
						if(!mapParent.containsKey(i)) {
							lstAll.add(mapPrj.get(i));
						}
					}
				}
				return lstAll;
			}
		}

		return null;
	}


	private static void reqListChildWithParent(int deep, Set<Integer> setChild, Map<Integer, Set<Integer>>	mapParentPrj, List<TaPrjProject> lstAll, Map<Integer, TaPrjProject> mapGetPrj) {
		if (deep<0) return;
		for(Integer i : setChild) {
			lstAll.add(mapGetPrj.get(i));
			if(mapParentPrj.containsKey(i)) {
				reqListChildWithParent(deep-1, mapParentPrj.get(i), mapParentPrj, lstAll, mapGetPrj);
			}
		}
	}


	private static void doEvaluation(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");

		List<TaPrjProject> lstPrj = reqEvaluation(user, json, response);

		if(lstPrj == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, lstPrj 
				));
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doContentRefresh(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");

		Integer 			objId	= ToolData.reqInt	(json, "id"		, -1	);	
		String	 			objCode	= ToolData.reqStr	(json, "code"	, null	);
		boolean 			forced 	= true;

		TaPrjProject 		ent 	= reqGet(user, forced, objId, objCode );

		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
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

	private static void doEpicRefresh(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		Integer 			id		= ToolData.reqInt	(json, "id"		, null	);	
		String 				code	= ToolData.reqStr	(json, "code"	, null	);	

		TaPrjProject  		ent	 	= TaPrjProject.reqPrjFromCache(user, id, code);
		if (ent==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		if (!canWorkWithObj(user, WORK_GET, ent)){ //other param after ent...
			API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			return;
		}

		ent.doBuildEpics	(true);
		ent.doBuildEpicInfo	(true);

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent.req(TaPrjProject.ATT_O_EPIC_INFO)
				));

	}

	private static void doTaskRefresh(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		Integer 			id		= ToolData.reqInt	(json, "id"		, null	);	
		String 				code	= ToolData.reqStr	(json, "code"	, null	);	

		TaPrjProject  		ent	 	= TaPrjProject.reqPrjFromCache(user, id, code);
		if (ent==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		if (!canWorkWithObj(user, WORK_GET, ent)){ //other param after ent...
			API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			return;
		}


		ent.doBuildTasks(true);

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent.req(TaPrjProject.ATT_O_TASKS)
				));
	}


	private static void doSprintRefresh(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		Integer 			id		= ToolData.reqInt	(json, "id"		, null	);	
		String 				code	= ToolData.reqStr	(json, "code"	, null	);	

		TaPrjProject  		ent	 	= TaPrjProject.reqPrjFromCache(user, id, code);
		if (ent==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		if (!canWorkWithObj(user, WORK_GET, ent)){ //other param after ent...
			API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			return;
		}

		ent.doBuildSprintTasks(true);

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent.req(TaPrjProject.ATT_O_TASKS)
				));
	}


	private static void doRefreshTestGroup(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");

		Integer 			objId	= ToolData.reqInt	(json, "id"		, -1	);	
		TaPrjProject          obj   = TaPrjProject.DAO.reqEntityByRef(objId);

		List<TaPrjProject> 	ent 	= reqPrjProjectRefreshTaskGroup(obj, user);

		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
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

	private static List<TaPrjProject> reqPrjProjectRefreshTaskGroup(TaPrjProject prj, TaAutUser user) throws Exception{

		String strIds  = prj.req(TaPrjProject.ATT_T_INFO_02).toString();
		JSONObject ids = ToolJSON.reqJSonObjectFromString(strIds);
		JSONArray  task_ids = (JSONArray) ids.get("task_ids");

		Set<Integer> setIds = new HashSet<Integer>();
		for(int i=0; i< task_ids.size(); i++) {
			setIds.add((int)(long) task_ids.get(i));
		}

		List<TaPrjProject> eles = TaPrjProject.DAO.reqList_In(TaPrjProject.ATT_I_ID, setIds, 
				Restrictions.eq(TaPrjProject.ATT_I_GROUP	, prj.req(TaPrjProject.ATT_I_GROUP)),
				Restrictions.eq(TaPrjProject.ATT_I_TYPE_00	, TaPrjProject.TYP_00_PRJ_TEST),
				Restrictions.eq(TaPrjProject.ATT_I_TYPE_01	, TaPrjProject.TYP_01_TEST_UNIT));

		return eles;
	}

	private static void doGetParent(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		List<TaPrjProject> 	ent 	= reqPrjParent(user, json, response);

		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
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

	private static List<TaPrjProject> reqPrjParent(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception{
		int 			prjID		= ToolData.reqInt	(json, "prjID"		, -1);
		int 			prjGroup	= ToolData.reqInt	(json, "prjGroup"	, -1);

		//only get path with epic, task
		if(prjID == prjGroup || prjID == -1 || prjGroup == -1)	return null;

		//get all prj same group
		List<TaPrjProject> grs = TaPrjProject.DAO.reqList(Restrictions.eq(TaPrjProject.ATT_I_GROUP	, prjGroup));

		if(grs == null || grs.size() == 0)	return null;

		//map prj for get
		Map<Integer, TaPrjProject> 	mapPrj 		= new HashMap<Integer, TaPrjProject>();
		//map prj children ===> parent
		Map<Integer, Integer> 		mapChild 	= new HashMap<Integer, Integer>();

		for(TaPrjProject p : grs) {
			mapPrj.put(p.reqId(), p);

			Integer iParent = (Integer)p.req(TaPrjProject.ATT_I_PARENT);
			if(iParent != null) {
				mapChild.put(p.reqId(), iParent);
			}
		}

		//not contain, erreur
		if(!mapPrj.containsKey(prjID))		return null;
		if(!mapChild.containsKey(prjID))	return null;

		List<TaPrjProject> lst = new ArrayList<TaPrjProject>();
		lst.add(mapPrj.get(prjID));

		//get parent .....
		Integer prjParent = prjID;
		while(mapChild.containsKey(prjParent)) {
			lst.add(mapPrj.get(mapChild.get(prjParent)));
			prjParent = mapChild.get(prjParent);
		}

		return lst;
	}


	private static void doGetDocCompanyUser(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		TaTpyDocument	doc 	= reqPrjProjectGetDocCompanyUser(user, json, response);

		if (doc == null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, doc
				));
	}

	private static TaTpyDocument reqPrjProjectGetDocCompanyUser(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception{
		Integer 			manId		= user.reqPerManagerId();

		if(manId == null)	return null;

		List<TaTpyDocument> docs = TaTpyDocument.DAO.reqList(
				Restrictions.eq(TaTpyDocument.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_PER_PERSON), 
				Restrictions.eq(TaTpyDocument.ATT_I_ENTITY_ID	, manId),
				Restrictions.eq(TaTpyDocument.ATT_I_TYPE_01		, TaTpyDocument.TYPE_01_FILE_MEDIA),
				Restrictions.eq(TaTpyDocument.ATT_I_TYPE_02		, TaTpyDocument.TYPE_02_FILE_IMG_AVATAR));

		if(docs == null || docs.size() == 0)	return null;

		return docs.get(0);
	}

	private static void doAddTaskToEpic(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		TaPrjProject 			ent		= reqPrjProjectAddTaskToEpic		(user, json, response);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent
				));		
	}

	private static TaPrjProject reqPrjProjectAddTaskToEpic(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		Integer idTask 	= ToolData.reqInt	(json, "idTask"	, null);
		Integer idEpic 	= ToolData.reqInt	(json, "idEpic"	, null);
		Integer id 		= ToolData.reqInt	(json, "id"		, null);

		if (!canWorkWithObj(user, WORK_MOD)){ //other param after obj...
			return null;
		}

		if(idTask == null || idEpic == null)	return null;

		Set<Integer> setId = new HashSet<Integer>();
		setId.add(idTask);
		setId.add(idEpic);

		List<TaPrjProject> prjs = TaPrjProject.DAO.reqList_In(TaPrjProject.ATT_I_ID, setId);

		if(prjs == null || prjs.size() < 2)	return null;

		TaPrjProject task = new TaPrjProject();

		for(TaPrjProject p : prjs) {
			if((int)p.reqId() == idTask) {
				task = p;
				break;
			}
		}

		task.reqSet(TaPrjProject.ATT_D_DATE_02			, new Date());
		task.reqSet(TaPrjProject.ATT_I_AUT_USER_02		, user.reqId());
		task.reqSet(TaPrjProject.ATT_I_PARENT			, idEpic);

		TaPrjProject.DAO.doMerge(task);

		//req to history
		//		JSONObject his 		= new JSONObject();
		//		his.put("typ"		, TYP_ADD);
		//		his.put("typTab"	, TAB_TASK);
		//		his.put("uID"		, user.reqRef());
		//		his.put("stat"		, task.req(TaPrjProject.ATT_I_STATUS));
		//		his.put("dt"		, ToolDate.reqString(new Date(), ToolDate.FORMAT_ISO));		
		//		reqSaveHistory(task.reqId(), his);

		reqSaveHistory(task.reqId(), (String) task.req(TaPrjProject.ATT_T_CODE_01), idEpic, user, TYP_ADD, TAB_TASK, null, (Integer)task.req(TaPrjProject.ATT_I_STATUS_01));

		Integer stat =  (Integer) task.req(TaPrjProject.ATT_I_STATUS_01);
		if (stat == TaPrjProject.STAT_01_PRJ_TODO || stat == TaPrjProject.STAT_01_PRJ_INPROGRESS ||  stat == TaPrjProject.STAT_01_PRJ_REVIEW || stat == TaPrjProject.STAT_01_PRJ_TEST ) {
			reqSaveNotificationComment(task, user, TAB_TASK, TYP_MOD);
		}

		//build prj, lst task, lst epic, nb task
		TaPrjProject 		ent 	= TaPrjProject.DAO.reqEntityByRef(id, false);
		if(ent == null)	return null;

		ent.doBuildEpics(true);	
		ent.doBuildTasks(true);	
		ent.doBuildEpicInfo(true);	

		return ent;
	}

	/*
	private static void doGetInfoPartnerMan(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		PartnerProjectResume 	result 	= reqPrjProjectGetInfoPartnerMan(user, json, response);

		if (result==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		if (!canWorkWithObj(user, WORK_GET, result)){
			API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, result
				));
	}

	//-----------------------------------------------------------------------------------------------------------------------
	private static Set<Integer> setTyp = new HashSet<Integer>();
	static {
		setTyp.add(TaPrjProject.TYP00_PRJ_PROJECT);
		setTyp.add(TaPrjProject.TYP00_PRJ_DATACENTER);
	}

	private static PartnerProjectResume reqPrjProjectGetInfoPartnerMan(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception{
		Integer 					id				= ToolData.reqInt	(json, "id", null);

		if(id == null)	return null;

		List<TaPrjProject> prjNormal 		= new ArrayList<TaPrjProject>();
		List<TaPrjProject> prjDataCenter 	= new ArrayList<TaPrjProject>();

		//get list prj and list data center, forced racine to first parent == null)
		List<TaPrjProject> lstPrj = TaPrjProject.DAO.reqList(	Order.asc(TaPrjProject.ATT_I_PARENT),
																Restrictions.in(TaPrjProject.ATT_I_TYPE_00		, setTyp),
																Restrictions.eq(TaPrjProject.ATT_I_PER_MANAGER	, id));

		if(lstPrj != null && lstPrj.size() > 0) {
			Map<Integer, TaPrjProject> 	mapGet = new HashMap<Integer, TaPrjProject>();
			Set<Integer> 				setPrj = ToolSet.reqSetInt(lstPrj, TaPrjProject.ATT_I_ID);

			//get map size all prj
			Map<Integer, Double> 	mapSize 	= new HashMap<Integer, Double>();
//			List<TaTpyDocument> 	lstDoc 		= ServiceTpyDocument.reqListDocumentSizeGroupByTypeId(DefDBExt.ID_TA_PRJ_PROJECT, setPrj);

			List<TaTpyDocument> 	lstDoc 		= DocTool.reqTpyDocuments(DefDBExt.ID_TA_PRJ_PROJECT, setPrj, null, null);
			if(lstDoc != null && lstDoc.size() > 0) {
				for(TaTpyDocument d: lstDoc) {
					Integer prjId 	= (Integer) d.req(TaTpyDocument.ATT_I_ENTITY_ID);
					Double 	size 	= d.reqDouble(d, TaTpyDocument.ATT_F_VAL_01);
					if(size == null) size = 0.0;

					Double  size01  = mapSize.get(prjId);
					if(size01 == null) size01 = 0.0;

					mapSize.put(prjId, size01+size);
				}
			}

			//build all value, nb epic, nb task of prj, nb child of datacenter
			Map<Integer, Double[]> mapValues 	= new HashMap<Integer, Double[]>();
			for(TaPrjProject p : lstPrj) {
				mapGet.put(p.reqId(), p);

				Double 	sizeDoc = mapSize.get(p.reqId());
				Integer parent 	= (Integer) p.req(TaPrjProject.ATT_I_PARENT);
				Integer typ00 	= (Integer) p.req(TaPrjProject.ATT_I_TYPE_00);

				if(typ00 	== null)	continue;
				if(sizeDoc == null) 	sizeDoc = 0.0;

				if(typ00 == TaPrjProject.TYP00_PRJ_PROJECT) {
					if(parent == null) {
						prjNormal.add(p);
						mapValues.put(p.reqId(), new Double[] {0.0, 0.0, sizeDoc});
					} else {
						Integer 	typ02 	= (Integer) p.req(TaPrjProject.ATT_I_TYPE_02);
						Double[] 	vals 	= mapValues.get(parent);

						if (vals!=null) {
							vals[2] += sizeDoc;
							if(typ02.equals(TaPrjProject.TYP02_PRJ_SUB))
								vals[0] += 1;
							else if(typ02.equals(TaPrjProject.TYP02_PRJ_ELE))	
								vals[1] += 1;
						}
						//---order aldready the parent in first, then map has parent id, if no, do nothing
					}
				} else if(typ00 == TaPrjProject.TYP00_PRJ_DATACENTER) {
					if(parent == null) {
						prjDataCenter.add(p);
						mapValues.put(p.reqId(), new Double[] {0.0, sizeDoc});
					}else {
						Double[] vals = mapValues.get(parent);
						if (vals!=null) {
							vals[0] += 1;
							vals[1] += sizeDoc;
						}
					}
				}				
			}

			//Map project
			for(TaPrjProject p : prjNormal) {
				p.reqSet(TaPrjProject.ATT_O_NB_EPIC_TASK_SIZE_DOC, mapValues.get(p.reqId()));
			}

			//Map datacenter
			for(TaPrjProject p : prjDataCenter) {
				p.reqSet(TaPrjProject.ATT_O_NB_CHILD_SIZE_DOC, mapValues.get(p.reqId()));
			}
		}

		//get list user
//		Set<Integer> setTyp = new HashSet<Integer>();
//		setTyp.add(TaAutUser.TYPE_ADM);
//		setTyp.add(TaAutUser.TYPE_EMP);
//		setTyp.add(TaAutUser.TYPE_CLIENT);
//		setTyp.add(TaAutUser.TYPE_CLIENT_PUBLIC);
//		setTyp.add(TaAutUser.TYPE_MENTOR);
//		setTyp.add(TaAutUser.TYPE_SHIPPER);
//		setTyp.add(TaAutUser.TYPE_ADM_ALL);
//
//		Set<Integer> setStat = new HashSet<Integer>();
//		setStat.add(TaAutUser.STAT_ACTIVE);
//		setStat.add(TaAutUser.STAT_WAITING);
//		setStat.add(TaAutUser.STAT_DELETED);

//		List<TaAutUser> 	lstUser = TaAutUser.DAO.reqList(
//				Restrictions.eq(TaAutUser.ATT_I_PER_MANAGER	, id),
//				Restrictions.in(TaAutUser.ATT_I_TYPE			, setTyp),
//				Restrictions.in(TaAutUser.ATT_I_STATUS		, setStat)
//				);


		List<TaAutUser> 	lstUser = TaAutUser.DAO.reqList(	Restrictions.eq(TaAutUser.ATT_I_PER_MANAGER	, id));
		if(lstUser != null && lstUser.size() > 0) {
			Set<Integer> 			setPer 	= ToolSet.reqSetInt(lstUser, TaAutUser.ATT_I_PER_PERSON);
			Map<Integer, Double> 	mapSize = new HashMap<Integer, Double>();
			List<TaTpyDocument> 	lstDoc 	= TaTpyDocument.reqTpyDocuments(DefDBExt.ID_TA_PER_PERSON, setPer, null, null);

			if(lstDoc != null && lstDoc.size() > 0) {
				for(TaTpyDocument d: lstDoc) {
					Integer perId 	= (Integer) d.req(TaTpyDocument.ATT_I_ENTITY_ID);
					Double 	size 	= d.reqDouble(d, TaTpyDocument.ATT_F_VAL_01);
					if(size == null)	size = 0.0;

					if(mapSize.containsKey(perId)) {
						mapSize.put(perId, mapSize.get(perId) + size);
					}else {
						mapSize.put(perId, size);
					}
				}
			}

			//Map User
			for(TaAutUser u : lstUser) {
				u.doHidePwd();
//				u.reqSet(TaAutUser.ATT_O_SIZE_DOC, mapSize.get(u.req(TaAutUser.ATT_I_PER_PERSON)));
			}
		}

		PartnerProjectResume result = new PartnerProjectResume(prjNormal, prjDataCenter, lstUser);

		return result;
	}
	 */
	//---------------------------------------------------------------------------------------------------
	/*static String sql = "select doc.* " 
			+ " from " 						+ DefDBExt.TA_PRJ_PROJECT 	+ " as prj "
			+    			" inner join " 	+ DefDBExt.TA_TPY_DOCUMENT 	+ " as doc "
			+    			" on prj." + TaPrjProject.COL_I_ID + " = doc." + TaTpyDocument.COL_I_PARENT_ID 
			+	 "where prj.I_Group in ("
			+	 "select p.I_Group from TA_PRJ_PROJECT as p "
			+ 	 "inner join .TA_TPY_RELATIONSHIP as r "
			+	 "on p.I_ID = r.I_Entity_ID_01 "
			+	 "where r.I_Entity_ID_02 = " + user.reqId()
			+	 " and p.I_Type_02 = 0" 
			+ 	 " and p.I_Type_00 = 1)" 
			+ 	 " and doc.T_Name LIKE '%" + searchName + "%'"
	;*/

	public static List<TaTpyDocument> reqTpyDoc(TaAutUser user, Integer grpId,  Integer prjId,  String searchName) throws Exception {
		Hashtable<Object, Object> tabPrj = reqPrjDataTree(user, grpId);
		if (tabPrj == null) return null;
		Set<Integer> ids = new HashSet<Integer>();
		doBuildPrjIds (ids, tabPrj, prjId, user.reqId());

		if (ids.size()==0) return null;

		List<TaTpyDocument> lstDocs		= TaTpyDocument.reqTpyDocuments (DefDBExt.ID_TA_PRJ_PROJECT, ids, Restrictions.ilike(TaTpyDocument.ATT_T_INFO_01, searchName));

		return lstDocs;
	}

	private static CacheData<Hashtable<Object, Object>> 	cache_prjData_tree= new CacheData<Hashtable<Object, Object>> (500, DefTime.TIME_00_10_00_000);
	private static void doSearchFile(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//		Integer 			objId	= ToolData.reqInt	(json, "id"				, -1	);				
		//		Boolean				forced	= ToolData.reqBool	(json, "forced"			, false	);

		Integer 			prjId	= ToolData.reqInt	(json, "prjId"			, -1);	
		Integer 			grpId	= ToolData.reqInt	(json, "grpId"			, -1);
		String 				name	= ToolData.reqStr	(json, "searchName"		, "");
		name	= "%" + name.trim().replace(" ", "%") + "%";

		List<TaTpyDocument> lstDoc = reqTpyDoc(user, grpId, prjId, name);

		if (lstDoc == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, lstDoc 
				));
	}


	private static Hashtable<Object, Object> reqPrjDataTree (TaAutUser user, Integer grpId) throws Exception{
		String 								key		= grpId+"";
		Hashtable 							tabPrj 	= cache_prjData_tree.reqData(key);	
		if (tabPrj==null){
			TaPrjProject prj = TaPrjProject.DAO.reqEntityByRef(grpId);
			if(prj == null || !prj.req(TaPrjProject.ATT_I_PER_MANAGER).equals(user.reqPerManagerId())) {
				return null;
			}
			List<TaPrjProject>	lstPrj 	= TaPrjProject.DAO.reqList(Restrictions.eq(TaPrjProject.ATT_I_GROUP, grpId));
			Set<Integer> 		ids 	= ToolSet.reqSetInt(lstPrj, TaPrjProject.ATT_I_ID);

			List<TaTpyRelationship> list 		= TaTpyRelationship.DAO.reqList_In(TaTpyRelationship.ATT_I_ENTITY_ID_01, ids, 
					Restrictions.and(
							Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT), 
							Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER)
							));

			Map<Integer, Set<Integer>> mapMember = new HashMap<Integer,  Set<Integer>>();
			if(list != null && list.size() > 0) {
				for(TaTpyRelationship m : list) {
					Integer idPrj = (Integer) m.req(TaTpyRelationship.ATT_I_ENTITY_ID_01);
					Set<Integer> lst = mapMember.get(idPrj);
					if (lst == null) {
						lst = new HashSet<Integer>();
						mapMember.put(idPrj, lst);
					}
					lst.add(m.reqInt(m, TaTpyRelationship.ATT_I_ENTITY_ID_02));
				}
			}
			//---build children + member
			tabPrj = ToolDBEntity.reqTabKeyInt(lstPrj, TaPrjProject.ATT_I_ID);
			for(TaPrjProject p: lstPrj) {
				Set<Integer> listMem = mapMember.get(p.reqId());
				if(listMem != null && listMem.size() > 0) {
					List<Integer> listMemArr = new ArrayList<>(listMem);
					p.reqSet(TaPrjProject.ATT_O_MEMBERS, listMemArr);
				}
				Integer parentId = (Integer) p.req(TaPrjProject.ATT_I_PARENT);
				if (parentId!=null) {
					TaPrjProject pParent = (TaPrjProject) tabPrj.get(parentId);
					if (pParent !=null)
						pParent.doAddChild(p);
				}
			}

			cache_prjData_tree.reqPut(key, tabPrj);
		}

		return tabPrj;
	}

	private static void doBuildPrjIds (Set<Integer>  ids, Hashtable<Object, Object> tabPrj, Integer pId, Integer userId){
		TaPrjProject parent = (TaPrjProject) tabPrj.get(pId);
		if (parent!=null) {
			List<Integer> list = (List<Integer>) parent.req(TaPrjProject.ATT_O_MEMBERS);
			if(list == null) return;
			Set<Integer> members = new HashSet<>(list);
			/*
			 * Set<Integer> members = (Set<Integer>) parent.req(TaPrjProject.ATT_O_MEMBERS);
			 */
			if (!members.contains(userId)) return;

			ids.add(pId);
			List<TaPrjProject> children =  (List<TaPrjProject>) parent.req(TaPrjProject.ATT_O_EPICS);
			if (children!=null && children.size()>0) {
				for (TaPrjProject child : children) {
					doBuildPrjIds(ids, tabPrj, child.reqId(), userId);
				}
			}
		}
	}

	//---------------------------------------------------------------------------------------------------------
	private static void doGetHistoryTask(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");

		Integer 			entId	= ToolData.reqInt	(json, "id"		, -1	);		
		int 				typ		= ToolData.reqInt	(json, "typ"		, -1	);	

		TaTpyInformation hist = TaTpyInformation.DAO.reqEntityByValues(
				TaTpyInformation.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_PRJ_PROJECT, 
				TaTpyInformation.ATT_I_ENTITY_ID	, entId,
				TaTpyInformation.ATT_I_TYPE_01		, TaTpyInformation.TYP_01_HISTO
				);
		if (hist==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, hist
				));
	}

	private static void doCalculPercentSprint(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");

		Integer 		prjId	    = ToolData.reqInt	(json, "prjId"			, -1);	

		TaPrjProject  		ent	 	= TaPrjProject.DAO.reqEntityByRef(prjId);

		if(ent == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		String desrc02 = (String) ent.req(TaPrjProject.ATT_T_INFO_02);
		if(desrc02 == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		JSONObject task_ids = ToolJSON.reqJSonFromString(desrc02);
		JSONArray  ids      = (JSONArray) task_ids.get("task_ids");

		Set<Integer> setIds = new HashSet<Integer>();

		for(int i=0; i< ids.size(); i++) {
			setIds.add((int)(long) ids.get(i));
		}

		List<TaPrjProject> lstDone = new ArrayList<TaPrjProject>();
		List<TaPrjProject> lstAll  = new ArrayList<TaPrjProject>();

		lstAll = TaPrjProject.DAO.reqList_In(TaPrjProject.ATT_I_ID, setIds, Restrictions.eq(TaPrjProject.ATT_I_TYPE_02, TaPrjProject.TYP_02_PRJ_ELE));

		for(TaPrjProject prj: lstAll) {
			Integer stat = (Integer) prj.req(TaPrjProject.ATT_I_STATUS_01);
			if(stat == TaPrjProject.STAT_01_PRJ_DONE) {
				lstDone.add(prj);
			}
		}

		Integer numDone = lstDone.size();
		Integer numAll  = lstAll.size();

		Double percent  = (double) (numDone * 100/ numAll);

		ent.reqSet(TaPrjProject.ATT_F_VAL_05, percent);

		TaPrjProject.DAO.doMerge(ent);

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, percent
				));		
	}

	private static void doGetWorkflowByTask(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");

		Integer 			objId	= ToolData.reqInt	(json, "id"		, -1	);	
		Integer 			grpId	= ToolData.reqInt	(json, "grId"	, null	);	
		Boolean				forced	= ToolData.reqBool	(json, "forced"	, false	);

		//--get wF ------
		TaPrjProject 		ent 	= reqPrjWorkflowGet (grpId, objId);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
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

	//-----------------------------------------------------------------------
	private static String sqlWF = "select p.* from " 
			+ DefDBExt.TA_PRJ_PROJECT + " p inner join "  	 + DefDBExt.TA_TPY_RELATIONSHIP + " r on "
			+ " r." + TaTpyRelationship.COL_I_ENTITY_ID_01   + "= p." + TaPrjProject.COL_I_ID  		+ " and " 
			+ " r." + TaTpyRelationship.COL_I_ENTITY_TYPE_01 + "=" + DefDBExt.ID_TA_PRJ_WORKFLOW	+ " and " 

			+ "(( r." + TaTpyRelationship.COL_I_ENTITY_TYPE_02 + "=" + DefDBExt.ID_TA_PRJ_PROJECT	+ " and " 
			+ "  r." + TaTpyRelationship.COL_I_ENTITY_ID_02  + "= %d)"

			+ "or " 

			+ "( r." + TaTpyRelationship.COL_I_ENTITY_TYPE_02 + "=" + DefDBExt.ID_TA_PRJ_PROJECT_TASK	+ " and " 
			+ "  r." + TaTpyRelationship.COL_I_ENTITY_ID_02  + "= %d ))"

			+ " order by p." + TaPrjProject.COL_I_ID + "  desc ";

	private static TaPrjProject reqPrjWorkflowGet(Integer prjId, Integer taskId) throws Exception{
		String sql = String.format(sqlWF, prjId, taskId);
		List<TaPrjProject> 		ent 	= TaPrjProject.DAO.reqList(sql);
		if(ent == null || ent.isEmpty())	return null;
		return ent.get(0);
	}

	private static void doSaveHistory(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		TaPrjProject 			ent		= reqPrjProjectSaveHistory		(user, json, response);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent
				));		
	}

	private static TaPrjProject reqPrjProjectSaveHistory(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception { 		

		JSONObject			obj		= ToolData.reqJson	(json, "obj", new JSONObject());

		Integer 			prjID 	= ((Long) (obj.get("parId"))).intValue();

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaTpyInformation.class);
		if(prjID == null)	return null;

		TaPrjProject  		ent	 	= TaPrjProject.DAO.reqEntityByRef(prjID);
		if(ent == null)	return null;

		String strIds  = ent.reqStr(TaPrjProject.ATT_T_INFO_02);
		if(strIds == null) return null;
		JSONObject ids 		= ToolJSON.reqJSonObjectFromString(strIds);
		JSONArray  task_ids = (JSONArray) ids.get("task_ids");

		Set<Integer> setIds = new HashSet<Integer>();
		for(int i=0; i< task_ids.size(); i++) {
			setIds.add((int)(long) task_ids.get(i));
		}

		List<TaPrjProject> lstTest = TaPrjProject.DAO.reqList_In(TaPrjProject.ATT_I_ID, setIds, 
				Restrictions.eq(TaPrjProject.ATT_I_TYPE_00	, TaPrjProject.TYP_00_PRJ_TEST),
				Restrictions.eq(TaPrjProject.ATT_I_TYPE_01	, TaPrjProject.TYP_01_TEST_UNIT));
		if (lstTest == null || lstTest.size() == 0) return null;

		Date now = new Date();

		TaTpyInformation re 		= new TaTpyInformation(attr);
		// INFO 01
		JSONObject info01 = new JSONObject();
		info01.put("dt1", ToolDate.reqStringFromDate(now, "yyyy-MM-dd HH:ss"));
		info01.put("dt2", ToolDate.reqStringFromDate(now, "yyyy-MM-dd HH:ss"));
		info01.put("stat", TaPrjProject.STAT_01_TEST_UNIT_TODO);

		JSONObject userObj = new JSONObject();
		userObj.put("id", user.reqId());
		userObj.put("login", user.req(TaAutUser.ATT_T_LOGIN_01));
		info01.put("autUser", userObj);
		re.reqSet(TaTpyInformation.ATT_T_INFO_01, info01.toString());
		// INFO 02
		JSONArray info02 = new JSONArray();
		for (TaPrjProject e : lstTest) {
			JSONObject tmp = new JSONObject();
			tmp.put("id", e.req(TaPrjProject.ATT_I_ID));
			tmp.put("code", e.req(TaPrjProject.ATT_T_CODE_01));
			tmp.put("name", e.req(TaPrjProject.ATT_T_NAME));
			tmp.put("desc", e.req(TaPrjProject.ATT_T_INFO_01));
			tmp.put("cmt", "");
			tmp.put("stat", TaPrjProject.STAT_01_TEST_UNIT_TODO);
			tmp.put("dt", now.toString());
			info02.add(tmp);
		}
		re.reqSet(TaTpyInformation.ATT_T_INFO_02, info02.toString());
		TaTpyInformation.DAO.doPersist(re);

		ent.doBuildTestGroup(true);
		ent.doBuildTestGroupHistory(true);
		return ent;
	}

	private static void doModHistory(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ServerLogTool.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");
		List<TaTpyInformation> 			ent		= reqPrjProjectModHistory		(user, json, response);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,  
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent
				));		
	}

	private static List<TaTpyInformation> reqPrjProjectModHistory(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception { 		

		JSONArray			obj		= ToolData.reqJsonArr	(json, "obj", new JSONArray());

		Set<Integer> 		setUids = new HashSet<Integer>();

		if (obj == null || obj.size() == 0) return null;
		for (int i = 0; i < obj.size(); i++) {
			JSONObject o = (JSONObject) obj.get(i);
			setUids.add((int)(long) o.get("id"));
		}
		Date now = new Date();
		List<TaTpyInformation> lstTest = TaTpyInformation.DAO.reqList_In(TaTpyInformation.ATT_I_ID, setUids);
		if (lstTest == null || lstTest.size() == 0) return null;
		for (int i = 0; i < obj.size(); i++) {
			for (int j = 0; j < lstTest.size(); j++) {
				JSONObject 			o 	 = (JSONObject) obj.get(i);
				Integer 			oId  = ((Long) o.get("id")).intValue();
				TaTpyInformation 	ent  = lstTest.get(j);

				if (oId == (int)ent.req(TaTpyInformation.ATT_I_ID)) {
					JSONObject		info01	= (JSONObject) o.get("info01");
					JSONArray		info02	= (JSONArray) o.get("info02");

					info01.put("dt2", ToolDate.reqStringFromDate(now, "yyyy-MM-dd HH:ss"));
					ent.reqSet(TaTpyInformation.ATT_T_INFO_01, info01.toString());
					// INFO 02
					ent.reqSet(TaTpyInformation.ATT_T_INFO_02, info02.toString());

					TaTpyInformation.DAO.doMerge(ent);
					break;
				}
			}
		}

		return lstTest;
	}



	private static void doCalcSLA(TaPrjProject ent) throws Exception {
		Double 	res 	= 0.0;
		Integer stat  	=  (Integer) ent.req(TaPrjProject.ATT_I_STATUS_01);
		Integer entId 	=  (Integer) ent.req(TaPrjProject.ATT_I_ID);
		Double 	val04 	= 0.0;

		TaTpyInformation hist = TaTpyInformation.DAO.reqEntityByValues(
				TaTpyInformation.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_PRJ_PROJECT, 
				TaTpyInformation.ATT_I_ENTITY_ID	, entId,
				TaTpyInformation.ATT_I_TYPE_01		, TaTpyInformation.TYP_01_HISTO
				);
		String 			val 		= hist.reqStr(hist, TaTpyInformation.ATT_T_INFO_01);
		JSONArray		lstHistory 	= ToolJSON.reqJSonArrayFromString(val);

		for (int i = 1; i < lstHistory.size(); i++) {
			JSONObject e1 	= (JSONObject) lstHistory.get(i);
			JSONObject e2 	= (JSONObject) lstHistory.get(i-1);

			Date d1 = ToolDate.reqDate((String) e1.get("dt"));
			Date d2 = ToolDate.reqDate((String) e2.get("dt"));

			Integer stat1 = e1.get("stat") != null ? ((Long) e1.get("stat")).intValue() : null;
			Integer stat2 = e2.get("stat") != null ? ((Long) e2.get("stat")).intValue() : null;

			if (stat1 != null && stat2 != null && stat1 != stat2 && stat2 != TaPrjProject.STAT_01_PRJ_NEW) {
				res += d1.getTime() - d2.getTime();
			}			
		}

		ent.reqSet(TaPrjProject.ATT_F_VAL_06, res/60000);
		TaPrjProject.DAO.doMerge(ent);
	}

}
