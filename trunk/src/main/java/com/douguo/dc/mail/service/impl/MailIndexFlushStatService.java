package com.douguo.dc.mail.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.dao.IndexFlushDao;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JFreeChartUtil;
import com.douguo.dc.util.JavaSendMail;
import com.douguo.dc.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lichang on 2017/8/14.
 */

@Repository("mailIndexTypeStatService")
public class MailIndexFlushStatService {

    @Autowired
    private IndexFlushDao indexFlushDao;


    /**
     * 发送首页报表邮件
     *
     * @param sysMailSet
     */
    public void sendIndexTypeSumMail(SysMailSet sysMailSet) {
        JavaSendMail javaSendMail = new JavaSendMail();

        String currDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String subject;
        String toAddress = "";
        StringBuffer content = new StringBuffer("");
        content.append("<style type=\"text/css\"> table.gridtable { font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse; } table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #dedede; } table.gridtable td { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff; } </style>");
        System.out.println(toAddress);
        if (sysMailSet != null) {
            subject = sysMailSet.getSubject() + "_" + currDate;
            toAddress = sysMailSet.getMailTo();
            System.out.println(toAddress);
        } else {
            subject = "首页类型报表统计_" + currDate;
            toAddress = "lichang@douguo.com";
        }


        //获取需要发送的顺序及编号
        String strConfig = StringUtils.trimToEmpty(sysMailSet.getConfig());
        JSONObject jsonConfig = JsonUtil.parseStrToJsonObj(strConfig);
        String config = jsonConfig.getString("type");
        String strDesc = jsonConfig.getString("trace_days");


        Integer nDays = 7;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(strDesc)) {
            try {
                nDays = Integer.parseInt(strDesc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String[] strArray = config.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        String[] imgPaths = new String[strArray.length];
        int index = 0;
        for (int i = 0; i < strArray.length; i++) {
            int type = Integer.parseInt(strArray[i]);
            switch (type) {
                case 0:
                    imgPaths[index] = MailConstants.MAIL_CHART_PATH + "/img-index-flush-pv-total.jpg";
                    content.append(indexFlushSumPv(nDays));//首页总数据只统计pv case0
                    index++;
                    break;
                case 1:
                    imgPaths[index] = MailConstants.MAIL_CHART_PATH + "/img-index-flush-total.jpg";
                    content.append(indexFlushSum(nDays));//首页总数据 case1
                    index++;
                    break;
                case 2:
                    imgPaths[index] = MailConstants.MAIL_CHART_PATH + "/img-index-flush-type-total.jpg";
                    content.append(indexFlushType(nDays));//首页刷新来源统计 case2
                    index++;
                    break;
                case 3:
                    imgPaths[index] = MailConstants.MAIL_CHART_PATH + "/img-index-flush-client-total.jpg";
                    content.append(indexFlushClientSum(nDays));//首页分客户端总数据统计 case3
                    index++;
                    break;
                case 4:
                    imgPaths[index] = MailConstants.MAIL_CHART_PATH + "/img-index-flush-reqNum-total.jpg";
                    content.append(indexFlushReqNumSum());//首页刷新次数统计 case4
                    index++;
                    break;
                case 5:
                    imgPaths[index] = MailConstants.MAIL_CHART_PATH + "/img-index-flush-client-reqNum-total.jpg";
                    content.append(indexFlushReqNumClientSum());//首页刷新分客户端pv、uv统计  case5
                    index++;
                    break;
            }
        }

        content.append("<br/><br/>");
        content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");
        try {
            //发送邮件
            javaSendMail.sendHtmlWithInnerImageEmail(subject, toAddress, content.toString(), imgPaths);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 首页总数据只统计pv case0
     *
     * @return string
     */
    private String indexFlushSumPv(Integer beforeDays) {

        StringBuffer sbContent = new StringBuffer();

        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, beforeDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 0);

        String sql = "select pv, uv , DATE_FORMAT (statdate, \"%Y-%m-%d\") AS statdate  " +
                "from dd_main_index_flush_sum WHERE statdate BETWEEN ? AND ?  ORDER BY statdate DESC";

        List<Map<String, Object>> mapList = indexFlushDao.queryIndexFlushSumList(sql, startDate, endDate);

        String[] columnKeysCooks = new String[mapList.size()];
        double[] arrayPv = new double[mapList.size()];

        sbContent.append("<h1>1.0、首页汇总数据</h1><br/>");
        sbContent.append("<img width=\"590\" height=\"500\" src=\"cid:img-index-flush-pv-total.jpg\" alt=\"\"/>");
        sbContent.append("<br/><br/>");

        // 构建首页汇总数据表格开始
        sbContent.append("<table class=\"gridtable\">");
        // 构造 title

        sbContent.append("<tr>" +
                "<th colspan=\"3\">首页汇总数据</th>" +
                "</tr>");
        sbContent.append("<tr>" +
                "<td>日期</td>" +
                "<td>人均刷新次数</td>" +
                "</tr>");

        //遍历查询结果
        DecimalFormat df = new DecimalFormat("#.00");
        int index = 0;
        for (int i = mapList.size() - 1; i >= 0; i--) {
            //取值
            int pv = (int) mapList.get(i).get("pv");
            int uv = (int) mapList.get(i).get("uv");
            String statdate = (String) mapList.get(i).get("statdate");
            String strWeek = DateUtil.getWeekDay(statdate);

            double avg = (double) pv / (double) uv;
            //用于折线图
            columnKeysCooks[index] = statdate;
            arrayPv[index] = pv;
            index++;

            //1.填值到表格
            sbContent.append("<tr>");
            sbContent.append("<td>" + statdate + "(" + strWeek + ")</td>");
            sbContent.append("<td>" + df.format(avg) + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");

        //构造首页汇总数据表格结束

        //构建首页汇总数据折线图 开始
        String[] rowKeysCooks = {"pv"};

        //图片名称
        String chartTitleCooks = "首页汇总数据";

        //X轴
        String categoryLabelCooks = "日期";

        // y轴
        String valueLabelCooks = "数量";

        //数据集
        double[][] dataCooks = new double[1][];

        dataCooks[0] = arrayPv;

        //家庭汇总折线图
        JFreeChartUtil.buildLineChat(chartTitleCooks, categoryLabelCooks, valueLabelCooks, rowKeysCooks, columnKeysCooks, dataCooks,
                MailConstants.MAIL_CHART_PATH + "/img-index-flush-pv-total.jpg");
        //构建家庭报表折线图 结束

        return sbContent.toString();
    }

    /**
     * 首页总数据 case1
     *
     * @return string
     */
    private String indexFlushSum(Integer beforeDays) {

        StringBuffer sbContent = new StringBuffer();

        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, beforeDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 0);

        String sql = "select pv, uv , DATE_FORMAT (statdate, \"%Y-%m-%d\") AS statdate  " +
                "from dd_main_index_flush_sum WHERE statdate BETWEEN ? AND ?  ORDER BY statdate DESC";

        List<Map<String, Object>> mapList = indexFlushDao.queryIndexFlushSumList(sql, startDate, endDate);

        String[] columnKeysCooks = new String[mapList.size()];
        double[] arrayFamily_num = new double[mapList.size()];
        double[] arrayFamily_member_num = new double[mapList.size()];

        sbContent.append("<h1>1.1、首页汇总数据</h1><br/>");
        sbContent.append("<img width=\"590\" height=\"500\" src=\"cid:img-index-flush-total.jpg\" alt=\"\"/>");
        sbContent.append("<br/><br/>");
        // 构建首页汇总数据表格开始
        sbContent.append("<table class=\"gridtable\">");
        // 构造 title

        sbContent.append("<tr>" +
                "<th colspan=\"4\">首页汇总数据</th>" +
                "</tr>");
        sbContent.append("<tr>" +
                "<td>日期</td>" +
                "<td>pv</td>" +
                "<td>uv</td>" +
                "<td>人均刷新次数</td>" +
                "</tr>");

        //遍历查询结果
        int index = 0;
        DecimalFormat df = new DecimalFormat("#.00");
        for (int i = mapList.size() - 1; i >= 0; i--) {
            //取值
            int pv = (int) mapList.get(i).get("pv");
            int uv = (int) mapList.get(i).get("uv");
            String statdate = (String) mapList.get(i).get("statdate");
            String strWeek = DateUtil.getWeekDay(statdate);
            double avg = (double) pv / (double) uv;

            //用于折线图
            columnKeysCooks[index] = statdate;
            arrayFamily_num[index] = pv;
            arrayFamily_member_num[index] = uv;
            index++;

            //1.填值到表格
            sbContent.append("<tr>");
            sbContent.append("<td>" + statdate + "(" + strWeek + ")</td>");
            sbContent.append("<td>" + pv + "</td>");
            sbContent.append("<td>" + uv + "</td>");
            sbContent.append("<td>" + df.format(avg) + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");

        //构造首页汇总数据表格结束

        //构建首页汇总数据折线图 开始
        String[] rowKeysCooks = {"pv", "uv"};

        //图片名称
        String chartTitleCooks = "首页汇总数据";

        //X轴
        String categoryLabelCooks = "日期";

        // y轴
        String valueLabelCooks = "数量";

        //数据集
        double[][] dataCooks = new double[2][];

        dataCooks[0] = arrayFamily_num;
        dataCooks[1] = arrayFamily_member_num;

        //家庭汇总折线图
        JFreeChartUtil.buildLineChat(chartTitleCooks, categoryLabelCooks, valueLabelCooks, rowKeysCooks, columnKeysCooks, dataCooks,
                MailConstants.MAIL_CHART_PATH + "/img-index-flush-total.jpg");
        //构建家庭报表折线图 结束

        return sbContent.toString();
    }

    /**
     * 首页刷新来源统计 case2
     *
     * @return string
     */
    private String indexFlushType(Integer beforeDays) {

        StringBuffer sbContent = new StringBuffer();


        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, beforeDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 0);



        String sql = "select flush_type, pv , DATE_FORMAT (statdate, \"%Y-%m-%d\") AS statdate  " +
                "from dd_main_index_flush_type WHERE flush_type>0 AND statdate BETWEEN ? AND ?  ORDER BY statdate DESC ";

        List<Map<String, Object>> mapList = indexFlushDao.queryIndexFlushSumList(sql, startDate, endDate);


        Map type1Map=new LinkedHashMap();
        Map type2Map=new LinkedHashMap();
        Map type3Map=new LinkedHashMap();
        Map type4Map=new LinkedHashMap();
        Map type5Map=new LinkedHashMap();

        for (Map<String,Object> map:mapList){
            int flushType=(int) map.get("flush_type");
            String statdate =(String) map.get("statdate");
            int pv=(int) map.get("pv");


            switch (flushType) {
                case 1:
                   type1Map.put(statdate,pv);
                    break;
                case 2:
                    type2Map.put(statdate,pv);
                    break;
                case 3:
                    type3Map.put(statdate,pv);
                    break;
                case 4:
                    type4Map.put(statdate,pv);
                    break;
                case 5:
                    type5Map.put(statdate,pv);
                    break;
            }
        }

        String[] columnKeysCooks = new String[beforeDays];
        int index=0;
            for (int i=beforeDays;i>0;i--){
                columnKeysCooks[index]=getNextDay(i);
                index++;
                System.out.println(getNextDay(i));
            }


        int  temp=beforeDays+1;//数据是从当前时间的前一天开取的
        List type1List= converList(type1Map,temp);
        List type2List=converList(type2Map,temp);
        List type3List=converList(type3Map,temp);
        List type4List=converList(type4Map,temp);
        List type5List=converList(type5Map,temp);

        double[] type1 = new double[beforeDays];
        double[] type2 = new double[beforeDays];
        double[] type3 = new double[beforeDays];
        double[] type4 = new double[beforeDays];
        double[] type5 = new double[beforeDays];

        for (int i=0;i<beforeDays;i++){
            int type1Pv=(int)type1List.get(i);
            int type2Pv=(int)type2List.get(i);
            int type3Pv=(int)type3List.get(i);
            int type4Pv=(int)type4List.get(i);
            int type5Pv=(int)type5List.get(i);
            type1[i]=(double)type1Pv;
            type2[i]=(double)type2Pv;
            type3[i]=(double)type3Pv;
            type4[i]=(double)type4Pv;
            type5[i]=(double)type5Pv;

        }


        sbContent.append("<h1>1.2、首页刷新来源统计</h1><br/>");
        sbContent.append("<img width=\"790\" height=\"670\" src=\"cid:img-index-flush-type-total.jpg\" alt=\"\"/>");
        sbContent.append("<br/><br/>");

        // 首页刷新来源统计表格
        sbContent.append("<table class=\"gridtable\">");
        // 构造 title
        sbContent.append("<tr>" +
                "<th colspan=\"6\">首页刷新来源统计</th>" +
                "</tr>");
        sbContent.append("<tr>" +
                "<td>日期</td>" +
                "<td>下拉刷新</td>" +
                "<td>上拉加载</td>" +
                "<td>底部的刷新按钮</td>" +
                "<td>列表中的刷新按钮</td>" +
                "<td>自动刷新</td>" +
                "</tr>");

        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(1);//控制保留小数点后几位，2：表示保留2位小数点

        for (int i = 0; i < beforeDays; i++) {
            String statdate = columnKeysCooks[i];
            String strWeek="";
            if (statdate!=null){
             strWeek = DateUtil.getWeekDay(statdate);
            }
            double typeOne = type1[i];
            double typeTwo = type2[i];
            double typeThree = type3[i];
            double typeFour = type4[i];
            double typeFive = type5[i];

            double total = typeOne + typeTwo + typeThree + typeFour+typeFive;


            String typeOnePrecet=  (int)typeOne==0?"0":nf.format(typeOne / total);
            String typeTwoPrecet=  (int)typeTwo==0?"0":nf.format(typeTwo / total);
            String typeThreePrecet=  (int)typeThree==0?"0":nf.format(typeThree / total);
            String typeFourPrecet=  (int)typeFour==0?"0":nf.format(typeFour / total);
            String typeFivePrecet=  (int) typeFive ==0?"0":nf.format( typeFive  / total);

            sbContent.append("<tr>");
            sbContent.append("<td>" + statdate + "(" + strWeek + ")</td>");
            sbContent.append("<td>" + (int) typeOne + " (" +  typeOnePrecet + ")</td>");//398745(37.79%)形式
            sbContent.append("<td>" + (int) typeTwo + " (" + typeTwoPrecet + ")</td>");
            sbContent.append("<td>" + (int) typeThree + " (" +typeThreePrecet + ")</td>");
            sbContent.append("<td>" + (int) typeFour + " (" + typeFourPrecet+ ")</td>");
            sbContent.append("<td>" + (int) typeFive + " (" + typeFivePrecet+ ")</td>");
            sbContent.append("</tr>");
        }

        sbContent.append("</table>");
        sbContent.append("<br/><br/>");

        // 首页刷新来源统计表格结束

        //首页刷新来源统计折线图 开始
        String[] rowKeysCooks = {"下拉刷新", "上拉加载", "底部的刷新按钮", "列表中的刷新按钮","自动刷新"};

        //图片名称
        String chartTitleCooks = "首页刷新来源统计";

        //X轴
        String categoryLabelCooks = "日期";

        // y轴
        String valueLabelCooks = "数量";

        //数据集
        double[][] dataCooks = new double[5][];
        dataCooks[0] = type1;
        dataCooks[1] = type2;
        dataCooks[2] = type3;
        dataCooks[3] = type4;
        dataCooks[4] = type5;

        //首页刷新来源统计柱形图
        JFreeChartUtil.buildBarChat2(chartTitleCooks, categoryLabelCooks, valueLabelCooks, rowKeysCooks, columnKeysCooks, dataCooks,
                MailConstants.MAIL_CHART_PATH + "/img-index-flush-type-total.jpg");
        //首页刷新来源统计柱形图 结束

        return sbContent.toString();
    }

    /**
     * 首页分客户端总数据统计 case3
     *
     * @return string
     */
    private String indexFlushClientSum(Integer beforeDays) {
        StringBuffer sbContent = new StringBuffer();


        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDays = DateUtil.getSpecifiedDayBefore(today, beforeDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 0);

        String sql = "select client, pv ,uv, DATE_FORMAT (statdate, \"%Y-%m-%d\") AS statdate  " +
                "from dd_main_index_flush_client_sum WHERE statdate BETWEEN ? AND ?  ORDER BY statdate DESC ";

        List<Map<String, Object>> mapList = indexFlushDao.queryIndexFlushSumList(sql, startDays, endDate);

        String[] columnKeysCooks = new String[mapList.size() / 2];
        double[] arrayIosPv = new double[mapList.size() / 2];
        double[] arrayIosUv = new double[mapList.size() / 2];
        double[] arrayAndroidPv = new double[mapList.size() / 2];
        double[] arrayAndroidUv = new double[mapList.size() / 2];

        int pv3 = 0, uv3 = 0, pv4 = 0, uv4 = 0, index = 0;
        String date = "";
        for (int i = mapList.size() - 1; i >= 0; i--) {
            //取值
            int client = (int) mapList.get(i).get("client");
            String statdate = (String) mapList.get(i).get("statdate");
            int pv = (int) mapList.get(i).get("pv");
            int uv = (int) mapList.get(i).get("uv");
            if (!date.equals(statdate)) {
                date = statdate;
                columnKeysCooks[index++] = statdate;
            }

            if (client == 3) {
                arrayIosPv[pv3++] = pv;
                arrayIosUv[uv3++] = uv;
            } else if (client == 4) {
                arrayAndroidPv[pv4++] = pv;
                arrayAndroidUv[uv4++] = uv;
            }
        }

        //首页分客户端总数据汇总统计柱形图 开始
        String[] rowKeysCooks = {"android-pv", "ios-pv", "android-uv", "ios-uv"};

        //图片名称
        String chartTitleCooks = "首页刷新客户端总数据";

        //X轴
        String categoryLabelCooks = "日期";

        // y轴
        String valueLabelCooks = "数量";

        //数据集
        double[][] dataCooks = new double[4][];
        dataCooks[0] = arrayAndroidPv;
        dataCooks[1] = arrayIosPv;
        dataCooks[2] = arrayAndroidUv;
        dataCooks[3] = arrayIosUv;


        //构建ios客户端数据折线图
        JFreeChartUtil.buildLineChat(chartTitleCooks, categoryLabelCooks, valueLabelCooks, rowKeysCooks, columnKeysCooks, dataCooks,
                MailConstants.MAIL_CHART_PATH + "/img-index-flush-client-total.jpg");
        //构建ios客户端数据折线图 结束


        // 首页分客户端总数据汇总统计折线图表格
        sbContent.append("<h1>2.1、首页分客户端来源统计</h1><br/>");
        sbContent.append("<img width=\"590\" height=\"500\" src=\"cid:img-index-flush-client-total.jpg\" alt=\"\"/>");
        sbContent.append("<br/><br/>");


        // 首页分客户端总数据汇总统计表格
        sbContent.append("<table class=\"gridtable\">");
        // 构造 title
        sbContent.append("<tr>" +
                "<th colspan=\"5\">首页分客户端来源统计</th>" +
                "</tr>");

        sbContent.append("<tr>" +
                "<td>日期</td>" +
                "<td>android-pv</td>" +
                "<td>ios-pv</td>" +
                "<td>android-uv</td>" +
                "<td>ios-uv</td>" +
                "</tr>");

        for (int i = 0; i < columnKeysCooks.length; i++) {
            String statdate = columnKeysCooks[i];
            String strWeek="";
            if (statdate!=null){
                strWeek = DateUtil.getWeekDay(statdate);
            }
            sbContent.append("<tr>");
            sbContent.append("<td>" + statdate + "(" + strWeek + ")</td>");
            sbContent.append("<td>" + (int) arrayAndroidPv[i] + "</td>");
            sbContent.append("<td>" + (int) arrayIosPv[i] + "</td>");
            sbContent.append("<td>" + (int) arrayAndroidUv[i] + "</td>");
            sbContent.append("<td>" + (int) arrayIosUv[i] + "</td>");

            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/><br/>");

        return sbContent.toString();

    }

    /**
     * 首页刷新次数统计 case4
     *
     * @return string
     */
    private String indexFlushReqNumSum() {

        StringBuffer sbContent = new StringBuffer();

        String sql = "select req_num, pv , uv,DATE_FORMAT (statdate, \"%Y-%m-%d\") AS statdate  " +
                "from dd_main_index_flush_req_sum " +
                "WHERE  statdate BETWEEN ? AND ? GROUP BY  req_num";

        List<Map<String, Object>> mapList = indexFlushDao.queryIndexFlush(sql);

        String[] columnKeysCooks = new String[mapList.size()];
        double[] arrayPv = new double[mapList.size()];
        double[] arrayUv = new double[mapList.size()];

        String date = "";
        double total = 0;
        for (int i = 0; i < mapList.size(); i++) {
            date = (String) mapList.get(i).get("statdate");
            int reqNum = (int) mapList.get(i).get("req_num");
            String type;
            switch (reqNum) {
                case 10:
                    type = "6-10";
                    break;
                case 15:
                    type = "11-15";
                    break;
                case 20:
                    type = "16-20";
                    break;
                case 30:
                    type = "21-30";
                    break;
                case 50:
                    type = "31-50";
                    break;
                case 100:
                    type = "51-100";
                    break;
                case 101:
                    type = "100以上";
                    break;
                default:
                    type = String.valueOf(reqNum);
                    break;
            }
            //取值
            columnKeysCooks[i] = type;

            int pv = (int) mapList.get(i).get("pv");
            total += pv;
            arrayPv[i] = pv;

            int uv = (int) mapList.get(i).get("uv");
            arrayUv[i] = uv;

        }

        //图片名称
        String barTitle = "首页刷新次数统计" + date;
        //X轴
        String categoryLabelCooks = "类型";
        // y轴
        String valueLabelCooks = "次数";

        String[] rowKeysCooks = {"pv"};

        //数据集
        double[][] dataCooks = new double[1][];
        dataCooks[0] = arrayPv;

        sbContent.append("<h1>2.2、首页刷新次数统计</h1><br/>");
        sbContent.append("<img width=\"790\" height=\"670\" src=\"cid:img-index-flush-reqNum-total.jpg\" alt=\"\"/>");
        sbContent.append("<br/><br/>");

        // 首页刷新总请求PV,UV统计表格
        sbContent.append("<table class=\"gridtable\">");
        // 构造 title
        sbContent.append("<tr>" +
                "<th colspan=\"5\">首页刷新次数统计</th>" +
                "</tr>");
        sbContent.append("<tr>" +
                "<td>类型</td>" +
                "<td>次数</td>" +
                "<td>百分比</td>" +
                "</tr>");

        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(1);//控制保留小数点后几位，2：表示保留2位小数点
        for (int i = 0; i < columnKeysCooks.length; i++) {
            sbContent.append("<tr>");
            sbContent.append("<td>" + columnKeysCooks[i] + "</td>");
            sbContent.append("<td>" + (int) arrayPv[i] + "</td>");
            double percentIos = arrayPv[i] / total;
            sbContent.append("<td>" + nf.format(percentIos) + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/><br/>");

        //生成柱形图
        JFreeChartUtil.buildBarChat2(barTitle, categoryLabelCooks, valueLabelCooks, rowKeysCooks, columnKeysCooks, dataCooks,
                MailConstants.MAIL_CHART_PATH + "/img-index-flush-reqNum-total.jpg");

        return sbContent.toString();
    }

    /**
     * 首页刷新次数分客户端pv、uv统计  case5
     *
     * @return string
     */
    private String indexFlushReqNumClientSum() {

        StringBuffer sbContent = new StringBuffer();

        String sql = "select client,req_num, pv , uv,DATE_FORMAT (statdate, \"%Y-%m-%d\") AS statdate  " +
                "from dd_main_index_flush_client_req_sum WHERE  statdate BETWEEN ? AND ?";

        List<Map<String, Object>> mapList = indexFlushDao.queryIndexFlush(sql);

        String[] columnKeysCooks = new String[mapList.size() / 2];
        double[] arrayIosPv = new double[mapList.size() / 2];
        double[] arrayIosUv = new double[mapList.size() / 2];

        double[] arrayAndroidPv = new double[mapList.size() / 2];
        double[] arrayAndroidUv = new double[mapList.size() / 2];

        String date = "";
        double totalIosPv = 0, totalAndroidPv = 0;
        int index = 0, current = 0;
        for (int i = 0; i < mapList.size(); i++) {
            date = (String) mapList.get(i).get("statdate");
            //取值
            int client = (int) mapList.get(i).get("client");


            if (client == 3) {
                int reqNum = (int) mapList.get(i).get("req_num");
                String type;
                switch (reqNum) {
                    case 10:
                        type = "6-10";
                        break;
                    case 15:
                        type = "11-15";
                        break;
                    case 20:
                        type = "16-20";
                        break;
                    case 30:
                        type = "21-30";
                        break;
                    case 50:
                        type = "31-50";
                        break;
                    case 100:
                        type = "51-100";
                        break;
                    case 101:
                        type = "100以上";
                        break;
                    default:
                        type = String.valueOf(reqNum);
                        break;
                }

                columnKeysCooks[index] = type;

                int pv = (int) mapList.get(i).get("pv");
                totalIosPv += pv;
                arrayIosPv[index] = pv;

                int uv = (int) mapList.get(i).get("uv");
                arrayIosUv[index] = uv;
                index++;

            } else if (client == 4) {

                int pv = (int) mapList.get(i).get("pv");
                totalAndroidPv += pv;
                arrayAndroidPv[current] = pv;

                int uv = (int) mapList.get(i).get("uv");
                arrayAndroidUv[current] = uv;
                current++;
            }

        }
        //首页刷新分客户端pv、uv统计图
        sbContent.append("<h1>2.3、首页刷新次数分客户端pv、uv统计</h1><br/>");
        sbContent.append("<img width=\"790\" height=\"670\" src=\"cid:img-index-flush-client-reqNum-total.jpg\" alt=\"\"/>");
        sbContent.append("<br/><br/>");

        // 首页刷新分客户端pv、uv统计表格
        sbContent.append("<table class=\"gridtable\">");
        // 构造 title
        sbContent.append("<tr>" +
                "<th colspan=\"5\">首页刷新分客户端PV统计</th>" +
                "</tr>");
        sbContent.append("<tr>" +
                "<td>类型</td>" +
                "<td>ios-次数</td>" +
                "<td>ios-百分比</td>" +
                "<td>android-次数</td>" +
                "<td>android-百分比</td>" +
                "</tr>");

        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(1);//控制保留小数点后几位，2：表示保留2位小数点
        for (int i = 0; i < columnKeysCooks.length; i++) {
            sbContent.append("<tr>");
            sbContent.append("<td>" + columnKeysCooks[i] + "</td>");
            sbContent.append("<td>" + (int) arrayIosPv[i] + "</td>");
            double percentIos = arrayIosUv[i] / totalIosPv;
            sbContent.append("<td>" + nf.format(percentIos) + "</td>");
            sbContent.append("<td>" + (int) arrayAndroidPv[i] + "</td>");
            double percentAndroid = arrayAndroidPv[i] / totalAndroidPv;
            sbContent.append("<td>" + nf.format(percentAndroid) + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/><br/>");
        // 首页刷新分客户端pv、uv统计表格结束

        //图片名称
        String barTitle = "首页刷新次数分客户端PV,UV统计" + date;
        //X轴
        String categoryLabelCooks = "类型";
        // y轴
        String valueLabelCooks = "次数";

        String[] rowKeysCooks = {"ios-pv", "android-pv", "ios-uv", "android-uv"};

        //数据集
        double[][] dataCooks = new double[4][];
        dataCooks[0] = arrayIosPv;
        dataCooks[1] = arrayAndroidPv;

        dataCooks[2] = arrayIosUv;
        dataCooks[3] = arrayAndroidUv;

        //生成柱形图
        JFreeChartUtil.buildBarChat2(barTitle, categoryLabelCooks, valueLabelCooks, rowKeysCooks, columnKeysCooks, dataCooks,
                MailConstants.MAIL_CHART_PATH + "/img-index-flush-client-reqNum-total.jpg");
        return sbContent.toString();
    }


    /**
     * 解决查出的数据某一天没有数据  用0填充
     * @param map
     * @return
     */
    private List converList(Map map,int size){
        List list=new ArrayList();
        if (map.isEmpty()){  //如果查出的集合为空，将数据全部用0填充
            for (int i=0;i<size;i++){
                list.add(0);
            }
        }else {
            int index=1;
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key =(String) entry.getKey();
                boolean tempDate= compare_date(key,getNextDay(index));//比较日期是否相等
                if (tempDate){
                    list.add(entry.getValue());
                    index++;
                }else {
                    while (!tempDate){//防止连续几天都没有数据的情况
                        list.add(0);
                        index++;
                        tempDate= compare_date(key,getNextDay(index));
                    }
                    list.add(entry.getValue());
                    index++;
                }
            }

            boolean tempDate= compare_date(getNextDay(size),getNextDay(index));//假如我要7天的数据，查出只有前4天的数据， 后面三天用0填充
            for (;!tempDate;){
                list.add(0);
                index++;
                tempDate= compare_date(getNextDay(size),getNextDay(index));
            }
        }
        Collections.reverse(list);
        return list;
    }

    /**
     * 获得指定日期
     * @param index
     * @return
     */
    public static String getNextDay(int index) {
        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -index);
        date = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");//设置日期格式
        String currentDate=df.format(date);


        return currentDate;
    }

    /**
     * 判断两个日期是否相等
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static boolean compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return false;
            } else if (dt1.getTime() < dt2.getTime()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }


}
