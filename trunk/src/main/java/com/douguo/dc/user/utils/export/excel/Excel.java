package com.douguo.dc.user.utils.export.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Excel {

	HSSFWorkbook wb = new HSSFWorkbook();
	List<ExcelSheet> sheets = new ArrayList<ExcelSheet>();

	private HSSFCellStyle titleStyle;

	/**
	 * 
	 * 
	 * @param fileName 文件全路径名
	 */
	public Excel() {
		this("", false);
	}

	/**
	 * 
	 * 
	 * @param fileName 文件全路径名
	 */
	public Excel(String fileName) {
		this(fileName, true);
	}

	/**
	 * 输入文件全路径，如果不存在就创建新文件
	 * 
	 * @param fileName
	 * @param override 是否重写原文件
	 */
	public Excel(String fileName, boolean override) {
		File f = new File(fileName);
		if (override) {
			// 如果存在就删除
			f.delete();
		}
		try {
			if (f.exists()) {
				wb = new HSSFWorkbook(new FileInputStream(fileName));
			} else {
				wb = new HSSFWorkbook();
			}
			titleStyle = wb.createCellStyle();
			HSSFFont titleFont = wb.createFont();
			//titleFont.setColor(HSSFFont.COLOR_RED);
			titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			titleStyle.setFont(titleFont);

			// create the Excel file.
			int num = wb.getNumberOfSheets();
			for (int i = 0; i < num; i++) {
				HSSFSheet sheet = wb.getSheetAt(i);
				String name = wb.getSheetName(i);
				sheets.add(new ExcelSheet(name, sheet, titleStyle));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param sheetName 
	 * @return
	 */
	public ExcelSheet getSheet(String sheetName) {
		for (ExcelSheet esh : this.sheets) {
			if (esh.getName().equalsIgnoreCase(sheetName)) {
				return esh;
			}
		}
		HSSFSheet sheet = wb.createSheet(sheetName);
		return new ExcelSheet(sheetName, sheet, titleStyle);
	}

	//写本地文件
	public void save(String fileName) {
		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//直接流输出
	public void writeExcel(OutputStream output) {
		try {
			output.flush();
			wb.write(output);
			output.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	public static void main(String[] argv) {
		String fileName = "E:/test.xls";//文件名
		String sheetName = "报表";//sheet名
		String[] arryHeader = new String[] { "用户名", "数量", "姓名", "日期", "其他" };
		//String[] arryData = new String[] { "yaozhou.zhang", "123124124124,123123", "张要周", "2012-07-25", "other。。。" };
		String[][] arryData = {
				new String[] { "yaozhou.zhang", "123124124124,123123", "张要周", "2012-07-25", "other。。。" },
				{ "yaozhou.zhang", "123124124124,123123", "张要周", "2012-07-25", "other。。。" } };

		Excel excel = new Excel(fileName);
		ExcelSheet eSheet = excel.getSheet(sheetName);
		eSheet.setHeader(arryHeader);
		eSheet.addRecord(arryData);
		excel.save(fileName);
	}
}
