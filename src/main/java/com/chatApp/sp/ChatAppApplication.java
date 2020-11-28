package com.chatApp.sp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;

@SpringBootApplication
public class ChatAppApplication {
	
	public static void main(String[] args) throws DbxApiException, DbxException {
		SpringApplication.run(ChatAppApplication.class, args);
	}
}
