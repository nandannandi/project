package com.example.easyShop.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.easyShop.model.UserRegister;
import com.example.easyShop.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

	Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
	
	@Autowired
	EntityManager manager;
	
	@Override
	public void registerUser(UserRegister user) {
		
		
		logger.info("Inside registerUser DAO method");
		String insertUserQueryString = "INSERT INTO User (FIRST_NAME,LAST_NAME,DOB,EMAIL,MOBILE,BALANCE,PASSWORD,ADDRESS,USER_ROLE)\r\n"
				+ "VALUES(?,?,?,?,?,?,?,?,?)";
		
		logger.info(insertUserQueryString);
		
		Query queryInstance = manager.createNativeQuery(insertUserQueryString);
		queryInstance.setParameter(1, user.getFirstName());
		queryInstance.setParameter(2, user.getLastName());
		queryInstance.setParameter(3, user.getDob());
		queryInstance.setParameter(4, user.getEmail());
		queryInstance.setParameter(5, user.getMobile());
		queryInstance.setParameter(6, user.getBalance());
		queryInstance.setParameter(7, user.getPassword());
		queryInstance.setParameter(8, user.getAddress());
		queryInstance.setParameter(9, user.getUserType());
		
		queryInstance.executeUpdate();
		logger.info("Insert query executed");
		
	}

	@Override
	public void createUserRole(String roleName, String roleDesc) {
		
		logger.info("Inside registerUser DAO method");
		String insertUserRoleQueryString = "INSERT INTO User_Role (ROLE_NAME, ROLE_DESC)\r\n"
				+ "VALUES(?,?)";
		
		logger.info("Query is " + insertUserRoleQueryString);
		
		Query queryInstance = manager.createNativeQuery(insertUserRoleQueryString);
		queryInstance.setParameter(1, roleName);
		queryInstance.setParameter(2, roleDesc);
		
		queryInstance.executeUpdate();
		logger.info("Insert query executed");
	}

	@Override
	public boolean isEmailExist(String email) {
		
		String queryString = "SELECT EMAIL FROM User WHERE EMAIL = ?";
		Query queryInstance = manager.createNativeQuery(queryString);
		queryInstance.setParameter(1, email);
		List<Object[]> lstObj = queryInstance.getResultList();
		if (lstObj.size() > 0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean isMobileNumberExist(String mobile) {
		
		String queryString = "SELECT MOBILE FROM User WHERE MOBILE = ?";
		Query queryInstance = manager.createNativeQuery(queryString);
		queryInstance.setParameter(1, mobile);
		List<Object[]> lstObj = queryInstance.getResultList();
		if (lstObj.size() > 0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean validateUser(String email, String password) {
		
		String queryString = "SELECT EMAIL FROM User where EMAIL = ? AND PASSWORD = ?";
		Query queryInstance = manager.createNativeQuery(queryString);
		queryInstance.setParameter(1, email);
		queryInstance.setParameter(2, password);
		List<Object[]> lstObj = queryInstance.getResultList();
		if (lstObj.size() > 0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public UserRegister getUserDetails(String email) {
		
		String queryString = "SELECT T.FIRST_NAME, T.LAST_NAME, T.EMAIL, T.MOBILE, T.DOB, T.USER_ROLE, T.BALANCE FROM User T where T.EMAIL = ?";
		Query queryInstance = manager.createNativeQuery(queryString);
		queryInstance.setParameter(1, email);
		List<Object[]> lstObj = queryInstance.getResultList();
		UserRegister user = new UserRegister();
		if (lstObj.size() >= 1) {
			Object rows[] = lstObj.get(0);
			user.setFirstName(String.valueOf(rows[0]));
			user.setLastName(String.valueOf(rows[1]));
			user.setEmail(String.valueOf(rows[2]));
			user.setMobile(String.valueOf(rows[3]));
			user.setDob((Date) rows[4]);
			user.setUserType(String.valueOf(rows[5]));
			user.setBalance(Double.parseDouble(String.valueOf(rows[6])));
		}
		return user;
	}

	@Override
	public void updateUserBalance(Map<String, Double> newBalance) {
		
		String updateBalanceQueryString = "UPDATE User T SET T.BALANCE = ?\r\n"
				+ "WHERE T.EMAIL = ?";
		Iterator<Entry<String, Double>> itr = newBalance.entrySet().iterator();
		while(itr.hasNext()) {
			Query queryInstance = manager.createNativeQuery(updateBalanceQueryString);
			Map.Entry<String, Double> entry = itr.next();
			queryInstance.setParameter(1, entry.getValue());
			queryInstance.setParameter(2, entry.getKey());
			queryInstance.executeUpdate();
		}
	}

}
