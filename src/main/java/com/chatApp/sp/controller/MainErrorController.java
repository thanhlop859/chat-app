
package com.chatApp.sp.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
public class MainErrorController implements ErrorController {

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, Model model) {
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	        
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	        	model.addAttribute("errorType", "404");
	        }
	        else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	        	model.addAttribute("errorType", "500");
	        }
	        else{
	        	model.addAttribute("errorType", "");
	        }
	    }
	    return "error";
	}
	
	
	@GetMapping("/accessDenied")
	public String error(Model model) {
		model.addAttribute("type", "403");
			return "login";
	}

	@Override
	@Deprecated
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return null;
	}

}
