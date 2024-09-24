define([
	'text!group/nso_post/tmpl/Blog_All.html'
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
		var multiLang               = "";
		var APP_WITH_PUB   			= 0;

		var self                    = this;

		const pr_STAT_VALIDATED			= 2;
		const pr_STAT_VALIDATED_HIDDEN	= 3;
		const pr_NUMBER					= 20;

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

		this.do_lc_show = () => {
			try {
				groupId       = parseInt(req_gl_GetURLParameter(['groupId']).groupId);
				isAdm         = parseInt(req_gl_GetURLParameter(['adm']).adm) == 1 ? true : false;
				
				if(!groupId && App.data.user.typ != 10)  return;
					
				multiLang    = localStorage.languageId;
				do_lc_load_view();
				do_lc_build_page();
			}catch(e) {
				console.log(e);
			}
		};

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
			let ref 			= req_gl_Request_Content_Send_With_Params("ServiceNsoPost", "SVNsoPostLstPrj", {mod, keyword : pr_SEARCH_KEY, groupId, multiStat, multiLang, typ : pr_TYP_POST});

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
				
				if(item.nameUser == App.data.user.login) item.isDel = true;
				else item.isDel = false;
					
				return item;
			})
			
			return data;
		}
		//-------------------------------------------------------------------------------------------

		const do_lc_delete_post 	= function (id){
			var ref 		= req_gl_Request_Content_Send("ServiceNsoPost", "SVNsoPostDel");	
			ref.id			= id;
			
			var lock 		= {};			
			lock.objectType = pr_TYPE_BLOG; 	//integer
			lock.objectKey 	= id; 		//integer
			ref['lock'	]	= JSON.stringify(lock);
			
			var fSucces			= [];
			fSucces.push(req_gl_funct(null	, do_lc_res_delete	, [])); //refresh menu
		
			var fError 		= req_gl_funct(App, do_gl_show_MsgAjax, [$.i18n("common_err_ajax"), 0]);	
			
			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);		
		}
		
		const do_lc_res_delete = (sharedJson) => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_lc_build_page(true);
			}else{
				do_gl_show_Notify_Msg_Error($.i18n("common_err_ajax"));
			}
		}

		const do_lc_bind_event_list = () => {
			$(".offer-item").off("click").on("click", function() {
				let {id} = $(this).data();
				id && window.open(`view_prj_post.html?id=${id}`, "_self");
			})

			$(".post-del").off("click").on("click", function() {
				let {id} = $(this).data();
				do_lc_delete_post(id);
			})

			$(".btn-addr").off("click").on("click", function() {
				let {addr, id, end} 	= $(this).data();
				if(!addr || (end ? req_gl_DayDiff(end) < 0 : false)){
					!!id && window.open(`view_prj_post.html?id=${id}`, "_self");
					return false;
				}
				let url 	= isValidURL(addr) ? addr : `https://google.com/search?q=${addr}`;
				
			    url 	= isHttpUrl(addr) ? addr : `https://google.com/search?q=${addr}`;
			    
				url && window.open(url, "_blank");
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