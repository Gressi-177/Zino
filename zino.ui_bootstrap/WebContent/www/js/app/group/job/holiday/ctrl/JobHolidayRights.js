/**
 * Right definitions for JOB REPORT
 */

define(['jquery'],
	function($) {

	var JobHolidayRights = function(){
		
		var pr_OBJ_TYPE						= 	2001;
		var RIGHT_ADM						= 	100;
		
		var RIGHT_JOB_HOLIDAY_G		=	pr_OBJ_TYPE*1000 + 1;
		var RIGHT_JOB_HOLIDAY_N		=	pr_OBJ_TYPE*1000 + 2;
		var RIGHT_JOB_HOLIDAY_M		=	pr_OBJ_TYPE*1000 + 3;
		var RIGHT_JOB_HOLIDAY_D		=	pr_OBJ_TYPE*1000 + 4;
		
		this.req_lc_Right = function(rightId){
			//Get specific right:
			var rightCode 	= -1;
			var listRight = [
				RIGHT_JOB_HOLIDAY_G, 
				RIGHT_JOB_HOLIDAY_N, 
				RIGHT_JOB_HOLIDAY_M, 
				RIGHT_JOB_HOLIDAY_D
			];
			
			rightCode 	= listRight[rightId];
			
			//Get user list right:
			var listUserRight = App.data.user.rights;
			if(listUserRight.includes(RIGHT_ADM)) return rightId

			if(!listUserRight){
				return -1;
			}
			for(var i = 0; i < listUserRight.length; i++){
				if(rightCode == listUserRight[i]){
					return rightCode;
				}
			}
			return -1;
		}
	};

	return JobHolidayRights;
  });