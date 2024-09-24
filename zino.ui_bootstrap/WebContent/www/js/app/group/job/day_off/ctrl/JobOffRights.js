/**
 * Right definitions for JOB OFF
 */

define(['jquery'],
	function($) {

	var JobOffRights = function(){
		
		var pr_ADMIN					= 	100;
		var pr_OBJ_TYPE					= 	2020;
		
		var RIGHT_JOB_OFF_USER_G		=	pr_OBJ_TYPE*1000 + 1;
		var RIGHT_JOB_OFF_USER_N		=	pr_OBJ_TYPE*1000 + 2;
		var RIGHT_JOB_OFF_USER_M		=	pr_OBJ_TYPE*1000 + 3;
		var RIGHT_JOB_OFF_USER_D		=	pr_OBJ_TYPE*1000 + 4;
		
		var RIGHT_JOB_OFF_ADM_G			=	pr_OBJ_TYPE*1000 + 11;
		var RIGHT_JOB_OFF_ADM_N			=	pr_OBJ_TYPE*1000 + 12;
		var RIGHT_JOB_OFF_ADM_M			=	pr_OBJ_TYPE*1000 + 13;
		var RIGHT_JOB_OFF_ADM_D			=	pr_OBJ_TYPE*1000 + 14;
		
		this.req_lc_Right = function(rightId){
			const rId		  = rightId % 10;
			var listUserRight = App.data.user.rights;
			if(!listUserRight){
				return -1;
			}
			
			if(listUserRight.includes(rightId) || listUserRight.includes(pr_ADMIN + rId)) return rightId;
			return -1;
s		}
	};

	return JobOffRights;
  });