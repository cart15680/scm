package com.smartcontractmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.smartcontractmanager.dao.UserRepo;
import com.smartcontractmanager.entities.User;
import com.smartcontractmanager.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {
	
	@Autowired
	 private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepo userRepo;

	@GetMapping("/")
	private String home( Model model) {
		
		model.addAttribute("title", "Home - Smart Contact Manager");
		
		return "home";
	}
	@GetMapping("/home")
	private String homeTemp( Model model) {
		
		model.addAttribute("title", "Home - Smart Contact Manager");
		
		return "home";
	}
	


	@GetMapping("/signup")
	private String signup( Model model, HttpSession session) {
		
		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user",new User());
		session.invalidate();
		return "signup";
	}
		

	@GetMapping("/about")
	private String about( Model model) {
		
		model.addAttribute("title", "About - Smart Contact Manager");
		
		return "about";
	}
		
	
	
	
	@PostMapping("/do_register")
	private String registerUser( 
			@Valid @ModelAttribute("user") User user, 
			BindingResult bindingResult,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, 
			Model model,
			HttpSession session) { 
		 
		System.out.println("agreement = "+agreement);
		System.out.println("User data === "+user);
		
		
		try {
			
			if(!agreement) {
				model.addAttribute("user", user);
				model.addAttribute("errorAgreement", "Please accept the Terms & Conditions");
				System.out.println("please click Agreement.");
				throw new Exception("Please accept the Terms & Conditions"); 
			}
			
			if(bindingResult.hasErrors()) {

				model.addAttribute("user", user); 
				return "signup";
				
			}
			
			
			
			user.setRole("ROLE_USER");
			user.setEnable(true);
			user.setImageUrl("default.jpg");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			User userResult =  this.userRepo.save(user);
			model.addAttribute("user",userResult);
			System.out.println("userResult ====== "+userResult);
			
			model.addAttribute("user",new User());
			session.setAttribute("Success", new Message("Successfully Registered...","alert-success"));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Ooops, Something went wrong!!! "+e.getMessage(),
					"alert-error"));

			return "signup";
			
		}
		
		
		
		return "login";
	}
	
	
	
	
	
	@GetMapping("/signin")
	public String login(Model model, HttpSession session) {
		
		System.out.println("Login page opened.");
		
		model.addAttribute("title", "Login Page"); 
		session.invalidate(); 
		return "login";
	}
	
	
	
	 
	@PostMapping("/do_login")
	private String loginUser(@RequestParam("password") String password, @RequestParam("email") String email) {
	    // Process the form data
	    System.out.println("ddddddddddddddddddddddddddddddd Name: " + password + ", Email: " + email);
	    return "login"; // Return a view or a redirect
	}
	
	
	
	

}
