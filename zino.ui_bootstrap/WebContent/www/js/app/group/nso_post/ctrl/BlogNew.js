define([
	'text!group/nso_post/tmpl/Blog_All.html'
	],
	function(
			Blog_All 		
	) {

	const BlogNew = function(header, content, footer) {
		const tmplName				= App.template.names;
		const tmplCtrl				= App.template.controller;
		const ctrlName				= "DashboardMain";

		const var_lc_STAT_VALIDE    = 2;
		var   var_lc_GROUP_ID       = null;
		const var_lc_GROUP_TYP      = 40000;
		var   self                  = this;
		// --------------------APIs--------------------------------------//
		this.do_lc_init = () => {
			tmplName.BLOG_LIST						= "Blog_List";	
			
			tmplName.BLOG_LIST_CATEGORY				= "Blog_List_Category";
			tmplName.BLOG_LIST_CONTENT				= "Blog_List_Content";
			tmplName.BLOG_LIST_CONTENT_DETAIL		= "Blog_List_Content_Detail";
			tmplName.BLOG_LIST_NOT_FOUND     		= "Blog_List_Not_Found";
			
			tmplName.BLOG_ENT						= "Blog_Ent";	
			tmplName.BLOG_MODIFY    				= "Blog_Modify";	
			tmplName.BLOG_CREATE    		    	= "Blog_Create";	
		}
		//--------------------------------------------------------------------------------------------------------------------------------	
		this.do_lc_show = () => {
			let params = req_gl_GetURLParameter(['groupId']);

			var_lc_GROUP_ID = params && params['groupId'] ? params['groupId'] : null;
					
			let obj = {files : []};
			do_lc_load_view(obj);
			do_lc_build_page(obj)
			do_lc_bind_event(obj);
		}

		const do_lc_show_msgbox_login = () => {
			App.MsgboxController.do_lc_show({
				title	: $.i18n("common_btn_message"),
				content : $.i18n("deal_ent_offer_new_no_login"),
				buttons	: {
					OK: {
						lab		: $.i18n("common_btn_login"),
						funct	: () => $("#div_login").click()
					},
					NO: {
						lab		: $.i18n("common_btn_cancel"),
						funct	: App.data.fCallBackNewOffer
					}
				}
			});
		}

		const do_lc_load_view = () => tmplCtrl.do_lc_put_tmplRaw(Blog_All);

		const do_lc_build_page = obj => {
			$("#div_main_content").html(tmplCtrl.req_lc_compile_tmpl(tmplName.BLOG_CREATE, obj));

			do_show_fileUploader(obj);
			App.SummerNoteController.do_lc_show("#div_new_blog", {height : 100});//text editor
		}

		//--------------------------------------------------------------------------------------------------------------------------------
		const do_lc_bind_event = obj => {
			$("#btn_save_blog").off('click').on('click', function(e) {
				let data = req_gl_data({
					dataZoneDom 	: $("#div_new_blog"),
					oldObject 		: obj,
					removeDeleted	: true
				});

				if(data.hasError) {
					do_gl_show_Notify_Msg_Error ($.i18n('common_err_entity_save'));
					return false;
				}

				do_lc_reform_sending_data(data);

				let isLogin = !App.controller.common.Login.can_lc_User_Guest();
				if(isLogin){
					do_lc_creat_new_offer(data);
				} else {
					App.data.fCallBackNewOffer = () => do_lc_creat_new_offer(data);
					do_lc_show_msgbox_login();
				}
			});
		}

		const do_lc_creat_new_offer = data => {
			let ref		= req_gl_Request_Content_Send("ServiceNsoPost", "SVNsoPostNewsNew");

			let fSucces	= [];
			fSucces.push(req_gl_funct(null, do_lc_open_offer_detail, []));

			let fError 	= req_gl_funct(App, do_lc_show_Msg , [$.i18n("common_err_ajax")]);

			data.do_lc_send_data(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, fSucces, fError, "obj");
		}

		const do_lc_open_offer_detail = sharedJson => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_gl_show_Notify_Msg_Success($.i18n("dashboard_create_offer_save_success"));
				let data = sharedJson[App['const'].RES_DATA];
				App.data.fCallBackNewOffer = null;

				let isLogin = !App.controller.common.Login.can_lc_User_Guest();
				isLogin ? window.open(`view_prj_post.html?id=${data.id}&code=${data.code01}`, "_self") : window.open(`index.html`, "_self");
			}else{
				do_gl_show_Notify_Msg_Error($.i18n("common_err_ajax"));
			}
		}

		const do_lc_reform_sending_data = originalData => {
			if(!var_lc_GROUP_ID) return;
			
			originalData.data.entId  = var_lc_GROUP_ID;
			originalData.data.entTyp = var_lc_GROUP_TYP;
			originalData.data.stat   = var_lc_STAT_VALIDE;
			originalData.data.typ02  = localStorage.languageId;
			
		}
		//--------------------------------------------------------------------------------------------------------------------------------
		const do_show_fileUploader = obj => {		

			let option		= {
					fileinput	: { param : {typ01: 1, typ02: 1, maxFiles : 1}},//option here
					obj			: obj//file existing here
			}
			do_gl_init_fileDropzone($("#div_fileinput_avatar"), option);
			
			let option2		= {
					fileinput	: { param : {typ01: 2, typ02: 10}},//option here
					obj			: obj//file existing here
			}
			
			do_gl_init_fileDropzone($("#div_files"), option2);
		}

		//--------------------------------------------------------------------------------------------------------------------------------

		const do_lc_show_Msg  = e => console.log(e);
	};

	return BlogNew;
});