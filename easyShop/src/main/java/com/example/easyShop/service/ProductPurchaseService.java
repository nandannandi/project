package com.example.easyShop.service;

import java.util.List;

import com.example.easyShop.model.Product;

public interface ProductPurchaseService {

	void purchaseProductAndPerformCalculation(Product product, String buyerUser);

	List<Product> getAllProductsPurchasedByBuyer(String user);

	List<Product> getSaleHistoryOfSeller(String user);

}
