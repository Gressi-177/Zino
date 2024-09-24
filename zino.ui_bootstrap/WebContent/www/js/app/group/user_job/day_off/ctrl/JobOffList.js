define([
        'jquery',
        'text!group/user_job/day_off/tmpl/JobOff_List.html',
        
        'text!group/user_job/day_off/tmpl/JobOff_List_Request_By_User_Header.html',  
        'text!group/user_job/day_off/tmpl/JobOff_List_Request_By_User_Content.html',
        
        'text!group/user_job/day_off/tmpl/JobOff_List_Request_Pending_Header.html',  
        'text!group/user_job/day_off/tmpl/JobOff_List_Request_Pending_Content.html',
        
        'text!group/user_job/day_off/tmpl/JobOff_List_Request_Validated_Header.html',  
        'text!group/user_job/day_off/tmpl/JobOff_List_Request_Validated_Content.html',

        ],
        function($, 
        		JobOff_List,
        		
        		JobOff_List_Request_By_User_Header,
        		JobOff_List_Request_By_User_Content,
        		
        		JobOff_List_Request_Pending_Header,
        		JobOff_List_Request_Pending_Content,
        		
        		JobOff_List_Request_Validated_Header,
        		JobOff_List_Request_Validated_Content
        ) 
        {

	var JobOffList 	= function (grpName, header, content, footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var tmplName				= App.template.names;
		var tmplCtrl				= App.template.controller;		
		//------------------------------------------------------------------------------------
		var pr_SERVICE_CLASS		= "ServiceJobDayoffRequest"; //to change by your need
		var svClass 				= App['const'].SV_CLASS;
		var svName					= App['const'].SV_NAME;
		var sessId					= App['const'].SESS_ID;
		var userId          		= App['const'].USER_ID;
		
		var fVar					= App['const'].FUNCT_SCOPE;
		var fName					= App['const'].FUNCT_NAME;
		var fParam					= App['const'].FUNCT_PARAM;		
		var url_header				= req_gl_LS_SecurityHeaderBearer (App.keys.KEY_STORAGE_CREDENTIAL);
		var self 					= this;		
		
		var pr_divList 					= "#div_JobOff_List";
		
		var pr_div_Rq_By_User			= "#div_JobOff_List_Request_By_User";
		var pr_div_Rq_By_User_Header	= "#div_JobOff_List_Request_By_User_Header";
		var pr_div_Rq_By_User_Content	= "#div_JobOff_List_Request_By_User_Content";
		
		var pr_div_Rq_Pending			= "#div_JobOff_List_Request_Pending";
		var pr_div_Rq_Pending_Header	= "#div_JobOff_List_Request_Pending_Header";
		var pr_div_Rq_Pending_Content	= "#div_JobOff_List_Request_Pending_Content";
		
		var pr_div_Rq_Validated			= "#div_JobOff_List_Request_Validated";
		var pr_div_Rq_Validated_Header	= "#div_JobOff_List_Request_Validated_Header";
		var pr_div_Rq_Validated_Content	= "#div_JobOff_List_Request_Validated_Content";
		
		//------------------controllers------------------------------------------------------
		var pr_ctr_Main 			= null;
		var pr_ctr_Ent				= null;
		var pr_ctr_Rights 			= null;		
		
		//-----------------------------------------------------------------------------------
		var pr_object				= null;
		var pr_mode					= null;
		
		var lc_rq_stat_draft		= 0;
		var lc_rq_stat_pending		= 1;
		var lc_rq_stat_validated	= 2;
		var lc_rq_stat_denied		= 3;
		
		//	RIGHT ACTION----------------------------------------
		var RIGHT_U_G		= 2002001;
		var RIGHT_U_N		= 2002002;
		var RIGHT_U_M		= 2002003;
		var RIGHT_U_D		= 2002004;

		var RIGHT_A_G		= 2002011;
		var RIGHT_A_N		= 2002012;
		var RIGHT_A_M		= 2002013;
		var RIGHT_A_D		= 2002014;
		//-----------------------------------------------------------------------------------
		//var url_header              = req_gl_Security_HttpHeader(App.keys.KEY_STORAGE_CREDENTIAL);
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			pr_ctr_Main 			= App.controller.JobOff.Main;
			pr_ctr_Ent				= App.controller.JobOff.Ent;
			pr_ctr_Rights			= App.controller.JobOff.Rights;
			
			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_OFF_LIST			, JobOff_List);
			
			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_OFF_LIST_REQUEST_BY_USER_HEADER	, JobOff_List_Request_By_User_Header); 		
			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_OFF_LIST_REQUEST_BY_USER_CONTENT	, JobOff_List_Request_By_User_Content);
			
			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_OFF_LIST_REQUEST_PENDING_HEADER	, JobOff_List_Request_Pending_Header); 		
			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_OFF_LIST_REQUEST_PENDING_CONTENT	, JobOff_List_Request_Pending_Content); 
			
			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_OFF_LIST_REQUEST_VALIDATED_HEADER	, JobOff_List_Request_Validated_Header); 		
			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_OFF_LIST_REQUEST_VALIDATED_CONTENT, JobOff_List_Request_Validated_Content);
		}
		
		//---------show-----------------------------------------------------------------------------
		this.do_lc_show = function(){               
			try{
				$(pr_divList				).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_OFF_LIST			, {}));
				
				$(pr_div_Rq_By_User_Header	).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_OFF_LIST_REQUEST_BY_USER_HEADER		, {}));
				$(pr_div_Rq_By_User_Content	).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_OFF_LIST_REQUEST_BY_USER_CONTENT		, {}));
				
				$(pr_div_Rq_Pending_Header	)	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_OFF_LIST_REQUEST_PENDING_HEADER 	, {}));
				$(pr_div_Rq_Pending_Content	)	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_OFF_LIST_REQUEST_PENDING_CONTENT	, {}));
				
				$(pr_div_Rq_Validated_Header) 	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_OFF_LIST_REQUEST_VALIDATED_HEADER 	, {}));
				$(pr_div_Rq_Validated_Content)	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_OFF_LIST_REQUEST_VALIDATED_CONTENT	, {}));
				
