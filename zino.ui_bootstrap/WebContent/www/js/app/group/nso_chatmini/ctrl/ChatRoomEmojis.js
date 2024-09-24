/**
 * Right definitions for JOB REPORT
 */

define(['jquery'],
	function($) {

	var ChatRoomEmojis = function(){
		this.do_lc_show = function(pr_div_content){
			document.emojiSource = 'www/js/lib/hnv-emoji';
			do_gl_emoji ($(pr_div_content + " .i-emoji"), $(pr_div_content + " .inp_msg"))
		}
	};

	return ChatRoomEmojis;
  });