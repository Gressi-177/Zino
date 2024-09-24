package com.hnv.api.service.publ.nso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

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
import com.hnv.common.tool.ToolString;
import com.hnv.common.util.CacheData;
import com.hnv.data.json.JSONArray;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.nso.TaNsoPost;
import com.hnv.db.nso.vi.ViNsoPostComment;
import com.hnv.db.nso.vi.ViNsoPostNew;
import com.hnv.db.nso.vi.ViNsoPostSearch;
import com.hnv.db.tpy.TaTpyCategoryEntity;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.def.DefDBExt;

public class ServiceNsoPostPubl implements IService {
	private static	String 			filePath		= null; 
	private	static	String 			urlPath			= null; 
	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 				= "HNV".toLowerCase();

	public static final String SV_CLASS 				= "ServiceNsoPostPubl".toLowerCase();	

	//NEW CODE------------------------------------------------------------------------------------------------------------
	public static final String SV_GET		        	= "SVGet".toLowerCase(); 
	
	public static final String SV_LST_NEWS		        = "SVLstNews".toLowerCase();
	public static final String SV_LST		       	 	= "SVLst".toLowerCase();
	public static final String SV_LST_BLOG_SEARCH		= "SVLstBlogSearch".toLowerCase();
	
	public static final String SV_LST_PAGE_CMT_BY_ENT   = "SVLstPageCmtByEnt".toLowerCase();
	
