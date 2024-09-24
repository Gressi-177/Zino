package com.hnv.db.sor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import org.hibernate.criterion.Restrictions;

import com.hnv.api.main.Hnv_CfgHibernate;
import com.hnv.db.EntityAbstract;
import com.hnv.db.EntityDAO;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.per.TaPerPerson;
import com.hnv.def.DefDBExt;

/**
 * TaSorOrder by H&V SAS
 */
@Entity
@Table(name = DefDBExt.TA_SOR_DEAL )
public class TaSorDeal extends EntityAbstract<TaSorDeal> {
	public static final	int		TYPE_01_DEAL_TO_MAT 			= 1;
	public static final	int		TYPE_01_DEAL_TO_ORDER 			= 2;
	public static final	int		TYPE_01_DEAL_TO_TRANSPORT 		= 3;
	
	public static final	int		TYPE_02_DEAL_PUBLIC 			= 1; //---ap dụng nhiều lần và không dùng chung
	public static final	int		TYPE_02_DEAL_PRIVATE			= 2; //---ap dụng chỉ cho khách hàng có id tương thích
	
	public static final int		STAT_INACTIVE	 				= 0;
	public static final int		STAT_ACTIVE 					= 1;
	
	private static final long serialVersionUID 					= 1L;
	
	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                              =	"I_ID";
	public static final String	COL_T_CODE_01                         =	"T_Code_01";
	public static final String	COL_T_CODE_02                         =	"T_Code_02";
	public static final String	COL_I_STATUS                          =	"I_Status";
	
	public static final String	COL_I_TYPE_01                         =	"I_Type_01";
	public static final String	COL_I_TYPE_02                         =	"I_Type_02";
	public static final String	COL_I_TYPE_03                         =	"I_Type_03";
	
	public static final String	COL_F_VAL_00                          =	"F_Val_00"; //---giá trị áp dụng
	public static final String	COL_F_VAL_01                          =	"F_Val_01"; //---giá trị tối thiểu phải đạt
	public static final String	COL_F_VAL_02                          =	"F_Val_02"; //--ID khách hàng liên quan, hoặc sản phẩm liên quan
	
	public static final String	COL_T_INFO_01                     	  =	"T_Info_01";
	public static final String	COL_T_INFO_02                     	  =	"T_Info_02";
	
	public static final String	COL_D_DATE_01                         =	"D_Date_01";
	public static final String	COL_D_DATE_02                         =	"D_Date_02";
	public static final String	COL_D_DATE_03                         =	"D_Date_03";
	public static final String	COL_D_DATE_04                         =	"D_Date_04";
	
	public static final String	COL_I_AUT_USER_01                     =	"I_Aut_User_01";
	public static final String	COL_I_AUT_USER_02                     =	"I_Aut_User_02";
	
	public static final String	COL_I_PER_MANAGER                     =	"I_Per_Manager";

	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              =	"I_ID";
	
	public static final String	ATT_T_CODE_01                         =	"T_Code_01";
	public static final String	ATT_T_CODE_02                         =	"T_Code_02";
	public static final String	ATT_I_STATUS                          =	"I_Status";
	
	public static final String	ATT_I_TYPE_01                         =	"I_Type_01";
	public static final String	ATT_I_TYPE_02                         =	"I_Type_02";
	public static final String	ATT_I_TYPE_03                         =	"I_Type_03";
	
	public static final String	ATT_F_VAL_00                          =	"F_Val_00";
	public static final String	ATT_F_VAL_01                          =	"F_Val_01";
	public static final String	ATT_F_VAL_02                          =	"F_Val_02";
	
	public static final String	ATT_T_INFO_01                     	  =	"T_Info_01";
	public static final String	ATT_T_INFO_02                     	  =	"T_Info_02";
	
	public static final String	ATT_D_DATE_01                         =	"D_Date_01";
	public static final String	ATT_D_DATE_02                         =	"D_Date_02";
	public static final String	ATT_D_DATE_03                         =	"D_Date_03";
	public static final String	ATT_D_DATE_04                         =	"D_Date_04";
	
	public static final String	ATT_I_AUT_USER_01                     =	"I_Aut_User_01";
	public static final String	ATT_I_AUT_USER_02                     =	"I_Aut_User_02";
	
	public static final String	ATT_I_PER_MANAGER                     =	"I_Per_Manager";
	
