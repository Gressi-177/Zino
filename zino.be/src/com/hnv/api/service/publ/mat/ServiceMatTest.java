package com.hnv.api.service.publ.mat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;

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
import com.hnv.common.tool.ToolSet;
import com.hnv.common.util.CacheData;
import com.hnv.data.json.JSONArray;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.mat.TaMatMaterial;
import com.hnv.db.mat.TaMatMaterialDetail;
import com.hnv.db.mat.vi.ViMatMaterial;
import com.hnv.db.per.TaPerPerson;
import com.hnv.db.sys.TaSysLock;
import com.hnv.db.tpy.TaTpyCategoryEntity;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.def.DefRight;



public class ServiceMatTest implements IService {

	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 			= "EC_V3".toLowerCase();

	public static final String SV_CLASS 			= "ServiceMatTest".toLowerCase();

	
	public static final String SV_GET 				= "SVGet"			.toLowerCase();	
	public static final String SV_LST				= "SVLst"			.toLowerCase(); 
	public static final String SV_LST_PAGE			= "SVLstPage"		.toLowerCase(); 


	public static final String SV_NEW 				= "SVNew"			.toLowerCase();	
	
	//-------------------------Default Constructor - Required -------------------------------------
	public ServiceMatTest(){
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		// ToolLogServer.doLogInf("--------- "+ SV_CLASS+ ".doService --------------");
		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {
			if (sv.equals(SV_GET)){
				doGet(user, json, response);
				
			} else if (sv.equals(SV_LST)){
				doLst(user, json, response);
			
			} else if (sv.equals(SV_LST_PAGE)){
				doLstPage(user, json, response);
			
			} else if (sv.equals(SV_NEW)) {
				doNew(user, json, response);
				
			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}
		} catch (Exception e) {
			API.doResponse(response, DefAPI.API_MSG_ERR_API);
			e.printStackTrace();
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------
	private static Hashtable<String,Integer> mapCol = new Hashtable<String, Integer>(){
	    {
	    	put("action"	, -1);
	    	put("id"		, 0 );
	    	put("name01"	, 1 );
	    	put("name02"	, 2 );
	    	put("code01"	, 3 );
	    	put("code02"	, 4 );
	    	put("code03"	, 5 );
	    	put("code04"	, 6 );
	    	put("stat"		, 7 );
	    	put("price02"	, 8 );
	    }
	};
	
	private static Object[] reqDataTableOption(String searchKey, int beginDisplay, int nbDisplay, String colN, int sortOption){
		Object[] res = new Object[10];

		List<String> keyword 	= new ArrayList<String>();
		if (searchKey!=null && searchKey.length()>0){				
			StringTokenizer token = new StringTokenizer(searchKey, " ");
			while (token.hasMoreTokens()){
				String s = "%" +token.nextToken()+ "%";
				s = s.replace("%+", "");
				s = s.replace("+%", "");
				s = s.replace("%$", "");
				s = s.replace("$%", "");
				s = s.replace("%\"", "");
				s = s.replace("\"%", "");
				s = s.replace("%'", "");
				s = s.replace("'%", "");
				keyword.add(s.toLowerCase());
			}			
		}else{
			keyword.add("%");
		}
		
		int colToSort= mapCol.get(colN);
		res[0]		= keyword;
		res[1]		= (int)beginDisplay;
		res[2]		= (int)nbDisplay;
		res[3]		= (int)colToSort;
		res[4]		= (int)sortOption;
		res[5]		= -1;		
		return res;

	}
	

	//---------------------------------------------------------------------------------------------------------------------------
	private static CacheData<TaMatMaterial> 	cache_entity= new CacheData<TaMatMaterial>		(10000, DefTime.TIME_01_00_00_000);
	private static CacheData<TaMatMaterial> 	cache_spam	= new CacheData<TaMatMaterial>		(10000, DefTime.TIME_01_00_00_000);
	private static CacheData<ResultPagination>	cache_rs 	= new CacheData<ResultPagination>	( 1000, DefTime.TIME_24_00_00_000);
	
	private static void doGet(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {	
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doGet --------------");

		Integer				objId		= ToolData.reqInt	(json, "id", null);
		TaMatMaterial 		ent     	= reqGet	(objId);

		if (ent!=null)
			API.doResponse(response, ToolJSON.reqJSonString(		//filter,
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, ent 
					));
		else
			API.doResponse(response, ToolJSON.reqJSonString(		//filter,
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO
					));
	}

	public static TaMatMaterial reqGet(Integer objId) throws Exception{
		String 			key		= objId+"";
		TaMatMaterial 	ent 	= cache_entity.reqData(key);	
		
		if (ent ==null) {
			ent		= cache_spam.reqData(key);	
			if (ent!=null) return ent; //---return empty obj
			
			ent 	= TaMatMaterial.DAO.reqEntityByRef(objId);
			
			if (ent!=null) {
				Integer stat = (Integer)ent.req(TaMatMaterial.ATT_I_STATUS_01);
				if(!stat.equals(TaMatMaterial.STAT_ACTIVE))	ent = null;	 //--filter all inactive ent
			}
			
			if (ent==null){
				cache_spam.reqPut(key, new TaMatMaterial()); //prevent spam
			}
			
		}else {				
			ToolLogServer.doLogInf("---reqGet use cache-----");
			cache_rs.doCheckTimeAuto(DefTime.TIME_00_30_00_000);//refresh cache each 30 minutes
			return ent;
		}

		//---do build something other of ent like details....
		if (ent!=null){		
			cache_entity.reqPut(key,ent); //--add to cache
			
			Integer 	typ01 	= (Integer) ent.req(TaMatMaterial.ATT_I_TYPE_01);
			Integer 	typ02 	= (Integer) ent.req(TaMatMaterial.ATT_I_TYPE_02);
			
//			ent.doBuildPricesPublic	(true);	
//			ent.doBuildUnitsPublic	(true);
//			ent.doBuildFiles		(true);			
//
//			ent.doBuildProducer		(true);
//			ent.doBuildManager		(true);
//			ent.doBuildPerAdvice    (null, true);
//
//			ent.doBuildEval			(true);
//			ent.doBuildPosts		(true);
//			ent.doBuildMatColor		(true);
//
//			if(typ01 == TaMatMaterial.TYPE_01_MAT && typ02 == TaMatMaterial.TYPE_02_BOM) 
//				ent.doBuildDetails	(true);
//			
//			if(typ02 != null)
//				if(typ02 == TaMatMaterial.TYPE_01_SERVICE)	
//					ent.doBuildDetail		(true);
		}

		return ent;
	}
	
	
	private static void doLst(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		ResultPagination  	res = reqViMatLst(user, json); //and other params if necessary
		if (res.reqList()==null ){
			API.doResponse(response, ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_NO));
			return;
		}
		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, res 
				));	
	}
	
