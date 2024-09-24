/**
 * Right definitions for JOB REPORT
 */

define(['jquery'],
	function($) {

	var ChatRoomEmojis = function(){
		this.do_lc_show = function(){
			document.emojiSource = 'www/js/lib/hnv-emoji';
			do_gl_emoji ($("#i-emoji"), $("#inp_msg"))
		}
	};

	return ChatRoomEmojis;
  });