requirejs.config(requirejs_config);
var bib = ['jquery',

//	'group/dashboard/ctrl/RouteController',
	'main/route/CommonRouteController',
];

var bib_all = [...bib,
...bib_hnv_tool,
...bib_prj,
//...bib_others,
//...bib_secu,
...bib_datetime,
];

if (can_gl_MobileOrTablet()) bib_all = [...bib_all, ...bib_cordova];

//Start the main app logic.
requirejs(bib_all, function ($, RouteController) {
	$(document).ready(function () {
		//--------------Begin app here ----------------------------------------------------------------------------------------------	
		do_gl_InitApp(RouteController);
		
		//App.data.url = decodeURIComponent(window.location.search.substring(1));
	});
});
//------------------------------------------------------------------------------


