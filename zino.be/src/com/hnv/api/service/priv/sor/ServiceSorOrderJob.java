package com.hnv.api.service.priv.sor;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.hnv.api.def.DefAPI;
import com.hnv.api.def.DefDB;
import com.hnv.api.def.DefJS;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.api.service.common.APIAuth;
import com.hnv.api.service.priv.fin.ServiceFinFinance;
import com.hnv.common.tool.ToolDBLock;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolDatatable;
import com.hnv.common.tool.ToolDate;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.common.tool.ToolSet;
import com.hnv.data.json.JSONArray;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.fin.TaFinFinance;
import com.hnv.db.mat.TaMatMaterial;
import com.hnv.db.mat.TaMatMaterialDetail;
import com.hnv.db.mat.TaMatPrice;
import com.hnv.db.mat.TaMatStockIo;
import com.hnv.db.msg.TaMsgMessage;
import com.hnv.db.nso.TaNsoOffer;
import com.hnv.db.nso.TaNsoPost;
import com.hnv.db.sor.TaSorDeal;
import com.hnv.db.sor.TaSorOrder;
import com.hnv.db.sor.TaSorOrderDetail;
import com.hnv.db.sor.vi.ViSorOrder;
import com.hnv.db.sys.TaSysLock;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.db.tpy.TaTpyInformation;
import com.hnv.def.DefDBExt;
import com.hnv.def.DefRight;


/**
 * ----- ServiceSorOrder by H&V
 * ----- Copyright 2017------------
 */
public class ServiceSorOrderJob implements IService {
	private static	String 			filePath		= null; 
	private	static	String 			urlPath			= null; 
	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 				= "EC_V3".toLowerCase();

	public static final String SV_CLASS 				= "ServiceSorOrderJob".toLowerCase();	

	//NEW CODE------------------------------------------------------------------------------------------------------------
	

	public static final String SV_NEW					= "SVNew"			.toLowerCase();
	public static final String SV_NEW_VENDOR			= "SVNewVendor"		.toLowerCase();
	
	public static final String SV_MOD					= "SVMod"			.toLowerCase();
	public static final String SV_MOD_STAT 				= "SVModStat"		.toLowerCase();
	public static final String SV_CANCEL				= "SVCancel"		.toLowerCase();
	
	public static final String SV_DEL					= "SVDel"			.toLowerCase();
	
	public static final String SV_CHECK_EXISTS			= "SVCheckExists"	.toLowerCase();
	

	public static final String SV_GET					= "SVGet"				.toLowerCase();
	public static final String SV_LST					= "SVLst"				.toLowerCase();//--tong ket xuat nhap kho
	public static final String SV_LST_BY_AUT_ID			= "SVLstByAutId"		.toLowerCase();

	public static final String SV_LST_DYN				= "SVLstDyn"			.toLowerCase();
	public static final String SV_LST_DYN_BY_PERSON		= "SVLstDynByPerson"	.toLowerCase();

	public static final String SV_LST_REP_PERSON		= "SVLstRepPerson"		.toLowerCase();

	

	// For shp sale
	
	public static final String SV_PING					= "SVPing"			.toLowerCase();

	public static final String SV_LCK_REQ				= "SVLckReq"	.toLowerCase();
	public static final String SV_LCK_SAV				= "SVLckSav"	.toLowerCase();
	public static final String SV_LCK_END				= "SVLckEnd"	.toLowerCase();
	public static final String SV_LCK_DEL				= "SVLckDel"	.toLowerCase();

	public static final String SV_INP_PRICE				= "SVLastInpPriceByMat"	.toLowerCase();

	public static final Integer	ENT_TYP					= DefDBExt.ID_TA_SOR_ORDER;
	//-----------------------------------------------------------------------------------------------
	private static final int SOR_INFO_TYPE_BANK_GROUP = 2;
	private static final int SOR_INFO_TYPE_BANK_ACC = 200000;

	private static final int SOR_GET = 1;
	private static final int SOR_NEW = 2;
	private static final int SOR_MOD = 3;
	private static final int SOR_DEL = 4;

	//-----------------------------------------------------------------------------------------------
	//-------------------------Default Constructor - Required -------------------------------------
	public ServiceSorOrderJob(){
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		//ToolLogServer.doLogInf("--------- "+ SV_CLASS+ ".doService --------------");
		if (filePath	==null) filePath		= API.reqContextParameter("INV_INVOICE_PATH_FILE");
		if (urlPath		==null) urlPath			= API.reqContextParameter("INV_INVOICE_PATH_URL");			


		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {

			if(sv.equals(SV_LCK_REQ)					&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doLckReq(user, json, response);
			} else if(sv.equals(SV_LCK_SAV)				&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doLckSav(user, json, response);
			} else if(sv.equals(SV_LCK_END)				&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doLckEnd(user, json, response);		
			} else if(sv.equals(SV_LCK_DEL)				&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doLckDel(user, json, response);
				
			}else if(sv.equals(SV_CHECK_EXISTS)					&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doCheckExists(user, json, response);
				
			}
			
			else if(sv.equals(SV_NEW)					&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doNew(user, json, response);
				
			} else if(sv.equals(SV_NEW_VENDOR)			&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doNewFromVendor(user, json, response);
				
			} else if(sv.equals(SV_MOD)					&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doMod(user, json, response);

			} else if(sv.equals(SV_MOD_STAT)			&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doModStatDetail(user, json, response);	

			} else if(sv.equals(SV_DEL)					&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doDel(user, json, response);


			} else if(sv.equals(SV_CANCEL)				&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doCancel(user, json, response);
			
			} else if(sv.equals(SV_GET)					&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
					doGet(user, json, response);
			} else if(sv.equals(SV_LST_DYN)				&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doLstDyn(user, json, response);

			} else if(sv.equals(SV_LST)					&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doLst(user, json, response);
			}else if(sv.equals(SV_LST_BY_AUT_ID)					&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doLstByAutId(user, json, response);
			}  else if(sv.equals(SV_LST_DYN_BY_PERSON)	&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doLstDynByPerson(user, json, response);

			} else if(sv.equals(SV_LST_REP_PERSON)		&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				doLstRepPerson(user, json, response);

			} else if(sv.equals(SV_INP_PRICE)				&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BUY_R)){
				svNewSorOrderInpPrice(user, json, response);

			} else if(sv.equals(SV_PING)				&& APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_)){
				API.doResponse(response, ToolJSON.reqJSonString(DefJS.SESS_STAT, 1, DefJS.SV_CODE, DefAPI.SV_CODE_API_YES));

			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}	
		}catch(Exception e){
			try {
				API.doResponse(response, DefAPI.API_MSG_ERR_API);
				e.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}


	}

	//--------------------------------------------------------------------------------------------------------
	private static Hashtable<String,Integer> mapCol = new Hashtable<String, Integer>(){
		{
			put("action", -1);
			put("code01", 0 );
			put("dt01", 1 );
			put("dt02", 2 );
			put("stat", 3 );
		}
	};

	// done
	private void doGet(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		Integer 			objId		= ToolData.reqInt	(json, "id"			, -1	);
		TaSorOrder 			ent 		= TaSorOrder.DAO.reqEntityByValue(TaSorOrder.ATT_I_ID, objId);

		if (ent != null) {
			//-----check right by type first
			Integer typ01 = (Integer) ent.req(TaSorOrder.ATT_I_TYPE_01);
			if (typ01 != null) {
				if (canAuthorizeWithRightsOne(user, typ01, SOR_GET) == false) ent = null;
			} else ent = null;
			//------------------------------------
		}

		if (ent==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		ent.doBuildDetails		(true);
		ent.doBuildSupplier		(true);
		ent.doBuildClient		(true);
		ent.doBuildTParty		(true);
		ent.doBuildWarehouse	(true);
		ent.doBuildUserCreate	(true);
		ent.doBuildDocuments	(true);

		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent 
		));
	}
	//--------------------------------------------------------------------------------------------------------

	private void doNew(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		TaSorOrder 	ent	 = reqNew(user, json);
		if (ent==null){
			API.doResponse(response, DefAPI.API_MSG_KO);
			return;
		}

		TaSysLock 	lock 	= ToolDBLock.reqLock (json, "lock", DefDB.DB_LOCK_NEW, ENT_TYP, (Integer)ent.reqID(), user.reqId(), user.reqStr(TaAutUser.ATT_T_LOGIN_01), null);
		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent,
				"lock"				, lock
		));
	}

	public static TaSorOrder reqNew(TaAutUser user, JSONObject json) throws Exception {
		JSONObject			obj		= ToolData.reqJson 		(json, "obj"		, null);

		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaSorOrder.class);
		TaSorOrder  		ent	 	= new TaSorOrder(attr);

		ent.reqSet(TaSorOrder.ATT_I_ID, 				null);
		ent.reqSet(TaSorOrder.ATT_D_DATE_01, 			new Date());
		ent.reqSet(TaSorOrder.ATT_I_AUT_USER_01, 		user.req(TaAutUser.ATT_I_ID));
		ent.reqSet(TaSorOrder.ATT_I_AUT_USER_02, 		ToolData.reqJson(json, "uId", null));
		ent.reqSet(TaSorOrder.ATT_I_PER_MANAGER, 		user.reqPerManagerId());
		
		ent.reqSet(TaSorOrder.ATT_I_STATUS, 			TaSorOrder.STAT_NEW);
		
		ent.reqSet(TaSorOrder.ATT_I_ENTITY_TYPE, 		DefDBExt.ID_TA_NSO_OFFER);

		if (ent.req(TaSorOrder.ATT_F_VAL_00) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_00, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_02) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_02, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_03) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_03, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_04) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_04, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_06) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_06, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_07) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_07, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_08) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_08, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_09) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_09, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_10) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_10, 0.0);
		ent.reqSet(TaSorOrder.ATT_F_VAL_01, 0.0); //recalculate after saving detail
		ent.reqSet(TaSorOrder.ATT_F_VAL_05, 0.0);


		if (ent.req(TaSorOrder.ATT_I_PER_PERSON_01) == null) ent.reqSet(TaSorOrder.ATT_I_PER_PERSON_01, 0);
		if (ent.req(TaSorOrder.ATT_I_PER_PERSON_02) == null) ent.reqSet(TaSorOrder.ATT_I_PER_PERSON_02, 0);
		if (ent.req(TaSorOrder.ATT_I_PER_PERSON_03) == null) ent.reqSet(TaSorOrder.ATT_I_PER_PERSON_03, 0);

		String code 	= ToolDate.reqString(new Date(), "yyMMddHHmmss");
		String oCode 	= (String) ent.req(TaSorOrder.ATT_T_CODE_01);
		if ( oCode == null || oCode.length()==0) {
			ent.reqSet(TaSorOrder.ATT_T_CODE_01, code);
		}

		TaSorOrder.DAO.doPersist(ent);
		
		TaMsgMessage.doNewNotification(
				ToolData.reqInt(json, "uId", null), //id ng nhan
				user.reqStr(TaAutUser.ATT_T_LOGIN_01), //userName ng gui
				ToolData.reqStr(json, "uAva", null), //ava ng gui
				TaMsgMessage.STAT_NOTI_NEW,
				ent.reqInt(TaSorOrder.ATT_I_ENTITY_TYPE),
				ent.reqInt(TaSorOrder.ATT_I_ENTITY_ID_02),
				ToolData.reqStr(json, "cod01", null),
				ToolData.reqStr(json, "tit01", null),
				DefDBExt.ID_TA_SOR_ORDER,
				ent.reqId(),
				TaSorOrder.STAT_NEW
				);

		// Add info client, supplier, tParty and matwarehouse
		ent.doBuildSupplier		(true);
		ent.doBuildClient		(true);
