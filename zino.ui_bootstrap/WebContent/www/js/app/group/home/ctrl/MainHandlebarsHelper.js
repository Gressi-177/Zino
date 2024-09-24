define([
	'handlebars'
	],
	function(Handlebars) {	
	//----------------------PRJ-----------------------------------------
	const do_lc_reqRandom_number 	= (min, max) => Math.floor(Math.random() * (max - min + 1)) + min;
	const formatFullDate 			= {"en": DateFormat.masks.enFullDate	, "fr": DateFormat.masks.frFullDate	, "vi": DateFormat.masks.viFullDate};
	const formatShortDate 			= {"en": DateFormat.masks.enShortDate	, "fr": DateFormat.masks.frShortDate, "vi": DateFormat.masks.viShortDate};
	const formatFullDateToMM		= {"en": DateFormat.masks.enFullDateToMM	, "fr": DateFormat.masks.frFullDateToMM	, "vi": DateFormat.masks.viFullDateToMM};
	const formatISOTime 			= DateFormat.masks.isoTime;
	const defautNumberFormat 		= "#,###.##";

	const pr_STAT_PRJ_NEW 			= 100100;
	const pr_STAT_PRJ_TODO 			= 100200;
	const pr_STAT_PRJ_INPROGRESS 	= 100300;
	const pr_STAT_PRJ_DONE 			= 100400;
	const pr_STAT_PRJ_TEST 			= 100500;
	const pr_STAT_PRJ_REVIEW 		= 100600;
	const pr_STAT_PRJ_FAIL 			= 100700;
	const pr_STAT_PRJ_UNRESOLVED 	= 100800;
	const pr_STAT_PRJ_CLOSED 		= 100900;

	const paramStats 				= [
		pr_STAT_PRJ_NEW,
		pr_STAT_PRJ_TODO,
		pr_STAT_PRJ_INPROGRESS,
		pr_STAT_PRJ_DONE,
		pr_STAT_PRJ_TEST,
		pr_STAT_PRJ_REVIEW,
		pr_STAT_PRJ_FAIL,
		pr_STAT_PRJ_UNRESOLVED,
		pr_STAT_PRJ_CLOSED,
	]

	const PRJ_MEMBER_LEVEL 			= {0: "prj_project_member_level_manager", 1: "prj_project_member_level_reporter", 2: "prj_project_member_level_worker", 3: "prj_project_member_level_watcher", 10: "prj_project_member_level_manager"};
	const PRJ_MEMBER_TYPE 			= {0: "prj_project_lev_bas"				, 1: "prj_project_lev_haute" , 4: "prj_project_lev_haute"};

	const PRJ_TYP_HISTORY 			= {1: "prj_unit_stat_todo", 2: "prj_unit_stat_executing"	, 3: "prj_unit_stat_fail"	, 4: "prj_unit_stat_pass", 5: "prj_unit_stat_abort", 6: "prj_unit_stat_blocked"	};
	const PRJ_TYP_HISTORY_COLOR 	= {1: "bg_todo", 2: "bg_executing"	, 3: "bg_fail"	, 4: "bg_pass", 5: "bg_abort", 6: "bg_blocked"	};
	const PRJ_TYP_TEST 				= {1: "prj_unit_test_manual", 2: "prj_unit_test_auto"	};
	const PRJ_LEVEL 				= {1: "prj_project_lev_01"	, 2: "prj_project_lev_02"	, 3: "prj_project_lev_03"	, 4: "prj_project_lev_04"};
	const PRJ_TYPE01 				= {1: "prj_project_type_01"	, 2: "prj_project_type_02"	, 3: "prj_project_type_03"	, 4: "prj_project_type_04"};
	const PRJ_STAT 					= {100100: "prj_project_stat_100100", 100200: "prj_project_stat_100200", 100300: "prj_project_stat_100300", 100400: "prj_project_stat_100400", 100500: "prj_project_stat_100500", 100600: "prj_project_stat_100600", 100700: "prj_project_stat_100700", 100800: "prj_project_stat_100800", 100900: "prj_project_stat_100900"};

	const PRJ_FILE_STAT 			= {0: "prj_file_stat_00"	, 1: "prj_file_stat_01"	, 2: "prj_file_stat_02"};

	const USER_TYPE 				= {2: "aut_user_ent_header_type_adm"	, 3: "aut_user_ent_header_type_agent"	, 5: "aut_user_ent_header_type_visistor"	, 6: "aut_user_ent_header_type_mentor", 8: "aut_user_ent_header_type_shipper"};
	const USER_STAT 				= {0: "aut_user_ent_header_stat_0"	    , 1: "aut_user_ent_header_stat_1"	    , 2: "aut_user_ent_header_stat_2"	        , 10: "aut_user_ent_header_stat_10"};
	const USER_LEGAL_STAT 			= {1020001: "per_person_mr"				, 1020002: "per_person_mrs"};
	
	
	const PARTNER_TYPE_PERSON		= {1000001: "per_person_type_moral"	, 1000002: "per_person_type_physic"};
	const PARTNER_CFGVAL02			= {1020001: "per_person_mr"		    , 1020002: "per_person_mrs",         1020003: "per_enterprise_sarl"	, 1020004: "per_enterprise_sa"	, 1020005: "per_enterprise_group"};
	const PARTNER_TYPE02			= {1010002: "per_person_typ_02"	    , 1010003: "per_person_typ_03"	, 1010004: "per_person_typ_04"	, 1010005: "per_person_typ_05"	, 1010006: "per_person_typ_06", 1010007: "per_person_typ_07", 1010008: "per_person_typ_08"};
	const PARTNER_STAT   			= {0: "per_partner_stat_00"	, 1: "per_partner_stat_01"	, 2: "per_partner_stat_02", 3: "per_partner_stat_03"	, 10: "per_partner_stat_10"	, 11: "per_partner_stat_11"};
	
	const PR_TYP_ADD 				= 1			, PR_TYP_MOD 	= 2			, PR_TYP_DEL = 3	, PR_TYP_JOIN 		= 4		, PR_TYP_MODIFY 	= 5		, PR_TYP_OUT 		= 6			, PR_TYP_COMMENT 	= 7		, PR_TYP_MOVE 		= 9;
	const PR_TAB_CONTENT 			= "content"	, PR_TAB_MEMBER = "member"	, PR_TAB_PRJ = "prj", PR_TAB_EPIC 		= "epic", PR_TAB_TASK 		= "task", PR_TAB_COMMENT 	= "comment"	, PR_TAB_FILE 		= "file";
	
	const pr_TABLE_CLASS_FAV		= {
		250000						: {
			"10,2,0"				: "text-primary", 	//Project Main
			"10,3,0"				: "text-primary", 	//Project Main
			"10,4,0"				: "text-primary", 	//Project Main

			"10,2,1"				: "text-danger", 	//Project Epic
			"10,3,1"				: "text-danger", 	//Project Epic
			"10,4,1"				: "text-danger", 	//Project Epic

			"10,2,2"				: "text-success", 	//Project Task
			"10,3,2"				: "text-success", 	//Project Task
			"10,4,2"				: "text-success", 	//Project Task
			
			"20,,0"					: "text-secondary",	//Project DataCenter
			"100,,"					: "text-warning",	//Project Sprint

			"30,1,1"				: "text-info",		//Project Test Unit
			"30,1,2"				: "text-info",		//Project Test Unit

			"30,2,1"				: "text-dark",		//Project Test Group
			"30,2,2"				: "text-dark",		//Project Test Group

			"100,,"					: "text-muted",		//Project Sprint
			"100,11,0"				: "text-muted",		//Project Sprint
			"100,12,0"				: "text-muted",		//Project Sprint
			"100,15,0"				: "text-muted",		//Project Sprint

			"200,,"					: "text-light",		//Project WORKFLOW

			//EMAIL
			"500,,2"				: "text-white-50",	//Project Email
			"500,,0"				: "text-white-50",	//Project Group Email
		},
		1000						: {
			",1,"					: "text-pink",		//User SUP_ADM
			",2,"					: "text-pink",		//User ADM
			",3,100"				: "text-pink",		//User AGENT
			",3,100"				: "text-pink",		//User AGENT
		}
	}

	const pr_TABLE_SRC_PAGE 		= {
		250000						: {
			//PRJ
			"10,2,0"				: "view_prj_project_content.html?id=#id&code=#code",	//IT		- PROJECT
			"10,2,1"				: "view_prj_project_content.html?id=#id&code=#code",	//IT		- EPIC
			"10,2,2"				: "view_prj_project_content.html?id=#id&code=#code",	//IT		- TASK
			"10,3,0"				: "view_prj_project_content.html?id=#id&code=#code",	//COMMERCE	- PROJECT
			"10,3,1"				: "view_prj_project_content.html?id=#id&code=#code",	//IT		- EPIC
			"10,3,2"				: "view_prj_project_content.html?id=#id&code=#code",	//COMMERCE	- TASK
			"10,4,0"				: "view_prj_project_content.html?id=#id&code=#code",	//OTHER		- PROJECT
			"10,4,1"				: "view_prj_project_content.html?id=#id&code=#code",	//IT		- EPIC
			"10,4,2"				: "view_prj_project_content.html?id=#id&code=#code",	//OTHER		- TASK

			//TEST
			"30,1,1"				: "view_prj_test_unit.html?id=#id&code=#code",			//TEST_UNIT
			"30,1,2"				: "view_prj_test_unit.html?id=#id&code=#code",			//TEST_UNIT
			"30,2,1"				: "view_prj_test_campaign.html?id=#id&code=#code",		//TEST_CAMPAIGN
			"30,2,2"				: "view_prj_test_campaign.html?id=#id&code=#code",		//TEST_CAMPAIGN
			
			//DATACENTER
			"20,,0"					: "view_prj_file_content.html?id=#id&code=#code",

			//SPRINT
			"100,,"					: "view_prj_sprint.html?id=#id&code=#code",
			"100,11,0"				: "view_prj_sprint.html?id=#id&code=#code",
			"100,12,0"				: "view_prj_sprint.html?id=#id&code=#code",
			"100,15,0"				: "view_prj_sprint.html?id=#id&code=#code",

			//WORKFLOW
			"200,,"					: "view_prj_workflow.html?id=#id&code=#code",

			//EMAIL
			"500,,2"				: "view_prj_email.html?id=#id&code=#code",
			"500,,0"				: "view_prj_email_group.html?id=#id&code=#code",
		},
		1000						: {
			",1,"					: "view_prj_user.html?id=#id&code=#code",		//User SUP_ADM
			",2,"					: "view_prj_user.html?id=#id&code=#code",		//User ADM
			",3,100"				: "view_prj_user.html?id=#id&code=#code",		//User AGENT
			",3,100"				: "view_prj_user.html?id=#id&code=#code",		//User AGENT
		}
	}

	const pr_ROUTE_PAGE 		= {
		250000						: {
			//PRJ
			"10,2,0"				: "VI_MAIN/prj_project_ent",	//IT		- PROJECT
			"10,2,1"				: "VI_MAIN/prj_project_ent",	//IT		- EPIC
			"10,2,2"				: "VI_MAIN/prj_project_ent",	//IT		- TASK
			"10,3,0"				: "VI_MAIN/prj_project_ent",	//COMMERCE	- PROJECT
			"10,3,1"				: "VI_MAIN/prj_project_ent",	//IT		- EPIC
			"10,3,2"				: "VI_MAIN/prj_project_ent",	//COMMERCE	- TASK
			"10,4,0"				: "VI_MAIN/prj_project_ent",	//OTHER		- PROJECT
			"10,4,1"				: "VI_MAIN/prj_project_ent",	//IT		- EPIC
			"10,4,2"				: "VI_MAIN/prj_project_ent",	//OTHER		- TASK

			//TEST
			"30,1,1"				: "VI_MAIN/prj_test_unit",			//TEST_UNIT
			"30,1,2"				: "VI_MAIN/prj_test_unit",			//TEST_UNIT
			"30,2,1"				: "VI_MAIN/prj_test_campaign",		//TEST_CAMPAIGN
			"30,2,2"				: "VI_MAIN/prj_test_campaign",		//TEST_CAMPAIGN

			//DATACENTER
			"20,,0"					: "VI_MAIN/prj_file_ent",

			//SPRINT
			"100,,"					: "VI_MAIN/prj_sprint",
			"100,11,0"				: "VI_MAIN/prj_sprint",
			"100,12,0"				: "VI_MAIN/prj_sprint",
			"100,15,0"				: "VI_MAIN/prj_sprint",

			//WORKFLOW
			"200,,"					: "VI_MAIN/prj_workflow",

			//EMAIL
			"500,,2"				: "VI_MAIN/prj_email",
			"500,,0"				: "VI_MAIN/prj_email_group",
		},
		1000						: {
			",1,"					: "VI_MAIN/prj_user",		//User SUP_ADM
			",2,"					: "VI_MAIN/prj_user",		//User ADM
			",3,100"				: "VI_MAIN/prj_user",		//User AGENT
			",3,100"				: "VI_MAIN/prj_user",		//User AGENT
		}
	}

	const strTyp 					= {
			1			: "prj_dashboard_history_typ_add"	, 2			: "prj_dashboard_history_typ_mod"	, 
			3			: "prj_dashboard_history_typ_del"	, 4			: "prj_dashboard_history_typ_join"	,
			5			: "prj_dashboard_history_typ_modify"	, 6			: "prj_dashboard_history_typ_out"	,
			7			: "prj_dashboard_history_typ_comment", 9			: "prj_dashboard_history_typ_move"	, 
			10			: "prj_dashboard_history_typ_late"	, 11		: "prj_dashboard_history_typ_has_change",
			12			: "prj_dashboard_history_typ_receive", 	
			
			"content"	: "prj_dashboard_history_tab_content", "member"	: "prj_dashboard_history_tab_member"	,
			"prj"		: "prj_dashboard_history_tab_prj"	, "epic"	: "prj_dashboard_history_tab_epic"	,
			"task"		: "prj_dashboard_history_tab_task"	, "comment"	: "prj_dashboard_history_tab_comment",
			"file"		: "prj_dashboard_history_tab_file"	, "customer": "prj_dashboard_history_tab_customer",
			"off"		: "prj_dashboard_history_tab_day_off", "report"	: "prj_dashboard_history_tab_report" , 
			"meeting"	: "prj_dashboard_history_tab_meeting", 	
		}
	
	const pr_TYPE02_PRJ				= 0;
	const pr_TYPE02_EPIC			= 1;
	const pr_TYPE02_TASK			= 2;
	
	const pr_EXTENSION_DOC 			= {gif : "gif", jpg : "jpg", png : "png", txt: "txt", md : "md", json : "json", js : "js", css : "css", html : "html", doc : "doc", jpeg : "jpg", docx : "doc", pdf : "pdf"};
	
	const STAT_PARTNER = {
			0: "per_partner_stat_00",	1: "per_partner_stat_01",	2: "per_partner_stat_02",
			3: "per_partner_stat_03",	10: "per_partner_stat_10",	11: "per_partner_stat_11",	100: "per_partner_stat_null"
	}
	
	const PR_ICON_FOLDER = {
			"INBOX"		: "mdi-email-outline"		, "Sent": "mdi-email-check-outline"	, "Trash"			: "mdi-trash-can-outline"	, "[Gmail]"	: "mdi-gmail",
			"Corbeille"	: "mdi-trash-can-outline"	, "Spam": "mdi-bacteria-outline"	, "Objets envoyés"	: "mdi-email-check-outline" , "Brouillons": "mdi-file-outline",
			"Archives"	: "mdi-bag-personal-outline", "folder" : "mdi-folder-outline"
	}

	const PR_LANGUAGE_FOLDER = {
			"INBOX"		: "prj_email_folder_inbox", "Sent": "prj_email_folder_sent", "Trash"			: "prj_email_folder_trash", "[Gmail]"	: "prj_email_folder_gmail",
			"Corbeille"	: "prj_email_folder_trash", "Spam": "prj_email_folder_spam", "Objets envoyés"	: "prj_email_folder_sent" , "Brouillons": "prj_email_folder_draft",
			"Archives"	: "prj_email_folder_archive"
	}
	
	const TYP_USER = {
			2: "aut_user_ent_header_type_adm"	,	3: "aut_user_ent_header_type_agent"	,	5: "aut_user_ent_header_type_member",
			6: "aut_user_ent_header_type_mentor",	8: "aut_user_ent_header_type_shipper", 4: "aut_user_ent_header_type_member_externe"
	}
	
	const SOCIAL_NETWORK = {
			"fb": {label: "Facebook", bgColor: "primary"}, "tw": {label: "Twitter", bgColor: "info"}, "ln": {label: "LinkedIn", bgColor: "info"}, "gg": {label: "Google", bgColor: "danger"}, "ig": {label: "Instagram", bgColor: "pink"}
	}

	const SOCIAL_NETWORK_URL = {
			"fb": {label: "https://www.facebook.com/"}, "tw": {label: "https://twitter.com/"}, "ln": {label: "https://www.linkedin.com/"}, "gg": {label: "https://www.google.com/"}, "ig": {label: "https://www.instagram.com/"}
	}
	
	Handlebars.registerHelper("reqSrcAvatarPrjLst", function(prj) {
		if(!prj.avatar){
			return UI_URL_ROOT + "img/prj/companies/img-" 		+ do_lc_reqRandom_number(1, 1) 	+ ".png";
		}else{
			let path = "";
			path = prj.avatar.urlPrev || prj.avatar.url;
			return  path;
		}
	});
	
	Handlebars.registerHelper("reqSrcAvatarPrj", function(prj) {
		if(!prj.files){
			return UI_URL_ROOT + "img/prj/companies/img-" 		+ do_lc_reqRandom_number(1, 1) 	+ ".png";
		}else{
			let path = "";
			try {
				const file = prj.files.filter(f => f.typ01==1 && f.typ02==1)[0];
				path = file.urlPrev || file.url
			}catch(e){
				return UI_URL_ROOT + "img/prj/companies/img-" 	+ do_lc_reqRandom_number(1, 1) 	+ ".png";
			}
			return  path;
		}
	});
	
	Handlebars.registerHelper("reqSrcAvatarPrjEpic", function(prj) {
		if(!prj.files){
			return UI_URL_ROOT + "img/prj/companies/img-" 		+ do_lc_reqRandom_number(2, 2) 	+ ".png";
		}else{
			let path = "";
			try {
				const file = prj.files.filter(f => f.typ01==1 && f.typ02==1)[0];
				path = file.urlPrev || file.url
			}catch(e){
				return UI_URL_ROOT + "img/prj/companies/img-" 	+ do_lc_reqRandom_number(2, 2) 	+ ".png";
			}
			return  path;
		}
	});
	
	Handlebars.registerHelper("reqSrcAvatarPrjTask", function(prj) {
		if(!prj.files){
			return UI_URL_ROOT + "img/prj/companies/img-" 		+ do_lc_reqRandom_number(3, 3) 	+ ".png";
		}else{
			let path = "";
			try {
				const file = prj.files.filter(f => f.typ01==1 && f.typ02==1)[0];
				path = file.urlPrev || file.url
			}catch(e){
				return UI_URL_ROOT + "img/prj/companies/img-" 	+ do_lc_reqRandom_number(3, 3) 	+ ".png";
			}
			return  path;
		}
	});

	Handlebars.registerHelper("reqSrcAvatarPartner", function(partner) {
		if(!partner.files || !partner.files.length){
			return UI_URL_ROOT + "img/prj/companies/img-" 		+ do_lc_reqRandom_number(1, 1) 	+ ".png";
		}else{
			return App.path.LOCATION_URL_HREF + partner.files[0].path01;
		}
	});
	
	Handlebars.registerHelper("reqSrcAvatarPartnerPrj", function(prj) {
		if(!prj.files){
			return UI_URL_ROOT + "img/prj/companies/img-" 		+ do_lc_reqRandom_number(1, 1) 	+ ".png";
		}else{
			let path = "";
			try {
				path = prj.files.filter(f => f.typ01==2 && f.typ02==1)[0].path01;
			}catch(e){
				return UI_URL_ROOT + "img/prj/companies/img-" 	+ do_lc_reqRandom_number(1, 1) 	+ ".png";
			}
			return App.path.LOCATION_URL_HREF + path;
		}
	});

	Handlebars.registerHelper("reqSrcAvatarMember", function(mem) {
		if(mem.avatar)	return mem.avatar.urlPrev;

		if(mem.files && mem.files.length){
			let avatar = mem.files.filter(f => f.typ01==1 && f.typ02==1);
			if(avatar &&  avatar.length)	return  avatar[0].path01;
		}
		return UI_URL_ROOT + "img/prj/users/avatar-" 			+ do_lc_reqRandom_number(1, 1) 	+ ".jpg";
	});
	
	Handlebars.registerHelper("reqSrcImg", function(file) {
		if(file.urlPrev)	return file.urlPrev;
		else return file.url
	});
	
	Handlebars.registerHelper("imageErrorUser", function() {
		return `this.src = 'www/img/prj/users/avatar-1.jpg'`;
	});

	Handlebars.registerHelper("reqSrcAvatarChat", function(mem) {
		if(mem.avatar)	return App.path.LOCATION_URL_HREF 		+ mem.avatar.urlPrev;

		if(mem.files && mem.files.length){
			let avatar = mem.files.filter(f => f.typ01==1 && f.typ02==1);
			if(avatar &&  avatar.length)	return App.path.LOCATION_URL_HREF + avatar[0].path01;
		}
		return UI_URL_ROOT + "img/prj/users/avatar-" 			+ do_lc_reqRandom_number(1, 1) 	+ ".jpg";
	});
	
	Handlebars.registerHelper("reqSrcTextAvatar", function(login) {
		if(!login)	return UI_URL_ROOT + "img/prj/users/avatar-" 			+ do_lc_reqRandom_number(1, 1) 	+ ".jpg";
		let first = login.charAt(0);
		let last  = login.charAt(login.length - 1);
		return first + last;
	});
	
	Handlebars.registerHelper("reqSrcTextColor", function(login) {
		if(!login)	return var_gl_colors[0];
		let first = login.charAt(0);
		let last  = login.charAt(login.length - 1);
		let index = var_gl_alphabet.indexOf(first.toLowerCase());
		return var_gl_colors[index];
		
	});
	
	Handlebars.registerHelper("reqSrcTextAvatarRelateChat", function(login) {
		if(!login)	return UI_URL_ROOT + "img/prj/users/avatar-" 			+ do_lc_reqRandom_number(1, 1) 	+ ".jpg";
		if(login === "HNV-TECH.COM") login = "HV"
		let first = login.charAt(0);
		let last  = login.charAt(login.length - 1);
		return first + last;
	});
	
	Handlebars.registerHelper("reqSrcTextColorRelateChat", function(login) {
		if(!login)	return var_gl_colors[0];
		if(login === "HNV-TECH.COM") login = "HV"
		let first = login.charAt(0);
		let last  = login.charAt(login.length - 1);
		let index = var_gl_alphabet.indexOf(first.toLowerCase());
		return var_gl_colors[index];
		
	});
	
	Handlebars.registerHelper("reqSrcTextLoginRelateChat", function(login) {
		if(!login)	return "";
		if(login === "HNV-TECH.COM") return "";
		return login;
	});

	Handlebars.registerHelper("reqSrcAvatarUser", function(prj) {
		if(!prj.files){
			return UI_URL_ROOT + "img/prj/users/avatar-" 		+ do_lc_reqRandom_number(1, 1) 	+ ".jpg";
		}else{
			let path = "";
			try {
				path = prj.files.filter(f => f.typ01==1 && f.typ02==1)[0].path01;
			}catch(e){
				return UI_URL_ROOT + "img/prj/users/avatar-" 	+ do_lc_reqRandom_number(1, 1) 	+ ".jpg";
			}
			return App.path.LOCATION_URL_HREF + path;
		}
	});

	Handlebars.registerHelper("reqSrcAvatarUserDashbord", function(user) {
		if(user.avatar)	return App.path.LOCATION_URL_HREF 		+ user.avatar.urlPrev;

		if(user.files){
			let fileAvatar = user.files.find(f => f.typ01 == 1 && f.typ02 == 1);
			if(fileAvatar)	return App.path.LOCATION_URL_HREF 	+ fileavatar.urlPrev;
		}

		return UI_URL_ROOT + "img/prj/users/avatar-" 			+ do_lc_reqRandom_number(1, 1) 	+ ".jpg";
	});

	Handlebars.registerHelper("reqStrTitleChatPrj", function(str) {
		if(!str)	return "";
		if(str && str.length > 30){
			return str.substr(0,30) + "...";
		}
		return str;
	});
	
	Handlebars.registerHelper("reqSubStrDescrPrj", function(str) {
		if(!str)	return "";
		if(str && str.length > 25){
			return str.substr(0,25) + "...";
		}
		return str;
	});
	
	Handlebars.registerHelper('extractMsgHtml', function (msg, length, options) {
		let s = msg;
		if(s && s.length) {
			try{
				let lstStr = s.replace(/<\/?[^>]+(>|$)/g, "^").split("^");
				s = lstStr.filter(Boolean).join(" ");
			}catch(e){
				console.log(e);
			}
			// if (!s.length) s=msg;

			if (s.length > length){
				s = s.substring(0, length) + "...";
			}
		}	
		return s;		
	});

	Handlebars.registerHelper("reqNameFilePrj", function(str) {
		if(!str)	return "";
		let index = str.indexOf(".");
		if(index > 15) {
			return str.substr(0, 15) + "..." + str.substr(index)
		}
		return str;
	});
	
	Handlebars.registerHelper("reqNameFilePrjDataCenter", function(str) {
		if(!str)	return "";
		if(str && str.length > 30){
			let length = str.length;
			return "..." + str.substr(length - 30, length);
		}
		return str;
	});

	Handlebars.registerHelper("reqSizeFile", function(str) {
		return req_gl_FileSize(str);
	});

	Handlebars.registerHelper("reqFormatDate", function(date) {
		if(!date)	return "";
		let local = localStorage.language ? localStorage.language : "en";
		return DateFormat(date, formatFullDate[local]);
	});
	
	Handlebars.registerHelper("reqFormatDateToMM", function(date) {
		if(!date)	return "";
		let local = localStorage.language ? localStorage.language : "en";
		return DateFormat(date, formatFullDateToMM[local]);
	});
	
	
	Handlebars.registerHelper("reqFormatShortDate", function(date) {
		if(!date)	return "";
		let local = localStorage.language ? localStorage.language : "en";
		return DateFormat(date, formatShortDate[local]);
	});
	
	Handlebars.registerHelper("reqFormatISOTime", function(date) {
		if(!date)	return "";
		return DateFormat(date, formatISOTime);
	});

	Handlebars.registerHelper("reqFormatNumber", function(value) {
		if(value == null)		return "";
		if(value == 0)			return 0;
		let local = localStorage.language ? localStorage.language : "en";
		return $.formatNumber(value, {format: defautNumberFormat, local});
	});
	
	

	Handlebars.registerHelper("reqBudgtetReal", function(val02, val01, typCurrency) {
		if(!val01 && !val02)	return "";
		let val = val02 ? val02 : val01;
		
		return val;
	});

	Handlebars.registerHelper("reqLevelMember", function(level) {
		if(level === undefined)	return "";
		return $.i18n(PRJ_MEMBER_LEVEL[+level]);
	});

	Handlebars.registerHelper("reqTypeMember", function(typ) {
		if(typ === undefined)	return "";
		return $.i18n(PRJ_MEMBER_TYPE[+typ]);
	});

	Handlebars.registerHelper("reqTypPrj", function(typ) {
		if(typ === undefined)	return "";
		return $.i18n(PRJ_TYPE01[+typ]);
	});

	Handlebars.registerHelper("reqTypTest", function(typ) {
		if(typ === undefined)	return "";
		return $.i18n(PRJ_TYP_TEST[+typ]);
	});

	Handlebars.registerHelper("reqTypHistoryColor", function(typ) {
		if(typ === undefined)	return "";
		return PRJ_TYP_HISTORY_COLOR[+typ];
	});
	Handlebars.registerHelper("reqTypHistory", function(typ) {
		if(typ === undefined)	return "";
		return $.i18n(PRJ_TYP_HISTORY[+typ]);
	});

	Handlebars.registerHelper("reqStatPrj", function(stat) {
		if(stat === undefined)	return "";
		if(PRJ_STAT[+stat]) return $.i18n(PRJ_STAT[+stat]);
		return $.i18n("prj_project_stat_undefined");
	});

	Handlebars.registerHelper("reqSelectedStat", function(lstStats, stat) {
		const sInd = !lstStats?-1:lstStats.findIndex(s => +s.id === +stat)

		if(sInd === -1) return $.i18n("prj_project_stat_undefined");

		return lstStats[sInd].trans ? $.i18n(lstStats[sInd].trans) : lstStats[sInd].lab;
	});
	
	Handlebars.registerHelper("reqStatFilePrj", function(stat) {
		if(stat === undefined)	 return "";
		return $.i18n(PRJ_FILE_STAT[+stat]);
	});
	
	Handlebars.registerHelper("reqTypUser", function(typ) {
		if(typ === undefined)	return "";
		return $.i18n(USER_TYPE[+typ]);
	});

	Handlebars.registerHelper("reqStatUser", function(stat) {
		if(stat === undefined)	return "";
		return $.i18n(USER_STAT[+stat]);
	});
	
	Handlebars.registerHelper("reqLegalStatUser", function(cfgVal02) {
		if(cfgVal02 === undefined)	return "";
		return $.i18n(USER_LEGAL_STAT[+cfgVal02]);
	});
	
	Handlebars.registerHelper("reqCfgVal02Partner", function(cfg) {
		if(cfg === undefined)	return "";
		return $.i18n(PARTNER_CFGVAL02[+cfg]);
	});
	
	Handlebars.registerHelper("reqTyp02Partner", function(typ) {
		if(typ === undefined)	return "";
		return $.i18n(PARTNER_TYPE02[+typ]);
	});

	Handlebars.registerHelper("reqStatPartnerPrj", function(stat) {
		if(stat === undefined)	return "";
		return $.i18n(PARTNER_STAT[+stat]);
	});
	
	Handlebars.registerHelper("reqTyp01Partner", function(typ) {
		if(typ === undefined)	return "";
		return $.i18n(PARTNER_TYPE_PERSON[+typ]);
	});

	Handlebars.registerHelper("reqLevPrj", function(lev) {
		if(lev === undefined)	return "";
		return $.i18n(PRJ_LEVEL[+lev]);
	});

	Handlebars.registerHelper("reqLevColor", function(lev) {
		if(lev === undefined)	return "info";
		if(lev == 1)	return "info";
		if(lev == 2)	return "primary";
		if(lev == 3)	return "warning";
		if(lev == 4)	return "danger";

		return "info";
	});

	Handlebars.registerHelper("reqNameMember", function(str) {
		if(!str)	return "";
		if(str && str.length > 10){
			return str.substr(0, 10) + "...";
		}
		return str;
	});

	Handlebars.registerHelper('concat', function(str1, str2) {
		return str1 + str2;
	});

	Handlebars.registerHelper('reqStatColor', function(stat) {
		if(stat === 0)	return "danger";
		if(stat === 1)	return "warning";
		return "success";
	});
	
	Handlebars.registerHelper('reqStatIcon', function(stat) {
		if(stat === 0)	return "lock-open-variant-outline";
		if(stat === 1)	return "lock-clock";
		if(stat === 2)	return "progress-clock";
		if(stat === 3)	return "lock-open-outline";
		if(stat === 4)	return "lock-outline";
		if(stat === 5)	return "lock";
		if(stat === 6)	return "lock-alert";
		if(stat === 7)	return "block-helper";
		return "";
	});

	Handlebars.registerHelper('reqContentHis', function(cmt, entID) {
		if(!cmt)	return "";
		let data 		= JSON.parse(cmt);

		let str 		= $.i18n("prj_dashboard_history_init");
		if(data.typ)	str += $.i18n(strTyp[data.typ]) + " ";
		if(data.typTab)	str += $.i18n(strTyp[data.typTab]) + " ";
		if(data.title)	str += "<a href='#' class='a_view_prj' data-id='" + entID + "'>" +data.title + "</a> ";

		if(data.typ == PR_TYP_MOVE){
			str += $.i18n("prj_dashboard_history_from") + " " + $.i18n(PRJ_STAT[data.statFrom]) + " " + $.i18n("prj_dashboard_history_to") + " " + $.i18n(PRJ_STAT[data.statTo]);
		}

		return str;
	});

	Handlebars.registerHelper('reqPercentComplete', function(val05) {
		if(!val05) 		return "0";
		if(val05 > 100)	return "100";
		return Math.floor(+val05);
	});

	Handlebars.registerHelper('reqDateLateTask', function(date, stat) {
		if(stat && (stat == 4 || stat == 5))	return "";
		
		if(!date)			return "";
		let diffDays 		= req_gl_DayDiff(date);
		let nbDays 			= Math.abs(diffDays);

		if(diffDays < 0) 	return "<span class='badge badge-danger badge-pill'>" + $.i18n("prj_project_expired_late") + nbDays + $.i18n("prj_project_expired_day")+ "</span>";
		if(diffDays > 10)	return "";
		
		return "<span class='badge badge-warning badge-pill'>" + $.i18n("prj_project_expired_in") + nbDays + $.i18n("prj_project_expired_day") + "</span>";
	});
	
	Handlebars.registerHelper('reqDateTimeLateTask', function(date, stat) {
		if(stat && (stat == pr_STAT_PRJ_DONE || stat == pr_STAT_PRJ_CLOSED))	return "";
		
		if(!date)			return "";
		
		var today    = new Date();
		var deadLine = new Date(date);
		
		var diffDays = Math.floor((today - deadLine) / 86400000); // days
		var diffHrs  = Math.floor(((today - deadLine) % 86400000) / 3600000); // hours
		var diffMins = Math.round((((today - deadLine) % 86400000) % 3600000) / 60000); 
		
		let nbDays 			= Math.abs(diffDays);
		let nbHrs			= Math.abs(diffHrs);
		let nbMins 			= Math.abs(diffMins);
		if((deadLine - today) > 0){
			if(diffDays == -1 && diffHrs == -1) return "<span class='badge badge-warning badge-pill'>" + $.i18n("prj_project_expired_in") + nbMins + $.i18n("prj_project_expired_minutes") + "</span>";
			if(diffDays == -1 ) return "<span class='badge badge-warning badge-pill'>" + $.i18n("prj_project_expired_in") + nbHrs + $.i18n("prj_project_expired_hours") + nbMins + $.i18n("prj_project_expired_minutes") + "</span>";
			if(nbDays > 10)	return "";
			
			return "<span class='badge badge-warning badge-pill'>" + $.i18n("prj_project_expired_in") + nbDays + $.i18n("prj_project_expired_day") + nbHrs + $.i18n("prj_project_expired_hours") + "</span>";
		}else{
			if(diffDays == 0 && diffHrs == 0) return "<span class='badge badge-danger badge-pill'>" + $.i18n("prj_project_expired_late") + nbMins + $.i18n("prj_project_expired_minutes") + "</span>";
			if(diffDays == 0 )               return "<span class='badge badge-danger badge-pill'>" + $.i18n("prj_project_expired_late") + nbHrs + $.i18n("prj_project_expired_hours") + nbMins + $.i18n("prj_project_expired_minutes") + "</span>";
			
			return  "<span class='badge badge-danger badge-pill'>" + $.i18n("prj_project_expired_late") + nbDays + $.i18n("prj_project_expired_day") + nbHrs + $.i18n("prj_project_expired_hours") + "</span>";
		}
	});
	
	Handlebars.registerHelper('reqDateLate', function(date, stat) {
		if(stat && stat == pr_STAT_PRJ_CLOSED)	return "";
		
		if(stat && stat == pr_STAT_PRJ_DONE){
			return "<span class='badge badge-success badge-pill'>" + $.i18n(PRJ_STAT[4]) + "</span>";
		}
		if(!date)			return "";
		let diffDays 		= req_gl_DayDiff(date);
		let nbDays 			= Math.abs(diffDays);

		if(diffDays < 0) 	return "<span class='badge badge-danger badge-pill'>" + $.i18n("prj_project_expired_late") + nbDays + $.i18n("prj_project_expired_day")+ "</span>";
		if(diffDays > 10)	return "";
		
		return "<span class='badge badge-warning badge-pill'>" + $.i18n("prj_project_expired_in") + nbDays + $.i18n("prj_project_expired_day") + "</span>";
	});

	Handlebars.registerHelper("reqNameCustomer", function(str) {
		if(!str)	return "";
		if(str && str.length > 10){
			return str.substr(0, 10) + "...";
		}
		return str;
	});

	Handlebars.registerHelper("reqClassFav", function(ID_TABLE, typ00, typ01, typ02) {
		let key = [typ00, typ01, typ02].join(",")

		if(!pr_TABLE_CLASS_FAV[ID_TABLE] || !pr_TABLE_CLASS_FAV[ID_TABLE][key]) return ""

		return pr_TABLE_CLASS_FAV[ID_TABLE][key];
	});

	Handlebars.registerHelper("reqRoutePage", function(ID_TABLE, typ00, typ01, typ02) {
		let key = [typ00, typ01, typ02].join(",")
		
		if(!pr_ROUTE_PAGE[ID_TABLE] || !pr_ROUTE_PAGE[ID_TABLE][key]) return "#"
		  
		return pr_ROUTE_PAGE[ID_TABLE][key];
	});

	Handlebars.registerHelper("reqSrcPage", function(ID_TABLE, typ00, typ01, typ02, id, code) {
		let key = [typ00, typ01, typ02].join(",")

		const map = {
			'#id': id,
			'#code': code,
		};
		
		if(!pr_TABLE_SRC_PAGE[ID_TABLE] || !pr_TABLE_SRC_PAGE[ID_TABLE][key]) return "#"
		  
		return pr_TABLE_SRC_PAGE[ID_TABLE][key].replace(/#id|#code/gi, m => map[m]);
	});

	Handlebars.registerHelper("reqSrcAvatarCustomer", function(cus) {
		if(!cus.avatar){
			return UI_URL_ROOT + "img/prj/users/avatar-" 		+ do_lc_reqRandom_number(1, 1) 	+ ".jpg";
		}else{
			return App.path.LOCATION_URL_HREF + cus.avatar.urlPrev;
		}
	});

	/*
	Handlebars.registerHelper('reqCodePrjNotify', function(content) {
		if(!content)	return "";
		let data 		= JSON.parse(content);
		if(data && data.title)	return data.title;

		return "";
	});

	Handlebars.registerHelper('reqIdPrjNotify', function(content) {
		if(!content)	return "";
		let data 		= JSON.parse(content);
		if(data && data.title)	return data.parID;

		return "";
	});
	*/
	
	// Handlebars.registerHelper('reqContentNotify', function(cmt, history, entID) {
	// 	if(!cmt)	return "";
	// 	let data 		= cmt; //JSON.parse(cmt);

	// 	let hisData = null; ;
	// 	if (history && history.cmt) {
	// 		hisData 		= JSON.parse(history.cmt);
	// 		hisData			= hisData[hisData.length - 1];
	// 	}
		
	// 	const isPassive = (data.typTab == "comment" || data.typTab == "content" || (data.typTab == "member" && data.typ == 11));
	// 	let str 		= $.i18n(isPassive ? "prj_dashboard_notify_init_comment": "prj_dashboard_history_init") + " ";
	// 	if (isPassive && hisData && hisData.uName && data.typTab == "content") {
	// 		str = hisData.uName +  " ";
	// 	}

	// 	if(data.typ)	str += $.i18n(strTyp[data.typ]) + " ";
	// 	if(data.typTab)	str += $.i18n(strTyp[data.typTab]) + " ";
	// 	if(data.title) {
	// 		if (data.typTab == "report") 
	// 			str += "<a href='view_prj_job_report_man.html'>" +data.title + "</a> ";
	// 		else 
	// 		if (data.typTab == "off") 	
	// 			str += "<a href='view_prj_job_off_man.html'>" +data.title + "</a> ";	
	// 		else 
	// 			str += "<a href='#' class='a_view_prj' data-id='" + entID + "'>" +data.title + "</a> "; 
	// 	}	

	// 	if(data.typ == PR_TYP_MOVE){
	// 		str += $.i18n("prj_dashboard_history_from") + " " + $.i18n(PRJ_STAT[data.statFrom]) + " " + $.i18n("prj_dashboard_history_to") + " " + $.i18n(PRJ_STAT[data.statTo]);
	// 	}

	// 	if(data.typTab == "content" && hisData.statFrom != null){
	// 		str += $.i18n("prj_dashboard_history_from") + " " + $.i18n(PRJ_STAT[hisData.statFrom]) + " " + $.i18n("prj_dashboard_history_to") + " " + $.i18n(PRJ_STAT[hisData.statTo]);
	// 	}

	// 	return str;
	// });

	Handlebars.registerHelper('reqFirstLetter', function(str) {
		if(!str)	return "A";

		return str.trim().substr(0,1).toUpperCase();
	});

	Handlebars.registerHelper('reqTypPerson', function(typ01) {
		if(!typ01)	return "";
		let objTyp = App.data.cfgValListTypePerson.find(item => item.id == typ01);
		if(objTyp)	return $.i18n(objTyp.val01);
		return "";
	});

	Handlebars.registerHelper('reqLegalStatus', function(cfgVal02, typ01) {
		let listLegalStatus   = App.data.cfgValListTypeLegalStatM;
		if(typ01 && typ01 == 1000002){
			listLegalStatus = App.data.cfgValListTypeLegalStatN;
		}


		if(!typ01)	return "";
		let objTyp = listLegalStatus.find(item => item.id == cfgVal02);
		if(objTyp)	return $.i18n(objTyp.val01);
		return "";
	});

	Handlebars.registerHelper('reqTypPartner', function(typ02) {
		if(!typ02)	return "";
		let objTyp = App.data.cfgValListTypePartner.find(item => item.id == typ02);
		if(objTyp)	return $.i18n(objTyp.val01);
		return "";
	});

	Handlebars.registerHelper('reqTypDomain', function(cfgVal01) {
		if(!cfgVal01)	return "";
		let objTyp = App.data.cfgValListTypeDomainPartner.find(item => item.id == cfgVal01);
		if(objTyp)	return $.i18n(objTyp.val01);
		return "";
	});

	Handlebars.registerHelper('reqStatPartner', function(stat) {
		if(!stat)				return $.i18n(STAT_PARTNER[100]);
		if(!STAT_PARTNER[stat])	return $.i18n(STAT_PARTNER[100]);

		return $.i18n(STAT_PARTNER[stat]);
	});

	Handlebars.registerHelper('reqStatBadgePartner', function(stat) {
		if(stat == 11)	return "badge-danger";
		if(stat == 3)	return "badge-success";
		return "badge-info";
	});

	Handlebars.registerHelper('cutStrInfo', function(str) {
		if(!str)	return "";
		if(str.length < 100)	return str;
		return str.substr(0, 100) + "...";
	});

	Handlebars.registerHelper('reqTypUser', function(typ) {
		if(!typ)				return $.i18n(TYP_USER[3]);
		if(!TYP_USER[typ])		return $.i18n(TYP_USER[3]);

		return $.i18n(TYP_USER[typ]);
	});

	Handlebars.registerHelper('reqTypBadgeUser', function(typ) {
		if(typ == 2)	return "badge-danger";
		if(typ == 6)	return "badge-success";
		return "badge-info";
	});

	Handlebars.registerHelper('reqPositionUser', function(pos) {
		if(!pos)	return $.i18n("prj_dashboard_tab_user_info_no_pos");

		return pos.reduce((name, item) => name + " /" + $.i18n("prj_dashboard_tab_user_info_" + item.code.toLowerCase()), "")
	});

	Handlebars.registerHelper('reqNameSocialNetwork', function(code) {
		if(!code)					return "";
		if(!SOCIAL_NETWORK[code])	return "";
		return SOCIAL_NETWORK[code].label;
	});

	Handlebars.registerHelper('reqUrlSocialNetwork', function(code, value) {
		if(!code)						return "";
		if(!SOCIAL_NETWORK_URL[code])	return "";
		
		value = value.toLowerCase();
		if (value.indexOf("http")>=0) return value;
		if (value.indexOf(SOCIAL_NETWORK_URL[code])>=0)   return value;
		
		return SOCIAL_NETWORK_URL[code].label + value;
	});

	Handlebars.registerHelper('reqIconSocialNetwork', function(code) {
		if(!code)					return "";
		if(!SOCIAL_NETWORK[code])	return "";
		return SOCIAL_NETWORK[code].label.toLowerCase();
	});

	Handlebars.registerHelper('reqBgColorSocialNetwork', function(code) {
		if(!code)					return "primary";
		if(!SOCIAL_NETWORK[code])	return "primary";
		return SOCIAL_NETWORK[code].bgColor;
	});

	Handlebars.registerHelper('reqIconFolderMail', function(folder) {
		if(PR_ICON_FOLDER[folder]) return PR_ICON_FOLDER[folder];
		else return PR_ICON_FOLDER["folder"];
	});
	
	Handlebars.registerHelper('reqNameFolderMail', function(name) {
		let folderName = "";
		if(PR_LANGUAGE_FOLDER[name]) folderName = $.i18n(PR_LANGUAGE_FOLDER[name]);
		else folderName = name;
		return folderName;
	});

	Handlebars.registerHelper('reqDestinationMail', function(folder, from, to) {
		if(["Brouillons", "Sent", "Objets envoyés"].includes(folder))	return to;
		return from;
	});

	Handlebars.registerHelper('reqNameEmail', function(name) {
		if(!name)		return "";
		let begin = name.indexOf("<");
		if(begin < 0)	return name;

		return name.slice(0, begin);
	});

	Handlebars.registerHelper('reqEmailEmail', function(name) {
		if(!name)		return "";
		let begin 	= name.indexOf("<");
		if(begin < 0)	return "";
		let end 	= name.indexOf(">");
		return name.slice(begin + 1, end);
	});

	Handlebars.registerHelper('cutStrName', function(str) {
		if(!str)	return "";
		if(str.length < 15)	return str;
		return str.substr(0, 12) + "...";
	});

	Handlebars.registerHelper('reqPathDoc', function(path) {
		if(can_gl_MobileOrTablet()){
			return App.path.LOCATION_URL_HREF + path;
		}

		return path;
	});

	Handlebars.registerHelper('reqCheckStat', function(stat, ...restStats) {
		let options 		= restStats[restStats.length - 1];
		restStats.length 	= restStats.length - 1;

		if(!paramStats.includes(stat)) return options.fn(this)
		
		if(restStats.includes(stat)) return options.fn(this)

		return options.inverse(this)
	});
	
	Handlebars.registerHelper('reqStatClose', function(stat) {
		if(!stat)	return false;
		return stat === pr_STAT_PRJ_CLOSED;
	});
	
	Handlebars.registerHelper('reqClassByStat', function(stat) {
		if(!stat)	return "";
		if(stat === pr_STAT_PRJ_CLOSED)	return "isPrjClose";
		if(stat === pr_STAT_PRJ_FAIL)	return "isPrjNoComplete";
		if(stat === pr_STAT_PRJ_UNRESOLVED)	return "isPrjNoCapable";
	});
	
	Handlebars.registerHelper('reqStatNew', function(stat) {
		if(stat === null)	return false;
		return stat === pr_STAT_PRJ_NEW;
	});
	
	Handlebars.registerHelper("cutStrNumber", function(str, number) {
		if(!str)	return "";
		if(str && str.length > number){
			return str.substr(0, number) + "...";
		}
		return str;
	});
	
	Handlebars.registerHelper("getFileExtensionIcon", function(fileName) {
		if(!fileName)	return "blank";
		const extension = fileName.split('.').pop().toLowerCase();
		if(!pr_EXTENSION_DOC[extension])	return "blank";
		
		return pr_EXTENSION_DOC[extension];
	});
	
	Handlebars.registerHelper('isFavorite', function(parTyp, parId){
		if(!App.data["lstFavorites"])	return false;
		return !!App.data["lstFavorites"][parTyp + '_' + parId];
	});
	
	Handlebars.registerHelper('isShowDelCmt', function(data){
		let usesId = App.data.user.id;
		if(data.autUser != usesId) return true;
		
		let dtAdd = new Date(data.dtAdd);
		let today = new Date();
			
		var diff = Math.abs(today - dtAdd) / 3600000;
		if (diff > 12) return true;
		
		return false;
	});
	
	Handlebars.registerHelper('getLabelName', function(typ02){
		if(typ02 === pr_TYPE02_PRJ)		return $.i18n("prj_project_project_name");
		if(typ02 === pr_TYPE02_EPIC)	return $.i18n("prj_project_epic_name");
		if(typ02 === pr_TYPE02_TASK)	return $.i18n("prj_project_task_name");
	});
	Handlebars.registerHelper('getPlaceHolderName', function(typ02){
		if(typ02 === pr_TYPE02_PRJ)		return $.i18n("prj_project_enter_project_name");
		if(typ02 === pr_TYPE02_EPIC)	return $.i18n("prj_project_enter_epic_name");
		if(typ02 === pr_TYPE02_TASK)	return $.i18n("prj_project_enter_task_name");
	});
	Handlebars.registerHelper('getLabelCode', function(typ02){
		if(typ02 === pr_TYPE02_PRJ)		return $.i18n("prj_project_code");
		if(typ02 === pr_TYPE02_EPIC)	return $.i18n("prj_project_code_epic");
		if(typ02 === pr_TYPE02_TASK)	return $.i18n("prj_project_code_task");
	});
	Handlebars.registerHelper('getPlaceHolderCode', function(typ02){
		if(typ02 === pr_TYPE02_PRJ)		return $.i18n("prj_project_code_enter");
		if(typ02 === pr_TYPE02_EPIC)	return $.i18n("prj_project_code_epic_enter");
		if(typ02 === pr_TYPE02_TASK)	return $.i18n("prj_project_code_task_enter");
	});
	
	Handlebars.registerHelper('getPlaceHolderFileName', function(typ02){
		if(typ02 === pr_TYPE02_PRJ)		return $.i18n("prj_file_enter_file_name");
		if(typ02 === pr_TYPE02_EPIC)	return $.i18n("prj_file_enter_epic_name");
	});
	
	Handlebars.registerHelper('getPlaceHolderFileCode', function(typ02){
		if(typ02 === pr_TYPE02_PRJ)	    return $.i18n("prj_file_code_enter");
		if(typ02 === pr_TYPE02_EPIC)	return $.i18n("prj_file_code_epic_enter");
	});
	
	const pr_TYPE_GRP_CHAT_OWNER 	= 10;
	const pr_TYPE_GRP_CHAT_ADMIN 	= 0;
	const pr_TYPE_GRP_CHAT_MEMBER 	= 2;
	Handlebars.registerHelper('hasRightTransformChat', function(list){
		if(!list)	return false;
		const me = list.find(item => item.uId === App.data.user.id)
		if(!me)		return false;
		
		if([pr_TYPE_GRP_CHAT_OWNER, pr_TYPE_GRP_CHAT_ADMIN].includes(me.typ))	return true;
		
		return false;
	});
	
	/*
	Handlebars.registerHelper('reqTabPrjNotify', function(content) {
		if(!content)	return "";
		let data 		= JSON.parse(content);
		if(data && data.title && data.typTab)	return data.typTab;

		return "";
	});*/
	
	Handlebars.registerHelper('reqParseJson', function(str) {
		return JSON.parse(str);
	});
	
	const actionHistory = {
			1 : "prj_history_action_add", 2 : "prj_history_action_mod", 3 : "prj_history_action_del", 4 : "prj_history_action_join", 6 : "prj_history_action_out", 7 : "prj_history_action_comment", 9 : "prj_history_action_move"
	}
	
	const contentHistory = {
			"content" : "prj_history_content_content", "member" : "prj_history_content_member", "prj" : "prj_history_content_prj", "epic" : "prj_history_content_epic", "task" : "prj_history_content_task", "comment" : "prj_history_content_comment", "customer" : "prj_history_content_cus"
	}

	const levHistory = {
		1 : "prj_project_lev_01", 2 : "prj_project_lev_02", 3 : "prj_project_lev_03", 4 : "prj_project_lev_04"
	}
	
	Handlebars.registerHelper('reqActionHistory', function(typAction) {
		return $.i18n(actionHistory[typAction]);
	});

	Handlebars.registerHelper('reqLevHistory', function(lev) {
		if (lev == null || lev == undefined) return "";
		return $.i18n(levHistory[lev]);
	});
	
	Handlebars.registerHelper('reqContentHistory', function(content) {
		return $.i18n(contentHistory[content]);
	});

	// Handlebar from 1001qua
	Handlebars.registerHelper("reqAvatarOfferDeal", function(files) {
		if(!files || !files.length)	return UI_URL_ROOT+ '/img/prj/noImg.jpg';
		let avatar = files.find(o => o.typ01 === 2 && o.typ02 === 1);
		
		if(avatar)	return decodeURIComponent(avatar.urlPrev);
		
		return decodeURIComponent(files[0].path01);
	});

	Handlebars.registerHelper("url_image_err", function(path) {
		// Helper to put planes tails icons for each company
		var errPath = UI_URL_ROOT+ '/img/prj/noImg.jpg'
		return 'this.src = "'+errPath+ '"';
	});

	Handlebars.registerHelper("getNameLogin", function(user) {
		if(!user.login01)	return "";
		return ["GG_", "FB_"].includes(user.login01.substr(0, 3)) ? user.v1 : user.login01;
	});

	Handlebars.registerHelper("decodeFile", function(file) {
		if(!file)	return UI_URL_ROOT+ '/img/prj/noImg.jpg';
		
		return decodeURIComponent(file);
	});

	Handlebars.registerHelper("reqAvatarUserWithPath", function(path) {
		if(!path)	return "../../www/images/default_user.png";
		return path;
	});

	Handlebars.registerHelper("getNameUserLogin", function(name, val01) {
		if(!name)	return "";
		if(name === "visitor")	return $.i18n("deal_list_name_visitor") + " - ";
		
		return ["GG_", "FB_"].includes(name.substr(0, 3)) ? val01 : name + " - ";
	});
	////////////////
	Handlebars.registerHelper('ifCond', function (v1, operator, v2, options) {
		switch (operator) {
		case '==':
			return (v1 == v2) ? options.fn(this) : options.inverse(this);
		case '===':
			return (v1 === v2) ? options.fn(this) : options.inverse(this);
		case '<':
			return (v1 < v2) ? options.fn(this) : options.inverse(this);
		case '<=':
			return (v1 <= v2) ? options.fn(this) : options.inverse(this);
		case '>':
			return (v1 > v2) ? options.fn(this) : options.inverse(this);
		case '>=':
			return (v1 >= v2) ? options.fn(this) : options.inverse(this);
		case '&&':
			return (v1 && v2) ? options.fn(this) : options.inverse(this);
		case '&&!':
			return (v1 && !v2) ? options.fn(this) : options.inverse(this);
		case '||':
			return (v1 || v2) ? options.fn(this) : options.inverse(this); 
		case '||!':
			return (v1 || !v2) ? options.fn(this) : options.inverse(this); 
		default:
			return options.inverse(this);
		}
	});
	
	Handlebars.registerHelper("reqNameFolderEmail", function(folder) {
		if(!folder)	return false;
		if(folder !== "Objets envoyés")	return false;
		
		return true;
	});
	
	Handlebars.registerHelper("reqTypeEmailSecure", function(mailContent) {
		if(!mailContent.subj)	return false;
		let subj = mailContent.subj;
		
		let firstIdx = subj.lastIndexOf("[");
		let lastIdx  = subj.lastIndexOf("]");
		if(firstIdx < 0 ||  lastIdx < 0 || firstIdx >= lastIdx) return false;
		
		let code = subj.substring(firstIdx + 1, lastIdx);
		
		if(code == null || code.length <= 12) return false;
		
		return true;
	});
	
	const PRJ_STAT_EMAIL_SECU	= {1: "prj_msgbox_secu_stat_send"	, 2: "prj_msgbox_secu_stat_del_ping"};
	Handlebars.registerHelper("reqStatEmailSecu", function(stat) {
		if(stat === undefined)	return "";
		return $.i18n(PRJ_STAT_EMAIL_SECU[+stat]);
	});
	
	Handlebars.registerHelper("isStatSendEmailSecu", function(stat) {
		if(stat === undefined || stat === 2)	return false;
		return true;
	});
	
	const fImage = ['.jpg', '.jpeg', '.png', 'PNG']
	Handlebars.registerHelper("isImage", function(path) {
		if(!path)	return false;
		for(let i=0; i< fImage.length; i++){
			if(path.includes(fImage[i])) return true;
		}
		return false;
	});
	
	const TYP_KINESIS_DEACTIVE	= 0;
	const TYP_KINESIS_ACTIVE	= 1;
	Handlebars.registerHelper("isCallVideoKinesis", function(group) {
		if(!group)	return false;
		if(!group.val02) return false;
		let val02 = JSON.parse(group.val02);
		if(val02.typ == TYP_KINESIS_ACTIVE) return true;
		return false;
	});
	
	Handlebars.registerHelper("reqHSStr", function(date) {
		if(!date) return false;
		return req_gl_DateStr_From_DateStr(date, "", "HH:mm");
	});
	
	Handlebars.registerHelper("reqDateStr", function(date) {
		if(!date) return false;
		
		var local = localStorage.language;
		if (!local) local = "en";
		var format = DateFormat.masks.enFullDateToMM;
		
		if (local=="fr")
			format = DateFormat.masks.frFullDateToMM;
		else if (local=="vn")
			format = DateFormat.masks.viFullDateToMM;
		else if (local=="vi")
			format = DateFormat.masks.viFullDateToMM;
		
		
		return req_gl_DateStr_From_DateStr(date, "", format);
	});
	
	const PRJ_STAT_GROUP_EMAIL_CAMPAGNE = {1: "prj_email_campagne_status_new"	, 2: "prj_email_campagne_status_validate"	, 5: "prj_email_campagne_status_delete"};
	Handlebars.registerHelper("reqStatGroupEmailCampagne", function(stat) {
		if(stat === undefined)	return "";
		return $.i18n(PRJ_STAT_GROUP_EMAIL_CAMPAGNE[+stat]);
	});
	
	const PRJ_STAT_GROUP_EMAIL = {1: "nso_group_email_header_status_not_validated"	, 2: "nso_group_email_header_status_validated"	, 3: "nso_group_email_header_status_wait_delete"};
	Handlebars.registerHelper("reqStatGroupEmail", function(stat) {
		if(stat === undefined)	return "";
		return $.i18n(PRJ_STAT_GROUP_EMAIL[+stat]);
	});
	
	Handlebars.registerHelper("reqNameStringFile", function(name) {
		if(!name) return "";
		name = decodeURIComponent(name)
		let index = name.indexOf(".");
		if(index < 20) return name;
		return name.substr(0, 20) + name.substr(index);
	});

	Handlebars.registerHelper("reqConvertTimeZone", function(date) {
		if(!date)	return "";

		date = new Date(date);
		let sysUTC  = App.data.utc;
		let userUTC = 0;
		if (App.data.user && App.data.user.utcZone)
			userUTC = App.data.user.utcZone;

		let int_sysUTC = Math.trunc(sysUTC);
		let float_sysUTC = Number((sysUTC - int_sysUTC).toFixed(2));
		if (Math.abs(float_sysUTC) == 0.5)  float_sysUTC = 30;
		if (Math.abs(float_sysUTC) == 0.75) float_sysUTC = 45;

		let int_userUTC = Math.trunc(userUTC);
		let float_userUTC = Number((userUTC - int_userUTC).toFixed(2));
		if (Math.abs(float_userUTC) == 0.5)  float_userUTC = 30;
		if (Math.abs(float_userUTC) == 0.75) float_userUTC = 45;

		date.setHours(date.getHours() - int_sysUTC + int_userUTC);
		date.setMinutes(date.getMinutes() - float_sysUTC + float_userUTC);

		let local = localStorage.language ? localStorage.language : "en";
		return DateFormat(date, formatFullDateToMM[local]);
	});

	Handlebars.registerHelper("canBeGG", function(name, options) {
		let check = false;
		if (name.includes("GG_") || name.includes("FB_")) check = true;

		if (check) {
			return options.inverse(this);
		} else {
			return options.fn(this);
		}
	});
	
	Handlebars.registerHelper('innerText', function(str, len) {
		if (!str) str = "";
		var doc 	= new DOMParser().parseFromString(str, "text/html");
		var isHtml 	= Array.from(doc.body.childNodes).some(node => node.nodeType === 1);
		var strEnd	= "";
		if (!isHtml) 
			strEnd =str;
		else
			strEnd = doc.innerText;
		
		if (!strEnd) strEnd = "";
		if (len>0) strEnd = strEnd.substring (0, len) + "...";
		return strEnd;
	});

});