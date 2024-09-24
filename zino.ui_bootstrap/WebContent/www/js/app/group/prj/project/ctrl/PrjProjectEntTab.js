define([
	'prjImageViewer/viewer',
	],
	function(Viewer){

	const pr_ENTITY_TYPE			= 20000;
	const pr_SERVICE_CLASS			= "ServicePrjProject"; //to change by your need
	const pr_SV_GET_LST        		= "SVLst";
	const pr_SV_GET					= "SVGet"; 
	const pr_SV_NEW					= "SVNew"; 
	const pr_SV_DEL					= "SVDel";

	const pr_SV_GET_REPORT			= "SVReportGet"; 
	const pr_SV_SAVE_REPORT			= "SVReportSave"; 
	
	const pr_SV_SAVE_CONTENT		= "SVSaveContent";
	const pr_SV_SAVE_FILES			= "SVFileSave"; 
	const pr_SV_ADD_FILES			= "SVFileAdd"; 
	const pr_SV_DEL_FILES			= "SVFileDel"; 
	
	const pr_SV_REFRESH_EPIC		= "SVEpicRefresh";
	const pr_SV_REFRESH_TASK		= "SVTaskRefresh";
	const pr_SV_REFRESH_CONTENT		= "SVContentRefresh";
	const pr_SV_REFRESH_TASK_SPRINT = "SVSprintRefresh";

	
	const pr_SV_UPDATE_INFO_WITH_CUSTOMER 	= "SVUpdateInfoWithCustomer";
	const pr_SV_GET_INFO_WITH_CUSTOMER 		= "SVInfoWithCustomer";
	const pr_SV_DELETE_CUSTOMER 			= "SVDeleteCustomer";
	const pr_SV_CHANGE_TYP_CUSTOMER			= "SVChangeTypCustomer";
	const pr_SV_NEW_CUSTOMER 				= "SVNewCustomer";
	const pr_SV_GET_CUSTOMER				= "SVGetCustomer";
	const pr_SV_SAVE_CUSTOMER				= "SVSaveCustomer"; 

	
	const pr_SV_SPRINT_DEL		= "SVPrjSpintDel";
	const pr_SV_GET_PARENT		= "SVGetParent";
	const pr_SV_MOVE_TASK		= "SVEpicAddTask";

	const pr_SERVICE_PER_CLASS	= "ServiceAutUser";
	const pr_SV_USER_SEARCH		= "SVLst";
	const pr_SV_PERSON_SEARCH	= "SVPersonLstSearchWithAvatar";

	const pr_SERVICE_GROUP_CLASS= "ServiceNsoGroup";
	const pr_SV_GROUP_SEARCH	= "SVLst";
	
	const pr_SERVICE_EVAL_CLASS	= "ServicePrjProjectEval";
	const pr_SV_EVAL_GET_BUDGET	= "SVPrjValReal";
	const pr_SV_EVAL_GET_PERCENT= "SVPrjValPercent";
	const pr_SV_EVALUATION		= "SVGetEvaluation";
	const pr_SV_SAVE_MOVE		= "SVTaskMove";
	
	const pr_SERVICE_DYN_CLASS		= "ServicePrjProjectDyn";
//	const pr_SV_GET_HISTORY_TASK	= "SVLstHistoryTask";
	
	const pr_SV_GET_HISTORY_TASK	= "SVGetHistoryTask";
	
	const pr_SV_CALCUL_PERCENT_SPRINT  =  "SVCalculPercentSprint";

	//------------------const object------------------------------------------------------

	const pr_TYPE02_PRJ				= 0;
	const pr_TYPE02_EPIC			= 1;
	const pr_TYPE02_TASK			= 2;

	const pr_STAT_PRJ_NEW 			= 100100;
	const pr_STAT_PRJ_TODO 			= 100200;
	const pr_STAT_PRJ_INPROGRESS 	= 100300;
	const pr_STAT_PRJ_DONE 			= 100400;
	const pr_STAT_PRJ_TEST 			= 100500;
	const pr_STAT_PRJ_REVIEW 		= 100600;
	const pr_STAT_PRJ_FAIL 			= 100700;
	const pr_STAT_PRJ_UNRESOLVED 	= 100800;
	const pr_STAT_PRJ_CLOSED 		= 100900;
	var paramStat					= null;

	const pr_ctr_Main 				= App.controller.DBoard.DBoardMain;

	var do_lc_bindEvent_resize = function(div){
		$(".btn-resize").off("click").on("click", function(){
			do_resize(this);
		})
		
		if ($(window).width() < 800){
			if (div) do_resize($(div).find(".btn-resize"));
		}
	}
	
	var do_resize = function (ele){
		if (ele.length==0) return;
		let $this 		= $(ele);
		let {divtoogle} = $this.data();
		let child 		= $this.find("i");
		let label 		= $this.find(".label-resize");
		child			.toggleClass("mdi-window-minimize mdi-window-maximize")
		$(divtoogle)	.toggle("hide");

		label.html(child.hasClass("mdi-window-minimize") ? $.i18n("prj_project_resize_min") : $.i18n("prj_project_resize_max"));
	}

	var do_scrollToTop = function (){
		if(window.scrollY<50) return;
		window.scrollTo(0, 0);
	}
	
	//-------------------------------------------------------------------------------------------------------------------------------
	const PrjProjectEntTabEval 	= function (grpName, header, content, footer) {
		//------------------------------------------------------------------------------------
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------

		//------------------------------------------------------------------------------------
		var self 					= this;
		
		var pr_isViewSprint         = false;
		//---------show-----------------------------------------------------------------------------
		this.do_lc_get_prj_evaluation = function(prj, isViewSprint){
			if(isViewSprint) pr_isViewSprint = true;
			if(prj.typ02 === pr_TYPE02_TASK)	return;

			do_lc_get_list_child(prj);
		}
		
		const do_lc_get_list_child = (prj) => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_EVALUATION, {id: prj.id, code: prj.code01});

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_getChild, [prj]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_after_getChild = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {
				const data = sharedJson[App['const'].RES_DATA];
				do_lc_transform_data(data, prj);
			}
		}
		
		const do_lc_transform_data = (data, prj) => {
			let lstEval = {
					lstLate: [], lstFinish : [], listWanrning: [], lstClosed: [], lstUnreSolved: [], lstFail: [],
			}
			do_lc_get_data_evaluation(data, lstEval);
			do_lc_show_evaluation(prj, lstEval, data);
		}

		var do_lc_get_data_evaluation = function(childs, lstEval){
			if(childs && childs.length){
				childs.reduce((curr, child) => {
					if(child.stat == pr_STAT_PRJ_DONE)			curr.lstFinish.push(child);
					if(child.stat == pr_STAT_PRJ_CLOSED)		curr.lstClosed.push(child);

					if(child.stat == pr_STAT_PRJ_FAIL)			curr.lstFail.push(child);
					if(child.stat == pr_STAT_PRJ_UNRESOLVED)	curr.lstUnreSolved.push(child);
					
					if([pr_STAT_PRJ_TODO, pr_STAT_PRJ_INPROGRESS, pr_STAT_PRJ_REVIEW, pr_STAT_PRJ_FAIL].includes(child.stat)){
						let diffDays = req_gl_DayDiff(child.dtEnd);
						if(diffDays < 0){
							curr.lstLate.push(child);
						} else if(diffDays <= 10) {
							curr.listWanrning.push(child);
						}
					}
					return curr;
				}, lstEval)
			}
		}

		var do_lc_show_evaluation = function(prj, lstEval, data){
			const totalTask = prj.tasks?prj.tasks.length : 0;
			const totalEpic = prj.epics?prj.epics.length : 0;
			let total 		= data.length;
			let dataEval = {
					totalTask	, totalEpic						, percentFinish	: total === 0 ? 0 : (lstEval.lstFinish.length + lstEval.lstClosed.length) * 100 / total, percentLate: lstEval.lstLate.length * 100 / total, 
					nbWarning	: lstEval.listWanrning.length	, nbUnreSolved	: lstEval.lstUnreSolved.length,
					typ02 		: prj.typ02 					, isViewSprint  : pr_isViewSprint
			}
			$("#div_prj_evaluation")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_EVALUATION	, dataEval));
			do_lc_bindEvent_resize("#div_prj_evaluation"); 
			
		}

	};

	//------------------------------Start Epic list-----------------------------------
	const PrjProjectEntTabEpic 	= function (grpName, header, content, footer) {
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		const members 				= {};
		
		const pr_ctr_Ent			= App.controller.PrjProject.Ent;
		const pr_member_lev_manager = 0;
		const pr_member_typ_high	= 1;
		//------------------------------Start Epic list-----------------------------------
		this.do_lc_show_prj_epic = function(prj, scrollToTop){
			do_lc_load_view(prj);
			do_lc_show_epic(prj, scrollToTop);
		}
		
		const do_lc_load_view = function(prj){
			members[App.data.user.id] = {"id": App.data.user.id, "lev": pr_member_lev_manager, "typ": pr_member_typ_high};

			$("#div_prj_epic")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_EPIC, {}));
			
			do_lc_bindEvent_header_epic(prj);
			do_lc_bindEvent_resize("#div_prj_epic");
		}

		const do_lc_transform_descr02 = data => {
			if(!data.descr02)	return;
			const list = Object.values(data.descr02).reduce((curr, item) => {
				(item && item.trim().length) && curr.push({item}); return curr;
			}, [])

			data.descr02 = JSON.stringify(list);
		}

		const do_lc_bindEvent_header_epic = prj => {
			const $spanTextFilterEpic = $("#sp_text_filter_epic");

			$("#btn_add_epic, #btn_add_epic_others").off("click").on("click", function(){
				var obj = {files: []}
				App.MsgboxController.do_lc_show({
					title		: $.i18n("prj_project_add_epic_popup"),
					content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_NEW, {epics : prj.epicInf, epicSelected: prj.id,typ02: pr_TYPE02_EPIC, currencys: App.data.currencys, paramStat}),	
					autoclose	: false,
					buttons		: {
						OK: {
							lab		: $.i18n("common_btn_save"),
							funct	: do_lc_save_new_epic,
							param	: [prj, obj],
							autoclose	: false,
							classBtn	: "btn-primary"
						},
						NO: {
							lab		:  $.i18n("common_btn_cancel"),
						}
					},
					bindEvent	: function() {
						App.SummerNoteController.do_lc_show("#div_create_prj", {dialogsInBody: true});
						do_lc_bindEvent_epic_popup_prj(obj);
						do_gl_init_repeater();
					}
				});	
			});

			$("#btn_refresh_epic").off("click").on("click", function() {
				$spanTextFilterEpic.html("");
				$(".epic-filter").css("display", "none");

				do_lc_refresh_epic(prj, prj.grp);
			})

			$("#btn_filter_complete_epic").off("click").on("click", e => {
				const epicCompletes = prj.epics.filter(o => o.stat === pr_STAT_PRJ_DONE) || [];
				do_lc_show_ui_epic(prj, epicCompletes);
				$spanTextFilterEpic.html(" | " + $.i18n("prj_project_epic_search_compete"));
			})

			$("#btn_filter_not_complete_epic").off("click").on("click", e =>  {
				const epicNotCompletes = prj.epics.filter(o => o.stat !== pr_STAT_PRJ_DONE) || [];
				do_lc_show_ui_epic(prj, epicNotCompletes);
				$spanTextFilterEpic.html(" | " + $.i18n("prj_project_epic_search_not_compete"));
			})

			$("#btn_filter_search_epic").off("click").on("click", () => {
				$("#div_search_epic").toggle();
				$spanTextFilterEpic.html(" | " + $.i18n("prj_project_epic_search_inp"));
			});

			$("#inp_filter_epic").off("keyup").on("keyup", function() {
				const searchKey = $(this).val();
				const do_lc_filter_epic = () => {
					const epicSearch = prj.epics.filter(o => o.name.includes(searchKey) || o.descr01.includes(searchKey)) || [];
					do_lc_show_ui_epic(prj, epicSearch);
				}

				do_gl_execute_debounce(do_lc_filter_epic);
			})
		}

		

		const do_lc_show_epic = function(prj, scrollToTop){
			if(!prj.epics)	prj.epics = [];
			prj.epics = prj.epics.map(o =>{
				o.descr01 = o.descr01? o.descr01.substring(0, 100): "";
				return o;
			})

			prj.epics = prj.epicInf ? prj.epicInf.filter(o1 => prj.epics.some(o2 => o1.id === o2.id)) : [];

			do_lc_show_ui_epic(prj, [...prj.epics], scrollToTop);
		}

		const do_lc_show_ui_epic = (prj, epics, scrollToTop) => {
			if(epics.length > 0) do_lc_sort_data(epics);
			$("#tabEpic").html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_EPIC_LIST, epics));
			
			do_lc_bindEvent_epics_prj(prj);
			
			if (scrollToTop)  do_gl_scrollToTop();;
		}

		const do_lc_sort_data = (data) => {
			data.sort(function (a, b) {
				return a.stat - b.stat || (a.name.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "") > b.name.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, ""))*2-1;
			});
		}

		const do_lc_bindEvent_epics_prj = function(prj){
//			$(".td-epic").off("click").on("click", function(){
//				let {id : idEpic} = $(this).data();
//				idEpic && pr_ctr_Main.do_lc_switch_mobile_or_pc(`view_prj_project_content.html?id=${idEpic}`, "VI_MAIN/"+ App.router.part.PRJ_PROJECT_ENT, [idEpic], '_self');
//				return false;
//			});


			$(".span-delete-epic").off("click").on("click", function() {
				const {id, code, stat} = $(this).data();
				if(stat !== pr_STAT_PRJ_NEW)	return;
				if(!id || !code)				return;
				const eIndex = prj.epics.findIndex(e => e.code01 === code);
				if(eIndex != -1 && (prj.epics[eIndex].nbTask > 0 || prj.epics[eIndex].nbEpic > 0)) {
					do_gl_show_Notify_Msg_Error($.i18n("prj_project_epic_del_error"))
					return;
				}

				App.MsgboxController.do_lc_show({
					title		: $.i18n("prj_project_del_epic_popup"),
					content 	: $.i18n("prj_project_del_epic_popup_content"),
					autoclose	: false,
					buttons		: {
						OK: {
							lab			: $.i18n("common_btn_ok"),
							funct		: do_lc_delete_new_epic,
							param		: [id, code, prj],
							autoclose	: false,
							classBtn	: "btn-primary"
						},
						NO: {
							lab		:  $.i18n("common_btn_cancel"),
						}
					}
				});	
			})
			
			$(".span-show-epic").off("click").on("click", function() {
				const {id, code} = $(this).data();
				if(!id || !code) return;
				$(this).addClass('d-none');
				$(`.tab_show_epic_detail[data-id=${id}]`).removeClass('d-none');
				do_lc_show_epic_det(id, code, prj);
			})
			
			$(".span-hide-epic").off("click").on("click", function() {
				const {id} = $(this).data();
				if(!id)							return;
				$(this).addClass('d-none');
				$(`.span-show-epic[data-id=${id}]`).removeClass('d-none');
				$(`.tab_show_epic_detail[data-id=${id}]`).addClass('d-none');
			})
			
			$(".td-epic").droppable({
				hoverClass			: "hoverDrop",
				tolerance			: "pointer",
				drop				: function(event, ui) {
					event.preventDefault();
					do_lc_epic_drop(ui.draggable, this, prj);
				}
			});
			
			App.router.controller.do_lc_binding_route();
		}
		
		const do_lc_epic_drop = (divFrom, divTo, prj) => {
			const {id : idTask} = $(divFrom).data();
			const {id : idEpic} = $(divTo).data();
			if(!idTask || !idEpic){
				do_lc_show_epic_task_prj(prj);
				return;
			}
			
			const task = prj.tasks.find(t => t.id === idTask);
			const epic = prj.epics.find(e => e.id === idEpic);
			
			if(!task || !epic){
				do_lc_show_epic_task_prj(prj);
				return;
			}
			
			App.MsgboxController.do_lc_show({
				title 		: $.i18n("msgbox_confirm_title"),
				content 	: `${$.i18n("prj_task_move_to_epic_01")} <b>${task.name}</b> ${$.i18n("prj_task_move_to_epic_02")} <b>${epic.name}</b> ?`,
				autoclose	: false,
				buttons 	: {
					UPDATE : {
						lab 		: $.i18n("common_btn_ok"),
						funct 		: do_lc_add_task_to_epic,
						param 		: [idTask, idEpic, prj],
						classBtn	: "btn-primary",
					},
					CALCEL : {
						lab 		: $.i18n("common_btn_cancel"),
					}
				}
			});
		}
		
		const do_lc_add_task_to_epic = (idTask, idEpic, prj) => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_MOVE_TASK, {idTask, idEpic, id : prj.id});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_after_add_task_to_epic, [prj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}
		
		const do_lc_after_add_task_to_epic = (sharedJson, prj) => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				let data 	= sharedJson[App['const'].RES_DATA];
				prj.epicInf = data.epicInf;
				prj.epics 	= data.epics;
				prj.tasks 	= data.tasks;
				pr_ctr_Ent.do_lc_show_epic_task(prj);
			}
		}

		const do_lc_show_epic_det = (id, code, prj) => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET, {id, code});			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_get_content_epic, [id]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax_background (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_after_get_content_epic = function(sharedJson, id){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				let data = sharedJson.res_data;
				$(`.tab_show_epic_detail[data-id=${id}]`).html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_EPIC_LIST_SHOW_CHILD, data));
				$(`.span-hide-epic[data-id=${id}]`).removeClass('d-none');
			}else{
				do_gl_show_Notify_Msg_Error($.i18n('common_err_msg_save'));
			}
			App.MsgboxController.do_lc_close();
		}
		
		const do_lc_delete_new_epic = (id, code, prj) => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_DEL, {id, code, prjId: prj.id, prjCode: prj.code01});			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_del_epic, [prj]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_after_del_epic = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				do_gl_show_Notify_Msg_Success($.i18n('common_ok_msg_save'));
				do_lc_refresh_epic(prj, prj.grp);
			}else{
				do_gl_show_Notify_Msg_Error($.i18n('common_err_msg_save'));
			}
			App.MsgboxController.do_lc_close();
		}

		const do_lc_save_new_epic = function(prj, obj){
			$("#btn_msgbox_OK").attr("disabled", "disabled");
			let	data	 	= req_gl_data({
				dataZoneDom		: $("#div_create_prj")
			});

			let $projectdesc = $("#projectdesc");
			if ($projectdesc.summernote('isEmpty')){
				$projectdesc.parent().append("<div class='errMsg'>" + $.i18n("validator_err_required") + "</div>")
			}

			if(data.hasError){
				$("#btn_msgbox_OK").removeAttr("disabled", "disabled");
				return false;
			}

			let objNew 		= data.data;
			objNew.typ02	= pr_TYPE02_EPIC;
			objNew.files	= obj.files;

			objNew.dtBegin 	= do_lc_convert_date(objNew.dtBegin);
			objNew.dtEnd 	= do_lc_convert_date(objNew.dtEnd);

			do_lc_transform_descr02(objNew);

			do_lc_create_epic(objNew, prj);
		}

		const do_lc_convert_date = objDate => {
			return objDate.date.substr(0, 10) + " " + objDate.time.substr(0, 5) + ":00";
		};

		const do_lc_create_epic = function(obj, prj){
			let ref 		= req_gl_Request_Content_Send(pr_SERVICE_CLASS, pr_SV_NEW);			
			ref["obj"]		= JSON.stringify(obj);
			ref["member"]	= JSON.stringify(Object.values(members));

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_create_epic, [prj]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_after_create_epic = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				do_gl_show_Notify_Msg_Success 	($.i18n('common_ok_msg_save'));
				let data = sharedJson[App['const'].RES_DATA];
				data.descr01 = data.descr01.substring(0, 100);
				prj.epics.push(data);
				do_lc_show_ui_epic(prj, prj.epics);
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
			App.MsgboxController.do_lc_close();
		}

		const do_lc_bindEvent_epic_popup_prj = function(obj){
			let option_ava		= {
					fileinput	: {maxFiles : 1, param : {typ01: 1, typ02: 1} },//option here
					obj			: obj//file existing here
			}
			do_gl_init_fileDropzone($("#div_create_prj #div_prj_avatar"), option_ava);

			let option = {
					fileinput		: {
						parallelUploads:10,
			            uploadMultiple:true,
					},//option here
					obj				: obj//file existing here
			}
			do_gl_init_fileDropzone($("#div_create_prj #div_prj_docs"), option);
			do_lc_req_autocompleteEpicTask();
			
			let now = new Date();
			let tomorrow =  new Date();
			tomorrow.setDate((new Date()).getDate() + 1);
			let hours = now.getHours();
			let minutes = now.getMinutes();

			$("#dtpicker_Begin").datepicker().datepicker("setDate", now);
			$("#dtpicker_End").datepicker().datepicker("setDate", tomorrow);
			
			$("#tmpicker_Begin").timepicker({//timepicker
				showMeridian: false,
				defaultTime :`${hours}:${minutes}`,
				icons		: {
					up		: "mdi mdi-chevron-up",
					down	: "mdi mdi-chevron-down"
				}
			});
			
			$("#tmpicker_End").timepicker({//timepicker
				showMeridian: false,
				defaultTime :`${hours}:${minutes}`,
				icons		: {
					up		: "mdi mdi-chevron-up",
					down	: "mdi mdi-chevron-down"
				}
			});

			do_lc_bind_event_dtInput()
		}

		const do_lc_bind_event_dtInput = () => {
			$( "#dtpicker_Begin" ).off("change").on("change", function() {
				const sDate = $(this).val()
				const eDate = $( "#dtpicker_End" ).val()

				if(eDate < sDate) $( "#dtpicker_End" ).val(sDate)
			})
			$("#tmpicker_Begin").off("change").on("change", function() {
				const sDate = $( "#dtpicker_Begin" ).val()
				const eDate = $( "#dtpicker_End" ).val()

				if(eDate != sDate) return;
				
				let sTimeArr = $(this).val().split(":")
				let eTimeArr = $( "#tmpicker_End" ).val().split(":")

				if(sTimeArr.length <= 0 || eTimeArr.length <= 0) return

				const sHour = +sTimeArr[0]
				const sMinutes = +sTimeArr[1]
				const eHour = +eTimeArr[0]
				const eMinutes = +eTimeArr[1]

				const sMinutesStr = sMinutes < 10 ? `0${sMinutes}` : sMinutes

				if(eHour < sHour) $( "#tmpicker_End" ).val(`${sHour}:${sMinutesStr}`)
				if(sHour == eHour && eMinutes < sMinutes) $( "#tmpicker_End" ).val(`${sHour}:${sMinutesStr}`)
			})

			
			$( "#dtpicker_End" ).off("change").on("change", function() {
				const eDate = $(this).val()
				const sDate = $( "#dtpicker_Begin" ).val()

				if(eDate < sDate) $( "#dtpicker_Begin" ).val(eDate)
			})
			$("#tmpicker_End").off("change").on("change", function() {
				const sDate = $( "#dtpicker_Begin" ).val()
				const eDate = $( "#dtpicker_End" ).val()

				if(eDate != sDate) return;
				
				let sTimeArr = $( "#tmpicker_Begin" ).val().split(":")
				let eTimeArr = $(this).val().split(":")

				if(sTimeArr.length <= 0 || eTimeArr.length <= 0) return

				const sHour = +sTimeArr[0]
				const sMinutes = +sTimeArr[1]
				const eHour = +eTimeArr[0]
				const eMinutes = +eTimeArr[1]

				const eMinutesStr = eMinutes < 10 ? `0${eMinutes}` : eMinutes

				if(eHour < sHour) $( "#tmpicker_Begin" ).val(`${eHour}:${eMinutesStr}`)
				if(sHour == eHour && eMinutes < sMinutes) $( "#tmpicker_Begin" ).val(`${sHour}:${eMinutesStr}`)
			})
		}

		const do_lc_req_autocompleteEpicTask = function(){
			let el = ".inp-name-member";
			let reqSelectMember = function(event, item){
				if(members[item.id])			return false;
				let lev 			= $("#div_create_prj #sel_member_level").val();
				let typ 			= $("#div_create_prj #sel_member_type").val();
				let user 			= {"id": item.id, "lev": +lev, "typ": +typ};

				let textColor   = null;
				let textAvatar  = null
				if(!item.avatar){
					let first = item.login01.charAt(0);
					let last  = item.login01.charAt(item.login01.length - 1);
					let index = var_gl_alphabet.indexOf(first.toLowerCase());
					
					textColor = var_gl_colors[index];
					textAvatar= first + last;
				}
				
				members[item.id] 	= user;

				let selOpt 			= `<div class='member-item'>`;
				if(item.avatar) selOpt 			+= `<div><img src='${ item.avatar.urlPrev}' class='rounded-circle avatar-xs' alt=""/> ${item.login01}`;
				else 			selOpt 			+= `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-white mr-1 text-uppercase text-center" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div> ${item.login01}`;
				selOpt 				+= `<a data-id='${item.id}' class='text-danger btn-remove-member' data-toggle='tooltip' data-placement='top' title='' data-original-title='Delete'><i class='mdi mdi-close font-size-18'></i></a>`;
				selOpt 				+= `</div></div>`;

				$("#div_list_member").append(selOpt);
				do_lc_bindEvent_autocompleteEpicTask();
				$(el).blur().val("");
			}

			let options = {
					dataService : [pr_SERVICE_PER_CLASS, pr_SV_USER_SEARCH], 
					fSelect		: reqSelectMember, 
					appendTo	: ".msg-box", 
					customShowList: do_lc_customLst_user_autocomplete
			}
			do_gl_req_autocompleteNew(el, options);
		}

		const do_lc_bindEvent_autocompleteEpicTask = function(){
			$(".btn-remove-member").off("click").on("click", function(){
				let $this 	= $(this);
				let parent 	= $this.parent();
				let {id} 	= $this.data();

				if(members[id])	delete members[id];
				parent.remove();
			})
		}

		const do_lc_customLst_user_autocomplete = function(item, selOpt = ""){
			if(item.avatar) return selOpt 		+= `<img src='${ item.avatar.urlPrev}' class='rounded-circle avatar-xs' alt=""/> ${item.login01}`;
			if(!item.avatar){
				let textColor   = null;
				let textAvatar  = null
				if(!item.avatar){
					let first = item.login01.charAt(0);
					let last  = item.login01.charAt(item.login01.length - 1);
					let index = var_gl_alphabet.indexOf(first.toLowerCase());
					
					textColor = var_gl_colors[index];
					textAvatar= first + last;
				}
				selOpt 			+= `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-white text-uppercase text-center mr-1" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div> ${item.login01}</div>`;
				return selOpt;
			}
		}

		const do_lc_refresh_epic = function(prj, prjId){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_REFRESH_EPIC, {id: prj.id, code: prj.code01});			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_refresh_epic, [prj]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_after_refresh_epic = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {

				prj.epicInf = sharedJson[App['const'].RES_DATA];
				do_lc_show_epic(prj);
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}
		//------------------------------End Epic list-----------------------------------
	}
	//------------------------------End Epic list-----------------------------------

	//------------------------------Start Task Task-----------------------------------
	const PrjProjectEntTabTask 	= function (grpName, header, content, footer) {
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		//------------------------------Start Task list-----------------------------------
		const self 			= this;
		const members 		= {};
		const groups 		= {};
	
		var pr_isViewSprint = false;
		
		var pr_desrc02      = {
				task_ids : []
		};
		
		const pr_member_lev_manager 	= 0;
		const pr_member_lev_reporter 	= 1;
		const pr_member_lev_worker 		= 2;
	
		const pr_member_typ_low 		= 0;
		const pr_member_typ_high 		= 1;
		
		this.do_lc_show_prj_task = function(prj, isViewSprint, scrollToTop){
			if(isViewSprint) pr_isViewSprint = isViewSprint;
			do_lc_load_view(prj);
			
			do_lc_show_task(prj, scrollToTop);
		}
		
		const do_lc_load_view = function(prj){
	
			members[App.data.user.id] = {"id": App.data.user.id, "lev": pr_member_lev_manager, "typ": pr_member_typ_high};
	
			$("#div_prj_task")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_TASK, {isViewSprint : pr_isViewSprint}));
			
			if(pr_isViewSprint){
				if (prj.userRole && typeof prj.userRole === "object") {
					if (!Object.values(prj.userRole).includes(pr_member_lev_manager)) {
						$("#inp_add_task").hide();
					}
				} else if (prj.userRole != pr_member_lev_manager) {
					$("#inp_add_task").hide();
				}
			}
			
			do_lc_bindEvent_header_task(prj);
			do_lc_bindEvent_resize("#div_prj_task");
		}
	
		const do_lc_transform_descr02 = data => {
			if(!data.descr02)	return;
			const list = Object.values(data.descr02).reduce((curr, item) => {
				(item && item.trim().length) && curr.push({item}); return curr;
			}, [])
	
			data.descr02 = JSON.stringify(list);
		}
	
		const do_lc_bindEvent_header_task = prj => {
			const $spanTextFilter = $("#sp_text_filter");
	
			$("#btn_add_task, #btn_add_task_others").off("click").on("click", function(){
				var obj = {files: []};
				App.MsgboxController.do_lc_show({
					title		: $.i18n("prj_project_add_task_popup"),
					content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_NEW, {epics : prj.epicInf, epicSelected: prj.id, typ02: pr_TYPE02_TASK, currencys: App.data.currencys, paramStat}),
					autoclose	: false,
					buttons		: {
						OK: {
							lab		: $.i18n("common_btn_save"),
							funct	: do_lc_save_new_task,
							param	: [prj, obj],
							autoclose	: false,
							classBtn	: "btn-primary"
						},
						NO: {
							lab		:  $.i18n("common_btn_cancel"),
						}
					},
					bindEvent: function() {
						App.SummerNoteController.do_lc_show("#div_create_prj", {dialogsInBody: true});
						do_lc_bindEvent_task_popup_prj(obj);
						do_gl_init_repeater();
					}
				});	
			});
	
			$("#btn_refresh_task").off("click").on("click", function() {
				$spanTextFilter.html("");
				$(".task-filter").css("display", "none");
				do_lc_refresh_task(prj, prj.id);
			})
	
			$("#btn_filter_complete_task").off("click").on("click", e => {
				const taskCompletes = prj.tasks.filter(o => o.stat === pr_STAT_PRJ_DONE) || [];
				do_lc_show_ui_task(prj, taskCompletes);
				$spanTextFilter.html(" | " + $.i18n("prj_project_task_search_compete"));
			})
	
			$("#btn_filter_not_complete_task").off("click").on("click", e =>  {
				const taskNotCompletes = prj.tasks.filter(o => o.stat !== pr_STAT_PRJ_DONE) || [];
				do_lc_show_ui_task(prj, taskNotCompletes);
				$spanTextFilter.html(" | " + $.i18n("prj_project_task_search_not_compete"));
			})
	
			$("#btn_filter_search_task").off("click").on("click", () => {
				$("#div_search_task").toggle();
				$spanTextFilter.html(" | " + $.i18n("prj_project_task_search_inp"));
			});
	
			$("#inp_filter_task").off("keyup").on("keyup", function() {
				const searchKey = $(this).val();
				const do_lc_filter_task = () => {
					const taskSearch = prj.tasks.filter(o => o.name.includes(searchKey) || o.descr01.includes(searchKey)) || [];
					do_lc_show_ui_task(prj, taskSearch);
				}
	
				do_gl_execute_debounce(do_lc_filter_task);
			})
			
			if(pr_isViewSprint){
				do_lc_req_autocomplete(prj);
				
				if(prj.tasks){
					pr_desrc02["task_ids"] = prj.tasks.map(item => item.id);
				}
				
				$("#btn_refresh_task_sprint").off("click").on("click", function() {
					do_lc_refresh_task_sprint(prj, prj.id);
				})
	
				
				$("#a_btn_save_task_sprint").off("click").on("click", e => {
					prj.descr02 = JSON.stringify(pr_desrc02);
					do_lc_save_list_task(prj);
				})
				
				$("#a_btn_cancel_task_sprint").off("click").on("click", e => {
					do_lc_show_task(prj);
					$("#a_btn_save_task_sprint, #a_btn_cancel_task_sprint").addClass("hide");
				})
			
			}
		}
		
		const do_lc_req_autocomplete = (prj) => {
			let el = "#inp_add_task";
			let customShowList = function(item, selOpt = ""){
				return selOpt = item.name;
			}
	
			let reqSelectMember = (event, item) => {
				if(pr_desrc02["task_ids"].some(task => task == item.id))			return false;
	
				pr_desrc02["task_ids"].push(item.id);
	
				if(($("#tabTask").find("#tbody_task").length) == 0) {
					$("#tabTask").html("");
					let html = `   <table class="table table-centered"><tbody id="tbody_task"></tbody></table>`;
					$("#tabTask").html(html);
				}
				$("#tbody_task").append(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_TASK_LIST_ELEMENT, item));
				$(el).blur().val("");
				$("#a_btn_save_task_sprint, #a_btn_cancel_task_sprint").removeClass("hide");
			}
	
			let options = {
					dataService : [pr_SERVICE_CLASS, "SVTaskListSearch"], 
					fSelect: reqSelectMember, 
					customShowList, 
					grId: prj.grp
			}
			do_gl_req_autocompleteNew(el, options);
		}
		
		const do_lc_save_list_task = (prj) => {
			
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_SAVE_CONTENT, {obj: JSON.stringify(prj)});	
	
			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterSave_task_sprint, [prj]));
	
			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
	
			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}
	
		var do_lc_afterSave_task_sprint = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_lc_refresh_task_sprint(prj, prj.id);
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get'));
			}
		}
		
		const do_lc_refresh_task_sprint = function(prj, prjId){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_REFRESH_TASK_SPRINT, {id: prjId, code:prj.code01});			
	
			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_refresh_task_sprint, [prj]));	
	
			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	
	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}
	
		const do_lc_after_refresh_task_sprint = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				let data = sharedJson[App['const'].RES_DATA];
				prj.tasks = data;
				do_lc_show_task(prj);
				$("#a_btn_save_task_sprint, #a_btn_cancel_task_sprint").addClass("hide");
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}
		
	
		const do_lc_show_task = function(prj, scrollToTop){
			if(prj.tasks){
				for(var i=0; i < prj.tasks.length; i++){
					if(prj.tasks[i].descr01) {
						prj.tasks[i].descr01 = prj.tasks[i].descr01.substring(0, 200);
					}
					prj.tasks[i].isViewSprint = pr_isViewSprint;
					
					if(pr_isViewSprint){
						prj.tasks[i].isManager = prj.userRole === pr_member_lev_manager;
					}
				}
			} else prj.tasks = [];
	
			do_lc_show_ui_task(prj, [...prj.tasks], scrollToTop);
		}
	
		const do_lc_show_ui_task = (prj, tasks, scrollToTop) => {
	
			if(tasks.length > 0) do_lc_sort_data(tasks);
	
			$("#tabTask").html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_TASK_LIST, tasks));
			
			do_lc_bindEvent_tasks_prj(prj);
			
			if (scrollToTop)  do_gl_scrollToTop();;
		}
	
		const do_lc_sort_data = (data) => {
			data.sort(function (a, b) {
				return a.stat - b.stat || (a.name.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "") > b.name.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, ""))*2-1;
			});
		}
	
		const do_lc_bindEvent_tasks_prj = function(prj){
			$(".span-delete-task").off("click").on("click", function() {
				const {id, code, stat} = $(this).data();
				if(stat !== pr_STAT_PRJ_NEW)	return;
				if(!id || !code)				return;
	
				App.MsgboxController.do_lc_show({
					title		: $.i18n("prj_project_del_task_popup"),
					content 	: $.i18n("prj_project_del_task_popup_content"),
					autoclose	: false,
					buttons		: {
						OK: {
							lab			: $.i18n("common_btn_ok"),
							funct		: do_lc_delete_new_task,
							param		: [id, code, prj],
							autoclose	: false,
							classBtn	: "btn-primary"
						},
						NO: {
							lab		:  $.i18n("common_btn_cancel"),
						}
					}
				});	
			})
			
			$(".remove-task").off("click").on("click", function(){
				let $this 					= $(this);
				let {id, stat: statFrom} 	= $this.data();
				if(id){
					let params = {id, statFrom,  statTo: pr_STAT_PRJ_FAIL};
	
					if (statFrom != pr_STAT_PRJ_NEW || statFrom != pr_STAT_PRJ_UNRESOLVED) {
						do_gl_show_Notify_Msg_Error ($.i18n('prj_cannot_delete_task') );
						return;
					}
					do_lc_save_change_stat(params, prj);
				}
			})
			
			$(".remove-task_sprint").off("click").on("click", function(){
				let $this 					= $(this);
				let {id, stat: statFrom} 	= $this.data();
				if(id){
					let index = pr_desrc02["task_ids"].indexOf(id);
					pr_desrc02["task_ids"].splice(index, 1);
					$("#tbody_task").find("tr[data-id='"+ id +"']").remove();
					$("#a_btn_save_task_sprint, #a_btn_cancel_task_sprint").removeClass("hide");
				}
			})
			
			$(".complete-task").off("click").on("click", function() {
				let $this 					= $(this);
				let {id, stat: statFrom} 	= $this.data();
				if(id){
					let params = {id, statFrom,  statTo: pr_STAT_PRJ_DONE};
					do_lc_save_change_stat(params, prj);
				}
			})
			
			$(".close-task").off("click").on("click", function() {
				let $this 					= $(this);
				let {id, stat: statFrom} 	= $this.data();
				if(id){
					const params = {id, statFrom,  statTo: pr_STAT_PRJ_CLOSED};
					do_lc_save_change_stat(params, prj);
				}
			})
			
			$(".td-task").draggable({
				cursor				: "move",
				revert				: true,
				containment			: "document",
				distance			: 50,
				appendTo 			: "#tabEpic",
				helper				: 'clone',
				opacity				: 0.70,
				zIndex				: 10000,
			});
	
			App.router.controller.do_lc_binding_route();
		}
		
		const do_lc_save_change_stat = (params, prj) => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_SAVE_MOVE, params);	
	
			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_after_change_stat, [params, prj]));
	
			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}
		
		const do_lc_after_change_stat = (sharedJson, params, prj) => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_gl_show_Notify_Msg_Success($.i18n('common_ok_msg_save'));
				const {id, statTo} = params;
				
				if(prj.tasks){
					const taskIndex = prj.tasks.findIndex(t => t.id === id);
					if(taskIndex > -1){
						prj.tasks[taskIndex].stat = statTo;
						do_lc_show_ui_task(prj, [...prj.tasks]);
					}
				}
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}
	
		const do_lc_delete_new_task = (id, code, prj) => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_DEL, {id, code, prjId: prj.id, prjCode: prj.code01});			
	
			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_del_task, [prj]));	
	
			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	
	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}
	
		const do_lc_after_del_task = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				do_gl_show_Notify_Msg_Success($.i18n('common_ok_msg_save'));
				do_lc_refresh_task(prj, prj.id);
			}else{
				do_gl_show_Notify_Msg_Error($.i18n('common_err_msg_save'));
			}
			App.MsgboxController.do_lc_close();
		}
	
		const do_lc_save_new_task = function(prj, obj){
			$("#btn_msgbox_OK").attr("disabled", "disabled");
			let	data	 		= req_gl_data({
				dataZoneDom		: $("#div_create_prj")
			});
	
			let $projectdesc = $("#projectdesc");
			if ($projectdesc.summernote('isEmpty')){
				$projectdesc.parent().append("<div class='errMsg'>" + $.i18n("validator_err_required") + "</div>")
			}
	
			if(data.hasError){
				$("#btn_msgbox_OK").removeAttr("disabled", "disabled");
				return false;
			}
	
			let objNew 		= data.data;
			objNew.parent 	= prj.id;
			objNew.typ02	= pr_TYPE02_TASK;
			objNew.files	= obj.files;
	
			objNew.dtBegin 	= do_lc_convert_date(objNew.dtBegin);
			objNew.dtEnd 	= do_lc_convert_date(objNew.dtEnd);
	
			do_lc_transform_descr02(objNew);
	
			do_lc_create_task(objNew, prj);
		}
	
		const do_lc_convert_date = objDate => objDate.date.substr(0, 10) + " " + objDate.time.substr(0, 5) + ":00";
	
		const do_lc_create_task = function(obj, prj){
			let ref 		= req_gl_Request_Content_Send(pr_SERVICE_CLASS, pr_SV_NEW);			
			ref["obj"]		= JSON.stringify(obj);
			ref["member"]	= JSON.stringify(Object.values(members));
			ref["group"]	= JSON.stringify(Object.values(groups));
	
			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_create_task, [prj]));	
	
			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	
	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}
	
		const do_lc_after_create_task = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				do_gl_show_Notify_Msg_Success 	($.i18n('common_ok_msg_save'));
				let data = sharedJson[App['const'].RES_DATA];
				if(data.parent == prj.id){
					data.descr01 = data.descr01.substring(0, 100);
					prj.tasks.push(data);
					do_lc_show_ui_task(prj, prj.tasks);
				}
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
			App.MsgboxController.do_lc_close();
		}
	
		const do_lc_bindEvent_task_popup_prj = function(obj){
	
			let option_ava		= {
					fileinput	: {maxFiles : 1, param : {typ01: 1, typ02: 1} },//option here
					obj			: obj//file existing here
			}
			do_gl_init_fileDropzone($("#div_create_prj #div_prj_avatar"), option_ava);
	
			let option = {
					fileinput		: {
						parallelUploads:10,
			            uploadMultiple:true,
					},//option here
					obj				: obj//file existing here
			}
			do_gl_init_fileDropzone($("#div_create_prj #div_prj_docs"), option);
			do_lc_req_autocompleteEpicTask();
	
	//			$("#div_create_prj .tmpicker").timepicker({//timepicker
	//				showMeridian: false,
	//				icons		: {
	//					up		: "mdi mdi-chevron-up",
	//					down	: "mdi mdi-chevron-down"
	//				}
	//			})
	
			let now = new Date();
			let tomorrow =  new Date();
			tomorrow.setDate((new Date()).getDate() + 1);
			let hours = now.getHours();
			let minutes = now.getMinutes();
			
			$("#dtpicker_Begin").datepicker().datepicker("setDate", now);
			$("#dtpicker_End").datepicker().datepicker("setDate", tomorrow);
			
			$("#tmpicker_Begin").timepicker({//timepicker
				showMeridian: false,
				defaultTime :`${hours}:${minutes}`,
				icons		: {
					up		: "mdi mdi-chevron-up",
					down	: "mdi mdi-chevron-down"
				}
			});
			
			$("#tmpicker_End").timepicker({//timepicker
				showMeridian: false,
				defaultTime :`${hours}:${minutes}`,
				icons		: {
					up		: "mdi mdi-chevron-up",
					down	: "mdi mdi-chevron-down"
				}
			});

			do_lc_bind_event_dtInput()
		}

		const do_lc_bind_event_dtInput = () => {
			$( "#dtpicker_Begin" ).off("change").on("change", function() {
				const sDate = $(this).val()
				const eDate = $( "#dtpicker_End" ).val()

				if(eDate < sDate) $( "#dtpicker_End" ).val(sDate)
			})
			$("#tmpicker_Begin").off("change").on("change", function() {
				const sDate = $( "#dtpicker_Begin" ).val()
				const eDate = $( "#dtpicker_End" ).val()

				if(eDate != sDate) return;
				
				let sTimeArr = $(this).val().split(":")
				let eTimeArr = $( "#tmpicker_End" ).val().split(":")

				if(sTimeArr.length <= 0 || eTimeArr.length <= 0) return

				const sHour = +sTimeArr[0]
				const sMinutes = +sTimeArr[1]
				const eHour = +eTimeArr[0]
				const eMinutes = +eTimeArr[1]

				const sMinutesStr = sMinutes < 10 ? `0${sMinutes}` : sMinutes

				if(eHour < sHour) $( "#tmpicker_End" ).val(`${sHour}:${sMinutesStr}`)
				if(sHour == eHour && eMinutes < sMinutes) $( "#tmpicker_End" ).val(`${sHour}:${sMinutesStr}`)
			})

			
			$( "#dtpicker_End" ).off("change").on("change", function() {
				const eDate = $(this).val()
				const sDate = $( "#dtpicker_Begin" ).val()

				if(eDate < sDate) $( "#dtpicker_Begin" ).val(eDate)
			})
			$("#tmpicker_End").off("change").on("change", function() {
				const sDate = $( "#dtpicker_Begin" ).val()
				const eDate = $( "#dtpicker_End" ).val()

				if(eDate != sDate) return;
				
				let sTimeArr = $( "#tmpicker_Begin" ).val().split(":")
				let eTimeArr = $(this).val().split(":")

				if(sTimeArr.length <= 0 || eTimeArr.length <= 0) return

				const sHour = +sTimeArr[0]
				const sMinutes = +sTimeArr[1]
				const eHour = +eTimeArr[0]
				const eMinutes = +eTimeArr[1]

				const eMinutesStr = eMinutes < 10 ? `0${eMinutes}` : eMinutes

				if(eHour < sHour) $( "#tmpicker_Begin" ).val(`${eHour}:${eMinutesStr}`)
				if(sHour == eHour && eMinutes < sMinutes) $( "#tmpicker_Begin" ).val(`${sHour}:${eMinutesStr}`)
			})
		}
	
		const do_lc_req_autocompleteEpicTask = function(){
			let el = ".inp-name-member";
			let reqSelectMember = function(event, item){
				if(members[item.id])			return false;
				let lev 			= $("#div_create_prj #sel_member_level").val();
				let typ 			= $("#div_create_prj #sel_member_type").val();
				let user 			= {"id": item.id, "lev": +lev, "typ": +typ};
	
				let textColor   = null;
				let textAvatar  = null
				if(!item.avatar){
					let first = item.login01.charAt(0);
					let last  = item.login01.charAt(item.login01.length - 1);
					let index = var_gl_alphabet.indexOf(first.toLowerCase());
					
					textColor = var_gl_colors[index];
					textAvatar= first + last;
				}
				
				members[item.id] 	= user;
	
				let selOpt 			= `<div class='member-item'>`;
				if(item.avatar) selOpt 			+= `<div><img src='${ item.avatar.urlPrev}' class='rounded-circle avatar-xs'/> ${item.login01}`;
				else 			selOpt 			+= `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-white mr-1 text-uppercase text-center" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div> ${item.login01}`;
				selOpt 				+= `<a data-id='${item.id}' class='text-danger btn-remove-member' data-toggle='tooltip' data-placement='top' title='' data-original-title='Delete'><i class='mdi mdi-close font-size-18'></i></a>`;
				selOpt 				+= `</div></div>`;
				
				$("#div_list_member").append(selOpt);
				do_lc_bindEvent_autocompleteEpicTask();
				$(el).blur().val("");
			}
	
			let options = {
					dataService : [pr_SERVICE_PER_CLASS, pr_SV_USER_SEARCH], fSelect: reqSelectMember, appendTo: ".msg-box", customShowList: do_lc_customLst_user_autocomplete
			}
			do_gl_req_autocompleteNew(el, options);
			
			
			let elG = ".inp-name-group";
			let customShowListGroup = function(item, selOpt = ""){
				let name = ""
				if(!item.name)	name = "A";
				else name =  item.name.trim().substr(0,1).toUpperCase();
				 
				if(!item.val01){
					selOpt 		+= `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-primary text-uppercase bg-soft-primary text-center mr-2"><div class="text-middle">${name}</div></div>${item.name}</div>`;
				}else{
					item.val01 = JSON.parse(item.val01);
					if(!item.val01.img) selOpt 		+=  `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-primary text-uppercase bg-soft-primary text-center mr-2"><div class="text-middle">${name}</div></div>${item.name}</div>`;
					else selOpt 		+= `<div class="media align-items-center"><img src='${item.val01.img}' class='rounded-circle avatar-xs mr-2'/>${item.name}</div>`;
				}
				return selOpt;
			}
	
			let reqSelectGroup = (event, item) => {
				if(groups[item.id])			return false;
	
				let typ 		= $("#sel_group_type").val();
				let mem 		= {"typ": +typ, "ent02": item, "entId02": item.id, "entTyp02": 40000};
				
				let name = ""
				if(!item.name)	name = "A";
				else name =  item.name.trim().substr(0,1).toUpperCase();
	
				let strName = item.name.length > 10?item.name.substr(0, 10) + "..." : item.name;
				
				groups[item.id] = mem;
				let selOpt 		= `<div class='member-item'>`;
				if(!item.val01) selOpt 			+= `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-primary text-uppercase bg-soft-primary text-center mr-2"><div class="text-middle">${name}</div></div> ${strName}`;
				else{
					if(!item.val01.img) selOpt 	+= `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-primary text-uppercase bg-soft-primary text-center mr-2"><div class="text-middle">${name}</div></div> ${strName}`;
					else                selOpt 	+= `<div class="media align-items-center"><img src='${item.val01.img}' class='rounded-circle avatar-xs mr-1' alt=''/> ${strName}`;
				}
				selOpt 			+= `<a data-id='${item.id}' class='text-danger btn-remove-group' data-toggle='tooltip' data-placement='top' title='' data-original-title='Delete'><i class='mdi mdi-close font-size-18'></i></a>`;
				selOpt 				+= `</div></div>`;
				
				$("#div_list_group").append(selOpt);
				$(elG).blur().val("");
				do_lc_bindEvent_autocomplete_group();
			}
	
			let optionsG = {
				dataService 	: [pr_SERVICE_GROUP_CLASS, pr_SV_GROUP_SEARCH],
				apiUrl			: App.path.BASE_URL_API_PRIV,
				dataRes 		: ["name"],
				dataReq			: {nbLine:5, typ01s: 300},
				selectCallback	: reqSelectGroup,
			}
			do_gl_set_input_autocomplete(elG, optionsG);
		}
	
		const do_lc_bindEvent_autocompleteEpicTask = function(){
			$(".btn-remove-member").off("click").on("click", function(){
				let $this 	= $(this);
				let parent 	= $this.parent();
				let {id} 	= $this.data();
	
				if(members[id])	delete members[id];
				parent.remove();
			})
		}
		
		var do_lc_bindEvent_autocomplete_group = function(){
			$(".btn-remove-group").off("click").on("click", function(){
				let $this 		= $(this);
				let parent 	    = $this.parent();
				let {id} 		= $this.data();
	
				if(groups[id])	delete groups[id];
				parent.remove();
			})
		}
	
		const do_lc_customLst_user_autocomplete = function(item, selOpt = ""){
			if(item.avatar) return selOpt 		+= `<img src='${ item.avatar.urlPrev}' class='rounded-circle avatar-xs'/> ${item.login01}`;
			if(!item.avatar){
				let textColor   = null;
				let textAvatar  = null
				if(!item.avatar){
					let first = item.login01.charAt(0);
					let last  = item.login01.charAt(item.login01.length - 1);
					let index = var_gl_alphabet.indexOf(first.toLowerCase());
					
					textColor = var_gl_colors[index];
					textAvatar= first + last;
				}
				selOpt 			+= `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-white text-uppercase text-center mr-1" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div> ${item.login01}</div>`;
				return selOpt;
			}
		}
	
		const do_lc_refresh_task = function(prj, prjId){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_REFRESH_TASK, {id: prjId, code: prj.code01, typ02: prj.typ02});			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_refresh_task, [prj]));	
	
			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	
	
			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}
	
		const do_lc_after_refresh_task = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				let data = sharedJson[App['const'].RES_DATA];
				prj.tasks = data;
				do_lc_show_task(prj);
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}
		//------------------------------End Task list-----------------------------------
	}
	//------------------------------End Task list-----------------------------------

	//------------------------------Start Task Task-----------------------------------
	const PrjProjectEntTabReport 	= function (grpName, header, content, footer) {
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		
		//------------------------------Start Task list-----------------------------------
		const self 			= this;
		const members 		= {};
		const groups 		= {};

		var pr_isViewSprint = false;
		
		var pr_desrc02      = {
			task_ids : []
		};
		
		const pr_member_lev_manager 	= 0;
		const pr_member_lev_reporter 	= 1;
		const pr_member_lev_worker 		= 2;

		const pr_TYP_01_REPORT			= 1000;

		const pr_NUMBER_RECORD			= 10;

		const pr_Tab_Report				= "#tabReport"
		const pr_Tab_Report_Pag			= "#tabReportPagination"

		this.do_lc_show_prj_report = (prj, isViewSprint, scrollToTop) => {
			if(isViewSprint) pr_isViewSprint = isViewSprint;

			do_lc_load_view(prj);
			do_lc_get_report(prj, false);
		}
		
		const do_lc_load_view = (prj) => {
			$("#div_prj_report")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_REPORT, {isViewSprint : pr_isViewSprint}));
			
			if(pr_isViewSprint){
				if(prj.userRole != pr_member_lev_manager) $("#inp_add_report").hide();
			}

			do_lc_bindEvent_resize("#div_prj_report");
		}

		const do_lc_bindEvent_header_report = (prj) => {
			const $spanTextFilter = $("#sp_text_filter");

			$("#btn_add_report, #btn_add_report_others").off("click").on("click", function(){
				var obj = {files: []};
				App.MsgboxController.do_lc_show({
					title		: $.i18n("prj_project_add_report_popup"),
					content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_REPORT_NEW, {}),
					autoclose	: false,
					buttons		: {
						OK: {
							lab		: $.i18n("common_btn_save"),
							funct	: do_lc_save_new_report,
							param	: [prj, obj],
							autoclose	: false,
							classBtn	: "btn-primary"
						},
						NO: {
							lab		:  $.i18n("common_btn_cancel"),
						}
					},
					bindEvent: function() {
						App.SummerNoteController.do_lc_show("#div_create_prj", {dialogsInBody: true});
						do_lc_bindEvent_report_popup_prj(obj);
						do_gl_init_repeater();
					}
				});	
			});

			$("#btn_refresh_report").off("click").on("click", function() {
				$spanTextFilter.html("");
				$(".task-filter").css("display", "none");
				do_lc_get_report(prj, true);
			})

			$("#btn_filter_search_report").off("click").on("click", () => {
				$("#div_search_report").toggle();
				$spanTextFilter.html(" | " + $.i18n("prj_project_report_search_inp"));
			});

			$("#inp_filter_report").off("keyup").on("keyup", function() {
				const do_lc_filter_report = () => {
					do_lc_get_report(prj, false)
					// const reportSearch = prj.lstReport.filter(o => o.inf01.includes(searchKey) || o.inf02.includes(searchKey)) || [];
					// do_lc_show_ui_report(prj, reportSearch);
				}

				do_gl_execute_debounce(do_lc_filter_report);
			})
			
			if(pr_isViewSprint){
				do_lc_req_autocomplete(prj);
				
				if(prj.lstReport){
					pr_desrc02["task_ids"] = prj.lstReport.map(item => item.id);
				}
				
				$("#btn_refresh_task_sprint").off("click").on("click", function() {
					do_lc_refresh_report_sprint(prj, prj.id);
				})

				
				$("#a_btn_save_report_sprint").off("click").on("click", e => {
					prj.descr02 = JSON.stringify(pr_desrc02);
					do_lc_save_list_report(prj);
				})
				
				$("#a_btn_cancel_report_sprint").off("click").on("click", e => {
					do_lc_show_report(prj);
					$("#a_btn_save_report_sprint, #a_btn_cancel_report_sprint").addClass("hide");
				})
			
			}
		}
		
		const do_lc_req_autocomplete = (prj) => {
			let el = "#inp_add_report";
			let customShowList = function(item, selOpt = ""){
				return selOpt = item.name;
			}

			let reqSelectMember = (event, item) => {
				if(pr_desrc02["task_ids"].some(task => task == item.id))			return false;

				pr_desrc02["task_ids"].push(item.id);

				if(($("#tabReport").find("#tbody_task").length) == 0) {
					$("#tabReport").html("");
					let html = `   <table class="table table-centered"><tbody id="tbody_task"></tbody></table>`;
					$("#tabReport").html(html);
				}
				$("#tbody_report").append(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_REPORT_LIST_ELEMENT, item));
				$(el).blur().val("");
				$("#a_btn_save_report_sprint, #a_btn_cancel_report_sprint").removeClass("hide");
			}

			let options = {
					dataService : [pr_SERVICE_CLASS, "SVTaskListSearch"], 
					fSelect: reqSelectMember, 
					customShowList, 
					grId: prj.grp
			}
			do_gl_req_autocompleteNew(el, options);
		}
		
		const do_lc_save_list_report = (prj) => {
			
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_SAVE_CONTENT, {obj: JSON.stringify(prj)});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterSave_report, [prj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_afterSave_report = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_lc_refresh_report_sprint(prj, prj.id);
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get'));
			}
		}
		
		const do_lc_refresh_report_sprint = function(prj, prjId){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_REFRESH_TASK_SPRINT, {id: prj.id, code: prj.code01});			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_refresh_task_sprint, [prj]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_after_refresh_report_sprint = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				let data = sharedJson[App['const'].RES_DATA];
				prj.lstReport = data;
				do_lc_show_report(prj);
				$("#a_btn_save_report_sprint, #a_btn_cancel_report_sprint").addClass("hide");
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}

		const do_lc_get_report = (prj, forced) => {
			let searchKey 		= $("#inp_filter_report").val()
			if(searchKey != null) searchKey = searchKey.trim() === "" ? null : searchKey.trim();
			let {id, code01} 		= prj;
			let dataSend			= {
				id: id,
				code: code01,
				stat: null,
				sKey: searchKey,
				typ01: pr_TYP_01_REPORT, 
				forced
			};

			let ref 				= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET_REPORT, dataSend);

			let divMain 			= pr_Tab_Report;
			let divPagination 		= pr_Tab_Report_Pag;

			let callbackFunct 		= (data) => {
				do_lc_get_report_callback(data, divMain, prj);
			}

			let opt = {
				divMain			: divMain,
				divPagination	: divPagination,
				url_api 		: App.path.BASE_URL_API_PRIV, 
				url_header 		: App.data["HttpSecuHeader"],
				url_api_param 	: ref,
				pageSize 		: pr_NUMBER_RECORD,
				pageRange		: 1,
				callback		: callbackFunct
			};

			do_gl_init_pagination_opt(opt);
		}

		const do_lc_get_report_callback = (sharedJson, div, prj) => {
			if (can_gl_AjaxSuccess(sharedJson)) {
				let data = sharedJson[App['const'].RES_DATA];

				do_lc_show_report(div, data, prj, false)
			}
		}
		
		const do_lc_show_report = function(div, data, prj, scrollToTop){
			if(data.lst){
				prj.lstReport = data.lst

				// for(var i=0; i < prj.lstReport.length; i++){
				// 	if(prj.lstReport[i].descr01) {
				// 		prj.lstReport[i].descr01 = prj.lstReport[i].descr01.substring(0, 200);
				// 	}
				// 	prj.lstReport[i].isViewSprint = pr_isViewSprint;
					
				// 	if(pr_isViewSprint){
				// 		prj.lstReport[i].isManager = prj.userRole === pr_member_lev_manager;
				// 	}
				// }
			} else prj.lstReport = [];

			do_lc_show_ui_report(prj, [...prj.lstReport], scrollToTop);
			do_lc_bindEvent_header_report(prj);
		}

		const do_lc_sort_date = (arr) => arr.sort((a, b) => new Date(b.dt01) - new Date(a.dt01)) 

		const do_lc_show_ui_report = (prj, reports, scrollToTop) => {
			if(reports.length > 0) reports = do_lc_sort_date(reports)

			$(pr_Tab_Report).html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_REPORT_LIST, reports));
			
			do_lc_bindEvent_reports_prj(prj);
			
			if (scrollToTop)  do_gl_scrollToTop();;
		}

		const do_lc_bindEvent_reports_prj = function(prj){
			$(".span-delete-task").off("click").on("click", function() {
				const {id, stat} = $(this).data();
				if(stat !== pr_STAT_PRJ_NEW)	return;
				if(!id)							return;

				App.MsgboxController.do_lc_show({
					title		: $.i18n("prj_project_del_task_popup"),
					content 	: $.i18n("prj_project_del_task_popup_content"),
					autoclose	: false,
					buttons		: {
						OK: {
							lab			: $.i18n("common_btn_ok"),
							funct		: do_lc_delete_new_report,
							param		: [id, prj],
							autoclose	: false,
							classBtn	: "btn-primary"
						},
						NO: {
							lab		:  $.i18n("common_btn_cancel"),
						}
					}
				});	
			})
			
			$(".remove-task").off("click").on("click", function(){
				let $this 					= $(this);
				let {id, stat: statFrom} 	= $this.data();
				if(id){
					let params = {id, statFrom,  statTo: pr_STAT_PRJ_FAIL};

					if (statFrom != pr_STAT_PRJ_NEW || statFrom != pr_STAT_PRJ_UNRESOLVED) {
						do_gl_show_Notify_Msg_Error ($.i18n('prj_cannot_delete_task') );
						return;
					}
					do_lc_save_change_stat(params, prj);
				}
			})
			
			$(".remove-task_report").off("click").on("click", function(){
				let $this 					= $(this);
				let {id, stat: statFrom} 	= $this.data();
				if(id){
					let index = pr_desrc02["task_ids"].indexOf(id);
					pr_desrc02["task_ids"].splice(index, 1);
					$("#tbody_task").find("tr[data-id='"+ id +"']").remove();
					$("#a_btn_save_report_sprint, #a_btn_cancel_report_sprint").removeClass("hide");
				}
			})
			
			$(".complete-task").off("click").on("click", function() {
				let $this 					= $(this);
				let {id, stat: statFrom} 	= $this.data();
				if(id){
					let params = {id, statFrom,  statTo: pr_STAT_PRJ_DONE};
					do_lc_save_change_stat(params, prj);
				}
			})
			
			$(".close-task").off("click").on("click", function() {
				let $this 					= $(this);
				let {id, stat: statFrom} 	= $this.data();
				if(id){
					const params = {id, statFrom,  statTo: pr_STAT_PRJ_CLOSED};
					do_lc_save_change_stat(params, prj);
				}
			})
			
			$(".td-report").off('click').on("click", function (){
				let $this 	= $(this);
				const {id} 	= $this.data()

				if(!id) return
				const data 	= prj.lstReport.find(r => r.id === +id)
				if(!data) return

				var obj = {files: []};
				App.MsgboxController.do_lc_show({
					title		: $.i18n("prj_project_add_report_popup"),
					content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_REPORT_CONT, data),
					autoclose	: false,
					buttons		: {
						COPY: {
							lab		: $.i18n("prj_project_btn_copy"),
							funct	: do_lc_copy_prj,
							param	: [prj],
							autoclose	: false,
							classBtn	: "btn-primary"
						},
						OK: {
							lab		: $.i18n("common_btn_save"),
							funct	: do_lc_save_new_report,
							param	: [prj, obj],
							autoclose	: false,
							classBtn	: "btn-primary"
						},
						NO: {
							lab		:  $.i18n("common_btn_cancel"),
						}
					},
					bindEvent: function() {
						App.SummerNoteController.do_lc_show("#div_create_prj", {dialogsInBody: true});
						do_lc_bindEvent_report_popup_prj(obj);
					}
				});	
			})

			App.router.controller.do_lc_binding_route();
		}
		
		const do_lc_save_change_stat = (params, prj) => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_SAVE_MOVE, params);	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_after_change_stat, [params, prj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}
		
		const do_lc_after_change_stat = (sharedJson, params, prj) => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_gl_show_Notify_Msg_Success($.i18n('common_ok_msg_save'));
				const {id, statTo} = params;
				
				if(prj.lstReport){
					const taskIndex = prj.lstReport.findIndex(t => t.id === id);
					if(taskIndex > -1){
						prj.lstReport[taskIndex].stat = statTo;
						do_lc_show_ui_report(prj, [...prj.lstReport]);
					}
				}
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}

		const do_lc_delete_new_report = (id, prj) => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_DEL, {id});			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_del_report, [prj]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_after_del_report = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				do_gl_show_Notify_Msg_Success($.i18n('common_ok_msg_save'));
				do_lc_refresh_report(prj, prj.id);
			}else{
				do_gl_show_Notify_Msg_Error($.i18n('common_err_msg_save'));
			}
			App.MsgboxController.do_lc_close();
		}

		const do_lc_copy_prj = (prj) => {
			let	data	 		= req_gl_data({
				dataZoneDom		: $("#div_create_prj")
			});

			if(data.hasError){
				$("#btn_msgbox_OK").removeAttr("disabled", "disabled");
				return false;
			}

			let objNew 		= data.data;
			objNew.typ01	= pr_TYP_01_REPORT;
			objNew.files	= [];

			objNew.dt01 	= do_lc_convert_date(objNew.dt01);
			objNew.dt02 	= do_lc_convert_date(objNew.dt02);

			App.MsgboxController.do_lc_close();
			var obj 		= {files: []}
			App.MsgboxController.do_lc_show({
				title		: $.i18n("prj_project_add_report_popup"),
				content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_REPORT_NEW, objNew),
				autoclose	: false,
				buttons		: {
					OK: {
						lab		: $.i18n("common_btn_save"),
						funct	: do_lc_save_new_report,
						param	: [prj, obj],
						autoclose	: false,
						classBtn	: "btn-primary"
					},
					NO: {
						lab		:  $.i18n("common_btn_cancel"),
					}
				},
				bindEvent: function() {
					App.SummerNoteController.do_lc_show("#div_create_prj", {dialogsInBody: true});
					do_lc_bindEvent_report_popup_prj(obj, true);
				}
			});	
		}

		const do_lc_save_new_report = function(prj, obj){
			$("#btn_msgbox_OK").attr("disabled", "disabled");
			let	data	 		= req_gl_data({
				dataZoneDom		: $("#div_create_prj"),
				oldObject 		: {"files": prj.files}
			});
			let $projectdesc = $("#projectdesc");
			if ($projectdesc.summernote('isEmpty')){
				$projectdesc.parent().append("<div class='errMsg'>" + $.i18n("validator_err_required") + "</div>")
			}

			if(data.hasError){
				$("#btn_msgbox_OK").removeAttr("disabled", "disabled");
				return false;
			}

			let objNew 		= data.data;
			objNew.typ01	= pr_TYP_01_REPORT;
			if(objNew.files) objNew.files.push(...obj.files)
			else objNew.files = obj.files

			objNew.dt01 	= do_lc_convert_date(objNew.dt01);
			objNew.dt02 	= do_lc_convert_date(objNew.dt02);

			do_lc_create_report(objNew, prj);
		}

		const do_lc_convert_date = objDate => objDate.date.substr(0, 10) + " " + objDate.time.substr(0, 5) + ":00";

		const do_lc_create_report = function(obj, prj){
			let ref 		= req_gl_Request_Content_Send(pr_SERVICE_CLASS, pr_SV_SAVE_REPORT);			
			ref["obj"]		= JSON.stringify(obj);
			ref["prjId"]	= prj.id;
			ref["prjCode"]	= prj.code01;

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_create_report, [prj]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_after_create_report = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				do_gl_show_Notify_Msg_Success 	($.i18n('common_ok_msg_save'));
				let data = sharedJson[App['const'].RES_DATA];
				if(data.entId == prj.id){
					const rIndex = prj.lstReport.findIndex(r => +r.id === +data.id)

					if(rIndex !== -1) prj.lstReport.splice(rIndex, 1)
					prj.lstReport.push(data);
					do_lc_show_ui_report(prj, prj.lstReport);
				}
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
			App.MsgboxController.do_lc_close();
		}

		const do_lc_bindEvent_report_popup_prj = function(obj, isCopied){
			if(!obj.files) obj.files = []

			let option = {
				fileinput	: {
					parallelUploads	: 10,
		            uploadMultiple	: true,
					param 			: {typ01: 2, filenameKept: 1}},//option here
				obj			: obj//file existing here
			}
			do_gl_init_fileDropzone($("#div_create_prj #div_report_docs"), option);

			if(!isCopied) {
				let now = new Date();
				let tomorrow =  new Date();
				tomorrow.setDate((new Date()).getDate() + 1);
				let hours = now.getHours();
				let minutes = now.getMinutes();
				
				$("#dtpicker_Begin").datepicker().datepicker("setDate", now);
				$("#dtpicker_End").datepicker().datepicker("setDate", tomorrow);
				
				$("#tmpicker_Begin").timepicker({//timepicker
					showMeridian: false,
					defaultTime :`${hours}:${minutes}`,
					icons		: {
						up		: "mdi mdi-chevron-up",
						down	: "mdi mdi-chevron-down"
					}
				});
				
				$("#tmpicker_End").timepicker({//timepicker
					showMeridian: false,
					defaultTime :`${hours}:${minutes}`,
					icons		: {
						up		: "mdi mdi-chevron-up",
						down	: "mdi mdi-chevron-down"
					}
				});
				do_lc_bind_event_dtInput()

				$("#btn_edit_doc").off("click").on("click", function(){
					$("#div_report_ent_file_upload").removeClass("hide");
					$(this).addClass("hide");
				})
			}


			App.controller.PrjProject.Ent.do_lc_reqRole_User();
			$(".info-edit").on("click", function(){
				let $parent = $(this).parent();
				$parent.find(".info-content")			.addClass("hide");
				$parent.find(".info-content-worker")	.addClass("hide");
				$parent.find(".content-edit")	.removeClass("hide");
			})
		}
		
		
		const do_lc_bind_event_dtInput = () => {
			$( "#dtpicker_Begin" ).off("change").on("change", function() {
				const sDate = $(this).val()
				const eDate = $( "#dtpicker_End" ).val()

				if(eDate < sDate) $( "#dtpicker_End" ).val(sDate)
			})
			$("#tmpicker_Begin").off("change").on("change", function() {
				const sDate = $( "#dtpicker_Begin" ).val()
				const eDate = $( "#dtpicker_End" ).val()

				if(eDate != sDate) return;
				
				let sTimeArr = $(this).val().split(":")
				let eTimeArr = $( "#tmpicker_End" ).val().split(":")

				if(sTimeArr.length <= 0 || eTimeArr.length <= 0) return

				const sHour = +sTimeArr[0]
				const sMinutes = +sTimeArr[1]
				const eHour = +eTimeArr[0]
				const eMinutes = +eTimeArr[1]

				const sMinutesStr = sMinutes < 10 ? `0${sMinutes}` : sMinutes

				if(eHour < sHour) $( "#tmpicker_End" ).val(`${sHour}:${sMinutesStr}`)
				if(sHour == eHour && eMinutes < sMinutes) $( "#tmpicker_End" ).val(`${sHour}:${sMinutesStr}`)
			})

			
			$( "#dtpicker_End" ).off("change").on("change", function() {
				const eDate = $(this).val()
				const sDate = $( "#dtpicker_Begin" ).val()

				if(eDate < sDate) $( "#dtpicker_Begin" ).val(eDate)
			})
			$("#tmpicker_End").off("change").on("change", function() {
				const sDate = $( "#dtpicker_Begin" ).val()
				const eDate = $( "#dtpicker_End" ).val()

				if(eDate != sDate) return;
				
				let sTimeArr = $( "#tmpicker_Begin" ).val().split(":")
				let eTimeArr = $(this).val().split(":")

				if(sTimeArr.length <= 0 || eTimeArr.length <= 0) return

				const sHour = +sTimeArr[0]
				const sMinutes = +sTimeArr[1]
				const eHour = +eTimeArr[0]
				const eMinutes = +eTimeArr[1]

				const eMinutesStr = eMinutes < 10 ? `0${eMinutes}` : eMinutes

				if(eHour < sHour) $( "#tmpicker_Begin" ).val(`${eHour}:${eMinutesStr}`)
				if(sHour == eHour && eMinutes < sMinutes) $( "#tmpicker_Begin" ).val(`${sHour}:${eMinutesStr}`)
			})
		}
		//------------------------------End Task list-----------------------------------
	}
	//------------------------------End Task list-----------------------------------

	//------------------------------Start Member list-----------------------------------
	var PrjProjectEntTabMember 	= function (grpName, header, content, footer) {
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		const pr_SV_GET_MEMBER		= "SVGetMember"; 
		const pr_SV_SAVE_MEMBER		= "SVSaveMember"; 
		
		//------------------------------------------------------------------------------------
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
		
		const pr_ctr_Ent				= App.controller.PrjProject.Ent;
		//------------------------------------------------------------------------------------------------
		
		var do_lc_load_view = function(){
			members[App.data.user.id] = {"id": App.data.user.id, "lev": pr_member_lev_manager, "typ": pr_member_typ_high};
		}

		//------------------------------------------------------------------------------------------------
		var do_lc_bindEvent_members = function(members, prj){
			pr_MEM_TEMP = $.extend(false, {}, members);

			$("#btn_add_member").off("click").on("click", function(){
				$(".action-item-member").removeClass("hide");
				$(this).addClass("hide");
			})

			$("#a_btn_save_member").off("click").on("click", function(){
				do_lc_save_members(members, prj);
			})

			$("#a_btn_cancel_member").off("click").on("click", function(){
				do_lc_show_members(members, prj);
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
				let mem 		= {"lev" : lev, "typ": typ, "ent02": item, "entId02": item.id, "entTyp02": pr_ENT_TYP_USER, "entId01": prj.id};
				let strlogin01 	= item.login01.length > 4?item.login01.substr(0, 4) + "..." : item.login01;
				
				let textColor   = null;
				let textAvatar  = null
				if(!item.avatar){
					let first = item.login01.charAt(0);
					let last  = item.login01.charAt(item.login01.length - 1);
					let index = var_gl_alphabet.indexOf(first.toLowerCase());
					
					textColor = var_gl_colors[index];
					textAvatar= first + last;
				}

				pr_MEM_TEMP[item.id] = mem;
				let selOpt 		= `<tr>`;
				selOpt 			+= `<td><a data-id='${item.id}' class='text-danger btn-remove-member' data-toggle='tooltip' data-placement='top' title='' data-original-title='Delete'><i class='mdi mdi-close font-size-18'></i></a></td>`;
				
				if(item.avatar) selOpt 			+= `<td style='width: 50px;'><img src='${ item.avatar.urlPrev}' class='rounded-circle avatar-xs' alt=''/></td>`;
				else 			selOpt 			+= `<td style='width: 50px;'> <div class="rounded-circle avatar-xs text-white text-uppercase text-center" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div></td>`;
				selOpt 			+= `<td><h5 class='font-size-14 m-0'><a href='' class='text-dark'>${strlogin01}</a></h5></td>`;
				selOpt 			+= `<td>` + $.i18n(PRJ_MEMBER_LEVEL[+lev]) 	+`</td>`;
				selOpt 			+= `<td class='hide'>` + $.i18n(PRJ_MEMBER_TYPE[+typ])	+`</td>`;
				selOpt 			+= `</tr>`;

				$("#tabMember table tbody").append(selOpt);
				do_lc_bindEvent_autocomplete(pr_MEM_TEMP);
				$(el).blur().val("");
			}

			let options = {
					dataService : [pr_SERVICE_PER_CLASS, pr_SV_USER_SEARCH], 
					svParams	: {wAvatar:true},
					fSelect		: reqSelectMember, 
					customShowList: do_lc_customLst_user_autocomplete
			}
			do_gl_req_autocompleteNew(el, options);

			App.router.controller.do_lc_binding_route()
		}
		
		var do_lc_bindEvent_autocomplete = function(pr_MEM_TEMP){
			$(".btn-remove-member").off("click").on("click", function(){
				let $this 		= $(this);
				let parentTR 	= $this.closest("tr");
				let {id} 		= $this.data();

				if(pr_MEM_TEMP[id])	delete pr_MEM_TEMP[id];
				parentTR.remove();
			})
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
				let first = item.login01.charAt(0);
				let last  = item.login01.charAt(item.login01.length - 1);
				let index = var_gl_alphabet.indexOf(first.toLowerCase());
				
				let textColor = var_gl_colors[index];
				let textAvatar= first + last;
				
				selOpt 		+= `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-white text-uppercase text-center mr-2" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div> ${item.login01}</div>`;
			}else{
				selOpt 		+= `<div class="media align-items-center"><img src='${ item.avatar.urlPrev}' class='rounded-circle avatar-xs mr-2'/> ${item.login01}</div>`;
			}
			return selOpt;
		}
		
		//------------------------------------------------------------------------------------------------
		
		
		this.do_lc_get_members = function(prj){
			if (prj.members && prj.members.length>0){
				do_lc_show_members(prj.members, prj);
				return;
			}
			
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET_MEMBER, {id: prj.id, code: prj.code01});

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_get_members_callback, [prj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_get_members_callback = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {
				let data 		= sharedJson[App['const'].RES_DATA];

				do_lc_show_members(data, prj);
				
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}


		var do_lc_save_members = function(members, prj){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_SAVE_MEMBER, {id:prj.id, code:prj.code01, members: JSON.stringify(Object.values(pr_MEM_TEMP))});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_save_members_callback, [members, prj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_save_members_callback = function(sharedJson, members, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_gl_show_Notify_Msg_Success 	($.i18n('common_success_update'));
				do_lc_show_members(pr_MEM_TEMP, prj);
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}

		var do_lc_show_members = function(data, prj){
			const is_Me 		= data.find(m => m.entId02 == App.data.user.id);
			const isSuperAdmin 	= App.controller.UI.Login && App.controller.UI.Login.can_lc_User_SuperAdmin();
			const isOwner		= App.data.user.id === prj.autUser01;
			
			let 	members 	= data.reduce((currentObj, mem)=>{
				if(mem.entId02 == prj.autUser01)	mem.isOwner = true;
				
				if(!isSuperAdmin && !isOwner){
					if(is_Me && is_Me.typ <= mem.typ && is_Me.lev >= mem.lev)	mem.notModif = true;
				}
				
				currentObj[mem.entId02] = mem;
				return currentObj;
			}, {});
			
			do_lc_load_view();
			$("#div_prj_member")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_MEMBER			, members));
			do_lc_bindEvent_members(members, prj);
			do_lc_bindEvent_resize("#div_prj_member");
			// do_gl_handle_member_external($("#div_prj_member"));
			
			pr_ctr_Ent.do_lc_reqRole_User();
		}
		//------------------------------End list member-----------------------------------
	}
	
	//------------------------------Start Member list-----------------------------------
	var PrjProjectEntTabMemberGroup = function (grpName, header, content, footer) {
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		const pr_SV_SAVE_MEMBER_GROUP	= "SVSaveMemberGroup"; 
		const pr_SV_GET_MEMBER_GROUP	= "SVGetMemberGroup"; 
		//------------------------------------------------------------------------------------
		//------------------------------Start list member-----------------------------------
		const PRJ_MEMBER_LEVEL 		= {0: "prj_project_member_level_manager", 1: "prj_project_member_level_reporter", 2: "prj_project_member_level_worker", 3: "prj_project_member_level_watcher"};
		const PRJ_GROUP_TYPE 		= {0: "prj_project_lev_bas"				, 1: "prj_project_lev_haute"};
		
		var pr_GROUP_TEMP		     	= {};
		
		const pr_member_lev_manager 	= 0;
		const pr_member_lev_reporter 	= 1;
		const pr_member_lev_worker 		= 2;
		const pr_member_typ_low 		= 0;
		const pr_member_typ_high 		= 1;

		const pr_ENT_TYP_GROUP          = 5000;

		
		var self                        = this;
		const pr_ctr_Ent				= App.controller.PrjProject.Ent;

		this.do_lc_get_grpMembers = function(prj){
			
			if (prj.groups && prj.groups.length>0){
				do_lc_show_grpMembers(prj.groups, prj);
				return;
			}
			
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET_MEMBER_GROUP, {id: prj.id, code: prj.code01});

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_get_grpMembers_callback, [prj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_get_grpMembers_callback = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {
				let data 		= sharedJson[App['const'].RES_DATA];
				do_lc_show_grpMembers(data, prj);
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}

		var do_lc_bindEvent_grpMembers = function(groups, prj){
			$("#btn_add_group").off("click").on("click", function(){
				$(".action-item-group").removeClass("hide");
				$(this).addClass("hide");
			})

			$("#a_btn_save_member_group").off("click").on("click", function(){
				var grToSave =  [];
				for (var i in pr_GROUP_TEMP){
					grToSave.push(pr_GROUP_TEMP[i]);
				}
				do_lc_save_grpMembers(grToSave, prj);
			})

			$("#a_btn_cancel_member_group").off("click").on("click", function(){
				do_lc_show_grpMembers(groups, prj);
			})

			$(".member-group-edit").off("click").on("click", function(){
				let $this 			= $(this);
				let {grpid} 		= $this.data();
				let grp 			= pr_GROUP_TEMP[grpid];
				if(grp){
					let parentTR 	= $this.closest("tr");
					parentTR.find(".content-member").addClass("hide");
					parentTR.find(".edit-member").removeClass("hide");
					let divLev 		= parentTR.find(".level-edit");
					let divTyp 		= parentTR.find(".typ-edit");
					do_lc_bindEvent_grpMember_Edit(grpid, divLev, divTyp);
					$(".action-mem-group").removeClass("hide");
				}
			})

			$(".member-group-delete").off("click").on("click", function(){
				let $this 			= $(this);
				let {grpid} 		= $this.data();
				let grp 			= pr_GROUP_TEMP[grpid];
				if(grp){
					delete pr_GROUP_TEMP[grpid];
					$(this).closest("tr").remove();
					$(".action-mem-group").removeClass("hide");
				}
			})
			
			$(".show_mem_group").off("click").on("click", function(){
				let {grpid} = $(this).data();
				let arr 	= Object.values(groups);
				
				let memGroup = arr.filter(item => item.id == grpid);
				
				App.MsgboxController.do_lc_show({
					title		: $.i18n("prj_project_member_group"),
					content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_MEMBER_GROUP_POPUP, memGroup[0]),	
//					widthMax	: '450px',
					css			: {"max-width":"450px"},
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

				let lev 		= $("#sel_group_level").val();
				let typ 		= $("#sel_group_type").val();
				let mem 		= {id:item.id, "lev":lev,"typ": typ, "ent02": item, "entId02": item.id, "entTyp02":pr_ENT_TYP_GROUP,"entId01": prj.id};
				
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
				selOpt 			+= `<td>` + $.i18n(PRJ_MEMBER_LEVEL[+lev]) 	+`</td>`;
				selOpt 			+= `<td class="hide">` + $.i18n(PRJ_GROUP_TYPE[+typ])	+`</td>`;
				selOpt 			+= `</tr>`;

				$("#tabGroup table tbody").append(selOpt);
				do_lc_bindEvent_autocomplete_group();
				$(el).blur().val("");
			}

			let Options = {
					dataService : [pr_SERVICE_GROUP_CLASS, pr_SV_GROUP_SEARCH], 
					fSelect: reqSelectGroup, 
					customShowList: do_lc_customLst_group_autocomplete
			}
			do_gl_req_autocompleteNew(el, Options);
		}

		var do_lc_bindEvent_autocomplete_group = function(){
			$(".btn-remove-group").off("click").on("click", function(){
				let $this 		= $(this);
				let parentTR 	= $this.closest("tr");
				let {id} 		= $this.data();

				if(pr_GROUP_TEMP[id])	delete pr_GROUP_TEMP[id];
				parentTR.remove();
			})
		}

		var do_lc_save_grpMembers = function(groups, prj){
			
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_SAVE_MEMBER_GROUP, {id: prj.id,code: prj.code01, groups: JSON.stringify(Object.values(pr_GROUP_TEMP))});

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_save_grpMembers_callback, [groups, prj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_save_grpMembers_callback = function(sharedJson, groups, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_gl_show_Notify_Msg_Success 	($.i18n('common_success_update'));
				do_lc_show_grpMembers(groups, prj)
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}

		var do_lc_bindEvent_grpMember_Edit = function(gId, divLev, divTyp){
			$(divLev).off("change").on("change", function(){
				pr_GROUP_TEMP[gId].lev = $(this).val();
			})

			$(divTyp).off("change").on("change", function(){
				pr_GROUP_TEMP[gId].typ = $(this).val();;
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

		var do_lc_show_grpMembers = function(groups, prj){
			pr_GROUP_TEMP 	= {};
			for (var i in groups){
				var g = groups[i];
				if (g.ent02 && g.ent02.val01 && (typeof g.ent02.val01 === "string")) g.ent02.val01 = JSON.parse (g.ent02.val01); //---parse json
				
				pr_GROUP_TEMP[g.id] = g.ent02;
			}
			
			
			$("#div_prj_member_group")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_MEMBER_GROUP		, groups));
			do_lc_bindEvent_grpMembers(groups, prj);
			do_lc_bindEvent_resize("#div_prj_member_group");
			// do_gl_handle_member_external($("#div_prj_member_group"));
			
			pr_ctr_Ent.do_lc_reqRole_User();//---binding event by user role
		}
		
//		const do_lc_get_member_of_group = (groupId, prj) => {
//			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET_MEMBER_GROUP, {id: prj.id});	
//
//			let fSucces		= [];
//			fSucces.push(req_gl_funct(null, do_lc_afterGet_member_group, [prj]));
//
//			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
//
//			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
//		}
//		
//		var do_lc_afterGet_member_group = function(sharedJson, prj){
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
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		//------------------------------Start File list-----------------------------------
		let self			= this;
		const pr_ctr_Ent	= App.controller.PrjProject.Ent;

		this.do_lc_show_prj_docs = function(prj){
			$("#div_prj_docs")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_DOCS, prj));
			do_lc_bindEvent_docs_prj(prj);
			do_lc_bindEvent_resize("#div_prj_docs");
			
			pr_ctr_Ent.do_lc_reqRole_User();
		}

		var do_lc_bindEvent_docs_prj = function(prj){
//			let	obj 		= {files:[]};
			prj.files 		= [];
			let option		= {
					fileinput	: { 
						parallelUploads	: 10,
			            uploadMultiple	: true,
						param 			: {typ01: 2, typ02: 10, filenameKept: 1},
						addRemoveLinks 	: !pr_ctr_Ent.can_lc_role_user_worker()
					},//option here
					obj			: prj//show empty box
			}
			do_gl_init_fileDropzone($("#div_prj_docs"), option);

			$(".item-file-download").off("click").on("click", function(){
				let {path} = $(this).data();
				path && window.open(path, "_blank");
			})

			$(".item-file-delete").off("click").on("click", function(){
				let fileId			= $(this).data("id");	
				var lineToRemove 	= $(this).parents("tr");
				
				//---MsgBox
				App.MsgboxController.do_lc_show({
					title	: $.i18n("msgbox_confirm_title"),
					content : $.i18n("msgbox_confirm_delete"),
					width	: "400px",
					autoclose	: false,
					buttons	: {
						OK: {
							lab		: $.i18n("common_btn_yes"),
							funct	: do_lc_del_files_prj,
							param	: [prj, fileId, lineToRemove],
							classBtn: "btn-success"
						},
						NO: {
							lab		: $.i18n("common_btn_cancel"),
							funct	: self.do_lc_clear_timeout_viewer,
							param	: [],
							classBtn: "btn-danger"
						}
					}
				});
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
				
				newprj.files 	= prj.files;
				newprj 			= $.extend(false, prj, newprj);

				console.log(prj.files);

				do_lc_save_files_prj(newprj);
			})

			$("#a_btn_cancel_doc").off("click").on("click", function(){
				self.do_lc_show_prj_docs(prj);
			})
		}

		var do_lc_save_files_prj = function(prj){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_ADD_FILES, {'id': prj.id, 'code': prj.code01, obj: prj});	

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
		
		var do_lc_del_files_prj = function(prj, fileId, lineToRemove){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_DEL_FILES, {'id': prj.id, 'code': prj.code01, 'fileId':fileId});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterDel_files_prj, [prj, fileId, lineToRemove]));

			let fError 		= req_gl_funct(App, pr_ctr_Main.do_show_Msg, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_afterDel_files_prj = function(sharedJson, prj, fileId, lineToRemove){
			if(can_gl_AjaxSuccess(sharedJson)) {
				lineToRemove.remove();
				if (prj.files) 
					prj.files = prj.files.filter(f => f.id != fileId);
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}
		//------------------------------End File list-----------------------------------
	}
	//------------------------------End File list-----------------------------------

	//------------------------------Start Comment list-----------------------------------
	var PrjProjectEntTabComment 	= function (grpName, header, content, footer) {
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		const pr_SV_GET_COMMENTS	= "SVGetComment";
		const pr_SV_NEW_COMMENTS	= "SVSaveComment";
		//------------------------------------------------------------------------------------
		//------------------variable pagination post------------------------------------------------------
		const pr_POST_BEGIN 			= 0;
		const pr_POST_NUMBER 		= 10;
		const pr_POST_NO_SUB 		= 1;
		const pr_POST_HAS_SUB 		= 2;
		const pr_POST_KEY_ENTER 	= 13;
		const self					= this;
		//------------------------------Start comment list-----------------------------------
		this.do_lc_get_comments = function(prj, reBuild = false, scrollToTop){
			let cond 		= {
					id		: prj.id			, 
					code	: prj.code01			,
					begin	: pr_POST_BEGIN		, 
					number	: pr_POST_NUMBER		, 
					nbLevel	: pr_POST_HAS_SUB	,			
					forced	: true, reBuild,
			}

			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET_COMMENTS, cond);	

			var callbackFunct = function(data) {		//data => sharedJson
				do_lc_show_comment_Dyn(data, prj, scrollToTop);
			}

			$("#div_prj_comments")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_COMMENT, {}));
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
		
		var do_lc_show_comments = function(dataCmts, prj, scrollToTop){
			$("#div_comment_list")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_COMMENT_LIST, dataCmts));
			
//			$(".a-delete[data-userid= '" + App.data.user.id +"']").removeClass("d-none");
			
			App.SummerNoteController.do_lc_show("#div_prj_comments", {height : 100}, true);//text editor
			
			do_lc_bindEvent_comments(prj);
			do_lc_bindEvent_resize("#div_comment_list");
			
			if (scrollToTop)  do_gl_scrollToTop();;
		}

		var do_lc_show_comment_Dyn = function(sharedJson, prj, scrollToTop){
			let data			= {};
			if (can_gl_AjaxSuccess(sharedJson)) {
				data		= sharedJson[App['const'].RES_DATA];
			}

			do_lc_show_comments(data, prj, scrollToTop);
		}

		var do_lc_bindEvent_comments = function(prj){
			$("#btn_send_comment").off("click").on("click", function(){
				let comment = $("#inp_comment").val();
				let iParent = $("#inp_parent_reply").val();
				if(!comment || !comment.length)	return false;
				do_lc_send_comment(prj, comment, iParent);
			})

			$(".a-reply").off("click").on("click", function(){
				let{parent, user} = $(this).data();
				parent && $("#inp_parent_reply").val(parent);
				if(user)	$("#inp_comment").summernote('code', `@${user} `);
			})
			
			$(".a-delete").off("click").on("click", function(){
				let{cmtId} = $(this).data();
				if(cmtId)	do_lc_del_comment(prj, cmtId);
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

		var do_lc_send_comment = function(prj, comment, iParent){
			let cond 		= {id: prj.id, code: prj.code01, comment, iParent};
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_NEW_COMMENTS, cond);

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_send_comment_callback, [prj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		var do_lc_send_comment_callback = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				let data = sharedJson[App['const'].RES_DATA];
				data && self.do_lc_get_comments(prj, true);
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}
		
		var do_lc_del_comment = function(prj, idCmt){
			let cond 		= {id: prj.id, code:prj.code01, cmtId: idCmt};
			let ref 		= req_gl_Request_Content_Send_With_Params("ServiceNsoPost" , "SVNsoPostDel12H", cond);

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_del_comment_callback, [prj, idCmt]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		var do_lc_del_comment_callback = function(sharedJson, prj, idCmt){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				self.do_lc_get_comments(prj, true);
				$(".post-lement-content[data-id='"+ idCmt +"']").remove();
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
		}
	}
	//------------------------------End comment list-----------------------------------

	//------------------------------Start Customer list-----------------------------------
	var PrjProjectEntTabCustomer 	= function (grpName, header, content, footer) {
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var pr_CUSTOMER_TEMP		= {};
		const PRJ_MEMBER_TYPE 		= {0: "prj_project_lev_bas"				, 1: "prj_project_lev_haute"};

		const pr_ctr_Ent			= App.controller.PrjProject.Ent;
		//------------------------------Start list customer-----------------------------------

		this.do_lc_get_list_customers = function(id){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET_CUSTOMER, {id});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_show_list_customers, [id]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_show_list_customers = function(sharedJson, idPrj){
			if(can_gl_AjaxSuccess(sharedJson)) {
				let data 		= sharedJson[App['const'].RES_DATA];

				let objCus 	= data.reduce((currentCus, customer)=>{
					currentCus[customer.entId02] = customer;
					return currentCus;
				}, {});

				do_lc_show_prj_customer(objCus, idPrj);
				
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}

		var do_lc_show_prj_customer = function(customers, idPrj){
			let hasData 			= !$.isEmptyObject(customers);
			let cusData 			= {customers, hasData};

			$("#div_prj_customers")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_CUSTOMER			, cusData));
			do_lc_bindEvent_customers_prj(customers, idPrj);
			do_lc_bindEvent_resize("#div_prj_customers");
			
			pr_ctr_Ent.do_lc_reqRole_User();//---binding event by user role
		}

		var do_lc_bindEvent_customers_prj = function(customers, idPrj){
			$(".dropdown-toggle").on("click", function(){
				setTimeout(() => {
					$(".dropdown-menu").css("transform", "unset");
				}, 5);

			})

			pr_CUSTOMER_TEMP = $.extend(false, {}, customers);

			$(".customer-typ").off("click").on("click", function(){
				let {id, changeto} 	= $(this).data();

				id && do_lc_changeTyp_customer(id, changeto);
			})

			$(".customer-delete").off("click").on("click", function(){
				let {id, cusid} 	= $(this).data();

				id && do_lc_delete_customer(id, cusid);
			})

			$(".customer-info").off("click").on("click", function() {
				let {id, cusid} = $(this).data();

				id && do_lc_get_info_with_customer(id, customers[cusid]);
			})

			let el = "#inp_name_customer";
			let reqSelectCus = function(event, item){
				if(pr_CUSTOMER_TEMP[item.id])			return false;

				let typ 		= $("#sel_customer_type").val();
				let cmt 		= $("#inp_note_customer").val();
				let cus 		= {typ, "ent02": item, "entId02": item.id, "entId01": idPrj, "cmt" : cmt};

				do_lc_new_customer(cus);
				$(el).blur().val("");
			}

			let options = {
					dataService : [pr_SERVICE_PER_CLASS, pr_SV_PERSON_SEARCH], fSelect: reqSelectCus, customShowList: do_lc_customLst_person_autocomplete
			}
			do_gl_req_autocompleteNew(el, options);
		}

		var do_lc_get_info_with_customer = function(id, cus) {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET_INFO_WITH_CUSTOMER, {id});

			let fSucces 	= [];
			fSucces.push(req_gl_funct(null, do_lc_after_get_info_with_customer, [cus]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax") ]);
			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		var do_lc_after_get_info_with_customer = function(sharedJson, cus) {
			if (can_gl_AjaxSuccess(sharedJson)) {
				let info = {};
				let data = sharedJson[App['const'].RES_DATA];
				if(data && data.length){
					info = data[0];
				}
				do_lc_show_info_with_customer(info, cus);
			} else {
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}

		var do_lc_new_customer = function(cus) {
			let ref 	= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_NEW_CUSTOMER, {obj: JSON.stringify(cus)});

			let fSucces = [];
			fSucces.push(req_gl_funct(null, do_lc_after_new_customer, [cus]));

			let fError = req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax") ]);
			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		var do_lc_after_new_customer = function(sharedJson, cus) {
			if (can_gl_AjaxSuccess(sharedJson)) {
				let data = sharedJson[App['const'].RES_DATA];

				pr_CUSTOMER_TEMP[data.entId02] = Object.assign(data, cus);
				do_lc_show_prj_customer(pr_CUSTOMER_TEMP, data.entId01);

				do_gl_show_Notify_Msg_Success 	($.i18n('common_ok_msg_save'));
			} else {
				do_gl_show_Notify_Msg_Error($.i18n('common_error_msg_save'));
			}
		}

		var do_lc_changeTyp_customer = function(id, changeto) {
			let ref 	= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_CHANGE_TYP_CUSTOMER, {id, changeto});

			let fSucces = [];
			fSucces.push(req_gl_funct(null, do_lc_after_changeTyp_customer, []));

			let fError = req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax") ]);
			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		var do_lc_after_changeTyp_customer = function(sharedJson) {
			if (can_gl_AjaxSuccess(sharedJson)) {
				let data = sharedJson[App['const'].RES_DATA];

				pr_CUSTOMER_TEMP[data.entId02] = Object.assign(pr_CUSTOMER_TEMP[data.entId02], data);
				do_lc_show_prj_customer(pr_CUSTOMER_TEMP, data.entId01);

				do_gl_show_Notify_Msg_Success 	($.i18n('common_ok_msg_save'));
			} else {
				do_gl_show_Notify_Msg_Error($.i18n('common_error_msg_save'));
			}
		}

		var do_lc_delete_customer = function(id, cusid) {
			let ref 	= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_DELETE_CUSTOMER, {id});

			let fSucces = [];
			fSucces.push(req_gl_funct(null, do_lc_after_delete_customer, [cusid]));

			let fError = req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax") ]);
			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		var do_lc_after_delete_customer = function(sharedJson, cusid) {
			if (can_gl_AjaxSuccess(sharedJson)) {
				let idPrj = pr_CUSTOMER_TEMP[cusid].entId01;
				delete pr_CUSTOMER_TEMP[cusid];
				do_lc_show_prj_customer(pr_CUSTOMER_TEMP, idPrj);

				do_gl_show_Notify_Msg_Success 	($.i18n('common_ok_msg_del'));
			} else {
				do_gl_show_Notify_Msg_Error($.i18n('common_error_msg_del'));
			}
		}

		var do_lc_show_info_with_customer = function(info, cus){
			App.MsgboxController.do_lc_show({
				title 		: cus.ent02.name01,
				content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_CUSTOMER_INFO, {info, cus}),
				autoclose	: false,
				buttons 	: {
					UPDATE : {
						lab 		: $.i18n("common_btn_send"),
						funct 		: do_lc_get_update_info,
						param 		: [cus],
						classBtn	: "btn-primary",
						autoclose	: false
					},
					CALCEL : {
						lab 		: $.i18n("common_btn_cancel"),
					}
				},
				bindEvent: function() {
					App.SummerNoteController.do_lc_show("#div_customer_info", {dialogsInBody: true});
				}
			});
		}

		var do_lc_get_update_info = function(cus){
			let dataCus = req_gl_data({
				dataZoneDom		: $("#div_customer_info")
			})

			if(dataCus.hasError)	return false;

			let info = dataCus.data;

			do_lc_send_update_info(info, cus);
		}

		var do_lc_send_update_info = function(info, cus) {
			let ref 	= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_UPDATE_INFO_WITH_CUSTOMER, {id: cus.id, obj: JSON.stringify(info)});

			let fSucces = [];
			fSucces.push(req_gl_funct(null, do_lc_after_update, []));

			let fError = req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax") ]);
			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		var do_lc_after_update = function(sharedJson) {
			if (can_gl_AjaxSuccess(sharedJson)) {
				do_gl_show_Notify_Msg_Success 	($.i18n('common_ok_msg_save'));
				App.MsgboxController.do_lc_close();
			} else {
				do_gl_show_Notify_Msg_Error($.i18n('common_error_msg_save'));
			}
		}

		var do_lc_bindEvent_tabCustomerEdit = function(cusid, divTyp){
			$(divTyp).off("change").on("change", function(){
				pr_CUSTOMER_TEMP[cusid].typ = $(this).val();
			})
		}

		var do_lc_customLst_person_autocomplete = function(item, selOpt = ""){
			if(!item.avatar)	item.avatar = {path01: UI_URL_ROOT + "img/prj/users/avatar-" 		+ do_gl_reqRandom_number(1, 1) 	+ ".jpg"};
			selOpt 		+= `<img src='${ item.avatar.urlPrev}' class='rounded-circle avatar-xs avatar-autocomplete'/> ${item.name01}`;
			return selOpt;
		}
		//------------------------------End list customer-----------------------------------
	}
	//------------------------------End list customer-----------------------------------

	//------------------------------Start Stats prj-----------------------------------
	var PrjProjectEntTabStat 	= function (grpName, header, content, footer) {
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		const self						= this;

		const pr_ctr_Ent				= App.controller.PrjProject.Ent;
		const pr_project				= App.controller.UI;

		const pr_TYPE02_MAIN			= 0;
		
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

		this.do_lc_show_prj_stats = function(prj, mode, isViewSprint){
			if(isViewSprint) pr_isViewSprint = isViewSprint;
			do_lc_show_content(prj, mode);
		}

		const do_lc_show_content = (prj, mode) => {
			do_lc_req_set_lstStats(prj)

			if(prj.typ02 !== pr_TYPE02_MAIN) {
				$("#div_prj_container_stat").remove()
			} else {
				 do_lc_show_statList(prj);
				$(".item-stat-show").prop("disabled",true); 
			}
		}

		const do_lc_req_set_lstStats = (prj) => {
			const prjMain = prj.prjMain

			if(prj.typ02 !== pr_TYPE02_MAIN) {
				prj.lstStatsClone 	= do_lc_req_def_stats(prjMain?.inf03)
				prj.lstStats 		= [...prj.lstStatsClone]
			} else {
				prj.lstStatsClone 	= do_lc_req_def_stats(prj.inf03)
				prj.lstStats 		= do_lc_req_def_stats()
			}
			do_lc_stat_selected(prj.lstStats, prj.stat)
			paramStat = prj.lstStatsClone
		}
		
		const do_lc_req_def_stats = (inf03) => {
			const defStats = [
				{
					id: pr_STAT_PRJ_NEW,
					lab: "new",
					trans: "prj_project_stat_100100",
					show: 1,
					ord: 1,
				},
				{
					id: pr_STAT_PRJ_TODO,
					lab: "tod",
					trans: "prj_project_stat_100200",
					show: 1,
					ord: 2,
				},{
					id: pr_STAT_PRJ_INPROGRESS,
					lab: "inp",
					trans: "prj_project_stat_100300",
					show: 1,
					ord: 3,
				},{
					id: pr_STAT_PRJ_DONE,
					lab: "don",
					trans: "prj_project_stat_100400",
					show: 1,
					ord: 4,
				},{
					id: pr_STAT_PRJ_TEST,
					lab: "tes",
					trans: "prj_project_stat_100500",
					show: 1,
					ord: 5,
				},{
					id: pr_STAT_PRJ_REVIEW,
					lab: "rev",
					trans: "prj_project_stat_100600",
					show: 1,
					ord: 6,
				},{
					id: pr_STAT_PRJ_FAIL,
					lab: "fai",
					trans: "prj_project_stat_100700",
					show: 1,
					ord: 7,
				},{
					id: pr_STAT_PRJ_UNRESOLVED,
					lab: "unr",
					trans: "prj_project_stat_100800",
					show: 1,
					ord: 8,
				},{
					id: pr_STAT_PRJ_CLOSED,
					lab: "clo",
					trans: "prj_project_stat_100900",
					show: 1,
					ord: 9,
				},
			]

			if(!inf03) return defStats;

			inf03 = JSON.parse(inf03)

			if(!inf03.stats) return defStats;

			return inf03.stats
		}

		const do_lc_stat_selected = (lstStats, stat) => {
			if(!lstStats) return

			for(const s of lstStats) {
				if(+s.id === +stat) {
					s.selected = true
					return
				}
			}
		}

		const do_lc_show_statList = (prj) => {
			prj.lstStatsClone = prj.lstStatsClone.sort(function(a, b) {
				return a.ord - b.ord;
			});

			$("#div_prj_stats").html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_STAT, prj));
			do_lc_bindEvent_statList(prj);
		}

		const do_lc_bindEvent_statList = (prj) => {
			$("#btn_add_stat_lst").off("click").on("click", function() {
				const do_lc_req_max_attr = (arr, attr) => {
					if(!arr || arr.length <= 0) return;
				
					var max = -Infinity;
				
					for (var i = 0; i < arr.length; i++) {
						if (+arr[i][attr] > max) {
							max = +arr[i][attr];
						}
					}
				
					return max;
				}

				const do_lc_add_stat_lst = () => {
					const _content_stat = $("#inp_stat_cnt").val();
					const _show_stat = $("#inp_stat_show").is(":checked");
					if(!_content_stat || !_content_stat.trim().length)	return false;

					prj.lstStatsClone.push({
						id: do_lc_req_max_attr(prj.lstStatsClone, "id") + 1,
						lab: _content_stat.trim(),
						trans: null,
						show: _show_stat,
						ord: do_lc_req_max_attr(prj.lstStatsClone, "ord") + 1
					});
					do_lc_show_statList(prj);
					$("#a_btn_save_stats, #a_btn_cancel_stats")	.removeClass("hide");
					pr_ctr_Ent.do_lc_reqRole_User();
					App.MsgboxController.do_lc_close();
				}

				App.MsgboxController.do_lc_show({
					title 		: $.i18n("prj_project_stats_msgbox_add"),
					content 	: `<div class="mb-4">
										<input class="form-control mb-2" id="inp_stat_cnt" type="text" placeholder="${$.i18n("prj_project_stat_enter_inp")}">
										<div class="ms-2 d-flex align-items-center">
											<label class="mb-0 mr-2">${$.i18n("prj_project_stat_enter_show")}</label>
											<input id="inp_stat_show" type="checkbox">
										</div>
									</div>`,
					autoclose	: false,
					buttons 	: {
						UPDATE : {
							lab 		: $.i18n("prj_project_stat_add"),
							funct 		: do_lc_add_stat_lst,
							classBtn	: "btn-primary",
							autoclose	: false
						},
						CALCEL : {
							lab 		: $.i18n("common_btn_cancel"),
						}
					}
				});
			})

			$(".item-stat-show").off("change").on("change", function(){
				const $this 	= $(this);
				const isCheck 	= $this.is(":checked");
				const {index} 	= $this.data();
				prj.lstStatsClone[index].show = isCheck;
				do_lc_show_statList(prj);
				$("#a_btn_save_stats, #a_btn_cancel_stats")	.removeClass("hide");
				pr_ctr_Ent.do_lc_reqRole_User();
			})

			$(".remove-item-stat").off("click").on("click", function(){
				const {index} 	= $(this).data();
				index > -1 && prj.lstStatsClone.splice(index, 1);
				do_lc_show_statList(prj);
				$("#a_btn_save_stats, #a_btn_cancel_stats")	.removeClass("hide");
			})

			$(".up-item-stat").off("click").on("click", function(){
				const {index} 	= $(this).data();
				const lstStats	= prj.lstStatsClone

				if (index === 0) {
					lstStats[index].ord = lstStats[lstStats.length - 1].ord
					for (let i = 1; i < lstStats.length; i++) {
						lstStats[i].ord -= 1;
					}
				} else {
					const ordTemp = lstStats[index].ord
					lstStats[index].ord = lstStats[index - 1].ord
					lstStats[index - 1].ord = ordTemp
				}

				do_lc_show_statList(prj);
				$("#a_btn_save_stats, #a_btn_cancel_stats")	.removeClass("hide");
			})

			$(".down-item-stat").off("click").on("click", function(){
				const {index} 	= $(this).data();
				const lstStats	= prj.lstStatsClone

				if (index === lstStats.length - 1) {
					lstStats[index].ord = lstStats[0].ord
					for (let i = 0; i < lstStats.length - 1; i++) {
						lstStats[i].ord += 1;
					}
				} else {
					const ordTemp = lstStats[index].ord
					lstStats[index].ord = lstStats[index + 1].ord
					lstStats[index + 1].ord = ordTemp
				}

				do_lc_show_statList(prj);
				$("#a_btn_save_stats, #a_btn_cancel_stats")	.removeClass("hide");
			})

			$(".edit-item-stat").off("click").on("click", function() {
				const {index} 		= $(this).data();
				let _content_stat 	= prj.lstStatsClone[index].lab;
				let _show_stat 		= +prj.lstStatsClone[index].show;
				if(!_content_stat)	return false;

				const do_lc_edit_stat_lst = () => {
					const _content_stat_new = $("#inp_stat_cnt").val();
					const _show_stat_new = $("#inp_stat_show").is(":checked");
					if(!_content_stat_new || !_content_stat_new.trim().length)	return false;

					prj.lstStatsClone[index] = {
						...prj.lstStatsClone[index],
						lab: _content_stat_new.trim(),
						show: _show_stat_new,
					}
					do_lc_show_statList(prj);
					$("#a_btn_save_stats, #a_btn_cancel_stats")	.removeClass("hide");
					pr_ctr_Ent.do_lc_reqRole_User();
					App.MsgboxController.do_lc_close();
				}

				App.MsgboxController.do_lc_show({
					title 		: $.i18n("prj_project_stats_msgbox_add"),
					content 	: `<div class="mb-4">
										<input class="form-control mb-2" id="inp_stat_cnt" value="${_content_stat}" type="text" placeholder="${$.i18n("prj_project_stat_enter_inp")}">
										<div class="ms-2 d-flex align-items-center">
											<label class="mb-0 mr-2">${$.i18n("prj_project_stat_enter_show")}</label>
											<input id="inp_stat_show" type="checkbox" ${_show_stat === 1 && "checked"}>
										</div>
									</div>`,
					autoclose	: false,
					buttons 	: {
						UPDATE : {
							lab 		: $.i18n("prj_project_stat_add"),
							funct 		: do_lc_edit_stat_lst,
							classBtn	: "btn-primary",
							autoclose	: false
						},
						CALCEL : {
							lab 		: $.i18n("common_btn_cancel"),
						}
					}
				});
			})

			$("#a_btn_save_stats").off("click").on("click", function(){
				let newPrj 		= {};
				
				newPrj.files 	= prj.files;
				newPrj 			= $.extend(false, prj, newPrj);
				
				if(newPrj.lstStatsClone) newPrj.inf03 = JSON.stringify({
					stats: newPrj.lstStatsClone
				});
				
				do_lc_save_prj_stat(newPrj, prj)
			})
		}

		const do_lc_save_prj_stat = function(newPrj, prj){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_SAVE_CONTENT, {obj: JSON.stringify(newPrj)});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterSave_prjStat, [prj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_afterSave_prjStat = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {
				let data 	= sharedJson[App['const'].RES_DATA];
				prj 		= $.extend(true, prj, data);
				self.do_lc_show_prj_stats(prj);
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get'));
			}
		}
	}

	//------------------------------End Stats prj-----------------------------------

	//------------------------------Start Content prj-----------------------------------
	var PrjProjectEntTabContent 	= function (grpName, header, content, footer) {
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		const self						= this;
		const pr_TYPE01_INDUSTRY		= 1;
		const pr_TYPE01_INFORMATIQUE	= 2;
		const pr_TYPE01_BUISINESS		= 3;
		const pr_TYPE01_TRAVEL			= 4;
		const pr_project				= App.controller.UI;

		const pr_ctr_Ent				= App.controller.PrjProject.Ent;

		const pr_TYPE02_MAIN			= 0;
		const pr_TYPE02_SUB				= 1;
		const pr_TYPE02_ELE				= 2;

		var dataWF						= null;
		var workflow  					= {};
		var members                     = {};
		var pr_isViewSprint             = false;
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

		const TAB_CONTENT 			= "content";
		const TAB_MEMBER 			= "member";
		const TAB_PRJ 				= "prj";
		const TAB_EPIC 				= "epic";
		const TAB_TASK 				= "task";
		//---------------------- -------------------------------------------------
		this.do_lc_show_prj_content = function(prj, mode, isViewSprint){
			if(isViewSprint) pr_isViewSprint = isViewSprint;
			do_lc_show_content(prj, mode);
			do_lc_get_path_prj(prj);
			
//			dataWF = null;
//			dataWF = do_lc_show_wf_task(prj);
			if(pr_isViewSprint) do_lc_get_percent_sprint(prj);
		}
		
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
						var   cont 	= $("#btn_back").data("url");
						const url 	= cont?cont.replace("#code", parObj.code01).replace("#id", parObj.id): "";
						
						$("#btn_back").data("url", url)
					}
					
					do_lc_transfert_data(data, id);
				}
			}
		}
		
		const do_lc_get_percent_sprint = ({id}) => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_CALCUL_PERCENT_SPRINT, {prjId : id});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_get_percent_response, [id]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			App.network.do_lc_ajax_bg (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		const do_lc_get_percent_response = (sharedJson, id) => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				let val05 = sharedJson.res_data;
				$("#val05").html(val05 + "%");
			}
		}

		const do_lc_transfert_data = (data, id) => {
			let prjInd 	= data.findIndex(prj => prj.id === prj.grp);
			let prjAll 	= data[prjInd];
			data.splice(prjInd, 1);

			let mapPrj 	= new Map();

			for(let p of data){
				mapPrj.set(p.parent, p);
				p["isShow"] = p.id == id;
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

		var do_lc_show_content = function(prj, mode){
			if(prj.epicInf){
				let epics = prj.epicInf;
				let obj = epics.reduce((currentEpic, epic)=>{
					currentEpic[epic.id] = epic;
					return currentEpic;
				}, {});

				if(obj[prj.parent]) prj.epicName = obj[prj.parent].name;
			}
			
			prj.isViewSprint = pr_isViewSprint;

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

			if (!pr_isViewSprint) $("#btn_delete").remove();
			
			do_lc_bindEvent_content_prj(prj, mode);
			do_lc_bindEvent_resize();

			do_lc_init_element(prj);

			//--- if business , load client, if not, hide div
			if(prj.typ01 == pr_TYPE01_BUISINESS){
				App.controller.UI.EntCustomer.do_lc_get_list_customers(prj.id);
			} else {
				$("#div_prj_customers")	.html("");
			}

			pr_ctr_Ent.do_lc_reqRole_User();
			
			if(prj.stat == pr_STAT_PRJ_DONE) {
				let $parent = $(".val05").parent();
				$parent.find(".info-edit")	.off("click");
				$parent.find(".val05")	.removeClass("content-edit");
			}
		}
		
		const do_lc_show_evalutation =  (eval, isDef) => {
			do_gl_bar_rating_init_one("eval01", eval);
			do_gl_bar_rating_show_all(pr_div_rating, "", null, eval, null );
			req_gl_bar_rating_value(pr_div_rating, eval);
			if(isDef) $("#div_star_eval").find("a[data-rating-value='"+ 1 +"']").removeClass("br-selected br-current");
		}

		const do_lc_show_checkList = (prj) => {
			//clone list check list
			$("#div_check_list").html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_CONTENT_CHECK_LIST, prj));
			do_lc_bindEvent_checkList(prj);
		}

		const do_lc_bindEvent_checkList = (prj) => {
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
			})
			
			setTimeout(function(){
				App.SummerNoteController.do_lc_show("#div_prj_info");
			},500);
		}

		var do_lc_bindEvent_content_prj = function(prj, mode){
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

					pr_ctr_Ent.do_lc_reqRole_User();
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

					pr_ctr_Ent.do_lc_reqRole_User();
				})

				$("#a_btn_save, #a_btn_save02, #a_btn_save_stats").off("click").on("click", function(){
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
//						newPrj.files	= newPrj.files.concat(obj.files);

						newPrj.dtBegin 	= do_lc_convert_date(newPrj.dtBegin);
						newPrj.dtEnd 	= do_lc_convert_date(newPrj.dtEnd);

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

					if(newPrj.lstStatsClone) newPrj.inf03 = JSON.stringify({
						stats: newPrj.lstStatsClone
					});

					//---remove some fields before send to server
					newPrj.epicInf 	= null;
					newPrj.epicName = null;
					newPrj.epics	= null;
					newPrj.lstClone = null;
					newPrj.tasks 	= null;
					
					self.do_lc_save_prj_content(newPrj, prj)
				})

				$("#a_btn_cancel, #a_btn_cancel02, #a_btn_cancel_stats").off("click").on("click", function(){
					self.do_lc_show_prj_content(prj, null, pr_isViewSprint);
				})

				$(".btn-reload").off("click").on("click", function(){
					let {name: typLoad} = $(this).data();
					do_lc_get_content_reload(prj, typLoad);
				})

				$("#btn_refresh_content").off("click").on("click", function() {
					do_lc_refresh_content(prj, prj.id, prj.code01);
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

				$("#btn_show_wf").off("click").on("click", function() {
					if (dataWF != null) {
						App.MsgboxController.do_lc_show({
							title		: $.i18n("prj_title_workflow_view"),
							content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_WORKFLOW_VIEW		, {}),	
							autoclose	: false,
							buttons		: {
								NO: {
									lab		:  $.i18n("common_btn_cancel"),
								}
							},
						});	
						do_lc_show_work_flow(dataWF);
					} else {
						do_gl_show_Notify_Msg_Error ($.i18n('workflow_err_msg_show'));
					}
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
				const viewer = new Viewer(document.getElementById('div_prj_content_01'), {
					hide: function () {
						viewer.destroy();
					},
				});

				// let src = $(this).attr("src");
				// App.MsgboxController.do_lc_show({
				// 	content 	: `<img src="${src}" style="width: 100%;">`,
				// 	autoclose	: false,
				// 	buttons 	: {
				// 		CALCEL : {
				// 			lab 		: $.i18n("common_btn_cancel"),
				// 			classBtn	: "btn-primary",
				// 		}
				// 	}
				// });
			})
			
			if(pr_isViewSprint){
				$("#a_percent_reload").off("click").on("click", function(){
					do_lc_get_percent_sprint(prj);
				})
			}
		};
		
		const do_lc_get_history = function(prj){
			const ref 				= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_GET_HISTORY_TASK, {id :prj.id, typ : prj.typ02});

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

			let obj = {arrContent:[], arrChild:[]};
			data.forEach((e) => {
				if (e.typTab == TAB_CONTENT) obj.arrContent.push(e)
				else obj.arrChild.push(e)
			});

			$("#div_history_list").html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_HISTORY , obj));
		}

		const do_lc_convert_date = objDate => objDate.date.substr(0, 10) + " " + objDate.time.substr(0, 5) + ":00";

		this.do_lc_save_prj_content = function(newPrj, prj){
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

			let ref 				= req_gl_Request_Content_Send_With_Params(pr_SERVICE_EVAL_CLASS, pr_SV_NAME_RELOAD, {id: prj.id, code: prj.code01});

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
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_SPRINT_DEL, {id: prjId});			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_del_content, [prj]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_after_del_content = function(sharedJson, prj){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				let data = sharedJson[App['const'].RES_DATA];
				do_gl_show_Notify_Msg_Success($.i18n('common_ok_msg_del'));
				
				$(".btn-refrest-list").trigger("click");
				$("#div_task_content_main").html("");
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_del'));
				do_gl_show_Notify_Msg_Error ($.i18n('sprint_err_msg_del'));
			}
		}

		var do_lc_refresh_content = function(prj, prjId, prjCode){
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_REFRESH_CONTENT, {id: prj.id, code: prj.code01});			

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

				pr_project.EntEpic		.do_lc_show_prj_epic(prj);
				pr_project.EntTask		.do_lc_show_prj_task(prj);
				pr_project.EntDoc		.do_lc_show_prj_docs(prj);
				pr_project.EntEval		.do_lc_get_prj_evaluation(prj);
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
			obj.stat	=  null;
			obj.stat01	=  null;
			
			return obj;
		}
		
		const do_lc_create_prj = prj => {
			let dataSend	= {obj: JSON.stringify(prj), member : Object.values(members)};
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_NEW, dataSend);			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_show_prj, [prj]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

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

		const do_lc_show_wf_task = (prj) => {
			let ref 		= req_gl_Request_Content_Send(pr_SERVICE_CLASS, "SVWorkflowByTask");
			ref["id"]		= prj.id;
			ref["grId"]		= prj.grp;

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_show_wf_task_success, [prj]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_show_wf_task_success = (sharedJson, prj) => {
			if(can_gl_AjaxSuccess(sharedJson)) {	
				let data = sharedJson[App['const'].RES_DATA];
				dataWF = data;

				// Handle select stat field
				do_lc_handle_select_stat(data, prj);
			}
		}

		const do_lc_handle_select_stat = (data, prj) => {
			let wf  = data.descr01;
			let lstStatEnd = [];
			let haveQuickStat = false;
			try{
				let connection 			= (JSON.parse(wf)).con;
				if (connection && connection.length > 0) {
					connection.forEach((e) => {
						if (prj.stat == e.statBegin) {
							lstStatEnd.push(e.statEnd);
						}
					});
				};

				for (let i = 0; i < 8; i++) {
					$(`#stat option[value=${i}]`).hide();
				}

				if (lstStatEnd.length < 4) haveQuickStat = true;
				if (lstStatEnd.length > 0) {
					lstStatEnd.forEach((e) => {
						$(`#stat option[value=${e}]`).show();
						if (haveQuickStat) {
							$(`#quick_stat_0${e}`).removeClass("hide");
							$(`#quick_stat_0${e}`).off("click").on("click", () => {
								do_gl_select_value($("#stat"), e);
								do_lc_quick_save_stat(prj);
							});
						}
					});
				};
				
			} catch(e) {
			}
		}

		const do_lc_quick_save_stat = (prj) => {
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
//						newPrj.files	= newPrj.files.concat(obj.files);

				newPrj.dtBegin 	= do_lc_convert_date(newPrj.dtBegin);
				newPrj.dtEnd 	= do_lc_convert_date(newPrj.dtEnd);

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

			//---remove some fields before send to server
			newPrj.epicInf 	= null;
			newPrj.epicName = null;
			newPrj.epics	= null;
			newPrj.lstClone = null;
			newPrj.tasks 	= null;
			
			do_lc_save_prj_content(newPrj, prj)
		}

		const do_lc_show_work_flow = (prj) => {
			let wf  = prj.descr01;
			try{
				workflow 			= JSON.parse(wf);
				workflow.statWF 	= req_lc_build_objWF(workflow.con);
				do_lc_show_workflow(workflow);
			} catch(e) {
				console.log(e);
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

		const do_lc_show_workflow = function(wf) {
			do_gl_show_workflow (wf, tmplCtrl, tmplName);
		}

		//------------------------------End content prj-----------------------------------
	}
	//------------------------------End content prj-----------------------------------

	return {PrjProjectEntTabEval, PrjProjectEntTabEpic, PrjProjectEntTabTask, PrjProjectEntTabReport, PrjProjectEntTabMember, PrjProjectEntTabMemberGroup, PrjProjectEntTabDoc, PrjProjectEntTabComment, PrjProjectEntTabCustomer, PrjProjectEntTabStat, PrjProjectEntTabContent};
});