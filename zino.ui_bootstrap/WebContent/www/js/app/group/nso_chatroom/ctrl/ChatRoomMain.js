define([
	'group/nso_chatroom/ctrl/ChatRoomChat',
	'group/nso_chatroom/ctrl/ChatRoomPerson',
	'group/nso_chatroom/ctrl/ChatRoomDocs',
	'group/nso_chatroom/ctrl/ChatRoomPost',
	'group/nso_chatroom/ctrl/ChatRoomUserOfGroup',
	'group/nso_chatroom/ctrl/ChatRoomEmojis',
	
	'group/nso_chatroom/ctrl/ChatWebRTC',
	'group/nso_chatroom/ctrl/ChatRoomIndexedDB',
	// 'group/nso_chatroom/ctrl/ChatWebChime',
	// 'group/nso_chatroom/ctrl/ChatWebChime_RTC',

	'group/nso_chatroom/ctrl/ChatRoomSocket',
	
	'text!group/nso_chatroom/tmpl/ChatRoom_Main.html',
	'text!group/nso_chatroom/tmpl/ChatRoom_Tabs.html',
	], function(
			ChatRoomChat,
			ChatRoomPerson,
			ChatRoomDocs,
			ChatRoomPost,
			ChatRoomUserOfGroup,
			ChatRoomEmojis,
			ChatWebRTC,
			ChatRoomIndexedDB,
			// ChatWebChime,
			// ChatWebChime_RTC,
			
			ChatRoomSocket,
			
			ChatRoom_Main_Tmpl,
			ChatRoom_Tab_Tmpl
			) {

	var ChatRoomMain     			= function (grpName, header, content, footer) {
		var pr_divHeader              = header;
		var pr_divContent             = content;
		var pr_divFooter              = footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:"ChatRoomChat";
		var pr_grpPath				= 'group/nso_chatroom';
		const tmplName				= App.template.names[pr_grpName] = {};
		const tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		var self                  = this;
		var var_lc_TYPE_SHOW      = null;
		var var_lc_GROUP_ID       = null;

		var	pr_custom_paths		= {
			"css"	: [
				"www/css/prj/custom_chat.css",
				"www/js/lib/imageviewer/viewer.css"
			],
			"js"	: [
				"https://sdk.amazonaws.com/js/aws-sdk-2.585.0.min.js",
				"https://unpkg.com/@ungap/url-search-params",
				"https://webrtc.github.io/adapter/adapter-latest.js"
			]
		};
		
		this.pr_LST_USER_ONLINE		= [];
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			tmplName.CHATROOM_MAIN										= pr_grpName+ "ChatRoom_Main";
			tmplName.CHATROOM_PERSON									= pr_grpName+ "ChatRoom_Person";
			
			
			tmplName.CHATROOM_TAB_GROUP_INFO							= pr_grpName+ "ChatRoom_Tab_Group_Info";
			tmplName.CHATROOM_TAB_GROUP_INFO_POPUP						= pr_grpName+ "ChatRoom_Tab_Group_Info_Popup";
			tmplName.CHATROOM_TAB_CHAT_MAIN				     			= pr_grpName+ "ChatRoom_Tab_Chat_Main";
			tmplName.CHATROOM_TAB_CHAT_CONTENT							= pr_grpName+ "ChatRoom_Tab_Chat_Content";
			tmplName.CHATROOM_TAB_CHAT_FOOTER							= pr_grpName+ "ChatRoom_Tab_Chat_Footer";
			tmplName.CHATROOM_TAB_CHAT_HEADER							= pr_grpName+ "ChatRoom_Tab_Chat_Header";
			tmplName.CHATROOM_TAB_CHAT_MSGBOX_CHOICE_OWNER				= pr_grpName+ "ChatRoom_Tab_Chat_Msgbox_Choice_Owner";
			tmplName.CHATROOM_TAB_CHAT_CONTENT_MSG_ITEM					= pr_grpName+ "ChatRoom_Tab_Chat_Content_Msg_Item";
			tmplName.CHATROOM_TAB_CHAT_CONTENT_MSG_READ					= pr_grpName+ "ChatRoom_Tab_Chat_Content_Msg_Read";
			tmplName.CHATROOM_TAB_CHAT_CONTENT_MSG_READ_ITEM			= pr_grpName+ "ChatRoom_Tab_Chat_Content_Msg_Read_Item";
			tmplName.CHATROOM_TAB_CHAT_CONTENT_MSG_READ_ITEM_CONTENT	= pr_grpName+ "ChatRoom_Tab_Chat_Content_Msg_Read_Item_Content";

			tmplName.CHATROOM_TAB_DOC_MAIN	    						= pr_grpName+ "ChatRoom_Tab_Doc_Main";
			tmplName.CHATROOM_TAB_DOC_DETAIL							= pr_grpName+ "ChatRoom_Tab_Doc_Detail";
			
			tmplName.CHATROOM_TAB_POST									= pr_grpName+ "ChatRoom_Tab_Post";
			tmplName.CHATROOM_TAB_POST_DETAIL							= pr_grpName+ "ChatRoom_Tab_Post_Detail";
			
			tmplName.CHATROOM_TAB_USER									= pr_grpName+ "ChatRoom_Tab_User";
			tmplName.CHATROOM_TAB_GROUP									= pr_grpName+ "ChatRoom_Tab_Group";
			tmplName.CHATROOM_TAB_GROUP_NEW								= pr_grpName+ "ChatRoom_Tab_Group_New";
			tmplName.CHATROOM_TAB_GROUP_RELATE							= pr_grpName+ "ChatRoom_Tab_Group_Relate";
			tmplName.CHATROOM_TAB_MSG_NEW 								= pr_grpName+ "ChatRoom_Tab_Msg_New";
			tmplName.CHATROOM_TAB_MSG_UNREAD							= pr_grpName+ "ChatRoom_Tab_Msg_UnRead";
			
			tmplName.CHATROOM_TAB_MEMBER								= pr_grpName+ "ChatRoom_Tab_Member";
			tmplName.CHATROOM_TAB_MEMBER_DETAIL							= pr_grpName+ "ChatRoom_Tab_Member_Detail";
			tmplName.CHATROOM_TAB_MEMBER_WAIT							= pr_grpName+ "ChatRoom_Tab_Member_Wait";
			
			tmplName.CHATROOM_TAB_CHAT_VIDEO 							= pr_grpName+ "ChatRoom_Tab_Chat_Video";
			tmplName.CHATROOM_POPUP_CHAT_VIDEO							= pr_grpName+ "ChatRoom_Popup_Chat_Video";
			
			tmplCtrl.do_lc_put_tmplRaw(ChatRoom_Main_Tmpl	, pr_grpName);
			tmplCtrl.do_lc_put_tmplRaw(ChatRoom_Tab_Tmpl	, pr_grpName);
			
			
			if (!App.controller.ChatRoom) App.controller.ChatRoom = {};
			
			if (!App.controller.ChatRoom.Socket) {
				App.controller.ChatRoom.Socket				= new ChatRoomSocket();
			}
			App.controller.ChatRoom.Socket.do_gl_init_SocketChat(App.data.user.login01);
			
			
			
			if (!App.controller.ChatRoom.Chat)  
				App.controller.ChatRoom.Chat				= new ChatRoomChat			(pr_grpName, null, null);
			
			if (!App.controller.ChatRoom.Docs)  
				App.controller.ChatRoom.Docs				= new ChatRoomDocs			(pr_grpName, null, null);
			
			if (!App.controller.ChatRoom.Post)  
				App.controller.ChatRoom.Post				= new ChatRoomPost			(pr_grpName, null, null);
			
			if (!App.controller.ChatRoom.UserOfGroup)  
				App.controller.ChatRoom.UserOfGroup			= new ChatRoomUserOfGroup	(pr_grpName, null, null);
			
			if (!App.controller.ChatRoom.Person)  
				App.controller.ChatRoom.Person				= new ChatRoomPerson		(pr_grpName, null, null);
			
			if (!App.controller.ChatRoom.ChatWebRTC)  
				App.controller.ChatRoom.ChatWebRTC			= new ChatWebRTC			(pr_grpName, null, null, null);

			if (!App.controller.ChatRoom.ChatRoomIndexedDB) 
				App.controller.ChatRoom.ChatRoomIndexedDB	= new ChatRoomIndexedDB	();

			if (!App.controller.ChatRoom.Emojis)  
				App.controller.ChatRoom.Emojis		        = new ChatRoomEmojis	();
			
			App.controller.ChatRoom.Person					.do_lc_init();
			App.controller.ChatRoom.Chat					.do_lc_init();
			App.controller.ChatRoom.Docs					.do_lc_init();
			App.controller.ChatRoom.Post					.do_lc_init();
			App.controller.ChatRoom.UserOfGroup				.do_lc_init();
			
			App.controller.ChatRoom.ChatWebRTC				.do_lc_init();
			App.controller.ChatRoom.ChatRoomIndexedDB		.do_lc_init();
			
		}

		var pr_showed		= false;
		this.do_lc_show = function(){
			if (!pr_showed){
				do_gl_lang_append (pr_grpPath + '/transl', self.do_lc_show_callback);
				pr_showed = true;
			}else {
				self.do_lc_show_callback();
			}
		};  
		
		this.do_lc_show_callback		= function(){
			try { 
				App.router.controller.do_lc_append_custom_tags(pr_custom_paths)
				//----hide menu minichat
				$("#men_prj_minichat")			. remove();				
				
				self.var_lc_URL_Aut_Header		= req_gl_LS_SecurityHeaderBearer(App.keys.KEY_STORAGE_CREDENTIAL);
				const params = req_gl_Url_Params();
				const {typ, id} = params;
				var_lc_GROUP_ID  = params && id ? parseInt(id) : null;
				var_lc_TYPE_SHOW = params && typ ? parseInt(typ) : null;
				
				$("#div_main_content")			.html(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_MAIN, {user: App.data.user}));

				if (var_lc_GROUP_ID != null && var_lc_TYPE_SHOW != null) {
					App.controller.ChatRoom.Person.do_lc_show(var_lc_TYPE_SHOW, var_lc_GROUP_ID);
				} else {
					let typ = localStorage.getItem("nsoGrpChatTyp") ? parseInt(localStorage.getItem("nsoGrpChatTyp")) : null;
					let id = localStorage.getItem("nsoGrpChatId") 	? parseInt(localStorage.getItem("nsoGrpChatId"))  : null;
					App.controller.ChatRoom.Person.do_lc_show(typ, id);
				}
//				do_lc_get_access_key();
				do_lc_bind_btn_mobile();				
				do_lc_build_list_message_wait_read();
			}catch(e) {				
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, self.var_lc_URL_Aut_Header, App.network, "prj.chatRoom", "ChatRoomMain", "do_lc_show", e.toString()) ;
			}
		};
		
		var do_lc_bind_btn_mobile = function () {
			$("#btn_chat_group").off("click").click(() => {
				$(".div_mobile").hide();
				$(".chatroom_left").show();
			})
		}
		
