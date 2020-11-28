package com.chatApp.sp.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.chatApp.sp.model.DBUser;
import com.chatApp.sp.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	public UserRepository userRepository;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private List<GrantedAuthority> getAuthority(String role){
		Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
		
		roles.add(new SimpleGrantedAuthority(role));
		
		List<GrantedAuthority> auth = new ArrayList<GrantedAuthority>(roles);
		
		return auth;
	}
	
	private UserDetails buildUser(DBUser user, List<GrantedAuthority> auth) {
		return new User(user.getEmail(), user.getPassword(), auth);
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		DBUser user = userRepository.findByEmail(email);
		if(user != null) {
			System.out.println("Found user: "+ email);
			return buildUser(user, getAuthority(user.getRole()));
		} else
			throw new UsernameNotFoundException("Email not found!");
	}

}
