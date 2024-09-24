package com.hnv.api.service.priv.mat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.hnv.api.def.DefAPI;
import com.hnv.api.def.DefJS;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.mat.TaMatMaterial;
import com.hnv.db.mat.TaMatPrice;
import com.hnv.db.mat.TaMatStock;
import com.hnv.db.mat.TaMatStockIo;
import com.hnv.db.mat.TaMatStockMonth;
import com.hnv.db.mat.vi.ViMatStockRQ;
import com.hnv.db.per.TaPerPerson;
import com.hnv.db.sor.TaSorOrder;
import com.hnv.db.sor.TaSorOrderDetail;
import com.hnv.db.sys.TaSysLock;
import com.hnv.def.DefDBExt;

public class ServiceMatStockRQ implements IService{ 
	
	public static final String SV_CLASS = "ServiceMatStockRQ".toLowerCase();

	public static final String SV_LST 			= "SVLst"		.toLowerCase();
	public static final String SV_FILTER 		= "SVFilter"	.toLowerCase();
	public static final String SV_FILTER_MONTH 	= "SVFilterMonth".toLowerCase();
	public static final String SV_TEST 			= "SVTest"		.toLowerCase();
	
	public static SimpleDateFormat changeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public ServiceMatStockRQ() {
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}
	
	@Override
	public void doService(JSONObject json, HttpServletResponse response)  throws Exception {
		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {
			if (sv.equals(SV_LST)) {
				svLstMatStockRQ(user, json, response);
				
			} else if (sv.equals(SV_FILTER)) {
				doResume(user, json, response);
				
			} else if (sv.equals(SV_FILTER_MONTH)) {
				doResumeMonth(user, json, response);
				
			} else if (sv.equals(SV_TEST)) {
				doTest(user, json, response);
			
			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}
		} catch (Exception e) {
			API.doResponse(response, DefAPI.API_MSG_ERR_API);
			e.printStackTrace();
		}
	}
	