	public static final String	ATT_O_AUT_USER_01                     =	"O_Aut_User_01";
	public static final String	ATT_O_AUT_USER_02                     =	"O_Aut_User_02";
	
	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<TaSorDeal> 	DAO;
	static{
		DAO = new EntityDAO<TaSorDeal>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), TaSorDeal.class,RIGHTS, HISTORY, DefDBExt.TA_SOR_DEAL, DefDBExt.ID_TA_SOR_DEAL);

	}

	//-----------------------Class Attributs-------------------------
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;
         
	@Column(name=COL_I_STATUS, nullable = true)
	private	Integer         I_Status;
     
	@Column(name=COL_T_CODE_01, nullable = true)
	private	String          T_Code_01;
    
	@Column(name=COL_T_CODE_02, nullable = true)
	private	String          T_Code_02;
    
	@Column(name=COL_I_TYPE_01, nullable = true)
	private	Integer         I_Type_01;
    
	@Column(name=COL_I_TYPE_02, nullable = true)
	private	Integer         I_Type_02;
    
	@Column(name=COL_I_TYPE_03, nullable = true)
	private	Integer         I_Type_03;
    
	@Column(name=COL_F_VAL_00, nullable = true)
	private	Double          F_Val_00;
     
	@Column(name=COL_F_VAL_01, nullable = true)
	private	Double          F_Val_01;
     
	@Column(name=COL_F_VAL_02, nullable = true)
	private	Double          F_Val_02;
     
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
    
	@Column(name=COL_I_AUT_USER_01, nullable = true)
	private	Integer         I_Aut_User_01;

	@Column(name=COL_I_AUT_USER_02, nullable = true)
	private	Integer         I_Aut_User_02;

	@Column(name=COL_I_PER_MANAGER, nullable = true)
	private	Integer         I_Per_Manager;

	
	//-----------------------Transient Variables-------------------------
	@Transient
	private TaAutUser O_Aut_User_01;
	@Transient
	private TaAutUser O_Aut_User_02;
	@Transient
	private TaPerPerson O_Per_Man;
	
	//---------------------Constructeurs----------------------
	public TaSorDeal(){}
	
	public TaSorDeal(
		// WEB SALE
			int I_Type_01, int I_Per_Manager, String T_Code, String T_Number, Date D_Date_01, int I_Status,
			Double F_Val_05, Double F_Val_06, Double F_Val_07, Double F_Val_08, Double F_Val_09,
			Double F_Val_10, String T_Currency, String T_Option,
			int I_Per_Client, String  T_Title,
			String T_Address_01, String T_Comment_01, String T_Comment_02) 
	throws Exception{
	}

	public TaSorDeal(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		//doInitDBFlag();
	}
	public TaSorDeal(TaSorDeal ord) throws Exception {
		this.doMergeWith(ord);
		//doInitDBFlag();
	}

	


	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}

	@Override
	public void doMergeWith(TaSorDeal ent) {
		if (ent == this) return;
		this.I_Status               = ent.I_Status;
		this.T_Code_01              = ent.T_Code_01;
		this.T_Code_02              = ent.T_Code_02;
		this.I_Type_01              = ent.I_Type_01;
		this.I_Type_02              = ent.I_Type_02;
		this.I_Type_03              = ent.I_Type_03;
		this.F_Val_00               = ent.F_Val_00;
		this.F_Val_01               = ent.F_Val_01;
		this.F_Val_02               = ent.F_Val_02;
		this.T_Info_01              = ent.T_Info_01;
		this.T_Info_02              = ent.T_Info_02;
		this.D_Date_01              = ent.D_Date_01;
		this.D_Date_02              = ent.D_Date_02;
		this.D_Date_03              = ent.D_Date_03;
		this.D_Date_04              = ent.D_Date_04;
		this.I_Aut_User_01          = ent.I_Aut_User_01;
		this.I_Aut_User_02          = ent.I_Aut_User_02;
		this.I_Per_Manager          = ent.I_Per_Manager;

	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;

		ok = (I_ID == ((TaSorDeal)o).I_ID);
		if (!ok) return ok;


		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}


	public void doBuildUsers() throws Exception{
		if (this.I_Aut_User_01!=null)  
			this.O_Aut_User_01 = TaAutUser.reqUserInfo(this.I_Aut_User_01);
		if (this.I_Aut_User_02!=null)
			this.O_Aut_User_02 = TaAutUser.reqUserInfo(this.I_Aut_User_02);
	}
	
	public  void doBuildManagerName() throws Exception{
		if(this.I_Per_Manager != null){	
			this.O_Per_Man = TaPerPerson.DAO.reqEntityByRef(I_Per_Manager);
		}
	}
	
	
	private static String reqRestriction(Set<String> searchKey) {	
		if (searchKey==null) return null;
		String restr = "";
		String orT	 =  "";	
		for (String s: searchKey){		
			if (s.equals("%")) continue;
			restr = restr + orT + String.format(SQL_WHERE_SEARCH, s, s);
			orT	  = " AND ";	
		}
		if (restr.length()==0) return null;
		else restr = "( " + restr + " )";
		return restr;
	}
	public static final String SQL_WHERE_SEARCH 	= "( lower(s."	+ TaSorDeal.COL_T_CODE_01 + ") like lower('%s') or lower(s."	+ TaSorDeal.COL_T_INFO_01 + ") like lower('%s'))";
	
	public static final String SQL_REQ_LIST_DYN_SELECT = "SELECT *";
	
	static final String SQL_REQ_LIST_DYN_COUNT 		= "SELECT count(DISTINCT(s."+ TaSorDeal.COL_I_ID		+"))";

	static final String SQL_REQ_WHERE 				=  " WHERE true ";
	
	static final String SQL_REQ_WHERE_PER_MAN		=  " AND s."	+ TaSorOrder.ATT_I_PER_MANAGER	+ " = "	;  
	static final String SQL_REQ_WHERE_TYPE_01 		=  " AND s."	+ TaSorOrder.COL_I_TYPE_01		+ " = "	;
	static final String SQL_REQ_WHERE_TYPE_02 		=  " AND s."	+ TaSorOrder.COL_I_TYPE_02		+ " = "	;    
    static final String SQL_REQ_WHERE_STATUS 		=  " AND s."	+ TaSorOrder.COL_I_STATUS		+ " = " ;
    
