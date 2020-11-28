package com.chatApp.sp.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chatApp.sp.controller.WebSocketController;
import com.chatApp.sp.model.ChatMessage;
import com.chatApp.sp.model.DBGroup;
import com.chatApp.sp.model.GroupMessage;
import com.chatApp.sp.model.Message;
import com.chatApp.sp.model.MessageState;
import com.chatApp.sp.model.MessageTemplate;
import com.chatApp.sp.model.MessageType;
import com.chatApp.sp.model.Notification;
import com.chatApp.sp.model.Type;
import com.chatApp.sp.repository.GroupMessageRepository;
import com.chatApp.sp.repository.GroupRepository;
import com.chatApp.sp.repository.MessageRepository;
import com.chatApp.sp.repository.NotiRepository;

@Service
public class MessageServices {

	//@Autowired
	public static SimpMessagingTemplate messageTemplate;
	
	@Autowired
	GroupRepository groupRepo;
	
	@Autowired
	GroupMessageRepository groupMesRepo;
	
	@Autowired
	NotiRepository notiRepo;
	
	@Autowired
	MessageRepository mesRepo;
	
	@Autowired
	CookieServices cookieServices;
	
	
	public void sendMessage(MessageTemplate mes) {
		Type type = getType(mes.getType());
		MessageType mesType = getMessageType(mes.getMessageType());
		
		
		if(type.equals(Type.GroupMessage)) {
			DBGroup group = groupRepo.findByGroupId(mes.getRecipient());
			GroupMessage message = new GroupMessage(mes.getRecipient(), mes.getSender(), mes.getMessage(), group.getMembers());
			sendGroupMessage(message);
		}
		if(type.equals(Type.Notification)) {
			Notification noti = new Notification(mes.getSender(), mes.getRecipient(), mes.getMessage(), mesType);
			sendNotification(noti);
		}
		if(type.equals(Type.PrivateMessage)) {
			ChatMessage message = new ChatMessage(mes.getSender(), mes.getRecipient(), mes.getMessage());
			sendPrivateMessage(message);
		}
	}
	
	//
	public void deleteMessage(String messageId, HttpServletRequest req) {
		
		String email = cookieServices.getEmail(req);
		
		String type = messageId.substring(0, messageId.indexOf("_"));
		if(type.equals("group")) {
			GroupMessage groupMes = groupMesRepo.findByMessageId(messageId);
			deleteGroupMessage(groupMes, email);
		}else {
			ChatMessage mes = mesRepo.findByMessageId(messageId);
			deleteMessage(mes, email);
		}
	}
	public void deleteMessage(String messageId, String email) {
		String type = messageId.substring(0, messageId.indexOf("_"));
		if(type.equals("group")) {
			GroupMessage groupMes = groupMesRepo.findByMessageId(messageId);
			deleteGroupMessage(groupMes, email);
		}else {
			ChatMessage mes = mesRepo.findByMessageId(messageId);
			deleteMessage(mes, email);
		}
	}
	
	/*
	 * Need to limit the results
	 */
	public List<GroupMessage> viewGroupMessages(String groupId, HttpServletRequest req) throws Exception{
		
		String email = cookieServices.getEmail(req);
		
		if(groupRepo.findByGroupId(groupId).getMembers().containsKey(email))
			return groupMesRepo.findByGroupid(groupId);
		throw new Exception("You are not a group member!");
	}
	public List<GroupMessage> viewGroupMessages(String groupId, String email) throws Exception{
		
		if(groupRepo.findByGroupId(groupId).getMembers().containsKey(email))
			return groupMesRepo.findByGroupid(groupId);
		throw new Exception("You are not a group member!");
	}
	
	//
	public List<ChatMessage> viewPrivateMessage(String chatId, HttpServletRequest req) throws Exception{
		
		String email = cookieServices.getEmail(req);
		
		if(chatId.contains(email))
			return mesRepo.findByChatId(chatId);
		throw new Exception("Something wrong!");
	}
	public List<ChatMessage> viewPrivateMessage(String chatId, String email) throws Exception{
		
		if(chatId.contains(email))
			return mesRepo.findByChatId(chatId);
		throw new Exception("Something wrong!");
	}
	
	//
	public List<Notification> viewNotification(HttpServletRequest req){
		
		String email = cookieServices.getEmail(req);
		
		return notiRepo.findByRecipient(email);
	}
	public List<Notification> viewNotification(String email){		
		
		return notiRepo.findByRecipient(email);
	}
	
	
	private void sendGroupMessage(GroupMessage mes) {	
		Map<String, Boolean> members = mes.getIsRemove();
		
		groupMesRepo.save(mes);
		
		for(Map.Entry<String, Boolean> mem: members.entrySet()) {
			if(WebSocketController.activeUser.contains(mem.getKey()))
				messageTemplate.convertAndSendToUser(mem.getKey(), "/msg", mes);
		}
	}
	
	private void sendPrivateMessage(ChatMessage mes) {
		mesRepo.save(mes);
		if(WebSocketController.activeUser.contains(mes.getRecipient()))
			messageTemplate.convertAndSendToUser(mes.getRecipient(), "/msg", mes);
	}
	
	private void sendNotification(Notification noti) {
		notiRepo.save(noti);
		if(WebSocketController.activeUser.contains(noti.getRecipient()))
			messageTemplate.convertAndSendToUser(noti.getRecipient(), "/msg", noti);
	}
	
	
	private Type getType(String type) {
		if(Type.GroupMessage.name().equals(type))
			return Type.GroupMessage;
		if(Type.Notification.name().equals(type))
			return Type.Notification;
		return Type.PrivateMessage;
	}
	
	private MessageType getMessageType(String mesType) {
		if(MessageType.AcceptFriendRequest.name().equals(mesType))
			return MessageType.AcceptFriendRequest;
		if(MessageType.FriendRequest.name().equals(mesType))
			return MessageType.FriendRequest;
		if(MessageType.Image.name().equals(mesType))
			return MessageType.Image;
		return MessageType.Text;
	}
	
	private void deleteGroupMessage(GroupMessage groupMes, String email) {
		Map<String, Boolean> isRemove = groupMes.getIsRemove();
		isRemove.replace(email, true);
		groupMes.setIsRemove(isRemove);
		
		groupMesRepo.save(groupMes);
	}
	
	
	private void deleteMessage(ChatMessage mes, String email) {
		if(mes.getSender().equals(email)) 
			mes.setSenderState(MessageState.REMOVED);
		else 
			mes.setRecipientState(MessageState.REMOVED);
		
		mesRepo.save(mes);
		
	}
	
}
