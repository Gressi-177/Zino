define([
	'text!group/prj/project/tmpl/PrjProject_Ent.html',
	'text!group/prj/project/tmpl/PrjProject_Ent_New.html',
	
	'text!group/prj/project/tmpl/PrjProject_Ent_Content.html',
	
	'text!group/prj/project/tmpl/PrjProject_Ent_Tabs.html',
	
	'text!group/prj/project/tmpl/PrjProject_Ent_Tab_History.html',
	
	'text!group/prj/project/tmpl/PrjProject_Ent_Tab_Customer.html',
	'text!group/prj/project/tmpl/PrjProject_Ent_Customer_Info.html',
	
	'text!group/prj/project/tmpl/PrjProject_Ent_Workflow_View.html',
	'text!group/prj/workflow/tmpl/PrjWorkflow_Popup_Pick_User.html',

	'group/prj/project/ctrl/PrjProjectEntTab',
	'group/prj/treeview/ctrl/TreeViewEntMsgbox',
	],
	function(	
			PrjProject_Ent,
			PrjProject_Ent_New,
			
			PrjProject_Ent_Content,
			
			PrjProject_Ent_Tabs,
			
			PrjProject_Ent_Tab_History,
			
			PrjProject_Ent_Tab_Customer,
			PrjProject_Ent_Customer_Info,
			
			PrjProject_Ent_Workflow_View,
			PrjWorkflow_Popup_Pick_User,
			
			{
				PrjProjectEntTabEval, 
				PrjProjectEntTabEpic, 
				PrjProjectEntTabTask, 
				PrjProjectEntTabMember, 
				PrjProjectEntTabMemberGroup,
				PrjProjectEntTabReport,
				PrjProjectEntTabDoc, 
				PrjProjectEntTabComment, 
				PrjProjectEntTabCustomer, 
				PrjProjectEntTabStat,
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
		//------------------------------------------------------------------------------------
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
		
		const  pr_TYP00_SPRINT          = 100;
		
		var pr_DIV_CONTENT               = "#div_main_content";
		var pr_isViewAllTask             = false;
		var pr_isViewSprint              = false;
		var pr_isViewMain                = false;
		var pr_isViewPopup               = false;
		//------------------controllers-----------------------------------
		const pr_project				= App.controller.UI;
		var pr_ctr_Main 				= null;
		var pr_ctr_Sidebar 				= null;
		var pr_ctr_Fav 					= null;
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			if(!tmplName) {
				App.template.names[pr_grpName] = {}
				tmplName 	= App.template.names[pr_grpName]
			}
			
			pr_ctr_Main 	= App.controller.DBoard.DBoardMain;
			pr_ctr_Sidebar 	= pr_project.Sidebar;
			pr_ctr_Fav		= App.controller.UI.Fav;
			
			
			tmplName.PRJ_PROJECT_ENT							= pr_grpName + "PrjProject_Ent";
			tmplName.PRJ_PROJECT_ENT_NEW						= pr_grpName + "PrjProject_Ent_New";
			
			tmplName.PRJ_PROJECT_ENT_CONTENT					= pr_grpName + "PrjProject_Ent_Content";
			tmplName.PRJ_PROJECT_ENT_CONTENT_PATH				= pr_grpName + "PrjProject_Ent_Content_Path";
			tmplName.PRJ_PROJECT_ENT_CONTENT_CHECK_LIST			= pr_grpName + "PrjProject_Ent_Content_Check_List";
			
			
			tmplName.PRJ_PROJECT_ENT_TAB_STAT					= pr_grpName + "PrjProject_Ent_Tab_Stat";
			
			tmplName.PRJ_PROJECT_ENT_TAB_EPIC					= pr_grpName + "PrjProject_Ent_Tab_Epic";
			tmplName.PRJ_PROJECT_ENT_TAB_EPIC_LIST				= pr_grpName + "PrjProject_Ent_Tab_Epic_List";
			tmplName.PRJ_PROJECT_ENT_TAB_EPIC_LIST_SHOW_CHILD	= pr_grpName + "PrjProject_Ent_Tab_Epic_List_Show_Child";
			
			tmplName.PRJ_PROJECT_ENT_TAB_TASK					= pr_grpName + "PrjProject_Ent_Tab_Task";
			tmplName.PRJ_PROJECT_ENT_TAB_TASK_LIST				= pr_grpName + "PrjProject_Ent_Tab_Task_List";
			tmplName.PRJ_PROJECT_ENT_TAB_TASK_LIST_ELEMENT		= pr_grpName + "PrjProject_Ent_Tab_Task_List_Element";
			
			tmplName.PRJ_PROJECT_ENT_TAB_MEMBER					= pr_grpName + "PrjProject_Ent_Tab_Member";
			tmplName.PRJ_PROJECT_ENT_TAB_MEMBER_GROUP			= pr_grpName + "PrjProject_Ent_Tab_Member_Group";
			tmplName.PRJ_PROJECT_ENT_TAB_MEMBER_GROUP_POPUP		= pr_grpName + "PrjProject_Ent_Tab_Member_Group_Popup";
			
			tmplName.PRJ_PROJECT_ENT_TAB_COMMENT				= pr_grpName + "PrjProject_Ent_Tab_Comment";
			tmplName.PRJ_PROJECT_ENT_TAB_COMMENT_LIST			= pr_grpName + "PrjProject_Ent_Tab_Comment_List";
			tmplName.PRJ_PROJECT_ENT_TAB_DOCS					= pr_grpName + "PrjProject_Ent_Tab_Docs";
			tmplName.PRJ_PROJECT_ENT_TAB_CUSTOMER				= pr_grpName + "PrjProject_Ent_Tab_Customer";
			tmplName.PRJ_PROJECT_ENT_TAB_EVALUATION				= pr_grpName + "PrjProject_Ent_Tab_Evaluation";
			tmplName.PRJ_PROJECT_ENT_CUSTOMER_INFO				= pr_grpName + "PrjProject_Ent_Customer_Info";
			tmplName.PRJ_PROJECT_ENT_TAB_HISTORY				= pr_grpName + "PrjProject_Ent_Tab_History";
			
			tmplName.PRJ_PROJECT_ENT_TAB_REPORT					= pr_grpName + "PrjProject_Ent_Tab_Report"
			tmplName.PRJ_PROJECT_ENT_TAB_REPORT_LIST			= pr_grpName + "PrjProject_Ent_Tab_Report_List";
			tmplName.PRJ_PROJECT_ENT_TAB_REPORT_LIST_ELEMENT	= pr_grpName + "PrjProject_Ent_Tab_Report_List_Element";
			tmplName.PRJ_PROJECT_ENT_TAB_REPORT_CONT			= pr_grpName + "PrjProject_Ent_Tab_Report_Content";
			tmplName.PRJ_PROJECT_ENT_TAB_REPORT_NEW				= pr_grpName + "PrjProject_Ent_Tab_Report_New";
			
			tmplName.PRJ_PROJECT_ENT_WORKFLOW_VIEW				= pr_grpName + "PrjProject_Ent_Workflow_View";
			tmplName.PRJ_WORKFLOW_POPUP_PICK_USER				= pr_grpName + "PrjWorkflow_Popup_Pick_User";
			
			tmplCtrl	.do_lc_put_tmpl(tmplName.PRJ_PROJECT_ENT							, PrjProject_Ent);
			
			tmplCtrl	.do_lc_put_tmplRaw(PrjProject_Ent_New								, pr_grpName);
			
			tmplCtrl	.do_lc_put_tmplRaw(PrjProject_Ent_Content							, pr_grpName);
			tmplCtrl	.do_lc_put_tmplRaw(PrjProject_Ent_Tabs								, pr_grpName);
			
			tmplCtrl	.do_lc_put_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_HISTORY				, PrjProject_Ent_Tab_History);
			
			tmplCtrl	.do_lc_put_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_CUSTOMER				, PrjProject_Ent_Tab_Customer);
			tmplCtrl	.do_lc_put_tmpl(tmplName.PRJ_PROJECT_ENT_CUSTOMER_INFO				, PrjProject_Ent_Customer_Info);
			
			tmplCtrl	.do_lc_put_tmpl(tmplName.PRJ_PROJECT_ENT_WORKFLOW_VIEW				, PrjProject_Ent_Workflow_View);
			tmplCtrl	.do_lc_put_tmpl(tmplName.PRJ_WORKFLOW_POPUP_PICK_USER				, PrjWorkflow_Popup_Pick_User);
			
			do_lc_init_ctrl_module	();			
		}
		
		const do_lc_init_ctrl_module = () => {
			let pr_LST_MODULE = {
					EntEval 		: PrjProjectEntTabEval		, 
					EntEpic 		: PrjProjectEntTabEpic		, 
					EntTask 		: PrjProjectEntTabTask		, 
					EntMember 		: PrjProjectEntTabMember	,  
					EntMemberGroup 	: PrjProjectEntTabMemberGroup, 
					EntReport 		: PrjProjectEntTabReport	,
					EntComment 		: PrjProjectEntTabComment	, 
					EntCustomer 	: PrjProjectEntTabCustomer	, 
					EntStat 		: PrjProjectEntTabStat		,
					EntContent 		: PrjProjectEntTabContent	, 
					EntDoc 			: PrjProjectEntTabDoc
			}

			for(let ctrlName in pr_LST_MODULE){
				if(!pr_project[ctrlName])		pr_project[ctrlName] 		= new pr_LST_MODULE[ctrlName](pr_grpName, null, null, null );
			}
		}
		
		//---------show-----------------------------------------------------------------------------
		var pr_grpPath 		= 'group/prj/_transl';
		var pr_showed		= false;
		this.do_lc_show = function(id, code, divContent, typ00, isPopup = false){
			if (!pr_showed){
				do_gl_lang_append (pr_grpPath, self.do_lc_show_callback, [id, code, divContent, typ00, isPopup]);
				pr_showed = true;
			}else {
				self.do_lc_show_callback(id, code, divContent, typ00, isPopup);
			}
		};
		
		this.do_lc_show_callback = function(id, code, divContent, typ00, isPopup = false){               
			try{
				App.router.controller.do_lc_append_custom_tags()
				
				let 		params 	= req_gl_Url_Params();
				if(!id) 	id 		= params.id;
				if(!code) 	code 	= params.code;
				if (id && code){
					if(divContent){
						pr_DIV_CONTENT = divContent;
						pr_isViewMain  = false;
						if(!isPopup){
							pr_isViewAllTask = true;
							pr_isViewSprint	 = false;
							if(typ00 == pr_TYP00_SPRINT){
								pr_isViewSprint  = true;
								pr_isViewAllTask = false;
							}
						}else{
							pr_isViewAllTask    = true;
							pr_isViewSprint	 = false;
							pr_isViewPopup      = isPopup;
						}
					}else{
						pr_DIV_CONTENT 	 = "#div_main_content"
						pr_isViewMain    = true;
						pr_isViewAllTask = false;
						pr_isViewSprint	 = false;
						pr_isViewPopup	 = false;
					}
					do_lc_get_prj(id, code);
				}else{
					do_lc_back_to_list();
				}
			}catch(e) {				
				console.log(e); 
				//do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "prj.project", "PrjProjectEnt", "do_lc_show", e.toString()) ;
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
			if(pr_isViewPopup){
				$(".isManager").remove();
				$(".isWatcher").remove();
				$(".info-content").off("click").removeClass("info-content");
				$(".info-edit").off("click").removeClass("info-edit");
				if($('.td-task').hasClass("ui-draggable")) $('.td-task').draggable( "destroy" );
				
				$("#div_content-left").removeClass("col-xl-8").addClass("col-xl-12");
				$("#div_content-right").hide();
				$("#div_prj_content_header").hide();
			} else {
				$("#div_content-left").removeClass("col-xl-12").addClass("col-xl-8");
				$("#div_content-right").show();
				$("#div_prj_content_header").show();
			} 
			
			let uRole = self.pr_member_role;
			if(uRole === null || uRole === undefined){
				do_gl_init_msgbox_annonce($.i18n("prj_project_not_right_view"), do_lc_back_to_list);
			}
			if(uRole == pr_member_lev_manager){
				$(".noManager").remove(); 
				
				$(".isWorker").remove();
				$(".isWatcher").remove();
				$("#btn_add_chk_lst").removeClass("hide");
				$(".item-stat-show").removeAttr('disabled');
			}else if(uRole == pr_member_lev_reporter){
				$(".noReporter").remove(); 
				
				$(".isManager")			.not(".isReporter").remove();
				$(".info-content")		.not(".isReporter").off("click").removeClass("info-content");
				$("#btn_add_chk_lst")	.removeClass("hide");
				$('.td-task')			.draggable( "destroy" );
			}else if(uRole == pr_member_lev_worker){
				$(".noWorker").remove(); 
				
				$(".isManager").remove();
				$(".isWatcher").remove();
				$(".info-content").off("click").removeClass("info-content");
				if($('.td-task').hasClass("ui-draggable")) $('.td-task').draggable( "destroy" );
			}else if(uRole == pr_member_lev_watcher){
				$(".noWatcher").remove(); 
				
				$(".isManager").remove();
				$(".isWorker").remove();
				$(".info-content").off("click").removeClass("info-content");
				if($('.td-task').hasClass("ui-draggable")) $('.td-task').draggable( "destroy" );
			}else{
				do_gl_init_msgbox_annonce($.i18n("prj_project_not_right_view"), do_lc_back_to_list);
			}
		}
		//--------------------------------------------------------------------------------------------
		const do_lc_get_prj = (id, code) => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET, {id, code});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_get_prj_callback, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		const do_lc_get_prj_callback = sharedJson => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				let data 				= sharedJson[App['const'].RES_DATA];
				
				data.userRole			= data.userRole [App.data.user.id];
				self.pr_member_role 	= data.userRole;

				do_lc_show_prj			(data);
				do_lc_show_prj_others	(data);

			} else {
				do_gl_init_msgbox_annonce($.i18n("prj_project_not_right_view"), pr_isViewSprint ? null : do_lc_back_to_list);
			}
		}

		//--------------------------------------------------------------------------------------------
		const do_lc_show_prj = prj => {
			let isFavorite 	= null;
			if(App.data.user && App.data.user['lstFav'] && App.data.user['lstFav'][pr_ID_TABLE_PRJ]) {
				const lstFav 	=  App.data.user['lstFav']
	
				if(lstFav[pr_ID_TABLE_PRJ].ids.includes(prj.id)) isFavorite = true;
			}

			$(pr_DIV_CONTENT)	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT, {
				prj,
				isFavorite,
				isViewAllTask 	: pr_isViewAllTask, 
				isViewSprint 	: pr_isViewSprint, 
				isViewMain 		: pr_isViewMain
			}));

			pr_project.EntStat		.do_lc_show_prj_stats	(prj, false, true);
			pr_project.EntContent	.do_lc_show_prj_content	(prj, null, pr_isViewSprint);
			
			do_lc_bind_event(prj);
		}
		
		const do_lc_bind_event = prj => {
			$("#btn_treeview").off("click").on("click", function() {
				let {id, grp, code} = $(this).data();
				if(id){
					if(!App.controller.PrjTreeView)		App.controller.PrjTreeView 		= {};
					if(!App.controller.PrjTreeView.EntMsgbox){
						App.controller.PrjTreeView.EntMsgbox 	= new TreeViewEntMsgbox();
						App.controller.PrjTreeView.EntMsgbox	.do_lc_init();
					}
					App.controller.PrjTreeView.EntMsgbox		.do_lc_show_with_id(id, grp, code);
				}
			})
			
			$("#btn_all_task").off("click").on("click", function() {
				let {id, grp, code} = $(this).data();
				App.router.controller.do_lc_run("VI_MAIN/prj_project_task_list", `view_prj_project_task_list.html?id=${id}&code=${code}`)
			})
			
			$("#btn_kanban_board").off("click").on("click", function() {
				let {id, grp} = $(this).data();
				id && App.router.controller.do_lc_run("VI_MAIN/prj_task_list", `view_prj_task_list.html?groupId=${id}`)
			})
			
			$("#btn_sprint_board").off("click").on("click", function() {
				let {id, grp, code} = $(this).data();
				App.router.controller.do_lc_run("VI_MAIN/prj_sprint", `view_prj_sprint.html?groupId=${id}&code=${code}`)
			})

			$("#btn_mod_favorite").off("click").on("click", function() {
				const isFav = $(this).hasClass("isFavorite");

				do_lc_mod_favorite(isFav, prj);
			})
		}
		//---------------------------------------------------------------------------------------------------------
		
		const do_lc_show_prj_others = prj => {
			if(pr_isViewPopup) return;
			
			var scrollToTop = false;
			
			do_lc_show_epic_task_prj(prj, scrollToTop);
			
			pr_project.EntEval							.do_lc_get_prj_evaluation	(prj, pr_isViewSprint, scrollToTop);
			pr_project.EntDoc							.do_lc_show_prj_docs		(prj, scrollToTop);
			pr_project.EntMember						.do_lc_get_members			(prj, scrollToTop);
			pr_project.EntMemberGroup					.do_lc_get_grpMembers		(prj, scrollToTop);
			pr_project.EntComment						.do_lc_get_comments			(prj, false, scrollToTop);

			prj.typ02 == 0 && pr_project.EntReport		.do_lc_show_prj_report		(prj, false, scrollToTop);
		}

		//--------------------------------------------------------------------------------------------
		//start drag drop epic, task
		this.do_lc_show_epic_task = function (prj, scrollToTop) {
			do_lc_show_epic_task_prj(prj, scrollToTop);
		}
		const do_lc_show_epic_task_prj  = function (prj, scrollToTop) {
			if(!pr_isViewSprint)
				pr_project.EntEpic		.do_lc_show_prj_epic(prj, scrollToTop);
			pr_project.EntTask			.do_lc_show_prj_task(prj, pr_isViewSprint, scrollToTop);
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
			do_lc_show_prj_others(prj);

			pr_ctr_Sidebar.do_lc_show_favorite()
		}
		//end favorite
		
		//--------------------------------------------------------------------------------------------
		const do_lc_back_to_list = () => {
			App.router.controller.do_lc_run("VI_MAIN/prj_project_list", `view_prj_project_list.html`)
			return;
		}
	};

	return PrjProjectEnt;
});