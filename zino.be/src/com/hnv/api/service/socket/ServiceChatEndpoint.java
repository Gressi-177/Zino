package com.hnv.api.service.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.hnv.api.def.DefTime;
import com.hnv.api.service.main.ServiceAPILoginCheck;
import com.hnv.common.tool.ToolJSON;
import com.hnv.common.tool.ToolSet;
import com.hnv.data.json.JSONArray;
import com.hnv.data.json.JSONObject;
import com.hnv.db.aut.TaAutUser;
import com.hnv.db.msg.TaMsgMessage;
import com.hnv.db.nso.TaNsoGroup;
import com.hnv.db.nso.TaNsoGroupMember;
import com.hnv.def.DefAPIExt;
import com.hnv.process.ThreadManager;


//@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
@Component
@ServerEndpoint(value = DefAPIExt.URL_API_CHAT + "/{username}/{tok}")///{tok}
public class ServiceChatEndpoint {

	static Set<Session> users = Collections.synchronizedSet(new HashSet<>());

	static Hashtable<Integer, List<Session>> 		tabUserSessByUserId 	= new Hashtable<Integer,  List<Session>>();
	static Hashtable<Integer, List<Session>> 		tabUserSessByManId 		= new Hashtable<Integer,  List<Session>>();
	static Hashtable<Integer, List<Session>> 		tabUserSessByGrpId 		= new Hashtable<Integer,  List<Session>>();
	
	static Hashtable<Integer, Set<Integer>> 		tabUserIdByGrpId 		= new Hashtable<Integer,  Set<Integer>>();

	static Thread checkSession = new Thread() {
		public void run() {
			try {
				List<Session>			  lstDel  = new ArrayList<Session>();
				Collection<List<Session>> lstSess = tabUserSessByUserId.values();
				for (List<Session> lst : lstSess)
					if (lst!=null) {
						Iterator<Session> it = lst.iterator();
						while (it.hasNext()) {
							Session s = it.next();
							if (!s.isOpen()) {
								s.close();
								it.remove();
								lstDel.add(s);
							}
						}
					}
				
				users.removeAll(lstDel);
				lstSess = tabUserSessByManId.values();
				for (List<Session> lst : lstSess) {
					lst.removeAll(lstDel);
				}
				lstSess = tabUserSessByGrpId.values();
				for (List<Session> lst : lstSess) {
					lst.removeAll(lstDel);
				}
			}catch(Exception e) {

			}
		}
	};
	
	static Thread checkGroup = new Thread() {
		public void run() {
			try {
				Collection<Integer> grpIds  = tabUserIdByGrpId.keySet();
				for (Integer id : grpIds){
					reqChatGrpMemberRefresh (id);
				}
			}catch(Exception e) {

			}
		}
	};
	static {
		ThreadManager.doExecuteInfini(checkSession	, DefTime.TIME_01_00_00_000);
		ThreadManager.doExecuteInfini(checkGroup	, DefTime.TIME_00_10_00_000);
	}
	
	
	public static List<Session> reqLstSessionByUserId (int uId){
		return tabUserSessByUserId.get(uId);
	}
	
	
	public static boolean canHaveMember (int grpId, int uId) {
		 Set<Integer> ids = tabUserIdByGrpId.get(grpId);
		 if(ids == null) return false;
		 return ids.contains(uId);
	}
	//--------------------------------------------------------------------------------------------------
	@OnOpen
	public void handleOpen(Session session, @PathParam("username") String username, @PathParam("tok") String tok) throws Exception {//
		TaAutUser user = ServiceAPILoginCheck.reqAutUserWithToken(username, tok);
		if (user==null) throw new Exception("Cannot connect");
		
		session.getUserProperties().put("username"	, username);
		session.getUserProperties().put("user"		, user);
		users.add(session);

		Integer uId = user.reqId();
		List<Session> lstSession = tabUserSessByUserId.get(uId);
		if (lstSession ==null) lstSession = new ArrayList<Session>();
		lstSession.add(session);
		tabUserSessByUserId.put(uId, lstSession);
		
		Integer manId = user.reqPerManagerId();
		lstSession = tabUserSessByManId.get(manId);
		if (lstSession ==null) lstSession = new ArrayList<Session>();
		lstSession.add(session);
		tabUserSessByManId.put(manId, lstSession);
		
		sendListUserOnlineByManId(manId);
	}
	
	
	private static void sendListUserOnlineByManId(Integer manId) throws Exception {		
		List<Session> lstSessions = tabUserSessByManId.get(manId);
		if (lstSessions==null) return;
		
		String lstUserOnline = reqListUserOnlineInMan(lstSessions);
		for (Session session : lstSessions) {
			if (session.isOpen()){
				session.getBasicRemote().sendText(lstUserOnline);
			}
		}
	}
	
