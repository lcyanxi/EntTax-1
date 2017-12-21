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

import java.util.*;


@Repository("mailMainIndexInterstTagsService")
public class MailMainIndexInterstTagsService {

    @Autowired
    private IndexFlushDao indexFlushDao;


    private static final int WIDTH = 590;//图片宽
    private static final int HIGH = 500;  //图片高
    private static final int TITLEFONTSIZE = 24; //图片标题字体大小
    private static final int ITEMFONTSIZE = 20;//图片内容字体大小

    /**
     * 首页兴趣标签邮件报表
     *
     * @param sysMailSet
     */
    public void sendMainIndexInterstTagsMail(SysMailSet sysMailSet) {
        JavaSendMail javaSendMail = new JavaSendMail();
        System.out.println(sysMailSet);

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


        //1. 家庭汇总数据统计
        content.append(queryClickStat(nDays));
        content.append("<br/><br/>");
        content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");


        String[] imgPaths = new String[5];
        imgPaths[0] = MailConstants.MAIL_CHART_PATH + "/img-index-new-user-total.jpg";
        imgPaths[1] = MailConstants.MAIL_CHART_PATH + "/img-index-sex-precentage-type.jpg";
        imgPaths[2] = MailConstants.MAIL_CHART_PATH + "/img-index-profession-precentage-type.jpg";
        imgPaths[3] = MailConstants.MAIL_CHART_PATH + "/img-index-tags-precentage-type.jpg";
        imgPaths[4] = MailConstants.MAIL_CHART_PATH + "/img-index-age-precentage-type.jpg";
        try {
            //发送邮件
            javaSendMail.sendHtmlWithInnerImageEmail(subject, toAddress, content.toString(), imgPaths);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String queryClickStat(int day) {

        StringBuffer sbContent = new StringBuffer();

        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, day);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);


        List<Map<String, Object>> newUserList = indexFlushDao.queryMainIndexInterstTagsNewUserList(startDate, endDate);

        List<Map<String, Object>> totalList = indexFlushDao.queryMainIndexInterstTagsTotalList();


        sbContent.append("<h1>1 总用户</h1><br/>");
        sbContent.append("<table class=\"gridtable\">");
        for (Map<String, Object> totalMap : totalList) {
            sbContent.append("<tr>" +
                    "<td>" + "总人数" + "</td>" +
                    "<td>" + totalMap.get("total") + "</td>" +
                    "</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");

        sbContent.append("<h1>2.1 新增用户</h1><br/>");
        sbContent.append("<br/><br/>");
        sbContent.append("<img width=\"590\" height=\"500\" src=\"cid:img-index-new-user-total.jpg\" alt=\"\"/>");
        sbContent.append("<h1>2.2 新增用户明细数据</h1><br/>");
        sbContent.append("<br/><br/>");
        // 构建新增用户数据表格开始

        sbContent.append("<table class=\"gridtable\">");
        // 构造表头
        sbContent.append("<tr>" +
                "<th colspan=\"2\">新增用户数据</th>" +
                "</tr>");
        sbContent.append("<tr>" +
                "<th>日期</th>" +
                "<th>数量</th>");

        String[] columnKeysCooks = new String[day];
        double[] arryDataNewUser = new double[day];
        int i = 0;
        for (Map<String, Object> newlist : newUserList) {
            Date statdate = (Date) newlist.get("statdate");
            String temDate = DateUtil.dateToString(statdate, "yyyy-MM-dd");
            int value = (int) newlist.get("value");
            double tem = value;
            columnKeysCooks[i] = temDate;
            arryDataNewUser[i] = tem;
            i++;
            sbContent.append("<tr>" +
                    "<td>" + statdate + "</td>" +
                    "<td>" + value + "</td>" +
                    "</tr>"
            );

        }

        sbContent.append("</table>");
        sbContent.append("<br/>");


        //构建新增用户折线图 开始
        String[] rowKeysKt = {"数量"};

        //图片名称
        String chartTitleKt = "新增用户";

        //X轴
        String categoryLabelCooks = "日期";

        // y轴
        String valueLabelCooks = "数量";

        //数据集
        double[][] dataNewUser = new double[1][];
        dataNewUser[0] = arryDataNewUser;


        //新增用户数据折线图
        JFreeChartUtil.buildLineChat(chartTitleKt, categoryLabelCooks, valueLabelCooks, rowKeysKt, columnKeysCooks, dataNewUser,
                MailConstants.MAIL_CHART_PATH + "/img-index-new-user-total.jpg");


        Map<String, Double> professionMap = new HashMap<>();
        Map<String, Double> birthdayMap = new LinkedHashMap<>();
        Map<String, Double> tagsMap = new LinkedHashMap<>();
        Map<String, Double> sexMap = new LinkedHashMap<>();
        List<Map<String, Object>> list = indexFlushDao.queryMainIndexInterstTagsList();

        for (Map<String, Object> map : list) {

            String type = (String) map.get("type");
            String type_name = (String) map.get("type_name");
            int value = (int) map.get("value");
            double tempValue = value;

            if (type.equals("profession")) {
                professionMap.put(type_name, tempValue);
            }
            if (type.equals("birthday")) {
                birthdayMap.put(type_name, tempValue);
            }
            if (type.equals("interested_tags")) {
                tagsMap.put(type_name, tempValue);
            }
            if (type.equals("gender")) {
                sexMap.put(type_name, tempValue);
            }
        }

        sbContent.append("<h1>3.1 性别占比</h1><br/>");
        sbContent.append(sexPrecentageStat(sexMap));
        sbContent.append("<h1>3.2 年龄占比</h1><br/>");
        sbContent.append(agePrecentageStat(birthdayMap));
        sbContent.append("<h1>3.3 职业占比</h1><br/>");
        sbContent.append(profPrecentageStat(professionMap));
        sbContent.append("<h1>3.4 兴趣标签占比</h1><br/>");
        sbContent.append(tagsBarStat(tagsMap));


        return sbContent.toString();
    }


    /**
     * 性别所占百分比
     *
     * @return 性别百分比饼状图
     */
    private StringBuffer sexPrecentageStat(Map<String, Double> sexTemMap) {

        StringBuffer sbContent = new StringBuffer();

        sbContent.append("<img width=\"490\" height=\"400\" src=\"cid:img-index-sex-precentage-type.jpg\" alt=\"\"/>");

        //生成图片
        String chartTitle = "性别";
        JFreeChartUtil.buildPieChat(chartTitle, sexTemMap, MailConstants.MAIL_CHART_PATH + "/img-index-sex-precentage-type.jpg", WIDTH, HIGH, TITLEFONTSIZE, ITEMFONTSIZE);
        //生成图片结束

        return sbContent;
    }

    /**
     * 年龄所占百分比
     *
     * @return 性别百分比饼状图
     */
    private StringBuffer agePrecentageStat(Map<String, Double> sexTemMap) {

        StringBuffer sbContent = new StringBuffer();

        sbContent.append("<img width=\"490\" height=\"400\" src=\"cid:img-index-age-precentage-type.jpg\" alt=\"\"/>");

        //生成图片
        String chartTitle = "年龄";
        JFreeChartUtil.buildPieChat(chartTitle, sexTemMap, MailConstants.MAIL_CHART_PATH + "/img-index-age-precentage-type.jpg", WIDTH, HIGH, TITLEFONTSIZE, ITEMFONTSIZE);
        //生成图片结束

        return sbContent;
    }

    /**
     * 职业所占百分比
     *
     * @return 性别百分比饼状图
     */
    private StringBuffer profPrecentageStat(Map<String, Double> sexTemMap) {

        StringBuffer sbContent = new StringBuffer();

        sbContent.append("<img width=\"490\" height=\"400\" src=\"cid:img-index-profession-precentage-type.jpg\" alt=\"\"/>");

        //生成图片
        String chartTitle = "职业";
        JFreeChartUtil.buildPieChat(chartTitle, sexTemMap, MailConstants.MAIL_CHART_PATH + "/img-index-profession-precentage-type.jpg", WIDTH, HIGH, TITLEFONTSIZE, ITEMFONTSIZE);
        //生成图片结束

        return sbContent;
    }

    /**
     * 兴趣标签所占百分比
     *
     * @return 性别百分比饼状图
     */
    private StringBuffer interPrecentageStat(Map<String, Double> sexTemMap) {

        StringBuffer sbContent = new StringBuffer();

        sbContent.append("<img width=\"490\" height=\"400\" src=\"cid:img-index-tags-precentage-type.jpg\" alt=\"\"/>");

        //生成图片
        String chartTitle = "兴趣标签";
        JFreeChartUtil.buildPieChat(chartTitle, sexTemMap, MailConstants.MAIL_CHART_PATH + "/img-index-tags-precentage-type.jpg", WIDTH, HIGH, TITLEFONTSIZE, ITEMFONTSIZE);
        //生成图片结束

        return sbContent;
    }


    /**
     * 兴趣标签
     * @param tagsTemMap
     * @return 兴趣标签柱形图
     */
    private StringBuffer tagsBarStat(Map<String, Double> tagsTemMap) {
        StringBuffer sbContent = new StringBuffer();

        int size = tagsTemMap.size();
        String[] columnKeysCooks = new String[size];
        double[] arrayCountry = new double[size];

        int i = 0;
        Iterator iter = tagsTemMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            double value = (double) entry.getValue();
            columnKeysCooks[i] = key;
            arrayCountry[i] = value;
            i++;
        }

        //兴趣标签柱形图 开始
        String[] rowKeysCooks = {"兴趣标签"};

        //图片名称
        String chartTitleCooks = "兴趣标签数据分布";

        //X轴
        String categoryLabelCooks = "名称";

        // y轴
        String valueLabelCooks = "数量";

        //数据集
        double[][] dataCooks = new double[1][];
        dataCooks[0] = arrayCountry;

        sbContent.append("<img width=\"990\" height=\"500\" src=\"cid:img-index-tags-precentage-type.jpg\" alt=\"\"/>");

        //生成柱形图
        JFreeChartUtil.buildBarChat3(chartTitleCooks, categoryLabelCooks, valueLabelCooks, rowKeysCooks, columnKeysCooks, dataCooks,
                MailConstants.MAIL_CHART_PATH + "/img-index-tags-precentage-type.jpg");

        return sbContent;
    }

}
