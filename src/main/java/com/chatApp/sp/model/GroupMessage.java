package com.chatApp.sp.model;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "groupMessage")
public class GroupMessage extends Message {

	
	@Id
	private String id;
	
	@Indexed
	private String messageId;
	
	private String timeStamp;
	
	private String groupid;
	
	private String sender;
	
	private String message;
	
	private Map<String, Boolean> isRemove;
	
	private Type type;
	
	
	public GroupMessage() {
		
	}
	public GroupMessage(String groupId, String sender, String message, Map<String, String> members) {
		this.groupid = groupId;
		this.messageId = "group_"+this.groupid+"_"+timeStamp;
		this.sender =sender;
		this.message = message;
		this.timeStamp = System.currentTimeMillis() + "";
		
		for(Map.Entry<String, String> m: members.entrySet()) {
			this.isRemove.put(m.getKey(), false);
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Map<String, Boolean> getIsRemove() {
		return isRemove;
	}
	public void setIsRemove(Map<String, Boolean> isRemove) {
		this.isRemove = isRemove;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	
	
	
}
