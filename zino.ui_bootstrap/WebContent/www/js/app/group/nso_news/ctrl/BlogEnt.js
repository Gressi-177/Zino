define([
	'prjImageViewer/viewer',
	'text!group/nso_news/tmpl/Blog_All.html',    

	'group/nso_news/ctrl/PostMain',
	'group/nso_news/ctrl/BlogMain',
	// 'controller/prj/msg/MsgClaim'
	],
	function(        	
			Viewer,
			Blog_All,
			PostMain,
			BlogMain
			// MsgClaim
	){

	var BlogEnt 	= function (grpName, header, content, footer) {
		const tmplName			= App.template.names;
		const tmplCtrl			= App.template.controller;
		//------------------controllers------------------------------------------------------
		var pr_ctr_Main 		= null;
		
		const pr_SERVICE_CLASS	= "ServiceNsoPost";
		const pr_SV_GET			= "SVGet";
		const pr_SV_MOD			= "SVMod";
		const pr_SV_MOD_HOT		= "SVNsoOfferDealModHot";//do nong cua ofer

		var files				= {files: []};
		var	pr_custom_paths		= {
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
		
		var pr_TYP_POST  		        = "101";
		var pr_TYP_NEWS  			= "103";
		const pr_STAT_VALIDATED			= 2;
		const pr_STAT_VALIDATED_HIDDEN	= 3;
		const pr_NUMBER					= 10;

		const 	RIGHT_NSO_NEWS_G		= 17000001;
		const 	RIGHT_NSO_NEWS_N		= 17000002;
		const 	RIGHT_NSO_NEWS_M		= 17000003;
		const 	RIGHT_NSO_NEWS_D		= 17000004;
		const 	RIGHT_NSO_NEWS_R		= 17000005;
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= () => {
			if (!App.controller.Post )
				App.controller.Post         = {};

			if (!App.controller.Post.Main )
				App.controller.Post.Main    = new PostMain ( null, "#div_post_main", null );

			App.controller.Post.Main		.do_lc_init();

			if (!App.controller.Msg )
				App.controller.Msg         	= {};
			

			if (!App.controller.Blog)				
				App.controller.Blog			= {};
			if (!App.controller.Blog.Main)
				App.controller.Blog.Main    = new BlogMain ( null, "#div_blog_main", null );

			App.controller.Blog.Main		.do_lc_init();
			
			tmplName.BLOG_LIST						= "Blog_List";
			tmplName.BLOG_LIST_CATEGORY				= "Blog_List_Category";
			tmplName.BLOG_LIST_CONTENT				= "Blog_List_Content";
			tmplName.BLOG_LIST_CONTENT_DETAIL		= "Blog_List_Content_Detail";
			tmplName.BLOG_LIST_NOT_FOUND     		= "Blog_List_Not_Found";
			
			tmplName.BLOG_ENT						= "Blog_Ent";
			tmplName.BLOG_MODIFY    				= "Blog_Modify";
			tmplName.BLOG_CREATE    		    	= "Blog_Create";
			tmplName.BLOG_ENT_CONTENT_DETAIL_LIST  	= "Blog_Ent_Content_Detail_List";
		}

		var pr_grpPath 		= 'group/nso_news';
		var pr_showed		= false;
		this.do_lc_show = function(){
			if (!pr_showed){
				do_gl_lang_append (pr_grpPath + '/transl', do_lc_show_callback);
				pr_showed = true;
			}else {
				do_lc_show_callback();
			}
		};  
		
		var do_lc_show_callback = function(){
			try {
				App.router.controller.do_lc_append_custom_tags(pr_custom_paths)

				if (!App.controller.Blog.Main.can_have_permission(RIGHT_NSO_NEWS_G) && !App.controller.Blog.Main.can_have_permission(RIGHT_NSO_NEWS_M)) {
					do_gl_init_msgbox_annonce($.i18n("prj_project_not_right_view"), () => {window.open("./", "_self")});
					return;
				}

				let params = req_gl_Url_Params();
				const {id, code, forManager, lang} = params;

				if(code && id){
					do_lc_show_ById({id, code, forManager, lang});
					return;
				}
				
			} catch(e) {
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], App.network, "blog", "OfferMain", "do_lc_show", e.toString());
			}
		};
		//--------------------------------------------------------------------------------------------

		const do_show_Lang_Change = (data, lang) => do_lc_show_blog(data, lang);

		const do_lc_show_blog = (data, lang, mod) => {               
			try{	 
				do_lc_check_build_template();
				$("#div_main_content").html(tmplCtrl.req_lc_compile_tmpl(tmplName.BLOG_ENT	, data));
				do_bind_ent_event(data, lang);
				App.controller.Post.Main.do_lc_show(data.id);
				do_lc_get_list_blog(data, mod);
			}catch(e) {
				console.log(e);
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], App.network, "blog", "BlogEnt", "do_lc_show", e.toString());
			}
		};
		
