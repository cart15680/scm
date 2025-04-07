package com.smartcontractmanager.dao;
 

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcontractmanager.entities.Contact;
import com.smartcontractmanager.entities.User; 

public interface ContactRepo extends JpaRepository< Contact, Integer>{
	
	//pagination
 
	
	//current page
	// contact per page
	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactByUser( @Param("userId") int userId, Pageable pageable   ); 
	
  
	public List<Contact> findByNameContainingAndUser(String name, User user);
	
	
	

}
