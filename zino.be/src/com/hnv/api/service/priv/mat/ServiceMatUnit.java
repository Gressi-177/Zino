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
import com.hnv.db.mat.TaMatPrice;
import com.hnv.db.mat.TaMatUnit;
import com.hnv.db.sys.TaSysLock;
import com.hnv.def.DefDBExt;

public class ServiceMatUnit implements IService {
	public static final Integer	ENT_TYP	= DefDBExt.ID_TA_MAT_UNIT;
	
	public static String SV_CLASS 		= "ServiceMatUnit".toLowerCase();

	public static String SV_GET 		= "SVGet".toLowerCase();
	public static String SV_LST 		= "SVLst".toLowerCase();
	public static String SV_LST_DYN 	= "SVLstDyn".toLowerCase();

	public static String SV_SEARCH 		= "SVSearch".toLowerCase();
	
	public static String SV_NEW 		= "SVNew".toLowerCase();
	public static String SV_MOD 		= "SVMod".toLowerCase();
	public static String SV_DEL 		= "SVDel".toLowerCase();

	public static String SV_LCK_REQ 	= "SVLckReq".toLowerCase(); //req or refresh	
	public static String SV_LCK_SAV 	= "SVLckSav".toLowerCase(); //save and continue
	public static String SV_LCK_END 	= "SVLckEnd".toLowerCase(); //save and end
	public static String SV_LCK_DEL 	= "SVLckDel".toLowerCase();

	
	public static String SV_NEW_ORDER 	= "SVNewOrder".toLowerCase();

	public ServiceMatUnit() {
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {
			if (sv.equals(SV_GET) 			&& APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doGet(user, json, response);
			} else if (sv.equals(SV_LST)	&& APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doLst(user, json, response);

			} else if (sv.equals(SV_LST_DYN) && APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doLstDyn(user, json, response);

			} else if (sv.equals(SV_NEW)	&& APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doNew(user, json, response);
			} else if (sv.equals(SV_MOD)	&& APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doMod(user, json, response);
			} else if (sv.equals(SV_DEL)	&& APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doDel(user, json, response);


			} else if (sv.equals(SV_LCK_REQ) && APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doLckReq(user, json, response);
			} else if (sv.equals(SV_LCK_SAV) && APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doLckSav(user, json, response);
			} else if (sv.equals(SV_LCK_END) && APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doLckEnd(user, json, response);
			} else if (sv.equals(SV_LCK_DEL) && APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doLckDel(user, json, response);

			} else if (sv.equals(SV_SEARCH)) {
				doSearch(user, json, response);
			} else if (sv.equals(SV_NEW_ORDER)) {
				doNewOrder(user, json, response);

			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}
		} catch (Exception e) {
			API.doResponse(response, DefAPI.API_MSG_ERR_API);
			e.printStackTrace();
		}
	}

