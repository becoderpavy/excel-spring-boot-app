package com.becoder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.becoder.model.ProductDtls;
import com.becoder.repo.ProductRepo;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepo productRepo;

	@Override
	public Boolean saveProduct(List<ProductDtls> productDtls) {
		List<ProductDtls> saveAll = productRepo.saveAll(productDtls);
		if (!CollectionUtils.isEmpty(saveAll))
			return true;
		return false;
	}

	@Override
	public List<ProductDtls> getAllProducts() {
		return productRepo.findAll();
	}

}
