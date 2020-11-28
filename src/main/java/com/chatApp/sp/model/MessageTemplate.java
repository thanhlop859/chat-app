package com.chatApp.sp.model;

public class MessageTemplate {
	private String type;
	private String sender;
	private String recipient;
	private String messageType;
	private String message;
	
	public MessageTemplate() {
		
	}

	public MessageTemplate(String sender, String recipient, String message, String type, String messageType) {
		this.type = type;
		this.sender = sender;
		this.recipient = recipient;
		this.message = message;
		this.messageType = messageType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
