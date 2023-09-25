package com.example.easyShop.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.easyShop.model.Product;
import com.example.easyShop.model.UserRegister;
import com.example.easyShop.util.AppUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class ProductDaoImpl implements ProductDao {

	@Autowired
	EntityManager manager;

	@Override
	public boolean saveNewProduct(Product product, String user) {

		String insertNewProductQuery = "INSERT INTO PRODUCT (PRODUCT_NAME,PRODUCT_DESC,PRODUCT_IMG,PRODUCT_SELL_PRICE,"
				+ "PRODUCT_COST_PRICE,STOCK_UNIT,CREATED_BY_USER,CREATED_DATE) VALUES (?,?,?,?,?,?,?,?)";

		Query queryInstance = manager.createNativeQuery(insertNewProductQuery);
		AppUtil util = new AppUtil();
		queryInstance.setParameter(1, product.getProductName());
		queryInstance.setParameter(2, product.getProductDescription());
		queryInstance.setParameter(3, product.getProductImage());
		queryInstance.setParameter(4, product.getProductsellPrice());
		queryInstance.setParameter(5, product.getProductcostPrice());
		queryInstance.setParameter(6, product.getStockUnit());
		queryInstance.setParameter(7, user);
		queryInstance.setParameter(8, util.getCurrentDate());

		int count = queryInstance.executeUpdate();
		if (count > 0)
			return true;
		else
			return false;
	}

	@Override
	public List<Product> getAllProductListedBySeller(String user) {

		String productListQueryString = "SELECT PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESC, PRODUCT_IMG, PRODUCT_SELL_PRICE,"
				+ "PRODUCT_COST_PRICE, STOCK_UNIT FROM PRODUCT T WHERE T.CREATED_BY_USER = ?" + "AND T.IS_DELETED = ?";

		Query queryInstance = manager.createNativeQuery(productListQueryString);
		queryInstance.setParameter(1, user);
		queryInstance.setParameter(2, 1);

		List<Object[]> lstObj = queryInstance.getResultList();
		List<Product> productList = new ArrayList<Product>();
		if (lstObj.size() > 0) {
			for (Object rows[] : lstObj) {
				Product product = new Product();
				product.setProductId(Long.parseLong(String.valueOf(rows[0])));
				product.setProductName(String.valueOf(rows[1]));
				product.setProductDescription(String.valueOf(rows[2]));
				product.setProductImage(String.valueOf(rows[3]));
				product.setProductsellPrice(Double.parseDouble(String.valueOf(rows[4])));
				product.setProductcostPrice(Double.parseDouble(String.valueOf(rows[5])));
				product.setStockUnit(Integer.parseInt(String.valueOf(rows[6])));
				productList.add(product);
			}
		}

		return productList;
	}

	@Override
	public boolean deleteProductById(Long id) {

		String queryString = "UPDATE PRODUCT T SET T.IS_DELETED = ? WHERE T.PRODUCT_ID = ?";
		Query queryInstance = manager.createNativeQuery(queryString);
		queryInstance.setParameter(1, 0);
		queryInstance.setParameter(2, id.longValue());
		int updateCount = queryInstance.executeUpdate();
		if (updateCount > 0)
			return true;
		else
			return false;
	}

	@Override
	public Product getProductDtlByProductId(Long id) {

		String productQueryString = "SELECT PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESC, PRODUCT_IMG, PRODUCT_SELL_PRICE,\r\n"
				+ "PRODUCT_COST_PRICE, STOCK_UNIT, T.CREATED_BY_USER FROM PRODUCT T WHERE T.PRODUCT_ID = ?\r\n" + "AND T.IS_DELETED = ?";

		Query queryInstance = manager.createNativeQuery(productQueryString);
		queryInstance.setParameter(1, id);
		queryInstance.setParameter(2, 1);

		List<Object[]> lstObj = queryInstance.getResultList();
		Product product = new Product();
		if (lstObj.size() > 0) {
			for (Object rows[] : lstObj) {
				product.setProductId(Long.parseLong(String.valueOf(rows[0])));
				product.setProductName(String.valueOf(rows[1]));
				product.setProductDescription(String.valueOf(rows[2]));
				product.setProductImage(String.valueOf(rows[3]));
				product.setProductsellPrice(Double.parseDouble(String.valueOf(rows[4])));
				product.setProductcostPrice(Double.parseDouble(String.valueOf(rows[5])));
				product.setStockUnit(Integer.parseInt(String.valueOf(rows[6])));
				product.setSeller(String.valueOf(rows[7]));
			}
		}

		return product;
	}

	@Override
	public boolean updateProductDtl(Product productDtl) {

		String queryStringUpdatePrdDtl = "UPDATE PRODUCT T\r\n" + "SET T.PRODUCT_DESC = ?,\r\n"
				+ "T.PRODUCT_SELL_PRICE = ?,\r\n" + "T.PRODUCT_COST_PRICE = ?,\r\n" + "T.STOCK_UNIT = ?,\r\n"
				+ "T.PRODUCT_IMG = ?\r\n"
				+ "WHERE T.PRODUCT_ID = ?";

		Query queryInstance = manager.createNativeQuery(queryStringUpdatePrdDtl);
		queryInstance.setParameter(1, productDtl.getProductDescription());
		queryInstance.setParameter(2, productDtl.getProductsellPrice());
		queryInstance.setParameter(3, productDtl.getProductcostPrice());
		queryInstance.setParameter(4, productDtl.getStockUnit());
		queryInstance.setParameter(5, productDtl.getProductImage());
		queryInstance.setParameter(6, productDtl.getProductId());

		int count = queryInstance.executeUpdate();
		if (count > 0)
			return true;
		else
			return false;
	}

	@Override
	public List<Product> getAllProductsListedForSell() {
		String productListQueryString = "SELECT PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESC, PRODUCT_IMG, PRODUCT_SELL_PRICE,"
				+ "PRODUCT_COST_PRICE, STOCK_UNIT, CREATED_BY_USER FROM PRODUCT T WHERE T.STOCK_UNIT > ? AND T.IS_DELETED = ?";

		Query queryInstance = manager.createNativeQuery(productListQueryString);
		queryInstance.setParameter(1, 0);
		queryInstance.setParameter(2, 1);

		List<Object[]> lstObj = queryInstance.getResultList();
		List<Product> productList = new ArrayList<Product>();
		if (lstObj.size() > 0) {
			for (Object rows[] : lstObj) {
				Product product = new Product();
				product.setProductId(Long.parseLong(String.valueOf(rows[0])));
				product.setProductName(String.valueOf(rows[1]));
				product.setProductDescription(String.valueOf(rows[2]));
				product.setProductImage(String.valueOf(rows[3]));
				product.setProductsellPrice(Double.parseDouble(String.valueOf(rows[4])));
				product.setProductcostPrice(Double.parseDouble(String.valueOf(rows[5])));
				product.setStockUnit(Integer.parseInt(String.valueOf(rows[6])));
				product.setSeller(String.valueOf(rows[7]));
				productList.add(product);
			}
		}

		return productList;
	}

	@Override
	public void updateProductStockUnit(Product product) {
		
		String updateStockString = "UPDATE PRODUCT T SET T.STOCK_UNIT = ?\r\n"
				+ "WHERE T.PRODUCT_ID = ?";
		Query queryInstance = manager.createNativeQuery(updateStockString);
		queryInstance.setParameter(1, product.getStockUnit());
		queryInstance.setParameter(2, product.getProductId());
		queryInstance.executeUpdate();
	}

}
