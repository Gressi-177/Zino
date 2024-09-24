package com.hnv.db.job;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hnv.api.main.Hnv_CfgHibernate;
import com.hnv.db.EntityAbstract;
import com.hnv.db.EntityDAO;
import com.hnv.def.DefDBExt;		

/**
* TaJobReportDetail by H&V SAS
*/
@Entity
@Table(name = DefDBExt.TA_JOB_REPORT_DETAIL )
public class TaJobReportDetail extends EntityAbstract<TaJobReportDetail> {

	private static final long serialVersionUID = 1L;

	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                         =	"I_ID";
	public static final String	COL_I_TPY_CATEGORY               =	"I_Tpy_Category";
	public static final String	COL_I_JOB_REPORT                 =	"I_Job_Report";
	public static final String	COL_F_VAL_01                     =	"F_Val_01";
	public static final String	COL_F_VAL_02                     =	"F_Val_02";
	public static final String	COL_F_VAL_03                     =	"F_Val_03";
	public static final String	COL_F_VAL_04                     =	"F_Val_04";
	public static final String	COL_F_VAL_05                     =	"F_Val_05";
	public static final String	COL_F_VAL_06                     =	"F_Val_06";
	public static final String	COL_F_VAL_07                     =	"F_Val_07";
	public static final String	COL_F_VAL_08                     =	"F_Val_08";
	public static final String	COL_F_VAL_09                     =	"F_Val_09";
	public static final String	COL_F_VAL_10                     =	"F_Val_10";
	public static final String	COL_F_VAL_11                     =	"F_Val_11";
	public static final String	COL_F_VAL_12                     =	"F_Val_12";
	public static final String	COL_F_VAL_13                     =	"F_Val_13";
	public static final String	COL_F_VAL_14                     =	"F_Val_14";
	public static final String	COL_F_VAL_15                     =	"F_Val_15";
	public static final String	COL_F_VAL_16                     =	"F_Val_16";
	public static final String	COL_F_VAL_17                     =	"F_Val_17";
	public static final String	COL_F_VAL_18                     =	"F_Val_18";
	public static final String	COL_F_VAL_19                     =	"F_Val_19";
	public static final String	COL_F_VAL_20                     =	"F_Val_20";
	public static final String	COL_F_VAL_21                     =	"F_Val_21";
	public static final String	COL_F_VAL_22                     =	"F_Val_22";
	public static final String	COL_F_VAL_23                     =	"F_Val_23";
	public static final String	COL_F_VAL_24                     =	"F_Val_24";
	public static final String	COL_F_VAL_25                     =	"F_Val_25";
	public static final String	COL_F_VAL_26                     =	"F_Val_26";
	public static final String	COL_F_VAL_27                     =	"F_Val_27";
	public static final String	COL_F_VAL_28                     =	"F_Val_28";
	public static final String	COL_F_VAL_29                     =	"F_Val_29";
	public static final String	COL_F_VAL_30                     =	"F_Val_30";
	public static final String	COL_F_VAL_31                     =	"F_Val_31";



	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                         =	"I_ID";
	public static final String	ATT_I_TPY_CATEGORY               =	"I_Tpy_Category";
	public static final String	ATT_I_JOB_REPORT                 =	"I_Job_Report";
	public static final String	ATT_F_VAL_01                     =	"F_Val_01";
	public static final String	ATT_F_VAL_02                     =	"F_Val_02";
	public static final String	ATT_F_VAL_03                     =	"F_Val_03";
	public static final String	ATT_F_VAL_04                     =	"F_Val_04";
	public static final String	ATT_F_VAL_05                     =	"F_Val_05";
	public static final String	ATT_F_VAL_06                     =	"F_Val_06";
	public static final String	ATT_F_VAL_07                     =	"F_Val_07";
	public static final String	ATT_F_VAL_08                     =	"F_Val_08";
	public static final String	ATT_F_VAL_09                     =	"F_Val_09";
	public static final String	ATT_F_VAL_10                     =	"F_Val_10";
	public static final String	ATT_F_VAL_11                     =	"F_Val_11";
	public static final String	ATT_F_VAL_12                     =	"F_Val_12";
	public static final String	ATT_F_VAL_13                     =	"F_Val_13";
	public static final String	ATT_F_VAL_14                     =	"F_Val_14";
	public static final String	ATT_F_VAL_15                     =	"F_Val_15";
	public static final String	ATT_F_VAL_16                     =	"F_Val_16";
	public static final String	ATT_F_VAL_17                     =	"F_Val_17";
	public static final String	ATT_F_VAL_18                     =	"F_Val_18";
	public static final String	ATT_F_VAL_19                     =	"F_Val_19";
	public static final String	ATT_F_VAL_20                     =	"F_Val_20";
	public static final String	ATT_F_VAL_21                     =	"F_Val_21";
	public static final String	ATT_F_VAL_22                     =	"F_Val_22";
	public static final String	ATT_F_VAL_23                     =	"F_Val_23";
	public static final String	ATT_F_VAL_24                     =	"F_Val_24";
	public static final String	ATT_F_VAL_25                     =	"F_Val_25";
	public static final String	ATT_F_VAL_26                     =	"F_Val_26";
	public static final String	ATT_F_VAL_27                     =	"F_Val_27";
	public static final String	ATT_F_VAL_28                     =	"F_Val_28";
	public static final String	ATT_F_VAL_29                     =	"F_Val_29";
	public static final String	ATT_F_VAL_30                     =	"F_Val_30";
	public static final String	ATT_F_VAL_31                     =	"F_Val_31";



	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static	final boolean				API_CACHE 	= false;
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<TaJobReportDetail> 	DAO;
	static{
		DAO = new EntityDAO<TaJobReportDetail>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), TaJobReportDetail.class,RIGHTS);
	}

	//-----------------------Class Attributs-------------------------
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;
         
	@Column(name=COL_I_TPY_CATEGORY, nullable = true)
	private	Integer         I_Tpy_Category;


	@Column(name=COL_I_JOB_REPORT, nullable = true)
	private	Integer         I_Job_Report;
     
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

	@Column(name=COL_F_VAL_11, nullable = true)
	private	Double          F_Val_11;

	@Column(name=COL_F_VAL_12, nullable = true)
	private	Double          F_Val_12;

	@Column(name=COL_F_VAL_13, nullable = true)
	private	Double          F_Val_13;

	@Column(name=COL_F_VAL_14, nullable = true)
	private	Double          F_Val_14;

	@Column(name=COL_F_VAL_15, nullable = true)
	private	Double          F_Val_15;

	@Column(name=COL_F_VAL_16, nullable = true)
	private	Double          F_Val_16;

	@Column(name=COL_F_VAL_17, nullable = true)
	private	Double          F_Val_17;

	@Column(name=COL_F_VAL_18, nullable = true)
	private	Double          F_Val_18;

	@Column(name=COL_F_VAL_19, nullable = true)
	private	Double          F_Val_19;

	@Column(name=COL_F_VAL_20, nullable = true)
	private	Double          F_Val_20;

	@Column(name=COL_F_VAL_21, nullable = true)
	private	Double          F_Val_21;

	@Column(name=COL_F_VAL_22, nullable = true)
	private	Double          F_Val_22;

	@Column(name=COL_F_VAL_23, nullable = true)
	private	Double          F_Val_23;

	@Column(name=COL_F_VAL_24, nullable = true)
	private	Double          F_Val_24;

	@Column(name=COL_F_VAL_25, nullable = true)
	private	Double          F_Val_25;

	@Column(name=COL_F_VAL_26, nullable = true)
	private	Double          F_Val_26;

	@Column(name=COL_F_VAL_27, nullable = true)
	private	Double          F_Val_27;

	@Column(name=COL_F_VAL_28, nullable = true)
	private	Double          F_Val_28;

	@Column(name=COL_F_VAL_29, nullable = true)
	private	Double          F_Val_29;

	@Column(name=COL_F_VAL_30, nullable = true)
	private	Double          F_Val_30;

	@Column(name=COL_F_VAL_31, nullable = true)
	private	Double          F_Val_31;


    
	//-----------------------Transient Variables-------------------------


	//---------------------Constructeurs-----------------------
	private TaJobReportDetail(){}

	public TaJobReportDetail(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		//doInitDBFlag();
	}
	
	public TaJobReportDetail(Date D_Date, Integer I_Tpy_Category_01, Integer I_Tpy_Category_02, Integer I_Report, String T_Comment, Double F_Val_01, Double F_Val_02, Double F_Val_03, Double F_Val_04, Double F_Val_05, Double F_Val_06, Double F_Val_07, Double F_Val_08, Double F_Val_09, Double F_Val_10, Double F_Val_11, Double F_Val_12, Double F_Val_13, Double F_Val_14, Double F_Val_15, Double F_Val_16, Double F_Val_17, Double F_Val_18, Double F_Val_19, Double F_Val_20, Double F_Val_21, Double F_Val_22, Double F_Val_23, Double F_Val_24, Double F_Val_25, Double F_Val_26, Double F_Val_27, Double F_Val_28, Double F_Val_29, Double F_Val_30, Double F_Val_31) throws Exception {
		this.reqSetAttr(
			ATT_I_TPY_CATEGORY      , I_Tpy_Category_01,
			ATT_I_JOB_REPORT       , I_Report,
			ATT_F_VAL_01          , F_Val_01,
			ATT_F_VAL_02          , F_Val_02,
			ATT_F_VAL_03          , F_Val_03,
			ATT_F_VAL_04          , F_Val_04,
			ATT_F_VAL_05          , F_Val_05,
			ATT_F_VAL_06          , F_Val_06,
			ATT_F_VAL_07          , F_Val_07,
			ATT_F_VAL_08          , F_Val_08,
			ATT_F_VAL_09          , F_Val_09,
			ATT_F_VAL_10          , F_Val_10,
			ATT_F_VAL_11          , F_Val_11,
			ATT_F_VAL_12          , F_Val_12,
			ATT_F_VAL_13          , F_Val_13,
			ATT_F_VAL_14          , F_Val_14,
			ATT_F_VAL_15          , F_Val_15,
			ATT_F_VAL_16          , F_Val_16,
			ATT_F_VAL_17          , F_Val_17,
			ATT_F_VAL_18          , F_Val_18,
			ATT_F_VAL_19          , F_Val_19,
			ATT_F_VAL_20          , F_Val_20,
			ATT_F_VAL_21          , F_Val_21,
			ATT_F_VAL_22          , F_Val_22,
			ATT_F_VAL_23          , F_Val_23,
			ATT_F_VAL_24          , F_Val_24,
			ATT_F_VAL_25          , F_Val_25,
			ATT_F_VAL_26          , F_Val_26,
			ATT_F_VAL_27          , F_Val_27,
			ATT_F_VAL_28          , F_Val_28,
			ATT_F_VAL_29          , F_Val_29,
			ATT_F_VAL_30          , F_Val_30,
			ATT_F_VAL_31          , F_Val_31
		);
		//doInitDBFlag();
	}
	
	
	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}

	@Override
	public void doMergeWith(TaJobReportDetail ent) {
		if (ent == this) return;
		this.I_Tpy_Category    = ent.I_Tpy_Category;
		this.F_Val_01          = ent.F_Val_01;
		this.F_Val_02          = ent.F_Val_02;
		this.F_Val_03          = ent.F_Val_03;
		this.F_Val_04          = ent.F_Val_04;
		this.F_Val_05          = ent.F_Val_05;
		this.F_Val_06          = ent.F_Val_06;
		this.F_Val_07          = ent.F_Val_07;
		this.F_Val_08          = ent.F_Val_08;
		this.F_Val_09          = ent.F_Val_09;
		this.F_Val_10          = ent.F_Val_10;
		this.F_Val_11          = ent.F_Val_11;
		this.F_Val_12          = ent.F_Val_12;
		this.F_Val_13          = ent.F_Val_13;
		this.F_Val_14          = ent.F_Val_14;
		this.F_Val_15          = ent.F_Val_15;
		this.F_Val_16          = ent.F_Val_16;
		this.F_Val_17          = ent.F_Val_17;
		this.F_Val_18          = ent.F_Val_18;
		this.F_Val_19          = ent.F_Val_19;
		this.F_Val_20          = ent.F_Val_20;
		this.F_Val_21          = ent.F_Val_21;
		this.F_Val_22          = ent.F_Val_22;
		this.F_Val_23          = ent.F_Val_23;
		this.F_Val_24          = ent.F_Val_24;
		this.F_Val_25          = ent.F_Val_25;
		this.F_Val_26          = ent.F_Val_26;
		this.F_Val_27          = ent.F_Val_27;
		this.F_Val_28          = ent.F_Val_28;
		this.F_Val_29          = ent.F_Val_29;
		this.F_Val_30          = ent.F_Val_30;
		this.F_Val_31          = ent.F_Val_31;
		//---------------------Merge Transient Variables if exist-----------------------
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;
		
		ok = (I_ID == ((TaJobReportDetail)o).I_ID);
		if (!ok) return ok;

				
		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}
}
