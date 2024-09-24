package com.hnv.db.sor;

import java.io.Serializable;
import java.util.Collection;
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

import org.hibernate.criterion.Restrictions;

import com.hnv.api.main.Hnv_CfgHibernate;
import com.hnv.data.json.JSONArray;
import com.hnv.db.EntityAbstract;
import com.hnv.db.EntityDAO;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.def.DefDBExt;		

/**
* TaInvInvoiceDetail by H&V SAS
*/
@Entity
@Table(name = DefDBExt.TA_SOR_ORDER_DETAIL )
public class TaSorOrderDetail extends EntityAbstract<TaSorOrderDetail> {

	private static final long serialVersionUID = 1L;
	
	private static final int  ENT_TYP			= DefDBExt.ID_TA_SOR_ORDER_DETAIL;

	public static final Integer DETAIL_TYPE_BALANCE_IN 	=1;
	public static final Integer DETAIL_TYPE_BALANCE_OUT =2;
	
	public static final int		STAT_NEW 			= 0;
	public static final int		STAT_ACTIVE 		= 1;
	public static final int		STAT_COMPLETED 		= 2;
	public static final int		STAT_REVIEW 		= 5; 
	public static final int		STAT_DELETED 		= 10;
	
	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                              =	"I_ID";
	public static final String	COL_I_SOR_ORDER                       =	"I_Sor_Order";
	public static final String	COL_I_STATUS                          =	"I_Status";
	public static final String	COL_I_PRIORITY                        =	"I_Priority";
	public static final String	COL_I_MAT_MATERIAL                    =	"I_Mat_Material";
	public static final String	COL_I_MAT_PRICE                       =	"I_Mat_Price";
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
	public static final String	COL_F_VAL_10                          =	"F_Val_10";
	public static final String	COL_T_INFO_01                         =	"T_Info_01";
	public static final String	COL_T_INFO_02                         =	"T_Info_02";
	public static final String	COL_T_INFO_03                         =	"T_Info_03";
	public static final String	COL_T_INFO_04                         =	"T_Info_04";
	public static final String	COL_T_INFO_05                         =	"T_Info_05";
	public static final String	COL_T_INFO_06                         =	"T_Info_06";
	public static final String	COL_T_INFO_07                         =	"T_Info_07";
	public static final String	COL_T_INFO_08                         =	"T_Info_08";
	public static final String	COL_T_INFO_09                         =	"T_Info_09";
	public static final String	COL_T_INFO_10                         =	"T_Info_10";
	public static final String	COL_D_DATE_01                         =	"D_Date_01";
	public static final String	COL_D_DATE_02                         =	"D_Date_02";
	public static final String	COL_I_PER_PERSON_01                   =	"I_Per_Person_01";
	public static final String	COL_I_PER_PERSON_02                   =	"I_Per_Person_02";
	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              =	"I_ID";
	public static final String	ATT_I_SOR_ORDER                       =	"I_Sor_Order";
	public static final String	ATT_I_STATUS                          =	"I_Status";
	public static final String	ATT_I_PRIORITY                        =	"I_Priority";
	public static final String	ATT_I_MAT_MATERIAL                    =	"I_Mat_Material";
	public static final String	ATT_I_MAT_PRICE                       =	"I_Mat_Price";
	public static final String	ATT_T_INFO_01                         =	"T_Info_01";
	public static final String	ATT_T_INFO_02                         =	"T_Info_02";
	public static final String	ATT_T_INFO_03                         =	"T_Info_03";
	public static final String	ATT_T_INFO_04                         =	"T_Info_04";
	public static final String	ATT_T_INFO_05                         =	"T_Info_05";
	public static final String	ATT_T_INFO_06                         =	"T_Info_06";
	public static final String	ATT_T_INFO_07                         =	"T_Info_07";
	public static final String	ATT_T_INFO_08                         =	"T_Info_08";
	public static final String	ATT_T_INFO_09                         =	"T_Info_09";
	public static final String	ATT_T_INFO_10                         =	"T_Info_10";
	
	
	public static final String	ATT_F_VAL_00                          =	"F_Val_00"; //Q input
	public static final String	ATT_F_VAL_01                          =	"F_Val_01"; //unit price
	public static final String	ATT_F_VAL_02                          =	"F_Val_02"; //final price
	public static final String	ATT_F_VAL_03                          =	"F_Val_03"; //tax %
	public static final String	ATT_F_VAL_04                          =	"F_Val_04"; //
	public static final String	ATT_F_VAL_05                          =	"F_Val_05"; //
	public static final String	ATT_F_VAL_06                          =	"F_Val_06"; //discount
	public static final String	ATT_F_VAL_07                          =	"F_Val_07"; //amount before tax
	public static final String	ATT_F_VAL_08                          =	"F_Val_08"; //tax amount
	public static final String	ATT_F_VAL_09                          =	"F_Val_09"; //final amount
	public static final String	ATT_F_VAL_10                          =	"F_Val_10"; //ratio base
	
