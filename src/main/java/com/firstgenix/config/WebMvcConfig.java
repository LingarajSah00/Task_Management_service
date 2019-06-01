package com.firstgenix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}
	
	public void addCorsMappings(CorsRegistry registry) {
		  registry.addMapping("/**")
		   	  .allowedOrigins("*")
			  .allowedMethods("POST", "GET",  "PUT", "OPTIONS", "DELETE")
			  .allowedHeaders("X-Auth-Token", "Content-Type","Authorization")
			  .exposedHeaders("Authorization")
			  .allowCredentials(true)
			  .maxAge(4800);
		}

}
