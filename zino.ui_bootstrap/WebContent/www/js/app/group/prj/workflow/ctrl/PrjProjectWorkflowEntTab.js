define([
	'text!group/prj/workflow/tmpl/PrjWorkflow_Ent_Content.html',
	'text!group/prj/workflow/tmpl/PrjWorkflow_Ent_Tab_Comment.html',
	'text!group/prj/workflow/tmpl/PrjWorkflow_Ent_Tab_Member.html',
	'text!group/prj/workflow/tmpl/PrjWorkflow_Ent_Tab_Member_Group.html',
	'text!group/prj/workflow/tmpl/PrjWorkflow_Ent_Tab_Member_Group_Popup.html',
	'text!group/prj/workflow/tmpl/PrjWorkflow_Ent_Tab_Docs.html',
	'text!group/prj/workflow/tmpl/PrjWorkflow_Popup_Pick_User.html',
	],
	function(	
			PrjWorkflow_Ent_Content,
			PrjWorkflow_Ent_Tab_Comment,
			PrjWorkflow_Ent_Tab_Member,
			PrjWorkflow_Ent_Tab_Member_Group,
			PrjWorkflow_Ent_Tab_Member_Group_Popup,
			PrjWorkflow_Ent_Tab_Docs,
			PrjWorkflow_Popup_Pick_User
	){

	const tmplName				= App.template.names;
	const tmplCtrl				= App.template.controller;
	const pr_ENTITY_TYPE		= 20000;
	const pr_SERVICE_CLASS		= "ServicePrjProject"; //to change by your need
	const pr_SV_GET				= "SVGet"; 
	const pr_SV_GET_LST         = "SVLst"
	const pr_SV_GET_MEMBER		= "SVGetMember"; 
	const pr_SV_SAVE_MEMBER		= "SVSaveMember"; 
	const pr_SV_SAVE_CONTENT	= "SVSaveContent"; 
	const pr_SV_NEW				= "SVNew"; 
	const pr_SV_SAVE_FILES		= "SVFileSave"; 
	const pr_SV_GET_COMMENTS	= "SVGetComments";
	const pr_SV_NEW_COMMENTS	= "SVNewComments";

	const pr_SV_REFRESH_EPIC	= "SVEpicRefresh";
	const pr_SV_REFRESH_TASK	= "SVTaskRefresh";
	const pr_SV_REFRESH_CONTENT	= "SVContentRefresh";
	const pr_SV_REFRESH_TASK_SPRINT = "SVSprintRefresh";


	const pr_SV_DEL				= "SVDel";
	const pr_SV_DEL_WF			= "SVPrjWorkflowDel";
	const pr_SV_GET_PARENT		= "SVGetParent";
	const pr_SV_MOVE_TASK		= "SVEpicAddTask";

	const pr_SERVICE_PER_CLASS	= "ServicePersonDyn";
	const pr_SV_USER_SEARCH		= "SVUserLstSearchWithAvatar";
	const pr_SV_PERSON_SEARCH	= "SVPersonLstSearchWithAvatar";

	const pr_SERVICE_GROUP_CLASS= "ServiceNsoGroupChat";
	const pr_SV_GROUP_SEARCH	= "SVNsoGroupLstSearch";
	
	const pr_SERVICE_DYN_CLASS		= "ServicePrjProjectDyn";
//	const pr_SV_GET_HISTORY_TASK	= "SVLstHistoryTask";
	
	const pr_SV_GET_HISTORY_TASK	= "SVGetHistoryTask";
	
	const pr_SV_SAVE_MEMBER_GROUP	= "SVSaveMemberGroup"; 
	const pr_SV_GET_MEMBER_GROUP	= "SVGetMemberGroup"; 
	
	const pr_SV_CALCUL_PERCENT_SPRINT  =  "SVCalculPercentSprint";

	//------------------const object------------------------------------------------------

	const pr_TYPE02_PRJ				= 0;
	const pr_TYPE02_EPIC			= 1;
	const pr_TYPE02_TASK			= 2;

	const STAT_PRJ_NEW 							= 0;
	const STAT_PRJ_TODO 						= 1;
	const STAT_PRJ_INPROGRESS 					= 2;
	const STAT_PRJ_REVIEW 						= 3;
	const STAT_PRJ_DONE 						= 4;
	const STAT_PRJ_CLOSED 						= 5;
	const STAT_PRJ_FAIL 						= 6;
	const STAT_PRJ_UNRESOLVED 					= 7; 

	const pr_STAT_PRJ_NEW 			= 100100;
	const pr_STAT_PRJ_TODO 			= 100200;
	const pr_STAT_PRJ_INPROGRESS 	= 100300;
	const pr_STAT_PRJ_DONE 			= 100400;
	const pr_STAT_PRJ_TEST 			= 100500;
	const pr_STAT_PRJ_REVIEW 		= 100600;
	const pr_STAT_PRJ_FAIL 			= 100700;
	const pr_STAT_PRJ_UNRESOLVED 	= 100800;
	const pr_STAT_PRJ_CLOSED 		= 100900;

	const pr_ctr_Main 				= App.controller.UI.Main;

	
	tmplName.PRJ_PROJECT_ENT_CONTENT					= "PrjWorkflow_Ent_Content";
	tmplName.PRJ_PROJECT_ENT_TAB_COMMENT				= "PrjWorkflow_Ent_Tab_Comment";
	
	tmplName.PRJ_PROJECT_ENT_TAB_MEMBER					= "PrjWorkflow_Ent_Tab_Member";
	tmplName.PRJ_PROJECT_ENT_TAB_MEMBER_GROUP			= "PrjWorkflow_Ent_Tab_Member_Group";
	tmplName.PRJ_PROJECT_ENT_TAB_MEMBER_GROUP_POPUP		= "PrjWorkflow_Ent_Tab_Member_Group_Popup";
	
	tmplName.PRJ_WORKFLOW_POPUP_PICK_USER    			= "PrjWorkflow_Popup_Pick_User";
	
	tmplName.PRJ_PROJECT_ENT_TAB_DOCS					= "PrjWorkflow_Ent_Tab_Docs";
	

	var do_lc_bind_event_resize = function(){
		$(".btn-resize").off("click").on("click", function(){
			let $this 		= $(this);
			let {divtoogle} = $this.data();
			let child 		= $this.find("i");
			let label 		= $this.find(".label-resize");
			child			.toggleClass("mdi-window-minimize mdi-window-maximize")
			$(divtoogle)	.toggle("hide");

			label.html(child.hasClass("mdi-window-minimize") ? $.i18n("prj_project_resize_min") : $.i18n("prj_project_resize_max"));
		})
	}

	//------------------------------Start Member list-----------------------------------
	var PrjProjectEntTabMember 	= function (grpName, header, content, footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		
		//------------------------------Start list member-----------------------------------
		const PRJ_MEMBER_LEVEL 		= {0: "prj_project_member_level_manager", 1: "prj_project_member_level_reporter", 2: "prj_project_member_level_worker", 3: "prj_project_member_level_watcher"};
		const PRJ_MEMBER_TYPE 		= {0: "prj_project_lev_bas"				, 1: "prj_project_lev_haute"};
		
		var pr_MEM_TEMP					= {};
		var members 					= {};
		const pr_member_lev_manager 	= 0;
		const pr_member_lev_reporter 	= 1;
		const pr_member_lev_worker 		= 2;
		const pr_member_typ_low 		= 0;
		const pr_member_typ_high 		= 1;
		
		const pr_ENT_TYP_USER           = 1000;
		
		const pr_ctr_Ent				= App.controller.PrjWorkflow.Ent;

		var do_lc_load_view = function(){
			members[App.data.user.id] = {"id": App.data.user.id, "lev": pr_member_lev_manager, "typ": pr_member_typ_high};
			tmplCtrl				.do_lc_put_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_MEMBER			, PrjWorkflow_Ent_Tab_Member);
		}

		this.do_lc_get_list_member = function(prj){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET_MEMBER, {id: prj.id});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_show_list_member, [prj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_show_list_member = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {
				let data 		= sharedJson[App['const'].RES_DATA];

				const is_Me 		= data.find(m => m.entId02 == App.data.user.id);
				const isSuperAdmin 	= App.controller.UI.Login && App.controller.UI.Login.can_lc_User_SuperAdmin();
				const isOwner		= App.data.user.id === prj.autUser01;
				
				let objData 	= data.reduce((currentObj, mem)=>{
					if(mem.entId02 == prj.autUser01)	mem.isOwner = true;
					
					if(!isSuperAdmin && !isOwner){
						if(is_Me && is_Me.typ <= mem.typ && is_Me.lev >= mem.lev)	mem.notModif = true;
					}
					
					currentObj[mem.entId02] = mem;
					return currentObj;
				}, {});

				do_lc_show_prj_member(objData, prj.id);
				pr_ctr_Ent.do_lc_reqRole_User_Workflow();
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}

		var do_lc_bind_event_members_prj = function(members, idPrj){
			pr_MEM_TEMP = $.extend(false, {}, members);

			$("#btn_add_member").off("click").on("click", function(){
				$(".action-item-member").removeClass("hide");
				$(this).addClass("hide");
			})

			$("#a_btn_save_member").off("click").on("click", function(){
				do_lc_save_member_prj(members, idPrj);
			})

			$("#a_btn_cancel_member").off("click").on("click", function(){
				do_lc_show_prj_member(members, idPrj);
			})

			$(".member-edit").off("click").on("click", function(){
				let $this 			= $(this);
				let {memid} 		= $this.data();
				let mem 			= pr_MEM_TEMP[memid];
				if(mem){
					let parentTR 	= $this.closest("tr");
					parentTR.find(".content-member").addClass("hide");
					parentTR.find(".edit-member").removeClass("hide");
					let divLev 		= parentTR.find(".level-edit");
					let divTyp 		= parentTR.find(".typ-edit");
					do_lc_bindEvent_tabMemberEdit(memid, divLev, divTyp);
					$(".action-mem").removeClass("hide");
				}
			})

			$(".member-delete").off("click").on("click", function(){
				let {memid} = $(this).data();
				let mem 	= pr_MEM_TEMP[memid];
				if(mem){
					delete pr_MEM_TEMP[memid];
					$(this).closest("tr").remove();
					$(".action-mem").removeClass("hide");
				}
			})

			let el = "#inp_name_member";
			let reqSelectMember = function(event, item){
				if(pr_MEM_TEMP[item.id])			return false;

				let lev 		= $("#sel_member_level").val();
				let typ 		= $("#sel_member_type").val();
				let mem 		= {"lev" : lev, "typ": typ, "ent02": item, "entId02": item.id, "entId01": idPrj, "entTyp01": pr_ENT_TYP_USER};
				let strlogin 	= item.login.length > 4?item.login.substr(0, 4) + "..." : item.login;
				
				let textColor   = null;
				let textAvatar  = null
				if(!item.avatar){
					let first = item.login.charAt(0);
					let last  = item.login.charAt(item.login.length - 1);
					let index = var_gl_alphabet.indexOf(first.toLowerCase());
					
					textColor = var_gl_colors[index];
					textAvatar= first + last;
				}

				pr_MEM_TEMP[item.id] = mem;
				let selOpt 		= `<tr>`;
				selOpt 			+= `<td><a data-id='${item.id}' class='text-danger btn-remove-member' data-toggle='tooltip' data-placement='top' title='' data-original-title='Delete'><i class='mdi mdi-close font-size-18'></i></a></td>`;
				
				if(item.avatar) selOpt 			+= `<td style='width: 50px;'><img src='${item.avatar.urlPrev}' class='rounded-circle avatar-xs' alt=''/></td>`;
				else 			selOpt 			+= `<td style='width: 50px;'> <div class="rounded-circle avatar-xs text-white text-uppercase text-center" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div></td>`;
				selOpt 			+= `<td><h5 class='font-size-14 m-0'><a href='' class='text-dark'>${strlogin}</a></h5></td>`;
				selOpt 			+= `<td>` + $.i18n(PRJ_MEMBER_LEVEL[+lev]) 	+`</td>`;
				selOpt 			+= `<td  class='hide'>` + $.i18n(PRJ_MEMBER_TYPE[+typ])	+`</td>`;
				selOpt 			+= `</tr>`;

				$("#tabMember table tbody").append(selOpt);
				do_lc_bind_event_autocomplete(pr_MEM_TEMP);
				$(el).blur().val("");
			}

			let options = {
					dataService : [pr_SERVICE_PER_CLASS, pr_SV_USER_SEARCH], fSelect: reqSelectMember, customShowList: do_lc_customLst_user_autocomplete
			}
			do_gl_req_autocompleteNew(el, options);
		}

		var do_lc_bind_event_autocomplete = function(pr_MEM_TEMP){
			$(".btn-remove-member").off("click").on("click", function(){
				let $this 		= $(this);
				let parentTR 	= $this.closest("tr");
				let {id} 		= $this.data();

				if(pr_MEM_TEMP[id])	delete pr_MEM_TEMP[id];
				parentTR.remove();
			})
		}
		
		var do_lc_save_member_prj = function(members, idPrj){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_SAVE_MEMBER, {idPrj, members: JSON.stringify(Object.values(pr_MEM_TEMP))});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterSave_member, [members, idPrj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_afterSave_member = function(sharedJson, members, idPrj){
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_gl_show_Notify_Msg_Success 	($.i18n('common_success_update'));
				do_lc_show_prj_member(pr_MEM_TEMP, idPrj);
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}

		var do_lc_bindEvent_tabMemberEdit = function(memid, divLev, divTyp){
			$(divLev).off("change").on("change", function(){
				pr_MEM_TEMP[memid].lev = $(this).val();
			})

			$(divTyp).off("change").on("change", function(){
				pr_MEM_TEMP[memid].typ = $(this).val();;
			})
		}

		var do_lc_customLst_user_autocomplete = function(item, selOpt = ""){
			if(!item.avatar){
				let first = item.login.charAt(0);
				let last  = item.login.charAt(item.login.length - 1);
				let index = var_gl_alphabet.indexOf(first.toLowerCase());
				
				let textColor = var_gl_colors[index];
				let textAvatar= first + last;
				
				selOpt 		+= `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-white text-uppercase text-center mr-2" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div> ${item.login}</div>`;
			}else{
				selOpt 		+= `<div class="media align-items-center"><img src='${item.avatar.urlPrev}' class='rounded-circle avatar-xs mr-2'/> ${item.login}</div>`;
			}
			return selOpt;
		}
		
		var do_lc_show_prj_member = function(members, idPrj){
			do_lc_load_view();
			$("#div_prj_member")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_MEMBER			, members));
			do_lc_bind_event_members_prj(members, idPrj);
			do_lc_bind_event_resize();
		}
		//------------------------------End list member-----------------------------------
	}
	
	//------------------------------Start Member list-----------------------------------
	var PrjProjectEntTabMemberGroup 	= function (grpName, header, content, footer) {
		//------------------------------Start list member-----------------------------------
		const PRJ_MEMBER_LEVEL 		= {0: "prj_project_member_level_manager", 1: "prj_project_member_level_reporter", 2: "prj_project_member_level_worker"};
		const PRJ_GROUP_TYPE 		= {0: "prj_project_lev_bas"				, 1: "prj_project_lev_haute"};
		
		var pr_GROUP_TEMP		     	= {};
		var pr_MEM_TEMP		        	= {};
		var allMembers 					= [];
		const pr_member_lev_manager 	= 0;
		const pr_member_lev_reporter 	= 1;
		const pr_member_lev_worker 		= 2;
		const pr_member_typ_low 		= 0;
		const pr_member_typ_high 		= 1;
		
		var self                        = this;
		const pr_ctr_Ent				= App.controller.UI.Ent;

		var do_lc_load_view = function(){
			tmplCtrl				.do_lc_put_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_MEMBER_GROUP				, PrjWorkflow_Ent_Tab_Member_Group);
			tmplCtrl				.do_lc_put_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_MEMBER_GROUP_POPUP			, PrjWorkflow_Ent_Tab_Member_Group_Popup);
		}

		this.do_lc_get_list_member = function(prj){
			pr_GROUP_TEMP   = {};
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET_MEMBER_GROUP, {id: prj.id});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_show_list_member, [prj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_show_list_member = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {
				let data 		= sharedJson[App['const'].RES_DATA];

				const is_Me 		= data.find(m => m.entId02 == App.data.user.id);
				const isSuperAdmin 	= App.controller.UI.Login && App.controller.UI.Login.can_lc_User_SuperAdmin();
				const isOwner		= App.data.user.id === prj.autUser01;
				
				allMembers 	= data.reduce((currentObj, mem)=>{
					if(mem.entId02 == prj.autUser01)	mem.isOwner = true;
					
					if(!isSuperAdmin && !isOwner){
						if(is_Me && is_Me.typ <= mem.typ && is_Me.lev >= mem.lev)	mem.notModif = true;
					}
					
					if(mem.entTyp02 == 1000) mem.isUser = true;
					else{
						if(mem.ent02.val01 && (typeof mem.ent02.val01 == 'string')){
							mem.ent02.val01 = JSON.parse(mem.ent02.val01);
						}
						
						pr_GROUP_TEMP[mem.entId02] = mem;
					}
					currentObj.push(mem);
					return currentObj;
				}, []);

				do_lc_show_prj_member(allMembers, prj);
				pr_ctr_Ent.do_lc_reqRole_User();
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}

		var do_lc_bind_event_members_group_prj = function(allMembers, prj){
//			pr_GROUP_TEMP = $.extend(false, {}, allMembers);
			
			$("#btn_add_group").off("click").on("click", function(){
				$(".action-item-group").removeClass("hide");
				$(this).addClass("hide");
			})

			$("#a_btn_save_member_group").off("click").on("click", function(){
				do_lc_save_member_prj(allMembers, prj);
			})

			$("#a_btn_cancel_member_group").off("click").on("click", function(){
				do_lc_show_prj_member(allMembers, prj);
			})

			$(".member-group-edit").off("click").on("click", function(){
				let $this 			= $(this);
				let {memid} 		= $this.data();
				let mem 			= pr_GROUP_TEMP[memid];
				if(mem){
					let parentTR 	= $this.closest("tr");
					parentTR.find(".content-member").addClass("hide");
					parentTR.find(".edit-member").removeClass("hide");
					let divLev 		= parentTR.find(".level-edit");
					let divTyp 		= parentTR.find(".typ-edit");
					do_lc_bindEvent_tabMemberEdit(memid, divLev, divTyp);
					$(".action-mem-group").removeClass("hide");
				}
			})

			$(".member-group-delete").off("click").on("click", function(){
				let {memid} = $(this).data();
				let mem 	= pr_GROUP_TEMP[memid];
				if(mem){
					delete pr_GROUP_TEMP[memid];
					$(this).closest("tr").remove();
					$(".action-mem-group").removeClass("hide");
				}
			})
			
			$(".show_mem_group").off("click").on("click", function(){
				let {memid} = $(this).data();
				let arr = Object.values(allMembers);
				
				let memGroup = arr.filter(item => item.stat == memid);
				
				App.MsgboxController.do_lc_show({
					title		: $.i18n("prj_project_member_group"),
					content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_MEMBER_GROUP_POPUP, memGroup),	
					autoclose	: false,
					buttons		: {
						Ok: {
							lab		:  $.i18n("common_btn_ok"),
						}
					},
				});	
			})
			
			

			let el = "#inp_name_group";
			let reqSelectGroup = function(event, item){
				if(pr_GROUP_TEMP[item.id])			return false;

				let typ 		= $("#sel_group_type").val();
				let mem 		= {"typ": typ, "ent02": item, "entId02": item.id, "entId01": prj.id, "entTyp02": 40000};
				
				let name = ""
				if(!item.name)	name = "A";
				else name =  item.name.trim().substr(0,1).toUpperCase();

				let strName = item.name.length > 10?item.name.substr(0, 10) + "..." : item.name;
				
				pr_GROUP_TEMP[item.id] = mem;
				let selOpt 		= `<tr>`;
				selOpt 			+= `<td><a data-id='${item.id}' class='text-danger btn-remove-group' data-toggle='tooltip' data-placement='top' title='' data-original-title='Delete'><i class='mdi mdi-close font-size-18'></i></a></td>`;
				
				
				if(!item.val01) selOpt 			+= `<td style='width: 50px;'><div class="rounded-circle avatar-xs text-primary text-uppercase bg-soft-primary text-center mr-2"><div class="text-middle">${name}</div></div></td>`;
				else{
					if(!item.val01.img) selOpt 	+= `<td style='width: 50px;'><div class="rounded-circle avatar-xs text-primary text-uppercase bg-soft-primary text-center mr-2"><div class="text-middle">${name}</div></div></td>`;
					else                selOpt 	+= `<td style='width: 50px;'><img src='${item.val01.img}' class='rounded-circle avatar-xs' alt=''/></td>`;
				}
				selOpt 			+= `<td><h5 class='font-size-14 m-0' title="${item.name}"><a href='' class='text-dark'>${strName}</a></h5></td>`;
				selOpt 			+= `<td>` + $.i18n(PRJ_GROUP_TYPE[+typ])	+`</td>`;
				selOpt 			+= `</tr>`;

				$("#tabGroup table tbody").append(selOpt);
				do_lc_bind_event_autocomplete_group(pr_GROUP_TEMP);
				$(el).blur().val("");
			}

			let Options = {
					dataService : [pr_SERVICE_GROUP_CLASS, pr_SV_GROUP_SEARCH], fSelect: reqSelectGroup, customShowList: do_lc_customLst_group_autocomplete
			}
			do_gl_req_autocompleteNew(el, Options);
		}

		var do_lc_bind_event_autocomplete_group = function(pr_GROUP_TEMP){
			$(".btn-remove-group").off("click").on("click", function(){
				let $this 		= $(this);
				let parentTR 	= $this.closest("tr");
				let {id} 		= $this.data();

				if(pr_GROUP_TEMP[id])	delete pr_GROUP_TEMP[id];
				parentTR.remove();
			})
		}

		var do_lc_save_member_prj = function(allMembers, prj){
			
			
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_SAVE_MEMBER_GROUP, {idPrj: prj.id, members: JSON.stringify(Object.values(pr_GROUP_TEMP))});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterSave_member_group, [allMembers, prj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_afterSave_member_group = function(sharedJson, allMembers, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_gl_show_Notify_Msg_Success 	($.i18n('common_success_update'));
//				do_lc_show_prj_member(members, prj);
				self.do_lc_get_list_member(prj)
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}

		var do_lc_bindEvent_tabMemberEdit = function(memid, divLev, divTyp){
			$(divLev).off("change").on("change", function(){
				pr_GROUP_TEMP[memid].lev = $(this).val();
			})

			$(divTyp).off("change").on("change", function(){
				pr_GROUP_TEMP[memid].typ = $(this).val();;
			})
		}

		var do_lc_customLst_group_autocomplete = function(item, selOpt = ""){
			let name = ""
			if(!item.name)	name = "A";
			else name =  item.name.trim().substr(0,1).toUpperCase();
			 
			if(!item.val01){
				selOpt 		+= `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-primary text-uppercase bg-soft-primary text-center mr-2"><div class="text-middle">${name}</div></div>${item.name}</div>`;
			}else{
				item.val01 = JSON.parse(item.val01);
				if(!item.val01.img) selOpt 		+=  `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-primary text-uppercase bg-soft-primary text-center mr-2"><div class="text-middle">${name}</div></div>${item.name}</div>`;
				else selOpt 		+= `<div class="media align-items-center"><img src='${item.val01.img}' class='rounded-circle avatar-xs mr-2'/> ${item.name}</div>`;
			}
			return selOpt;
		}

		var do_lc_show_prj_member = function(allMembers, prj){
			do_lc_load_view();
			$("#div_prj_member_group")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_MEMBER_GROUP		, allMembers));
			do_lc_bind_event_members_group_prj(allMembers, prj);
			do_lc_bind_event_resize();
		}
		
