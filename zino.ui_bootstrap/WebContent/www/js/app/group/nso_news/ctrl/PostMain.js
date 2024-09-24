define([
        'text!group/nso_news/tmpl/Post_Main.html',
        'group/nso_news/ctrl/PostList',
        ],
        function(
        		Post_Main,
        		PostList
        ) {
	const PostMain = function (header, content, footer) {
		const tmplName	= App.template.names;
		const tmplCtrl	= App.template.controller;
		//--------------------APIs--------------------------------------//
		this.do_lc_init	= function(){		
			if (!App.controller.Post)				
				 App.controller.Post			= {};
			
			if (!App.controller.Post.Main	)	
				App.controller.Post.Main 		= this; //important for other controller can get ref, when new this controller,

			if (!App.controller.Post.List	)  
				App.controller.Post.List		= new PostList (null, null, null);
			//--------------------------------------------------------------------------------------------------
			App.controller.Post.List			.do_lc_init();
			
			tmplName.POST_MAIN					= "Post_Main";	
		}
		
		this.do_lc_show = id => {
			try {
				tmplCtrl.do_lc_put_tmpl(tmplName.POST_MAIN	, Post_Main); 
				$("#div_post_main").html(tmplCtrl.req_lc_compile_tmpl(tmplName.POST_MAIN, {}));
				//show post list
				App.controller.Post.List.do_lc_show(id);
			} catch(e) {
				console.log(e);
			}
		};
		
		this.do_show_Msg = error => console.log(error);
	};

	return PostMain;
  });