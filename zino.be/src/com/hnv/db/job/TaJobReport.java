package com.hnv.db.job;

import java.io.Serializable;
import java.util.ArrayList;
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

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.hnv.api.main.Hnv_CfgHibernate;
import com.hnv.common.tool.ToolDBEntity;
import com.hnv.common.tool.ToolSet;
import com.hnv.db.EntityAbstract;
import com.hnv.db.EntityDAO;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.per.TaPerPerson;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.def.DefDBExt;		

/**
* TaJobReport by H&V SAS
*/
@Entity
@Table(name = DefDBExt.TA_JOB_REPORT )
public class TaJobReport extends EntityAbstract<TaJobReport> {

	private static final long serialVersionUID = 1L;

	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                              =	"I_ID";
	public static final String	COL_D_DATE_01                         =	"D_Date_01";
	public static final String	COL_D_DATE_02                         =	"D_Date_02";
	public static final String	COL_T_CODE_01                         =	"T_Code_01";
	public static final String	COL_T_CODE_02                         =	"T_Code_02";
	public static final String	COL_T_INFO_01                         =	"T_Info_01";
	public static final String	COL_T_INFO_02                         =	"T_Info_02";
	public static final String	COL_I_STATUS                          =	"I_Status";
	public static final String	COL_I_AUT_USER_01                     =	"I_Aut_User_01"; //---who create
	public static final String	COL_I_AUT_USER_02                     =	"I_Aut_User_02"; //---owner
	public static final String	COL_I_AUT_USER_03                     =	"I_Aut_User_03"; //---who have to validate
	public static final String	COL_I_PER_MANAGER                     =	"I_Per_Manager";
	public static final String	COL_F_VAL_01                          =	"F_Val_01";
	public static final String	COL_F_VAL_02                          =	"F_Val_02";
	public static final String	COL_F_VAL_03                          =	"F_Val_03";
	public static final String	COL_F_VAL_04                          =	"F_Val_04";

	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              =	"I_ID";
	public static final String	ATT_D_DATE_01                         =	"D_Date_01";
	public static final String	ATT_D_DATE_02                         =	"D_Date_02";
	public static final String	ATT_T_CODE_01                         =	"T_Code_01";
	public static final String	ATT_T_CODE_02                         =	"T_Code_02";
	public static final String	ATT_T_INFO_01                         =	"T_Info_01";
	public static final String	ATT_T_INFO_02                         =	"T_Info_02";
	public static final String	ATT_I_STATUS                          =	"I_Status";
	public static final String	ATT_I_AUT_USER_01                     =	"I_Aut_User_01";
	public static final String	ATT_I_AUT_USER_02                     =	"I_Aut_User_02";
	public static final String	ATT_I_AUT_USER_03                     =	"I_Aut_User_03";
	public static final String	ATT_I_PER_MANAGER                     =	"I_Per_Manager";
	public static final String	ATT_F_VAL_01                          =	"F_Val_01";
	public static final String	ATT_F_VAL_02                          =	"F_Val_02";
	public static final String	ATT_F_VAL_03                          =	"F_Val_03";
	public static final String	ATT_F_VAL_04                          =	"F_Val_04";
	
	public static final String	ATT_O_LIST_REPORT_DETAIL              =	"O_List_Report_Detail";
	public static final String	ATT_O_LIST_REPORT_RESUME              =	"O_List_Report_Resume";
	public static final String	ATT_O_PER_CREATE              		  =	"O_Per_Create";    	//uId01
	public static final String	ATT_O_PER_VALIDATE             		  =	"O_Per_Validate";  	//uId02
	public static final String	ATT_O_PER_OWNER             		  =	"O_Per_Owner";  	//uId03
	public static final String	ATT_O_PER_MAN             		  	  =	"O_Per_Man";  		//manId
	public static final String  ATT_O_DOCUMENTS						  = "O_Files";
	
	//---------------------------Variables-----------------------------
	public static final Integer STAT_DRAFT					 	  	  = 0;
	public static final Integer STAT_PENDING						  = 1;
	public static final Integer STAT_VALIDATE						  = 2;
	public static final Integer STAT_DENY						  	  = 3;
	public static final Integer STAT_CLOSED						  	  = 10;
	