//				var rightCode = pr_ctr_Rights.req_lc_Right(RIGHT_A_G);
//				if(rightCode == -1){
//					rightCode = pr_ctr_Rights.req_lc_Right(RIGHT_U_G);
//					if(rightCode == -1){
//						do_gl_show_Notify_Msg_Error($.i18n("job_report_msg_user_right_error"));
//						return;
//					} else {
//						$(pr_div_Rq_Pending).hide();
//						$(pr_div_Rq_Validated).hide();
//					}
//				} else {
//					$(pr_div_Rq_Pending_Header	)	.html(tmplCtrl.req_lc_compile_tmpl(
//							tmplName.JOB_OFF_LIST_REQUEST_PENDING_HEADER 	, {}));
//					$(pr_div_Rq_Pending_Content	)	.html(tmplCtrl.req_lc_compile_tmpl(
//							tmplName.JOB_OFF_LIST_REQUEST_PENDING_CONTENT	, {}));
//					do_get_list_request_dyn(pr_div_Rq_Pending, 		lc_rq_stat_pending, 	null);
//					
//					$(pr_div_Rq_Validated_Header) 	.html(tmplCtrl.req_lc_compile_tmpl(
//							tmplName.JOB_OFF_LIST_REQUEST_VALIDATED_HEADER 	, {}));
//					$(pr_div_Rq_Validated_Content)	.html(tmplCtrl.req_lc_compile_tmpl(
//							tmplName.JOB_OFF_LIST_REQUEST_VALIDATED_CONTENT	, {}));
//					do_get_list_request_dyn(pr_div_Rq_Validated, lc_rq_stat_validated, null);
//				}
				
				do_get_list_request_dyn(pr_div_Rq_By_User      ,lc_rq_stat_draft       ,App.data.user.id);
				do_get_list_request_dyn(pr_div_Rq_Pending 	   ,lc_rq_stat_pending 	   ,App.data.user.id);
				do_get_list_request_dyn(pr_div_Rq_Validated    ,lc_rq_stat_validated   ,App.data.user.id);

				App.controller.UI.Main.do_lc_bind_event_resize();
			}catch(e) {
				do_gl_show_Notify_Msg_Error("JobOff > List :" + e.toString());
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "job.off", "JobOffList", "do_lc_show", e.toString()) ;
			}
		};
		
		//---------------------------------------------------------------------------------------------
		//LIST REQUEST DYNAMIC-----LIST REQUEST DYNAMIC-----LIST REQUEST DYNAMIC-----LIST REQUEST DYNAM
		//---------------------------------------------------------------------------------------------
		var dateFormat = function(nTd, sData, oData,iRow, iCol){
			var local = localStorage.language;
			if (!local) local = "en";
			var format = DateFormat.masks.enShortDate;
			if (local=="fr")
				format = DateFormat.masks.frShortDate;
			else if (local=="vn")
				format = DateFormat.masks.viShortDate;
				
			$(nTd).html(DateFormat(sData, format));
		}
		
		function do_get_list_request_dyn(div, stat, uId03 ){	
			var ref 		= req_gl_Request_Content_Send(pr_SERVICE_CLASS, "SVJobDayoffRequestLstDyn");
			ref["uId03"]	= uId03;
			ref["stat"]		= stat;

			var lang 		= localStorage.language;
			if (lang == null ) lang = "en";
			var filename = "www/js/lib/datatables/datatable_"+lang+".json";
			var additionalConfig = {
					"stat": {
						fnCreatedCell: function(nTd, sData, oData, iRow, iCol) {
							var status = "Unknown";
		    				var statusRef = oData.stat;
		    				switch(statusRef){
			    				case lc_rq_stat_draft: 		status = $.i18n("job_report_stat_draft");
			    				break;
			    				case lc_rq_stat_pending: 	status = $.i18n("job_report_stat_pending");
			    				break;
			    				case lc_rq_stat_validated: 	status = $.i18n("job_report_stat_validate");
			    				break;
			    				case lc_rq_stat_denied: 	status = $.i18n("job_report_stat_denied");
			    				break;
		    				}
		    				$(nTd).html(status);
						}
					},
					"dt01": {fnCreatedCell: dateFormat},
					"dt03": {fnCreatedCell: dateFormat},
					"dt04": {fnCreatedCell: dateFormat}
						
			};
			var colConfig   = req_gl_table_col_config($(div).find("table"), null, additionalConfig);
			var fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);
			var oTable  	= req_gl_Datatable_Ajax_Dyn(div, App.path.BASE_URL_API_PRIV, url_header, filename, colConfig, ref, fError, undefined, null, undefined, do_bind_list_request);
		}
		
		var do_bind_list_request = function(data, div, oTable) {
			// do_gl_enhance_within($(div));
			$(div).find('.table-datatableDyn tbody').off('click', 'tr');
			$(div).find('.table-datatableDyn tbody').on('click', 'tr', function(){
				// do_gl_Add_Class_List($(this).parent(), $(this), "selected");
				var oData = oTable.fnGetData(this);
				pr_ctr_Ent. do_lc_show_ById(oData, pr_ctr_Main.var_lc_MODE_SEL);
			});
		};
	};

	return JobOffList;
  });