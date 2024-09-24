package com.hnv.api.service.publ.nso;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.hnv.api.def.DefAPI;
import com.hnv.api.def.DefJS;
import com.hnv.api.def.DefTime;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.api.service.common.ResultPagination;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.common.util.CacheData;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.nso.TaNsoOffer;
import com.hnv.db.nso.vi.ViNsoOfferMap;
import com.hnv.index.tool.GeoPoint;

/**
 * ----- Service by H&V
 * ----- Copyright 2017------------
 */
public class ServiceNsoOfferMap implements IService {
	private static	String 			filePath	= null; 
	private	static	String 			urlPath		= null; 


	public static final Integer 	LIMIT_DEFAULT	= 20;
	public static final Integer 	LIMIT_MAX 		= 200;

	//--------------------------------Service Definition----------------------------------
	public static final String SV_CLASS 			= "ServiceNsoOfferMap".toLowerCase();	

	public static final String SV_LST_MAP 			= "SVLstMap".toLowerCase();
	public static final String SV_LST_MAP_AROUND 	= "SVLstMapAround".toLowerCase();

	//-----------------------------------------------------------------------------------------------
	//-------------------------Default Constructor - Required -------------------------------------
	public ServiceNsoOfferMap(){
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}
	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {

		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {

			if(sv.equals(SV_LST_MAP_AROUND)	){
				doLstMapAround(user, json, response);
			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}	

		}catch(Exception e){
			API.doResponse(response, DefAPI.API_MSG_ERR_API);
			e.printStackTrace();
		}			
	}
	//---------------------------------------------------------------------------------------------------------

	//---------------------------------------------------------------------------------------------------------
	//--cache for UI public
	private static CacheData<List<ViNsoOfferMap>> 		cache 		= new CacheData<List<ViNsoOfferMap>>(100, DefTime.TIME_05_00_00_000);
	private static CacheData<ResultPagination>			cache_rs 	= new CacheData<ResultPagination>();

	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	private static void doLstMapAround(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {	
		List<ViNsoOfferMap> lst 	= reqLstMapAround(user, json); //and other params if necessary
		JSONObject 			result 	= EntityMap.reqEntityMap(lst);

		if (lst==null || lst.size()==0){
			API.doResponse(response, ToolJSON.reqJSonString(						
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO
			));	
			return;
		}

		API.doResponse(response, ToolJSON.reqJSonString(						
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, result
		));	
	}

	//---------------------------------------------------------------------------------------------------------
	private static List<ViNsoOfferMap> reqLstMapAround(TaAutUser user, JSONObject json, Object...params) throws Exception {
		String 		 searchKey 	= ToolData.reqStr		(json, "sKey"	, "%");
		Set<Integer> lstCat 	= ToolData.reqSetInt	(json, "catIds"	, null);
		Integer 	 typ01 		= ToolData.reqInt		(json, "typ01"	, null);
		Integer 	 typ02 		= ToolData.reqInt		(json, "typ02"	, null);
		Integer 	 typ03 		= ToolData.reqInt		(json, "typ03"	, null);
		JSONObject 	 mon 		= ToolData.reqJson		(json, "money"	, null);
		Integer 	 typmon 	= ToolData.reqInt		(json, "typmon"	, null);
		Integer		 entId		= ToolData.reqInt		(json, "id"		, null);
		Double		 pLat		= ToolData.reqDouble	(json, "lat"	, null);
		Double		 pLong		= ToolData.reqDouble	(json, "lng"	, null);
		Double		 pDist		= ToolData.reqDouble	(json, "dis"	, null);
		Integer		 nb			= ToolData.reqInt		(json, "number"	, 10);
		

		if(entId != null) {
			TaNsoOffer ent = TaNsoOffer.DAO.reqEntityByRef(entId);
			//Get location by area, override pLat + pLong
			if(ent != null) {
				if(ent.req(TaNsoOffer.ATT_F_VAL_01)!=null)	pLat	= (Double) ent.req(TaNsoOffer.ATT_F_VAL_01);
				if(ent.req(TaNsoOffer.ATT_F_VAL_02)!=null)	pLong	= (Double) ent.req(TaNsoOffer.ATT_F_VAL_02);
			}
		}
		
		List<GeoPoint>  lstPoint = new ArrayList<GeoPoint>();
		if(!pLat.equals(0.0) && !pLong.equals(0.0)) {
			lstPoint = GeoPoint.reqBound(pLat, pLong, pDist); //5 diem, diem dau tien origin,  Nord, south, est, west
		}

		String 			key 	= searchKey+ "_"+lstCat+ "_"+pDist+"_"+pLat+"_"+pLong;
		List<ViNsoOfferMap> rs 	= null;

		if(rs==null) {
			List<ViNsoOfferMap> list01 	= ViNsoOfferMap.reqLstMap(0, Math.min(Math.max(LIMIT_DEFAULT, nb), LIMIT_MAX), TaNsoOffer.STAT_01_ACTIVE, TaNsoOffer.STAT_02_PUBLIC, typ01, typ03, searchKey, lstCat, mon,typmon, lstPoint);
			rs							= list01;
			cache.reqPut(key, rs);			
		} else {
			cache.reqCheckIfOld(key);
		}

		return rs;
	}

	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------

}
