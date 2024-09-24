/**
 * Created by NGOC SON 19/07/2017
 * Last edit: 03/04/2018
 */
define([
        'jquery'
        ],
        function($) {

	var JobReportDateTime 	= function () {
		var tmplName		= App.template.names;
		var tmplCtrl		= App.template.controller;
		var self 			= this;
		var svClass 		= App['const'].SV_CLASS;
		var svName			= App['const'].SV_NAME;
		var sessId			= App['const'].SESS_ID;
		var userId          = App['const'].USER_ID;
		var fVar			= App['const'].FUNCT_SCOPE;
		var fName			= App['const'].FUNCT_NAME;
		var fParam			= App['const'].FUNCT_PARAM;
		//var url_header		= req_gl_LS_SecurityHeaderBearer (App.keys.KEY_STORAGE_CREDENTIAL);
		
		var holiday			= "HLD";
		//_______________________________
		var sunday 		= $.i18n('common_date_sunday');
		var monday 		= $.i18n('common_date_monday');
		var tuesday		= $.i18n('common_date_tuesday');
		var wednesday 	= $.i18n('common_date_wednesday');
		var thursday 	= $.i18n('common_date_thursday');
		var friday 		= $.i18n('common_date_friday');
		var saturday 	= $.i18n('common_date_saturday');
		
		var weekdays = [sunday, monday, tuesday, wednesday, thursday, friday, saturday];
		var d_day 		= "d_day";
		var d_weekday 	= "d_weekday";
		
		//_______________________________
		this.getcurrentReportCode = function(){
			var today = new Date();
			
			var currentMonth_ 	= today.getMonth() + 1;
			var currentMonth 	= currentMonth_.toString();
			if(currentMonth < 10){
				currentMonth 	= "0" + currentMonth_.toString();
			}
			
			var currentYear 	= today.getFullYear();
			
			App.data.currentReportCode = currentYear.toString()+ "-" + currentMonth;
		};
		
		this.getReportCode = function(){
			var today = new Date();
			var currentYear 		= today.getFullYear();
			
			if(!App.data.reportCodes) App.data.reportCodes = [];
			for(var i = 1;i<=12;i++){
				var month = i;
				if(month < 10){
					month 	= "0" + month.toString();
				}
				App.data.reportCodes.push({"date":currentYear.toString()+ "-" + month,"id":i})
			}
		};
		
		this.getcurrentAndPrevReportCode = function(){
			var today = new Date();
			
			var currentMonth_ 	= today.getMonth() + 1;
			var currentMonth 	= currentMonth_.toString();
			var prevMonth 		= today.getMonth().toString();
			if(currentMonth < 10){
				currentMonth 	= "0" + currentMonth_.toString();
			}
			if(prevMonth.toString() < 10){
				prevMonth 	= "0" + prevMonth.toString();
			}
			var currentYear 		= today.getFullYear();
			var YearOfPrevMonth 	= today.getFullYear();
			if(currentMonth == 1){
				YearOfPrevMonth		= YearOfPrevMonth-1;
			}
			
			
			if(!App.data.reportCodes) App.data.reportCodes = [];
			App.data.reportCodes.push({"date":YearOfPrevMonth.toString()+ "-" + prevMonth,"id":1})
			App.data.reportCodes.push({"date":currentYear.toString()+ "-" + currentMonth,"id":2});
		};
		this.getInfoDayOff = function(codeReport, uId02){
			App.data.monthData	 	= null;
			App.data.workingDay 	= null;
			
			if(!App.data.monthDataInfo){
				App.data.monthDataInfo 		= [];
			}
			if(!App.data.holidayDataInfo){
				App.data.holidayDataInfo 	= [];
			}
			
			for(var i = 0; i < App.data.monthDataInfo.length; i++){
				var record = App.data.monthDataInfo[i];
				if(codeReport == record.codeReport && uId02 == record.uId02){
					App.data.monthData  = record.monthData;
					App.data.workingDay = record.workingDay;
					return;
				}
			}

			var infoWd = null;
			if(!App.data.selectedUser.wdConfig){
				infoWd = "AFFFFFA";
				do_gl_show_Notify_Msg_Error($.i18n("job_rp_message_no_wdConfig"));
			} else {
				infoWd = App.data.selectedUser.wdConfig;
			}
			
			var sun = infoWd.charAt(0);
			var mon = infoWd.charAt(1);
			var tue = infoWd.charAt(2);
			var wed = infoWd.charAt(3);
			var thu = infoWd.charAt(4);
			var fri = infoWd.charAt(5);
			var sat = infoWd.charAt(6);
			
			var wDConfig = [sun, mon, tue, wed, thu, fri, sat];
			
			//Total days number of month
			var year 		= codeReport.substring(0,4);
			var month 		= codeReport.substring(5);
			
			var daysNumber 	= daysInMonth(month, year);
			var workingDay  = daysNumber*100;
				
			//Get Weekdays
			var firstdate = year + "-" + month + "-01";
			var dayOfWeek = new Date(firstdate).getDay();
			var monthData = [];
			for(var i=1; i < daysNumber+1; i++){
				var d = i.toString();
				if(i < 10){
					d = "0" + i.toString()
				}
				var dataname = "q" + d;
					
				var obj_day = {d_day : d, d_weekday : wDConfig[dayOfWeek], "dataname" : dataname, "datahover": weekdays[dayOfWeek] };
				monthData.push(obj_day);   
					
				switch(wDConfig[dayOfWeek]){
					case "H" : workingDay = workingDay - 50;  break;		//Work 1/2 day
					case "Q" : workingDay = workingDay - 75;  break;		//Work 1/4 day
					case "A" : workingDay = workingDay - 100; break;		//Absent
				}
					
				dayOfWeek++;
				if (dayOfWeek > 6){
					dayOfWeek = 0;
				}
			}
				
			for(var j = 0; j < App.data.holidayDataInfo.length; j++){
				var infoHoliday = App.data.holidayDataInfo[j];
				if(codeReport == infoHoliday.codeReport){
					do_build_month_data(infoHoliday, uId02, workingDay, monthData);
					return;
				}
			}
			
			doRequestHolidays(codeReport, uId02, workingDay, monthData);
		}
		
		function daysInMonth(month, year) {
		    return new Date(year, month, 0).getDate();
		}
	
		function doRequestHolidays(codeReport, uId02, workingDay, monthData){  	
			var ref				= {};
			ref[svClass] 		= "ServiceJobHoliday" ;
			ref[svName] 		= "SVJobHolidayLst";
			ref[userId]			= App.data.user.id;	
			ref[sessId]			= App.data.session_id;
			ref["code"]   		= codeReport;
			
			var fSucces		= [];
			var f01 = {}; 	f01[fVar]	= null;		f01[fName] = getHolidays;		f01[fParam]=[true, codeReport, uId02, workingDay, monthData];			
			fSucces.push(f01);

			var fError 		= {};
			fError[fVar]	= null; fError[fName] 	= getHolidays; fError[fParam]  = [false, null];
			App.network.ajax (App.path.BASE_URL_API_PRIV, url_header, ref,100000, fSucces, fError) ;       		
		}
		
		function getHolidays(sharedJson, ajaxOk, codeReport, uId02, workingDay, monthData){
			if(ajaxOk == true){
				if(sharedJson.sv_code 	== App['const'].SV_CODE_API_NOTFOUND){	
					var infoHoliday = {"codeReport":codeReport, "listHLD" : null};
					App.data.holidayDataInfo.push(infoHoliday);
					do_build_month_data(infoHoliday, uId02, workingDay, monthData);
				}
				else if (sharedJson.sv_code == App['const'].SV_CODE_OK ){
					var infoHoliday = {"codeReport":codeReport, "listHLD" : sharedJson.res_data};
					App.data.holidayDataInfo.push(infoHoliday);
					do_build_month_data(infoHoliday, uId02, workingDay, monthData);
				} else{
					do_gl_show_Notify_Msg_Error("Error get Holidays: " + sharedJson.sv_code);
				}
			}
			else{
				do_gl_show_Notify_Msg_Error("Ajax was not sent successfully.");
			}
		}
		
		function do_build_month_data(infoHoliday, uId02, workingDay, monthData){
			if(infoHoliday.listHLD){
				for(var i = 0; i < infoHoliday.listHLD.length; i++){
					var hld = infoHoliday.listHLD[i];
					var dateIndex = hld.dt.substring(8,10);
					monthData[parseInt(dateIndex) - 1]["datahover"] = hld.cmt;
					var oldValue = monthData[parseInt(dateIndex) - 1].d_weekday;
					switch(oldValue){
						case "F" : workingDay = workingDay - 100; break;		//Work 1 day
						case "H" : workingDay = workingDay - 50;  break;		//Work 1/2 day
						case "Q" : workingDay = workingDay - 25;  break;		//Work 1/4 day
					}
					monthData[parseInt(dateIndex) - 1].d_weekday = holiday;
				}
			}
			App.data.monthData 	= monthData;
			App.data.workingDay = parseFloat(workingDay)/100;
			var infoMonthData 	= {"monthData" : monthData, "codeReport" : infoHoliday.codeReport,  "workingDay" : parseFloat(workingDay)/100, "uId02" : uId02};
			App.data.monthDataInfo.push(infoMonthData);
		}
	}; 
	return JobReportDateTime;
});