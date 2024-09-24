define([
        'jquery',
        'text!group/user_job/report/tmpl/JobReport_List.html',
        'text!group/user_job/report/tmpl/JobReport_List_Report_Content.html',
        
//        'text!group/user_job/report/tmpl/JobReport_List_User_Content.html',
        
        'text!group/user_job/report/tmpl/JobReport_Config_Working_Day.html'

        ],
        function($, 
        		JobReport_List,
        		JobReport_List_Report_Content,
        		
//        		JobReport_List_User_Content,
        		
        		JobReport_Config_Working_Day
        ) 
        {

	var JobReportList 	= function () {
		
		var pr_divList 						= "#div_JobReport_List";
		
		var pr_div_Rp_By_User				= "#div_JobReport_List_Report_By_User";
		var pr_div_Rp_By_User_Content		= "#div_JobReport_List_Report_By_User_Content";
		
		var pr_div_Rp_Draft					= "#div_JobReport_List_Report_Draft";
		var pr_div_Rp_Draft_Content			= "#div_JobReport_List_Report_Draft_Content";
		
		var pr_div_Rp_Pending				= "#div_JobReport_List_Report_Pending";
		var pr_div_Rp_Pending_Content		= "#div_JobReport_List_Report_Pending_Content";
		
		var pr_div_Rp_Validated				= "#div_JobReport_List_Report_Validated";
		var pr_div_Rp_Validated_Content		= "#div_JobReport_List_Report_Validated_Content";
		
//		var pr_div_Lst_User					= "#div_JobReport_List_User";
//		var pr_div_Lst_User_Content			= "#div_JobReport_List_User_Content";
		
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
		//var url_header              = req_gl_Security_HttpHeader(App.keys.KEY_STORAGE_CREDENTIAL);
		//------------------------------------------------------------------------------------
		var pr_SERVICE_CLASS		= "ServiceJobReport"; //to change by your need
		
		//------------------controllers------------------------------------------------------
		var pr_ctr_Main 			= null;
		var pr_ctr_Ent				= null;
		var pr_ctr_Rights			= null;
		
		//-----------------------------------------------------------------------------------
		var pr_object				= null;
		var pr_mode					= null;

		var lc_rp_stat_draft			= 0;
		var lc_rp_stat_pending			= 1;
		var lc_rp_stat_validate			= 2;
		
		var lc_rp_stat_denied			= 3;
		var lc_rp_stat_resume_updated	= 4;
			
		//	RIGHT ACTION----------------------------------------
		var RIGHT_U_G		= 2002001;
		var RIGHT_U_N		= 2002002;
		var RIGHT_U_M		= 2002003;
		var RIGHT_U_D		= 2002004;

		var RIGHT_A_G		= 2002011;
		var RIGHT_A_N		= 2002012;
		var RIGHT_A_M		= 2002013;
		var RIGHT_A_D		= 2002014;
		
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			pr_ctr_Main 			= App.controller.JobReport.Main;
			pr_ctr_Ent				= App.controller.JobReport.Ent;
			pr_ctr_Rights			= App.controller.JobReport.Rights;
			
			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_REPORT_LIST							, JobReport_List);
			
			
			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_REPORT_LIST_REPORT_DRAFT_CONTENT		, JobReport_List_Report_Content);		
			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_REPORT_LIST_REPORT_PENDING_CONTENT	, JobReport_List_Report_Content);			
			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_REPORT_LIST_REPORT_VALIDATED_CONTENT	, JobReport_List_Report_Content);
//			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_REPORT_LIST_REPORT_BY_USER_CONTENT	, JobReport_List_Report_Content);		
			
//			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_REPORT_LIST_USER_CONTENT				, JobReport_List_User_Content);
			
			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_REPORT_CONFIG_WORKING_DAY				, JobReport_Config_Working_Day);
		}
		
		//--------------------------------------------------------------------------------------------
		this.do_lc_show = function(){               
			try{
				var currentRpCode = App.data.currentReportCode;
				$(pr_divList	).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_REPORT_LIST, {}));
				
