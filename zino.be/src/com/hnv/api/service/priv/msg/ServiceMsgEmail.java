package com.hnv.api.service.priv.msg;

import javax.servlet.http.HttpServletResponse;

import com.hnv.api.def.DefTime;
import com.hnv.api.interf.IService;
import com.hnv.common.tool.ToolEmail;
import com.hnv.common.tool.ToolLogServer;
import com.hnv.data.json.JSONObject;
import com.hnv.db.msg.TaMsgMessage;
import com.hnv.def.DefAPIExt;
import com.hnv.process.ThreadManager;

/**
 * ----- ServiceNsoMSG by H&V
 * ----- Copyright 2018------------
 */
public class ServiceMsgEmail implements IService {

	public static final String SV_MODULE 					= "EC_V3".toLowerCase();
	public static final String SV_CLASS 					= "ServiceMsgEmail".toLowerCase();	

	public ServiceMsgEmail(){
		ToolLogServer.doLogInf("----" + SV_CLASS + " is loaded -----");
	}

	//---------------------------------------------------------------------------------------------------------
	//SERVICE DEFINITION------SERVICE DEFINITION------SERVICE DEFINITION------SERVICE DEFINITION------SERVICE D								
	//---------------------------------------------------------------------------------------------------------

	private static 			String EMAIL_HOST   = DefAPIExt.API_EMAIL_HOST;
	private static 			String EMAIL_PORT   = DefAPIExt.API_EMAIL_PORT;
	private static 			String EMAIL_USER   = DefAPIExt.API_EMAIL_USER;
	private static 			String EMAIL_PASS   = DefAPIExt.API_EMAIL_PASS;

	//---------------------------------------------------------------------------------------------------------
	//NOTIFY NEW MSG RECEIVED------NOTIFY NEW MSG RECEIVED------NOTIFY NEW MSG RECEIVED------NOTIFY NEW O
	//---------------------------------------------------------------------------------------------------------
	public static void doSendEmail(TaMsgMessage msg) throws Exception{
		String E_SRC 		= (String) msg.req(TaMsgMessage.ATT_T_INFO_01);
		String E_DEST 		= (String) msg.req(TaMsgMessage.ATT_T_INFO_02);
		String E_TITLE		= (String) msg.req(TaMsgMessage.ATT_T_INFO_03);
		String E_CONTENT	= (String) msg.req(TaMsgMessage.ATT_T_INFO_04);
		String E_INFO	 	= (String) msg.req(TaMsgMessage.ATT_T_INFO_05);

		String E_CONT = E_CONTENT + " <br/><hr/><br/> Email: " + E_SRC  + " <br/> Tel: " + E_INFO;

		Thread t = new Thread(){
			public void run() {
				try {
					doSendEmail(E_SRC, E_TITLE, E_CONT, E_DEST); 
				}catch(Exception e) {
					System.out.println(e.getMessage());
				}				
			}
		}; ThreadManager.doExecute(t, DefTime.TIME_00_00_01_000);
	}


	//---------------------------------------------------------------------------------------------------------
	//DO SEND EMAIL------DO SEND EMAIL------DO SEND EMAIL------DO SEND EMAIL------DO SEND EMAIL------DO SEND EM
	//---------------------------------------------------------------------------------------------------------

	private static void doSendEmail( String EMAIL_SRC , String EMAIL_TITLE, String EMAIL_CONTENT,  String EMAIL_DESTINATION ) throws Exception{
		if (EMAIL_DESTINATION!=null)
			ToolEmail.canSendEmail(
					EMAIL_HOST, EMAIL_PORT, null, EMAIL_USER, EMAIL_PASS, EMAIL_SRC, 
					EMAIL_TITLE, EMAIL_CONTENT, EMAIL_DESTINATION, 
					null, null, null);
		ToolLogServer.doLogDebug("---NSO MSG : SEND EMAIL SERVICE: Email sent to: "+ EMAIL_DESTINATION);
	}


	@Override
	public void doService(JSONObject json, HttpServletResponse response) throws Exception {
	}


	
}
