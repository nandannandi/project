package com.example.easyShop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.easyShop.dao.ProductDao;
import com.example.easyShop.model.Product;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductDao productDao;
	
	@Override
	public boolean saveNewProduct(Product product, String user) {
		boolean isProductSaved = productDao.saveNewProduct(product, user);
		return isProductSaved;
	}

	@Override
	public List<Product> getAllProductListedBySeller(String user) {
		
		return productDao.getAllProductListedBySeller(user);
	}

	@Override
	public boolean deleteProductById(Long id) {
		
		return productDao.deleteProductById(id);
	}

	@Override
	public Product getProductDtlByProductId(Long id) {
		return productDao.getProductDtlByProductId(id);
	}

	@Override
	public boolean updateProductDtl(Product productDtl) {
		return productDao.updateProductDtl(productDtl);
	}

	@Override
	public List<Product> getAllProductListedForSell() {
		return productDao.getAllProductsListedForSell();
	}

	@Override
	public void updateProductStockUnit(Product product) {
		int newStockUnit = product.getStockUnit() - product.getBuyUnits();
		product.setStockUnit(newStockUnit);
		productDao.updateProductStockUnit(product);
	}

}
