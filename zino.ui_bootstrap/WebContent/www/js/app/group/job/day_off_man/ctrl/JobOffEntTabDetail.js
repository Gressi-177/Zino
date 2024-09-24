define([
        'jquery',
        'text!group/job/day_off_man/tmpl/JobOff_Ent_Tab_Detail.html'      

        ],
        function($,
        		JobOff_Ent_Tab_Detail    		
        		) {

	var JobOffEntTabDetail     = function (grpName, header, content, footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		//------------------controllers------------------------------------------------------
		var pr_ctr_Main 			= null;
		var pr_ctr_EntBtn			= null;
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			pr_ctr_Main 			= App.controller.JobOffMan.Main;
			pr_ctr_EntBtn			= App.controller.JobOffMan.EntBtn;

			if(!tmplName) {
				App.template.names[pr_grpName] = {}
				tmplName 	= App.template.names[pr_grpName]
			}

			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_OFF_ENT_TAB_DETAIL, JobOff_Ent_Tab_Detail);
		}
		
		this.do_lc_show		= function(obj, mode){
			try{	
				$(pr_divContent).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_OFF_ENT_TAB_DETAIL, obj));
				App.controller.UI.Main.do_lc_bind_event_resize();
				do_bind_event(obj);
			}catch(e) {
				do_gl_show_Notify_Msg_Error("JobOffEntTabDetail: " + e.toString());
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "job.off", "JobOffEntTabDetail", "do_lc_show", e.toString()) ;
			}
		}
		
		function do_bind_event(obj){
			if(obj.val03){
				var nb = parseInt(obj.val03);
				if(nb == 1){
					$("#halfday_start")	.prop( "checked", true );
				} else if(nb == 2){
					$("#halfday_end")	.prop( "checked", true );
				} else if(nb == 3){
					$("#halfday_start")	.prop( "checked", true );
					$("#halfday_end")	.prop( "checked", true );
				}
			}
			
			$('input#halfday_start').change(function(){
				App.data.count = 0;
				do_calculate_date_lc(false);
			});
			$('input#halfday_end').change(function(){
				App.data.count = 0;
				do_calculate_date_lc(false);
			});
			$('input.datepicker').change(function(){
				do_calculate_date_lc(false);
			});
		}
		
		this.do_calculate_date = function(){
			return do_calculate_date_lc(true);
		}
		
		function do_calculate_date_lc(checkCanSubmit){
			if(!checkCanSubmit){
				App.data.count +=1;
				if(App.data.count == 3){
					App.data.count = 0;
				}
				if(App.data.count != 1){
					return;
				}
			}
			var dt03 = $("#inp_dt03").val();
			if(dt03 == ""){
				if(checkCanSubmit) do_gl_show_Notify_Msg_Error($.i18n("job_off_msg_rp_dt03_missing"));
				return false;
			}
			
			var dt04 = $("#inp_dt04").val();
			if(dt04 == ""){
				if(checkCanSubmit) do_gl_show_Notify_Msg_Error($.i18n("job_off_msg_rp_dt04_missing"));
				return false;
			}
			
			var halfS = $('input#halfday_start').is(':checked');
			var halfE = $('input#halfday_end').is(':checked');

			var dS = convertToDateObj(dt03);	
			var dE = convertToDateObj(dt04);

			var s = dS.getTime();
			var e = dE.getTime();
			
			if(s==e){
				$("#halfday_end")	.prop( "checked", false );
				$("#div_halfday_end").hide();
				halfE = false;
			} else {
				$("#div_halfday_end").show();
			}
			
			//Avoid Summer, Winter time switch.
			var totalDay 	= parseFloat(parseInt((e - s + 5*3600*1000)/(24*3600*1000)) + 1);
			
			if(totalDay < 0){
				do_gl_show_Notify_Msg_Error($.i18n("job_off_msg_rp_daterang_error"));
				return false;
			}
			
			var val03 		= 0;
			if(halfS){
				totalDay = totalDay - 0.5;
				val03 += 1;
			}
			
			if(halfE){
				totalDay = totalDay - 0.5;
				val03 += 2;
			}
			$("#inp_val03").val(val03);
			
			if (totalDay > 28){
				do_gl_show_Notify_Msg_Error($.i18n("job_off_msg_rp_daterang_plus28"));
				$("#inp_val01").val("");
				return false;
			}
			
			$("#inp_val01").val(totalDay);
			
			var wdConfig = App.data.selectedUser.wdConfig;

			//TO DO: Calculate Day real;
			$("#inp_val02").val(totalDay);
			
			return true;
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
	};

	return JobOffEntTabDetail;
});