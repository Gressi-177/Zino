define([
	'jquery',
	'text!group/cfg/value/tmpl/Ent_Btn.html',
	'text!group/cfg/value/tmpl/Ent_Btn_Sub.html',
	],

	function($, 
			Tmpl_Ent_Btn,
			Tmpl_Ent_Btn_Sub

	) {


	var CtrlEntBtn     = function (header,content,footer,grpName) {
		var pr_divHeader 			= header;
		var pr_divContent 			= content
		var pr_divFooter 			= footer;

		//------------------------------------------------------------------------------------
		var pr_grpName				= grpName?grpName:((new Date()).getTime()+"");
		//------------------------------------------------------------------------------------
		var tmplName				= App.template.names[pr_grpName];
		var tmplCtrl				= App.template.controller;

		var svClass 				= App['const'].SV_CLASS;
		var svName					= App['const'].SV_NAME;
		var sessId					= App['const'].SESS_ID;
		var userId          		= App['const'].USER_ID;

		var fVar					= App['const'].FUNCT_SCOPE;
		var fName					= App['const'].FUNCT_NAME;
		var fParam					= App['const'].FUNCT_PARAM;		

		var self 					= this;
		//------------------------------------------------------------------------------------

		//------------------controllers------------------------------------------------------
		var pr_ctr_Main 			= null;
		var pr_ctr_List 			= null;
		var pr_ctr_Ent				= null;
		var pr_ctr_EntHeader 		= null;
		var pr_ctr_EntBtn 			= null;
		var pr_ctr_EntTabs 			= null;


		//-----------------------------------------------------------------------------------
		var pr_obj					= null;
		var pr_mode					= null;

		//-----------------------------------------------------------------------------------
		var pr_btn_Create 			= "#btn_create";
		var pr_btn_Edit 			= "#btn_edit";
		var pr_btn_Duplicate 		= "#btn_duplicate";
		var pr_btn_Del 				= "#btn_del";
		
		var pr_btn_Save 			= "#btn_save";
		var pr_btn_Save_Sub			= "#btn_save_sub";
		var pr_btn_Cancel 			= "#btn_cancel";
		var pr_btn_Cancel_Sub		= "#btn_cancel_sub";
		
		var pr_btn_Export 			= "#btn_export";
		var pr_btn_Send 			= "#btn_send";
		var pr_btn_Print 			= "#btn_print";
		var pr_btn_Transform 		= "#btn_transform";
		var pr_btn_Upload 			= "#btn_upload";
		var pr_btn_Preview 			= "#btn_preview";
		var pr_btn_See 				= "#btn_see";
		
	

		//--------------------APIs--------------------------------------//
		this.do_lc_init		= function(){
			tmplName.ENT_BTN					= "EntBtn";
			tmplName.ENT_BTN_SUB				= "EntBtn_Sub";
			tmplCtrl.do_lc_put_tmpl(tmplName.ENT_BTN		, Tmpl_Ent_Btn); 
			tmplCtrl.do_lc_put_tmpl(tmplName.ENT_BTN_SUB	, Tmpl_Ent_Btn_Sub); 
			//----------------------------------------------------------------------
			pr_ctr_Main 			= App.controller[pr_grpName].Main;
			pr_ctr_List 			= App.controller[pr_grpName].List;
			pr_ctr_Ent				= App.controller[pr_grpName].Ent;
		}


		this.do_lc_show		= function(obj, mode){
			pr_obj 			= obj;
			pr_mode			= mode;
			try{
				$("#div_Ent_Btn")		.html(tmplCtrl.req_lc_compile_tmpl(tmplName.ENT_BTN		, pr_obj));
				$("#div_Ent_Btn_Sub")	.html(tmplCtrl.req_lc_compile_tmpl(tmplName.ENT_BTN_SUB	, pr_obj));
				$("#div_Ent_Btn_Sub")	.hide();

				if(mode==App['const'].MODE_INIT	){ // no material selected				
					$(pr_btn_Create		)	.show();
					$(pr_btn_Edit		)	.hide();
					$(pr_btn_Duplicate	)	.hide();
					$(pr_btn_Del		)	.hide();

					$(pr_btn_Save		)	.hide();
					$(pr_btn_Cancel		)	.hide();
					$(pr_btn_Save_Sub	)	.hide();
					$(pr_btn_Cancel_Sub	)	.hide();
					
					$(pr_btn_Export		)	.hide();
					$(pr_btn_Send		)	.hide();
					$(pr_btn_Print		)	.hide();
					$(pr_btn_Transform	)	.hide();	
					$(pr_btn_Send		)	.hide();	
					$(pr_btn_Upload		)	.hide();	
					$(pr_btn_Preview	)	.hide();	
					$(pr_btn_See		)	.hide();	

				}else if(mode==App['const'].MODE_SEL){ // a object selected				
					$(pr_btn_Create		)	.show();
					$(pr_btn_Edit		)	.show();
					$(pr_btn_Duplicate	)	.show();
					$(pr_btn_Del		)	.show();

					$(pr_btn_Save		)	.hide();
					$(pr_btn_Cancel		)	.hide();
					$(pr_btn_Save_Sub	)	.hide();
					$(pr_btn_Cancel_Sub	)	.hide();
					
					$(pr_btn_Export		)	.hide();
					$(pr_btn_Send		)	.hide();
					$(pr_btn_Print		)	.hide();
					$(pr_btn_Transform	)	.hide();	
					$(pr_btn_Send		)	.hide();	
					$(pr_btn_Upload		)	.hide();	
					$(pr_btn_Preview	)	.hide();	
					$(pr_btn_See		)	.hide();

				}else if(mode==App['const'].MODE_NEW){ // in creation or duplication		
					$(pr_btn_Create		)	.hide();
					$(pr_btn_Edit		)	.hide();
					$(pr_btn_Duplicate	)	.hide();
					$(pr_btn_Del		)	.hide();

					$(pr_btn_Save		)	.show();
					$(pr_btn_Cancel		)	.show();
					$(pr_btn_Save_Sub	)	.show();
					$(pr_btn_Cancel_Sub	)	.show();
					
					$(pr_btn_Export		)	.hide();
					$(pr_btn_Send		)	.hide();
					$(pr_btn_Print		)	.hide();
					$(pr_btn_Transform	)	.hide();	
					$(pr_btn_Send		)	.hide();	
					$(pr_btn_Upload		)	.hide();	
					$(pr_btn_Preview	)	.hide();	
					$(pr_btn_See		)	.hide();
					
					$("#div_Ent_Btn_Sub")	.show();
					
				}else if(mode==App['const'].MODE_MOD){ // in modification		
					$(pr_btn_Create		)	.hide();
					$(pr_btn_Edit		)	.hide();
					$(pr_btn_Duplicate	)	.hide();
					$(pr_btn_Del		)	.hide();

					$(pr_btn_Save		)	.show();
					$(pr_btn_Cancel		)	.show();
					$(pr_btn_Save_Sub	)	.show();
					$(pr_btn_Cancel_Sub	)	.show();
					
					$(pr_btn_Export		)	.hide();
					$(pr_btn_Send		)	.hide();
					$(pr_btn_Print		)	.hide();
					$(pr_btn_Transform	)	.hide();	
					$(pr_btn_Send		)	.hide();	
					$(pr_btn_Upload		)	.hide();	
					$(pr_btn_Preview	)	.hide();	
					$(pr_btn_See		)	.hide();
					
					$("#div_Ent_Btn_Sub")	.show();
				
				}else if(mode==App['const'].MODE_TRANSL){
					$(pr_btn_Save		)	.show();
					$(pr_btn_See		)   .show();
					$(pr_divContent+"_Sub")	.show();
				}

				do_bind_event_btn_create	(pr_obj, pr_mode);
				do_bind_event_btn_edit		(pr_obj, pr_mode);
				do_bind_event_btn_duplicate	(pr_obj, pr_mode);
				do_bind_event_btn_del		(pr_obj, pr_mode);

				do_bind_event_btn_save		(pr_obj, pr_mode);
				do_bind_event_btn_cancel	(pr_obj, pr_mode);
				
				do_bind_event_btn_export	(pr_obj, pr_mode);
				do_bind_event_btn_send		(pr_obj, pr_mode);
				do_bind_event_btn_print		(pr_obj, pr_mode);
				do_bind_event_btn_transform	(pr_obj, pr_mode);
				do_bind_event_btn_preview	(pr_obj, pr_mode);
				do_bind_event_btn_see	    (pr_obj, pr_mode);
				do_bind_event_btn_upload	(pr_obj, pr_mode);

			}catch(e) {	
				console.log(e);
				//do_gl_exception_send(App.path.BASE_URL_API_PRIV,  "cfg.", "EntBtn", "do_lc_show", e.toString()) ;
			}
		};


		//---------private-----------------------------------------------------------------------------
		//---------private-----------------------------------------------------------------------------

		var do_bind_event_btn_create		= function(){
			$(pr_btn_Create).off('click');
			$(pr_btn_Create).click(function(){
				pr_ctr_Ent.do_lc_new();
			});
		}	

		var do_bind_event_btn_duplicate	= function(obj){
			$(pr_btn_Duplicate).off('click');
			$(pr_btn_Duplicate).click(function(){
				//clone obj
				pr_ctr_Ent.do_lc_duplicate(obj);
			});
		}

		var do_bind_event_btn_edit			= function(obj){
			$(pr_btn_Edit).off('click');
			$(pr_btn_Edit).click(function(){
				pr_ctr_Ent	.do_lc_Lock_Begin(obj);
			});	
		}

		var do_bind_event_btn_del			= function(obj){
			$(pr_btn_Del).off('click');
			$(pr_btn_Del).click(function(){				
				App.MsgboxController.do_lc_show({
					title	: $.i18n("msgbox_del_title"),
					content : $.i18n("msgbox_del_cont"	),
					width	: window.innerWidth<1024?"95%":"40%",
							buttons	: {
								OK: {
									lab		: $.i18n("common_btn_ok"),
									funct	: pr_ctr_Ent	.do_lc_delete,
									param	: [obj]						
								},
								NO: {
									lab		:  $.i18n("common_btn_cancel"),
								}
							}
				});				
			});
		}

		var do_bind_event_btn_save			= function(obj, mode){
			$(pr_btn_Save).off('click');
			$(pr_btn_Save).click(function(){				
				pr_ctr_Ent.do_lc_save(obj, mode);				
			});

			$(pr_btn_Save_Sub).off('click');
			$(pr_btn_Save_Sub).click(function(){				
				pr_ctr_Ent.do_lc_save(obj, mode);				
			});
		}
		//----------------------------------------------------------------------------------------------
		var do_bind_event_btn_cancel		= function(obj, mode){
			var functCancel = function(){
				App.MsgboxController.do_lc_show({
					title	: $.i18n("msgbox_cancel_title"),
					content : $.i18n("msgbox_cancel_cont"),
					width	: window.innerWidth<1024?"95%":"40%",
							buttons	: {
								OK: {
									lab		: $.i18n("common_btn_ok"),
									funct	: doCancel,
									param	: [obj, mode]							
								},
								NO: {
									lab		:  $.i18n("common_btn_cancel")								
								}
							}
				});	
			}

			$(pr_btn_Cancel).off('click');
			$(pr_btn_Cancel).click(functCancel);

			$(pr_btn_Cancel_Sub).off('click');
			$(pr_btn_Cancel_Sub).click(functCancel);
		}

		var doCancel = function (obj, mode){		
			if(mode == App['const'].MODE_NEW) {	
				pr_ctr_Ent	.do_lc_show			(null);
			} else if(mode == App['const'].MODE_MOD) {				
				pr_ctr_Ent	.do_lc_Lock_Cancel	(obj);			
			} else if(mode == App['const'].MODE_TRANSL) {				
				pr_ctr_Ent	.do_lc_show			(null, App['const'].MODE_SEL);	
			} 	 			
		}

		//----------------------------------------------------------------------------------------------
		//----------------------------------------------------------------------------------------------
		var do_bind_event_btn_export		= function(obj, mode){
			$(pr_btn_Export).off('click');
			$(pr_btn_Export).click(function(){
			});
		}

		var do_bind_event_btn_send		= function(obj, mode){
			$(pr_btn_Send).off('click');
			$(pr_btn_Send).click(function(){
			});
		}

		var do_bind_event_btn_print		= function(obj, mode){
			$(pr_btn_Print).off('click');
			$(pr_btn_Print).click(function(){
			});
		}

		var do_bind_event_btn_transform		= function(obj, mode){
			$(pr_btn_Transform	).off('click');
			$(pr_btn_Transform	).click(function(){
			});
		}
	
		var do_bind_event_btn_preview		= function(obj){
			$(pr_btn_Preview).off('click');
			$(pr_btn_Preview).click(function(){				
			});
		}
		
		var do_bind_event_btn_see		= function(obj){
			$(pr_btn_See).off('click');
			$(pr_btn_See).click(function(){				
			});
		}
		
		var do_bind_event_btn_upload		= function(obj){
			$(pr_btn_Upload).off('click');
			$(pr_btn_Upload).click(function(){				
			});
		}
		
	};

	return CtrlEntBtn;
});