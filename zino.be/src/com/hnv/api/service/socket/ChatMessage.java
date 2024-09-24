package com.hnv.api.service.socket;

import com.hnv.common.tool.ToolJSON;
import com.hnv.data.json.JSONObject;

public class ChatMessage {
	public static String MSG_CHAT_GROUP_BEGIN		= "MSG_CHAT_GROUP_BEGIN";
	public static String MSG_CHAT_GROUP_END			= "MSG_CHAT_GROUP_END";
	public static String MSG_CHAT_GROUP_MEMBER		= "MSG_CHAT_GROUP_MEMBER"; //refresh member
	public static String MSG_CHAT_GROUP_ONLINE		= "MSG_CHAT_GROUP_ONLINE";
	
	public static String MSG_CHAT_GROUP_ACCEPT		= "MSG_CHAT_GROUP_ACCEPT";
	public static String MSG_CHAT_GROUP_REFUSE		= "MSG_CHAT_GROUP_REFUSE";
	
	
	public static String MSG_CHAT_USER_ONLINE		= "MSG_CHAT_USER_ONLINE";
	public static String MSG_CHAT_MSG_NEW			= "MSG_CHAT_MSG_NEW";
	public static String MSG_CHAT_MSG_DEL			= "MSG_CHAT_MSG_DEL";
	public static String MSG_CHAT_MSG_HIDE			= "MSG_CHAT_MSG_HIDE";
	
	public static String MSG_CHAT_START_CALL 		= "MSG_CHAT_START_CALL";
	public static String MSG_CHAT_FINISH_CALL 		= "MSG_CHAT_FINISH_CALL";
	public static String MSG_CHAT_FINISH_VIEWER 	= "MSG_CHAT_FINISH_VIEWER";
	public static String MSG_CHAT_START_CALL_CHIME 	= "MSG_CHAT_START_CALL_CHIME";
	public static String MSG_CHAT_USER_READ 		= "MSG_CHAT_USER_READ";
	
	//----------------------------------------------------------------------------------------------
	public static String MSG_CHAT_RTC_MSG 			= "MSG_CHAT_RTC_MSG";
	public static String MSG_CHAT_RTC_VIDEO 		= "MSG_CHAT_RTC_VIDEO";
	
	
	//----------------------------------------------------------------------------------------------
	private String  	name;
	private JSONObject	val;
	
	public ChatMessage(String name) {
		this.name 		= name;
	}
	 
    public ChatMessage(String name, JSONObject	val) {
    	this.name 		= name;
    	this.val 		= val;
    }
    
    public ChatMessage(JSONObject	js) {
    	this.name 		= (String)		js.get("name");
    	this.val 		= (JSONObject)	js.get("val");
    }

    public static ChatMessage reqMessage(String jsonStr) {
    	JSONObject json 	= ToolJSON.reqJSonFromString(jsonStr);
        ChatMessage message = new ChatMessage (json);
        return message;
    }
    
    @Override
    public String toString() {
        return ToolJSON.reqJSonString("name", this.name, "val", this.val);
    }
    
    public JSONObject reqVal() {
    	return this.val;
    }
    
    public void doSetVal(JSONObject val) {
    	this.val = val;
    }
    
    public String reqName() {
    	return this.name;
    }

}