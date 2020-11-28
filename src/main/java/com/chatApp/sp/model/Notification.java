package com.chatApp.sp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notification")
public class Notification extends Message{
	
	@Id
	private String id;
	
	@Indexed
	private String messageId;
	private String message;
	private String sender;
	private String recipient;
	private MessageState state;
	private String timeStamp;
	private MessageType notiType;
	private Type type;
	
	public Notification() {
		
	}
	
	public Notification(String sender, String recipient, String message, MessageType notiType) {
		this.messageId = "notification_"+timeStamp;
		
		this.message = message;
		this.sender = sender;
		this.recipient = recipient;
		this.timeStamp = System.currentTimeMillis() + "";
		this.state = MessageState.SENT;
		this.notiType = notiType;
		this.type = Type.Notification;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public MessageState getState() {
		return state;
	}

	public void setState(MessageState state) {
		this.state = state;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public MessageType getNotiType() {
		return notiType;
	}

	public void setNotiType(MessageType notiType) {
		this.notiType = notiType;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	

	
}
