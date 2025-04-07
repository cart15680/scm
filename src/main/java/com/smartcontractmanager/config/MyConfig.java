package com.smartcontractmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain; 

@Configuration
@EnableWebSecurity
public class MyConfig          {  


    @Bean
    UserDetailsService getUserDetailsService() {
		 return new UserDetailsServiceImpl();
	}
	
    
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    
    //old method 
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    	daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService()); 
    	daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
     
    	return daoAuthenticationProvider;
    		
    }
  
 
//	@Bean
//	UserDetailsService userDetailsService() {
//		UserDetails normalUser = User.withUsername("b").password(passwordEncoder().encode("b")).roles("NORMAL").build();
//		UserDetails adminUser  = User.withUsername("s").password(passwordEncoder().encode("s")).roles("ADMIN").build(); 
//		
//		InMemoryUserDetailsManager inMemoryUserDetailsManager  =new InMemoryUserDetailsManager( normalUser,adminUser );
//		
//		 return inMemoryUserDetailsManager;
//		
//	}
//	  

    @Bean
	protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
	   return  httpSecurity
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth 
	        		//if you are using @EnableMethodSecurity then you  don't need to mention those line. you will have to use @PreAuthorize("hasRole('NORMAL')") at the specific endpoint
	        		.requestMatchers("/admin/**").hasRole("ADMIN") 
	        		.requestMatchers("/user/**").hasRole("USER") 
//	        		.requestMatchers("/home/public").permitAll()	
//	        		.requestMatchers("/user/**")
	        		.requestMatchers("/**").permitAll()
	            
	        )
	        .formLogin(form -> form
	     				.loginPage("/signin")
	     				.loginProcessingUrl("/dologin")
	     				.defaultSuccessUrl("/user/index", true)
	     				.failureUrl("/signin?error") )
	        .build();
	         
//	     		httpSecurity.formLogin(form -> form
//	     				.loginPage("/signin")
//	     				.loginProcessingUrl("/dologin")
//	     				.defaultSuccessUrl("/user/index")
//	     				.failureUrl("/login-fail") 
//	     			) ;
//	     		httpSecurity.httpBasic(Customizer.withDefaults());
	       
	}
    

//	@Bean
//	protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//	     httpSecurity
//	        .csrf(csrf -> csrf.disable())
//	        .authorizeHttpRequests(auth -> auth 
//	        		//if you are using @EnableMethodSecurity then you  don't need to mention those line. you will have to use @PreAuthorize("hasRole('NORMAL')") at the specific endpoint
////	        		.requestMatchers("/home/normal").hasRole("NORMAL") 
////	        		.requestMatchers("/home/admin").hasRole("ADMIN") 
////	        		.requestMatchers("/home/public").permitAll()	
//	        		.requestMatchers("**").permitAll()	
//	            .anyRequest().authenticated()
//	        );
//	         
//	     		httpSecurity.httpBasic(Customizer.withDefaults());
//	   return  httpSecurity.build();
//	}
	

}