	public static final String SV_MOD_RATE				= "SVModRate".toLowerCase();
	
	
	static final Integer	ENT_TYP						= DefDBExt.ID_TA_SOR_DEAL;
	//-----------------------------------------------------------------------------------------------
	//-------------------------Default Constructor - Required -------------------------------------
	public ServiceNsoPostPubl(){
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	// -----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		// ToolLogServer.doLogInf("--------- "+ SV_CLASS+ ".doService --------------");
		if (filePath == null)
			filePath = API.reqContextParameter("INV_INVOICE_PATH_FILE");
		if (urlPath == null)
			urlPath = API.reqContextParameter("INV_INVOICE_PATH_URL");

		String sv = API.reqSVFunctName(json);
		TaAutUser user = (TaAutUser) json.get("userInfo");
		try {

			if (sv.equals(SV_LST_NEWS)) {
				doNsoPostLstNews(user, json, response);

			} else if (sv.equals(SV_GET)) {
				doGet(user, json, response);

			} else if (sv.equals(SV_LST_BLOG_SEARCH)) {
				doLstPageBlog(user, json, response);

			} else if (sv.equals(SV_LST_PAGE_CMT_BY_ENT)) {
				doLstPageCmtByEnt(user, json, response);
			}else if(sv.equals(SV_MOD_RATE)	&& APIAuth.canAuthorize(user, SV_CLASS, sv)) {
				doModRate(user, json, response);
				
			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}
		} catch (Exception e) {
			try {
				API.doResponse(response, DefAPI.API_MSG_ERR_API);
				e.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	private static void doNsoPostLstNews(TaAutUser user, JSONObject json, HttpServletResponse response)
			throws Exception {
		// ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");

		ResultPagination res = reqNsoPostLstNews(user, json); // and other params if necessary

		if (res.reqList() == null || res.reqList().size() == 0) {
			API.doResponse(response, ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_NO));
			return;
		} else {
			API.doResponse(response, ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA, res));
		}

	}

	private static CacheData<ResultPagination> cache_rs = new CacheData<ResultPagination>(100, DefTime.TIME_24_00_00_000);

	private static ResultPagination reqNsoPostLstNews(TaAutUser user, JSONObject json, Object... params)
			throws Exception {

		Integer type = ToolData.reqInt(json, "type", null);
		Integer status = TaNsoPost.STAT_01_ACTIVE;
		Integer style = ToolData.reqInt(json, "style", null);
		Integer begin = ToolData.reqInt(json, "begin", 0);
		Integer number = ToolData.reqInt(json, "number", 20);
		Integer total = ToolData.reqInt(json, "total", 0);
		String typMulti = ToolData.reqStr(json, "typMulti", null);
		String sCatIds = ToolData.reqStr(json, "catIds", null);
		String langId = ToolData.reqStr(json, "langId", null); // format (1,2,3)
		Boolean withTransl = ToolData.reqBool(json, "withTransl", true);

		// other params here

		String keyWord = typMulti + "_" + type + "_" + status + "_" + style + "_" + begin + "_" + number + "_" + total
				+ "_" + langId + "_" + withTransl;
		ResultPagination rs = cache_rs.reqData(keyWord);
		
		if (rs == null) {
			ToolLogServer.doLogInf("---reqGetLstGrid build vi-----");

			Set<Integer> catIds = null;
			List<ViNsoPostNew> list = ViNsoPostNew.reqLstPost(begin, number, type, typMulti, status, style, catIds,
					langId);

			if (list != null) {
				ViNsoPostNew.doBuildListUsers(list);
				ViNsoPostNew.doBuildListDocuments(list);
			}
				
			if (total == 0)
				total = (Integer) ViNsoPostNew.reqLstPostCount(type, typMulti, status, null, catIds, langId);
			rs = new ResultPagination(list, total, begin, number);

			if (withTransl)
				ViNsoPostNew.doBuildTranslations(list);

			cache_rs.reqPut(keyWord, rs);
		} else {
			ToolLogServer.doLogInf("---reqNsoAreaLstGrid use cache-----");
		}

		return rs;
	}

	
	
	private static CacheData<TaNsoPost> cache_entity = new CacheData<TaNsoPost>(500, DefTime.TIME_24_00_00_000);	
	private static void doGet(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		Integer 	entId 		= 	ToolData.reqInt(json,"id", null);
		String 		entCode01		=	ToolData.reqStr(json, "code01", null);
		
		if(entId == null || entCode01 == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		TaNsoPost ent = reqGet(entId, entCode01);

		if (ent == null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		ent.doBuildUsers(true);

		API.doResponse(response,
				ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_YES, DefJS.RES_DATA, ent));
	}

	public static TaNsoPost reqGet(Integer entId, String entCode01) throws Exception {

		TaNsoPost ent = TaNsoPost.DAO.reqEntityByValues(TaNsoPost.ATT_I_ID, entId, TaNsoPost.ATT_T_CODE_01, entCode01,
				TaNsoPost.ATT_I_STATUS_01, TaNsoPost.STAT_01_ACTIVE, TaNsoPost.ATT_I_STATUS_02,
				TaNsoPost.STAT_02_PUBLIC);
		
		if (ent != null) {
			ent.doBuildAll(true, true);
		}

		return ent;

	}

	// --------------------------------------------------------------------------------------------------------------
	private static void doLstPageBlog(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		// ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doGetPostLstByEntId
		// --------------");

		ResultPagination res = reqListPageBlog(user, json); // and other params if necessary

		if (res.reqList() == null || res.reqList().size() == 0) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,
				ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_YES, DefJS.RES_DATA, res));
	}

	private static CacheData<ResultPagination> cacheEnt_rs_Blog = new CacheData<ResultPagination>(100,
			DefTime.TIME_01_00_00_000);

