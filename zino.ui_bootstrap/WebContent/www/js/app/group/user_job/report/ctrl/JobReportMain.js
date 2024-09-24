define([
	'jquery',

	'text!group/user_job/report/tmpl/JobReport_Main.html',
	'text!group/user_job/report/tmpl/Job_Report_Config_Working_Day_User.html',

	'group/user_job/report/ctrl/JobReportList',
	'group/user_job/report/ctrl/JobReportEnt',
	'group/user_job/report/ctrl/JobReportEntHeader',
	'group/user_job/report/ctrl/JobReportEntBtn',
	'group/user_job/report/ctrl/JobReportEntTabs',       
	'group/user_job/report/ctrl/JobReportEntTabReportDetail',
	'group/user_job/report/ctrl/JobReportEntTabDoc',
	'group/user_job/report/ctrl/JobReportDateTime',
	'group/user_job/report/ctrl/JobReportRights'

	],
	function($,         		
			JobReport_Main,
			Job_Report_Config_Working_Day_User,

			JobReportList, 
			JobReportEnt, 
			JobReportEntHeader, 
			JobReportEntBtn, 
			JobReportEntTabs, 
			JobReportEntTabReportDetail,
			JobReportEntTabDoc,
			JobReportDateTime,
			JobReportRights
	) {

	var JobReportMain 	= function (grpName, header, content, footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;

		//------------------------------------------------------------------------------------
		var tmplName				= App.template.names;
		var tmplCtrl				= App.template.controller;

		var svClass 				= App['const'].SV_CLASS;
		var svName					= App['const'].SV_NAME;
		var sessId					= App['const'].SESS_ID;
		var userId          		= App['const'].USER_ID;

		var fVar					= App['const'].FUNCT_SCOPE;
		var fName					= App['const'].FUNCT_NAME;
		var fParam					= App['const'].FUNCT_PARAM;		

		var self 					= this;		
		//------------------------------------------------------------------------------------

//		RIGHT ACTION----------------------------------------
		var RIGHT_U_G		= 2002001;
		var RIGHT_U_N		= 2002002;
		var RIGHT_U_M		= 2002003;
		var RIGHT_U_D		= 2002004;

		var RIGHT_A_G		= 2002011;
		var RIGHT_A_N		= 2002012;
		var RIGHT_A_M		= 2002013;
		var RIGHT_A_D		= 2002014;
		
		this.var_lc_MODE_INIT 		= 0;
		this.var_lc_MODE_NEW 		= 1; //duplicate is the mode new after clone object
		this.var_lc_MODE_MOD 		= 2;
		this.var_lc_MODE_DEL 		= 3;	
		this.var_lc_MODE_SEL 		= 5;

		var pr_ctr_Main 			= null;
		var pr_ctr_Ent				= null;
		var pr_ctr_List				= null;
		var pr_ctr_DateTime 		= null;

		

		var pr_SERVICE_CLASS		= "ServiceJobReport"; //to change by your need

		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){		
			tmplName.JOB_REPORT_MAIN			= "JobReport_Main";
			tmplName.JOB_REPORT_LIST			= "JobReport_List";

			tmplName.JOB_REPORT_ENT				= "JobReport_Ent";
			tmplName.JOB_REPORT_ENT_BTN			= "JobReport_EntBtn";
			tmplName.JOB_REPORT_ENT_HEADER		= "JobReport_EntHeader";
			tmplName.JOB_REPORT_ENT_TABS		= "JobReport_EntTabs";

			tmplName.JOB_REPORT_ENT_HEADER_USER_SUMMARY 		= "JobReport_Ent_Header_User_Summary";

			tmplName.JOB_REPORT_LIST_REPORT_PENDING_HEADER		= "JobReport_List_Pending_Header";
			tmplName.JOB_REPORT_LIST_REPORT_PENDING_CONTENT		= "JobReport_List_Pending_Content";
			tmplName.JOB_REPORT_LIST_REPORT_VALIDATED_HEADER	= "JobReport_List_Validated_Header";
			tmplName.JOB_REPORT_LIST_REPORT_VALIDATED_CONTENT	= "JobReport_List_Validated_Content";
			tmplName.JOB_REPORT_LIST_REPORT_BY_USER_HEADER		= "JobReport_List_By_User_Header";
			tmplName.JOB_REPORT_LIST_REPORT_BY_USER_CONTENT		= "JobReport_List_By_User_Content";
			tmplName.JOB_REPORT_LIST_USER_HEADER				= "JobReport_List_User_Header";
			tmplName.JOB_REPORT_LIST_USER_CONTENT				= "JobReport_List_User_Content";

			tmplName.JOB_REPORT_ENT_TAB_REPORT_DETAIL		= "JobReport_EntTabReportDetail";
			tmplName.JOB_REPORT_ENT_TAB_DOC					= "JobReport_EntTabDoc";
			tmplName.JOB_REPORT_DETAIL_ACTION				= "JobReport_Detail_Action";

			tmplName.JOB_REPORT_CONFIG_WORKING_DAY			= "JobReport_Config_Working_Day";
			tmplName.JOB_REPORT_CONFIG_WORKING_DAY_USER		= "JobReport_Config_Working_Day_User";
			tmplName.JOB_REPORT_DISPLAY_QR_CODE				= "JobReport_Display_QR_Code";

			if (!App.controller.JobReport)				
				App.controller.JobReport				= {};

			if (!App.controller.JobReport.Main)	
				App.controller.JobReport.Main 			= this; //important for other controller can get ref, when new this controller,

			if (!App.controller.JobReport.List			)  
				App.controller.JobReport.List			= new JobReportList				();				
			if (!App.controller.JobReport.Ent			)  
				App.controller.JobReport.Ent			= new JobReportEnt				(null, "#div_JobReport_Ent", null);
			if (!App.controller.JobReport.EntBtn		)  
				App.controller.JobReport.EntBtn		= new JobReportEntBtn			(null, "#div_JobReport_Ent_Btn", null);
			if (!App.controller.JobReport.EntHeader	)  
				App.controller.JobReport.EntHeader		= new JobReportEntHeader		(null, "#div_JobReport_Ent_Header", null);
			if (!App.controller.JobReport.EntTabs		)  
				App.controller.JobReport.EntTabs		= new JobReportEntTabs			(null, "#div_JobReport_Ent_Tabs", null);
			if (!App.controller.JobReport.DateTime		)  
				App.controller.JobReport.DateTime		= new JobReportDateTime			(null, null, null);
			if (!App.controller.JobReport.Rights)	
				App.controller.JobReport.Rights 		= new JobReportRights			();
			//----------tab name----------------------------------------------------------------------------------------
			if (!App.controller.JobReport.EntTabReportDetail)  
				App.controller.JobReport.EntTabReportDetail= new JobReportEntTabReportDetail(null, "#div_JobReport_Ent_Tab_Report_Detail", null);
			if (!App.controller.JobReport.EntTabDoc)  
				App.controller.JobReport.EntTabDoc			= new JobReportEntTabDoc(null, "#div_JobReport_Ent_Tab_Doc", null);
			//--------------------------------------------------------------------------------------------------

			self.var_lc_URL_Aut_Header				= req_gl_LS_SecurityHeaderBearer (App.keys.KEY_STORAGE_CREDENTIAL);

			App.controller.JobReport.List					.do_lc_init();
			App.controller.JobReport.Ent					.do_lc_init();
			App.controller.JobReport.EntBtn					.do_lc_init();
			App.controller.JobReport.EntHeader				.do_lc_init();
			App.controller.JobReport.EntTabs				.do_lc_init();
			App.controller.JobReport.EntTabReportDetail		.do_lc_init();
			App.controller.JobReport.EntTabDoc				.do_lc_init();

			pr_ctr_Main 	= App.controller.JobReport.Main;
			pr_ctr_Ent		= App.controller.JobReport.Ent;
			pr_ctr_List		= App.controller.JobReport.List;
			pr_ctr_DateTime = App.controller.JobReport.DateTime;

			do_get_category_data();
			do_get_report_by_user();

			App.controller.JobReport.DateTime.getcurrentReportCode();
//			App.controller.JobReport.DateTime.getcurrentAndPrevReportCode();
			App.controller.JobReport.DateTime.getReportCode();

			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_REPORT_MAIN	, JobReport_Main); 
			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_REPORT_CONFIG_WORKING_DAY_USER, Job_Report_Config_Working_Day_User);
		}

		this.do_lc_show = function(){
			try {
				let rightCode = pr_ctr_Rights.req_lc_Right(RIGHT_U_G);
				if(rightCode == -1){
					do_gl_show_Notify_Msg_Error($.i18n("job_report_msg_user_right_error"));
					return;
				}
				
				self.var_lc_URL_Aut_Header				= req_gl_LS_SecurityHeaderBearer (App.keys.KEY_STORAGE_CREDENTIAL);
				doBindings();
				$(pr_divContent).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_REPORT_MAIN, {}));	

				do_get_info_user();

				App.controller.JobReport.List	.do_lc_show();	
				App.controller.JobReport.Ent	.do_lc_show(null);	//init: obj is null	

				do_gl_init_Resizable("#div_JobReport_List");
				App.controller.UI.Main.do_bind_event_btn_vertical_list('#div_JobReport_List', '#div_JobReport_Ent');
				App.controller.UI.Main.do_lc_bind_event_resize();
				App.controller.UI.Main.do_lc_bind_event_minimize('#div_JobReport_List', '#div_JobReport_Ent');
			}catch(e) {
				do_gl_show_Notify_Msg_Error("JobReport: Main :" + e.toString());
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, self.var_lc_URL_Aut_Header, App.network, "job.report", "JobReportMain", "do_lc_show", e.toString()) ;
			}

		};

		this.do_lc_binding_pages = function(div, option) {
			try {
				if(div.length>0) do_gl_enhance_within(div, option);
			} catch (e) {
				console.log(e);
				do_gl_show_Notify_Msg_Error(null, e);
			}
		};

		//---------------------------------------------------------------------------------------------
		//GET CATEGORY DATA-----GET CATEGORY DATA-----GET CATEGORY DATA-----GET CATEGORY DATA-----GET C
		//---------------------------------------------------------------------------------------------

		var doBindings = function() {
			do_gl_bindingAppLTE();
		}.bind(this);

		function do_get_category_data(){
			var ref 		= req_gl_Request_Content_Send('SVJobReportLstCat');
			var fSucces		= [];
			fSucces.push	( req_gl_funct(this, do_create_source_category, [true] ));
			var fError 		= req_gl_funct(this, do_create_source_category, [false]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, self.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError);
		}

		var do_create_source_category = function(sharedJson, ajaxStat){
			if(ajaxStat){
				var code = sharedJson[App['const'].SV_CODE];
				if(code == App['const'].SV_CODE_API_YES) {
					var lstData = sharedJson.res_data;
					var lstCat  = [];

					for(var i = 0; i < lstData.length; i++){
						var data = lstData[i];
						if(data.typ01 == 1){  //List Project
							var data = {};
							data.catId01		= lstData[i].id;
							data.catId01_Select = lstData[i].name;
							lstCat.push(data);
						}
						if(data.typ01 == 2){  //Hld rate values
							App.data.hldRate = data.code;
						}
					}
					var do1 = {};
					do1.catId01			= -1;
					do1.catId01_Select  = $.i18n("job_report_dayoff_cp");
					lstCat.push(do1);
					var do2 = {};
					do2.catId01			= -2
					do2.catId01_Select 	= $.i18n("job_report_dayoff_aa");
					lstCat.push(do2);
					App.data.lstCat = lstCat;
				} else {
					do_gl_show_Notify_Msg_Error($.i18n("Error or category has nothing: SV Code:" + code));
				}
			} else {
				do_gl_show_Notify_Msg_Error($.i18n("common_err_ajax"));
			}
		}

		//---------------------------------------------------------------------------------------------
		//GET REPORT USER-----GET REPORT USER----GET REPORT USER-----GET REPORT USER-----GET REPORT USER-----GET
		//---------------------------------------------------------------------------------------------

		var do_get_report_by_user = function (){
			var ref 		= req_gl_Request_Content_Send('SVJobReportLstByUser');
			var fSucces		= [];
			fSucces.push(req_gl_funct(this, do_reponse_list_report, [true]));
			var fError 		= req_gl_funct(this, do_create_info_user, [false]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, self.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError);
		}

		var do_reponse_list_report = function(sharedJson, ajaxStat){
			if(ajaxStat){
				var code = sharedJson[App['const'].SV_CODE];
				if(code == App['const'].SV_CODE_API_YES) {
					App.data.lstReport = [];
					App.data.lstReport = sharedJson.res_data;
				} else {
					do_gl_show_Notify_Msg_Error($.i18n("Error do_get_report_by_user: SV Code:" + code));
				}
			} else {
				do_gl_show_Notify_Msg_Error($.i18n("common_err_ajax"));
			}
		}

		//---------------------------------------------------------------------------------------------
		//GET INFO USER-----GET INFO USER-----GET INFO USER-----GET INFO USER-----GET INFO USER-----GET
		//---------------------------------------------------------------------------------------------

		function do_get_info_user(){
			var ref 		= req_gl_Request_Content_Send('SVJobReportInfoUser');
			ref["uId02"] 	= App.data.user.id;
			ref["codeRp"]  	= App.data.currentReportCode;
			var fSucces		= [];
			fSucces.push(req_gl_funct(this, do_create_info_user, [true]));
			var fError 		= req_gl_funct(this, do_create_info_user, [false]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, self.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError);
		}

		var do_create_info_user = function(sharedJson, ajaxStat){
			App.data.lstUser = [];
			App.data.selectedUser = null;
			if(ajaxStat){
				var code = sharedJson[App['const'].SV_CODE];
				if(code == App['const'].SV_CODE_API_YES) {
					App.data.selectedUser = sharedJson.res_data;
					if(App.data.selectedUser.wdConfig == null){
//						pr_ctr_List.do_active_msg_config_user();
						do_lc_create_wdConfig_defaut();
					}else{
						App.data.lstUser.push(App.data.selectedUser);
						pr_ctr_DateTime.getInfoDayOff(App.data.currentReportCode, App.data.user.id);
						pr_ctr_Ent. do_lc_show_user_summary(App.data.selectedUser);
					}
					
				} else {
					do_gl_show_Notify_Msg_Error($.i18n("Error do_create_info_user: SV Code:" + code));
				}
			} else {
				do_gl_show_Notify_Msg_Error($.i18n("common_err_ajax"));
			}
		}
		
		const do_lc_create_wdConfig_defaut = () => {
			var ref		= {};
			ref 		= req_gl_Request_Content_Send("SVJobReportWorkingDate");
			ref["uId02"]= App.data.user.id;
			ref["wD"]	= "AFFFFFF"; //work t2 => t7
			ref["wH"]	= "00000000070018000700180007001800070018000700180007001800";// work 7h-18h00/day

			var fSucces		= [];
			fSucces.push	( req_gl_funct(this, do_lc_show_after_create_default, [true] ));
			var fError 		= req_gl_funct(this, do_lc_show_after_create_default, [false]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}
		
		var do_lc_show_after_create_default = function(sharedJson, ajaxStat){
			if(ajaxStat){
				var code = sharedJson[App['const'].SV_CODE];
				if(code == App['const'].SV_CODE_API_YES ) {
					App.data.selectedUser  =  sharedJson.res_data;
					App.data.lstUser.push(App.data.selectedUser);
					pr_ctr_DateTime.getInfoDayOff(App.data.currentReportCode, App.data.user.id);
					pr_ctr_Ent. do_lc_show_user_summary(App.data.selectedUser);
				} else {
					do_gl_show_Notify_Msg_Error($.i18n("Error update_working_date: SV Code:" + code));
				}
			} else {
				do_gl_show_Notify_Msg_Error($.i18n("common_err_ajax"));
			}
		}
		

		function req_gl_Request_Content_Send(serviceName){
			var ref 		= {};
			ref[svClass] 	= pr_SERVICE_CLASS;
			ref[svName]		= serviceName;
			ref[userId]		= App.data.user.id;
			ref[sessId]		= App.data.session_id;		

			return ref;
		}
	}

	return JobReportMain;
});