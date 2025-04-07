package com.smartcontractmanager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smartcontractmanager.dao.ContactRepo;
import com.smartcontractmanager.dao.UserRepo;
import com.smartcontractmanager.entities.Contact;
import com.smartcontractmanager.entities.User;
import com.smartcontractmanager.helper.Message;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.multipart.MultipartFile;




@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ContactRepo contactRepo;
	
	private Logger  logger =  LogManager.getLogger(HomeController.class);
	
	
	
	 

	//Adding common data in every handling
	@ModelAttribute
	public void addCommonDataSend(Model model, Principal principal, HttpSession session) {

		String UserName =  principal.getName(); 
		
		User user = userRepo.getUserByUserName(UserName);
		System.out.println( user + "Username from login === "+  UserName);
		
		model.addAttribute("user", user);

		System.out.println(   "Username from login === "+  UserName);
		logger.info( "This is from loger =======================================" + UserName );
		System.out.println( "Username from login === "+  UserName);
		  
	}
	
	 
	
	
	
	
	
	
	
	
	

	///                                    User Dashboard Handler
	@GetMapping("/index") 
	public String dashboard(Model model, Principal principal) {
		

		model.addAttribute("title","Home Page");
		
		return "/normal/user_dashboard";
	}
	
	
	     
	
	
	
	
	
	

	///                                     add Contact page Handler 
	//open add form handler
	@GetMapping("/add-contact") 
	public  String openAddContactForm(Model model, HttpSession session) {
		
		session.removeAttribute("message");
		model.addAttribute("title","Add Contact Page");
		Contact contact =  new Contact();
		
		contact.setDescription("l@l");
		contact.setEmail("l@l");
		contact.setName("l@l");
		contact.setWork("l@l");
		contact.setSecondName("l@l");
		contact.setPhone("1235456"); 
		
		
		
		model.addAttribute("contact", contact );
		
		return "normal/add_contact_form";
	}
	
	
	
	
	
	
	
	
	 

	///                            Add Contact process Handler
	@PostMapping("/addContact")
	public String addContactForm(@RequestParam("image") MultipartFile  image ,@Valid   @ModelAttribute   Contact contact,
			BindingResult bindingResult , Model model, Principal principal , HttpSession session ) {
		
 
		System.out.println("Contact ========= "+contact);
		
		try {
			
			if(contact.getEmail() == null) { 
				model.addAttribute("contact", contact); 
				System.out.println("please click Agreement.");
				throw new Exception("Something error"); 
				
			}

			String userName = principal.getName();
			User user = this.userRepo.getUserByUserName(userName);
			//image file checking
			if(image.isEmpty()) {
				//
				System.out.println("file has not been uploaded.");
				contact.setImage( "default.jpg") ;
				
			}else {
				
				
				
				// time format for uploading. 
				 LocalDateTime now = LocalDateTime.now();
			        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
			        String formattedTime = now.format(formatter);
				
				File saveFile =  new ClassPathResource("/static/contactPhoto").getFile();
				Path path =  Paths.get(saveFile.getAbsolutePath()+File.separator+  formattedTime +'_' +  contact.getPhone() +".jpg" );
				System.out.println("Image path Named === " + path);
				Files.copy(image.getInputStream(),  path , StandardCopyOption.REPLACE_EXISTING );
				System.out.println("Image is uploaded. "  );
				contact.setImage( formattedTime +'_' +  contact.getPhone() +".jpg" ) ;
				
			} 
			contact.setUser(user);
			user.getContacts().add(contact); 
			System.out.println("user ========= "+  user  );
			 
			this.userRepo.save(user);
			System.out.println(" User added.");
			
			
			//message success
			session.setAttribute("message", new Message("Your contact has been added successfully!! Add more...", "success"));
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("contact", contact); 
			model.addAttribute("statusFalse",  false );
			//message success
			session.setAttribute("message", new Message(" Ooops, Something went wrong!!!", "danger"));

			return "normal/add_contact_form";
		}
			model.addAttribute("statusTrue",  true );
			model.addAttribute("contact", contact);
//		model.addAttribute("contact", new Contact());
		return  "normal/add_contact_form";
	}
	
	
	
	
	
	
	
	
	
	//                                   show all contacts handler
