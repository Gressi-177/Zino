package com.hnv.db.sor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hnv.db.aut.vi.ViAutUserDyn;
import com.hnv.db.mat.TaMatStockIo;
import com.hnv.db.mat.TaMatWarehouse;
import com.hnv.db.per.TaPerPerson;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.db.tpy.TaTpyInformation;
import com.hnv.def.DefDBExt;

/**
 * TaSorOrder by H&V SAS
 */
@Entity
@Table(name = DefDBExt.TA_SOR_ORDER )
public class TaSorOrder extends EntityAbstract<TaSorOrder> {
	private static final int  ENT_TYP			= DefDBExt.ID_TA_SOR_ORDER;
			
//	public static final	int		TYPE_01_ORDER 					= 2;
//	public static final	int		TYPE_01_ORDER_TRANSFER 			= 69;
//	public static final	int		TYPE_01_ORDER_CONTRACT			= 10;
//	
//	public static final	int		TYPE_02_ORDER_IN 				= 1;
//	public static final	int		TYPE_02_ORDER_OUT 				= 2;
//	public static final	int		TYPE_02_ORDER_TRANSFER 			= 11;
//	
//	public static final	int		TYPE_03_ORDER_IN_01_NEW 		= 1;
//	public static final	int		TYPE_03_ORDER_IN_02_TRANSFERT	= 2;
//	public static final	int		TYPE_03_ORDER_IN_03_RETURN 		= 3;
//	
//	public static final	int		TYPE_03_ORDER_OUT_01_SELL 		= 1;
//	public static final	int		TYPE_03_ORDER_OUT_02_TRANSFERT 	= 2;
//	public static final	int		TYPE_03_ORDER_OUT_03_REVIEW 	= 3;
//	public static final	int		TYPE_03_ORDER_OUT_04_DESTROY 	= 4;
	
	public static final int		STAT_NEW	 					= 0;
	public static final int		STAT_SUBMITED 					= 1;
	public static final int		STAT_WAITING					= 2;
	public static final int		STAT_REFUSE						= 3;
	public static final int		STAT_ACCEPT	 					= 4;
	
	public static final int		STAT_VALIDATION					= 10;
	public static final int		STAT_STOCK_ACCEPT				= 11;
	public static final int		STAT_STOCK_COMPLETED			= 12;
	public static final int 	STAT_STOCK_UNCOMPLETED 			= 13; 
	
	public static final int		STAT_COMPLETED 					= 20;
	public static final int		STAT_CANCEL 					= 30;
	public static final int		STAT_EXPIRED 					= 31;

	
	private static final long 	serialVersionUID 				= 1L;
	
	public static final int		STAT_WAITING_SHIPPING			= 0;
	public static final int		STAT_INCOURS_SHIPPING	 		= 1;
	public static final int		STAT_REFUSE_SHIPPING			= 2;
	public static final int		STAT_CANCEL_SHIPPING 			= 3;
	public static final int		STAT_COMPLETED_SHIPPING 		= 4;
	
	public static final int 	SOR_ORDER_ADD      				= 1;
    public static final int 	SOR_ORDER_MOD      				= 2;
    
    public static final int 	TYPE_01_IN_STK_BUY             	= 101;
    public static final int 	TYPE_01_IN_STK_TRANSFER        	= 102;
    public static final int 	TYPE_01_IN_STK_BALANCE_IN      	= 103; //nhập thêm/trả hàng,căn bằng 
    public static final int 	TYPE_01_IN_STK_BALANCE_OTHER   	= 105; //khác

    public static final int 	TYPE_01_OU_STK_SELL            	= 201; // bán hàng
    public static final int 	TYPE_01_OU_STK_TRANSFER        	= 202; // xuất chuyển kho
    public static final int 	TYPE_01_OU_STK_BALANCE_OUT     	= 203; // mất, căn bằng
    public static final int 	TYPE_01_OU_STK_BALANCE_FAIL    	= 204; //expiration
    public static final int 	TYPE_01_OU_STK_BALANCE_OTHER   	= 205; //expiration

