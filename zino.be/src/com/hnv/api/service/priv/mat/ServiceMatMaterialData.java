package com.hnv.api.service.priv.mat;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

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
import com.hnv.db.mat.TaMatMaterial;
import com.hnv.db.mat.TaMatMaterialData;
import com.hnv.db.mat.vi.ViMatMaterial;



public class ServiceMatMaterialData implements IService {

	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 			= "EC_V3".toLowerCase();

	public static final String SV_CLASS 			= "ServiceMatMaterialData".toLowerCase();

	
	public static final String SV_GET 				= "SVGet"			.toLowerCase();	
	public static final String SV_LST				= "SVLst"			.toLowerCase(); 
	public static final String SV_LST_PAGE			= "SVLstPage"		.toLowerCase(); 


	public static final String SV_NEW 				= "SVNew"			.toLowerCase();	
	
	//-------------------------Default Constructor - Required -------------------------------------
	public ServiceMatMaterialData(){
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		// ToolLogServer.doLogInf("--------- "+ SV_CLASS+ ".doService --------------");
		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {
			if (sv.equals(SV_NEW)) {
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
	
	private static void doNew(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		TaMatMaterialData 	ent	 = reqNew(user, json);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response, DefAPI.API_MSG_OK);
	}


	private static TaMatMaterialData reqNew(TaAutUser user, JSONObject json) throws Exception {	
		JSONObject		obj			= ToolData.reqJson 		(json, "obj"		, null);
		Integer			id			= ToolData.reqInt 		(obj,  "matId"		, null);
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
		
		return new TaMatMaterialData(id, v01,v02,v03,v04,v05, null, null, null, null, null);
	}
}
