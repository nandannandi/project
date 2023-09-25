package com.example.easyShop.model;

public class BuyProduct {

	private long prodId;
	private int purchaseUnits;
	
	public int getBuyUnits() {
		return purchaseUnits;
	}
	public void setBuyUnits(int buyUnits) {
		this.purchaseUnits = buyUnits;
	}
	public long getProdId() {
		return prodId;
	}
	public void setProdId(long prodId) {
		this.prodId = prodId;
	}
}