	public static final int 	TYPE_02_IN						= 1;
	public static final int 	TYPE_02_OUT						= 2;
	
    public static final int 	TYPE_03_ORD_NORMAL            	= 1;
    public static final int 	TYPE_03_ORD_VOUCHER            	= 10;
    
    public static final int 	TYPE_04_PAY_BY_CASH            	= 1;
    public static final int 	TYPE_04_PAY_BY_BANK            	= 2;
    public static final int 	TYPE_04_PAY_BY_EWALLET          = 3;
    public static final int 	TYPE_04_PAY_BY_CARD         	= 4;
    public static final int 	TYPE_04_PAY_BY_VOUCHER	       	= 10;
    
    
    
	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                              =	"I_ID";
	public static final String	COL_I_TYPE_01                         =	"I_Type_01";
	public static final String	COL_I_TYPE_02                         =	"I_Type_02";
	public static final String	COL_I_TYPE_03                         =	"I_Type_03";
	public static final String	COL_I_TYPE_04                         =	"I_Type_04";
	public static final String	COL_I_TYPE_05                         =	"I_Type_05";
	public static final String	COL_T_CODE_01                         =	"T_Code_01";
	public static final String	COL_T_CODE_02                         =	"T_Code_02";
	public static final String	COL_D_DATE_01                         =	"D_Date_01";// Ngay tao don hang
	public static final String	COL_D_DATE_02                         =	"D_Date_02";// Ngay Nhap kho san pham
	public static final String	COL_D_DATE_03                         =	"D_Date_03";
	public static final String	COL_D_DATE_04                         =	"D_Date_04";
	
	public static final String	COL_F_VAL_00                          =	"F_Val_00"; // number of line'
	public static final String	COL_F_VAL_01                          =	"F_Val_01"; // tổng trước thuế
	public static final String	COL_F_VAL_02                          =	"F_Val_02"; // tổng thuế theo detail
	public static final String	COL_F_VAL_03                          =	"F_Val_03"; // discount trước thuế
	public static final String	COL_F_VAL_04                          =	"F_Val_04"; // discount sau thuế
	public static final String	COL_F_VAL_05                          =	"F_Val_05"; // thuế trừ đi khi áp dụng khuyến mãi: F05 = (F04-F03)+(F02)
	public static final String	COL_F_VAL_06                          =	"F_Val_06"; // final amount TTC
	public static final String	COL_F_VAL_07                          =	"F_Val_07"; // final amount TTC after discount
	public static final String	COL_F_VAL_08                          =	"F_Val_08"; // client amount
	public static final String	COL_F_VAL_09                          =	"F_Val_09"; // return amount
	public static final String	COL_F_VAL_10                          =	"F_Val_10";
	
	public static final String	COL_I_PER_MANAGER                     =	"I_Per_Manager";
	public static final String	COL_I_PER_PERSON_01                   =	"I_Per_Person_01";
	public static final String	COL_I_PER_PERSON_02                   =	"I_Per_Person_02";
	public static final String	COL_I_PER_PERSON_03                   =	"I_Per_Person_03";
	public static final String	COL_I_PER_PERSON_04                   =	"I_Per_Person_04";
	public static final String	COL_I_PER_PERSON_05                   =	"I_Per_Person_05";
	
	public static final String	COL_I_ENTITY_TYPE                     =	"I_Entity_Type"; //typ warehouse/offer
	public static final String	COL_I_ENTITY_ID_01                    =	"I_Entity_ID_01";//src/supplier
	public static final String	COL_I_ENTITY_ID_02                   =	"I_Entity_ID_02";//dest/client
	
	public static final String	COL_I_MAT_VAL_01                      =	"I_Mat_Val_01";//COL_I_MAT_WAREHOUSE_01
	public static final String	COL_I_MAT_VAL_02                      =	"I_Mat_Val_02";//COL_I_MAT_WAREHOUSE_02
	
