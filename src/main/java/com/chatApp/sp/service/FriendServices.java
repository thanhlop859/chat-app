package com.chatApp.sp.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chatApp.sp.model.DBUser;
import com.chatApp.sp.repository.UserRepository;

@Service
public class FriendServices {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	CookieServices cookieServices;
	
	private Map<String, String> getFriendRequestList(DBUser user){
		Map<String, String> friendRequest = user.getFriendRequest();
		if(friendRequest == null)
			return new HashMap<String, String>();
		return friendRequest;
	}
	
	private Map<String, String> getReceivedFriendRequest(DBUser user){
		Map<String, String> receivedFriendRequest = user.getReceivedFriendRequest();
		if(receivedFriendRequest == null)
			return new HashMap<String, String>();
		return receivedFriendRequest;
	}
	
	private Map<String, String> getFriendsList(DBUser user){
		Map<String, String> map = user.getFriend();
		if(map == null)
			return new HashMap<String, String>();
		return map;
	}
	
	private Map<String, String> putMap(Map<String, String> map, String key, String value){
		map.put(key, value);
		return map;
	}
	
	private Map<String, String> removeMapElement(Map<String, String> map, String key){
		map.remove(key);
		return map;
	}
	
	//xem danh sách bạn
	public Map<String, String> viewFriendlist(HttpServletRequest req){
		String email = cookieServices.getEmail(req);
		
		DBUser user = userRepo.findByEmail(email);
		
		if(user != null)
			return getFriendsList(user);
		else return null;
	}
	
	public Map<String, String> viewFriendlist(String email){
		
		DBUser user = userRepo.findByEmail(email);
		
		if(user != null)
			return getFriendsList(user);
		else return null;
	}
	
	//gửi lời mời kết bạn
	public String sendFriendRequest(String friendEmail, HttpServletRequest req) throws Exception {
		
		String email = cookieServices.getEmail(req);
		
		DBUser user = userRepo.findByEmail(email);
		DBUser friend = userRepo.findByEmail(friendEmail);
		
		if(friend != null) {
			
			Map<String, String> friendRequest = getFriendRequestList(user);
			Map<String, String> receivedFriendRequest = getReceivedFriendRequest(friend);
			
			if(!friendRequest.containsKey(friendEmail)) {
				user.setFriendRequest(putMap(friendRequest, friendEmail, friend.getUserName()));
				friend.setReceivedFriendRequest(putMap(receivedFriendRequest, email, user.getUserName()));
				userRepo.save(user);
				userRepo.save(friend);
				return "SUCCEED";
			}
			return "Already sent friend request";
		}
		else throw new Exception("User does not exist!");
	}
	
public String sendFriendRequest(String friendEmail, String email) throws Exception {
		
		DBUser user = userRepo.findByEmail(email);
		DBUser friend = userRepo.findByEmail(friendEmail);
		
		if(friend != null) {
			
			Map<String, String> friendRequest = getFriendRequestList(user);
			Map<String, String> receivedFriendRequest = getReceivedFriendRequest(friend);
			
			if(!friendRequest.containsKey(friendEmail)) {
				user.setFriendRequest(putMap(friendRequest, friendEmail, friend.getUserName()));
				friend.setReceivedFriendRequest(putMap(receivedFriendRequest, email, user.getUserName()));
				userRepo.save(user);
				userRepo.save(friend);
				return "SUCCEED";
			}
			return "Already sent friend request";
		}
		else throw new Exception("User does not exist!");
	}
	
	
	//chấp nhận lời mời kết bạn
	public String acceptFriendRequest(String friendEmail, HttpServletRequest req) throws Exception {
		String email = cookieServices.getEmail(req);
		
		DBUser user = userRepo.findByEmail(email);
		
		if(userRepo.findByEmail(friendEmail) != null) {
			Map<String, String> receivedFriendRequest = getReceivedFriendRequest(user);
			
			DBUser friend = userRepo.findByEmail(friendEmail);
			
			
			if(receivedFriendRequest.containsKey(friendEmail)) {
				user.setFriend(putMap(getFriendsList(user), friendEmail, friend.getUserName()));
				user.setReceivedFriendRequest(removeMapElement(receivedFriendRequest, friendEmail));
				friend.setFriend(putMap(getFriendsList(friend), email, user.getUserName()));
				friend.setFriendRequest(removeMapElement(friend.getFriendRequest(), email));
				userRepo.save(friend);
				userRepo.save(user);
				return "SUCCEED";
			}
			return "You did not receive friend request from this user";
		}else throw new Exception("User does not exist");
	}
	
	public String acceptFriendRequest(String friendEmail, String email) throws Exception {
		
		DBUser user = userRepo.findByEmail(email);
		
		if(userRepo.findByEmail(friendEmail) != null) {
			Map<String, String> receivedFriendRequest = getReceivedFriendRequest(user);
			
			DBUser friend = userRepo.findByEmail(friendEmail);
			
			
			if(receivedFriendRequest.containsKey(friendEmail)) {
				user.setFriend(putMap(getFriendsList(user), friendEmail, friend.getUserName()));
				user.setReceivedFriendRequest(removeMapElement(receivedFriendRequest, friendEmail));
				friend.setFriend(putMap(getFriendsList(friend), email, user.getUserName()));
				friend.setFriendRequest(removeMapElement(friend.getFriendRequest(), email));
				userRepo.save(friend);
				userRepo.save(user);
				return "SUCCEED";
			}
			return "You did not receive friend request from this user";
		}else throw new Exception("User does not exist");
	}
	
	// xoá bạn
	public String removeFriend(String friendEmail, HttpServletRequest req) throws Exception {
		String email = cookieServices.getEmail(req);
		
		DBUser user = userRepo.findByEmail(email);
		
		Map<String, String> friends = getFriendsList(user);
		
		DBUser friend = userRepo.findByEmail(friendEmail);
		
		if(friends.containsKey(friendEmail)) {
			user.setFriend(removeMapElement(friends, friendEmail));
			friend.setFriend(removeMapElement(friend.getFriend(), email));
			userRepo.save(user);
			userRepo.save(friend);
			return "SUCCEED";
		}	
		else throw new Exception("Something wrong!");
	}
	public String removeFriend(String friendEmail, String email) throws Exception {
		
		DBUser user = userRepo.findByEmail(email);
		
		Map<String, String> friends = getFriendsList(user);
		
		DBUser friend = userRepo.findByEmail(friendEmail);
		
		if(friends.containsKey(friendEmail)) {
			user.setFriend(removeMapElement(friends, friendEmail));
			friend.setFriend(removeMapElement(friend.getFriend(), email));
			userRepo.save(user);
			userRepo.save(friend);
			return "SUCCEED";
		}	
		else throw new Exception("Something wrong!");
	}
	
	
	//Xem danh sách đã gửi lời mời kết bạn
	public Map<String, String> viewFriendRequest(HttpServletRequest req){
		String email = cookieServices.getEmail(req);
		
		DBUser user = userRepo.findByEmail(email);
		
		return user.getFriendRequest();
	}
	public Map<String, String> viewFriendRequest(String email){
		
		DBUser user = userRepo.findByEmail(email);
		
		return user.getFriendRequest();
	}
	
	
	public Map<String, String> viewReceivedFriendRequest(HttpServletRequest req){
		String email = cookieServices.getEmail(req);
		
		DBUser user = userRepo.findByEmail(email);
		
		return getReceivedFriendRequest(user);
	}
	public Map<String, String> viewReceivedFriendRequest(String email){
		
		DBUser user = userRepo.findByEmail(email);
		
		return getReceivedFriendRequest(user);
	}
	
	

}
