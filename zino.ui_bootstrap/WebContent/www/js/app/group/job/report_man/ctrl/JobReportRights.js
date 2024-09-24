/**
 * Right definitions for JOB REPORT
 */

define(['jquery'],
	function($) {

	var JobReportRights = function(){
		var pr_OBJ_TYPE					= 	2002;
		var RIGHT_ADM					= 	100;
		
		this.req_lc_Right = function(rightId){
			
			const rightCode = `${rightId}`.substr(rightId.length - 1)
			
			//Get user list right:
			var listUserRight = App.data.user.rights;

			if(!listUserRight){
				return -1;
			}

			if(listUserRight.includes(RIGHT_ADM)) return rightId
			if(listUserRight.includes(RIGHT_ADM + rightCode) 
				|| listUserRight.includes(pr_OBJ_TYPE * 1000 + rightCode)) return rightId

			return -1;
		}
	};

	return JobReportRights;
  });