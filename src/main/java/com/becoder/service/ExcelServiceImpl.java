package com.becoder.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.weaver.ast.Instanceof;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.model.ProductDtls;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ExcelServiceImpl implements ExcelService {

	@Override
	public List<ProductDtls> importExcel(MultipartFile file) throws Exception {

		List<ProductDtls> productList = new ArrayList<>();

		if (file.isEmpty()) {
			throw new IllegalArgumentException("Please import file");
		}

		if (!file.isEmpty() && !file.getOriginalFilename().endsWith(".xlsx")) {
			throw new IllegalArgumentException("invalid excel file");
		}

		InputStream ios = file.getInputStream();
		XSSFWorkbook workbook = new XSSFWorkbook(ios);
		XSSFSheet sheet = workbook.getSheetAt(0);

		for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = sheet.getRow(i);

			if (row == null)
				continue;

			String category = row.getCell(0).getStringCellValue();
			String productName = row.getCell(1).getStringCellValue();
			Integer quantity = (int) row.getCell(2).getNumericCellValue();
			double price = row.getCell(3).getNumericCellValue();
			double totalPrice = row.getCell(4).getNumericCellValue();

			ProductDtls product = new ProductDtls();
			product.setCategory(category);
			product.setProductName(productName);
			product.setQuantity(quantity);
			product.setPrice(price);
			product.setTotalPrice(totalPrice);
			productList.add(product);
		}
		workbook.close();

		return productList;
	}

	private String[] header = { "Category", "Product Name", "Quantity", "Price", "Total Price" };

	@Override
	public void generateExcel(List<ProductDtls> products, HttpServletResponse response) {
		ServletOutputStream outputStream = null;
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			XSSFSheet sheet = workbook.createSheet("Product Details");

			XSSFCellStyle headerStyle = workbook.createCellStyle();
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setFontHeight(12);
			headerStyle.setFont(font);

			XSSFCellStyle dataStyle = workbook.createCellStyle();
			XSSFFont font2 = workbook.createFont();
			font2.setBold(false);
			font2.setFontHeight(10);
			headerStyle.setFont(font);

			int rowCount = 0;
			// Header Creation
			XSSFRow headerRow = sheet.createRow(rowCount);
			createCell(sheet, headerRow, header, headerStyle);

			// Data Creation
			for (ProductDtls product : products) {
				rowCount++;
				XSSFRow row = sheet.createRow(rowCount);
				Object[] cellValue = { product.getCategory(), product.getProductName(), product.getQuantity(),
						product.getPrice(), product.getTotalPrice() };

				createCell(sheet, row, cellValue, dataStyle);
			}

			outputStream = response.getOutputStream();
			workbook.write(outputStream);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void createCell(XSSFSheet sheet, XSSFRow row, Object[] data, XSSFCellStyle style) {

		for (int i = 0; i < data.length; i++) {
			sheet.autoSizeColumn(i);
			// create cell
			XSSFCell cell = row.createCell(i);
			Object dataValue = data[i];

			if (dataValue instanceof String) {
				cell.setCellValue((String) dataValue);
			} else if (dataValue instanceof Integer) {
				cell.setCellValue((Integer) dataValue);
			} else if (dataValue instanceof Long) {
				cell.setCellValue((Long) dataValue);
			} else if (dataValue instanceof Boolean) {
				cell.setCellValue((Boolean) dataValue);
			} else {
				cell.setCellValue(cell != null ? dataValue.toString() : "");
			}
			cell.setCellStyle(style);
		}

	}

}
