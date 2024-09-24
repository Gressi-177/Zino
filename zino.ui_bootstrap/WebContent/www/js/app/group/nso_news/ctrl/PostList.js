define([
    'text!group/nso_news/tmpl/Post_List.html', 
    'text!group/nso_news/tmpl/Post_List_Element.html', 
    ],
    function(
		Post_List,
		Post_List_Element
    ){

	const PostList = function (grpName, header, content, footer) {
		const tmplName				= App.template.names;
		const tmplCtrl				= App.template.controller;
		//------------------controllers------------------------------------------------------
		var pr_ctr_Main 			= null;
		//-----------------------------------------------------------------------------------
		const pr_SERVICE_CLASS		= "ServiceNsoPost";
		const pr_SV_LST_CMT			= "SVLstCmt";
		const pr_SV_NEW_COMMENTS	= "SVNewCmt";
		
		const pr_POST_KEY_ENTER		= 13;
		const pr_ID_TABLE_OFFER		= 15000;
		const pr_POST_HAS_SUB 		= 2;
		const pr_POST_NUMBER 		= 10;
		//--------------------APIs--------------------------------------//
		this.do_lc_init	= () => {
			pr_ctr_Main 				= App.controller.Post.Main;
			
			tmplName.POST_LIST			= "Post_List";	
			tmplName.POST_LIST_ELEMENT	= "Post_List_Element";	
		}
		
		const do_lc_load_view = () => {
			tmplCtrl.do_lc_put_tmpl(tmplName.POST_LIST			, Post_List); 
			tmplCtrl.do_lc_put_tmpl(tmplName.POST_LIST_ELEMENT	, Post_List_Element); 
		}
		//--------------------------------------------------------------------------------------------
		this.do_lc_show = id => {               
			try{
				do_lc_load_view();
				$("#div_post_main").html(tmplCtrl.req_lc_compile_tmpl(tmplName.POST_LIST, {}));
				do_lc_get_list_ByAjax_Dyn(id);
			}catch(e) {
				console.log(e);
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], App.network, "post", "PostList", "do_lc_show", e.toString()) ;
			}
		};
		//--------------------------------------------------------------------------------------------
		const do_lc_get_list_ByAjax_Dyn = (id, reBuild = false) => {
			const cond 			= {
					entId: id				, reBuild : true,
					number: pr_POST_NUMBER	, nbLevel: pr_POST_HAS_SUB	, forced: true, 
			};
			
			const ref 			= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_LST_CMT, cond);
			
			const callbackFunct = (data) => do_show_list(data, id);
			
			const opt 			= {
					divMain			: "#post_list",
					divPagination	: "#post_panigation",
					url_api 		: App.path.BASE_URL_API_PRIV, 
					url_header 		: App.data["HttpSecuHeader"],
					url_api_param 	: ref,
					pageSize 		: pr_POST_NUMBER,
					pageRange		: 1,
					callback		: callbackFunct
			};
			
			do_gl_init_pagination_opt (opt);
		}
		//-------------------------------------------------------------------------------------------
		const do_show_list = (sharedJson, id) => {
			let data = can_gl_AjaxSuccess(sharedJson) ? sharedJson[App['const'].RES_DATA] : {};
			do_lc_show_list_comment(data, id);
		}
		
		const do_lc_show_list_comment = (data, id) => {
			let isLogin = !App.controller.common.Login.can_lc_User_Guest();
			$("#post_list").html(tmplCtrl.req_lc_compile_tmpl(tmplName.POST_LIST_ELEMENT, {data, isLogin}));
			do_lc_bind_event_comment(data, id);
		}
		
		const do_lc_bind_event_comment = (data, id) => {
			$("#btn_send_comment").off("click").on("click", () => {
				let comment = $("#inp_comment").val();
				let iParent = $("#inp_parent_reply").val();
				if(!comment || !comment.length)	return false;
				do_lc_send_comment(comment, iParent, id);
			})

			$(".a-reply").off("click").on("click", function(){
				let{parent, user} = $(this).data();
				parent && $("#inp_parent_reply").val(parent);
				if(user)	$("#inp_comment").val(`@${user} `).focus();
			})

			$("#inp_comment").off("keypress").on("keypress", function(e){
				if(e.keyCode == pr_POST_KEY_ENTER){
					$("#btn_send_comment").click();
					return;
				}
				let comment = $(this).val();
				(!comment || !comment.length) && $("#inp_parent_reply").val("");
			})
			
			$("#btn_login").off("click").on("click", e => $("#div_login").click());
		};
		
		const do_lc_send_comment = (comment, iParent, id) => {
			const ref      = req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_NEW_COMMENTS);
			ref["obj"]     = {comment,v02: id, parId: iParent};

			let fSucces    = [];
			fSucces.push(req_gl_funct(null, do_lc_afterSend_cmts, [id]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		const do_lc_afterSend_cmts = (sharedJson, id) => {
			if(can_gl_AjaxSuccess(sharedJson)) {	
				const data = sharedJson[App['const'].RES_DATA];
				data && do_lc_get_list_ByAjax_Dyn(id, true);
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_unknow'));
			}
		}
	};

	return PostList;
  });