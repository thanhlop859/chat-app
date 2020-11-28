package com.chatApp.sp.controller;


import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chatApp.sp.service.DropboxServices;
import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.UploadErrorException;

@Controller
@CrossOrigin
public class MainController {
	
	@GetMapping("/login")
	public String loginPage(Model model) {
		System.out.println("login:  ++++++++++");
		model.addAttribute("type", "login");
		return "login";
	}
	@GetMapping("/")
	public String wellcome(Model model) {
		System.out.println("home: +++++++++++++");
		model.addAttribute("type", "");
		return "login";
	}
	@GetMapping("/signup")
	public String signup(Model model) throws DbxApiException, DbxException {
		System.out.println("signup: +++++++++++++++");
		model.addAttribute("type", "signup");
		return "login";
	}	
	
	@GetMapping("/upload")
	@ResponseBody
	public String huhu() throws UploadErrorException, DbxException, IOException {
		DropboxServices.getFiles();
		return "upload";
	}
}
