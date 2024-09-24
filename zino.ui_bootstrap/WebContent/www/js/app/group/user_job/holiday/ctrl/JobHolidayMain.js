define([
        'jquery',
        
        'text!group/user_job/holiday/tmpl/JobHoliday_Main.html',
        
        'group/user_job/holiday/ctrl/JobHolidayList',
        'group/user_job/holiday/ctrl/JobHolidayEnt',
        'group/user_job/holiday/ctrl/JobHolidayEntHeader',
        'group/user_job/holiday/ctrl/JobHolidayEntBtn',
        'group/user_job/holiday/ctrl/JobHolidayEntTabs',       
        'group/user_job/holiday/ctrl/JobHolidayEntTab01',
        'group/user_job/holiday/ctrl/JobHolidayRights'  
       
      
        ],
        function($,         		
        		JobHoliday_Main, 
        		
        		JobHolidayList, 
        		JobHolidayEnt, 
        		JobHolidayEntHeader, 
        		JobHolidayEntBtn, 
        		JobHolidayEntTabs, 
        		JobHolidayEntTab01,
        		JobHolidayRights
        ) {

	var JobHolidayMain 	= function (grpName, header, content, footer) {
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
		var RIGHT_U_G				= 2001001;
		var RIGHT_U_N				= 2001002;
		var RIGHT_U_M				= 2001003;
		var RIGHT_U_D				= 2001004;
		
		this.var_lc_MODE_INIT 		= 0;
		this.var_lc_MODE_NEW 		= 1; //duplicate is the mode new after clone object
		this.var_lc_MODE_MOD 		= 2;
		this.var_lc_MODE_DEL 		= 3;	
		this.var_lc_MODE_SEL 		= 5;
	
		
		//---------------------------------------------------------------
		
					
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			
			tmplName.JOB_HOLIDAY_MAIN			= "JobHoliday_Main";
			tmplName.JOB_HOLIDAY_LIST			= "JobHoliday_List";
			tmplName.JOB_HOLIDAY_LIST_HEADER	= "JobHoliday_ListHeader";
			tmplName.JOB_HOLIDAY_LIST_CONTENT	= "JobHoliday_ListContent";
			tmplName.JOB_HOLIDAY_ENT			= "JobHoliday_Ent";
			tmplName.JOB_HOLIDAY_ENT_BTN		= "JobHoliday_EntBtn";
			tmplName.JOB_HOLIDAY_ENT_HEADER		= "JobHoliday_EntHeader";
			tmplName.JOB_HOLIDAY_ENT_TABS		= "JobHoliday_EntTabs";
			tmplName.JOB_HOLIDAY_ENT_TAB_01		= "JobHoliday_EntTab01";
			
			if (!App.controller.JobHoliday)				
				 App.controller.JobHoliday				= {};
			
			if (!App.controller.JobHoliday.Main)	
				App.controller.JobHoliday.Main 			= this; //important for other controller can get ref, when new this controller,
			
			if (!App.controller.JobHoliday.List			)  
				 App.controller.JobHoliday.List			= new JobHolidayList			("#div_JobHoliday_List", "#div_JobHoliday_List_Header", "#div_JobHoliday_List_Content");				
			if (!App.controller.JobHoliday.Ent			)  
				 App.controller.JobHoliday.Ent			= new JobHolidayEnt				(null, "#div_JobHoliday_Ent", null);
			if (!App.controller.JobHoliday.EntBtn		)  
				 App.controller.JobHoliday.EntBtn		= new JobHolidayEntBtn			(null, "#div_JobHoliday_Ent_Btn", null);
			if (!App.controller.JobHoliday.EntHeader	)  
				 App.controller.JobHoliday.EntHeader	= new JobHolidayEntHeader		(null, "#div_JobHoliday_Ent_Header", null);
			if (!App.controller.JobHoliday.EntTabs		)  
				 App.controller.JobHoliday.EntTabs		= new JobHolidayEntTabs			(null, "#div_JobHoliday_Ent_Tabs", null);
			if (!App.controller.JobHoliday.Rights)	
				 App.controller.JobHoliday.Rights 		= new JobHolidayRights			();
			
			//----------tab name----------------------------------------------------------------------------------------
			if (!App.controller.JobHoliday.EntTab01)  
				 App.controller.JobHoliday.EntTab01= new JobHolidayEntTab01	(null, "#div_JobHoliday_Ent_Tab_01", null);
			//--------------------------------------------------------------------------------------------------
			
			self.var_lc_URL_Aut_Header				= req_gl_LS_SecurityHeaderBearer(App.keys.KEY_STORAGE_CREDENTIAL);
			
			App.controller.JobHoliday.List			.do_lc_init();
			App.controller.JobHoliday.Ent			.do_lc_init();
			App.controller.JobHoliday.EntBtn		.do_lc_init();
			App.controller.JobHoliday.EntHeader		.do_lc_init();
			App.controller.JobHoliday.EntTabs		.do_lc_init();
			App.controller.JobHoliday.EntTab01		.do_lc_init();
			
//			do_Get_Cfg_Values(); //begin by fetching all mandatory values
			pr_ctr_Main 			= App.controller.JobHoliday.Main;
			pr_ctr_Rights			= App.controller.JobHoliday.Rights;
			
		}
		
		this.do_lc_show = function(){
			try { 
				self.var_lc_URL_Aut_Header				= req_gl_LS_SecurityHeaderBearer(App.keys.KEY_STORAGE_CREDENTIAL);
				
				var rightCode = pr_ctr_Rights.req_lc_Right(RIGHT_U_G);
				if(rightCode == -1){
					do_gl_show_Notify_Msg_Error($.i18n("job_report_msg_user_right_error"));
					return;
				}
						
				tmplCtrl.do_lc_put_tmpl(tmplName.JOB_HOLIDAY_MAIN	, JobHoliday_Main); 
				$(pr_divContent).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_HOLIDAY_MAIN, {}));	
				
				
				self.do_lc_show_lst_jobholiday();
				
				App.controller.JobHoliday.Ent	.do_lc_show(null);	//init: obj is null	
				
				do_gl_init_Resizable("#div_JobHoliday_List");		
				App.controller.UI.Main.do_bind_event_btn_vertical_list('#div_JobHoliday_List', '#div_JobHoliday_Ent');
				App.controller.UI.Main.do_lc_bind_event_resize();
				App.controller.UI.Main.do_lc_bind_event_minimize('#div_JobHoliday_List', '#div_JobHoliday_Ent');	
			
				doBindings();
			
			}catch(e) {				
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, self.var_lc_URL_Aut_Header, App.network, "job.holiday", "JobHolidayMain", "do_lc_show", e.toString()) ;
			}

		};
		
		
		var doBindings = function() {
        	do_gl_bindingAppLTE();
        }.bind(this);
		
        
        this.do_lc_show_lst_jobholiday = function(){
        	App.controller.JobHoliday.List	.do_lc_show();
				
			if(App.data.user.manId == 1 && (App.data.user.typ==2 || App.data.user.typ==10)){
				App.controller.JobHoliday.List	.do_lc_show_all_partner();
			}
		}
        
		this.do_lc_binding_pages = function(div) {
			try {
				if(div.length>0) do_gl_enhance_within(div);
			} catch (e) {
				// do_gl_show_Notify_Msg_Error(null, e);
			}
		};

		//---------private-----------------------------------------------------------------------------
		var do_Get_Cfg_Values= function(){
			//ajax to get all fix values here			
			var ref 		= req_gl_Request_Content_Send('SVCfgClass', 'SVCfgService');			
		
			var fSucces		= [];		
			fSucces.push(req_gl_funct(App, App.funct.put, ['cfg_value_01']));	
			
			var fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error [$.i18n("common_err_ajax"), 0]);	
			
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, self.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError) ;	
		}	

		//--------------------------------------------------------------------------------------------
	};

	return JobHolidayMain;
  });