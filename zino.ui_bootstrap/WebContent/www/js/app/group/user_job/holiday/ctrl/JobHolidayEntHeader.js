define([
        'jquery',
        'text!group/user_job/holiday/tmpl/JobHoliday_Ent_Header.html'

        ],

        function($, 
        		JobHoliday_Ent_Header) {


	var JobHolidayEntHeader     = function (grpName, header, content, footer) {
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

		var svClass         			= App['const'].SV_CLASS;
		var svName          			= App['const'].SV_NAME;
		var userId          			= App['const'].USER_ID;
		var sessId          			= App['const'].SESS_ID;
		var fVar            			= App['const'].FUNCT_SCOPE;
		var fName           			= App['const'].FUNCT_NAME;
		var fParam          			= App['const'].FUNCT_PARAM;
		
		var pr_TYP_ADM_ALL           = 10;

		//------------------controllers------------------------------------------------------
		var pr_ctr_Main 			= null;
		var pr_ctr_List 			= null;
		var pr_ctr_Ent				= null;
		var pr_ctr_EntHeader 		= null;
		var pr_ctr_EntBtn 			= null;
		var pr_ctr_EntTabs 			= null;
		
	
		//-----------------------------------------------------------------------------------
		var pr_object				= null;
		var pr_mode					= null;
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			pr_ctr_Main 			= App.controller.JobHoliday.Main;
			pr_ctr_List 			= App.controller.JobHoliday.List;
			
			pr_ctr_Ent				= App.controller.JobHoliday.Ent;
			pr_ctr_EntHeader 		= App.controller.JobHoliday.EntHeader;
			pr_ctr_EntBtn 			= App.controller.JobHoliday.EntBtn;
			pr_ctr_EntTabs 			= App.controller.JobHoliday.EntTabs;
			
		}      
		
		this.do_lc_show		= function(obj, mode){
			pr_object 	= obj;
			pr_mode		= mode;
			
			try{
				tmplCtrl.do_lc_put_tmpl(tmplName.JOB_HOLIDAY_ENT_HEADER	, JobHoliday_Ent_Header);
				$(pr_divContent).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_HOLIDAY_ENT_HEADER, obj));
				App.controller.UI.Main.do_lc_bind_event_resize();
				
				if(App.data.user.typ == pr_TYP_ADM_ALL){
					$("#inp_manager").removeClass("hide");
				}
				
				if(mode == pr_ctr_Main.var_lc_MODE_NEW){
					$(".info-content").addClass("hide");
					$(".content-edit").removeClass("hide");
				}
				
				do_bind_event(obj, mode);
			}catch(e) {
				do_gl_show_Notify_Msg_Error("JobHoliday: EntHeader :" + e.toString());
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "job.holiday", "JobHolidayEntHeader", "do_lc_show", e.toString()) ;
			}
		}
		
		function do_bind_event(obj,mode){
			$("#inp_date").off("change");
			$("#inp_date").on("change", function(){
				var newDate = $("#inp_date").val();
				var newVal = convertToDateObj(newDate);
				var code = getCodeFromDate(newVal);
				$("#inp_code").val(code);
				$("#inp_codeStr").html(code);
				
			});
			
			$(".info-edit").on("click", function(){
				let $parent = $(this).parent();
				$parent.find(".info-content")			.addClass("hide");
				$parent.find(".info-content-worker")	.addClass("hide");
				$parent.find(".content-edit")	.removeClass("hide");
				$parent.find(".content-edit")	.prop("disabled", false);
				if($parent.find(".content-edit").length > 0){
					$("#a_btn_save, #a_btn_cancel")	.removeClass("hide");
				}
			})
			
			$("#a_btn_save").off("click").on("click", function(){
				pr_ctr_Ent.do_lc_Save_Entity(pr_divContent, obj);
			})
			$("#a_btn_cancel").off("click").on("click", function(){
				self.do_lc_show(obj,mode);	
			})
		}
		
		function convertToDateObj(stringDate){
			var local = localStorage.language;
			if (!local) local = "en";
			if (local=="en"){
				return new Date(stringDate);
			} else {
				var dateParts = stringDate.split("/");
				var dateObject = new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
				return new Date(dateObject);
			}
		}
		
		function getCodeFromDate(newVal){
			var YYYY 	= newVal.getFullYear();
			var MM_ 	= newVal.getMonth() + 1;
			
			var MM 		= MM_.toString();
			if(MM_ < 10){
				MM = "0" + MM_.toString();
			}
			
			return YYYY.toString() + "-" + MM;
		}
	
	};


	return JobHolidayEntHeader;
});