	public static final String	COL_I_STATUS                          =	"I_Status";
	public static final String	COL_I_AUT_USER_01                     =	"I_Aut_User_01";
	public static final String	COL_I_AUT_USER_02                     =	"I_Aut_User_02";
	public static final String	COL_T_INFO_01                     	  =	"T_Info_01";
	public static final String	COL_T_INFO_02                     	  =	"T_Info_02";
	public static final String	COL_T_INFO_03                     	  =	"T_Info_03";
	public static final String	COL_T_INFO_04                     	  =	"T_Info_04";
	public static final String	COL_T_INFO_05                     	  =	"T_Info_05";
	
	public static final String	COL_I_PARENT                     	  =	"I_Parent"; // order source, ex : transfert
	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              =	"I_ID";
	public static final String	ATT_I_TYPE_01                         =	"I_Type_01";
	public static final String	ATT_I_TYPE_02                         =	"I_Type_02";
	public static final String	ATT_I_TYPE_03                         =	"I_Type_03";
	public static final String	ATT_I_TYPE_04                         =	"I_Type_04";
	public static final String	ATT_I_TYPE_05                         =	"I_Type_05";
	public static final String	ATT_T_CODE_01                         =	"T_Code_01";
	public static final String	ATT_T_CODE_02                         =	"T_Code_02";
	public static final String	ATT_D_DATE_01                         =	"D_Date_01";// Ngay tao don hang
	public static final String	ATT_D_DATE_02                         =	"D_Date_02";// Ngay Nhap kho san pham
	public static final String	ATT_D_DATE_03                         =	"D_Date_03";
	public static final String	ATT_D_DATE_04                         =	"D_Date_04";
	
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
	
	public static final String	ATT_I_PER_MANAGER                     =	"I_Per_Manager";
	public static final String	ATT_I_PER_PERSON_01                   =	"I_Per_Person_01";
	public static final String	ATT_I_PER_PERSON_02                   =	"I_Per_Person_02";
	public static final String	ATT_I_PER_PERSON_03                   =	"I_Per_Person_03";
	public static final String	ATT_I_PER_PERSON_04                   =	"I_Per_Person_04";
	public static final String	ATT_I_PER_PERSON_05                   =	"I_Per_Person_05";
	
	public static final String	ATT_I_ENTITY_TYPE                     =	"I_Entity_Type";
	public static final String	ATT_I_ENTITY_ID_01                    =	"I_Entity_ID_01";
	public static final String	ATT_I_ENTITY_ID_02                    =	"I_Entity_ID_02";
	
	public static final String	ATT_I_MAT_VAL_01                      =	"I_Mat_Val_01";
	public static final String	ATT_I_MAT_VAL_02                      =	"I_Mat_Val_02";
	public static final String	ATT_I_STATUS                          =	"I_Status";
	public static final String	ATT_I_AUT_USER_01                     =	"I_Aut_User_01";
	public static final String	ATT_I_AUT_USER_02                     =	"I_Aut_User_02";
	public static final String	ATT_T_INFO_01                     	  =	"T_Info_01";
	public static final String	ATT_T_INFO_02                     	  =	"T_Info_02";
	public static final String	ATT_T_INFO_03                     	  =	"T_Info_03";
	public static final String	ATT_T_INFO_04                     	  =	"T_Info_04";
	public static final String	ATT_T_INFO_05                     	  =	"T_Info_05";
	
	public static final String	ATT_I_PARENT                     	  =	"I_Parent";
	
	public static final String	ATT_O_DETAILS                    	  =	"O_Details";
	
	public static final String	ATT_O_PAYMENTS                    	  =	"O_Payments";

	public static final String	ATT_O_DETAILS01                    	  =	"O_Details01"; //used for invoice balance in	
	public static final String	ATT_O_DETAILS02                    	  =	"O_Details02"; //used for invoice balance out

