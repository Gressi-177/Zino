package com.hnv.db.job;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.hnv.db.EntityAbstract;
import com.hnv.db.EntityDAO;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.per.TaPerPerson;
import com.hnv.def.DefDBExt;

@Entity
@Table(name = DefDBExt.VI_JOB_REPORT_USER )
public class ViJobReportUser extends EntityAbstract<ViJobReportUser> {

	private static final long serialVersionUID = 1L;

	//---------------------------List of Column from DB-----------------------------
	private static final String	COL_I_ID                          		=	"I_ID"		; //uId03
	private static final String	COL_T_LOGIN		                        =	"T_Login"	; //
	private static final String	COL_T_EMAIL		                        =	"T_Email"	;
	private static final String	COL_T_VAL_02                            =	"T_Val_02"	; //Code Emp
			
	private static final String	COL_T_NAME_01                 			=	"T_Name_01"	; //PerN_Name01
	private static final String	COL_T_NAME_02                 			=	"T_Name_02"	; //PerN_Name02
	private static final String	COL_T_NAME_03                 			=	"T_Name_03"	; //PerN_Name03
	private static final String	COL_I_PERSON                  			=	"I_Person"	; //PerN
			
	private static final String	COL_I_HLD                              	=	"I_Hld";
	private static final String	COL_F_TOTAL                          	=	"F_Total";
	private static final String	COL_F_REALIZED                         	=	"F_Realized";
	private static final String	COL_F_AVAILABLE                         =	"F_Available";
	private static final String COL_T_CONFIG_01							= 	"T_Config_01";
	private static final String COL_T_CONFIG_02							= 	"T_Config_02";
			
	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                          		=	"I_ID"		;
	public static final String	ATT_T_LOGIN	                 			=	"T_Login"	;
	public static final String	ATT_T_EMAIL		                        =	"T_Email"	;
	public static final String	ATT_T_VAL_02		                    =	"T_Val_02"	;
	
	public static final String	ATT_T_NAME_01                 			=	"T_Name_01"	;
	public static final String	ATT_T_NAME_02                 			=	"T_Name_02"	;
	public static final String	ATT_T_NAME_03                 			=	"T_Name_03"	;
	public static final String	ATT_I_PERSON                  			=	"I_Person"	;
			
	public static final String	ATT_I_HLD                              	=	"I_Hld";
	public static final String	ATT_F_TOTAL                   			=	"F_Total";
	public static final String	ATT_F_REALIZED                          =	"F_Realized";
	public static final String	ATT_F_AVAILABLE              		  	=	"F_Available";
	public static final String	ATT_T_CONFIG_01              		  	=	"T_Config_01";
	public static final String	ATT_T_CONFIG_02              		  	=	"T_Config_02";
	
	public static final String 	ATT_O_HASLATESTREPORT					=   "O_HasLatestReport";
	//---------------------------Variables-----------------------------
	public static final Integer PR_CURRENT_REPORT_NOT_CREATED			= 0;
	public static final Integer PR_CURRENT_REPORT_CREATED				= 1;
	public static final Integer PR_CURRENT_REPORT_NOT_SUBMITTED			= 0;
	public static final Integer PR_CURRENT_REPORT_SUBMITTED				= 1;

	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static	final boolean				API_CACHE 	= false;
	private 	static 	final boolean[]				HISTORY		= {true, true, true}; //add, mod, del