	// -- GET --
	private void doGet(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		Integer 	objId	= ToolData.reqInt	(json, "id"		, -1	);	
		TaMatUnit 	ent 	= TaMatUnit.DAO.reqEntityByRef(objId);

		if (ent == null) {
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA	, ent));
	}

	// -- LST --
	private static void doLst(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		List<TaMatUnit> list = TaMatUnit.DAO.reqList(Order.asc(TaMatUnit.ATT_T_NAME), 
				Restrictions.eq(TaMatUnit.ATT_I_STATUS, TaMatPrice.STAT_ACTIV),
				Restrictions.eq(TaMatUnit.ATT_I_PER_MANAGER, user.reqPerManagerId()));

		if (list == null) {
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA	, list));

	}

	// -- NEW --
	private void doNew(TaAutUser user,JSONObject json, HttpServletResponse response) throws Exception {
		TaMatUnit 			ent		= reqNew		(user, json);
		if (ent==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		TaSysLock 	lock 	= ToolDBLock.reqLock (json, "lock", DefDB.DB_LOCK_NEW, ENT_TYP, (Integer)ent.req(TaMatUnit.ATT_I_ID), user.reqId(), user.reqStr(TaAutUser.ATT_T_LOGIN_01), null);
		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent,
				"lock"				, lock
				));
	}

	private static TaMatUnit reqNew(TaAutUser user, JSONObject json) throws Exception {
		JSONObject		obj			= ToolData.reqJson (json, "obj", null);
		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaMatUnit.class);	

		TaMatUnit  		ent	 		= new TaMatUnit(attr);
		Integer 		manId 		= user.reqPerManagerId();
		ent.reqSet(TaMatUnit.ATT_I_PER_MANAGER, manId);
		
		TaMatUnit.DAO.doPersist(ent);

		return ent;
	}
	
	// -- MOD --
	private void doMod(TaAutUser user,JSONObject json, HttpServletResponse response) throws Exception {
		TaMatUnit  		ent	 	=  reqMod(user, json); 								
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
	
	private static TaMatUnit reqMod(TaAutUser user, JSONObject json) throws Exception {
		JSONObject			obj		= ToolData.reqJson (json, "obj"	, null);
		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaMatUnit.class);
		
		int				entId 		= ToolData.reqInt(obj, "id", null);
		TaMatUnit 		ent 		= TaMatUnit.DAO.reqEntityByRef(entId);
		if (ent==null){
			return null;
		}

		Integer manId = user.reqPerManagerId();

		if (ent==null){
			return null;
		}

		ent.reqSet(TaMatUnit.ATT_I_PER_MANAGER, manId);
		TaMatUnit.DAO.doMerge(ent, attr);
		return ent;
	}

	// -- DEL
	private void doDel(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		// -- lock --
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
		Integer 	entId	= ToolData.reqInt	(json, "id", null	);	
		TaMatUnit  	ent	 	= TaMatUnit.DAO.reqEntityByRef(entId);
		if (ent==null){
			return false;
		}

		TaMatUnit.DAO.doRemove(ent);
		return true;
	}

	//----------------------------------------------------------------------------------------
	private static Hashtable<String,Integer> mapCol = new Hashtable<String, Integer>(){
		{
			put("action", -1);
			put("id"	, 0 );
			put("name"	, 1 );
			put("code"	, 2 );
			put("stat"	, 3 );
		}
	};

	private static void doLstDyn(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {	
		Object[]  			dataTableOption = ToolDatatable.reqDataTableOption (json, null);
		Set<String>			searchKey		= (Set<String>)dataTableOption[0];	
		Set<Integer>		stat			= ToolData.reqSetInt(json, "stat"	, null);
		Set<Integer>		typ				= ToolData.reqSetInt(json, "typ"	, null);
		
		if(typ == null && stat ==null) {
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
		
		Integer 			manId 			= user.reqPerManagerId();
		Criterion 			cri 			= reqRestriction(user, searchKey,  stat, typ);				

		List<TaMatUnit> 	unitList 		= reqListDyn(dataTableOption, cri);
		if (unitList==null ){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		Integer iTotalRecords 			= reqNbListDyn(manId).intValue();				
		Integer iTotalDisplayRecords 	= reqNbListDyn(cri).intValue();


		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT				, 1, 
				DefJS.SV_CODE				, DefAPI.SV_CODE_API_YES,					
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, unitList
				));
	}


	private static List<TaMatUnit> reqListDyn(Object[] dataTableOption, Criterion 	cri) throws Exception {		
		int 		begin 		= (int)	dataTableOption[1];
		int 		number 		= (int)	dataTableOption[2]; 
		int 		sortCol 	= (int)	dataTableOption[3]; 
		int 		sortTyp 	= (int)	dataTableOption[4];	

		List<TaMatUnit> list 	= new ArrayList<TaMatUnit>();		

		Order order = null;			
		String colName = null;

		switch(sortCol){
		case 0: colName = TaMatUnit.ATT_I_ID		; break;
		case 1: colName = TaMatUnit.ATT_T_CODE		; break;
		case 2: colName = TaMatUnit.ATT_T_NAME		; break;		
		case 3: colName = TaMatUnit.ATT_I_STATUS	; break;		
		}

		if (colName!=null){
			switch(sortTyp){
			case 0: order = Order.asc(colName); break;
			case 1: order = Order.desc(colName); break;								
			}
		}

		if (order==null)
			list	= TaMatUnit.DAO.reqList(begin, number, cri);
		else
			list	= TaMatUnit.DAO.reqList(begin, number, order, cri);			

		return list;
	}

	private static Number reqNbListDyn(Integer manId) throws Exception {	
		if (manId!=null){
			Criterion cri = Restrictions.eq(TaMatUnit.ATT_I_PER_MANAGER, manId);
			return TaMatUnit.DAO.reqCount(cri);		
		}
		return TaMatUnit.DAO.reqCount();	
	}

	private static Number reqNbListDyn(Criterion cri) throws Exception {			
		return TaMatUnit.DAO.reqCount(cri);
	}

	private static Criterion reqRestriction(TaAutUser user, Set<String> searchKey, Set<Integer> status, Set<Integer> typ) throws Exception {		
		
		Criterion cri = Restrictions.eq(TaMatUnit.ATT_I_PER_MANAGER, user.reqPerManagerId());
		if (status!=null)cri = Restrictions.and(cri, Restrictions.in(TaMatUnit.ATT_I_STATUS, status));

		for (String s : searchKey){
			cri = 	Restrictions.and(	cri, 
					Restrictions.or(
							Restrictions.ilike(TaMatUnit.ATT_T_CODE		, s), 
							Restrictions.ilike(TaMatUnit.ATT_T_NAME		, s)
							));
		}				

		return cri;
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

		TaMatUnit  		ent	 	=  reqMod(user, json); 								
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


		TaMatUnit ent = reqMod(user, json);						
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

	//------------sv_lock----------------


	//----------search------------
	private void doSearch(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		String 	searchkey	= json.get("searchkey").toString();
		Integer nbLine		= json.get("nbLine") != null && json.get("nbLine").toString() != "" ? ((Long) json.get("nbLine")).intValue() : null;

		Integer manId		= json.get("manId") != null && json.get("manId").toString() != "" ? ((Long) json.get("manId")).intValue() : null;
		Integer stat		= json.get("typ01") != null && json.get("typ01").toString() != "" ? ((Long) json.get("typ01")).intValue() : null;

		List<TaMatUnit> lstResult = reqSearch(user, manId, stat, searchkey, nbLine);

		API.doResponse(response, ToolJSON.reqJSonString(	
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, lstResult
				));
	}

	private static List<TaMatUnit> reqSearch(TaAutUser user, Integer manId, Integer stat, String searchkey, Integer nbLine) throws Exception {
		Criterion cri = Restrictions.gt(TaMatUnit.ATT_I_ID, 0);

		if (manId!=null) {
			cri = Restrictions.and(cri, Restrictions.eq(TaMatUnit.ATT_I_PER_MANAGER, manId));			
		}
		if(stat != null){
			cri = Restrictions.and(cri, Restrictions.eq(TaMatUnit.ATT_I_STATUS, stat));			
		} 

		if (searchkey!=null && searchkey.length()>0) {
			cri = 	Restrictions.and(	cri, 
					Restrictions.or(							
							Restrictions.like(TaMatUnit.ATT_T_NAME		, "%"+searchkey+"%"),
							Restrictions.like(TaMatUnit.ATT_T_CODE		, "%"+searchkey+"%")
							));
		}


		if ( nbLine == null || nbLine <= 0) nbLine = 10;

		return TaMatUnit.DAO.reqList(0, nbLine, Order.asc(TaMatUnit.ATT_T_NAME), cri);
	}

	private void doNewOrder(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		String 			str 	= (String) json.get("obj");
		JSONObject 		obj 	= ToolJSON.reqJSonFromString(str);

		Map<String, Object> attr = API.reqMapParamsByClass(obj, TaMatUnit.class);

		TaMatUnit matUnit = new TaMatUnit(attr);

		//			Integer manId = user.reqPerManagerId();
		//			Integer stat	= obj.get("stat") != null && obj.get("stat").toString() != "" ? ((Long) obj.get("stat")).intValue() : 1;
		//
		//			ent.reqSet(TaMatUnit.ATT_I_PER_MANAGER, manId);
		//			ent.reqSet(TaMatUnit.ATT_I_STATUS, stat);
		//
		TaMatUnit.DAO.doPersist(matUnit);

		// Add matPrice
		if (obj.get("pricing") != null) {
			JSONObject pricing = (JSONObject)obj.get("pricing");
			TaMatPrice matPrice = new TaMatPrice();

			matPrice.reqSet(TaMatPrice.ATT_I_MAT_MATERIAL, 	obj.get("matId") != null ? (int)obj.get("matId") : -1);
			matPrice.reqSet(TaMatPrice.ATT_T_INFO_01, 		obj.get("name").toString() != null ? obj.get("name").toString() : "");
			matPrice.reqSet(TaMatPrice.ATT_F_VAL_00, 		obj.get("ratio") != null ? (double)obj.get("ratio") : 0);
			matPrice.reqSet(TaMatPrice.ATT_F_VAL_03, 		pricing.get("taxPrice") != null ? (double)pricing.get("taxPrice") : 0);
			matPrice.reqSet(TaMatPrice.ATT_F_VAL_01, 		pricing.get("price01") != null ? (double)pricing.get("price01") : 0);
			matPrice.reqSet(TaMatPrice.ATT_F_VAL_02, 		pricing.get("price02") != null ? (double)pricing.get("price02") : 0);


			TaMatPrice.DAO.doPersist(matPrice);
		}
		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA, matUnit));

	}
}
