define([
	'text!group/nso_post/tmpl/Blog_All.html',    

	'group/nso_post/ctrl/PostMain',
	// 'controller/prj/msg/MsgClaim'
	],
	function(        		
			Blog_All,
			PostMain
			// MsgClaim
	){

	const BlogEnt 	= function (grpName, header, content, footer) {
		const tmplName			= App.template.names;
		const tmplCtrl			= App.template.controller;
		//------------------controllers------------------------------------------------------
		var pr_ctr_Main 		= null;
		
		const pr_SERVICE_CLASS	= "ServiceNsoPost";
		const pr_SV_GET			= "SVNsoPostGet";
		const pr_SV_MOD			= "SVNsoPostMod";
		const pr_SV_MOD_HOT		= "SVNsoOfferDealModHot";//do nong cua ofer

		var files				= {files: []};
		
		var pr_TYP_POST  		        = "101";
		const pr_STAT_VALIDATED			= 2;
		const pr_STAT_VALIDATED_HIDDEN	= 3;
		const pr_NUMBER					= 10;
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= () => {
			if (!App.controller.Post )
				App.controller.Post         = {};

			if (!App.controller.Post.Main )
				App.controller.Post.Main    = new PostMain ( null, "#div_post_main", null );

			App.controller.Post.Main		.do_lc_init();

			if (!App.controller.Msg )
				App.controller.Msg         	= {};
			// if (!App.controller.Msg.Claim )
			// 	App.controller.Msg.Claim    = new MsgClaim ( null, null, null );

			// App.controller.Msg.Claim		.do_lc_init();
			
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
		
		this.do_lc_show = function(){
			try {
				
				let params = req_gl_GetURLParameter(['id', 'forManager', 'lang']);

				if(params && params.id){
					do_lc_show_ById(params['id'], params['forManager'], params['lang']);
					return;
				}
				
				if(ENT_ID){
					do_lc_show_ById(ENT_ID, false, PAGE_LANG);
					return;
				}
			} catch(e) {
				console.log(e);
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], App.network, "blog", "OfferMain", "do_lc_show", e.toString());
			}
		};
		//--------------------------------------------------------------------------------------------
		const do_show_Lang_Change = (data, lang) => do_lc_show_blog(data, lang);

		const do_lc_show_blog = (data, lang, mod) => {               
			try{	 
				do_lc_check_build_template();
//				data = do_lc_build_category(data);
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
		const do_lc_show_ById = (id, forManager, lang) => {
			do_lc_get_ent(id, forManager, lang);
		}
		//---------private-------------------------------------------------------than----------------------		
		const do_lc_get_ent = (id, forManager, lang) => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET, {id, forManager});

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
					let src = $(this).attr("src");
					App.MsgboxController.do_lc_show({
						content 	: `<img src="${src}" class="img_group_chat_popup"/>`,
						autoclose	: true,
						buttons		: "none",
					});	
				})
			})
			
			$("#div_post_content").find(".div-image-detail").each(function(){
				$(this).off('click').on('click', function(){
					let {path} = $(this).data();
					App.MsgboxController.do_lc_show({
						content 	: `<img src="${path}" class="img_group_chat_popup"/>`,
						autoclose	: true,
						buttons		: "none",
					});	
				})
			})
			
			if (blog.autUser == App.data.user.id) {
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
			let ref 			= req_gl_Request_Content_Send_With_Params("ServiceNsoPost", "SVNsoPostLstPrj", {multiStat, multiLang, groupId: data.entId, typ : pr_TYP_POST, mod});

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
				let {id} = $(this).data();
				id && window.open(`view_prj_post.html?id=${id}`, "_self");
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