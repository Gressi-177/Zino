define([
        'jquery',
        'text!group/job/report/tmpl/JobReport_Ent.html'
        ],
        function($,
        		JobReport_Ent) {


	var JobReportEnt     = function (grpName,header,content,footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplContr				= App.template.controller;
		
		var svClass 				= App['const'].SV_CLASS;
		var svName					= App['const'].SV_NAME;
		var sessId					= App['const'].SESS_ID;
		var userId          		= App['const'].USER_ID;

		var fVar					= App['const'].FUNCT_SCOPE;
		var fName					= App['const'].FUNCT_NAME;
		var fParam					= App['const'].FUNCT_PARAM;		

		var self 					= this;		
		//------------------------------------------------------------------------------------
		var pr_OBJ_TYPE				= 2002;
		
		var pr_SERVICE_CLASS		= "ServiceJobReport"; //to change by your need
				
		var pr_SV_GET				= "SVGet"; 
		var pr_SV_NEW				= "SVNew"; 
		var pr_SV_DEL				= "SVDel"; 
		
		var pr_SV_MOD				= "SVMod"; 	//if not use lock
				
		var pr_SV_LCK_NEW			= "SVLckReq"; 
		var pr_SV_LCK_END			= "SVLckEnd"; 
		var pr_SV_LCK_DEL			= "SVLckDel";
		
		var pr_SV_WD				= "SVWorkingDate";
		//------------------controllers------------------------------------------------------
		var pr_ctr_Main 			= null;
		var pr_ctr_List 			= null;
		var pr_ctr_Ent				= null;
		var pr_ctr_EntHeader 		= null;
		var pr_ctr_EntBtn 			= null;
		var pr_ctr_EntTabs 			= null;
		var pr_ctr_DateTime			= null;
		var pr_ctr_Rights			= null;
		//-----------------------------------------------------------------------------------
		var pr_object				= null;
		var pr_mode					= null;
		var pr_lock					= null;
		
		var lc_rp_stat_draft		= 0;
		var lc_rp_stat_pending		= 1;
		var lc_rp_stat_validated	= 2;
		var lc_rp_stat_denied		= 3;
		var username				= App.data.user.per;
		if(!username.name03){
			username.name03 = "";
		}
		var lc_username 			= username.name01 + " " + username.name02 + " " + username.name03;
		
		var currentRpCreated		= 1;
		
		//	RIGHT ACTION----------------------------------------
		var RIGHT_U_G		= 0;
		var RIGHT_U_N		= 1;
		var RIGHT_U_M		= 2;
		var RIGHT_U_D		= 3;
		
		var RIGHT_A_G		= 4;
		var RIGHT_A_N		= 5;
		var RIGHT_A_M		= 6;
		var RIGHT_A_D		= 7;
		var RIGHT_A_WD		= 8;
		
		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			pr_ctr_Main 			= App.controller.JobReport.Main;
			pr_ctr_List 			= App.controller.JobReport.List;
			
			pr_ctr_Ent				= App.controller.JobReport.Ent;
			pr_ctr_EntHeader 		= App.controller.JobReport.EntHeader;
			pr_ctr_EntBtn 			= App.controller.JobReport.EntBtn;
			pr_ctr_EntTabs 			= App.controller.JobReport.EntTabs;
			pr_ctr_DateTime			= App.controller.JobReport.DateTime;
			pr_ctr_Rights			= App.controller.JobReport.Rights;
			
			if(!tmplName) {
				App.template.names[pr_grpName] = {}
				tmplName = App.template.names[pr_grpName]
			}
			
			tmplContr.do_lc_put_tmpl(tmplName.JOB_REPORT_ENT	, JobReport_Ent);
		}
		
		//---------------------------------------------------------------------------------------------
		//SHOW REPORT-----SHOW REPORT-----SHOW REPORT-----SHOW REPORT-----SHOW REPORT-----SHOW REPORT--
		//---------------------------------------------------------------------------------------------
		this.do_lc_show		= function(obj, mode){			
			pr_object 	= obj;
			pr_mode		= mode;		
			
			if (!obj || pr_mode == pr_ctr_Main.var_lc_MODE_INIT){
				$(pr_divContent).html(tmplContr.req_lc_compile_tmpl(tmplName.JOB_REPORT_ENT, {mode : pr_ctr_Main.var_lc_MODE_INIT}));
				obj = {};
				obj.uId03 = App.data.user.id;
				pr_ctr_EntBtn	.do_lc_show(obj, pr_ctr_Main.var_lc_MODE_INIT);
				return;
			}
			
			if (obj){
				do_lc_handle_rp_resume_backToList (obj);

				$(pr_divContent).html(tmplContr.req_lc_compile_tmpl(tmplName.JOB_REPORT_ENT, obj));				
				pr_ctr_EntBtn		.do_lc_show(obj, mode);
				pr_ctr_EntHeader	.do_lc_show(obj, mode);
				pr_ctr_EntTabs		.do_lc_show(obj, mode);
			}
			
			pr_ctr_Main.do_lc_binding_pages($(pr_divContent), {
				obj: obj
			});
			
			if(mode == pr_ctr_Main.var_lc_MODE_NEW || mode == pr_ctr_Main.var_lc_MODE_MOD) {
				do_gl_enable_edit($(pr_divContent), ".canEnabled", mode);
			} else {
				do_gl_disable_edit($(pr_divContent), ".canEnabled", mode);
			}
			
			var rightCode = pr_ctr_Rights.req_lc_Right(RIGHT_A_M);
			if(obj.stat == lc_rp_stat_pending && rightCode != -1){
				$(".rp_validated_zone").show();
				$("#inp_cmt").prop("disabled", false);
			}
		}
		
		function do_show_Obj(sharedJson, mode, localObj){
			var code = sharedJson[App['const'].SV_CODE];
			if(code == App['const'].SV_CODE_API_YES) {
				if (localObj){
					self.do_lc_show(localObj, mode); 
				} else{					
					var object = sharedJson[App['const'].RES_DATA];        		
					self.do_lc_show(object, mode);  
				}			     		
        	} else {
                do_gl_show_Notify_Msg_Error("JobReport: Ent Get Obj > SV CODE :" + code);
            }
		}
			
		// this.do_lc_show_ById = function(obj, mode){
		// 	for(var i = 0; i < App.data.lstUser.length; i++){
		// 		var user = App.data.lstUser[i];
		// 		if(user.id == obj.uId03){
		// 			App.data.selectedUser = user;
		// 		}
		// 	}
		// 	pr_ctr_DateTime.getInfoDayOff(obj.code01, obj.uId03);
		// 	self.do_lc_show(obj, mode);
		// }

		this.do_lc_show_ById = function(obj, mode){
			//--------------------------------------------------------
			for(var i = 0; i < App.data.lstUser.length; i++){
				var user = App.data.lstUser[i];
				if(user.id == obj.uId02){
					App.data.selectedUser = user;
				}
			}
			pr_ctr_DateTime.getInfoDayOff(obj.code01, obj.uId02);
			//--------------------------------------------------------
			
			var svName 		= pr_SV_GET;
			if (pr_mode == pr_ctr_Main.var_lc_MODE_MOD){
				//not use lock
				svName = pr_SV_GET
				//use lock
				//svName = pr_SV_LCK_NEW
			}

			var ref 		= req_gl_Request_Content_Send(pr_SERVICE_CLASS, pr_SV_GET);			
			ref.id			= obj.id;

			var fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_show_Obj, [mode]));	

			var fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error [$.i18n("common_err_ajax"), 0]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;			
		}
		
		//---------------------------------------------------------------------------------------------
		//DISPLAY USER SUMMARY-----DISPLAY USER SUMMARY-----DISPLAY USER SUMMARY-----DISPLAY USER SUMMA 
		//---------------------------------------------------------------------------------------------
		this.do_lc_show_user_summary = function(objUser){	
			$(pr_divContent).html(tmplContr.req_lc_compile_tmpl(tmplName.JOB_REPORT_ENT,  {mode : pr_ctr_Main.var_lc_MODE_SEL}));
			pr_ctr_EntBtn		.do_lc_show(objUser, pr_ctr_Main.var_lc_MODE_INIT);
			pr_ctr_EntHeader	.do_lc_show_user_summary(objUser);
		}
		
		//---------------------------------------------------------------------------------------------
		//USER WORKING DATE-----USER WORKING DATE-----USER WORKING DATE-----USER WORKING DATE-----USER 
		//---------------------------------------------------------------------------------------------
		
		const do_lc_req_WH = (cf, attr) => {
			if(cf[attr] == "A") return "########"

			let start = "0000", end = "0000"
			if(cf[attr + "S"]) start = cf[attr + "S"]
			if(cf[attr + "E"]) end = cf[attr + "E"]

			return start + end;
		}

		this.do_lc_update_user_wd = function(objUser, createRp){
			var wd 				= req_gl_data({
				dataZoneDom		: $("#div_wd_user")
			});
		
			if(wd.hasError) {
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_entity_save'));				
			} else {
				var cf = wd.data;
				var stringWd = cf.sun + cf.mon + cf.tue + cf.wed + cf.thu + cf.fri + cf.sat;
				
				var whSun = do_lc_req_WH(cf, 'sun')
				var whMon = do_lc_req_WH(cf, 'mon')
				var whTue = do_lc_req_WH(cf, 'tue')
				var whWed = do_lc_req_WH(cf, 'wed')
				var whThu = do_lc_req_WH(cf, 'thu')
				var whFri = do_lc_req_WH(cf, 'fri')
				var whSat = do_lc_req_WH(cf, 'sat')
		
				var stringWh = whSun + whMon + whTue + whWed + whThu + whFri + whSat;
				
				stringWh = stringWh.replace(/:/g, "");
				do_send_workingDate(stringWd, stringWh, objUser, createRp);
			}
		}
		
		function do_send_workingDate(stringWd, stringWh, objUser, createRp){
			var ref		= {};
			ref 		= req_gl_Request_Content_Send(pr_SERVICE_CLASS, pr_SV_WD);
			ref["uId03"]= objUser.id;
			ref["wD"]	= stringWd;
			ref["wH"]	= stringWh;
			
			var fSucces		= [];
			fSucces.push	( req_gl_funct(this, do_update_workingDate, [true, createRp] ));
			var fError 		= req_gl_funct(this, do_update_workingDate, [false]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError);
		}
		
		var do_update_workingDate = function(sharedJson, ajaxStat, createRp){
			if(ajaxStat){
				var code = sharedJson[App['const'].SV_CODE];
				if(code == App['const'].SV_CODE_API_YES) {
					var u = sharedJson.res_data;
					if(!u.name03) u.name03 = "";
					App.data.selectedUser = u;
					for(var i = 0; i < App.data.lstUser.length; i++){
						if(App.data.lstUser[i].id == u.id){
							App.data.lstUser[i]   = u;
							break;
						}
					}
					if(App.data.monthDataInfo){
						for(var i = 0; i < App.data.monthDataInfo.length; i++){
							var record = App.data.monthDataInfo[i];
							if(App.data.currentReportCode == record.codeReport && u.id == record.uId03){
								App.data.monthDataInfo.splice(i, 1);
								break;
							}
						}
					}
					do_gl_show_Notify_Msg_Success($.i18n("job_report_success_update_wd") + u.name01 + " " 
							+ u.name02 + " " + u.name03);
					
					if(createRp){
						self.do_lc_new();
					} else {
						pr_ctr_EntHeader.do_bind_event_user_summary();
					}
				} else {
					do_gl_show_Notify_Msg_Error($.i18n("Error update_working_date: SV Code:" + code));
				}
			} else {
				do_gl_show_Notify_Msg_Error($.i18n("common_err_ajax"));
			}
		}
		
		//---------------------------------------------------------------------------------------------
		//ADD NEW REPORT-----ADD NEW REPORT-----ADD NEW REPORT-----ADD NEW REPORT-----ADD NEW REPORT---
		//---------------------------------------------------------------------------------------------
		this.do_lc_new = function() {
			var u = App.data.selectedUser;
			if(!u.name03) u.name03 = "";
			var newObj		 = {};
			newObj.uId01     = App.data.user.id;
			newObj.uId01_name=lc_username;
			newObj.uId03     = u.id;
			newObj.uId03_name= u.name01 + " " + u.name02 + " " + u.name03 ;
			newObj.code01	 = App.data.currentReportCode;
			newObj.dt01		 = new Date();
			newObj.stat      = lc_rp_stat_draft;
			newObj.hldrate   = App.data.hldRate;
			newObj.val02	 = 0;
			newObj.val03	 = 0;
			newObj.val04     = App.data.hldRate;
			pr_ctr_DateTime.getInfoDayOff(newObj.code01, newObj.uId03);
			self		.do_lc_show(newObj, pr_ctr_Main.var_lc_MODE_NEW);
		}
		
		//---------------------------------------------------------------------------------------------
		//SAVE REPORT-----SAVE REPORT-----SAVE REPORT-----SAVE REPORT-----SAVE REPORT-----SAVE REPORT--
		//---------------------------------------------------------------------------------------------
		
		this.do_lc_save		= function(obj, mode, rpStat, directSave){	//save new object or save with lock	
			do_gl_req_tab_active($(pr_divContent));
				
			if (!obj.files) obj.files = [];
			var cra 				= req_gl_data({
					dataZoneDom		: $(pr_divContent),
					oldObject 		: {"files": obj.files},
					removeDeleted	: true				
			});
			
			//check data error
			if(cra.hasError) {
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_entity_save'));				
			} else {
				var nbRow = cra.data.lstRp.length;
				//Remove unnecessary data of cra
				if(typeof cra.data['undefined'] !== 'undefined') delete cra.data['undefined']
				if(nbRow > 0){
					for(var i = 0; i < nbRow; i++){
						cra.data.lstRp[i].row_action = null;
						cra.data.lstRp[i].catId01_Select = null;
					}
				}

				cra.data.stat = rpStat;
				if (rpStat == lc_rp_stat_denied)  {
					App.MsgboxController.do_lc_show({
						title	: $.i18n("job_rp_msgbox_confirm_deny_title"),
						content : $.i18n("job_rp_msgbox_confirm_deny_message"),
						buttons	: {
							OK: {
								lab		: 	$.i18n("job_rp_msgbox_confirm_deny_btn1"),
								funct	: 	function(){
												do_send_mod_direct(cra);
											}						
							},
							CANCEL: {
								lab		:  	$.i18n("job_rp_msgbox_confirm_deny_btn2")
							}
						}
					});
					return;
				}
				
				if(rpStat == lc_rp_stat_validated){
					do_send_mod_direct(cra);
					return;
				}
				
				if (rpStat == lc_rp_stat_pending && directSave){
					App.MsgboxController.do_lc_show({
						title	: $.i18n("job_rp_msgbox_confirm_submit_title"),
						content : $.i18n("job_rp_msgbox_confirm_submit_message"),
						buttons	: {						
							CANCEL: {
								lab		:  	$.i18n("job_rp_msgbox_confirm_submit_btn2")
							},
							OK: {
								lab		: 	$.i18n("job_rp_msgbox_confirm_submit_btn1"),
								funct	: 	function(){
									do_send_mod_direct(cra);
								}							
							}
						}
					});
					return;
				}
				
				if(rpStat == lc_rp_stat_pending){
					do_lc_handle_rp_resume(cra)

					App.MsgboxController.do_lc_show({
						title	: $.i18n("job_rp_msgbox_confirm_submit_title"),
						content : $.i18n("job_rp_msgbox_confirm_submit_message"),
						buttons	: {						
							CANCEL: {
								lab		:  	$.i18n("job_rp_msgbox_confirm_submit_btn2")
							},
							OK: {
								lab		: 	$.i18n("job_rp_msgbox_confirm_submit_btn1"),
								funct	: 	function(){
											if(mode==pr_ctr_Main.var_lc_MODE_MOD || mode==pr_ctr_Main.var_lc_MODE_SEL){
												do_send_mod_exit(cra)
											} else if(mode==pr_ctr_Main.var_lc_MODE_NEW){
												do_send_new_exit(cra);
											}
								}							
							}
						}
					});
					return;
				}
				
				if(rpStat == lc_rp_stat_draft){
					do_lc_handle_rp_resume(cra)

					App.MsgboxController.do_lc_show({
						title	: $.i18n("msgbox_confirm_title"),
						content : $.i18n("common_msg_mod_confirm"),
						buttons	: {
							SAVE_EXIT: {
								lab		: 	$.i18n("common_btn_save_exit"),
								funct	: 	function(){
												if(mode==pr_ctr_Main.var_lc_MODE_MOD){
													do_send_mod_exit(cra)
												} else if(mode==pr_ctr_Main.var_lc_MODE_NEW){
													do_send_new_exit(cra);
												}
											}							
								},
							SAVE_CONTINUE: {
								lab		:  	$.i18n("common_btn_save_continue"),
								funct	: 	function(){
												if(mode==pr_ctr_Main.var_lc_MODE_MOD){
													do_send_mod_continue(cra)
												} else if(mode==pr_ctr_Main.var_lc_MODE_NEW){
													do_send_new_continue(cra);	
												}
											}
							}
							}
					});
				}
			}	
		}

		//---------------------------------------------------------------------------------------------
		//	HANDLE REPORT RESUME
		//---------------------------------------------------------------------------------------------
		var do_lc_handle_rp_resume = function(cra) {
			let lstRpResume 	= cra.data?.lstRpResume || cra.lstRpResume;
			if (!lstRpResume) return;

			let objRpResume 	= {};
			let newLstResume 	= [];
			// filter
			lstRpResume.forEach((e) => {
				let attr 	= "t";
				let i 		= e.day;
				if (i < 10) attr = attr + "0" + i;
				else attr = attr + i;
				objRpResume[attr] = e.cmt;
			})

			newLstResume.push(objRpResume);
			if(cra.data) cra.data.lstRpResume = newLstResume;
			else cra.lstRpResume = newLstResume
		}

		var do_lc_handle_rp_resume_backToList = function(cra) {
			let lstRpResume 	= cra.lstRpResume;
			if (!lstRpResume) return; 
			if (lstRpResume.length ==0) return;

			let objRpResume 	= lstRpResume[0];
			let newLstResume 	= [];
			// init obj
			for (let i = 1; i <= 31; i++) {
				let attr = "t";
				if (i < 10) attr = attr + "0" + i;
				else attr = attr + i;

				if (objRpResume[attr])
					newLstResume.push({"day":i, "cmt": objRpResume[attr]});
			}

			cra.lstRpResume = newLstResume;
		}
		
		//---------------------------------------------------------------------------------------------
		//VALIDATE / REJECT / SUBMIT REPORT IN SELECT MODE-----VALIDATE / REJECT / SUBMIT REPORT IN SEL
		//---------------------------------------------------------------------------------------------
		
		var do_send_mod_direct = function(cra){
			var ref 		= req_gl_Request_Content_Send(pr_SERVICE_CLASS, 'SVModDirect');
			ref['lock_id'] 	= pr_lock.id;
			ref['reportId'] = cra.data.id;
			ref['stat'] 	= cra.data.stat;
			ref['cmt'] 		= cra.data.cmt;
			
			var fSucces			= [];
			fSucces.push(req_gl_funct(null	, do_show_msg			, ["OK", cra.data.stat]));
			fSucces.push(req_gl_funct(null	, do_show_Obj			, [pr_ctr_Main.var_lc_MODE_SEL]));
			fSucces.push(req_gl_funct(null	, do_refresh_list		, []));
			
			var fError 			= req_gl_funct(null, do_show_msg, ["KO"]);
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError);
		}
		
		//---------------------------------------------------------------------------------------------
		//SEND NEW-----SEND NEW-----SEND NEW-----SEND NEW-----SEND NEW-----SEND NEW-----SEND NEW-----SE
		//---------------------------------------------------------------------------------------------
		
		var do_send_new_continue = function(cra) {
			var ref		= {};
			ref 		= req_gl_Request_Content_Send(pr_SERVICE_CLASS, pr_SV_NEW);
			ref["lock"]	= 1;
			ref["obj"]	= JSON.stringify(cra.data);
			
			var fSucces			= [];
			fSucces.push(req_gl_funct(null	, do_show_msg			, ["OK", cra.data.stat]));
			fSucces.push(req_gl_funct(null	, do_show_Obj			, [pr_ctr_Main.var_lc_MODE_MOD]));
			fSucces.push(req_gl_funct(null	, do_refresh_list		, []));
			fSucces.push(req_gl_funct(null	, do_begin_lock_new		, []));
			fSucces.push(req_gl_funct(null	, do_mark_cra_created	, [cra.data.uId03]));
		
			var fError 			= req_gl_funct(null, do_show_msg, ["KO"]);
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError) ;	
		}
		
		var do_send_new_exit = function(cra) {
			var ref		= {};
			ref 		= req_gl_Request_Content_Send(pr_SERVICE_CLASS, pr_SV_NEW);
			ref["lock"]	= 2;
			ref["obj"]	= JSON.stringify(cra.data);
			
			var fSucces			= [];
			fSucces.push(req_gl_funct(null	, do_show_msg			, ["OK", cra.data.stat]));
			fSucces.push(req_gl_funct(null	, do_show_Obj			, [pr_ctr_Main.var_lc_MODE_SEL]));
			fSucces.push(req_gl_funct(null	, do_refresh_list		, []));
			fSucces.push(req_gl_funct(null	, do_mark_cra_created	, [cra.data.uId03]));
		
			var fError 			= req_gl_funct(null, do_show_msg, ["KO"]);		
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError) ;	
		}
		
		var do_mark_cra_created = function(sharedJson, uId03){
			App.data.selectedUser.hasRpCreated = currentRpCreated;
			for(var i = 0; i < App.data.lstUser.length; i++){
				if(App.data.lstUser[i].id == uId03){
					App.data.lstUser[i]   = App.data.selectedUser;
					break;
				}
			}
		}
		
		//---------------------------------------------------------------------------------------------
		//SEND MOD-----SEND MOD-----SEND MOD-----SEND MOD-----SEND MOD-----SEND MOD-----SEND MOD-----SE
		//---------------------------------------------------------------------------------------------
		
		var do_send_mod_continue = function(cra) {
			var ref 	= {};
			ref 		= req_gl_Request_Content_Send(pr_SERVICE_CLASS, pr_SV_MOD);
			ref['obj']	= JSON.stringify(cra.data)
			
		
			var fSucces			= [];
			fSucces.push(req_gl_funct(null	, do_show_msg			, ["OK", cra.data.stat]));
			fSucces.push(req_gl_funct(null	, do_show_Obj			, [pr_ctr_Main.var_lc_MODE_MOD]));
			
			var fError 			= req_gl_funct(null, do_show_msg, ["KO"]);
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError) ;
		}

		var do_send_mod_exit = function(cra) {
			var ref 			= {};
			ref 				= req_gl_Request_Content_Send(pr_SERVICE_CLASS, pr_SV_LCK_END);
			ref['lock_id'] 		= pr_lock.id;
			ref['obj']			= JSON.stringify(cra.data)
			
			var fSucces			= [];
			fSucces.push(req_gl_funct(null	, do_show_msg			, ["OK", cra.data.stat]));
			fSucces.push(req_gl_funct(null	, do_show_Obj			, [pr_ctr_Main.var_lc_MODE_SEL]));
			fSucces.push(req_gl_funct(null	, do_refresh_list		, []));
			
			var fError 			= req_gl_funct(null, do_show_msg, ["KO"]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError) ;	
		}
				
		//---------------------------------------------------------------------------------------------
		//LOCK-----LOCK-----LOCK-----LOCK-----LOCK-----LOCK-----LOCK-----LOCK-----LOCK-----LOCK-----LOC 
		//---------------------------------------------------------------------------------------------
		
		this.do_lc_Lock_Begin = function (obj, directSave, stat){			
			var ref 			= req_gl_Request_Content_Send(pr_SERVICE_CLASS, pr_SV_LCK_NEW);
			var lock 			= {};			
			lock.objectType 	= pr_OBJ_TYPE; 	//integer
			lock.objectKey 		= obj.id; 		//integer
			ref['req_data'	]	= JSON.stringify(lock); 
			
			var fSucces			= [];
			fSucces.push(req_gl_funct(null, do_begin_lock, [obj, directSave, stat]));
			
			var fError 			= req_gl_funct(null, do_show_msg, ["KO"]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError) ;	
		}	
		
		this.do_lc_Lock_Cancel = function (obj){
			var ref 			= req_gl_Request_Content_Send(pr_SERVICE_CLASS, pr_SV_LCK_DEL);		
			ref['lock_id'	]	= pr_lock.id; 
			
			var fSucces			= [];
			var fSucces			= [];
			fSucces.push(req_gl_funct(null, do_del_lock		, [obj]									));	
			fSucces.push(req_gl_funct(null, do_show_Obj		, [pr_ctr_Main.var_lc_MODE_SEL, obj]	));	
			
			var fError 			= req_gl_funct(null, do_show_msg, ["KO"]);
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError) ;	
		}
			
		function do_begin_lock(sharedJson, obj, directSave, stat){
			//to comeback on tab curent active
		    do_gl_req_tab_active($(pr_divContent));
		    		    
			if(sharedJson[App['const'].SV_CODE] == App['const'].SV_CODE_API_YES) {					
				pr_lock = sharedJson[App['const'].RES_DATA];
				if(directSave == false){
					App.data.mode 	= pr_ctr_Main.var_lc_MODE_MOD;
					pr_ctr_EntBtn.do_lc_show(obj, App.data.mode);
					self.do_lc_show			(obj, App.data.mode);
				} else {
					pr_ctr_Ent.do_lc_save(obj, pr_ctr_Main.var_lc_MODE_SEL, stat, directSave);
				}
			} else if(sharedJson[App['const'].SV_CODE] == App['const'].SV_CODE_API_NO) {
				pr_lock 	= null;
				var uName 	= sharedJson[App['const'].RES_DATA].username;
        		do_gl_show_Notify_Msg_Error ($.i18n('lock_err_begin') + uName);
			}else{
        		pr_lock = null;
        		do_gl_show_Notify_Msg_Error ($.i18n('lock_err_inconnu'));
        		//notify something if the lock is taken by other person
        		//show lock.information
        	}		
		}
		
		function do_begin_lock_new(sharedJson, obj){    		    
			if(sharedJson[App['const'].SV_CODE] == App['const'].SV_CODE_API_YES) {					
				pr_lock 		= sharedJson[App['const'].RES_LOCK];   
				App.data.mode 	= pr_ctr_Main.var_lc_MODE_MOD;					
			}
		}
		
		function do_del_lock(sharedJson, obj){
			if(sharedJson[App['const'].SV_CODE] == App['const'].SV_CODE_API_YES) {					
				pr_lock = null;				
			} else {   
				pr_lock = null;				
				do_gl_show_Notify_Msg_Error ($.i18n('lock_err_inconnu') );
			}
			App.data.mode 	= pr_ctr_Main.var_lc_MODE_SEL;
			pr_ctr_EntBtn.do_lc_show(obj, App.data.mode);
			self.do_lc_show			(obj, App.data.mode);
		}
		
		this.can_lc_have_lock = function (){
			if (this.pr_lock!=null)
				App.MsgboxController.do_lc_show({
					title	: $.i18n('lock_err_title') ,
					content	: $.i18n('lock_err_msg')
				});	
			return this.pr_lock!=null;
		}
		
		//---------------------------------------------------------------------------------------------
		//DELETE REPORT-----DELETE REPORT-----DELETE REPORT-----DELETE REPORT-----DELETE REPORT-----DEL
		//---------------------------------------------------------------------------------------------
		this.do_lc_delete 	= function (obj){
			var ref 		= req_gl_Request_Content_Send(pr_SERVICE_CLASS, pr_SV_DEL);	
			ref.id          = obj.id;
			
			var lock 		= {};			
			lock.objectType = pr_OBJ_TYPE; 	//integer
			lock.objectKey 	= obj.id; 		//integer
			ref['lock'	]	= JSON.stringify(lock);
			
			var fSucces			= [];
			fSucces.push(req_gl_funct(null	, do_show_msg			, ["OK", "DEL"]));
			fSucces.push(req_gl_funct(null	, do_show_Obj			, [pr_ctr_Main.var_lc_MODE_INIT]));	
			fSucces.push(req_gl_funct(null	, do_refresh_list		, []							)); //refresh menu
			
			var fError 			= req_gl_funct(null, do_show_msg, ["KO"]);
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError) ;			
		}
		
		//---------------------------------------------------------------------------------------------
		//FILES-----FILES-----FILES-----FILES-----FILES-----FILES-----FILES-----FILES-----FILES-----FIL
		//---------------------------------------------------------------------------------------------
		
		this.do_lc_gen_pdf 	= function(obj){			
			var ref 		= {};		
			ref 			= req_gl_Request_Content_Send("SVSysReportGen", "ServiceSysReport");	
			
			$.extend(true, ref, result.data);
			ref['reportId']	= -1;
			ref['params']	= JSON.stringify({"objId" : obj.id});
			ref['toPrint']	= false;

			var fSucces		= [];			
			fSucces.push(req_gl_funct(null	, do_show_File, []));				

			var fError 			= req_gl_funct(null, do_show_msg, ["KO"]);
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Aut_Header, ref, 100000, fSucces, fError) ;
		}
		
		var do_show_File = function(sharedJSon){
			if(sharedJSon[App['const'].SV_CODE] == App['const'].SV_CODE_API_YES) {				
				var data 	= sharedJSon.res_data;
				var url 	= App.path.BASE_URL_API_PRIV + "?" +	data.path01;
				window.open(url, "_blank");
			} 
		}
		
		//---------------------------------------------------------------------------------------------
		//OTHER FUNCTIONS-----OTHER FUNCTIONS-----OTHER FUNCTIONS-----OTHER FUNCTIONS-----OTHER FUNCTIO
		//---------------------------------------------------------------------------------------------
		this.do_lc_duplicate = function (obj){
			var newObj 	= $.extend(true,{},obj);
			newObj.id	= null;
		}
		
		function do_refresh_list (sharedJSon) {
			if(sharedJSon[App['const'].SV_CODE] == App['const'].SV_CODE_API_YES) {
				pr_ctr_List.do_lc_show();
			}
		}
		
		//---------------------------------------------------------------------------------------------
		//DISPLAY NOTIFICATIONS-----DISPLAY NOTIFICATIONS-----DISPLAY NOTIFICATIONS-----DISPLAY NOTIFIC
		//---------------------------------------------------------------------------------------------
		function do_show_msg(sharedJson, ajaxOk, craStat){
			var stringBase = "job_rp_message_";
			var stringMsg  = stringBase + ajaxOk + "_" + craStat.toString();
			if(ajaxOk == "OK"){
				if (sharedJson.sv_code == App['const'].SV_CODE_OK ){
					do_gl_show_Notify_Msg_Success 	($.i18n(stringMsg));
				} else{
					do_gl_show_Notify_Msg_Error 	($.i18n(stringBase + "KO") + sharedJson.sv_code);
				}
			}
			else{
				do_gl_show_Notify_Msg_Error("Ajax was not sent successfully.");
			}
		}

		this.do_lc_duplicate = function (obj){
			var newObj 	= $.extend(true, {}, obj);
			newObj.id	= null;
			App.data.mode 	= pr_ctr_Main.var_lc_MODE_NEW;
			self		.do_lc_show(newObj, App.data.mode);
		}
	}

	return JobReportEnt;
});