package com.chatApp.sp.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatApp.sp.model.DBGroup;
import com.chatApp.sp.repository.UserRepository;
import com.chatApp.sp.service.CookieServices;
import com.chatApp.sp.service.GroupServices;

@RestController
@CrossOrigin
public class GroupController {
	
	
	@Autowired
	GroupServices groupServices;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	CookieServices cookieServices;
	
	
	//tạo nhóm mới +
	@PostMapping("/groups/create")
	public String createGroup(@RequestParam("newGroupName") String groupName,@RequestParam("email") String email,  HttpServletRequest req) {
		
		String manager = email;//cookieU.getEmail(req);
		
		String a = groupServices.createGroup(groupName, manager);
		
		return a;
	}
	@PostMapping("/app/groups/create")
	public String appCreateGroup(@RequestParam("newGroupName") String groupName, @RequestParam("members") String members , @RequestHeader("email") String email) {
		
		String manager = email;//cookieU.getEmail(req);
		
		String a = groupServices.createGroup(groupName, manager, members);
		
		return a;
	}
	
	//lấy danh sách nhóm của một user +
	@GetMapping("/groups")
	public Map<String, String> getAllGroups(HttpServletRequest req){		
		String email = cookieServices.getEmail(req);
		System.out.println("+++++++email: "+ email+" +++++++++");
		return userRepo.findByEmail(email).getGroup();	
	}
	@GetMapping("/app/groups")
	public Map<String, String> appGetAllGroups(@RequestHeader("email") String email){	
		return userRepo.findByEmail(email).getGroup();	
	}
	
	
	//xoá members khỏi nhóm (nếu là manager thì hiện cái này lên, còn không phải thì ẩn đi) +
	@DeleteMapping("/groups/delete/members")
	public String deleteMembers(@RequestParam("groupId") String groupId, @RequestParam("member") String member,@RequestParam("email") String email, HttpServletRequest req) throws Exception {
		return groupServices.deleteMember(groupId, member, req);
	}
	@DeleteMapping("/app/groups/delete/members")
	public String appDeleteMembers(@RequestParam("groupId") String groupId, @RequestParam("member") String member,@RequestHeader("email") String email) throws Exception {
		return groupServices.deleteMember(groupId, member, email);
	}
	
	
	//xem thông tin nhóm +
	@GetMapping("/groups/profile/{groupid}") 
	public DBGroup getGroup(@PathVariable("groupid") String groupId) {
		return groupServices.getGroupInfo(groupId);
	}
	
	
	//xem danh sách members +
	@GetMapping("/groups/members/{groupId}")
	public Map<String, String> viewGroupMember(@PathVariable("groupId") String groupId){
		return groupServices.getMembers(groupId);
	}
	
	
	//thêm members (1 lần thêm 1 người) +
	@PostMapping("/groups/add")
	public String addMember(@RequestParam("friendEmail") String friendEmail, @RequestParam("groupId") String groupId,@RequestParam("email") String email,  HttpServletRequest req) throws Exception {
		return groupServices.addGroupMember(friendEmail, groupId, req);
	}
	@PostMapping("/app/groups/add")
	public String appAddMember(@RequestParam("newMembers") String newMembers, @RequestParam("groupId") String groupId,@RequestHeader("email") String email) throws Exception {
		return groupServices.addGroupMember(newMembers, groupId, email);
	}
	
	
	//rời nhóm
	@PostMapping("/groups/leave")
	public String leaveGroup(@RequestParam("groupId") String groupId, @RequestParam("email") String email, HttpServletRequest req) throws Exception {
		return groupServices.leaveGroup(groupId, req);
	}
	@PostMapping("/app/groups/leave")
	public String leaveGroup(@RequestParam("groupId") String groupId, @RequestHeader("email") String email) throws Exception {
		return groupServices.leaveGroup(groupId, email);
		//System.out.println("***********************leave group: groupId: "+groupId+ ", email: "+email);
		//return "testing";
	}
	
	
	//xoá nhóm(nếu là manager thì hiện cái này lên, còn không phải thì ẩn đi)
	@DeleteMapping("/groups/delete")
	public String deleteGroup(@RequestParam("groupId") String groupId,@RequestParam("email") String email, HttpServletRequest req) throws Exception {
		return groupServices.deleteGroup(groupId, req);
	}
	@DeleteMapping("/app/groups/delete")
	public String appDeleteGroup(@RequestParam("groupId") String groupId,@RequestHeader("email") String email) throws Exception {
		return groupServices.deleteGroup(groupId, email);
		//System.out.println("----------------------------delete group: groupId: "+groupId+ ", email: "+email);
		//return "testing";
	}
}