	//public static final Integer PR_REPORT_STAT_DENIED 						  = 3;
	//public static final Integer PR_REPORT_STAT_RESUME_UPDATED 				  = 4;
	
	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<TaJobReport> 	DAO;

	
	static{
		DAO = new EntityDAO<TaJobReport>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), TaJobReport.class,RIGHTS);
	}

	//-----------------------Class Attributs-------------------------
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;
         
       
	@Column(name=COL_D_DATE_01, nullable = false)
	private	Date            D_Date_01;
    
	@Column(name=COL_D_DATE_02, nullable = true)
	private	Date            D_Date_02;
    
	@Column(name=COL_T_CODE_01, nullable = true)
	private	String          T_Code_01;

	@Column(name=COL_T_CODE_02, nullable = true)
	private	String          T_Code_02;
       
	@Column(name=COL_T_INFO_01, nullable = true)
	private	String          T_Info_01;

	@Column(name=COL_T_INFO_02, nullable = true)
	private	String          T_Info_02;
    
	@Column(name=COL_I_STATUS, nullable = true)
	private	Integer         I_Status;
     
	@Column(name=COL_I_AUT_USER_01, nullable = true)
	private	Integer         I_Aut_User_01;

	@Column(name=COL_I_AUT_USER_02, nullable = true)
	private	Integer         I_Aut_User_02;

	@Column(name=COL_I_AUT_USER_03, nullable = false)
	private	Integer         I_Aut_User_03;

	@Column(name=COL_I_PER_MANAGER, nullable = true)
	private	Integer         I_Per_Manager;

	@Column(name=COL_F_VAL_01, nullable = true)
	private	Double          F_Val_01;
	
	@Column(name=COL_F_VAL_02, nullable = true)
	private	Double          F_Val_02;
	
	@Column(name=COL_F_VAL_03, nullable = true)
	private	Double          F_Val_03;
	
	@Column(name=COL_F_VAL_04, nullable = true)
	private	Double          F_Val_04;

	//-----------------------Transient Variables---------------
	@Transient
	private	List<TaJobReportDetail> 		O_List_Report_Detail;
	@Transient
	private	List<TaJobReportResume> 		O_List_Report_Resume;
	
	@Transient
	private String							O_Per_Create;
	@Transient
	private String							O_Per_Validate;
	@Transient
	private String							O_Per_Owner;
	@Transient
	private String							O_Per_Man;
	@Transient
	private List<TaTpyDocument> 			O_Files;

	//---------------------Constructeurs-----------------------
	private TaJobReport(){}

	public TaJobReport(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		//doInitDBFlag();
	}
	
	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}

	@Override
	public void doMergeWith(TaJobReport ent) {
		if (ent == this) return;
		this.T_Code_01              = ent.T_Code_01;
		this.T_Code_02              = ent.T_Code_02;
		this.D_Date_01              = ent.D_Date_01;
		this.D_Date_02              = ent.D_Date_02;
		this.T_Info_01              = ent.T_Info_01;
		this.T_Info_02              = ent.T_Info_02;
		this.I_Status               = ent.I_Status;
		this.I_Aut_User_01          = ent.I_Aut_User_01;
		this.I_Aut_User_02          = ent.I_Aut_User_02;
		this.I_Aut_User_03          = ent.I_Aut_User_03;
		this.I_Per_Manager          = ent.I_Per_Manager;
		this.F_Val_01               = ent.F_Val_01;
		this.F_Val_02               = ent.F_Val_02;
		this.F_Val_03               = ent.F_Val_03;
		this.F_Val_04               = ent.F_Val_04;
		//---------------------Merge Transient Variables if exist-----------------------
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;
		
		ok = (I_ID == ((TaJobReport)o).I_ID);
		if (!ok) return ok;

				
		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}

	public Integer reqId() {
		return this.I_ID;
	}
	
	public void doBuildListReportDetail(boolean forced) throws Exception{
		if (!forced && O_List_Report_Detail!=null) return;
			
		List<TaJobReportDetail> lstReportDetail = new ArrayList<TaJobReportDetail>();
		Criterion cri 	= Restrictions.eq(TaJobReportDetail.COL_I_JOB_REPORT, I_ID);
		lstReportDetail 						= TaJobReportDetail.DAO.reqList(cri);
		this.O_List_Report_Detail = lstReportDetail;
	}
	
	public void doBuildListReportResume(boolean forced) throws Exception{
		if (!forced && O_List_Report_Resume!=null) return;
			
		Criterion cri 	= Restrictions.eq(TaJobReportResume.COL_I_JOB_REPORT, I_ID);
		this.O_List_Report_Resume = TaJobReportResume.DAO.reqList(cri);
	}
	
	
	//------------------------------------------------------------------------------------------------------------------
	public void doBuildPerCreateAndOwner(boolean forced) throws Exception{
		if (!forced && O_Per_Create!=null) return;
		TaAutUser 	aut1 = TaAutUser.DAO.reqEntityByRef(I_Aut_User_01);
		TaPerPerson per1 = TaPerPerson.DAO.reqEntityByRef((int) aut1.req(TaAutUser.ATT_I_PER_PERSON));
		if(per1 != null){
			this.O_Per_Create = per1.req(TaPerPerson.ATT_T_NAME_01) +  " " + per1.req(TaPerPerson.ATT_T_NAME_02) + " " + per1.req(TaPerPerson.ATT_T_NAME_03);
		}
		if (O_Per_Owner !=null) return;
		TaAutUser 	aut3 = TaAutUser.DAO.reqEntityByRef(I_Aut_User_03);
		TaPerPerson per3 = TaPerPerson.DAO.reqEntityByRef((int) aut3.req(TaAutUser.ATT_I_PER_PERSON));
		if(per3 != null){
			this.O_Per_Owner = per3.req(TaPerPerson.ATT_T_NAME_01) +  " " + per3.req(TaPerPerson.ATT_T_NAME_02) +  " " + per3.req(TaPerPerson.ATT_T_NAME_03);
		}
		
		if (O_Per_Man !=null) return;
		if (I_Per_Manager == null) return;
		TaPerPerson perMan = TaPerPerson.DAO.reqEntityByRef(I_Per_Manager);
		if(perMan != null){
			this.O_Per_Man = perMan.req(TaPerPerson.ATT_T_NAME_01) +  " " + perMan.req(TaPerPerson.ATT_T_NAME_02) +  " " + perMan.req(TaPerPerson.ATT_T_NAME_03);
		}
	}
	
	public void doBuildPerValidate(boolean forced) throws Exception{
		if (!forced && O_Per_Validate!=null) return;
		if (I_Aut_User_02 == null) return;
		TaAutUser 	aut2 = TaAutUser.DAO.reqEntityByRef(I_Aut_User_02);	
		TaPerPerson per2 = TaPerPerson.DAO.reqEntityByRef((int) aut2.req(TaAutUser.ATT_I_PER_PERSON));
		if(per2 != null){
			this.O_Per_Validate = per2.req(TaPerPerson.ATT_T_NAME_01) +  " " + per2.req(TaPerPerson.ATT_T_NAME_02) +  " " + per2.req(TaPerPerson.ATT_T_NAME_03);
		}
	}
	//----------------------------------------------------------------------------------------------------------------
	public void doBuildFiles(boolean forced) throws Exception {
		if (!forced && O_Files!=null) return;

		this.O_Files = TaTpyDocument.DAO.reqList(
				Restrictions.eq(TaTpyDocument.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_JOB_REPORT),
				Restrictions.eq(TaTpyDocument.ATT_I_ENTITY_ID	, this.I_ID));			
	}
	
	public void doAddReportResume(TaJobReportResume rs) {
		if (O_List_Report_Resume ==null) O_List_Report_Resume = new ArrayList<TaJobReportResume>();
		O_List_Report_Resume.add(rs);
	}
	//-----------------------------------------------------------------------------------------------------------------
	private static TaPerPerson reqPerson (Hashtable tabUser , Hashtable tabPer, Integer idUser) {
		TaAutUser 	user = null;
		TaPerPerson	pers = null;
		Integer 	idPers;
		if (idUser!=null) {
			user = (TaAutUser) tabUser.get(idUser);
			if(user!=null) {
				idPers = user.reqInt(user, TaAutUser.ATT_I_PER_PERSON);
				if(idPers!=null) {
					pers = (TaPerPerson) tabPer.get(idPers);
				} else 
					pers = null;
			}
		}
		return pers;
	}
	
	public static void doBuildInfo(List<TaJobReport> list) throws Exception{
		Set<Integer> setIdUser01 = ToolSet.reqSetInt(list, TaJobReport.ATT_I_AUT_USER_01);
		Set<Integer> setIdUser02 = ToolSet.reqSetInt(list, TaJobReport.ATT_I_AUT_USER_02);
		Set<Integer> setIdUser03 = ToolSet.reqSetInt(list, TaJobReport.ATT_I_AUT_USER_03);
		Set<Integer> setIdUsers	 = new HashSet<Integer>();
		
		if (setIdUser01!=null && setIdUser01.size()>0) setIdUsers.addAll(setIdUser01);
		if (setIdUser02!=null && setIdUser02.size()>0) setIdUsers.addAll(setIdUser02);
		if (setIdUser03!=null && setIdUser03.size()>0) setIdUsers.addAll(setIdUser03);
		List<TaAutUser> setUsers = TaAutUser.DAO.reqList_In(TaAutUser.ATT_I_ID, setIdUsers);
		
		
		Set<Integer> setIdPers 		= ToolSet.reqSetInt(setUsers, TaAutUser.ATT_I_PER_PERSON);
		Set<Integer> setIdPerMans 	= ToolSet.reqSetInt(list, TaJobReport.ATT_I_PER_MANAGER);
		if (setIdPerMans!=null && setIdPerMans.size()>0) setIdPers.addAll(setIdPerMans);
		
		List<TaPerPerson> setPers 	= TaPerPerson.DAO.reqList_In(TaPerPerson.ATT_I_ID, setIdPers);
		
		Hashtable tabUser 	= ToolDBEntity.reqTab(setUsers, TaAutUser.ATT_I_ID);
		Hashtable tabPer 	= ToolDBEntity.reqTab(setPers , TaPerPerson.ATT_I_ID);
		
		TaAutUser 	user;
		TaPerPerson	pers;
		Integer 	idUser, idPers;
		for (TaJobReport rp: list) {
			idUser 	= rp.reqInt(rp, TaJobReport.ATT_I_AUT_USER_01);
			pers	= reqPerson(tabUser, tabPer, idUser); 
			if (pers!=null) {
				rp.O_Per_Create = pers.req(TaPerPerson.ATT_T_NAME_01) +  " " + pers.req(TaPerPerson.ATT_T_NAME_02) + " " + pers.req(TaPerPerson.ATT_T_NAME_03);
			}
			
			idUser 	= rp.reqInt(rp, TaJobReport.ATT_I_AUT_USER_02);
			pers	= reqPerson(tabUser, tabPer, idUser); 
			if (pers!=null) {
				rp.O_Per_Validate = pers.req(TaPerPerson.ATT_T_NAME_01) +  " " + pers.req(TaPerPerson.ATT_T_NAME_02) + " " + pers.req(TaPerPerson.ATT_T_NAME_03);
			}
			
			idUser 	= rp.reqInt(rp, TaJobReport.ATT_I_AUT_USER_03);
			pers	= reqPerson(tabUser, tabPer, idUser); 
			if (pers!=null) {
				rp.O_Per_Owner = pers.req(TaPerPerson.ATT_T_NAME_01) +  " " + pers.req(TaPerPerson.ATT_T_NAME_02) + " " + pers.req(TaPerPerson.ATT_T_NAME_03);
			}
			
			if (rp.I_Per_Manager == null) continue;
			TaPerPerson perMan =  (TaPerPerson) tabPer.get(rp.I_Per_Manager);
			if(perMan != null){
				rp.O_Per_Man = perMan.req(TaPerPerson.ATT_T_NAME_01) +  " " + perMan.req(TaPerPerson.ATT_T_NAME_02) +  " " + perMan.req(TaPerPerson.ATT_T_NAME_03);
			}
		}
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public static void doBuildReportResume(List<TaJobReport> lst) throws Exception{
		Set<Integer> 			ids 				= ToolSet.reqSetInt(lst, TaJobReport.ATT_I_ID);
		List<TaJobReportResume> lstReportResume 	= TaJobReportResume.DAO.reqList_In(TaJobReportResume.COL_I_JOB_REPORT, ids);
		
		Hashtable 				tabRep				= ToolDBEntity.reqTab(lst, TaJobReport.ATT_I_ID);
		
		for (TaJobReportResume rs : lstReportResume) {
			Integer 	rpId 	= rs.reqInt(rs, TaJobReportResume.COL_I_JOB_REPORT);
			TaJobReport rp 		= (TaJobReport) tabRep.get(rpId);
			rp.doAddReportResume(rs);
		}
	}
}