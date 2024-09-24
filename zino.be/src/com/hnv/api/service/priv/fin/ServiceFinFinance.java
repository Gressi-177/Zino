package com.hnv.api.service.priv.fin;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import com.hnv.api.def.DefAPI;
import com.hnv.api.def.DefJS;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.common.tool.ToolDate;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.data.json.JSONArray;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.fin.TaFinFinance;
import com.hnv.db.sor.TaSorOrder;

public class ServiceFinFinance implements IService{ 
	
	public static final String SV_CLASS 		= "ServiceFinFinance".toLowerCase();
	
	public static final String SV_CHECK 		= "SVCheck".toLowerCase();
	public static final String SV_GET 			= "SVGet".toLowerCase();
	
	public ServiceFinFinance() {
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}
	
	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");

		try {
			if (sv.equals(SV_CHECK)) {
				svFinCheck(user, json, response);
			} else if (sv.equals(SV_GET)) {
				svFinGet(user, json, response);
				
			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}
		} catch (Exception e) {
			API.doResponse(response, DefAPI.API_MSG_ERR_API);
			e.printStackTrace();
		}
	}
	
	
	private static void svFinCheck(TaAutUser user, JSONObject json, HttpServletResponse response ) throws Exception {
		Integer perId 	 	= json.get("id") 	!= null && json.get("id").toString()  != "" ? Integer.parseInt(json.get("id").toString()) : null; 
		Double 	val 		= json.get("val") 	!= null && json.get("val").toString() != "" ? Double.parseDouble(json.get("val").toString()) : null;
		
		if (perId==null || val ==null ) {
			API.doResponse(response, ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, 1,
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO					
					));
			return;
		}
		
		TaFinFinance	fi	= TaFinFinance.DAO.reqEntityByValues(TaFinFinance.ATT_I_PER_PERSON_01, perId, TaFinFinance.ATT_I_STATUS, TaFinFinance.STAT_ACTIVE);
		Double			sum = 0.0;
		if (fi!=null){
			sum = fi.reqDouble(TaFinFinance.ATT_F_VAL_01);
		} 
		
		if (sum<val) {
			API.doResponse(response, ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, 1,
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO					
					));
		} else {
			API.doResponse(response, ToolJSON.reqJSonString(	
					DefJS.SESS_STAT		, 1,
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES
					));
		}
	}
	private static void svFinGet(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		Integer perId 	 	= json.get("id") 	!= null && json.get("id").toString()  != "" ? Integer.parseInt(json.get("id").toString()) : null; 
		Integer typ 	 	= json.get("typ") 	!= null && json.get("typ").toString()  != "" ? Integer.parseInt(json.get("typ").toString()) : null; 
		
		if (perId==null ) {
			API.doResponse(response, ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, 1,
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO					
					));
			return;
		}
		
		TaFinFinance	fi	= TaFinFinance.DAO.reqEntityByValues(TaFinFinance.ATT_I_PER_PERSON_01, perId, TaFinFinance.ATT_I_STATUS, TaFinFinance.STAT_ACTIVE);
		if (fi==null){
			API.doResponse(response, ToolJSON.reqJSonString(						
					DefJS.SESS_STAT		, 1,
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO));	
			return;
		}
		
		if (typ!=null && typ==10){//---manager get
			API.doResponse(response, ToolJSON.reqJSonString(						
					DefJS.SESS_STAT		, 1,
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, fi
					));	
			return;
		}
		
		Double			sum = 0.0;
		if (fi!=null){
			sum = fi.reqDouble(TaFinFinance.ATT_F_VAL_01);
		} 
		
		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1,
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, sum				
				));
	}
	
	//------------------------------------------------------------------------------------------------------------------
	
	public static TaFinFinance reqFinEntityForUpd (Integer manId, Integer perId, Date ordDt,  Integer ordId, String ordRef, Integer ordTypIO, Double ordVal) throws Exception{
		//101 : in/buy
		//201 : out/use
		
		if (ordDt==null||perId==null||ordId==null||ordTypIO==null||ordVal==null) {
			return null;
		}
		
		ordVal = Math.abs(ordVal); 
		TaFinFinance	fi	= TaFinFinance.DAO.reqEntityByValues(TaFinFinance.ATT_I_PER_PERSON_01, perId, TaFinFinance.ATT_I_STATUS, TaFinFinance.STAT_ACTIVE);
		if (fi ==null) {
			fi = new TaFinFinance (manId, perId);
			TaFinFinance.DAO.doPersist(fi);
		}
		
		Double sum = fi.reqDouble(TaFinFinance.ATT_F_VAL_01);
		if (ordTypIO.equals(TaSorOrder.TYPE_01_IN_STK_BUY)) {
			
		} else if (ordTypIO.equals(TaSorOrder.TYPE_01_OU_STK_SELL)) {
			if (sum<ordVal) return null;
			ordVal = -ordVal;
		}
		//---update
		String 		histo 		= fi.reqStr(TaFinFinance.ATT_T_INFO_01);
		JSONArray 	histArray 	= histo == null ? new JSONArray() : ToolJSON.reqJSonArrayFromString(histo);
		JSONObject	js			= new JSONObject ();
		js.put("ordDt"	, ToolDate.reqString(ordDt, ToolDate.FORMAT_ISO));
		js.put("ordId"	, ordId);
		js.put("ordRef"	, ordRef);
		js.put("ordVal"	, ordVal);
		js.put("ordTyp"	, ordTypIO);
		js.put("lastSum", sum);
		js.put("finalSum", sum + ordVal);
		histArray.add(js);
		fi.reqSet(TaFinFinance.ATT_T_INFO_01, histArray.toJSONString());
		
		fi.reqSet(TaFinFinance.ATT_F_VAL_02, sum);
		fi.reqSet(TaFinFinance.ATT_D_DATE_02, fi.req(TaFinFinance.ATT_D_DATE_01));
		sum = sum + ordVal;
		fi.reqSet(TaFinFinance.ATT_F_VAL_01, sum);
		fi.reqSet(TaFinFinance.ATT_D_DATE_01, new Date());
		
//		TaFinFinance.DAO.doMerge(fi);
		return fi;
	}
	
}
