package com.hnv.api.service.priv.mat;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hnv.api.def.DefAPI;
import com.hnv.api.def.DefDB;
import com.hnv.api.def.DefJS;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.api.service.common.APIAuth;
import com.hnv.common.tool.ToolDBLock;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolDatatable;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.mat.TaMatUnit;
import com.hnv.db.mat.TaMatWarehouse;
import com.hnv.db.nso.TaNsoPost;
import com.hnv.db.sys.TaSysLock;
import com.hnv.def.DefDBExt;
import com.hnv.def.DefRight;

public class ServiceMatWareHouse implements IService {
	public static final String SV_CLASS 	= "ServiceMatWarehouse".toLowerCase();

	public static final String SV_GET 		= "SVGet".toLowerCase();
	public static final String SV_LST 		= "SVLst".toLowerCase();
	public static final String SV_LST_DYN  	= "SVLstDyn".toLowerCase();

	public static final String SV_NEW 		= "SVNew".toLowerCase();
	public static final String SV_MOD 		= "SVMod".toLowerCase();
	public static final String SV_DEL 		= "SVDel".toLowerCase();

	public static String SV_LCK_REQ 		= "SVLckReq".toLowerCase(); //req or refresh	
	public static String SV_LCK_SAV 		= "SVLckSav".toLowerCase(); //save and continue
	public static String SV_LCK_END 		= "SVLckEnd".toLowerCase(); //save and end
	public static String SV_LCK_DEL 		= "SVLckDel".toLowerCase();

	public static final Integer	ENT_TYP		= DefDBExt.ID_TA_MAT_WAREHOUSE;

	public ServiceMatWareHouse() {
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	//----------------------------------------------------------------------
	//   private readonly DBContext dbContext;
	//   public ServiceMatWarehouse(DBContext context) {
	//       dbContext = context;
	//       initHashTable();  
	//   }
	//
	//   [HttpPost]
	//   public ActionResult<APIResponse> reqService(JObject req) {
	//       TaAutUser user = (TaAutUser)this.HttpContext.Items["userInfo"];
	//       return reqServiceName(Request, req, dbContext, user);
	//   }
	//-----------------------------------------------------------------------
	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {
			// mapping service
			if (sv.equals(SV_GET) && APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doGet(user, json, response);
			}else if (sv.equals(SV_LST) && APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doLst(user, json, response);
			} else if (sv.equals(SV_LST_DYN) && APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doLstDyn(user, json, response);

			} else if (sv.equals(SV_NEW) && APIAuth.canAuthorize(user, SV_CLASS, sv) ) {
				doNew(user, json, response);
			} else if (sv.equals(SV_MOD) && APIAuth.canAuthorize(user, SV_CLASS, sv) ) {
				doMod(user, json, response);
			} else if (sv.equals(SV_DEL) && APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doDel(user, json, response);

			}else if (sv.equals(SV_LCK_REQ) && APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doLckReq(user, json, response);
			} else if (sv.equals(SV_LCK_SAV) && APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doLckSav(user, json, response);
			} else if (sv.equals(SV_LCK_END) && APIAuth.canAuthorize(user, SV_CLASS, sv)) { //-- finish mode MOD
				doLckEnd(user, json, response);
			} else if (sv.equals(SV_LCK_DEL) && APIAuth.canAuthorize(user, SV_CLASS, sv)) { //-- finish mode MOD
				doLckDel(user, json, response);


			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}
		} catch (Exception e) {
			API.doResponse(response, DefAPI.API_MSG_ERR_API);
			e.printStackTrace();
		}
	}

