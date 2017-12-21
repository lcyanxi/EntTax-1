package com.douguo.dc.channel.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.douguo.dc.util.excel.SpringExcelUtil;

import java.math.BigDecimal;
import java.net.URLEncoder;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 生成excel视图，可用excel工具打开或者保存 由ViewController的return new ModelAndView(viewExcel,
 * model)生成
 */
public class ChannelSumExcelView extends AbstractExcelView {
    public void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {

        // 设置文件名称
        String excelName = "渠道汇总列表.xls";
        // 设置sheet名称
        String sheetName = "渠道汇总列表";

        // 设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelName, "UTF-8"));

        // Excel表头
        HSSFSheet sheet = workbook.createSheet(sheetName);

        // 产生标题列
        String[] headerNames = new String[]{"日期", "渠道名称", "渠道code", "日活", "新增用户", "每日占比", "次日留存", "7日留存", "渠道大类", "渠道类别", "渠道小类", "渠道合集", "负责人", "合作模式"};
        SpringExcelUtil excelUtil = new SpringExcelUtil();
        excelUtil.setHeader(sheet, headerNames);

        //
        HSSFCellStyle cellStyleDate = workbook.createCellStyle();
        cellStyleDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-mm-dd"));

        // 填充数据
        List<Map<String, Object>> stuList = (List<Map<String, Object>>) model.get("list");
        List<Map<String, Object>> listTotal = (List<Map<String, Object>>) model.get("listTotal");
        List<Map<String, Object>> listAppTotal = (List<Map<String, Object>>) model.get("listAppTotal");
        Map<Date, BigDecimal> mapTotal = new HashMap<Date, BigDecimal>();
        Map<String, String> mapAppTotal = new HashMap<String, String>();

        for (Map<String, Object> totObj : listTotal) {
            Date statDate = (Date) totObj.get("statdate");
            BigDecimal totalNewUsers = (BigDecimal) totObj.get("total_new_users");
            mapTotal.put(statDate, totalNewUsers);
        }

        for (Map<String, Object> channel : stuList) {
            if (channel != null) {
                Date statdate = (Date) channel.get("statdate");
                BigDecimal daus = (BigDecimal) channel.get("daus");
                BigDecimal totalNewUsers = mapTotal.get(statdate);
                if (totalNewUsers == null) {
                    totalNewUsers = new BigDecimal(0);
                }
                if (daus == null) {
                    daus = new BigDecimal("0");
                }
                Integer news = (Integer) channel.get("news");
                if (news == null) {
                    news = new Integer(0);
                }
                String userlast1 = (String) channel.get("userlast1");
                String userlast7 = (String) channel.get("userlast7");
                String channelName = (String) channel.get("channel_name");
                String channelCode = (String) channel.get("channel");
                //
                String channelTypeName1 = (String) channel.get("type1_name");
                String channelTypeName2 = (String) channel.get("type2_name");
                String channelTypeName3 = (String) channel.get("type3_name");
                String channelTag = (String) channel.get("channel_tag");
                String strUserName = (String) channel.get("user_name");
                String channelCooperation = (String) channel.get("channel_cooperation");

                //
                HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
                HSSFCell cell0 = row.createCell(0);
                //cell0.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                //cell0.setCellStyle(cellStyleDate);
                cell0.setCellValue(String.valueOf(statdate));
                HSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(channelName);
                HSSFCell cell2 = row.createCell(2);
                cell2.setCellValue(channelCode);
                HSSFCell cell3 = row.createCell(3);
                cell3.setCellValue(daus.doubleValue());
                HSSFCell cell4 = row.createCell(4);
                cell4.setCellValue(news);
                HSSFCell cell5 = row.createCell(5);
                cell5.setCellValue(getPercentage(news.toString(), totalNewUsers.intValue(), 2).doubleValue());
                HSSFCell cell6 = row.createCell(6);
                cell6.setCellValue(getPercentage(userlast1, news, 2).doubleValue());
                HSSFCell cell7 = row.createCell(7);
                cell7.setCellValue(getPercentage(userlast7, news, 2).doubleValue());

                //
                HSSFCell cell8 = row.createCell(8);
                cell8.setCellValue(channelTypeName1);
                HSSFCell cell9 = row.createCell(9);
                cell9.setCellValue(channelTypeName2);
                HSSFCell cell10 = row.createCell(10);
                cell10.setCellValue(channelTypeName3);
                HSSFCell cell11 = row.createCell(11);
                cell11.setCellValue(channelTag);
                HSSFCell cell12 = row.createCell(12);
                cell12.setCellValue(strUserName);
                HSSFCell cell13 = row.createCell(13);
                cell13.setCellValue(channelCooperation);
                //*******************
            }
        }
    }

    private BigDecimal getPercentage(String sub, Integer total, int scale) {
        if (null == total || total.intValue() == 0 || null == sub || sub.equals("0") || sub.equals("")) {
            return new BigDecimal("0");
        }
        if (scale < 0) {
            scale = 2;
        }
        BigDecimal divisor = new BigDecimal(sub);
        BigDecimal dividend = new BigDecimal(total);
        double d = divisor.divide(dividend, scale + 2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;

        BigDecimal result = new BigDecimal(Double.toString(d));
        BigDecimal one = new BigDecimal("1");
        BigDecimal ret = result.divide(one, scale, BigDecimal.ROUND_HALF_UP);
        return ret;
    }
}