package com.douguo.crm.web;

import com.douguo.crm.model.VIPUserNew;
import com.douguo.dc.util.excel.SpringExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 生成excel视图，可用excel工具打开或者保存 由ViewController的return new ModelAndView(viewExcel,
 * model)生成
 */
public class VipUserNewExcelView extends AbstractExcelView {
	public void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// 设置文件名称
		String excelName = "达人新用户.xls";
		// 设置sheet名称
		String sheetName = "达人新用户";

		// 设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelName, "UTF-8"));

		// Excel表头
		HSSFSheet sheet = workbook.createSheet(sheetName);

		// 产生标题列
		String[] headerNames = new String[] { "用户id","用户名","昵称","标签名称","负责人","创建时间"};
		SpringExcelUtil excelUtil = new SpringExcelUtil();
		excelUtil.setHeader(sheet, headerNames);

		//
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("mm/dd/yyyy"));

		// 填充数据
		List<VIPUserNew> stuList = (List<VIPUserNew>) model.get("list");
		for (VIPUserNew vipUserNew : stuList) {
			if (vipUserNew != null) {
				String[] data = new String[headerNames.length];
				data[0] = String.valueOf(vipUserNew.getUserId());
				data[1] = vipUserNew.getUserName();
				data[2] = vipUserNew.getNickName();
				//
				//
				data[3] = vipUserNew.getTagName();
				data[4] = vipUserNew.getPrincipal();
				data[5] = vipUserNew.getCreatetime();
				excelUtil.addRecord(sheet, data);
			}
		}
	}
}