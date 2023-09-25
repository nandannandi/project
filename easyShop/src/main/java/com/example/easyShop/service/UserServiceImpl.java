package com.example.easyShop.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.easyShop.dao.UserDao;
import com.example.easyShop.model.UserRegister;

@Service
public class UserServiceImpl implements UserService {

	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	UserDao userDao;
	
	@Override
	public void registerUser(UserRegister user) {
		
		logger.info("Inside UserServiceImpl.registerUser Service");
		logger.info("Calling UserDao-registerUser");
		userDao.registerUser(user);
		createUserRole(user.getUserType());
	}
	
	private void createUserRole(String userType) {
		
		logger.info("Inside createUserRole method");
		if("BUYER".equalsIgnoreCase(userType)) {
			
			String roleName = "BUYER";
			String roleDesc = "User Role for Buyer";
			userDao.createUserRole(roleName, roleDesc);
		}
		if("SELLER".equalsIgnoreCase(userType)) {
			
			String roleName = "SELLER";
			String roleDesc = "User Role for Seller";
			userDao.createUserRole(roleName, roleDesc);
		}
	}

	@Override
	public boolean isEmailExist(String email) {
		return userDao.isEmailExist(email);
	}

	@Override
	public boolean isMobileNumberExist(String mobile) {
		return userDao.isMobileNumberExist(mobile);
	}

	@Override
	public boolean validateUser(String email, String password) {
		return userDao.validateUser(email, password);
	}

	@Override
	public UserRegister getUserDetails(String email) {
		UserRegister user = new UserRegister();
		user = userDao.getUserDetails(email);
		return user;
	}

	@Override
	public void updateUserBalance(Map<String, Double> newBalance) {
		
		userDao.updateUserBalance(newBalance);
	}

}