//		const do_lc_get_member_of_group = (groupId, idPrj) => {
//			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET_MEMBER_GROUP, {id: idPrj});	
//
//			let fSucces		= [];
//			fSucces.push(req_gl_funct(null, do_lc_afterGet_member_group, [idPrj]));
//
//			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
//
//			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
//		}
//		
//		var do_lc_afterGet_member_group = function(sharedJson, idPrj){
//			if(can_gl_AjaxSuccess(sharedJson)) {
//				members = sharedJson.res_data;
//			} else {   
//				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
//			}
//		}
		//------------------------------End list member group-----------------------------------
	}

	//------------------------------Start Doc list-----------------------------------
	var PrjProjectEntTabDoc 	= function (grpName, header, content, footer) {
		//------------------------------Start File list-----------------------------------
		let self			= this;
		const pr_ctr_Ent	= App.controller.UI.Ent;

		var do_lc_load_view = function(){
			tmplCtrl				.do_lc_put_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_DOCS				, PrjWorkflow_Ent_Tab_Docs);
		}

		this.do_lc_show_prj_docs = function(prj){
			do_lc_load_view();

			$("#div_prj_docs")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_DOCS, prj));
			do_lc_bind_event_docs_prj(prj);
			do_lc_bind_event_resize();
			
			pr_ctr_Ent.do_lc_reqRole_User();
		}

		var do_lc_bind_event_docs_prj = function(prj){
//			let	obj 		= {files:[]};
			if(!prj.files)	prj.files = [];
			let option		= {
					fileinput	: { 
						param 			: {typ01: 1, typ02: 1, filenameKept: 1},
						addRemoveLinks 	: !pr_ctr_Ent.can_lc_role_user_worker()
					},//option here
					obj			: prj//file existing here
			}
			do_gl_init_fileDropzone($("#div_prj_docs"), option);

			$(".item-file-download").off("click").on("click", function(){
				let {path} = $(this).data();
				path && window.open(path, "_blank");
			})

			$(".item-file-delete").off("click").on("click", function(){
				let id	= $(this).data("id");
				$(this).parents("tr").remove();
			})

			$("#btn_add_doc").off("click").on("click", function(){
				$(".action-item-doc").removeClass("hide");
				$("#div_prj_ent_file_upload").removeClass("hide");
				$(this).addClass("hide");
				$(".item-file-delete").removeClass("hide");
			})

			$("#a_btn_save_doc").off("click").on("click", function(){
				prj.files 		= prj.files ? [...prj.files].filter(Boolean) : [];
				let	data	= req_gl_data({
					dataZoneDom		: $("#div_prj_docs"),
					skipError		: true
				});

				if(data.hasError)	return false;

				let newprj 		= data.data;
				
				newprj.files 		=   prj.files;
				
				newprj = $.extend(false, prj, newprj);

				console.log(prj.files);

				do_lc_save_files_prj(newprj);
			})

			$("#a_btn_cancel_doc").off("click").on("click", function(){
				self.do_lc_show_prj_docs(prj);
			})
		}

		var do_lc_save_files_prj = function(prj){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_SAVE_FILES, {obj: JSON.stringify(prj)});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterSave_files_prj, [prj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_afterSave_files_prj = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {
				prj.files = sharedJson[App['const'].RES_DATA].files;
				self.do_lc_show_prj_docs(prj);
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}
		//------------------------------End File list-----------------------------------
	}
	//------------------------------End File list-----------------------------------

	//------------------------------Start Comment list-----------------------------------
	var PrjProjectEntTabComment 	= function (grpName, header, content, footer) {
		//------------------variable pagination post------------------------------------------------------
		var pr_POST_BEGIN 			= 0;
		const pr_POST_NUMBER 		= 10;
		const pr_POST_TYPE_PRJ 		= 202;
		const pr_POST_NO_SUB 		= 1;
		const pr_POST_HAS_SUB 		= 2;
		const pr_POST_KEY_ENTER 	= 13;
		const self					= this;
		//------------------------------Start comment list-----------------------------------
		var do_lc_load_view = function(){
			tmplCtrl				.do_lc_put_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_COMMENT			, PrjWorkflow_Ent_Tab_Comment);
		}

		var do_lc_show_prj_comment = function(dataCmts, idPrj){
			do_lc_load_view();
			$("#div_comment_list")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_COMMENT, dataCmts));
			
