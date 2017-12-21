package com.douguo.dc.mail.service.impl;

import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mail.service.SysMailSetService;
import com.douguo.dc.userbehavior.service.UserActionsStatService;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JFreeChartUtil;
import com.douguo.dc.util.JavaSendMail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("mailUserDayAllActionsStatService")
public class MailUserDayAllActionsStatService {

    @Autowired
    private UserActionsStatService userActionsStatService;

    @Autowired
    private SysMailSetService sysMailSetService;

    /**
     * 发送用户网站行为监测_相关统计
     *
     * @param sysMailSet
     */
    public void sendUserAllActionsStatMail(SysMailSet sysMailSet) {
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
                subject = "用户每天网站行为监测日报_" + curDate;
                to = "zhangyaozhou@douguo.com,zhangjianfei@douguo.com";
            }

            String strDesc = sysMailSet.getDesc();
            Integer nDays = 8;
            if (StringUtils.isNotBlank(strDesc)) {
                try {
                    nDays = Integer.parseInt(strDesc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 1. 基本数据统计
            //可选数据
            String userDay_ActionsStat = userDay_ActionsStat(nDays);

            content.append("<h1>用户网站基础数据</h1><br/>");
            content.append(userDay_ActionsStat);
            content.append("<br/><br/>");

            content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

            String[] imgPaths = new String[9];
            imgPaths[0] = MailConstants.MAIL_CHART_PATH + "/img-cooks.jpg";
            imgPaths[1] = MailConstants.MAIL_CHART_PATH + "/img-dishs.jpg";
            imgPaths[2] = MailConstants.MAIL_CHART_PATH + "/img-cook-comments.jpg";
            imgPaths[3] = MailConstants.MAIL_CHART_PATH + "/img-dish-comments.jpg";
            imgPaths[4] = MailConstants.MAIL_CHART_PATH + "/img-dish-favs.jpg";
            imgPaths[5] = MailConstants.MAIL_CHART_PATH + "/img-group-posts.jpg";
            imgPaths[6] = MailConstants.MAIL_CHART_PATH + "/img-group-replys.jpg";
            imgPaths[7] = MailConstants.MAIL_CHART_PATH + "/img-cook-collects.jpg";
            imgPaths[8] = MailConstants.MAIL_CHART_PATH + "/img-user-friend_num.jpg";
            javaSendMail.sendHtmlWithInnerImageEmail(subject, to, content.toString(), imgPaths);
//            String[] imgPaths = new String[]{MailConstants.MAIL_CHART_PATH + "/img-group-hour.jpg"};

//            javaSendMail.sendHtmlWithInnerImageEmail(subject, to, content.toString(), imgPaths);

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
    private String userDay_ActionsStat(Integer beforeDays) {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, beforeDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);

        // 所有用户
        List<Map<String, Object>> rowlistDay = userActionsStatService.queryAllDayList(startDate, endDate);


        // 构造表格
        sbContent.append("<table class=\"gridtable\">");

        // 构造 title
        sbContent.append("<tr>" +
                "<th rowspan=\"2\">日期</th>" +
                "<th rowspan=\"2\">新增用户</th>" +
                "<th colspan=\"2\">上传菜谱</th>" +
                "<th colspan=\"2\">上传作品</th>" +
                "<th colspan=\"2\">菜谱评论</th>" +
                "<th colspan=\"2\">作品评论</th>" +
                "<th colspan=\"2\">菜谱收藏</th>" +
                "<th colspan=\"2\">作品点赞</th>" +
                "<th colspan=\"2\">圈圈发帖</th>" +
                "<th colspan=\"2\">圈圈回复</th>" +
                "<th rowspan=\"2\">用户关注</th>" +
                "</tr>");
        sbContent.append("<tr>" +
                "<th nowrap='nowrap'>总人次</th><th nowrap='nowrap'>总人数</th>" +
                "<th nowrap='nowrap'>总人次</th><th nowrap='nowrap'>总人数</th>" +
                "<th nowrap='nowrap'>总人次</th><th nowrap='nowrap'>总人数</th>" +
                "<th nowrap='nowrap'>总人次</th><th nowrap='nowrap'>总人数</th> " +
                "<th nowrap='nowrap'>总人次</th> <th nowrap='nowrap'>总人数</th>" +
                " <th nowrap='nowrap'>总人次</th> <th nowrap='nowrap'>总人数</th>" +
                " <th nowrap='nowrap'>总人次</th> <th nowrap='nowrap'>总人数</th>" +
                "<th nowrap='nowrap'>总人次</th> <th nowrap='nowrap'>总人数</th></tr> ");

        int i = 0;

        //
        for (Map<String, Object> map : rowlistDay) {
            i++;
            Date statdate = (Date) map.get("statdate");
            String strWeek = DateUtil.getWeekDay(statdate);

            Integer view_cook_pv = (Integer) map.get("view_cook_pv");
            Integer view_cook_users = (Integer) map.get("view_cook_users");
            Integer cooks = (Integer) map.get("cooks");
            Integer cook_users = (Integer) map.get("cook_users");
            Integer view_dish_pv = (Integer) map.get("view_dish_pv");
            Integer view_dish_users = (Integer) map.get("view_dish_users");
            Integer dishs = (Integer) map.get("dishs");
            Integer dish_users = (Integer) map.get("dish_users");
            Integer cook_comments = (Integer) map.get("cook_comments");
            Integer cook_comment_users = (Integer) map.get("cook_comment_users");
            Integer dish_comments = (Integer) map.get("dish_comments");
            Integer dish_comment_users = (Integer) map.get("dish_comment_users");
            Integer cook_collects = (Integer) map.get("cook_collects");
            Integer cook_collect_users = (Integer) map.get("cook_collect_users");
            Integer dish_favs = (Integer) map.get("dish_favs");
            Integer dish_fav_users = (Integer) map.get("dish_fav_users");
            Integer group_posts = (Integer) map.get("group_posts");
            Integer group_post_users = (Integer) map.get("group_post_users");
            Integer group_replys = (Integer) map.get("group_replys");
            Integer group_reply_users = (Integer) map.get("group_reply_users");
            Integer mall_orders = (Integer) map.get("mall_orders");
            Integer mall_pays = (Integer) map.get("mall_pays");
            BigDecimal mall_moneys = (BigDecimal) map.get("mall_moneys");
            Integer searchs = (Integer) map.get("searchs");

            BigDecimal  new_active_users = (BigDecimal) map.get("new_active_users");
            Integer  user_friend_num = (Integer) map.get("user_friend_num");





            //
            sbContent.append("<tr>");
            sbContent.append("<td>" + statdate + "(" + strWeek + ")</td>");
            sbContent.append("<td>" + new_active_users + "</td>");
            sbContent.append("<td>" + cooks + "</td>");
            sbContent.append("<td>" + cook_users + "</td>");
            sbContent.append("<td>" + dishs + "</td>");
            sbContent.append("<td>" + dish_users + "</td>");
            sbContent.append("<td>" + cook_comments + "</td>");
            sbContent.append("<td>" + cook_comment_users + "</td>");
            sbContent.append("<td>" + dish_comments + "</td>");
            sbContent.append("<td>" + dish_comment_users + "</td>");
            sbContent.append("<td>" + cook_collects + "</td>");
            sbContent.append("<td>" + cook_collect_users+ "</td>");
            sbContent.append("<td>" + dish_favs + "</td>");
            sbContent.append("<td>" + dish_fav_users + "</td>");
            sbContent.append("<td>" + group_posts + "</td>");
            sbContent.append("<td>" + group_post_users + "</td>");
            sbContent.append("<td>" + group_replys + "</td>");
            sbContent.append("<td>" + group_reply_users + "</td>");
            sbContent.append("<td>" + user_friend_num + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");
        
        
        // ======= 生成图片 - begin
        double[] arrayCooks = new double[rowlistDay.size()];    
        double[] arrayDishs = new double[rowlistDay.size()]; 
        double[] arrayCookComments = new double[rowlistDay.size()]; 
        double[] arrayDishComments = new double[rowlistDay.size()]; 
        double[] arrayDishFavs = new double[rowlistDay.size()]; 
        double[] arrayGroupPosts = new double[rowlistDay.size()]; 
        double[] arrayGroupReplys = new double[rowlistDay.size()];
        double[] arrayCookCollects = new double[rowlistDay.size()];
        double[] arrayUserFriends = new double[rowlistDay.size()];

        //
        String[] rowKeysCooks = {"上传菜谱"};
        String[] rowKeysDishs = {"上传作品"};
        String[] rowKeysCookComments = {"菜谱评论"};
        String[] rowKeysDishComments = {"作品评论"};
        String[] rowKeysDishFavs = {"作品点赞"};
        String[] rowKeysGroupPosts = {"圈圈发帖"};
        String[] rowKeysGroupReplys = {"圈圈回复"};
        String[] rowKeysCookCollects = {"菜谱收藏"};
        String[] rowKeysUserFriend = {"用户关注"};
        
        //
        String[] columnKeysDishs = new String[rowlistDay.size()];

        for (int j = 0; j < rowlistDay.size(); j++) {
        	//date
            Date statDate = (Date) rowlistDay.get(j).get("statdate");
            String strWeek = DateUtil.getWeekDay(statDate);
            columnKeysDishs[j] = String.valueOf(statDate) + "(" + strWeek + ")";
            
            //cooks
            arrayCooks[j] = (Integer) rowlistDay.get(j).get("cooks");
            
            //dishs
            arrayDishs[j] = (Integer) rowlistDay.get(j).get("dishs");
            
            //cook_comments
            arrayCookComments[j] = (Integer) rowlistDay.get(j).get("cook_comments");
            
            //dish_comments
            arrayDishComments[j] = (Integer) rowlistDay.get(j).get("dish_comments");
            		
            //dish_favs
            arrayDishFavs[j] = (Integer) rowlistDay.get(j).get("dish_favs");
            
            //group_posts
            arrayGroupPosts[j] = (Integer) rowlistDay.get(j).get("group_posts");
            
            //group_replys
            arrayGroupReplys[j] = (Integer) rowlistDay.get(j).get("group_replys");

            //cook_collects
            arrayCookCollects[j] = (Integer) rowlistDay.get(j).get("cook_collects");

            //user_friend
            arrayUserFriends[j] =(Integer)   rowlistDay.get(j).get("user_friend_num");
        } 
        
        //图片名称
        String chartTitleCooks = "上传菜谱人次趋势";
        String chartTitleDishs = "上传作品人次趋势";
        String chartTitleCookComments = "菜谱评论人次趋势";
        String chartTitleDishComments = "作品评论人次趋势";
        String chartTitleDishFavs = "作品点赞人次趋势";
        String chartTitleGroupPosts = "圈圈发帖人次趋势";
        String chartTitleGroupReplys = "圈圈回复人次趋势";
        String chartTitleCookCollects = "菜谱收藏人次趋势";
        String chartTitleUserFriend = "用户关注数趋势";

        //X轴
        String categoryLabelDishs = "日期";

        // y轴
        String valueLabelDishs = "人次";
        
        //数据集
        double[][] dataCooks = new double[1][];
        double[][] dataDishs = new double[1][];
        double[][] dataCookComments = new double[1][];
        double[][] dataDishComments = new double[1][];
        double[][] dataDishFavs = new double[1][];
        double[][] dataGroupPosts = new double[1][];
        double[][] dataGroupReplys = new double[1][];
        double[][] dataCookCollects = new double[1][];
        double[][] dataUserFriends = new double[1][];

        dataCooks[0] = arrayCooks;
        dataDishs[0] = arrayDishs;
        dataCookComments[0] = arrayCookComments;
        dataDishComments[0] = arrayDishComments;
        dataDishFavs[0] = arrayDishFavs;
        dataGroupPosts[0] = arrayGroupPosts;
        dataGroupReplys[0] = arrayGroupReplys;
        dataCookCollects[0]=arrayCookCollects;
        dataUserFriends[0]=arrayUserFriends;

        //作品数折线图
        JFreeChartUtil.buildLineChat(chartTitleCooks, categoryLabelDishs, valueLabelDishs, rowKeysCooks, columnKeysDishs, dataCooks,
                MailConstants.MAIL_CHART_PATH + "/img-cooks.jpg");
        
        JFreeChartUtil.buildLineChat(chartTitleDishs, categoryLabelDishs, valueLabelDishs, rowKeysDishs, columnKeysDishs, dataDishs,
                MailConstants.MAIL_CHART_PATH + "/img-dishs.jpg");
        
        JFreeChartUtil.buildLineChat(chartTitleCookComments, categoryLabelDishs, valueLabelDishs, rowKeysCookComments, columnKeysDishs, dataCookComments,
                MailConstants.MAIL_CHART_PATH + "/img-cook-comments.jpg");
        
        JFreeChartUtil.buildLineChat(chartTitleDishComments, categoryLabelDishs, valueLabelDishs, rowKeysDishComments, columnKeysDishs, dataDishComments,
                MailConstants.MAIL_CHART_PATH + "/img-dish-comments.jpg");
        
        JFreeChartUtil.buildLineChat(chartTitleDishFavs, categoryLabelDishs, valueLabelDishs, rowKeysDishFavs, columnKeysDishs, dataDishFavs,
                MailConstants.MAIL_CHART_PATH + "/img-dish-favs.jpg");
        
        JFreeChartUtil.buildLineChat(chartTitleGroupPosts, categoryLabelDishs, valueLabelDishs, rowKeysGroupPosts, columnKeysDishs, dataGroupPosts,
                MailConstants.MAIL_CHART_PATH + "/img-group-posts.jpg");
        
        JFreeChartUtil.buildLineChat(chartTitleGroupReplys, categoryLabelDishs, valueLabelDishs, rowKeysGroupReplys, columnKeysDishs, dataGroupReplys,
                MailConstants.MAIL_CHART_PATH + "/img-group-replys.jpg");
        JFreeChartUtil.buildLineChat(chartTitleCookCollects, categoryLabelDishs, valueLabelDishs, rowKeysCookCollects, columnKeysDishs, dataCookCollects,
                MailConstants.MAIL_CHART_PATH + "/img-cook-collects.jpg");
        JFreeChartUtil.buildLineChat(chartTitleUserFriend, categoryLabelDishs, valueLabelDishs, rowKeysUserFriend, columnKeysDishs, dataUserFriends,
                MailConstants.MAIL_CHART_PATH + "/img-user-friend_num.jpg");
        
        // ======= 生成图片 - end

        //添加图片到邮件中
        sbContent.append("<h1>2、趋势统计</h1><br/>");
        sbContent.append("<table class=\"gridtable\">");

        sbContent.append("<tr>" +
                "        <th style=\"background-color:#ff7f00\" colspan=\"4\" align=\"center\">数据</th>" +
                "    </tr>" +
                "    <tr>" +
                "        <td rowspan=\"10\">一周趋势</td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-cooks.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-dishs.jpg\"/></td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-cook-comments.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-dish-comments.jpg\"/></td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-dish-favs.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-group-posts.jpg\"/></td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-group-replys.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-cook-collects.jpg\"/></td>" +
                "</tr>"+
                "    <tr>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-user-friend_num.jpg\"/></td>" +
                "</tr>") ;

        sbContent.append("</table>");
        sbContent.append("<br/>");

        return sbContent.toString();
    }

    
    
    
    /**
     * 构造邮件内容
     *
     * @return
     * @unused
     */
    private String userAllActionsStat(Integer beforeDays) {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, beforeDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);

        // 所有用户
        List<Map<String, Object>> rowlist7Day = userActionsStatService.queryAll7DayList(startDate, endDate);

        // 新注册用户
        List<Map<String, Object>> rowlistNewRegUser = userActionsStatService.queryNewRegUser7DayList(startDate, endDate);

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");

        // 构造 title
        sbContent.append("<tr><th rowspan=\"2\">日期</th><th rowspan=\"2\">新激活数</th><th rowspan=\"2\">新注册数</th><th rowspan=\"2\">周活</th><th rowspan=\"2\">新用户互动占比</th><th colspan=\"3\">活跃用户(去重)</th><th colspan=\"4\">上传菜谱</th><th colspan=\"4\">上传作品</th><th colspan=\"4\">菜谱评论</th><th colspan=\"4\">作品评论</th><th colspan=\"4\">菜谱收藏</th><th colspan=\"4\">作品点赞</th><th colspan=\"4\">圈圈发帖</th><th colspan=\"4\">圈圈回复</th></tr>");
        sbContent.append("<tr><th nowrap='nowrap'>新用户人数</th> <th nowrap='nowrap'>总人数</th><th nowrap='nowrap'>总登陆数</th> <th nowrap='nowrap'>新用户人次</th> <th nowrap='nowrap'>总人次</th> <th nowrap='nowrap'>新用户人数</th> <th nowrap='nowrap'>总人数</th> <th nowrap='nowrap'>新用户人次</th> <th nowrap='nowrap'>总人次</th> <th nowrap='nowrap'>新用户人数</th> <th nowrap='nowrap'>总人数</th> <th nowrap='nowrap'>新用户人次</th> <th nowrap='nowrap'>总人次</th> <th nowrap='nowrap'>新用户人数</th> <th nowrap='nowrap'>总人数</th> <th nowrap='nowrap'>新用户人次</th> <th nowrap='nowrap'>总人次</th> <th nowrap='nowrap'>新用户人数</th> <th nowrap='nowrap'>总人数</th> <th nowrap='nowrap'>新用户人次</th> <th nowrap='nowrap'>总人次</th> <th nowrap='nowrap'>新用户人数</th> <th nowrap='nowrap'>总人数</th> <th nowrap='nowrap'>新用户人次</th> <th nowrap='nowrap'>总人次</th> <th nowrap='nowrap'>新用户人数</th> <th nowrap='nowrap'>总人数</th> <th nowrap='nowrap'>新用户人次</th> <th nowrap='nowrap'>总人次</th> <th nowrap='nowrap'>新用户人数</th> <th nowrap='nowrap'>总人数</th> <th nowrap='nowrap'>新用户人次</th> <th nowrap='nowrap'>总人次</th> <th nowrap='nowrap'>新用户人数</th> <th nowrap='nowrap'>总人数</th> </tr>");

        int i = 0;

        Map<Date, Map<String, Object>> mapNewReguser = new HashMap<Date, Map<String, Object>>();
        for (Map<String, Object> map : rowlistNewRegUser) {
            Date statdate = (Date) map.get("statdate");
            mapNewReguser.put(statdate, map);
        }

        //
        for (Map<String, Object> map : rowlist7Day) {
            i++;
            Date statdate = (Date) map.get("statdate");
            Map<String, Object> mapNew = mapNewReguser.get(statdate);
            if (mapNew == null) {
                continue;
            }

            BigDecimal new_active_users = (BigDecimal) map.get("new_active_users");
            Integer new_reg_users = (Integer) map.get("new_reg_users");
            BigDecimal active_users = (BigDecimal) map.get("active_users");
            Integer login_users = (Integer) map.get("login_users");
            Integer login_users_new = (Integer) mapNew.get("login_users");
            Integer action_users = (Integer) map.get("action_users");
            Integer action_users_new = (Integer) mapNew.get("action_users");

            Integer view_cook_pv = (Integer) map.get("view_cook_pv");
            Integer view_cook_pv_new = (Integer) mapNew.get("view_cook_pv");
            Integer view_cook_users = (Integer) map.get("view_cook_users");
            Integer view_cook_users_new = (Integer) mapNew.get("view_cook_users");
            Integer cooks = (Integer) map.get("cooks");
            Integer cooks_new = (Integer) mapNew.get("cooks");
            Integer cook_users = (Integer) map.get("cook_users");
            Integer cook_users_new = (Integer) mapNew.get("cook_users");
            Integer view_dish_pv = (Integer) map.get("view_dish_pv");
            Integer view_dish_pv_new = (Integer) mapNew.get("view_dish_pv");
            Integer view_dish_users = (Integer) map.get("view_dish_users");
            Integer view_dish_users_new = (Integer) mapNew.get("view_dish_users");
            Integer dishs = (Integer) map.get("dishs");
            Integer dishs_new = (Integer) mapNew.get("dishs");
            Integer dish_users = (Integer) map.get("dish_users");
            Integer dish_users_new = (Integer) mapNew.get("dish_users");
            Integer cook_comments = (Integer) map.get("cook_comments");
            Integer cook_comments_new = (Integer) mapNew.get("cook_comments");
            Integer cook_comment_users = (Integer) map.get("cook_comment_users");
            Integer cook_comment_users_new = (Integer) mapNew.get("cook_comment_users");
            Integer dish_comments = (Integer) map.get("dish_comments");
            Integer dish_comments_new = (Integer) mapNew.get("dish_comments");
            Integer dish_comment_users = (Integer) map.get("dish_comment_users");
            Integer dish_comment_users_new = (Integer) mapNew.get("dish_comment_users");
            Integer cook_collects = (Integer) map.get("cook_collects");
            Integer cook_collects_new = (Integer) mapNew.get("cook_collects");
            Integer cook_collect_users = (Integer) map.get("cook_collect_users");
            Integer cook_collect_users_new = (Integer) mapNew.get("cook_collect_users");
            Integer dish_favs = (Integer) map.get("dish_favs");
            Integer dish_favs_new = (Integer) mapNew.get("dish_favs");
            Integer dish_fav_users = (Integer) map.get("dish_fav_users");
            Integer dish_fav_users_new = (Integer) mapNew.get("dish_fav_users");
            Integer group_posts = (Integer) map.get("group_posts");
            Integer group_posts_new = (Integer) mapNew.get("group_posts");
            Integer group_post_users = (Integer) map.get("group_post_users");
            Integer group_post_users_new = (Integer) mapNew.get("group_post_users");
            Integer group_replys = (Integer) map.get("group_replys");
            Integer group_replys_new = (Integer) mapNew.get("group_replys");
            Integer group_reply_users = (Integer) map.get("group_reply_users");
            Integer group_reply_users_new = (Integer) mapNew.get("group_reply_users");
            Integer mall_orders = (Integer) map.get("mall_orders");
            Integer mall_orders_new = (Integer) mapNew.get("mall_orders");
            Integer mall_pays = (Integer) map.get("mall_pays");
            Integer mall_pays_new = (Integer) mapNew.get("mall_pays");
            BigDecimal mall_moneys = (BigDecimal) map.get("mall_moneys");
            BigDecimal mall_moneys_new = (BigDecimal) mapNew.get("mall_moneys");
            Integer searchs = (Integer) map.get("searchs");
            Integer searchs_new = (Integer) mapNew.get("searchs");

            BigDecimal newRegUserRate = new BigDecimal(0);
            BigDecimal bd_action_users_new = new BigDecimal(action_users_new * 100);
            if (new_reg_users.intValue() != 0) {
                newRegUserRate = bd_action_users_new.divide(new BigDecimal(new_reg_users), 2, BigDecimal.ROUND_HALF_UP);
            }


            //
            sbContent.append("<tr>");
            sbContent.append("<td>" + statdate + "</td>");
            sbContent.append("<td>" + new_active_users + "</td>");
            sbContent.append("<td>" + new_reg_users + "</td>");
            sbContent.append("<td>" + active_users + "</td>");
            sbContent.append("<td>" + newRegUserRate + "%</td>");
            sbContent.append("<td>" + action_users_new + "</td>");
            sbContent.append("<td>" + action_users + "</td>");
            sbContent.append("<td>" + login_users + "</td>");
            sbContent.append("<td>" + cooks_new + "</td>");
            sbContent.append("<td>" + cooks + "</td>");
            sbContent.append("<td>" + cook_users_new + "</td>");
            sbContent.append("<td>" + cook_users + "</td>");
            sbContent.append("<td>" + dishs_new + "</td>");
            sbContent.append("<td>" + dishs + "</td>");
            sbContent.append("<td>" + dish_users_new + "</td>");
            sbContent.append("<td>" + dish_users + "</td>");
            sbContent.append("<td>" + cook_comments_new + "</td>");
            sbContent.append("<td>" + cook_comments + "</td>");
            sbContent.append("<td>" + cook_comment_users_new + "</td>");
            sbContent.append("<td>" + cook_comment_users + "</td>");
            sbContent.append("<td>" + dish_comments_new + "</td>");
            sbContent.append("<td>" + dish_comments + "</td>");
            sbContent.append("<td>" + dish_comment_users_new + "</td>");
            sbContent.append("<td>" + dish_comment_users + "</td>");
            sbContent.append("<td>" + cook_collects_new + "</td>");
            sbContent.append("<td>" + cook_collects + "</td>");
            sbContent.append("<td>" + cook_collect_users_new + "</td>");
            sbContent.append("<td>" + cook_collect_users + "</td>");
            sbContent.append("<td>" + dish_favs_new + "</td>");
            sbContent.append("<td>" + dish_favs + "</td>");
            sbContent.append("<td>" + dish_fav_users_new + "</td>");
            sbContent.append("<td>" + dish_fav_users + "</td>");
            sbContent.append("<td>" + group_posts_new + "</td>");
            sbContent.append("<td>" + group_posts + "</td>");
            sbContent.append("<td>" + group_post_users_new + "</td>");
            sbContent.append("<td>" + group_post_users + "</td>");
            sbContent.append("<td>" + group_replys_new + "</td>");
            sbContent.append("<td>" + group_replys + "</td>");
            sbContent.append("<td>" + group_reply_users_new + "</td>");
            sbContent.append("<td>" + group_reply_users + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");

        return sbContent.toString();
    }
}