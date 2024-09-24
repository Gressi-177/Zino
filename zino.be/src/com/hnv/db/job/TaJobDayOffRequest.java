package com.hnv.db.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.hnv.db.EntityAbstract;
import com.hnv.db.EntityDAO;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.per.TaPerPerson;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.def.DefDBExt;		

/**
* TaJobDayoffRequest by H&V SAS
*/
@Entity
@Table(name = DefDBExt.TA_JOB_DAYOFF_REQUEST )
public class TaJobDayOffRequest extends EntityAbstract<TaJobDayOffRequest> {

	private static final long serialVersionUID = 1L;

	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                              =	"I_ID";
	public static final String	COL_T_CODE_01                         =	"T_Code_01";//T_Ref
	public static final String	COL_T_CODE_02                         =	"T_Code_02";
	
	public static final String	COL_D_DATE_01                         =	"D_Date_01";
	public static final String	COL_D_DATE_02                         =	"D_Date_02";
	public static final String	COL_D_DATE_03                         =	"D_Date_03";
	public static final String	COL_D_DATE_04                         =	"D_Date_04";
	
	public static final String	COL_I_STATUS                          =	"I_Status";
	public static final String	COL_I_AUT_USER_01                     =	"I_Aut_User_01";
	public static final String	COL_I_AUT_USER_02                     =	"I_Aut_User_02";
	public static final String	COL_I_AUT_USER_03                     =	"I_Aut_User_03";
	
	public static final String	COL_I_PER_MANAGER                     =	"I_Per_Manager";
	
	public static final String	COL_T_INFO_01                         =	"T_Info_01";//T_Comment
	public static final String	COL_T_INFO_02                         =	"T_Info_02";//T_Reason
	
	public static final String	COL_F_VAL_01                          =	"F_Val_01";
	public static final String	COL_F_VAL_02                          =	"F_Val_02";
	public static final String	COL_F_VAL_03                          =	"F_Val_03";

	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              =	"I_ID";
	public static final String	ATT_T_CODE_01                         =	"T_Code_01";//T_Ref
	public static final String	ATT_T_CODE_02                         =	"T_Code_02";
	
	public static final String	ATT_D_DATE_01                         =	"D_Date_01";
	public static final String	ATT_D_DATE_02                         =	"D_Date_02";
	public static final String	ATT_D_DATE_03                         =	"D_Date_03";
	public static final String	ATT_D_DATE_04                         =	"D_Date_04";
	
	public static final String	ATT_I_STATUS                          =	"I_Status";
	public static final String	ATT_I_AUT_USER_01                     =	"I_Aut_User_01";
	public static final String	ATT_I_AUT_USER_02                     =	"I_Aut_User_02";
	public static final String	ATT_I_AUT_USER_03                     =	"I_Aut_User_03";
	
	public static final String	ATT_I_PER_MANAGER                     =	"I_Per_Manager";
	
	public static final String	ATT_T_INFO_01                         =	"T_Info_01";//T_Comment
	public static final String	ATT_T_INFO_02                         =	"T_Info_02";//T_Reason
	
	public static final String	ATT_F_VAL_01                          =	"F_Val_01";
	public static final String	ATT_F_VAL_02                          =	"F_Val_02";
	public static final String	ATT_F_VAL_03                          =	"F_Val_03";

	public static final String	ATT_O_PER_CREATE              		  =	"O_Per_Create";    	//uId01
	public static final String	ATT_O_PER_VALIDATE             		  =	"O_Per_Validate";  	//uId02
	public static final String	ATT_O_PER_OWNER             		  =	"O_Per_Owner";  	//uId02
	public static final String	ATT_O_PER_MAN             		  	  =	"O_Per_Man";  		//manId
	
	public static final String	ATT_O_EMAIL              		  	  =	"O_Email";          //User Holiday Info
	public static final String  ATT_O_LOGIN						  	  = "O_Login";			//Documents
	
	public static final String	ATT_O_HLD              		  		  =	"O_Hld";            //User Holiday Info
	public static final String  ATT_O_DOCUMENTS						  = "O_Files";			//Documents
	
