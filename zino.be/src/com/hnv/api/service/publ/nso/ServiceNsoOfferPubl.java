package com.hnv.api.service.publ.nso;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
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
import com.hnv.common.util.CacheData;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.nso.TaNsoOffer;
import com.hnv.db.nso.TaNsoPost;
import com.hnv.db.nso.vi.ViNsoOfferSearch;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.def.DefDBExt;
import com.hnv.def.DefRight;
import com.hnv.process.ThreadManager;
/**
* ----- ServiceNsoOffer by H&V
* ----- Copyright 2023------------
*/
public class ServiceNsoOfferPubl implements IService {
	private static	String 			filePath	= null; 
	private	static	String 			urlPath		= null; 
	

	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 				= "HNV".toLowerCase();

	public static final String SV_CLASS 				= "ServiceNsoOfferPubl".toLowerCase();	
	
	public static final String SV_GET 					= "SVGet"		.toLowerCase();	
	public static final String SV_LST 					= "SVLst"		.toLowerCase();
	public static final String SV_LST_PAGE				= "SVLstPage"	.toLowerCase();
	public static final String SV_LST_SEARCH			= "SVLstSearch"	.toLowerCase();

	public static final Integer	ENT_TYP					= DefDBExt.ID_TA_NSO_OFFER;
		
