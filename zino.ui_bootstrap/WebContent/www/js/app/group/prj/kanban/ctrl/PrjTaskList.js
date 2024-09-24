define([
	'text!group/prj/kanban/tmpl/PrjTask_List.html', 
	'text!group/prj/kanban/tmpl/PrjTask_List_Tab.html', 
	'text!group/prj/kanban/tmpl/PrjTask_List_Content.html',
	'text!group/prj/kanban/tmpl/PrjTask_List_Header.html',
	'text!group/prj/kanban/tmpl/PrjTask_Popup_Content.html', 
	'text!group/prj/kanban/tmpl/PrjTask_Ent_New.html',
	
	'group/prj/project/ctrl/PrjProjectEnt',
	], function(
			PrjTask_List, 
			PrjTask_List_Tab,
			PrjTask_List_Content,
			PrjTask_List_Header,
			PrjTask_Content_Popup,
			PrjTask_Ent_New,
			
			PrjProjectEnt
	){

	const PrjTaskList = function (grpName, header, content, footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;

		var self					= this;
		//------------------------------------------------------------------------------------
		const pr_SERVICE_CLASS		= "ServicePrjProject"; //to change by your need
		const pr_SV_NEW				= "SVNew"; 
		const pr_SV_SAVE_MOVE		= "SVTaskMove";
		const pr_SV_LST_PRJ			= "SVTaskList";
		
		const pr_SERVICE_CLASS_DYN	= "ServicePrjProjectDyn"; //to change by your need
		const pr_SV_LIST_DYN		= "SVLstPage";
		

		const pr_SERVICE_USER_CLASS = "ServiceAutUser";
		const pr_SV_USER_SEARCH		= "SVLst";

		const pr_SERVICE_GROUP_CLASS= "ServiceNsoGroup";
		const pr_SV_GROUP_SEARCH	= "SVLst";
		//-----------------------------------------------------------------------------------
		const pr_TYPE_CONFIG			= 2000; 

		const pr_NUMBER_RECORD			= 10;

		const pr_TYPE00_PROJECT 		= 10;

		const pr_TYPE02_PRJ 			= 0;
		const pr_TYPE02_EPIC 			= 1;
		const pr_TYPE02_TASK 			= 2;

		const pr_STAT_PRJ_NEW 			= 100100;
		const pr_STAT_PRJ_TODO 			= 100200;
		const pr_STAT_PRJ_INPROGRESS 	= 100300;
		const pr_STAT_PRJ_DONE 			= 100400;
		const pr_STAT_PRJ_TEST 			= 100500;
		const pr_STAT_PRJ_REVIEW 		= 100600;
		const pr_STAT_PRJ_FAIL 			= 100700;
		const pr_STAT_PRJ_UNRESOLVED 	= 100800;
		const pr_STAT_PRJ_CLOSED 		= 100900;


		var paramStatArr				= [
			{id: pr_STAT_PRJ_NEW		, trans: "prj_task_tab_stat_100100"	, show : false	, colorBgr: "#efefef"},
			{id: pr_STAT_PRJ_TODO		, trans: "prj_task_tab_stat_100200"	, show : false	, colorBgr: "#e2dede"},
			{id: pr_STAT_PRJ_INPROGRESS	, trans: "prj_task_tab_stat_100300"	, show : false	, colorBgr: "#c7c7c7"},
			{id: pr_STAT_PRJ_DONE		, trans: "prj_task_tab_stat_100400"	, show : false	, colorBgr: "#D6EAF8"},
			{id: pr_STAT_PRJ_TEST		, trans: "prj_task_tab_stat_100500"	, show : false	, colorBgr: "#C7D1B9"},
			{id: pr_STAT_PRJ_REVIEW		, trans: "prj_task_tab_stat_100600"	, show : false	, colorBgr: "#FCF3CF"},
			{id: pr_STAT_PRJ_FAIL		, trans: "prj_task_tab_stat_100700"	, show : false	, colorBgr: "#ABB2B9"},
			{id: pr_STAT_PRJ_UNRESOLVED	, trans: "prj_task_tab_stat_100800"	, show : false	, colorBgr: "#C6B7D9"},
			{id: pr_STAT_PRJ_CLOSED		, trans: "prj_task_tab_stat_100900"	, show : false	, colorBgr: "#A3E4D7"},
		]

		const paramStatDef 				= {
			[pr_STAT_PRJ_NEW]		: paramStatArr[0],
			[pr_STAT_PRJ_TODO]		: paramStatArr[1],
			[pr_STAT_PRJ_INPROGRESS]: paramStatArr[2],
			[pr_STAT_PRJ_DONE]		: paramStatArr[3],
			[pr_STAT_PRJ_TEST]		: paramStatArr[4],
			[pr_STAT_PRJ_REVIEW]	: paramStatArr[5],
			[pr_STAT_PRJ_FAIL]		: paramStatArr[6],
			[pr_STAT_PRJ_UNRESOLVED]: paramStatArr[7],
			[pr_STAT_PRJ_CLOSED]	: paramStatArr[8],
		}

		var paramStat 					= {...paramStatDef}

		const initialValues = {
			lstPrj 			: [],
			prjMain			: null,
			config			: {
				id			: "prjTask",
				stats		: {
					"-1": [pr_STAT_PRJ_TODO, pr_STAT_PRJ_INPROGRESS, pr_STAT_PRJ_REVIEW, pr_STAT_PRJ_DONE]
				},
				searchKey	: "",
				searchUser	: "",
				group		: null
			}
		}

		var pr_ctr_Main 				= null;
		var pr_ctr_Fav 					= null;

		var groupId 					= null;
		var members                     = {};


		//--------------------APIs--------------------------------------//
		this.do_lc_init	= function(){
			if(!tmplName) {
				App.template.names[pr_grpName] = {}
				tmplName = App.template.names[pr_grpName]
			}

			tmplName.PRJ_TASK_LIST			= pr_grpName + "PrjTask_List";
			tmplName.PRJ_TASK_LIST_TAB		= pr_grpName + "PrjTask_List_Tab";
			tmplName.PRJ_TASK_LIST_CONTENT	= pr_grpName + "PrjTask_List_Content";
			tmplName.PRJ_TASK_TASK_CONTENT	= pr_grpName + "PrjTask_Task_Content";
			tmplName.PRJ_TASK_LIST_HEADER	= pr_grpName + "PrjTask_List_Header";
			tmplName.PRJ_TASK_CONTENT_POPUP	= pr_grpName + "PrjTask_Content_Popup";
			tmplName.PRJ_PROJECT_ENT_NEW	= pr_grpName + "PrjTask_Ent_New";
			
			tmplCtrl.do_lc_put_tmpl(tmplName.PRJ_TASK_LIST				, PrjTask_List); 
			tmplCtrl.do_lc_put_tmpl(tmplName.PRJ_TASK_LIST_TAB			, PrjTask_List_Tab);
			tmplCtrl.do_lc_put_tmpl(tmplName.PRJ_TASK_LIST_CONTENT		, PrjTask_List_Content);
			tmplCtrl.do_lc_put_tmpl(tmplName.PRJ_TASK_LIST_HEADER		, PrjTask_List_Header);
			tmplCtrl.do_lc_put_tmpl(tmplName.PRJ_TASK_CONTENT_POPUP		, PrjTask_Content_Popup);
			tmplCtrl.do_lc_put_tmpl(tmplName.PRJ_PROJECT_ENT_NEW		, PrjTask_Ent_New);
			
			
			pr_ctr_Main 					= App.controller.DBoard.DBoardMain;
			pr_ctr_Fav						= App.controller.UI.Fav
			
			if(!App.controller.PrjProject) App.controller.PrjProject = {}
			if(!App.controller.PrjProject.Ent) {
				App.controller.PrjProject.Ent				= new PrjProjectEnt		("PrjProject", null, "#div_PrjProject_Ent_Header" 	, null);
				App.controller.PrjProject.Ent.do_lc_init();
			} 
		}

		//---------show-----------------------------------------------------------------------------
		var pr_grpPath 		= 'group/prj/_transl';
		var pr_showed		= false;
		this.do_lc_show = function(){
			if (!pr_showed){
				do_gl_lang_append (pr_grpPath, self.do_lc_show_callback, []);
				pr_showed = true;
			}else {
				self.do_lc_show_callback();
			}
		};

		this.do_lc_show_callback = function(){               
			try{
				App.router.controller.do_lc_append_custom_tags()

				do_lc_init_config();

				groupId = req_gl_Url_Params()['groupId'];
				if(!groupId || groupId < 0) {
					groupId = initialValues.config.group
				}
				
				do_lc_load_view();
				do_lc_load_header();
				do_lc_get_list_prj_ofUser();
			}catch(e) {				
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "prj.project", "PrjTaskList", "do_lc_show", e.toString()) ;
			}
		};

		const do_lc_init_config = () => {
			let config = null;

			if(pr_ctr_Fav) {
				const lstConf = pr_ctr_Fav.req_lc_myFavorites(pr_TYPE_CONFIG)

				if(lstConf.length > 0) {
					config = lstConf.find(e => e.id === "prjTask")

					if(config) {
						delete config.fav
						localStorage.setItem("prjTaskConfig", JSON.stringify(config));
					}
				}
			}

			// if(!config) {
			// 	config = JSON.parse(localStorage.getItem("prjTaskConfig"))
			// }
		
			if(!config) {
				paramStat[pr_STAT_PRJ_TODO]			.show = true;
				paramStat[pr_STAT_PRJ_INPROGRESS]	.show = true;
				paramStat[pr_STAT_PRJ_REVIEW]		.show = true;
				paramStat[pr_STAT_PRJ_DONE]			.show = true;

				do_lc_mod_config_attr('stats', initialValues.config.stats)
				return;
			};

			initialValues.config = config;

			if(!initialValues.config.group) {
				initialValues.config.group = ""
			}

			let grpStats = initialValues.config.stats[initialValues.config.group]
			if(!grpStats || grpStats.length === 0) {
				grpStats = [
					pr_STAT_PRJ_TODO,
					pr_STAT_PRJ_INPROGRESS,
					pr_STAT_PRJ_REVIEW,
					pr_STAT_PRJ_DONE
				]
				initialValues.config.stats[initialValues.config.group] = grpStats
				do_lc_mod_config_attr('stats', initialValues.config.stats)
			}

			grpStats.forEach(s => {
				if(!paramStat[s]) return
				else paramStat[s].show = true
			})
		}

		const do_lc_mod_config_attr = (attr, val) => {
			initialValues.config[attr] = val

			localStorage.setItem("prjTaskConfig", JSON.stringify(initialValues.config))
			
			if(pr_ctr_Fav) {
				pr_ctr_Fav.do_lc_push_myFavorites(initialValues.config, pr_TYPE_CONFIG, Object.keys(initialValues.config), true)
			}
		}

		const do_lc_req_config_attr = (attr) => {
			return initialValues.config[attr]
		}

		const do_lc_load_view = () => {	
			$("#div_main_content").html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_TASK_LIST, {}));
		}
		
		const req_lst_sort_alphab = () => {
			let tmp = initialValues.lstPrj;
			if (tmp && tmp.length > 0) {
				tmp.sort(function(a, b) {
					return a.name.toLowerCase().localeCompare(b.name.toLowerCase());
				});

				tmp.forEach((e) => {
					if (e.typ02 != pr_TYPE02_PRJ) {
						e.name = "---" + e.name;
					}
				})
			}
			
			return tmp;
		}

		const do_req_obj_sort_by_ord = (obj) => {
			let arr = Object.values(obj);

			function compareByOrd(obj1, obj2) {
				const ord1 = obj1.ord || Number.MAX_SAFE_INTEGER; // Handle missing ord
				const ord2 = obj2.ord || Number.MAX_SAFE_INTEGER; // Handle missing ord
			
				return ord1 - ord2;
			}

			return arr.sort(compareByOrd);
		}

		const do_lc_req_set_lstStats = (grpId) => {
			if(!grpId) return

			const grp = initialValues.lstPrj.find(e => +e.id === +grpId)
			Object.values(paramStatDef).forEach(s => s.show = false)
			paramStat = {...paramStatDef}

			if(!grp) {
				const lstStatsAll = do_lc_req_config_attr("stats")[-1]
				if(lstStatsAll) {
					for(const s of lstStatsAll) {
						paramStat[s].show = true
					}
				}
				paramStatArr = Object.values(paramStat)
				return
			}

			if(!grp.inf03 || !do_lc_req_config_attr("stats")[grpId]) {
				paramStat[pr_STAT_PRJ_TODO]			.show = true;
				paramStat[pr_STAT_PRJ_INPROGRESS]	.show = true;
				paramStat[pr_STAT_PRJ_REVIEW]		.show = true;
				paramStat[pr_STAT_PRJ_DONE]			.show = true;
				paramStatArr = Object.values(paramStat)
				return
			}

			if(typeof grp.inf03 === 'string') {
				grp.inf03 = JSON.parse(grp.inf03)
			}

			if(!grp.inf03.stats || !do_lc_req_config_attr("stats")[grpId]) {
				paramStatArr = Object.values(paramStat)
				return
			}

			const stats = grp.inf03.stats

			for (let i = 0; i < stats.length; i++) {
				if(!stats[i]) continue

				const id = stats[i].id
				
				if(!stats[i].show || stats[i].show != 1) {
					delete paramStat[id]
					continue
				}

				if(!paramStat[id]) paramStat[id] = {id: id, trans: null, custom: true, lab: stats[i].lab, show : false, colorBgr: "#efefef"}
				
				const initShow = do_lc_req_config_attr("stats")[grpId].includes(id)
				paramStat[id].show = initShow
			}

			paramStatArr = do_req_obj_sort_by_ord(paramStat)
		}

		const do_lc_load_header = () => {
			let lst = req_lst_sort_alphab();

			do_lc_req_set_lstStats(groupId)

			$("#div_task_header").html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_TASK_LIST_HEADER, {
				lstStats: paramStatArr,
				lst: lst,
				group: groupId
			}));
			do_lc_bind_eventPage();
			do_lc_bind_event_new_task();
			do_gl_apply_right($("#div_main_content"));
			do_lc_req_list_by_stat()
		}

		const do_lc_req_list_by_stat = () => {
			$("#div_task_content").html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_TASK_LIST_TAB, paramStatArr));
			
			do_gl_initSwiper();
			
			let div_kanboard = [];
			for(const obj of paramStatArr){
				if(!obj.show)	continue;
				do_get_list_ByAjax(obj);
				div_kanboard.push(document.getElementById(`div_prj_list_stat_${obj.id}`));
			}

			const drake = dragula(div_kanboard);

			drake.on('drop', (el, target, source, sibling) => {
				let statTo 		= $(target).data("stat");
				let {id} 		= $(el).data();
				let params 		= {id, statTo};

				do_lc_save_task_drag(params);
			})
		}

		const do_lc_bind_event_new_task = () => {
			$(".btn_new_task_kanban").off("click").on("click", () => {
				var 	obj 		= {files: []};
				const 	lstStats 	= [...paramStatArr]
				lstStats.map(e => e.show = true)

				App.MsgboxController.do_lc_show({
					title		: $.i18n("prj_project_add_task_popup"),
					content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_NEW, {epics : initialValues.lstPrj, typ02: pr_TYPE02_TASK, currencys: App.data.currencys, epicSelected: groupId || do_lc_req_config_attr("group"), paramStat: lstStats}),
					autoclose	: false,
					buttons		: {
						OK: {
							lab		: $.i18n("common_btn_save"),
							funct	: do_lc_save_new_task,
							param	: [obj],
							autoclose	: false,
							classBtn	: "btn-primary"
						},
						NO: {
							lab		:  $.i18n("common_btn_cancel"),
						}
					},
					bindEvent: function() {
						App.SummerNoteController.do_lc_show("#div_create_prj", {dialogsInBody: true});
						do_lc_bind_event_task_popup_prj(obj);
						do_gl_init_repeater();
						// do_gl_handle_member_external($("#div_create_prj"));
						$("#projectepic").select2();
						$(".select2").addClass("maxw");
					}
				});	
			});

			$(".prj-toggle-stat").off("click").on("click", () => {
				$("#div_choose_stat").toggleClass("show");
			});

			$(".prj-toggle-search").off("click").on("click", () => {
				$(".div-search").toggleClass("show");
				$(".select2").toggleClass("maxw");
			});
		}

		const do_binding_event_task = () => {

			// $(".task-item-name").off("click").on("click", function(event){
			// 	if($(event.target).hasClass("task-option"))	return;

			// 	let $this = $(this);
			// 	if ($this.hasClass('ui-draggable-dragging'))	return;

			// 	let {id, code} = $this.data();
			// 	id && pr_ctr_Main.do_lc_switch_mobile_or_pc(`view_prj_project_content.html?id=${id}&code=${code}`, "VI_MAIN/"+ App.router.part.PRJ_PROJECT_ENT, [id], '_blank');
			// });

			$(".btn-resize").off("click").on("click", function(){
				let $this 		= $(this);
				let child		= $this.find("i");
				let {divtoggle} = $this.data();

				$(divtoggle)	.toggle("hide");
				child			.toggleClass("mdi-window-minimize mdi-window-maximize")
			})	

			$(".view-all-task-prj").off("click").on("click", function(){
				let {group, code} = $(this).data();
				if(group){
					do_lc_mod_config_attr('group', group)
					groupId = initialValues.config.group
					do_lc_req_list_by_stat();
					$(".inp-search, .inp-search-responsive").val(code);
				}
			})

			$(".remove-task").off("click").on("click", function(){
				let $this 					= $(this);
				let {id, stat: statFrom} 	= $this.data();
				if(id){
					let params = {id, statFrom,  statTo: pr_STAT_PRJ_UNRESOLVED};
					do_lc_save_change_stat(params);
				}
			})
			
			$(".todo-task").off("click").on("click", function() {
				let $this 					= $(this);
				let {id, stat: statFrom} 	= $this.data();
				if(id){
					let params = {id, statFrom,  statTo: pr_STAT_PRJ_INPROGRESS};
					do_lc_save_change_stat(params);
				}
			})
			
			
			$(".review-task").off("click").on("click", function() {
				let $this 					= $(this);
				let {id, stat: statFrom} 	= $this.data();
				if(id){
					let params = {id, statFrom,  statTo: pr_STAT_PRJ_REVIEW};
					do_lc_save_change_stat(params);
				}
			})

			$(".complete-task").off("click").on("click", function() {
				let $this 					= $(this);
				let {id, stat: statFrom} 	= $this.data();
				if(id){
					let params = {id, statFrom,  statTo: pr_STAT_PRJ_DONE};
					do_lc_save_change_stat(params);
				}
			})

			$(".close-task").off("click").on("click", function() {
				let $this 					= $(this);
				let {id, stat: statFrom} 	= $this.data();
				if(id){
					const params = {id, statFrom,  statTo: pr_STAT_PRJ_CLOSED};
					do_lc_save_change_stat(params);
				}
			})
			
			$(".btn-refrest-list").off("click").on("click", function(){
				const {stat} = $(this).data();
				do_get_list_ByAjax(paramStat[stat], true);
			})
			
			// $(".btn-close-list").off("click").on("click", function(){
			// 	const {stat} = $(this).data();
			// 	$(`#div-stat-task_${stat}`).remove();
			// 	$(`#customCheck${stat}`).prop('checked', false);
			// 	paramStat[stat].isShow 	= false;

			// 	const iStatArr = paramStatArr.findIndex(e => +e.stat === +stat)
			// 	$($("li.task-stat-cbx")[iStatArr]).removeClass("active")
			// 	$(`#choose_multi_stat > option[value=${stat}]`).removeAttr("selected")
			// 	// $(`#choose_multi_stat > option[value=${stat}]`).trigger('change')

			// 	// const labelStr 		= $($("li.task-stat-cbx")[iStatArr]).find('label').text().trim()
			// 	// let replacedStr 	= $(".multiselect-selected-text").text().replace(labelStr, "")
			// 	// if(replacedStr.startsWith(",")) replacedStr = replacedStr.substring(1)
			// 	pr_multiSelect.updateButtonText()

			// 	const statsConfig 		= do_lc_req_config_attr('stats');
			// 	const iStat				= statsConfig.findIndex(e => +e === +stat);
			// 	if(iStat < 0) return
			// 	statsConfig.splice(iStat, 1)
			// 	do_lc_mod_config_attr('stats', statsConfig)
			// }) 
			
			$(".horizontal-menu-btn").off("click").on("click", function(e){
				$(this).closest(".div-stat-task").toggleClass('col-xl-3 col-lg-6').toggleClass('col-xl-6 col-lg-12');
			})
			
			$(".copy_code").off("click").on("click", function(){
				const {id} = $(this).data();
				let element = `#task_code_${id}`;
				let $temp = $("<input>");
			 	$("body").append($temp);
			 	$temp.val($(element).text()).select();
			 	document.execCommand("copy");
			 	$temp.remove();
			 	do_gl_show_Notify_Msg_Success($.i18n('common_btn_copy_success'));
			}) 
			
			$(".show_content_task").off("click").on("click", function(){
				const {id,code}    = $(this).data();
				const isPopup = true;
				App.MsgboxController.do_lc_show({
					title		: $.i18n("prj_project_sidebar_task"),
					content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_TASK_CONTENT_POPUP, {}),	
					autoclose	: false,
					buttons		: {
						OK: {
							lab		: $.i18n("common_btn_ok"),
							autoclose	: false,
							classBtn	: "btn-primary"
						},
					},
				});	
				
				App.controller.PrjProject.Ent.do_lc_show(id, code, "#div_task_content_popup", pr_TYPE02_PRJ, isPopup);
			}) 

			App.router.controller.do_lc_binding_route()
		}

		const do_lc_save_new_task = function(obj){
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
			objNew.parent 	= objNew.parent;
			objNew.typ02	= pr_TYPE02_TASK;
			objNew.files	= obj.files;

			objNew.dtBegin 	= do_lc_convert_date(objNew.dtBegin);
			objNew.dtEnd 	= do_lc_convert_date(objNew.dtEnd);

			do_lc_transform_descr02(objNew);

			do_lc_create_task(objNew);
		}

		const do_lc_transform_descr02 = data => {
			if(!data.descr02)	return;
			const list = Object.values(data.descr02).reduce((curr, item) => {
				(item && item.trim().length) && curr.push({item}); return curr;
			}, [])

			data.descr02 = JSON.stringify(list);
		}

		const do_lc_convert_date = objDate => objDate.date.substr(0, 10) + " " + objDate.time.substr(0, 5) + ":00";

		const do_lc_create_task = function(obj){
			let ref 		= req_gl_Request_Content_Send(pr_SERVICE_CLASS, pr_SV_NEW);			
			ref["obj"]		= JSON.stringify(obj);
			ref["member"]	= JSON.stringify(Object.values(members));
			ref["group"]	= JSON.stringify(Object.values(groups));
			ref["frView"]	= 1;

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_after_create_task, []));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_after_create_task = function(sharedJson){
			if(can_gl_AjaxSuccess(sharedJson)) {	
				do_gl_show_Notify_Msg_Success 	($.i18n('common_ok_msg_save'));
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
			App.MsgboxController.do_lc_close();
		}

		const do_lc_bind_event_task_popup_prj = function(obj){
			members = {};
			groups  = {};

			let option_ava		= {
					fileinput	: {maxFiles : 1, param : {typ01: 1, typ02: 1} },//option here
					obj			: obj//file existing here
			}
			do_gl_init_fileDropzone($("#div_create_prj #div_prj_avatar"), option_ava);

			let option = {
					fileinput		: {},//option here
					obj				: obj//file existing here
			}
			do_gl_init_fileDropzone($("#div_create_prj #div_prj_docs"), option);
			do_lc_req_autocompleteEpicTask();

			$("#dtpicker_Begin").datepicker().datepicker("setDate", new Date());
			$("#dtpicker_End").datepicker().datepicker("setDate", new Date());
			
			$("#tmpicker_Begin").timepicker({//timepicker
				showMeridian: false,
				defaultTime :'7:00',
				icons		: {
					up		: "mdi mdi-chevron-up",
					down	: "mdi mdi-chevron-down"
				}
			});
			
			$("#tmpicker_End").timepicker({//timepicker
				showMeridian: false,
				defaultTime :'19:00',
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
			let reqSelectMember = function(item){
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
				if(item.avatar) selOpt 			+= `<div><img src='${item.avatar.urlPrev}' class='rounded-circle avatar-xs'/> ${item.login01}`;
				else 			selOpt 			+= `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-white mr-1 text-uppercase text-center" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div> ${item.login01}`;
				selOpt 				+= `<a data-id='${item.id}' class='text-danger btn-remove-member' data-toggle='tooltip' data-placement='top' title='' data-original-title='Delete'><i class='mdi mdi-close font-size-18'></i></a>`;
				selOpt 				+= `</div></div>`;
				
				$("#div_list_member").append(selOpt);
				do_lc_bind_event_autocompleteEpicTask();
				$(el).blur().val("");
			}

			let typ01Arr = []
			let typ01Str = ""
			const typ01 = App.data.user.typ01
			for (const x of [1, 2, 3]) {
				if(typ01 <= x) typ01Arr.push(x)
			}
			typ01Str = typ01Arr.join(',')

			let options = {
				// appendTo		: ".msg-box",
				dataService 	: [pr_SERVICE_USER_CLASS, pr_SV_USER_SEARCH], 
				dataRes 		: ["login01", "name01"],
				dataReq			: {nbLine: 5, typ01s: typ01Str, stats: 1},
				selectCallback	: reqSelectMember, 
				arrSource		: do_lc_customLst_user_autocomplete
			}
			do_gl_set_input_autocomplete(el, options);
			
			
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

			let reqSelectGroup = (item) => {
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
				do_lc_bind_event_autocomplete_group();
			}

			let optionsG = {
					dataService : [pr_SERVICE_GROUP_CLASS, pr_SV_GROUP_SEARCH], 
					dataRes 		: ["name"],
					dataReq			: {nbLine:5, typ01s: 300},
					selectCallback	: reqSelectGroup, 
					customShowList	: customShowListGroup
			}
			do_gl_set_input_autocomplete(elG, optionsG);
		}

		const do_lc_bind_event_autocompleteEpicTask = function(){
			$(".btn-remove-member").off("click").on("click", function(){
				let $this 	= $(this);
				let parent 	= $this.parent();
				let {id} 	= $this.data();

				if(members[id])	delete members[id];
				parent.remove();
			})
		}
		
		var do_lc_bind_event_autocomplete_group = function(){
			$(".btn-remove-group").off("click").on("click", function(){
				let $this 		= $(this);
				let parent 	    = $this.parent();
				let {id} 		= $this.data();

				if(groups[id])	delete groups[id];
				parent.remove();
			})
		}

		const do_lc_customLst_user_autocomplete = function(item, selOpt = ""){
			if(item.avatar) return selOpt 		+= `<img src='${item.avatar.urlPrev}' class='rounded-circle avatar-xs'/> ${item.login}`;
			if(!item.avatar){
				let textColor   = null;
				let textAvatar  = null
				if(!item.avatar){
					let first = item.login.charAt(0);
					let last  = item.login.charAt(item.login.length - 1);
					let index = var_gl_alphabet.indexOf(first.toLowerCase());
					
					textColor = var_gl_colors[index];
					textAvatar= first + last;
				}
				selOpt 			+= `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-white text-uppercase text-center mr-1" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div> ${item.login}</div>`;
				return selOpt;
			}
		}

		const do_lc_bind_eventPage = () => {
			$("#sel_prj_filter").select2();

			pr_multiSelect = $("#choose_multi_stat").multiselect();
			
			$(".task-stat-cbx").off("change").on("change", function(){
				let val 				= +$(this).find('input').val();
				let option				= $(`#choose_multi_stat > option[value=${val}]`)
				let isSelected 			= $(option).is(":selected");

				const sInd 				= paramStatArr.findIndex(s => s.id === val)
				paramStatArr[sInd].show = isSelected ? true : false;
				
				const grpConfig			= do_lc_req_config_attr('group') || -1;
				const statsConfig 		= do_lc_req_config_attr('stats');
				const iStat				= statsConfig[grpConfig].findIndex(e => +e === val)
				if(iStat >= 0) {
					statsConfig[grpConfig].splice(iStat, 1)
					do_lc_req_list_by_stat()
				} else {
					statsConfig[grpConfig].push(val)
				}

				do_lc_mod_config_attr('stats', statsConfig)
				do_lc_req_list_by_stat()
			})

			$(".inp-search, .inp-search-responsive").off("keyup").on("keyup", function(e){
				e.preventDefault();
				if(VIEW_PART !==  App.router.part.PRJ_TASK_LIST)	return false;//add foreach view prj search

				do_lc_mod_config_attr('group', -1)
				groupId = initialValues.config.group
				do_lc_mod_config_attr('searchKey', $(this).val())
				do_gl_execute_debounce(do_lc_req_list_by_stat);
			})
			
			$("#btn_search_responsive").off("click").on("click", function(e){
				if(VIEW_PART !==  App.router.part.PRJ_TASK_LIST)	return false;//add foreach view prj search
				e.preventDefault();

				do_lc_mod_config_attr('group', -1)
				groupId = initialValues.config.group

				let searchNormal 	= $(".inp-search").hasClass("d-none");
				const searchkey 	= searchNormal ? $(".inp-search").val() : $(".inp-search-responsive").val();
				do_lc_mod_config_attr('searchKey', searchkey)
				do_lc_req_list_by_stat();
			})

			$("#inp_search_user").off("keyup").on("keyup", function(e){
				if(VIEW_PART !==  App.router.part.PRJ_TASK_LIST)	return false;//add foreach view prj search

				do_lc_mod_config_attr('group'		, -1)
				groupId = initialValues.config.group
				do_lc_mod_config_attr('searchUser'	, $(this).val());
				do_lc_mod_config_attr('searchKey'	, $(this).val());
				do_gl_execute_debounce(do_lc_req_list_by_stat);
			})
			
			$("#sel_prj_filter").on("change", function(){
				const group = $(this).val();
				do_lc_mod_config_attr('group', !group ? -1 : group)
				groupId = initialValues.config.group

				let grpStats = initialValues.config.stats[groupId]
				if(!grpStats || grpStats.length === 0) {
					grpStats = [
						pr_STAT_PRJ_TODO,
						pr_STAT_PRJ_INPROGRESS,
						pr_STAT_PRJ_REVIEW,
						pr_STAT_PRJ_DONE
					]
					initialValues.config.stats[groupId] = grpStats
					do_lc_mod_config_attr('stats', initialValues.config.stats)
				}

				do_lc_load_header();
			})
		}

		const do_get_list_ByAjax = (params, forced) => {	
			let {id} 				= params;
			let dataSend			= {
				stats: id+"", 
				typ00: pr_TYPE00_PROJECT, 
				typ02: pr_TYPE02_TASK, 
				searchKey: do_lc_req_config_attr('searchKey'), 
				group: (!groupId || groupId < 0) ? null : groupId, 
				searchUser: (do_lc_req_config_attr('searchUser') && do_lc_req_config_attr('searchUser').length) ? do_lc_req_config_attr('searchUser') : null, 
				wAva : false, 
				wParent: true, 
				forced
			};

			let ref 				= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS_DYN, pr_SV_LIST_DYN, dataSend);

			let divMain 			= `#div_prj_list_stat_${id}`;
			let divPagination 		= `#div_prj_pagination_stat_${id}`;

			let callbackFunct 		= (data) => {
				do_lc_show_list_callback(data, divMain);
				if ($("body, html").scrollTop() !=0)	$("body, html").animate({scrollTop :0}, 500);
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

		const do_lc_bind_event_ParentPrj = () => {
			const lstPrj = initialValues.lstPrj

			if(!lstPrj || lstPrj.length <= 0) return;

			$(".par_prj_item").each((i, e) => {
				const grp = $(e).data("grp")

				const parObj = lstPrj.find(e => +e.id === +grp)

				if(parObj) {
					const url = $(e).data("url").replace("#code", parObj.code01).replace("#id", parObj.id)
					
					$(e).data("url", url)
				}
			})
		}

		const do_lc_show_list_callback = (sharedJson, div) => {
			let data 			= {};
			if (can_gl_AjaxSuccess(sharedJson)) {
				let tasks		= sharedJson[App['const'].RES_DATA];
				if(tasks.lst) data = do_lc_remove_mem_same(tasks);
				$(div)	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_TASK_LIST_CONTENT		, data));
				do_binding_event_task();
			} else $(div).html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_TASK_LIST_CONTENT		, {}));		
		}
		
		const do_lc_remove_mem_same = tasks => {
			tasks.lst = tasks.lst.map(item => {
				if(!item.members) return item;
				let objMem = item.members.reduce((curr, mem) => {
					curr[mem.id] = mem;
					return curr;
				}, {});
				
				item.members = Object.values(objMem);
				return item;
			})
			
			return tasks;
		}

		const do_lc_save_change_stat = params => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_SAVE_MOVE, params);	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_after_change_stat, [params]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_after_change_stat = (sharedJson, params) => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_gl_show_Notify_Msg_Success($.i18n('common_ok_msg_save'));
				const {statFrom, statTo} = params;
