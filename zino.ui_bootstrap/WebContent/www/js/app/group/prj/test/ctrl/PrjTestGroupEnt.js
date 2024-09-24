define([
	'text!group/prj/test/tmpl/PrjTestUnit_Ent.html',

	'group/prj/test/ctrl/PrjTestGroupEntTab'
	],
	function(	
			PrjTestUnit_Ent,
			{				
				PrjTestUnitEntTabTask, 
				PrjTestUnitEntTabMember, 				
				PrjTestUnitEntTabDoc, 
				PrjTestUnitEntTabComment, 			
				PrjTestUnitEntTabContent,
				PrjTestGroupEntTabMemberGroup
			}
			
	){

	var PrjTestGroupEnt 	= function (grpName, header, content, footer) {
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

		const pr_SERVICE_CLASS_FAV		= "ServiceTpyFavorite";
		const pr_SV_NEW_FAVORITE		= "SVNsoNew";
		const pr_SV_MOD_FAVORITE		= "SVNsoMod";

		var self 						= this;
		this.pr_member_role				= null;

		const pr_member_lev_manager 	= 0;
		const pr_member_lev_reporter 	= 1;
		const pr_member_lev_worker 		= 2;
		const pr_member_lev_watcher 	= 3;

		const pr_ID_TABLE_PRJ			= 250000;
		
		const  pr_TYP00_SPRINT          = 10;
		const  pr_TYP00_TEST          	 = 30;
		const  pr_TYP01_TEST_GROUP       = 2;

		const pr_TYP_FAV_UNLIKE         = 0;
		const pr_TYP_FAV_LIKE          	= 1;
		
		var pr_DIV_CONTENT               = "#div_main_content";
		var pr_isViewAllTask             = false;
		var pr_isViewSprint              = false;
		var pr_isViewMain                = false;
		var pr_isViewPopup               = false;
		//------------------controllers---------------
		//------------------controllers------------------------------------------------------
		const pr_project				= App.controller.PrjTestGroup;
		var pr_ctr_Main 				= null;
		var pr_ctr_Sidebar 				= null;
		var pr_ctr_Fav 					= null;
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			pr_ctr_Main 				= pr_project.Main;
			pr_ctr_Sidebar 				= App.controller.UI.Sidebar;
			pr_ctr_Fav					= App.controller.UI.Fav;

			if(!tmplName) {
				App.template.names[pr_grpName] = {}
				tmplName = App.template.names[pr_grpName]
			}
			
			tmplName.PRJ_TESTGROUP_ENT	= "PrjTestGroup_Ent";
			tmplName.PRJ_TESTUNIT_ENT	= "PrjTestUnit_Ent";
			tmplCtrl.do_lc_put_tmpl(tmplName.PRJ_TESTUNIT_ENT, PrjTestUnit_Ent);
		}

		//---------show-----------------------------------------------------------------------------
		this.do_lc_show = function(id, code, divContent, typ00, typ02, isPopup = false){               
			try{
				let params = req_gl_Url_Params(App.data.url?App.data.url:decodeURIComponent(window.location.search.substring(1)));
				if(!id) id = params.id;
				if (id){
					if(divContent){
						pr_DIV_CONTENT = divContent;
						if(!isPopup){
							pr_isViewAllTask = true;
							if(typ00 == pr_TYP00_TEST && typ02 == pr_TYP01_TEST_GROUP){
								pr_isViewSprint  = true;
								pr_isViewAllTask = false;;
							}
						}else{
							pr_isViewAllTask    = true;
							pr_isViewPopup      = isPopup;
						}
					}else{
						pr_isViewMain    = true;
					}
					do_lc_get_prj(id, code);
				}else{
					pr_ctr_Main.do_lc_switch_mobile_or_pc(`view_prj_project_list.html`, "VI_MAIN/"+ App.router.part.PRJ_TESTUNIT_LIST);
				}
			}catch(e) {				
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "prj.project", "PrjTestUnitEnt", "do_lc_show", e.toString()) ;
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
			}
			
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

		const do_lc_get_prj = (id, code) => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET, {id, code});	

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
					EntTask 	: PrjTestUnitEntTabTask		, 
					EntMember 	: PrjTestUnitEntTabMember,  
					EntComment 	: PrjTestUnitEntTabComment	, 
					EntContent 	: PrjTestUnitEntTabContent	, 
					EntDoc 		: PrjTestUnitEntTabDoc		,
					EntMemberGroup 		: PrjTestGroupEntTabMemberGroup
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
			
			$(pr_DIV_CONTENT)	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_TESTUNIT_ENT, {
				prj, 
				isFavorite,
				isViewAllTask 	: pr_isViewAllTask, 
				isViewSprint 	: pr_isViewSprint, 
				isViewMain 		: pr_isViewMain
			}));

			pr_project.EntContent	.do_lc_show_prj_content(prj, null, pr_isViewSprint);
			do_lc_bind_event(prj);
			if(pr_isViewPopup) return;
			
			do_lc_show_epic_task_prj(prj);

			
			// pr_project.EntDoc		.do_lc_show_prj_docs(prj);
			// pr_project.EntEval		.do_lc_get_prj_evaluation(prj, pr_isViewSprint);
		}
		
		const do_lc_get_element_ofPrj = prj => {
			if(pr_isViewPopup) return;
			pr_project.EntMember		.do_lc_get_list_member(prj);
			pr_project.EntMemberGroup	.do_lc_get_list_member(prj);
			pr_project.EntComment		.do_lc_get_list_comments(prj.id, prj.code01);
		}

		const do_lc_bind_event = prj => {			
			
			$("#btn_all_task").off("click").on("click", function() {
				let {id, grp} = $(this).data();
				id && window.open(`view_prj_project_task_list.html?groupId=${id}`, "_self");
			})
			
			$("#btn_kanban_board").off("click").on("click", function() {
				let {id, grp} = $(this).data();
				id && window.open(`view_prj_task_list.html?groupId=${id}`, "_self");
			})
			
			$("#btn_sprint_board").off("click").on("click", function() {
				let {id, grp} = $(this).data();
				id && window.open(`view_prj_sprint.html?groupId=${id}`, "_self");
			})

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
			// pr_project.EntEpic		.do_lc_show_prj_epic(prj);
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
				pr_ctr_Main.do_lc_switch_mobile_or_pc(`view_prj_sprint.html`, "VI_MAIN/"+ App.router.part.PRJ_SPRINT);
				return;
			}
			pr_ctr_Main.do_lc_switch_mobile_or_pc(`view_prj_testUnit_list.html`, "VI_MAIN/"+ App.router.part.PRJ_TESTUNIT_LIST);
		}
	};

	return PrjTestGroupEnt;
});