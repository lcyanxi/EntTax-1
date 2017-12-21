package com.douguo.dc.util.excel;

import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * 
 * Excel写操作工具类
 * 
 * @author jianfei.zhang
 * @since 2016-09-13
 * 
 */

public class XlsxSpringExcelUtil {

	public void setHeader(XSSFSheet sheet, String[] header) {
		fillContent(sheet, header, 0, null);
	}

	public void setHeader(XSSFSheet sheet, String[] header, XSSFCellStyle style) {
		fillContent(sheet, header, 0, style);
	}

	public void addRecord(XSSFSheet sheet, String[] record) {
		//
		fillContent(sheet, record, sheet.getLastRowNum() + 1, null);
	}
	
	public void addRecord(XSSFSheet sheet, String[] record,XSSFCellStyle style) {
		//
		fillContent(sheet, record, sheet.getLastRowNum() + 1, style);
	}

	public void addRecord(XSSFSheet sheet, ArrayList<String[]> arr) {
		for (String[] row : arr) {
			this.addRecord(sheet, row);
		}
	}

	public void addRecord(XSSFSheet sheet, String[][] records) {
		for (String[] row : records) {
			this.addRecord(sheet, row);
		}
	}

	/**
	 * 填充内容
	 * 
	 * @param crow
	 * @param rowNum
	 * @param style
	 */
	private void fillContent(XSSFSheet sheet, String[] crow, int rowNum, XSSFCellStyle style) {
		XSSFRow row = sheet.createRow(rowNum);

		for (int i = 0; i < crow.length; i++) {
			String s = crow[i];
			XSSFCell cell = row.createCell(i);
			if (style != null) {
				cell.setCellStyle(style);
			}
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(s);
		}
	}
}