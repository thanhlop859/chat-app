package com.chatApp.sp.authservice.filter;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.chatApp.sp.authservice.service.TokenAuthenticationService;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter{
	
	public JWTLoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		//DBUser user = new ObjectMapper().readValue(request.getInputStream(), DBUser.class);
		
		
		//System.out.println("e and p: "+user.getEmail()+" / "+user.getPassword());
		
		String email = request.getParameter("email");//user.getEmail();
		String password = request.getParameter("password");//user.getPassword();
		
		System.out.printf("JWTLoginFilter.attemptAuthentication: email/password= %s,%s", email, password);
        System.out.println();
        
		return getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(email,  password, Collections.emptyList()));
	}
	
	@Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException{
		
		 System.out.println("JWTLoginFilter.successfulAuthentication:");
		 
	        // Write Authorization to Headers of Response.
	        TokenAuthenticationService.addAuthentication(response, authResult.getName());
	 
	        String authorizationString = response.getHeader("Authorization");
	 
	        System.out.println("Authorization String=" + authorizationString);
	}
	
	
}