	public		static 	final EntityDAO<ViJobReportUser> 	DAO;
	static{
		DAO = new EntityDAO<ViJobReportUser>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), ViJobReportUser.class,RIGHTS);
	}

	//-----------------------Class Attributs-------------------------
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;
			
	@Column(name=COL_T_LOGIN, nullable = false)
	private	String         T_Login;
			
	@Column(name=COL_T_EMAIL)
	private	String         	T_Email;
	
	@Column(name=COL_T_VAL_02)
	private	String         	T_Val_02;
			
	@Column(name=COL_T_NAME_01, nullable = false)
	private	String         	T_Name_01;
			
	@Column(name=COL_T_NAME_02, nullable = true)
	private	String         	T_Name_02;
	
	@Column(name=COL_T_NAME_03, nullable = true)
	private	String         	T_Name_03;
			
	@Column(name=COL_I_PERSON, nullable = false)
	private	Integer         I_Person;
			
	@Column(name=COL_I_HLD, nullable = true)
	private	Integer         I_Hld;
			
	@Column(name=COL_F_TOTAL, nullable = true)
	private	Float            F_Total;
			
	@Column(name=COL_F_REALIZED, nullable = true)
	private	Float         F_Realized;
			
	@Column(name=COL_F_AVAILABLE, nullable = true)
	private	Float         F_Available;
	
	@Column(name=COL_T_CONFIG_01, nullable = true)
	private	String        T_Config_01;
	
	@Column(name=COL_T_CONFIG_02, nullable = true)
	private	String        T_Config_02;
	
	//---------------------Transient-----------------------
	@Transient
	public	Integer       O_HasLatestReport;
	@Transient
	public 	Integer 	  O_HasReportSubmitted;

	//---------------------Constructeurs-----------------------
	private ViJobReportUser(){}

	public ViJobReportUser(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		//doInitDBFlag();
	}

	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;
	}

	public ViJobReportUser(String T_Login, String T_Email, String T_Val_02, String T_Name_01, String T_Name_02, String T_Name_03, Integer I_Person, 
			Integer I_Hld, Integer F_Total, Integer F_Realized, Integer F_Available, String T_Config_01, String T_Config_02) throws Exception {
		this.reqSetAttr(
				ATT_T_LOGIN	   		,T_Login,
				ATT_T_EMAIL		    ,T_Email,
				ATT_T_VAL_02		,T_Val_02,
				ATT_T_NAME_01       ,T_Name_01,
				ATT_T_NAME_02       ,T_Name_02,
				ATT_T_NAME_03       ,T_Name_03,
				ATT_I_PERSON        ,I_Person,
						
				ATT_I_HLD           ,I_Hld,
				ATT_F_TOTAL         ,F_Total,
				ATT_F_REALIZED      ,F_Realized,
				ATT_F_AVAILABLE     ,F_Available,
				ATT_T_CONFIG_01     ,T_Config_01,
				ATT_T_CONFIG_02     ,T_Config_02
				);
		//doInitDBFlag();
	}
	
	@Override
	public void doMergeWith(ViJobReportUser ent) {
		if (ent == this) return;
		this.T_Login              	= ent.T_Login;
		this.T_Email              	= ent.T_Email;
		this.T_Val_02              	= ent.T_Val_02;
		this.T_Name_01              = ent.T_Name_01;
		this.T_Name_02              = ent.T_Name_02;
		this.I_Person               = ent.I_Person;
		this.I_Hld             	 	= ent.I_Hld;
		this.F_Total              	= ent.F_Total;
		this.F_Realized             = ent.F_Realized;
		this.F_Available            = ent.F_Available;
		this.T_Config_01            = ent.T_Config_01;
		this.T_Config_02            = ent.T_Config_02;
		
		//---------------------Merge Transient Variables if exist-----------------------
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;

		ok = (I_ID == ((ViJobReportUser)o).I_ID);
		if (!ok) return ok;


		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}

	private static String SQL_WHERE_USER_MAN_ID 	= " AND 	autuser."  	+ TaAutUser.ATT_I_AUT_USER_03  	+ " = "; 	
	private static String SQL_WHERE_USER_ID 		= " AND 	autuser."  	+ TaAutUser.ATT_I_ID  			+ " = ";
	private static String SQL_WHERE_USER_STAT 		= " AND 	autuser."  	+ TaAutUser.ATT_I_STATUS  		+ " = ";
	
	public static  String SQL_WHERE_SEARCH = " AND (lower(autuser."	+ TaAutUser.COL_T_LOGIN_01 	+ ") like lower('%s') or "
												+ " lower(person."	+ TaPerPerson.ATT_T_NAME_01 + ") like lower('%s') or "
												+ " lower(person."	+ TaPerPerson.ATT_T_NAME_02 + ") like lower('%s') or "
												+ " lower(person."	+ TaPerPerson.ATT_T_NAME_03 + ") like lower('%s') )";
	
	private static String SQL_LST_ACTOR = 
			"SELECT "
					 + "autuser."	+TaAutUser.ATT_I_ID					+" as " + COL_I_ID 			+ ", "
					 + "autuser."	+TaAutUser.COL_T_LOGIN_01			+" as " + COL_T_LOGIN		+ ", "
					 + "autuser."	+TaAutUser.ATT_T_INFO_01			+" as " + COL_T_EMAIL		+ ", "
					 + "autuser."	+TaAutUser.ATT_T_INFO_02			+" as " + COL_T_VAL_02		+ ", "
					 + "person."	+TaPerPerson.ATT_I_ID				+" as " + COL_I_PERSON		+ ", "
					 + "person."	+TaPerPerson.ATT_T_NAME_01			+" as " + COL_T_NAME_01		+ ", "
					 + "person."	+TaPerPerson.ATT_T_NAME_02			+" as " + COL_T_NAME_02 	+ ", "
					 + "person."	+TaPerPerson.ATT_T_NAME_03			+" as " + COL_T_NAME_03 	+ ", "
					 + "hld."		+TaJobDayOffResume.ATT_I_ID			+" as " + COL_I_HLD			+ ", "
					 + "hld."		+TaJobDayOffResume.ATT_F_VAL_01		+" as " + COL_F_TOTAL		+ ", "
					 + "hld."		+TaJobDayOffResume.ATT_F_VAL_02		+" as " + COL_F_REALIZED	+ ", "
					 + "hld."		+TaJobDayOffResume.ATT_F_VAL_03		+" as " + COL_F_AVAILABLE	+ ", "
					 + "hld."		+TaJobDayOffResume.ATT_T_INFO_01	+" as " + COL_T_CONFIG_01   + ", "
					 + "hld."		+TaJobDayOffResume.ATT_T_INFO_02	+" as " + COL_T_CONFIG_02 
			+ " FROM " 
					 + DefDBExt.TA_AUT_USER + " autuser"
			
			+ " INNER JOIN " 
				+ DefDBExt.TA_PER_PERSON + " person"
							 		+ " ON 	autuser."  	+ TaAutUser.ATT_I_PER_PERSON  	+ " = person." 	+ TaPerPerson.ATT_I_ID		
							 		+ " %s " + " %s " + " %s "
							 		
			+ " LEFT JOIN "
					 + DefDBExt.TA_JOB_DAYOFF_RESUME + " hld"
							+ " ON hld."  + TaJobDayOffResume.ATT_I_AUT_USER + "= autuser."  + TaAutUser.ATT_I_ID ;
	
	private static String SQL_LST_ACTOR_COUNT = 
			"SELECT count(autuser."	+TaAutUser.ATT_I_ID					+") as nb "					
			+ " FROM " 
					 + DefDBExt.TA_AUT_USER + " autuser"
			+ " INNER JOIN " 
			 		 + DefDBExt.TA_PER_PERSON + " person"
					 		+ " ON 	autuser."  	+ TaAutUser.ATT_I_PER_PERSON  	+ " = person." 	+ TaPerPerson.ATT_I_ID		
					 		+ " %s " + " %s " ;
		
	
	private static List<ViJobReportUser> reqAllList() throws Exception{
		List<ViJobReportUser> listAll = ViJobReportUser.DAO.reqList(String.format(ViJobReportUser.SQL_LST_ACTOR,"","", ""));
		return listAll;
	}
	
	public static List<ViJobReportUser> reqListActorInfo(Integer uManagerId, String currentRp, Integer begin, Integer number, Set<String> searchKey, Integer stat) throws Exception {
		String condMan 	= SQL_WHERE_USER_MAN_ID + uManagerId;
		String condStat	= SQL_WHERE_USER_STAT   + stat;
		String condSrc  = reqRestriction(searchKey);
		String sql 		= String.format(ViJobReportUser.SQL_LST_ACTOR, condMan, condStat, condSrc) ;
		
		List<ViJobReportUser> listActorInfo = ViJobReportUser.DAO.reqList(begin, number, sql, null);
		if(listActorInfo.size() > 0){
			
		}
//		if(listActorInfo.size() > 0){
//			for (ViJobReportUser u : listActorInfo){
//				u.O_HasLatestReport 		= PR_CURRENT_REPORT_NOT_CREATED;
//				u.O_HasReportSubmitted		= PR_CURRENT_REPORT_NOT_SUBMITTED;
//				int uId = (int) u.req(ViJobReportUser.ATT_I_ID);
//				List<TaJobReport> lstRp = new ArrayList<>();
//				Criterion cri = Restrictions.and(Restrictions.eq(
//						TaJobReport.ATT_I_AUT_USER_03, uId), 
//						Restrictions.eq(TaJobReport.ATT_T_CODE, currentRp)
//						);
//				lstRp = TaJobReport.DAO.reqList(cri);
//				if(lstRp.size() > 0){
//					u.O_HasLatestReport = PR_CURRENT_REPORT_CREATED;
//					int stat = (int) lstRp.get(0).req(TaJobReport.ATT_I_STATUS);
//					if(stat == TaJobReport.STAT_PENDING || 	stat == TaJobReport.STAT_VALIDATE){
//						u.O_HasReportSubmitted = PR_CURRENT_REPORT_SUBMITTED;
//					}
//				}
//			};
//		}
		return listActorInfo;
	}
	
	private static String reqRestriction(Set<String> searchKey) {			
		String restr = "";
		String orT	 =  "";	
		for (String s: searchKey){			
			restr = restr + orT + String.format(SQL_WHERE_SEARCH, s, s, s, s);
			orT	  = " AND ";	
		}
		if (restr.length()==0) return "";
		
		return restr;
	}
	
	public static List<ViJobReportUser> reqListActorInfo(Integer uManagerId) throws Exception {
		String condMan 	= SQL_WHERE_USER_MAN_ID + uManagerId;
		String sql 		= String.format(ViJobReportUser.SQL_LST_ACTOR, condMan, "", "") ;
		
		List<ViJobReportUser> listActorInfo = ViJobReportUser.DAO.reqList(sql, null);
		return listActorInfo;
	}

	public static ViJobReportUser reqActorInfo(Integer userId, String codeRp) throws Exception{
		String condUser 	= SQL_WHERE_USER_ID + userId;
		String sql 			= String.format(ViJobReportUser.SQL_LST_ACTOR, condUser, "", "") ;
		List<ViJobReportUser> listActorInfo = ViJobReportUser.DAO.reqList(sql);

		ViJobReportUser actorInfo = null;
		if(listActorInfo.size() > 0){
			actorInfo = listActorInfo.get(0);
			actorInfo.O_HasLatestReport = PR_CURRENT_REPORT_NOT_CREATED;
			int uId = (int) actorInfo.req(ViJobReportUser.ATT_I_ID);
			List<TaJobReport> lstRp = new ArrayList<>();
			Criterion cri = Restrictions.and(Restrictions.eq(TaJobReport.ATT_I_AUT_USER_03, uId), Restrictions.eq(TaJobReport.ATT_T_CODE_01, codeRp));
			lstRp = TaJobReport.DAO.reqList(cri);
			if(lstRp.size() > 0){
				actorInfo.O_HasLatestReport = PR_CURRENT_REPORT_CREATED;
			}
		}
		return actorInfo;
	}
	
	public static int reqCountLstUser(Set<String> searchKey, Integer uManId, Integer userId, Integer stat) throws Exception{
		String cond 	="";
		if (uManId!=null) {
			cond = cond +  SQL_WHERE_USER_MAN_ID + uManId;
		}
		if (userId!=null) {
			cond = cond +  SQL_WHERE_USER_ID + userId;
		}
		
		if (stat!=null) {
			cond = cond +  SQL_WHERE_USER_STAT + stat;
		}
		
		String condSrc  = reqRestriction(searchKey);
		
		String sql 			= String.format(ViJobReportUser.SQL_LST_ACTOR_COUNT, cond, condSrc, "") ;
		
		return ViJobReportUser.DAO.reqCount(sql).intValue();
	}
	
	
	public static ViJobReportUser reqUserInfoForJobOff(Integer uId03) throws Exception{
		String condUser 	= SQL_WHERE_USER_ID + uId03;
		String sql 			= String.format(ViJobReportUser.SQL_LST_ACTOR, condUser, "", "") ;
		List<ViJobReportUser> listActorInfo = ViJobReportUser.DAO.reqList(sql);
		ViJobReportUser actorInfo = null;
		if(listActorInfo.size() > 0){
			for (ViJobReportUser u : listActorInfo){
				if((int) u.req(ViJobReportUser.ATT_I_ID) == uId03){
					return u;
				}
			}
		}
		return actorInfo;
	}
}