package com.hnv.api.service.priv.mat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Restrictions;

import com.hnv.api.def.DefAPI;
import com.hnv.api.def.DefJS;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.data.json.JSONArray;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.mat.TaMatMaterial;
import com.hnv.db.mat.TaMatStock;
import com.hnv.db.mat.TaMatStockMonth;
import com.hnv.db.sor.TaSorOrder;

public class ServiceMatStock implements IService{ 
	
	public static final String SV_CLASS 	= "ServiceMatStock".toLowerCase();
	
	public static final String SV_CHECK 	= "SVCheck".toLowerCase();
	
	public static final String SV_INVENTORY = "SVInventory".toLowerCase();
	
	public ServiceMatStock() {
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}
	
	@Override
	public void doService(JSONObject json, HttpServletResponse response)  throws Exception {
		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {
			if (sv.equals(SV_CHECK)) {
				doCheck(user, json, response);
			
			} else if (sv.equals(SV_INVENTORY)) {
				doInventory(user, json, response);
					
			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}
		} catch (Exception e) {
			API.doResponse(response, DefAPI.API_MSG_ERR_API);
			e.printStackTrace();
		}
	}
	
	/*
	private static void svMatStockCheck(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
        
        	JSONArray 		matArray 	= ToolJSON.reqJSonArrayFromString((String) json.get("matArray")) == null ? new JSONArray() : ToolJSON.reqJSonArrayFromString((String) json.get("matArray"));
        	JSONArray 		rs 			= new JSONArray();
        	
        	for(int i = 0; i < matArray.size(); i++) {
        		JSONObject 			x 		= (JSONObject) matArray.get(i);
        		
        		Integer id 	 = x.get("id") != null && x.get("id").toString() != "" ? Integer.parseInt(x.get("id").toString()) : null; 
        		Integer quant = x.get("quant") != null && x.get("quant").toString() != "" ? Integer.parseInt(x.get("quant").toString()) : null;
                Integer wId  = x.get("wId") != null && x.get("wId").toString() != "" ? Integer.parseInt(x.get("wId").toString()) : null;
                Integer stat  = x.get("stat") != null && x.get("stat").toString() != "" ? Integer.parseInt(x.get("stat").toString()) : null;
                
                List<TaMatStock> stks ;
                
                if (wId != null && wId > 0) {
	                stks = TaMatStock.DAO.reqList(
	                		Order.asc(TaMatStock.ATT_D_DATE_04),
	    					Restrictions.eq(TaMatStock.ATT_I_MAT_MATERIAL, id),
	    					Restrictions.eq(TaMatStock.ATT_I_STATUS, TaMatStock.STAT_OK),
	    					Restrictions.gt(TaMatStock.ATT_F_VAL_04, 0.0),
	    					Restrictions.eq(TaMatStock.ATT_I_MAT_WAREHOUSE, wId));
                } else {
	                stks = TaMatStock.DAO.reqList(
	                		Order.asc(TaMatStock.ATT_D_DATE_04),
	    					Restrictions.eq(TaMatStock.ATT_I_MAT_MATERIAL, id),
	    					Restrictions.eq(TaMatStock.ATT_I_STATUS, TaMatStock.STAT_OK),
	    					Restrictions.gt(TaMatStock.ATT_F_VAL_04, 0.0));
                }
                
                Double rqTotal = (double) 0;
                for (TaMatStock s : stks) {
                	rqTotal += (Double)s.req(TaMatStock.ATT_F_VAL_04);
                }
                
                JSONObject  result = new JSONObject();
                boolean canStock = quant > rqTotal ? false : true;
                if (stat != null && stat == 0) canStock = true;
                result.put("matId", id);
                result.put("canStock", canStock);
                result.put("total", rqTotal);
                rs.add(result);
        	}
        
            API.doResponse(response, ToolJSON.reqJSonString(	
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, rs
					));
	}*/
	
	
	
	
	private static void doCheck(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		Integer			ordTyp03 	= ToolData.reqInt 		(json, "ordTyp03", 0);
		JSONArray 		matArray 	= ToolData.reqJsonArr	(json,  "matArray", null);
		JSONArray 		rs 			= new JSONArray();

		Hashtable<Integer, Double> quants 		= new Hashtable<Integer, Double>();
		Hashtable<Integer, Double> quantTotals 	= new Hashtable<Integer, Double>();
		Integer 					wareH  		= null;

		for(int i = 0; i < matArray.size(); i++) {
			JSONObject 			x 		= (JSONObject) matArray.get(i);

			Integer id 	 	= x.get("id") 	 != null && x.get("id")		.toString() != "" ? Integer.parseInt(x.get("id")		.toString()) : null; 
			Double 	quant 	= x.get("quant") != null && x.get("quant")	.toString() != "" ? Double.parseDouble(x.get("quant")	.toString()) : null;
			Integer wId  	= x.get("wId") 	 != null && x.get("wId")	.toString() != "" ? Integer.parseInt(x.get("wId")		.toString()) : null;

			quants.put(id, quant);
			if (wId !=null) wareH = wId;
		}

		Set<Integer> 		ids		= quants.keySet();

		if (ordTyp03==null || !ordTyp03.equals(TaSorOrder.TYPE_03_ORD_VOUCHER)){
			List<TaMatStock> 	stks ;
			if (wareH != null && wareH > 0) {
				stks = TaMatStock.DAO.reqList_In(TaMatStock.ATT_I_MAT_MATERIAL, ids,
						Restrictions.eq(TaMatStock.ATT_I_STATUS, TaMatStock.STAT_OK),
						Restrictions.gt(TaMatStock.ATT_F_VAL_04, 0.0),
						Restrictions.eq(TaMatStock.ATT_I_MAT_WAREHOUSE, wareH));
			} else {
				stks = TaMatStock.DAO.reqList_In(TaMatStock.ATT_I_MAT_MATERIAL, ids,
						Restrictions.eq(TaMatStock.ATT_I_STATUS, TaMatStock.STAT_OK),
						Restrictions.gt(TaMatStock.ATT_F_VAL_04, 0.0));
			}

			for (TaMatStock s : stks) {
				Integer matId 	= (Integer) s.req(TaMatStock.ATT_I_MAT_MATERIAL);
				Double 	rq		= (Double)s.req(TaMatStock.ATT_F_VAL_04);

				Double  rqT		= quantTotals.get(matId);
				if (rqT==null) rqT = 0.0;

				rqT += rq;
				quantTotals.put(matId, rqT);
			}

			for (Integer id : ids) {
				Double qOrd = quants.get(id);
				Double qT	= quantTotals.get(id);
				if (qT == null) qT = 0.0;

				JSONObject  result 		= new JSONObject();
				boolean 	canStock 	= qOrd > qT ? false : true;

				if (!canStock){
					//---check if material is service
					TaMatMaterial 	mat 	= TaMatMaterial.DAO.reqEntityByRef(id);
					Integer 		typ02 	= (Integer) mat.req(TaMatMaterial.ATT_I_TYPE_02);
					if (typ02!=null && typ02.equals(TaMatMaterial.TYPE_02_BOM)) canStock= true;
				}
				result.put("matId"		, id);
				result.put("canStock"	, canStock);
				result.put("total"		, qT);
				rs.add(result);
			}
		}else { //---ord voucher, we don't need check stock
			for (Integer id : ids) {
				Double qT	= quantTotals.get(id);
				if (qT == null) qT = 0.0;

				JSONObject  result 		= new JSONObject();
				result.put("matId"		, id);
				result.put("canStock"	, true);
				result.put("total"		, qT);
				rs.add(result);
			}
		}
		API.doResponse(response, ToolJSON.reqJSonString(	
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, rs
				));
	}
	