//				paramStat[statFrom].isShow && do_get_list_ByAjax(paramStat[statFrom], true);
//				paramStat[statTo].isShow && do_get_list_ByAjax(paramStat[statTo], true);
				
				paramStat[statFrom].isShow && do_lc_modify_interface(params.id, true);
				paramStat[statTo].isShow && do_get_list_ByAjax(paramStat[statTo], true);
				
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}
		
		const do_lc_modify_interface = (id, hide) => {
			if(hide){
				$(".task-item[data-id='" + id +"']").hide();
			}
			
		}
		

		const do_lc_save_task_drag = params => {
			let ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_SAVE_MOVE, params);	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterDrag, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_afterDrag = sharedJson => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				do_gl_show_Notify_Msg_Success($.i18n('common_ok_msg_save'));
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}

		const do_lc_get_list_prj_ofUser = () => {
			const ref 		= req_gl_Request_Content_Send_With_Params(pr_SERVICE_CLASS, pr_SV_LST_PRJ);	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_after_get_list_prj_ofUser, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_after_get_list_prj_ofUser = (sharedJson) => {
			if(can_gl_AjaxSuccess(sharedJson)) {
				let data = sharedJson[App['const'].RES_DATA];
				if(data && data.length)	initialValues.lstPrj = data;
				do_lc_bind_event_ParentPrj();
				do_lc_load_header()
			}
		}

	};

	return PrjTaskList;
});