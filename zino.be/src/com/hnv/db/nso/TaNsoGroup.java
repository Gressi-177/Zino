package com.hnv.db.nso;

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

import com.hnv.api.main.Hnv_CfgHibernate;
import com.hnv.data.json.JSONObject;
import com.hnv.db.EntityAbstract;
import com.hnv.db.EntityDAO;
import com.hnv.db.tpy.TaTpyDocument;
import com.hnv.def.DefDBExt;		

/**
* TaNsoGroup by H&V SAS
*/
@Entity
@Table(name = DefDBExt.TA_NSO_GROUP )
public class TaNsoGroup extends EntityAbstract<TaNsoGroup> {

	private static final long serialVersionUID 		= 1L;
	private static final int  ENT_TYP				= DefDBExt.ID_TA_NSO_GROUP;
	
	public static final int		STAT_01_NEW 		= 0;
	public static final int		STAT_01_ACTIVE 		= 1;
	public static final int		STAT_01_REVIEW 		= 5;
	public static final int		STAT_01_DELETED 	= 10;
	
	public static final int TYP_02_PRIVATE			= 401;
	public static final int TYP_02_PUBLIC			= 402;

	public static final int TYP_01_MEETING  		= 100;
	public static final int TYP_01_WORK  			= 300;
	public static final int TYP_01_ROOM_MULTI		= 201;
	public static final int TYP_01_ROOM_SINGLE		= 200;
	public static final int TYP_01_EMAIL  			= 500;
	public static final int TYP_01_PLANNING  		= 600;
	
	public static final int STAT_EMAIL_NEW			= 1;
	public static final int STAT_EMAIL_VALIDATE		= 2;
	public static final int STAT_EMAIL_WAIT_DEL  	= 3;
	
	//---------------------------List of Column from DB-----------------------------
	public static final String	COL_I_ID                              =	"I_ID";
	public static final String	COL_T_REF                             =	"T_Ref";
	public static final String	COL_T_NAME                            =	"T_Name";
	
	public static final String	COL_D_DATE_01                         =	"D_Date_01";
	public static final String	COL_D_DATE_02                         =	"D_Date_02";
	public static final String	COL_I_STATUS_01                       =	"I_Status_01";
	public static final String	COL_I_STATUS_02                       =	"I_Status_02";
	public static final String	COL_I_TYPE_01                         =	"I_Type_01";
	public static final String	COL_I_TYPE_02                         =	"I_Type_02";
	public static final String	COL_T_INFO_01                         =	"T_Info_01";
	public static final String	COL_T_INFO_02                         =	"T_Info_02";
	public static final String	COL_T_VAL_01                          =	"T_Val_01";
	public static final String	COL_T_VAL_02                          =	"T_Val_02";
	
	public static final String	COL_I_AUT_USER                        =	"I_Aut_User";
	public static final String	COL_I_PER_MANAGER                     =	"I_Per_Manager";

	//---------------------------List of ATTR of class-----------------------------
	public static final String	ATT_I_ID                              =	"I_ID";
	public static final String	ATT_T_REF                             =	"T_Ref";
	public static final String	ATT_T_NAME                            =	"T_Name";
	
	public static final String	ATT_D_DATE_01                         =	"D_Date_01";
	public static final String	ATT_D_DATE_02                         =	"D_Date_02";
	public static final String	ATT_I_STATUS_01                       =	"I_Status_01";
	public static final String	ATT_I_STATUS_02                       =	"I_Status_02";
	public static final String	ATT_I_TYPE_01                         =	"I_Type_01";
	public static final String	ATT_I_TYPE_02                         =	"I_Type_02";
	public static final String	ATT_T_INFO_01                         =	"T_Info_01";
	public static final String	ATT_T_INFO_02                         =	"T_Info_02";
	public static final String	ATT_T_VAL_01                          =	"T_Val_01";
	public static final String	ATT_T_VAL_02                          =	"T_Val_02";
	
	public static final String	ATT_I_AUT_USER                        =	"I_Aut_User";
	public static final String	ATT_I_PER_MANAGER                     =	"I_Per_Manager";
	
	public static final String	ATT_O_AVATAR                          =	"O_Avatar";
	public static final String	ATT_O_DOCUMENTS                       =	"O_Documents";
	
	public static final String	ATT_O_MEMBERS                         =	"O_Members";
	
	//-------every entity class must initialize its DAO from here -----------------------------
	private 	static 	final boolean[] 			RIGHTS		= {true, true, true, true, false}; //canRead, canAdd, canUpd, canDel, del physique or flag only 
	private 	static	final boolean				API_CACHE 	= false;
	private 	static 	final boolean[]				HISTORY		= {false, false, false}; //add, mod, del

	public		static 	final EntityDAO<TaNsoGroup> 	DAO ;
	static{
		DAO = new EntityDAO<TaNsoGroup>(Hnv_CfgHibernate.reqFactoryEMSession(Hnv_CfgHibernate.ID_FACT_MAIN), TaNsoGroup.class,RIGHTS);
	}