//    static final String SQL_REQ_WHERE_DT02_FROM		=  " AND s."	+ TaSorOrder.COL_D_DATE_02 + " >= ";
//    static final String SQL_REQ_WHERE_DT02_TO		=  " AND s."	+ TaSorOrder.COL_D_DATE_02 + " <= ";
//    static final String SQL_REQ_WHERE_DT03_FROM		=  " AND s."	+ TaSorOrder.COL_D_DATE_03 + " >= ";
//    static final String SQL_REQ_WHERE_DT03_TO		=  " AND s."	+ TaSorOrder.COL_D_DATE_03 + " <= ";
//    static final String SQL_REQ_WHERE_CODE			=  " AND (lower(s."  + TaSorOrder.COL_T_CODE_01 + ") like lower('%";
    
    static final String SQL_REQ_LIST_DYN_FROM		=  " FROM "		+ DefDBExt.TA_SOR_DEAL			+ " s"; 
    		
    public static final String SQL_ORDER			= " ORDER BY %s %s";
	public static SimpleDateFormat changeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static List<TaSorDeal> reqList(
			int begin, int end,
			Integer idMan,
			Integer type01, Integer type02, Integer status, 
			Set<String> searchKey, 
			String orderCol, String orderDir
			) throws Exception {
		
		List<TaSorDeal> result = null;
		String sql = SQL_REQ_LIST_DYN_SELECT;	
		
		sql += SQL_REQ_LIST_DYN_FROM + SQL_REQ_WHERE;

		if(idMan != null)
			sql += SQL_REQ_WHERE_PER_MAN 	+ idMan;
		
		if(type01 != null)
			sql += SQL_REQ_WHERE_TYPE_01 	+ type01;
		
		if(type02 != null)
			sql += SQL_REQ_WHERE_TYPE_02 	+ type02;
		
		if(status != null)
			sql += SQL_REQ_WHERE_STATUS 	+ status;	
	
		String 					restriction 			= reqRestriction(searchKey);
		if(restriction != null) {
			sql += " AND " + restriction;
		}
	
		sql += String.format(SQL_ORDER, orderCol, orderDir);
		result = TaSorDeal.DAO.reqList(begin, end, sql);		
		
		return result;
	}
	
	public static int reqListCount(
			Integer idMan,
			Integer type01, Integer type02, Integer status, 
			Set<String> searchKey ) throws Exception {
		String sql = SQL_REQ_LIST_DYN_COUNT;
		
		sql += SQL_REQ_LIST_DYN_FROM + SQL_REQ_WHERE;
		
		if(idMan != null)
			sql += SQL_REQ_WHERE_PER_MAN 	+ idMan;
		if(type01 != null)
			sql += SQL_REQ_WHERE_TYPE_01 	+ type01;
		if(type02 != null)
			sql += SQL_REQ_WHERE_TYPE_02 	+ type02;
		if(status != null)
			sql += SQL_REQ_WHERE_STATUS 	+ status;
		
		String 					restriction 			= reqRestriction(searchKey);
		if(restriction != null) {
			sql += " AND " + restriction;
		}

		int count = TaSorDeal.DAO.reqCount(sql).intValue();		
		return count;
	}

	
	public static TaSorDeal reqDealByCode(String code) throws Exception{
		if (code==null) return null;
		code = code.toUpperCase();
		Date now = new Date();
		
		List<TaSorDeal>		lst 	= TaSorDeal.DAO.reqList(Restrictions.ilike(TaSorDeal.ATT_T_CODE_01, code), 
															Restrictions.eq(TaSorDeal.ATT_I_STATUS, TaSorDeal.STAT_ACTIVE),
															Restrictions.le(TaSorDeal.ATT_D_DATE_03, now),
															Restrictions.ge(TaSorDeal.ATT_D_DATE_04, now)
															);
		if (lst==null || lst.size()==0)
							lst 	= TaSorDeal.DAO.reqList(Restrictions.ilike(TaSorDeal.ATT_T_CODE_02, code), 
															Restrictions.eq(TaSorDeal.ATT_I_STATUS, TaSorDeal.STAT_ACTIVE),
															Restrictions.le(TaSorDeal.ATT_D_DATE_03, now),
															Restrictions.ge(TaSorDeal.ATT_D_DATE_04, now));
		
		if (lst==null || lst.size()==0) return null;
				
		return lst.get(0);
	}
}