//	per page = 5[N]
//	current page  = 0 [page]
	@GetMapping("/show-contacts/{page}") 
	public String showContacts(	@PathVariable("page") Integer page,	Model model, Principal principal) {
		 
		try { 
			String userName = principal.getName();
			User user = this.userRepo.getUserByUserName(userName); 
//			List<Contact> contactList = 	user.getContacts();
//			System.out.println("Contact list is ====== " + contactList); 
			System.out.println(" user.getId()user.getId()user.getId()user.getId()" +user.getId()); 
			
			
			Pageable pageable = 	PageRequest.of(page, 5);
			
			Page<Contact> contacts     =	this.contactRepo.findContactByUser(user.getId(), pageable); 
			model.addAttribute("contacts", contacts); 
			model.addAttribute("currentPage",page);
			System.out.println("contacts.getTotalPages() ====== "+contacts.getTotalPages());
			model.addAttribute("totalPages",contacts.getTotalPages() );
			model.addAttribute("data_count", contacts.getTotalElements());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		model.addAttribute("title","View Contact Page"); 
		return "normal/show_contacts";
	}
	
	
	
	
	
	
	
	
	
	
	
	

	///                                      Modify Contact Details Handler
	@GetMapping("/{cID}/modifyPerson")
	public String modifyContactPerson( @PathVariable("cID") Integer cID, Model model, Principal principal) {
		
		try {
 
			model.addAttribute("title","Modify Contact");  
			Optional<Contact> contact1 = this.contactRepo.findById(cID);
			  Contact contactPerson = contact1.get();
			  System.out.println("Contact ID =============== " +contactPerson    );
			  User user = this.userRepo.getUserByUserName(principal.getName());
			  if(user.getId() == contactPerson.getUser().getId()) {
				   model.addAttribute("contact", contactPerson  );
			  }else {
				  model.addAttribute("noData", "Oops!!! No contact present..."  );
			  }
				 
			  
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return  "normal/modify_contact_person";
	}
	
	
	
	
	
	
	
	
	
	
	
	

	///                                       show Contact Details Handler
	@GetMapping("/{cID}/contactPerson")
	public String showContactPerson( @PathVariable("cID") Integer cID, Model model, Principal principal) {
		
		try {
 
			model.addAttribute("title","Show Contact Details");  
			Optional<Contact> contactDetails = this.contactRepo.findById(cID);
			  Contact contact = contactDetails.get();
			  System.out.println("Contact ID =============== " +contact    );
			  User user = this.userRepo.getUserByUserName(principal.getName());
			  if(user.getId() == contact.getUser().getId()) {
				   model.addAttribute("contact", contact  );
			  }else {
				  model.addAttribute("noData", "Oops!!! No contact present..."  );
			  }
				 
			  
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return  "normal/show_person_contact";
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	///                                                    Delete Contact Handler
	@GetMapping("/{cID}/deleteContact")
	public String deleteContactPerson( @PathVariable("cID") Integer cID, Model model, Principal principal, HttpSession session) {
		
		try {

			model.addAttribute("title","View Contact Page"); 
			Optional<Contact> contactDetails = this.contactRepo.findById(cID);
			Contact contact  = contactDetails.get();
			System.out.println("Contact ID =============== " +contact    );
			User user = this.userRepo.getUserByUserName(principal.getName());
			if(user.getId() == contact.getUser().getId()) {
				contact.setUser(null);
				
				
			System.out.println("file deleting=============== " + contact.getImage()   );
//				String fileName = contact.getImage() ;
				if(contact.getImage().equals("default.jpg" )  ) {
					
					System.out.println("file deleting=============== " + contact.getImage()   );
					
				} else {
					
	 				File saveFile =  new ClassPathResource("/static/contactPhoto").getFile(); 		
	 				Path path =  Paths.get(saveFile.getAbsolutePath()+File.separator+ contact.getImage() ); 
	 				System.out.println("path==========================================================="+path ); 
	 				Files.delete( path);

					 
					
				}
				 
				this.contactRepo.delete(contact);
				
				 
				session.setAttribute("message",new Message("Contact deleted sucessfully...","success")); 
			}else {
				session.setAttribute("message",new Message("You are not the authorize person to delete this contact. ","danger"));  
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			session.setAttribute("message",new Message("Oops!!! Something went wrong!!!","danger")); 
			e.printStackTrace();
		}
		 
		
		return  "redirect:/user/show-contacts/0";
	}
	
	
	
	
	
	
	
	
	@PostMapping("/process-update")
	public String updateContact( @RequestParam("image") MultipartFile  image ,@Valid   @ModelAttribute   Contact contact,
			BindingResult bindingResult , Model model, Principal principal , HttpSession session    ) {
		

		System.out.println("cIdcIdcIdcId========"+contact.getcId());
		try {
			
			
			if(image.isEmpty()) {
				//file work

				System.out.println("file has not been uploaded.");
//				contact.setImage( "default.jpg") ; 
			}else {

				Optional<Contact> contactDetails = this.contactRepo.findById( contact.getcId());
				Contact contactDetail  = contactDetails.get();
				
				System.out.println("ClassPathResource1		"+contactDetail.getImage());
				if(!contactDetail.getImage().equals("default.jpg" ) ) {
						
				System.out.println("ClassPathResource2		"+contactDetail.getImage());
					
	 				File deleteFile   =  new ClassPathResource("/static/contactPhoto").getFile(); 		
	 				Path pathForDelete =  Paths.get(deleteFile.getAbsolutePath()+File.separator+ contactDetail.getImage() ); 
	 				System.out.println("path==========================================================="+pathForDelete ); 
	 				Files.delete( pathForDelete);
	 				
				}
// 				File deleteFile   =  new ClassPathResource("/static/contactPhoto").getFile(); 		
// 				Path pathForDelete =  Paths.get(deleteFile.getAbsolutePath()+File.separator+ contactDetail.getImage() ); 
// 				System.out.println("path==========================================================="+pathForDelete ); 
// 				Files.delete( pathForDelete);
 				
 				
				// time format for uploading. 
				 LocalDateTime now = LocalDateTime.now();
			        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
			        String formattedTime = now.format(formatter);
				
				File saveFile =  new ClassPathResource("/static/contactPhoto").getFile();
				Path path =  Paths.get(saveFile.getAbsolutePath()+File.separator+  formattedTime +'_' +  contact.getPhone() +".jpg" );
				System.out.println("Image path Named === " + path);
				Files.copy(image.getInputStream(),  path , StandardCopyOption.REPLACE_EXISTING );
				System.out.println("Image is uploaded. "  );
				contact.setImage( formattedTime +'_' +  contact.getPhone() +".jpg" ) ;
				


				
			}

			String userName = principal.getName();
			User user = this.userRepo.getUserByUserName(userName);

			contact.setUser(user);  
			 
			this.contactRepo.save(contact);
			System.out.println(" User added."); 
			 
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
		return  "redirect:/user/"+contact.getcId() +"/contactPerson";
		
		
	}
	
	
	
	
	
	
	//your profile showing page handling
	@GetMapping("/profile")
	public String yourProfile(Model model, Principal principal) {
		

		  User user = this.userRepo.getUserByUserName(principal.getName()); 
		  System.out.println("user result =================== " + user);
		 
		   
		  model.addAttribute("user",  user  ); 
		  model.addAttribute("title","Profile Page");
		  return "normal/profile";
	}
	
	
	
	
	
	
	
	
	
}






















