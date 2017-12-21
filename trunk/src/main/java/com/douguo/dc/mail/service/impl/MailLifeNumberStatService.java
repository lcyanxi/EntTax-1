package com.douguo.dc.mail.service.impl;

import com.douguo.dc.article.lifenumber.service.LifeNumberService;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JFreeChartUtil;
import com.douguo.dc.util.JavaSendMail;
import com.douguo.dc.util.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 生活号数据统计
 * @author zjf
 * @date 2017-12-06
 *
 * 邮件图片标识字典
 * a: 主动入驻用户对比(7日前)趋势图
 * b: 新增文章数对比(7日前)趋势图
 * c: 优质文章占比趋势图
 * d: 文章内关注数趋势图
 * e: 文章内点赞数趋势图
 * f: 文章内评论数趋势图
 * g: 文章浏览量趋势图
 *
 */
@Repository("mailLifeNumberStatService")
public class MailLifeNumberStatService {

    @Autowired
    private LifeNumberService lifeNumberService;

    /**
     * 生活号数据邮件报表
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
            content.append("<style type=\"text/css\"> table.gridtable " +
                    "{ font-family: verdana,arial,sans-serif; font-size:11px; color:#333333;" +
                    " border-width: 1px; border-color: #666666; border-collapse: collapse; } " +
                    "table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; " +
                    "border-color: #666666; background-color: #dedede; } " +
                    "table.gridtable td { border-width: 1px; padding: 8px; " +
                    "border-style: solid; border-color: #666666; background-color: #ffffff; " +
                    "} </style>");

            if (sysMailSet != null) {
                subject = sysMailSet.getSubject() + "_" + curDate;
                to = sysMailSet.getMailTo();
            } else {
                subject = "生活号整体数据日报_" + curDate;
                to = "zhangjianfei@douguo.com";
            }

            // 1. 基本数据统计
            String articleBaseStat = baseStat(15);
            
            content.append("<h1>生活号文章数据统计日报</h1><br/>");
            content.append(articleBaseStat);
            content.append("<br/><br/>");
            content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");
            
            String[] imgPaths = new String[7];
            imgPaths[0] = MailConstants.MAIL_CHART_PATH + "/img-lifenumber-a.jpg";
            imgPaths[1] = MailConstants.MAIL_CHART_PATH + "/img-lifenumber-b.jpg";
            imgPaths[2] = MailConstants.MAIL_CHART_PATH + "/img-lifenumber-c.jpg";
            imgPaths[3] = MailConstants.MAIL_CHART_PATH + "/img-lifenumber-d.jpg";
            imgPaths[4] = MailConstants.MAIL_CHART_PATH + "/img-lifenumber-e.jpg";
            imgPaths[5] = MailConstants.MAIL_CHART_PATH + "/img-lifenumber-f.jpg";
            imgPaths[6] = MailConstants.MAIL_CHART_PATH + "/img-lifenumber-g.jpg";

            javaSendMail.sendHtmlWithInnerImageEmail(subject, to, content.toString(), imgPaths);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构造邮件内容
     * @return
     */
    private String baseStat(Integer beforeDays) {

        StringBuffer sbContent = new StringBuffer();

        // 构建查询日期
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String startDate = DateUtil.getSpecifiedDayBefore(endDate, beforeDays);
        // 构建7日前查询对比日期
        String endDateAgo = DateUtil.getSpecifiedDayBefore(endDate, 7);
        String startDateAgo = DateUtil.getSpecifiedDayBefore(endDateAgo, beforeDays);

        List<Map<String, Object>> rowlist = lifeNumberService.queryLifeNumberList(startDate, endDate);
        List<Map<String, Object>> rowlistAgo = lifeNumberService.queryLifeNumberList(startDateAgo, endDateAgo);

        double[] articles = new double[rowlist.size()];
        double[] articlesAgo = new double[rowlistAgo.size()];
        double[] applyUsers = new double[rowlist.size()];
        double[] applyUsersAgo = new double[rowlistAgo.size()];
        double[] qualityArticlesRate = new double[rowlist.size()];
        double[] authorfollows = new double[rowlist.size()];
        double[] articleFavs = new double[rowlist.size()];
        double[] articleCmmts = new double[rowlist.size()];
        double[] articleViews = new double[rowlist.size()];

        String[] columnKeys = new String[rowlist.size()];

        // 构建表格开始
        sbContent.append("<table class=\"gridtable\">");
        // 构造 title
        sbContent.append("<tr>" +
                "<th colspan=\"13\">生活号汇总数据</th>" +
                "</tr>");
        sbContent.append("<tr>" +
                "<td>日期</td>" +
                "<td>新增文章数</td>" +
                "<td>运营上传文章数</td>" +
                "<td>用户上传文章数</td>" +
                "<td>爬取文章数</td>" +
                "<td>优质文章数</td>" +
                "<td>优质文章占比</td>" +
                "<td>累计申请入驻数</td>" +
                "<td>文章浏览数</td>" +
                "<td>文章浏览UV</td>" +
                "<td>文章内关注数</td>" +
                "<td>文章点赞数</td>" +
                "<td>文章评论数</td>" +
                "</tr>");

        // 遍历对比数据
        int indexAgo = 0;
        for (int i=0; i<rowlistAgo.size(); i++) {
            Integer articles_all = (Integer) rowlistAgo.get(i).get("articles");
            Integer apply_all_users = (Integer) rowlistAgo.get(i).get("apply_all_users");
            articlesAgo[indexAgo] = Double.valueOf(articles_all.toString());
            applyUsersAgo[indexAgo] = Double.valueOf(apply_all_users.toString());

            indexAgo ++;
        }

        // 遍历查询结果
        int index = 0;
        for (int i=0; i<rowlist.size(); i++) {
            //取值
            Integer articles_all = (Integer) rowlist.get(i).get("articles");
            Integer articles_upload = (Integer) rowlist.get(i).get("articles_upload");
            Integer articles_user_upload = (Integer) rowlist.get(i).get("articles_user_upload");
            Integer articles_grab = (Integer) rowlist.get(i).get("articles_grab");
            Integer quality_num = (Integer) rowlist.get(i).get("quality_articles");
            // 计算优质文章占比
            Double rate = Double.valueOf(MathUtil.parseIntDivisionStr(quality_num, articles_all));

            // Double all_users = Double.valueOf(((Integer) rowlist.get(i).get("all_users")).toString());
            Integer apply_all_users = (Integer) rowlist.get(i).get("apply_all_users");
            // Double invite_all_users = Double.valueOf(((Integer) rowlist.get(i).get("invite_all_users")).toString());

            Integer article_views_pv = (Integer) rowlist.get(i).get("article_views_pv");
            Integer article_views_uv = (Integer) rowlist.get(i).get("article_views_uv");

            Integer article_user_follows = (Integer) rowlist.get(i).get("article_user_follows");
            Integer article_user_favs = (Integer) rowlist.get(i).get("article_user_favs");
            Integer article_user_cmmts = (Integer) rowlist.get(i).get("article_user_cmmts");

            String statdate = rowlist.get(i).get("statdate").toString();
            String strWeek = DateUtil.getWeekDay(statdate);

            // 趋势图数据构建
            // X 日期数据
            columnKeys[index] = statdate + "(" + strWeek + ")";
            // Y 数据构建
            articles[index] = Double.valueOf(articles_all.toString());
            qualityArticlesRate[index] = rate;
            authorfollows[index] = Double.valueOf(article_user_follows.toString());
            articleFavs[index] = Double.valueOf(article_user_favs.toString());
            articleCmmts[index] = Double.valueOf(article_user_cmmts.toString());
            articleViews[index] = Double.valueOf(article_views_pv.toString());
            applyUsers[index] = Double.valueOf(apply_all_users.toString());

            index++;

            //1.填值到表格
            sbContent.append("<tr>");
            sbContent.append("<td>" + statdate + "(" + strWeek + ")</td>");
            sbContent.append("<td>" + String.valueOf(articles_all) + "</td>");
            sbContent.append("<td>" + String.valueOf(articles_upload) + "</td>");
            sbContent.append("<td>" + String.valueOf(articles_user_upload) + "</td>");
            sbContent.append("<td>" + String.valueOf(articles_grab) + "</td>");
            sbContent.append("<td>" + String.valueOf(quality_num) + "</td>");
            sbContent.append("<td>" + String.valueOf(rate) + "%</td>");
            sbContent.append("<td>" + String.valueOf(apply_all_users) + "</td>");
            sbContent.append("<td>" + String.valueOf(article_views_pv) + "</td>");
            sbContent.append("<td>" + String.valueOf(article_views_uv) + "</td>");
            sbContent.append("<td>" + String.valueOf(article_user_follows) + "</td>");
            sbContent.append("<td>" + String.valueOf(article_user_favs) + "</td>");
            sbContent.append("<td>" + String.valueOf(article_user_cmmts) + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");
        // 构造数据表格结束

        if ( rowlist.size()==rowlistAgo.size() ) {
            //构建首页汇总数据折线图 开始
            // a: 主动入驻用户对比(7日前)趋势图
            String[] rowKeysApplyUsers = {"人数", "7日前同比人数"};
            // 图片名称
            String chartTitleApplyUsers = "主动入驻用户对比(7日前)趋势图";
            // X轴
            String categoryLabelApplyUsers = "日期";
            // y轴
            String valueLabelApplyUsers = "人数";
            // 构建数据集
            double[][] dataApplyUsers = new double[2][];
            dataApplyUsers[0] = applyUsers;
            dataApplyUsers[1] = applyUsersAgo;
            // 构建趋势图a
            JFreeChartUtil.buildLineChat(chartTitleApplyUsers, categoryLabelApplyUsers, valueLabelApplyUsers, rowKeysApplyUsers, columnKeys, dataApplyUsers,
                    MailConstants.MAIL_CHART_PATH + "/img-lifenumber-a.jpg");
        }

        if ( rowlist.size()==rowlistAgo.size() ) {
            // b: 新增文章数对比(7日前)趋势图
            String[] rowKeysArticles = {"新增文章数", "7日前同比新增数"};
            // 图片名称
            String chartTitleArticles = "新增文章数对比(7日前)趋势图";
            // X轴
            String categoryLabelArticles = "日期";
            // y轴
            String valueLabelArticles = "新增数";
            // 构建数据集
            double[][] dataArticles = new double[2][];
            dataArticles[0] = articles;
            dataArticles[1] = articlesAgo;
            // 构建趋势图 b
            JFreeChartUtil.buildLineChat(chartTitleArticles, categoryLabelArticles, valueLabelArticles, rowKeysArticles, columnKeys, dataArticles,
                    MailConstants.MAIL_CHART_PATH + "/img-lifenumber-b.jpg");
        }

        // c: 优质文章占比趋势图
        String[] rowKeysRate = {"优质文章占比数"};
        // 图片名称
        String chartTitleRate = "优质文章占比趋势图";
        // X轴
        String categoryLabelRate = "日期";
        // y轴
        String valueLabelRate = "占比数";
        // 构建数据集
        double[][] dataRate = new double[1][];
        dataRate[0] = qualityArticlesRate;
        // 构建趋势图 C
        JFreeChartUtil.buildLineChat(chartTitleRate, categoryLabelRate, valueLabelRate, rowKeysRate, columnKeys, dataRate,
                MailConstants.MAIL_CHART_PATH + "/img-lifenumber-c.jpg");

        // d: 文章内关注趋势图
        String[] rowKeysFollows = {"文章内关注数"};
        // 图片名称
        String chartTitleFollows = "文章内关注数趋势图";
        // X轴
        String categoryLabelFollows = "日期";
        // y轴
        String valueLabelFollows = "关注数";
        // 构建数据集
        double[][] dataFollows = new double[1][];
        dataFollows[0] = authorfollows;
        // 构建趋势图 d
        JFreeChartUtil.buildLineChat(chartTitleFollows, categoryLabelFollows, valueLabelFollows, rowKeysFollows, columnKeys, dataFollows,
                MailConstants.MAIL_CHART_PATH + "/img-lifenumber-d.jpg");

        // e: 文章内点赞趋势图
        String[] rowKeysFav = {"文章内点赞数"};
        // 图片名称
        String chartTitleFav = "文章内点赞数趋势图";
        // X轴
        String categoryLabelFav = "日期";
        // y轴
        String valueLabelFav = "点赞数";
        // 构建数据集
        double[][] dataFav = new double[1][];
        dataFav[0] = articleFavs;
        // 构建趋势图 d
        JFreeChartUtil.buildLineChat(chartTitleFav, categoryLabelFav, valueLabelFav, rowKeysFav, columnKeys, dataFav,
                MailConstants.MAIL_CHART_PATH + "/img-lifenumber-e.jpg");

        // f: 文章内评论趋势图
        String[] rowKeysCmmt = {"文章内评论数"};
        // 图片名称
        String chartTitleCmmt = "文章内评论数趋势图";
        // X轴
        String categoryLabelCmmt = "日期";
        // y轴
        String valueLabelCmmt = "评论数";
        // 构建数据集
        double[][] dataCmmt = new double[1][];
        dataCmmt[0] = articleCmmts;
        // 构建趋势图 f
        JFreeChartUtil.buildLineChat(chartTitleCmmt, categoryLabelCmmt, valueLabelCmmt, rowKeysCmmt, columnKeys, dataCmmt,
                MailConstants.MAIL_CHART_PATH + "/img-lifenumber-f.jpg");

        // g: 文章内评论趋势图
        String[] rowKeysView = {"文章内浏览数"};
        // 图片名称
        String chartTitleView = "文章内浏览数趋势图";
        // X轴
        String categoryLabelView = "日期";
        // y轴
        String valueLabelView = "浏览数";
        // 构建数据集
        double[][] dataView = new double[1][];
        dataView[0] = articleViews;
        // 构建趋势图 g
        JFreeChartUtil.buildLineChat(chartTitleView, categoryLabelView, valueLabelView, rowKeysView, columnKeys, dataView,
                MailConstants.MAIL_CHART_PATH + "/img-lifenumber-g.jpg");

        //构建家庭报表折线图 结束

        //添加图片到邮件中
        sbContent.append("<h1>趋势统计</h1><br/>");
        sbContent.append("<table class=\"gridtable\">");

        sbContent.append(
                "    <tr>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-lifenumber-a.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-lifenumber-b.jpg\"/></td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-lifenumber-c.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-lifenumber-d.jpg\"/></td>" +
                "    </tr>"+
                "    <tr>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-lifenumber-e.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-lifenumber-f.jpg\"/></td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-lifenumber-g.jpg\"/></td>" +
                "    </tr>"
                );

        sbContent.append("</table>");
        sbContent.append("<br/>");

        return sbContent.toString();
    }



}
