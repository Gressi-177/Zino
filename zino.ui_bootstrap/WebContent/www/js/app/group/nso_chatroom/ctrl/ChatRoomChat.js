define([
	'group/nso_chatroom/ctrl/Webcam',
	'group/nso_chatroom/ctrl/Recorder',
	'prjImageViewer/viewer',
	
	
	],
	function(
			Webcam,
			Recorder,
			Viewer
	){

	const ChatRoomChat 	= function (grpName, header, content, footer) {
		var pr_divHeader              = header;
		var pr_divContent             = content;
		var pr_divFooter              = footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:"ChatRoomChat";
		var pr_grpPath				= 'group/nso_chatroom';
		const tmplName				= App.template.names[pr_grpName];
		const tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------

		const pr_SERVICE_CLASS			= "ServiceMsgMessage"; //to change by your need
		const pr_SV_LIST				= "SVChatLst";
		const pr_SV_DEL_LIST			= "SVChatLstById";
		const pr_SV_MEMBER_LIST			= "SVMemberLst";
		const pr_SV_MEMBER_ROLE			= "SVMemberRole";
		const pr_SV_MEMBER_JOIN			= "SVMemberJoin";
		const pr_SV_MEMBER_JOIN_PUBLIC	= "SVMemberJoinPublic";
		const pr_SV_MEMBER_CANCEL_JOIN	= "SVMemberCancelJoin";
		const pr_SV_NEW					= "SVChatNew";
		const pr_SV_NEW_WITH_IMG		= "SVChatNewWithImg";
		const pr_SV_DEL					= "SVChatDel";
		const pr_SV_HIDE				= "SVChatHide";
		const pr_SV_GET_TOTAL 			= "SVCountTotal";
		const pr_SV_NEW_HISTORY			= "SVChatHistoryNew";
		const pr_SV_LST_HISTORY			= "SVChatHistoryLst";

		const pr_SERVICE_CLASS_GROUP	= "ServiceNsoGroupChat"; //to change by your need
		const pr_SV_DEL_GROUP			= "SVDelRoom";
		const pr_SV_OUT_GROUP			= "SVOutRoom";
		const pr_SV_MOD_GROUP			= "SVModRoom";
		//------------------variable pagination post------------------------------------------------------
		const pr_MSG_NUMBER 			= 100;

		const pr_TYP_MSG_PRIVATE 		= 200;
		const pr_TYP_MSG_PUBLIC 		= 201;

		const pr_TYP_CHAT_USER			= 1;
		const pr_TYP_CHAT_GROUP			= 2;
		const pr_TYP_CHAT_CONTACT		= 3;

		const TYP_KINESIS_DEACTIVE		= 0;
		const TYP_KINESIS_ACTIVE		= 1;

		const pr_KEY_ENTER 				= 13;
		const pr_KEY_ENTER_CTRL			= 10;
		const pr_KEY_ENTER_ALT			= 10;

		const pr_member_lev_manager 	= 0;
		const pr_member_lev_worker 		= 2;
		const pr_member_lev_owner 		= 10;

		const pr_number_msg_closest		= 10;

		const CHAT_GROUP_PRIVATE		= 401;
		const CHAT_GROUP_PUBLIC			= 402;
		
		var pr_ENT_TPY_GROUP        	= 40000;

		var pr_SEARCH_KEY				= "";
		var catIds						= "";
		var pr_TYP_POST  				= 101;
		var multiLang               	= "";
		var ios 						= null;
		
		const pr_STAT_VALIDATED			= 2;
		const pr_STAT_VALIDATED_HIDDEN	= 3;
		const pr_NUMBER					= 20;
		
		//------------------controllers------------------------------------------------------
		var pr_ctr_Main 				= null;
		var pr_ctr_User 				= null;
		var pr_ctr_UserOfGroup			= null;
		var pr_ctr_ChatWebRTC			= null;
		var pr_ctr_ChatRoomIndexedDB	= null;

		var pr_LST_INPUT_FILE			= [];
		var pr_index_msg_search_min 	=  0;
		var pr_old_msg_search 			= "";

		var pr_TIME_REFRESH				= 10 * 60 * 1000;

		var pr_initialeValues_to_search = {};

		var intervalLstHistory			= null;
		var self 						= null;
		var avatarGr 					= {files: []};
		var camMode 					= "user";

		const initialeValues = {
				chatSimple 		: true,
//				isLoadMore 		: false,
				obj		   		: null,
				currentTyp 		: pr_TYP_CHAT_GROUP,
				lstMsgCurrent 	: [],
				begin			: 0,
				members			: {},
				isOwner			: false,
				isGroupUser		: false,
		}
		
		var pr_sound_chat_off = {
		}
		
		var webcam 		= null;
		
		var gumStream 	= null;				//stream from getUserMedia()
		var rec 		= null;				//Recorder.js object
		var input 		= null; 	
		var pr_interval_recording = null;

		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			pr_ctr_Main 			= App.controller.ChatRoom.Main;
			pr_ctr_Person 			= App.controller.ChatRoom.Person;
			pr_ctr_Doc 		     	= App.controller.ChatRoom.Docs;
			pr_ctr_Post 		    = App.controller.ChatRoom.Post;
			pr_ctr_UserOfGroup 		= App.controller.ChatRoom.UserOfGroup;
			pr_ctr_ChatWebRTC 		= App.controller.ChatRoom.ChatWebRTC;
			// pr_ctr_ChatWebChime 	= App.controller.ChatRoom.ChatWebChime;
			// pr_ctr_ChatWebChime_RTC	= App.controller.ChatRoom.ChatWebChime_RTC;
			pr_ctr_Emojis 		    = App.controller.ChatRoom.Emojis
			pr_ctr_ChatRoomIndexedDB= App.controller.ChatRoom.ChatRoomIndexedDB

			self 					= this;
			
			// do_gl_GoogleAPI_OAuthInit();
		}

		//---------show-----------------------------------------------------------------------------
		this.do_lc_show = function(obj, typChat){      //typChat user or group         
			try{
				ios = !window.MSStream && /iPad|iPhone|iPod/.test(navigator.userAgent);
				if(!obj)	return false;
				webcam = Webcam;
				
				do_lc_init_new_chat(obj, typChat);
				do_lc_build_page();
				do_lc_get_chat_bg();
			}catch(e) {				
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "prj.chat", "ChatRoomChat", "do_lc_show", e.toString()) ;
			}
		};

		const do_lc_init_new_chat = (obj, typChat) => {
			//-----exit old------------------------------------------------------
			if (initialeValues.obj){
				var msg_Grp_End = {name : "MSG_CHAT_GROUP_END", val :{group: obj.id}};
				App.controller.ChatRoom.Socket.can_lc_msg_Out (msg_Grp_End);
			}

			//----add user to grp--------------------------------------------------
			var msg_Grp_Begin = {name : "MSG_CHAT_GROUP_BEGIN", val :{group: obj.id}};
			App.controller.ChatRoom.Socket.can_lc_msg_Out (msg_Grp_Begin);


			//-----------------------------------------------------------------------
			initialeValues.lstMsgCurrent	= [];
			initialeValues.obj				= obj;
//			initialeValues.currentTyp 		= typChat;
			initialeValues.begin 			= 0;
			initialeValues.isGroupUser 		= obj.typ01 === pr_TYP_MSG_PRIVATE;
			pr_initialeValues_to_search		= initialeValues;
			pr_index_msg_search_min			= 0;

			self.do_lc_show_grp_btn_mobile(typChat);
			self.do_lc_bind_btn_mobile();
		}


		this.do_lc_show_grp_btn_mobile = function(typeChat){
			$(".grp_btn_right").show();
			if (typeChat==pr_TYP_CHAT_USER){
				$("#btn_chat_member").hide();
				$("#btn_chat_member_waiting").hide();
				$("#btn_chat_post").hide();
			}else{
				$("#btn_chat_member").show();
				$("#btn_chat_member_waiting").show();
				$("#btn_chat_post").show();
			}
			if($('.grp_btn_right').is(':visible')){
				$(".div_mobile").hide();
				$("#div_chat_main").show();
				$("#btn_chat_msg").addClass("active");
			}
		}
		
		this.do_lc_bind_btn_mobile = function () {		
			$("#btn_chat_msg").off("click").click(() => {
				$(".btn_mobile_custom").removeClass("active");
				$(".div_mobile").hide();
				$("#div_chat_main").show(); 
			});
			
			$("#btn_chat_member").off("click").click(() => {
				$(".btn_mobile_custom").removeClass("active");
				$(".div_mobile").hide();
				$("#div_member_global").show();
				
//				$("#div_member>div").addClass("max-height effect_shadow");
//				$("#div_member_wait>div").addClass("max-height effect_shadow");
			});
			
			$("#btn_chat_file").off("click").click(() => {
				$(".btn_mobile_custom").removeClass("active");
				$(".div_mobile").hide();
				$("#div_files").show();
//				$("#div_chat_info").removeClass("col-sm-12 col-lg-3");
//				$("#div_files>div").addClass("max-height effect_shadow");
			});

			$("#btn_chat_post").off("click").click(() => {
				$(".btn_mobile_custom").removeClass("active");
				$(".div_mobile").hide();
				$("#div_post").show(); 
			});
		}
		
		this.do_lc_push_msg_socket = function(msg){
			if(msg){
				let me_id 			= App.data.user.id;
				if(!initialeValues.obj){
					//khi thêm 1 msg của 1 user vào chat thì remove hết avatar read của user này
					//Ví dụ Trang thi remove icon read của Trang ở trên trước khi add msg của Trang
					$("#div_avatar_" + msg.uId).remove(); 

					do_lc_play_sound_move(msg.entId);
					pr_ctr_Person.do_lc_push_newMSG(msg);
					return;
				}
				let groupId 		= initialeValues.obj.id;

				let isCurrentChat 	= (groupId == msg.entId);
				if(isCurrentChat){
					do_lc_pushTo_zoneChat(msg);
					if(msg.files.length > 0) $('#btn_refresh_doc').trigger("click", [true]); 

					let isCurrentWhoChat = (me_id == msg.uId);
					if(!isCurrentWhoChat){
						//khi thêm 1 msg của 1 user vào chat thì remove hết avatar read của user này
						//Ví dụ Trang thi remove icon read của Trang ở trên trước khi add msg của Trang
						$("#div_avatar_" + msg.uId).remove(); 

						pr_ctr_Person.do_lc_push_newMSG(msg, !isCurrentWhoChat);
						do_lc_play_sound_move(msg.entId);

						
					}

				}else{
					pr_ctr_Person.do_lc_push_newMSG(msg);
					do_lc_play_sound_move(msg.entId);
				}
			}
		}

		var do_lc_play_sound_move = function(entId) {
			let mute = pr_sound_chat_off[entId];
			
			if (!mute) {
				try {
					var x = document.getElementById("audio_new_msg"); 
					x.play();
					x.stop();
				} catch (error) {
				}
			}
		}

		this.do_lc_del_msg_socket = function(grpId, msgId){
			if(grpId && msgId){
				let me_id 			= App.data.user.id;
				if(!initialeValues.obj){
					return;
				}	
				if (grpId != initialeValues.obj.id) return;
				$("#li_msg_item_append_"+msgId).remove();
			}
		}
		
		//------------------------------------------------------------------------------------------------------------------
		this.do_lc_bind_list_online = () => {
			if(initialeValues.currentTyp === pr_TYP_CHAT_GROUP)	return;
			if(!initialeValues.obj)						return;

			const lstOnline = App.controller.DBoard.DBoardMain.pr_LST_USER_ONLINE;
			const isOnline 	= lstOnline.includes(initialeValues.obj.name);

			do_lc_show_chat_header({isOnline})
		}


		const do_lc_pushTo_zoneChat = function(msg){
			if(msg.uId == App.data.user.id){
				msg.forMe 				    = true;
				msg.dataUser 			    = App.data.user;
				let avatar                  = App.data.user.per.files;
				if(avatar) msg.dataUser.avatar 	= App.data.user.per.files[0];

			} else {
				let memSend = initialeValues.members[msg.uIDSend];
				msg.dataUser = {login01: msg.uNameSend, id: msg.uIDSend, avatar : memSend.mem.avatar?memSend.mem.avatar : null};
			}

			App.data.msgIdCurrent 	= msg.id;
			App.data.msgCurrent		= msg;

			initialeValues.lstMsgCurrent.unshift(msg);
			do_lc_show_chatroom_append(msg);
		}


		const do_lc_show_form_chat = function(){
			$("#div_chat")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_CHAT_MAIN	, {}));

			if(initialeValues.currentTyp == pr_TYP_CHAT_USER)	initialeValues.obj.name = initialeValues.obj.login01;
			const isOnline = App.controller.DBoard.DBoardMain.pr_LST_USER_ONLINE.includes(initialeValues.obj.name);
			$("#div_chat_main").show();

			do_lc_show_chat_header({isOnline});
			do_lc_show_chat_footer();
		}

		this.do_lc_show_chat_header_init = () => {
//			const isOnline = App.controller.DBoard.DBoardMain.pr_LST_USER_ONLINE.includes(initialeValues.obj.name);
			do_lc_show_chat_header({});
			
		}
		
		const do_lc_show_chat_header = ({isOnline}) => {
			const isMe = initialeValues.members[App.data.user.id];
			if(isMe && (isMe.typ === pr_member_lev_manager) ){
				initialeValues.isManager = true;
			} else {
				initialeValues.isManager = false;
			}

			let memIds = Object.keys(initialeValues.members);
			let countMan = 0;
			initialeValues.canDelete = false;
			for(let i in memIds){
				let user = initialeValues.members[memIds[i]];
				if(user.typ == pr_member_lev_manager) countMan ++;
			}
			if(countMan < 2) initialeValues.canDelete = true;

			let objChatDefine = {user : initialeValues.obj, typChat : initialeValues.currentTyp};
			if(initialeValues.isGroupUser){
				if(Object.keys(initialeValues.members).length > 0){
					let le = Object.keys(initialeValues.members);
					const userChat = le['length'] > 1? Object.values(initialeValues.members).find(m => m.uId !== App.data.user.id) : initialeValues.members[App.data.user.id];
					if(userChat){
						userChat.name = userChat.mem.login01;
						isOnline = App.controller.DBoard.DBoardMain.pr_LST_USER_ONLINE.includes(userChat.name);
						objChatDefine = {user : userChat, typChat : pr_TYP_CHAT_USER};
					} else{
						objChatDefine = null;
						return;
					}					
				}
			}
			
			//Check call kinesis
			initialeValues.isCallKinesis = false;
			if(initialeValues.obj.val02 && initialeValues.obj.typ01 == pr_TYP_MSG_PUBLIC){
				let val02 =JSON.parse(initialeValues.obj.val02);
				if( val02.typ == TYP_KINESIS_ACTIVE) initialeValues.isCallKinesis = true;
			}
			
			// check master kinesis
			initialeValues.isMasterKinesis = false;
			if(initialeValues.obj.val02 && initialeValues.obj.typ01 == pr_TYP_MSG_PUBLIC){
				let val02 =JSON.parse(initialeValues.obj.val02);
				if( val02.login == App.data.user.login) initialeValues.isMasterKinesis = true;
			}else{
				if(initialeValues.isManager) initialeValues.isMasterKinesis = true;
			}
			
			if(App.data.lstGroupKinesis && App.data.lstGroupKinesis[initialeValues.obj.id] && initialeValues.obj.typ01 == pr_TYP_MSG_PUBLIC){
				initialeValues.isCallKinesis = true;
				
				if(App.data.lstGroupKinesis[initialeValues.obj.id]){
					initialeValues.isMasterKinesis = false;
					
					if(App.data.lstGroupKinesis[initialeValues.obj.id].call && App.data.lstGroupKinesis[initialeValues.obj.id].call == 2) { //call = 2 end call
						initialeValues.isCallKinesis = false;
						if(initialeValues.isManager) initialeValues.isMasterKinesis = true;
					}
				}
			}
			
			$("#div_chat_header").html(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_CHAT_HEADER	, {isMasterKinesis : initialeValues.isMasterKinesis ,isCallKinesis : initialeValues.isCallKinesis, canDelete :initialeValues.canDelete, isManager : initialeValues.isManager, isOnline, ...objChatDefine}));
			
			do_lc_bind_event_chat_header();
		}

		const initValueCall = {
				meetingResponse : null,
				attendeeResponse: null,
				callCreated		: false,
		}
		const do_lc_bind_event_chat_header = () => {
			$("#btn_clear_chat").off("click").on("click", function(){
				do_lc_show_chatroom({user: initialeValues.obj, lstMessage: [], hasMsg: true});
				do_lc_deletet_content_chat();
			});

			$("#btn_view_info").off("click").on("click", function(){
//				$("#div_chat_main, #div_info").toggle();
				do_lc_get_total_msg(initialeValues);
			});

			$("#btn_create_video").off("click").on("click", () => {
				pr_ctr_ChatWebRTC.do_lc_show(initialeValues);
			});
//
			$("#btn_call_video").off("click").on("click", async () => {
				// pr_ctr_ChatWebChime.do_lc_show(initialeValues);

				const lstMem 	= Object.values(App.data.lstGrpMember)	
				if(!lstMem) return;
					
				lstMem.forEach(m => {
					if(!m.uId || m.uId === App.data.user.id) return

					App.controller.ChatRoom.Socket.do_lc_rtc_offer(m.uId);
				})
				
				// pr_ctr_ChatWebChime_RTC.do_lc_show_chat('call');
				// pr_ctr_ChatWebChime_RTC.do_lc_play_sound_call();
            	// pr_ctr_ChatWebRTC.do_lc_show(initialeValues);

				// var msg_Grp_End = {name : "MSG_CHAT_START_CALL", val: {typ: 2, group: 19}};
				// App.controller.ChatRoom.Socket.can_lc_msg_Out (msg_Grp_End);
			});
			
			// $("#btn_call_video").off("click").on("click", () => {
			// 	var gApi =false;
			// 	try{
			// 		gApi = gapi.auth2.getAuthInstance().isSignedIn.get();
			// 	}catch(e){}
				
			// 	if (!gApi)
			// 		window.open ("https://meet.google.com/new", "_blank");
			// 	else{
			// 		var options = {
			// 				summary 	: "Call from " + App.data.user.login + (App.data.user.email?"(" + App.data.user.email + ")":""),
			// 				location 	: '',
			// 				description : '',
			// 		};
			// 		do_gl_GoogleAPI_newMeeting(options, function(event){
			// 			//do_gl_show_Notify_Msg_Info('Event created: ' + event.htmlLink);
			// 			//do_gl_show_Notify_Msg_Info('Event created: ' + event.hangoutLink);
			// 			var img = `<img src="www/js/lib/hnv-emoji/img/blank.gif" class="img" style="display:inline-block;width:25px;height:25px;background:url('www/js/lib/hnv-emoji/img/emoji_spritesheet_2.png') -200px -75px no-repeat;background-size:825px 175px;" alt=":date:">`
			// 			var img = '<i class="bx bx-slideshow"></i>' 
			// 				$('#inp_msg').html(img + '&nbsp;&nbsp;' + event.hangoutLink);
			// 			do_lc_send_msg_chat({files:[]});	
			// 			$('#inp_msg').html('');
						
						
			// 		}) ;
			// 	}
					
			// });

			$("#btn_delete_group").off("click").on("click", () => {
				do_lc_del_group();
			})

			$("#btn_show_files").off("click").on("click", () => {
				pr_ctr_Doc.do_lc_get_files(initialeValues.obj, initialeValues.currentTyp);
			});
			
			$("#btn-sound").off("click").on("click", () => {
				$("#btn-sound").addClass("hide");
				$("#btn-sound-off").removeClass("hide");
				do_lc_turn_off_sound();
			});
			
			$("#btn-sound-off").off("click").on("click", () => {
				$("#btn-sound-off").addClass("hide");
				$("#btn-sound").removeClass("hide");
				do_lc_turn_on_sound();
			});

			$("#btn_out_group").off("click").on("click", () => {
				const isMe = initialeValues.members[App.data.user.id];
				if(isMe){
					if(isMe.typ === pr_member_lev_manager){
						do_lc_owner_out_group();
					} else {
						do_gl_init_msgbox_confirm($.i18n("prj_chat_out_group_msg_confirm"), do_lc_out_group);
					}
				}
			})

			$(".btn-resize-middle").off("click").on("click", function(){
				let $this 		= $(this);
				let child		= $this.find("i");
				let {divtoggle} = $this.data();

				$(divtoggle)	.toggle("hide");
				child			.toggleClass("mdi-window-minimize mdi-window-maximize");
			})	

			$(".btn-resize-col").off("click").on("click", function(){
				let isExistClass = $(".chatroom_middle").hasClass("col-lg-6");
				if(isExistClass){
					let isClassleft = $(".chatroom_left"  ).hasClass("col-lg-3");
					if(isClassleft){
						$(".chatroom_middle").toggleClass('col-lg-6').toggleClass('col-lg-4');
						$(".chatroom_right"  ).toggleClass('col-lg-3').toggleClass('col-lg-5');
					}else{
						$(".chatroom_middle").toggleClass('col-lg-6').toggleClass('col-lg-7');
						$(".chatroom_right"  ).toggleClass('col-lg-4').toggleClass('col-lg-3');
					}
					return;
				}

				isExistClass = $(".chatroom_middle").hasClass("col-lg-4");
				if(isExistClass){
					let isClassleft = $(".chatroom_left"  ).hasClass("col-lg-3");
					if(isClassleft){
						$(".chatroom_middle").toggleClass('col-lg-4').toggleClass('col-lg-6');
						$(".chatroom_right"  ).toggleClass('col-lg-5').toggleClass('col-lg-3');
					}else{
						$(".chatroom_middle").toggleClass('col-lg-4').toggleClass('col-lg-7');
						$(".chatroom_right"  ).toggleClass('col-lg-6').toggleClass('col-lg-3');
					}
					return;
				}

				isExistClass = $(".chatroom_middle").hasClass("col-lg-5");
				if(isExistClass){
					let isClassleft = $(".chatroom_left"  ).hasClass("col-lg-3");
					if(isClassleft){
						$(".chatroom_middle").toggleClass('col-lg-5').toggleClass('col-lg-6');
						$(".chatroom_right"  ).toggleClass('col-lg-4').toggleClass('col-lg-3');
					}else{
						$(".chatroom_middle").toggleClass('col-lg-5').toggleClass('col-lg-7');
						$(".chatroom_right"  ).toggleClass('col-lg-5').toggleClass('col-lg-3');
					}
					return;
				}

				isExistClass = $(".chatroom_middle").hasClass("col-lg-7");
				if(isExistClass){
					let isClassleft = $(".chatroom_left"  ).hasClass("col-lg-3");
					if(isClassleft){
						$(".chatroom_middle").toggleClass('col-lg-7').toggleClass('col-lg-4');
						$(".chatroom_right"  ).toggleClass('col-lg-2').toggleClass('col-lg-5');
					}else{
						$(".chatroom_middle").toggleClass('col-lg-7').toggleClass('col-lg-4');
						$(".chatroom_right"  ).toggleClass('col-lg-3').toggleClass('col-lg-6');
					}
					return;
				}

				isExistClass = $(".chatroom_middle").hasClass("col-lg-3");
				if(isExistClass){
					let isClassleft = $(".chatroom_left"  ).hasClass("col-lg-3");
					if(isClassleft){
						$(".chatroom_middle").toggleClass('col-lg-3').toggleClass('col-lg-6');
						$(".chatroom_right"  ).toggleClass('col-lg-6').toggleClass('col-lg-3');
					}else{
						$(".chatroom_middle").toggleClass('col-lg-3').toggleClass('col-lg-7');
						$(".chatroom_right"  ).toggleClass('col-lg-7').toggleClass('col-lg-3');
					}
					return;
				}
			})	

			var do_search_chat = function (){
				//do_lc_get_total_msg(initialeValues);
				
				const msg 		= $("#inp_search_chat").val() || "";

				var userData	= {user: initialeValues.obj, lstMessage: [...initialeValues.lstMsgCurrent].reverse(), hasMsg: true};
				
				if (!msg || msg.length <= 0) {
					do_lc_show_chatroom(userData, true);
					return;
				} 

				if (msg != pr_old_msg_search && pr_old_msg_search!="") {
					do_lc_show_chatroom(userData, true);
					
					$("#inp_search_chat").addClass("chat-searching");
					$("#btn_cancel_search_chat").removeClass("hide");
					pr_old_msg_search 			= msg;
					pr_index_msg_search_min 	= 0;

					if (!pr_initialeValues_to_search || pr_initialeValues_to_search.lstMsgCurrent.length < initialeValues.lstMsgCurrent.length)
						pr_initialeValues_to_search = initialeValues;
				}
				setTimeout(function(){
					do_lc_chat_search_msg(msg);
				},500);
				
			};

			$("#inp_search_chat").off("keypress").on("keypress", function(e){
				if (e.keyCode == pr_KEY_ENTER){
					e.preventDefault();

					//TODO: test performance remove highlight chat 
					// console.time('executionTime');
					// $(".ul_lst_msg_append").find(".highlight").removeClass("highlight");
					// console.timeEnd('executionTime');

					do_search_chat();
				}
			})

			$("#btn_search_chat").off("click").on("click", function(e){
				do_search_chat();
			})

			$("#btn_cancel_search_chat").off("click").on("click", function(e){
				$("#btn_cancel_search_chat").addClass("hide");
				$("#inp_search_chat").removeClass("chat-searching");
				$("#inp_search_chat").val('');
				$('.chats-text-cont div').remove();
				pr_old_msg_search = "";
				pr_index_msg_search_min 	= 0;
				do_lc_show_chatroom({user: initialeValues.obj, lstMessage: [...initialeValues.lstMsgCurrent].reverse(), hasMsg: true});
			})
			$("#btn_create_stream").off("click").on("click", function(e){
				pr_ctr_ChatWebRTC.do_lc_show(initialeValues);
			});
			$("#btn_refresh_channel").off("click").on("click", function(e){
				pr_ctr_ChatWebRTC.req_participate_channel(initialeValues);
			});
			
		}
		
		const do_lc_deletet_content_chat = () => {
			const {obj} 	= initialeValues;

			const params 	= {entId: obj.id};
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_DEL_LIST, params);	

			const fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_del_list_msg_response, []));

			const fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_del_list_msg_response = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_gl_show_Notify_Msg_Success ($.i18n('common_del_success_msg') );
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}

		/*	const do_lc_get_list_show_after_search (){
			let lstMsgClosest = {};
			lstMsgClosest.lstMessage 	= lstMsgCurrent.slice(begin, end).reverse();
			lstMsgClosest.hasMsg 		= true;
			lstMsgClosest.user 			= initialeValues.obj;
			lstMsgClosest.lstMessage[i].body = `<div class='text-highlight' > ${lstMsgCurrent[i].body} </div>`
		}*/

		
		const do_lc_chat_search_msg = (msg) => {
			let ok = do_lc_chat_search_msg_in_list(pr_initialeValues_to_search.lstMsgCurrent, msg);

			if (!ok && pr_initialeValues_to_search){ 
				var msgTyp		= pr_initialeValues_to_search.currentTyp == pr_TYP_CHAT_USER ? pr_TYP_MSG_PRIVATE : pr_TYP_MSG_PUBLIC;
				var entId 		= pr_initialeValues_to_search.obj.id;
//				pr_initialeValues_to_search.begin += pr_MSG_NUMBER;

				do_lc_get_more_msg_and_search(entId, msgTyp, pr_initialeValues_to_search.lstMsgCurrent.length, msg);
			}
		}
		
		const do_lc_chat_search_msg_in_list = (lstMsgCurrent, msg) => {
			if (!lstMsgCurrent || lstMsgCurrent.length<=0) return false;
			
			var msg_last 		= lstMsgCurrent[lstMsgCurrent.length-1];
			var msg_last_id 	= msg_last.id;
			var $div_msg_last 	= $("#li_msg_item_append_"+ msg_last_id);
			var scrollPosLast 	= $div_msg_last.position().top;
			
			for (let i = pr_index_msg_search_min; i< lstMsgCurrent.length; i++){
				//pureText to remove tab html: img, emoji,....
				var pureText = new DOMParser().parseFromString(lstMsgCurrent[i].inf04, "text/html").documentElement.textContent;
				if (pureText.includes(msg)) {
					pr_index_msg_search_min = i+1;
					
					var msg_id 		= lstMsgCurrent[i].id;
					var $div_msg 	= $("#li_msg_item_append_"+ msg_id);
					
					const 	d 		= new Date();
					let 	id 		= d.getTime();
					$div_msg.highlight(msg, "highlight", id);
					
					//-----scroll to msg
					const $divScroll = $("#ul_lst_msg .simplebar-content-wrapper"); //$("#ul_lst_msg_append");
//					$divScroll.scrollTop(0);
					
					var scrollPos 	= $div_msg.position().top;
					
					var diff 		= Math.abs(scrollPosLast - scrollPos);
					
					$divScroll.scrollTop(diff);
					return true;
					
					
//					$btn_load_more = $("#btn_load_more");
//					var posY =  $divScroll.prop("scrollHeight")/lstMsgCurrent.length * (lstMsgCurrent.length-(i>3?i-3:i));
//					$divScroll.scrollTop(posY);
					
//					$("#li_msg_item_append_25993").offset().top
					
				}
			}
			return false;
		}


		const do_lc_get_more_msg_and_search = function(entId, msgTyp, begin, msg){
			const dateLast 	= pr_initialeValues_to_search.lstMsgCurrent.length ? pr_initialeValues_to_search.lstMsgCurrent[pr_initialeValues_to_search.lstMsgCurrent.length - 1].dt : null;

			const params 	= {entId, msgTyp, begin, nb: pr_MSG_NUMBER, dateLast};
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_LIST, params);	

			const fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_getMsg_response_search, [msg]));

			const fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_getMsg_response_search = function(sharedJson, msg){
			if(can_gl_AjaxSuccess(sharedJson)) {
				if (sharedJson[App['const'].RES_DATA].length == 0) { //search until end
					pr_index_msg_search_min = 0;
					App.MsgboxController.do_lc_show({
						title		: $.i18n("prj_chat_search_title"),
						content 	: $.i18n("prj_chat_no_match_found"),
						autoclose	: true,
						buttons		: {
							OK: {
								lab			: $.i18n("msgbox_OK_title"),
								autoclose	: true,
								classBtn	: "btn-primary"
							},
						}
					});	
					return;
				} 
				
				const {obj} 	= initialeValues;
				let data 		= sharedJson[App['const'].RES_DATA] || [];
				let userData 	= {}, user_me_id = App.data.user.id, user_you_id = obj.id;

				if(data.length){					
					data = do_build_info_and_filter(data);
					data = do_build_avatar_user(data);
					
					initialeValues.lstMsgCurrent 	= [...initialeValues.lstMsgCurrent, ...data];
					userData 	= {user: obj, lstMessage: [...initialeValues.lstMsgCurrent].reverse(), hasMsg: initialeValues.lstMsgCurrent.length ? true: false, isShowLoadMore : true};

					do_lc_show_chatroom(userData, false);
					
					setTimeout(function(){
						do_lc_chat_search_msg(msg);
					},500);
				}
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}

		
		const do_lc_get_total_msg = function(initialeValues){
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET_TOTAL, {id : initialeValues.obj.id});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_after_get_msg_total, [initialeValues]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		const do_lc_after_get_msg_total = function(sharedJson, initialeValues){
			if(can_gl_AjaxSuccess(sharedJson)) {
				initialeValues.obj.countMsg = sharedJson.res_data;
				do_lc_get_total_file(initialeValues);
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}


		const do_lc_get_total_file = function(initialeValues){
			const ref 		= req_gl_Request_Content_Send_With_Params("ServiceTpyDocument", "SVTpyDocumentCountFileChat", {id : initialeValues.obj.id});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_after_get_file_total, [initialeValues]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		const do_lc_after_get_file_total = function(sharedJson, initialeValues){
			if(can_gl_AjaxSuccess(sharedJson)) {
				initialeValues.obj.countFile = sharedJson.res_data;
				do_lc_show_popup_info_group(initialeValues)
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}

		const do_lc_show_popup_info_group = function(initialeValues){
			let obj = initialeValues.obj;
			if(obj.val01 && typeof obj.val01 === 'string'){
				obj.val01 = JSON.parse(obj.val01);
			}
			initialeValues.obj.isManager = initialeValues.isManager;
			if (initialeValues.isManager) {
				App.MsgboxController.do_lc_show({
					title		: $.i18n("prj_chat_group_title"),
					content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_GROUP_INFO_POPUP	, {obj: initialeValues.obj, hasJoin: true}),	
					autoclose	: true,
					buttons		: {
						SEND 	: {
							lab 		: "<i class='mdi mdi-send'></i>",
							funct		: do_lc_update_chat_group,
							autoclose	: true
						},
					},
					onClose		: () => {
						avatarGr		= {files: []};
					},
				});
				if(initialeValues.obj.typ02		) do_gl_select_value($("#chat_group_typ"), initialeValues.obj.typ02);
				if (initialeValues.obj.avatar) avatarGr.files.push(initialeValues.obj.avatar);
				let option	= {
					obj 			: avatarGr,
					fileinput		: {maxFiles : 1, param : {typ01: 1, typ02: 1} },//option here
				}
				do_gl_init_fileDropzone($("#frm_dropzone_send"), option);
			} else {
				App.MsgboxController.do_lc_show({
					title		: $.i18n("prj_chat_group_title"),
					content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_GROUP_INFO_POPUP	, {obj: initialeValues.obj, hasJoin: true}),	
					autoclose	: true,
					buttons		: "none",
				});
			}
		}

		const do_lc_update_chat_group = function() {
			let ent = {
				id: initialeValues.obj.id,
				files: avatarGr.files,
				typ02: parseInt($("#chat_group_typ").val(), 10),
				name: $("#chat_group_name").val()
			}
			if (avatarGr.files.length > 0) {
				ent.val01 = {img: decodeURIComponent(avatarGr.files[0].path01)}; 
			}
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS_GROUP, pr_SV_MOD_GROUP, {obj: JSON.stringify(ent)});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_update_chat_group_success, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}
		const do_lc_update_chat_group_success = function(sharedJson, group){
			if(can_gl_AjaxSuccess(sharedJson)) {
				const data = sharedJson[App['const'].RES_DATA];
				if(data){
					do_gl_show_Notify_Msg_Success 	($.i18n("common_success_update") );
					initialeValues.obj = data;
					$(".chat-item[data-id='"+data.id+"']").find("img").attr("src" , data.avatar.urlPrev);
				}
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}

		const do_lc_owner_out_group = () => {
			const lstOtherMember = Object.values(initialeValues.members).filter(m => m.uId !== App.data.user.id);
			if(!lstOtherMember || !lstOtherMember.length){
				do_lc_del_group();
			} else {
				App.MsgboxController.do_lc_show({
					title		: $.i18n("common_btn_validate"),
					content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_CHAT_MSGBOX_CHOICE_OWNER, {members : lstOtherMember}),	
					autoclose	: false,
					buttons		: {
						OK: {
							lab		: $.i18n("prj_chat_out_group_btn_out_group"),
							funct	: () => {
								const idSel = $(".sel-owner:checked").val();
								idSel && do_lc_out_group(idSel);
							},
							autoclose	: false,
							classBtn	: "btn-primary"
						},
						NO: {
							lab		: $.i18n("prj_chat_out_group_btn_delete_group"),
							funct	: do_lc_del_group,
							classBtn: "btn-danger"
						}
					}
				});	
			}
		}

		const do_lc_out_group = function(idSel){
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS_GROUP, pr_SV_OUT_GROUP, {id : initialeValues.obj.id, idSel});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterOut_group, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		const do_lc_afterOut_group = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {
				$("#div_chat, #div_video_call, #div_member, #div_member_wait, #div_doc, #div_files").html("");
				$(".modal").length && App.MsgboxController.do_lc_close();
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}

		const do_lc_del_group = function(){
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS_GROUP, pr_SV_DEL_GROUP, {id : initialeValues.obj.id});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterDel_group, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		const do_lc_afterDel_group = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {
				$("#div_chat, #div_video_call, #div_member, #div_member_wait, #div_doc").html("");
				pr_ctr_Person.do_lc_get_list_chat();
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}

		const do_lc_show_chat_footer = () => {
			$("#div_chat_footer").html(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_CHAT_FOOTER	, {simpleChat : initialeValues.chatSimple}));
			// pr_ctr_Emojis.do_lc_show();
			do_lc_bind_event_footer();
		}

		const do_lc_send_msg_chat = (lstFile, audio) => {
			const htmlImg   = $("#div_chat_list_img").html().split("chat-insert-img").join("files_content_chat mw-100 min-w-50")
			.split("<i class=\"btn-chat-delete-img fa fa-times\"></i>").join("") || "";

			//----format content msg------
			var   cont		=  initialeValues.chatSimple?   $("#inp_msg").html() || "" :  $("#inp_msg").val() || "";
				
			cont			= cont.trim();
			while (cont.indexOf("<br>") == 0){
				cont = cont.substring(4, cont.length);
			}
			if(cont.length > 3){
				while (cont.lastIndexOf("<br>") == cont.length - 4){
					cont = cont.substring(0, cont.length - 4);
				}
			}
			
			const urlify = function(text) {
		        var urlRegex =/(\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/ig;   
				return text.replace(urlRegex, function(url) {
					return '<a href="' + url + '" target="_blank">' + url + '</a>';
				})
			}
			
			const do_lc_check_url = function(text){
				let href = text.includes("href");
				if(href) return true;
				return false;
			}
			
			let isUrl = do_lc_check_url(cont);
			if(!isUrl) cont = urlify(cont);
			
			//----build msg content
			if (!htmlImg || !htmlImg.length){
				const msg 		= audio? audio: cont;

				if((!msg || !msg.length) && !lstFile.files.length)	return false;
				do_lc_send_msg(msg, lstFile.files);
			} else {

				const msg 		= htmlImg + cont;
				//do_lc_send_msg_with_img(msg, lstFile.files, "<div class=\"div-chat-img-parent\">");
				do_lc_send_msg_with_img(msg, lstFile.files, "</div>");
			}

			/*const htmlImg   = $("#div_chat_list_img").html().replaceAll("chat-insert-img", "chat-display-img")
											.replaceAll("<i class=\"btn-chat-delete-img fa fa-times\"></i>", "") || "";

			const lstImg 	= htmlImg.split("<div class=\"div-chat-img-parent\">");
			if (lstImg.length > 0){
				for(let i=0; i < lstImg.length; i++){
					if (lstImg[i].length > 0){
						do_lc_send_msg(lstImg[i], []);
					} 
				}
			}

			callback(lstFile);*/

		}

		const do_lc_send_msg_callback = (lstFile) => {
			const msg 		= $("#inp_msg").html() || "";

			if((!msg || !msg.length) && !lstFile.files.length)	return false;
			do_lc_send_msg(msg, lstFile.files);
		}

		const do_lc_bind_event_footer = (simpleChat) => {
			!initialeValues.chatSimple && App.SummerNoteController.do_lc_show("#div_chat", {height : 100}, false);//text editor

			const fileData  = { files: [] };
			do_lc_bind_event_fileInput(fileData);

			$("#btn_send_msg").off("click").on("click", function(){
				if (rec && rec.recording){
					stopRecording(true);
					return;
				};

				const lstFile 	= fileData;
				do_lc_send_msg_chat(lstFile);	

				$('#inp_msg').html('');
				$('.chats-text-cont div').remove();	
				//enter function send
			})

			$("#btn_send_like_direct").off("click").on("click", function(){
				const lstFile 	= fileData;
				const msg 		= "&#128077;";
				do_lc_send_msg(msg, lstFile.files);
				//enter function send
			})

			$("#inp_msg").off("keypress").on("keypress", function(e){
				if (e.keyCode == pr_KEY_ENTER && e.shiftKey){
					return;
				}
//				if (e.keyCode == pr_KEY_ENTER_CTRL && e.ctrlKey ){
//				var cont = $('#inp_msg').html() + "<br>";
//				$('#inp_msg').html(cont); 
//				$('#inp_msg').height("100px");

//				var range = document.body.createTextRange();//Create a range (a range is a like the selection but invisible)
//				range.moveToElementText(document.getElementById("inp_msg"));//Select the entire contents of the element with the range
//				range.collapse(false);//collapse the range to the end point. false means collapse to end rather than the start
//				range.select();
//				return;
//				}

				if (e.keyCode == pr_KEY_ENTER){
//					$("#btn_send_msg").click();
//					return;
					e.preventDefault();
					const lstFile 	= fileData;
					do_lc_send_msg_chat(lstFile);

					$('#inp_msg').html('');
//					$('#inp_msg').height("35px");

					$('.chats-text-cont div').remove();	
				} 
			})
			$("#inp_msg").focus();
			
			$("#btn_add_file").off("click").on("click", function() {
				const isHide 		= $("#div_inp_file").hasClass("hide");
				$("#div_inp_file")	.toggleClass("hide");
				if(isHide){
					$("#div_inp_file form")	.click();
				} else {
					pr_LST_INPUT_FILE[0]	.removeAllFiles();
					fileData.files.length 	= 0;
				}
			})

			$("#inp_msg").bind("paste", function(event){
				var items = (event.clipboardData || event.originalEvent.clipboardData).items;
				for (index in items) {
					var item = items[index];
					if (item.kind === 'file') {
						$("#div_msg_with_img")	.removeClass("hide");

						var blob = item.getAsFile();
						do_lc_up_file_inline("#div_chat_list_img", blob);
						/*						
						var reader = new FileReader();
						reader.onload = function(e){
							var image = `<img src='${e.target.result}' class='chat-insert-img'>`;
				            $("#div_chat_list_img").append(image);

							pr_HTML_IMG_INLINE += image.replace("chat-insert-img", "chat-display-img");
							pr_LST_IMG_INLINE.push(blob);
						}
						reader.readAsDataURL(blob);*/
					}
				}
			} );

			$("#btn_trans_simple").off("click").on("click", function() {
				if(initialeValues.chatSimple)	return;
				initialeValues.chatSimple = true;
				do_lc_show_chat_footer();
			})

			$("#btn_trans_summer").off("click").on("click", function() {
				if(!initialeValues.chatSimple)	return;
				initialeValues.chatSimple = false;
				do_lc_show_chat_footer();
				$("#div_table_emoji, #btn_add_file").parent().addClass("d-none");
			})

			$("#btn_add_emoji").on("click", function() {
				$("#div_table_emoji")	.toggleClass("d-none");
			})

			$("#inp_msg").off("input").on("input", function(e) {
				var tmp = $("#inp_msg").find('img');
				if (tmp.length > 0){
					for (let i = 0; i<tmp.length; i++){
						if (!tmp.get(i).src.includes("emoji")){
							$("#inp_msg").find('img').get(i).remove();
						}
					}
				}
			})

			$("#inp_msg").focus(function() {
				do_lc_read_chat();
			});

			$("#btn_capture").off("click").on("click", function() {
				if (!ios) {
					do_lc_show_webcam(() => {
						$("#div_inp_capture")	.toggleClass("hide");
					});
				} else {
					do_lc_show_webcam();
					// // take snapshot and get image data
					webcam.snap( function(data_uri) {
						var file = dataURLtoFile(data_uri, 'photo.jpeg');
						do_lc_up_file_inline("#div_chat_list_img", file);
						$("#div_msg_with_img")	.removeClass("hide");
					});
				}
			})

			$("#switch_cam").off("click").click(() => {
//				if (camMode == "user") {
//					webcam.set('constraints',{
//						facingMode: "environment"
//					});
//					camMode = "environment";
//				} else {
//					webcam.set('constraints',{
//						facingMode: "user"
//					});
//					camMode = "user";
//				}
//				webcam.attach( '#my_camera' );
				
				webcam.reset();
				if (camMode == "user") {
					webcam.set('constraints',{
						facingMode: "environment"
					});
					camMode = "environment";
				} else {
					webcam.set('constraints',{
						facingMode: "user"
					});
					camMode = "user";
				}
				webcam.attach( '#my_camera' );
				
				$("#my_camera").show();
				$("#photo_res").removeAttr("src");
				$("#take_sc").removeClass("icon-disabled");
				$("#trash").addClass("icon-disabled");
				$("#send_sc").addClass("icon-disabled");
			})

			$("#take_sc").off("click").click(()=> {
				// take snapshot and get image data
				webcam.snap( function(data_uri) {
					$("#my_camera").hide();
					// display results in page
					$("#photo_res").show();
					$("#photo_res").attr("src", data_uri);
				});

				$("#take_sc").addClass("icon-disabled");
				$("#trash").removeClass("icon-disabled");
				$("#send_sc").removeClass("icon-disabled");
			});

			$("#trash").off("click").click(()=> {
				$("#photo_res").hide();
				$("#my_camera").show();
				

				$("#take_sc").removeClass("icon-disabled");
				$("#trash").addClass("icon-disabled");
				$("#send_sc").addClass("icon-disabled");
			});

			$("#close_sc").off("click").click(()=> {
				webcam.reset();
				$("#photo_res").hide();
				$("#div_inp_capture")	.toggleClass("hide");
			});

			$("#send_sc").off("click").click(()=> {
				//
				var file = dataURLtoFile($("#photo_res").attr("src"), 'photo.jpeg');
				do_lc_up_file_inline("#div_chat_list_img", file);
				$("#div_msg_with_img")	.removeClass("hide");

				$("#trash" ).trigger("click");

				webcam.reset();
				$("#div_inp_capture")	.toggleClass("hide");
			});
			
			$("#btn_recorder").off("click").on("click", function() {
				startRecording();
			})

			$("#btn_recorder_stop").off("click").on("click", function() {
				stopRecording(false);
			})
		}

		const dataURLtoFile = (dataurl, filename) => {
			var arr = dataurl.split(','),
				mime = arr[0].match(/:(.*?);/)[1],
				bstr = atob(arr[1]), 
				n = bstr.length, 
				u8arr = new Uint8Array(n);
				
			while(n--){
				u8arr[n] = bstr.charCodeAt(n);
			}
			return new File([u8arr], filename, {type:mime});
		}
		
		const do_lc_show_webcam = (callback) => {
			// Configure a few settings and attach camera
			webcam.set({
				width: 320,
				height: 240,
				image_format: 'jpeg',
				jpeg_quality: 90
			});
			webcam.attach( '#my_camera' , callback);
			$("#my_camera").show();
		}
		
		const do_lc_effect_message_read = () => {
			pr_ctr_Person.do_lc_effect_message_read_by_chat_room(initialeValues);
		}

		const do_lc_up_file_inline = function(divListImg, file){
			let ref = new FormData();
			ref.append('sv_class'	, 'ServiceTpyDocument');
			ref.append('sv_name'	, 'SVTpyDocumentNewPublic');
			ref.append('typ01'		, 1);
			ref.append('typ02'		, 1);
			ref.append(`file${0}`, file);

			let fSucces 	= [];
			fSucces.push(req_gl_funct(null, do_lc_after_upload_file_inline, [divListImg]));

			let fError 	= req_gl_funct(null, do_lc_upload_error, [$.i18n("common_err_ajax") ]);
			App.network.do_lc_ajax_form(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		const do_lc_after_upload_file_inline = function(sharedJson, divListImg){
			if (can_gl_AjaxSuccess(sharedJson)) {
				let data = sharedJson[App['const'].RES_DATA];
				if(data && data.length){
					for(let img of data){
						//<div id='div_chat_tpy_doc_inline' class="hide" value='${img.id}'/>
						let rotate = ""; 
						if (ios) rotate = "style='transform: rotate(-90deg);'";
						var image = `<div class='div-chat-img-parent'><img src='${img.path01}' ${rotate} class='chat-insert-img' data-path='${img.path01}'><i class='btn-chat-delete-img fa fa-times'></i></div>`;
						//  $("#div_chat_list_img").append(image);
						$(divListImg).append(image);
					}
					do_lc_bind_event_img_inline();
				}
			} else {
				do_gl_show_Notify_Msg_Error ($.i18n("common_err_ajax"));	
			}
		}

		const do_lc_bind_event_img_inline = function () {
			$(".btn-chat-delete-img").off("click").on("click", function() {
				$(this).closest('.div-chat-img-parent').remove();
				if ($("#div_chat_list_img").html() == "") $("#div_msg_with_img").addClass("hide"); 
			})
		}

		const do_lc_bind_event_fileInput = (fileData) => {
			const options 	= {
					fileinput	: {
						maxFiles : 20,
						param	: {
							parType : 40000,
							parId   : initialeValues.obj.id,
							filenameKept: 1
						}
					},//option here
					obj			: fileData,
			}
			pr_LST_INPUT_FILE = do_gl_init_fileDropzone($("#div_chat"), options);
		}

		const do_lc_build_page = function(){
//			if(initialeValues.currentTyp == pr_TYP_CHAT_USER){
//			do_lc_show_form_chat();
//			do_lc_get_content_chat();
//			} else if(initialeValues.currentTyp == pr_TYP_CHAT_GROUP){
//			}

			do_lc_get_relation_user_group();
		}

		const do_lc_get_chat_bg = async () => {
			if(!pr_ctr_ChatRoomIndexedDB) return
			
			const {obj} 		= initialeValues;
			let oldMessagesCont = await pr_ctr_ChatRoomIndexedDB.do_lc_req_messages(obj.id, obj.ref, "grp") || {}
			
			do_lc_get_chat_bg_by_date(oldMessagesCont)
		}

		const do_lc_get_chat_bg_by_date = (oldMessagesCont) => {
			const {obj} 	= initialeValues;
			const msgTyp 	= initialeValues.currentTyp == pr_TYP_CHAT_USER ? pr_TYP_MSG_PRIVATE : pr_TYP_MSG_PUBLIC;

			const dtFrom 	= oldMessagesCont?.dt || null;

			const params 	= {entId: obj.id, msgTyp, dtFrom};
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, "SVChatLstBg", params);	

			const fSucces	= [];
			fSucces.push(req_gl_funct(null, do_lc_get_chat_bg_by_date_response, [obj.id, obj.ref, oldMessagesCont]));

			const fError 	= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_get_chat_bg_by_date_response = (sharedJson, entId, entCode, oldMessagesCont) => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				try {
					let newMessages = sharedJson[App['const'].RES_DATA]
					let mergeMessages = []

					if(!newMessages || newMessages.length <= 0) return

					if(!oldMessagesCont || !oldMessagesCont.data) mergeMessages = []
					else mergeMessages = oldMessagesCont.data

					if(mergeMessages.length > 0 && oldMessagesCont.dt) {
						const lastDate = new Date(oldMessagesCont.dt)
						lastDate.setDate(lastDate.getDate() - 1);

						mergeMessages.forEach((m, i) => {
							if(new Date(m.dt01) < lastDate) return

							delete mergeMessages[i]
						})
					}
					mergeMessages = [...mergeMessages].filter(e => e != undefined)

					mergeMessages.unshift(...newMessages)
					pr_ctr_ChatRoomIndexedDB.do_lc_update_messages(entId, entCode, "grp", mergeMessages)
				} catch(e) {
				}
			}
		}

		const do_lc_load_more_chat = async () => {
			if(!pr_ctr_ChatRoomIndexedDB) return
			
			const {obj} 			= initialeValues;
			const oldMessagesCont 	= await pr_ctr_ChatRoomIndexedDB.do_lc_req_messages(obj.id, obj.ref, "grp") || {}

			if(oldMessagesCont.data.length <= 0) return;

			let currentMessages 	= initialeValues.lstMsgCurrent
			
			if(currentMessages.length <= 0) {
				currentMessages 	= oldMessagesCont.data.slice(0, 100);
				do_lc_read_chat()
				return
			}

			const oldestMessageDt 	= currentMessages[currentMessages.length - 1].dt01;
			let count = 1;

			for(const m of oldMessagesCont.data) {
				if(new Date(oldestMessageDt) <= new Date(m.dt01)) continue
				currentMessages.push(m)
				count++
				
				if(count >= 100) return
			}
			do_lc_read_chat()
		}

		this. do_lc_get_content_chat_bg = function(doScroll){ //sau khi call api thi se gan lst msg vao initialeValues de show ra
			const {obj} 	= initialeValues;
			const msgTyp 	= initialeValues.currentTyp == pr_TYP_CHAT_USER ? pr_TYP_MSG_PRIVATE : pr_TYP_MSG_PUBLIC;

			const dateLast 	= initialeValues.lstMsgCurrent.length ? initialeValues.lstMsgCurrent[initialeValues.lstMsgCurrent.length - 1].dt : null;

			const params 	= {entId: obj.id, msgTyp, begin : initialeValues.begin, nb: pr_MSG_NUMBER, dateLast};
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_LIST, params);	

			const fSucces		= [];
			fSucces.push(req_gl_funct(null, self.do_lc_getMsg_response_bg, [doScroll, obj.id, obj.ref]));

			const fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		this. do_lc_getMsg_response_bg = function(sharedJson, doScroll){
			if(can_gl_AjaxSuccess(sharedJson)) {
				try {
					let data = localStorage.getItem("chatRoom.chatLst");
					if (data !== JSON.stringify(sharedJson)) {
						initialeValues.lstMsgCurrent = [];
						self.do_lc_getMsg_response(sharedJson, doScroll);
					}
				} catch(e) {
				}
			}
		}

		this. do_lc_get_content_chat = function(doScroll){ //sau khi call api thi se gan lst msg vao initialeValues de show ra
			const {obj} 	= initialeValues;
			const msgTyp 	= initialeValues.currentTyp == pr_TYP_CHAT_USER ? pr_TYP_MSG_PRIVATE : pr_TYP_MSG_PUBLIC;

			const dateLast 	= initialeValues.lstMsgCurrent.length ? initialeValues.lstMsgCurrent[initialeValues.lstMsgCurrent.length - 1].dt : null;

			const params 	= {entId: obj.id, msgTyp, begin : initialeValues.begin, nb: pr_MSG_NUMBER, dateLast};
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_LIST, params);	

			const fSucces		= [];
			fSucces.push(req_gl_funct(null, self.do_lc_getMsg_response, [doScroll]));

			const fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		this. do_lc_getMsg_response = function(sharedJson, doScroll){
			if(can_gl_AjaxSuccess(sharedJson)) {

				const {obj} 	= initialeValues;
				let data 		= sharedJson[App['const'].RES_DATA] || [];
				if (data.length==0){
//					do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
					$("#div_chat_msg")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_CHAT_CONTENT	, {}));
					$("#btn_load_more").hide();
					self.do_lc_init_call_rtc();
					return;
				}
				
//				let isShowLoadMore = data.length == 0 || data.length < pr_MSG_NUMBER? false : true;
				let isShowLoadMore = data.length == 0 ? false : true;
				let userData 	= {}, user_me_id = App.data.user.id, user_you_id = obj.id;

				data = do_build_info_and_filter(data);
				data = do_build_avatar_user(data);

				var div_msg_last 	= null;
				if (initialeValues.lstMsgCurrent && initialeValues.lstMsgCurrent.length>0){
					var msg_last 		= initialeValues.lstMsgCurrent[initialeValues.lstMsgCurrent.length-1];
					var msg_last_id 	= msg_last.id;
					 	div_msg_last 	= ("#li_msg_item_append_"+ msg_last_id);
				}else{
					localStorage.setItem("chatRoom.chatLst",JSON.stringify(sharedJson));
					//--save only the first time
				}
				
				initialeValues.lstMsgCurrent 	= [...initialeValues.lstMsgCurrent, ...data];

				App.data.msgIdLast  			= 0;
				App.data.msgIdCurrent  			= 0;
				if (initialeValues.lstMsgCurrent.length > 0) {
					App.data.msgIdCurrent  		= initialeValues.lstMsgCurrent[0].id;
					App.data.msgCurrent  		= initialeValues.lstMsgCurrent[0];
				}

				userData 	= {user: obj, lstMessage: [...initialeValues.lstMsgCurrent].reverse(), hasMsg: initialeValues.lstMsgCurrent.length ? true: false, isShowLoadMore : isShowLoadMore};

				do_lc_show_chatroom(userData, doScroll, div_msg_last);

				//-----get lst history---------------------------------------------------
				// Get historyLst from local
				let canCallAjax = true;
				try {
					let data = JSON.parse(localStorage.getItem("chatRoom.historyLst"));
					if (data.res_data[0].groupId == initialeValues.obj.id) {
						canCallAjax = false;
						do_lc_lst_history_chat_success(data);

						// Call bg reload new data
						do_lc_lst_history_chat();
					}
				} catch (e) {
				}
				if (canCallAjax) do_lc_lst_history_chat();

				if (intervalLstHistory != null) {
					clearInterval(intervalLstHistory);
				}
				intervalLstHistory = setInterval(() => {
					do_lc_lst_history_chat();
				}, pr_TIME_REFRESH);

			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}
		const do_build_info_and_filter = (data) => {
			var result = [];
			var user_me_id = App.data.user.id;
			data.map(o => {
				if(o.uId == user_me_id)	o.forMe = true;
				o.inf04 = App.network.req_lc_DecodeUTF8 (o.inf04);
				if (o.info){
					try{
						o.info = JSON.parse(o.info);
						if (o.info) {
							o.files = o.info.files;
							o.hide	= o.info.hide;
						}
						
						if (!o.hide) result.push(o);
						else
							if (!o.hide[user_me_id]) result.push(o);
						
					}catch(e){
					}
				}
				
				return o;
			})
			
			return result;
		}
		
		const do_build_avatar_user = (data) => {

			if (!App.data["lstGrpMember"] || App.data["lstGrpMember"] == null) {
				setTimeout (do_build_avatar_user, 500, data);
				return;
			}

			for(let i in data){
				try{
					let userId = data[i].dataUser["id"];

					let user = App.data["lstGrpMember"][userId];				

					if(user && user.mem.avatar){
						data[i].dataUser.avatar = user.mem.avatar;
					}
				}catch(e){}
			}
			return data;
		}

		const do_lc_show_chatroom_append = function(msg){
			$("#ul_lst_msg_append").append(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_CHAT_CONTENT_MSG_ITEM	, msg));
			
			do_lc_bind_event_chat_content();
			do_lc_scrollBottom_toChat();
			