	//------------------------------------------------------------------------------------------------------------------
	
	private void doInventory(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		List<TaMatStockMonth> ent = reqMatStockMonth(user, json, response);
        
        if (ent==null){
        	API.doResponse(response,DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response,DefAPI.API_MSG_OK);
		}
	}
	
	private List<TaMatStockMonth> reqMatStockMonth(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		/*--  `F_Val_01` : 'QBaseIn tổng của tháng trước(tất cả Q nhập vào trong tháng trước)',
		--  `F_Val_02` : 'RQBase tổng của tháng trước',
		--  `F_Val_03` : 'QBaseOut tổng của tháng trước',

		--  `F_Val_00` :  Ratio của đv đang check nếu làm kiểm ke tồn kho
		--  `F_Val_04` :  Nếu kiểm kê thì giá trị hiện đang có tại cửa hàng*/
		
		JSONObject 	obj 	= (JSONObject) json.get("obj");
		
		JSONArray lstDetail 	= obj.get("details") == null ? new JSONArray() : (JSONArray) obj.get("details");
		if (lstDetail==null || lstDetail.size()==0) return null;
		
		List		<TaMatStockMonth> 			lstNew 			= new ArrayList<TaMatStockMonth> 	();
		
		for(int i = 0; i< lstDetail.size(); i++){
			JSONObject 			o 		= (JSONObject) lstDetail.get(i);
			Double val10 				= o.get("val10") != null && o.get("val10").toString() != "" ? Double.parseDouble(o.get("val10").toString()) : null;
			Double val00 				= o.get("val00") != null && o.get("val00").toString() != "" ? Double.parseDouble(o.get("val00").toString()) : null;
			Integer matId				= o.get("id") != null && o.get("id").toString() != "" ? Integer.parseInt(o.get("id").toString()) : null;

			TaMatStockMonth 	poO	= new TaMatStockMonth();
			poO	.reqSet(TaMatStockMonth.ATT_D_DATE_01		, new Date());
			poO	.reqSet(TaMatStockMonth.ATT_I_AUT_USER		, user.reqId());
			poO	.reqSet(TaMatStockMonth.ATT_I_PER_MANAGER	, user.reqPerManagerId());
			
			poO	.reqSet(TaMatStockMonth.ATT_I_MAT_MATERIAL	, matId);
			poO	.reqSet(TaMatStockMonth.ATT_F_VAL_00		, val10); //---quantity in order detail
			poO	.reqSet(TaMatStockMonth.ATT_F_VAL_04		, val00); //---ratio in order detail
				
			lstNew	.add	(poO);
		}
		
		TaMatStockMonth.DAO.doPersist		(lstNew);
		return lstNew;
	}
	
	
}
