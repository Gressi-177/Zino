define([
	'group/prj/workflow/ctrl/PrjProjectWorkflowList',
	'group/prj/workflow/ctrl/PrjProjectWorkflowEnt',
	
	'text!group/prj/workflow/tmpl/PrjWorkflow_Main.html',
	], function(
			PrjProjectWorkflowList,
			PrjProjectWorkflowEnt,

			PrjWorkflow_Main) {

	var PrjProjectWorkflowMain     	= function (grpName, header, content, footer) {
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName					= App.template.names[pr_grpName];
		const tmplCtrl				= App.template.controller;
		var self 					= this;
		var pr_GROUP				= null;
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			if(!tmplName){
				App.template.names[pr_grpName] = {};
				tmplName = App.template.names[pr_grpName];
			}
			if (!App.controller.PrjWorkflow) App.controller.PrjWorkflow = {};
			if (!App.controller.PrjProject) App.controller.PrjProject 	= {};
			
			if (!App.controller.PrjWorkflow.Main)  
				App.controller.PrjWorkflow.Main				= this;
			
			if (!App.controller.PrjWorkflow.List)  
				App.controller.PrjWorkflow.List				= new PrjProjectWorkflowList		(null, null, null);
			
			if (!App.controller.PrjWorkflow.Ent)  
				App.controller.PrjWorkflow.Ent				= new PrjProjectWorkflowEnt			(null, null, null);
			
			
			App.controller.PrjWorkflow.List					.do_lc_init();
			App.controller.PrjWorkflow.Ent					.do_lc_init();

			tmplName.PRJ_WORKFLOW_MAIN						= "PrjWorkflow_Main";
			tmplCtrl										.do_lc_put_tmpl(tmplName.PRJ_WORKFLOW_MAIN, PrjWorkflow_Main); 
		}     
		
		this.do_lc_show		= function(){
			try { 
				let params = req_gl_Url_Params(App.data.url?App.data.url:decodeURIComponent(window.location.search.substring(1)));
				let groupId = params.groupId;
				if(groupId) pr_GROUP = parseInt(groupId);
				
				$("#div_main_content")			.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_WORKFLOW_MAIN, {}));

				App.controller.PrjWorkflow.List.do_lc_show(pr_GROUP);
			}catch(e) {				
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, self.var_lc_URL_Aut_Header, App.network, "prj.chatRoom", "PrjProjectWorkflowMain", "do_lc_show", e.toString()) ;
			}
		};
		
		//--------------------------------------------------------------------------------------------
		this.do_lc_show_workflow = function(wf) {
			do_gl_show_workflow(wf, tmplCtrl, tmplName );
		}
		
		this.req_lc_save_workflow = (wf) => {
			return req_gl_save_workflow(wf);
		}

		
	};

	return PrjProjectWorkflowMain;
});