package com.chatApp.sp.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chatApp.sp.model.ChatMessage;
import com.chatApp.sp.model.GroupMessage;
import com.chatApp.sp.model.MessageTemplate;
import com.chatApp.sp.model.Notification;
import com.chatApp.sp.service.DropboxServices;
import com.chatApp.sp.service.MessageServices;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.UploadErrorException;

@Controller
@CrossOrigin
public class WebSocketController {
  private static SimpMessagingTemplate simpMessagingTemplate;
  
  @Autowired
  private MessageServices messageServices;
  
  public static Set<String> activeUser;
  
  public WebSocketController(SimpMessagingTemplate simpMessagingTemplate){ 
    this.simpMessagingTemplate = simpMessagingTemplate; 
    MessageServices.messageTemplate = simpMessagingTemplate;
    activeUser = new HashSet<String>();
  }
  
  
  
  // add an user to connected user list
  @MessageMapping("/register")  
  @SendToUser("/queue/newMember")
  public Set<String> registerUser(String webChatUsername){
	  System.out.println("webSocketController: new connect "+ webChatUsername);
    if(!activeUser.contains(webChatUsername)) {
    	activeUser.add(webChatUsername);
      simpMessagingTemplate.convertAndSend("/topic/newMember", webChatUsername); 
      return activeUser;
    } else {
      return activeUser;
    }
  }
  
  
  // remove an user from connected user list
  @MessageMapping("/unregister") 
  @SendTo("/topic/disconnectedUser")
  public String unregisterUser(String webChatUsername){
	  activeUser.remove(webChatUsername);
    return webChatUsername;
  }

  
  // send message to a specific user 
  @MessageMapping("/message") 
  public void sendMessage(MessageTemplate message){
	  messageServices.sendMessage(message);
	  //simpMessagingTemplate.convertAndSendToUser(message.getRecipient(), "/msg", message);
  }
  
  //xoá tin nhắn
  @DeleteMapping("/messages/delete")
  @ResponseBody
  public void deleteMessage(@RequestHeader("messageId") String messageId, HttpServletRequest req) {
	  messageServices.deleteMessage(messageId, req);
  }
  @DeleteMapping("/app/messages/delete")
  @ResponseBody
  public void appDeleteMessage(@RequestHeader("messageId") String messageId, @RequestHeader("email") String email) {
	  messageServices.deleteMessage(messageId, email);
  }
  
  
  //lấy tin nhắn nhóm
  @GetMapping("/groups/messages")
  @ResponseBody
  public List<GroupMessage> viewGroupMessage(@RequestHeader("groupId") String groupId, @RequestHeader("email") String email, HttpServletRequest req) throws Exception{
	  return messageServices.viewGroupMessages(groupId, req);
  }
  @GetMapping("/app/groups/messages")
  @ResponseBody
  public List<GroupMessage> appViewGroupMessage(@RequestHeader("groupId") String groupId, @RequestHeader("email") String email) throws Exception{
	  return messageServices.viewGroupMessages(groupId, email);
  }
  
  
  //lấy tin nhắn cá nhân
  @GetMapping("/users/messages")
  @ResponseBody
  public List<ChatMessage> viewPrivateMessage(@RequestHeader("chatId") String chatId, @RequestHeader("email") String email, HttpServletRequest req) throws Exception{
	  return messageServices.viewPrivateMessage(chatId, req);
  }
  @GetMapping("/app/users/messages")
  @ResponseBody
  public List<ChatMessage> appViewPrivateMessage(@RequestHeader("chatId") String chatId, @RequestHeader("email") String email) throws Exception{
	  return messageServices.viewPrivateMessage(chatId, email);
  }
  
  
  //lấy thông báo
  @GetMapping("/users/notification")
  @ResponseBody
  public List<Notification> viewNotification(HttpServletRequest req){
	  return messageServices.viewNotification(req);
  }
  @GetMapping("/app/users/notification")
  @ResponseBody
  public List<Notification> appViewNotification(@RequestHeader("email") String email){
	  return messageServices.viewNotification(email);
  }
  
}
