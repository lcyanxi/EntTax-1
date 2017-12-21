package com.douguo.dc.mall.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.douguo.dc.util.excel.AbstractPOIExcelView;
import com.douguo.dc.util.excel.SpringExcelUtil;
import com.douguo.dc.util.excel.XlsxSpringExcelUtil;

import java.math.BigDecimal;
import java.net.URLEncoder;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 生成excel视图，可用excel工具打开或者保存 由ViewController的return new ModelAndView(viewExcel,
 * model)生成
 */
public class MallSingleProductStatExcelView extends AbstractPOIExcelView{

	@Override
	protected Workbook createWorkbook() {
		XSSFWorkbook workbook = new XSSFWorkbook() ;
		return workbook;
	}
	
    @SuppressWarnings("unchecked")
	public void buildExcelDocument(Map model, XSSFWorkbook workbook, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {

        // 设置文件名称
        String excelName = "点击商品统计列表.xlsx";
        // 设置sheet名称
        String sheetName = "点击商品统计列表";

        // 设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelName, "UTF-8"));

        // Excel表头
        XSSFSheet sheet = workbook.createSheet(sheetName);

        // 产生标题列
        String[] headerNames = new String[]{"团品id", "团品名称", "浏览数", "UV数", "下单数", "下单率", "成交数", "成交率", "商品数",
                "总支付金额", "统计日期"};
        XlsxSpringExcelUtil excelUtil = new XlsxSpringExcelUtil();
		excelUtil.setHeader(sheet, headerNames);

        // 填充数据
		List<Map<String, Object>> stuList = (List<Map<String, Object>>) model.get("rowlst");
		
        String[] headerNamesTitle = new String[]{"tuan_id", "tuan_name", "clicks", "uids", "orders", "orderRate", "pays", "payRate", "goods",
                "moneys", "statdate"};
		for (Map<String, Object> map : stuList) {
			if (map != null) {
				String[] data = new String[headerNamesTitle.length];
				for(int j=0; j<headerNamesTitle.length; j++){
					data[j] = String.valueOf(map.get(headerNamesTitle[j])) ;
				}
				excelUtil.addRecord(sheet, data);
			}
		}
        
    }

}