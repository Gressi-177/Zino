define([
	'text!group/nso_news/tmpl/Blog_All.html'
	],
	function(
			Blog_All
	) {

	const BlogMain = function (header, content, footer) {
		const tmplName				= App.template.names;
		const tmplCtrl				= App.template.controller;

		const pr_varname_Category   = "lstCat";
		const pr_TYPE_BLOG = 17000;

		var pr_SEARCH_KEY			= "";
		var groupId                 = "";
		var isAdm                   = false;
		var pr_TYP_POST  			= "101";
		var pr_TYP_NEWS  			= "103";
		var multiLang               = "";
		var APP_WITH_PUB   			= 0;

		var self                    = this;
		var	pr_custom_paths			= {
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

		const pr_STAT_VALIDATED			= 2;
		const pr_STAT_VALIDATED_HIDDEN	= 3;
		const pr_NUMBER					= 20;

		const 	pr_OBJ_TYPE				= 17000;
		const 	RIGHT_ADM				= 100;
		const 	RIGHT_NSO_NEWS_G		= 17000001;
		const 	RIGHT_NSO_NEWS_N		= 17000002;
		const 	RIGHT_NSO_NEWS_M		= 17000003;
		const 	RIGHT_NSO_NEWS_D		= 17000004;
		const 	RIGHT_NSO_NEWS_R		= 17000005;

		const pr_object_pub = [
			{"stat": "GG", "ord" : 1},
			{"stat": "GG", "ord" : 2},
			{"stat": "GG", "ord" : 3},
			{"stat": "GG", "ord" : 4}
			];

		//---------------------------------------------------------------
		this.do_lc_init	= function() {
			if (!App.controller.Blog)				
				App.controller.Blog			= {};

			if (!App.controller.Blog.Main)				
				App.controller.Blog.Main 	= this;

			tmplName.BLOG_LIST						= "Blog_List";	
			tmplName.BLOG_LIST_CATEGORY				= "Blog_List_Category";
			tmplName.BLOG_LIST_CONTENT				= "Blog_List_Content";
			tmplName.BLOG_LIST_CONTENT_DETAIL		= "Blog_List_Content_Detail";
			tmplName.BLOG_LIST_NOT_FOUND     		= "Blog_List_Not_Found";
			
			tmplName.BLOG_ENT						= "Blog_Ent";	
			tmplName.BLOG_MODIFY    				= "Blog_Modify";	
			tmplName.BLOG_CREATE    		    	= "Blog_Create";	
		}

		var pr_grpPath 		= 'group/nso_news';
		var pr_showed		= false;
		this.do_lc_show = function(id, code, divContent, typ00, isPopup = false){
			if (!pr_showed){
				do_gl_lang_append (pr_grpPath + '/transl', self.do_lc_show_callback);
				pr_showed = true;
			}else {
				self.do_lc_show_callback();
			}
		};  
		this.do_lc_show_callback = () => {
			try {
				App.router.controller.do_lc_append_custom_tags(pr_custom_paths)
				
				if (!self.can_have_permission(RIGHT_NSO_NEWS_G)) {
					do_gl_init_msgbox_annonce($.i18n("prj_project_not_right_view"), () => {window.open("./", "_self")});
					return;
				}

				let params = req_gl_Url_Params();


				groupId       = params.groupId;
				isAdm         = params.adm == 1 ? true : false;
				
				if(!groupId && App.data.user.typ != 10)  return;
					
				multiLang    = localStorage.languageId;
				do_lc_load_view();
				do_lc_build_page();
			}catch(e) {
				console.log(e);
			}
		};

		this.can_have_permission = (role) => {
			const rightCode = `${role}`.substr(role.length - 1)
			var listUserRight = App.data.user.rights;

			if(!listUserRight){
				return -1;
			}

			if(listUserRight.includes(RIGHT_ADM)) return true
			if(listUserRight.includes(RIGHT_ADM * 100 + rightCode) 
				|| listUserRight.includes(pr_OBJ_TYPE * 1000 + rightCode)) return true

			return false;
		}

		const do_lc_load_view = () => {
			tmplCtrl.do_lc_put_tmplRaw(Blog_All);
			$("#li_search").removeClass("hide");
		}

		const do_lc_build_page = (mod) => {
			$("#div_main_content")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.BLOG_LIST	, {}));
			$("#div_Blog_Block_Content")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.BLOG_LIST_CONTENT	, {}));
			do_lc_get_blog(mod);
			
//			if(App.data["lstCat"]) self.do_lc_show_category();
		}

		const do_lc_get_blog = (mod) => {
			let multiStat 		= [pr_STAT_VALIDATED, pr_STAT_VALIDATED_HIDDEN].join(",");
			let ref = req_gl_Request_Content_Send_With_Params("ServiceNsoPost", "SVLstPage", {
				mod,
				searchkey     : pr_SEARCH_KEY,
				groupId,
				multiStat,
				langId        : multiLang,
				typ01         : pr_TYP_NEWS,
				forced        : mod,
				withAva 	  :true
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
				templContent 	= data.lst ? tmplName.BLOG_LIST_CONTENT_DETAIL : tmplName.BLOG_LIST_NOT_FOUND;

				data  = do_lc_rebuild_data(data);
				data  = do_lc_cond_delete_blog(data)
			}

			$("#div_blog_grid").html(tmplCtrl.req_lc_compile_tmpl(templContent, data));
			do_gl_scrollToEle("body");
			do_lc_bind_event_list();
		}
		
		const var_lc_limSize = 5;
		const do_lc_rebuild_data = (data) => {
			if (!APP_WITH_PUB) return data;
			
			if (var_lc_limSize<=0) return data;
			
			let lst = data.lst;
			if (lst.length<var_lc_limSize) return data;
			
			let nbOffer = Math.round(lst.length/var_lc_limSize);
			let off = [];
			for (i=0; i<nbOffer; i++){
				off.push({"stat": "GG", "ord" : i+1});
			}
			
			let posMin 	= var_lc_limSize;			
			for (i=0; i<nbOffer; i++){
				let posMax	= Math.max(posMin, lst.length-(nbOffer-i-2)*var_lc_limSize);
				let posPub 	= getRandomInt (posMin, posMax);
				lst.splice(posPub, 0, off[i]);
				posMin 		= posPub+2;			
			}	
			return data;
		}
		
		const getRandomInt = (min, max) => Math.floor(Math.random() * (max - min)) + min;
		
		const do_lc_cond_delete_blog = (data) => {
			data.lst = data.lst.map(item => {
				if(isAdm){
					item.isDel = true;
					return item;
				}
				
				if(item.uId01 == App.data.user.id) item.isDel = true;
				else item.isDel = false;
					
				return item;
			})
			
			return data;
		}
		//-------------------------------------------------------------------------------------------

		const do_lc_delete_post 	= function (id){
			var ref 		= req_gl_Request_Content_Send("ServiceNsoPost", "SVDel");	
			ref.id			= id;

			var fSucces			= [];
			fSucces.push(req_gl_funct(null	, do_lc_res_delete	, [])); //refresh menu
		
			var fError 		= req_gl_funct(App, do_gl_show_MsgAjax, [$.i18n("common_err_msg_unknow"), 0]);	
			
			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);		
		}
		
		const do_lc_res_delete = (sharedJson) => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_lc_build_page(true);
			}else{
				do_gl_show_Notify_Msg_Error($.i18n("common_err_msg_unknow"));
			}
		}

		const do_lc_bind_event_list = () => {
			$(".offer-item").off("click").on("click", function() {
				let {id, code} = $(this).data();
				if(id && code){
					App.router.controller.do_lc_run("VI_MAIN/prj_news",`view_prj_news.html?id=${id}&code=${code}`);
				}
			})

			$(".post-del").off("click").on("click", function() {
				let {id} = $(this).data();
				do_lc_delete_post(id);
			})

			$(".btn-addr").off("click").on("click", function() {
				let {addr, id, end, code} 	= $(this).data();
				if(!addr || (end ? req_gl_DayDiff(end) < 0 : false)){
					if(id && code)	App.router.controller.do_lc_run("VI_MAIN/prj_news",`view_prj_news.html?id=${id}&code=${code}`);

				}
				// let url 	= isValidURL(addr) ? addr : `https://google.com/search?q=${addr}`;
				
			    // url 	= isHttpUrl(addr) ? addr : `https://google.com/search?q=${addr}`;
			    
				// url && window.open(url, "_blank");
			})

			$("#inp_search_blog").off("keyup").on("keyup", function(e){
				e.preventDefault();
				pr_SEARCH_KEY	= $(this).val();
				do_gl_execute_debounce(do_lc_get_blog);
			})
		}
		
		
		const isHttpUrl = (addr) => {
			if(addr.includes("http")) return true;
			return false;
		}
	};

	return BlogMain;
});