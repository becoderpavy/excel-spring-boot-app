package com.becoder.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.becoder.model.ProductDtls;

import jakarta.servlet.http.HttpServletResponse;

public interface ExcelService {

	public List<ProductDtls> importExcel(MultipartFile file) throws Exception;

	public void generateExcel(List<ProductDtls> products, HttpServletResponse response);

}
