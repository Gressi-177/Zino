define([
        'jquery',
        'text!group/job/day_off/tmpl/JobOff_Ent_Tabs.html'
        ],
        function($, 
        		JobOff_Ent_Tabs        		
        		) {

	var JobOffEntTabs     = function (grpName, header, content, footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;

		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		//------------------controllers------------------------------------------------------
		var pr_ctr_EntTabs 			= null;
		var pr_ctr_EntTabDetail 	= null;
		var pr_ctr_EntTabDoc 		= null;
		
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			pr_ctr_EntTabs 			= App.controller.JobOff.EntTabs;
			pr_ctr_EntTabDetail 	= App.controller.JobOff.EntTabDetail;
			pr_ctr_EntTabDoc		= App.controller.JobOff.EntTabDoc;

			if(!tmplName) {
				App.template.names[pr_grpName] = {}
				tmplName 	= App.template.names[pr_grpName]
			}

			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_OFF_ENT_TABS, JobOff_Ent_Tabs); 	
		}     
		this.do_lc_show		= function(obj, mode){
			try{		
				$(pr_divContent).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_OFF_ENT_TABS, obj));				
				pr_ctr_EntTabDetail	.do_lc_show(obj, mode);
				pr_ctr_EntTabDoc	.do_lc_show(obj, mode);

			}catch(e) {
				do_gl_show_Notify_Msg_Error("JobOff > Tabs :" + e.toString());
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "job.off", "JobOffEntTabs", "do_lc_show", e.toString()) ;
			}
		};
	};
	
	return JobOffEntTabs;
});