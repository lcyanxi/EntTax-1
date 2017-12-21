package com.douguo.dc.mail.service.impl;

import com.douguo.dc.dish.service.DishService;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mail.service.SysMailSetService;
import com.douguo.dc.userbehavior.service.UserActionsStatService;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JavaSendMail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("mailUserAllActionsStatService")
public class MailUserAllActionsStatService {

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
                subject = "用户网站行为监测日报_" + curDate;
                to = "zhangyaozhou@douguo.com";
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
            String userAllActionsStat = userAllActionsStat(nDays);
            content.append("<h1>1、用户网站行为监测数据</h1><br/>");
            content.append(userAllActionsStat);
            content.append("<br/><br/>");

            content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

            javaSendMail.sendHtmlEmail(subject, to, content.toString());
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
            String strWeek = DateUtil.getWeekDay(statdate);
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
            sbContent.append("<td>" + statdate + "(" + strWeek + ")</td>");
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