	private void doTest(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		
		Session sess 	= null;
		
		Integer begin      = json.containsKey("begin") ? Integer.parseInt(json.get("begin").toString()) : 0;
		Integer end      = json.containsKey("end") ? Integer.parseInt(json.get("end").toString()) : 10000;
		
		for (Integer i = begin; i < end; i++) {
			sess = TaSysLock.DAO.reqSessionCurrent();
			doNewMaterialTest(user, sess, i);
		}
		
		
		API.doResponse(response, ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE,
				DefAPI.SV_CODE_API_YES));
	}
	
	private void doNewMaterialTest(TaAutUser user, Session sess, Integer i) throws Exception {
		TaMatMaterial ent = new TaMatMaterial();
		
		ent.reqSet(TaMatMaterial.ATT_T_NAME_01, "Name01_" + i.toString());
		ent.reqSet(TaMatMaterial.ATT_T_NAME_02, "Name02_" + i.toString());
		ent.reqSet(TaMatMaterial.ATT_T_CODE_01, "Code01_" + i.toString());
		ent.reqSet(TaMatMaterial.ATT_T_CODE_02, "Code02_" + i.toString());
		ent.reqSet(TaMatMaterial.ATT_T_CODE_03, "Code03_" + i.toString());
		ent.reqSet(TaMatMaterial.ATT_T_CODE_04, "Code04_" + i.toString());
		ent.reqSet(TaMatMaterial.ATT_T_INFO_01, "Chai");
		
		Random rand = new Random();

		ent.reqSet(TaMatMaterial.ATT_I_PER_MANAGER,  (Integer)rand.nextInt(20) + 1);
		ent.reqSet(TaMatMaterial.ATT_I_AUT_USER_01, 17);
		ent.reqSet(TaMatMaterial.ATT_I_AUT_USER_02, 17);
		
		ent.reqSet(TaMatMaterial.ATT_I_TYPE_01, 1);
		ent.reqSet(TaMatMaterial.ATT_I_TYPE_02, 1);
		ent.reqSet(TaMatMaterial.ATT_I_STATUS_01,  2);
		
		ent.reqSet(TaMatMaterial.ATT_D_DATE_01,  new Date());
		ent.reqSet(TaMatMaterial.ATT_D_DATE_02,  new Date());
		
		
		TaMatMaterial.DAO.doPersist(ent);
	}
	
	private void svLstMatStockRQ(TaAutUser user, JSONObject json, HttpServletResponse response)	throws Exception {
		
		Double qZero = json.get("quantZero")== null? 0: Double.parseDouble(json.get("quantZero").toString())-1;
 		String query = reqRestrictionFilterMatStockRQ(null, qZero);

		List<ViMatStockRQ> lst = ViMatStockRQ.DAO.reqList(0, 200,query);;
		if (lst == null){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
		
		API.doResponse(response, ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE,
				DefAPI.SV_CODE_API_YES, DefJS.RES_DATA, lst));
	}
	
	//------------------------------------------------------------------------------------------------------------------------------
	private static	String sqlPri =	"Select "	
			+ "p." 			+ TaMatPrice.COL_I_ID					+ " "	 		
			+ "from " 		+ DefDBExt.TA_MAT_PRICE 				+ " p" 		+ " "
			+ "where (p." 	+ TaMatPrice.COL_I_STATUS 				+ " = " 				+ TaMatPrice.STAT_ACTIV 	+ " "
			+ "and p." 		+ TaMatPrice.COL_I_MAT_MATERIAL 		+ " = " 	+ "mat."	+ TaMatMaterial.COL_I_ID 	+ ") "
			+ "ORDER BY "	+ TaMatPrice.COL_I_PRIORITY				+ "," 		+ TaMatPrice.COL_F_VAL_00				+ " "
			+ "LIMIT 1";
	
	private static	String sqlMain = "Select "
			+ "stk." 		+ TaMatStock.COL_I_ID					+ " as " 	+ ViMatStockRQ.COL_I_ID 			+ ", "
			+ "stk." 		+ TaMatStock.COL_I_MAT_MATERIAL 		+ " as " 	+ ViMatStockRQ.COL_I_MAT_MATERIAL 	+ ", "
			+ "sIO." 		+ TaMatStockIo.COL_I_MAT_STOCK 			+ " as " 	+ ViMatStockRQ.COL_I_MAT_STOCK 		+ ", "
			+ "sIO." 		+ TaMatStockIo.COL_I_SOR_ORDER 			+ " as " 	+ ViMatStockRQ.COL_I_SOR_ORDER 		+ ", "
			+ "sIO." 		+ TaMatStockIo.COL_I_SOR_ORDER_DETAIL 	+ " as " 	+ ViMatStockRQ.COL_I_SOR_ORDER_DETAIL 	+ ", "
			+ "per1." 		+ TaPerPerson.COL_I_ID 					+ " as " 	+ ViMatStockRQ.COL_I_PER_PERSON_01 		+ ", "
			+ "per2."		+ TaPerPerson.COL_I_ID 					+ " as " 	+ ViMatStockRQ.COL_I_PER_PERSON_02 		+ ", "
			
			+ "sIO." 		+ TaMatStockIo.COL_F_VAL_01 			+ " as " 	+ ViMatStockRQ.COL_F_VAL_00 	+ ", " //-- q input
			+ "sIO." 		+ TaMatStockIo.COL_F_VAL_02 			+ " as " 	+ ViMatStockRQ.COL_F_VAL_01 	+ ", " //-- ratio input
			+ "sIO." 		+ TaMatStockIo.COL_F_VAL_03 			+ " as " 	+ ViMatStockRQ.COL_F_VAL_02 	+ ", " //-- qBase input
			+ "oDet." 		+ TaSorOrderDetail.COL_F_VAL_01 		+ " as " 	+ ViMatStockRQ.COL_F_VAL_03 	+ ", " //-- price HT
			+ "oDet." 		+ TaSorOrderDetail.COL_F_VAL_02 		+ " as " 	+ ViMatStockRQ.COL_F_VAL_04 	+ ", " //-- price VAT
			+ "oDet." 		+ TaSorOrderDetail.COL_F_VAL_09 		+ " as " 	+ ViMatStockRQ.COL_F_VAL_05 	+ ", " //-- amount VAT
			
			
			+ "(stk." 		+ TaMatStock.COL_F_VAL_03 + " - " 				
			+ "stk." 		+ TaMatStock.COL_F_VAL_04 				+ ") as " 	+ ViMatStockRQ.COL_F_VAL_06 	+ ", " //-- qbase sell
			+ "stk." 		+ TaMatStock.COL_F_VAL_04 				+ " as " 	+ ViMatStockRQ.COL_F_VAL_07 	+ ", " //-- rqBase
			
			+ "pri." 		+ TaMatPrice.COL_F_VAL_00 				+ " as " 	+ ViMatStockRQ.COL_F_VAL_08 	+ ", " //-- ratio sell
			+ "pri." 		+ TaMatPrice.COL_F_VAL_02 				+ " as " 	+ ViMatStockRQ.COL_F_VAL_09 	+ ", " //-- price sell
			
			+ "oDet." 		+ TaSorOrderDetail.COL_F_VAL_06 		+ " as " 	+ ViMatStockRQ.COL_F_VAL_10 	+ ", "
			+ "oDet." 		+ TaSorOrderDetail.COL_F_VAL_00  		+ " * " 
			+ "oDet." 		+ TaSorOrderDetail.COL_F_VAL_01			+ " as " 	+ ViMatStockRQ.COL_F_VAL_11 	+ ", "
			
			+ "mat." 		+ TaMatMaterial.COL_T_NAME_01			+ " as " 	+ ViMatStockRQ.COL_T_INFO_01 	+ ", "
			+ "mat." 		+ TaMatMaterial.COL_T_NAME_02			+ " as " 	+ ViMatStockRQ.COL_T_INFO_02 	+ ", "
			+ "mat." 		+ TaMatMaterial.COL_T_INFO_01			+ " as " 	+ ViMatStockRQ.COL_T_INFO_03 	+ ", "
			+ "oDet." 		+ TaSorOrderDetail.COL_T_INFO_03		+ " as " 	+ ViMatStockRQ.COL_T_INFO_04 	+ ", "
			+ "pri." 		+ TaMatPrice.COL_T_INFO_01				+ " as " 	+ ViMatStockRQ.COL_T_INFO_05 	+ ", "
			
			
			+ "per1." 		+ TaPerPerson.COL_T_NAME_01				+ " as " 	+ ViMatStockRQ.COL_T_INFO_06 	+ ", "
			+ "per2." 		+ TaPerPerson.COL_T_NAME_01				+ " as " 	+ ViMatStockRQ.COL_T_INFO_07 	+ ", "
			
