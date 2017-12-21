package com.douguo.dc.mail.service.impl;

import com.douguo.dc.cook.service.CookService;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mail.service.SysMailSetService;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JFreeChartUtil;
import com.douguo.dc.util.JavaSendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("mailCookSumStatService")
public class MailCookSumStatService {

    @Autowired
    private CookService cookService;

    @Autowired
    private SysMailSetService sysMailSetService;

    /**
     * 发送菜谱相关统计邮件
     *
     * @param sysMailSet
     */
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
                subject = "菜谱汇总数据日报_" + curDate;
                to = "zhangyaozhou@douguo.com";
            }

            String strDesc = sysMailSet.getDesc();
            Integer nDays = 8;
            if (org.apache.commons.lang3.StringUtils.isNotBlank(strDesc)) {
                try {
                    nDays = Integer.parseInt(strDesc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 1. 基本数据统计
            String cookBaseStat = cookSumBaseStat(nDays);
            content.append("<h1>1、菜谱汇总数据统计</h1><br/>");
            content.append(cookBaseStat);
            content.append("<br/><br/>");

            content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

            String[] imgPaths = new String[2];
            imgPaths[0] = MailConstants.MAIL_CHART_PATH + "/img-cook-sum-cooks.jpg";
            imgPaths[1] = MailConstants.MAIL_CHART_PATH + "/img-cook-sum-comments.jpg";
            javaSendMail.sendHtmlWithInnerImageEmail(subject, to, content.toString(), imgPaths);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 构造邮件内容
     *
     * @return
     */
    private String cookSumBaseStat(Integer beforeDays) {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, beforeDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        //
        List<Map<String, Object>> rowlist = cookService.queryCookSumStatList(startDate, endDate);

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");

        // 构造 title
        sbContent.append("<tr><th>序号</th><th>日期</th><th>菜谱数</th><th>活动菜谱数</th><th>菜谱人数</th><th>首次上传菜谱数</th>" +
                "<th>首次上传菜谱人数</th><th>菜谱评论数</th><th>菜谱评论人数</th><th>菜谱评论回复数</th><th>菜谱评论回复人数</th><th>新用户菜谱评论回复数</th><th>新用户菜谱评论回复人数</th></tr>");

        int i = 0;

        //
        for (Map<String, Object> map : rowlist) {
            i++;
            Date statdate = (Date) map.get("statdate");
            String strWeek = DateUtil.getWeekDay(statdate);
            Integer cooks = (Integer) map.get("cooks");
            Integer cook_users = (Integer) map.get("cook_users");
            Integer huodong_cooks = (Integer) map.get("huodong_cooks");
            Integer first_cooks = (Integer) map.get("first_cooks");
            Integer first_cook_users = (Integer) map.get("first_cook_users");

            Integer cook_comments = (Integer) map.get("cook_comments");
            Integer cook_comment_users = (Integer) map.get("cook_comment_users");
            Integer cook_comment_replys = (Integer) map.get("cook_comment_replys");
            Integer cook_comment_reply_users = (Integer) map.get("cook_comment_reply_users");
            Integer first_cook_comment_replys = (Integer) map.get("first_cook_comment_replys");
            Integer first_cook_comment_reply_users = (Integer) map.get("first_cook_comment_reply_users");

            //
            sbContent.append("<tr>");
            sbContent.append("<td>" + i + "</td>");
            sbContent.append("<td>" + statdate + "(" + strWeek + ")</td>");
            sbContent.append("<td>" + cooks + "</td>");
            sbContent.append("<td>" + huodong_cooks + "</td>");
            sbContent.append("<td>" + cook_users + "</td>");
            sbContent.append("<td>" + first_cooks + "</td>");
            sbContent.append("<td>" + first_cook_users + "</td>");

            sbContent.append("<td>" + cook_comments + "</td>");
            sbContent.append("<td>" + cook_comment_users + "</td>");
            sbContent.append("<td>" + cook_comment_replys + "</td>");
            sbContent.append("<td>" + cook_comment_reply_users + "</td>");
            sbContent.append("<td>" + first_cook_comment_replys + "</td>");
            sbContent.append("<td>" + first_cook_comment_reply_users + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");

        // ======= 生成图片 - begin
        double[] arrayCooks = new double[rowlist.size()];
        double[] arrayCookComments = new double[rowlist.size()];
        //
        String[] rowKeysCooks = {"菜谱数"};
        String[] rowKeysCookComments = {"菜谱评论数"};

        String[] columnKeysCooks = new String[rowlist.size()];

        for (int j = 0; j < rowlist.size(); j++) {
            //获取日期 : 格式：2016-09-27(二)
            columnKeysCooks[j] = String.valueOf(rowlist.get(j).get("statdate")) + "(" + DateUtil.getWeekDay(String.valueOf(rowlist.get(j).get("statdate"))) + ")";
            arrayCooks[j] = (Integer) rowlist.get(j).get("cooks");
            arrayCookComments[j] = (Integer) rowlist.get(j).get("cook_comments");
        }
        //图片名称
        String chartTitleCooks = "菜谱趋势";
        String chartTitleCookComments = "菜谱评论趋势";

        //X轴
        String categoryLabelCooks = "日期";
        String categoryLabelCookComments = "日期";

        // y轴
        String valueLabelCooks = "菜谱数";
        String valueLabelCookComments = "菜谱评论数";

        //数据集
        double[][] dataCooks = new double[1][];
        double[][] dataCookComments = new double[1][];

        dataCooks[0] = arrayCooks;
        dataCookComments[0] = arrayCookComments;

        //菜谱数折线图
        JFreeChartUtil.buildLineChat(chartTitleCooks, categoryLabelCooks, valueLabelCooks, rowKeysCooks, columnKeysCooks, dataCooks,
                MailConstants.MAIL_CHART_PATH + "/img-cook-sum-cooks.jpg");

        //评论折线图
        JFreeChartUtil.buildLineChat(chartTitleCookComments, categoryLabelCookComments, valueLabelCookComments, rowKeysCookComments, columnKeysCooks, dataCookComments,
                MailConstants.MAIL_CHART_PATH + "/img-cook-sum-comments.jpg");
        // ======= 生成图片 - end

        //添加图片到邮件中
        sbContent.append("<h1>2、趋势统计</h1><br/>");
        sbContent.append("<table class=\"gridtable\">");

        sbContent.append("<tr>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"1\" align=\"center\">整体数据</td>" +
                "        <td style=\"background-color:#ff7f00\" colspan=\"2\" align=\"center\">菜谱数</td>" +
                "        <td style=\"background-color:#ff7f00\" colspan=\"2\" align=\"center\">菜谱评论</td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td rowspan=\"1\">一周趋势</td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-cook-sum-cooks.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-cook-sum-comments.jpg\"/></td>" +
                "    </tr>");

        sbContent.append("</table>");
        sbContent.append("<br/>");

        return sbContent.toString();
    }
}