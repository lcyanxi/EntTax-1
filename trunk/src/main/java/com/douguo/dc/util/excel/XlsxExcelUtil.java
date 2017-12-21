package com.douguo.dc.util.excel;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * Excel写操作工具类
 * 
 * @author jianfei.zhang
 * @since 2016-09-13
 * 
 */

public class XlsxExcelUtil {

	private static final Logger log = Logger.getLogger(XlsxExcelUtil.class);

	/**
	 * 功能：将XSSFWorkbook写入Excel文件
	 * 
	 * @param workBook
	 * @param absPath
	 *            写入文件的相对路径
	 * @param fileName
	 *            文件名
	 */

	public static void writeWorkbook(XSSFWorkbook workBook, String fileName) {

		FileOutputStream fos = null;

		try {

			fos = new FileOutputStream(fileName);

			workBook.write(fos);

		} catch (Exception e) {

			log.error(new StringBuffer("[").append(e.getMessage()).append("]").append(e.getCause()));

		} finally {

			try {

				if (fos != null) {

					fos.close();

				}

			} catch (IOException e) {

				log.error(new StringBuffer("[").append(e.getMessage()).append("]").append(e.getCause()));

			}

		}

	}

	/**
	 * 
	 * 创建XSSFSheet工作簿
	 * 
	 * @param workBook
	 * @param sheetName
	 * 
	 * @return XSSFSheet
	 */

	public static XSSFSheet createSheet(XSSFWorkbook workBook, String sheetName) {

		XSSFSheet sheet = workBook.createSheet(sheetName);

		sheet.setDefaultColumnWidth(12);

//		sheet.setGridsPrinted(false);

		sheet.setDisplayGridlines(false);

		return sheet;

	}

	/**
	 * 
	 * 创建XSSFRow
	 * 
	 * @param sheet
	 *            sheet
	 * @param rowNum
	 *            行数
	 * @param height
	 *            行高
	 * 
	 * @return HSSFRow
	 */

	public static XSSFRow createRow(XSSFSheet sheet, int rowNum, int height) {

		XSSFRow row = sheet.createRow(rowNum);

		row.setHeight((short) height);

		return row;

	}

	/**
	 * 
	 * 创建CellStyle样式
	 * 
	 * @param workBook
	 * @param backgroundColor
	 *            背景色
	 * @param foregroundColor
	 *            前置色
	 * @param halign
	 *            对齐方式
	 * @param font
	 *            字体
	 * 
	 * @return CellStyle
	 */

	public static CellStyle createCellStyle(XSSFWorkbook workBook, short backgroundColor, short foregroundColor,
			short halign, Font font) {

		CellStyle cs = workBook.createCellStyle();

		cs.setAlignment(halign);

		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		cs.setFillBackgroundColor(backgroundColor);

		cs.setFillForegroundColor(foregroundColor);

		cs.setFillPattern(CellStyle.SOLID_FOREGROUND);

		cs.setFont(font);

		return cs;

	}

	/**
	 * 
	 * 创建带边框的CellStyle样式
	 * 
	 * @param workBook
	 * @param backgroundColor
	 *            背景色
	 * @param foregroundColor
	 *            前置色
	 * @param halign
	 *            对齐方式
	 * @param font
	 *            字体
	 * 
	 * @return CellStyle
	 */

	public static CellStyle createBorderCellStyle(XSSFWorkbook workBook, short backgroundColor, short foregroundColor,
			short halign, Font font) {

		CellStyle cs = workBook.createCellStyle();

		cs.setAlignment(halign);

		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		cs.setFillBackgroundColor(backgroundColor);

		cs.setFillForegroundColor(foregroundColor);

		cs.setFillPattern(CellStyle.SOLID_FOREGROUND);

		cs.setFont(font);

		cs.setBorderLeft(CellStyle.BORDER_DASHED);

		cs.setBorderRight(CellStyle.BORDER_DASHED);

		cs.setBorderTop(CellStyle.BORDER_DASHED);

		cs.setBorderBottom(CellStyle.BORDER_DASHED);

		return cs;

	}

	/**
	 * 
	 * 创建CELL
	 * 
	 * @param row
	 *            行对象
	 * @param cellNum
	 *            cell数量
	 * @param style
	 *            cell样式
	 * 
	 * @return HSSFCell
	 */

	public static XSSFCell createCell(XSSFRow row, int cellNum, CellStyle style) {

		XSSFCell cell = row.createCell(cellNum);

		cell.setCellStyle(style);

		return cell;

	}

	/**
	 * 合并单元格
	 * 
	 * @param sheet
	 * @param firstRow
	 * @param lastRow
	 * @param firstColumn
	 * @param lastColumn
	 * 
	 * @return int 合并区域号码
	 */

	public static int mergeCell(XSSFSheet sheet, int firstRow, int lastRow, int firstColumn, int lastColumn) {

		return sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstColumn, lastColumn));

	}

	/**
	 * 创建字体
	 * 
	 * @param wb
	 * @param boldweight
	 * @param color
	 * 
	 * @return Font
	 */

	public static Font createFont(XSSFWorkbook wb, short boldweight, short color, short size) {

		Font font = wb.createFont();

		font.setBoldweight(boldweight);

		font.setColor(color);

		font.setFontHeightInPoints(size);

		return font;
	}

	/**
	 * 
	 * 设置合并单元格的边框样式
	 * 
	 * @param sheet
	 * @param cra
	 * @param style
	 */
//	public static void setRegionStyle(XSSFSheet sheet, CellRangeAddress cra, CellStyle style) {
//		for (int i = cra.getFirstRow(); i <= cra.getLastRow(); i++) {
//			XSSFRow row = XSSFCellUtil.getRow(i, sheet);
//			for (int j = cra.getFirstColumn(); j <= cra.getLastColumn(); j++) {
//				XSSFCell cell = XSSFCellUtil.getCell(row, j);
//				cell.setCellStyle(style);
//			}
//		}
//	}
}