//		const do_lc_build_category = (data) => {
//			if(data.categorie){
//				let cats = data.categorie;
//				
//				cats = cats.map(cat => {
//					if(cat.descr && cat.descr.length > 0){
//						if(typeof cat.descr === 'string' ){
//							cat.descr = JSON.parse(cat.descr);
//						}
//					}
//					return cat;
//				} );
//			}
//			
//			return data;
//		}
//		
		//--------------------------------------------------------------------------------------------
		const do_lc_show_ById = (params) => {
			do_lc_get_ent(params);
		}
		//---------private-------------------------------------------------------than----------------------		
		const do_lc_get_ent = (params) => {
			let {lang} = params;
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET, {...params, forced: true});

			let fSucces		= [];
			fSucces.push(req_gl_funct(App, do_lc_show_ent, [lang]));

			let fError 		= req_gl_funct(App, do_gl_show_MsgAjax, [ $.i18n("common_err_ajax") ]);

			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}
		//--------------------------------------------------------------------------------------------
		const do_lc_show_ent = (sharedJson, lang) => {
			if (can_gl_AjaxSuccess(sharedJson)) {
				let data = sharedJson[App['const'].RES_DATA];
				do_lc_show_blog(data, lang);
			} else {
//				do_gl_show_Notify_Msg_Error($.i18n ("deal_ent_blog_msg_no_get"));
//				window.open(`index.html`, "_self");
			}
		}
		//--------------------------------------------------------------------------------------------
		const do_bind_ent_event = (blog, lang) => {
			
			$('.carousel').carousel({
				  interval: 3000
			})
				
			$("#btn_view_detail").off("click").on("click", function() {
				let {addr} 	= $(this).data();
				let url 	= isValidURL(addr) ? addr : `https://google.com/search?q=${addr}`;
				url && window.open(url, "_blank");
			})
			
			$("#div_post_content").find("img").each(function(){
				$(this).off('click').on('click', function(){
					const viewer = new Viewer(document.getElementById('div_post_content'), {
						hide: function () {
							viewer.destroy();
						},
					});
					// let src = $(this).attr("src");
					// App.MsgboxController.do_lc_show({
					// 	content 	: `<img src="${src}" class="img_group_chat_popup"/>`,
					// 	autoclose	: true,
					// 	buttons		: "none",
					// });	
				})
			})
			
			$("#div_post_content").find(".div-image-detail").each(function(){
				$(this).off('click').on('click', function(){
					const viewer = new Viewer(document.getElementById('div_post_content'), {
						hide: function () {
							viewer.destroy();
						},
					});
					// let {path} = $(this).data();
					// App.MsgboxController.do_lc_show({
					// 	content 	: `<img src="${path}" class="img_group_chat_popup"/>`,
					// 	autoclose	: true,
					// 	buttons		: "none",
					// });	
				})
			})
			
			if (blog.uId01 == App.data.user.id) {
				$("#btn_modify").off("click").on("click", () => {
					let data = {blog, cats : App.data.cats, typs : App.data.offerTyps};
					
					$("#div_main_content").html(tmplCtrl.req_lc_compile_tmpl(tmplName.BLOG_MODIFY, data));
					$("#select_lang").find("option[value='" + blog.typ02 +"']").attr("selected" , "selected");
					do_show_fileUploader(blog);
					do_lc_BindingDateTime('#div_modify_blog');
					App.SummerNoteController.do_lc_show("#div_modify_blog", {height : 100});//text editor
					do_lc_bind_event_modify(blog, lang);
				})
			} else {
				$("#btn_modify").hide();
			}
			
			// $("#btn_Msg_Claim").off("click").on("click", () => App.controller.Msg.Claim.do_lc_show(offer.id));
		}
		
		
		const do_lc_get_list_blog = (data, mod) => {
			let multiStat 		= [pr_STAT_VALIDATED, pr_STAT_VALIDATED_HIDDEN].join(",");
			let multiLang       = localStorage.languageId;
			let ref = req_gl_Request_Content_Send_With_Params("ServiceNsoPost", "SVLstPage", {
				mod,
				multiStat,
				langId        : multiLang,
				typ01         : pr_TYP_NEWS,
				forced        : mod,
				withAva 	  : true
			});

			const callbackFunct = (data) => do_lc_show_list_pagination(data);

			const opt 			= {
					divMain			: "#div_blog_grid",
					divPagination	: "#blog-pagination",
					url_api 		: App.path.BASE_URL_API_PRIV, 
					url_header 		: App.data["HttpSecuHeader"],
					url_api_param 	: ref,
					pageSize 		: pr_NUMBER,
					pageRange		: 1,
					callback		: callbackFunct
			};

			do_gl_init_pagination_opt (opt);
		}
		//-------------------------------------------------------------------------------------------
		const do_lc_show_list_pagination = sharedJson => {
			let data 			= {};
			let templContent 	= tmplName.BLOG_LIST_NOT_FOUND;
			if (can_gl_AjaxSuccess(sharedJson)) {
				data 			=  sharedJson[App['const'].RES_DATA];
				templContent 	= data.lst ? tmplName.BLOG_ENT_CONTENT_DETAIL_LIST : tmplName.BLOG_LIST_NOT_FOUND;

//				data  = do_lc_rebuild_data(data);
			}

			$("#div_blog_grid").html(tmplCtrl.req_lc_compile_tmpl(templContent, data));
			do_gl_scrollToEle("body");
			do_lc_bind_event_list();
		}
		
		
		const do_lc_bind_event_list = () => {
			$(".offer-item").off("click").on("click", function() {
				let {id, code} = $(this).data();
				if(id && code){
					App.router.controller.do_lc_run("VI_MAIN/prj_news",`view_prj_news.html?id=${id}&code=${code}`);
				}
			})
		}
		
		//--------------------------------------------------------------------------------------------------------------------------------
		const do_lc_BindingDateTime = function(div) {
			$(div).find(".input-datetime").each(function(){
				let $this = $(this);
				let {formatDate = "DD-MM-YYYY", date} = $this.data();
				if(App.language === "en") formatDate = "MM-DD-YYYY";
				$this.datetimepicker({
					locale: App.language,
					widgetPositioning: {
						horizontal	: 'left',
						vertical	: 'top'
					},
					format : formatDate,
					date : new Date(date ? date : "")
				})
			})
		}
		
		const do_show_fileUploader = function (obj) {		
			if (obj.files) {
				files.files = obj.files;
			}
			let option		= {
					fileinput	: { param : {typ01: 1, typ02: 1, maxFiles : 1}},//option here
					obj			: files//file existing here
			}
			do_gl_init_fileDropzone($("#div_fileinput_avatar"), option);
			
			let option2		= {
					fileinput	: { param : {typ01: 2, typ02: 10}},//option here
					obj			: files//file existing here
			}
			
			do_gl_init_fileDropzone($("#div_files"), option2);
		}
		
		const do_lc_bind_event_modify = (blog, lang) => {
			$(".input-categorie").off("change").on("change", function(){
				let $this = $(this);
				if($this.is(":checked")){
					$(".input-categorie")	.prop('checked', false);
					$this					.prop('checked', true);
				}
			})
			
			$("#btn_calcel").off("click").on("click", () => {
				do_lc_show_blog(blog);
				do_bind_ent_event(blog);
			})
			
			$("#btn_save_blog").off("click").on("click", () => {
				let data = req_gl_data({
					dataZoneDom 	: $("#div_modify_blog"),
					oldObject 		: blog,
					removeDeleted	: true
				});
				
				if(data.hasError) {
					do_gl_show_Notify_Msg_Error ($.i18n('common_err_entity_save'));
					return false;
				}
				
				//do_lc_reform_sending_data(data);
				do_lc_modify_blog(data, lang);
			})
		}
		