	public static final String	ATT_D_DATE_01                         =	"D_Date_01";// Ngay tao don hang
	public static final String	ATT_D_DATE_02                         =	"D_Date_02";
	
	public static final String	ATT_I_PER_PERSON_01                   =	"I_Per_Person_01";
	public static final String	ATT_I_PER_PERSON_02                   =	"I_Per_Person_02";
	
	public static final String	ATT_O_DOCUMENTS                    	  =	"O_Documents";
	
	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<TaSorOrderDetail> 	DAO;
	static{
		DAO = new EntityDAO<TaSorOrderDetail>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), TaSorOrderDetail.class,RIGHTS, HISTORY, DefDBExt.TA_SOR_ORDER_DETAIL, DefDBExt.ID_TA_SOR_ORDER_DETAIL);

	}

	//-----------------------Class Attributs-------------------------
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;
         
	@Column(name=COL_I_SOR_ORDER, nullable = true)
	private	Integer         I_Sor_Order;
  
	@Column(name=COL_I_STATUS, nullable = true)
	private	Integer         I_Status;
     
	@Column(name=COL_I_PRIORITY, nullable = true)
	private	Integer         I_Priority;
   
	@Column(name=COL_I_MAT_MATERIAL, nullable = true)
	private	Integer         I_Mat_Material;

	@Column(name=COL_I_MAT_PRICE, nullable = true)
	private	Integer         I_Mat_Price;
  
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
    
	@Column(name=COL_T_INFO_06, nullable = true)
	private	String          T_Info_06;
    
	@Column(name=COL_T_INFO_07, nullable = true)
	private	String          T_Info_07;
    
	@Column(name=COL_T_INFO_08, nullable = true)
	private	String          T_Info_08;
    
	@Column(name=COL_T_INFO_09, nullable = true)
	private	String          T_Info_09;
    
	@Column(name=COL_T_INFO_10, nullable = true)
	private	String          T_Info_10;
    
	@Column(name=COL_D_DATE_01, nullable = true)
	private	Date            D_Date_01;
    
	@Column(name=COL_D_DATE_02, nullable = true)
	private	Date            D_Date_02;
    
	@Column(name=COL_I_PER_PERSON_01, nullable = true)
	private	Integer         I_Per_Person_01;

	@Column(name=COL_I_PER_PERSON_02, nullable = true)
	private	Integer         I_Per_Person_02;
	//---------------------------------------------------------------------
	@Transient
	private	List<TaTpyDocument> 	O_Documents;
	
	//---------------------Constructeurs-----------------------
	private TaSorOrderDetail(){}

	public TaSorOrderDetail(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		//doInitDBFlag();
	}
	
	public TaSorOrderDetail(Integer I_ID, Integer I_Order, Double F_Quantity, Double F_Val_01) throws Exception {
		this.reqSetAttr(
			ATT_I_ID         , I_ID,
//			ATT_I_ORDER    , I_Order,
//			ATT_F_QUANTITY   , F_Quantity,
			ATT_F_VAL_01     , F_Val_01
		);
		//doInitDBFlag();
	}
	public TaSorOrderDetail(Integer I_ID, Integer I_Order, Integer I_Priority, String T_Code, Integer I_Type, Integer I_Status, Integer I_Article, String T_Title, String T_Description, String T_Comment_01, String T_Comment_02, Double F_Quantity, Integer I_Mat_Unit, String T_Unit, Double F_Unit_Ratio, Double F_Val_01, Double F_Val_02, Double F_Val_03, Double F_Val_04, Double F_Val_05, Double F_Val_06, Double F_Val_07, Integer I_Order_Ref,
			Integer I_Parent_Type, Integer I_Parent_ID) throws Exception {
//		this.reqSetAttr(
//			ATT_I_ID                   , I_ID,
//			ATT_I_ORDER                , I_Order,
//			ATT_I_PRIORITY             , I_Priority,
//			ATT_T_CODE                 , T_Code,
//			ATT_I_TYPE                 , I_Type,
//			ATT_I_STATUS               , I_Status,		
//			ATT_T_TITLE                , T_Title,
//			ATT_T_DESCRIPTION          , T_Description,
//			ATT_T_COMMENT_01           , T_Comment_01,
//			ATT_T_COMMENT_02           , T_Comment_02,
//			ATT_F_QUANTITY             , F_Quantity,
//			ATT_I_MAT_UNIT             , I_Mat_Unit,
//			ATT_T_UNIT                 , T_Unit,
//			ATT_F_UNIT_RATIO           , F_Unit_Ratio,
//			ATT_F_VAL_01               , F_Val_01,
//			ATT_F_VAL_02               , F_Val_02,
//			ATT_F_VAL_03               , F_Val_03,
//			ATT_F_VAL_04               , F_Val_04,
//			ATT_F_VAL_05               , F_Val_05,
//			ATT_F_VAL_06               , F_Val_06,
//			ATT_F_VAL_07               , F_Val_07,
//		
//			ATT_I_PARENT_TYPE			, I_Parent_Type,
//			ATT_I_PARENT_ID				, I_Parent_ID
//		);
		//doInitDBFlag();
	}
	
	
	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}
	
	public Integer reqID() {
		return this.I_ID;

	}

	@Override
	public void doMergeWith(TaSorOrderDetail ent) {
		if (ent == this) return;
//		this.I_Db_Flag              = ent.I_Db_Flag;
//		this.I_Order              = ent.I_Order;
		this.I_Priority             = ent.I_Priority;
//		this.T_Code                 = ent.T_Code;
//		this.I_Type                 = ent.I_Type;
//		this.I_Status               = ent.I_Status;
//		
//		this.T_Title                = ent.T_Title;
//		this.T_Description          = ent.T_Description;
//		this.T_Comment_01           = ent.T_Comment_01;
//		this.T_Comment_02           = ent.T_Comment_02;
//		this.F_Quantity             = ent.F_Quantity;
//		this.I_Mat_Unit             = ent.I_Mat_Unit;
//		this.T_Unit                 = ent.T_Unit;
//		this.F_Unit_Ratio           = ent.F_Unit_Ratio;
		this.F_Val_01               = ent.F_Val_01;
		this.F_Val_02               = ent.F_Val_02;
		this.F_Val_03               = ent.F_Val_03;
		this.F_Val_04               = ent.F_Val_04;
		this.F_Val_05               = ent.F_Val_05;
		this.F_Val_06               = ent.F_Val_06;
		this.F_Val_07               = ent.F_Val_07;
		
//		this.I_Parent_Type          = ent.I_Parent_Type;
//		this.I_Parent_ID            = ent.I_Parent_ID;


		//---------------------Merge Transient Variables if exist-----------------------
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;
		
		ok = (I_ID == ((TaSorOrderDetail)o).I_ID);
		if (!ok) return ok;

				
		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}

	@Override
	public String toString() {
		return 	"TaSorOrderDetail { " +
				"I_ID:"+                      I_ID +"," + 
//				"I_Db_Flag:"+                 I_Db_Flag +"," + 
//				"I_Order:"+                   I_Order +"," + 
				"I_Priority:"+                I_Priority +"," + 
//				"T_Code:"+                    T_Code +"," + 
//				"I_Type:"+                    I_Type +"," + 
//				"I_Status:"+                  I_Status +"," + 				
//				"T_Title:"+                   T_Title +"," + 
//				"T_Description:"+             T_Description +"," + 
//				"T_Comment_01:"+              T_Comment_01 +"," + 
//				"T_Comment_02:"+              T_Comment_02 +"," + 
//				"F_Quantity:"+                F_Quantity +"," + 
//				"I_Mat_Unit:"+                I_Mat_Unit +"," + 
//				"T_Unit:"+                    T_Unit +"," + 
//				"F_Unit_Ratio:"+              F_Unit_Ratio +"," + 
				"F_Val_01:"+                  F_Val_01 +"," + 
				"F_Val_02:"+                  F_Val_02 +"," + 
				"F_Val_03:"+                  F_Val_03 +"," + 
				"F_Val_04:"+                  F_Val_04 +"," + 
				"F_Val_05:"+                  F_Val_05 +"," + 
				"F_Val_06:"+                  F_Val_06 +"," + 
				"F_Val_07:"+                  F_Val_07 +"," + 
				
//				"I_Parent_Type:"+             I_Parent_Type +"," + 
//				"I_Parent_ID:"+               I_Parent_ID +"," + 
				 "}";

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
	
	public void doCalcualtePrices(Integer taxType){
		
//		if(taxType == 0){//no tax
//			Double fBase = this.F_Quantity * this.F_Val_01;
//			if(fBase == 0)
//				return ;
//			if(this.F_Val_03 == null)
//				this.F_Val_03 = 0.0;
//			this.F_Val_02 = this.F_Val_01 * (1+ this.F_Val_03/100);
//			
//			if(this.F_Val_05 != null && this.F_Val_05 > 0){
//				this.F_Val_04 = this.F_Val_05 / fBase * 100;
//			}else{
//				if(this.F_Val_04 == null)
//					this.F_Val_04 = 0.0;
//				this.F_Val_05 = this.F_Val_04 * fBase/100;
//			}
//			this.F_Val_06 = this.F_Val_05 * ( 1* this.F_Val_03/100);
//			this.F_Val_07 = fBase - this.F_Val_05;
//			this.F_Val_08 = fBase * this.F_Val_03/100;
//			this.F_Val_09 = this.F_Val_07 + this.F_Val_08;
//			
//		}else{ //with tax
//			this.F_Val_01 = this.F_Val_02/(1+ this.F_Val_03/100);
//			//this.F_Val_02 = this.F_Val_01 * (1+ this.F_Val_03);
//			
//			Double fBaseTax = this.F_Quantity * this.F_Val_02  ;
//			if(fBaseTax == 0)
//				return ;
//			if(this.F_Val_06 != null && this.F_Val_06 > 0){
//				this.F_Val_04 = this.F_Val_06 / fBaseTax;
//			}else{
//				this.F_Val_06 = this.F_Val_04 * fBaseTax;
//			}
//			this.F_Val_05 = this.F_Val_06 / ( 1* this.F_Val_03);
//			this.F_Val_08 = this.F_Val_01 * this.F_Quantity * this.F_Val_03;
//			this.F_Val_09 = fBaseTax - this.F_Val_06;
//			this.F_Val_07 = this.F_Val_09 - this.F_Val_08;
//		}
	}
	
	
	public TaSorOrderDetail reqClone() throws Exception {
		TaSorOrderDetail det = new TaSorOrderDetail();
		
		det.reqSetAttr(
				ATT_I_ID                   , null,
				ATT_I_SOR_ORDER            , null,
				
				ATT_I_PRIORITY             , I_Priority,
				ATT_I_MAT_MATERIAL         , I_Mat_Material,	
				ATT_I_MAT_PRICE			   , I_Mat_Price,
				
				ATT_I_STATUS               , I_Status,	
				
				ATT_T_INFO_01              , T_Info_01,
				ATT_T_INFO_02              , T_Info_02,
				ATT_T_INFO_03              , T_Info_03,
				ATT_T_INFO_04              , T_Info_04,
				ATT_T_INFO_05              , T_Info_05,
				ATT_T_INFO_06              , T_Info_06,
				
				ATT_F_VAL_00               , F_Val_00, //Q input
				ATT_F_VAL_01               , F_Val_01, //unit price
				ATT_F_VAL_02               , F_Val_02, //final price
				ATT_F_VAL_03               , F_Val_03, //tax %
				ATT_F_VAL_04               , F_Val_04, //
				ATT_F_VAL_05               , F_Val_05, //
				ATT_F_VAL_06               , F_Val_06, //discount
				ATT_F_VAL_07               , F_Val_07, //amount before tax
				ATT_F_VAL_08               , F_Val_08, //tax amount
				ATT_F_VAL_09               , F_Val_09, //final amount
				ATT_F_VAL_10               , F_Val_10, //ratio base
				
				ATT_D_DATE_01              , D_Date_01,// Ngay tao don hang
				ATT_D_DATE_02              , D_Date_02,
				
				ATT_I_PER_PERSON_01   	   , I_Per_Person_01,
				ATT_I_PER_PERSON_02        , I_Per_Person_02
			);
		
		return det;
	}
	//------------------------------------------------------------------------------------------------------------------
	private static String SELECT_ORDER_DET 	= "select det.* from " 
												+ DefDBExt.TA_SOR_ORDER + " ord inner join " + DefDBExt.TA_SOR_ORDER_DETAIL + " det  on " 
												+ " ord."+ TaSorOrder.COL_I_ID + " = det." + TaSorOrderDetail.COL_I_SOR_ORDER ; 
			
	private static String COND_ORDER_TYPE 	= " and ord."+ TaSorOrder.COL_I_TYPE_01 			+ " = ";
	private static String COND_MAT_ID 		= " and det."+ TaSorOrderDetail.COL_I_MAT_MATERIAL 	+ " = ";
	private static String ORD_DET_ID 		= " order by det." + TaSorOrderDetail.COL_I_ID	+ " desc";
	
	public static List<TaSorOrderDetail> reqList(Integer ordTyp, Integer matId, Integer lim) throws Exception{
		String sql = SELECT_ORDER_DET;
		if (ordTyp!=null) 	sql += COND_ORDER_TYPE 	+ ordTyp;
		if (matId!=null) 	sql += COND_MAT_ID 		+ matId;
		
		sql+= ORD_DET_ID;
		
		if (lim==null) lim =10;
		
		List<TaSorOrderDetail> dets = TaSorOrderDetail.DAO.reqList(0,lim,sql);
		
		return dets;
	}
	
}
