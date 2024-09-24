define([
	'text!group/user_appointment/tmpl/Prj_Appointment_View.html',
	'text!group/user_appointment/tmpl/Prj_Appointment_New.html',
	'text!group/user_appointment/tmpl/Prj_Appointment_Show.html',

	], function(
		Prj_Appointment_View, 
		Prj_Appointment_New,
		Prj_Appointment_Show
	){

	var PrjAppointmentList = function (grpName, header, content, footer) {
		const tmplName				= App.template.names;
		const tmplCtrl				= App.template.controller;
		//------------------------------------------------------------------------------------
		const pr_SERVICE_CLASS		= "ServicePrjProject"; //to change by your need
		const pr_SERVICE_CLASS_DYN	= "ServicePrjProjectDyn"; //to change by your need
		const pr_SV_LIST_DYN		= "SVLstPage"; 
		const pr_SV_SAVE_MOVE		= "SVTaskMove";

		const pr_SERVICE_AUT_CLASS	= "ServiceAutUser";

		const pr_SV_USER_SEARCH		= "SVLst";
		const pr_SV_GET_MEMBER		= "SVGetMember";

		const self					= this;
		//-----------------------------------------------------------------------------------
		var pr_ctr_Main 					= null;
		
		var pr_SEARCHKEY					= "";
		var pr_GROUP						= null;

		var pr_lstAvailableTime				=	[];

		var TIME_RANGE						= 3;
		var TYP_01_MEETING					= 100;
		var TYP_02_APPOINTMENT				= 1000;
		
		const STAT_APPOINTMENT_DESACTIVE    = 2;

		var members 						= {};
//		var membersDel 						= [];
		let files							= {files: []};
		var customers 						= [];
		var customersAdd 				    = [];
		var customersDel                    = [];
		
		var dp_nav 							= null;
		var dp_schedule						= null;
		var locale							= "vi-vi";
		
		const pr_member_lev_manager 		= 0;
		const pr_member_lev_reporter 		= 1;
		const pr_member_lev_worker 			= 2;

		const pr_stat_pending				= 0;
		const pr_stat_active				= 1;
		const pr_stat_deny					= 11;
		
		//--------------------APIs--------------------------------------//
		this.do_lc_init	= function(){
			pr_ctr_Main 					= App.controller.UI.Main;

			tmplName.PRJ_APPOINTMENT_VIEW	= 	"Prj_Appointment_View";
			tmplName.PRJ_APPOINTMENT_NEW	= 	"Prj_Appointment_New";
			tmplName.PRJ_APPOINTMENT_SHOW	= 	"Prj_Appointment_Show";
		}

		//---------show-----------------------------------------------------------------------------
		var pr_grpPath 		= 'group/user_appointment';
		var pr_showed		= false;
		this.do_lc_show = function(){
			if (!pr_showed){
				do_gl_lang_append (pr_grpPath + '/transl', self.do_lc_show_callback, []);
				pr_showed = true;
			}else {
				self.do_lc_show_callback();
			}
		};
		
		this.do_lc_show_callback = function(){    
			try{
				do_lc_load_view();
				do_register_locale_custom();
				do_build_schedulue(pr_lstAvailableTime);
				
				do_get_availableTimeList(dp_schedule);
				
				do_lc_bind_eventPage();
			}catch(e) {				
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "prj.project", "PrjAppointmentList", "do_lc_show", e.toString()) ;
			}
		};

		const do_lc_init_element = function (e) {
			if (e) {
				if (e.files) files.files = e.files;
				let option		= {
					fileinput	: {maxFiles : 1, param : {typ01: 1, typ02: 1} },//option here
					obj			: files//file existing here
				}
				do_gl_init_fileDropzone($("#div_prj_docs"), option);

				let dt01 = do_lc_handle_date(e.dt01);
				let dt02 = do_lc_handle_date(e.dt02);
				$( "#dtpicker_Begin" )	.datepicker( "setDate", dt01.dt);
				$( "#dtpicker_End" )	.datepicker( "setDate", dt02.dt);

				$("#tmpicker_Begin").timepicker({//timepicker
					showMeridian: false,
					defaultTime : dt01.tm,
					icons		: {
						up		: "mdi mdi-chevron-up",
						down	: "mdi mdi-chevron-down"
					}
				});
				
				$("#tmpicker_End").timepicker({//timepicker
					showMeridian: false,
					defaultTime : dt02.tm,
					icons		: {
						up		: "mdi mdi-chevron-up",
						down	: "mdi mdi-chevron-down"
					}
				});

				do_lc_bind_event_dtInput()
			} else {
				let option		= {
					fileinput	: {maxFiles : 1, param : {typ01: 1, typ02: 1} },//option here
					obj			: files//file existing here
				}
				do_gl_init_fileDropzone($("#div_prj_docs"), option);

				$( "#dtpicker_Begin" )	.datepicker( "setDate", new Date());
				$( "#dtpicker_End" )	.datepicker( "setDate", new Date());
				$("#tmpicker_Begin").timepicker({//timepicker
					showMeridian: false,
					defaultTime :'7:00',
					icons		: {
						up		: "mdi mdi-chevron-up",
						down	: "mdi mdi-chevron-down"
					},
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

		const do_lc_load_view = () => {	
			tmplCtrl.do_lc_put_tmpl(tmplName.PRJ_APPOINTMENT_VIEW, Prj_Appointment_View);
			tmplCtrl.do_lc_put_tmpl(tmplName.PRJ_APPOINTMENT_NEW, Prj_Appointment_New);
			tmplCtrl.do_lc_put_tmpl(tmplName.PRJ_APPOINTMENT_SHOW, Prj_Appointment_Show);

			$("#div_main_content").html(tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_APPOINTMENT_VIEW, {}));
		}

		//-------------------------------------------------------------------------------------------------
		var do_register_locale_custom = function () {
			DayPilot.Locale.register(
				new DayPilot.Locale('vi-vi', 
				{
				  dayNames: ['Chủ nhật','Thứ 2','Thứ 3','Thứ 4','Thứ 5','Thứ 6','Thứ 7'],
				  dayNamesShort: ['CN','T2','T3','T4','T5','T6','T7'],
				  monthNames: ['Tháng 1','Tháng 2','Tháng 3','Tháng 4','Tháng 5','Tháng 6','Tháng 7','Tháng 8','Tháng 9','Tháng 10','Tháng 11','Tháng 12'],
				  monthNamesShort: ['Thg 1','Thg 2','Thg 3','Thg 4','Thg 5','Thg 6','Thg 7','Thg 8','Thg 9','Thg 10','Thg 11','Thg 12'],
				  timePattern: 'h:mm tt',
				  datePattern: 'M/d/yyyy',
				  dateTimePattern: 'M/d/yyyy h:mm tt',
				  timeFormat: 'Clock12Hours',
				  weekStarts: 0
				}
			));
			let tmp = localStorage.getItem("language");
			if (tmp == "en") locale = "en-us";
			else locale = tmp + "-" + tmp;
		}

		var pr_backColor_01 = "#fff";
		var pr_backColor_02 = "beige"; 
		function do_build_schedulue(pr_lstAvailableTime) {
			// var date = new Date(pr_savedObject_Step_1.dt).getTime() - 2*60*60*24*1000;
			// var startDate = getDateISOShort(new Date(date));
			var startDate = new Date();
			startDate.setDate(startDate.getDate() - 1); // get 2 days before
			startDate = getDateISOShort(startDate);

			dp_nav = new DayPilot.Navigator("dp_nav");
		    dp_nav.showMonths = 3;
		    dp_nav.skipMonths = 3;
		    dp_nav.selectMode = "week";
		    dp_nav.onTimeRangeSelected = function (args) {
		        dp_schedule.startDate = args.day;
		        do_get_availableTimeList(dp_schedule, args.start.value.replace("T", " "), args.end.value.replace("T", " "));
			};
			dp_nav.locale = locale;
			
		    dp_nav.init();
			
			
		    dp_schedule 							= new DayPilot.Calendar("dp_schedule");
		    dp_schedule.viewType 					= "Week";
//			dp_schedule.viewType 					= "Days";
			
//			dp_schedule.startDate 					= startDate;

			dp_schedule.days 						= TIME_RANGE*2+1;
			dp_schedule.eventClickHandling 			= "Select";
			dp_schedule.allowMultiSelect 			= false;
			dp_schedule.durationBarVisible 			= true;
			dp_schedule.eventMoveHandling 			= "Disabled";
			dp_schedule.eventResizeHandling 		= "Disabled";
			dp_schedule.timeRangeSelectedHandling 	= "Enable";
			dp_schedule.headerDateFormat 			= "dd/MM/yyyy";
			dp_schedule.timeFormat 					= "Clock24Hours";
//			dp_schedule.cssOnly 					= false;
			dp_schedule.cssClassPrefix 				= "workadm";
//			dp_schedule.allowEventOverlap 			= false;
//			// add list of schedules
			dp_schedule.events.list 				= pr_lstAvailableTime;
			
			dp_schedule.heightSpec 					= "Parent100Pct";
			
			dp_schedule.onBeforeCellRender = function(args) {
				if (args.cell.start.getDatePart().getTime() === new DayPilot.Date().getDatePart().getTime()) {
					args.cell.backColor = "silver";
				}
			};

			dp_schedule.onBeforeEventRender = function(args) {
				if(args.data.tags.type !== "overdue") {
					if(typeof args.data.obj.inf02 === 'string') {
						args.data.obj.inf02 = JSON.parse(args.data.obj.inf02);
					}

					if (args.data.obj.inf02.color) {
						args.data.borderColor = args.data.obj.inf02.color;
					}
					else {
						args.data.borderColor 	= args.data.color;
					}
				}
				args.data.areas = [
					{ top: 2, right: 2, icon: "icon-triangle-down", visibility: "Hover", action: "ContextMenu", style: "font-size: 12px; background-color: #f9f9f9; border: 1px solid #ccc; padding: 2px 2px 0px 2px; cursor:pointer;" }
				];
				args.data.fontColor	  	= "#00000";
				
//				args.data.backColor 	= pr_backColor_01;
				var div = "";
				if(args.data.members) {
					for (var key in args.data.members) {
						let item 			= args.data.members[key].mem;
						
						let textColor   = null;
						let textAvatar  = null
						if(!item.avatar){
							let first = item.login01.charAt(0);
							let last  = item.login01.charAt(item.login01.length - 1);
							let index = var_gl_alphabet.indexOf(first.toLowerCase());
							
							textColor = var_gl_colors[index];
							textAvatar= first + last;
						}
						
						let classCss = "";
						let opacity  = "";
						if(args.data.members[key].stat == 2 ){
							classCss = "text-decoration-line-through";
							opacity  = "opacity-03";
						}
						
						let selOpt 			= `<div class='team-member member-item d-flex'>`;

						if(!item.avatar)	selOpt 			+= `<div class="rounded-circle avatar-xs text-white mx-1 text-uppercase text-center ${opacity}" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div><span class="tooltiptext ${classCss}">${item.login01}</span>`;
						else 		        selOpt 			+= `<img src='${ item.avatar.urlPrev}' class='rounded-circle avatar-xs ${opacity}'/><span class="tooltiptext ${classCss}">${item.login01}</span>`;
						selOpt 				+= `</div>`;
						div += selOpt;
					};
				}
		
				args.data.html = "<h5 class='text-truncate font-size-15 h-inline task-item-name'>" + "<p>" + args.data.text + "</p></h5>" + "<p class='long-txt'><i>" + args.data.obj.inf02.desc + "</i></p>" + "<div id='div_prj_list' class='row'>" + div + "</div>";
			};

			dp_schedule.onTimeRangeSelected = function (args) {
				dp_schedule.clearSelection();
	            
	            App.MsgboxController.do_lc_show({
					title		: $.i18n("prj_appointment_msg_title"),
					content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_APPOINTMENT_NEW, {}),
					autoclose	: true,
					buttons 	: {
						SEND 	: {
							lab 		: "<i class='mdi mdi-send'></i>",
							funct		: do_lc_create_appointment,
							autoclose	: true
						},
					},
					onClose		: () => {
						members 	= {};
//						membersDel  = [];
						files		= {files: []};
						customers   = [];
						customersAdd= [];
						customersDel= [];
					},
				});
				do_lc_binding_event_add_customer(false);
				do_lc_req_autocomplete();
				
				let option		= {
						fileinput	: {maxFiles : 1, param : {typ01: 1, typ02: 1} },//option here
						obj			: files//file existing here
				}
				do_gl_init_fileDropzone($("#div_prj_docs"), option);

				let dt01 = do_lc_handle_date(new Date(args.start.value));
				let dt02 = do_lc_handle_date(new Date(args.end.value));
				
				$( "#dtpicker_Begin" )	.datepicker( "setDate", dt01.dt);
				$( "#dtpicker_End" )	.datepicker( "setDate", dt02.dt);
				$("#tmpicker_Begin").timepicker({//timepicker
					showMeridian: false,
					defaultTime : dt01.tm,
					icons		: {
						up		: "mdi mdi-chevron-up",
						down	: "mdi mdi-chevron-down"
					}
				});

				$("#tmpicker_End").timepicker({//timepicker
					showMeridian: false,
					defaultTime :dt02.tm,
					icons		: {
						up		: "mdi mdi-chevron-up",
						down	: "mdi mdi-chevron-down"
					}
				});
	        };
	        
			dp_schedule.onEventClick = function(args) {
				
				if(args.e.data.tags.type !== "overdue") {
					// var id 		= args.e.id();
					// var list 	= dp_schedule.events.list;
					// for(var i = 0 ; i < list.length ; i++){
					// 	list[i].backColor = {};
					// 	if(list[i].id === id) {
					// 		list[i].backColor = pr_backColor_02;
					// 	}else {
					// 		list[i].backColor = pr_backColor_01;
					// 	}
					// }
					// dp_schedule.events.list = list;
					// dp_schedule.update();
					// do_handleSCheduleClick(args.e.data);
					
					// $("#schedule_toolTip").show();
					// $("#schedule_toolTip span").html(args.e.data.toolTip);
					if(args.e.data.obj.val02) {
						if (!/^https?:\/\//i.test(args.e.data.obj.val02)) {
							args.e.data.obj.val02 = 'http://' + args.e.data.obj.val02;
						}
					}
					App.MsgboxController.do_lc_show({
						title		: $.i18n("prj_appointment_msg_title"),
						content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_APPOINTMENT_SHOW, args.e.data.obj),
						autoclose	: true,
						buttons		: "none",
						onClose		: () => {
							members 	= {};
//							membersDel  = [];
							files		= {files: []};
						},
					});
					
					// Handle div file
					var divFile = "";
					if (args.e.data.obj.files) {
						args.e.data.obj.files.forEach((e) => {
							tmpl 	=  "<a href='" + e.path01  + "' target='_blank' class='mr-3 text-decoration-underline' download = " + e.name + " >" + e.name + "</a>";
							divFile += tmpl;
						});
					}
					if (divFile) {
						$("#div_list_files").append(divFile)
					} else {
						$("#div_list_files_par").remove();
					}

					// Handle div list member
					var div = "";
					if(args.e.data.members) {
						for (var key in args.e.data.members) {
							
							let classCss = "";
							let opacity = "";
							let mem = args.e.data.members[key];
							if(mem.stat && mem.stat == 2 ) {
								classCss = "text-decoration-line-through";
								opacity  = "opacity-03"
							}
							
							let item 			= mem.mem;
							let selOpt 			= `<div class='team-member member-item d-flex'>`;
	
							let textColor   = null;
							let textAvatar  = null
							if(!item.avatar){
								let first = item.login01.charAt(0);
								let last  = item.login01.charAt(item.login01.length - 1);
								let index = var_gl_alphabet.indexOf(first.toLowerCase());
								
								textColor = var_gl_colors[index];
								textAvatar= first + last;
							}
							
							if(!item.avatar)	selOpt 			+= `<div class="rounded-circle avatar-xs text-white mr-1 text-uppercase text-center ${opacity}" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div><span class="tooltiptext ${classCss}"> ${item.login01}</span>`;
							else                selOpt 		    += `<img src='${ item.avatar.urlPrev}' class='rounded-circle avatar-xs ${opacity} mr-1 avatar-autocomplete'/><span class="tooltiptext ${classCss}">${item.login01}</span>`;
							selOpt 				+= `</div>`;
							div 				+= selOpt;
						};
					}
					if (div) {
						$("#div_list_member").append(div)
					} else {
						$("#div_list_member_par").remove();
					}
					
					// Handle div list customer
					var divCustomer = "";
					if(args.e.data.obj.addr03) {
						let data = JSON.parse(args.e.data.obj.addr03);
						for (let i=0; i < data.length; i++) {
							let email  			= data[i];
							let selOpt 			= `<div class='mr-1'><button class="btn btn-secondary">${email}</button></div>`;
							divCustomer += selOpt;
						};
					}
					if (divCustomer) {
						$("#div_list_email_customer").append(divCustomer)
					} else {
						$("#div_list_email_customer_par").remove();
					}
					
					
					function isValidURL(string) {
						var res = string.match(/(http(s)?:\/\/.)?(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)/g);
						return (res !== null)
					};

				}
				else {
					// do something
				}
				
			};

			dp_schedule.contextMenu = new DayPilot.Menu({
				items: [
					{
						text: $.i18n("prj_appointment_event_edit"),
						onClick: function (args) {
							var e 	= args.source.data.obj;
							App.MsgboxController.do_lc_show({
								title		: $.i18n("prj_appointment_msg_title"),
								content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_APPOINTMENT_NEW, { ent: e }),
								autoclose	: true,
								buttons 	: {
									SEND 	: {
										lab 		: "<i class='mdi mdi-send'></i>",
										funct		: do_lc_mod_appointment,
										autoclose	: true
									},
								},
								onClose		: () => {
									members 	= {};
//									membersDel  = [];
									files		= {files: []};
									customers   = [];
									customersAdd= [];
									customersDel= [];
								},
							});
							do_lc_init_element(e);
							
							do_lc_build_view_member(args.source.data.members, "#div_list_member"); // build lst member
							do_lc_build_view_customer(args.source.data.obj.addr03, "#div_list_email_customer");
							do_lc_binding_event_add_customer(true);
							do_lc_bind_event_autocomplete(); // bind event delete for each member element

							do_lc_req_autocomplete();
						}
					},
					{
						text: $.i18n("prj_appointment_event_del"),
						onClick: function (args) {
							var e = args.source;
							do_lc_remove_appointment(e);
						}
					},
					{
						text:  $.i18n("prj_appointment_event_denied"),
						onClick: function (args) {
							var e = args.source;
							do_lc_deny_appointment(e);
						}
					},
					
					{
						text: "-"
					},
					{
						text: $.i18n("prj_appointment_color_blue"),
						icon: "fa fa-square ic-blue",
						color: "#1066a8",
						onClick: function (args) { updateColor(args.source, args.item.color); }
					},
					{
						text: $.i18n("prj_appointment_color_green"),
						icon: "fa fa-square ic-green",
						color: "#6aa84f",
						onClick: function (args) { updateColor(args.source, args.item.color); }
					},
					{
						text: $.i18n("prj_appointment_color_yellow"),
						icon: "fa fa-square ic-yellow",
						color: "#f1c232",
						onClick: function (args) { updateColor(args.source, args.item.color); }
					},
					{
						text: $.i18n("prj_appointment_color_red"),
						icon: "fa fa-square ic-red",
						color: "#cc0000",
						onClick: function (args) { updateColor(args.source, args.item.color); }
					},
		
				],
				onShow: function(args) {
					const isSuperAdmin 	= App.controller.common.Login && App.controller.common.Login.can_lc_User_SuperAdmin();
					if(isSuperAdmin){
						args.menu.items[2].hidden = true;
						return;
					}
					
				    var e = args.source;
				    if(e.data.obj.uId == App.data.user.id){
				    	args.menu.items[2].hidden = true;
				    	return;
				    }
				    
				    if (e.data.members) {
				    	let members = e.data.members;
				    	args.menu.items[0].hidden = true;
				    	args.menu.items[1].hidden = true;
				    }
				  }
			});
			
			dp_schedule.init();
		}

		var do_lc_mod_appointment = function () {
			let	data	 		= req_gl_data({
				dataZoneDom		: $("#div_create_prj_appointment")
			});
			let prj 	= data.data;
			prj.dt01 	= do_lc_convert_date(prj.dtBegin).replace("T"," ");
			prj.dt02 	= do_lc_convert_date(prj.dtEnd).replace("T"," ");
			
			prj.files 	= files.files;
			prj.typ01 	= TYP_01_MEETING;
			// prj.typ02 	= TYP_02_APPOINTMENT;
			prj.nb 		= 0;
			prj.val01 	= JSON.stringify(customers);
			do_lc_mod_prj_appointment(prj);
		}
		var do_lc_mod_prj_appointment = function (prj) {
			let dataSend	= {obj: JSON.stringify(prj), member: JSON.stringify(Object.values(members)), customersAdd: JSON.stringify(customersAdd), customersDel: JSON.stringify(customersDel)};
			let ref 		= req_gl_Request_Content_Send_With_Params("ServiceNsoGroup", "SVMod", dataSend);			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_mod_prj_appointment_success, []));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}
		var do_lc_mod_prj_appointment_success = function (sharedJson) {
			if(can_gl_AjaxSuccess(sharedJson)) {	
				dp_schedule.message($.i18n("prj_appointment_msg_update"));

				// reload
				if (dp_nav && dp_schedule) {
					do_get_availableTimeList(dp_schedule, dp_nav.selectionStart.value.replace("T", " "));	
				}
			} else {
				do_gl_show_Notify_Msg_Error($.i18n("common_err_ajax"));
			}
			members = {};
//			membersDel = [];
		}
		
		var updateColor = function (e, color) {
			e.data.color = color;
			do_lc_edit_color_appointment(e, color);
		}
		var do_lc_edit_color_appointment = function (e, color) {
			let prj 		= e.data.obj;
			prj.inf02.color = color;
			let dataSend	= {obj: JSON.stringify(prj)};
			let ref 		= req_gl_Request_Content_Send_With_Params("ServiceNsoGroup", "SVMod", dataSend);			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_edit_color_appointment_success, [e]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}
		var do_lc_edit_color_appointment_success = function (sharedJson, e) {
			if(can_gl_AjaxSuccess(sharedJson, e)) {	
				dp_schedule.events.update(e);
				dp_schedule.message($.i18n("prj_appointment_msg_update_color"));
				// reload
				if (dp_nav && dp_schedule) {
					do_get_availableTimeList(dp_schedule, dp_nav.selectionStart.value.replace("T", " "));	
				}  
			} else {
				do_gl_show_Notify_Msg_Error($.i18n("common_err_ajax"));
			}
		}

		var do_lc_remove_appointment = function (e) {
			let dataSend	= {id: e.data.obj.id};
			let ref 		= req_gl_Request_Content_Send_With_Params("ServiceNsoGroup", "SVDel", dataSend);			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_remove_appointment_success, [e]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}
		var do_lc_remove_appointment_success = function (sharedJson, e) {
			if(can_gl_AjaxSuccess(sharedJson)) {	
				dp_schedule.events.remove(e);
				dp_schedule.message($.i18n("prj_appointment_msg_del")); 
				// reload
				if (dp_nav && dp_schedule) {
					do_get_availableTimeList(dp_schedule, dp_nav.selectionStart.value.replace("T", " "));	
				}
			} else {
				do_gl_show_Notify_Msg_Error($.i18n("common_err_ajax"));
			}
		}
		
		var do_lc_deny_appointment = function (e) {
			let dataSend	= {id: e.data.obj.id, stat: pr_stat_active};
			let ref 		= req_gl_Request_Content_Send_With_Params("ServiceNsoGroup", "SVModStatMember", dataSend);			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_deny_appointment_success, [e]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}
		var do_lc_deny_appointment_success = function (sharedJson, e) {
			if(can_gl_AjaxSuccess(sharedJson)) {	
				dp_schedule.events.remove(e);
				dp_schedule.message($.i18n("prj_appointment_msg_deny")); 
				// reload
				if (dp_nav && dp_schedule) {
					do_get_availableTimeList(dp_schedule, dp_nav.selectionStart.value.replace("T", " "));	
				}
			} else {
				do_gl_show_Notify_Msg_Error($.i18n("common_err_ajax"));
			}
		}
		    
		var getDateISOShort = function(dObj) {
			return req_gl_DateStr_From_DateObj(dObj, DateFormat.masks.isoDate);		
		}

		var getDateEN = function(dObj) {
			return req_gl_DateStr_From_DateObj(dObj, DateFormat.masks.enFullDate);		
		}

		//-------------------------------------------------------------------------------------------------

		var do_lc_create_appointment = function () {
			let	data	 		= req_gl_data({
				dataZoneDom		: $("#div_create_prj_appointment")
			});
			let prj 	= data.data;
			prj.stat01	= 1; //Active
			prj.dt01 	= do_lc_convert_date(prj.dtBegin).replace("T"," ");
			prj.dt02 	= do_lc_convert_date(prj.dtEnd).replace("T"," ");
			prj.cinf02mt01 	= {
				"dt01" : prj.dtBegin,
				"dt02" : prj.dtEnd,
			}
			prj.files 	= files.files;
			prj.typ01 	= TYP_01_MEETING;
			// prj.typ02 	= TYP_02_APPOINTMENT;
			prj.nb 		= 0;
			prj.val01  	= JSON.stringify(customers);
			if (prj.val02 && !/^https?:\/\//i.test(prj.val02)) {
				prj.val02 = 'http://' + prj.val02;
			}

			if(!members && members.length > 0) {
				members.map(e => e.stat = pr_stat_pending)
			}

			do_lc_create_prj_appointment(prj);
		}

		const do_lc_create_prj_appointment = prj => {
			let dataSend	= {obj: JSON.stringify(prj), member: JSON.stringify(Object.values(members)), customers: JSON.stringify(customers)};
			// let dataSend	= {obj: JSON.stringify(prj)};
			let ref 		= req_gl_Request_Content_Send_With_Params("ServiceNsoGroup", "SVNew", dataSend);			

			let fSucces		= [];		
			fSucces.push(req_gl_funct(null, do_lc_create_prj_appointment_success, [prj]));	

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax"), 0]);	

			App.network.do_lc_ajax (App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		const do_lc_create_prj_appointment_success = (sharedJson, prj) => {
			if(can_gl_AjaxSuccess(sharedJson)) {	
				// reload
				if (dp_nav && dp_schedule) {
					do_get_availableTimeList(dp_schedule, dp_nav.selectionStart.value.replace("T", " "));	
				}
			}else{
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_save'));
			}
			members = {};
//			membersDel = [];w
		}

		const do_lc_convert_date = (objDate) => {
			if (objDate.time.length < 5) objDate.time = "0" + objDate.time;
			return objDate.date.substr(0, 10) + "T" + objDate.time.substr(0, 5) + ":00";
		}

		const do_lc_bind_eventPage = () => {
			$("#btn_create_prj").off('click').click(() => {
				App.MsgboxController.do_lc_show({
					title		: $.i18n("prj_appointment_msg_title"),
					content 	: tmplCtrl.req_lc_compile_tmpl(tmplName.PRJ_APPOINTMENT_NEW, {}),
					autoclose	: true,
					buttons 	: {
						SEND 	: {
							lab 		: "<i class='mdi mdi-send'></i>",
							funct		: do_lc_create_appointment,
							autoclose	: true
						},
					},
					onClose		: () => {
						members 	= {};
//						membersDel  = [];
						files		= {files: []};
						customers   = [];
						customersAdd= [];
						customersDel= [];
					},
				});
				do_lc_binding_event_add_customer(false);
				do_lc_init_element();
				do_lc_req_autocomplete();
			});

			$("#dtpicker_Begin")
		}

		const do_lc_req_autocomplete = () => {
			let el = ".inp-name-member";
			let customShowList = function(item, selOpt = ""){
				if(item.avatar)	return selOpt += `<img src='${ item.avatar.urlPrev}' class='rounded-circle avatar-xs avatar-autocomplete'/> ${item.login01}`;
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

			let reqSelectMember = (item) => {
				if(members[item.id])			return false;
				let lev 			= $("#sel_member_level").val();
				let typ 			= $("#sel_member_type").val();
				let user 			= {
					"uId": item.id, 
					"typ": +lev, 
					"stat": pr_stat_pending
					// "typ": +typ
				};

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
				if(item.avatar) 
					selOpt 			+= `<div><img src='${ item.avatar.urlPrev}' class='rounded-circle avatar-xs'/> ${item.login01}`;
				else 			
					selOpt 			+= `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-white mr-1 text-uppercase text-center" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div> ${item.login01}`;
				
				selOpt 				+= `<a data-id='${item.id}' class='text-danger btn-remove-member' data-toggle='tooltip' data-placement='top' title='' data-original-title='Delete'><i class='mdi mdi-close font-size-18'></i></a>`;
				selOpt 				+= `</div>`;

				$("#div_list_member").append(selOpt);
				do_lc_bind_event_autocomplete();
				$(el).blur().val("");
			}

			let typ01Arr = []
			let typ01Str = ""
			const typ01 = +App.data.user.typ01
			for (const x of [1, 2, 3]) {
				if(typ01 <= x) typ01Arr.push(x)
			}
			typ01Str = typ01Arr.join(',')

			let options = {
				dataService 	: [pr_SERVICE_AUT_CLASS, pr_SV_USER_SEARCH], 
				dataRes 		: ["login01", "name01"], 
				dataReq			: {nbLine:5, typ01s: typ01Str, stats:1},
				selectCallback	: reqSelectMember, 
				// customShowList: customShowList
			}
			do_gl_set_input_autocomplete(el, options);
		}

		const do_lc_bind_event_autocomplete = () => {
			$(".btn-remove-member").off("click").on("click", function(){
				let $this 	= $(this);
				let parent 	= $this.parent();
				let {id} 	= $this.data();

//				if(members.id)	delete members.id;
//				membersDel.push(id);
				if(members[id])	delete members[id];
				parent.remove();
			})
		}

		const do_lc_binding_event_add_customer = function(mod){
			$("#btn_add_customer").off("click").on("click", function(){
				let email = $(".inp-email-customer").val();
				let validate = validateEmail(email);
				if(!validate){
					$(".inp-email-customer").css("border", "1px solid red");
					return;
				}
				if(customers.indexOf(email) > -1){
					$(".inp-email-customer").css("border", "1px solid red");
					return
				}
				
				$("#div_list_email_customer").append(`<div class="mr-1"><button class="btn btn-secondary">${email}</button><a data-email='${email}' class='text-danger btn-remove-customer' data-toggle='tooltip' data-placement='top' title='' data-original-title='Delete'><i class='mdi mdi-close font-size-18'></i></a><div>`);
				$(".inp-email-customer").val("");
				customers.push(email);
				if(mod) customersAdd.push(email);
				
				$(".inp-email-customer").css("border", "unset");
				
				function validateEmail(email) {
				    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
				    return re.test(String(email).toLowerCase());
				}
				
				do_lc_binding_event_add_customer(mod);
			})
			
			
			$(".btn-remove-customer").off("click").on("click", function(){
					let $this 	= $(this);
					let {email} 	= $this.data();
					let parent 	= $this.parent();
					let index = customers.indexOf(email);
					customers.splice(index, 1);
					customersDel.push(email);
					
					let idx = customersAdd.indexOf(email);
					if(idx > -1) customersAdd.splice(idx, 1);
					
					parent.remove();
				})
			
		}
		//------------------------------------------------------------------------------------
		//-------------------------------------------------------------------------------------------------
		function do_get_availableTimeList(dp, dtBegin, dtEnd) {
			var ref 	= req_gl_Request_Content_Send("ServiceNsoGroup", "SVLst");
			ref.typ01s 	= TYP_01_MEETING;
			// ref.dtBegin	= dtBegin;
			// ref.dtEnd	= dtEnd;
			
			var fSucces	= [];
			fSucces.push(req_gl_funct(		null, do_show_list_available_time, [true, dp]));
			
			var fError 		= req_gl_funct(	null, do_show_list_available_time, [false]);
			App.network.do_lc_ajax(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError);
		}

		//--------------------------------------------------------------------------------------------
		function do_show_list_available_time(sharedJson, ajaxStat, dp) {
			if(ajaxStat){
				var code = sharedJson[App['const'].SV_CODE];
				if(code == App['const'].SV_CODE_API_YES) {
					pr_lstAvailableTime = [];
					var lstTime	= sharedJson[App['const'].RES_DATA];
					if(lstTime.length > 0){
						const dataSend = [];
						const lstCurObj = [];
						var curTime  = (new Date()).getTime();
						for (let i = 0; i < lstTime.length; i++) {
							if(lstTime[i]) {
								// var prop01 = JSON.parse(lstTime[i].cmt01);
							
								var curObj    	= {};
								curObj.text	  	= lstTime[i].name;
								curObj.id	 	= DayPilot.guid();
								curObj.toolTip  = lstTime[i].inf02.desc;
								curObj.tags   	= {};

								curObj.backColor 	= "#fff";
								curObj.borderColor  = "#1066a8";

								curObj.start  	 = lstTime[i].dt01;
								curObj.end    	 = lstTime[i].dt02;

								curObj.obj 		 = lstTime[i];

								// curObj.obj.cmt01 = JSON.parse(lstTime[i].cmt01);
								curObj.obj.inf02.dt01 = new Date(curObj.obj.inf02.dt01);
								curObj.obj.inf02.dt02 = new Date(curObj.obj.inf02.dt02);
								// get lst member
								dataSend.push(req_gl_Request_Content_Send_With_Params("ServiceNsoGroup", pr_SV_GET_MEMBER, {id: lstTime[i].id}));
								lstCurObj.push(curObj);
							}

						};	
						do_lc_get_list_member_when(dp, dataSend, lstTime, lstCurObj);	
					}
				}				
			} else {
				do_gl_show_Notify_Msg_Error($.i18n("common_err_ajax"));
			}

			if (dp) dp.update();
		}

		var do_lc_get_list_member_when = function (dp, dataSend, lstTime, lstCurObj){

			if (lstCurObj.length==0){			
				return;
			}

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_show_list_member_when, [dp, lstTime, lstCurObj]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	
			
			App.network.do_lc_ajax_when(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], dataSend, 100000, fSucces, fError) ;
		}

		var do_lc_show_list_member_when = function(sharedJson, dp, lstTime, lstCurObj){
			if(can_gl_AjaxSuccessAll(sharedJson)) {
				sharedJson.forEach((e, index) => {
					let prj			= lstTime[index];
					let curObj		= lstCurObj[index];
					let data 		= e[App['const'].RES_DATA];

					// const is_Me 		= data.find(m => m.uId == App.data.user.id && m.stat != STAT_APPOINTMENT_DESACTIVE);
					const isSuperAdmin 	= App.controller.common.Login && App.controller.common.Login.can_lc_User_SuperAdmin();
					const isOwner		= App.data.user.id === prj.uId;
					
					let objData 	= data.reduce((currentObj, mem)=>{
						if(mem.uId == prj.uId)	mem.isOwner = true;
						
						if(!isSuperAdmin && !isOwner){
							if(is_Me && is_Me.typ <= mem.typ && is_Me.lev >= mem.lev)	mem.notModif = true;
						}
						
						currentObj[mem.id] = mem;
						return currentObj;
					}, {});

					curObj.members = objData;

					// if (is_Me || isOwner || isSuperAdmin)	pr_lstAvailableTime.push(curObj);
					if (isOwner || isSuperAdmin)	pr_lstAvailableTime.push(curObj);
				});
				
				if (dp){
					dp.events.list 	= pr_lstAvailableTime;
					dp.update();
				}
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}			
		}

		var do_lc_handle_date = (strDate) => {
			let tmp = getDateEN(strDate);
			let res = {};
			res.dt = tmp.slice(0, 10);
			res.tm = tmp.slice(11, 16);
			return res;
		}

		var do_lc_build_view_member = (data, view) => {
			var div = "";
			if(data) {
				for (var key in data) {
					// build view lst member
					let classCss = "";
					let opacity  = "";
					if(data[key].stat == 2 ){
						classCss = "text-decoration-line-through";
						opacity  = "opacity-03";
					}
					
					let item 			= data[key].mem;
					let selOpt 			= `<div class='member-item'>`;

					
					if(item.avatar)	 selOpt 				+= `<img src='${ item.avatar.urlPrev}' class='rounded-circle avatar-xs ${opacity} avatar-autocomplete'/> <span class="${classCss}">${item.login01}</span>`;
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
						selOpt 			+= `<div class="media align-items-center"><div class="rounded-circle avatar-xs text-white text-uppercase text-center ${opacity} mr-1" style="background-color: ${textColor}"><div class="text-middle">${textAvatar}</div></div> <span class="${classCss}">${item.login01}</span></div>`;
					}
					
					selOpt 				+= `<a data-id='${item.id}' class='text-danger btn-remove-member' data-toggle='tooltip' data-placement='top' title='' data-original-title='Delete'><i class='mdi mdi-close font-size-18'></i></a>`;
					selOpt 				+= `</div>`;

					div += selOpt;

					// build data lst members
					members[item.id]  	= {id: key, uId: item.id, stat: data[key].stat, lev: data[key].lev, typ: data[key].typ};
				};
			}
			$(view).append(div);
		}
		
		var do_lc_build_view_customer = (customersStr, view) => {
			var div = "";
			if(customersStr) {
				let data = JSON.parse(customersStr);
				for (var key in data) {
					let email  			= data[key];
					let selOpt 			= `<div class='mr-1'><button class="btn btn-secondary">${email}</button><a data-email='${email}' class='text-danger btn-remove-customer' data-toggle='tooltip' data-placement='top' title='' data-original-title='Delete'><i class='mdi mdi-close font-size-18'></i></a></div>`;
					div += selOpt;
					
					customers.push(email);
				};
			}
			$(view).append(div);
		}

	
	};

	return PrjAppointmentList;
});