	public static final String	ATT_O_DOCUMENTS                    	  =	"O_Documents";
	public static final String	ATT_O_INFORMATIONS                    =	"O_Informations";
	public static final String	ATT_O_HISTORIES                    	  =	"O_Histories";

	public static final String	ATT_O_SUPPLIER                    	  =	"O_Supplier";
	public static final String	ATT_O_CLIENT                   		  =	"O_Client";
	public static final String	ATT_O_TPARTY                   		  =	"O_TParty";

	public static final String	ATT_O_INVOICE                     	  =	"O_Invoice";
	public static final String	ATT_O_STOCKIO                     	  =	"O_StockIo";
	
	public static final String	ATT_O_OFFER                           =	"O_Offer";

	
	public static final String	ATT_O_USER_01                         =	"O_User_01";
	public static final String	ATT_O_USER_02                         =	"O_User_02";



	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<TaSorOrder> 	DAO;
	static{
		DAO = new EntityDAO<TaSorOrder>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), TaSorOrder.class,RIGHTS, HISTORY, DefDBExt.TA_SOR_ORDER, DefDBExt.ID_TA_SOR_ORDER);

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
    
	@Column(name=COL_I_STATUS, nullable = true)
	private	Integer         I_Status;
     
	@Column(name=COL_I_TYPE_01, nullable = true)
	private	Integer         I_Type_01;
    
	@Column(name=COL_I_TYPE_02, nullable = true)
	private	Integer         I_Type_02;
    
	@Column(name=COL_I_TYPE_03, nullable = true)
	private	Integer         I_Type_03;
    
	@Column(name=COL_I_TYPE_04, nullable = true)
	private	Integer         I_Type_04;
    
	@Column(name=COL_I_TYPE_05, nullable = true)
	private	Integer         I_Type_05;
    
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
     
	@Column(name=COL_F_VAL_10, nullable = true)
	private	Double          F_Val_10;
     
	@Column(name=COL_T_INFO_01, nullable = true)
	private	String          T_Info_01;
    
	@Column(name=COL_T_INFO_02, nullable = true)
	private	String          T_Info_02;
    
	@Column(name=COL_T_INFO_03, nullable = true)
	private	String          T_Info_03;
    
	@Column(name=COL_T_INFO_04, nullable = true)
	private	String          T_Info_04;
    
	@Column(name=COL_T_INFO_05, nullable = true)
	private	String          T_Info_05;
    
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
    
	@Column(name=COL_I_ENTITY_TYPE, nullable = true)
	private	Integer         I_Entity_Type;

	@Column(name=COL_I_ENTITY_ID_01, nullable = true)
	private	Integer         I_Entity_ID_01;

	@Column(name=COL_I_ENTITY_ID_02, nullable = true)
	private	Integer         I_Entity_ID_02;

	@Column(name=COL_I_MAT_VAL_01, nullable = true)
	private	Integer         I_Mat_Val_01;
 
	@Column(name=COL_I_MAT_VAL_02, nullable = true)
	private	Integer         I_Mat_Val_02;
 
	@Column(name=COL_I_PER_PERSON_01, nullable = true)
	private	Integer         I_Per_Person_01;

	@Column(name=COL_I_PER_PERSON_02, nullable = true)
	private	Integer         I_Per_Person_02;

	@Column(name=COL_I_PER_PERSON_03, nullable = true)
	private	Integer         I_Per_Person_03;

	@Column(name=COL_I_PER_PERSON_04, nullable = true)
	private	Integer         I_Per_Person_04;

	@Column(name=COL_I_PER_PERSON_05, nullable = true)
	private	Integer         I_Per_Person_05;

	@Column(name=COL_I_PARENT, nullable = true)
	private	Integer         I_Parent;
     
	@Column(name=COL_I_PER_MANAGER, nullable = true)
	private	Integer         I_Per_Manager;
	
	//-----------------------Transient Variables-------------------------
	@Transient
	private String 						T_Per_Manager;
	
	@Transient
	private	List<TaTpyDocument> 		O_Documents;
	
	@Transient
	private	List<TaTpyInformation> 		O_Informations;

	@Transient
	private	List<TaSorOrderDetail> 		O_Details; 

	@Transient
	private	TaPerPerson 				O_Supplier; 

	@Transient
	private	TaPerPerson 				O_Client;
	
	@Transient
	private	TaPerPerson 				O_TParty; 
	
	@Transient
	private	TaMatWarehouse 				O_Mat_Warehouse; 

	@Transient
	private	List<TaMatStockIo> 			O_StockIo; 
	
	@Transient
	private ViAutUserDyn					O_User_01;
	//---------------------Constructeurs----------------------
	public TaSorOrder(){}
	
	public TaSorOrder(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		//doInitDBFlag();
	}
	public TaSorOrder(TaSorOrder ord) throws Exception {
		this.doMergeWith(ord);
		//doInitDBFlag();
	}



	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}

	@Override
	public void doMergeWith(TaSorOrder ent) {
		if (ent == this) return;
		this.T_Code_01              = ent.T_Code_01;
		this.T_Code_02              = ent.T_Code_02;
		this.I_Status               = ent.I_Status;
		this.I_Type_01              = ent.I_Type_01;
		this.I_Type_02              = ent.I_Type_02;
		this.I_Type_03              = ent.I_Type_03;
		this.I_Type_04              = ent.I_Type_04;
		this.I_Type_05              = ent.I_Type_05;
		this.F_Val_00               = ent.F_Val_00;
		this.F_Val_01               = ent.F_Val_01;
		this.F_Val_02               = ent.F_Val_02;
		this.F_Val_03               = ent.F_Val_03;
		this.F_Val_04               = ent.F_Val_04;
		this.F_Val_05               = ent.F_Val_05;
		this.F_Val_06               = ent.F_Val_06;
		this.F_Val_07               = ent.F_Val_07;
		this.F_Val_08               = ent.F_Val_08;
		this.F_Val_09               = ent.F_Val_09;
		this.F_Val_10               = ent.F_Val_10;
		this.T_Info_01              = ent.T_Info_01;
		this.T_Info_02              = ent.T_Info_02;
		this.T_Info_03              = ent.T_Info_03;
		this.T_Info_04              = ent.T_Info_04;
		this.T_Info_05              = ent.T_Info_05;
		this.D_Date_01              = ent.D_Date_01;
		this.D_Date_02              = ent.D_Date_02;
		this.D_Date_03              = ent.D_Date_03;
		this.D_Date_04              = ent.D_Date_04;
		this.I_Aut_User_01          = ent.I_Aut_User_01;
		this.I_Aut_User_02          = ent.I_Aut_User_02;
		this.I_Entity_Type			= ent.I_Entity_Type;
		this.I_Entity_ID_01			= ent.I_Entity_ID_01; 
		this.I_Entity_ID_02			= ent.I_Entity_ID_02;
		this.I_Mat_Val_01           = ent.I_Mat_Val_01;
		this.I_Mat_Val_02           = ent.I_Mat_Val_02;
		this.I_Per_Person_01        = ent.I_Per_Person_01;
		this.I_Per_Person_02        = ent.I_Per_Person_02;
		this.I_Per_Person_03        = ent.I_Per_Person_03;
		this.I_Per_Person_04        = ent.I_Per_Person_04;
		this.I_Per_Person_05        = ent.I_Per_Person_05;
		this.I_Parent               = ent.I_Parent;
		this.I_Per_Manager          = ent.I_Per_Manager;

		//---------------------Merge Transient Variables if exist-----------------------
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;

		ok = (I_ID == ((TaSorOrder)o).I_ID);
		if (!ok) return ok;


		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}

	public static List<TaSorOrder> reqListInvoice(int begin, int number, String orderCol, int orderType, int typ01, int lstType, String param, String searchKey, int userId, HashMap<String, String> filterOpt) throws Exception {
		try{
			List<TaSorOrder> result = DAO.reqList(begin,number);
			return result;
		}catch(Exception e){
			System.out.println(e.toString());
		}

		return null;
	}


	public  void doBuildManagerName(boolean forced) throws Exception{
		if(this.I_Per_Manager != null){	
			TaPerPerson per = TaPerPerson.DAO.reqEntityByRef(I_Per_Manager);
			if(per !=null){
				this.T_Per_Manager = (String)per.req(TaPerPerson.ATT_T_NAME_01);
			}
		}
	}

	public void doBuildInformations(boolean forced) throws Exception{
		if (!forced && this.O_Informations!=null) return;		
		this.O_Informations= TaTpyInformation.DAO.reqList(
				Restrictions.eq(TaTpyInformation.ATT_I_ENTITY_TYPE	, DefDBExt.ID_TA_SOR_ORDER),
				Restrictions.eq(TaTpyInformation.ATT_I_ENTITY_ID	, this.I_ID));


	}

	public void doBuildDocuments(boolean forced) throws Exception {
		if (this.O_Documents != null && !forced) return;
		this.O_Documents = TaTpyDocument.reqTpyDocuments(ENT_TYP, I_ID, null, null);
	}

	public void doRemoveDocuments () throws Exception{				
		this.doBuildDocuments(true);		
		if (O_Documents!=null && O_Documents.size()>0)
			TaTpyDocument.DAO.doRemove(O_Documents);	
		O_Documents = null;
	}	

	public void doRemoveDocuments (Collection docIds) throws Exception{				
		List<TaTpyDocument> lP  = TaTpyDocument.DAO.reqList(Restrictions.in(TaTpyDocument.ATT_I_ID, docIds));
		if (lP!=null && lP.size()>0) TaTpyDocument.DAO.doRemove(lP);

		this.doBuildDocuments(true);
	}

	public void doRemoveDocuments (Integer... docIds) throws Exception{		
		List<TaTpyDocument> lP  = TaTpyDocument.DAO.reqList(Restrictions.in(TaTpyDocument.ATT_I_ID, docIds));
		if (lP!=null && lP.size()>0) TaTpyDocument.DAO.doRemove(lP);

		this.doBuildDocuments(true);
	}

	
	public void doBuildDetails (boolean forced) throws Exception{
		if (!forced && this.O_Details!=null) return;		
		this.O_Details = TaSorOrderDetail.DAO.reqList(Restrictions.eq(TaSorOrderDetail.ATT_I_SOR_ORDER, I_ID));	
		//		if(this.O_Details != null && this.O_Details.size() > 0){
		//			for(TaSorOrderDetail o : this.O_Details){
		//				o.doBuildDocuments(forced);
		//			}			
		//		}
	}

