/**
 * Right definitions for JOB OFF
 */

define(['jquery'],
	function($) {

	var JobOffRights = function(){
		const pr_ADMIN 			= 100;
		
		this.req_lc_Right 		= function(rightId){
			const rId		  	= rightId % 10;
			var listUserRight 	= App.data.user.rights;
			if(!listUserRight){
				return -1;
			}
			
			if(listUserRight.includes(rightId) || listUserRight.includes(pr_ADMIN + rId)) return rightId;
			return -1;
		}
	};

	return JobOffRights;
  });