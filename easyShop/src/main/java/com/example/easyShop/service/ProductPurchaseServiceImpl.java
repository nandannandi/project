package com.example.easyShop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.easyShop.dao.ProductPurchaseDao;
import com.example.easyShop.model.Product;
import com.example.easyShop.model.UserRegister;

@Service
public class ProductPurchaseServiceImpl implements ProductPurchaseService {

	@Autowired
	UserService userService;
	
	@Autowired ProductService productService;
	
	@Autowired
	ProductPurchaseDao productPurchaseDao;
	
	@Override
	public void purchaseProductAndPerformCalculation(Product product, String buyerUser) {
		
		String buyer = buyerUser;
		String seller = product.getSeller();
		Map<String,Double> newBalance = new HashMap<String, Double>();
		newBalance = calculateNewBalanceOfBuyerAndSeller(product, buyer, seller);
		userService.updateUserBalance(newBalance);
		productService.updateProductStockUnit(product);
		createPurchaseHistory(product,buyerUser);
		
	}

	private void createPurchaseHistory(Product product, String buyerUser) {
		
		productPurchaseDao.createPurchaseHistory(product, buyerUser);
	}

	private Map<String, Double> calculateNewBalanceOfBuyerAndSeller(Product product, String buyer, String seller) {
		
		UserRegister buyerUser = userService.getUserDetails(buyer);
		UserRegister sellerUser = userService.getUserDetails(seller);
		Map<String, Double> returnBalance = new HashMap<String, Double>();
		if(buyerUser != null && sellerUser != null && buyerUser.getBalance() != null && sellerUser.getBalance() != null) {
			Double buyerNewBalance = buyerUser.getBalance() - (product.getProductsellPrice()*product.getBuyUnits());
			Double sellerNewBalance = sellerUser.getBalance() + (product.getProductsellPrice()*product.getBuyUnits());
			returnBalance.put(buyerUser.getEmail(), buyerNewBalance);
			returnBalance.put(sellerUser.getEmail(), sellerNewBalance);
		}
		return returnBalance;
	}

	@Override
	public List<Product> getAllProductsPurchasedByBuyer(String user) {
		return productPurchaseDao.getAllProductsPurchasedByBuyer(user);
	}

	@Override
	public List<Product> getSaleHistoryOfSeller(String user) {
		return productPurchaseDao.getSaleHistoryofSeller(user);
	}

}
