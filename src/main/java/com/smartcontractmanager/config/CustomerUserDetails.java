package com.smartcontractmanager.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.smartcontractmanager.entities.User;

public class CustomerUserDetails implements UserDetails{

	
	private User user;
	
	
	public CustomerUserDetails(User user) {
		super();
		this.user = user;
	}
 

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority simpleGrantedAuthority =  new SimpleGrantedAuthority(user.getRole());
		// TODO Auto-generated method stub
		return  List.of(simpleGrantedAuthority);
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
		  
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}
	
	

}
