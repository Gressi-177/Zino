/**
 * Right definitions for JOB REPORT
 */

define(['jquery'],
	function($) {

	var JobReportRights = function(){
		
		//	RIGHT ACTION----------------------------------------
		var RIGHT_U_G		= 2002001;
		var RIGHT_U_N		= 2002002;
		var RIGHT_U_M		= 2002003;
		var RIGHT_U_D		= 2002004;

		var RIGHT_A_G		= 2002011;
		var RIGHT_A_N		= 2002012;
		var RIGHT_A_M		= 2002013;
		var RIGHT_A_D		= 2002014;
		
		this.req_lc_Right = function(rightId){
			
			var listUserRight = App.data.user.rights;
			if(!listUserRight){
				return -1;
			}
			
			if(listUserRight.includes(rightId)) return rightId;
			return -1;
			//return 1;
		}
	};

	return JobReportRights;
  });