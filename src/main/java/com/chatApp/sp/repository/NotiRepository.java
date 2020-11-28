package com.chatApp.sp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chatApp.sp.model.Notification;

@Repository
public interface NotiRepository extends MongoRepository<Notification, String > {
	
	public Notification findByMessageId(String messageId);
	
	public List<Notification> findByRecipient(String recipient);

}
