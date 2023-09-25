package com.example.easyShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(scanBasePackages = {"com.example.easyShop","com.example.easyShop.controller","com.example.easyShop.model","com.example.easyShop.service","com.example.easyShop.dao", "com.example.easyShop.util"}, exclude = { SecurityAutoConfiguration.class })
public class EasyShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyShopApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new MD5PasswordEncoder();
	}
}