	private static String reqListUserOnlineInMan(List<Session> lstSessions) {
		JSONObject obj = new JSONObject();
		obj.put("type", ChatMessage.MSG_CHAT_USER_ONLINE);
		
		JSONArray list = new JSONArray();
		for (Session session : lstSessions) {
			if (session.isOpen()){
				String username = (String) session.getUserProperties().get("username");
				list.add(username);
			}
		}
		
		obj.put("payLoad", list);
		return obj.toJSONString();
	}

	private static void sendListUserOnlineByGrpId(Integer grpId) throws Exception {		
		List<Session> lstSessions = tabUserSessByGrpId.get(grpId);
		if (lstSessions==null) return;
		
		String lstUserOnline = reqListUserOnlineInGrp(lstSessions);
		for (Session session : lstSessions) {
			if (session.isOpen()){
				session.getBasicRemote().sendText(lstUserOnline);
			}
		}
	}
	
	private static  String reqListUserOnlineInGrp(List<Session> lstSessions) {
		JSONObject obj = new JSONObject();
		obj.put("type", ChatMessage.MSG_CHAT_GROUP_ONLINE);
		
		JSONArray list = new JSONArray();
		for (Session session : lstSessions) {
			if (session.isOpen()){
				String username = (String) session.getUserProperties().get("username");
				list.add(username);
			}
		}
		
		obj.put("payLoad", list);
		return obj.toJSONString();
	}
	
	//---------------------------------------------------------------------------------------
	@OnMessage
	public void handleMessage(String message, Session sess) throws Exception {
		ChatMessage msgIn 	= ChatMessage.reqMessage (message);
		TaAutUser 	user 	= (TaAutUser)sess.getUserProperties().get("user");
		
		if (msgIn.reqName().equals(ChatMessage.MSG_CHAT_USER_ONLINE)) {
			
		//-----------------------------------------------------------------------------
		}else if (msgIn.reqName().equals(ChatMessage.MSG_CHAT_GROUP_BEGIN)) {
			doChatGrpBegin(user, sess, msgIn);	
		}else if (msgIn.reqName().equals(ChatMessage.MSG_CHAT_GROUP_END)) {
			doChatGrpEnd(user, sess, msgIn);
		}else if (msgIn.reqName().equals(ChatMessage.MSG_CHAT_GROUP_MEMBER)) {
			doChatGrpMemberRefresh(user, sess, msgIn);	
			
		}else if (msgIn.reqName().equals(ChatMessage.MSG_CHAT_GROUP_ONLINE)) {
			
			
		//-----------------------------------------------------------------------------
		}else if (msgIn.reqName().equals(ChatMessage.MSG_CHAT_START_CALL)) {
			doChatStartCall(user, sess, msgIn);	
		}else if (msgIn.reqName().equals(ChatMessage.MSG_CHAT_FINISH_CALL)) {
			doChatFinishCall(user, sess, msgIn);	
		}else if (msgIn.reqName().equals(ChatMessage.MSG_CHAT_FINISH_VIEWER)) {
			doChatFinishViewer(user, sess, msgIn);	
		}else if (msgIn.reqName().equals(ChatMessage.MSG_CHAT_START_CALL_CHIME)) {
			doChatStartCallChime(user, sess, msgIn);	
		
		
		//-----------------------------------------------------------------------------
		}else if (msgIn.reqName().equals(ChatMessage.MSG_CHAT_RTC_MSG)) {
			doChatRTCMsg(user, sess, msgIn);
		}
	}