//		const do_lc_reform_sending_data = originalData => {
//			let dataCat = originalData.data.topicOpt01;
//			if(dataCat){
//				let dataGenerated = [];
//				if(dataCat){
//					for(let o in dataCat){
//						if(dataCat[o] == 1)	dataGenerated.push({"catId" : o});
//					}
//				}
//				originalData.data.cats = dataGenerated;
//			}
//		}
		
		const do_lc_modify_blog = (data, lang) => {
			let ref		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_MOD);

			let fSucces	= [];
			fSucces.push(req_gl_funct(null, do_lc_open_blog_detail, [lang]));

			let fError 	= req_gl_funct(App, do_gl_show_MsgAjax, [$.i18n("common_err_ajax")]);

			data.data.files = files.files;
			data.do_lc_send_data(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, fSucces, fError, "obj");
		}
		
		const do_lc_open_blog_detail = (sharedJson, lang) => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_gl_show_Notify_Msg_Success($.i18n("common_success_update"));
				let data = sharedJson.res_data;
				let mod = true;
				do_lc_show_blog(data, lang, mod);
			}else{
				do_gl_show_Notify_Msg_Error($.i18n("common_err_ajax"));
			}
		}
		//--------------------------------------------------------------------------------------------------
		const do_lc_check_build_template = () => {
			tmplCtrl.do_lc_put_tmplRaw (Blog_All);	
		}
		
	};

	return BlogEnt;
});