	public static ResultPagination reqListPageBlog(TaAutUser user, JSONObject json) throws Exception {
		Integer begin = ToolData.reqInt(json, "begin", 0);
		Integer number = ToolData.reqInt(json, "number", 10);
		Integer total = ToolData.reqInt(json, "total", 0);
		Boolean withAvatar = ToolData.reqBool(json, "withAvatar", false);

		Set<String> searchKey = ToolData.reqSetStr(json, "sKey", null);
		Integer type01 = ToolData.reqInt(json, "typ01", null);
		Integer type02 = ToolData.reqInt(json, "typ02", null);
		Integer type03 = ToolData.reqInt(json, "typ03", null);
		Integer stat01 = null;
		Integer stat02 = null;
//		Set<Integer>		catIds  	= ToolData.reqSetInt	(json, "catIds"		, null);

		String keyWord = type01 + "_" + begin + "_" + number + "_" + total;
		ResultPagination rs = null;
		Set<Integer> lstIds = null;
		boolean addCache = true;

		if (searchKey != null)
			addCache = false;
		else
			rs = cacheEnt_rs_Blog.reqData(keyWord); // cache những trang cố đinh

		if (rs == null) {
			if (type01 == null && type03 == null) {
				return null;
			}

			if (stat01 == null) {
				stat01 = TaNsoPost.STAT_01_ACTIVE;
			}

			if (stat02 == null) {
				stat02 = TaNsoPost.STAT_02_PUBLIC;
			}
			Criterion cri = Restrictions.eq(TaNsoPost.ATT_I_STATUS_01, stat01);
			cri = Restrictions.and(cri, Restrictions.eq(TaNsoPost.ATT_I_STATUS_02, stat02));

			if (type01 != null) {
				cri = Restrictions.and(cri, Restrictions.eq(TaNsoPost.ATT_I_TYPE_01, type01));
			}

//			if(catIds != null) {
//				Disjunction catsCri = Restrictions.disjunction();
//				for (Integer integer : catIds) {
//					catsCri.add(Restrictions.ilike(TaNsoPost.ATT_T_CONTENT_04, "%\"id\":\"" + integer + "\"%"));
//				}
//				cri = Restrictions.and(cri, catsCri);
//			}			

			if (searchKey != null) {
				for (String s : searchKey) {
					cri = Restrictions.and(cri,
							Restrictions.or(Restrictions.ilike(TaNsoPost.ATT_T_TITLE, "%" + s + "%"),
									Restrictions.ilike(TaNsoPost.ATT_T_CONTENT_01, "%" + s + "%"),
									Restrictions.ilike(TaNsoPost.ATT_T_CONTENT_02, "%" + s + "%"),
									Restrictions.ilike(TaNsoPost.ATT_T_CONTENT_03, "%" + s + "%")));
				}
			}

			List<ViNsoPostSearch> list = ViNsoPostSearch.DAO.reqList(begin, number,
					Order.desc(ViNsoPostSearch.ATT_I_ID), cri);

			if (list != null) {
				ViNsoPostSearch.doBuildListUsers(list);
				ViNsoPostSearch.doBuildListDocuments(list);
			}

			if (total == 0)
				total = ViNsoPostSearch.DAO.reqCount(cri).intValue();
			rs = new ResultPagination(list, total, begin, number);
			if (addCache)
				cacheEnt_rs_Blog.reqPut(keyWord, rs);

		} else {
			ToolLogServer.doLogInf("---reqGetPostByEntId use cache-----");
			cacheEnt_rs_Blog.reqCheckIfOld(keyWord); // cache in 2 hour
		}

		return rs;

	}

	// --------------------------------------------------------------------------------------------------------------
	private static void doLstPageCmtByEnt(TaAutUser user, JSONObject json, HttpServletResponse response)
			throws Exception {
		// ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doGetPostLstByEntId
		// --------------");

		ResultPagination res = reqListPageCmt(user, json); // and other params if necessary

		if (res.reqList() == null || res.reqList().size() == 0) {
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response,
				ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_YES, DefJS.RES_DATA, res));
	}

