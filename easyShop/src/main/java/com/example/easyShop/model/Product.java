package com.example.easyShop.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Product {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;
	private String productName;
	private String productDescription;
	private double productsellPrice;
	private double productcostPrice;
	private int stockUnit;
	private String productImage;
	private String seller;
	private int buyUnits;
	private Date purchaseDate;
	
	
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public double getProductsellPrice() {
		return productsellPrice;
	}
	public void setProductsellPrice(double productsellPrice) {
		this.productsellPrice = productsellPrice;
	}
	public double getProductcostPrice() {
		return productcostPrice;
	}
	public void setProductcostPrice(double productcostPrice) {
		this.productcostPrice = productcostPrice;
	}
	public int getStockUnit() {
		return stockUnit;
	}
	public void setStockUnit(int stockUnit) {
		this.stockUnit = stockUnit;
	}
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public int getBuyUnits() {
		return buyUnits;
	}
	public void setBuyUnits(int buyUnits) {
		this.buyUnits = buyUnits;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
}