//			$(".a-delete[data-userid= '" + App.data.user.id +"']").removeClass("d-none");
			
			App.SummerNoteController.do_lc_show("#div_prj_comments", {height : 100}, true);//text editor
			
			do_lc_bind_event_comments_prj(idPrj);
			do_lc_bind_event_resize();
		}

		this.do_lc_get_list_comments = function(idPrj, reBuild = false){
			let cond 		= {
					entId: idPrj				, entTyp : pr_ENTITY_TYPE		, type: pr_POST_TYPE_PRJ	,			reBuild,
					begin: pr_POST_BEGIN		, number: pr_POST_NUMBER		, nbLevel: pr_POST_HAS_SUB	,			forced: true, 
			}

			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET_COMMENTS, cond);	

			var callbackFunct = function(data) {		//data => sharedJson
				do_lc_show_comment_Dyn(data, idPrj);
			}

			var opt = {
					divMain			: "#div_comment_list",
					divPagination	: "#div_comment_pagination",
					url_api 		: App.path.BASE_URL_API_PRIV, 
					url_header 		: App.data["HttpSecuHeader"],
					url_api_param 	: ref,
					pageSize 		: pr_POST_NUMBER,
					pageRange		: 1,
					callback		: callbackFunct
			};
			do_gl_init_pagination_opt(opt);
		}

		var do_lc_show_comment_Dyn = function(sharedJson, idPrj){
			let data			= {};
			if (can_gl_AjaxSuccess(sharedJson)) {
				data		= sharedJson[App['const'].RES_DATA];
			}

			do_lc_show_prj_comment(data, idPrj);
		}

		var do_lc_bind_event_comments_prj = function(idPrj){
			$("#btn_send_comment").off("click").on("click", function(){
				let comment = $("#inp_comment").val();
				let iParent = $("#inp_parent_reply").val();
				if(!comment || !comment.length)	return false;
				do_lc_send_comment(comment, iParent, idPrj);
			})

			$(".a-reply").off("click").on("click", function(){
				let{parent, user} = $(this).data();
				parent && $("#inp_parent_reply").val(parent);
				if(user)	$("#inp_comment").summernote('code', `@${user} `);
			})
			
			$(".a-delete").off("click").on("click", function(){
				let{id} = $(this).data();
				if(id)	do_lc_delete_comment(id, idPrj);
			})

			$("#inp_comment").off("keypress").on("keypress", function(e){
				if(e.keyCode == pr_POST_KEY_ENTER){
					$("#btn_send_comment").click();
					return;
				}
				let comment = $(this).val();
				(!comment || !comment.length) && $("#inp_parent_reply").val("");
			})
			
			$("#div_list_item img:not(.avatar-xs)").off("click").on("click", function(){
				let src = $(this).attr("src");
				App.MsgboxController.do_lc_show({
					content 	: `<img src="${src}" style="width: 100%;">`,
					autoclose	: false,
					buttons 	: {
						CALCEL : {
							lab 		: $.i18n("common_btn_cancel"),
							classBtn	: "btn-primary",
						}
					}
				});
			})
		};

		var do_lc_send_comment = function(comment, iParent, idPrj){
			let cond 		= {comment, idPrj, iParent};
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_NEW_COMMENTS, cond);

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterSend_cmts, [idPrj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		var do_lc_afterSend_cmts = function(sharedJson, idPrj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				let data = sharedJson[App['const'].RES_DATA];
				data && self.do_lc_get_list_comments(idPrj, true);
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}
		
		var do_lc_delete_comment = function(idCmt, idPrj){
			let cond 		= {"id": idCmt};
			let ref 		= req_gl_Request_Content_Send_With_Params("ServiceNsoPost" , "SVNsoPostDel12H", cond);

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterDelete_cmts, [idCmt, idPrj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		var do_lc_afterDelete_cmts = function(sharedJson, idCmt, idPrj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				self.do_lc_get_list_comments(idPrj, true);
				$(".post-lement-content[data-id='"+ idCmt +"']").remove();
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}
	}
	//------------------------------End comment list-----------------------------------

	//------------------------------Start Content prj-----------------------------------
	var PrjProjectEntTabContent 	= function (grpName, header, content, footer) {
		const self						= this;
		const pr_TYPE01_INDUSTRY		= 1;
		const pr_TYPE01_INFORMATIQUE	= 2;
		const pr_TYPE01_BUISINESS		= 3;
		const pr_TYPE01_TRAVEL			= 4;
		const pr_project				= App.controller.UI;

		const pr_ctr_Ent				= App.controller.PrjWorkflow.Ent;

		var workflow  					= {};

		var members                     = {};
		const pr_member_lev_manager 	= 0;
		const pr_member_lev_reporter 	= 1;
		const pr_member_lev_worker 		= 2;

		const pr_CHECK_NOT_FINISH 		= 1;
		const pr_CHECK_FINISH 			= 2;
		
		const var_lc_MODE_SEL       = 0;
		const var_lc_MODE_NEW       = 1;
		const var_lc_MODE_MOD       = 2;

		const pr_STAT_PRJ_NEW 			= 100100;
		const pr_STAT_PRJ_TODO 			= 100200;
		const pr_STAT_PRJ_INPROGRESS 	= 100300;
		const pr_STAT_PRJ_DONE 			= 100400;
		const pr_STAT_PRJ_TEST 			= 100500;
		const pr_STAT_PRJ_REVIEW 		= 100600;
		const pr_STAT_PRJ_FAIL 			= 100700;
		const pr_STAT_PRJ_UNRESOLVED 	= 100800;
		const pr_STAT_PRJ_CLOSED 		= 100900;
		
		const pr_NB_RECORD_HISTORY		= 10;

		var pr_DEFAULT_VAL			    = 0;
		var pr_div_rating		        = ['#rating_01'];
		//----------------------Get Path -------------------------------------------------
		const do_lc_get_path_prj = ({id, grp, typ02, parent}) => {
			if([pr_TYPE02_PRJ].includes(typ02))	return;
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET_PARENT, {prjID : id, prjGroup : grp});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_getPrjParent_response, [id, parent]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_bg (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		const do_lc_getPrjParent_response = (sharedJson, id, parent) => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				let data = sharedJson[App['const'].RES_DATA];
				if(data && Array.isArray(data) && data.length){
					const parObj = data.find(e => +e.id === +parent)

					if(parObj) {
						const url = $("#btn_back").data("url").replace("#code", parObj.code01).replace("#id", parObj.id)
						
						$("#btn_back").data("url", url)
					}
					
					do_lc_transfert_data(data, id);
				}
			}
		}


		const do_lc_transfert_data = (data, id) => {
			let prjInd 	= data.findIndex(prj => prj.id === prj.grp);
			let prjAll 	= data[prjInd];
			data.splice(prjInd, 1);

			let mapPrj 	= new Map();

			for(let p of data){
				mapPrj.set(p.parent, p);
				p["isShow"] = p.id == id ? true : false;
			}

			const addTree = pItem => {
				if(mapPrj.has(pItem.id)){
					pItem["child"] = mapPrj.get(pItem.id);
					addTree(pItem["child"] );
				}
			}

			addTree(prjAll);
			do_lc_show_path(prjAll);
		}

		const do_lc_show_path = prjAll => {
			$("#div_prj_path").html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_CONTENT_PATH, prjAll));
		}
		//------------------------------Start content prj-----------------------------------
		var do_lc_load_view = function(){
			tmplCtrl.do_lc_put_tmpl(tmplName.PRJ_PROJECT_ENT_CONTENT				, PrjWorkflow_Ent_Content);
			tmplCtrl.do_lc_put_tmpl(tmplName.PRJ_WORKFLOW_POPUP_PICK_USER			, PrjWorkflow_Popup_Pick_User);
		}
		
		this.do_lc_show_prj_content = function(prj, mode){
			do_lc_load_view();
			do_lc_show_content(prj, mode);
			do_lc_get_path_prj(prj);
		}

		var do_lc_show_content = function(prj, mode){
			if(prj.epicInf){
				let epics = prj.epicInf;
				let obj = epics.reduce((currentEpic, epic)=>{
					currentEpic[epic.id] = epic;
					return currentEpic;
				}, {});

				if(obj[prj.parent]) prj.epicName = obj[prj.parent].name;
			}
			
			$("#div_prj_content")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_CONTENT, prj));
			
			$("#projectepic").find("option[value="+ prj.parent	+"]")	.attr("selected","selected");

			prj.lstClone = prj.descr02 ? JSON.parse(prj.descr02) : [];
			do_lc_show_checkList(prj);

			if(prj.stat == pr_STAT_PRJ_CLOSED){
				$("#div_star_eval").show();
				if(prj.val00 != null && prj.val00 != pr_DEFAULT_VAL) do_lc_show_evalutation(prj.val00, false);
				else do_lc_show_evalutation(pr_DEFAULT_VAL, true);
			}else{
				do_lc_show_evalutation(pr_DEFAULT_VAL, true);
			}
			
			do_lc_bind_event_content_prj(prj, mode);
			do_lc_bind_event_resize();

			do_lc_init_element(prj);

			//--- if business , load client, if not, hide div
			// if(prj.typ01 == pr_TYPE01_BUISINESS){
			// 	App.controller.UI.EntCustomer.do_lc_get_list_customers(prj.id);
			// } else {
				$("#div_prj_customers")	.html("");
			// }

			if(prj.stat == pr_STAT_PRJ_DONE) {
				let $parent = $(".val05").parent();
				$parent.find(".info-edit")	.off("click");
				$parent.find(".val05")	.removeClass("content-edit");
			}

			do_lc_show_work_flow(prj);

			pr_ctr_Ent.do_lc_reqRole_User();
			pr_ctr_Ent.do_lc_reqRole_User_Workflow();
		}

		const do_lc_show_work_flow = (prj) => {
			let wf  = prj.descr01;
			try{
				workflow 			= JSON.parse(wf);
				workflow.statWF 	= req_lc_build_objWF(workflow.con);
				App.controller.PrjWorkflow.Main.do_lc_show_workflow(workflow);
			} catch(e) {
			}
		}

		const req_lc_build_objWF = (data) => {
			var statWF ={};
			if (data && data.length > 0) {
				data.forEach((e) => {
					statWF[e.i] = e.userTyp;
				});
			}
			return statWF;
		}
		
		const do_lc_show_checkList = (prj) => {
			//clone list check list
			$("#div_check_list").html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_CONTENT_CHECK_LIST, prj));
			do_lc_bind_event_checkList(prj);
		}
		
		const do_lc_show_evalutation =  (eval, isDef) => {
			do_gl_bar_rating_init_one("eval01", eval);
			do_gl_bar_rating_show_all(pr_div_rating, "", null, eval, null );
			req_gl_bar_rating_value(pr_div_rating, eval);
			if(isDef) $("#div_star_eval").find("a[data-rating-value='"+ 1 +"']").removeClass("br-selected br-current");
		}

		const do_lc_bind_event_checkList = (prj) => {
			$(".item-chk-box").off("change").on("change", function(){
				const $this 	= $(this);
				const isCheck 	= $this.is(":checked");
				const {index} 	= $this.data();
				prj.lstClone[index].stat = isCheck ? pr_CHECK_FINISH : pr_CHECK_NOT_FINISH;
				do_lc_show_checkList(prj);
				$("#a_btn_save, #a_btn_cancel")	.removeClass("hide");
				pr_ctr_Ent.do_lc_reqRole_User();
			})

			$(".remove-item-chk").off("click").on("click", function(){
				const {index} 	= $(this).data();
				index > -1 && prj.lstClone.splice(index, 1);
				do_lc_show_checkList(prj);
				$("#a_btn_save, #a_btn_cancel")	.removeClass("hide");
			})

			$(".edit-item-chk").off("click").on("click", function() {
				const {index} 		= $(this).data();
				let _content_chk 	= prj.lstClone[index].item;
				if(!_content_chk)	return false;

				const do_lc_edit_chk_lst = () => {
					const _content_chk_new = $("#inp_chk_lst").val();
					if(!_content_chk_new || !_content_chk_new.trim().length)	return false;

					prj.lstClone[index].item = _content_chk_new.trim();
					do_lc_show_checkList(prj);
					$("#a_btn_save, #a_btn_cancel")	.removeClass("hide");
					App.MsgboxController.do_lc_close();
				}

				App.MsgboxController.do_lc_show({
					title 		: $.i18n("prj_project_descr02_msgbox_edit"),
					content 	: `<div class="mb-4"><input class="form-control" id="inp_chk_lst" value="${_content_chk}" type="text" placeholder="${$.i18n("prj_project_descr02_enter_inp")}"></div>`,
					autoclose	: false,
					buttons 	: {
						UPDATE : {
							lab 		: $.i18n("prj_project_descr02_edit"),
							funct 		: do_lc_edit_chk_lst,
							classBtn	: "btn-primary",
							autoclose	: false
						},
						CALCEL : {
							lab 		: $.i18n("common_btn_cancel"),
						}
					}
				});
			})
		}

		const do_lc_init_element = function(){
			$(".tmpicker").timepicker({//timepicker
				showMeridian: false,
				icons		: {
					up		: "mdi mdi-chevron-up",
					down	: "mdi mdi-chevron-down"
				}
			});
			setTimeout(function(){
				App.SummerNoteController.do_lc_show("#div_prj_info");
			},500);
		}

		var do_lc_bind_event_content_prj = function(prj, mode){

//			let files = prj.avatar ? [prj.avatar] : [];
//			let	obj 		= {files};
			if(!prj.files)	prj.files = [];
			let option		= {
					fileinput	: {maxFiles : 1, param : {typ01: 1, typ02: 1} },//option here
					obj			: prj//file existing here
			}
			do_gl_init_fileDropzone($("#div_prj_content"), option);

			if(mode && mode == var_lc_MODE_NEW){
				$(".action-item-duplicate").remove();

				$("#div_prj_member"		).remove();
//				$("#div_prj_docs"		).remove();				
				$("#div_prj_comments"	).remove();
				$("#div_prj_epic"		).remove();
				$("#div_prj_task"		).remove();
				$("#div_prj_evaluation"	).remove();
				
//				$(".info-content").addClass("hide");			
//				$(".content-edit").removeClass("hide");
//				$("#div_prj_ent_file_avatar").removeClass("hide");
				
				$("#div_partner_funct").removeClass("hide");
				$("#btn_save").off("click").on("click", function(){
					prj.files 	= prj.files ? [...prj.files].filter(Boolean) : [];
					
					let	data	 				= req_gl_data({
						dataZoneDom		: $("#div_prj_content"),
						oldObject 		: prj,
					});

					if(data.hasError)	return false;

					let newPrj 			= data.data;

					
//					newPrj.files	= newPrj.files.concat(obj.files);

					newPrj.dtBegin 	= do_lc_convert_date(newPrj.dtBegin);
					newPrj.dtEnd 	= do_lc_convert_date(newPrj.dtEnd);

					newPrj.parent 	= newPrj.parent == 0? prj.grp: newPrj.parent;

					switch(parseInt(newPrj.stat)){
					case pr_STAT_PRJ_NEW		: newPrj.val05 =   0; break;
					case pr_STAT_PRJ_TODO		: newPrj.val05 =   0; break;
					case pr_STAT_PRJ_INPROGRESS : newPrj.val05 =  10; break;
					case pr_STAT_PRJ_REVIEW		: newPrj.val05 =  90; break;
					case pr_STAT_PRJ_DONE		: newPrj.val05 = 100; break;
					case pr_STAT_PRJ_CLOSED		: newPrj.val05 = 100; break;
					case pr_STAT_PRJ_FAIL		: newPrj.val05 =   0; break;
					case pr_STAT_PRJ_UNRESOLVED	: newPrj.val05 =   0; break;
					}

					if(newPrj.lstClone)	newPrj.descr02 = JSON.stringify(newPrj.lstClone);

					do_lc_create_prj(newPrj)
				})
				
				$(".info-edit").on("click", function(){
					let $parent = $(this).parent();
					$parent.find(".info-content")			.addClass("hide");
					$parent.find(".info-content-worker")	.addClass("hide");
					$parent.find(".content-edit")	.removeClass("hide");

					if($parent.find(".content-edit").length > 0){
						$("#a_btn_save, #a_btn_cancel")	.removeClass("hide");
					}

					pr_project.Ent.do_lc_reqRole_User();
				})
				
			}else{
				$(".info-edit").on("click", function(){
					let $parent = $(this).parent();
					$parent.find(".info-content")			.addClass("hide");
					$parent.find(".info-content-worker")	.addClass("hide");
					$parent.find(".content-edit")	.removeClass("hide");

					if($parent.find(".content-edit").length > 0){
						let $parents = $parent.closest(".card");
						$parents.find("#a_btn_save, #a_btn_cancel")	.removeClass("hide");
						$parents.find("#a_btn_save02, #a_btn_cancel02")	.removeClass("hide");

					}

					// pr_project.Ent.do_lc_reqRole_User();
				})

				$("#btn_edit_wf").off("click").on("click", function() {
					prj.files 	= prj.files ? [...prj.files].filter(Boolean) : [];
					let	data	 				= req_gl_data({
						dataZoneDom		: $("#div_prj_content")
					});

					if(data.hasError)	return false;

					let newPrj 			= data.data;
					
					let oldStat = prj.stat; //get stat for check percent

					if(prj && (prj.userRole == pr_member_lev_reporter || prj.userRole == pr_member_lev_worker)){
						newPrj 			= Object.assign({}, prj);
						newPrj.stat 	= data.data.stat;
						newPrj.val05 	= data.data.val05;
					}else{
						newPrj 			= $.extend(false, prj, newPrj);
					}

					newPrj.parent 	= newPrj.parent == 0? prj.grp: newPrj.parent;
					newPrj.val00    = null;
					
					switch(parseInt(newPrj.stat)){
					case pr_STAT_PRJ_NEW		: newPrj.val05 =   0; break;
					case pr_STAT_PRJ_TODO		: newPrj.val05 =   0; break;
					case pr_STAT_PRJ_INPROGRESS : 
						if(oldStat != newPrj.stat){
							newPrj.val05 =  10; 
						}else {
							//todo
						}
						break;
					case pr_STAT_PRJ_REVIEW		: newPrj.val05 =  90; break;
					case pr_STAT_PRJ_DONE		: newPrj.val05 = 100; break;
					case pr_STAT_PRJ_CLOSED		: 
						newPrj.val05 = 100; 
						newPrj.val00 = App.data.curEval.eval01;
						break;
					case pr_STAT_PRJ_FAIL		: newPrj.val05 =   0; break;
					case pr_STAT_PRJ_UNRESOLVED	: newPrj.val05 =   0; break;
					}

					if(newPrj.lstClone)	newPrj.descr02 = JSON.stringify(newPrj.lstClone);
					newPrj.descr01 = JSON.stringify(App.controller.PrjWorkflow.Main.req_lc_save_workflow(workflow));

					do_lc_save_prj_content(newPrj, prj)
				});

				$("#a_btn_save, #a_btn_save02").off("click").on("click", function(){
					prj.files 	= prj.files ? [...prj.files].filter(Boolean) : [];
					let	data	 				= req_gl_data({
						dataZoneDom		: $("#div_prj_content")
					});

					if(data.hasError)	return false;

					let newPrj 			= data.data;
					
					let oldStat = prj.stat; //get stat for check percent

					if(prj && (prj.userRole == pr_member_lev_reporter || prj.userRole == pr_member_lev_worker)){
						newPrj 			= Object.assign({}, prj);
						newPrj.stat 	= data.data.stat;
						newPrj.val05 	= data.data.val05;
					}else{
						// newPrj.files	= newPrj.files.concat(obj.files);

						// newPrj.dtBegin 	= do_lc_convert_date(newPrj.dtBegin);
						// newPrj.dtEnd 	= do_lc_convert_date(newPrj.dtEnd);

						newPrj 			= $.extend(false, prj, newPrj);
					}

					newPrj.parent 	= newPrj.parent == 0? prj.grp: newPrj.parent;
					newPrj.val00    = null;
					
					switch(parseInt(newPrj.stat)){
					case pr_STAT_PRJ_NEW		: newPrj.val05 =   0; break;
					case pr_STAT_PRJ_TODO		: newPrj.val05 =   0; break;
					case pr_STAT_PRJ_INPROGRESS : 
						if(oldStat != newPrj.stat){
							newPrj.val05 =  10; 
						}else {
							//todo
						}
						break;
					case pr_STAT_PRJ_REVIEW		: newPrj.val05 =  90; break;
					case pr_STAT_PRJ_DONE		: newPrj.val05 = 100; break;
					case pr_STAT_PRJ_CLOSED		: 
						newPrj.val05 = 100; 
						newPrj.val00 = App.data.curEval.eval01;
						break;
					case pr_STAT_PRJ_FAIL		: newPrj.val05 =   0; break;
					case pr_STAT_PRJ_UNRESOLVED	: newPrj.val05 =   0; break;
					}


					if(newPrj.lstClone)	newPrj.descr02 	= JSON.stringify(newPrj.lstClone);
					newPrj.stat 						= oldStat == 0 ? 1 : 0;
					do_lc_save_prj_content(newPrj, prj)
				})

				$(".btn-reload").off("click").on("click", function(){
					let {name: typLoad} = $(this).data();
					do_lc_get_content_reload(prj, typLoad);
				})

				$("#btn_refresh_content").off("click").on("click", function() {
					do_lc_refresh_content(prj, prj.id);
				})

				$("#btn_delete").off("click").on("click", function() {
					do_lc_delete_content(prj, prj.id);
				})

				$("#btn_duplicate_content").off("click").on("click", function() {
					do_lc_duplicate_content(prj);
				})

				$("#btn_add_avatar").off("click").on("click", function(){
					$("#div_prj_ent_file_avatar").removeClass("hide");
					$(this).addClass("hide");
					$("#a_btn_save, #a_btn_cancel")	.removeClass("hide");
				})
				
				$("#btn_add_chk_lst").off("click").on("click", function() {
					const do_lc_add_chk_lst = () => {
						const _content_chk = $("#inp_chk_lst").val();
						if(!_content_chk || !_content_chk.trim().length)	return false;
	
						prj.lstClone.push({item: _content_chk.trim()});
						do_lc_show_checkList(prj);
						$("#a_btn_save, #a_btn_cancel")	.removeClass("hide");
						pr_ctr_Ent.do_lc_reqRole_User();
						App.MsgboxController.do_lc_close();
					}
	
					App.MsgboxController.do_lc_show({
						title 		: $.i18n("prj_project_descr02_msgbox_add"),
						content 	: `<div class="mb-4"><input class="form-control" id="inp_chk_lst" type="text" placeholder="${$.i18n("prj_project_descr02_enter_inp")}"></div>`,
						autoclose	: false,
						buttons 	: {
							UPDATE : {
								lab 		: $.i18n("prj_project_descr02_add"),
								funct 		: do_lc_add_chk_lst,
								classBtn	: "btn-primary",
								autoclose	: false
							},
							CALCEL : {
								lab 		: $.i18n("common_btn_cancel"),
							}
						}
					});
				})
				
				$("#btn_show_history").off("click").on("click", function() {
					do_gl_init_msgbox_annonce(`<div id="div_history_list"></div><div id="div_history_pagination"></div>`, null, $.i18n("prj_history_title"));
					do_lc_get_history(prj);
				})
				
				if(prj.autUser01 == App.data.user.id){
					$("#div_star_eval a").on("click", function() {
						$("#a_btn_save, #a_btn_cancel")	.removeClass("hide");
					})
				}else{
					$("#div_star_eval a").css("pointer-events","none");
				}
			}
			
			$("#div_prj_content_01 img").off("click").on("click", function(){
				let src = $(this).attr("src");
				App.MsgboxController.do_lc_show({
					content 	: `<img src="${src}" style="width: 100%;">`,
					autoclose	: false,
					buttons 	: {
						CALCEL : {
							lab 		: $.i18n("common_btn_cancel"),
							classBtn	: "btn-primary",
						}
					}
				});
			})
			
		};
		
		const do_lc_get_history = function(prj){
			const ref 				= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET_HISTORY_TASK, {taskId : prj.id});

