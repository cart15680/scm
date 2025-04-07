package com.smartcontractmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartcontractmanager.dao.ContactRepo;
import com.smartcontractmanager.dao.UserRepo;
import com.smartcontractmanager.entities.Contact;
import com.smartcontractmanager.entities.User;

@RestController
public class SearchController {
	
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ContactRepo contactRepo;
	
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query , Principal principal ){
		
		User user = this.userRepo.getUserByUserName(principal.getName());
		List<Contact> contacts =  this.contactRepo.findByNameContainingAndUser(query,user);
		
		return ResponseEntity.ok(contacts);
		
		
	}
}
















