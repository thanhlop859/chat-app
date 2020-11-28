package com.chatApp.sp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chatApp.sp.model.DBUser;

@Repository
public interface UserRepository extends MongoRepository<DBUser, String>{
	
	public DBUser findByEmail(String email);

}
