define([
	'text!group/home/tmpl/Main_Header.html',
	],
	function(PrjProject_Header) {

	var PrjProjectHeader     = function (grpName, header,content,footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		var self 					= this;
		//------------------controllers------------------------------------------------------
		var pr_ctr_Main 			= null;
		var pr_ctr_Notify			= null;
		
		const pr_SERVICE_CLASS		= "ServicePrjProject"; //to change by your need
		const pr_SV_COUNT_NOTIF		= "SVCountNotification"; 
		const pr_SV_LIST_NOTIF		= "SVListNotification"; 
		
		const pr_TIME_REFRESH_NOTIFY		= 12*60*1000;
		
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			pr_ctr_Main 					= App.controller.UI.Main;
			pr_ctr_Notify					= App.controller.UI.Notify;
			
			if(!tmplName) {
				App.template.names[pr_grpName] = {}
				tmplName = App.template.names[pr_grpName]
			}
			tmplName.VI_HEADER				= "VI_Header";
			tmplCtrl.do_lc_put_tmpl(tmplName.VI_HEADER, PrjProject_Header); 
		}      

		this.do_lc_show		= function(){
			try{
				do_lc_build_page();
				do_bind_event();
			}catch(e) {				
				console.log(e);
			}
		};


		var do_lc_build_page = function(){
			let user 			= App.data.user;
			$("#page-topbar")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.VI_HEADER, user));
			
			do_lc_get_count_new_notification();
			do_lc_setTime_refresh_notify();
		} 
		//---------private-----------------------------------------------------------------------------
		var do_bind_event = function (){
			$(".a-languge").off("click").on("click", function(){
				let {lan: language} 	= $(this).data();
				let languageId 			= 1;
				
				if (!language) {
	    			if (localStorage && localStorage.language) {
	    				language 	= localStorage.language;
	    				languageId	= localStorage.languageId;
	    			} else {
	    				language 	= 'vn';
	    			}
	    		}
	    		
	    		$.i18n({locale: language});
	    		localStorage.language 	= language;
	    		localStorage.languageId = languageId;
	    		window.location.reload();
			})
			
			$("#a_disconnect").off("click").on("click", function(){
				if (FIRST_VIEW){
					App.router.controller.do_lc_run(App.router.routes.LOGOUT_PRJ+'/'+FIRST_VIEW);
				}else{
					App.router.controller.do_lc_run(App.router.routes.LOGOUT_PRJ);
				}
				App.controller.ChatRoom.Socket.do_gl_closeSocket();
			})
		};
		
		var do_lc_get_count_new_notification = function(){
			let ref 		= req_gl_Request_Content_Send(pr_SERVICE_CLASS, pr_SV_COUNT_NOTIF);	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterGet_Count, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}
		
		var do_lc_afterGet_Count = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {
				let data 	= sharedJson[App['const'].RES_DATA];
				data && $("#sp_nbNew_notify").html(data);
			}
			do_lc_bind_event_notify();
		}
		
		var do_lc_bind_event_notify = function(){
			$("#btn_notify_dropdown").off("click").on("click", function(){
				pr_ctr_Notify.do_lc_show();
			})
		}
		
		var do_lc_setTime_refresh_notify = function(){
			setInterval(() => {
				do_lc_get_count_new_notification();
			}, pr_TIME_REFRESH_NOTIFY);
		}
	};
	
	return PrjProjectHeader;
});