	//---------------------------Variables-----------------------------
	public static final Integer PR_REQUEST_STAT_DRAFT					 	  = 0;
	public static final Integer PR_REQUEST_STAT_PENDING						  = 1;
	public static final Integer PR_REQUEST_STAT_VALIDATE					  = 2;
	public static final Integer PR_REQUEST_STAT_DENIED 						  = 3;

	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static	final boolean				API_CACHE 	= false;
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<TaJobDayOffRequest> 	DAO;
	static{
		DAO = new EntityDAO<TaJobDayOffRequest>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), TaJobDayOffRequest.class,RIGHTS);
	}

	//-----------------------Class Attributs-------------------------
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;
         
	@Column(name=COL_T_CODE_01, nullable = true)
	private	String          T_Code_01;

	@Column(name=COL_T_CODE_02, nullable = true)
	private	String          T_Code_02;
       
	@Column(name=COL_T_INFO_01, nullable = true)
	private	String          T_Info_01;

	@Column(name=COL_T_INFO_02, nullable = true)
	private	String          T_Info_02;
        
	@Column(name=COL_D_DATE_01, nullable = true)
	private	Date            D_Date_01;
    
	@Column(name=COL_D_DATE_02, nullable = true)
	private	Date            D_Date_02;
    
	@Column(name=COL_D_DATE_03, nullable = true)
	private	Date            D_Date_03;
    
	@Column(name=COL_D_DATE_04, nullable = true)
	private	Date            D_Date_04;
    
	@Column(name=COL_I_STATUS, nullable = true)
	private	Integer         I_Status;
     
	@Column(name=COL_I_AUT_USER_01, nullable = true)
	private	Integer         I_Aut_User_01;

	@Column(name=COL_I_AUT_USER_02, nullable = true)
	private	Integer         I_Aut_User_02;

	@Column(name=COL_I_AUT_USER_03, nullable = true)
	private	Integer         I_Aut_User_03;

	@Column(name=COL_I_PER_MANAGER, nullable = true)
	private	Integer         I_Per_Manager;

	@Column(name=COL_F_VAL_01, nullable = true)
	private	Double          F_Val_01;
     
	@Column(name=COL_F_VAL_02, nullable = true)
	private	Double          F_Val_02;
	
	@Column(name=COL_F_VAL_03, nullable = true)
	private	Double          F_Val_03;
    
	//-----------------------Transient Variables-------------------------
	@Transient
	private String							O_Per_Create;
	@Transient
	private String							O_Per_Validate;
	@Transient
	private String							O_Per_Man;
	@Transient
	private String							O_Per_Owner;
	@Transient
	private TaJobDayOffResume				O_Hld;
	@Transient
	private List<TaTpyDocument> 			O_Files;
	@Transient
	private String							O_Email;
	@Transient
	private String							O_Login;

	//---------------------Constructeurs-----------------------
	private TaJobDayOffRequest(){}

	public TaJobDayOffRequest(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		//doInitDBFlag();
	}
	
	public TaJobDayOffRequest(String T_Ref, Date D_Date_01, Date D_Date_03, Date D_Date_04, Integer I_Aut_User_01, Integer I_Aut_User_03, String I_Per_Manager, String T_Reason) throws Exception {
		this.reqSetAttr(
			ATT_T_CODE_01    , T_Ref,
			ATT_D_DATE_01    , D_Date_01,
			ATT_D_DATE_03    , D_Date_03,
			ATT_D_DATE_04    , D_Date_04,
			ATT_I_AUT_USER_01, I_Aut_User_01,
			ATT_I_AUT_USER_03, I_Aut_User_03,
			ATT_I_PER_MANAGER, I_Per_Manager,
			ATT_T_INFO_02    , T_Reason
		);
		//doInitDBFlag();
	}
	public TaJobDayOffRequest(String T_Ref, Date D_Date_01, Date D_Date_02, Date D_Date_03, Date D_Date_04, Integer I_Status, Integer I_Aut_User_01, Integer I_Aut_User_02, Integer I_Aut_User_03, String I_Per_Manager, String T_Comment, String T_Reason, Double F_Val_01, Double F_Val_02, Double F_Val_03) throws Exception {
		this.reqSetAttr(
			ATT_T_CODE_01              , T_Ref,
			ATT_D_DATE_01              , D_Date_01,
			ATT_D_DATE_02              , D_Date_02,
			ATT_D_DATE_03              , D_Date_03,
			ATT_D_DATE_04              , D_Date_04,
			ATT_I_STATUS               , I_Status,
			ATT_I_AUT_USER_01          , I_Aut_User_01,
			ATT_I_AUT_USER_02          , I_Aut_User_02,
			ATT_I_AUT_USER_03          , I_Aut_User_03,
			ATT_I_PER_MANAGER          , I_Per_Manager,
			ATT_T_INFO_01              , T_Comment,
			ATT_T_INFO_02               , T_Reason,
			ATT_F_VAL_01               , F_Val_01,
			ATT_F_VAL_02               , F_Val_02,
			ATT_F_VAL_03               , F_Val_03
		);
		//doInitDBFlag();
	}
	
	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}

	@Override
	public void doMergeWith(TaJobDayOffRequest ent) {
		if (ent == this) return;
		this.T_Code_01              = ent.T_Code_01;
		this.D_Date_01              = ent.D_Date_01;
		this.D_Date_02              = ent.D_Date_02;
		this.D_Date_03              = ent.D_Date_03;
		this.D_Date_04              = ent.D_Date_04;
		this.I_Status               = ent.I_Status;
		this.I_Aut_User_01          = ent.I_Aut_User_01;
		this.I_Aut_User_02          = ent.I_Aut_User_02;
		this.I_Aut_User_03          = ent.I_Aut_User_03;
		this.I_Per_Manager          = ent.I_Per_Manager;
		this.T_Info_01              = ent.T_Info_01;
		this.T_Info_02               = ent.T_Info_02;
		this.F_Val_01               = ent.F_Val_01;
		this.F_Val_02               = ent.F_Val_02;
		this.F_Val_03               = ent.F_Val_03;

		//---------------------Merge Transient Variables if exist-----------------------
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;
		
		ok = (I_ID == ((TaJobDayOffRequest)o).I_ID);
		if (!ok) return ok;

				
		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}

	public void doBuildUserDayoffResume(boolean forced) throws Exception{
		if (!forced && O_Hld !=null) return;
		List<TaJobDayOffResume> dayOffInfo = new ArrayList<TaJobDayOffResume>();
		Criterion cri 	= Restrictions.eq(TaJobDayOffResume.ATT_I_AUT_USER, I_Aut_User_03);
		dayOffInfo 	= TaJobDayOffResume.DAO.reqList(cri);
		if(dayOffInfo.size() > 0){
			this.O_Hld = dayOffInfo.get(0);
		} else {
			TaJobDayOffResume res = new TaJobDayOffResume(I_Aut_User_03, null, null);
			this.O_Hld = res;
		}
	}
	
	public void doBuildPerCreateAndOwner(boolean forced) throws Exception{
		if (!forced && O_Per_Create!=null) return;
		TaAutUser aut1 = TaAutUser.DAO.reqEntityByRef(I_Aut_User_01);
		TaPerPerson per1 = TaPerPerson.DAO.reqEntityByRef((int) aut1.req(TaAutUser.ATT_I_PER_PERSON));
		if(per1 != null){
			this.O_Per_Create = per1.req(TaPerPerson.ATT_T_NAME_01) +  " " + per1.req(TaPerPerson.ATT_T_NAME_02) + " " + per1.req(TaPerPerson.ATT_T_NAME_03);
		}
		
		if (!forced && O_Per_Owner!=null) return;
		TaAutUser aut3 = TaAutUser.DAO.reqEntityByRef(I_Aut_User_03);
		TaPerPerson per3 = TaPerPerson.DAO.reqEntityByRef((int) aut3.req(TaAutUser.ATT_I_PER_PERSON));
		if(per3 != null){
			this.O_Per_Owner = per3.req(TaPerPerson.ATT_T_NAME_01) +  " " + per3.req(TaPerPerson.ATT_T_NAME_02) +  " " + per3.req(TaPerPerson.ATT_T_NAME_03);
			this.O_Email     = (String) aut3.req(TaAutUser.ATT_T_INFO_01);
			this.O_Login     = (String) aut3.req(TaAutUser.ATT_T_LOGIN_01);
		}
		
		if (O_Per_Man !=null) return;
		if (I_Per_Manager == null) return;
		Integer supervisor = (Integer) aut1.req(TaAutUser.ATT_I_AUT_USER_03);
		TaAutUser autMan = TaAutUser.DAO.reqEntityByRef(supervisor);
		TaPerPerson perMan = TaPerPerson.DAO.reqEntityByRef((int) autMan.req(TaAutUser.ATT_I_PER_PERSON));
		if(perMan != null){
			this.O_Per_Man = perMan.req(TaPerPerson.ATT_T_NAME_01) +  " " + perMan.req(TaPerPerson.ATT_T_NAME_02) +  " " + perMan.req(TaPerPerson.ATT_T_NAME_03);
		}
	}
	
	public void doBuildPerValidate(boolean forced) throws Exception{
		if (!forced && O_Per_Validate!=null) return;
		if (I_Aut_User_02 == null || I_Status == PR_REQUEST_STAT_PENDING || I_Status == PR_REQUEST_STAT_DRAFT) return;
		TaAutUser aut2 = TaAutUser.DAO.reqEntityByRef(I_Aut_User_02);
		if(aut2 == null){
			return;
		}
		TaPerPerson per2 = TaPerPerson.DAO.reqEntityByRef((int) aut2.req(TaAutUser.ATT_I_PER_PERSON));
		if(per2 != null){
			this.O_Per_Validate = per2.req(TaPerPerson.ATT_T_NAME_01) +  " " + per2.req(TaPerPerson.ATT_T_NAME_02) +  " " + per2.req(TaPerPerson.ATT_T_NAME_03);
		}
	}
	
	public void doBuildFiles(boolean forced) throws Exception {
		if (!forced && O_Files!=null) return;
		this.O_Files = TaTpyDocument.DAO.reqList(
				Restrictions.eq(TaTpyDocument.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_JOB_DAYOFF_REQUEST),
				Restrictions.eq(TaTpyDocument.ATT_I_ENTITY_ID	, this.I_ID));			
	}
}
