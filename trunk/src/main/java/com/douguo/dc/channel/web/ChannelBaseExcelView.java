package com.douguo.dc.channel.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.douguo.dc.channel.model.Channel;
import com.douguo.dc.util.excel.SpringExcelUtil;

import java.net.URLEncoder;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 生成excel视图，可用excel工具打开或者保存 由ViewController的return new ModelAndView(viewExcel,
 * model)生成
 */
public class ChannelBaseExcelView extends AbstractExcelView {
	public void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// 设置文件名称
		String excelName = "渠道列表.xls";
		// 设置sheet名称
		String sheetName = "渠道列表";

		// 设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelName, "UTF-8"));

		// Excel表头
		HSSFSheet sheet = workbook.createSheet(sheetName);

		// 产生标题列
		String[] headerNames = new String[] { "渠道大类","渠道类别","渠道小类","渠道名称","渠道代码","负责人","对接人","部门","联系方式","合作模式","合作进度"};
		SpringExcelUtil excelUtil = new SpringExcelUtil();
		excelUtil.setHeader(sheet, headerNames);

		//
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("mm/dd/yyyy"));

		// 填充数据
		List<Channel> stuList = (List<Channel>) model.get("list");
		for (Channel channel : stuList) {
			if (channel != null) {
				String[] data = new String[headerNames.length];
				data[0] = channel.getChannelTypeName1();
				data[1] = channel.getChannelTypeName2();
				data[2] = channel.getChannelTypeName3();
				data[3] = channel.getChannelName();
				data[4] = channel.getChannelCode();
				//
				data[5] = channel.getUserName();
				data[6] = channel.getPrincipal();
				data[7] = channel.getPrincipalDep();
				data[8] = channel.getPrincipalContact();
				data[9] = channel.getChannelCooperation();
				data[10] = channel.getPlanDesc();
				//
				excelUtil.addRecord(sheet, data);
			}
		}
	}
}