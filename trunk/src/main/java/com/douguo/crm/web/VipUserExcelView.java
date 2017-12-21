package com.douguo.crm.web;

import com.douguo.crm.model.VIPUser;
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
public class VipUserExcelView extends AbstractExcelView {
	public void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// 设置文件名称
		String excelName = "达人用户.xls";
		// 设置sheet名称
		String sheetName = "达人用户";

		// 设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelName, "UTF-8"));

		// Excel表头
		HSSFSheet sheet = workbook.createSheet(sheetName);

		// 产生标题列
		String[] headerNames = new String[] { "用户id","用户名","昵称","真实姓名","地址","手机","国家","省","市","微博地址","标签名称","qq群","负责人","创建时间"};
		SpringExcelUtil excelUtil = new SpringExcelUtil();
		excelUtil.setHeader(sheet, headerNames);

		//
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("mm/dd/yyyy"));

		// 填充数据
		List<VIPUser> stuList = (List<VIPUser>) model.get("list");
		for (VIPUser vipUser : stuList) {
			if (vipUser != null) {
				String[] data = new String[headerNames.length];
				data[0] = String.valueOf(vipUser.getUserId());
				data[1] = vipUser.getUserName();
				data[2] = vipUser.getNickName();
				data[3] = vipUser.getRealName();
				data[4] = vipUser.getAddress();
				//
				data[5] = vipUser.getMobile();
				data[6] = vipUser.getCountry();
				data[7] = vipUser.getProvince();
				data[8] = vipUser.getCity();
				data[9] = vipUser.getWeiboUrl();
				//
				data[10] = vipUser.getTagName();
				data[11] = vipUser.getQqGroup();
				data[12] = vipUser.getPrincipal();
				data[13] = vipUser.getCreatetime();
				excelUtil.addRecord(sheet, data);
			}
		}
	}
}