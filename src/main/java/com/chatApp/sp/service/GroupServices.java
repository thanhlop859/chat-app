package com.chatApp.sp.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chatApp.sp.model.DBGroup;
import com.chatApp.sp.model.DBUser;
import com.chatApp.sp.repository.GroupMessageRepository;
import com.chatApp.sp.repository.GroupRepository;
import com.chatApp.sp.repository.UserRepository;


@Service
public class GroupServices {
	
	@Autowired
	GroupMessageRepository groupMessRepo;
	
	@Autowired
	GroupRepository groupRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	CookieServices cookieServices;
	
	private Map<String, String> getUserGroups(DBUser user){
		Map<String, String> userGroups = user.getGroup();
		if(userGroups == null)
			return new HashMap<String, String>();
		return userGroups;
	}
	
	private void addUserGroup(String email, String groupId, String groupName) {
		DBUser user = userRepo.findByEmail(email);
		Map<String, String> userGroups = getUserGroups(user);
		userGroups.put(groupId, groupName);
		user.setGroup(userGroups);
		userRepo.save(user);
	}
	
	private void deleteUserGroup(String email, String groupId) {
		DBUser user = userRepo.findByEmail(email);
		Map<String, String> groups = getUserGroups(user);
		groups.remove(groupId);
		user.setGroup(groups);
		userRepo.save(user);
	}
	
	public String createGroup(String groupName, String manager) {
			
		DBGroup group = new DBGroup( manager, groupName);	
		Map<String, String> members = new HashMap<String, String>();
		members.put(manager, userRepo.findByEmail(manager).getUserName());
		group.setMembers(members);
		addUserGroup(manager, group.getGroupId(), group.getGroupName());
		groupRepo.insert(group);
		return group.getGroupId();
	}
	public String createGroup(String groupName, String manager, String members) {
		
		System.out.println("groupService: manager = "+manager);
		
		DBGroup group = new DBGroup(manager, groupName);
		Map<String, String> member = new HashMap<String, String>();
		member.put(manager, userRepo.findByEmail(manager).getUserName());
		addUserGroup(manager, group.getGroupId(), group.getGroupName());
		
		System.out.println("groupService: members = "+members);
		
		if(!members.equals("NONE")) {
			String mems[] = members.split("_");
			for(String email: mems) {
				member.put(email, userRepo.findByEmail(email).getUserName());
				addUserGroup(email, group.getGroupId(), group.getGroupName());
			}
		}
		
		group.setMembers(member);
		
		groupRepo.save(group);
		return group.getGroupId();
	}
	
	public DBGroup getGroupInfo(String groupId) {
		return groupRepo.findByGroupId(groupId);
	}
	
	public String deleteGroup(String groupId, HttpServletRequest req) throws Exception {
		
		String email = cookieServices.getEmail(req);
		
		DBGroup group = groupRepo.findByGroupId(groupId);
		Map<String, String> members = group.getMembers();

			if(group.getManager().equals(email)) {
				groupRepo.delete(group);
				
				for (Map.Entry<String, String> m : members.entrySet()) {
					deleteUserGroup(m.getKey(), groupId);
				}
				return "SUCCEED";
			}else throw new Exception("something wrong!");
	}
	public String deleteGroup(String groupId, String email) throws Exception {
		
		
		DBGroup group = groupRepo.findByGroupId(groupId);
		Map<String, String> members = group.getMembers();

			if(group.getManager().equals(email)) {
				groupRepo.delete(group);
				
				for (Map.Entry<String, String> m : members.entrySet()) {
					deleteUserGroup(m.getKey(), groupId);
				}
				return "SUCCEED";
			}else throw new Exception("something wrong!");
	}
	
	public String leaveGroup(String groupId, HttpServletRequest req) throws Exception {
		
		String email = cookieServices.getEmail(req);
		
		DBGroup group = groupRepo.findByGroupId(groupId);
		
		Map<String, String> groupMembers = group.getMembers();
		
		if(groupMembers.containsKey(email)) {
			deleteUserGroup(email, groupId);
			groupMembers.remove(email);
			group.setMembers(groupMembers);
			groupRepo.save(group);
			return "SUCCEED";
		}else throw new Exception("You are not a members of that group");
	}
public String leaveGroup(String groupId, String email) throws Exception {
		
		DBGroup group = groupRepo.findByGroupId(groupId);
		
		Map<String, String> groupMembers = group.getMembers();
		
		if(groupMembers.containsKey(email)) {
			deleteUserGroup(email, groupId);
			groupMembers.remove(email);
			group.setMembers(groupMembers);
			groupRepo.save(group);
			return "SUCCEED";
		}else throw new Exception("You are not a members of that group");
	}
	
	
	public String deleteMember(String groupId, String member, HttpServletRequest req) throws Exception {
		
		String manager = cookieServices.getEmail(req);
		
		DBGroup group = groupRepo.findByGroupId(groupId);
		
		Map<String, String> groupMembers = group.getMembers();
		
		if (group.getManager().equals(manager)) {
			groupMembers.remove(member);
			deleteUserGroup(member, groupId);
			group.setMembers(groupMembers);
			groupRepo.save(group);
			return "SUCCEED";
		}else throw new Exception("Permission denied");
	}
	public String deleteMember(String groupId, String member, String email) throws Exception {
		
		String manager = email;
		
		DBGroup group = groupRepo.findByGroupId(groupId);
		
		Map<String, String> groupMembers = group.getMembers();
		
		if (group.getManager().equals(manager)) {
			groupMembers.remove(member);
			deleteUserGroup(member, groupId);
			group.setMembers(groupMembers);
			groupRepo.save(group);
			return "SUCCEED";
		}else throw new Exception("Permission denied");
	}
	
	public Map<String, String> getMembers(String groupId){
		
		DBGroup group = groupRepo.findByGroupId(groupId);
		
		return group.getMembers();
	}
	
	public String addGroupMember(String newMember, String groupId, HttpServletRequest req) throws Exception {
		DBGroup group = groupRepo.findByGroupId(groupId);
		
		DBUser user = userRepo.findByEmail(newMember);
		
		String email = cookieServices.getEmail(req);
		
		Map<String, String> members = group.getMembers();
		
		if(email != null && members.containsKey(email)) {
			if(user != null && !group.getMembers().containsKey(newMember)) {
				
				addUserGroup(newMember, group.getGroupId(), group.getGroupName());
				
				members.put(newMember, user.getUserName());
				group.setMembers(members);
				groupRepo.save(group);
				
				return "SUCCESS";
			}
			return "User does not exist or already a group member";
		}else throw new Exception("Something wrong!");
	}
	public String addGroupMember(String newMembers, String groupId, String email) throws Exception {
		DBGroup group = groupRepo.findByGroupId(groupId);
		
		Map<String, String> members = group.getMembers();
		
		if(email != null && members.containsKey(email)) {
				if(!newMembers.equals("NONE")) {
					String mems[] = newMembers.split("_");
					for(String mem: mems) {
						if(!members.containsKey(mem)) {
							addUserGroup(mem, group.getGroupId(), group.getGroupName());
							members.put(mem, userRepo.findByEmail(mem).getUserName());
						}
					}
					group.setMembers(members);
					groupRepo.save(group);
					return "Succeed";
				}
			return "User does not exist or already a group member";
		}else throw new Exception("Something wrong!");
	}
	
}