	@OnClose
	public void handleClose(Session sess) throws Exception {
		users.remove(sess);
		
		TaAutUser 	user 	= (TaAutUser)	sess.getUserProperties().get("user");
		Integer 	uId 	= user.reqId();
		Integer 	manId 	= user.reqPerManagerId();
		
		List<Session> lstSession = tabUserSessByUserId.get(uId);
		if (lstSession!=null) {
			lstSession.remove(sess);
			if (lstSession.size()==0) {
				//---all session of user closed => send msg offline
				sendListUserOnlineByManId(manId);
			}
		}
		
		
		lstSession = tabUserSessByManId.get(manId);
		if (lstSession!=null) {
			lstSession.remove(sess);
		}
		
		//----remove from actual group-----
		Integer grID =  (Integer) sess.getUserProperties().get("group");
		if (grID!=null) {
			lstSession = tabUserSessByGrpId.get(grID);
			lstSession.remove(sess);
			sendListUserOnlineByGrpId(grID);
		}
	}

	@OnError
	public void handleError(Throwable t) {
		t.printStackTrace();
	}
	
	

	//----------------------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------
	private static void doChatGrpBegin(TaAutUser user, Session sess, ChatMessage msgIn) throws Exception {
		JSONObject msgObj = msgIn.reqVal();
		int grID = (int)(long) msgObj.get("group");
		
		List<Session> lstSession = tabUserSessByGrpId.get(grID);
		if (lstSession ==null) lstSession = new ArrayList<Session>();
		lstSession.add(sess);
		tabUserSessByGrpId.put(grID, lstSession);
		
		reqChatGrpMemberRefresh(grID);
		
		sess.getUserProperties().put("group"	, grID);
		
		sendListUserOnlineByGrpId(grID);
	}
	
	private static void doChatGrpEnd(TaAutUser user, Session sess, ChatMessage msgIn) throws Exception {
		JSONObject msgObj = msgIn.reqVal();
		int grID = (int)(long) msgObj.get("group");
		
		List<Session> lstSession = tabUserSessByGrpId.get(grID);
		if (lstSession ==null) return;
		lstSession.remove(sess);
	}
	
	private static void doChatGrpMemberRefresh(TaAutUser user, Session sess, ChatMessage msgIn) throws Exception {
		JSONObject msgObj = msgIn.reqVal();
		int grID = (int)(long) msgObj.get("group");
		
		Set<Integer>  ids = reqChatGrpMemberRefresh(grID);

		//---remove all session not member
		List<Session> listSession = tabUserSessByGrpId.get(grID);
		Iterator<Session> it = listSession.iterator();
		while(it.hasNext()) {
			Session 	uSess 	= it.next();
			if (!uSess.isOpen()) {
				it.remove();
				continue;
			}
			
			TaAutUser 	u 		= (TaAutUser)uSess.getUserProperties().get("user");
			Integer 	uId 	= u.reqId();
			if (!ids.contains(uId)) it.remove();
		}
	}
	private static Set<Integer> reqChatGrpMemberRefresh(int grID ) throws Exception {
		TaNsoGroup 				gr 			= TaNsoGroup.DAO.reqEntityByRef(grID);
		List<TaNsoGroupMember> 	lstMem 		= TaNsoGroupMember.DAO.reqList(Restrictions.eq(TaNsoGroupMember.ATT_I_NSO_GROUP, grID), Restrictions.eq(TaNsoGroupMember.ATT_I_STATUS, TaNsoGroupMember.STAT_ACTIVE));
		Set<Integer>			ids 		= ToolSet.reqSetInt(lstMem, TaNsoGroupMember.ATT_I_AUT_USER);
		
		Integer typ02 = (Integer)gr.req(TaNsoGroup.ATT_I_TYPE_02);
		if(typ02 == null || typ02 != TaNsoGroup.TYP_02_PUBLIC) {// xiu e sua
			tabUserIdByGrpId.put(grID, ids);
		}
		
		return ids;
	}
	
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------
	public static void doSendMsg (TaMsgMessage msg, TaAutUser userSend) throws Exception {
		Thread t = new Thread() {
			public void run()  {
				try {
					doDistributeMsg (msg, userSend, ChatMessage.MSG_CHAT_MSG_NEW);	
				}catch(Exception e) {
					e.printStackTrace();
				}				
			}
		};
		ThreadManager.doExecute(t);			
	}

//	public static void doDelMsg (TaMsgMessage msg, TaAutUser userSend) throws Exception {
//		Thread t = new Thread() {
//			public void run()  {
//				try {
//					doDistributeMsg (msg, userSend, ChatMessage.MSG_CHAT_MSG_DEL);	
//				}catch(Exception e) {
//					e.printStackTrace();
//				}				
//			}
//		};
//		ThreadManager.doExecute(t);			
//	}

	
	private static void doDistributeMsg(TaMsgMessage msg, TaAutUser userSend, String typeSend) throws Exception{
		Integer msgType 	= msg.reqInt(msg, TaMsgMessage.ATT_I_TYPE_01);
		
		Integer grpId 		= msg.reqInt(msg, TaMsgMessage.ATT_I_ENTITY_ID);
//		Integer entTyp 		= msg.reqInt(msg, TaMsgMessage.ATT_I_ENTITY_TYPE);
		
		Integer uIDSend 	= userSend.reqInt(userSend, TaAutUser.ATT_I_ID);
		String 	uNameSend 	= userSend.reqStr(userSend, TaAutUser.ATT_T_LOGIN_01);
		
		Date 	date 		= msg.reqDate(msg, TaMsgMessage.ATT_D_DATE_01);
		Integer msgId 		= msg.reqInt(msg, TaMsgMessage.ATT_I_ID);
		
		String 	cont		= msg.reqStr(msg, TaMsgMessage.ATT_T_INFO_04);
		String  info		= msg.reqStr(msg, TaMsgMessage.ATT_T_INFO_05);

		Message mes 		= new Message( msgType, grpId, cont,  info,  uIDSend, uNameSend, date, msgId);

	
		Set<Integer> 	ids 	= tabUserIdByGrpId.get(grpId);
		if (ids==null)  ids 	= reqChatGrpMemberRefresh(grpId);
		String 			msgOut 	= ToolJSON.reqJSonString("type", typeSend, "payLoad", mes);
		for (Integer id: ids) {
			List<Session> lstSession = tabUserSessByUserId.get(id);
			doDistributeMsg(lstSession, msgOut);
		}
	}
	
