package com.becoder.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.model.ProductDtls;

public interface ProductRepo extends JpaRepository<ProductDtls, Integer> {

}
