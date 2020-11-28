package com.chatApp.sp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chatApp.sp.model.DBGroup;

@Repository
public interface GroupRepository extends MongoRepository<DBGroup, String>{

	public DBGroup findByGroupId(String groupId);
}
