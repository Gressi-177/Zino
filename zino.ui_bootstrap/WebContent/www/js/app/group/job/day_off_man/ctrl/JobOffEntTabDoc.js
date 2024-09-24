define([
        'jquery',
        'text!group/job/day_off_man/tmpl/JobOff_Ent_Tab_Doc.html',
     
        ],
        function($,
        		JobOff_Ent_Tab_Doc
        		) {


	var JobOffEntTabDoc     = function (grpName, header, content, footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;
		
		this.do_lc_init		= function(){
			if(!tmplName) {
				App.template.names[pr_grpName] = {}
				tmplName 	= App.template.names[pr_grpName]
			}

			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_OFF_ENT_TAB_DOC, JobOff_Ent_Tab_Doc);
			pr_ctr_Main 			= App.controller.JobOffMan.Main;
		}
		
		this.do_lc_show		= function(obj, mode){
			try{
				obj = do_lc_decode_fileName(obj);
				
				$(pr_divContent).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_OFF_ENT_TAB_DOC, obj));
				
				App.controller.UI.Main.do_lc_bind_event_resize();
				
				do_lc_bind_event_docs_prj(obj,mode);
				
			}catch(e) {
				do_gl_show_Notify_Msg_Error("JobOffEntTabDoc " + e.toString());
				console.log(e); //do_gl_send_exception(App.path.BASE_URL_API_PRIV, pr_ctr_Main.var_lc_URL_Header, App.network, "job.off", "JobOffEntTabDoc", "do_lc_show", e.toString()) ;
			}
		};
		
		var do_lc_decode_fileName = function(prj){
			let files = [];
			if(prj.files) files = prj.files
			
			for(let i=0; i < files.length; i++){
				let name = decodeURIComponent(files[i].name)
				let indice   = name.indexOf("_");
				let fileName = name.slice(indice + 1);
				files[i].fileName = fileName;
			};
			
			prj.files = files;
			
			return prj;
		}
		
		var do_lc_bind_event_docs_prj = function(prj,mode){
//			let	obj 		= {files:[]};
			if(!prj.files)	prj.files = [];
			let option		= {
					fileinput	: { param : {typ01: 2, typ02: 10}},//option here
					obj			: prj//file existing here
			}
			do_gl_init_fileDropzone($("#div_document"), option);

			$(".item-file-download").off("click").on("click", function(){
				let {path} = $(this).data();
				path && window.open(path, "_blank");
			})
		}
		
	}
	
	return JobOffEntTabDoc;
});