package com.hnv.api.service.priv.mat;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Restrictions;

import com.hnv.api.def.DefAPI;
import com.hnv.api.def.DefJS;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.mat.TaMatPrice;

public class ServiceMatPrice implements IService {
	public static String SV_CLASS = "ServiceMatPrice".toLowerCase();

	public static String SV_GET = "SVGet".toLowerCase();
	public static String SV_LST = "SVLst".toLowerCase();
	public static String SV_NEW = "SVNew".toLowerCase();
	public static String SV_MOD = "SVMod".toLowerCase();
	public static String SV_DEL = "SVDel".toLowerCase();

	public static String SV_SEARCH = "SVSearch".toLowerCase();
	public static String SV_LST_BY_MAT = "SVLstByMat".toLowerCase();

	public ServiceMatPrice() {
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	//-----------------------------------------------------------------------
	@Override
	public void doService(JSONObject json, HttpServletResponse response)  throws Exception {
		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {
			if (sv.equals(SV_GET)) {
				doGet(user, json, response);
			} else if (sv.equals(SV_LST)) {
				doLst(user, json, response);
			} else if (sv.equals(SV_NEW)) {
				doNew(user, json, response);
			} else if (sv.equals(SV_MOD)) {
				doMod(user, json, response);
			} else if (sv.equals(SV_DEL)) {
				doDel(user, json, response);
			} else if (sv.equals(SV_LST_BY_MAT)) {
				doLstByMat(user, json, response);

			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}
		} catch (Exception e) {
			API.doResponse(response, DefAPI.API_MSG_ERR_API);
			e.printStackTrace();
		}
	}

	// -- GET --
	private static void doGet(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		int 		objId 	= ToolData.reqInt (json, "id", 0);
		TaMatPrice 	ent 	= TaMatPrice.DAO.reqEntityByRef(objId);

		if (ent == null) {
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT, 1, 
				DefJS.SV_CODE, DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA, ent));
	}

	// -- LST --
	private static void doLst(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		List<TaMatPrice> list = TaMatPrice.DAO.reqList();

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
	private void doNew(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		JSONObject			obj		= ToolData.reqJson 		(json, "obj"	, null);
		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaMatPrice.class);

		TaMatPrice ent = new TaMatPrice(attr);

		ent.reqSet(TaMatPrice.ATT_D_DATE_01, new Date());
		ent.reqSet(TaMatPrice.ATT_D_DATE_02, new Date());
		ent.reqSet(TaMatPrice.ATT_I_AUT_USER_01, user.req(TaAutUser.ATT_I_ID));
		ent.reqSet(TaMatPrice.ATT_I_AUT_USER_02, user.req(TaAutUser.ATT_I_ID));

		TaMatPrice.DAO.doPersist(ent);

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT, 1, 
				DefJS.SV_CODE,	DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA, ent));
	}

	// -- MOD --
	private void doMod(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		// -- lock --
		JSONObject			obj		= ToolData.reqJson 		 (json, "obj"	, null);
		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaMatPrice.class);

		Integer 			objId 	= (Integer) attr.get(TaMatPrice.ATT_I_ID);
		TaMatPrice 			ent 	= TaMatPrice.DAO.reqEntityByRef(objId);

		if (ent == null) {
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		} 

		attr.remove(TaMatPrice.ATT_I_ID);
		attr.remove(TaMatPrice.ATT_D_DATE_01);
		attr.remove(TaMatPrice.ATT_I_AUT_USER_01);
		
		attr.put(TaMatPrice.ATT_D_DATE_02, new Date());
		attr.put(TaMatPrice.ATT_I_AUT_USER_02, user.reqId());

		TaMatPrice.DAO.doMerge(ent, attr);

		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA	, ent));
	}

	// -- DEL
	private void doDel(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		// -- Del --
		boolean ok = canDel(user, json);
		if (!ok) {
			API.doResponse(response,DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response,DefAPI.API_MSG_OK);
		}
	}

	public static boolean canDel(TaAutUser user, JSONObject json) throws Exception {
		Integer 	objId	= ToolData.reqInt	(json, "id", null	);	
		TaMatPrice 	ent 	= TaMatPrice.DAO.reqEntityByRef(objId);
		if (ent == null) {
			return false;
		}

		TaMatPrice.DAO.doRemove(ent);
		return true;
	}

	private void doLstByMat(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		Integer 	matId	= ToolData.reqInt	(json, "matId", null	);	
		List<TaMatPrice> result = TaMatPrice.DAO.reqList(Restrictions.eq(TaMatPrice.ATT_I_MAT_MATERIAL, matId));

		API.doResponse(response, ToolJSON.reqJSonString(	
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, result
				));
	}

}
