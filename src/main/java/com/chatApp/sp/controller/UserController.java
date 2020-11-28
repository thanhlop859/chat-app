package com.chatApp.sp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatApp.sp.model.DBUser;
import com.chatApp.sp.utils.UserUtils;

@RestController
@CrossOrigin
public class UserController {
	
	@Autowired
	UserUtils userUtils;
	
	
	//tạo tài khoản
	@PostMapping("/signup")
	public String createAccount(@RequestParam("newEmail") String email, @RequestParam("newPassword") String password, 
								@RequestParam("newUserName") String userName,
								@RequestParam("newAge") String age,
								@RequestParam("NewGender") String gender) throws Exception {
		return  userUtils.createUser(email, userName, password, age, gender);
	}
	
	
	//lấy thông tin tài khoản
	@GetMapping("/users/{email}")
	public DBUser viewUserProfile(@PathVariable("email") String email) throws Exception {
		return userUtils.viewUserProfile(email);
	}
	
	
	//sửa thông tin tài khoản
	@PutMapping("/users/edit")
	public String editUserProfile(@RequestParam("editPassword") String password,
								  @RequestParam("editUserName") String userName,
								  @RequestParam("editAge") String age,
								  HttpServletRequest req) {
		
		System.out.println("tên, mật khẩu và tuổi: "+password+" "+ userName+" "+age);
		return userUtils.updateUserProfile(password, userName, age, req);
	}
	
	//sửa thông tin tài khoản trên app
	@PutMapping("/app/users/edit")
	public String appEditUserProfile(@RequestParam("editPassword") String password,
								  	@RequestParam("editUserName") String userName,
								  	@RequestParam("editAge") String age,
								  	@RequestHeader("email") String email) {
		return userUtils.updateUserProfile(password, userName, age, email);
	}
	
	
	//Xoá tài khoản
	@DeleteMapping("/users/delete")
	public String deleteAccount(HttpServletRequest req) {
		return userUtils.deleteAccount(req);
	}	
	
	//Xoá tài khoản trên app
	@DeleteMapping("/app/users/delete")
	public String appDeleteAccount(@RequestHeader("email") String email) {
		return userUtils.deleteAccount(email);
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "done";
	}
}