	public static void doDistributeMsg(List<Session> lstSession, String obj){
		if (lstSession!=null) {
			Iterator<Session> it = lstSession.iterator();
			while (it.hasNext()) {
				try {
					Session peer = it.next();
					if (peer.isOpen()) 
						peer.getBasicRemote().sendText(obj);
					else {
						peer.close();
						it.remove();
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------
//	private static final int TYP_CHAT_USER 		= 1;
	private static final int TYP_CHAT_GROUP 	= 2;
	
	private static void doChatStartCall(TaAutUser user, Session sess, ChatMessage msgIn) throws Exception {
		JSONObject msgObj = msgIn.reqVal();
		msgObj.put("userCall"	, user.req(TaAutUser.ATT_T_LOGIN_01));
		
		JSONObject obj 		= new JSONObject();
		obj.put("type"		, "VIDEO_CALL_START");
		obj.put("payLoad"	, msgObj);
		String msg = obj.toJSONString();
		
		int typ = (int)(long) msgObj.get("typ");
//		if(typ == TYP_CHAT_USER) {
//			String userDest = (String) msgObj.get("userDes");
//			doSendMsgToUser(userDest, send);
//		} else 
		if(typ == TYP_CHAT_GROUP) {
			int userReceive = (int)(long) msgObj.get("uReceiveId");
			int grID = (int)(long) msgObj.get("group");
			doSendMsgCall (user, grID, msg, userReceive);
		}
	}
	
	private static void doChatStartCallChime(TaAutUser user, Session sess, ChatMessage msgIn) throws Exception {
		JSONObject msgObj = msgIn.reqVal();
		msgObj.put("userCall"	, user.req(TaAutUser.ATT_T_LOGIN_01));
		
		JSONObject obj 		= new JSONObject();
		obj.put("type"		, "MSG_CHAT_START_CALL_CHIME");
		obj.put("payLoad"	, msgObj);
		String msg = obj.toJSONString();
		
		int typ = (int)(long) msgObj.get("typ");
//		if(typ == TYP_CHAT_USER) {
//			String userDest = (String) msgObj.get("userDes");
//			doSendMsgToUser(userDest, send);
//		} else 
		if(typ == TYP_CHAT_GROUP) {
			Integer userReceive = (int)(long) msgObj.get("uReceiveId");
			int grID = (int)(long) msgObj.get("group");
			doSendMsgCall (user, grID,msg, userReceive);
		}
	}
	
	private static void doSendMsgCall(TaAutUser userSent, Integer grpId, String msg, Integer userReceive) throws Exception {
		Set<Integer> 	ids 	=  tabUserIdByGrpId.get(grpId);
		if (ids==null)  ids 	= reqChatGrpMemberRefresh(grpId);
		if(userReceive != null) {
			List<Session> lstSession = tabUserSessByUserId.get(userReceive);
			doDistributeMsg(lstSession, msg);
			return;
		}
		for (Integer id: ids) {
			if(id == (int)userSent.reqId())	continue;
			List<Session> lstSession = tabUserSessByUserId.get(id);
			doDistributeMsg(lstSession, msg);
		}
	}
	
	public static void doSendMsgToGroup(Integer grpId, String msg) throws IOException {
		List<Session> lstSession = tabUserSessByGrpId.get(grpId);
		doDistributeMsg(lstSession, msg);
	}
	
	public static void doSendMsgToUser(Integer uId, String msg) throws IOException {
		List<Session> lstSession = tabUserSessByUserId.get(uId);
		doDistributeMsg(lstSession, msg);
	}
	
//	private static void doChatFinishCall(TaAutUser user, Session sess, ChatMessage msgIn) throws IOException {
//		JSONObject msgObj = msgIn.reqVal();
//		
//		JSONObject obj 		= new JSONObject();
//		obj.put("type"		, "VIDEO_CALL_FINISH");
//		obj.put("payLoad"	, "");
//		String send = obj.toJSONString();
//		
//		String userDest = (String) msgObj.get("userDes");
//		
//		for (Session session : users) {
//			String username = (String) session.getUserProperties().get("username");
//			if(username.equals(userDest)) {
//				session.getBasicRemote().sendText(send);
//			}
//		}
//	}
	
	private static void doChatFinishCall(TaAutUser user, Session sess, ChatMessage msgIn) throws Exception {
		JSONObject msgObj = msgIn.reqVal();
		msgObj.put("userCall"	, user.req(TaAutUser.ATT_T_LOGIN_01));
		
		JSONObject obj 		= new JSONObject();
		obj.put("type"		, "VIDEO_CALL_FINISH");
		obj.put("payLoad"	, msgObj);
		String send = obj.toJSONString();
		
		String userDest = (String) msgObj.get("userDes");
		
		for (Session session : users) {
			String username = (String) session.getUserProperties().get("username");
			if(username.equals(userDest)) {
				session.getBasicRemote().sendText(send);
			}
		}
		
		int grID = (int)(long) msgObj.get("group");
		doSendMsgCall (user, grID, send, null);
	}
	
	private static void doChatFinishViewer(TaAutUser user, Session sess, ChatMessage msgIn) throws IOException {
		JSONObject msgObj = msgIn.reqVal();
		
		JSONObject obj 		= new JSONObject();
		obj.put("type"		, "VIDEO_FINISH_VIEWER");
		obj.put("payLoad"	, msgObj);
		String send = obj.toJSONString();
		
		String userCall = (String) msgObj.get("userCall");
		
		for (Session session : users) {
			String username = (String) session.getUserProperties().get("username");
			if(username.equals(userCall)) {
				session.getBasicRemote().sendText(send);
			}
		}
	}
	
	//----------------------------------------------------------------------------------
	private static void doChatRTCMsg(TaAutUser user, Session sess, ChatMessage msgIn) throws Exception {
		JSONObject msgObj 	= msgIn.reqVal();
		int uID 			= (int)(long) msgObj.get("uId");
		if (uID != user.reqId()) return;
		
		int grID = (int)(long) msgObj.get("rId");
		if (!canHaveMember(grID, uID)) return;
		
		JSONObject obj 		= new JSONObject();
		obj.put("type"			, ChatMessage.MSG_CHAT_RTC_MSG);		
		msgObj.put("userCall"	, user.req(TaAutUser.ATT_T_LOGIN_01));
		obj.put("payLoad"		, msgObj);
		String msg = obj.toJSONString();
		
		//typ == TYP_CHAT_GROUP		
		Integer userReceive = (int)(long) msgObj.get("uReceiveId");
		doSendMsgCall (user, grID, msg, userReceive);
	}
	
	
}
