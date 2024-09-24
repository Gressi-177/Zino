package com.hnv.api.service.priv.tpy;

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
import com.hnv.api.def.DefJS;
import com.hnv.api.def.DefTime;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.api.service.common.APIAuth;
import com.hnv.api.service.common.ResultPagination;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.common.tool.ToolSet;
import com.hnv.common.util.CacheData;
import com.hnv.data.json.JSONArray;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.per.TaPerPerson;
import com.hnv.db.prj.TaPrjProject;
import com.hnv.db.tpy.TaTpyFavorite;
import com.hnv.db.tpy.TaTpyRelationship;
import com.hnv.def.DefDBExt;

/**
 * ----- ServiceTpyFavorite by H&V ----- Copyright 2017------------
 */
public class ServiceTpyFavorite implements IService {
	private static String       filePath                                 = null;
	private static String       urlPath                                  = null;

	public static final Integer NEW_CONTINUE                             = 1;
	public static final Integer NEW_EXIT                                 = 2;
	// --------------------------------Service
	// Definition----------------------------------
	public static final String  SV_MODULE                                = "EC_V3".toLowerCase();

	public static final String  SV_CLASS                                 = "ServiceTpyFavorite".toLowerCase();

	public static final String  SV_GET                   = "SVGet".toLowerCase();
	
	public static final String  SV_MOD                   = "SVMod".toLowerCase();
	
	
	public static final String  SV_DEL                   = "SVDel".toLowerCase();

	public static final String  SV_LST_BY_USER_ID        = "SVLstByUserId".toLowerCase();
	public static final String  SV_LST_BY_USER           = "SVLstByUser".toLowerCase();
	public static final String  SV_LST_BY_USER_ID_TYPE   = "SVLstByUserIdAndType".toLowerCase();
	public static final String  SV_COUNT_BY_USER_ID_TYPE = "SVCountByUserIdAndType".toLowerCase();

