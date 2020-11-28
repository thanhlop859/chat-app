package com.chatApp.sp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "message")
public class ChatMessage extends Message{
	
	@Id
	private String id;
	
	@Indexed
	private String chatId;
	private String messageId;
	private String sender;
	private String recipient;
	private String timeStamp;
	private String message;
	private MessageState senderState;
	private MessageState recipientState;
	private Type type;
	
	

	public MessageState getSenderState() {
		return senderState;
	}

	public void setSenderState(MessageState senderState) {
		this.senderState = senderState;
	}

	public MessageState getRecipientState() {
		return recipientState;
	}

	public void setRecipientState(MessageState recipientState) {
		this.recipientState = recipientState;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timestamp) {
		this.timeStamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public ChatMessage() {
		
	}
	
	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public ChatMessage(String sender, String recipient, String message) {
		
		if(sender.compareTo(recipient) > 0)
			this.chatId = sender+recipient;
		else this.chatId = recipient+sender;
		this.timeStamp = System.currentTimeMillis() + "";
		
		this.messageId = "private_"+this.chatId+"_"+this.timeStamp;
		
		this.message = message;
		this.recipient = recipient;
		this.sender = sender;
		this.senderState = MessageState.SENT;
		this.recipientState = MessageState.RECEIVED;
		this.type = Type.PrivateMessage;
	}	
	
	@Override
	public String toString() {
		return "sender: "+sender+" | recipient: "+ recipient+" | message: "+message+" | timeStamp: "+timeStamp+" | chatId: "+chatId+" | senderState: "+senderState+" | recipientState: "+recipientState+"|||||";
	}
}
