package com.smartcontractmanager.entities;
 
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne; 

@Entity
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId; 
	private String name;
	private String secondName;
	private String work;
	private String email;
	private String phone;
	private String image;
	@Column(length = 1000)
	private String description;
	
	


	@ManyToOne
	@JsonIgnore
	private User user;
	
	
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Contact(int cId, String name, String secondName, String work, String email, String phone, String image,
			String description) {
		super();
		this.cId = cId;
		this.name = name;
		this.secondName = secondName;
		this.work = work;
		this.email = email;
		this.phone = phone;
		this.image = image;
		this.description = description;
	}
	public int getcId() {
		return cId;
	}
	public String getName() {
		return name;
	}
	public String getSecondName() {
		return secondName;
	}
	public String getWork() {
		return work;
	}
	public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phone;
	}
	public String getImage() {
		return image;
	}
	public String getDescription() {
		return description;
	}
	public void setcId(int cId) {
		this.cId = cId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	@Column(length = 50000)
	public void setDescription(String description) {
		this.description = description;
	}
	

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}


	
	
	@Override
	public String toString() {
		return "Contact [cId=" + cId + ", name=" + name + ", secondName=" + secondName + ", work=" + work + ", email="
				+ email + ", phone=" + phone + ", image=" + image + ", description=" + description + "]";
	}
	
	
	
	
}
