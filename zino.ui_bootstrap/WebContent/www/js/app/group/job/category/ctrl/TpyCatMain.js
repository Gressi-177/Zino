define([
        'jquery',
        
        'text!group/job/category/tmpl/TpyCat_Main.html',
        'text!group/job/category/tmpl/TpyCat_List.html',
        
        'group/job/category/ctrl/TpyCatList',
        'group/job/category/ctrl/TpyCatEnt',
        'group/job/category/ctrl/TpyCatEntHeader',
        'group/job/category/ctrl/TpyCatEntBtn',
        'group/job/category/ctrl/TpyCatRights' 
        ],
        function($,         		
        		TpyCat_Main,
        		TpyCat_List,
        		
        		TpyCatList, 
        		TpyCatEnt, 
        		TpyCatEntHeader, 
        		TpyCatEntBtn,
        		TpyCatRights
        ) {

	var TpyCatMain 	= function (grpName, header, content, footer) {
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
		var RIGHT_U_G				= 7000001;
		var RIGHT_U_N				= 7000002;
		var RIGHT_U_M				= 7000003;
		var RIGHT_U_D				= 7000004;

		this.var_lc_MODE_INIT 		= 0;
		this.var_lc_MODE_NEW 		= 1; //duplicate is the mode new after clone object
		this.var_lc_MODE_MOD 		= 2;
		this.var_lc_MODE_DEL 		= 3;	
		this.var_lc_MODE_SEL 		= 5;
	
		
		//---------------------------------------------------------------
		this.var_lc_PAR_TYPE_PROJECT_REPORT 		= 2002;
		//---------------------------------------------------------------
		this.pr_right_soc_manage	= [30002001, 30002002, 30002003, 30002004, 30002005];
		//---------------------------------------------------------------
		var societeListChild		= 1010011;
		var societeCompany			= 1010010;
					
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			

			tmplName.TPY_CAT_MAIN			= "TpyCat_Main";
			tmplName.TPY_CAT_LIST			= "TpyCat_List";
			
			tmplName.TPY_CAT_PROJECT_REPORT_LIST_CONTENT	= "TpyCat_Project_Report_List_Content";
			
			tmplName.TPY_CAT_ENT					= "TpyCat_Ent";
			tmplName.TPY_CAT_ENT_BTN				= "TpyCat_EntBtn";
			tmplName.TPY_CAT_ENT_HEADER				= "TpyCat_EntHeader";

			
			if (!App.controller.TpyCat)				
				 App.controller.TpyCat				= {};
			
			if (!App.controller.TpyCat.Main)	
				App.controller.TpyCat.Main 			= this; //important for other controller can get ref, when new this controller,
			
			if (!App.controller.TpyCat.List			)  
				 App.controller.TpyCat.List			= new TpyCatList			(grpName, "#div_TpyCat_List", "#div_TpyCat_List_Header", "#div_TpyCat_List_Content");				
			if (!App.controller.TpyCat.Ent			)  
				 App.controller.TpyCat.Ent			= new TpyCatEnt				(grpName, null, "#div_TpyCat_Ent", null);
			if (!App.controller.TpyCat.EntBtn		)  
				 App.controller.TpyCat.EntBtn		= new TpyCatEntBtn			(grpName, null, "#div_TpyCat_Ent_Btn", null);
			if (!App.controller.TpyCat.EntHeader	)  
				 App.controller.TpyCat.EntHeader	= new TpyCatEntHeader		(grpName, null, "#div_TpyCat_Ent_Header", null);
//			if (!App.controller.TpyCat.EntTabs		)  
//				 App.controller.TpyCat.EntTabs		= new TpyCatEntTabs			(null, "#div_TpyCat_Ent_Tabs", null);
			
			if (!App.controller.TpyCat.Rights)	
				 App.controller.TpyCat.Rights 		= new TpyCatRights		();
			
			//----------tab name----------------------------------------------------------------------------------------
//			if (!App.controller.TpyCat.EntTab01)  
//				 App.controller.TpyCat.EntTab01= new TpyCatEntTab01	(null, "#div_TpyCat_Ent_Tab_01", null);
			//--------------------------------------------------------------------------------------------------
			
			self.var_lc_URL_Aut_Header				= req_gl_LS_SecurityHeaderBearer (App.keys.KEY_STORAGE_CREDENTIAL);
			
			App.controller.TpyCat.List			.do_lc_init();
			App.controller.TpyCat.Ent			.do_lc_init();
			App.controller.TpyCat.EntBtn		.do_lc_init();
			App.controller.TpyCat.EntHeader		.do_lc_init();
//			App.controller.TpyCat.EntTabs		.do_lc_init();
//			App.controller.TpyCat.EntTab01		.do_lc_init();
			
			pr_ctr_Rights			= App.controller.TpyCat.Rights;
		}
		
		//---------------------------------------------------------------------
		var pr_grpPath 		= 'group/job/category';
		var pr_showed		= false;
		this.do_lc_show = function(sharedJson){
			if (!pr_showed){
				do_gl_lang_append (pr_grpPath + '/transl', self.do_lc_show_callback, [sharedJson]);
				pr_showed = true;
			}else {
				self.do_lc_show_callback(sharedJson);
			}
		};
		
		this.do_lc_show_callback = function(sharedJson){
			try { 
				
				var rightCode = pr_ctr_Rights.req_lc_Right(RIGHT_U_G);
				if(rightCode == -1){
					do_gl_show_Notify_Msg_Error($.i18n("job_report_msg_user_right_error"));
					return;
				}
							
				tmplCtrl.do_lc_put_tmpl(tmplName.TPY_CAT_MAIN	, TpyCat_Main); 
				tmplCtrl.do_lc_put_tmpl(tmplName.TPY_CAT_LIST	, TpyCat_List); 
				$(pr_divContent)		.html(tmplCtrl.req_lc_compile_tmpl(tmplName.TPY_CAT_MAIN, {}));	
				do_gl_apply_right($(pr_divContent));
				
				$("#div_TpyCat_List")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.TPY_CAT_LIST, {}));
				
//				App.controller.TpyCat.List	.do_lc_show();
				self.do_lc_show_lst();
				
				App.controller.TpyCat.Ent	.do_lc_show(null);	//init: obj is null	
				
				do_gl_init_Resizable("#div_TpyCat_List");
				App.controller.DBoard.DBoardMain.do_bind_event_btn_vertical_list('#div_TpyCat_List', '#div_TpyCat_Ent');
				App.controller.DBoard.DBoardMain.do_lc_bind_event_resize();
				App.controller.DBoard.DBoardMain.do_lc_bind_event_minimize('#div_TpyCat_List', '#div_TpyCat_Ent');
							
			}catch(e) {				
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, self.var_lc_URL_Aut_Header, App.network, "tpy.cat", "TpyCatMain", "do_lc_show", e.toString()) ;
			}
			do_gl_enhance_within($(pr_divContent));
		};
		
		this.do_lc_show_lst = function(){
			App.data["catsList"] = {};
			App.controller.TpyCat.List.do_lc_show("#div_TpyCat_Project_Report_List"	, self.var_lc_PAR_TYPE_PROJECT_REPORT);
		}
		
		this.do_lc_binding_pages = function(div) {
			try {
				if(div.length>0) do_gl_enhance_within(div);
			} catch (e) {
				do_gl_show_Notify_Msg_Error(null, e);
			}
		}
		
		this.do_verify_user_right_soc_manage = function(){
			for(var i = 0; i< this.pr_right_soc_manage.length; i++){
				if(App.data.user.rights.includes(this.pr_right_soc_manage[i]))
					return true;
			}
			return false;
		}

		//--------------------------------------------------------------------------------------------
		//--------------------------------------------------------------------------------------------
		this.do_show_Msg= function(sharedJson, msg){
			//alert(msg);
			console.log("do_show_Msg::" + msg);
		}		
			
	};

	return TpyCatMain;
  });