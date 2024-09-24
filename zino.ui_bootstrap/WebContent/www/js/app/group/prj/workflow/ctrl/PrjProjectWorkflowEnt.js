define([
	'text!group/prj/workflow/tmpl/PrjWorkflow_Ent.html',

	'group/prj/workflow/ctrl/PrjProjectWorkflowEntTabLink',
	'group/prj/workflow/ctrl/PrjProjectWorkflowEntTabLinkPrj',
	'group/prj/workflow/ctrl/PrjProjectWorkflowEntTab',
	
	'group/prj/treeview/ctrl/TreeViewEntMsgbox',
	],
	function(	
			PrjWorkflow_Ent,

			PrjProjectWorkflowEntTabLink,
			PrjProjectWorkflowEntTabLinkPrj,
			{
				PrjProjectEntTabMember, 
				PrjProjectEntTabMemberGroup, 
				PrjProjectEntTabDoc, 
				PrjProjectEntTabComment, 
				PrjProjectEntTabContent
			},
			TreeViewEntMsgbox
	){

	var PrjProjectEnt 	= function (grpName, header, content, footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;

		const pr_SERVICE_CLASS			= "ServicePrjProject"; //to change by your need
		const pr_SV_GET					= "SVGet";
		const pr_SV_NEW_FAVORITE		= "SVNewFavorite";
		const pr_SV_REMOVE_FAVORITE		= "SVRemoveFavorite";

		var self 						= this;
		this.pr_member_role				= null;

		const pr_member_lev_manager 	= 0;
		const pr_member_lev_reporter 	= 1;
		const pr_member_lev_worker 		= 2;
		const pr_member_lev_watcher 	= 3;

		const pr_ID_TABLE_PRJ			= 250000;
		
		const  pr_TYP00_SPRINT          = 10;
		
		var pr_DIV_CONTENT               = "#div_task_content_main";
		//------------------controllers---------------
		//------------------controllers------------------------------------------------------
		const pr_project				= App.controller.UI;
		var pr_ctr_Main 				= null;
		var pr_ctr_Sidebar 				= null;
		var pr_ctr_Fav 					= null;
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			if(!tmplName){
				App.template.names[pr_grpName] = {};
				tmplName = App.template.names[pr_grpName];
			}
			
			pr_ctr_Main 				= pr_project.Main;
			pr_ctr_Sidebar 				= pr_project.Sidebar;
			pr_ctr_Fav					= pr_project.Fav;

			tmplName.PRJ_PROJECT_ENT	= "PrjWorkflow_Ent";
		}

		//---------show-----------------------------------------------------------------------------
		this.do_lc_show = function(id, mode){               
			try{
				let params = req_gl_Url_Params(App.data.url?App.data.url:decodeURIComponent(window.location.search.substring(1)));
				if(!id) id = params.id;
				if (id){
					pr_isViewSprint  = true;
					do_lc_load_view();
					do_lc_get_prj(id);
					do_lc_bind_event();
				}else{
					pr_ctr_Main.do_lc_switch_mobile_or_pc(`view_prj_project_list.html`, "VI_MAIN/"+ App.router.part.PRJ_PROJECT_LIST);
				}
			}catch(e) {				
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "prj.project", "PrjProjectEnt", "do_lc_show", e.toString()) ;
			}
		};
		
		this.can_lc_role_user_manager = function() {
			return this.pr_member_role && this.pr_member_role == pr_member_lev_manager;
		}
		
		this.can_lc_role_user_reporter = function() {
			return this.pr_member_role && this.pr_member_role == pr_member_lev_reporter;
		}
		
		this.can_lc_role_user_worker = function() {
			return this.pr_member_role && this.pr_member_role == pr_member_lev_worker;
		}

		this.do_lc_reqRole_User = function(){
			// if(pr_isViewPopup){
			// 	$(".isManager").remove();
			// 	$(".isWatcher").remove();
			// 	$(".info-content").off("click").removeClass("info-content");
			// 	$(".info-edit").off("click").removeClass("info-edit");
			// 	if($('.td-task').hasClass("ui-draggable")) $('.td-task').draggable( "destroy" );
				
			// 	$("#div_content-left").removeClass("col-xl-8").addClass("col-xl-12");
			// 	$("#div_content-right").hide();
			// 	$("#div_prj_content_header").hide();
			// }
			
			let uRole = self.pr_member_role;
			if(uRole === null || uRole === undefined){
				do_gl_init_msgbox_annonce($.i18n("prj_project_not_right_view"), do_lc_back_to_list);
			}
			if(uRole == pr_member_lev_manager){
				$(".isWorker").remove();
				$(".isWatcher").remove();
				$("#btn_add_chk_lst").removeClass("hide");
			}else if(uRole == pr_member_lev_reporter){
				$(".isManager")			.not(".isReporter").remove();
				$(".info-content")		.not(".isReporter").off("click").removeClass("info-content");
				$("#btn_add_chk_lst")	.removeClass("hide");
				$('.td-task')			.draggable( "destroy" );
			}else if(uRole == pr_member_lev_worker){
				$(".isManager").remove();
				$(".isWatcher").remove();
				$(".info-content").off("click").removeClass("info-content");
				if($('.td-task').hasClass("ui-draggable")) $('.td-task').draggable( "destroy" );
			}else if(uRole == pr_member_lev_watcher){
				$(".isManager").remove();
				$(".isWorker").remove();
				$(".info-content").off("click").removeClass("info-content");
				if($('.td-task').hasClass("ui-draggable")) $('.td-task').draggable( "destroy" );
			}else{
				do_gl_init_msgbox_annonce($.i18n("prj_project_not_right_view"), do_lc_back_to_list);
			}
		}

		this.do_lc_reqRole_User_Workflow = function(){
		
			let uRole = self.pr_member_role;
			
			if(uRole == pr_member_lev_manager){

			}else if(uRole == pr_member_lev_reporter){

			}else if(uRole == pr_member_lev_worker){
				$("#btn_add_member").remove();
				$("#btn_add_link").remove();
				$("#btn_add_link_prj").remove();

				$("#btn_delete").remove();
				$("#btn_edit_wf").remove();
			}else if(uRole == pr_member_lev_watcher){
				$("#btn_add_member").remove();
				$("#btn_add_link").remove();
				$("#btn_add_link_prj").remove();

				$("#btn_delete").remove();
				$("#btn_edit_wf").remove();
			}
		}

		const do_lc_load_view = () => {
			tmplCtrl.do_lc_put_tmpl(tmplName.PRJ_PROJECT_ENT, PrjWorkflow_Ent);
		}

		const do_lc_get_prj = id => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET, {id});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_getPrj_response, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		const do_lc_getPrj_response = sharedJson => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				let data 				= sharedJson[App['const'].RES_DATA];
				data.userRole			= data.userRole [App.data.user.id];
				self.pr_member_role 	= data.userRole;

				do_lc_init_ctrl_module();
				do_lc_show_prj(data);
				do_lc_get_element_ofPrj(data);
			} else {
				do_gl_init_msgbox_annonce($.i18n("prj_project_not_right_view"), do_lc_back_to_list);
			}
		}

		const do_lc_init_ctrl_module = () => {
			let pr_LST_MODULE = {
					EntMember 		: PrjProjectEntTabMember, 
					// EntMemberGroup 	: PrjProjectEntTabMemberGroup,
					EntComment 		: PrjProjectEntTabComment	,
					EntContent 		: PrjProjectEntTabContent	,
					EntLink			: PrjProjectWorkflowEntTabLink,
					EntLinkPrj		: PrjProjectWorkflowEntTabLinkPrj,
					// EntDoc 			: PrjProjectEntTabDoc
			}

			for(let ctrlName in pr_LST_MODULE){
				if(!pr_project[ctrlName])		pr_project[ctrlName] 		= new pr_LST_MODULE[ctrlName](null, null, null);
			}
		}

		const do_lc_show_prj = prj => {
			let isFavorite 	= null;
			if(App.data.user && App.data.user['lstFav'] && App.data.user['lstFav'][pr_ID_TABLE_PRJ]) {
				const lstFav 	=  App.data.user['lstFav']
	
				if(lstFav[pr_ID_TABLE_PRJ].ids.includes(prj.id)) isFavorite = true;
			}

			prj.isFavorite = isFavorite
			$(pr_DIV_CONTENT)	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT, {}));

			pr_project.EntContent	.do_lc_show_prj_content(prj, null, pr_isViewSprint);
			do_lc_bind_event(prj);
			
			// do_lc_show_epic_task_prj(prj);
			// pr_project.EntDoc		.do_lc_show_prj_docs(prj);
			// pr_project.EntEval		.do_lc_get_prj_evaluation(prj, pr_isViewSprint);
		}
		
		const do_lc_get_element_ofPrj = prj => {
			pr_project.EntMember		.do_lc_get_list_member(prj);
			pr_project.EntComment		.do_lc_get_list_comments(prj.id);
			// pr_project.EntMemberGroup	.do_lc_get_list_member(prj);

			pr_project.EntLink			.do_lc_get_list_member(prj);
			pr_project.EntLinkPrj		.do_lc_get_list_member(prj);
		}

		const do_lc_bind_event = prj => {
			$("#btn_mod_favorite").off("click").on("click", function() {
				const isFav = $(this).hasClass("isFavorite");

				do_lc_mod_favorite(isFav, prj);
			})

		}
		
		//start drag drop epic, task
		this.do_lc_show_epic_task = prj => {
			do_lc_show_epic_task_prj(prj);
		}
		const do_lc_show_epic_task_prj = prj => {
			pr_project.EntEpic		.do_lc_show_prj_epic(prj);
			pr_project.EntTask		.do_lc_show_prj_task(prj, pr_isViewSprint);
		}
		//end drag drop epic, task
		
		//start favorite
		const do_lc_mod_favorite = (isFav, prj) => {
			if(!isFav) {
				do_lc_send_mod_inList(isFav, pr_ID_TABLE_PRJ, prj)

				return
			}
			App.MsgboxController.do_lc_show({
				title 		: $.i18n("prj_project_favorite_title_delete"),
				content 	: $.i18n("prj_project_favorite_title_delete_content"),
				autoclose	: false,
				buttons 	: {
					UPDATE : {
						lab 		: $.i18n("common_btn_send"),
						funct 		: do_lc_send_mod_inList,
						param 		: [isFav, pr_ID_TABLE_PRJ, prj],
						classBtn	: "btn-primary",
						autoclose	: true
					},
					CALCEL : {
						lab 		: $.i18n("common_btn_cancel"),
					}
				}
			});
		}

		const do_lc_send_mod_inList = (isFav, parTyp, prj) => {
			if(isFav) pr_ctr_Fav.do_lc_remove_myFavorites(prj, parTyp)
			else pr_ctr_Fav.do_lc_push_myFavorites(prj, parTyp)

			do_lc_show_prj(prj);
			do_lc_get_element_ofPrj(prj);

			pr_ctr_Sidebar.do_lc_show_favorite()
		}
		//end favorite
		
		const do_lc_back_to_list = () => {
			if(pr_isViewSprint){
				pr_ctr_Main.do_lc_switch_mobile_or_pc(`view_prj_workflow.html`, "VI_MAIN/"+ App.router.part.PRJ_WORKFLOW);
				return;
			}
			pr_ctr_Main.do_lc_switch_mobile_or_pc(`view_prj_project_list.html`, "VI_MAIN/"+ App.router.part.PRJ_PROJECT_LIST);
		}
	};

	return PrjProjectEnt;
});