//		ent.doBuildUserCreate	(true);

		return ent;		
	}

	private void doNewFromVendor(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		TaSorOrder ent = reqNewVendor(user, json, response);

		if (ent==null){
			API.doResponse(response, ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO					
					));
		} else {

			API.doResponse(response, ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, ent
					));
		}
	}

	private TaSorOrder reqNewVendor(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		JSONObject		obj			= ToolData.reqJson 		(json, "obj"		, null);
		JSONArray 		lstDetail 	= ToolData.reqJsonArr	(obj,  "details"	, null);
		
		String 			addr01 		= ToolData.reqStr		(obj,  "addr01"		, "");
		String 			addr02 		= ToolData.reqStr		(obj,  "addr02"		, "");
		String 			addr03 		= ToolData.reqStr		(obj,  "addr03"		, "");
		
		String 			cmt01 		= ToolData.reqStr		(obj,  "cmt01"		, "");
		String 			cmt02 		= ToolData.reqStr		(obj,  "cmt02"		, "");
		
		Integer 		typ01 		= ToolData.reqInt		(obj,	"typ01"		, null); 
		Integer 		typ02 		= ToolData.reqInt		(obj,	"typ02"		, null); 
		Integer 		typ03 		= ToolData.reqInt		(obj,	"typ03"		, null);
		Integer 		typ04 		= ToolData.reqInt		(obj,	"typ04"		, null); //payment typ
		
		Integer 		uId01 		= ToolData.reqInt		(obj,	"uId01"		, null); 
		
		JSONArray 		docs 		= ToolData.reqJsonArr	(obj,  "files"		, null);
		JSONArray 		payments	= ToolData.reqJsonArr	(obj,  "payments"	, null);


		Map<String, Object> attr 	= API.reqMapParamsByClass(obj, TaSorOrder.class);
		TaSorOrder 			ent 	= new TaSorOrder(attr);
		
		if (	(typ03!=null && typ03.equals(TaSorOrder.TYPE_03_ORD_VOUCHER))||
				(typ04!=null && typ04.equals(TaSorOrder.TYPE_04_PAY_BY_VOUCHER))) {
			//---check if person is ok
			Integer cliId = (Integer) ent.req(TaSorOrder.ATT_I_PER_PERSON_02);
			if (cliId == null || cliId<=0)  return null;
		}

		String code = ToolDate.reqString(new Date(), "yyMMddHHmmss");
		ent.reqSet(TaSorOrder.ATT_T_CODE_01, "S-"+code);

		code 		= ToolDate.reqString(new Date(), "yyMMdd");
		ent.reqSet(TaSorOrder.ATT_T_CODE_02, code);

		
		ent.reqSet(TaSorOrder.ATT_T_INFO_01, addr01);
		ent.reqSet(TaSorOrder.ATT_T_INFO_02, addr02);
		ent.reqSet(TaSorOrder.ATT_T_INFO_03, addr03);
		ent.reqSet(TaSorOrder.ATT_T_INFO_04, cmt01);
		ent.reqSet(TaSorOrder.ATT_T_INFO_05, cmt02);

		ent.reqSet(TaSorOrder.ATT_I_TYPE_01, typ01);
		ent.reqSet(TaSorOrder.ATT_I_TYPE_02, typ02);

		ent.reqSet(TaSorOrder.ATT_D_DATE_01, new Date());
		ent.reqSet(TaSorOrder.ATT_D_DATE_02, new Date());

		if (uId01!=null) 
			ent.reqSet(TaSorOrder.ATT_I_AUT_USER_01, uId01);
		else
			ent.reqSet(TaSorOrder.ATT_I_AUT_USER_01, ent.req(TaSorOrder.ATT_I_AUT_USER_02));

		ent.reqSet(TaSorOrder.ATT_I_PER_MANAGER, user.reqPerManagerId());

		ent.reqSet(TaSorOrder.ATT_F_VAL_01, 0.0); //recalculate after sanving detail
		ent.reqSet(TaSorOrder.ATT_F_VAL_05, 0.0);

		if (ent.req(TaSorOrder.ATT_F_VAL_00) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_00, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_02) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_02, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_03) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_03, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_04) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_04, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_06) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_06, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_07) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_07, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_08) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_08, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_09) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_09, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_10) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_10, 0.0);

		if (ent.req(TaSorOrder.ATT_I_PER_PERSON_01) == null) ent.reqSet(TaSorOrder.ATT_I_PER_PERSON_01, 0);
		if (ent.req(TaSorOrder.ATT_I_PER_PERSON_02) == null) ent.reqSet(TaSorOrder.ATT_I_PER_PERSON_02, 0);
		if (ent.req(TaSorOrder.ATT_I_PER_PERSON_03) == null) ent.reqSet(TaSorOrder.ATT_I_PER_PERSON_03, 0);
		if (ent.req(TaSorOrder.ATT_I_PER_PERSON_04) == null) ent.reqSet(TaSorOrder.ATT_I_PER_PERSON_04, 0);
		
		ent.reqSet(TaSorOrder.ATT_I_ID, 	null);
		TaSorOrder.DAO.doPersist(ent);
		
		try {
			// save details of invoice including details, documents, information	, payment
			// Add New detail

			for(int i = 0; i< lstDetail.size(); i++){
				JSONObject 			o 		= (JSONObject) lstDetail.get(i);
				if (o.get("id") != null) {
					o.put("matId", Integer.parseInt(o.get("id").toString()));
					o.remove("id");
				}
			}
			
			List<TaSorOrderDetail> details = reqOrderDetailsNewMod(user, lstDetail, ent, true);
			ent.reqSet(TaSorOrder.ATT_O_DETAILS, details);

			Integer			dealId		= obj.get("dealId") == null ? null: Integer.parseInt(obj.get("dealId").toString());
			String 			dealCode 	= obj.get("deal") == null ? null: (String) obj.get("deal");

			TaSorDeal 		deal 		= dealId!=null? TaSorDeal.DAO.reqEntityByRef(dealId) : null;		
			if (deal==null)	deal 		= dealCode!=null?TaSorDeal.reqDealByCode(dealCode):null;

			// add or mod EntityState of order in this function (1 time only for this service)
			doOrderSysthesis(ent, details, deal, TaSorOrder.SOR_ORDER_ADD); // tính tổng thành tiền + check deal + update order info

			//----update Fin_Finance 
			if (typ03!=null && typ03.equals(TaSorOrder.TYPE_03_ORD_VOUCHER)) {
				//--check if order is buy voucher
				TaFinFinance fi = ServiceFinFinance.reqFinEntityForUpd(
						user.reqPerManagerId(), 
						ent.reqInt(TaSorOrder.ATT_I_PER_PERSON_02),
						new Date(), 
						ent.reqInt(TaSorOrder.ATT_I_ID), 
						ent.reqStr(TaSorOrder.ATT_T_CODE_01), 
						TaSorOrder.TYPE_01_IN_STK_BUY, 
						ent.reqDouble(TaSorOrder.ATT_F_VAL_06)); //--val for client is val before discount
				if (fi==null) {
					//---remove all order + detail
					TaSorOrder.DAO.doRemove(ent);
					TaSorOrderDetail.DAO.doRemove(details);
					return null;
				}else {
					TaFinFinance.DAO.doMerge(fi);
				}
			}else if (typ04!=null && typ04.equals(TaSorOrder.TYPE_04_PAY_BY_VOUCHER)) {
				TaFinFinance fi = ServiceFinFinance.reqFinEntityForUpd(
						user.reqPerManagerId(), 
						ent.reqInt(TaSorOrder.ATT_I_PER_PERSON_02),
						new Date(), 
						ent.reqInt(TaSorOrder.ATT_I_ID), 
						ent.reqStr(TaSorOrder.ATT_T_CODE_01), 
						TaSorOrder.TYPE_01_OU_STK_SELL, 
						ent.reqDouble(TaSorOrder.ATT_F_VAL_07));
				if (fi==null) {
					//---remove all order + detail
					TaSorOrder.DAO.doRemove(ent);
					TaSorOrderDetail.DAO.doRemove(details);
					return null;
				}else {
					TaFinFinance.DAO.doMerge(fi);
				}
			} 
			//-----gen stock IO -------------------------------------------
			if (typ03==null || !typ03.equals(TaSorOrder.TYPE_03_ORD_VOUCHER)){
				Integer stat = (Integer)ent.req(TaSorOrder.ATT_I_STATUS);
				if (stat == TaSorOrder.STAT_STOCK_ACCEPT) {
					reqGenerateStockIO(user, ent, details);
				}
			}
		}catch(Exception e) {
			TaSorOrder.DAO.doRemove(ent);
			ent = null;
		}
		return ent;
	}
	//---------------------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------------------
	public static List<TaMatStockIo> reqGenerateStockIO(TaAutUser user, TaSorOrder order, List<TaSorOrderDetail> orderDetails) throws Exception {

		Hashtable tabIO = new Hashtable();
		for (int i = 0; i < orderDetails.size(); i++) {
			TaSorOrderDetail e 		= orderDetails.get(i);
			Integer 		 stat	= (Integer)e.req(TaSorOrderDetail.ATT_I_STATUS);
			if (!stat.equals(TaSorOrderDetail.STAT_ACTIVE)) continue; 

			reqMatIdAndQ(tabIO, 
					(Integer)e.req(TaSorOrderDetail.ATT_I_MAT_MATERIAL), 
					(Integer)e.req(TaSorOrderDetail.ATT_I_ID), 
					(Double) e.req(TaSorOrderDetail.ATT_F_VAL_00), (Double)e.req(TaSorOrderDetail.ATT_F_VAL_10), 
					(Date)   e.req(TaSorOrderDetail.ATT_D_DATE_01), (Date)e.req(TaSorOrderDetail.ATT_D_DATE_02));
		}
		return reqGenerateStockIO (order, tabIO);
	}

	private static List<TaMatStockIo> reqGenerateStockIO (TaSorOrder order, Hashtable tabIO)throws Exception {
		List<TaMatStockIo> 	stkIOs 		= new ArrayList<TaMatStockIo>();
		Integer 			orderId 	= (Integer) order.req(TaSorOrder.ATT_I_ID);
		Integer 			manId 		= (Integer) order.req(TaSorOrder.ATT_I_PER_MANAGER);
		List<Integer> 		tabIOKeys 	= new ArrayList<Integer>(tabIO.keySet());
		for (int i = 0; i < tabIOKeys.size(); i++) {
			Integer matId = tabIOKeys.get(i);

			List<Object[]> vals = (List<Object[]>)tabIO.get(matId);
			for (int j = 0; j < vals.size(); j++) {
				Object[] o = vals.get(j);

				TaMatStockIo stockIo = new TaMatStockIo();
				stockIo.reqSet(TaMatStockIo.ATT_I_SOR_ORDER, orderId);
				stockIo.reqSet(TaMatStockIo.ATT_I_PER_MANAGER, manId);

				stockIo.reqSet(TaMatStockIo.ATT_I_MAT_MATERIAL, matId);

				stockIo.reqSet(TaMatStockIo.ATT_I_SOR_ORDER_DETAIL, (Integer)o[0]);

				stockIo.reqSet(TaMatStockIo.ATT_D_DATE_01, new Date());
				stockIo.reqSet(TaMatStockIo.ATT_D_DATE_02, new Date());
				stockIo.reqSet(TaMatStockIo.ATT_D_DATE_03, (Date)o[3]);
				stockIo.reqSet(TaMatStockIo.ATT_D_DATE_04, (Date)o[4]);

				Double val01 = (Double) o[1];
				Double val02 = (Double) o[2];

				stockIo.reqSet(TaMatStockIo.ATT_F_VAL_01, val01); // quantity
				stockIo.reqSet(TaMatStockIo.ATT_F_VAL_02, val02); // ratio
				stockIo.reqSet(TaMatStockIo.ATT_F_VAL_03, 
						(Double) stockIo.req(TaMatStockIo.ATT_F_VAL_01) * (Double) stockIo.req(TaMatStockIo.ATT_F_VAL_02)); // Q1 * R1 

				// RQ base of stock before operation 
				stockIo.reqSet(TaMatStockIo.ATT_F_VAL_04, 
						(Double) stockIo.req(TaMatStockIo.ATT_F_VAL_01) * (Double) stockIo.req(TaMatStockIo.ATT_F_VAL_02));

				stockIo.reqSet(TaMatStockIo.ATT_I_TYPE, order.req(TaSorOrder.ATT_I_TYPE_01));
				stockIo.reqSet(TaMatStockIo.ATT_I_STATUS, TaMatStockIo.STAT_WAITING); // default = waiting, using thread to update stat

				stockIo.reqSet(TaMatStockIo.ATT_I_MAT_WAREHOUSE, order.req(TaSorOrder.ATT_I_MAT_VAL_01));

				stkIOs.add(stockIo);
			}
		}

		TaMatStockIo.DAO.doPersist(stkIOs);

		order.reqSet(TaSorOrder.ATT_I_STATUS, TaSorOrder.STAT_COMPLETED);
		TaSorOrder.DAO.doMerge(order);

		return stkIOs;
	}

	public static List<TaMatStockIo> reqGenerateStockIO(TaAutUser user, TaSorOrder order, TaSorOrderDetail det) throws Exception {
		Hashtable tabIO = new Hashtable();
		Integer stat		= (Integer)det.req(TaSorOrderDetail.ATT_I_STATUS);
		if (!stat.equals(TaSorOrderDetail.STAT_ACTIVE)) return null;

		reqMatIdAndQ(tabIO, 
				(Integer)det.req(TaSorOrderDetail.ATT_I_MAT_MATERIAL), 
				(Integer)det.req(TaSorOrderDetail.ATT_I_ID), 
				(Double) det.req(TaSorOrderDetail.ATT_F_VAL_00), (Double)det.req(TaSorOrderDetail.ATT_F_VAL_10), 
				(Date)   det.req(TaSorOrderDetail.ATT_D_DATE_01), (Date)det.req(TaSorOrderDetail.ATT_D_DATE_02));


		return reqGenerateStockIO (order, tabIO);
	}

	public static Hashtable reqMatIdAndQ(Hashtable tabs, Integer parentMatId, Integer ordDetId, Double q, Double ratio, Date dtProd, Date dtExp) throws Exception {
		if (tabs == null) tabs = new Hashtable();
		TaMatMaterial mat = TaMatMaterial.DAO.reqEntityByValue(TaMatMaterial.ATT_I_ID, parentMatId);


		if ((int)mat.req(TaMatMaterial.ATT_I_TYPE_02) == TaMatMaterial.TYPE_02_SINGLE) {
			List<Object[]> vals = (List<Object[]>)tabs.get(mat.req(TaMatMaterial.ATT_I_ID));
			if (vals == null) {
				vals = new ArrayList<Object[]>();
				tabs.put(mat.req(TaMatMaterial.ATT_I_ID), vals);
			}
			vals.add(new Object[] { ordDetId,  q, ratio, dtProd, dtExp });

		} else if ((int)mat.req(TaMatMaterial.ATT_I_TYPE_02) == TaMatMaterial.TYPE_02_BOM) {
			//----get children
			List<TaMatMaterialDetail> dets = TaMatMaterialDetail.DAO.reqList(
					Restrictions.eq(TaMatMaterialDetail.ATT_I_MAT_MATERIAL_01, mat.req(TaMatMaterial.ATT_I_ID)));

			for (int i=0; i < dets.size(); i++) {
				TaMatMaterialDetail d = dets.get(i);
				Double qChild = (Double) d.req(TaMatMaterialDetail.ATT_F_VAL_01);
				Double rChild = (Double) d.req(TaMatMaterialDetail.ATT_F_VAL_02);
				reqMatIdAndQ(tabs, (Integer)d.req(TaMatMaterialDetail.ATT_I_MAT_MATERIAL_02), ordDetId,  q*ratio*qChild, rChild, dtProd, dtExp);
			}
		}
		return tabs;
	}

	//---------------------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------------------

	private static void doOrderSysthesis(TaSorOrder sor, List<TaSorOrderDetail> detailLst, TaSorDeal deal, int mode) throws Exception{
		double fNet 			= 0;
		double fDiscount 		= 0;
		double fTax 			= 0;
		double fFinal 			= 0;

		if(detailLst != null && detailLst.size() >0){
			for(TaSorOrderDetail d:detailLst){
				Double discount 	= (Double) d.req(TaSorOrderDetail.ATT_F_VAL_06) != null ? (Double) d.req(TaSorOrderDetail.ATT_F_VAL_06) : 0;
				Double totalHT 		= (Double) d.req(TaSorOrderDetail.ATT_F_VAL_07) != null ? (Double) d.req(TaSorOrderDetail.ATT_F_VAL_07) : 0;
				Double tax 			= (Double) d.req(TaSorOrderDetail.ATT_F_VAL_08) != null ? (Double) d.req(TaSorOrderDetail.ATT_F_VAL_08) : 0;
				Double totalTTC 	= (Double) d.req(TaSorOrderDetail.ATT_F_VAL_09) != null ? (Double) d.req(TaSorOrderDetail.ATT_F_VAL_09) : 0;

				fDiscount 	+= discount; 
				fNet 		+= totalHT; 
				fTax 		+= tax; 				
				fFinal 		+= totalTTC;
			}
		}
		sor.reqSet(TaSorOrder.ATT_F_VAL_00, (double)detailLst.size());
		sor.reqSet(TaSorOrder.ATT_F_VAL_01, fNet);
		sor.reqSet(TaSorOrder.ATT_F_VAL_02, fTax);

		double fDisc01 		= (double)sor.req(TaSorOrder.ATT_F_VAL_03);// discount before tax
		double fDisc02 		= (double)sor.req(TaSorOrder.ATT_F_VAL_04);// discount after tax

		if (fDisc02<fDisc01) fDisc02 = fDisc01;
		sor.reqSet(TaSorOrder.ATT_F_VAL_04, fDisc02); 

		double f05 = (fDisc02-fDisc01)+(fTax);		
		sor.reqSet(TaSorOrder.ATT_F_VAL_05, f05);

		sor.reqSet(TaSorOrder.ATT_F_VAL_06, fFinal);		
		sor.reqSet(TaSorOrder.ATT_F_VAL_07, fFinal - fDisc02);

		sor.reqSet(TaSorOrder.ATT_F_VAL_08, (Double) sor.req(TaSorOrder.ATT_F_VAL_08));
		if (sor.req(TaSorOrderDetail.ATT_F_VAL_08) != null) {
			Double val09 = (Double) sor.req(TaSorOrder.ATT_F_VAL_08) - (Double) sor.req(TaSorOrder.ATT_F_VAL_07);
			sor.reqSet(TaSorOrder.ATT_F_VAL_09, val09);
		}

		if (deal!=null) {
			//---aplly
			doOrderWithDeal(sor, detailLst, deal);
		}

		if (mode ==  TaSorOrder.SOR_ORDER_ADD){
			//			sor.reqSet(TaSorOrder.ATT_T_CODE_01, sor.req(TaSorOrder.ATT_T_CODE_01) + "-" + sor.req(TaSorOrder.ATT_I_ID).toString());
			sor.reqSet(TaSorOrder.ATT_T_CODE_02, sor.req(TaSorOrder.ATT_T_CODE_02) + "-" + sor.req(TaSorOrder.ATT_I_ID).toString());
		}
		TaSorOrder.DAO.doMerge(sor);
	}

	private static void doOrderWithDeal(TaSorOrder sor, List<TaSorOrderDetail> detailLst, TaSorDeal deal) throws Exception{
		//calcul aldready from UI.

		Integer typ01 = (Integer) deal.req(TaSorDeal.ATT_I_TYPE_01);
		Integer typ02 = (Integer) deal.req(TaSorDeal.ATT_I_TYPE_02);
		//		Double	val00 = (Double) deal.req(TaSorDeal.ATT_F_VAL_00);
		//		Double	val01 = (Double) deal.req(TaSorDeal.ATT_F_VAL_01);


		if (typ02.equals(TaSorDeal.TYPE_02_DEAL_PRIVATE)) {
			deal.reqSet(TaSorDeal.ATT_I_STATUS, TaSorDeal.STAT_INACTIVE);
		}

		String desc02 = (String) deal.req(TaSorDeal.ATT_T_INFO_02)	;
		if (desc02==null) desc02= "[]";

		JSONArray 	lst = ToolJSON.reqJSonArrayFromString(desc02);
		JSONObject 	ord = new JSONObject();
		ord.put("id", sor.reqRef());
		lst.add(ord);
		deal.reqSet(TaSorDeal.ATT_T_INFO_02, lst.toJSONString());

		TaSorDeal.DAO.doMerge(deal);
	}

	private static List<TaSorOrderDetail> reqOrderDetailsNewMod(TaAutUser user, JSONArray lstDetail, TaSorOrder ent, boolean isNew) throws Exception{
		if (lstDetail==null || lstDetail.size()==0) return null;

		Integer sorId  = (Integer) ent.req(TaSorOrder.ATT_I_ID);
		List		<TaSorOrderDetail> 			lstMod 			= new ArrayList<TaSorOrderDetail > 	();
		List		<Map<String, Object>> 		lstModVal 		= new ArrayList<Map<String, Object>> ();
		List		<TaSorOrderDetail> 			lstNew 			= new ArrayList<TaSorOrderDetail > 	();
		List		<TaSorOrderDetail> 			lstReturn 		= new ArrayList<TaSorOrderDetail > 	();
		Collection	<TaSorOrderDetail> 			lstDel 			= null;
		List		<TaSorOrderDetail>  		lstObj 			= TaSorOrderDetail.DAO.reqList(Restrictions.eq(TaSorOrderDetail.ATT_I_SOR_ORDER, sorId));		


		Integer ordStat = ent.reqInt(ent, TaSorOrder.ATT_I_STATUS);
		if (ordStat.equals(TaSorOrder.STAT_COMPLETED))  return lstObj;

		HashMap		<Integer,TaSorOrderDetail> 	map 			= new HashMap<Integer,TaSorOrderDetail>();
		if (lstObj!=null){
			for(TaSorOrderDetail d:lstObj){
				Integer id = (Integer) d.req(TaSorOrderDetail.ATT_I_ID);
				map.put(id, d);			 
			}
		}

		for(int i = 0; i< lstDetail.size(); i++){
			JSONObject 			o 		= (JSONObject) lstDetail.get(i);
			Map<String, Object> oDet 	= API.reqMapParamsByClass(o, TaSorOrderDetail.class);
			//if (!checkValue(newD)) continue;
			Integer 			id		= (Integer) oDet.get(TaSorOrderDetail.ATT_I_ID);
			if (id==null || id<=0){
				if (ordStat.equals(TaSorOrder.STAT_STOCK_COMPLETED)) continue; //không lưu thêm
				oDet.remove(TaSorOrderDetail.ATT_I_ID);
				id = null;
			}
			if (ordStat.equals(TaSorOrder.STAT_STOCK_COMPLETED)){
				//khong duoc thay dổi so luong, stat, và matId
				oDet.remove(TaSorOrderDetail.ATT_I_STATUS);
				oDet.remove(TaSorOrderDetail.ATT_I_MAT_MATERIAL);
				oDet.remove(TaSorOrderDetail.ATT_F_VAL_00);
				oDet.remove(TaSorOrderDetail.ATT_F_VAL_10);
			}

			if (map.containsKey(id)){
				lstMod		.add(map.get(id));
				lstModVal	.add(oDet);
				map			.remove(id);
			}else{
				TaSorOrderDetail poO	= new TaSorOrderDetail(oDet);
				poO	.reqSet(TaSorOrderDetail.ATT_I_SOR_ORDER	, sorId);
				poO	.reqSet(TaSorOrderDetail.ATT_I_ID			, null);
				poO	.reqSet(TaSorOrderDetail.ATT_I_PRIORITY		, i+1);

				if ((Integer)poO.req(TaSorOrderDetail.ATT_I_STATUS) >= TaSorOrderDetail.STAT_ACTIVE) { 
					JSONObject inf06 = new JSONObject();
					inf06.put("stat"	, poO.req(TaSorOrderDetail.ATT_I_STATUS));
					inf06.put("uId"		, user.req(TaAutUser.ATT_I_ID));
					inf06.put("uName"	, user.req(TaAutUser.ATT_T_LOGIN_01));
					inf06.put("dt"		, ToolDate.reqString(new Date(), ToolDate.FORMAT_ISO));
					poO	.reqSet(TaSorOrderDetail.ATT_T_INFO_06		, inf06.toString());
				}

				lstNew	.add	(poO);
			}
		}

		if (map.size()>0){
			lstDel = map.values();			
		}

		Session sess = TaSorOrderDetail.DAO.reqSessionCurrent();
		try {
			TaSorOrderDetail.DAO.doMerge		(sess, lstMod, lstModVal);
			TaSorOrderDetail.DAO.doPersist		(sess, lstNew);
			TaSorOrderDetail.DAO.doRemove		(sess, lstDel);			
			TaSorOrderDetail.DAO.doSessionCommit(sess);

			lstReturn.addAll(lstNew);
			lstReturn.addAll(lstMod);
			return lstReturn;
		}catch(Exception e){
			TaSorOrderDetail.DAO.doSessionRollback(sess);
		}
		return null;
	}

	private static TaSorOrder reqOrderInFromTransfert( TaAutUser user, TaSorOrder ordOut, List<TaSorOrderDetail> listOut) throws Exception{
		if (ordOut==null || listOut==null || listOut.size()==0) return null;
		Session sess = null;
		TaSorOrder ordIn = null;
		List<TaSorOrderDetail> listIn = null;
		try {
			sess =  TaSorOrder.DAO.reqSessionCurrent();

			ordIn = ordOut.reqClone();

			String code 	= ToolDate.reqString(new Date(), "yyMMddHHmmss");
			ordIn.reqSet(TaSorOrder.ATT_T_CODE_01, "T-"+code);
			code 			= (String) ordIn.req(TaSorOrder.ATT_T_CODE_02);
			if ( code == null || code.length()==0) {
				ordIn.reqSet(TaSorOrder.ATT_T_CODE_02,  ToolDate.reqString(new Date(), "yyMMdd"));
			}

			ordIn.reqSet(TaSorOrder.ATT_I_STATUS, 		TaSorOrder.STAT_NEW);
			ordIn.reqSet(TaSorOrder.ATT_I_TYPE_01, 		TaSorOrder.TYPE_01_IN_STK_TRANSFER);
			ordIn.reqSet(TaSorOrder.ATT_I_MAT_VAL_01, 	ordOut.req(TaSorOrder.ATT_I_MAT_VAL_02));
			ordIn.reqSet(TaSorOrder.ATT_I_PARENT, 		ordOut.reqID());
			TaSorOrder.DAO.doPersist(sess, ordIn);

			listIn = new ArrayList<TaSorOrderDetail>();
			for(TaSorOrderDetail d:listOut){
				TaSorOrderDetail det = d.reqClone();
				det.reqSet(TaSorOrderDetail.ATT_I_SOR_ORDER, ordIn.req(TaSorOrder.ATT_I_ID));
				listIn.add(det);
			}
			TaSorOrderDetail.DAO.doPersist(sess, listIn);
			ordIn.reqSet(TaSorOrder.ATT_O_DETAILS, listIn);


			TaSorOrder.DAO.doSessionCommit(sess);
		}catch(Exception e) {
			if (sess!=null) TaSorOrder.DAO.doSessionRollback(sess); 
			ordIn 	= null;
			listIn 	= null;
			e.printStackTrace();
		}

		return ordIn;
	}

	//---------------------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------------------

	private void doModStatDetail(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		Integer 		entId 		= ToolData.reqInt		(json,	"id"		, null); 
		JSONArray 		lst 		= ToolData.reqJsonArr	(json,  "data"		, null);
		
		TaSorOrder 		ent			= TaSorOrder.DAO.reqEntityByRef(entId);

		if (ent == null) {
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		if (lst!=null && lst.size() > 0) {
			Set<Integer> 					ids 	= ToolSet.reqSetInt(lst, "id");
			Map<Integer, TaSorOrderDetail> 	tabs 	= TaSorOrderDetail.DAO.reqMap(Restrictions.in(TaSorOrderDetail.ATT_I_ID, ids));
			for (int i = 0; i < lst.size(); i++) {
				JSONObject  tmp 	= (JSONObject) lst.get(i);
				Integer 	objId 	= ToolData.reqInt	(tmp, "id"		, null);
				JSONObject  obj		= ToolData.reqJson 	(tmp, "inf06"	, null);

				TaSorOrderDetail det = tabs.get(objId);

				if (det!=null) {
					Integer oldStat = (Integer) det.req(TaSorOrderDetail.ATT_I_STATUS);
					if (oldStat!=null && !oldStat.equals(TaSorOrderDetail.STAT_NEW)) 
						continue;

					Integer stat   = ToolData.reqInt	(obj, "stat"		, null); 
					det.reqSet(TaSorOrderDetail.ATT_I_STATUS	, stat);
					det.reqSet(TaSorOrderDetail.ATT_T_INFO_06	, obj.toString());
					
					TaSorOrderDetail.DAO.doMerge(det);
					reqGenerateStockIO(user, ent, det);
				}
			}
		}

		ent.doBuildDetails		(true);
		
		API.doResponse(response, ToolJSON.reqJSonString(
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA		, ent
				));
	}

	private void doMod(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		TaSorOrder 		ent			= reqMod	(user, json);
		if (ent==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response, ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, ent
			));	
		}
	}
	
	public static TaSorOrder reqMod(TaAutUser user, JSONObject json) throws Exception {
		JSONObject			obj			= ToolData.reqJson (json, "obj"	, null);
		Map<String, Object> attr 		= API.reqMapParamsByClass(obj, TaNsoPost.class);
		
		Integer 			idMan 		= user.reqPerManagerId();		
		int					entId 		= ToolData.reqInt		(obj,  "id"			, null);
		JSONArray 			lstDetail 	= ToolData.reqJsonArr	(obj,  "details"	, null);
		JSONArray 			docs 		= ToolData.reqJsonArr	(obj,  "files"		, null);
		JSONArray 			payments	= ToolData.reqJsonArr	(obj,  "payments"	, null);

		TaSorOrder  		ent	 		= TaSorOrder.DAO.reqEntityByRef(entId);
		if (ent != null) {
			//-----check right by type first
			Integer typ01 = (Integer) ent.req(TaSorOrder.ATT_I_TYPE_01);
			if (typ01 != null) {
				if (canAuthorizeWithRightsOne(user, typ01, SOR_MOD) == false) return null;
				//				switch (typ01) {
				//				case TaSorOrder.TYPE_01_IN_STK_BUY 				: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BUY_M))  	return null; break;
				//				case TaSorOrder.TYPE_01_IN_STK_TRANSFER 		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_SHOP_M))  	return null; break;
				//				case TaSorOrder.TYPE_01_IN_STK_BALANCE_IN 		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BAL_M))  	return null; break;
				//				case TaSorOrder.TYPE_01_IN_STK_BALANCE_OTHER 	: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BAL_M))  	return null; break;
				//				
				//				case TaSorOrder.TYPE_01_OU_STK_SELL 			: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_SELL_M))  	return null; break;
				//				case TaSorOrder.TYPE_01_OU_STK_TRANSFER			: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_SHOP_M))  	return null; break;
				//				case TaSorOrder.TYPE_01_OU_STK_BALANCE_OUT		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_BAL_M))  	return null; break;
				//				case TaSorOrder.TYPE_01_OU_STK_BALANCE_FAIL		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_BAL_M))  	return null; break;
				//				case TaSorOrder.TYPE_01_OU_STK_BALANCE_OTHER	: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_BAL_M))  	return null; break;
				//				}
			} else return null;
			//------------------------------------
		}

		//---Dam bao cac thông tin tối thiểu không bị thay đổi 
		attr.remove(TaSorOrder.ATT_I_ID				);
		attr.remove(TaSorOrder.ATT_I_TYPE_03		);
		attr.remove(TaSorOrder.ATT_I_AUT_USER_01	);
		attr.remove(TaSorOrder.ATT_D_DATE_01		);
		attr.remove(TaSorOrder.ATT_I_PER_MANAGER	);
		
		Integer stat = ent.reqInt(TaSorOrder.ATT_I_STATUS);
		if (	stat.equals(TaSorOrder.STAT_STOCK_ACCEPT)|| 
				stat.equals(TaSorOrder.STAT_STOCK_COMPLETED)|| 
				stat.equals(TaSorOrder.STAT_COMPLETED)|| 
				stat.equals(TaSorOrder.STAT_CANCEL))
			attr.remove(TaSorOrder.ATT_I_STATUS	);

		TaSorOrder.DAO.doMerge(ent, attr);
		
		if (ent.req(TaSorOrder.ATT_F_VAL_00) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_00, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_02) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_02, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_03) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_03, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_04) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_04, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_08) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_08, 0.0);
		if (ent.req(TaSorOrder.ATT_F_VAL_10) == null) ent.reqSet(TaSorOrder.ATT_F_VAL_10, 0.0);
		ent.reqSet(TaSorOrder.ATT_F_VAL_01, 0.0); //recalculate after saving detail
		ent.reqSet(TaSorOrder.ATT_F_VAL_05, 0.0);
		
		
		//save details of invoice including details, documents, information	, payment
		//Add New detail
		List<TaSorOrderDetail> details = reqOrderDetailsNewMod(user, lstDetail, ent, false); 
		ent.reqSet(TaSorOrder.ATT_O_DETAILS, details);

		Integer			dealId		= obj.get("dealId") == null ? null: Integer.parseInt(obj.get("dealId").toString());
		String 			dealCode 	= obj.get("deal") == null ? null: (String) obj.get("deal");

		TaSorDeal 		deal 		= dealId!=null? TaSorDeal.DAO.reqEntityByRef(dealId) : null;		
		if (deal==null)	deal 		= dealCode!=null?TaSorDeal.reqDealByCode(dealCode):null;

		// add or mod EntityState of order in this function (1 time only for this service)
		doOrderSysthesis(ent, details, deal, TaSorOrder.SOR_ORDER_MOD);

		//-----gen stock IO -------------------------------------------
		Integer typ01	= ent.reqInt(TaSorOrder.ATT_I_TYPE_01);
		Integer wh01 	= ent.reqInt(TaSorOrder.ATT_I_MAT_VAL_01);
		Integer wh02 	= ent.reqInt(TaSorOrder.ATT_I_MAT_VAL_02);
		if (stat!=null && stat.equals(TaSorOrder.STAT_STOCK_ACCEPT)) {
			List<TaMatStockIo> stkIO = reqGenerateStockIO(user, ent, details);
		}

		//-----gen order in if order out for transfer and all stock Io for warehouse02------------------

		if (	stat !=null && stat .equals(TaSorOrder.STAT_STOCK_ACCEPT) && 	
				typ01!=null && typ01.equals(TaSorOrder.TYPE_01_OU_STK_TRANSFER)	&&  
				wh01 !=null && wh02!=null) {

			//---transfert from warehouse 01 to warehouse 02
			TaSorOrder ordIn = reqOrderInFromTransfert (user, ent, details);//--status new
		}

		//-------check file --------- ------------
		ent.reqSet(TaNsoPost.ATT_O_DOCUMENTS, TaTpyDocument.reqListCheck(DefAPI.SV_MODE_MOD, user, ENT_TYP, entId, docs));

		// Add info client, supplier, tParty and matwarehouse
		ent.doBuildSupplier		(true);
		ent.doBuildClient		(true);
		ent.doBuildTParty		(true);
		ent.doBuildWarehouse	(true);
		ent.doBuildUserCreate	(true);


		// Alarm
		if ((Integer)ent.req(TaSorOrder.ATT_I_STATUS) == TaSorOrder.STAT_STOCK_ACCEPT) {
			doNewAlarmPriceAcceptStock(user, ent);
		}

		return ent;		
	}	

	// THIẾU SERVICE XÓA SOR ORDER DETAIL
	private void doDel(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		int				entId 	= ToolData.reqInt(json, "id", null);
		TaSysLock 		lock 	= ToolDBLock.reqLock(ENT_TYP, entId, user.reqId(), user.reqStr(TaAutUser.ATT_T_LOGIN_01), null);
		if (lock==null || lock.reqStatus() == 0){
			API.doResponse(response, ToolJSON.reqJSonString(						
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO,
					DefJS.RES_DATA		, lock
					));	
			return;
		}

		if (!canDel(user, json)){
			API.doResponse(response,DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response,DefAPI.API_MSG_OK);
		}

		ToolDBLock.canDeleteLock(lock);
	}
	private static boolean canDel(TaAutUser user, JSONObject json) throws Exception {
		int				entId 	= ToolData.reqInt(json, "id", null);
		TaSorOrder  	ent	 	= TaSorOrder.DAO.reqEntityByRef(entId);

		if (ent != null) {
			//-----check right by type first
			Integer typ01 = (Integer) ent.req(TaSorOrder.ATT_I_TYPE_01);
			if (typ01 != null) {
				if (canAuthorizeWithRightsOne(user, typ01, SOR_DEL) == false) return false;
			} else return false;
			//------------------------------------
		}

		if (ent==null){
			return false;
		}

		//remove all other object connecting to this ent first-------
		Integer 	stat 		= (Integer)		ent.req(TaSorOrder.ATT_I_STATUS);

		if(		stat.equals(TaSorOrder.STAT_STOCK_ACCEPT )|| 
				stat.equals(TaSorOrder.STAT_STOCK_COMPLETED) || 
				stat.equals(TaSorOrder.STAT_COMPLETED  )){
			return false;
		}

		//---------detail....
		return canDel (ent);
	}
	private static boolean canDel (TaSorOrder ord) throws Exception{
		Integer 	entId 		= (Integer)		ord.req(TaSorOrder.ATT_I_ID);
		Session 	sess 		= null;
		try {
			sess = TaSorOrder.DAO.reqSessionCurrent();
			List<TaSorOrderDetail> 	dets = TaSorOrderDetail.DAO.reqList	(sess, Restrictions.eq(TaSorOrderDetail.ATT_I_SOR_ORDER, entId));
			
			TaTpyDocument.doListDel(sess, ENT_TYP, entId);
			
			TaSorOrderDetail.DAO.doRemove	(sess, dets);
			TaSorOrder		.DAO.doRemove	(sess, ord);	

			TaSorOrder		.DAO.doSessionCommit(sess);
		}catch (Exception e) {
			if (sess!=null) TaSorOrder.DAO.doSessionRollback(sess);
			return false;
		}

		return true;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------
	private void doCancel(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		int				entId 	= ToolData.reqInt(json, "id", null);
		TaSysLock 		lock 	= ToolDBLock.reqLock(ENT_TYP, entId, user.reqId(), user.reqStr(TaAutUser.ATT_T_LOGIN_01), null);
		if (lock==null || lock.reqStatus() == 0){
			API.doResponse(response, ToolJSON.reqJSonString(						
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO,
					DefJS.RES_DATA		, lock
					));	
			return;
		}
		
		if (!canCancel(user, json)){
			API.doResponse(response,DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response,DefAPI.API_MSG_OK);
		}

		ToolDBLock.canDeleteLock(lock);
	}
	
	private static boolean canCancel(TaAutUser user, JSONObject json) throws Exception {
		int				entId 	= ToolData.reqInt(json, "id", null);
		TaSorOrder  	ent	 	= TaSorOrder.DAO.reqEntityByRef(entId);

		if (ent != null) {
			//-----check right by type first
			Integer typ01 = (Integer) ent.req(TaSorOrder.ATT_I_TYPE_01);
			if (typ01 != null) {
				if (canAuthorizeWithRightsOne(user, typ01, SOR_DEL) == false) return false;
			} else return false;
			//------------------------------------
		}

		if (ent==null){
			return false;
		}

		//remove all other object connecting to this ent first-------
		Integer 	ordId 		= (Integer)		ent.req(TaSorOrder.ATT_I_ID);
		Integer 	stat 		= (Integer)		ent.req(TaSorOrder.ATT_I_STATUS);
		Integer 	type 		= (Integer)		ent.req(TaSorOrder.ATT_I_TYPE_01);
		if(		stat.equals(TaSorOrder.STAT_STOCK_ACCEPT )||  
				stat.equals(TaSorOrder.STAT_STOCK_COMPLETED) || 
				stat.equals(TaSorOrder.STAT_COMPLETED )){
			if (!user.canBeAdmin()) return false;

			//---tao lai stock
			List<TaSorOrderDetail> 	dets 	= TaSorOrderDetail.DAO.reqList	(Restrictions.eq(TaSorOrderDetail.ATT_I_SOR_ORDER, ordId));
			Set<Integer>			ids		= ToolSet.reqSetInt(dets, TaSorOrderDetail.ATT_I_ID);

			List<TaMatStockIo> 		ios 	= TaMatStockIo.DAO.reqList_In(TaMatStockIo.ATT_I_SOR_ORDER_DETAIL, ids, Restrictions.eq(TaMatStockIo.ATT_I_SOR_ORDER, ordId), Restrictions.eq(TaMatStockIo.ATT_I_TYPE, type));
			if (ios.size()>0) {
				Integer newTyp = null;
				if (	type.equals(TaSorOrder.TYPE_01_IN_STK_BUY) || 
						type.equals(TaSorOrder.TYPE_01_IN_STK_TRANSFER) ||
						type.equals(TaSorOrder.TYPE_01_IN_STK_BALANCE_IN) ||
						type.equals(TaSorOrder.TYPE_01_IN_STK_BALANCE_OTHER)) {
					newTyp = TaSorOrder.TYPE_01_OU_STK_BALANCE_OTHER;
				} else {
					newTyp = TaSorOrder.TYPE_01_IN_STK_BALANCE_OTHER;
				}

				for (TaMatStockIo io: ios) {
					io.reqSet(TaMatStockIo.ATT_I_ID		, null);
					io.reqSet(TaMatStockIo.ATT_I_TYPE	, newTyp);
					io.reqSet(TaMatStockIo.ATT_I_STATUS	, TaMatStockIo.STAT_WAITING);
				}
				TaMatStockIo.DAO.doPersist(ios);
			}
		}

		ent.reqSet(TaSorOrder.ATT_I_STATUS, TaSorOrder.STAT_CANCEL);
		TaSorOrder.DAO.doMerge(ent);	
		return true;
	}
	
	//--------------------------------------------------------------------------------------
	private void doCheckExists(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception {
		Integer entId 		= ToolData.reqInt(json, "entId"		, null);
		Integer entTyp 		= ToolData.reqInt(json, "entTyp"	, null);
		
		TaSorOrder order 	= TaSorOrder.DAO.reqEntityByValues(
									TaSorOrder.ATT_I_AUT_USER_01, user.reqId(),
									TaSorOrder.ATT_I_ENTITY_TYPE, entTyp,
									TaSorOrder.ATT_I_ENTITY_ID_02, entId
								);
		
		if(order == null) {
			API.doResponse(response,DefAPI.API_MSG_KO);
		} else {
			API.doResponse(response, ToolJSON.reqJSonString(		//filter,
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES
			));
		}
	}


	//---------------------------------------------------------------------------------------------------------------------------------------
	private void doLst(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
			String dt01_from 		= ToolData.reqStr(json, "dt01_from"	, null);
			String dt01_to 			= ToolData.reqStr(json, "dt01_to"	, null);
			String dt03_from 		= ToolData.reqStr(json, "dt03_from"	, null);
			String dt03_to 			= ToolData.reqStr(json, "dt03_to"	, null);

			String code 			= ToolData.reqStr(json, "code"		, null);
			String cliPhone 		= ToolData.reqStr(json, "phone"		, null);

			Integer uId				= ToolData.reqInt(json, "uId"		, null);
			Integer cliId			= ToolData.reqInt(json, "cliId"		, null);
			Integer supId			= ToolData.reqInt(json, "supId"		, null);
			Integer tpId			= ToolData.reqInt(json, "tpId"		, null);
			Integer type01			= ToolData.reqInt(json, "typ01"		, null);
			Integer type02			= ToolData.reqInt(json, "typ02"		, null);
			Integer type03			= ToolData.reqInt(json, "typ03"		, null);

			Integer fromId			= ToolData.reqInt(json, "fromId"	, null);
			Integer toId			= ToolData.reqInt(json, "toId"		, null);

			Integer matId			= ToolData.reqInt(json, "matId"		, null);
			Integer socId			= ToolData.reqInt(json, "socId"		, null);
			Integer stat 			= ToolData.reqInt(json, "stat"		, null);


			Integer manId 			= (Integer)user.req(TaAutUser.ATT_I_PER_MANAGER);
			if (socId != null) manId = socId;


			// ----------- CHECK-right-----------
			List<ViSorOrder> 	stockIOOrderList = null;
			if (type01 != null) {
				if (canAuthorizeWithRightsOne(user, type01, SOR_GET) == true) 
					stockIOOrderList 	= reqList(user, null, 
							fromId, toId, 
							type01, type02, type03, stat, 
							dt01_from, dt01_to, dt03_from, dt03_to, 
							manId, uId, matId, 
							supId, cliId, tpId,
							code, cliPhone );
			}

			// ----------------------------------

			if (stockIOOrderList==null ){
				API.doResponse(response,DefAPI.API_MSG_KO);
				return;
			}

			Integer iTotalRecords 			= reqListDynCountTotal(manId, type01, type02, type03, stat, dt01_from, dt01_to, dt03_from, dt03_to, supId);				
			Integer iTotalDisplayRecords 	= reqListDynCount(null, manId, type01, type02, type03, stat, dt01_from, dt01_to, dt03_from, dt03_to, code, cliPhone, cliId, uId);

			API.doResponse(response, ToolJSON.reqJSonString(		//filter,
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,					
					"iTotalRecords"				, iTotalRecords,
					"iTotalDisplayRecords"		, iTotalDisplayRecords,
					"aaData"					, stockIOOrderList
			));
	}
	
	private void doLstByAutId(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		Integer autId 			= user.reqId();
		
		if(autId == null) {
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
		
		List<TaSorOrder> lst 	= TaSorOrder.DAO.reqList(Restrictions.or(
				Restrictions.eq(TaSorOrder.ATT_I_AUT_USER_01, autId), 
				Restrictions.eq(TaSorOrder.ATT_I_AUT_USER_02, autId)));
		
		if(lst == null) {
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
		
		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT				, 1, 
				DefJS.SV_CODE				, DefAPI.SV_CODE_API_YES,
				DefJS.RES_DATA				, lst
		));
	}

	private void doLstDyn(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		Object[]  			dataTableOption = ToolDatatable.reqDataTableOption (json, mapCol);
		Set<String>			searchKey		= (Set<String>)dataTableOption[0];	
		int					searchOpt		= (int)dataTableOption[5];	
		
		
		String dt01_from 		= ToolData.reqStr(json, "dt01_from"	, null);
		String dt01_to 			= ToolData.reqStr(json, "dt01_to"	, null);
		String dt03_from 		= ToolData.reqStr(json, "dt03_from"	, null);
		String dt03_to 			= ToolData.reqStr(json, "dt03_to"	, null);

		String code 			= ToolData.reqStr(json, "code"		, null);
		String cliPhone 		= ToolData.reqStr(json, "phone"		, null);

		Integer wId				= ToolData.reqInt(json, "warehouseId", null);

		Integer uId				= ToolData.reqInt(json, "uId"		, null);
		Integer cliId			= ToolData.reqInt(json, "cliId"		, null);
		Integer supId			= ToolData.reqInt(json, "supId"		, null);
		Integer tpId			= ToolData.reqInt(json, "tpId"		, null);
		Integer type01			= ToolData.reqInt(json, "typ01"		, null);
		Integer type02			= ToolData.reqInt(json, "typ02"		, null);
		Integer type03			= ToolData.reqInt(json, "typ03"		, null);

		Integer fromId			= ToolData.reqInt(json, "fromId"	, null);
		Integer toId			= ToolData.reqInt(json, "toId"		, null);

		Integer matId			= ToolData.reqInt(json, "matId"		, null);
		Integer socId			= ToolData.reqInt(json, "socId"		, null);
		Integer stat 			= ToolData.reqInt(json, "stat"		, null);
		
		Integer manId 			= user.reqPerManagerId();
		if (socId != null) manId = socId;

		// ----------- CHECK-right-----------
		List<ViSorOrder> 	stockIOOrderList = null;
		if (type01 != null) {
			if (canAuthorizeWithRightsOne(user, type01, SOR_GET) == true) 
				stockIOOrderList 	= reqList(user, dataTableOption, 
						fromId, toId, 
						type01, type02, type03, stat, 
						dt01_from, dt01_to, dt03_from, dt03_to, 
						manId, uId, matId, 
						supId, cliId, tpId,
						code, cliPhone );
		}
		// ----------------------------------

		if (stockIOOrderList==null ){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		Integer iTotalRecords 			= reqListDynCountTotal(manId, type01, type02, type03, stat, dt01_from, dt01_to, dt03_from, dt03_to, supId);				
		Integer iTotalDisplayRecords 	= reqListDynCount(dataTableOption, manId, type01, type02, type03, stat, dt01_from, dt01_to, dt03_from, dt03_to, code, cliPhone, cliId, uId);

		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT				, 1, 
				DefJS.SV_CODE				, DefAPI.SV_CODE_API_YES,					
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, stockIOOrderList
		));
	}
	private static Integer reqListDynCountTotal(Integer manId, Integer typ01, Integer typ02, Integer typ03, Integer stat, String dt01_from, String dt01_to, String dt03_from, String dt03_to, Integer supId) throws Exception {
		Integer result = ViSorOrder.reqCountInvOrder(null, typ01, typ02, null, stat, manId, null, null, null, null);
		return result;
	}
	private static Integer reqListDynCount(Object[] dataTableOption, Integer manId, Integer typ01, Integer typ02, Integer typ03, Integer stat, String dt01_from, String dt01_to, String dt03_from, String dt03_to , String code, String cliPhone, Integer cliId, Integer uId) throws Exception {
		Set<String>			searchKey				= dataTableOption!=null?(Set<String>)dataTableOption[0]:null;
		Integer result = ViSorOrder.reqCountInvOrder(null, typ01, typ02, searchKey, stat, manId, code, cliPhone, cliId, uId);
		return result;
	}

	private static List<ViSorOrder> reqList(
			TaAutUser user,   Object[] dataTableOption,
			Integer fromId, Integer toId,
			Integer type01, Integer type02, Integer type03,  Integer status, 

			String dt01_from, String dt01_to, //ngay tao
			String dt03_from, String dt03_to, //ngay so sach

			Integer idMan, Integer uId, Integer matId,		
			Integer supId, Integer cliId, Integer tpId,  
			String ordCode, String cliPhone
			) throws Exception {
		Set<String>			searchKey				= dataTableOption!=null?(Set<String>)dataTableOption[0]: null;

		int 		begin 		= dataTableOption!=null?(int)	dataTableOption[1]:0;
		int 		number 		= dataTableOption!=null?(int)	dataTableOption[2]:1000; 
		int 		sortCol 	= dataTableOption!=null?(int)	dataTableOption[3]:2; 
		int 		sortTyp 	= dataTableOption!=null?(int)	dataTableOption[4]:0;

		String sortColName = ViSorOrder.ATT_T_CODE_01;
		String sortDir	   = null;

		switch(sortCol) {
		//		case 0: sortColName = ViSorOrder.ATT_I_ID; break;
		case 0: sortColName = ViSorOrder.ATT_T_CODE_01; break;
		case 1: sortColName = ViSorOrder.ATT_T_NAME_01; break;
		case 2: sortColName = ViSorOrder.ATT_D_DATE_01; break;
		case 3: sortColName = ViSorOrder.ATT_D_DATE_02; break;
		}

		if (sortColName != null) {
			switch(sortTyp) {
			case 0: sortDir = " ASC"; break;
			case 1: sortDir = " DESC"; break;								
			}
		}

		List<ViSorOrder> lst = ViSorOrder.reqListByFilterCondition(user, begin, number, searchKey, sortColName, sortDir, 
				fromId, toId,
				type01, type02, type03,  status, 

				dt01_from, dt01_to, dt03_from, dt03_to, 
				idMan, uId, 
				null, matId,
				supId, cliId, tpId,  
				ordCode, cliPhone);

		return lst;
	}

	// ...
	private void doLstDynByPerson(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		
		Object[]  			dataTableOption = ToolDatatable.reqDataTableOption (json, mapCol);
		Set<String>			searchKey		= (Set<String>)dataTableOption[0];	
		int					searchOpt		= (int)dataTableOption[5];	
		Integer				perId			= ToolData.reqInt	(json, "perId"	, null);
		Integer				typPerson		= ToolData.reqInt	(json, "typPerson"	, null);

		List<TaSorOrder> 	stockIOOrderLst = reqListDynByPerson(user, perId, typPerson, dataTableOption);

		if (stockIOOrderLst==null ){
			API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			return;
		}

		Integer iTotalRecords 			= reqListDynByPersonCountTotal(dataTableOption, perId, typPerson);				
		Integer iTotalDisplayRecords 	= reqListDynByPersonCount(dataTableOption, perId, typPerson);

		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT				, 1, 
				DefJS.SV_CODE				, DefAPI.SV_CODE_API_YES,					
				"iTotalRecords"				, iTotalRecords,
				"iTotalDisplayRecords"		, iTotalDisplayRecords,
				"aaData"					, stockIOOrderLst
				));
	}
	
	private static Integer reqListDynByPersonCountTotal(Object[] dataTableOption, Integer perId, Integer typPerson) throws Exception {
		Integer result = ViSorOrder.reqRestrictionSrc(perId, typPerson, null, -1);
		return result;
	}
	private static Integer reqListDynByPersonCount(Object[] dataTableOption, Integer perId, Integer typPerson) throws Exception {
		Set<String>			searchKey				= (Set<String>)dataTableOption[0];
		int searchOpt = (int)dataTableOption[5];
		Integer result = ViSorOrder.reqRestrictionSrc(perId, typPerson, searchKey, searchOpt);
		return result;
	}
	private static List<TaSorOrder> reqListDynByPerson(TaAutUser user,  Integer perId, Integer typPerson, Object[] dataTableOption) throws Exception {
		Set<String>	searchKey	= (Set<String>)dataTableOption[0];

		int 		begin 		= (int)	dataTableOption[1];
		int 		number 		= (int)	dataTableOption[2]; 
		int 		sortCol 	= (int)	dataTableOption[3]; 
		int 		sortTyp 	= (int)	dataTableOption[4];
		int 		searchOpt 	= (int) dataTableOption[5];

		String sortColName = ViSorOrder.ATT_T_CODE_01;
		String sortDir	   = null;

		switch(sortCol) {
		//		case 0: sortColName = ViSorOrder.ATT_I_ID; break;
		case 0: sortColName = ViSorOrder.ATT_T_CODE_01; break;
		case 1: sortColName = ViSorOrder.ATT_D_DATE_01; break;
		case 2: sortColName = ViSorOrder.ATT_D_DATE_02; break;
		case 3: sortColName = ViSorOrder.ATT_I_STATUS; break;
		}

		if (sortColName != null) {
			switch(sortTyp) {
			case 0: sortDir = " ASC"; break;
			case 1: sortDir = " DESC"; break;								
			}
		}

		List<TaSorOrder> lst = ViSorOrder.reqRestrictionSrc(begin, number, perId, typPerson, searchKey, sortColName, sortDir, searchOpt);
		return lst;
	}


	// IN PROGRESS
	private void doLstRepPerson(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		String dt01 			= ToolData.reqStr(json, "dt01"		, null);
		String dt02 			= ToolData.reqStr(json, "dt02"		, null);

		if (dt01==null) dt01 = ToolDate.reqString(ToolDate.reqDateByAdding(new Date(), 0, 0, -30, 0, 0, 0), "yyyy-MM-dd HH:mm:ss");
		if (dt02==null) dt02 = ToolDate.reqString(new Date(), "yyyy-MM-dd HH:mm:ss");
		
		String code 			= ToolData.reqStr(json, "code"		, null);
		Integer matId			= ToolData.reqInt(json, "matId"		, null);

		Integer perId			= ToolData.reqInt(json, "perId"		, null);
		Integer perTyp			= ToolData.reqInt(json, "perTyp"		, null);
		Integer socId			= ToolData.reqInt(json, "socId"		, null);
		
		Integer manId 			= user.reqPerManagerId();
		if (socId != null) manId = socId;

		List<ViSorOrder> 	lst 	= ViSorOrder.reqRestrictionFilterByPerson(manId, matId, dt01, dt02, perId, perTyp, code);

		if (lst==null||lst.size()==0){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}
		
		API.doResponse(response, ToolJSON.reqJSonString(		//filter,
				DefJS.SESS_STAT		, 1, 
				DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,					
				"res_data"			, lst
		));
	}
	
	private void svNewSorOrderInpPrice(TaAutUser user, JSONObject json, HttpServletResponse response) throws Exception  {
		Integer 				matId	= ToolData.reqInt(json.get("matId"));
		List<TaSorOrderDetail> lst 		= TaSorOrderDetail.reqList(TaSorOrder.TYPE_01_IN_STK_BUY, matId, 1);

		if (lst != null && lst.size()>0) {
			//-----check right by type first
			API.doResponse(response, ToolJSON.reqJSonString(		//filter,
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, lst.get(0) 
			));
		} else {
			API.doResponse(response,DefAPI.API_MSG_KO);
		}
	}
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------
	private void doLckReq(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLckReq --------------");		

		Integer 		entId	= ToolData.reqInt	(json, "id", null);	
		TaSysLock 		lock 	= ToolDBLock.reqLock(ENT_TYP, entId, user.reqId(), user.reqStr(TaAutUser.ATT_T_LOGIN_01), null);
		if (lock==null || lock.reqStatus() != TaSysLock.STAT_ACTIVE){
			API.doResponse(response, ToolJSON.reqJSonString(						
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_NO,
					DefJS.RES_DATA		, lock
					));	
		}else{
			API.doResponse(response, ToolJSON.reqJSonString(						
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, lock
					));	
		}			
	}
	private void doLckDel(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLckDel --------------");

		boolean isDeleted = ToolDBLock.canDeleteLock(json);		
		if (isDeleted)
			API.doResponse(response, DefAPI.API_MSG_OK);		
		else 
			API.doResponse(response, DefAPI.API_MSG_KO);
	}
	private void doLckSav(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLckSav --------------");	
		boolean isLocked 	= ToolDBLock.canExistLock(json);
		if(!isLocked){
			API.doResponse(response, DefAPI.API_MSG_ERR_LOCK);
			return;
		}

		TaSorOrder  		ent	 	=  reqMod(user, json); 								
		if (ent==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
		} else {				
			API.doResponse(response, ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, ent
					));	
		}
	}
	private void doLckEnd(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		//ToolLogServer.doLogDebug("--------- "+ SV_CLASS+ ".doLckEnd --------------");	
		boolean isLocked 	= ToolDBLock.canExistLock(json);
		if(!isLocked){
			API.doResponse(response,DefAPI.API_MSG_KO);
			return;
		}

		TaSorOrder ent = reqMod(user, json);						
		if (ent==null){
			API.doResponse(response,DefAPI.API_MSG_KO);
		} else {
			ToolDBLock.canDeleteLock(json);
			API.doResponse(response, ToolJSON.reqJSonString(
					DefJS.SESS_STAT		, 1, 
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, ent
					));	
		}	
	}

	// ------------------------------------------------------------------------------------------
	private static void doNewAlarmPriceAcceptStock(TaAutUser user, TaSorOrder order) throws Exception {

		List<TaSorOrderDetail>  details = (List<TaSorOrderDetail>) order.req(TaSorOrder.ATT_O_DETAILS);
		Set<Integer> 		listIDMat = new HashSet<Integer>();
		listIDMat = ToolSet.reqSetInt(details, TaSorOrderDetail.ATT_I_MAT_MATERIAL);

		HashMap	<Integer, TaTpyInformation> mapAlarm 		= new HashMap<Integer, TaTpyInformation>();

		//allow many child have the same I_Entity_ID
		List<TaTpyInformation> listAlarm = TaTpyInformation.DAO.reqList_In(TaTpyInformation.ATT_I_ENTITY_ID, listIDMat, 
				Restrictions.eq(TaTpyInformation.ATT_I_TYPE_01, TaTpyInformation.TYPE_01_BASE));

		for (TaTpyInformation a:listAlarm) { //allow many child have the same I_Entity_ID
			mapAlarm.put((Integer) a.req(TaTpyInformation.ATT_I_ENTITY_ID), a);
		}

		List<TaMatPrice> listPrice = TaMatPrice.DAO.reqList_In(TaMatPrice.ATT_I_MAT_MATERIAL, listIDMat);

		TaTpyInformation newAlarm ;

		if (mapAlarm.size() > 0) {
			for (TaSorOrderDetail d:details) { 
				TaTpyInformation alarm = mapAlarm.get(d.req(TaSorOrderDetail.ATT_I_MAT_MATERIAL));

				if (alarm != null) {
					for (TaMatPrice p:listPrice) {
						if (p.req(TaMatPrice.ATT_I_MAT_MATERIAL) == d.req(TaSorOrderDetail.ATT_I_MAT_MATERIAL) 
								&& p.req(TaMatPrice.ATT_T_INFO_01).equals(d.req(TaSorOrderDetail.ATT_T_INFO_03))) {
							Double valAlarm 	= (Double) alarm.req(TaTpyInformation.ATT_F_VAL_03); //% bao dong gia
							Double priceOrder 	= (Double) d.req(TaSorOrderDetail.ATT_F_VAL_02); //gia order
							Double priceMat 	= (Double) p.req(TaMatPrice.ATT_F_VAL_02); //gia trong mat price

							if (priceMat * (valAlarm / 100 + 1) <= priceOrder) {
								newAlarm = new TaTpyInformation();
								newAlarm.reqSet(TaTpyInformation.ATT_I_ENTITY_TYPE, 	DefDBExt.ID_TA_MAT_MATERIAL);
								newAlarm.reqSet(TaTpyInformation.ATT_I_ENTITY_ID, 		d.req(TaSorOrderDetail.ATT_I_MAT_MATERIAL));
								newAlarm.reqSet(TaTpyInformation.ATT_I_STATUS, 			1);
								newAlarm.reqSet(TaTpyInformation.ATT_I_TYPE_01, 		TaTpyInformation.TYPE_01_BASE);

								newAlarm.reqSet(TaTpyInformation.ATT_T_INFO_01, 		alarm.req(TaTpyInformation.ATT_T_INFO_01));
								newAlarm.reqSet(TaTpyInformation.ATT_T_INFO_02, 		alarm.req(TaTpyInformation.ATT_T_INFO_02));
								newAlarm.reqSet(TaTpyInformation.ATT_T_INFO_03, 		alarm.req(TaTpyInformation.ATT_T_INFO_03));
								newAlarm.reqSet(TaTpyInformation.ATT_T_INFO_04, 		alarm.req(TaTpyInformation.ATT_T_INFO_04));
								newAlarm.reqSet(TaTpyInformation.ATT_T_INFO_05, 		alarm.req(TaTpyInformation.ATT_T_INFO_05));

								newAlarm.reqSet(TaTpyInformation.ATT_F_VAL_01, 			priceOrder);
								newAlarm.reqSet(TaTpyInformation.ATT_F_VAL_02, 			priceMat);
								newAlarm.reqSet(TaTpyInformation.ATT_F_VAL_03, 			valAlarm);
								newAlarm.reqSet(TaTpyInformation.ATT_I_AUT_USER_01, 	user.req(TaAutUser.ATT_I_PER_MANAGER));

								newAlarm.reqSet(TaTpyInformation.ATT_D_DATE_01, 		new Date());

								TaTpyInformation.DAO.doPersist(newAlarm);
							}
						}
					}
				}
			}
		}
	}

	private static boolean canAuthorizeWithRightsOne(TaAutUser user, int typ01, int opt) {
		switch (opt) {
		case SOR_GET: 
			switch (typ01) {
			case TaSorOrder.TYPE_01_IN_STK_BUY 				: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BUY_G))  	return false;  break;
			case TaSorOrder.TYPE_01_IN_STK_TRANSFER 		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_SHOP_G))  	return false;  break;
			case TaSorOrder.TYPE_01_IN_STK_BALANCE_IN 		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BAL_G))  	return false; break;
			case TaSorOrder.TYPE_01_IN_STK_BALANCE_OTHER 	: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BAL_G))  	return false;  break;

			case TaSorOrder.TYPE_01_OU_STK_SELL 			: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_SELL_G))  	return false;  break;
			case TaSorOrder.TYPE_01_OU_STK_TRANSFER			: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_SHOP_G))  	return false;  break;
			case TaSorOrder.TYPE_01_OU_STK_BALANCE_OUT		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_BAL_G))  	return false;  break;
			case TaSorOrder.TYPE_01_OU_STK_BALANCE_FAIL		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_BAL_G))  	return false;  break;
			case TaSorOrder.TYPE_01_OU_STK_BALANCE_OTHER	: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_BAL_G))  	return false;  break;
			} break;
		case SOR_NEW:
			switch (typ01) {
			case TaSorOrder.TYPE_01_IN_STK_BUY 				: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BUY_N))  	return false; break;
			case TaSorOrder.TYPE_01_IN_STK_TRANSFER 		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_SHOP_N))  	return false; break;
			case TaSorOrder.TYPE_01_IN_STK_BALANCE_IN 		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BAL_N))  	return false; break;
			case TaSorOrder.TYPE_01_IN_STK_BALANCE_OTHER 	: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BAL_N))  	return false; break;

			case TaSorOrder.TYPE_01_OU_STK_SELL 			: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_SELL_N))  	return false; break;
			case TaSorOrder.TYPE_01_OU_STK_TRANSFER			: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_SHOP_N))  	return false; break;
			case TaSorOrder.TYPE_01_OU_STK_BALANCE_OUT		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_BAL_N))  	return false; break;
			case TaSorOrder.TYPE_01_OU_STK_BALANCE_FAIL		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_BAL_N))  	return false; break;
			case TaSorOrder.TYPE_01_OU_STK_BALANCE_OTHER	: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_BAL_N))  	return false; break;
			
			} break;
		case SOR_MOD:
			switch (typ01) {
			case TaSorOrder.TYPE_01_IN_STK_BUY 				: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BUY_M))  	return false; break;
			case TaSorOrder.TYPE_01_IN_STK_TRANSFER 		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_SHOP_M))  	return false; break;
			case TaSorOrder.TYPE_01_IN_STK_BALANCE_IN 		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BAL_M))  	return false; break;
			case TaSorOrder.TYPE_01_IN_STK_BALANCE_OTHER 	: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BAL_M))  	return false; break;

			case TaSorOrder.TYPE_01_OU_STK_SELL 			: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_SELL_M))  	return false; break;
			case TaSorOrder.TYPE_01_OU_STK_TRANSFER			: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_SHOP_M))  	return false; break;
			case TaSorOrder.TYPE_01_OU_STK_BALANCE_OUT		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_BAL_M))  	return false; break;
			case TaSorOrder.TYPE_01_OU_STK_BALANCE_FAIL		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_BAL_M))  	return false; break;
			case TaSorOrder.TYPE_01_OU_STK_BALANCE_OTHER	: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_BAL_M))  	return false; break;
			} break;
		case SOR_DEL:
			switch (typ01) {
			case TaSorOrder.TYPE_01_IN_STK_BUY 				: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BUY_D))  	return false; break;
			case TaSorOrder.TYPE_01_IN_STK_TRANSFER 		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_SHOP_D))  	return false; break;
			case TaSorOrder.TYPE_01_IN_STK_BALANCE_IN 		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BAL_D))  	return false; break;
			case TaSorOrder.TYPE_01_IN_STK_BALANCE_OTHER 	: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_I_STK_BAL_D))  	return false; break;

			case TaSorOrder.TYPE_01_OU_STK_SELL 			: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_SELL_D))  	return false; break;
			case TaSorOrder.TYPE_01_OU_STK_TRANSFER			: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_SHOP_D))  	return false; break;
			case TaSorOrder.TYPE_01_OU_STK_BALANCE_OUT		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_BAL_D))  	return false; break;
			case TaSorOrder.TYPE_01_OU_STK_BALANCE_FAIL		: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_BAL_D))  	return false; break;
			case TaSorOrder.TYPE_01_OU_STK_BALANCE_OTHER	: if (!APIAuth.canAuthorizeWithRights(user, DefRight.RIGHT_SOR_ORDER_O_STK_BAL_D))  	return false; break;
			} break;
		}
		return true;
	}

	//---------------------------------------------------------------------------------------------------------

	private static final int WORK_GET 	= 1;
	private static final int WORK_LST 	= 2;
	private static final int WORK_NEW 	= 3;
	private static final int WORK_MOD 	= 4;
	private static final int WORK_DEL 	= 5;
	private static final int WORK_UPL 	= 10; //upload

	private static boolean canWorkWithObj ( TaAutUser user, int typWork, Object...params){
		if (params==null|| params.length==0 || user==null) return false;
		//int userType	= (int) user.req(TaAutUser.ATT_I_TYPE);
		//if (userType==TYPE_ADM_ALL) return true;

		switch(typWork){

		case WORK_MOD : 
			return true;
		case WORK_GET : 
			return true;
		case WORK_DEL :
			return true;
		case WORK_LST : 
			return true;
		case WORK_NEW : 
			//check something with params
			return true;

		}

		return false;
	}

	
}
