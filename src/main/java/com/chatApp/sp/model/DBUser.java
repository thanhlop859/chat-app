package com.chatApp.sp.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class DBUser {
	
	@Id
	private String id;
	
	@Indexed(unique = true)
	private String email;
	private String password;
	private String age;
	private Gender gender;
	private String userName;
	private String role;
	private Map<String, String> friend;
	private Map<String, String> group;
	private Map<String, String> friendRequest;
	private Map<String, String> receivedFriendRequest;
	

	public DBUser() {
		
	}
	
	public DBUser(String email, String password, String age, String gender, String userName) {
		this.age = age;
		this.email = email;
		this.password = password;
		System.out.println("--------gender-----");
		System.out.println(gender);
		switch (gender) {
		case "0":
			this.gender = Gender.HIDDEN;
			break;
		case "1":
			this.gender = Gender.MALE;
			break;
		default:
			this.gender = Gender.FEMALE;
			break;
		}
		
		this.userName = userName;
		this.role ="ROLE_USER";
		this.friend = new HashMap<String, String>();
		this.group = new HashMap<String, String>();
		this.friendRequest = new HashMap<String, String>();
		this.receivedFriendRequest = new HashMap<String, String>();
	}
	
	public void setUser(DBUser user) {
		this.gender = user.getGender();
		this.age = user.getAge();
		this.password = user.getPassword();
		this.userName = user.getUserName();
	}
		
	public Map<String, String> getFriendRequest() {
		return friendRequest;
	}

	public void setFriendRequest(Map<String, String> friendRequest) {
		this.friendRequest = friendRequest;
	}

	public Map<String, String> getReceivedFriendRequest() {
		return receivedFriendRequest;
	}

	public void setReceivedFriendRequest(Map<String, String> receivedFriendRequest) {
		this.receivedFriendRequest = receivedFriendRequest;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public Map<String, String> getFriend() {
		return friend;
	}

	public void setFriend(Map<String, String> friend) {
		this.friend = friend;
	}

	public Map<String, String> getGroup() {
		return group;
	}

	public void setGroup(Map<String, String> group) {
		this.group = group;
	}

}
