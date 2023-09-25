package com.example.easyShop.dao;

import java.util.Map;

import com.example.easyShop.model.UserRegister;

public interface UserDao {

	public void registerUser(UserRegister user);
	
	public void createUserRole(String roleName, String roleDesc);
	
	public boolean isEmailExist(String email);
	
	public boolean isMobileNumberExist(String mobile);

	public boolean validateUser(String email, String password);

	public UserRegister getUserDetails(String email);

	public void updateUserBalance(Map<String, Double> newBalance);
}