//			initialeValues.isLoadMore = false;
		}
		
		const do_lc_show_chatroom = function(userData, doScroll, divDest){
			$("#div_chat_msg")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_CHAT_CONTENT	, userData));
			
			do_lc_bind_event_chat_content();
			do_lc_read_chat();
			
			if (doScroll == undefined || doScroll == true) 
				setTimeout(function(){
					if (divDest){
						var post = $(divDest).position().top;
						const $divScroll = $("#ul_lst_msg .simplebar-content-wrapper");
						$divScroll.scrollTop(post - 200);
					}else{
						do_lc_scrollBottom_toChat();
					}
				}, 100);
				
//			initialeValues.isLoadMore = false;
			self.do_lc_init_call_rtc();
		}

		this. do_lc_init_call_rtc = () => {
			App.controller.ChatRoom.Socket.do_lc_rtc_init(initialeValues.obj.id);
			pr_ctr_ChatWebRTC.do_lc_show(initialeValues);
			//em init media tiep o day
			let myMedia 	= document.getElementById("video-master");
			let remoteMedia = document.getElementById("tile-user");
			App.controller.ChatRoom.Socket.do_lc_userMedia_init(myMedia, remoteMedia);
		}

		
		function placeCaretAtEnd(el) {
		    el.focus();
		    if (typeof window.getSelection != "undefined"
		            && typeof document.createRange != "undefined") {
		        var range = document.createRange();
		        range.selectNodeContents(el);
		        range.collapse(false);
		        var sel = window.getSelection();
		        sel.removeAllRanges();
		        sel.addRange(range);
		    } else if (typeof document.body.createTextRange != "undefined") {
		        var textRange = document.body.createTextRange();
		        textRange.moveToElementText(el);
		        textRange.collapse(false);
		        textRange.select();
		    }
		}
		
		const do_lc_bind_event_chat_content = () => {

			$(".message-item").off("click").on("click", function() {
				$(this).find(".dropdown").removeClass('hide');
			})
			
			$(".message-item").off("dblclick").on("dblclick", function() {
				var body = $(this).find(".msg-body-other").text();
				if (!body)
					body = $(this).find(".msg-body-forme").text();
				
				if (body){
					copyToClipboard(body.trim());		
					do_gl_show_Notify_Msg_Success ($.i18n('common_copyToClipboard_success') );
				}
			})
		
			
			$(".content-reponse").off('click').on('click', function() {
				let {user, msg} = $(this).data();
				if(user) {
					if(initialeValues.chatSimple) $("#inp_msg").append(`<a href="javascript:void(0)">@${user}</a>:${msg}<br/>`);
					if(!initialeValues.chatSimple) $("#inp_msg").summernote('code', `<span style="color: #556ee6;">@${user}</span>:${msg}<span>&nbsp;</span><br/>`);
					
					var input = document.getElementById('inp_msg');
					placeCaretAtEnd (input);
				}
			})
			
			$(".content-copy").off("click").on("click", function() {
				const {body} = $(this).data();
				body && copyToClipboard(body);
			})

			$(".content-hide").off("click").on("click", function() {
				const {id, dt, grptyp, grpid } = $(this).data();
				$(this).closest(".message-item").remove();
				id && do_lc_hide_msg(id, dt, grptyp, grpid)
			})

			$(".content-delete").off("click").on("click", function() {
				const {id, dt, grptyp, grpid } = $(this).data();
				$(`#li_msg_item_append_${id}`).remove();
				id && do_lc_del_msg(id, dt, grptyp, grpid)
			})

			$("#btn_load_more").off("click").on("click", function() {
				initialeValues.begin += pr_MSG_NUMBER;
				do_lc_load_more_chat();
//				initialeValues.isLoadMore = true;
			})

			$(".files_content_chat").off("click").on("click", function() {
				const {path} = $(this).data();
				let isImage = do_lc_check_image(path);
				if(isImage){
					const viewer = new Viewer(document.getElementById('div_chat_msg'), {
						filterImgClass: ['msg-body-forme', 'msg-body-other'],
						hide: function () {
							viewer.destroy();
						},
					});
				}else{
					window.open(path, "_blank");
				}
			})

//			$(".div-chat-img-parent").off("click").on("click", function() {	
//				let path01 = $(this).find('img').attr('src');
//				let isImage = do_lc_check_image(path01);
//				if(isImage){
//					App.MsgboxController.do_lc_show({
//						content 	: `<img src="${path01}" class="img_group_chat_popup"/>`,
//						autoclose	: true,
//						buttons		: "none",
//					});	
//				}else{
//					window.open(path, "_blank");
//				}
//			})
		}
		
		function do_lc_turn_off_sound () {
			pr_sound_chat_off[initialeValues.obj.id] = true;
		}
		
		function do_lc_turn_on_sound () {
			pr_sound_chat_off[initialeValues.obj.id] = false;
		}

		function do_lc_getExtension_from_name(filename) {
			var parts = filename.split('.');
			return parts[parts.length - 1];
		}


		function do_lc_check_image(filename) {
			var ext = do_lc_getExtension_from_name(filename);
			switch (ext.toLowerCase()) {
			case 'jpg':
			case 'jpeg':
			case 'gif':
			case 'bmp':
			case 'png':
			case 'PNG':
			case 'webp':
				//etc
				return true;
			}
			return false;
		}

		const do_lc_del_msg = function(id, dt, grptyp, grpid){
			//var	  grpTyp	= 
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_DEL, {id, dt, grptyp, grpid});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterDel_msg, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}
		
		const do_lc_hide_msg = function(id, dt, grptyp, grpid){
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_HIDE, {id, dt, grptyp, grpid});	

			let fSucces		= [];
			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		const do_lc_afterDel_msg = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {
				//const a = 3;
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}

		const do_lc_send_msg_with_img = function(msg, files, split){
			var info = [];
			for (var i in files){
				var fi = files[i];
				var fObj = {"fId" : fi.id, "fName": fi.fName, "fUrl" : fi.path01};
				info.push(fObj);
			}			
			var infoF		= '{"files": ' + JSON.stringify(info) + '}';		
			//---------------------------------------------------------------------------

			$("#btn_send_msg").prop('disabled', true);

			const typMsg 	= initialeValues.currentTyp == pr_TYP_CHAT_USER ? pr_TYP_MSG_PRIVATE : pr_TYP_MSG_PUBLIC;
			const cond 		= {msg, typMsg, entId: initialeValues.obj.id, info : infoF, split};

			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_NEW_WITH_IMG, cond);	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterSend_msg, [files]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		const do_lc_send_msg = function(msg, files){
			var info = [];
			for (var i in files){
				var fi = files[i];
				var fObj = {"id" : fi.id, "fName": fi.fName, "fUrl" : fi.path01};
				info.push(fObj);
			}			
			var infoF		= '{"files": ' + JSON.stringify(info) + '}';		
			//---------------------------------------------------------------------------

			$("#btn_send_msg").prop('disabled', true);

			const typMsg 	= initialeValues.currentTyp == pr_TYP_CHAT_USER ? pr_TYP_MSG_PRIVATE : pr_TYP_MSG_PUBLIC;
			const cond 		= {msg, typMsg, entId: initialeValues.obj.id, info : infoF};

			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_NEW, cond);	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterSend_msg, [files]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		const do_lc_afterSend_msg = function(sharedJson, files){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				$("#inp_msg")		.val("").focus();
				!initialeValues.chatSimple && $("#inp_msg").summernote('code', '');

				pr_LST_INPUT_FILE[0].files = pr_LST_INPUT_FILE[0].files.map(f =>{
					f.notDel = true; return f;
				});
				pr_LST_INPUT_FILE[0].removeAllFiles(true);
				files.length 		= 0;
				$("#div_inp_file")	.addClass("hide");
				$("#div_msg_with_img")	.addClass("hide");
				$("#div_chat_list_img").html("");
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}

			$("#btn_send_msg").prop('disabled', false); 
		}

		const do_lc_get_relation_user_group = function(){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_MEMBER_ROLE, {groupId: initialeValues.obj.id});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_getRole_response, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_getRole_response = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {
				const data 		= sharedJson[App['const'].RES_DATA];
				if(data){
					if(data.stat === 1){
						do_lc_show_form_chat();	
						pr_ctr_UserOfGroup.do_lc_show(initialeValues);
						pr_ctr_Doc.do_lc_show(initialeValues.obj);
//						do_lc_get_content_chat();
						pr_ctr_Post.do_lc_show(initialeValues.obj);
					} else {
						do_lc_show_info_group(true);
						$("#div_member, #div_member_wait, #div_member_wait, #div_files, #div_post").html('');
					}
				} else {
					do_lc_show_info_group();
					$("#div_member, #div_member_wait, #div_member_wait, #div_files, #div_post").html('');
				}
			} else {  
				if (initialeValues.obj.typ02 == CHAT_GROUP_PUBLIC) {
					do_lc_send_join_group_public();
				} else {
					do_lc_show_info_group();
				}
				$("#div_member, #div_member_wait, #div_member_wait, #div_files, #div_post").html('');
			}
		}

		const do_lc_show_info_group = function(hasJoin = false){
			$("#div_chat")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_GROUP_INFO	, {obj: initialeValues.obj, hasJoin}));
			$("#div_member, #div_member_wait, #div_files", "#div_post").html("");
			do_lc_bind_event_send_request();
		}

		const do_lc_bind_event_send_request = function(){
			$("#btn_join_group").off("click").on("click", function(){
				do_lc_send_join_group();
			})

			$("#btn_cancel_join").off("click").on("click", function(){
				do_lc_cancel_join_group();
			})

			$("#btn_refresh_join").off("click").on("click", () => do_lc_get_relation_user_group());
		}

		const do_lc_send_join_group_public = function(){
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_MEMBER_JOIN_PUBLIC, {groupId: initialeValues.obj.id});	

			const fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_send_join_group_public_response, []));

			const fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}
		const do_lc_send_join_group_public_response = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_lc_get_relation_user_group();
			} else {   
			}
		}

		const do_lc_send_join_group = function(){
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_MEMBER_JOIN, {groupId: initialeValues.obj.id});	

			const fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_getSend_response, []));

			const fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_getSend_response = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {
				$("#div_chat")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_GROUP_INFO	, {obj : initialeValues.obj, hasJoin: true}));
			} else {   
				$("#div_chat")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_GROUP_INFO	, {obj : initialeValues.obj}));
			}
			do_lc_bind_event_send_request();
		}

		const do_lc_cancel_join_group = function(){
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_MEMBER_CANCEL_JOIN, {groupId: initialeValues.obj.id});	

			const fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_getSendCancel_response, []));

			const fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_getSendCancel_response = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {
				$("#div_chat")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_GROUP_INFO	, {obj : initialeValues.obj}));
				do_lc_bind_event_send_request();
			}
		}

		const do_lc_scrollBottom_toChat = function(){
			setTimeout(() => {
				const $divScroll = $("#ul_lst_msg .simplebar-content-wrapper");
				$divScroll.animate({ scrollTop: $divScroll.prop("scrollHeight")}, 300);
			}, 100);
		}

		const do_lc_read_chat = function(){
			do_lc_effect_message_read();
			if (initialeValues.lstMsgCurrent.length==0) return;

			let lstMsgUserCurrent = null;
			for (let i = 0; i < initialeValues.lstMsgCurrent.length; i++) {
				if (initialeValues.lstMsgCurrent[i].uId == App.data.user.id) {
					lstMsgUserCurrent = initialeValues.lstMsgCurrent[i].id;
					break;
				}
			};
			// condition avoiding send newHistory too much
			if (App.data.msgIdLast == lstMsgUserCurrent) return;

			// find last msg
			for (let i = 0; i < initialeValues.lstMsgCurrent.length; i++) {
				if (initialeValues.lstMsgCurrent[i].uId != App.data.user.id) {
					App.data.msgIdLast = initialeValues.lstMsgCurrent[i].id;
					break;
				}
			};
			let obj = {
					group	: initialeValues.obj.id,
					msg		: App.data.msgIdLast 
			};
			
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_NEW_HISTORY, {obj: JSON.stringify(obj)});	

			const fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_read_chat_success, []));

			const fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_read_chat_success = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {

			}
		}
		//------------------------------------------------------------------------------------------------------------------------

		const do_lc_lst_history_chat = function(){
			if (App.data.msgIdLast == App.data.msgIdCurrent) {
				if (App.data.msgCurrent) 
					if (App.data.msgCurrent.lstHistory) 
						if (App.data.msgCurrent.lstHistory.length>5 || 
							App.data.msgCurrent.lstHistory.length>=App.data.lstGrpMember.length-2) return;
			}

			let obj = {
					group	: initialeValues.obj.id,
			};
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_LST_HISTORY, {obj: JSON.stringify(obj)});	

			const fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_lst_history_chat_success, [App.data.msgIdCurrent]));

			const fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_lst_history_chat_success = function(sharedJson, msgId){
			if(can_gl_AjaxSuccess(sharedJson)) {
				// Save to local
				localStorage.setItem("chatRoom.historyLst",JSON.stringify(sharedJson));

				let lst 		= sharedJson[App['const'].RES_DATA] || [];
				if (lst.length>0) do_lc_filter_user_history(lst);

				App.data.msgIdLast  			= msgId;
			}
		}

		const do_lc_filter_user_history = function (lst) {
			let lstHistoryFilter = [];
			let lstMems = Object.assign({}, initialeValues.members);
			if (lstMems) {
				for (const key in lstMems) {
					lstMems[key] = 0;
				}
			}
			// find lstHistoryFilter (lọc và chỉ giữ lại mỗi user 1 history)
			if (lst && lst.length > 0) {
				lst.forEach((e) => {
					if (lstMems[e.uId] == 0) {
						lstHistoryFilter.push(e);
						lstMems[e.uId] = 1;
					}
				})
			}

			// mapping to lstMsgCurrent
			if (lstHistoryFilter  && lstHistoryFilter.length > 0) {
				initialeValues.lstMsgCurrent.forEach((e) => {
					e.lstHistory = [];
					lstHistoryFilter.forEach((e2) => {
						if (e.id == e2.msg && e2.uId!=e.uId && e2.uId!=App.data.user.id) {
							let tmp = {
									avatar 	: initialeValues.members[e2.uId].mem,
									dt  	: e2.dt,
							}
							e.lstHistory.push(tmp);
						}
					})
					if (e.lstHistory.length == 0) e.hasHistory = false;
					else e.hasHistory = true;
				});

				userData 	= {user: initialeValues.obj, lstMessage: [...initialeValues.lstMsgCurrent].reverse(), hasMsg: initialeValues.lstMsgCurrent.length ? true: false};
				//do_lc_show_chatroom(userData);
				do_lc_show_chatroom_read(initialeValues.lstMsgCurrent);
			}
		}
		
		const do_lc_show_chatroom_read = function(lst){
			let lst_msg_read = $(".message-read");
			
			if (lst_msg_read.length > 0 ){
				for (let i = 0; i<lst_msg_read.length; i++){
					lst_msg_read[i].remove();
				}
			}
			
			let lstHasHistory = lst.filter(function(e) { return e.hasHistory; }); 
			
			if (lstHasHistory.length > 0 ){
				lstHasHistory.forEach((e) => {
					$("#li_msg_item_append_" + e.id).append(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_CHAT_CONTENT_MSG_READ	, e));
				})
			}
		}
		
		this.do_lc_new_chatroom_user_read = function(userid, msgid){
			if (App.data.user.id == userid) return;
			
			$("#div_avatar_" + userid).remove(); 

			let obj = Object.assign({}, initialeValues.members[userid]);
			obj.msgId = msgid;
			
			$("#li_msg_item_append_" + msgid).show();
			
			if ($("#li_msg_read_" + msgid).length > 0) {
				$("#li_msg_read_" + msgid).append(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_CHAT_CONTENT_MSG_READ_ITEM_CONTENT, obj))
			} else {
				$("#li_msg_item_append_" + msgid).append(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_CHAT_CONTENT_MSG_READ_ITEM	, obj))
			}
