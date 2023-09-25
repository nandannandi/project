package com.example.easyShop.dao;

import java.util.List;

import com.example.easyShop.model.Product;

public interface ProductPurchaseDao {

	void createPurchaseHistory(Product product, String buyerUser);

	List<Product> getAllProductsPurchasedByBuyer(String user);

	List<Product> getSaleHistoryofSeller(String user);

}
