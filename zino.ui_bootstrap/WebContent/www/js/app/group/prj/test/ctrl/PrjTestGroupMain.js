define([
	'group/prj/test/ctrl/PrjTestGroupList',
	'group/prj/test/ctrl/PrjTestGroupEnt',
	
	'text!group/prj/test/tmpl/PrjTestGroup_Main.html',
	], function(
			PrjTestGroupList,
			PrjTestGroupEnt,

			PrjTestGroup_Main) {

	var PrjTestGroupMain     			= function (grpName, header, content, footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		var self 					= this;
		var pr_GROUP				= null;
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			if (!App.controller.PrjTestGroup) App.controller.PrjTestGroup = {};
			
			if (!App.controller.PrjTestGroup.Main)  
				App.controller.PrjTestGroup.Main				= this;
			
			if (!App.controller.PrjTestGroup.List)  
				App.controller.PrjTestGroup.List				= new PrjTestGroupList		(pr_grpName, null, null, null);
			
			if (!App.controller.PrjTestGroup.Ent)  
				App.controller.PrjTestGroup.Ent					= new PrjTestGroupEnt		(pr_grpName, null, null, null);
			
			
			App.controller.PrjTestGroup.List				.do_lc_init();
			App.controller.PrjTestGroup.Ent					.do_lc_init();

			if(!tmplName) {
				App.template.names[pr_grpName] = {}
				tmplName = App.template.names[pr_grpName]
			}
			
			tmplName.PRJ_TESTGRP_MAIN						= "PrjTestGroup_Main";
			tmplCtrl										.do_lc_put_tmpl(tmplName.PRJ_TESTGRP_MAIN, PrjTestGroup_Main); 
		}     
		
		this.do_lc_show		= function(){
			try { 
				App.router.controller.do_lc_append_custom_tags()
				
				let groupId = req_gl_Url_Params(location.href).groupId;
				
				if(groupId) pr_GROUP = parseInt(groupId);
				
				$("#div_main_content")			.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_TESTGRP_MAIN, {}));

				App.controller.PrjTestGroup.List.do_lc_show(pr_GROUP);
			}catch(e) {				
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, self.var_lc_URL_Aut_Header, App.network, "prj.chatRoom", "PrjTestGroupMain", "do_lc_show", e.toString()) ;
			}
		};
		
	};

	return PrjTestGroupMain;
});