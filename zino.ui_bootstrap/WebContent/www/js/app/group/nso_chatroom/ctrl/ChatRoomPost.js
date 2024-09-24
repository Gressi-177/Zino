define([], function(){

	const ChatRoomPost 	= function (grpName, header, content, footer) {
		var pr_divHeader              = header;
		var pr_divContent             = content;
		var pr_divFooter              = footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:"ChatRoomChat";
		var pr_grpPath				= 'group/nso_chatroom';
		const tmplName				= App.template.names[pr_grpName];
		const tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------

		//------------------variable pagination post------------------------------------------------------
		const pr_NUMBER_RECORD 		= 8;
		
		const pr_TYP_MSG_PRIVATE 	= 200;
		const pr_TYP_MSG_PUBLIC 	= 201;
		
		const pr_TYP_CHAT_USER		= 1;
		const pr_TYP_CHAT_GROUP		= 2;
		const pr_TYP_CHAT_CONTACT	= 3;
		
		var   pr_isLoadMore         = false;
		
		const pr_STAT_VALIDATED			= 2;
		const pr_STAT_VALIDATED_HIDDEN	= 3;
		var pr_ENT_TPY_GROUP        = 40000;
		var pr_SEARCH_KEY			= "";
		var catIds					= "";
		var pr_TYP_POST  			= 101;
		var multiLang               = "";
		
		const pr_KEY_ENTER 			= 13;
		
		const pr_member_lev_manager 			= 0;
		const pr_member_lev_worker 				= 2;
		const pr_member_lev_owner 				= 10;
		//------------------controllers------------------------------------------------------
		var pr_ctr_Main 			= null;
		var pr_ctr_User 			= null;
		var pr_ctr_UserOfGroup		= null;
		var pr_ctr_ChatWebRTC		= null;
		
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			pr_ctr_Main 			= App.controller.ChatRoom.Main;
			pr_ctr_Person 			= App.controller.ChatRoom.Person
			pr_ctr_Chat 			= App.controller.ChatRoom.Chat
			pr_ctr_UserOfGroup 		= App.controller.ChatRoom.UserOfGroup;
			pr_ctr_ChatWebRTC 		= App.controller.ChatRoom.ChatWebRTC;
			// pr_ctr_ChatWebChime 	= App.controller.ChatRoom.ChatWebChime;

			
		}

		//---------show-----------------------------------------------------------------------------
		this.do_lc_show = function(obj){      //typChat user or group         
			try{
				do_lc_load_view(obj);
			}catch(e) {				
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "prj.chat", "ChatRoomPost", "do_lc_show", e.toString()) ;
			}
		};
		
		const do_lc_load_view = function(obj){
			$("#div_post").html(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_POST	, {}));
			
			if(can_gl_MobileOrTablet()){
				$("#div_post_block").show();
				$(".btn-resize-post").find("i").toggleClass("mdi-window-minimize mdi-window-maximize");
				do_lc_get_offer(obj);
			}
			
			$('#btn_new_post').off("click").click(() => {
				window.open(`view_prj_post_new.html?groupId=${obj.id}`, "_blank");
			});
			
			$('#btn_lst_post').off("click").click(() => {
				let url = `view_prj_post_list.html?groupId=${obj.id}`;
				
				const isMe = App.data.lstGrpMember[App.data.user.id];
				if([pr_member_lev_manager, pr_member_lev_owner].includes(isMe.typ)) url = `view_prj_post_list.html?groupId=${obj.id}&adm=1`;
				
				window.open(url, "_blank");
			});
			
			$(".btn-resize-post").off("click").on("click", function(){
				let $this 		= $(this);
				let child		= $this.find("i");
				let {divtoggle} = $this.data();

				$(divtoggle)	.toggle("hide");
				child			.toggleClass("mdi-window-maximize mdi-window-minimize")
				
				do_lc_get_offer(obj);
			})	
		}
		
		const do_lc_get_offer = (obj) => {
			let multiStat 		= [pr_STAT_VALIDATED, pr_STAT_VALIDATED_HIDDEN].join(",");
			let ref 			= req_gl_Request_Content_Send_With_Params("ServiceNsoPost", "SVNsoPostLstByEntId", {entId: obj.id, entTyp: pr_ENT_TPY_GROUP, keyword : pr_SEARCH_KEY, multiStat, multiLang, type : pr_TYP_POST});

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_show_list_pagination, []));
			
			let fError 		= req_gl_funct(App, do_gl_show_MsgAjax, [$.i18n("common_err_ajax")]);
			
			App.network.do_lc_ajax_bg (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_show_list_pagination = sharedJson => {
			let data 			= {};
			if (can_gl_AjaxSuccess(sharedJson)) {
				data 			=  sharedJson[App['const'].RES_DATA];
				if (data.lst.length > 10) {
					data.lst = data.lst.slice(0, 9);
				}
				
				data.lst = data.lst.map(item => {
					if(!item.files) return item;
					let files = item.files;
					let avatar = files.find(file => file.typ01 == 2);
					item.avatar = avatar? avatar : null;
					return item;
				})
				
				$("#div_post_list")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_POST_DETAIL	, {data: data.lst}));
				do_lc_bind_btn_slide_post();
			}else{
				$("#div_post_list")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_POST_DETAIL	, {}));
			}
		}

		const do_lc_bind_btn_slide_post = () => {
			$(".slideshow-img").off("click").click(function() {
				var id		= $(this).attr("data-id");
				window.open(`view_prj_post.html?id=${id}`, "_blank");
			});
		}

		
	}

	return ChatRoomPost;
});