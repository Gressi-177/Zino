package com.hnv.api.service.publ.nso;

import java.util.List;

import com.hnv.data.json.JSONArray;
import com.hnv.data.json.JSONObject;
import com.hnv.db.nso.TaNsoOffer;
import com.hnv.db.nso.vi.ViNsoOfferMap;

//var PR_DATA_MAP = {
//		"status": 'found',
//		"listing": [
//			{
//				"id": 1,
//				"longitude": 2.5591705,
//				"latitude": 49.008677299999995,
//				"image": "www/images/mappostimg/img-03.jpg",
//				"subjects": "MDS - Paedodontics & Preventive Dentistry",
//				"title": "Blue Bird Organization",
//				"url": "#",
//				"featured": 'no',
//				"marker": 'www/images/icons/markerone.png',
//			}
//		]
//}
public class EntityMap {
	private static final String JSON_KEY_COD_01				= "code01"; 
	private static final String JSON_KEY_STATUS				= "sta"; 		//"status";
	private static final String JSON_KEY_LIST				= "lst"; 		//"listing";
	private static final String JSON_KEY_ID 				= "id";
	private static final String JSON_KEY_REF 				= "ref";
	private static final String JSON_KEY_LAT				= "lat";		//"latitude";
	private static final String JSON_KEY_LNG				= "lng";		//"longitude";
	private static final String JSON_KEY_IMG				= "img";		//"imgPath";
	private static final String JSON_KEY_TITLE				= "tit";		//"title";
	private static final String JSON_KEY_SUBJ				= "sbj";		//"subjects";
	private static final String JSON_KEY_URL				= "url";
	private static final String JSON_KEY_FEATURED			= "ft";			//"featured";
	private static final String JSON_KEY_MARKER				= "mrk";		//"marker";
	
	private static final String JSON_KEY_ADDRESS			= "adr";		//"address";
	private static final String JSON_KEY_MONEY				= "mon";		//"money";
	private static final String JSON_KEY_MIN_AGE			= "miAge";		//"minAge";
	private static final String JSON_KEY_MAX_AGE			= "maAge";		//"maxAge";
//	private static final String JSON_KEY_TYP_MONEY		    = "monTyp";		//"typMoney";
	
	
	private static final String JSON_KEY_CONTENT			= "con";		//"content";
	private static final String JSON_KEY_TIMES				= "times";		//"content";
	private static final String JSON_KEY_DATE_UPDATE		= "dU";			//dBegin";
	private static final String JSON_KEY_DATE_BEGIN			= "dB";			//dBegin";
	private static final String JSON_KEY_DATE_END			= "dE";			//dEnd";
	private static final String JSON_KEY_DATE_EXPIRATION	= "dEx";		//dExpiration";
	

	private static final Integer JSON_VALUE_STATUS_NOT_FOUND	= 0;
	private static final Integer JSON_VALUE_STATUS_FOUND		= 1;
	private static final Integer JSON_VALUE_FEATURED_NO			= 0;
	private static final Integer JSON_VALUE_FEATURED_YES		= 1;
	
	private static JSONObject reqEntityMap(ViNsoOfferMap ent) {
		JSONObject jsonObj	= new JSONObject();
		
		try {
			jsonObj.put(JSON_KEY_ID					, ent.reqRef());
			jsonObj.put(JSON_KEY_REF				, ent.req(ViNsoOfferMap.ATT_T_CODE_01));
			
			jsonObj.put(JSON_KEY_ADDRESS			, ent.req(ViNsoOfferMap.ATT_T_INFO_02));
			
			jsonObj.put(JSON_KEY_DATE_UPDATE		, ent.req(ViNsoOfferMap.ATT_D_DATE_02));
			jsonObj.put(JSON_KEY_DATE_BEGIN			, ent.req(ViNsoOfferMap.ATT_D_DATE_03));
			jsonObj.put(JSON_KEY_DATE_END			, ent.req(ViNsoOfferMap.ATT_D_DATE_04));
			jsonObj.put(JSON_KEY_DATE_EXPIRATION	, ent.req(ViNsoOfferMap.ATT_D_DATE_05));
			
			jsonObj.put(JSON_KEY_LAT				, ent.req(ViNsoOfferMap.ATT_F_VAL_01));
			jsonObj.put(JSON_KEY_LNG				, ent.req(ViNsoOfferMap.ATT_F_VAL_02));
			
			jsonObj.put(JSON_KEY_IMG				, ent.req(ViNsoOfferMap.ATT_T_AVATAR));
			
			jsonObj.put(JSON_KEY_TITLE				, ent.req(ViNsoOfferMap.ATT_T_TITLE));
			jsonObj.put(JSON_KEY_COD_01				, ent.req(ViNsoOfferMap.ATT_T_CODE_01));
			jsonObj.put(JSON_KEY_SUBJ				, ent.req(ViNsoOfferMap.ATT_T_CONTENT_01));
			jsonObj.put(JSON_KEY_TIMES				, ent.req(ViNsoOfferMap.ATT_T_CONTENT_05));
				
	//		jsonObj.put(JSON_KEY_TYP_MONEY			, ent.req(ViNsoOfferMap.ATT_I_VAL_02));
			jsonObj.put(JSON_KEY_MONEY				, ent.req(ViNsoOfferMap.ATT_I_VAL_03));
			jsonObj.put(JSON_KEY_MIN_AGE			, ent.req(ViNsoOfferMap.ATT_I_VAL_04));
			jsonObj.put(JSON_KEY_MAX_AGE			, ent.req(ViNsoOfferMap.ATT_I_VAL_05));
			
			jsonObj.put(JSON_KEY_URL				, ent.req(ViNsoOfferMap.ATT_T_INFO_01));
			jsonObj.put(JSON_KEY_MARKER				, ent.req(ViNsoOfferMap.ATT_I_TYPE_01));
			jsonObj.put(JSON_KEY_FEATURED			, JSON_VALUE_FEATURED_NO);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonObj;
	}
	
	private static JSONArray reqLstEntityMap(List<ViNsoOfferMap> lstOffer) {
		JSONArray jsonArr	= new JSONArray();
		
		try {
			for(int i = 0; i < lstOffer.size(); i++) {
				ViNsoOfferMap 	offer	= lstOffer.get(i);
				JSONObject 		obj		= reqEntityMap(offer);
				jsonArr.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonArr;
	}
	
	public static JSONObject reqEntityMap(List<ViNsoOfferMap> lstOffer) {
		JSONObject jsonObj	= new JSONObject();
		
		try {
			JSONArray jsonArr = new JSONArray();
			
			if(lstOffer!=null  && lstOffer.size() > 0) {
				jsonObj.put(JSON_KEY_STATUS, JSON_VALUE_STATUS_FOUND);
				jsonArr = reqLstEntityMap(lstOffer);
				
			} else {
				jsonObj.put(JSON_KEY_STATUS, JSON_VALUE_STATUS_NOT_FOUND);
			}
			
			jsonObj.put(JSON_KEY_LIST, jsonArr);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonObj;
	}
	
}