//	public void doRemoveDetails (Collection dIds) throws Exception{				
//		List<TaSorOrderDetail> lP  = TaSorOrderDetail.DAO.reqList_In(TaSorOrderDetail.ATT_I_ID, dIds);
//		if (lP!=null && lP.size()>0) TaSorOrderDetail.DAO.doRemove(lP);
//
//		this.doBuildDetails(true);
//	}

	public void doBuildSupplier (boolean forced) throws Exception{				
		if (!forced && this.O_Supplier!=null) return;		
		Integer supplier = (Integer) this.req(TaSorOrder.ATT_I_PER_PERSON_01);
		if(supplier != null){
			this.O_Supplier = TaPerPerson.DAO.reqEntityByRef(supplier);
		}
	}

	public void doBuildClient (boolean forced) throws Exception{				
		if (!forced && this.O_Client!=null) return;		
		Integer client = (Integer) this.req(TaSorOrder.ATT_I_PER_PERSON_02);
		if(client != null){
			this.O_Client = TaPerPerson.DAO.reqEntityByRef(client);
		}
	}
	
	public void doBuildTParty (boolean forced) throws Exception{				
		if (!forced && this.O_TParty!=null) return;		
		Integer tparty = (Integer) this.req(TaSorOrder.ATT_I_PER_PERSON_03);
		if(tparty != null){
			this.O_TParty = TaPerPerson.DAO.reqEntityByRef(tparty);
		}
	}
	
	public void doBuildWarehouse (boolean forced) throws Exception{				
		if (!forced && this.O_Mat_Warehouse!=null) return;		
		Integer wh = (Integer) this.req(TaSorOrder.ATT_I_MAT_VAL_01);
		if(wh != null){
			this.O_Mat_Warehouse = TaMatWarehouse.DAO.reqEntityByRef(wh);
		}
	}
	
	public void doBuildUserCreate (boolean forced) throws Exception{				
		if (!forced && this.O_User_01!=null) return;		
		Integer uId01 = (Integer) this.req(TaSorOrder.ATT_I_AUT_USER_01);
		if(uId01 != null){
			this.O_User_01 = ViAutUserDyn.DAO.reqEntityByRef(uId01);
		}
	}

	
	public void doSetAutUser01 (Integer uId) {
		this.I_Aut_User_01 = uId;
	}
	public void doSetAutUser02 (Integer uId) {
		this.I_Aut_User_02 = uId;
	}
	
	public TaSorOrder reqClone () throws Exception {
		TaSorOrder ord = new TaSorOrder();
		ord.reqSetAttr(
				ATT_I_ID                              ,null,
				ATT_I_TYPE_01                         ,I_Type_01,
				ATT_I_TYPE_02                         ,I_Type_02,
				ATT_I_TYPE_03                         ,I_Type_03,
				ATT_I_TYPE_04                         ,I_Type_04,
				ATT_T_CODE_01                         ,T_Code_01,
				ATT_T_CODE_02                         ,T_Code_02,
				ATT_D_DATE_01                         ,D_Date_01,// Ngay tao don hang
				ATT_D_DATE_02                         ,D_Date_02,// Ngay Nhap kho san pham
				ATT_D_DATE_03                         ,D_Date_03,
				ATT_D_DATE_04                         ,D_Date_04,
				ATT_F_VAL_01                          ,F_Val_01,
				ATT_F_VAL_02                          ,F_Val_02,
				ATT_F_VAL_03                          ,F_Val_03,
				ATT_F_VAL_04                          ,F_Val_04,
				ATT_F_VAL_05                          ,F_Val_05,
				ATT_F_VAL_06                          ,F_Val_06,
				ATT_F_VAL_07                          ,F_Val_07,
				ATT_F_VAL_08                          ,F_Val_08,
				ATT_F_VAL_09                          ,F_Val_09,
				ATT_F_VAL_10                          ,F_Val_10,
				ATT_F_VAL_00                          ,F_Val_00,
				ATT_I_PER_MANAGER                     ,I_Per_Manager,
				ATT_I_PER_PERSON_01                   ,I_Per_Person_01,
				ATT_I_PER_PERSON_02                   ,I_Per_Person_02,
				ATT_I_PER_PERSON_03                   ,I_Per_Person_03,
				ATT_I_PER_PERSON_04                   ,I_Per_Person_04,
				ATT_I_MAT_VAL_01                      ,I_Mat_Val_01,
				ATT_I_MAT_VAL_02                      ,I_Mat_Val_02,
				ATT_I_STATUS                          ,I_Status,
				ATT_I_AUT_USER_01                     ,I_Aut_User_01,
				ATT_I_AUT_USER_02                     ,I_Aut_User_02,
				ATT_T_INFO_01                     	  ,T_Info_01,
				ATT_T_INFO_02                     	  ,T_Info_02,
				ATT_T_INFO_03                     	  ,T_Info_03,
				ATT_T_INFO_04                     	  ,T_Info_04,
				ATT_T_INFO_05                     	  ,T_Info_05,
				
				ATT_I_PARENT                     	  ,I_Parent
				
		);
		return ord;
	}
	
	public Integer reqID () {
		return this.I_ID;
	}
}