/*			$("#li_msg_item_append_" + msgid).append
			$("#li_msg_read_" + msgid).show();
			$("#li_msg_read_" + msgid).append(tmplCtrl.req_lc_compile_tmpl(tmplName.CHATROOM_TAB_CHAT_CONTENT_MSG_READ_ITEM	, initialeValues.members[userid].mem))
*/		
			do_lc_scrollBottom_toChat();	
		}
		
		const startRecording = function (){
		    var constraints = { audio: true, video:false }
		    
			navigator.mediaDevices.getUserMedia(constraints).then(function(stream) {
				var audioContext = new window.AudioContext;
				gumStream = stream;
				input = audioContext.createMediaStreamSource(stream);
				rec = new Recorder(input,{numChannels:1})
				rec.record();
				
				$("#btn_recorder i").addClass("text-danger");
				$("#btn_recorder_stop").show();
				$("#btn_recorder").hide();
				$(".btn-not-recording").addClass('hide_recording');
				$(".sound-wave").removeClass("hide_recording");

				
//				$(".chat-input-links").addClass('hide');
//				$("#recording").removeClass("hide");
//				updateDateTime();

			}).catch(function(err) {
				console.log(err);
			});
		}
		
		var updateDateTime = function() {
			var sec = null;
			sec = rec.recordingTime() | 0;
			$("#time-display").html("" + (minSecStr(sec / 60 | 0)) + ":" + (minSecStr(sec % 60)));
			pr_interval_recording = setInterval(updateDateTime, 200);
		};

		 var minSecStr = function(n) {
			 return (n < 10 ? "0" : "") + n;
		 }
		
		function stopRecording(save) {
			rec.stop();
			gumStream.getAudioTracks()[0].stop();
			
			$("#btn_recorder i").removeClass("text-danger");
			$("#btn_recorder_stop").hide();
			$("#btn_recorder").show();
			
			$(".btn-not-recording").removeClass('hide_recording');
			$(".sound-wave").addClass("hide_recording");

//			clearInterval(pr_interval_recording);
//			$(".chat-input-links").removeClass('hide');
//			$("#recording").addClass("hide");
			
			if (save) rec.exportWAV(createDownloadLink);
		}

		function blobToFile(theBlob, fileName){       
		    return new File([theBlob], fileName, { lastModified: new Date().getTime(), type: theBlob.type })
		}
		
		function createDownloadLink(blob) {
			URL = window.URL || window.webkitURL;
			
			var url = URL.createObjectURL(blob);
			var filename = new Date().getTime();
			
			var file = blobToFile(blob, filename + ".wav");
			do_lc_up_file_audio_inline(file);
		}
		
		const do_lc_up_file_audio_inline = function(file){
			let ref = new FormData();
			ref.append('sv_class'	, 'ServiceTpyDocument');
			ref.append('sv_name'	, 'SVTpyDocumentNewPublic');
			ref.append('typ01'		, 1);
			ref.append('typ02'		, 2);
			ref.append('file', file);
			

			let fSucces 	= [];
			fSucces.push(req_gl_funct(null, do_lc_after_upload_file_audio, []));

			let fError 	= req_gl_funct(null, do_lc_upload_error, [$.i18n("common_err_ajax") ]);
			App.network.do_lc_ajax_form(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}
		
		const do_lc_after_upload_file_audio = function(sharedJson){
			if (can_gl_AjaxSuccess(sharedJson)) {
				let data = sharedJson[App['const'].RES_DATA];
				if(data && data.length){
					for(let item of data){
						var audio = `<audio class="audio_recorder" controls src='${item.path01}'></audio>`;
					}
				}
				do_lc_send_msg_chat({ files: [] }, audio);
			} else {
				do_gl_show_Notify_Msg_Error ($.i18n("common_err_ajax"));	
			}
		}

		

	};

	return ChatRoomChat;
});