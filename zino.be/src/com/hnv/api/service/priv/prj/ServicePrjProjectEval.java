package com.hnv.api.service.priv.prj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.hnv.api.def.DefAPI;
import com.hnv.api.def.DefJS;
import com.hnv.api.def.DefTime;
import com.hnv.api.interf.IService;
import com.hnv.api.main.API;
import com.hnv.api.service.common.ResultPagination;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.common.tool.ToolSet;
import com.hnv.common.util.CacheData;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.prj.TaPrjProject;	


public class ServicePrjProjectEval implements IService {

	//--------------------------------Service Definition----------------------------------
	public static final String SV_MODULE 							= "EC_V3".toLowerCase();

	public static final String SV_CLASS 							= "ServicePrjProjectEval".toLowerCase();


	public static final String SV_VAL_02					= "SVPrjValReal"		 .toLowerCase(); 
	public static final String SV_VAL_05					= "SVPrjValPercent"		 .toLowerCase(); 
	//-------------------------Default Constructor - Required -------------------------------------
	public ServicePrjProjectEval(){
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	//-----------------------------------------------------------------------------------------------

	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
		String 		sv 		= API.reqSVFunctName(json);
		TaAutUser 	user	= (TaAutUser) json.get("userInfo");
		try {
			if (sv.equals(SV_VAL_02)){
				doPrjProjectVal02(user,  json, response);
			} else if (sv.equals(SV_VAL_05)){
				doPrjProjectVal05(user,  json, response);
			} else {
				API.doResponse(response, DefAPI.API_MSG_ERR_RIGHT);
			}

		}catch(Exception e){
			API.doResponse(response, DefAPI.API_MSG_ERR_API);
			e.printStackTrace();
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------

	//---------------------------------------------------------------------------------------------------------------------------
	private static CacheData<ResultPagination>		cache_rs 		= new CacheData<ResultPagination>	(100, DefTime.TIME_00_30_00_000); //30 minutes if project or epic
	private static CacheData<ResultPagination>		cache_rs_task 	= new CacheData<ResultPagination>	(100, DefTime.TIME_00_00_10_000); //30 s if task

	//-------------------------------------------------List dynamique filter mat--------------------------------------------------------------------------------------
	private static void doPrjProjectVal02(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {	
		TaPrjProject  	res = reqTaPrjVal02(user, json, response); //and other params if necessary

		if (res==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
		}else {
			API.doResponse(response,ToolJSON.reqJSonString(		//filter,
					DefJS.SESS_STAT		, 1,  
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, res
					));	
		}
	}

	private static 	TaPrjProject reqTaPrjVal02(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		Integer 		manId 		= user.reqPerManagerId();		
		Integer 		uId			= ToolData.reqInt	(json, "user_id"		, null);		
		Integer			prjId		= ToolData.reqInt	(json, "id"			, null); 

		//		Double			val01		= API.reqParamDouble(request, "val01"		, null);
		//		Double			val02		= API.reqParamDouble(request, "val02"		, null); 
		//		Boolean			updChildren	= ToolData.reqBool	(json, "updSub"		, true); 
		//		Integer			nbLev		= ToolData.reqInt	(json, "nbLev"		, 2); 

		TaPrjProject 	ent 		= null;
		Integer 		level 		= 0;

		if (uId==null) 		return null;
		if (prjId==null) 	return null;

		Session sess = TaPrjProject.DAO.reqSessionCurrent();
		try {
			ent = TaPrjProject.DAO.reqEntityByRef(sess, prjId);
			if (ent == null) {
				TaPrjProject.DAO.doSessionRollback(sess);
				return null;
			}

			List<TaPrjProject> 						nodes	 	= new ArrayList<TaPrjProject>();
			Set<Integer> 							ids 		= new HashSet<Integer>();
			Hashtable<Integer, List<TaPrjProject>> 	dictLev 	= new Hashtable<Integer, List<TaPrjProject>>();
			Hashtable<Integer, Double> 				dictVal02 	= new Hashtable<Integer, Double>(); //prjId, val02 computed

			ids			.add(prjId);
			nodes		.add(ent);
			dictLev		.put(level, nodes);
			dictVal02	.put(prjId, 0.0);


			while (ids!=null && ids.size()>0) {
				nodes = TaPrjProject.DAO.reqList_In(sess, TaPrjProject.ATT_I_PARENT, ids);
				if (nodes!=null && nodes.size()>0) {
					ids = ToolSet.reqSetInt(nodes, TaPrjProject.ATT_I_ID);
					level++;
					dictLev.put(level, nodes);
				} else ids = null;
			}

			//---compute from last level to 0;
			for (int lev=level;lev>=0;lev--) {				
				List<TaPrjProject> lst = dictLev.get(lev);
				for (TaPrjProject prj: lst) {
					Integer id					= prj.reqId();
					Double 	v02Comp 			= dictVal02.get(id);
					if (v02Comp==null) v02Comp 	= 0.0;

					Double v01 = (Double) prj.req(TaPrjProject.ATT_F_VAL_01);
					Double v02 = (Double) prj.req(TaPrjProject.ATT_F_VAL_02);
					if(v01 == null)	v01 = 0.0;
					if(v02 == null)	v02 = 0.0;

					//---compare to value computed from children
					v02 = Math.max(v02, v02Comp);
					if (v02 == 0.0) v02 = v01;	

					prj.reqSet(TaPrjProject.ATT_F_VAL_01, v01);
					prj.reqSet(TaPrjProject.ATT_F_VAL_02, v02);

					//---compute to parent
					Integer parId		= (Integer) prj.req(TaPrjProject.ATT_I_PARENT);
					if (parId!=null) {
						Double v02Par 	= dictVal02.get(parId);
						if (v02Par==null) v02Par = 0.0;
						v02Par+= v02;
						dictVal02.put(parId, v02Par);
					}
				}
				//merge all change
				TaPrjProject.DAO.doMerge(sess, lst);
			}

			TaPrjProject.DAO.doSessionCommit(sess);
		}catch(Exception e) {
			if (sess!=null) TaPrjProject.DAO.doSessionRollback(sess);
		}

		return ent;
	}
	//---------------------------------------------------------------------------------------------------------------------------------------
	private static void doPrjProjectVal05(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception {	
//		TaPrjProject  	res = reqTaPrjVal05(user, json, response); //and other params if necessary
		TaPrjProject  	res = reqTaPrjValPercent(user, json, response);
		if (res==null) {
			API.doResponse(response, DefAPI.API_MSG_KO);
		}else {
			API.doResponse(response,ToolJSON.reqJSonString(		//filter,
					DefJS.SESS_STAT		, 1,  
					DefJS.SV_CODE		, DefAPI.SV_CODE_API_YES,
					DefJS.RES_DATA		, res
					));	
		}
	}

	private static 	TaPrjProject reqTaPrjVal05(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		Integer 		manId 		= user.reqPerManagerId();		
		Integer 		uId			= ToolData.reqInt	(json, "user_id"		, null);		
		Integer			prjId		= ToolData.reqInt	(json, "id"			, null); 


		//v05 : 0 - 100

		TaPrjProject 	ent 		= null;
		Integer 		level 		= 0;

		if (uId==null) 		return null;
		if (prjId==null) 	return null;

		Session sess = TaPrjProject.DAO.reqSessionCurrent();
		try {
			ent = TaPrjProject.DAO.reqEntityByRef(sess, prjId);
			if (ent == null) {
				TaPrjProject.DAO.doSessionRollback(sess);
				return null;
			}

			List<TaPrjProject> 						nodes	 		= new ArrayList<TaPrjProject>();
			Set<Integer> 							ids 			= new HashSet<Integer>();
			Hashtable<Integer, List<TaPrjProject>> 	dictLev 		= new Hashtable<Integer, List<TaPrjProject>>();
			Hashtable<Integer, Double> 				dictVal05 		= new Hashtable<Integer, Double>(); //prjId, val05 computed
			Hashtable<Integer, Double> 				dictVal05Count 	= new Hashtable<Integer, Double>(); //prjId, val05 computed

			ids				.add(prjId);
			nodes			.add(ent);
			dictLev			.put(level, nodes);
			dictVal05		.put(prjId, 0.0);
			dictVal05Count	.put(prjId, 0.0);

			while (ids!=null && ids.size()>0) {
				nodes = TaPrjProject.DAO.reqList_In(sess, TaPrjProject.ATT_I_PARENT, ids);
				if (nodes!=null && nodes.size()>0) {
					ids = ToolSet.reqSetInt(nodes, TaPrjProject.ATT_I_ID);
					level++;
					dictLev.put(level, nodes);
				} else ids = null;
			}

			//---compute from last level to 0;
			for (int lev=level;lev>=0;lev--) {				
				List<TaPrjProject> lst = dictLev.get(lev);
				for (TaPrjProject prj: lst) {
					Integer id					= prj.reqId();
					Integer stat				= (Integer) prj.req(TaPrjProject.ATT_I_STATUS_01);

					Double 	v05Comp 			= dictVal05		.get(id);
					Double	v05Count 			= dictVal05Count.get(id);
					if (v05Comp==null) 	v05Comp = 0.0;
					if (v05Count==null) v05Count= 0.0;

					Double v05 = (Double) prj.req(TaPrjProject.ATT_F_VAL_05);
					if(v05 == null)	v05 = 0.0;
					v05 = Math.min(Math.max(v05, 0),100);

					switch(stat) {
					case TaPrjProject.STAT_01_PRJ_REVIEW 		: v05 = 90.0; break;
					
					case TaPrjProject.STAT_01_PRJ_FAIL 		:
					case TaPrjProject.STAT_01_PRJ_UNRESOLVED	:
					case TaPrjProject.STAT_01_PRJ_NEW 			: v05 = 0.0; break;
					
					case TaPrjProject.STAT_01_PRJ_TODO 		: 
					case TaPrjProject.STAT_01_PRJ_INPROGRESS 	: v05 = Math.min(100, Math.max(v05, v05Comp/v05Count)); break; //--keep v05 from db and compare to val computed from children

					case TaPrjProject.STAT_01_PRJ_DONE 		: 
					case TaPrjProject.STAT_01_PRJ_CLOSED 		: v05 = 100.0; break;
					}

					prj.reqSet(TaPrjProject.ATT_F_VAL_05, v05);

					//---compute to parent
					Integer parId		= (Integer) prj.req(TaPrjProject.ATT_I_PARENT);
					Integer parLev		= (Integer) prj.req(TaPrjProject.ATT_I_LEVEL);
					if (parId!=null) {
						Double 	v05Par 		= dictVal05.get(parId);
						Double	v05ParCount = dictVal05Count.get(id);
						if (v05ParCount==null) 	v05ParCount = 0.0;
						if (v05Par==null) 		v05Par 		= 0.0;
						v05Par				+= v05;
						v05ParCount 		= v05ParCount + 1 + (parLev * 0.25) ; //lev : 0 1 2 3 / 3: ưu tiên (top)
						dictVal05		.put(parId, v05Par);
						dictVal05Count	.put(parId, v05ParCount);
					}
				}
				//merge all change
				TaPrjProject.DAO.doMerge(sess, lst);
			}

			TaPrjProject.DAO.doSessionCommit(sess);
		}catch(Exception e) {
			if (sess!=null) TaPrjProject.DAO.doSessionRollback(sess);
		}

		return ent;
	}
	
	private static final Double COEF_LOW 		= 1.0;
	private static final Double COEF_MEDIUM 	= 1.25;
	private static final Double COEF_HIGH 		= 1.5;
	private static final Double COEF_PRIORITY 	= 1.75;
	private static 	TaPrjProject reqTaPrjValPercent(TaAutUser user,  JSONObject json, HttpServletResponse response) throws Exception  {
		Integer 		uId			= ToolData.reqInt	(json, "user_id"		, null);		
		Integer			prjId		= ToolData.reqInt	(json, "id"			, null); 

		//v05 : 0 - 100

		TaPrjProject 	ent 		= null;

		if (uId==null) 		return null;
		if (prjId==null) 	return null;

		Session sess = TaPrjProject.DAO.reqSessionCurrent();
		try {
			ent = TaPrjProject.DAO.reqEntityByRef(sess, prjId);
			if (ent == null) {
				TaPrjProject.DAO.doSessionRollback(sess);
				return null;
			}
			
//			List<TaPrjProject> allInGroup = TaPrjProject.DAO.reqList(sess,
//					Restrictions.eq(TaPrjProject.ATT_I_GROUP	, ent.req(TaPrjProject.ATT_I_GROUP)),
//					Restrictions.ne(TaPrjProject.ATT_I_ID		, ent.reqRef())
//					);
			
			List<TaPrjProject> allInGroup = TaPrjProject.DAO.reqList(sess,
					Restrictions.eq(TaPrjProject.ATT_I_GROUP	, ent.req(TaPrjProject.ATT_I_GROUP))
					);
			
			TaPrjProject entRoot = new TaPrjProject();
			
			Map<Integer, List<TaPrjProject>> mapParent = new HashMap<Integer, List<TaPrjProject>>();
			if(allInGroup != null && allInGroup.size() > 0) {
				List<TaPrjProject> newAll = new ArrayList<TaPrjProject>();
				for(TaPrjProject prj: allInGroup) {
					if((int)prj.req(TaPrjProject.ATT_I_ID) == (int)ent.req(TaPrjProject.ATT_I_GROUP)) {
						entRoot = prj;
					} else {
						newAll.add(prj);
					}
				}
				
				if(newAll.size() > 0) {
					for(TaPrjProject prj: newAll) {
						Integer parent = (Integer) prj.req(TaPrjProject.ATT_I_PARENT);
						if(parent == null)	continue;
						if(mapParent.containsKey(parent)) {
							mapParent.get(parent).add(prj);
						} else {
							List<TaPrjProject> lstChild = new ArrayList<TaPrjProject>();
							lstChild.add(prj);
							mapParent.put(parent, lstChild);
						}
					}
				}
			}
			Integer stat = (Integer) entRoot.req(TaPrjProject.ATT_I_STATUS_01);
			if (stat == null) stat = TaPrjProject.STAT_01_PRJ_NEW;
			Double cumulPrj = 0.0;
			switch (stat) {
			case TaPrjProject.STAT_01_PRJ_NEW			: cumulPrj =   0.0; break;
			case TaPrjProject.STAT_01_PRJ_TODO			: cumulPrj = reqCumulVal05(entRoot, mapParent, sess);break;
			case TaPrjProject.STAT_01_PRJ_INPROGRESS	: cumulPrj = reqCumulVal05(entRoot, mapParent, sess);break;
			case TaPrjProject.STAT_01_PRJ_REVIEW		: cumulPrj =  90.0; break;
			case TaPrjProject.STAT_01_PRJ_DONE			: cumulPrj = 100.0; break;
			case TaPrjProject.STAT_01_PRJ_FAIL			: cumulPrj =   0.0; break;
			case TaPrjProject.STAT_01_PRJ_UNRESOLVED	: cumulPrj =   0.0; break;
			}
			
			//---ưu tiên Stat, STAT_PRJ_REVIEW => %=90
			if(Double.compare(cumulPrj, 100.0) == 0) {			
				if (stat.equals(TaPrjProject.STAT_01_PRJ_INPROGRESS) || stat.equals(TaPrjProject.STAT_01_PRJ_TODO))			
					stat = TaPrjProject.STAT_01_PRJ_DONE;
			}
			
			entRoot.reqSet(TaPrjProject.ATT_I_STATUS_01, stat);
			entRoot.reqSet(TaPrjProject.ATT_F_VAL_05, cumulPrj);
			TaPrjProject.DAO.doMerge(sess, entRoot);
			
			if((int)ent.req(TaPrjProject.ATT_I_ID) != (int)ent.req(TaPrjProject.ATT_I_GROUP)) {
				ent = TaPrjProject.DAO.reqEntityByRef(sess, prjId);
			}
			
			TaPrjProject.DAO.doSessionCommit(sess);
		}catch(Exception e) {
			if (sess!=null) TaPrjProject.DAO.doSessionRollback(sess);
		}

		return ent;
	}
	
	private static double reqCoefChildLevel (TaPrjProject prj) throws Exception {
		Integer level = (Integer) prj.req(TaPrjProject.ATT_I_LEVEL);
		if(level == null) {
			level = TaPrjProject.TYP_LEV_BASE;
			prj.reqSet(TaPrjProject.ATT_I_LEVEL, level);
		}
		
		if(level == TaPrjProject.TYP_LEV_BASE)		return COEF_LOW;
		if(level == TaPrjProject.TYP_LEV_MEDIUM)	return COEF_MEDIUM;
		if(level == TaPrjProject.TYP_LEV_HIGH)		return COEF_HIGH;
		if(level == TaPrjProject.TYP_LEV_TOP)		return COEF_PRIORITY;
		
		return COEF_LOW;
	}
	
	public static double reqPercentByStatus (TaPrjProject prj) throws Exception {
		Double v05 								= (Double)  prj.req(TaPrjProject.ATT_F_VAL_05);
		if (v05 == null || v05<0) v05			= 0.0;
		if (v05 >100) 			  v05			= 100.0;
		Integer stat							= (Integer) prj.req(TaPrjProject.ATT_I_STATUS_01);
		if (stat == null) return v05;
		switch(stat) {
		case TaPrjProject.STAT_01_PRJ_REVIEW 		: v05 =  90.0; break;
		
		case TaPrjProject.STAT_01_PRJ_FAIL 		:
		case TaPrjProject.STAT_01_PRJ_UNRESOLVED	:
		case TaPrjProject.STAT_01_PRJ_NEW 			: v05 =   0.0; break;

		case TaPrjProject.STAT_01_PRJ_TODO 		: v05 =   0.0; break;
		case TaPrjProject.STAT_01_PRJ_INPROGRESS 	: v05 =  Math.max(v05, 10); break; //--keep v05 from db and compare to val computed from children

		case TaPrjProject.STAT_01_PRJ_DONE 		: 
		case TaPrjProject.STAT_01_PRJ_CLOSED 		: v05 = 100.0; break;
			
		default									: v05 =   0.0; break;			

		}
		
		return v05;
	}
	
	public static double reqPercentByStatus (Integer stat) throws Exception {
		Double v05 								= 0.0;
		if (stat == null) return v05;
		switch(stat) {
		case TaPrjProject.STAT_01_PRJ_REVIEW 		: v05 =  90.0; break;
		
		case TaPrjProject.STAT_01_PRJ_FAIL 		:
		case TaPrjProject.STAT_01_PRJ_UNRESOLVED	:
		case TaPrjProject.STAT_01_PRJ_NEW 			: v05 =   0.0; break;

		case TaPrjProject.STAT_01_PRJ_TODO 		: v05 =   0.0; break;
		case TaPrjProject.STAT_01_PRJ_INPROGRESS 	: v05 =  10.0; break; //--keep v05 from db and compare to val computed from children

		case TaPrjProject.STAT_01_PRJ_DONE 		: 
		case TaPrjProject.STAT_01_PRJ_CLOSED 		: v05 = 100.0; break;
			
		default									: v05 =   0.0; break;			

		}
		
		return v05;
	}
	
	private static Double reqCumulVal05(TaPrjProject prj, Map<Integer, List<TaPrjProject>>mapParent, Session sess) throws Exception {
		Double COUNT_PART 	= 0.0;
		Double cumulPrj 	= 0.0;
		
		if(mapParent.containsKey(prj.reqRef())) {
			List<TaPrjProject> childs = mapParent.get(prj.reqRef());
			if(childs != null && childs.size() > 0) {
				for(TaPrjProject child: childs) {
					COUNT_PART += reqCoefChildLevel(child);
				}
				
				Double 		VALUE_PART 	= 100/COUNT_PART;
				
				for(TaPrjProject child: childs) {
					
					Integer stat 	= child.reqInt	 (child, TaPrjProject.ATT_I_STATUS_01);
					Double	v05 	= child.reqDouble(child, TaPrjProject.ATT_F_VAL_05);
					
					if (stat == null) stat = TaPrjProject.STAT_01_PRJ_NEW;					
					if(v05 == null || Double.compare(v05, 0.0) == 0) {
						v05 		= reqPercentByStatus(child);
					}
					
					Integer typ02		= (Integer) child.req(TaPrjProject.ATT_I_TYPE_02);
					Double 	coefChild 	= reqCoefChildLevel(child);
					
					if(typ02 == TaPrjProject.TYP_02_PRJ_ELE) {
						if(Double.compare(v05, 100.0) == 0) {			
							if (stat.equals(TaPrjProject.STAT_01_PRJ_INPROGRESS) || stat.equals(TaPrjProject.STAT_01_PRJ_TODO))			
								stat 	= TaPrjProject.STAT_01_PRJ_DONE;
						}						
						cumulPrj += v05 * VALUE_PART * coefChild/100;
						
					}else if(typ02 == TaPrjProject.TYP_02_PRJ_SUB) {
						//---recompute v05
						if (stat.equals(TaPrjProject.STAT_01_PRJ_INPROGRESS) || stat.equals(TaPrjProject.STAT_01_PRJ_TODO)){
							v05 = reqCumulVal05(child, mapParent, sess);
							if(Double.compare(v05, 100.0) == 0) {			
								stat 	= TaPrjProject.STAT_01_PRJ_DONE;
							}
						}							
						cumulPrj += v05 * VALUE_PART * coefChild/100;
					}
					
					child.reqSet(TaPrjProject.ATT_I_STATUS_01, stat);
					child.reqSet(TaPrjProject.ATT_F_VAL_05, v05);
				}
				
				TaPrjProject.DAO.doMerge(sess, childs);
			}
		} else {
			cumulPrj = reqPercentByStatus(prj);
		}
		
		return cumulPrj;
	}

	//---------------------------------------------------------------------------------------------------------------------------------------
	
}
