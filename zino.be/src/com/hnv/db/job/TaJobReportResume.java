package com.hnv.db.job;

import java.io.Serializable;
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
@Table(name = DefDBExt.TA_JOB_REPORT_RESUME )
public class TaJobReportResume extends EntityAbstract<TaJobReportResume> {

	private static final long serialVersionUID = 1L;

	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                         =	"I_ID";
	public static final String	COL_I_JOB_REPORT                 =	"I_Job_Report";
	public static final String	COL_T_VAL_01                     =	"T_Val_01";
	public static final String	COL_T_VAL_02                     =	"T_Val_02";
	public static final String	COL_T_VAL_03                     =	"T_Val_03";
	public static final String	COL_T_VAL_04                     =	"T_Val_04";
	public static final String	COL_T_VAL_05                     =	"T_Val_05";
	public static final String	COL_T_VAL_06                     =	"T_Val_06";
	public static final String	COL_T_VAL_07                     =	"T_Val_07";
	public static final String	COL_T_VAL_08                     =	"T_Val_08";
	public static final String	COL_T_VAL_09                     =	"T_Val_09";
	public static final String	COL_T_VAL_10                     =	"T_Val_10";
	public static final String	COL_T_VAL_11                     =	"T_Val_11";
	public static final String	COL_T_VAL_12                     =	"T_Val_12";
	public static final String	COL_T_VAL_13                     =	"T_Val_13";
	public static final String	COL_T_VAL_14                     =	"T_Val_14";
	public static final String	COL_T_VAL_15                     =	"T_Val_15";
	public static final String	COL_T_VAL_16                     =	"T_Val_16";
	public static final String	COL_T_VAL_17                     =	"T_Val_17";
	public static final String	COL_T_VAL_18                     =	"T_Val_18";
	public static final String	COL_T_VAL_19                     =	"T_Val_19";
	public static final String	COL_T_VAL_20                     =	"T_Val_20";
	public static final String	COL_T_VAL_21                     =	"T_Val_21";
	public static final String	COL_T_VAL_22                     =	"T_Val_22";
	public static final String	COL_T_VAL_23                     =	"T_Val_23";
	public static final String	COL_T_VAL_24                     =	"T_Val_24";
	public static final String	COL_T_VAL_25                     =	"T_Val_25";
	public static final String	COL_T_VAL_26                     =	"T_Val_26";
	public static final String	COL_T_VAL_27                     =	"T_Val_27";
	public static final String	COL_T_VAL_28                     =	"T_Val_28";
	public static final String	COL_T_VAL_29                     =	"T_Val_29";
	public static final String	COL_T_VAL_30                     =	"T_Val_30";
	public static final String	COL_T_VAL_31                     =	"T_Val_31";



	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                         =	"I_ID";
	public static final String	ATT_I_JOB_REPORT                 =	"I_Job_Report";
	public static final String	ATT_T_VAL_01                     =	"T_Val_01";
	public static final String	ATT_T_VAL_02                     =	"T_Val_02";
	public static final String	ATT_T_VAL_03                     =	"T_Val_03";
	public static final String	ATT_T_VAL_04                     =	"T_Val_04";
	public static final String	ATT_T_VAL_05                     =	"T_Val_05";
	public static final String	ATT_T_VAL_06                     =	"T_Val_06";
	public static final String	ATT_T_VAL_07                     =	"T_Val_07";
	public static final String	ATT_T_VAL_08                     =	"T_Val_08";
	public static final String	ATT_T_VAL_09                     =	"T_Val_09";
	public static final String	ATT_T_VAL_10                     =	"T_Val_10";
	public static final String	ATT_T_VAL_11                     =	"T_Val_11";
	public static final String	ATT_T_VAL_12                     =	"T_Val_12";
	public static final String	ATT_T_VAL_13                     =	"T_Val_13";
	public static final String	ATT_T_VAL_14                     =	"T_Val_14";
	public static final String	ATT_T_VAL_15                     =	"T_Val_15";
	public static final String	ATT_T_VAL_16                     =	"T_Val_16";
	public static final String	ATT_T_VAL_17                     =	"T_Val_17";
	public static final String	ATT_T_VAL_18                     =	"T_Val_18";
	public static final String	ATT_T_VAL_19                     =	"T_Val_19";
	public static final String	ATT_T_VAL_20                     =	"T_Val_20";
	public static final String	ATT_T_VAL_21                     =	"T_Val_21";
	public static final String	ATT_T_VAL_22                     =	"T_Val_22";
	public static final String	ATT_T_VAL_23                     =	"T_Val_23";
	public static final String	ATT_T_VAL_24                     =	"T_Val_24";
	public static final String	ATT_T_VAL_25                     =	"T_Val_25";
	public static final String	ATT_T_VAL_26                     =	"T_Val_26";
	public static final String	ATT_T_VAL_27                     =	"T_Val_27";
	public static final String	ATT_T_VAL_28                     =	"T_Val_28";
	public static final String	ATT_T_VAL_29                     =	"T_Val_29";
	public static final String	ATT_T_VAL_30                     =	"T_Val_30";
	public static final String	ATT_T_VAL_31                     =	"T_Val_31";



	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 

