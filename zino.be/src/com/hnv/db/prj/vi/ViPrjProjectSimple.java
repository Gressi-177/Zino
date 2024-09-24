package com.hnv.db.prj.vi;

import java.io.Serializable;
import java.util.Date;
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

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hnv.api.def.DefTime;
import com.hnv.api.main.Hnv_CfgHibernate;
import com.hnv.common.tool.ToolDBEntity;
import com.hnv.common.util.CacheData;
import com.hnv.db.EntityAbstract;
import com.hnv.db.EntityDAO;
import com.hnv.db.aut.vi.ViAutUserMember;
import com.hnv.db.nso.TaNsoPost;
import com.hnv.db.prj.TaPrjProject;
import com.hnv.db.tpy.TaTpyCategory;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.db.tpy.TaTpyInformation;
import com.hnv.def.DefDBExt;		

/**
 * TaPrjProject by H&V SAS
 */
@Entity
@Table(name = DefDBExt.TA_PRJ_PROJECT )
public class ViPrjProjectSimple extends EntityAbstract<ViPrjProjectSimple> {

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
		public static final String	ATT_O_PROJECT                      	  =	"O_Project";
		public static final String	ATT_O_EPICS                      	  =	"O_Epics";
		public static final String	ATT_O_TASKS                      	  =	"O_Tasks";
		
		public static final String	ATT_O_COMMENTS                        =	"O_Comments";
		
		public static final String	ATT_O_USER_ROLE                       =	"O_User_Role";
		
		public static final String	ATT_O_EPIC_INFO                       =	"O_Epic_Info";
		public static final String	ATT_O_COUNT_EPIC                      =	"O_Count_Epic";
		public static final String	ATT_O_COUNT_TASK                      =	"O_Count_Task";
		public static final String	ATT_O_COUNT_EPIC_FILE                 =	"O_Count_Epic_File";
		
		public static final String	ATT_O_PARENT                      	  =	"O_Parent";
		
		public static final String	ATT_O_NB_EPIC_TASK_SIZE_DOC           =	"O_Nb_Epic_Task_Size_Doc";
		public static final String	ATT_O_NB_CHILD_SIZE_DOC               =	"O_Nb_Child_Size_Doc";
		
		public static final String	ATT_O_TREE              			  =	"O_Tree";
	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static	final boolean				API_CACHE 	= false;
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<ViPrjProjectSimple> 	DAO;
	static{
		DAO = new EntityDAO<ViPrjProjectSimple>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN) , ViPrjProjectSimple.class, RIGHTS);

	}

	//-----------------------Class Attributs-------------------------
	@Id
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;

	@Column(name=COL_I_GROUP, nullable = false)
	private	Integer         I_Group;

	@Column(name=COL_I_PARENT, nullable = false)
	private	Integer         I_Parent;

	@Column(name=COL_T_CODE_01, nullable = true)
	private	String          T_Code_01;
	
	@Column(name=COL_T_NAME, nullable = true)
	private	String          T_Name;


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

	//-----------------------Transient Variables-------------------------
	@Transient
	private List<ViPrjProjectSimple>	O_Epic_Info			= null;
	
	@Transient
	private Integer						O_Count_Epic		= null;
	
	@Transient
	private Integer						O_Count_Task		= null;
	
	@Transient
	private Integer						O_Count_Epic_File	= null;
	
	
	
	//---------------------Constructeurs-----------------------
	public ViPrjProjectSimple(){}

	private static CacheData<Object> 	cache_entity= new CacheData<Object>	(500, DefTime.TIME_00_30_00_000);
	public static List<ViPrjProjectSimple>  reqEpicInfo(Integer iGroup, boolean forced) throws Exception {
		String 						key 	=  iGroup + "_EPICS";
		List<ViPrjProjectSimple> 	epics 	= (List<ViPrjProjectSimple>) cache_entity.reqData(key);	//we need cache because every task, epic in project request this
		if (forced || epics == null) {
			Set<Integer> typeEpics	= new HashSet<Integer>() ;
			typeEpics.add(TaPrjProject.TYP_02_PRJ_MAIN);
			typeEpics.add(TaPrjProject.TYP_02_PRJ_SUB);
			
			epics = ViPrjProjectSimple.DAO.reqList(Order.asc(ViPrjProjectSimple.ATT_I_TYPE_02),
					Restrictions.eq(ViPrjProjectSimple.ATT_I_GROUP		, iGroup),
					Restrictions.in(ViPrjProjectSimple.ATT_I_TYPE_02	, typeEpics)
					);
			
			if (epics!=null && epics.size()>0) {	
				Hashtable	mapEpics 	= ToolDBEntity.reqTab( epics, ViPrjProjectSimple.ATT_I_ID);
				
				//reset 0
				for (ViPrjProjectSimple epic : epics) {
					epic.O_Count_Task 		= 0;
					epic.O_Count_Epic 		= 0;
					epic.O_Count_Epic_File 	= 0;
				}
				
				List<ViPrjProjectSimple> 	tasks 	=  ViPrjProjectSimple.DAO.reqList(
						Restrictions.eq(ViPrjProjectSimple.ATT_I_GROUP		, iGroup),
						Restrictions.eq(ViPrjProjectSimple.ATT_I_TYPE_02	, TaPrjProject.TYP_02_PRJ_ELE)
						);
				//--compute count task for each epic
				for (ViPrjProjectSimple ent : tasks) {
					Integer 		iParent = ent.reqInt(ent, ViPrjProjectSimple.ATT_I_PARENT);
					ViPrjProjectSimple 	epic 	= iParent!=null?(ViPrjProjectSimple) mapEpics.get(iParent):null;
					if (epic!=null) {
						epic.O_Count_Task ++;
					}
				}
				
				for (ViPrjProjectSimple ent : epics) {
					Integer 		iParent = ent.reqInt(ent	, ViPrjProjectSimple.ATT_I_PARENT);
					ViPrjProjectSimple 	epic 	= iParent!=null?(ViPrjProjectSimple) mapEpics.get(iParent):null;
					if (epic!=null) {
						epic.O_Count_Epic ++;
					}
				}
				
				for (ViPrjProjectSimple epic : epics) {
					Integer 		epicId  = epic.reqInt(epic	, ViPrjProjectSimple.ATT_I_ID);
					epic.O_Count_Epic_File =  TaTpyDocument.DAO.reqCount(
							Restrictions.eq(TaTpyDocument.ATT_I_ENTITY_ID	, epicId),
							Restrictions.eq(TaTpyDocument.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_PRJ_PROJECT)).intValue();;
				}
				cache_entity.reqPut(key, epics);
			}			
		}else {				
			cache_entity.doRefresh(key); 
		}
		
		return epics;
	}

	@Override
	public void doMergeWith(ViPrjProjectSimple arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Serializable reqRef() {
		// TODO Auto-generated method stub
		return I_ID;
	}
	

	
}