	private static void doGet(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		Integer 		objId		 = ToolData.reqInt	(json, "id"		, -1	);	
		TaMatWarehouse 	ent 		= reqGet(objId);

		if (ent == null) {
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA	, ent));

	}

	private static TaMatWarehouse reqGet(Integer id) throws Exception {
		if (id == null)	return null;
		TaMatWarehouse ent = TaMatWarehouse.DAO.reqEntityByRef(id);
		return ent;
	}

	//----------------------------------------------------------------------------------------------
	private static void doLst(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");

		List<TaMatWarehouse> list = reqLst();
		if (list==null ){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, list 
				));
	}

	private static List<TaMatWarehouse> reqLst() throws Exception {
		List<TaMatWarehouse> list = TaMatWarehouse.DAO.reqList();
		return list;
	}

	//----------------------------------------------------------------------------------------------
	private static void doNew(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doNew --------------");
		TaMatWarehouse 	ent		= reqNew	(user, json);
		if (ent==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
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

	private static TaMatWarehouse reqNew(TaAutUser user, JSONObject json) throws Exception {
		JSONObject obj 				= ToolJSON.reqJSonFromString((String)json.get("obj")) ;

		if(obj == null)	return null;

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaMatWarehouse.class);

		TaMatWarehouse  ent	 		= new TaMatWarehouse(attr);
		Integer 		idMan 		= user.reqPerManagerId();		

		ent.reqSet(TaMatWarehouse.ATT_I_PER_MANAGER, idMan);
		TaMatWarehouse.DAO.doPersist(ent);
		return ent;
	}

	private static void doMod(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doMod --------------");

		TaMatWarehouse			ent		= reqMod(user, json);
		if (ent == null){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent
				));
	}

	private static TaMatWarehouse reqMod(TaAutUser user, JSONObject json) throws Exception {
		JSONObject			obj		= ToolData.reqJson (json, "obj"	, null);
		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaNsoPost.class);
		int					entId 	= ToolData.reqInt(obj, "id", null);

		TaMatWarehouse  	ent	 	= TaMatWarehouse.DAO.reqEntityByRef(entId);
		if (ent==null)	return null;

		TaMatWarehouse.DAO.doMerge(ent, attr);		
		return ent;
	}

	private static void doDel(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
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

	public static boolean canDel(TaAutUser user, JSONObject json) throws Exception {
		Integer 		entId	= ToolData.reqInt	(json, "id", null	);
		TaMatWarehouse  ent	 	= TaMatWarehouse.DAO.reqEntityByRef(entId);
		if (ent==null){
			return false;
		}
		TaMatWarehouse.DAO.doRemove(ent);		
		return true;
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
			API.doResponse(response,DefAPI.API_MSG_OK);	
		else 
			API.doResponse(response,DefAPI.API_MSG_KO);
	}

	private void doLckSav(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLckSav --------------");	
		boolean isLocked 	= ToolDBLock.canExistLock(json);
		if(!isLocked){
			API.doResponse(response, DefAPI.API_MSG_ERR_LOCK);
			return;
		}

		TaMatWarehouse  		ent	 	=  reqMod(user, json); 								
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


		TaMatWarehouse ent = reqMod(user, json);						
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

	//------------------------SV Lock---------------------------------------------------------
	private static Hashtable<String,Integer> mapCol = new Hashtable<String, Integer>(){
		{
			put("action", -1);
			put("id"	, 0 );
			put("name"	, 1 );
			put("code"	, 2 );
		}
	};

	private void doLstDyn(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		Object[]  			dataTableOption = ToolDatatable.reqDataTableOption (json, mapCol);
		Set<String>		searchKey		= (Set<String>)dataTableOption[0];	

		Integer				stat			= ToolData.reqInt	(json, "stat"	, null);
		Integer				typ				= ToolData.reqInt	(json, "type"	, null);
		Set<Integer>		typMulti		= ToolData.reqSetInt(json, "typMulti", null);

		Integer 			manId 			= user.reqPerManagerId();
		Criterion 			cri 			= reqRestriction(user, searchKey, manId);	

		List<TaMatWarehouse> lst 			= reqListDyn(dataTableOption, cri);

		if (lst == null) {
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		Integer iTotalRecords 			= reqNbListDyn(manId).intValue();				
		Integer iTotalDisplayRecords 	= reqNbListDyn(cri).intValue();

		API.doResponse(response, ToolJSON.reqJSonString(	
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				"aaData"					, lst,
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords
				));


	}
	private static Criterion reqRestriction(TaAutUser user,  Set<String> searchKey, Integer manId) {
		Criterion cri = Restrictions.gt (TaMatWarehouse.ATT_I_ID, 0);
		if (manId!=null)
			cri = Restrictions.and(cri, Restrictions.eq(TaMatWarehouse.ATT_I_PER_MANAGER, manId));

		for (String s: searchKey){	
			if (s.equals("%")) continue;
			cri = Restrictions.and(cri, Restrictions.ilike(TaMatWarehouse.ATT_T_NAME, "%" + s + "%"));
		}

		return cri;
	}

	private static List<TaMatWarehouse> reqListDyn(Object[] dataTableOption, Criterion 	cri) throws Exception {		
		int 		begin 		= (int)	dataTableOption[1];
		int 		number 		= (int)	dataTableOption[2]; 
		int 		sortCol 	= (int)	dataTableOption[3]; 
		int 		sortTyp 	= (int)	dataTableOption[4];	

		List<TaMatWarehouse> list 	= new ArrayList<TaMatWarehouse>();		

		Order order = null;			
		String colName = null;

		switch(sortCol){
		case 0: colName = TaMatWarehouse.ATT_I_ID		; break;
		case 1: colName = TaMatWarehouse.ATT_T_CODE_01	; break;
		case 2: colName = TaMatWarehouse.ATT_T_NAME		; break;	
		}

		if (colName!=null){
			switch(sortTyp){
			case 0: order = Order.asc(colName); break;
			case 1: order = Order.desc(colName); break;								
			}
		}

		if (order==null)
			list	= TaMatWarehouse.DAO.reqList(begin, number, cri);
		else
			list	= TaMatWarehouse.DAO.reqList(begin, number, order, cri);			

		return list;
	}

	private static Number reqNbListDyn(Integer manId) throws Exception {	
		if (manId!=null){
			Criterion cri = Restrictions.eq(TaMatWarehouse.ATT_I_PER_MANAGER, manId);
			return TaMatWarehouse.DAO.reqCount(cri);		
		}
		return TaMatWarehouse.DAO.reqCount();	
	}

	private static Number reqNbListDyn(Criterion cri) throws Exception {			
		return TaMatUnit.DAO.reqCount(cri);
	}

}
