package com.example.easyShop.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.easyShop.model.Product;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface ProductDao {

	boolean saveNewProduct(Product product, String user);

	List<Product> getAllProductListedBySeller(String user);

	boolean deleteProductById(Long id);

	Product getProductDtlByProductId(Long id);

	boolean updateProductDtl(Product productDtl);

	List<Product> getAllProductsListedForSell();

	void updateProductStockUnit(Product product);

}
