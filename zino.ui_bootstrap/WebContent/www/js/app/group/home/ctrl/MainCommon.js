define([
	'group/nso_chatmini/ctrl/MiniChatMain',
],
function(	
			MiniChatMain
	){
	const MainSidebar     	= function (grpName, header,content,footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:"Home";
		var pr_grpPath				= 'group/home';
		const tmplName				= App.template.names[pr_grpName];
		const tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		var pr_ctr_Main 			= null;
		var pr_ctr_Fav 				= null;
		const self					= this;
		const pr_ID_TABLE_PRJ		= 250000;

		const initialValues = {
			lstFav : {}
		}
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			pr_ctr_Main 							= App.controller.UI.Main;
			pr_ctr_Fav 								= App.controller.UI.Fav;
		}      

		this.do_lc_show		= function(){
			try{
				do_lc_build_page();
				// do_lc_get_list_favorites();

				if (VIEW_PART != "prj_chatroom")
					do_lc_get_minichat();
			}catch(e) {				
				console.log(e);
			}
		};

		this.do_lc_show_favorite = () => {
			do_lc_build_page();
		}


		const do_lc_build_page = function(){
			$("#div-menu-sidebar").html(tmplCtrl.req_lc_compile_tmpl(tmplName.VI_SIDEBAR			, initialValues));

			const lstFav = {}
			if(App.data.user.lstFav) {
				Object.keys(App.data.user.lstFav).map(key => {
					if(!App.data.user.lstFav[key].lst || App.data.user.lstFav[key].lst.length <= 0 || +key === 2000) return

					lstFav[key] = App.data.user.lstFav[key]
				})
			}

			$("#li_lst_favorite").html(tmplCtrl.req_lc_compile_tmpl(tmplName.VI_SIDEBAR_FAVORITE	, 
				{lstFav: lstFav}
			));
			
			do_lc_bindEvent_sidebar();
			
			do_gl_apply_right($("#div-menu-sidebar"));
			// do_gl_handle_member_external($("#page-topbar"));
		}

		const do_lc_get_minichat = function() {
			MiniChat 	= new MiniChatMain();
			MiniChat.do_lc_init();
			MiniChat.do_lc_show_old();
		}

		const do_lc_bindEvent_sidebar = () => {
			App.router.controller.do_lc_binding_route();
			
			$("#prj_minichat").off("click").on("click", () => {
				const MiniChat 	= new MiniChatMain();
				MiniChat.do_lc_init();
				MiniChat.do_lc_show("#div_mini_chat");
			});

			$(".prj-trash-menu").off("click").on("click", function() {
				let id		= $(this).attr("data-id");
				let type	= $(this).attr("data-type"); //ID_TABLE - 250000: Prj
				id 			= parseInt(id, 10);

				if(!pr_ctr_Fav) pr_ctr_Fav = App.controller.UI.Fav
				if(!pr_ctr_Fav) return;

				pr_ctr_Fav.do_lc_remove_myFavorites({id}, type)
			});

			$("#side-menu").metisMenu();

			$("#sidebar-menu a").each(function() {
				const setActiveMenu = function(){
					$(this).addClass("active");
					$(this).parent().addClass("mm-active");
					$(this).parent().parent().addClass("mm-show");
					$(this).parent().parent().prev().addClass("mm-active");
					$(this).parent().parent().parent().addClass("mm-active");
					$(this).parent().parent().parent().parent().addClass("mm-show");
					$(this).parent().parent().parent().parent().parent().addClass("mm-active");
				}.bind(this);

				let view = window.location.href.split(/[?#]/)[0];

				if(this.href == view){
					setActiveMenu();
					return;
				}

				view = window.location.href.split(/[#]/)[0];
				if(this.href == view){
					setActiveMenu();
					return;
				}
			});

			do_lc_get_open_menu();

			$(".li-has-menu").off("click").on("click", function() {
				const $this		= $(this);
				const {open} 	= $this.data();
				const isOpen 	= $this.hasClass("mm-active");
				let lstOpen 	= JSON.parse(localStorage.getItem(App.keys.KEY_MENU_SIDEBAR_OPEN)) || [];

				if(isOpen){
					!lstOpen.includes(open) && lstOpen.push(open);
				}else{
					lstOpen = lstOpen.filter(o => (o !== open));
				}
				localStorage.setItem(App.keys.KEY_MENU_SIDEBAR_OPEN, JSON.stringify(lstOpen));

				do_lc_get_open_menu();
			})

			$(".menu-title").off("click").on("click", function() {
				const 	$this		= $(this);
				const 	data 		= $this.data();
				var 	grp			= data.grp;
				var		open		= data.open;
				
				if (open=="1"){
					data.open = "0";
					$("."+grp).hide();
					$this.addClass('menu-closed');
				}else{
					data.open = "1";
					$("."+grp).show();
					$this.removeClass('menu-closed');
				}
			})
			
			// do_gl_req_right_for_user();
		}

		const do_lc_get_open_menu = () => {
			const menuOpens 	= JSON.parse(localStorage.getItem(App.keys.KEY_MENU_SIDEBAR_OPEN)) || [];
			if(menuOpens.length){
				for(let open of menuOpens){
					const $this		= $(`.li-has-menu[data-open='${open}']`)
					if($this.length){
						const $ul 	= $this.find("ul");
						$this		.addClass("mm-active");
						$ul			.addClass("mm-show");
						$ul			.css("height", "");
						$this		.children("a").attr("aria-expanded", true);
					}
				}
			}
		}

		

		const do_lc_after_remove_favorite_response = (sharedJson, parTyp, parId) => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				if(App.data["lstFavorites"])	delete App.data["lstFavorites"][parTyp + "_" + parId];
				do_lc_get_list_favorites();
			}
			App.MsgboxController.do_lc_close();
		}

		const do_lc_get_list_favorites = () => {
			let ref 		= req_gl_Request_Content_Send_With_Params("ServiceTpyFavorite", "SVLstByUser");	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_after_list_favorites, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		const do_lc_after_list_favorites = sharedJson => {
			App.data["lstFavorites"] = {};
			if(can_gl_AjaxSuccess(sharedJson)) {
				let data 		= sharedJson[App['const'].RES_DATA];
				if(data && data.length){
					App.data["lstFavorites"] = data
						.filter(({entTyp}) => entTyp == pr_ID_TABLE_PRJ)
						.reduce((curr, val) => {
							curr[val.entTyp + "_" + val.entId] 	= val; 
							return curr;
					}, {});
				}
			}

			initialValues.lstFavorite = App.data["lstFavorites"];
			// do_lc_build_page();
		}
	};


	const MainHeader    	= function (grpName, header, content, footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:"Home";
		var pr_grpPath				= 'group/home';
		const tmplName				= App.template.names[pr_grpName];
		const tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		var self 					= this;
		//------------------controllers------------------------------------------------------
		var pr_ctr_Main 			= null;
		var pr_ctr_Notify			= null;

		
		const pr_TYPE_ADMIN_ALL		= 1;
		const pr_TYPE_ADMIN		    = 2;

		const pr_RIGHT_REPORT_SEE	    = 2002001;
		const pr_RIGHT_REPORT_MAN_SEE	= 2002011;
		const pr_RIGHT_HOLIDAY_SEE	    = 2001001;
		const pr_RIGHT_TPY_CAT_SEE	    = 7000001;

		const initialValues 		= {
				isShowSearch : [App.router.part.PRJ_PROJECT_LIST, App.router.part.PRJ_EPIC_LIST, App.router.part.PRJ_PARTNER_LIST
					, App.router.part.PRJ_USER_LIST, App.router.part.PRJ_JOB_HOLIDAY_LIST, App.router.part.PRJ_FILE_LIST].includes(VIEW_PART),
		};
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			pr_ctr_Main 					= App.controller.UI.Main;
			pr_ctr_Notify					= App.controller.UI.Notify;
		}      

		this.do_lc_show		= function(){
			try{
				do_lc_build_page();
				do_bind_event();
			}catch(e) {				
				console.log(e);
			}
		};

		const do_lc_build_page = function(){
			let user 	= App.data.user;
			if(!user.avatar && user.per.files) user.avatar = user.per.files[0];
			let isAdmin = false;

			if (user.typ01== pr_TYPE_ADMIN_ALL || user.typ01==pr_TYPE_ADMIN){
				isAdmin = true ;
			}

			$("#page-topbar").html(tmplCtrl.req_lc_compile_tmpl(tmplName.VI_HEADER, {user, url: UI_URL_ROOT, lan: localStorage.language, isAdmin, isShowSearch : initialValues.isShowSearch}));

			pr_ctr_Notify.do_lc_get_CountNew();
			pr_ctr_Notify.do_lc_refresh ()
			
			do_lc_get_doc_company_user();

			
			do_gl_apply_right($("#div-menu-functions"));
			do_gl_apply_right($("#div-menu-user-dropdown"));
			
		} 

		//---------private-----------------------------------------------------------------------------
		const do_bind_event = function (){
			App.router.controller.do_lc_binding_route();
			
			$(".a-languge").off("click").on("click", function(){
				let {lan: language, lanid: languageId, loc: locale} 	= $(this).data();

				if (!language || !languageId || !locale) {
					 do_gl_Set_Lang_Build_UI_URL_PATH();
					 locale   	= localStorage.getItem("locale");
					 language 	= localStorage.getItem("language");
					 languageId = localStorage.getItem("languageId");
				}
			        
				$.i18n({locale: language});
				localStorage.locale 	= locale;
				localStorage.language 	= language;
				localStorage.languageId = languageId;
				window.location.reload();
			})

			$("#a_disconnect").off("click").on("click", function(){
				App.router.controller.do_lc_run(App.router.routes.LOGOUT);
				App.controller.ChatRoom && App.controller.ChatRoom.Socket && App.controller.ChatRoom.Socket.do_gl_closeSocket();
			})

			$("#div_notify_content").off("click").on("click", function(e) {
				e.stopPropagation();
			})
			
			$("#btn_notify_dropdown").off("click").on("click", function(){
				pr_ctr_Notify.do_lc_show();
			})

			$("#btn_page_reload").off("click").on("click", function(){
//				window.open(window.location.href, "_self");
				window.location.reload();
			})

			if(App.data.user.rights){
				if(App.data.user.rights.includes(pr_RIGHT_REPORT_SEE))     $("#div_menu_report").removeClass("hide");
				if(App.data.user.rights.includes(pr_RIGHT_REPORT_SEE))     $("#div_menu_dayoff").removeClass("hide");
				if(App.data.user.rights.includes(pr_RIGHT_HOLIDAY_SEE))    $("#div_menu_holiday").removeClass("hide");
				if(App.data.user.rights.includes(pr_RIGHT_TPY_CAT_SEE))    $("#div_menu_tpy_cat").removeClass("hide");
				if(App.data.user.rights.includes(pr_RIGHT_REPORT_MAN_SEE)) {
					$("#div_menu_report_man").removeClass("hide");
					$("#div_menu_dayoff_man").removeClass("hide");
				}
			}

//			if([pr_TYPE_ADMIN_ALL, pr_TYPE_ADMIN].includes(App.data.user.typ)){
//			if(App.data.user.rights.includes(2001001)) $("#div_menu_holiday").show();
//			if(App.data.user.rights.includes(2002011)) $("#div_menu_report_man").show();
//			if(App.data.user.rights.includes(2002011)) $("#div_menu_dayoff_man").show();
//			if(App.data.user.rights.includes(7000001)) $("#div_menu_tpy_cat").show();
//			if(App.data.user.rights.includes(2002001)) $("#div_menu_report").show();
//			}
//			else{
//			$("#div_menu_holiday").hide();
//			$("#div_menu_report_man").hide();
//			$("#div_menu_dayoff_man").hide();
//			$("#div_menu_tpy_cat").hide();
//			if(App.data.user.rights.includes(2002001)) $("#div_menu_report").show();
//			}

//			$("#a_report_man").off("click").on("click", function(){
//			if([pr_TYPE_ADMIN_ALL, pr_TYPE_ADMIN].includes(App.data.user.typ)){
//			window.open("view_prj_job_report_man.html", "_self");
//			}
//			else{
//			window.open("view_prj_job_report.html", "_self");
//			}
//			})
		};

		const do_lc_get_doc_company_user = () => {
			const dataAvatar  	= localStorage.getItem(App.keys.KEY_COMPANY_DATA_AVATAR);
			const lstDocs 		= dataAvatar ? JSON.parse(dataAvatar) : {};
			if(lstDocs[App.data.user.manId]){
				$(".img-company").attr("src", lstDocs[App.data.user.manId].path01);
			} else {
				do_lc_get_docs_company_user();
			}
		}

		const do_lc_get_docs_company_user = () => {
			const ref 		= req_gl_Request_Content_Send_With_Params("ServicePrjProject", "SVGetDocCompanyUser");	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_after_get_docs_company, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		const do_lc_after_get_docs_company = sharedJson => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				const data 			= sharedJson[App['const'].RES_DATA];
				$(".img-company")	.attr("src", data.path01);

				const dataAvatar  	= localStorage.getItem(App.keys.KEY_COMPANY_DATA_AVATAR);
				const lstDocs 		= dataAvatar ? JSON.parse(dataAvatar) : {};

				lstDocs[App.data.user.manId] = data;
				localStorage.setItem(App.keys.KEY_COMPANY_DATA_AVATAR, JSON.stringify(lstDocs));
			} else {

			}
		}
	};


	const MainNotification  = function (grpName, header, content, footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:"Home";
		var pr_grpPath				= 'group/home';
		const tmplName				= App.template.names[pr_grpName];
		const tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		var pr_ctr_Main 			= null;
		var pr_ctr_List 			= null;
		var pr_ctr_Ent				= null;

		const pr_SERVICE_CLASS		= "ServiceMsgMessage"; //to change by your need
		const pr_SV_NOTI_LST		= "SVNotiLst"; 
		const pr_SV_NOTI_DEL		= "SVNotiDel"; 
		const pr_SV_NOTI_DEL_ALL	= "SVNotiDelAll"; 
		const pr_SV_NOTI_READ		= "SVNotiRead"; 
		const pr_SV_NOTI_COUNT		= "SVNotiCount";

		
		const pr_NUMBER_NOTIFY		= 40;
		const pr_TIME_REFRESH_NOTIFY= 12 * 60 * 1000;
		
		var pr_BEGIN_NOTIFY			= 0;
		var self					= this;
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			pr_ctr_Main 					= App.controller.UI.Main;		
		}      

		//---------------------------------------------------------------
		this.do_lc_get_CountNew = function(){
			const ref 		= req_gl_Request_Content_Send(pr_SERVICE_CLASS, pr_SV_NOTI_COUNT);	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_get_CountNew_callback, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_get_CountNew_callback = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {
				const data 	= sharedJson[App['const'].RES_DATA];
				data && $("#sp_nbNew_notify").html(data);
			}
			do_lc_bindEvent_notify();
		}


		this.do_lc_refresh = function(){
			setInterval(() => {
				self.do_lc_get_CountNew();
			}, pr_TIME_REFRESH_NOTIFY);
		}
		
		//---------------------------------------------------------------
		this.do_lc_show		= function(){
			try{
				do_lc_list();
			}catch(e) {				
				console.log(e);
			}
		};


		const do_lc_list = function(){
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_NOTI_LST, {number: pr_NUMBER_NOTIFY, begin: pr_BEGIN_NOTIFY});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_list_callback, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_list_callback = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {
				const data 			= sharedJson[App['const'].RES_DATA];
				
				//---remove notif same prj, same action typ
				const dataFilter 	= data.reduce((curr, item) => {
					const content 	= JSON.parse(item.inf01) || {};
					item.inf01		= content;
					
					//---check and remove
					const lastItem 	= curr.length ? curr[curr.length - 1].inf01 : {};
					if(content.parID !== lastItem.parID || content.typ !== lastItem.typ){
						curr.push(item);
					} else {
						curr[curr.length - 1].idSub ? curr[curr.length - 1].idSub.push(item.id) : curr[curr.length - 1].idSub = [item.id];
					}

					return curr;
				}, []);
				dataFilter &&	$("#div_notify_content")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.VI_NOTIFICATION, dataFilter));

				//set toggle btn new, prev dynamique
				do_lc_toggle_btn_view(data);
			}
			do_lc_bindEvent_notify();
		}
		//---------private-----------------------------------------------------------------------------
		const do_lc_bindEvent_notify = function (){
			$(".notification-item").off("click").on("click", function(){
				let {prj: idPrj, id, tab} = $(this).data();
				
				if (tab == "off") window.open(`view_prj_job_off_man.html`, "_blank");
				else if (tab == "report") window.open(`view_prj_job_report_man.html`, "_blank");
				else if (tab == "meeting") window.open(`view_prj_appointment_list.html`, "_blank");
				else idPrj && window.open(`view_prj_project_content.html?id=${idPrj}`, "_blank");
				do_lc_read(id);
			})

			$("#btn_view_next").off("click").on("click", function(){
				pr_BEGIN_NOTIFY += pr_NUMBER_NOTIFY;
				do_lc_list();
				return false;//stop toggle dropdown
			})

			$("#btn_view_prev").off("click").on("click", function(){
				pr_BEGIN_NOTIFY -= pr_NUMBER_NOTIFY;
				do_lc_list();
				return false;//stop toggle dropdown
			})

			$("#btn_view_all").off("click").on("click", function(){
				do_lc_read(null, true);
				return false;//stop toggle dropdown
			})

			$("#btn_delete_all").off("click").on("click", function(){
				App.MsgboxController.do_lc_show({
					title 		: $.i18n("prj_project_noti_title_delete"),
					content 	: $.i18n("prj_project_noti_title_delete_content"),
					autoclose	: false,
					buttons 	: {
						UPDATE : {
							lab 		: $.i18n("common_btn_send"),
							funct 		: do_lc_del_all,
							classBtn	: "btn-primary",
							autoclose	: true
						},
						CALCEL : {
							lab 		: $.i18n("common_btn_cancel"),
						}
					}
				});

				return false;//stop toggle dropdown
			})

			$(".span-delete-notif").off("click").on("click", function() {
				const $this 		= $(this);
				const {id, idsub} 	= $this.data();
				const $itemNotif 	= $this.closest(".div-notif-item");
				const ids 			= idsub && id ? `[${id}, ${idsub}]` : id ? `[${id}]` : "";
				ids && do_lc_del(ids, $itemNotif);
			})

			$(".span-read-notif", ".a_view_prj" ).off("click").on("click", function() {
				const $this 		= $(this);
				const {id, idsub} 	= $this.data();

				do_lc_read(id);
			})
		};

		const do_lc_read = function(idNotify, isReadAll){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_NOTI_READ, {id: idNotify, isReadAll});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_read_callback, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_read_callback = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {
				let data 	= sharedJson[App['const'].RES_DATA];
				$("#sp_nbNew_notify").html(data ? data : "");
				do_lc_list();
			}
		}

		const do_lc_del = (ids, $itemNotif) => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_NOTI_DEL, {ids});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_del_callback, [$itemNotif]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_del_callback = function(sharedJson, $itemNotif){
			if(can_gl_AjaxSuccess(sharedJson)) {
				$itemNotif.remove();
			}
		}

		const do_lc_del_all = (ids, isDelAll) => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_NOTI_DEL_ALL, {});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_del_all_callback, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_del_all_callback = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_gl_show_Notify_Msg_Success($.i18n('common_ok_msg_del'));
				$("#sp_nbNew_notify").html("");
				do_lc_list();
			}
		}

		const do_lc_toggle_btn_view = data => {
			pr_BEGIN_NOTIFY === 0 ?	$("#btn_view_prev").hide() : $("#btn_view_prev").show();

			if(data.length < pr_NUMBER_NOTIFY)	$("#btn_view_next").hide();
		}
	};

	
	const MainFavorite 		= function (grpName, header, content, footer) {
		const pr_divHeader 			= header;
		const pr_divContent 		= content?content:".div-favorite";
		const pr_divFooter 			= footer;
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:"Home";
		var pr_grpPath				= 'group/home';
		const tmplName				= App.template.names[pr_grpName];
		const tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------

		const svClass 				= App['const'].SV_CLASS;
		const svName				= App['const'].SV_NAME;
		const sessId				= App['const'].SESS_ID;
		const userId          		= App['const'].USER_ID;

		const fVar					= App['const'].FUNCT_SCOPE;
		const fName					= App['const'].FUNCT_NAME;
		const fParam				= App['const'].FUNCT_PARAM;

		const self 					= this;

		//------------------------------------------------------------------------------------
		const pr_varname			= "lstFav";
		let   pr_thread_sync 		= null;

		const pr_SYNC_INTERVAL 		= 1000 * 59 * 60;// ~1 minute

		var   pr_ctr_Sidebar		= App.controller.UI.Sidebar
		//--------------------APIs--------------------------------------//
		this.do_lc_init	= () => {
			if(App.data.user) {
				App.data.user[pr_varname] =  sessionStorage[pr_varname]?JSON.parse(sessionStorage[pr_varname]):{};
			}
		}

		//--------------------------------------------------------------------------------------------
		this.do_lc_build_myFavorites = (entTyps) => {
			if (!App.data.user) return [];
			
			const ref       = req_gl_Request_Content_Send("ServiceTpyFavorite", "SVLstByUser");

			if(entTyps) {
				ref['entTyps']  = entTyps.join(',');
			}

			const fSucces	= [];
			fSucces.push(req_gl_funct(null, do_lc_build_myFavorites_callback, [entTyps]));

			const fError    = req_gl_funct(null, do_gl_show_Notify_Msg_Error, [ $.i18n("common_err_ajax") ]);

			App.network.do_lc_ajax(
					App.path.BASE_URL_API_PRIV,
					req_gl_LS_SecurityHeaderBearer(App.keys.KEY_STORAGE_CREDENTIAL),
					ref, 100000, fSucces, fError);
		}

		//--------------------------------------------------------------------------------------------
		const do_lc_build_myFavorites_callback = (sharedJson, entTyps) => {
			if (sharedJson[App['const'].SV_CODE] === App['const'].SV_CODE_API_YES) {
				var data 					=  sharedJson[App['const'].RES_DATA];
				App.data.user[pr_varname] 	= {};

				$.each(data, function(i, e){
					App.data.user[pr_varname][e.entTyp] = JSON.parse(!e.descr?"{}":e.descr);
					//e.descr = {ids:[1,2,3], lst:[{id:1, name:'abc'}, {}]}
					
					var fav = App.data.user[pr_varname][e.entTyp];
					if (!fav.ids) fav.ids = [];
					if (!fav.lst) fav.lst = [];
					
					for (var ent in fav.lst){
						fav.lst[ent].fav = 1; //add fav
					}
				});

				//save to sessionStorage
				sessionStorage[pr_varname] 		= JSON.stringify(App.data.user[pr_varname]);

				if(App.controller.UI.Sidebar) {
					App.controller.UI.Sidebar.do_lc_show_favorite()
				}

				return;
			}

			if (sharedJson[App['const'].SV_CODE] == App['const'].SV_CODE_API_NO) {
				// do something else
				for (var i in entTyps) 
					App.data.user[pr_varname][entTyps[i]] = {ids:[], lst:[]}
			}

			//doShowBindingUpdateFavorite(obj);
		}
		//--------------------------------------------------------------------------------------------
		this.do_lc_check_myFavorites = (data, entTyp) => {
			if (!App.data.user) return;
			if (!App.data.user[pr_varname]) return;
			if (!App.data.user[pr_varname][entTyp]) return;
			if (!data) return;
			
			const setFav = new Set(App.data.user[pr_varname][entTyp].ids);
			
			for (var i in data){
				var d = data[i];
				if (setFav.has(d.id))
					d.fav = 1;
				else
					d.fav = 0;
			}
		}
		//--------------------------------------------------------------------------------------------
		this.req_lc_myFavorites = (entTyp) => {
			if (!App.data.user) return [];
			if (!App.data.user[pr_varname]) return [];
			if (!App.data.user[pr_varname][entTyp]) return[];
			
			var lst = $.extend(true, [], App.data.user[pr_varname][entTyp].lst); 
			return lst;
		}
		//em khong khai báo this thi làm sao goi từ ben ngoai vào? e chua khai báo pr_Type
		this.req_lc_myFavIds = (entTyp) => {
			if (!App.data.user) return [];
			if (!App.data.user[pr_varname]) return [];
			if (!App.data.user[pr_varname][entTyp]) return[];
			//copy from
			var ids = $.extend(true, [], App.data.user[pr_varname][entTyp].ids); 
			return ids;
		}
		
		this.req_lc_merge_myFavorites = (data, entTyp, sizeMax) => {
			if (!sizeMax) sizeMax = 20;
			
			if (!App.data.user) 					return data;
			if (!App.data.user[pr_varname]) 		return data;
			if (!App.data.user[pr_varname][entTyp]) return data;
			
			var fav = App.data.user[pr_varname][entTyp];
			if (!fav.ids) fav.ids = [];
			const setFav = new Set(fav.ids);
			
			var lst = $.extend(true, [], App.data.user[pr_varname][entTyp].lst); 
			
			for (var i in data){
				var ent = data[i];
				if (setFav.has(ent.id)) continue;
				lst.push (ent);
				if (lst.length>=sizeMax) break;
			}
			
			return lst;
		}
		//--------------------------------------------------------------------------------------------
		this.do_lc_push_myFavorites = (ent, entTyp, savedAttrs = ["id", "name", "code01", "typ00", "typ01", "typ02"], isUpdate = false) => {
			if (!ent || !ent.id) 	return;
			if (!App.data.user) 	return;
			
			if (!App.data.user[pr_varname])				App.data.user[pr_varname] 			= {};
			if (!App.data.user[pr_varname][entTyp]) 	App.data.user[pr_varname][entTyp] 	= {};
			
			
			var fav = App.data.user[pr_varname][entTyp];
			if (!fav.ids) fav.ids = [];
			if (!fav.lst) fav.lst = [];
			
			if(!isUpdate) {
				const setFav = new Set(fav.ids);
				if (setFav.has(ent.id)) return;
			} else {
				const iFav = fav.lst.findIndex(e => e.id === ent.id)
				
				if(iFav >= 0) fav.lst.splice(iFav, 1)
			}
			
			ent.fav = 1;
			fav.ids.push(ent.id);

			const data = {}
			savedAttrs.map(attr => {
				if(!ent.hasOwnProperty(attr)) return
				data[attr] = ent[attr]
			})
			fav.lst.push(data);
			
			do_save_myFavorites (entTyp, fav);
		}
		
		this.do_lc_remove_favorite = (isFav, ID_TABLE, obj) => {
			if(!isFav) {
				do_lc_send_mod_inList(isFav, ID_TABLE, obj)

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
						param 		: [isFav, ID_TABLE, obj],
						classBtn	: "btn-primary",
						autoclose	: true
					},
					CALCEL : {
						lab 		: $.i18n("common_btn_cancel"),
					}
				}
			});
		}

		const do_lc_send_mod_inList = (isFav, parTyp, obj) => {
			if(isFav) self.do_lc_remove_myFavorites(obj, parTyp)
			else self.do_lc_push_myFavorites(obj, parTyp)
		}

		this.do_lc_remove_myFavorites = (ent, entTyp) => {
			if (!ent || !ent.id) 	return;
			if (!App.data.user) 	return;
			
			if (!App.data.user[pr_varname])				App.data.user[pr_varname] 			= {};
			if (!App.data.user[pr_varname][entTyp]) 	App.data.user[pr_varname][entTyp] 	= {};
			
			var fav = App.data.user[pr_varname][entTyp];
			if (!fav.ids) fav.ids = [];
			if (!fav.lst) fav.lst = [];
			
			for (var i in fav.ids){
				if (fav.ids[i]==ent.id){
					fav.ids.splice(i, 1);
					fav.lst.splice(i, 1);
					break;
				}
			}
			do_save_myFavorites (entTyp, fav);
		}
		
		
		const do_save_myFavorites = (entTyp, data) => {
			const ref 		= req_gl_Request_Content_Send("ServiceTpyFavorite", "SVNsoMod");
			ref['entTyp'] 	= entTyp;
			ref['data'] 	= JSON.stringify(data);

			const fSucces	= [];
			fSucces.push(req_gl_funct(null, do_save_myFavorites_callback, []));

			const fError 	= req_gl_funct(null, do_gl_show_Notify_Msg_Error, [ $.i18n("common_err_ajax") ]);

			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV,
				req_gl_LS_SecurityHeaderBearer(App.keys.KEY_STORAGE_CREDENTIAL),
				ref, 100000, fSucces, fError);
		}

		const do_save_myFavorites_callback = (sharedJson) => {
			if (sharedJson[App['const'].SV_CODE] === App['const'].SV_CODE_API_YES) {
				sessionStorage[pr_varname] 	= JSON.stringify(App.data.user[pr_varname]);
				
				if(!pr_ctr_Sidebar) pr_ctr_Sidebar = App.controller.UI.Sidebar

				if(pr_ctr_Sidebar) {
					pr_ctr_Sidebar.do_lc_show_favorite()
				}
			} else {
				do_gl_show_Notify_Msg_Error($.i18n ("content_send_not_succes"));
			}
		}
		
		
		//-------------------- FavIds List ------------------------------------------------------------------------
		
		
		
		//-------------------- End ------------------------------------------------------------------------
		
		
		//--------------------------------------------------------------------------------------------
	};

	return { MainSidebar, MainHeader, MainNotification, MainFavorite};
});