//			const callbackFunct 	= data => do_lc_after_reqList_history(data);
//
//			const opt = {
//					divMain			: "#div_history_list",
//					divPagination	: "#div_history_pagination",
//					url_api 		: App.path.BASE_URL_API_PRIV, 
//					url_header 		: App.data["HttpSecuHeader"],
//					url_api_param 	: ref,
//					pageSize 		: pr_NB_RECORD_HISTORY,
//					pageRange		: 1,
//					callback		: callbackFunct
//			};
//
//			do_gl_init_pagination_opt(opt);
			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_after_req_history, [prj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_after_req_history = function(sharedJson){
			const data = can_gl_AjaxSuccess(sharedJson) ? sharedJson[App['const'].RES_DATA] :  {};
			if(data.cmt){
				let cmt = JSON.parse(data.cmt);
				do_lc_show_listHistory(cmt);
			}
		}

		const do_lc_show_listHistory = function(data){
			data = data.reverse();
			$("#div_history_list").html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_HISTORY , data));
		}

		const do_lc_convert_date = objDate => objDate.date.substr(0, 10) + " " + objDate.time.substr(0, 5) + ":00";

		var do_lc_save_prj_content = function(newPrj, prj){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_SAVE_CONTENT, {obj: JSON.stringify(newPrj)});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterSave_prjContent, [prj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_afterSave_prjContent = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {
				let data 	= sharedJson[App['const'].RES_DATA];
				prj 		= $.extend(true, prj, data);
				self.do_lc_show_prj_content(prj);
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get'));
			}
		}

		var do_lc_get_content_reload = function(prj, typLoad){
			if(!typLoad)	return false;
			let pr_SV_NAME_RELOAD 	= typLoad === "val02" ? pr_SV_EVAL_GET_BUDGET : pr_SV_EVAL_GET_PERCENT;

			let ref 				= req_gl_Request_Content_Send_With_Params(pr_SERVICE_EVAL_CLASS, pr_SV_NAME_RELOAD, {id: prj.id});	

			let fSucces				= [];
			fSucces.push(req_gl_funct(null, do_lc_after_Reload, [prj]));

			let fError 				= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		var do_lc_after_Reload = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				let data = sharedJson[App['const'].RES_DATA];
				if(data){
					prj = $.extend(true, prj, data);
					self.do_lc_show_prj_content(prj);
				}
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}

		var do_lc_delete_content = function(prj, prjId){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_DEL_WF, {id: prjId});			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_delete_content, [prj]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_after_delete_content = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				let data = sharedJson[App['const'].RES_DATA];
				do_gl_show_Notify_Msg_Success($.i18n('common_ok_msg_del'));
				$(".btn-refrest-list").trigger("click");
				$("#div_task_content_main").html("");
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_del'));
			}
		}

		var do_lc_refresh_content = function(prj, prjId){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_REFRESH_CONTENT, {id: prjId});			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_refresh_content, [prj]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_after_refresh_content = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				let data = sharedJson[App['const'].RES_DATA];
				prj = Object.assign(prj, data);
				do_lc_show_content(prj);

				// pr_project.EntEpic		.do_lc_show_prj_epic(prj);
				// pr_project.EntTask		.do_lc_show_prj_task(prj);
				// pr_project.EntDoc		.do_lc_show_prj_docs(prj);
				// pr_project.EntEval		.do_lc_get_prj_evaluation(prj);
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}

		var do_lc_duplicate_content = function(prj){
			let m
			var newObj 			= $.extend(true,{},prj);

			//---duplicate 	record in document and detail		
			newObj = do_duplicate_record(newObj);
			
			self.do_lc_show_prj_content(newObj, var_lc_MODE_NEW)
			pr_project.EntDoc		.do_lc_show_prj_docs(newObj, 1);
			
		}
		
		function do_duplicate_record(obj){
			obj.id			= null;
			obj.code        = null;
			obj.name		= obj.name + " - COPY"  ;
			
			if(obj.files){
				for(let i=0; i < obj.files.length; i++){
					obj.files[i].id 		= null;
				}
			}
			
			obj.dtBegin =  req_gl_DateStr_From_DateObj (new Date());
			obj.dtEnd 	=  req_gl_DateStr_From_DateObj (new Date());
			obj.dtMod	=  null;
			obj.dtNew	=  null;
			
			return obj;
		}
		
		const do_lc_create_prj = prj => {
			let dataSend	= {obj: JSON.stringify(prj), member : Object.values(members)};
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_NEW, dataSend);			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_show_prj, [prj]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_show_prj = (sharedJson, prj) => {
			if(can_gl_AjaxSuccess(sharedJson)) {	
				let data = sharedJson[App['const'].RES_DATA];
				pr_ctr_Main.do_lc_switch_mobile_or_pc(`view_prj_project_content.html?id=${data.id}&code=${data.code01}`, "VI_MAIN/"+ App.router.part.PRJ_PROJECT_ENT, [data.id], '_self');
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}

		//------------------------------End content prj-----------------------------------
	}
	//------------------------------End content prj-----------------------------------

	return {PrjProjectEntTabMember, PrjProjectEntTabMemberGroup, PrjProjectEntTabDoc, PrjProjectEntTabComment, PrjProjectEntTabContent};
});