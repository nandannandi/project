package com.example.easyShop.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.easyShop.model.Product;
import com.example.easyShop.util.AppUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class ProductPurchaseDaoImpl implements ProductPurchaseDao {

	@Autowired
	EntityManager manager;

	@Override
	public void createPurchaseHistory(Product product, String buyerUser) {
		
		String pcreatePurchaseQueryString = "INSERT INTO USER_PURCHASE\r\n"
				+ "(PURCHASE_DATE, PRODUCT_ID, TOTAL_UNIT, PURCHASE_BY_USER, PURCHASE_FROM_USER)\r\n"
				+ "VALUES(?,?,?,?,?)";
		AppUtil dateUtil = new AppUtil();
		Query queryInstance = manager.createNativeQuery(pcreatePurchaseQueryString);
		queryInstance.setParameter(1, dateUtil.getCurrentDate());
		queryInstance.setParameter(2, product.getProductId());
		queryInstance.setParameter(3, product.getBuyUnits());
		queryInstance.setParameter(4, buyerUser);
		queryInstance.setParameter(5, product.getSeller());
		queryInstance.executeUpdate();
	}

	@Override
	public List<Product> getAllProductsPurchasedByBuyer(String buyerUser) {
		
		String purchaseHistoryQuery = "SELECT T2.PRODUCT_NAME, T2.PRODUCT_SELL_PRICE, T1.TOTAL_UNIT, T1.PURCHASE_DATE, T2.PRODUCT_IMG FROM USER_PURCHASE T1, PRODUCT T2\r\n"
				+ "WHERE T2.PRODUCT_ID = T1.PRODUCT_ID\r\n"
				+ "AND T1.PURCHASE_BY_USER = ?\r\n"
				+ "ORDER BY T1.PURCHASE_DATE DESC";
		
		Query queryInstance = manager.createNativeQuery(purchaseHistoryQuery);
		queryInstance.setParameter(1, buyerUser);
		List<Product> purchasedProductList = new ArrayList<Product>();
		List<Object[]> lstObj = queryInstance.getResultList();
		if (lstObj.size() >= 1) {
			for (Object rows[] : lstObj) {
			Product purchasedProduct = new Product();
			purchasedProduct.setProductName(String.valueOf(rows[0]));
			purchasedProduct.setProductsellPrice(Double.parseDouble(String.valueOf(rows[1])));
			purchasedProduct.setBuyUnits(Integer.parseInt(String.valueOf(rows[2])));
			purchasedProduct.setPurchaseDate((Date) rows[3]);
			purchasedProduct.setProductImage(String.valueOf(rows[4]));
			purchasedProductList.add(purchasedProduct);
			}
		}
		
		return purchasedProductList ;	
	}

	@Override
	public List<Product> getSaleHistoryofSeller(String user) {
		String saleHistoryQuery = "SELECT T2.PRODUCT_NAME, T2.PRODUCT_SELL_PRICE, T1.TOTAL_UNIT, T1.PURCHASE_DATE, T2.PRODUCT_IMG, T2.PRODUCT_COST_PRICE  FROM USER_PURCHASE T1, PRODUCT T2\r\n"
				+ "WHERE T2.PRODUCT_ID = T1.PRODUCT_ID\r\n"
				+ "AND T1.PURCHASE_FROM_USER = ?\r\n"
				+ "ORDER BY T1.PURCHASE_DATE DESC";
		
		Query queryInstance = manager.createNativeQuery(saleHistoryQuery);
		queryInstance.setParameter(1, user);
		List<Product> soldProductList = new ArrayList<Product>();
		List<Object[]> lstObj = queryInstance.getResultList();
		if (lstObj.size() >= 1) {
			for (Object rows[] : lstObj) {
			Product soldProduct = new Product();
			soldProduct.setProductName(String.valueOf(rows[0]));
			soldProduct.setProductsellPrice(Double.parseDouble(String.valueOf(rows[1])));
			soldProduct.setBuyUnits(Integer.parseInt(String.valueOf(rows[2])));
			soldProduct.setPurchaseDate((Date) rows[3]);
			soldProduct.setProductImage(String.valueOf(rows[4]));
			soldProduct.setProductcostPrice(Double.parseDouble(String.valueOf(rows[5])));
			soldProductList.add(soldProduct);
			}
		}
		return soldProductList;
	}
	
}
