package com.douguo.dc.mail.service.impl;

import com.douguo.dc.mail.dao.LiveClassDao;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mall.common.MallConstants;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JavaSendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("/mailLiveClassStatService")
public class MailLiveClassStatService {


    @Autowired
    private LiveClassDao liveClassDao;


    public void sendMail(SysMailSet sysMailSet) {
        JavaSendMail javaSendMail = new JavaSendMail();
        try {
//			SysMailSet sysMailSet = sysMailSetService
//					.getSysMailSetByMailType(MailConstants.MAIL_TYPE_MALL_DAILY_TIME_SUM);

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
                subject = "豆果课堂-时段数据日报_" + curDate;
                to = "zhangyaozhou@douguo.com";
            }

            // 1. 数据统计
            String gaotieBaseStat = gaotieBaseStat();
            content.append("<h1>1、课堂数据统计</h1><br/>");
            content.append(gaotieBaseStat);
            content.append("<br/><br/>");

            // 2. 成交量前10商品详情
            String top10Product = top10Product();
            content.append("<h1>2、课堂TOP10数据</h1><br/>");
            content.append(top10Product);
            content.append("<br/><br/>");

            content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

            javaSendMail.sendHtmlEmail(subject, to, content.toString());
            // String[] imgPaths = new String[] { MailConstants.MAIL_CHART_PATH
            // + "/img-group-hour.jpg" };
            // javaSendMail.sendHtmlWithInnerImageEmail(subject, to,
            // content.toString(), imgPaths);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private String gaotieBaseStat() {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 0);

        System.out.println(startDate+":"+endDate);

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");
        sbContent
                .append("<tr><th>序号</th><th>日期</th><th>时间</th><th>成交单数</th><th>昨日环比</th><th>成交金额</th><th>昨日环比</th></tr>");

        List<Integer> hourList = new ArrayList<Integer>();
        hourList.add(12);
        hourList.add(14);
        hourList.add(16);
        hourList.add(18);
        hourList.add(20);
        hourList.add(22);
        hourList.add(23);

        for (Integer nHour : hourList) {
            List<Map<String, Object>> rowlist = liveClassDao.queryList(
                    MallConstants.MALL_COMMON_TYPE_DAILY_HOUR, String.valueOf(nHour), startDate, endDate);
            BigDecimal moneyChainRate = new BigDecimal(0);
            BigDecimal paysChainRate = new BigDecimal(0);
            int i = 0;
            BigDecimal preMoneys = new BigDecimal(0);
            Integer prePays = 0;
            for (Map<String, Object> map : rowlist) {
                i++;

                Date statdate = (Date) map.get("statdate");
                Integer orders = (Integer) map.get("orders");
                Integer pays = (Integer) map.get("pays");
                // BigDecimal dbPays = new BigDecimal(pays);
                String strHour = (String) map.get("stat_value");
                BigDecimal moneys = (BigDecimal) map.get("moneys");
                moneys = moneys.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);

                if (i > 1) {
                    BigDecimal increasePays = new BigDecimal((pays - prePays) * 100);
                    paysChainRate = increasePays.divide(new BigDecimal(prePays), 2, BigDecimal.ROUND_HALF_UP);
                    //
                    BigDecimal increateseMoneys = moneys.subtract(preMoneys).multiply(new BigDecimal(100));
                    // tmp = tmp.multiply(new BigDecimal(100));
                    moneyChainRate = increateseMoneys.divide(preMoneys, 2, BigDecimal.ROUND_HALF_UP);
                }

                sbContent.append("<tr>");
                sbContent.append("<td>" + i + "</td>");
                sbContent.append("<td>" + statdate + "</td>");
                sbContent.append("<td>" + strHour + "</td>");
                sbContent.append("<td>" + pays + "</td>");
                sbContent.append("<td>" + paysChainRate + "%</td>");
                sbContent.append("<td>" + moneys + "</td>");
                sbContent.append("<td>" + moneyChainRate + "%</td>");
                sbContent.append("</tr>");

                preMoneys = moneys;
                prePays = pays;
            }
        }

        sbContent.append("</table>");
        sbContent.append("<br/>");

        return sbContent.toString();
    }

    private String top10Product() {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 0);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 0);
        String order = "pays";
        //
        List<Map<String, Object>> rowlst = liveClassDao.queryProductList(startDate, endDate, order);
        int i = 0;

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");
        sbContent
                .append("<tr><th>课堂名称</th><th>浏览数</th><th>UV数</th><th>下单数</th><th>成交数</th><th>成交转化率（成交数/UV数）</th><th>成交率（成交数/下单数）</th><th>总金额(元)</th></tr>");
        for (Map<String, Object> map : rowlst) {
            i++;
            // 只取前10条数据
            if (i > 10) {
                break;
            }

            String tuanName = (String) map.get("class_name");
            BigDecimal clicks = (BigDecimal) map.get("clicks");
            BigDecimal uids = (BigDecimal) map.get("uids");
            BigDecimal orders = (BigDecimal) map.get("orders");
            BigDecimal pays = (BigDecimal) map.get("pays");
           // BigDecimal goods = (BigDecimal) map.get("goods");
            BigDecimal moneys = (BigDecimal) map.get("moneys");
            BigDecimal orderRate = new BigDecimal(0);
            BigDecimal payConversionRate = new BigDecimal(0);//成交转化率
            BigDecimal payRate = new BigDecimal(0);//成交率

            //
            if (0 != uids.intValue()) {
                BigDecimal bdOrders = orders.multiply(new BigDecimal(100));
                orderRate = bdOrders.divide(uids, 2, BigDecimal.ROUND_HALF_UP);
            }
            //
            if (0 != uids.intValue()) {
                BigDecimal bdPays = pays.multiply(new BigDecimal(100));
                payConversionRate = bdPays.divide(uids, 2, BigDecimal.ROUND_HALF_UP);
            }
            //
            if (0 != orders.intValue()) {
                BigDecimal bdPays = pays.multiply(new BigDecimal(100));
                payRate = bdPays.divide(orders, BigDecimal.ROUND_HALF_UP);
            }

            map.put("orderRate", orderRate);
            map.put("payRate", payRate);
            //

            try {
                moneys = moneys.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
            } catch (Exception e) {
                e.printStackTrace();
                moneys = new BigDecimal(0);
            }

            //
            sbContent.append("<tr>");
            sbContent.append("<td>" + tuanName + "</td>");
            sbContent.append("<td>" + clicks + "</td>");
            sbContent.append("<td>" + uids + "</td>");
            sbContent.append("<td>" + orders + "</td>"); //下单数
            sbContent.append("<td>" + pays + "</td>");  //成交数
            sbContent.append("<td>" + payConversionRate + "%</td>");
            sbContent.append("<td>" + payRate + "%</td>");
            sbContent.append("<td>" + moneys + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");

        return sbContent.toString();
    }


}