	public static final String 	SV_NSO_NEW 				  = "SVNsoNew".toLowerCase();
	public static final String 	SV_NSO_MOD 				  = "SVNsoMod".toLowerCase();
	// -----------------------------------------------------------------------------------------------
	// -------------------------Default Constructor - Required
	// -------------------------------------
	public ServiceTpyFavorite() {
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}
	// -----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		// ToolLogServer.doLogInf("--------- "+ SV_CLASS+ ".doService --------------");
		String    sv   = API.reqSVFunctName(json);
		TaAutUser user = (TaAutUser) json.get("userInfo");
		try {
			// mapping service

			if (sv.equals(SV_GET)							) {
				doGet(user, json, response);

			}
			else if (sv.equals(SV_MOD)						) {
				doMod(user, json, response);
			}else if (sv.equals(SV_DEL)						) {
				doDel(user, json, response);

			} else if (sv.equals(SV_LST_BY_USER_ID)			) {
				doLstByUserId(user, json, response);
				
			} else if (sv.equals(SV_LST_BY_USER) 			) {
				doLstByUser(user, json, response);
			} else if (sv.equals(SV_LST_BY_USER_ID_TYPE) 	) {
				doLstByUserIdAndType(user, json, response);
			} else if (sv.equals(SV_COUNT_BY_USER_ID_TYPE)	) {
				doCountByUserIdAndType(user, json, response);

			} else if (sv.equals(SV_NSO_NEW) 				) {
				doNsoNew(user, json, response);

			} else if (sv.equals(SV_NSO_MOD)				) {
				doNsoMod(user, json, response);
			
			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}

		} catch (Exception e) {
			API.doResponse(response, DefAPI.API_MSG_ERR_API);
			return;
		}
	}
	// ---------------------------------------------------------------------------------------------------------

	private static final int WORK_GET = 1;
	private static final int WORK_LST = 2;
	private static final int WORK_NEW = 3;
	private static final int WORK_MOD = 4;
	private static final int WORK_DEL = 5;
	private static final int WORK_UPL = 10; // upload

	private static boolean canWorkWithObj(TaAutUser user, int typWork, Object... params) {
		switch (typWork) {
		case WORK_GET:
			// check something with params
			return true;
		case WORK_LST:
			// check something with params
			return true;
		case WORK_NEW:
			// check something with params
			return true;
		case WORK_MOD:
			// check something with params
			return true;
		case WORK_DEL:
			// check something with params
			return true;
		case WORK_UPL:
			// check something with params
			return true;
		}
		return false;
	}

	// ---------------------------------------------------------------------------------------------------------
	private static void doGet(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		// ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");

		Integer       objId      = ToolData.reqInt(json, "id", -1);
		Boolean       forced     = ToolData.reqBool(json, "forced", false);
		Boolean       forManager = ToolData.reqBool(json, "forManager", false);

		TaTpyFavorite ent        = reqGet(objId, forced, forManager);

		if (ent == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		API.doResponse(response,
				ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_YES, DefJS.RES_DATA, ent));
	}

	// ---------------------------------------------------------------------------------------------------------

	private static TaTpyFavorite reqGet(Integer objId, Boolean forced, boolean forManager) throws Exception {
		String        key = objId + "";
		TaTpyFavorite ent = cache_entity.reqData(key);
		if (forced || ent == null) {
			ent = TaTpyFavorite.DAO.reqEntityByRef(objId, forced);

			if (ent != null)
				cache_entity.reqPut(key, ent);
		} else {
			ToolLogServer.doLogInf("---reqGet use cache-----");
			cache_entity.reqCheckIfOld(key); // cache in 20 hour
		}

		// ---do build something other of ent like details....
		// if (ent!=null){
		// if(!forManager && (Integer)ent.req(TaTpyFavorite.ATT_I_STATUS) !=
		// TaTpyFavorite.FAVORITE_STATUS_VALIDATED) return null;
		// ent.doBuildAll(forced, forManager);
		// }

		return ent;
		

	}
	
	// ---------------------------------------------------------------------------------------------------------
	
	private static void doMod(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doMod --------------");
		TaTpyFavorite  		ent	 	=  reqMod(user, json); 								
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response, ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, ent
			));	
		}		
	}

	private static TaTpyFavorite reqMod(TaAutUser user,  JSONObject json) throws Exception {
		JSONObject			obj		= ToolData.reqJson(json, "obj"	, null);
		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaTpyFavorite.class);
		int 				entId	 	= Integer.parseInt(obj.get("id").toString());
		TaTpyFavorite 		ent 	= TaTpyFavorite.DAO.reqEntityByRef(entId);
		if (ent==null){
			return null;
		}

		if (!canWorkWithObj(user, WORK_MOD, ent)){ //other param after obj...
			return null;
		}

		TaTpyFavorite.DAO.doMerge(ent, attr);
		cache_rs.doClear();
		
		return ent;
	}

	// ---------------------------------------------------------------------------------------------------------
	
	private static void doDel(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//----------------------------------------------------------------------------------------------------------------------
		boolean		ok		= canDel(user, json, response); 				
		if (!ok){
			API.doResponse(response, DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response, DefAPI.API_MSG_OK);
		}	
	}

	private static boolean canDel(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {
		
		Integer 			objId	= ToolData.reqInt	(json, "id"		, -1	);	
		
		if (objId < 0) {
			return false;
		}
			
		TaTpyFavorite ent = TaTpyFavorite.DAO.reqEntityByValues(TaPrjProject.ATT_I_ID, objId);
		if(ent == null) return false;

		if (!canWorkWithObj(user, WORK_DEL, ent)){ //other param after ent...
			return false;
		}

		try {
			TaTpyFavorite.DAO.doRemove(ent);
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}
	
	// ---------------------------------------------------------------------------------------------------------

	private static CacheData<TaTpyFavorite>       cache_entity = new CacheData<TaTpyFavorite>(500,
			DefTime.TIME_00_01_00_000);
	private static CacheData<ResultPagination>    cache_rs     = new CacheData<ResultPagination>(100,
			DefTime.TIME_00_20_00_000);
	private static CacheData<List<TaTpyFavorite>> cache_lst    = new CacheData<List<TaTpyFavorite>>(100,
			DefTime.TIME_00_00_05_000);

	// ---------------------------------------------------------------------------------------------------------
	private static void doLstByUserId(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		// ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");

		List<TaTpyFavorite> lst = reqLstByUserId(user, json); // and other params if necessary
		if (lst == null || lst.size() == 0) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		API.doResponse(response,
				ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_YES, DefJS.RES_DATA, lst));
	}

	private static List<TaTpyFavorite> reqLstByUserId(TaAutUser user, JSONObject json, Object... params)
			throws Exception {
		Integer             userId  = user.reqId();
		Set<Integer>        entTyps  = ToolData.reqSetInt(json, "entTyps", null);

		String              keyWord = userId + "" + entTyps.toString();
		List<TaTpyFavorite> lst     = cache_lst.reqData(keyWord);

		if (lst == null || lst.size() <= 0) {
			ToolLogServer.doLogInf("---reqGetFavoriteByUserIdNew build list-----");

			Criterion cri = reqCriterion(userId);

			if (entTyps != null && entTyps.size() > 0) {
				cri = Restrictions.and(cri, Restrictions.in(TaTpyFavorite.ATT_I_ENTITY_TYPE, entTyps));
			}

			lst = TaTpyFavorite.DAO.reqList(cri);
			

			cache_lst.reqPut(keyWord, lst);
		} else {
			ToolLogServer.doLogInf("---reqGetFavoriteByUserIdNew use cache-----");
			cache_lst.reqCheckIfOld(keyWord); // cache in 2 hour
		}

		return lst;
	}

	private static void doLstByUser(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		// ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");

		List<TaTpyFavorite> lst = reqLstByUser(user, json); // and other params if necessary
		if (lst == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		API.doResponse(response,
				ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_YES, DefJS.RES_DATA, lst));
	}

	private static List<TaTpyFavorite> reqLstByUser(TaAutUser user, JSONObject json, Object... params)
			throws Exception {
		// other params here
		Criterion           cri = reqCriterion(user.reqId());

		List<TaTpyFavorite> lst = TaTpyFavorite.DAO.reqList(cri);

		return lst;
	}

	// ---------------------------------------------------------------------------------------------------------
	private static void doLstByUserIdAndType(TaAutUser user, JSONObject json, HttpServletResponse response)
			throws Exception {
		// ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");

		ResultPagination rs = reqLstByUserIdAndEntTyp(user, json); // and other params if necessary
		if (rs == null || rs.reqList().size() == 0) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		API.doResponse(response,
				ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_YES, DefJS.RES_DATA, rs));
	}

	private static ResultPagination reqLstByUserIdAndEntTyp(TaAutUser user, JSONObject json, Object... params)
			throws Exception {
		Boolean          pagination = ToolData.reqBool(json, "pagination", false);

		Integer          begin       = ToolData.reqInt(json, "begin", 0);
		Integer          number      = ToolData.reqInt(json, "number", 5);
		Integer          total       = ToolData.reqInt(json, "total", 0);
		Integer          entTyp      = ToolData.reqInt(json, "entTyp", 0);
		Integer			 typ		 = ToolData.reqInt(json, "typ", null);
		String 			 searchKey   = ToolData.reqStr(json, "searchKey"	, null);
		Boolean          forced      = ToolData.reqBool(json,"forced", false);

		Integer          userId     = (Integer) user.reqRef();
		String           keyWord    = userId + "_" + entTyp + "_" + typ + "_" + begin + "_" + number + "_" + total + "_" + searchKey;
		
		ResultPagination rs         = null;
		if(!forced)		 rs		    = cache_rs.reqData(keyWord);

		if (forced || rs == null) {
			ToolLogServer.doLogInf("---reqGetFavoriteByUserIdAndType build list-----");

			List<TaTpyFavorite> list = new ArrayList<TaTpyFavorite>();
			Criterion cri = null;
					  cri = Restrictions.ilike(TaTpyFavorite.ATT_T_TITLE, "%"+ searchKey +"%");
					  cri = Restrictions.and(cri, Restrictions.eq(TaTpyFavorite.ATT_I_ENTITY_TYPE, entTyp));
			if(typ != null) {
					  cri = Restrictions.and(cri, Restrictions.eq(TaTpyFavorite.ATT_I_TYPE, typ));
			}
			
			if (pagination)
				list = TaTpyFavorite.DAO.reqList(begin, number, Restrictions.eq(TaTpyFavorite.ATT_I_AUT_USER, userId), cri);

			else
				list = TaTpyFavorite.DAO.reqList(Restrictions.eq(TaTpyFavorite.ATT_I_AUT_USER, userId),cri);

			if (total == 0)
				total = (Integer) TaTpyFavorite.DAO.reqCount(cri).intValue();

			rs = new ResultPagination(list, total, begin, number);
			cache_rs.reqPut(keyWord, rs);
		} else {
			ToolLogServer.doLogInf("---reqGetFavoriteByUserIdAndType use cache-----");
			cache_rs.reqCheckIfOld(keyWord); // cache in 2 hour
		}

		return rs;
	}

	// ---------------------------------------------------------------------------------------------------------
	private static void doCountByUserIdAndType(TaAutUser user, JSONObject json, HttpServletResponse response)
			throws Exception {
		// ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");

		ResultPagination rs = reqCountByUserIdAndEntTyp(user, json); // and other params if necessary
		if (rs == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,
				ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_YES, DefJS.RES_DATA, rs));
	}

	private static int count_lim = 50;

	private static ResultPagination reqCountByUserIdAndEntTyp(TaAutUser user, JSONObject json, Object... params)
			throws Exception {

		Integer          total   = ToolData.reqInt(json, "total", 0);
		Integer          entTyp  = ToolData.reqInt(json, "entTyp", 0);
		Boolean          forced  = ToolData.reqBool(json, "forced", false);

		Integer          userId  = (Integer) user.reqRef();
		String           keyWord = userId + "_" + entTyp + "_" + total;
		ResultPagination rs      = cache_rs.reqData(keyWord);

		if (rs == null) {
			ToolLogServer.doLogInf("---reqGetFavoriteByUserIdAndType build list-----");

			if (total == 0)
				total = (Integer) TaTpyFavorite.DAO.reqCount(Restrictions.eq(TaTpyFavorite.ATT_I_AUT_USER, userId),
						Restrictions.eq(TaTpyFavorite.ATT_I_ENTITY_TYPE, TaTpyFavorite.FAVORITE_TYPE_LIKE));

			// ----check lim
			try {
				if (total > count_lim) {
					List<TaTpyFavorite> lsToDel = TaTpyFavorite.DAO.reqList(0, total - count_lim,
							Order.asc(TaTpyFavorite.ATT_I_ID),
							Restrictions.and(Restrictions.eq(TaTpyFavorite.ATT_I_AUT_USER, userId),
									Restrictions.eq(TaTpyFavorite.ATT_I_ENTITY_TYPE, entTyp),
									Restrictions.eq(TaTpyFavorite.ATT_I_TYPE, TaTpyFavorite.FAVORITE_TYPE_LIKE)));
					TaTpyFavorite.DAO.doRemove(lsToDel);
					total = count_lim;
				}
			} catch (Exception e) {
				ToolLogServer.doLogErr(e);
			}

			rs = new ResultPagination(null, total, null, null);

			cache_rs.reqPut(keyWord, rs);
		} else {
			ToolLogServer.doLogInf("---reqGetFavoriteByUserIdAndType use cache-----");
			cache_rs.reqCheckIfOld(keyWord); // cache in 2 hour
		}

		return rs;
	}

	// =========================================================================================================
	private static Criterion reqCriterion(Object... params) throws Exception {
		if (params == null || params.length == 0)
			return null;

		Criterion cri = Restrictions.gt("I_ID", 0);

		if (params != null && params.length > 0) {
			int userId = (int) params[0];
			cri = Restrictions.and(cri, Restrictions.eqOrIsNull(TaTpyFavorite.ATT_I_AUT_USER, userId));
			cri = Restrictions.and(cri, Restrictions.ne(TaTpyFavorite.ATT_I_ENTITY_TYPE, DefDBExt.ID_TA_TYP_STICKY_NOTE));
		}

		if (params != null && params.length > 1) {
			// do something
		}

		return cri;
	}

	// =========================================================================================================
	private static Integer reqGetFavoriteLstByUserIdCount(Integer userId) throws Exception {
		Criterion cri   = Restrictions.eq(TaTpyFavorite.ATT_I_AUT_USER, userId);

		Integer   count = 0;
		count = TaTpyFavorite.DAO.reqCount(cri).intValue();

		return count;
	}


	// ---------------------------------------------------------------------------------------------------------
	private static void doNsoNew(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		TaTpyFavorite ent = null;
		ent = reqNsoNew(user, json);

		if (ent == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		API.doResponse(response,
				ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_YES, DefJS.RES_DATA, ent));

	}

	private static TaTpyFavorite reqNsoNew(TaAutUser user, JSONObject json) throws Exception {
		JSONObject          obj  = ToolData.reqJson(json, "obj", null);
		
		Map<String, Object> attr = API.reqMapParamsByClass(obj, TaTpyFavorite.class);

		TaTpyFavorite       ent  = new TaTpyFavorite(attr);

		if (!canWorkWithObj(user, WORK_NEW, ent)) { // other param after obj...
			return null;
		}

		ent.reqSet(TaTpyFavorite.ATT_I_AUT_USER, user.reqId());
		if(ent.reqInt(TaTpyFavorite.ATT_I_TYPE) == null) {			
			ent.reqSet(TaTpyFavorite.ATT_I_TYPE, TaTpyFavorite.FAVORITE_TYPE_LIKE);
		}

		// check exist favorite
		List<TaTpyFavorite> lst = TaTpyFavorite.DAO.reqList(
				Restrictions.eq(TaTpyFavorite.ATT_I_AUT_USER, ent.req(TaTpyFavorite.ATT_I_AUT_USER)),
				Restrictions.eq(TaTpyFavorite.ATT_I_ENTITY_TYPE, ent.req(TaTpyFavorite.ATT_I_ENTITY_TYPE)),
				Restrictions.eq(TaTpyFavorite.ATT_I_ENTITY_ID, ent.req(TaTpyFavorite.ATT_I_ENTITY_ID)),
				Restrictions.ne(TaTpyFavorite.ATT_I_ENTITY_TYPE, DefDBExt.ID_TA_TYP_STICKY_NOTE)
				);
		if (lst.size() > 0)
			return null;

		TaTpyFavorite.DAO.doPersist(ent);
		return ent;
	}

	// ---------------------------------------------------------------------------------------------------------
	private static void doNsoMod(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		// ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doMod --------------");

		TaTpyFavorite ent = reqNsoMod(user, json);
		if (ent == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		API.doResponse(response,
				ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_YES, DefJS.RES_DATA, ent));
	}

	private static TaTpyFavorite reqNsoMod(TaAutUser user, JSONObject json) throws Exception {
		Integer             entTyp 	= ToolData.reqInt	(json, "entTyp"	, 	null);
		String        		data   	= ToolData.reqStr	(json, "data"	, 	null);

		Criterion           cri    = 	Restrictions.eq(TaTpyFavorite.ATT_I_AUT_USER		, 	user.reqId());
		cri = Restrictions.and(cri, 	Restrictions.eq(TaTpyFavorite.ATT_I_ENTITY_TYPE		, 	entTyp));

		TaTpyFavorite       ent = null;
		List<TaTpyFavorite> lst = TaTpyFavorite.DAO.reqList(cri);
		if (lst.size() > 0)
			ent = lst.get(0);

		if (ent == null) {
			ent  = new TaTpyFavorite();
			ent.reqSet(TaTpyFavorite.ATT_I_AUT_USER		, user.reqId());
			ent.reqSet(TaTpyFavorite.ATT_I_ENTITY_TYPE	, entTyp);
			ent.reqSet(TaTpyFavorite.ATT_I_ENTITY_ID	, 0);
			ent.reqSet(TaTpyFavorite.ATT_T_DESRCIPTION	, data);
			TaTpyFavorite.DAO.doPersist(ent);
		} else {
			ent.reqSet(TaTpyFavorite.ATT_T_DESRCIPTION	, data);
			TaTpyFavorite.DAO.doMerge(ent);
		}

		return ent;
	}

	public static TaTpyFavorite reqNsoNewAll(TaAutUser user, JSONObject json) throws Exception {
		JSONObject          obj  = ToolData.reqJson(json, "obj", null);
		Map<String, Object> attr = API.reqMapParamsByClass(obj, TaTpyFavorite.class);

		TaTpyFavorite       ent  = new TaTpyFavorite(attr);

		if (!canWorkWithObj(user, WORK_NEW, ent)) { // other param after obj...
			return null;
		}

		ent.reqSet(TaTpyFavorite.ATT_I_AUT_USER, user.reqRef());
		ent.reqSet(TaTpyFavorite.ATT_I_TYPE, TaTpyFavorite.FAVORITE_TYPE_LIKE);
		ent.reqSet(TaTpyFavorite.ATT_D_DATE, new Date());

		// check exist favorite
		List<TaTpyFavorite> lst = TaTpyFavorite.DAO.reqList(Restrictions.eq(TaTpyFavorite.ATT_I_AUT_USER, user.reqId()),
				Restrictions.eq(TaTpyFavorite.ATT_I_ENTITY_TYPE, ent.req(TaTpyFavorite.ATT_I_ENTITY_TYPE)),
				Restrictions.eq(TaTpyFavorite.ATT_I_ENTITY_ID, ent.req(TaTpyFavorite.ATT_I_ENTITY_ID)));
		if (lst.size() > 0)
			return null;

		TaTpyFavorite.DAO.doPersist(ent);
		return ent;
	}

	public static boolean reqNsoRemove(TaAutUser user, JSONObject json) throws Exception {
		Integer parTyp = ToolData.reqInt(json, "parTyp", null);
		Integer parId  = ToolData.reqInt(json, "parId", null);

		if (parTyp == null || parId == null)
			return false;

		if (!canWorkWithObj(user, WORK_DEL)) { // other param after obj...
			return false;
		}

		// check exist favorite
		List<TaTpyFavorite> lst = TaTpyFavorite.DAO.reqList(Restrictions.eq(TaTpyFavorite.ATT_I_AUT_USER, user.reqId()),
				Restrictions.eq(TaTpyFavorite.ATT_I_ENTITY_TYPE, parTyp),
				Restrictions.eq(TaTpyFavorite.ATT_I_ENTITY_ID, parId));
		if (lst != null && lst.size() > 0) {
			TaTpyFavorite.DAO.doRemove(lst);
			return true;
		}

		return false;
	}


	// -----------------------------------------------------------------------------------------------------------------------------------

}
