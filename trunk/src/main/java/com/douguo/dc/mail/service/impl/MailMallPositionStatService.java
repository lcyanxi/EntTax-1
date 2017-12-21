package com.douguo.dc.mail.service.impl;

import com.douguo.dc.group.service.GroupBaseStatService;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mail.service.SysMailSetService;
import com.douguo.dc.mall.service.MallPositionStatService;
import com.douguo.dc.stat.hour.common.CommonHourConstants;
import com.douguo.dc.stat.hour.service.CommonHourStatService;
import com.douguo.dc.util.BigDecimalUtil;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JFreeChartUtil;
import com.douguo.dc.util.JavaSendMail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository("mailMallPositionStatService")
public class MailMallPositionStatService {

    @Autowired
    private MallPositionStatService mallPositionStatService;

    @Autowired
    private SysMailSetService sysMailSetService;

    public void sendMail(SysMailSet sysMailSet) {
        JavaSendMail javaSendMail = new JavaSendMail();
        try {
            String curDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
            String subject = "";
            String to = "";
            StringBuffer content = new StringBuffer("");
            // 增加样式
            content.append("<style type=\"text/css\"> table.gridtable { font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse; } table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #dedede; } table.gridtable td { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff; } </style>");

            if (sysMailSet != null) {
                subject = sysMailSet.getSubject() + "_" + curDate;
                to = sysMailSet.getMailTo();
            } else {
                subject = "豆果优食汇入口监测日报_" + curDate;
                to = "zhangyaozhou@douguo.com";
            }

            String strDesc = sysMailSet.getDesc();
            Integer nDays = 1;
            if (StringUtils.isNotBlank(strDesc)) {
                nDays = Integer.parseInt(strDesc);
            }
            System.out.println("nDays:" + nDays);


            // 1. 基本数据统计
            String mallPositionBaseStat = mallPositionBaseStat(nDays);
            content.append("<h1>1、优食汇入口监测数据统计</h1><br/>");
            content.append(mallPositionBaseStat);
            content.append("<br/><br/>");

            content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

            javaSendMail.sendHtmlEmail(subject, to, content.toString());
//			String[] imgPaths = new String[] { MailConstants.MAIL_CHART_PATH + "/img-group-hour.jpg" };
//
//			javaSendMail.sendHtmlWithInnerImageEmail(subject, to, content.toString(), imgPaths);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String getResName(String page, String view, String position) {

        if (page.equals("p1") && view.equals("v104")) {
            return "运营广告位";
        } else if (page.equals("p1") && view.equals("v105")) {
            return "大家都在看-第4条";
        } else if (page.equals("p1") && view.equals("v106")) {
            return "销售广告位";
        } else if (page.equals("p1") && view.equals("v111")) {
            return "大家都在看-商品-" + position;
        } else if (page.equals("p2") && view.equals("v2")) {
            return "优食汇首焦-" + position;
        } else if (page.equals("p2") && view.equals("v3")) {
            return "列表";
        } else if (page.equals("p2") && view.equals("v5")) {
            return "分类";
        } else if (page.equals("p2") && view.equals("v7")) {
            return "抢购专区";
        } else if (page.equals("p2") && view.equals("v19")) {//原来p2_v8;现在p2_v19
            return "必败专区-" + position;
        } else if (page.equals("p11") && view.equals("v2")) {
            return "圈圈banner";
        } else if (page.equals("p0") && view.equals("v0")) {
            return "push推送";
        } else if (page.equals("p13") && view.equals("v1")) {
            return "订单详情页";
        } else if (page.equals("p14") && view.equals("v1")) {
            return "店铺列表";
        } else if (page.equals("p21") && view.equals("v1")) {
            return "抢购专区-查看全部";
        } else if (page.equals("p22") && view.equals("v1")) {
            return "必败专区-查看全部";
        } else if (page.equals("p10") && view.equals("v1")) {
            return "帖子楼层";
        } else if (page.equals("p11") && view.equals("v1")) {
            return "圈圈置顶帖";
        } else if (page.equals("p6") && view.equals("v8")) {
            return "菜谱详情页-购物车图标";
        } else if (page.equals("p2") && view.equals("v6")) {
            return "优食汇首页-购物车图标";
        } else {
            return "";
        }
    }

    private String mallPositionBaseStat(Integer nDays) {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, nDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, nDays);
        //
        List<Map<String, Object>> rowlistPageView = mallPositionStatService.queryListSumWithPageView(startDate, endDate, "");
        List<Map<String, Object>> rowlistPosition = mallPositionStatService.queryList(startDate, endDate);
        // 构造表格
        sbContent.append("<table class=\"gridtable\">");
        sbContent.append("<tr><th>资源位</th><th>商品PV</th><th>商品UV</th><th>商品成交量</th><th>成交单数</th><th>成交人数</th><th>商品成交金额</th><th>成交转化率</th></tr>");

        //page&view
        Map<String, Map<String, Object>> mapPageView = new HashMap<String, Map<String, Object>>();
        for (Map<String, Object> map : rowlistPageView) {
            String page = (String) map.get("page");
            String view = (String) map.get("view");
            mapPageView.put(page + "_" + view, map);
        }

        //position
        Map<String, Map<String, Object>> mapPosition = new HashMap<String, Map<String, Object>>();
        for (Map<String, Object> map : rowlistPosition) {
            String page = (String) map.get("page");
            String view = (String) map.get("view");
            String position = (String) map.get("position");
            mapPosition.put(page + "_" + view + "_" + position, map);
        }

        List<String> list = new ArrayList<String>();
        list.add("p1_v104");
        list.add("p1_v106");
        list.add("p1_v105");
        list.add("p1_v111_po1");//大家都在看
        list.add("p11_v2");
        list.add("p2_v2_po1");//
        list.add("p2_v7");//限时抢-列表
        list.add("p21_v1");//限时抢-全部列表
        list.add("p2_v8_po1");//必败专区-banner
        list.add("p22_v1");//必败-全部列表
        list.add("p2_v5");
        list.add("p2_v3");
        list.add("p0_v0");//push推送
        list.add("p10_v1");//帖子楼层
        list.add("p11_v1");//圈圈置顶帖
        list.add("p6_v8");//菜谱详情页-购物车图标
        list.add("p2_v6");//优食汇首页-购物车图标
        //list.add("p13_v1");//订单详情页
        //list.add("p14_v1");//店铺列表


        //
        for (String po : list) {
            String[] arryPo = po.split("_");
            Map<String, Object> map = null;
            String resName = "";
            if (arryPo.length == 3) {
                for (int i = 0; i < 10; i++) {
                    map = mapPosition.get(arryPo[0] + "_" + arryPo[1] + "_po" + i);
                    if (map == null) {
                        continue;
                    }
                    //

                    resName = getResName(arryPo[0], arryPo[1], String.valueOf(i));
                    Integer clicks = (Integer) map.get("clicks");
                    Integer productPv = (Integer) map.get("product_pv");
                    Integer productUv = (Integer) map.get("product_uv");
                    Integer goods = (Integer) map.get("goods");
                    Integer pays = (Integer) map.get("pays");
                    Integer payUids = (Integer) map.get("pay_uids");
                    Integer moneys = (Integer) map.get("moneys");
                    BigDecimal bdPays = new BigDecimal(pays);
                    BigDecimal bdProductUv = new BigDecimal(productUv);
                    BigDecimal bdMoneys = new BigDecimal(moneys);
                    bdMoneys = bdMoneys.divide(new BigDecimal(100), 2);
                    BigDecimal payRate = BigDecimalUtil.getPercentage(bdPays, bdProductUv, 2);
                    //
                    sbContent.append("<tr>");
                    sbContent.append("<td>" + resName + "</td>");
                    sbContent.append("<td>" + productPv + "</td>");
                    sbContent.append("<td>" + productUv + "</td>");
                    sbContent.append("<td>" + goods + "</td>");
                    sbContent.append("<td>" + pays + "</td>");
                    sbContent.append("<td>" + payUids + "</td>");
                    sbContent.append("<td>" + BigDecimalUtil.format(bdMoneys, 0) + "</td>");
                    sbContent.append("<td>" + payRate + "%</td>");
                    sbContent.append("</tr>");
                }
            } else {
                map = mapPageView.get(po);
                if (map == null) {
                    continue;
                }
                resName = getResName(arryPo[0], arryPo[1], "");
                //
                BigDecimal clicks = (BigDecimal) map.get("clicks");
                BigDecimal productPv = (BigDecimal) map.get("product_pv");
                BigDecimal productUv = (BigDecimal) map.get("product_uv");
                BigDecimal goods = (BigDecimal) map.get("goods");
                BigDecimal pays = (BigDecimal) map.get("pays");
                BigDecimal payUids = (BigDecimal) map.get("pay_uids");
                BigDecimal moneys = (BigDecimal) map.get("moneys");
                moneys = moneys.divide(new BigDecimal(100), 2);
                BigDecimal payRate = BigDecimalUtil.getPercentage(pays, productUv, 2);
                //
                sbContent.append("<tr>");
                sbContent.append("<td>" + resName + "</td>");
                sbContent.append("<td>" + productPv + "</td>");
                sbContent.append("<td>" + productUv + "</td>");
                sbContent.append("<td>" + goods + "</td>");
                sbContent.append("<td>" + pays + "</td>");
                sbContent.append("<td>" + payUids + "</td>");
                sbContent.append("<td>" + BigDecimalUtil.format(moneys, 0) + "</td>");
                sbContent.append("<td>" + payRate + "%</td>");
                sbContent.append("</tr>");
            }
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");

        return sbContent.toString();
    }
}