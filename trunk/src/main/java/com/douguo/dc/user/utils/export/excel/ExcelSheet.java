package com.douguo.dc.user.utils.export.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author yaozhou.zhang
 * @date 2012-07-25
 */
public class ExcelSheet {
	private HSSFSheet sheet;

	private String name;

	//标题栏
	private String[] header;

	private HSSFCellStyle titleStyle;

	public ExcelSheet(String sheetName, HSSFSheet sh) {
		this.name = sheetName;
		this.sheet = sh;
		sheet.setDisplayGridlines(true);
	}

	public ExcelSheet(String sheetName, HSSFSheet sh, HSSFCellStyle titleStyle) {
		this.name = sheetName;
		this.sheet = sh;
		this.titleStyle = titleStyle;
	}

	public void addRecord(String[] record) {
		if (header != null) {
			if (header.length != record.length) {
				//return;
			}
		}
		fillContent(record, sheet.getLastRowNum() + 1, null);
	}

	public String[] getHeader() {
		return this.header;
	}

	public void setHeader(String[] header) {
		this.header = header;
		fillContent(header, 0, this.titleStyle);
	}

	public void setHeader(String[] header, HSSFCellStyle titleStyle) {
		this.header = header;
		fillContent(header, 0, titleStyle);
	}

	/**
	 * 填充内容
	 * 
	 * @param crow
	 * @param rowNum
	 * @param style
	 */
	private void fillContent(String[] crow, int rowNum, HSSFCellStyle style) {
		HSSFRow row = sheet.createRow(rowNum);

		for (int i = 0; i < crow.length; i++) {
			String s = crow[i];
			HSSFCell cell = row.createCell(i);
			if (style != null) {
				cell.setCellStyle(style);
			}
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(s);
		}
	}

	public void setValue(int rowNum, int colNum, String value) {
		HSSFRow row = this.sheet.getRow(rowNum);
		HSSFCell cell = row.getCell(colNum);
		if (cell == null) {
			cell = row.createCell(colNum);
		}
		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue(value);
	}

	public String getName() {
		return this.name;
	}

	public void addRecord(ArrayList<String[]> arr) {
		for (String[] row : arr) {
			this.addRecord(row);
		}
	}

	public void addRecord(String[][] records) {
		for (String[] row : records) {
			this.addRecord(row);
		}
	}

	public List<String[]> getRecords() {
		ArrayList<String[]> vs = new ArrayList<String[]>();
		for (int j = 0; j < this.sheet.getLastRowNum(); j++) {
			HSSFRow row = this.sheet.getRow(j);
			ArrayList<String> cellsStr = new ArrayList<String>();
			for (int k = 0; k < row.getLastCellNum(); k++) {
				HSSFCell cell = row.getCell(k);
				if (cell != null) {
					cellsStr.add(cell.getStringCellValue());
				}
			}
			vs.add(cellsStr.toArray(new String[cellsStr.size()]));
		}
		return vs;
	}
}