define([
	'text!group/nso_news/tmpl/Blog_All.html',
	'group/nso_news/ctrl/BlogMain',
	],
	function(
			Blog_All,
			BlogMain, 		
	) {

	const BlogNew = function (grpName, header, content, footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		const ctrlName				= "DashboardMain";

		const var_lc_STAT_VALIDE    = 2;
		var   var_lc_GROUP_ID       = null;
		const var_lc_GROUP_TYP      = 40000;
		const 	RIGHT_NSO_NEWS_G		= 17000001;
		const 	RIGHT_NSO_NEWS_N		= 17000002;
		const 	RIGHT_NSO_NEWS_M		= 17000003;
		const 	RIGHT_NSO_NEWS_D		= 17000004;
		const 	RIGHT_NSO_NEWS_R		= 17000005;

		const pr_TYP01_NEWS				= 103;
		
		var	pr_custom_paths	= {
			"css"	: [
				"www/css/prj/custom_chat.css",
				"www/css/prj/custom_post.css",
				"www/css/prj/chime_video.css"
			],
			"js"	: [
				"https://sdk.amazonaws.com/js/aws-sdk-2.585.0.min.js",
				"https://unpkg.com/@ungap/url-search-params",
				"https://webrtc.github.io/adapter/adapter-latest.js"
			]
		};

		var   self                  = this;
		// --------------------APIs--------------------------------------//
		this.do_lc_init = () => {
			if(!tmplName) {
				App.template.names[pr_grpName] = {}
				tmplName 	= App.template.names[pr_grpName]
			}
			tmplName.BLOG_LIST						= "Blog_List";	
			
			tmplName.BLOG_LIST_CATEGORY				= "Blog_List_Category";
			tmplName.BLOG_LIST_CONTENT				= "Blog_List_Content";
			tmplName.BLOG_LIST_CONTENT_DETAIL		= "Blog_List_Content_Detail";
			tmplName.BLOG_LIST_NOT_FOUND     		= "Blog_List_Not_Found";
			
			tmplName.BLOG_ENT						= "Blog_Ent";	
			tmplName.BLOG_MODIFY    				= "Blog_Modify";	
			tmplName.BLOG_CREATE    		    	= "Blog_Create";	

			if (!App.controller.Blog)				
				App.controller.Blog			= {};
			if (!App.controller.Blog.Main)
				App.controller.Blog.Main    = new BlogMain ( null, "#div_blog_main", null );

			App.controller.Blog.Main		.do_lc_init();
		}
		//--------------------------------------------------------------------------------------------------------------------------------	
		var pr_grpPath 		= 'group/nso_news';
		var pr_showed		= false;
		this.do_lc_show = function(){
			if (!pr_showed){
				do_gl_lang_append (pr_grpPath + '/transl', self.do_lc_show_callback);
				pr_showed = true;
			}else {
				self.do_lc_show_callback();
			}
		};  
		this.do_lc_show_callback = () => {
			// TODO: Trans
			App.router.controller.do_lc_append_custom_tags(pr_custom_paths)

			if (!App.controller.Blog.Main.can_have_permission(RIGHT_NSO_NEWS_N)) {
				do_gl_init_msgbox_annonce($.i18n("prj_project_not_right_view"), () => {window.open("./", "_self")});
				return;
			}

			let params = req_gl_Url_Params();
			
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
			let ref		= req_gl_Request_Content_Send("ServiceNsoPost", "SVNew");
			data

			let fSucces	= [];
			fSucces.push(req_gl_funct(null, do_lc_open_offer_detail, []));

			let fError 	= req_gl_funct(App, do_lc_show_Msg , [$.i18n("common_err_msg_unknow")]);

			data.do_lc_send_data(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, fSucces, fError, "obj");
		}

		const do_lc_open_offer_detail = sharedJson => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_gl_show_Notify_Msg_Success($.i18n("dashboard_create_offer_save_success"));
				let data = sharedJson[App['const'].RES_DATA];
				App.data.fCallBackNewOffer = null;

				let isLogin = !App.controller.common.Login.can_lc_User_Guest();
				isLogin ? window.open(`view_prj_news.html?id=${data.id}&?code=${data.code01}`, "_self") : window.open(`index.html`, "_self");
			}else{
				do_gl_show_Notify_Msg_Error($.i18n("common_err_msg_unknow"));
			}
		}

		const do_lc_reform_sending_data = originalData => {
			originalData.data.stat01     = var_lc_STAT_VALIDE;
			originalData.data.typ02      = localStorage.languageId;
			originalData.data.typ01      = pr_TYP01_NEWS;
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