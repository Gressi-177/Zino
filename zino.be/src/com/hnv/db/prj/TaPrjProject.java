package com.hnv.db.prj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.checkerframework.common.returnsreceiver.qual.This;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hnv.api.def.DefTime;
import com.hnv.api.main.Hnv_CfgHibernate;
import com.hnv.api.service.priv.prj.ServicePrjProject;
import com.hnv.common.tool.ToolDBEntity;
import com.hnv.common.tool.ToolData;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolSet;
import com.hnv.common.util.CacheData;
import com.hnv.data.json.JSONArray;
import com.hnv.data.json.JSONObject;
import com.hnv.db.EntityAbstract;
import com.hnv.db.EntityDAO;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.aut.vi.ViAutUserMember;
import com.hnv.db.nso.TaNsoPost;
import com.hnv.db.prj.vi.ViPrjProjectSimple;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.db.tpy.TaTpyInformation;
import com.hnv.db.tpy.TaTpyRelationship;
import com.hnv.def.DefDBExt;		

/**
 * TaPrjProject by H&V SAS
 */
@Entity
@Table(name = DefDBExt.TA_PRJ_PROJECT )
public class TaPrjProject extends EntityAbstract<TaPrjProject> {

	private static final long serialVersionUID = 1L;
	
	public static final int STAT_01_TEST_UNIT_NEW					=  0;
	public static final int STAT_01_TEST_UNIT_TODO					= 10;
	public static final int STAT_01_TEST_UNIT_EXECUTING				= 20;
	public static final int STAT_01_TEST_UNIT_BLOCKED				= 30;
	public static final int STAT_01_TEST_UNIT_PASS					= 40;
	public static final int STAT_01_TEST_UNIT_FAIL					= 60;	
	public static final int STAT_01_TEST_UNIT_ABORT					= 70;
	
	public static final int STAT_01_PRJ_NEW 						= 100100;
	public static final int STAT_01_PRJ_TODO 						= 100200;
	public static final int STAT_01_PRJ_INPROGRESS 					= 100300;
	public static final int STAT_01_PRJ_DONE 						= 100400;
	public static final int STAT_01_PRJ_TEST 						= 100500;
	public static final int STAT_01_PRJ_REVIEW 						= 100600;
	public static final int STAT_01_PRJ_FAIL 						= 100700;
	public static final int STAT_01_PRJ_UNRESOLVED 					= 100800;
	public static final int STAT_01_PRJ_CLOSED 						= 100900;
	//-------------------------------------------------------------------------
	
	public static final int TYP_00_PRJ_PROJECT 						= 10;
	public static final int TYP_00_PRJ_DATACENTER 					= 20;
	public static final int TYP_00_PRJ_TEST							= 30;
	public static final int TYP_00_PRJ_SPRINT 						= 100;
	public static final int TYP_00_PRJ_WORKFLOW 					= 200;
	public static final int TYP_00_PRJ_EMAIL 						= 500;
	
	//----------------------------------------------------------------------------
	public static final int TYP_01_PRJ_IT 							= 11;
	public static final int TYP_01_PRJ_COMMERCE						= 12;
	public static final int TYP_01_PRJ_OTHER						= 15;
	
	public static final int TYP_02_PRJ_MAIN 						= 0;
	public static final int TYP_02_PRJ_SUB 							= 1;
	public static final int TYP_02_PRJ_ELE 							= 2;
	
	//----------------------------------------------------------------------------
	public static final int TYP_01_TEST_UNIT						= 1;
	public static final int TYP_01_TEST_GROUP						= 2;

	public static final int TYP_02_TEST_MANUAL						= 1;
	public static final int TYP_02_TEST_AUTO						= 2;
	//-------------------------------------------------------------------------

	public static final int LEV_ROLE_PRJ_MANAGER 					= 0;
	public static final int LEV_ROLE_PRJ_REPORTER 					= 1;
	public static final int LEV_ROLE_PRJ_WORKER 					= 2;
	public static final int LEV_ROLE_PRJ_WATCHER 					= 3;

	public static final int TYP_LEV_BASE 							= 0;
	public static final int TYP_LEV_MEDIUM 							= 1;
	public static final int TYP_LEV_HIGH 							= 3;
	public static final int TYP_LEV_TOP 							= 4;
	
	public static final int TYP_WF_ENTITY_01 						= 20002;
	
	
	
	
	
	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                              =	"I_ID";
	public static final String	COL_I_GROUP                           =	"I_Group";
	public static final String	COL_I_PARENT                          =	"I_Parent";
	
	public static final String	COL_T_CODE_01                         =	"T_Code_01";
	public static final String	COL_T_CODE_02                         =	"T_Code_02";
	public static final String	COL_T_NAME                            =	"T_Name";
	
	public static final String	COL_T_INFO_01                  		  =	"T_Info_01";
	public static final String	COL_T_INFO_02                  		  =	"T_Info_02";
	public static final String	COL_T_INFO_03                  		  =	"T_Info_03";
	
	public static final String	COL_I_TYPE_00                         =	"I_Type_00";
	public static final String	COL_I_TYPE_01                         =	"I_Type_01";
	public static final String	COL_I_TYPE_02                         =	"I_Type_02";
	public static final String	COL_I_STATUS_01                       =	"I_Status_01";
	public static final String	COL_I_STATUS_02                       =	"I_Status_02";
	public static final String	COL_I_LEVEL                           =	"I_Level";
	
