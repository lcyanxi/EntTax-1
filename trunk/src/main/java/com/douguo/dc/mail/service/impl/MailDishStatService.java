package com.douguo.dc.mail.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.douguo.dc.dish.service.DishService;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mail.service.SysMailSetService;
import com.douguo.dc.mail.service.impl.tmp.CommonMailQtypeStatService;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JFreeChartUtil;
import com.douguo.dc.util.JavaSendMail;
import com.douguo.dc.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("mailDishStatService")
public class MailDishStatService {

    @Autowired
    private DishService dishService;
    @Autowired
    private CommonMailQtypeStatService commonMailQtypeStatService;

    @Autowired
    private SysMailSetService sysMailSetService;

    /**
     * 发送作品相关统计邮件
     *
     * @param sysMailSet
     */
    public void sendDishSumMail(SysMailSet sysMailSet) {
        JavaSendMail javaSendMail = new JavaSendMail();
        try {
            String strConfig = StringUtils.trimToEmpty(sysMailSet.getConfig());
            JSONObject jsonConfig = JsonUtil.parseStrToJsonObj(strConfig);
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
                subject = "作品数据日报_" + curDate;
                to = "zhangyaozhou@douguo.com";
            }

            //获取需统计天数
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
            String dishBaseStat = dishBaseStat(nDays);
            content.append("<h1>1、作品数据统计</h1><br/>");
            content.append(dishBaseStat);
            content.append("<br/><br/>");


            if (org.apache.commons.lang3.StringUtils.isNotBlank(jsonConfig.getString("qtypeStr")) && org.apache.commons.lang3.StringUtils.isNotBlank(jsonConfig.getString("qtypeCh"))) {
                Map targetMap = commonMailQtypeStatService.qtypeClassificationStatOderByNum(jsonConfig);
                content.append(targetMap.get("sbContent"));
                List imgUrlArr = (List) targetMap.get("imgUrlArr");
                imgUrlArr.add("img-dish-dishs.jpg");
                imgUrlArr.add("img-cook-dish.jpg");
                imgUrlArr.add("img-tag-dishs.jpg");
                imgUrlArr.add("img-blank-dishs.jpg");
                imgUrlArr.add("img-dish-comments.jpg");
                imgUrlArr.add("img-cook_dish-comments.jpg");
                imgUrlArr.add("img-tag_dish_comments.jpg");
                imgUrlArr.add("img-blank_dish_comments.jpg");
                imgUrlArr.add("img-dish_favs.jpg");
                imgUrlArr.add("img-cook_dish_favs.jpg");
                imgUrlArr.add("img-tag_dish_favs.jpg");
                imgUrlArr.add("img-blank_dish_favs.jpg");


                String[] imgPaths = new String[imgUrlArr.size()];
                for (int i = 0; i < imgUrlArr.size(); i++) {
                    imgPaths[i] = MailConstants.MAIL_CHART_PATH + "/" + imgUrlArr.get(i);
                }
                javaSendMail.sendHtmlWithInnerImageEmail(subject, to, content.toString(), imgPaths);
            } else {
                JavaSendMail.sendHtmlEmail(subject, to, content.toString());
            }

            content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

            //javaSendMail.sendHtmlEmail(subject, to, content.toString());
/*
            String[] imgPaths = new String[12];
            imgPaths[0] = MailConstants.MAIL_CHART_PATH + "/img-dish-dishs.jpg";
            imgPaths[1] = MailConstants.MAIL_CHART_PATH + "/img-cook-dish.jpg";
            imgPaths[2] = MailConstants.MAIL_CHART_PATH + "/img-tag-dishs.jpg";
            imgPaths[3] = MailConstants.MAIL_CHART_PATH + "/img-blank-dishs.jpg";
            imgPaths[4] = MailConstants.MAIL_CHART_PATH + "/img-dish-comments.jpg";
            imgPaths[5] = MailConstants.MAIL_CHART_PATH + "/img-cook_dish-comments.jpg";
            imgPaths[6] = MailConstants.MAIL_CHART_PATH + "/img-tag_dish_comments.jpg";
            imgPaths[7] = MailConstants.MAIL_CHART_PATH + "/img-blank_dish_comments.jpg";
            imgPaths[8] = MailConstants.MAIL_CHART_PATH + "/img-dish_favs.jpg";

            imgPaths[9] = MailConstants.MAIL_CHART_PATH + "/img-cook_dish_favs.jpg";
            imgPaths[10] = MailConstants.MAIL_CHART_PATH + "/img-tag_dish_favs.jpg";
            imgPaths[11] = MailConstants.MAIL_CHART_PATH + "/img-blank_dish_favs.jpg";*/
         //   javaSendMail.sendHtmlWithInnerImageEmail(subject, to, content.toString(), imgPaths);
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
    private String dishBaseStat(Integer beforeDays) {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, beforeDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        //
        List<Map<String, Object>> rowlist = dishService.queryDishSumList(startDate, endDate);

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");

        // 构造 title
        sbContent.append("<tr><th>序号</th><th>日期</th><th>作品数</th><th>菜谱作品数</th><th>(菜谱作品数/作品数)占比</th><th>秀美食话题作品数</th><th>秀美食非话题作品数</th>" +
                "<th>作品人数</th><th>菜谱作品人数</th><th>秀美食话题作品人数</th><th>秀美食非话题作品人数</th>" +
                "<th>作品总赞数</th><th>菜谱作品赞数</th><th>秀美食话题作品赞数</th><th>秀美食非话题作品赞数</th>" +
                "<th>作品总赞人数</th><th>菜谱作品赞人数</th><th>秀美食话题作品赞人数</th><th>秀美食非话题作品赞人数</th>" +
                "<th>作品总评论数</th><th>菜谱作品评论数</th><th>秀美食话题作品评论数</th><th>秀美食非话题作品评论数</th>" +
                "<th>作品总评论人数</th><th>菜谱作品评论人数</th><th>秀美食话题作品评论人数</th><th>秀美食非话题作品评论人数</th></tr>");

        int i = 0;

        //
        for (Map<String, Object> map : rowlist) {
            i++;
            Date statdate = (Date) map.get("statdate");
            String strWeek = DateUtil.getWeekDay(statdate);
            Integer dishs = (Integer) map.get("dishs");
            Integer cook_dishs = (Integer) map.get("cook_dishs");
            Integer tag_dishs = (Integer) map.get("tag_dishs");
            Integer blank_dishs = (Integer) map.get("blank_dishs");

            Integer dish_users = (Integer) map.get("dish_users");
            Integer cook_dish_users = (Integer) map.get("cook_dish_users");
            Integer tag_dish_users = (Integer) map.get("tag_dish_users");
            Integer blank_dish_users = (Integer) map.get("blank_dish_users");

            //喜欢
            Integer dish_favs = (Integer) map.get("dish_favs");
            Integer dish_fav_users = (Integer) map.get("dish_fav_users");
            Integer cook_dish_favs = (Integer) map.get("cook_dish_favs");
            Integer cook_dish_fav_users = (Integer) map.get("cook_dish_fav_users");
            Integer tag_dish_favs = (Integer) map.get("tag_dish_favs");
            Integer tag_dish_fav_users = (Integer) map.get("tag_dish_fav_users");
            Integer blank_dish_favs = (Integer) map.get("blank_dish_favs");
            Integer blank_dish_fav_users = (Integer) map.get("blank_dish_fav_users");

            //评论
            Integer dish_comments = (Integer) map.get("dish_comments");
            Integer dish_comment_users = (Integer) map.get("dish_comment_users");
            Integer cook_dish_comments = (Integer) map.get("cook_dish_comments");
            Integer cook_dish_comment_users = (Integer) map.get("cook_dish_comment_users");
            Integer tag_dish_comments = (Integer) map.get("tag_dish_comments");
            Integer tag_dish_comment_users = (Integer) map.get("tag_dish_comment_users");
            Integer blank_dish_comments = (Integer) map.get("blank_dish_comments");
            Integer blank_dish_comment_users = (Integer) map.get("blank_dish_comment_users");

            double percent=((double)cook_dishs/(double)dishs)*100;

            //
            sbContent.append("<tr>");
            sbContent.append("<td>" + i + "</td>");
            sbContent.append("<td>" + statdate + "(" + strWeek + ")</td>");
            sbContent.append("<td>" + dishs + "</td>");
            sbContent.append("<td>" + cook_dishs + "</td>");
            sbContent.append("<td>" +  String.format("%.1f", percent) + "%</td>");
            sbContent.append("<td>" + tag_dishs + "</td>");
            sbContent.append("<td>" + blank_dishs + "</td>");
            sbContent.append("<td>" + dish_users + "</td>");
            sbContent.append("<td>" + cook_dish_users + "</td>");
            sbContent.append("<td>" + tag_dish_users + "</td>");
            sbContent.append("<td>" + blank_dish_users + "</td>");
            //喜欢
            sbContent.append("<td>" + dish_favs + "</td>");
            sbContent.append("<td>" + cook_dish_favs + "</td>");
            sbContent.append("<td>" + tag_dish_favs + "</td>");
            sbContent.append("<td>" + blank_dish_favs + "</td>");
            sbContent.append("<td>" + dish_fav_users + "</td>");
            sbContent.append("<td>" + cook_dish_fav_users + "</td>");
            sbContent.append("<td>" + tag_dish_fav_users + "</td>");
            sbContent.append("<td>" + blank_dish_fav_users + "</td>");
            //评论
            sbContent.append("<td>" + dish_comments + "</td>");
            sbContent.append("<td>" + cook_dish_comments + "</td>");
            sbContent.append("<td>" + tag_dish_comments + "</td>");
            sbContent.append("<td>" + blank_dish_comments + "</td>");
            sbContent.append("<td>" + dish_comment_users + "</td>");
            sbContent.append("<td>" + cook_dish_comment_users + "</td>");
            sbContent.append("<td>" + tag_dish_comment_users + "</td>");
            sbContent.append("<td>" + blank_dish_comment_users + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");

        // ======= 生成图片 - begin
        double[] arrayDishs = new double[rowlist.size()];     
        double[] arrayCookDishs = new double[rowlist.size()];
        double[] arrayTagDishs = new double[rowlist.size()];
        double[] arrayBlankDishs = new double[rowlist.size()];
        //
        double[] arrayDishComments = new double[rowlist.size()];  
        double[] arrayCookDishComments = new double[rowlist.size()];  
        double[] arrayTagDishComments = new double[rowlist.size()];  
        double[] arrayBlankDishComments = new double[rowlist.size()];  
        //
        double[] arrayDishFavs = new double[rowlist.size()];  
        double[] arrayCookDishFavs = new double[rowlist.size()];  
        double[] arrayTagDishFavs = new double[rowlist.size()];  
        double[] arrayBlankDishFavs = new double[rowlist.size()];  
        
        //
        String[] rowKeysDishs = {"作品数"};
        String[] rowKeyCookDishs = {"菜谱作品数"};
        String[] rowKeyTagDishs = {"秀美食话题作品数"};
        String[] rowKeyBlankDishs = {"秀美食非话题作品数"} ;
        //
        String[] rowKeysDishComments = {"作品评论数"};
        String[] rowKeyCookDishComments = {"菜谱作品评论数"} ;
        String[] rowKeyTagDishComments = {"秀美食话题作品评论数"} ;
        String[] rowKeyBlankDishComments = {"秀美食非话题作品评论数"} ;
        //
        String[] rowKeyDishFavs = {"作品总赞数"} ;
        String[] rowKeyCookDishFavs = {"菜谱作品赞数	"} ;
        String[] rowKeyTagDishFavs = {"秀美食话题作品赞数"} ;
        String[] rowKeyBlankDishFavs = {"秀美食非话题作品赞数"} ;
        
        //
        String[] columnKeysDishs = new String[rowlist.size()];

        for (int j = 0; j < rowlist.size(); j++) {
        	//date
            Date statDate = (Date) rowlist.get(j).get("statdate");
            String strWeek = DateUtil.getWeekDay(statDate);
            columnKeysDishs[j] = String.valueOf(statDate) + "(" + strWeek + ")";
            
            //dishs
            arrayDishs[j] = (Integer) rowlist.get(j).get("dishs");
            arrayCookDishs[j] = (Integer) rowlist.get(j).get("cook_dishs");
            arrayTagDishs[j] = (Integer) rowlist.get(j).get("tag_dishs");
            arrayBlankDishs[j] = (Integer) rowlist.get(j).get("blank_dishs");
            
            //comments
            arrayDishComments[j] = (Integer) rowlist.get(j).get("dish_comments");
            arrayCookDishComments[j] = (Integer) rowlist.get(j).get("cook_dish_comments");
            arrayTagDishComments[j] = (Integer) rowlist.get(j).get("tag_dish_comments");
            arrayBlankDishComments[j] = (Integer) rowlist.get(j).get("blank_dish_comments");
            
            //fav
            arrayDishFavs[j] = (Integer) rowlist.get(j).get("dish_favs");
            arrayCookDishFavs[j] = (Integer) rowlist.get(j).get("cook_dish_favs");
            arrayTagDishFavs[j] = (Integer) rowlist.get(j).get("tag_dish_favs");
            arrayBlankDishFavs[j] = (Integer) rowlist.get(j).get("blank_dish_favs");
        }
        //图片名称
        String chartTitleDishs = "作品趋势";
        String chartTitleCookDishs = "菜谱作品趋势" ;
        String chartTitleTagDishs = "秀美食话题作品趋势" ;
        String chartTitleBlankDishs = "秀美食非话题作品趋势" ;
        //
        String chartTitleDishComments = "作品总评论趋势";
        String chartTitleCookDishComments = "菜谱作品评论趋势";
        String chartTitleTagDishComments = "秀美食话题作品评论趋势";
        String chartTitleBlankDishComments = "秀美食非话题作品评论趋势";
        //
        String chartTitleDishFavs = "作品总赞趋势";
        String chartTitleCookDishFavs = "菜谱作品赞趋势";
        String chartTitleTagDishFavs = "秀美食话题作品赞趋势";
        String chartTitleBlankDishFavs = "秀美食非话题作品赞趋势";

        //X轴
        String categoryLabelDishs = "日期";
        String categoryLabelDishComments = "日期";
        String categoryLabelCookDishs = "日期" ;
        String categoryLabelTagDishs = "日期" ;
        String categoryLabelBlankDishs = "日期" ;

        // y轴
        String valueLabelDishs = "作品数";
        String valueLabelCookDishs = "菜谱作品数" ;
        String valueLabelTagDishs = "秀美食话题作品数" ;
        String valueLabelBlankDishs = "秀美食非话题作品数" ;
        //
        String valueLabelDishComments = "作品评论数";
        String valueLabelCookDishComments = "菜谱作品评论数";
        String valueLabelTagDishComments = "秀美食话题作品评论数" ;
        String valueLabelBlankDishComments = "秀美食非话题作品评论数" ;
        //
        String valueLabelDishFavs = "作品总赞数";
        String valueLabelCookDishFavs = "菜谱作品赞数";
        String valueLabelTagDishFavs = "秀美食话题作品赞数";
        String valueLabelBlankDishFavs = "秀美食非话题作品赞数";

        //数据集
        double[][] dataDishs = new double[1][];
        double[][] dataCookDishs = new double[1][];
        double[][] dataTagDishs = new double[1][];
        double[][] dataBlankDishs = new double[1][];
        //
        double[][] dataDishComments = new double[1][];
        double[][] dataCookDishComments = new double[1][];
        double[][] dataTagDishComments = new double[1][];
        double[][] dataBlankDishComments = new double[1][];
        //
        double[][] dataDishFavs = new double[1][];
        double[][] dataCookDishFavs = new double[1][];
        double[][] dataTagDishFavs = new double[1][];
        double[][] dataBlankDishFavs = new double[1][];
        
        dataDishs[0] = arrayDishs;
        dataCookDishs[0] = arrayCookDishs ;
        dataTagDishs[0] = arrayTagDishs ;
        dataBlankDishs[0] = arrayBlankDishs ;
        
        dataDishComments[0] = arrayDishComments;
        dataCookDishComments[0] = arrayCookDishComments;
        dataTagDishComments[0] = arrayTagDishComments;
        dataBlankDishComments[0] = arrayBlankDishComments;
        
        dataDishFavs[0] = arrayDishFavs;
        dataCookDishFavs[0] = arrayCookDishFavs;
        dataTagDishFavs[0] = arrayTagDishFavs;
        dataBlankDishFavs[0] = arrayBlankDishFavs;

        //作品数折线图
        JFreeChartUtil.buildLineChat(chartTitleDishs, categoryLabelDishs, valueLabelDishs, rowKeysDishs, columnKeysDishs, dataDishs,
                MailConstants.MAIL_CHART_PATH + "/img-dish-dishs.jpg");

        JFreeChartUtil.buildLineChat(chartTitleCookDishs, categoryLabelCookDishs, valueLabelCookDishs, rowKeyCookDishs, columnKeysDishs, dataCookDishs,
                MailConstants.MAIL_CHART_PATH + "/img-cook-dish.jpg");
        
        JFreeChartUtil.buildLineChat(chartTitleTagDishs, categoryLabelTagDishs, valueLabelTagDishs, rowKeyTagDishs, columnKeysDishs, dataTagDishs,
                MailConstants.MAIL_CHART_PATH + "/img-tag-dishs.jpg");
        
        JFreeChartUtil.buildLineChat(chartTitleBlankDishs, categoryLabelBlankDishs, valueLabelBlankDishs, rowKeyBlankDishs, columnKeysDishs, dataBlankDishs,
                MailConstants.MAIL_CHART_PATH + "/img-blank-dishs.jpg");
        
        //评论折线图
        JFreeChartUtil.buildLineChat(chartTitleDishComments, categoryLabelDishComments, valueLabelDishComments, rowKeysDishComments, columnKeysDishs, dataDishComments,
                MailConstants.MAIL_CHART_PATH + "/img-dish-comments.jpg");
        
        JFreeChartUtil.buildLineChat(chartTitleCookDishComments, categoryLabelDishComments, valueLabelCookDishComments, rowKeyCookDishComments, columnKeysDishs, dataCookDishComments,
                MailConstants.MAIL_CHART_PATH + "/img-cook_dish-comments.jpg");
        
        JFreeChartUtil.buildLineChat(chartTitleTagDishComments, categoryLabelDishComments, valueLabelTagDishComments, rowKeyTagDishComments, columnKeysDishs, dataTagDishComments,
                MailConstants.MAIL_CHART_PATH + "/img-tag_dish_comments.jpg");
        
        JFreeChartUtil.buildLineChat(chartTitleBlankDishComments, categoryLabelDishComments, valueLabelBlankDishComments, rowKeyBlankDishComments, columnKeysDishs, dataBlankDishComments,
                MailConstants.MAIL_CHART_PATH + "/img-blank_dish_comments.jpg");
        
        //点赞折线图
        JFreeChartUtil.buildLineChat(chartTitleDishFavs, categoryLabelDishComments, valueLabelDishFavs, rowKeyDishFavs, columnKeysDishs, dataDishFavs,
                MailConstants.MAIL_CHART_PATH + "/img-dish_favs.jpg");
        
        JFreeChartUtil.buildLineChat(chartTitleCookDishFavs, categoryLabelDishComments, valueLabelCookDishFavs, rowKeyCookDishFavs, columnKeysDishs, dataCookDishFavs,
                MailConstants.MAIL_CHART_PATH + "/img-cook_dish_favs.jpg");
        
        JFreeChartUtil.buildLineChat(chartTitleTagDishFavs, categoryLabelDishComments, valueLabelTagDishFavs, rowKeyTagDishFavs, columnKeysDishs, dataTagDishFavs,
                MailConstants.MAIL_CHART_PATH + "/img-tag_dish_favs.jpg");
        
        JFreeChartUtil.buildLineChat(chartTitleBlankDishFavs, categoryLabelDishComments, valueLabelBlankDishFavs, rowKeyBlankDishFavs, columnKeysDishs, dataBlankDishFavs,
                MailConstants.MAIL_CHART_PATH + "/img-blank_dish_favs.jpg");
        
        // ======= 生成图片 - end

        //添加图片到邮件中
        sbContent.append("<h1>2、趋势统计</h1><br/>");
        sbContent.append("<table class=\"gridtable\">");

        sbContent.append("<tr>" +
                "        <th style=\"background-color:#ff7f00\" colspan=\"4\" align=\"center\">作品数据</th>" +
        //       "        <td style=\"background-color:#ff7f00\" colspan=\"2\" align=\"center\">作品</td>" +
        //       "        <td style=\"background-color:#ff7f00\" colspan=\"2\" align=\"center\">作品评论</td>" +
                "    </tr>" +
                "    <tr>" +
        //        "        <td rowspan=\"10\">一周趋势</td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-dish-dishs.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-cook-dish.jpg\"/></td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-tag-dishs.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-blank-dishs.jpg\"/></td>" +
                "   </tr>" +
                "	 <tr>" +
                "        <th style=\"background-color:#ff7f00\" colspan=\"4\" align=\"center\">评论数据</th>" +
                "    </tr>" +
                "    <tr>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-dish-comments.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-cook_dish-comments.jpg\"/></td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-tag_dish_comments.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-blank_dish_comments.jpg\"/></td>" +
                "   </tr>" +
                "	 <tr>" +
                "        <th style=\"background-color:#ff7f00\" colspan=\"4\" align=\"center\">点赞数据</th>" +
                "    </tr>" +
                "    <tr>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-dish_favs.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-cook_dish_favs.jpg\"/></td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-tag_dish_favs.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-blank_dish_favs.jpg\"/></td>" +
                "    </tr>");

        sbContent.append("</table>");
        sbContent.append("<br/>");

        return sbContent.toString();
    }
}