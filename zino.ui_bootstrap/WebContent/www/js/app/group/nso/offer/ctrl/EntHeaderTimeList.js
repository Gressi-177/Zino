define([
    'jquery',

    'text!group/nso/offer/tmpl/Ent_Header_Time_View.html',

    'mapPath/map'
], function (
    $,
    Tmpl_Ent_Header_Time_View,
) {

    var EntHeaderTimeList = function (grpName, header, content, footer) {
        var pr_divHeader  = header;
        var pr_divContent = content;
        var pr_divFooter  = footer;
        var pr_grpName    = grpName;

        var USER_TYP_CANDIDATE = 100;

        const MP_DAY_START    = 6;
        const MP_DAY_END      = 12;
        const MP_MONTH        = 8;
        const MP_YEAR         = 2023;

        const tmplName             = App.template.names[pr_grpName];
        const tmplCtrl            = App.template.controller;
        //------------------------------------------------------------------------------------
        const pr_SERVICE_CLASS     = "ServicePrjProject"; //to change by your need
        const pr_SERVICE_CLASS_DYN = "ServicePrjProjectDyn"; //to change by your need
        const pr_SV_LIST_DYN       = "SVLst";
        const pr_SV_SAVE_MOVE      = "SVTaskMove";

        const pr_SERVICE_PER_CLASS = "ServicePersonDyn";

        const pr_SV_USER_SEARCH      = "SVUserLstSearchWithAvatar";
        const pr_SV_GET_MEMBER       = "SVSorOrderEntHeaderTimeGetMem";
        //-----------------------------------------------------------------------------------
        var pr_ctr_Main              = null;
        var pr_ctr_EntHeader         = null;
        var pr_ctr_EntHeaderTimeMain = null;

        var pr_SEARCHKEY = "";
        var pr_GROUP     = null;

        var pr_obj              = null;
        var pr_mode             = null;
        var pr_lstAvailableTime = [];

        var TIME_RANGE         = 3;
        var TYP_01_APPOINTMENT = 20001;
        var TYP_02_APPOINTMENT = 1000;

        const STAT_APPOINTMENT_DESACTIVE = 2;

        var members      = {};
//		var membersDel 						= [];
        let files        = {files: []};
        var customers    = [];
        var customersAdd = [];
        var customersDel = [];

        var dayStart    = null;
        var dayEnd      = null;
        var dayGap      = null;
        var location    = null;
        var dp_schedule = null;
        var locale      = "vi-vi";

        const pr_member_lev_manager  = 0;
        const pr_member_lev_reporter = 1;
        const pr_member_lev_worker   = 2;

        //--------------------APIs--------------------------------------//
        this.do_lc_init = function () {
            pr_ctr_Main              = App.controller[pr_grpName].Main;
            pr_ctr_EntHeader         = App.controller[pr_grpName].EntHeader;
            pr_ctr_EntHeaderTimeMain = App.controller[pr_grpName].EntHeaderTimeMain;

            tmplName.ENT_HEADER_TIME_VIEW = "Tmpl_Ent_Header_Time_View";
        }

        //---------show-----------------------------------------------------------------------------
        this.do_lc_show = function (obj, mode) {
            pr_obj  = obj;
            pr_mode = mode;
            try {
                do_lc_load_view();
                do_register_locale_custom();
                do_build_schedulue();
                do_show_list_FreeTimes();

                // do_lc_bind_eventPage();
                do_lc_bind_change_Date();
                do_lc_bind_save_FreeTimes();
            } catch (e) {
                console.log(e);
                do_gl_exception_send(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "prj.project",
                    "EntHeaderTimeList", "do_lc_show", e.toString());
            }
        };

        const do_lc_load_view = () => {
            tmplCtrl.do_lc_put_tmpl(tmplName.ENT_HEADER_TIME_VIEW, Tmpl_Ent_Header_Time_View);

            $(pr_divContent).html(tmplCtrl.req_lc_compile_tmpl(tmplName.ENT_HEADER_TIME_VIEW, {}));
        }

        //-------------------------------------------------------------------------------------------------
        var do_register_locale_custom = function () {
            DayPilot.Locale.register(
                new DayPilot.Locale("vi-vi",
                    {
                        dayNames       : ['Chủ nhật', 'Thứ hai', 'Thứ ba', 'Thứ tư', 'Thứ năm', 'Thứ sáu', 'Thứ bảy'],
                        dayNamesShort  : ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'],
                        monthNames     : ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7',
                                          'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
                        monthNamesShort: ['Thg 1', 'Thg 2', 'Thg 3', 'Thg 4', 'Thg 5', 'Thg 6', 'Thg 7', 'Thg 8',
                                          'Thg 9', 'Thg 10', 'Thg 11', 'Thg 12'],
                        timePattern    : 'h:mm tt',
                        datePattern    : 'M/d/yyyy',
                        dateTimePattern: 'M/d/yyyy h:mm tt',
                        timeFormat     : 'Clock12Hours',
                        weekStarts     : 0
                    }
                ));
            let tmp = localStorage.getItem("language");
            if (tmp == "en") locale = "en-us";
            else locale = tmp + "-" + tmp;
        }

        var do_build_schedulue = function (pr_lstAvailableTime) {
            dayStart = $('input[data-name=dt03]').val()
            dayEnd   = $('input[data-name=dt04]').val()
            dayStart = req_gl_DateObj_From_DateStr(dayStart, 'dd/MM/yyyy');
            dayEnd   = req_gl_DateObj_From_DateStr(dayEnd, 'dd/MM/yyyy');
            dayGap   = req_gl_Date_CompareObj(dayEnd, dayStart);
            dayGap   = dayGap / (1000 * 3600 * 24);

            dp_schedule = new DayPilot.Calendar("dp_schedule");

            dp_schedule.eventClickHandling        = "Select";
            dp_schedule.allowMultiSelect          = false;
            dp_schedule.allowEventOverlap         = false;
            dp_schedule.durationBarVisible        = true;
            dp_schedule.eventMoveHandling         = "Disabled";
            dp_schedule.eventResizeHandling       = "Disabled";
            dp_schedule.timeRangeSelectedHandling = "Enable";
            dp_schedule.headerDateFormat          = "dddd";
            dp_schedule.timeFormat                = "Clock24Hours";
            dp_schedule.heightSpec                = "Fixed";
            dp_schedule.height                    = 400;
            dp_schedule.startDate                 = req_lc_Daypilot_date(0);
            dp_schedule.dayBeginsHour             = 6;
            dp_schedule.dayEndsHour               = 21;
            dp_schedule.viewType                  = "Week";
            dp_schedule.locale                    = locale

            if (dayGap < 7 && App.data.user.typ02 === USER_TYP_RECRUITER) {
                dp_schedule.viewType    = "Days";
                dp_schedule.days        = dayGap ? dayGap + 1 : 0;
            } else if(App.data.user.typ02 === USER_TYP_CANDIDATE) {
                dayStart                = req_lc_Daypilot_date(0, 'dd/MM/yyyy');
                dp_schedule.startDate   = req_gl_DateObj_From_DateStr(dayStart, 'dd/MM/yyyy');
            }

//			dp_schedule.cssOnly 					= false;
//			// add list of schedules
            dp_schedule.events.list = pr_lstAvailableTime;

            dp_schedule.onTimeRangeSelected = function (args) {
                dp_schedule.clearSelection();

                do_lc_create_appointment(args);
            };

            dp_schedule.contextMenu = new DayPilot.Menu({
                zIndex: 1050,
                items : [
                    {
                        text: "Delete", onClick: args => {
                            const e = args.source;
                            dp_schedule.events.remove(e).queue();
                        }
                    }
                ]
            });

            dp_schedule.init();
        }

        //-------------------------------------------------------------------------------------------------

        var do_lc_create_appointment = function (args) {
            let count  = 0;
            let start  = args.start;
            let end    = args.end;
            const date = start.toString('yyyy/MM/dd');

            $.each([...pr_lstAvailableTime], function (i, item) {
                if (item.start.toString('yyyy/MM/dd') === date) {
                    if (end === item.start) {
                        pr_lstAvailableTime.splice(i - count, 1);
                        end = item.end;

                        ++count;
                    } else if (start === item.end) {
                        pr_lstAvailableTime.splice(i - count, 1);
                        start = item.start;

                        ++count;
                    }

                    if (count === 2) return false;
                }
            })

            pr_lstAvailableTime.push({
                start: start,
                end  : end,
                text : $.i18n("nso_offer_free_time")
            })
            dp_schedule.events.list = pr_lstAvailableTime;
            dp_schedule.update()
        }

        var do_lc_bind_change_Date = function () {
            $('input[data-name=dt03]').off('change').on('change', function () {
                dayEnd = req_gl_DateObj_From_DateStr($('input[data-name=dt04]').val(), 'dd/MM/yyyy');
                if (dayEnd == null) return;

                dayStart = req_gl_DateObj_From_DateStr($(this).val(), 'dd/MM/yyyy');
                dayGap   = req_gl_Date_CompareObj(dayEnd, dayStart);
                dayGap   = dayGap / (1000 * 3600 * 24);

                dp_schedule.startDate = req_lc_Daypilot_date(dayStart.getDay(), 'dd/MM/yyyy');
                do_lc_update_ViewType()
            })

            $('input[data-name=dt04]').off('change').on('change', function () {
                dayStart = req_gl_DateObj_From_DateStr($('input[data-name=dt03]').val(), 'dd/MM/yyyy');
                if (dayStart == null) return;

                dayEnd = req_gl_DateObj_From_DateStr($(this).val(), 'dd/MM/yyyy');
                dayGap = req_gl_Date_CompareObj(dayEnd, dayStart);
                dayGap = dayGap / (1000 * 3600 * 24);

                do_lc_update_ViewType()
            })
        }

        const do_lc_update_location = (place) => {
            location = {
                latitude: place.coordinate.latitude,
                long    : place.coordinate.longitude
            }
        }

        var do_lc_update_ViewType = () => {
            dayStart = req_lc_Daypilot_date(dayStart.getDay(), 'dd/MM/yyyy')
            dayStart = req_gl_DateObj_From_DateStr(dayStart, 'dd/MM/yyyy');
            dp_schedule.update({
                startDate   : dayStart
            });
            if (dayGap < 7) {
                dp_schedule.update({
                    viewType: "Days",
                    days    : dayGap + 1
                });
            } else {
                dp_schedule.update({
                    viewType: "Week"
                });
            }
            ;
        }

        var do_lc_bind_save_FreeTimes = function () {
            $('#btn_save_time').off('click').on('click', function () {
                if (pr_mode === App['const'].MODE_NEW) return;
                let lst = req_lc_reformat_data_send(pr_lstAvailableTime);

                if (lst.length < 1) {
                    do_gl_show_Notify_Msg()
                    return;
                }

                let dataSend = {id: pr_obj.id, obj: JSON.stringify(lst)};
                let ref      = req_gl_Request_Content_Send_With_Params("ServiceNsoOffer", "SVModTime", dataSend);

                let fSucces = [];
                fSucces.push(req_gl_funct(null, do_close_TimeMain, []));
                fSucces.push(req_gl_funct(App, do_gl_show_Notify_Msg, [null, null, App['const'].MODE_MOD]));

                let fError = req_gl_funct(App, do_gl_show_Notify_Msg, [$.i18n("common_err_ajax"), 0]);

                App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
            })
        }
        //------------------------------------------------------------------------------------
        //-------------------------------------------------------------------------------------------------
        function do_show_list_FreeTimes() {
            pr_lstAvailableTime = [];
            if (!pr_obj['times']) {
                dp_schedule.days = 0;
                dp_schedule.update()
                return;
            }
            const lstFreeTimes = JSON.parse(pr_obj['times']);

            $.each(lstFreeTimes, function (i, item) {
                const lst = req_lc_format_Time_to_Daypilot_Date(item)

                $.each(lst, function (idx, o) {
                    pr_lstAvailableTime.push({
                        start: o.start,
                        end  : o.end,
                        text : $.i18n("nso_offer_free_time")
                    });
                })
            })

            dp_schedule.events.list = pr_lstAvailableTime;
            dp_schedule.update();
        }

        var req_lc_reformat_data_send = (obj) => {
            let lst = [];
            $.each(obj, function (i, item) {
                const date = new Date(item.start.toString('yyyy/MM/dd')).getDay();
                let start  = item.start.toString('HH:mm')
                let end    = item.end.toString('HH:mm')
                let flag   = 0;

                start = `${start.substr(0, 2)}${start.substr(3, 2)}`;
                end   = `${end.substr(0, 2)}${end.substr(3, 2)}`;
                if ((flag = req_lc_check_date_exists(lst, date)) !== -1) {
                    lst[flag].time.push({
                        start: start,
                        end  : end
                    })
                } else {
                    lst.push({
                        date: date,
                        time: [
                            {
                                start: start,
                                end  : end
                            }
                        ]
                    })
                }
            })

            return lst;
        }

        var req_lc_check_date_exists = (lst, date) => {
            for (let i = 0; i < lst.length; i++) {
                if (lst[i].date === date) return i;
            }

            return -1;
        }

        var req_lc_format_Time_to_Daypilot_Date = (obj) => {
            let lst   = [];
            let date  = req_lc_Daypilot_date(obj.date);
            let time  = obj.time;
            let start = null;
            let end   = null;

            $.each(time, function (i, item) {
                start = item["start"].substr(0, 2) + ":" + item["start"].substr(2, 2) + ":00";
                end   = item["end"].substr(0, 2) + ":" + item["end"].substr(2, 2) + ":00";

                start = new DayPilot.Date(`${date}T${start}`)
                end   = new DayPilot.Date(`${date}T${end}`)

                lst.push({
                    start: start,
                    end  : end
                })
            })

            return lst;
        }

        var req_lc_convert_Date_to_Time = (day, hour, minutes) => {
            let time = day.toString();
            if (hour < 10) {
                time += "0" + hour;
            } else {
                time += hour;
            }
            if (minutes < 10) {
                time += "0" + minutes;
            } else {
                time += minutes;
            }

            return time;
        }

        var req_lc_Daypilot_date = (dayOfWeek, format) => {
            const day = MP_DAY_START + parseInt(dayOfWeek);

            if(format == "dd/MM/yyyy") {
                return `${day < 10 ? "0" + day : day}/0${MP_MONTH}/${MP_YEAR}`;
            }

            return `${MP_YEAR}/0${MP_MONTH}/${day < 10 ? "0" + day : day}`;
        }
        //------------------------------------------------------------------------------------
        var do_close_TimeMain           = () => {
            $('#modal_time_main').modal('hide');
        }
    };

    return EntHeaderTimeList;
});