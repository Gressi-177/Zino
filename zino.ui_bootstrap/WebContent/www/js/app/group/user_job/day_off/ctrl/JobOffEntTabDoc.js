define([
        'jquery',
        'text!group/user_job/day_off/tmpl/JobOff_Ent_Tab_Doc.html',
     
        ],
        function($,
        		JobOff_Ent_Tab_Doc
        		) {


	var JobOffEntTabDoc     = function (grpName, header, content, footer) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content;
		var pr_divFooter 			= footer;
		
		var tmplName				= App.template.names;
		var tmplCtrl				= App.template.controller;
		
		var pr_ctr_Main 			= null;
		var pr_ctr_List 			= null;
		var pr_ctr_Ent 				= null;
		var pr_stat_draft      		= 0;
		var self					= this;
		//--------------------APIs--------------------------------------//
	
		this.do_lc_init		= function(){
			tmplCtrl.do_lc_put_tmpl(tmplName.JOB_OFF_ENT_TAB_DOC, JobOff_Ent_Tab_Doc);
			pr_ctr_Main 			= App.controller.JobOff.Main;
		}
		
		this.do_lc_show		= function(obj, mode){
			try{
				obj = do_lc_decode_fileName(obj);
				
				$(pr_divContent).html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_OFF_ENT_TAB_DOC, obj));
				
				App.controller.UI.Main.do_lc_bind_event_resize();
				if(obj.stat != pr_stat_draft){
					$("#btn_add_doc").attr('disabled', 'disabled');
				}
				
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
					fileinput	: { maxFiles : 1, param : {typ01: 1, typ02: 1}},//option here
					obj			: prj//file existing here
			}
			do_gl_init_fileDropzone($("#div_document"), option);

			$(".item-file-download").off("click").on("click", function(){
				let {path} = $(this).data();
				path && window.open(path, "_blank");
			})

			$(".item-file-delete").off("click").on("click", function(){
				let id	= $(this).data("id");
				$(this).parents("tr").remove();
			})

			$("#btn_add_doc").off("click").on("click", function(){
				if(mode != pr_ctr_Main.var_lc_MODE_NEW){
					$(".action-item-doc").removeClass("hide");
				}
				$("#div_prj_ent_file_upload").removeClass("hide");
				$(this).addClass("hide");
				$(".item-file-delete").removeClass("hide");
			})

			$("#a_btn_save_doc").off("click").on("click", function(){
				var rightCode = pr_ctr_Rights.req_lc_Right(RIGHT_U_M);
				if(rightCode == -1){
					do_gl_show_Notify_Msg_Error($.i18n("job_off_msg_cant_modify"));
					return;
				}
				
				prj.files 		= prj.files ? [...prj.files].filter(Boolean) : [];
				do_lc_save_files_prj(prj, mode);
			})

			$("#a_btn_cancel_doc").off("click").on("click", function(){
				self.do_lc_show(prj,mode);
			})
		}

		var do_lc_save_files_prj = function(prj,mode){
			let ref 		= req_gl_Request_Content_Send_With_Params("ServiceJobDayoffRequest", "SVJobDayoffRequestSaveFiles", {obj: JSON.stringify(prj)});	

			let fSucces		= [];
			fSucces.push(req_gl_funct(null, do_lc_afterSave_files_prj, [prj,mode]));

			let fError 		= req_gl_funct(App, do_gl_show_Notify_Msg_Error, [$.i18n("common_err_ajax")]);	

			App.network.do_lc_ajax_background(App.path.BASE_URL_API_PRIV, App.data["HttpSecuHeader"], ref, 100000, fSucces, fError) ;
		}

		var do_lc_afterSave_files_prj = function(sharedJson, prj,mode){
			if(can_gl_AjaxSuccess(sharedJson)) {
				prj.files = sharedJson[App['const'].RES_DATA].files;
				do_lc_show_prj_docs(prj,mode);
			} else {   
				do_gl_show_Notify_Msg_Error ($.i18n('common_err_msg_get') );
			}
		}
		var do_lc_show_prj_docs = function(prj,mode){
			prj = do_lc_decode_fileName(prj);
			
			$(pr_divContent)	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.JOB_REPORT_ENT_TAB_DOC, prj));
			if(prj.stat != pr_stat_draft){
				$("#btn_add_doc").attr('disabled', 'disabled');
			}
			do_lc_bind_event_docs_prj(prj,mode);
		}
		
	}
	
	return JobOffEntTabDoc;
});