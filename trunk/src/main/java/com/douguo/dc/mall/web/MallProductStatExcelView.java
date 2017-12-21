package com.douguo.dc.mall.web;

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
public class MallProductStatExcelView extends AbstractExcelView {
    public void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {

        // 设置文件名称
        String excelName = "商品统计列表.xls";
        // 设置sheet名称
        String sheetName = "商品统计列表";

        // 设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelName, "UTF-8"));

        // Excel表头
        HSSFSheet sheet = workbook.createSheet(sheetName);

        // 产生标题列
        String[] headerNames = new String[]{"商品id", "商品名称", "一级分类", "二级分类", "商户id", "商户名称", "上架时间", "商家报价", "豆果售价",
                "浏览数", "UV数", "下单数", "下单率", "成交数", "成交人数", "成交率", "商品数", "总支付金额"};
        SpringExcelUtil excelUtil = new SpringExcelUtil();
        excelUtil.setHeader(sheet, headerNames);

        //
        HSSFCellStyle cellStyleDate = workbook.createCellStyle();
        cellStyleDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-mm-dd"));

        // 填充数据
        List<Map<String, Object>> stuList = (List<Map<String, Object>>) model.get("list");
        Map<Date, BigDecimal> mapTotal = new HashMap<Date, BigDecimal>();
        Map<String, String> mapAppTotal = new HashMap<String, String>();

        for (Map<String, Object> goodsObj : stuList) {
            if (goodsObj != null) {
                Date statdate = (Date) goodsObj.get("statdate");
                BigDecimal daus = (BigDecimal) goodsObj.get("daus");
                BigDecimal totalNewUsers = mapTotal.get(statdate);
                if (totalNewUsers == null) {
                    totalNewUsers = new BigDecimal(0);
                }
                if (daus == null) {
                    daus = new BigDecimal("0");
                }
                Integer news = (Integer) goodsObj.get("news");
                if (news == null) {
                    news = new Integer(0);
                }
                Long tuanId = (Long) goodsObj.get("tuan_id");
                String tuanName = (String) goodsObj.get("tuan_name");
                String fCateName = (String) goodsObj.get("fCateName");
                String cateName = (String) goodsObj.get("cateName");
                Integer storeId = (Integer) goodsObj.get("storeId");

                String storeName = (String) goodsObj.get("storeName");
                Date sellFlagTime = (Date) goodsObj.get("sellFlagTime");

                BigDecimal marketPrice = (BigDecimal) goodsObj.get("marketPrice");
                BigDecimal price = (BigDecimal) goodsObj.get("price");
                BigDecimal clicks = (BigDecimal) goodsObj.get("clicks");

                BigDecimal uids = (BigDecimal) goodsObj.get("uids");
                BigDecimal orders = (BigDecimal) goodsObj.get("orders");
                BigDecimal orderRate = (BigDecimal) goodsObj.get("orderRate");
                BigDecimal pays = (BigDecimal) goodsObj.get("pays");
                BigDecimal payUids = (BigDecimal) goodsObj.get("pay_uids");

                BigDecimal payRate = (BigDecimal) goodsObj.get("payRate");
                BigDecimal goods = (BigDecimal) goodsObj.get("goods");
                BigDecimal moneys = (BigDecimal) goodsObj.get("moneys");
                if(moneys.intValue() != 0){
                    moneys = moneys.divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
                }

                //
                // *******************
                HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
                HSSFCell cell0 = row.createCell(0);
                cell0.setCellValue(String.valueOf(tuanId));
                HSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(tuanName);
                HSSFCell cell2 = row.createCell(2);
                cell2.setCellValue(fCateName);
                HSSFCell cell3 = row.createCell(3);
                cell3.setCellValue(cateName);
                HSSFCell cell4 = row.createCell(4);
                cell4.setCellValue(storeId);

                HSSFCell cell5 = row.createCell(5);
                cell5.setCellValue(storeName);
                HSSFCell cell6 = row.createCell(6);
                if (sellFlagTime == null) {
                    cell6.setCellValue("");
                } else {
                    cell6.setCellValue(String.valueOf(sellFlagTime));
                }

                HSSFCell cell7 = row.createCell(7);
                cell7.setCellValue(marketPrice.doubleValue());

                HSSFCell cell8 = row.createCell(8);
                cell8.setCellValue(price.doubleValue());

                HSSFCell cell9 = row.createCell(9);
                cell9.setCellValue(clicks.doubleValue());

                HSSFCell cell10 = row.createCell(10);
                cell10.setCellValue(uids.doubleValue());

                HSSFCell cell11 = row.createCell(11);
                cell11.setCellValue(orders.doubleValue());

                HSSFCell cell12 = row.createCell(12);
                cell12.setCellValue(orderRate.doubleValue());

                HSSFCell cell13 = row.createCell(13);
                cell13.setCellValue(pays.doubleValue());

                HSSFCell cell14 = row.createCell(14);
                cell14.setCellValue(payUids.doubleValue());

                HSSFCell cell15 = row.createCell(15);
                cell15.setCellValue(payRate.doubleValue());

                HSSFCell cell16 = row.createCell(16);
                cell16.setCellValue(goods.doubleValue());

                HSSFCell cell17 = row.createCell(17);
                cell17.setCellValue(moneys.doubleValue());
                // *******************
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