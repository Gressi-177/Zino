/**
 * Right definitions for JOB REPORT
 */

define(['jquery'],
	function($) {

	var JobHolidayRights = function(){
		this.req_lc_Right = function(rightId){
			var listUserRight = App.data.user.rights;
			if(!listUserRight){
				return -1;
			}
			
			if(listUserRight.includes(rightId)) return rightId;
			return -1;
		}
	};

	return JobHolidayRights;
  });