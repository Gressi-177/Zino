package com.hnv.api.service.socket;

import java.util.Date;

import com.hnv.def.DefDBExt;

public class Message {
    private String 	from;
    private String 	to;
    private String 	body;
    private String 	files;
    
    private Integer typMsg;
    private Integer entId;
    private Integer entTyp;
  
    private Integer id;
    private Integer uId;
    private Date 	dt;

    private Integer uIDSend;
    private String 	uNameSend;
    private Integer objIDSend;
    private String 	objNameSend;
    
    public Message(String fr, String to, String cont, Integer typMsg, Integer entId, Integer entTyp, Integer autUserId) {
    	this.from 		= fr;
    	this.to 		= to;
    	this.body 	= cont;
    	this.typMsg 	= typMsg;
    	this.entId 		= entId;
    	this.entTyp 	= entTyp;
    	this.uId 	= autUserId;
    }
    
    public Message(String fr, String to, String cont, Integer typMsg, Integer entId, Integer entTyp, Integer autUserId, Date date, Integer uIDSend, String uNameSend, String files, Integer msgId) {
    	this.from 		= fr;
    	this.to 		= to;
    	this.body 	= cont;
    	this.typMsg 	= typMsg;
    	this.entId 		= entId;
    	this.entTyp 	= entTyp;
    	this.uId 	= autUserId;
    	this.dt		= date;
    	this.uIDSend	= uIDSend;
    	this.uNameSend	= uNameSend;
    	this.files		= files;
    	this.id		= msgId;
    }
    
    public Message(Integer typMsg, Integer grpId, String cont, String files,  Integer uId, String uNameSend,  Date date, Integer msgId) {
    	this.body 	= cont;
    	this.files		= files;
    	
    	this.typMsg 	= typMsg;
    	this.entId 		= grpId;
    	this.entTyp 	= DefDBExt.ID_TA_NSO_GROUP;
    	
    	this.uId 	= uId;
    	this.uIDSend 	= uId;
    	this.uNameSend	= uNameSend;
    	
    	this.dt		= date;
    	this.id		= msgId;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }

	public void setObjIDSend(Integer objIDSend) {
		this.objIDSend = objIDSend;
	}

	public void setObjNameSend(String objNameSend) {
		this.objNameSend = objNameSend;
	}
	
}