//		const do_lc_get_access_key = function(){
//			const ref 		= req_gl_Request_Content_Send_With_Params("ServiceCfgGroup", "SVCfgGroupGet", {"code":"TA_CFG_CHAT_WEB_RTC"});	
//
//			let fSucces		= [];
//			fSucces.push(req_gl_funct(App, App.funct.put, [ "keyChatWebRTC" ]));
//
//			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error [$.i18n("common_err_ajax")]);	
//			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, self.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError);
//		}
		
		const do_lc_build_list_message_wait_read = function(){
			const ref 		= req_gl_Request_Content_Send_With_Params("ServiceMsgMessage", "SVLstWaitRead", {});	

			let fSucces		= [];
//			fSucces.push(req_gl_funct(App, App.funct.put, [ "lstMsgWaitRead" ]));
			
			fSucces.push(req_gl_funct(null, do_lc_show_msg_wait_read, []));
			
			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, self.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError);
		}
		
		const do_lc_show_msg_wait_read = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {
				const data = sharedJson[App['const'].RES_DATA];
				if(data){
					App.controller.ChatRoom.Person.do_lc_show_messge_wait_read([...data])
					
					//----hide menu minichat
					setTimeout(function(){
						$("#men_prj_minichat"). remove();	
					},2000);
				}
			} else {   
//				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}
		
	};

	return ChatRoomMain;
});