	public		static 	final EntityDAO<TaJobReportResume> 	DAO;
	static{
		DAO = new EntityDAO<TaJobReportResume>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), TaJobReportResume.class,RIGHTS);
	}

	//-----------------------Class Attributs-------------------------
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;
         

	@Column(name=COL_I_JOB_REPORT, nullable = true)
	private	Integer         I_Job_Report;
     
	@Column(name=COL_T_VAL_01, nullable = true)
	private String          T_Val_01;

	@Column(name=COL_T_VAL_02, nullable = true)
	private String          T_Val_02;

	@Column(name=COL_T_VAL_03, nullable = true)
	private String          T_Val_03;

	@Column(name=COL_T_VAL_04, nullable = true)
	private String          T_Val_04;

	@Column(name=COL_T_VAL_05, nullable = true)
	private String          T_Val_05;

	@Column(name=COL_T_VAL_06, nullable = true)
	private String          T_Val_06;

	@Column(name=COL_T_VAL_07, nullable = true)
	private String          T_Val_07;

	@Column(name=COL_T_VAL_08, nullable = true)
	private String          T_Val_08;

	@Column(name=COL_T_VAL_09, nullable = true)
	private String          T_Val_09;

	@Column(name=COL_T_VAL_10, nullable = true)
	private String          T_Val_10;

	@Column(name=COL_T_VAL_11, nullable = true)
	private String          T_Val_11;

	@Column(name=COL_T_VAL_12, nullable = true)
	private String          T_Val_12;

	@Column(name=COL_T_VAL_13, nullable = true)
	private String          T_Val_13;

	@Column(name=COL_T_VAL_14, nullable = true)
	private String          T_Val_14;

	@Column(name=COL_T_VAL_15, nullable = true)
	private String          T_Val_15;

	@Column(name=COL_T_VAL_16, nullable = true)
	private String          T_Val_16;

	@Column(name=COL_T_VAL_17, nullable = true)
	private String          T_Val_17;

	@Column(name=COL_T_VAL_18, nullable = true)
	private String          T_Val_18;

	@Column(name=COL_T_VAL_19, nullable = true)
	private String          T_Val_19;

	@Column(name=COL_T_VAL_20, nullable = true)
	private String          T_Val_20;

	@Column(name=COL_T_VAL_21, nullable = true)
	private String          T_Val_21;

	@Column(name=COL_T_VAL_22, nullable = true)
	private String          T_Val_22;

	@Column(name=COL_T_VAL_23, nullable = true)
	private String          T_Val_23;

	@Column(name=COL_T_VAL_24, nullable = true)
	private String          T_Val_24;

	@Column(name=COL_T_VAL_25, nullable = true)
	private String          T_Val_25;

	@Column(name=COL_T_VAL_26, nullable = true)
	private String          T_Val_26;

	@Column(name=COL_T_VAL_27, nullable = true)
	private String          T_Val_27;

	@Column(name=COL_T_VAL_28, nullable = true)
	private String          T_Val_28;

	@Column(name=COL_T_VAL_29, nullable = true)
	private String          T_Val_29;

	@Column(name=COL_T_VAL_30, nullable = true)
	private String          T_Val_30;

	@Column(name=COL_T_VAL_31, nullable = true)
	private String          T_Val_31;


    
	//-----------------------Transient Variables-------------------------


	//---------------------Constructeurs-----------------------
	private TaJobReportResume(){}

	public TaJobReportResume(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
	}
	
	
	public TaJobReportResume ( Integer I_Report, String T_Val_01, String T_Val_02, String T_Val_03, String T_Val_04, String T_Val_05, String T_Val_06, String T_Val_07, String T_Val_08, String T_Val_09, String T_Val_10, String T_Val_11, String T_Val_12, String T_Val_13, String T_Val_14, String T_Val_15, String T_Val_16, String T_Val_17, String T_Val_18, String T_Val_19, String T_Val_20, String T_Val_21, String T_Val_22, String T_Val_23, String T_Val_24, String T_Val_25, String T_Val_26, String T_Val_27, String T_Val_28, String T_Val_29, String T_Val_30, String T_Val_31) throws Exception {
		this.reqSetAttr(
			ATT_I_JOB_REPORT      , I_Report,
			ATT_T_VAL_01          , T_Val_01,
			ATT_T_VAL_02          , T_Val_02,
			ATT_T_VAL_03          , T_Val_03,
			ATT_T_VAL_04          , T_Val_04,
			ATT_T_VAL_05          , T_Val_05,
			ATT_T_VAL_06          , T_Val_06,
			ATT_T_VAL_07          , T_Val_07,
			ATT_T_VAL_08          , T_Val_08,
			ATT_T_VAL_09          , T_Val_09,
			ATT_T_VAL_10          , T_Val_10,
			ATT_T_VAL_11          , T_Val_11,
			ATT_T_VAL_12          , T_Val_12,
			ATT_T_VAL_13          , T_Val_13,
			ATT_T_VAL_14          , T_Val_14,
			ATT_T_VAL_15          , T_Val_15,
			ATT_T_VAL_16          , T_Val_16,
			ATT_T_VAL_17          , T_Val_17,
			ATT_T_VAL_18          , T_Val_18,
			ATT_T_VAL_19          , T_Val_19,
			ATT_T_VAL_20          , T_Val_20,
			ATT_T_VAL_21          , T_Val_21,
			ATT_T_VAL_22          , T_Val_22,
			ATT_T_VAL_23          , T_Val_23,
			ATT_T_VAL_24          , T_Val_24,
			ATT_T_VAL_25          , T_Val_25,
			ATT_T_VAL_26          , T_Val_26,
			ATT_T_VAL_27          , T_Val_27,
			ATT_T_VAL_28          , T_Val_28,
			ATT_T_VAL_29          , T_Val_29,
			ATT_T_VAL_30          , T_Val_30,
			ATT_T_VAL_31          , T_Val_31
		);
		//doInitDBFlag();
	}
	
	
	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}

	@Override
	public void doMergeWith(TaJobReportResume ent) {
		if (ent == this) return;
		this.I_Job_Report          = ent.I_Job_Report;
		this.T_Val_01          = ent.T_Val_01;
		this.T_Val_02          = ent.T_Val_02;
		this.T_Val_03          = ent.T_Val_03;
		this.T_Val_04          = ent.T_Val_04;
		this.T_Val_05          = ent.T_Val_05;
		this.T_Val_06          = ent.T_Val_06;
		this.T_Val_07          = ent.T_Val_07;
		this.T_Val_08          = ent.T_Val_08;
		this.T_Val_09          = ent.T_Val_09;
		this.T_Val_10          = ent.T_Val_10;
		this.T_Val_11          = ent.T_Val_11;
		this.T_Val_12          = ent.T_Val_12;
		this.T_Val_13          = ent.T_Val_13;
		this.T_Val_14          = ent.T_Val_14;
		this.T_Val_15          = ent.T_Val_15;
		this.T_Val_16          = ent.T_Val_16;
		this.T_Val_17          = ent.T_Val_17;
		this.T_Val_18          = ent.T_Val_18;
		this.T_Val_19          = ent.T_Val_19;
		this.T_Val_20          = ent.T_Val_20;
		this.T_Val_21          = ent.T_Val_21;
		this.T_Val_22          = ent.T_Val_22;
		this.T_Val_23          = ent.T_Val_23;
		this.T_Val_24          = ent.T_Val_24;
		this.T_Val_25          = ent.T_Val_25;
		this.T_Val_26          = ent.T_Val_26;
		this.T_Val_27          = ent.T_Val_27;
		this.T_Val_28          = ent.T_Val_28;
		this.T_Val_29          = ent.T_Val_29;
		this.T_Val_30          = ent.T_Val_30;
		this.T_Val_31          = ent.T_Val_31;



		//---------------------Merge Transient Variables if exist-----------------------
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;
		
		ok = (I_ID == ((TaJobReportResume)o).I_ID);
		if (!ok) return ok;

				
		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}
}