//				var rightCode = pr_ctr_Rights.req_lc_Right(RIGHT_A_G);
//				if(rightCode == -1){
//					rightCode = pr_ctr_Rights.req_lc_Right(RIGHT_U_G);
//					if(rightCode == -1){
//						do_gl_show_Notify_Msg_Error($.i18n("job_report_msg_user_right_error"));
//						return;
//					} else {
//						$(pr_div_Rp_Draft		).hide();
//						$(pr_div_Rp_Pending		).hide();
//						$(pr_div_Rp_Validated	).hide();
//						$(pr_div_Lst_User		).hide();
//						do_get_list_rp_of_user(App.data.user.id, lc_rp_stat_pending);
//					}
//				} else {
//					do_get_list_rp_draft	(currentRpCode);
//					do_get_list_rp_pending	(currentRpCode);
//					do_get_list_rp_validated(currentRpCode);
//					do_get_list_info_user();
//					$(pr_div_Rp_By_User).hide();
//				}
				
				do_get_list_rp_draft	(currentRpCode);
				do_get_list_rp_pending	(currentRpCode);
			}catch(e) {
				do_gl_show_Notify_Msg_Error("JobReport: EntList :" + e.toString());
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "job.report", "JobReportList", "do_lc_show", e.toString()) ;
			}
		};
		
		//---------------------------------------------------------------------------------------------
		//GET LIST REPORT OF USER-----GET LIST REPORT OF USER-----GET LIST REPORT OF USER-----GET LIST 
		//---------------------------------------------------------------------------------------------
		
		function do_get_list_rp_of_user(uId02, stat){
			var ref  = req_gl_Request_Content_Send("SVJobReportLstDyn");
			ref["uId02"] 	= uId02;
			ref["stat"]		= stat;
			
			$(pr_div_Rp_By_User).show();
			$(pr_div_Rp_By_User_Content	).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_REPORT_LIST_REPORT_BY_USER_CONTENT, {user_visible: true}));
			
			var lang = localStorage.language;
			if (lang ==null ) lang = "en";
			var filename = "www/js/lib/datatables/datatable_"+lang+".json";
			var additionalConfig = {
					"stat": {
						fnCreatedCell: function(nTd, sData, oData, iRow, iCol) {
							var status = "Unknown";
		    				var statusRef = oData.stat;
		    				switch(statusRef){
			    				case lc_rp_stat_draft: 			status = $.i18n("job_report_stat_draft");
			    				break;
			    				case lc_rp_stat_pending: 		status = $.i18n("job_report_stat_pending");
			    				break;
			    				case lc_rp_stat_validate: 		status = $.i18n("job_report_stat_validate");
			    				break;
			    				case lc_rp_stat_denied: 		status = $.i18n("job_report_stat_denied");
			    				break;
			    				case lc_rp_stat_resume_updated: status = $.i18n("job_report_stat_validate");
			    				break;
		    				}
		    				$(nTd).html(status);
						}
					}
			};
			var colConfig   = req_gl_table_col_config($(pr_div_Rp_By_User).find("table"), null, additionalConfig);
			var fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);
			var oTable  	= req_gl_Datatable_Ajax_Dyn(pr_div_Rp_By_User, App.path.BASE_URL_API_PRIV,url_header, filename, colConfig, ref, fError, undefined, null, undefined, do_bind_list_report);
		}
		
		var do_bind_list_report = function(data, div, oTable) {
			// do_gl_enhance_within($(div), {table: oTable});
			$(div).find('.table-datatableDyn tbody').off('click', 'tr');
			$(div).find('.table-datatableDyn tbody').on('click', 'tr', function(){
				// do_gl_Add_Class_List($(this).parent(), $(this), "selected");
				var oData = oTable.fnGetData(this);
				pr_ctr_Ent. do_lc_show_ById(oData, pr_ctr_Main.var_lc_MODE_SEL);
			});
		};
		
		//---------------------------------------------------------------------------------------------
		//GET LIST REPORT DRAFT-----GET LIST REPORT DRAFT-----GET LIST REPORT DRAFT-----GET LIST 
		//---------------------------------------------------------------------------------------------
		
		function do_get_list_rp_draft(currentRpCode){	
			var ref  = req_gl_Request_Content_Send("SVJobReportLstDyn");
			ref["stat"] = lc_rp_stat_draft;
	
			$(pr_div_Rp_Draft_Content	).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_REPORT_LIST_REPORT_DRAFT_CONTENT, {user_visible: false}));
			
			var lang = localStorage.language;
			if (lang ==null ) lang = "en";	
			var filename = "www/js/lib/datatables/datatable_"+lang+".json";
			var additionalConfig = {
					"stat": {
						fnCreatedCell: function(nTd, sData, oData, iRow, iCol) {
							$(nTd).html($.i18n("job_report_stat_draft"));
						}
					}
			};
			var colConfig   = req_gl_table_col_config($(pr_div_Rp_Draft).find("table"), null, additionalConfig);
			var fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);
			var oTable  = req_gl_Datatable_Ajax_Dyn(pr_div_Rp_Draft, App.path.BASE_URL_API_PRIV,url_header, filename, colConfig, ref, fError, undefined, null, undefined, do_bind_list_rp_draft);
		}
		
		var do_bind_list_rp_draft = function(data, div, oTable){
			// do_gl_enhance_within($(div), {table: oTable});
			$(div).find('.table-datatableDyn tbody').off('click', 'tr');
			$(div).find('.table-datatableDyn tbody').on('click', 'tr', function(){
				// do_gl_Add_Class_List($(this).parent(), $(this), "selected");
				var oData = oTable.fnGetData(this);
				pr_ctr_Ent. do_lc_show_ById(oData, pr_ctr_Main.var_lc_MODE_SEL);
				$(pr_div_Rp_By_User).hide();
			});
		}
		
		//---------------------------------------------------------------------------------------------
		//GET LIST REPORT PENDING-----GET LIST REPORT PENDING-----GET LIST REPORT PENDING-----GET LIST 
		//---------------------------------------------------------------------------------------------
		
		function do_get_list_rp_pending(currentRpCode){	
			var ref  = req_gl_Request_Content_Send("SVJobReportLstDyn");
			ref["stat"] = lc_rp_stat_pending;
	
			$(pr_div_Rp_Pending_Content	).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_REPORT_LIST_REPORT_PENDING_CONTENT, {user_visible: false}));
			
			var lang = localStorage.language;
			if (lang ==null ) lang = "en";	
			var filename = "www/js/lib/datatables/datatable_"+lang+".json";
			var additionalConfig = {
					"stat": {
						fnCreatedCell: function(nTd, sData, oData, iRow, iCol) {
							$(nTd).html($.i18n("job_report_stat_pending"));
						}
					}
			};
			var colConfig   = req_gl_table_col_config($(pr_div_Rp_Pending).find("table"), null, additionalConfig);
			var fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);
			var oTable  = req_gl_Datatable_Ajax_Dyn(pr_div_Rp_Pending, App.path.BASE_URL_API_PRIV,url_header, filename, colConfig, ref, fError, undefined, null, undefined, do_bind_list_rp_pending);
		}
		
		var do_bind_list_rp_pending = function(data, div, oTable){
			// do_gl_enhance_within($(div), {table: oTable});
			$(div).find('.table-datatableDyn tbody').off('click', 'tr');
			$(div).find('.table-datatableDyn tbody').on('click', 'tr', function(){
				// do_gl_Add_Class_List($(this).parent(), $(this), "selected");
				var oData = oTable.fnGetData(this);
				pr_ctr_Ent. do_lc_show_ById(oData, pr_ctr_Main.var_lc_MODE_SEL);
				$(pr_div_Rp_By_User).hide();
			});
		}
		
		//---------------------------------------------------------------------------------------------
		//GET LIST REPORT VALIDATED-----GET LIST REPORT VALIDATED-----GET LIST REPORT VALIDATED-----GET
		//---------------------------------------------------------------------------------------------
		
		function do_get_list_rp_validated(currentRpCode){	
			var ref  	= req_gl_Request_Content_Send("SVJobReportLstDyn");
			ref["stat"] = lc_rp_stat_validate;
			ref["code"] = currentRpCode;

			$(pr_div_Rp_Validated_Content).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_REPORT_LIST_REPORT_VALIDATED_CONTENT, {user_visible: false}));
			
			var lang = localStorage.language;
			if (lang ==null ) lang = "en";	
			var filename = "www/js/lib/datatables/datatable_"+lang+".json";
			var additionalConfig = {
					"stat": {
						fnCreatedCell: function(nTd, sData, oData, iRow, iCol) {
							var status = "Unknown";
		    				var statusRef = oData.stat;
		    				switch(statusRef){
			    				case lc_rp_stat_denied: 		status = $.i18n("job_report_stat_denied");
			    				break;
		    					case lc_rp_stat_validate: 		status = $.i18n("job_report_stat_validate");
			    				break;
			    				case lc_rp_stat_resume_updated: status = $.i18n("job_report_stat_validate");
			    				break;
		    				}
		    				$(nTd).html(status);
						}
					}
			};
			var colConfig   = req_gl_table_col_config($(pr_div_Rp_Validated).find("table"), null, additionalConfig);
			var fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);
			var oTable  = req_gl_Datatable_Ajax_Dyn(pr_div_Rp_Validated, App.path.BASE_URL_API_PRIV,url_header, filename, colConfig, ref, fError, undefined, null, undefined, do_bind_list_rp_validated);
		}
		
		var do_bind_list_rp_validated = function(data, div, oTable){
			// do_gl_enhance_within($(div), {table: oTable});
			$(div).find('.table-datatableDyn tbody').off('click', 'tr');
			$(div).find('.table-datatableDyn tbody').on('click', 'tr', function(){
				// do_gl_Add_Class_List($(this).parent(), $(this), "selected");
				var oData = oTable.fnGetData(this);
				pr_ctr_Ent. do_lc_show_ById(oData, pr_ctr_Main.var_lc_MODE_SEL);
				$(pr_div_Rp_By_User).hide();
			});
		}
		
		//---------------------------------------------------------------------------------------------
		//GET LIST INFO USER-----GET LIST INFO USER-----GET LIST INFO USER-----GET LIST INFO USER-----G
		//---------------------------------------------------------------------------------------------
		
		function do_get_list_info_user(){
			var ref  = req_gl_Request_Content_Send("SVJobReportLstUserDyn");
			ref["codeRp"]  = App.data.currentReportCode;
			
			$(pr_div_Lst_User_Content	).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_REPORT_LIST_USER_CONTENT, {}));
			var lang = localStorage.language;
			if (lang ==null ) lang = "en";
			var filename = "www/js/lib/datatables/datatable_"+lang+".json";
			var additionalConfig = {
					"name01": {
						fnCreatedCell: function(nTd, sData, oData, iRow, iCol) {
							if(!oData.name03) oData.name03 = "";
		    				$(nTd).html(oData.name01 + " " + oData.name02 + " " + oData.name03);
		    				if(oData.id != App.data.user.id){
		    					App.data.lstUser.push(oData);
		    				}
						}
					}
			};
			var colConfig   = req_gl_table_col_config($(pr_div_Lst_User).find("table"), null, additionalConfig);
			var fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);
			var oTable  = req_gl_Datatable_Ajax_Dyn(pr_div_Lst_User, App.path.BASE_URL_API_PRIV,url_header, filename, colConfig, ref, fError, undefined, null, undefined, do_bind_list_user);
		}
		
		var do_bind_list_user = function(data, div, oTable){
			// do_gl_enhance_within($(div), {table: oTable});
			$(div).find('.table-datatableDyn tbody').off('click', 'tr');
			$(div).find('.table-datatableDyn tbody').on('click', 'tr', function(){
				// do_gl_Add_Class_List($(this).parent(), $(this), "selected");
				var objUser = oTable.fnGetData(this);
				var uId02 = objUser.id;
				for(var i = 0; i < App.data.lstUser.length; i++){
					if(App.data.lstUser[i].id == uId02){
						App.data.selectedUser = App.data.lstUser[i];
						break;
					}
				}
				if(!App.data.selectedUser.wdConfig){
					do_lc_active_msg_config_user();
				}
				do_get_list_rp_of_user(objUser.id, lc_rp_stat_pending);
				pr_ctr_Ent. do_lc_show_user_summary(objUser);
			});
		}
		
		//---------------------------------------------------------------------------------------------
		//REQ CONTENT SEND-----REQ CONTENT SEND-----REQ CONTENT SEND-----REQ CONTENT SEND-----REQ CONTE
		//---------------------------------------------------------------------------------------------
		
		function req_gl_Request_Content_Send(serviceName){
			var ref 		= {};
			ref[svClass] 	= pr_SERVICE_CLASS; 
			ref[svName]		= serviceName;
			ref[userId]		= App.data.user.id;
			ref[sessId]		= App.data.session_id;	
			
			return ref;
		}
		
		//---------------------------------------------------------------------------------------------
		//SET WORKING DATE AND WORKING HOUR-----SET WORKING DATE AND WORKING HOUR-----SET WORKING DATE 
		//---------------------------------------------------------------------------------------------
		
		this.do_active_msg_config_user = function(createRp){
			do_lc_active_msg_config_user(createRp);
		};
		
		function do_lc_active_msg_config_user(createRp){
			var oData = App.data.selectedUser;
			var rightCode = pr_ctr_Rights.req_lc_Right(RIGHT_A_M);
			if(rightCode == -1){
				App.MsgboxController.do_lc_show({
					title	: $.i18n("job_rp_edit_config_wd_title") + oData.name01 + " " + oData.name02 + " " + oData.name03,
					content : tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_REPORT_CONFIG_WORKING_DAY_USER, {}),
					buttons	: {
						OK: {
							lab	:  $.i18n("job_rp_config_btn03")
						}
					}
				});
			} else {
				App.MsgboxController.do_lc_show({
					title	: $.i18n("job_rp_new_config_wd_title") + oData.name01 + " " + oData.name02 + " " + oData.name03,
					content : tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_REPORT_CONFIG_WORKING_DAY, oData),
					buttons	: {
						CANCEL: {
							lab		:  	$.i18n("job_rp_config_btn01")
						},
						SAVE_EXIT: {
							lab		: 	$.i18n("job_rp_config_btn02"),
							funct	: 	function(){
								pr_ctr_Ent.do_lc_update_user_wd(oData, createRp);
							}							
						}
					}
				});
				do_gl_enable_edit($("#div_wd_user"));
			}		
		}
	};

	return JobReportList;
  });