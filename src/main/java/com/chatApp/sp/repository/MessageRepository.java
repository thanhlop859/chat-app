package com.chatApp.sp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chatApp.sp.model.ChatMessage;

@Repository
public interface MessageRepository extends MongoRepository<ChatMessage, String> {
	
	public ChatMessage findByMessageId(String messageId);
	
	public List<ChatMessage> findByChatId(String chatId);

}