	public static final String	COL_D_DATE_01                         =	"D_Date_01"; //D_Date_01
	public static final String	COL_D_DATE_02                         =	"D_Date_02"; //D_Date_02
	public static final String	COL_D_DATE_03                         =	"D_Date_03"; //D_Date_03
	public static final String	COL_D_DATE_04                         =	"D_Date_04"; //D_Date_04
	
	public static final String	COL_I_AUT_USER_01                     =	"I_Aut_User_01";
	public static final String	COL_I_AUT_USER_02                     =	"I_Aut_User_02";
	public static final String	COL_I_PER_MANAGER                     =	"I_Per_Manager";
	
	public static final String	COL_F_VAL_00                          =	"F_Val_00";
	public static final String	COL_F_VAL_01                          =	"F_Val_01";
	public static final String	COL_F_VAL_02                          =	"F_Val_02";
	public static final String	COL_F_VAL_03                          =	"F_Val_03";
	public static final String	COL_F_VAL_04                          =	"F_Val_04";
	public static final String	COL_F_VAL_05                          =	"F_Val_05";
	public static final String	COL_F_VAL_06                          =	"F_Val_06";
	public static final String	COL_F_VAL_07                          =	"F_Val_07";
	public static final String	COL_F_VAL_08                          =	"F_Val_08";
	public static final String	COL_F_VAL_09                          =	"F_Val_09";


	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              =	"I_ID";
	public static final String	ATT_I_GROUP                           =	"I_Group";
	public static final String	ATT_I_PARENT                          =	"I_Parent";
	
	public static final String	ATT_T_CODE_01                         =	"T_Code_01";
	public static final String	ATT_T_CODE_02                         =	"T_Code_02";
	public static final String	ATT_T_NAME                            =	"T_Name";
	
	public static final String	ATT_T_INFO_01                  		  =	"T_Info_01";
	public static final String	ATT_T_INFO_02                  		  =	"T_Info_02";
	public static final String	ATT_T_INFO_03                  		  =	"T_Info_03";
	
	public static final String	ATT_I_TYPE_00                         =	"I_Type_00";
	public static final String	ATT_I_TYPE_01                         =	"I_Type_01";
	public static final String	ATT_I_TYPE_02                         =	"I_Type_02";
	
	public static final String	ATT_I_STATUS_01                       =	"I_Status_01";
	public static final String	ATT_I_STATUS_02                       =	"I_Status_02";
	public static final String	ATT_I_LEVEL                           =	"I_Level";
	
	public static final String	ATT_D_DATE_01                         =	"D_Date_01"; //D_Date_01
	public static final String	ATT_D_DATE_02                         =	"D_Date_02"; //D_Date_02
	public static final String	ATT_D_DATE_03                         =	"D_Date_03"; //D_Date_03
	public static final String	ATT_D_DATE_04                         =	"D_Date_04"; //D_Date_04
	
	public static final String	ATT_I_AUT_USER_01                     =	"I_Aut_User_01";
	public static final String	ATT_I_AUT_USER_02                     =	"I_Aut_User_02";
	public static final String	ATT_I_PER_MANAGER                     =	"I_Per_Manager";
	
	public static final String	ATT_F_VAL_00                          =	"F_Val_00";
	public static final String	ATT_F_VAL_01                          =	"F_Val_01";
	public static final String	ATT_F_VAL_02                          =	"F_Val_02";
	public static final String	ATT_F_VAL_03                          =	"F_Val_03";
	public static final String	ATT_F_VAL_04                          =	"F_Val_04";
	public static final String	ATT_F_VAL_05                          =	"F_Val_05";
	public static final String	ATT_F_VAL_06                          =	"F_Val_06";
	public static final String	ATT_F_VAL_07                          =	"F_Val_07";
	public static final String	ATT_F_VAL_08                          =	"F_Val_08";
	public static final String	ATT_F_VAL_09                          =	"F_Val_09";
	public static final String	ATT_F_VAL_10                          =	"F_Val_10";

	public static final String	ATT_O_DOCUMENTS        	  			  =	"O_Documents";
	public static final String	ATT_O_AVATAR                      	  =	"O_Avatar";
	public static final String	ATT_O_MEMBERS                      	  =	"O_Members";
	public static final String	ATT_O_GROUPS                      	  =	"O_Groups";
	
	public static final String	ATT_O_EPICS                      	  =	"O_Epics";
	public static final String	ATT_O_TASKS                      	  =	"O_Tasks";
	
	public static final String	ATT_O_COMMENTS                        =	"O_Comments";
	
	public static final String	ATT_O_USER_ROLE                       =	"O_User_Role";
	
	public static final String	ATT_O_EPIC_INFO                       =	"O_Epic_Info";
	
	public static final String	ATT_O_PARENT                      	  =	"O_Parent";
	public static final String	ATT_O_PROJECT                      	  =	"O_Project";
	
	public static final String	ATT_O_NB_EPIC_TASK_SIZE_DOC           =	"O_Nb_Epic_Task_Size_Doc";
	public static final String	ATT_O_NB_CHILD_SIZE_DOC               =	"O_Nb_Child_Size_Doc";
	