	//-----------------------------------------------------------------------------------------------
	//-------------------------Default Constructor - Required -------------------------------------
	public ServiceNsoOfferPubl(){
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
		
	}
	
	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {
			//---------------------------------------------------------------------------------
			if(sv.equals(SV_GET) 				){
				doGet(user,  json, response);
			} else if(sv.equals(SV_LST)			){
				doLst(json, response);
			} else if(sv.equals(SV_LST_PAGE)	){
				doLstPage(user,  json, response);
			} else if(sv.equals(SV_LST_SEARCH)	){
				doLstPage(user,  json, response);
			}

		}catch(Exception e){
			API.doResponse(response, DefAPI.API_MSG_ERR_API);
			e.printStackTrace();
		}		
	}
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	private static CacheData<TaNsoOffer> 		cache_entity= new CacheData<TaNsoOffer>		(500, DefTime.TIME_24_00_00_000 );
	private static void doGet(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {	
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");

		Integer 		entId		= ToolData.reqInt	(json, "id"			, -1	);		
		String			entCode01	= ToolData.reqStr	(json, "code01"		, null	);

		if (entId==null || entCode01==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}
		
		TaNsoOffer 		ent 		= reqGet(entId, entCode01);

		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response		, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent 
				));
	}
	
	public static TaNsoOffer reqGet(Integer entId, String entCode01) throws Exception{
		TaNsoOffer 		ent 	= cache_entity.reqData(entId + "-" + entCode01);	
		if (ent ==null) {
			ent 	= TaNsoOffer.DAO.reqEntityByValues(
					TaNsoOffer.ATT_I_ID			, entId, 
					TaNsoOffer.ATT_T_CODE_01	, entCode01,
					TaNsoOffer.ATT_I_STATUS_01	, TaNsoOffer.STAT_01_ACTIVE,
					TaNsoOffer.ATT_I_STATUS_02	, TaNsoOffer.STAT_02_PUBLIC
					);
			
			if (ent!=null) {
				//---do something and put to cache
				cache_entity.reqPut(entCode01, ent);
			}
		}else {				
			ToolLogServer.doLogInf("---reqGet use cache-----");
			cache_entity.reqCheckIfOld(entCode01); //cache in 20 hour
		}
		
		//---do build something other of ent like details, document, categories....
//		if (ent != null) {
//			ent.doBuildCats(forced);
//		}

		return ent;
	}
	
	//---------------------------------------------------------------------------------------------------------
	private static CacheData<List<ViNsoOfferSearch>> 	cache_vi_search= new CacheData<List<ViNsoOfferSearch>>		(500, DefTime.TIME_24_00_00_000 );
	private static void doLst(JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLst --------------");

		List<ViNsoOfferSearch> 	list = reqLst(json); //and other params if necessary
		if (list==null ){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response, ToolJSON.reqJSonString(//filter,		
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, list 
				));				
	}
	private static List<ViNsoOfferSearch> reqLst(JSONObject json) throws Exception  {
		Integer 	begin     	= ToolData.reqInt		(json, "begin" 		, 0);
		Integer 	nbLine     	= ToolData.reqInt		(json, "nbLine" 	, Integer.MAX_VALUE);
		Set<String>	searchKey	= ToolData.reqSetStr	(json, "sKey"		, null);
		Integer		stat01		= ToolData.reqInt		(json, "stat01"		, null);
		Integer		stat02		= ToolData.reqInt		(json, "stat02"		, null);
		Integer		typ01		= ToolData.reqInt		(json, "typ01"		, null);
		Integer		typ02		= ToolData.reqInt		(json, "typ02"		, null);
		Integer		typ03		= ToolData.reqInt		(json, "typ03"		, null);
		
		
		if(typ01 == null && typ02 == null && typ03 == null && stat01 == null && stat02 == null) {
			return null;
		}
		
		String 					keyCache	= nbLine + "_" + searchKey + "_" + stat01 + "_" + stat02 + "_" + typ01 + "_" + typ02 + "_" + typ03;
		List<ViNsoOfferSearch>	lst			= cache_vi_search.reqData(keyCache);
		
		if (lst != null && lst.size() > 0) {
			cache_vi_search.reqCheckIfOld(keyCache);
			return lst;
		}
		
		lst = ViNsoOfferSearch.reqLst(stat01, stat02, typ01, typ03, searchKey, null, null,null, begin, nbLine);
		
		cache_vi_search.reqPut(keyCache, lst);
		return lst;
	}
	
	private static Criterion reqRestriction(Set<String> searchKey, Integer stat01, Integer stat02, Integer typ01, Integer typ02, Integer typ03) throws Exception {		
		if (stat01 == null){
			stat01 = TaNsoOffer.STAT_01_ACTIVE; 
		}
		if (stat02 == null){
			stat02 = TaNsoOffer.STAT_02_PUBLIC;
		}
		
		Criterion cri = Restrictions.eq(ViNsoOfferSearch.ATT_I_STATUS_01, stat01);
		cri = Restrictions.and(	cri, Restrictions.eq(ViNsoOfferSearch.ATT_I_STATUS_02, stat02));
		
		if(typ01!=null) {
			cri = Restrictions.and(	cri, Restrictions.eq(ViNsoOfferSearch.ATT_I_TYPE_01 , typ01));
		}
		if(typ02!=null) {
			cri = Restrictions.and(	cri, Restrictions.eq(ViNsoOfferSearch.ATT_I_TYPE_02 , typ02));
		}
		if(typ03!=null) {
			cri = Restrictions.and(	cri, Restrictions.eq(ViNsoOfferSearch.ATT_I_TYPE_03 , typ03));
		}

		if (searchKey!=null) {
			for (String s : searchKey){
				cri = 	 Restrictions.and	(cri, Restrictions.or(
													Restrictions.ilike(ViNsoOfferSearch.ATT_T_TITLE	, s), 
													Restrictions.ilike(ViNsoOfferSearch.ATT_T_CODE_01	, s)
											));
			}
		}
		
		return cri;
	}

	//---------------------------------------------------------------------------------------------------------		
	private static void doLstPage(TaAutUser user,  JSONObject json, HttpServletResponse response)	throws Exception {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doGetPostLstByEntId --------------");

		ResultPagination  	res = reqListPage(user, json); //and other params if necessary
		if (res.reqList()==null ){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT				, 1, 
				DefJS.SV_CODE				, DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA				, res));
	}

	private static CacheData<ResultPagination>		cacheEnt_rs 	= new CacheData<ResultPagination>(100, DefTime.TIME_02_00_00_000);
	public static ResultPagination reqListPage(TaAutUser user, JSONObject json) throws Exception {
		Integer 			begin		= ToolData.reqInt		(json, "begin"		, 0	);
		Integer 			number		= ToolData.reqInt		(json, "number"		, 10); 
		Integer 			total		= ToolData.reqInt		(json, "total"		, 0	);
		Boolean 			withAvatar	= ToolData.reqBool		(json, "withAvatar"	, false	);
	
		Set<String> 		searchKey	= ToolData.reqSetStr	(json, "sKey"		, null);
		Integer				type01		= ToolData.reqInt		(json, "typ01"		, null);
		Integer				type02		= ToolData.reqInt		(json, "typ02"		, null);
		Integer				type03		= ToolData.reqInt		(json, "typ03"		, null);
		Integer				stat01		= null;
		Integer				stat02		= null;
		Set<Integer>		catIds  	= ToolData.reqSetInt	(json, "catIds"		, null);
		JSONObject			money  		= ToolData.reqJson		(json, "money"		, null);
		Integer				monTyp		= ToolData.reqInt		(json, "monTyp"	, null);

		String 				keyWord 	= type01 + "_" + type02 + "_" +type03 + "_" + begin + "_" + number + "_" + total ;
		ResultPagination 	rs 			= null;
		boolean				addCache	= true;
		
		if (catIds!=null||money!=null||searchKey!=null)
			addCache	= false;
		else
			rs			= cacheEnt_rs.reqData(keyWord); //cache những trang cố đinh

		if(rs==null) {
			if(type01 == null && type03 == null) {
				return null;
			}
			
			if(stat01 == null) {
				stat01 = TaNsoOffer.STAT_01_ACTIVE;
			}
			
			if(stat02 == null) {
				stat02 = TaNsoOffer.STAT_02_PUBLIC;
			}

			String 	sqlWithoutSelect 	= ViNsoOfferSearch.reqSqlWithoutSelect(stat01, stat02, type01, type03, searchKey, catIds, money,monTyp);
			String 	sqlFull 			= ViNsoOfferSearch.reqLstSql(sqlWithoutSelect);
			
			List<ViNsoOfferSearch> list = ViNsoOfferSearch.DAO.reqList(begin, number, sqlFull);

			if (total == 0 ) {
				String sqlReqCount 		= ViNsoOfferSearch.reqCountSql(sqlWithoutSelect);
				total					= ViNsoOfferSearch.DAO.reqCount(sqlReqCount).intValue();
			}
			rs							= new ResultPagination(list, total, begin, number);
			if (addCache) cacheEnt_rs.reqPut(keyWord, rs);			
		
		} else {
			ToolLogServer.doLogInf("---reqGetPostByEntId use cache-----");
			cacheEnt_rs.reqCheckIfOld(keyWord); //cache in 2 hour
		}

		return rs;

	}
	
}