//	private static CacheData<ResultPagination>		cacheEnt_rs_Cmt 	= new CacheData<ResultPagination>(100, DefTime.TIME_01_00_00_000);
	public static ResultPagination reqListPageCmt(TaAutUser user, JSONObject json) throws Exception {
		Integer begin = ToolData.reqInt(json, "begin", 0);
		Integer number = ToolData.reqInt(json, "number", 10);
		Integer total = ToolData.reqInt(json, "total", 0);
		Boolean withAvatar = ToolData.reqBool(json, "withAvatar", false);

		Integer entTyp = ToolData.reqInt(json, "entTyp", null);
		Integer entId = ToolData.reqInt(json, "entId", null);
		String entCod = ToolData.reqStr(json, "entCod", null);
		Integer stat01 = null;
		Integer stat02 = null;

//		String 				keyWord 	= entTyp + "_"  + "_"  + entId + begin + "_" + number;
		ResultPagination rs = null;
		Set<Integer> lstIds = null;
		boolean addCache = true;

//							rs			= cacheEnt_rs_Cmt.reqData(keyWord); //cache những trang cố đinh
		if (rs == null) {
			if (entTyp == null || entId == null || entCod == null) {
				return null;
			}

			if (stat01 == null) {
				stat01 = TaNsoPost.STAT_01_ACTIVE;
			}

			if (stat02 == null) {
				stat02 = TaNsoPost.STAT_02_PUBLIC;
			}

			Criterion	cri = Restrictions.eq(TaNsoPost.ATT_I_TYPE_01, TaNsoPost.TYPE_01_CMT);
						cri = Restrictions.eq(TaNsoPost.ATT_I_STATUS_01, stat01);
						cri = Restrictions.and(cri, Restrictions.eq(TaNsoPost.ATT_I_STATUS_02, stat02));
						cri = Restrictions.and(cri, Restrictions.eq(TaNsoPost.ATT_I_VAL_01, entTyp));
						cri = Restrictions.and(cri, Restrictions.eq(TaNsoPost.ATT_I_VAL_02, entId));
						
			Criterion   criChild = cri;
			
			Criterion   criParent = Restrictions.and(cri, Restrictions.isNull(TaNsoPost.ATT_I_PARENT));
			List<ViNsoPostComment> list = ViNsoPostComment.DAO.reqList(begin, number,
					Order.desc(ViNsoPostComment.ATT_I_ID), criParent);

			
			if(list != null && list.size() > 0) {
				list					= ViNsoPostComment.reqBuildChildren(list, criChild);
			}
			
			if (list != null) {
				ViNsoPostComment.doBuildListUsers(list);
			}
			
			if (total == 0)
				total = ViNsoPostComment.DAO.reqCount(cri).intValue();
			rs = new ResultPagination(list, total, begin, number);
			
//			if (addCache) cacheEnt_rs_Cmt.reqPut(keyWord, rs);			

		} else {
			ToolLogServer.doLogInf("---reqGetPostByEntId use cache-----");
//			cacheEnt_rs_Cmt.reqCheckIfOld(keyWord); //cache in 2 hour
		}

		return rs;

	}
	
	private static void doModRate(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		JSONObject		obj			= ToolData.reqJson (json, "obj", null);
		TaNsoPost 		ent			= reqModRate		(user, json);
		if (ent==null){
			API.doResponse(response		, ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO					
					));
			return;
		}

		API.doResponse(response		, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent
				));
	}

	private static TaNsoPost reqModRate(TaAutUser user, JSONObject json) throws Exception {		
		Integer						entId 		= ToolData.reqInt(json, "id", null);
		Integer						point		= ToolData.reqInt(json, "point", null);
		Integer 					uId 		= user.reqId();
		
		if(point==null || entId==null||user==null) {
			return null;
		}
		
		TaNsoPost 		ent 		= TaNsoPost.DAO.reqEntityByRef(entId);
		
		if (ent==null){
			return null;
		}

		String currentIdsStr = ent.reqStr(TaNsoPost.ATT_T_INFO_03);
		List<String> idList = new ArrayList<>();

		if (currentIdsStr != null) {
		    idList = Arrays.stream(currentIdsStr.split(","))
		            .map(String::trim)
		            .filter(id -> Math.abs(Integer.parseInt(id)) != uId)
		            .collect(Collectors.toList());
		}

		Integer currentPoint = ent.reqInt(TaNsoPost.ATT_I_VAL_03) ;
		
		idList.add(String.valueOf(point< 0 ? "-"+user.reqId() : uId));

		ent.reqSet(TaNsoPost.ATT_T_INFO_03, String.join(", ", idList));
		ent.reqSet(TaNsoPost.ATT_I_VAL_03, currentPoint + point);

		TaNsoPost.DAO.doMerge(ent);
		return ent;
	}

}
