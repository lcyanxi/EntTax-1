package com.zyz.open.hiveadmin.web;

import com.douguo.dc.util.excel.AbstractPOIExcelView;
import com.douguo.dc.util.excel.XlsxSpringExcelUtil;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 生成excel视图，可用excel工具打开或者保存 由ViewController的return new ModelAndView(viewExcel,
 * model)生成
 * XSSFWorkbook
 */
public class HiveAdminStatDateExportExcelView extends AbstractPOIExcelView{

	@Override
	protected Workbook createWorkbook() {
		XSSFWorkbook workbook = new XSSFWorkbook() ;
		return workbook;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// 设置文件名称
		String jobName = model.get("exclName").toString() ;
		String excelName = jobName+".xlsx";
		
		// 设置sheet名称
		String sheetName = jobName;
		
		// 设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelName, "UTF-8"));

		// Excel表头
		XSSFSheet sheet = workbook.createSheet(sheetName);

		// 产生标题列
		List<String> headerNamesList = new ArrayList<String>() ;
		headerNamesList = (List<String>) model.get("colTitleList") ;
		String[] headerNames = new String[headerNamesList.size()] ;
		for(int i=0; i<headerNamesList.size(); i++){
			headerNames[i] = headerNamesList.get(i) ;
		}
		XlsxSpringExcelUtil excelUtil = new XlsxSpringExcelUtil();
		excelUtil.setHeader(sheet, headerNames);

		//
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("mm/dd/yyyy"));

		// 填充数据
		List<Map<String, Object>> stuList = (List<Map<String, Object>>) model.get("list");
		for (Map<String, Object> map : stuList) {
			if (map != null) {
				String[] data = new String[map.size()];
				for(int j=0; j<map.size(); j++){
					data[j] = String.valueOf(map.get(String.valueOf(j))) ;
				}
				excelUtil.addRecord(sheet, data);
			}
		}
		
	}

	

}