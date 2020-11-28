package com.chatApp.sp.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class CookieServices {
	
	public String getEmail(HttpServletRequest req) {
		Cookie cookies[] = req.getCookies();
		
		for(Cookie c: cookies) {
			
			if(c.getName().equals("email")) {
				return c.getValue();
			}
		}
		
		return null;
	}
	
	public Cookie setCookie(String name, String value, boolean httpOnly, int age) {
		Cookie c = new Cookie(name, value);
		c.setHttpOnly(httpOnly);
		c.setMaxAge(age);
		return c;
	}
	
	public String getCookie(String cookieName, HttpServletRequest req) {
		Cookie cookies[] = req.getCookies();
		
		for(Cookie c: cookies) {
			
			if(c.getName().equals(cookieName)) {
				return c.getValue();
			}
		}
		
		return null;
	}
}
