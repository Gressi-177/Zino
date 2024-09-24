define([
  "text!group/user_group/tmpl/PrjUserGroup_Member.html",
  //	'text!group/user_group/tmpl/PrjUserGroup_Member_Detail.html',
], function (
  PrjUserGroup_Member
  //			PrjUserGroup_Member_Detail
) {
  const PrjUserGroupMember = function (grpName, header, content, footer) {
    var pr_divHeader = header;
    var pr_divContent = content;
    var pr_divFooter = footer;

    //------------------------------------------------------------------------------------
    var pr_grpName = grpName ? grpName : new Date().getTime() + "";
    var tmplName = App.template.names[pr_grpName];
    var tmplCtrl = App.template.controller;
    //------------------------------------------------------------------------------------
    const pr_SERVICE_CLASS = "ServiceMsgMessage"; //to change by your need
    const pr_SV_MEMBER_LIST = "SVMemberLst";
    const pr_SV_MEMBER_LIST_WAITING = "SVMemberLstWaiting";

    const pr_SERVICE_PER_CLASS = "ServiceAutUser";
    const pr_SV_USER_SEARCH = "SVLst";

    const pr_SERVICE_CLASS_NSO_GROUP = "ServiceNsoGroup"; //to change by your need
    const pr_SV_MEMBER_NOT_VALIDATED = "SVNsoGroupDelEnt";
    const pr_SV_MEMBER_VALIDATED = "SVNsoGroupValidated";
    const pr_SV_MEMBER_TRANSFORM_MAN = "SVNsoGroupTransform";
    const pr_SV_MEMBER_SAVE = "SVWorkSaveMember";

    var self = this;
    var pr_PAGESIZE = 10;
    const pr_STAT_WAITING = 1;
    const pr_STAT_VALIDATED = 2;
    //------------------controllers------------------------------------------------------
    var pr_ctr_Main = null;
    var pr_ctr_ChatRoomChat = null;
    //-----------------------------------------------------------------------------------
    var pr_MEM_TEMP = {};

    const pr_member_lev_manager = 0;
    const pr_member_lev_worker = 2;
    const pr_member_lev_owner = 10;

    const CHAT_GROUP_PRIVATE = 401;
    const CHAT_GROUP_PUBLIC = 402;

    const pr_TYP_CHAT_USER = 1;

    const PRJ_MEMBER_LEVEL = {
      0: "prj_project_member_level_manager",
      1: "prj_project_member_level_reporter",
      2: "prj_project_member_level_worker",
    };

    //--------------------APIs--------------------------------------//
    this.do_lc_init = function () {
      if (!tmplName) {
        App.template.names[pr_grpName] = {};
        tmplName = App.template.names[pr_grpName];
      }

      pr_ctr_Main = App.controller.UI.Main;
      pr_ctr_Main = App.controller.PrjUserGroup.Main;
      pr_ctr_List = App.controller.PrjUserGroup.List;

      tmplName.PRJ_USER_GROUP_MEMBER = "PrjUserGroup_Member";
      tmplName.PRJ_USER_GROUP_MEMBER_DETAIL = "PrjUserGroup_Member_Detail";
    };

    const initialValues = {
      members: {},
      group: {},
    };

    //---------show-----------------------------------------------------------------------------
    this.do_lc_show = function (group) {
      try {
        do_lc_init_values(group);
        do_lc_load_view();
        do_get_list_member();
        do_lc_setTime_refresh_member_lst();
      } catch (e) {
        console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "prj.project", "PrjUserGroupMember", "do_lc_show", e.toString()) ;
      }
    };

    const do_lc_init_values = (group) => {
      initialValues.group = group;
      initialValues.members = {};
    };

    const do_lc_build_page = () => {
      do_lc_build_table_member();
    };

    const do_lc_build_table_member = () => {
      $("#div_usergroup_member").html(
        tmplCtrl.req_lc_compile_tmpl(
          tmplName.PRJ_USER_GROUP_MEMBER,
          initialValues.members
        )
      );
      do_lc_bind_event_member(initialValues.members, initialValues.group.id);
    };

    var do_lc_bind_event_member = function (members, idGroup) {
      pr_MEM_TEMP = $.extend(false, {}, members);

      $("#btn_add_member")
        .off("click")
        .on("click", function () {
          $(".action-item-member").removeClass("hide");
          $(this).addClass("hide");
        });

      $("#a_btn_save_member")
        .off("click")
        .on("click", function () {
          do_lc_save_member_toGroup(members, idGroup);
        });

      $("#a_btn_cancel_member")
        .off("click")
        .on("click", function () {
          do_lc_build_table_member();
        });

      $(".member-edit")
        .off("click")
        .on("click", function () {
          let $this = $(this);
          let { memid } = $this.data();
          let mem = pr_MEM_TEMP[memid];
          if (mem) {
            let parentTR = $this.closest("tr");
            parentTR.find(".content-member").addClass("hide");
            parentTR.find(".edit-member").removeClass("hide");
            let divLev = parentTR.find(".level-edit");
            do_lc_bindEvent_tabMemberEdit(memid, divLev);
            $(".action-mem").removeClass("hide");
          }
        });

      $(".member-delete")
        .off("click")
        .on("click", function () {
          let { memid } = $(this).data();
          let mem = pr_MEM_TEMP[memid];
          if (mem) {
            delete pr_MEM_TEMP[memid];
            $(this).closest("tr").remove();
            $(".action-mem").removeClass("hide");
          }
        });

      $(".btn-resize")
        .off("click")
        .on("click", function () {
          let $this = $(this);
          let child = $this.find("i");
          let { divtoggle } = $this.data();

          $(divtoggle).toggle("hide");
          child.toggleClass("mdi-window-minimize mdi-window-maximize");
        });

      let el = "#inp_name_member";
      let reqSelectMember = function (event, item) {
        if (pr_MEM_TEMP[item.id]) return false;

        let typ = $("#sel_member_level").val();
        let mem = {
          typ: typ,
          mem: item,
          uId: item.id,
          group: idGroup,
          stat: pr_STAT_VALIDATED,
        };
        let strlogin =
          item.login01.length > 4 ? item.login01.substr(0, 4) + "..." : item.login01;

        let textColor = null;
        let textAvatar = null;
        if (!item.avatar) {
          let first = item.login01.charAt(0);
          let last = item.login01.charAt(item.login01.length - 1);
          let index = var_gl_alphabet.indexOf(first.toLowerCase());

          textColor = var_gl_colors[index];
          textAvatar = first + last;
        }

        pr_MEM_TEMP[item.id] = mem;
        let selOpt = `<tr>`;
        selOpt += `<td><a data-id='${item.id}' class='text-danger btn-remove-member' data-toggle='tooltip' data-placement='top' title='' data-original-title='Delete'><i class='mdi mdi-close font-size-18'></i></a></td>`;

        if (item.avatar)
          selOpt += `<td style='width: 50px;'><img src='${item.avatar.urlPrev}' class='rounded-circle avatar-xs' alt=''/></td>`;
        else
          selOpt += `<td style='width: 50px;'> <div class="rounded-circle avatar-xs text-white text-uppercase text-center" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div></td>`;
        selOpt += `<td><h5 class='font-size-14 m-0'><a href='' class='text-dark'>${strlogin}</a></h5></td>`;
        selOpt += `<td>` + $.i18n(PRJ_MEMBER_LEVEL[+typ]) + `</td>`;

        selOpt += `</tr>`;

        $("#tabMember table tbody").append(selOpt);
        do_lc_bind_event_autocomplete(pr_MEM_TEMP);
        $(el).blur().val("");
      };

      let options = {
        dataService: [pr_SERVICE_PER_CLASS, pr_SV_USER_SEARCH],
        fSelect: reqSelectMember,
        customShowList: do_lc_customLst_user_autocomplete,
      };
      do_gl_req_autocompleteNew(el, options);
    };

    var do_lc_bind_event_autocomplete = function (pr_MEM_TEMP) {
      $(".btn-remove-member")
        .off("click")
        .on("click", function () {
          let $this = $(this);
          let parentTR = $this.closest("tr");
          let { id } = $this.data();

          if (pr_MEM_TEMP[id]) delete pr_MEM_TEMP[id];
          parentTR.remove();
        });
    };

    const do_lc_load_view = function () {
      tmplCtrl.do_lc_put_tmpl(
        tmplName.PRJ_USER_GROUP_MEMBER,
        PrjUserGroup_Member
      );
    };

    const do_get_list_member = function () {
      App.data["lstGrpWorkMember"] = null;

      const ref = req_gl_Request_Content_Send_With_Params(
        pr_SERVICE_CLASS,
        pr_SV_MEMBER_LIST,
        { groupId: initialValues.group.id }
      );

      let fSucces = [];
      fSucces.push(req_gl_funct(null, do_lc_getMember_response, []));

      let fError = req_gl_funct(App, do_gl_show_Notify_Msg_Error, [
        $.i18n("common_err_ajax"),
      ]);
      App.network.do_lc_ajax_bg_keepState(
        App.path.BASE_URL_API_PRIV,
        App.data["HttpSecuHeader"],
        ref,
        100000,
        fSucces,
        fError
      );
    };

    var pr_setTime_refresh_member_lst;
    var pr_TIME_REFRESH = 15 * 60 * 1000;
    var do_lc_setTime_refresh_member_lst = function () {
      if (pr_setTime_refresh_member_lst)
        clearInterval(pr_setTime_refresh_member_lst);
      pr_setTime_refresh_member_lst = setInterval(() => {
        do_get_list_member();
      }, pr_TIME_REFRESH);
    };

    const do_lc_getMember_response = function (sharedJson) {
      if (can_gl_AjaxSuccess(sharedJson)) {
        const data = sharedJson[App["const"].RES_DATA];

        const isSuperAdmin =
          App.controller.UI.Login &&
          App.controller.UI.Login.can_lc_User_SuperAdmin();

        let objData = data.reduce((currentObj, item) => {
          if (item.uId == App.data.user.id) item.isOwner = true;

          if (!isSuperAdmin) {
            if (item.typ != pr_member_lev_manager) item.notModif = true;
          }

          if (!!item.mem) currentObj[item.uId] = item;
          return currentObj;
        }, {});

        App.data["lstGrpWorkMember"] = { ...objData };
        App.data["lstGrpWorkMember"].length = Object.keys(objData).length;
        initialValues.members = objData;
        do_lc_build_page();

        pr_ctr_List.do_lc_build_role_user(initialValues.group);
      } else {
        do_gl_show_Notify_Msg_Error($.i18n("common_err_msg_get"));
      }
    };

    const do_lc_customLst_user_autocomplete = function (item, selOpt = "") {
      if (!item.avatar) {
        let first = item.login01.charAt(0);
        let last = item.login01.charAt(item.login01.length - 1);
        let index = var_gl_alphabet.indexOf(first.toLowerCase());

        let textColor = var_gl_colors[index];
        let textAvatar = first + last;

        selOpt += `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-white text-uppercase text-center mr-2" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div> ${item.login01}</div>`;
      } else {
        selOpt += `<div class="media align-items-center"><img src='${item.avatar.prevUrl}' class='rounded-circle avatar-xs mr-2'/> ${item.login01}</div>`;
      }
      return selOpt;
    };

    const do_lc_save_member_toGroup = function (members, idGroup) {
      const ref = req_gl_Request_Content_Send_With_Params(
        pr_SERVICE_CLASS_NSO_GROUP,
        pr_SV_MEMBER_SAVE,
        {
          groupId: idGroup,
          members: JSON.stringify(Object.values(pr_MEM_TEMP)),
        }
      );

      let fSucces = [];
      fSucces.push(
        req_gl_funct(null, do_lc_afterSave_member, [members, idGroup])
      );

      let fError = req_gl_funct(App, do_gl_show_Notify_Msg_Error, [
        $.i18n("common_err_ajax"),
      ]);

      App.network.do_lc_ajax_background(
        App.path.BASE_URL_API_PRIV,
        App.data["HttpSecuHeader"],
        ref,
        100000,
        fSucces,
        fError
      );
    };

    const do_lc_afterSave_member = function (sharedJson, members, idGroup) {
      if (can_gl_AjaxSuccess(sharedJson)) {
        initialValues.members = pr_MEM_TEMP;
        do_lc_build_page();
        do_gl_show_Notify_Msg_Success($.i18n("common_success_update"));
      } else {
        do_gl_show_Notify_Msg_Error($.i18n("common_err_msg_get"));
      }
    };

    var do_lc_bindEvent_tabMemberEdit = function (memid, divLev) {
      $(divLev)
        .off("change")
        .on("change", function () {
          pr_MEM_TEMP[memid].typ = $(this).val();
        });
    };
  };

  return PrjUserGroupMember;
});