	//-----------------------Class Attributs-------------------------
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=COL_I_ID, nullable = false)
	private	Integer         I_ID;
         
	@Column(name=COL_T_REF, nullable = true)
	private	String          T_Ref;
        
	@Column(name=COL_T_NAME, nullable = true)
	private	String          T_Name;
       
	@Column(name=COL_D_DATE_01, nullable = true)
	private	Date            D_Date_01;
    
	@Column(name=COL_D_DATE_02, nullable = true)
	private	Date            D_Date_02;
    
	@Column(name=COL_I_STATUS_01, nullable = true)
	private	Integer         I_Status_01;
	
	@Column(name=COL_I_STATUS_02, nullable = true)
	private	Integer         I_Status_02;
     
	@Column(name=COL_I_TYPE_01, nullable = true)
	private	Integer         I_Type_01;
    
	@Column(name=COL_I_TYPE_02, nullable = true)
	private	Integer         I_Type_02;
    
	@Column(name=COL_I_PER_MANAGER, nullable = true)
	private	Integer         I_Per_Manager;
	
	@Column(name=COL_T_INFO_01, nullable = true)
	private	String          T_Info_01;
    
	@Column(name=COL_T_INFO_02, nullable = true)
	private	String          T_Info_02;
	
	@Column(name=COL_T_VAL_01, nullable = true)
	private	String          T_Val_01;
	
	@Column(name=COL_T_VAL_02, nullable = true)
	private	String          T_Val_02;

	@Column(name=COL_I_AUT_USER, nullable = true)
	private	Integer        I_Aut_User;
	//-----------------------Transient Variables-------------------------
	@Transient
	private TaTpyDocument			O_Avatar;

	@Transient
	private List<TaTpyDocument>		O_Documents;
	
	
	@Transient
	private List<TaNsoGroupMember>	O_Members;
	//---------------------Constructeurs-----------------------
	private TaNsoGroup(){}

	public TaNsoGroup(Map<String, Object> attrs) throws Exception {
		this.reqSetAttrFromMap(attrs);
		doInitDBFlag();
	}
	
	public TaNsoGroup (String T_Ref, Integer I_Type_01) {
		this.T_Ref = T_Ref;
		this.I_Type_01 = I_Type_01;
	}
	
	
	//---------------------EntityInterface-----------------------
	@Override
	public Serializable reqRef() {
		return this.I_ID;

	}

	@Override
	public void doMergeWith(TaNsoGroup ent) {
		if (ent == this) return;
		this.T_Ref                  = ent.T_Ref;
		this.T_Name                 = ent.T_Name;
		this.D_Date_01              = ent.D_Date_01;
		this.D_Date_02              = ent.D_Date_02;
		this.I_Status_01            = ent.I_Status_01;
		this.I_Status_02            = ent.I_Status_02;
		this.I_Type_01              = ent.I_Type_01;
		this.I_Type_02              = ent.I_Type_02;
		this.I_Per_Manager          = ent.I_Per_Manager;
		this.T_Info_01              = ent.T_Info_01;
		this.T_Info_02              = ent.T_Info_02;
		this.T_Val_01				= ent.T_Val_01;
		this.T_Val_02				= ent.T_Val_02;
		this.I_Aut_User        		= ent.I_Aut_User;


		//---------------------Merge Transient Variables if exist-----------------------
	}

	@Override
	public boolean equals(Object o)  {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		boolean ok = false;
		
		ok = (I_ID == ((TaNsoGroup)o).I_ID);
		if (!ok) return ok;

				
		if (!ok) return ok;
		return ok;
	}

	@Override
	public int hashCode() {
		return this.I_ID;

	}

	//---khong chung session duoc vi nam trong package khac
	public void doBuildDocuments(boolean forced) throws Exception {
		if (this.O_Documents != null && !forced) return;
		this.O_Documents = TaTpyDocument.reqTpyDocuments(ENT_TYP, I_ID, null, null);
	}
	
	public void doBuildMembers (boolean forced) throws Exception{
		if (this.O_Members != null && !forced) return;
		this.O_Members = TaNsoGroupMember.reqListByGroupId(this.I_ID);
	}
	
	
	public void doAddMember (TaNsoGroupMember mem) throws Exception{
		if (this.O_Members == null) this.O_Members= new ArrayList<TaNsoGroupMember>();
		this.O_Members.add(mem);
	}
	
	public void doBuildMembersWithDate (Date dtBegin, Date dtEnd) throws Exception{
		this.O_Members = TaNsoGroupMember.reqListByGroupIdAndDate(this.I_ID, dtBegin, dtEnd);
	}
	
	public void doBuilAvatarPath()  throws Exception{
		if (this.O_Documents == null) return;
		
		for (TaTpyDocument doc : O_Documents){
			Integer typ01 = doc.reqInt(TaTpyDocument.ATT_I_TYPE_01);
			Integer typ02 = doc.reqInt(TaTpyDocument.ATT_I_TYPE_02);
			if (typ01!=null && typ02!=null 
					&& typ01.equals(TaTpyDocument.TYPE_01_FILE_MEDIA)  
					&& typ02.equals(TaTpyDocument.TYPE_02_FILE_IMG_AVATAR)){
				
				JSONObject 	ava 	= new JSONObject();
				String 		imgPrv 	= doc.reqStr(TaTpyDocument.ATT_T_INFO_05);
				String 		imgRaw 	= doc.reqStr(TaTpyDocument.ATT_T_INFO_03);
				if (imgPrv==null) imgPrv = imgRaw;
				ava.put("img"	, imgPrv);
				ava.put("imgRaw", imgRaw);
				this.T_Val_01 = ava.toJSONString();
				return;
			}
		}
	}
	
}
