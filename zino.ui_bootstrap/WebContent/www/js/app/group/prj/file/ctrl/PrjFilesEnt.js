define([
	'text!group/prj/file/tmpl/PrjFiles_Ent.html',
	
	'group/prj/file/ctrl/PrjFilesEntTab',
	'group/prj/treeview/ctrl/TreeViewEntMsgbox',
	],
	function(	
			PrjFiles_Ent,
			{
				PrjFilesEntTabEpic, 
				PrjFilesEntTabMember, 
				PrjFilesEntTabDoc, 
				PrjFilesEntTabComment, 
				PrjFilesEntTabContent
			},
			TreeViewEntMsgbox
	){

	var PrjFilesEnt 	= function (grpName, header, content, footer) {
		var pr_divHeader              = header;
		var pr_divContent             = content;
		var pr_divFooter              = footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName                = grpName?grpName:((new Date()).getTime()+"");
		var tmplName                  = App.template.names[pr_grpName];
		var tmplCtrl                  = App.template.controller;

		const pr_SERVICE_CLASS        = "ServicePrjProject"; //to change by your need
		const pr_SV_GET               = "SVGet";
		const pr_SV_NEW_FAVORITE      = "SVNewFavorite";
		const pr_SV_REMOVE_FAVORITE   = "SVRemoveFavorite";
		
		var self                      = this;
		this.pr_member_role           = null;
		
		const pr_member_lev_manager   = 0;
		const pr_member_lev_reporter  = 1;
		const pr_member_lev_worker    = 2;
		const pr_member_lev_watcher   = 3;
		
		const pr_member_lev_visitor   = 3;
		
		const pr_ID_TABLE_PRJ         = 250000;
		//------------------controllers------------------------------------------------------
		var pr_ctr_Main               = null;
		var pr_ctr_List               = null;
		var pr_ctr_Ent                = null;
		var pr_ctr_Sidebar            = null;
		var pr_ctr_Fav				  = null;
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			if(!tmplName) {
				App.template.names[pr_grpName] = {}
				tmplName 	= App.template.names[pr_grpName]
			}

			pr_ctr_Main 				= App.controller.UI.Main;
			pr_ctr_Sidebar 				= App.controller.UI.Sidebar;
			pr_ctr_Fav					= App.controller.UI.Fav;
			pr_ctr_Ent				    = App.controller.PrjFiles.Ent;

			tmplName.PRJ_FILES_ENT	= "PrjFiles_Ent";
		}

		//---------show-----------------------------------------------------------------------------
		var pr_grpPath 		= 'group/prj/_transl';
		var pr_showed		= false;
		this.do_lc_show 	= function(id, code){
			if (!pr_showed){
				do_gl_lang_append (pr_grpPath, self.do_lc_show_callback, [id, code]);
				pr_showed = true;
			}else {
				self.do_lc_show_callback(id, code);
			}
		};
		
		this.do_lc_show_callback = function(id, code){               
			try{
				App.router.controller.do_lc_append_custom_tags()
				
				let params = req_gl_Url_Params();
				if(!id) id = params.id;
				if(!code) code = params.code;
				if (id && code){
					do_lc_load_view();
					do_lc_get_prj(id, code);
				}else{
					pr_ctr_Main.do_lc_switch_mobile_or_pc(`view_prj_file_list.html`, "VI_MAIN/"+ App.router.part.PRJ_FILE_LIST);
				}
			}catch(e) {				
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "prj.project", "PrjFilesList", "do_lc_show", e.toString()) ;
			}
		};
		
		this.do_lc_reqRole_User = function(){
			let uRole = self.pr_member_role;
			if(uRole === null || uRole === undefined){
				do_gl_init_msgbox_annonce($.i18n("prj_file_not_right_view"), do_lc_back_to_list);
			}
			if(uRole == pr_member_lev_manager){
				$(".isWorker").remove();
			}else if(uRole == pr_member_lev_reporter || uRole == pr_member_lev_worker  ||  uRole == pr_member_lev_visitor){
				$(".isManager").remove();
				$(".info-content").off("click").removeClass("info-content");
			}else{
				do_gl_init_msgbox_annonce($.i18n("prj_file_not_right_view"), do_lc_back_to_list);
			}
		}

		const do_lc_load_view = () => {
			tmplCtrl.do_lc_put_tmpl(tmplName.PRJ_FILES_ENT, PrjFiles_Ent);
		}

		const do_lc_get_prj = (id, code) => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET, {id,code});	

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
				do_gl_init_msgbox_annonce($.i18n("prj_file_not_right_view"), do_lc_back_to_list);
			}
		}
		
		const do_lc_init_ctrl_module = () => {
			let pr_LST_MODULE = {
					 EntEpic 		: PrjFilesEntTabEpic, EntMember : PrjFilesEntTabMember, EntComment 	: PrjFilesEntTabComment, EntContent 	: PrjFilesEntTabContent	, EntDoc 	: PrjFilesEntTabDoc
			}
			
			for(let ctrlName in pr_LST_MODULE){
				if(!pr_ctr_Ent[ctrlName])		pr_ctr_Ent[ctrlName] 		= new pr_LST_MODULE[ctrlName](null, null, null);
			}
		}

		const do_lc_show_prj = (prj) => {
			let isFavorite 	= null;
			if(App.data.user && App.data.user['lstFav'] && App.data.user['lstFav'][pr_ID_TABLE_PRJ]) {
				const lstFav 	=  App.data.user['lstFav']
	
				if(lstFav[pr_ID_TABLE_PRJ].ids.includes(prj.id)) isFavorite = true;
			}

			prj.isFavorite = isFavorite
			$("#div_main_content")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_FILES_ENT, prj));
			
			pr_ctr_Ent.EntContent	.do_lc_show_prj_content(prj);
			pr_ctr_Ent.EntEpic		.do_lc_show_prj_epic(prj);
			pr_ctr_Ent.EntDoc		.do_lc_show_prj_docs(prj);
			
			do_lc_bind_event(prj);
		}
		
		const do_lc_get_element_ofPrj = prj => {
			pr_ctr_Ent.EntMember	.do_lc_get_list_member(prj);
			pr_ctr_Ent.EntComment	.do_lc_get_list_comments(prj);
		}
		
		const do_lc_bind_event = (prj) => {
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
			
			
			$("#btn_mod_favorite").off("click").on("click", function() {
				const isFav = $(this).hasClass("isFavorite");

				do_lc_mod_favorite(isFav, prj);
			})
			
			$("#inp_search_name_file").off("keyup").on("keyup", function(e){
				let src 	= $(this).val();
				let files 	= prj.files;
				
				for(let i=0; i < files.length; i++){
					if(files[i].typ01 == 1){
						if(files[i].name.includes(src)){
							$("#div_document").find("tr[data-id='"+ files[i].id +"']").show();
						}else{
							$("#div_document").find("tr[data-id='"+ files[i].id +"']").hide();
						}
					}
				}
			})
		}
		
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
		
		const do_lc_back_to_list = () => {
			 setTimeout(function(){ 
				 window.history.back()
			}, 2000);
		}
	};

	return PrjFilesEnt;
});