	//-------------------------------------------------List dynamique filter mat--------------------------------------------------------------------------------------
		
	private static void doLstPage(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {	
		ResultPagination  	res = reqViMatLst(user, json); //and other params if necessary
		
		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, res 
				));	
	}
	
	private static 	ResultPagination reqViMatLst(TaAutUser user, JSONObject json) throws Exception  {
		Integer 		uIdFavorite	= ToolData.reqInt	(json, "uIdFav"		, null);
		Integer			manId		= ToolData.reqInt	(json, "manId"		, null);
		Set<Integer> 	catIds		= ToolData.reqSetInt(json, "catIds"		, null);
		Integer			prodId		= ToolData.reqInt	(json, "prodId"		, null);
		
		Integer 		begin		= ToolData.reqInt	(json, "begin"		, 0	);
		Integer 		number		= ToolData.reqInt	(json, "number"		, 20);
		Integer 		total		= ToolData.reqInt	(json, "total"		, 0	);
		String 			searchKey   = ToolData.reqStr	(json, "searchKey"	, null);
		
		String 			sortCol   	= ToolData.reqStr	(json, "sortCol"	, "id");
		Integer 		sortDir   	= ToolData.reqInt	(json, "sortDir"	, 1);
		
		Integer			typ01		= ToolData.reqInt	(json, "typ01"		, null); //TaMatMaterial.TYPE_01_MAT);
		Integer			typ02		= ToolData.reqInt	(json, "typ02"		, null); //TaMatMaterial.TYPE_02_SINGLE);
		
		Double			priceMin	= ToolData.reqDouble(json, "priceMin"	, null);
		Double			priceMax	= ToolData.reqDouble(json, "priceMax"	, null); 
		
		boolean			forHome  	= ToolData.reqBool	(json, "forHome"	, false);
		
		

		String 			keyWord 	= manId + "_" + catIds!=null?catIds.toString():"" + "_" + prodId + "_" +  begin + "_" + number + "_" + total + "_" + searchKey + "_" + sortCol+ "_" + sortDir +"_" + typ01+ "_" + typ02 +"_" + priceMin+ "_" + priceMax+ "_"+ uIdFavorite + "_" + forHome;
		ResultPagination rs 		= cache_rs.reqData(keyWord);

		if(rs==null) {
			ToolLogServer.doLogInf("---reqViMatLst build vi-----");
			
			Object[] dataTableOption 		 	= reqDataTableOption(searchKey, begin, number, sortCol, sortDir);
			List<ViMatMaterial> materialList 	= reqListFilterDyn(dataTableOption, manId,  prodId, catIds, typ01, typ02, TaMatMaterial.STAT_ACTIVE, priceMin, priceMax, uIdFavorite, forHome);
			if (materialList==null || materialList.size() ==0 ){
				rs								= new ResultPagination(materialList, 0, 0, 0);
			}else {
				if (total == 0 )	total		= reqViMatFilterListDynCount(dataTableOption, manId, prodId,catIds, typ01, typ02, TaMatMaterial.STAT_ACTIVE, priceMin, priceMax, uIdFavorite, forHome);
				rs								= new ResultPagination(materialList, total, begin, number);
			}
			
			cache_rs.reqPut(keyWord, rs);			
		} else {
			ToolLogServer.doLogInf("---reqViMatLst use cache-----");
			cache_rs.doCheckTimeAuto(DefTime.TIME_00_30_00_000);//refresh cache each 30 minutes
		}

		return rs;
	}
	
	private static List<ViMatMaterial> reqListFilterDyn(Object[] dataTableOption, Integer manId, Integer producerId, Set<Integer> catIds, Integer typ01, Integer typ02,Integer status,  Double priceMin, Double priceMax,  Integer uIdFavorite, boolean forHome) throws Exception {
		Set<String>		searchKey	= (Set<String>)dataTableOption[0];
		int 			begin 		= (int)	dataTableOption[1];
		int 			number 		= (int)	dataTableOption[2]; 
		int 			sortCol 	= (int)	dataTableOption[3]; 
		int 			sortTyp 	= (int)	dataTableOption[4];

		String sortColName = null;
		String sortDir	   = null;

    	
		switch(sortCol) {
		case 0: sortColName = ViMatMaterial.ATT_I_ID; break;		
		case 1: sortColName = ViMatMaterial.ATT_T_NAME_01; break;
		case 2: sortColName = ViMatMaterial.ATT_T_NAME_02; break;
		case 3: sortColName = ViMatMaterial.ATT_T_CODE_01; break;
		case 4: sortColName = ViMatMaterial.ATT_T_CODE_02; break;
//		case 5: sortColName = ViMatMaterial.ATT_T_CODE_03; break;
		case 6: sortColName = ViMatMaterial.ATT_T_CODE_04; break;
//		case 7: sortColName = ViMatMaterial.ATT_I_STATUS; break;
		case 8: sortColName = ViMatMaterial.ATT_F_PRICE_02; break;
		default: sortColName = TaMatMaterial.ATT_T_NAME_01; break;
		}

		if (sortColName != null) {
			switch(sortTyp) {
			case 0: sortDir = " ASC"; break;
			case 1: sortDir = " DESC"; break;								
			}
		}

		List<ViMatMaterial> lst = ViMatMaterial.reqListMatFilter(manId, catIds, producerId, begin, number, searchKey, sortColName, sortDir, typ01, typ02, status, priceMin, priceMax, uIdFavorite , true, forHome);

		return lst;
	}
	
	private static Integer reqViMatFilterListDynCount(Integer manId,   Integer producerId,   Set<Integer> catIds,  Integer typ01, Integer typ02, Integer status,  Double priceMin, Double priceMax,  Integer uIdFavorite, boolean forHome) throws Exception {
		Integer result = ViMatMaterial.reqCountMatFilter(manId, catIds, producerId,   typ01, typ02,  status, priceMin, priceMax, uIdFavorite, forHome);
		return result;
	}
	private static Integer reqViMatFilterListDynCount(Object[] dataTableOption, Integer manId, Integer producerId, Set<Integer> catIds,  Integer typ01, Integer typ02,  Integer status,  Double priceMin, Double priceMax,  Integer uIdFavorite, boolean forHome) throws Exception {
		Set<String>	searchKey				= (Set<String>)dataTableOption[0];
		Integer result = ViMatMaterial.reqCountMatFilter(manId, catIds, producerId, searchKey,  typ01, typ02, status, priceMin, priceMax, uIdFavorite, forHome);
		return result;
	}

	
	
	private static void doNew(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		TaMatMaterial 	ent	 = reqNew(user, json);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response, DefAPI.API_MSG_OK);
	}


	private static TaMatMaterial reqNew(TaAutUser user, JSONObject json) throws Exception {	
		JSONObject		obj			= ToolData.reqJson 		(json, "obj"		, null);
		Double 			v01 		= ToolData.reqDouble 	(obj,  "node"		, null);
		Double 			v02 		= ToolData.reqDouble	(obj,  "voltage"	, null);
		Double 			v03 		= ToolData.reqDouble	(obj,  "current"	, null);
		Double 			v04 		= ToolData.reqDouble	(obj,  "power"		, null);
		Double 			v05 		= ToolData.reqDouble	(obj,  "energy"		, null);
		
		System.out.println("--- v1 : " + v01);
		System.out.println("--- v2 : " + v02);
		System.out.println("--- v3 : " + v03);
		System.out.println("--- v4 : " + v04);
		System.out.println("--- v5 : " + v05);
		
		return new TaMatMaterial();
	}
}
