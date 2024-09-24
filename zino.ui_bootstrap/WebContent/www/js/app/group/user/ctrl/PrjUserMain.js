define([
	'group/user/ctrl/PrjUserList02',
	'group/user/ctrl/PrjUserEnt',
	'group/dashboard/ctrl/PrjDashboardEnt',
	
	'text!group/user/tmpl/PrjUser_Main.html',
	], function(
			PrjUserList02,
			PrjUserEnt,
			PrjDashboardEnt,
			
			PrjUser_Main) {

	var PrjUserMain     			= function (grpName, header, content, footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		
		var self 					= this;
		var var_lc_TYPE_SHOW        = null;
		var var_lc_GROUP_ID         = null;
		
		var RIGHT_U_G	        	= 1000001;
		var RIGHT_ADM	        	= 100;
		var RIGHT_A_G	        	= 101;

		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			if(!tmplName) {
				App.template.names[pr_grpName] = {}
				tmplName = App.template.names[pr_grpName]
			}
			
			if (!App.controller.PrjUser) App.controller.PrjUser = {};
			
			
			if (!App.controller.PrjUser.List)  
				App.controller.PrjUser.List				= new PrjUserList02		(null, null, null);
			
			if (!App.controller.PrjUser.Ent)  
				App.controller.PrjUser.Ent				= new PrjUserEnt		(null, null, null);
			
			if (!App.controller.PrjUser.Dashbord)  
				App.controller.PrjUser.Dashbord			= new PrjDashboardEnt	(null, null, null);
			
			App.controller.PrjUser.List					.do_lc_init();
			App.controller.PrjUser.Ent					.do_lc_init();
			App.controller.PrjUser.Dashbord				.do_lc_init();
			
			tmplName.PRJ_USER_MAIN = "PrjUser_Main";
			tmplCtrl.do_lc_put_tmpl(tmplName.PRJ_USER_MAIN, PrjUser_Main); 
		}     
		
		//--------show-------------------------------------------------------------------
		var pr_grpPath 		= 'group/user';
		var pr_showed		= false;
		this.do_lc_show = function(){
			if (!pr_showed){
				do_gl_lang_append (pr_grpPath + '/transl', self.do_lc_show_callback, []);
				pr_showed = true;
			}else {
				self.do_lc_show_callback();
			}
		};
		
		this.do_lc_show_callback = function(){    
			try { 
				App.router.controller.do_lc_append_custom_tags()
				
				var listUserRight = App.data.user.rights;
				if(!listUserRight){
					do_gl_show_Notify_Msg_Error($.i18n("prj_project_not_right_view"));
					return;
				}
				
				var isRight = listUserRight.includes(RIGHT_U_G) || listUserRight.includes(RIGHT_ADM)|| listUserRight.includes(RIGHT_A_G);
				if(!isRight){
					do_gl_show_Notify_Msg_Error($.i18n("prj_project_not_right_view"));
					return;
				}
				
				
				$("#div_main_content")			.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_USER_MAIN, {}));

				App.controller.PrjUser.List.do_lc_show("#div_user_list");

			}catch(e) {				
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, self.var_lc_URL_Aut_Header, App.network, "prj.chatRoom", "PrjUserMain", "do_lc_show", e.toString()) ;
			}
		}
		
	};

	return PrjUserMain;
});