	public static final String	ATT_O_TREE              			  =	"O_Tree";

	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static	final boolean				API_CACHE 	= false;
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<TaPrjProject> 	DAO;
	static{
		DAO = new EntityDAO<TaPrjProject>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN) , TaPrjProject.class, RIGHTS, HISTORY, DefDBExt.TA_PRJ_PROJECT, DefDBExt.ID_TA_PRJ_PROJECT);

	}

	//-----------------------Class Attributs-------------------------
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;

	@Column(name=COL_I_GROUP, nullable = false)
	private	Integer         I_Group;

	@Column(name=COL_T_CODE_01, nullable = true)
	private	String          T_Code_01;
	
	@Column(name=COL_T_CODE_02, nullable = true)
	private	String          T_Code_02;

	@Column(name=COL_T_NAME, nullable = true)
	private	String          T_Name;

	@Column(name=COL_T_INFO_01, nullable = true)
	private	String          T_Info_01;

	@Column(name=COL_T_INFO_02, nullable = true)
	private	String          T_Info_02;
	
	@Column(name=COL_T_INFO_03, nullable = true)
	private	String          T_Info_03;

	

	@Column(name=COL_I_TYPE_00, nullable = true)
	private	Integer         I_Type_00;
	
	@Column(name=COL_I_TYPE_01, nullable = true)
	private	Integer         I_Type_01;

	@Column(name=COL_I_TYPE_02, nullable = true)
	private	Integer         I_Type_02;

	@Column(name=COL_I_STATUS_01, nullable = true)
	private	Integer         I_Status_01;
	
	@Column(name=COL_I_STATUS_02, nullable = true)
	private	Integer         I_Status_02;

	@Column(name=COL_I_LEVEL, nullable = true)
	private	Integer         I_Level;

	@Column(name=COL_D_DATE_01, nullable = true)
	private	Date            D_Date_01;

	@Column(name=COL_D_DATE_02, nullable = true)
	private	Date            D_Date_02;

	@Column(name=COL_D_DATE_03, nullable = true)
	private	Date            D_Date_03;

	@Column(name=COL_D_DATE_04, nullable = true)
	private	Date            D_Date_04;

	@Column(name=COL_I_AUT_USER_01, nullable = true)
	private	Integer         I_Aut_User_01;

	@Column(name=COL_I_AUT_USER_02, nullable = true)
	private	Integer         I_Aut_User_02;

	@Column(name=COL_I_PER_MANAGER, nullable = true)
	private	Integer         I_Per_Manager;

	@Column(name=COL_I_PARENT, nullable = true)
	private	Integer         I_Parent;

	@Column(name=COL_F_VAL_00, nullable = true)
	private	Double          F_Val_00;
	
	@Column(name=COL_F_VAL_01, nullable = true)
	private	Double          F_Val_01;

	@Column(name=COL_F_VAL_02, nullable = true)
	private	Double          F_Val_02;

	@Column(name=COL_F_VAL_03, nullable = true)
	private	Double          F_Val_03;

	@Column(name=COL_F_VAL_04, nullable = true)
	private	Double          F_Val_04;

	@Column(name=COL_F_VAL_05, nullable = true)
	private	Double          F_Val_05;

	@Column(name=COL_F_VAL_06, nullable = true)
	private	Double          F_Val_06;

	@Column(name=COL_F_VAL_07, nullable = true)
	private	Double          F_Val_07;

	@Column(name=COL_F_VAL_08, nullable = true)
	private	Double          F_Val_08;

	@Column(name=COL_F_VAL_09, nullable = true)
	private	Double          F_Val_09;


	//-----------------------Transient Variables-------------------------
	@Transient
	private List<TaTpyDocument>			O_Documents			= null;

	@Transient
	private TaTpyDocument				O_Avatar			= null;

	@Transient
	private List						O_Members			= null;
	
	@Transient
	private List						O_Groups			= null;//--group member

	@Transient
	private List						O_Epics				= null;

	@Transient
	private List						O_Tasks				= null;
	
	@Transient
	private List<TaTpyInformation>		O_History			= null;
	
	@Transient
	private List<TaNsoPost>				O_Comments			= null;
	
	@Transient
	private Hashtable					O_User_Role			= null;
	
	@Transient
	private Object						O_Epic_Info			= null;
	
	@Transient
	private TaPrjProject			    O_Parent			= null;
	
	@Transient
	private TaPrjProject			    O_Project			= null;
	
	@Transient
	private Object			    		O_Tree				= null;
	
	@Transient
	private Double[]			    	O_Nb_Epic_Task_Size_Doc			= null;
	
	@Transient
	private Double[]			    	O_Nb_Child_Size_Doc				= null;
	
	
	//---------------------Constructeurs-----------------------
	public TaPrjProject(){}

	public TaPrjProject(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
//		doInitDBFlag();
	}

	public TaPrjProject(Integer I_Group) throws Exception {
		this.reqSetAttr(
				ATT_I_GROUP      , I_Group
				);
		doInitDBFlag();
	}
	public TaPrjProject(Integer I_Group, String T_Code_01, String T_Name, String T_Info_01, String T_Info_02, String T_Code_02, Integer I_Type_01, Integer I_Type_02, Integer I_Status, Integer I_Level, Date D_Date_01, Date D_Date_02, Date D_Date_03, Date D_Date_04, Integer I_Aut_User_01, Integer I_Aut_User_02, Integer I_Per_Manager, Integer I_Parent, Double F_Val_01, Double F_Val_02, Double F_Val_03, Double F_Val_04, Double F_Val_05) throws Exception {
		this.reqSetAttr(
				ATT_I_GROUP                , I_Group,
				ATT_T_CODE_01                 , T_Code_01,
				ATT_T_NAME                 , T_Name,
				ATT_T_INFO_01       		, T_Info_01,
				ATT_T_INFO_02     		  , T_Info_02,
				ATT_T_CODE_02                  , T_Code_02,
				ATT_I_TYPE_01              , I_Type_01,
				ATT_I_TYPE_02              , I_Type_02,
				ATT_I_STATUS_01               , I_Status,
				ATT_I_LEVEL                , I_Level,
				ATT_D_DATE_01             , D_Date_01,
				ATT_D_DATE_02             , D_Date_02,
				ATT_D_DATE_03           , D_Date_03,
				ATT_D_DATE_04             , D_Date_04,
				ATT_I_AUT_USER_01          , I_Aut_User_01,
				ATT_I_AUT_USER_02          , I_Aut_User_02,
				ATT_I_PER_MANAGER          , I_Per_Manager,
				ATT_I_PARENT               , I_Parent,
				ATT_F_VAL_00               , F_Val_00,
				ATT_F_VAL_01               , F_Val_01,
				ATT_F_VAL_02               , F_Val_02,
				ATT_F_VAL_03               , F_Val_03,
				ATT_F_VAL_04               , F_Val_04,
				ATT_F_VAL_05               , F_Val_05
				);
		doInitDBFlag();
	}


	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;

		ok = (I_ID == ((TaPrjProject)o).I_ID);
		if (!ok) return ok;


		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}

	public void doBuildDocuments(boolean forced) {
		if (!forced && this.O_Documents != null) return;

		try {
			this.O_Documents = TaTpyDocument.reqTpyDocuments (DefDBExt.ID_TA_PRJ_PROJECT, this.I_ID, null,null);
			
			if (this.O_Documents!=null) {
				for (TaTpyDocument d: O_Documents) {
					Integer i1 = d.reqInt(TaTpyDocument.ATT_I_TYPE_01);
					Integer i2 = d.reqInt(TaTpyDocument.ATT_I_TYPE_02);
					if (i1.equals(TaTpyDocument.TYPE_01_FILE_MEDIA)&&i2.equals(TaTpyDocument.TYPE_02_FILE_IMG_AVATAR)) {
						O_Avatar = d;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doBuildAvatar(boolean forced) {
		if (!forced && this.O_Avatar != null) return;

		try {
			this.O_Avatar = TaTpyDocument.DAO.reqEntityByValues(
					TaTpyDocument.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_PRJ_PROJECT,
					TaTpyDocument.ATT_I_ENTITY_ID	, this.I_ID,
					TaTpyDocument.ATT_I_TYPE_01		, TaTpyDocument.TYPE_01_FILE_MEDIA,
					TaTpyDocument.ATT_I_TYPE_02		, TaTpyDocument.TYPE_02_FILE_IMG_AVATAR);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doBuildEpicInfo(boolean forced) throws Exception {
		if (!forced && this.O_Epic_Info != null) return;

		this.O_Epic_Info  = ViPrjProjectSimple.reqEpicInfo(this.I_Group, forced);
	}
	
	public void doBuildEpics(boolean forced) {
		if (!forced && this.O_Epics != null) return;
		if (this.I_Type_02 == TaPrjProject.TYP_02_PRJ_ELE) return;
		
		try {
			Criterion cri = Restrictions.and(Restrictions.eq(ViPrjProjectSimple.ATT_I_TYPE_02	, TYP_02_PRJ_SUB), Restrictions.eq(ViPrjProjectSimple.ATT_I_PARENT, this.I_ID));
			this.O_Epics = ViPrjProjectSimple.DAO.reqList(cri);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doBuildTasks(boolean forced) {
		if (!forced && this.O_Tasks != null) return;
		if (this.I_Type_02 == TaPrjProject.TYP_02_PRJ_ELE) return;
		try {
			Criterion cri = Restrictions.and(Restrictions.eq(ViPrjProjectSimple.ATT_I_TYPE_02	, TYP_02_PRJ_ELE), Restrictions.eq(ViPrjProjectSimple.ATT_I_PARENT, this.I_ID));
			this.O_Tasks = ViPrjProjectSimple.DAO.reqList(cri);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doBuildSprintTasks(boolean forced) {
		if (!forced && this.O_Tasks != null) return;

		try {
			String strIds  = this.T_Info_02;
			if(strIds == null) return;
			
			JSONObject ids 		= ToolJSON.reqJSonObjectFromString(strIds);
			JSONArray  task_ids = (JSONArray) ids.get("task_ids");
			
			Set<Integer> setIds = new HashSet<Integer>();
			for(int i=0; i< task_ids.size(); i++) {
				setIds.add((int)(long) task_ids.get(i));
			}
			
			this.O_Tasks = ViPrjProjectSimple.DAO.reqList_In(ViPrjProjectSimple.ATT_I_ID, setIds, 
					Restrictions.eq(ViPrjProjectSimple.ATT_I_GROUP		, this.I_Group),
					Restrictions.eq(ViPrjProjectSimple.ATT_I_TYPE_02	, TYP_02_PRJ_ELE));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doBuildTestGroup(boolean forced) {
		if (!forced && this.O_Tasks != null) return;

		try {
			String strIds  = this.T_Info_02;
			if(strIds == null) return;
			JSONObject ids 		= ToolJSON.reqJSonObjectFromString(strIds);
			JSONArray  task_ids = (JSONArray) ids.get("task_ids");
			
			Set<Integer> setIds = new HashSet<Integer>();
			for(int i=0; i< task_ids.size(); i++) {
				setIds.add((int)(long) task_ids.get(i));
			}
			
			this.O_Tasks = ViPrjProjectSimple.DAO.reqList_In(ViPrjProjectSimple.ATT_I_ID, setIds, 
					Restrictions.eq(ViPrjProjectSimple.ATT_I_GROUP		, this.I_Group),
					Restrictions.eq(ViPrjProjectSimple.ATT_I_TYPE_00	, TYP_00_PRJ_TEST),
					Restrictions.eq(ViPrjProjectSimple.ATT_I_TYPE_01	, TYP_01_TEST_UNIT));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doBuildTestGroupHistory(boolean forced) {
		if (!forced && this.O_History != null) return;

		try {
			this.O_History = TaTpyInformation.DAO.reqList(Restrictions.and(
					Restrictions.eq(TaTpyInformation.ATT_I_ENTITY_ID	, this.I_ID),
					Restrictions.eq(TaTpyInformation.ATT_I_ENTITY_TYPE	, TYP_00_PRJ_TEST)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doAddDocument(TaTpyDocument doc) throws Exception {
		if (O_Documents==null){
			O_Documents = new ArrayList<TaTpyDocument>();
		}
		O_Documents.add(doc);
	}
	
	public void doBuildParent(boolean forced, TaAutUser user) throws Exception {
		if (!forced && this.O_Parent != null) return;
		if (I_Parent!=null) {
			O_Parent = TaPrjProject.reqPrjFromCache(user, I_Parent);
			
			if (O_Parent == null)
				O_Parent = TaPrjProject.DAO.reqEntityByID(I_Parent);
			
			if (I_Parent.equals(I_Group)) 
				O_Project = O_Parent;
			else
				O_Project = TaPrjProject.DAO.reqEntityByID(I_Group);
		}
	}
	
	public void doCopyInf03FromPrjMain(Boolean forced) throws Exception{
		if (this.O_Project!=null)
			this.T_Info_03 = ((TaPrjProject) this.O_Project).reqStr(ATT_T_INFO_03);
	}


	public Integer reqId() {
		return this.I_ID;
	}
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public static List<TaPrjProject> reqListPrjFilter(int begin, int countRow, String orderCol, String orderDir, Criterion cri) throws Exception {
		List<TaPrjProject> result = null;

//		Criterion cri = reqCri(searchKey, manId, uId ,typ01, typ02, status, valMin, valMax, group, stat, searchUser);
		
		Order 		order 	= null;
		List<Order> orders 	= null;
		if (orderCol!=null && orderDir!=null ) {
			if 		(orderDir.equals("DESC")) order = Order.desc(orderCol);
			else if (orderDir.equals("ASC"))  order = Order.asc(orderCol);
		} else {
			orders = new ArrayList<Order>();
			orders.add(Order.desc(TaPrjProject.ATT_I_LEVEL));
			orders.add(Order.asc(TaPrjProject.ATT_D_DATE_04));
		}
		
		if (order!=null)
			result = TaPrjProject.DAO.reqList(begin, countRow, order, cri);
		else 
			result = TaPrjProject.DAO.reqList(begin, countRow, orders, cri);
		
		return result;
	}


	private static Criterion reqRestrictionFromNameCodeTag(List<String> searchKey) {	
		if (searchKey==null || searchKey.size()==0) return null;
		
		Criterion cri = null;
		for (String s: searchKey){	
			if (s.equals("%")) continue;
			if (cri ==null) 
				cri = Restrictions.or(
						Restrictions.ilike(TaPrjProject.ATT_T_NAME, s), 
						Restrictions.ilike(TaPrjProject.ATT_T_CODE_01, s),
						Restrictions.ilike(TaPrjProject.ATT_T_CODE_02, s));
			else {
				cri = Restrictions.and(cri, Restrictions.or(
						Restrictions.ilike(TaPrjProject.ATT_T_NAME, s), 
						Restrictions.ilike(TaPrjProject.ATT_T_CODE_01, s),
						Restrictions.ilike(TaPrjProject.ATT_T_CODE_02, s)));
			}
		}

		return cri;
	}
	
	private static Criterion reqRestrictionFromUsername(List<String> searchKey) throws Exception {	
		if (searchKey==null || searchKey.size()==0) return null;
		
		Criterion cri = null;
		for (String s: searchKey){	
			if (s.equals("%")) continue;
			if (cri ==null) 
				cri = Restrictions.ilike(ViAutUserMember.ATT_T_LOGIN_01, "%" + s + "%");
			else {
				cri = Restrictions.and(cri, Restrictions.ilike(ViAutUserMember.ATT_T_LOGIN_01, "%" + s + "%"));
			}
		}

		if (cri!=null) {
			Set<Integer> 		setUids = new HashSet<Integer>();
			List<ViAutUserMember> 	users 	= ViAutUserMember.DAO.reqList(cri);
			if(users != null && users.size() > 0) {
				setUids = ToolSet.reqSetInt(users, ViAutUserMember.ATT_I_ID);
			}
			
			if (setUids!=null && setUids.size()>0) {
				Criterion criU = Restrictions.and(
						Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT),
						Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER),
						Restrictions.in(TaTpyRelationship.ATT_I_ENTITY_ID_02	, setUids));
				List<TaTpyRelationship> rel = TaTpyRelationship.DAO.reqList(criU);
				Set<Integer> 			ids = ToolSet.reqSetInt(rel, TaTpyRelationship.ATT_I_ENTITY_ID_01);
				if (ids!=null && ids.size()>0)
					cri = Restrictions.in(TaPrjProject.ATT_I_ID, ids);
				else 
					cri = null;
			}else cri = null;
		}
		return cri;
	}


	public static Number reqCountPrjFilter(Criterion cri) throws Exception {
		return TaPrjProject.DAO.reqCount(cri);
	}

	public static Criterion reqCri(List<String> searchKey, Integer manId, Integer uId, Integer typ00, Integer typ01, Integer typ02, Double valMin, Double valMax, Integer group, Set<Integer>stats) throws Exception{
		Criterion cri = Restrictions.eq(TaPrjProject.ATT_I_PER_MANAGER, manId);

		if(typ00 != null)
			cri = Restrictions.and(cri, Restrictions.eq(TaPrjProject.ATT_I_TYPE_00, typ00));
		
		if(typ01 != null)
			cri = Restrictions.and(cri, Restrictions.eq(TaPrjProject.ATT_I_TYPE_01, typ01));

		if(typ02 != null)
			cri = Restrictions.and(cri, Restrictions.eq(TaPrjProject.ATT_I_TYPE_02, typ02));

		if(stats != null) {
			cri = Restrictions.and(cri, Restrictions.in(TaPrjProject.ATT_I_STATUS_01, stats));
		} 
			
		if(group != null)
			cri = Restrictions.and(cri, Restrictions.eq(TaPrjProject.ATT_I_GROUP, group));

		
		if (valMin!=null) {
			cri = Restrictions.and(cri, Restrictions.or(
					Restrictions.ge(TaPrjProject.ATT_F_VAL_01, valMin),
					Restrictions.ge(TaPrjProject.ATT_F_VAL_02, valMin),
					Restrictions.ge(TaPrjProject.ATT_F_VAL_03, valMin),
					Restrictions.ge(TaPrjProject.ATT_F_VAL_04, valMin),
					Restrictions.ge(TaPrjProject.ATT_F_VAL_05, valMin)
					));
		}

		if (valMax!=null) {
			cri = Restrictions.and(cri, Restrictions.or(
					Restrictions.le(TaPrjProject.ATT_F_VAL_01, valMax),
					Restrictions.le(TaPrjProject.ATT_F_VAL_02, valMax),
					Restrictions.le(TaPrjProject.ATT_F_VAL_03, valMax),
					Restrictions.le(TaPrjProject.ATT_F_VAL_04, valMax),
					Restrictions.le(TaPrjProject.ATT_F_VAL_05, valMax)
					));
		}
		
		//--- with user view list task
		Criterion criUserMain	= null;
		if (uId !=null) {
			Criterion criU = Restrictions.and(
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02	, uId));
			List<TaTpyRelationship> rel = TaTpyRelationship.DAO.reqList(criU);
			
			Set<Integer> ids = ToolSet.reqSetInt(rel, TaTpyRelationship.ATT_I_ENTITY_ID_01);
			
			if (ids!=null && ids.size()>0)
				criUserMain = Restrictions.or(Restrictions.eq(TaPrjProject.ATT_I_AUT_USER_01, uId), Restrictions.in(TaPrjProject.ATT_I_ID, ids));
			else 
				criUserMain = Restrictions.eq(TaPrjProject.ATT_I_AUT_USER_01, uId);
		}
		if (criUserMain!=null) 
			cri = Restrictions.and(cri, criUserMain);
		
		//--- filter list user search
		Criterion criUserSearch = reqRestrictionFromUsername (searchKey);		

		//----combine with search project by keyword-----
		Criterion criKey =   reqRestrictionFromNameCodeTag (searchKey);		
		
		if (criKey !=null && criUserSearch!=null)
			cri = Restrictions.and(cri, Restrictions.or(criKey,criUserSearch));
		else if (criKey !=null)
			cri = Restrictions.and(cri, criKey);
		else if (criUserSearch !=null)
			cri = Restrictions.and(cri, criUserSearch);
		
		return cri;
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------------
	public static List<TaPrjProject> reqListPrjLate(int begin, int countRow, String orderCol, String orderDir, List<String> searchKey, Integer manId, Integer uId, Integer typ00, Integer typ01, Integer typ02) throws Exception {
		List<TaPrjProject> result = null;

		Criterion cri = reqCriLate(searchKey, manId, uId ,typ00, typ01, typ02);
		
		Order order = null;
		if (orderCol!=null && orderDir!=null )
			if 		(orderDir.equals("DESC")) order = Order.desc(orderCol);
			else if (orderDir.equals("ASC"))  order = Order.asc(orderCol);

		if (order!=null)
			result = TaPrjProject.DAO.reqList(begin, countRow, order, cri);
		else 
			result = TaPrjProject.DAO.reqList(begin, countRow, cri);

		return result;
	}
	
	public static Number reqCountPrjLate(List<String> searchKey, Integer manId, Integer uId , Integer typ00, Integer typ01, Integer typ02) throws Exception {
		Criterion cri = reqCriLate(searchKey, manId, uId ,typ00, typ01, typ02);
		
		return TaPrjProject.DAO.reqCount(cri);
	}
	
	private static Criterion reqCriLate(List<String> searchKey, Integer manId, Integer uId , Integer typ00, Integer typ01, Integer typ02) throws Exception{
		Criterion cri = Restrictions.eq(TaPrjProject.ATT_I_PER_MANAGER, manId);

		if(typ00 != null)
			cri = Restrictions.and(cri, Restrictions.eq(TaPrjProject.ATT_I_TYPE_00, typ00));
		
		if(typ01 != null)
			cri = Restrictions.and(cri, Restrictions.eq(TaPrjProject.ATT_I_TYPE_01, typ01));

		if(typ02 != null)
			cri = Restrictions.and(cri, Restrictions.eq(TaPrjProject.ATT_I_TYPE_02, typ02));

		List<Integer> stats = Arrays.asList(TaPrjProject.STAT_01_PRJ_TODO, TaPrjProject.STAT_01_PRJ_INPROGRESS, TaPrjProject.STAT_01_PRJ_REVIEW, TaPrjProject.STAT_01_PRJ_TEST);
		cri = Restrictions.and(cri, Restrictions.in(TaPrjProject.ATT_I_STATUS_01, stats));

		Criterion criKey =   reqRestrictionFromNameCodeTag (searchKey);
		if (criKey !=null)
			cri = Restrictions.and(cri, criKey);

		
		Date dtMax = new Date();
		cri = Restrictions.and(cri, Restrictions.le(TaPrjProject.ATT_D_DATE_04		, dtMax));

		Criterion criU = null;
		if (uId !=null) {
			criU = Restrictions.and(
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01	, DefDBExt.ID_TA_PRJ_PROJECT),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02	, DefDBExt.ID_TA_AUT_USER),
					Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_ID_02	, uId));
			List<TaTpyRelationship> rel = TaTpyRelationship.DAO.reqList(criU);
			Set<Integer> 			ids = ToolSet.reqSetInt(rel, TaTpyRelationship.ATT_I_ENTITY_ID_01);
			if (ids!=null && ids.size()>0)
				criU = Restrictions.or(Restrictions.eq(TaPrjProject.ATT_I_AUT_USER_01, uId), Restrictions.in(TaPrjProject.ATT_I_ID, ids));
			else 
				criU = Restrictions.eq(TaPrjProject.ATT_I_AUT_USER_01, uId);
		}

		if (criU!=null) cri = Restrictions.and(criU, cri);
		return cri;
	}
	
	public  void doAddChild(Object prj) {
		if (this.O_Epics==null) this.O_Epics = new ArrayList<>();
		this.O_Epics.add(prj);
	}
	
	public  void doAddMember(TaTpyRelationship r) {
		if (this.O_Members==null) this.O_Members = new ArrayList();
		this.O_Members.add(r);
	}
	
	public  void doAddGroupMember(TaTpyRelationship r) {
		if (this.O_Groups==null) this.O_Groups = new ArrayList();
		this.O_Groups.add(r);
	}

	@Override
	public void doMergeWith(TaPrjProject arg0) {
		// TODO Auto-generated method stub
		
	}
	//-----------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------
	
	public static void doBuildGrpMemberForList(List<TaPrjProject>  list) throws Exception {
		if (list==null && list.size()==0) return;
		Set<Integer> 			ids 	= ToolSet.reqSetInt(list, ATT_I_ID);
	
		List<TaTpyRelationship> lstGrps	= TaTpyRelationship.reqList (	DefDBExt.ID_TA_PRJ_PROJECT, ids, DefDBExt.ID_TA_NSO_GROUP, null);
		
		
		if(lstGrps != null && lstGrps.size() > 0) {
			//---build users + their avatars
			Set<Integer> 			setG 		= ToolSet.reqSetInt(lstGrps, TaTpyRelationship.ATT_I_ENTITY_ID_02);
			ServicePrjProject.doBuildGroupMemberInfo (setG, lstGrps);
			
			Hashtable<Integer, EntityAbstract> map = ToolDBEntity.reqTabKeyInt(list, ATT_I_ID);
			for (TaTpyRelationship r: lstGrps) {
				int pId = r.reqInt(TaTpyRelationship.ATT_I_ENTITY_ID_01);
				TaPrjProject p = (TaPrjProject) map.get(pId);
				if (p!=null) p.doAddGroupMember(r);
			}
		}
	}

	public static void doBuildMemberForList(List<TaPrjProject>  list) throws Exception {
		if (list==null && list.size()==0) return;
		Set<Integer> 			ids 	= ToolSet.reqSetInt(list, ATT_I_ID);
	
		List<TaTpyRelationship> lstMems = TaTpyRelationship.DAO.reqList_In(TaTpyRelationship.ATT_I_ENTITY_ID_01, ids,
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_01		, DefDBExt.ID_TA_PRJ_PROJECT), 
				Restrictions.eq(TaTpyRelationship.ATT_I_ENTITY_TYPE_02		, DefDBExt.ID_TA_AUT_USER)
				);
	
		if(lstMems != null && lstMems.size() > 0) {
			//---build users + their avatars
			List<ViAutUserMember> lstUsers = TaAutUser		.reqBuildUserMember	(lstMems, TaTpyRelationship.ATT_I_ENTITY_ID_02, TaTpyRelationship.ATT_O_ENTITY_02);
			TaTpyDocument	.reqBuildAvatar(lstUsers, DefDBExt.ID_TA_AUT_USER, ViAutUserMember.ATT_O_AVATAR);
			
			Hashtable<Integer, EntityAbstract> map = ToolDBEntity.reqTabKeyInt(list, ATT_I_ID);
			for (TaTpyRelationship r: lstMems) {
				int pId = r.reqInt(TaTpyRelationship.ATT_I_ENTITY_ID_01);
				TaPrjProject p = (TaPrjProject) map.get(pId);
				if (p!=null) p.doAddMember(r);
			}
		}
	}

	public static void doBuildAvatarForList(List<TaPrjProject>  list) throws Exception {
		if (list==null && list.size()==0) return;
		Set<Integer> 			ids 	= ToolSet.reqSetInt(list, ATT_I_ID);
		List<TaTpyDocument> 	docs 	= TaTpyDocument.reqTpyDocuments (DefDBExt.ID_TA_PRJ_PROJECT, ids, TaTpyDocument.TYPE_01_FILE_MEDIA, TaTpyDocument.TYPE_02_FILE_IMG_AVATAR, null, null);
	
		if(docs != null && docs.size() > 0) {
			Map<Integer, TaTpyDocument> mapDoc 	= new HashMap<Integer, TaTpyDocument>();		
			for (TaTpyDocument d: docs){
				mapDoc.put((int) d.req(TaTpyDocument.ATT_I_ENTITY_ID), d);
			}
	
			if(!mapDoc.isEmpty()) {
				for (TaPrjProject p: list){
					if(mapDoc.containsKey(p.reqRef())) {
						p.reqSet(ATT_O_AVATAR, mapDoc.get(p.reqRef()));
					}
				}
			}
		}
	}

	
	public static void doBuildParentForList(List<TaPrjProject>  list) throws Exception {
		if (list==null && list.size()==0) return;
		Set<Integer> pIds = ToolSet.reqSetInt(list, ATT_I_GROUP);
	
		List<TaPrjProject> 	listPrj = DAO.reqList_In(ATT_I_ID   , pIds,
				Restrictions.eq(ATT_I_TYPE_02    , TYP_02_PRJ_MAIN));		
	
		Hashtable  			tabPrj = ToolDBEntity.reqTabKeyInt(listPrj, ATT_I_ID);	
		for(TaPrjProject prj : list) {
			Integer pId = (Integer) prj.req(ATT_I_GROUP);
	
			prj.reqSet(ATT_O_PROJECT, tabPrj.get(pId));
		}
	}
	//---------------------------------------------------------------------------------------------------------
	public void doPutRole (Integer userId, Integer roleLev) {
		if (userId==null || roleLev==null) return;
		if (this.O_User_Role==null) this.O_User_Role = new Hashtable<Integer, Integer>();
		O_User_Role.put(userId, roleLev);
	}
	public boolean canHaveRole (Integer userId) {
		if (userId==null) return false;
		if (this.O_User_Role==null) return false;
		
		return O_User_Role.contains(userId);		
	}
	public boolean canHaveRoleManager (Integer userId) {
		if (userId==null) return false;
		if (this.O_User_Role==null) return false;
		
		Integer level = (Integer) O_User_Role.get(userId);	
		if (level!=null && level == TaPrjProject.LEV_ROLE_PRJ_MANAGER) return true;
		return false;
	}
	//---------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------
	public static CacheData<TaPrjProject> cache_Ent = new CacheData<TaPrjProject>(100, DefTime.TIME_12_00_00_000);
	
	public static TaPrjProject reqPrjFromCache (TaAutUser user, JSONObject json) {
		Integer 			objId	= ToolData.reqInt	(json, "id"		, null	);
		if (objId==null)	objId	= ToolData.reqInt	(json, "prjId"	, null	);
		String	 			objCode	= ToolData.reqStr	(json, "code"	, null	);
		if (objCode==null)	objCode	= ToolData.reqStr	(json, "prjCode", null	);
		
		if (objCode==null||objId==null||objId<0) {
			return null;
		}

		String			key		= objId + ":" + objCode;
		TaPrjProject 	prj		= cache_Ent.reqData(key);

		if (!user.canBeSuperAdmin()) {
			if(prj == null || !prj.req(TaPrjProject.ATT_I_PER_MANAGER).equals(user.reqPerManagerId())) {
				return null;
			}
		}

		return prj;
	}

	public static TaPrjProject reqPrjFromCache (TaAutUser user, Integer objId, String objCode) {
		if (objCode==null||objId==null||objId<0) {
			return null;
		}

		String			key		= objId + ":" + objCode;
		TaPrjProject 	prj		= cache_Ent.reqData(key);

		if (!user.canBeSuperAdmin()) {
			if(prj == null || !prj.req(TaPrjProject.ATT_I_PER_MANAGER).equals(user.reqPerManagerId())) {
				return null;
			}
		}

		return prj;
	}

	public static TaPrjProject reqPrjFromCache (TaAutUser user, Integer objId) {
		if (objId==null  || objId<0) {
			return null;
		}

		String			key		= objId + "::" ;
		TaPrjProject 	prj		= cache_Ent.reqData(key);

		if (!user.canBeSuperAdmin()) {
			if(prj == null || !prj.req(TaPrjProject.ATT_I_PER_MANAGER).equals(user.reqPerManagerId())) {
				return null;
			}
		}

		return prj;
	}

	public static void doPutPrjToCache (Integer objId, String	 objCode, TaPrjProject ent) {
		if (ent==null) return;
		
		String			key		= objId + ":" + objCode;
		cache_Ent.reqPut(key, ent);
		key		= objId + "::";
		cache_Ent.reqPut(key, ent);
	}
	
	public static void doDelPrjToCache (Integer objId, String	 objCode) {
		if (objId==null  || objId<0)  return;
		
		String			key		= objId + ":" + objCode;
		cache_Ent.reqDel(key);
		key		= objId + "::";
		cache_Ent.reqDel(key);
	}
}
