define([
	'group/prj/sprint/ctrl/PrjProjectSprintList',
	'group/prj/project/ctrl/PrjProjectEnt',
	
	'text!group/prj/sprint/tmpl/PrjSprint_Main.html',
	], function(
			PrjProjectSprintList,
			PrjProjectEnt,

			PrjSprint_Main) {

	var PrjProjectSprintMain     	= function (grpName, header, content, footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		
		var self 					= this;
		var pr_GROUP				= null;
		var pr_CODE 				= null;
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			if(!tmplName){
				tmplName = App.template.names[pr_grpName] = {};
			}

			if (!App.controller.PrjSprint) 	App.controller.PrjSprint 	= {};
			if (!App.controller.PrjProject) App.controller.PrjProject 	= {};
			
			if (!App.controller.PrjSprint.Main)  
				App.controller.PrjSprint.Main				= this;
			
			if (!App.controller.PrjSprint.List)  
				App.controller.PrjSprint.List				= new PrjProjectSprintList		(null, null, null);
			
			if (!App.controller.PrjProject.Ent)  
				App.controller.PrjProject.Ent				= new PrjProjectEnt			(null, null, null);
			
			
			App.controller.PrjSprint.List					.do_lc_init();
			App.controller.PrjProject.Ent					.do_lc_init();
			
			if(!tmplName) {
				App.template.names[pr_grpName] = {}
				tmplName = App.template.names[pr_grpName]
			}

			tmplName.PRJ_SPRINT_MAIN						= "PrjSprint_Main";
			tmplCtrl										.do_lc_put_tmpl(tmplName.PRJ_SPRINT_MAIN, PrjSprint_Main); 
		}     
		
		var pr_grpPath 		= 'group/prj/_transl';
		var pr_showed		= false;
		this.do_lc_show = function(id, code){
			if (!pr_showed){
				do_gl_lang_append (pr_grpPath, self.do_lc_show_callback);
				pr_showed = true;
			}else {
				self.do_lc_show_callback();
			}
		};
		this.do_lc_show_callback		= function(){
			try { 
				let params              = req_gl_Url_Params();
				let {groupId, code}     = params;

				if(groupId) pr_GROUP 	= parseInt(groupId);
				if(code) 	pr_CODE 	= code;
				
				
				$("#div_main_content")			.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_SPRINT_MAIN, {}));

				App.controller.PrjSprint.List.do_lc_show(pr_GROUP, pr_CODE);
			}catch(e) {				
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, self.var_lc_URL_Aut_Header, App.network, "prj.chatRoom", "PrjProjectSprintMain", "do_lc_show", e.toString()) ;
			}
		};
		
	};

	return PrjProjectSprintMain;
});