//			+ "concat(" + "per1." 		+ TaPerPerson.COL_T_NAME_01	+ ","			
//			+ "per1." 		+ TaPerPerson.COL_T_NAME_02	+ ") "		+ " as " 	+ ViMatStockRQ.COL_T_INFO_06 	+ ", "
//			+ "concat(" + "per2." 		+ TaPerPerson.COL_T_NAME_01	+ ","			
//			+ "per2." 		+ TaPerPerson.COL_T_NAME_02	+ ") "		+ " as " 	+ ViMatStockRQ.COL_T_INFO_07 	+ ", "
			
			+ "mat." 		+ TaMatMaterial.COL_T_CODE_01 			+ " as " 	+ ViMatStockRQ.COL_T_CODE_01 	+ ", "
			+ "mat." 		+ TaMatMaterial.COL_T_CODE_02 			+ " as " 	+ ViMatStockRQ.COL_T_CODE_02 	+ ", "
			+ "mat." 		+ TaMatMaterial.COL_T_CODE_03 			+ " as " 	+ ViMatStockRQ.COL_T_CODE_03 	+ ", "
			+ "ord." 		+ TaSorOrder.COL_T_CODE_01	 			+ " as " 	+ ViMatStockRQ.COL_T_CODE_04 	+ ", "
			
			+ "oDet." 		+ TaSorOrderDetail.COL_D_DATE_01		+ " as " 	+ ViMatStockRQ.COL_D_DATE_01 	+ ", "
			+ "oDet." 		+ TaSorOrderDetail.COL_D_DATE_02		+ " as " 	+ ViMatStockRQ.COL_D_DATE_02 	+ ", "
			
			+ "ord." 		+ TaSorOrder.COL_D_DATE_03				+ " as " 	+ ViMatStockRQ.COL_D_DATE_03 	+ ", "
			+ "ord." 		+ TaSorOrder.COL_D_DATE_01				+ " as " 	+ ViMatStockRQ.COL_D_DATE_04 	+ ", "
			+ "ord." 		+ TaSorOrder.COL_D_DATE_02				+ " as " 	+ ViMatStockRQ.COL_D_DATE_05 	+ " "
			
			+ "from " 		+ DefDBExt.TA_MAT_STOCK 				+ " stk" 				+ " "
			+ "inner join " + DefDBExt.TA_MAT_MATERIAL	 			+" mat on stk." 		+ TaMatStock.COL_I_MAT_MATERIAL			+" = mat." 	+ TaMatMaterial.COL_I_ID + " "
			
			+ "inner join " + DefDBExt.TA_MAT_STOCK_IO 				+" sIO on stk." 		+ TaMatStock.COL_I_ID 						+ " = sIO." 	+ TaMatStockIo.COL_I_MAT_STOCK + " "
																	+" and sIO."			+ TaMatStockIo.COL_I_TYPE + ">100 and sIO." + TaMatStockIo.COL_I_TYPE + " <200 " 
																	
			+ "inner join " + DefDBExt.TA_SOR_ORDER_DETAIL 			+" oDet on sIO." 		+ TaMatStockIo.COL_I_SOR_ORDER_DETAIL 	+" = oDet." + TaSorOrderDetail.COL_I_ID + " "
			+ "inner join " + DefDBExt.TA_SOR_ORDER		 			+" ord on sIO." 		+ TaMatStockIo.COL_I_SOR_ORDER 			+" = ord." 	+ TaSorOrder.COL_I_ID + " "
			
			+ "left join " + DefDBExt.TA_PER_PERSON	 				+" per1 on mat." 		+ TaMatMaterial.COL_I_PER_PERSON_01		+" = per1." + TaPerPerson.COL_I_ID + " "
			+ "left join " + DefDBExt.TA_PER_PERSON	 				+" per2 on ord." 		+ TaSorOrder.COL_I_PER_PERSON_01		+" = per2." + TaPerPerson.COL_I_ID + " "

			+ "left join " + DefDBExt.TA_MAT_PRICE	 				+" pri on (" 			+ sqlPri 					+ ") = pri."	+ TaMatPrice.COL_I_ID + " "	
			
			+ "where stk." + TaMatStock.COL_F_VAL_04 			+" > " ;
	
	
	private void doResume(TaAutUser user, JSONObject json, HttpServletResponse response)
			throws Exception {
		
		JSONObject 			obj 	= (JSONObject) json.get("obj");
		
		Double qZero 				= obj.get("quantZero")== null? 0 : Double.parseDouble(obj.get("quantZero").toString())-1;
		String queryFilter 			= reqRestrictionFilterMatStockRQ(obj, qZero);

		List<ViMatStockRQ> lst 		= ViMatStockRQ.DAO.reqList(0, 200, queryFilter);;

		if (lst == null){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
		
		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES, 
				DefJS.RES_DATA	, lst));
	}
	
	private String reqRestrictionFilterMatStockRQ(JSONObject cfg, Double quantMin)
			throws Exception {
		
		String  sql 	= sqlMain + quantMin;
		
		if (cfg!=null) {
			Integer idMat			= ToolData.reqInt(cfg.get("id"));
			
			String dt01_from 		= ToolData.reqStr(cfg.get("dt01_from"));
			String dt01_to 			= ToolData.reqStr(cfg.get("dt01_to"));
			String dt03_from 		= ToolData.reqStr(cfg.get("dt03_from"));
			String dt03_to 			= ToolData.reqStr(cfg.get("dt03_to"));
			Double quant			= ToolData.reqDouble(cfg.get("quant"));	
			
			Integer prodId			= ToolData.reqInt(cfg.get("prodId"));
			Integer suplId			= ToolData.reqInt(cfg.get("suplId"));
			
			String  matCode			= ToolData.reqStr(cfg.get("code"));
			String  matInf			= ToolData.reqStr(cfg.get("inf01"));
			
			
	        if (idMat != null) {
	        	sql += " and stk." 	+ TaMatStock.COL_I_MAT_MATERIAL +  " = " + idMat;
	        } else if (matInf!=null){
	        	matInf 	= "%" + matInf.toUpperCase() + "%";
	        	sql += " and (UPPER(mat." 	+ TaMatMaterial.COL_T_NAME_01 +  ") like '" + matInf + "' or UPPER(mat." 	+ TaMatMaterial.COL_T_NAME_02 +  ") like '" + matInf +"')";
	        }
			
	        if (dt01_from!=null) {
	        	sql +=  " and ord." 		+ TaSorOrder.COL_D_DATE_01 + " >= '" + dt01_from + "'"; //dtCreate
	        }
	        
	        if (dt01_to!=null) {
	        	sql +=  " and  ord." 	+ TaSorOrder.COL_D_DATE_01 + " <= '" + dt01_to + "'"; //dtCreate
	        }
	        
	        if (dt03_from!=null) {
	        	sql +=  " and oDet." 	+ TaSorOrderDetail.COL_D_DATE_01 + " >= '" + dt03_from + "'"; //dtProd
	        }
	        if (dt03_to!=null) {
	        	sql +=  " and oDet." 	+ TaSorOrderDetail.COL_D_DATE_02 + " <= '" + dt03_to + "'"; //dtExp
	        }
	        
	        if (prodId!=null && prodId>0) {
	        	sql +=  " and  per1."		+ TaPerPerson.COL_I_ID +  " = " + prodId; //ViMatStockRQ.I_Per_Person_02
	        }
	        
	        if (suplId!=null && suplId>0) {
	        	sql +=  " and  per2."		+ TaPerPerson.COL_I_ID +  " = " + suplId; //ViMatStockRQ.I_Per_Person_02
	        }
	        
	        
	        if (matCode!=null) {
	        	sql += " and (";
	        	sql += " mat." 			+ TaMatMaterial.COL_T_CODE_01 	 + " like '%" + matCode + "%' "; //ViMatStockRQ.T_Code_01
	        	sql += " or mat." 		+ TaMatMaterial.COL_T_CODE_02 	 + " like '%" + matCode + "%' "; //ViMatStockRQ.T_Code_02
	        	sql += " or mat." 		+ TaMatMaterial.COL_T_CODE_03 	 + " like '%" + matCode + "%' "; //ViMatStockRQ.T_Code_03
	        	sql += " ) ";
	        }
	        
	        
	        if (quant!=null && quant>=0) {
	        	sql +=  " and  stk." 		+ TaMatStock.COL_F_VAL_04 + " <= " + quant; //ViMatStockRQ.F_Val_03
	        }
		}
        return sql ;
	}
	
	private void doResumeMonth(TaAutUser user, JSONObject json, HttpServletResponse response)
			throws Exception {
		
		JSONObject 			obj 	= (JSONObject) json.get("obj");
		
		String queryFilter = reqRestrictionFilterMatStockRQMonth(obj);

		List<TaMatStockMonth> lst = TaMatStockMonth.DAO.reqList(0, 200, queryFilter);;

		if (lst != null) {
			for (TaMatStockMonth e : lst) {
				e.doBuildMat();
				e.doBuildWarehouse();
			}
		}
		
		if (lst == null){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
		
		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT	, 1, 
				DefJS.SV_CODE	, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA	, lst));
	}
	
	private String reqRestrictionFilterMatStockRQMonth(JSONObject cfg)	throws Exception {
		
		String  sql 	= "Select * From TA_MAT_STOCK_MONTH stk ";
		sql += " INNER JOIN TA_MAT_MATERIAL mat on mat.I_ID = stk.I_Mat_Material ";
		sql += " WHERE TRUE ";
		if (cfg!=null) {

	        if (cfg.get("dt01") != null && cfg.get("dt01").toString().length()>0) {
	        	sql +=  " and stk." + TaMatStockMonth.COL_D_DATE_01 + " >= " + "'" + cfg.get("dt01") + "'";
	        }
	        
	        if (cfg.get("dt02") != null && cfg.get("dt02").toString().length()>0) {
	        	sql +=  " and stk."+ TaMatStockMonth.COL_D_DATE_01 + " <= " + "'" + cfg.get("dt02") + "'";
	        }
	        
	        if (cfg.get("code") != null && cfg.get("code").toString().length()>0) {
	        	String code = (String)cfg.get("code").toString().toUpperCase();
	        	sql += " and mat." 	+ TaMatMaterial.COL_T_CODE_01 	 + " like '%" + code + "%' "; //ViMatStockRQ.T_Code_01
	        }
		}
        return sql ;
	}
	
	
}
