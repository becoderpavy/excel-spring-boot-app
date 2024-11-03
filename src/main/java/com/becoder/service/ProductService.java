package com.becoder.service;

import java.util.List;

import com.becoder.model.ProductDtls;

public interface ProductService {

	public Boolean saveProduct(List<ProductDtls> productDtls);

	public List<ProductDtls> getAllProducts();

}
