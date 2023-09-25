package com.example.easyShop.service;

import java.util.List;

import com.example.easyShop.model.Product;

public interface ProductService {

	public boolean saveNewProduct(Product product, String user);

	public List<Product> getAllProductListedBySeller(String user);

	public boolean deleteProductById(Long id);

	public Product getProductDtlByProductId(Long id);

	public boolean updateProductDtl(Product productDtl);

	public List<Product> getAllProductListedForSell();

	public void updateProductStockUnit(Product product);
}
