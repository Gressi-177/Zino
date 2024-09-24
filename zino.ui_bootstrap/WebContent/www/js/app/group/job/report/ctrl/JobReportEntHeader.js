define([
        'jquery',
        'text!group/job/report/tmpl/JobReport_Ent_Header.html',
        'text!group/job/report/tmpl/JobReport_Ent_Header_User_Summary.html',
        'text!group/job/report/tmpl/JobReport_Display_QR_Code.html'
        ],

        function($, 
        		JobReport_Ent_Header,
        		JobReport_Ent_Header_User_Summary,
        		JobReport_Display_QR_Code) {

	var JobReportEntHeader     = function (grpName,header,content,footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;

		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplContr				= App.template.controller;
		//------------------controllers------------------------------------------------------
		var pr_ctr_Main 			= null;
		var pr_ctr_List 			= null;
		var pr_ctr_Ent				= null;
		var pr_ctr_Rights			= null;
		
		var lc_rp_stat_draft			= 0;
		var lc_rp_stat_pending			= 1;
		var lc_rp_stat_validate			= 2;
		var lc_rp_stat_denied			= 3;
		var lc_rp_stat_resume_updated 	= 4;
		
		var lc_username 			= App.data.user.per.name01 + " " + App.data.user.per.name02;

		//-----------------------------------------------------------------------------------
		var pr_object				= null;
		var pr_mode					= null;
		
		//	RIGHT ACTION----------------------------------------
		var RIGHT_U_G		= 0;
		var RIGHT_U_N		= 1;
		var RIGHT_U_M		= 2;
		var RIGHT_U_D		= 3;
		
		var RIGHT_A_G		= 4;
		var RIGHT_A_N		= 5;
		var RIGHT_A_M		= 6;
		var RIGHT_A_D		= 7;
		var RIGHT_A_WD		= 8;
		
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			pr_ctr_Main 			= App.controller.JobReport.Main;
			pr_ctr_List 			= App.controller.JobReport.List;
			pr_ctr_Ent				= App.controller.JobReport.Ent;
			pr_ctr_Rights			= App.controller.JobReport.Rights;
			pr_ctr_DateTime			= App.controller.JobReport.DateTime;
			pr_ctr_EntTabReportDetail 	= App.controller.JobReport.EntTabReportDetail;
			
			if(!tmplName) {
				App.template.names[pr_grpName] = {}
				tmplName = App.template.names[pr_grpName]
			}
			
			tmplContr.do_lc_put_tmpl(tmplName.JOB_REPORT_ENT_HEADER, JobReport_Ent_Header);
			tmplContr.do_lc_put_tmpl(tmplName.JOB_REPORT_ENT_HEADER_USER_SUMMARY, JobReport_Ent_Header_User_Summary);
			tmplContr.do_lc_put_tmpl(tmplName.JOB_REPORT_DISPLAY_QR_CODE, JobReport_Display_QR_Code);
		}
		
		this.do_lc_show		= function(obj, mode){
			pr_object 	= obj;
			pr_mode		= mode;
			
			try{
				$(pr_divContent).html(tmplContr.req_lc_compile_tmpl(tmplName.JOB_REPORT_ENT_HEADER, obj));
				do_bind_event(obj, mode);
			}catch(e) {
				do_gl_show_Notify_Msg_Error("JobReport: EntHeader :" + e.toString());
				// do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "job.report", "JobReportEntHeader", "do_lc_show", e.toString()) ;
			}
		}
		
		this.do_lc_show_user_summary = function(obj){
			try{
				$("#div_JobReport_Ent_Header").html(tmplContr.req_lc_compile_tmpl(tmplName.JOB_REPORT_ENT_HEADER_USER_SUMMARY, obj));
				do_lc_bind_event_user_summary();
			}catch(e) {
				do_gl_show_Notify_Msg_Error("JobReport: User Summary :" + e.toString());
				// do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "job.report", "JobReportEntHeader", "do_lc_show", e.toString()) ;
			}
		}
		
		//---------private-----------------------------------------------------------------------------
		function do_bind_event(obj, mode){
			$("#inp_hldrate").val(App.data.hldRate);
			$("#inp_stat").val(obj.stat);
			
			if(obj.stat == lc_rp_stat_draft || obj.stat == lc_rp_stat_pending){
				$(".rp_validated_zone").hide();
			}

			$(".info-edit").on("click", function(){
				if(obj.stat == lc_rp_stat_draft){
					let $parent = $(this).parent();
					$parent.find(".info-content")			.addClass("hide");
					$parent.find(".info-content-worker")	.addClass("hide");
					$parent.find(".content-edit")	.removeClass("hide");
					$parent.find(".content-edit")	.prop("disabled", false);
					if($parent.find(".content-edit").length > 0){
						if(mode != pr_ctr_Main.var_lc_MODE_NEW){
							$("#a_btn_save, #a_btn_cancel")	.removeClass("hide");
						}
					}
				}
			})

			$("#a_btn_export").off("click").on("click", function(){
				do_lc_gen_report(obj);
			})

			$("#a_btn_save").off("click").on("click", function(){
				pr_ctr_Ent.do_lc_save(obj, pr_ctr_Main.var_lc_MODE_SEL, lc_rp_stat_draft);	
			})

			$("#a_btn_cancel").off("click").on("click", function(){
				self.do_lc_show(obj, mode);
			})

//			if(!App.data.reportCodes)	pr_ctr_DateTime.getcurrentAndPrevReportCode();
			if(!App.data.reportCodes)	pr_ctr_DateTime.getReportCode();
			do_gl_autocomplete({
				el: $("#inp_code"),
				source: App.data.reportCodes,
				selectCallback: function(item) {
					$("#inp_code"	).val(item.date);	
					var u = App.data.selectedUser;
					if(!u.name03) u.name03 = "";

					pr_ctr_DateTime.getInfoDayOff(item.date, u.id);
					pr_ctr_EntTabReportDetail.do_lc_show(obj, mode);
					return true;
				},
				renderAttrLst: ["date"],
				minLength: 0,
				placeholder	: $.i18n("job_report_label_code"),
				required: true
			});
		}
		
		this.do_bind_event_user_summary = function(){
			do_lc_bind_event_user_summary();
		}
		
		function do_lc_bind_event_user_summary(){
			var infoWd = App.data.selectedUser.wdConfig;
			if(infoWd){
				$("#btn_new_wd").hide();
				$("#btn_display_wd").show();
				$("#btn_display_wd").off("click");
				$("#btn_display_wd").on("click", function(){
					var wd = do_get_values();
					App.MsgboxController.do_lc_show({
						title	: $.i18n("job_rp_show_config_wd_title") + wd.name01 + " " + wd.name02 + " " + wd.name03,
						content : tmplContr.req_lc_compile_tmpl(tmplName.JOB_REPORT_CONFIG_WORKING_DAY, wd),
						buttons	: {
							OK: {
								lab		:  	$.i18n("job_rp_config_btn03")
							}
						}
					});
					$("#job_rp_config_header").hide();
					do_match_values(wd);
				});
				
				$("#btn_modify_wd").show();
				$("#btn_modify_wd").off("click");
				$("#btn_modify_wd").on("click", function(){
					var rightCode = pr_ctr_Rights.req_lc_Right(RIGHT_A_WD);
					if(rightCode == -1){
						do_gl_show_Notify_Msg_Error($.i18n("job_report_msg_user_right_error"));
					} else {
						var wd = do_get_values();
						App.MsgboxController.do_lc_show({
							title	: $.i18n("job_rp_edit_config_wd_title") + wd.name01 + " " + wd.name02 + " " + wd.name03,
							content : tmplContr.req_lc_compile_tmpl(tmplName.JOB_REPORT_CONFIG_WORKING_DAY, wd),
							buttons	: {
								CANCEL: {
									lab		:  	$.i18n("job_rp_config_btn04")
								},
								SAVE_EXIT: {
									lab		: 	$.i18n("job_rp_config_btn02"),
									funct	: 	function(){
										pr_ctr_Ent.do_lc_update_user_wd(App.data.selectedUser);
									}
								}
							}
						});
						
						$("#job_rp_config_header").hide();
						do_gl_enable_edit($("#div_wd_user"));
						do_match_values(wd);
					}	
				});
			} else {
				$("#btn_modify_wd").hide();
				$("#btn_display_wd").hide();
				$("#btn_new_wd").show();
				$("#btn_new_wd").off("click");
				$("#btn_new_wd").on("click", function(){
					var rightCode = pr_ctr_Rights.req_lc_Right(RIGHT_A_WD);
					if(rightCode == -1){
						do_gl_show_Notify_Msg_Error($.i18n("job_report_msg_user_right_error"));
					} else {
						pr_ctr_List.do_active_msg_config_user(false);
					}
				});
			}
			
			$("#a_show_qr_code").off("click");
			$("#a_show_qr_code").on("click", function(){
				App.MsgboxController.do_lc_show({
					title	: $.i18n("job_rp_btn_show_qr_code"),
					content : tmplContr.req_lc_compile_tmpl(tmplName.JOB_REPORT_DISPLAY_QR_CODE, App.data.selectedUser),
					buttons	: {
						OK: {
							lab		:  	$.i18n("job_rp_config_btn03")
						}
					}
				});
				if(App.data.selectedUser.code != null && App.data.selectedUser.code != ""){
					$("#div_user_qr_code").qrcode({"text":App.data.selectedUser.code});
				} else {
					do_gl_show_Notify_Msg_Error($.i18n("job_report_msg_user_code_missing"));
				}
				
			});
		}
		
		function do_get_values(){
			var infoWd = App.data.selectedUser.wdConfig;
			var wd = {};
			if(infoWd){
				wd.sun = infoWd.charAt(0);
				wd.mon = infoWd.charAt(1);
				wd.tue = infoWd.charAt(2);
				wd.wed = infoWd.charAt(3);
				wd.thu = infoWd.charAt(4);
				wd.fri = infoWd.charAt(5);
				wd.sat = infoWd.charAt(6);
			}
			wd.name01 = App.data.selectedUser.name01;
			wd.name02 = App.data.selectedUser.name02;
			wd.name03 = App.data.selectedUser.name03;
			
			var infoWh = App.data.selectedUser.whConfig;
			if(infoWh){
				var splitTime = infoWh.match(/.{2}/g);
				const req_Time = (start, end) => {
					if(splitTime[start] === '##' || splitTime[end] === '##') return ""

					return splitTime[start] +":"+ splitTime[end]
				}

				wd.sunS = req_Time(0, 1)
				wd.sunE = req_Time(2, 3)
				wd.monS = req_Time(4, 5)
				wd.monE = req_Time(6, 7)
				wd.tueS = req_Time(8, 9)
				wd.tueE = req_Time(10, 11)
				wd.wedS = req_Time(12, 13)
				wd.wedE = req_Time(14, 15)
				wd.thuS = req_Time(16, 17)
				wd.thuE = req_Time(18, 19)
				wd.friS = req_Time(20, 21)
				wd.friE = req_Time(22, 23)
				wd.satS = req_Time(24, 25)
				wd.satE = req_Time(26, 27)
			}
			return wd;
		}
		
		function do_match_values(wd){
			$("#sel_mon").val(wd.mon);
			$("#sel_tue").val(wd.tue);
			$("#sel_wed").val(wd.wed);
			$("#sel_thu").val(wd.thu);
			$("#sel_fri").val(wd.fri);
			$("#sel_sat").val(wd.sat);
			$("#sel_sun").val(wd.sun);
		}
	};

	return JobReportEntHeader;
});