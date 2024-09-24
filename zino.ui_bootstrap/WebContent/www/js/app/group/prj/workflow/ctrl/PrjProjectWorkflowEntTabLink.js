define([
	
        'text!group/prj/workflow/tmpl/PrjWorkflow_Ent_Tab_Link.html',

	],
	function(	
            PrjProject_Ent_Tab_Link
	){

    var PrjProjectWorkflowEntTabLink 	= function (grpName, header, content, footer) {
        const tmplName				= App.template.names;
	    const tmplCtrl				= App.template.controller;
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
        
        const pr_ctr_Main 				= App.controller.UI.Main;
        const pr_ctr_Ent				= App.controller.PrjWorkflow.Ent;

        tmplName.PRJ_PROJECT_ENT_TAB_LINK = "PrjProject_Ent_Tab_Link";

        var do_lc_load_view = function(){
            members[App.data.user.id] = {"id": App.data.user.id, "lev": pr_member_lev_manager, "typ": pr_member_typ_high};
            tmplCtrl				.do_lc_put_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_LINK			, PrjProject_Ent_Tab_Link);
        }

        this.do_lc_get_list_member = function(prj){
            let ref 		= req_gl_Request_Content_Send_With_Params("ServicePrjProject", "SVTaskGet", {id: prj.id});	

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
            } else {   
                do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
            }
        }

        var do_lc_bind_event_members_prj = function(members, idPrj){
            pr_MEM_TEMP = $.extend(false, {}, members);

            $("#btn_add_link").off("click").on("click", function(){
                $(".action-item-link").removeClass("hide");
                $(this).addClass("hide");
            })

            $("#a_btn_save_link").off("click").on("click", function(){
                do_lc_save_member_prj(members, idPrj);
            })

            $("#a_btn_cancel_link").off("click").on("click", function(){
                do_lc_show_prj_member(members, idPrj);
            })

            $(".link-edit").off("click").on("click", function(){
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
                    $(".action-item-link").removeClass("hide");
                }
            })

            $(".link-delete").off("click").on("click", function(){
                let {memid} = $(this).data();
                let mem 	= pr_MEM_TEMP[memid];
                if(mem){
                    delete pr_MEM_TEMP[memid];
                    $(this).closest("tr").remove();
                    $(".action-item-link").removeClass("hide");
                }
            })

            let el = "#inp_list_task";
            let reqSelectTask = function(event, item){
                if(pr_MEM_TEMP[item.id])			return false;

                let mem 		= {"ent02": item, "entId02": item.id, "entId01": idPrj, "entTyp01": pr_ENT_TYP_USER};

                pr_MEM_TEMP[item.id] = mem;
                let selOpt 		= `<tr>`;
                selOpt 			+= `<td><a data-id='${item.id}' class='text-danger btn-remove-member' data-toggle='tooltip' data-placement='top' title='' data-original-title='Delete'><i class='mdi mdi-close font-size-18'></i></a></td>`;
                
                selOpt 			+= `<td>` + item.code 	+`</td>`;
                selOpt 			+= `<td>` + item.name	+`</td>`;
                selOpt 			+= `</tr>`;

                $("#tabLink table tbody").append(selOpt);
                do_lc_bind_event_autocomplete(pr_MEM_TEMP);
                $(el).blur().val("");
            }

            let options = {
                    dataService : ["ServicePrjProject", "SVTaskListSearch"], fSelect: reqSelectTask, customShowList: do_lc_customLst_item_autocomplete
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
            let ref 		= req_gl_Request_Content_Send_With_Params("ServicePrjProject", "SVTaskSave", {idPrj, members: JSON.stringify(Object.values(pr_MEM_TEMP))});	

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

        var do_lc_customLst_item_autocomplete = function(item, selOpt = ""){
            if(item){
                selOpt 		+= `<div class="media align-items-center">${item.code} ${item.name}</div>`;
            }
            return selOpt;
        }
        
        var do_lc_show_prj_member = function(members, idPrj){
            do_lc_load_view();
            $("#div_prj_link")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_PROJECT_ENT_TAB_LINK			, members));
            do_lc_bind_event_members_prj(members, idPrj);
            do_lc_bind_event_resize();
            pr_ctr_Ent.do_lc_reqRole_User_Workflow();
        }

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
        //------------------------------End list member-----------------------------------
    }

	return PrjProjectWorkflowEntTabLink;
});