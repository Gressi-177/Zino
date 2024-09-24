define([
	'text!group/home/tmpl/Main_Sidebar.html',
	],
	function(PrjProject_Sidebar) {

	var PrjProjectSidebar     = function (grpName, header,content,footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		
		var pr_ctr_Main 			= null;
		const pr_ROLE_ADMIN			= 1;
		
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			pr_ctr_Main 					= App.controller.UI.Main;
			
			if(!tmplName) {
				App.template.names[pr_grpName] = {}
				tmplName = App.template.names[pr_grpName]
			}
			
			tmplName.VI_SIDEBAR				= "VI_Sidebar";
			tmplCtrl.do_lc_put_tmpl(tmplName.VI_SIDEBAR	, PrjProject_Sidebar); 
		}      

		this.do_lc_show		= function(){
			try{
				do_lc_load_view();
				do_lc_build_page();
			}catch(e) {				
				console.log(e);
			}
		};

		var do_lc_load_view = function(){
			$("#side-menu-main"		).attr("href", "view_prj_email.html");
			$("#side-menu-main"		).parent().removeClass("li-has-menu");
			$("#menu-mail-mobile"	).addClass("hide");
		} 

		var do_lc_build_page = function(){
			let isAdmin = App.data.user.roles.includes(pr_ROLE_ADMIN);
			$("#div-menu-sidebar")			.html(tmplCtrl.req_lc_compile_tmpl(tmplName.VI_SIDEBAR	, {isAdmin}));
		} 
	};
	
	return PrjProjectSidebar;
});