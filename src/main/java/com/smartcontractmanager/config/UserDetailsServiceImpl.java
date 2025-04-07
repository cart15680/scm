package com.smartcontractmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartcontractmanager.dao.UserRepo;
import com.smartcontractmanager.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	
	
	@Override
	public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException {
		
		
		//fetching user by database; 
		User user = 	userRepo.getUserByUserName(username); 
		System.out.println("USER is  =====  "+user); 
			if(user == null) {
				throw new UsernameNotFoundException("could not found user ");
			}  
			CustomerUserDetails customerUserDetails = new   CustomerUserDetails(user ); 
		return customerUserDetails;
	}
	
	
	
}
