package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.model.ProductDtls;
import com.becoder.service.ExcelService;
import com.becoder.service.ProductService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ExcelController {

	@Autowired
	private ExcelService excelService;

	@Autowired
	private ProductService productService;

	@PostMapping("/import-excel")
	public ResponseEntity<?> importExcel(@RequestParam MultipartFile file) {
		try {
			List<ProductDtls> importExcelProducts = excelService.importExcel(file);
			Boolean saveProduct = productService.saveProduct(importExcelProducts);
			if (saveProduct) {
				return new ResponseEntity<>("upload success", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("upload failed ! please try again", HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/download-excel")
	public void generateExcel(HttpServletResponse response) {
		try {
			List<ProductDtls> products = productService.getAllProducts();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("content-Disposition", "attachment;filename=product_dtls.xlsx");
			excelService.generateExcel(products, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
