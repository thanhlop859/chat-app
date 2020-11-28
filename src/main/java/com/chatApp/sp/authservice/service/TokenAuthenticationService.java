package com.chatApp.sp.authservice.service;

import java.util.Collections;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.chatApp.sp.service.CookieServices;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenAuthenticationService {
	
	static final long EXPIRATION_TIME = 864000000;
	
	static final String SECRET_KEY = "ThisIsSecretKey";
	
	static final String TOKEN_PREFIX = "Bearer";
	
	static final String HEADER_STRING = "Authorization";
	
	// add "authorization" to response header
	public static void addAuthentication(HttpServletResponse res, String username) {
		
		String JWT = Jwts.builder().setSubject(username)
						.setExpiration(new Date(System.currentTimeMillis()+ EXPIRATION_TIME))
						.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
						.compact();
		System.out.println("Header: "+HEADER_STRING+ ": "+TOKEN_PREFIX+"v"+JWT);
		
		//res.setHeader(HEADER_STRING, TOKEN_PREFIX+" "+JWT);
		
		ResponseCookieBuilder authBuilder = ResponseCookie.from(HEADER_STRING,TOKEN_PREFIX+"_"+JWT);
		authBuilder.maxAge(24*2*60*60);
		authBuilder.sameSite("None");
		authBuilder.httpOnly(true);
		authBuilder.secure(true);
		
		
		ResponseCookieBuilder emailBuilder = ResponseCookie.from("email",username);
		emailBuilder.maxAge(24*2*60*60);
		emailBuilder.sameSite("None");
		emailBuilder.secure(true);
		
		ResponseCookie auth = authBuilder.build();
		ResponseCookie email = emailBuilder.build();
		
		res.addHeader("Set-Cookie",auth.toString());
		res.addHeader("Set-Cookie",email.toString());
		
		System.out.println(HEADER_STRING + " " + JWT);
	}
	
	public static Authentication getAuthentication(HttpServletRequest req, HttpServletResponse res) {
		
		//String token  = req.getHeader(HEADER_STRING);
		String token = null;
		
		String jsessionId = null;
		
		Cookie cookies[] = req.getCookies();
		
		if(cookies != null)
		for(Cookie c: cookies) {
			if(c.getName().equals(HEADER_STRING))
				token = c.getValue();
			if(c.getName().equals("JSESSIONID"))
				jsessionId = c.getValue();
		}		
		
		if(jsessionId != null) {
			ResponseCookieBuilder jsession = ResponseCookie.from("JSESSIONID", jsessionId);
			jsession.sameSite("None");
			jsession.secure(true);
			jsession.httpOnly(true);
			res.addHeader("Set-Cookie", jsessionId.toString());
		}
		
		
		
		if(req.getHeader("logout") != null) {
			Cookie auth = new Cookie(HEADER_STRING, "");
			auth.setMaxAge(0);
			auth.setHttpOnly(true);
			Cookie email = new Cookie("email", "");
			email.setMaxAge(0);
			res.addCookie(auth);
			res.addCookie(email);
		}
	
		if(token != null && !token.equals("")) {
			String user = Jwts.parser().setSigningKey(SECRET_KEY)
								.parseClaimsJws(token.replace(TOKEN_PREFIX+"_", "")).getBody()
								.getSubject();
			return (user != null)? new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()): null;
		}
		
		return null;
	}

}
