package com.douguo.dc.util.excel;

import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * 
 * Excel写操作工具类
 * 
 * @author yaozhou.zhang
 * @since 2015-05-20
 * 
 */

public class SpringExcelUtil {

	public void setHeader(HSSFSheet sheet, String[] header) {
		fillContent(sheet, header, 0, null);
	}

	public void setHeader(HSSFSheet sheet, String[] header, HSSFCellStyle style) {
		fillContent(sheet, header, 0, style);
	}

	public void addRecord(HSSFSheet sheet, String[] record) {
		//
		fillContent(sheet, record, sheet.getLastRowNum() + 1, null);
	}
	
	public void addRecord(HSSFSheet sheet, String[] record,HSSFCellStyle style) {
		//
		fillContent(sheet, record, sheet.getLastRowNum() + 1, style);
	}

	public void addRecord(HSSFSheet sheet, ArrayList<String[]> arr) {
		for (String[] row : arr) {
			this.addRecord(sheet, row);
		}
	}

	public void addRecord(HSSFSheet sheet, String[][] records) {
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
	private void fillContent(HSSFSheet sheet, String[] crow, int rowNum, HSSFCellStyle style) {
		HSSFRow row = sheet.createRow(rowNum);

		for (int i = 0; i < crow.length; i++) {
			String s = crow[i];
			HSSFCell cell = row.createCell(i);
			if (style != null) {
				cell.setCellStyle(style);
			}
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(s);
		}
	}
}