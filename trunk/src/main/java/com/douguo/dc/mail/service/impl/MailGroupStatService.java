package com.douguo.dc.mail.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douguo.dg.group.service.DgGroupService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.group.service.GroupBaseStatService;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mail.service.SysMailSetService;
import com.douguo.dc.stat.hour.common.CommonHourConstants;
import com.douguo.dc.stat.hour.service.CommonHourStatService;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JFreeChartUtil;
import com.douguo.dc.util.JavaSendMail;

@Repository("mailGroupStatService")
public class MailGroupStatService {

    @Autowired
    private GroupBaseStatService groupBaseStatService;

    @Autowired
    private CommonHourStatService commonHourStatService;

    @Autowired
    private SysMailSetService sysMailSetService;

    @Autowired
    private DgGroupService dgGroupService;

    public void sendGroupSumMail(SysMailSet sysMailSet) {
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
                subject = "豆果圈子数据日报_" + curDate;
                to = "zhangyaozhou@douguo.com";
            }

            //获取需统计天数
            String strDesc = sysMailSet.getDesc();
            Integer nDays = 8;
            if (org.apache.commons.lang3.StringUtils.isNotBlank(strDesc)) {
                try {
                    nDays = Integer.parseInt(strDesc);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            // 1. 圈圈-汇总数据统计
            // 2. 趋势图
            String groupBaseStat = groupBaseStat(nDays);
            content.append("<h1>1、圈圈-汇总数据统计</h1><br/>");
            content.append(groupBaseStat);
            content.append("<br/><br/>");

            // 3. 圈圈-分圈子数据统计
            String groupGroupStat = groupGroupStat();
            content.append("<h1>3、圈圈-分圈子数据统计</h1><br/>");
            content.append(groupGroupStat);
            content.append("<br/><br/>");

            // 4. 圈圈-发帖量时间分布
            String groupHour = groupHourStat();
            content.append("<h1>4、圈圈-发帖量时间分布</h1><br/>");
            content.append(groupHour);
            content.append("<br/>");

            content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

            String[] imgPaths = new String[3];
            imgPaths[0] = MailConstants.MAIL_CHART_PATH + "/img-group-totalactions.jpg";
            imgPaths[1] = MailConstants.MAIL_CHART_PATH + "/img-group-replies.jpg";
            imgPaths[2] = MailConstants.MAIL_CHART_PATH + "/img-group-hour.jpg";

            javaSendMail.sendHtmlWithInnerImageEmail(subject, to, content.toString(), imgPaths);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取圈子分组信息
     *
     * @return
     */
    public Map<String, String> getGroups() {
        //
        Map<String, String> mapGroup = new HashMap<String, String>();
        List<Map<String, Object>> list = dgGroupService.getGroupList();

        for (Map<String, Object> map : list) {
            Integer groupId = (Integer) map.get("id");
            String groupName = (String) map.get("name");
            mapGroup.put(String.valueOf(groupId), groupName);
        }

        return mapGroup;
    }

    /**
     * 圈圈-汇总信息
     *
     * @return
     */
    private String groupBaseStat(Integer beforeDays){
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, beforeDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);

        //
        List<Map<String,Object>> rowList = groupBaseStatService.queryGroupList(startDate,endDate);
        // 构造表格
        sbContent.append("<table class=\"gridtable\">");
        sbContent.append("<tr>" +
                "           <th>序号</th><th>圈子</th><th>总发帖数</th><th>主贴数</th><th>爬取数</th><th>回复数</th>" +
                "           <th>发帖用户数</th><th>总浏览数</th><th>总浏览人数</th><th>圈子列表PV</th><th>圈子列表UV</th>" +
                "           <th>帖子PV</th><th>帖子UV</th><th>首次发帖用户数</th><th>重复发帖用户数</th>" +
                "         </tr>");

        int i = 0;
        for (Map<String, Object> map : rowList) {
            i++;

            Date statdate = (Date) map.get("statdate");
            BigDecimal total_actions = (BigDecimal) map.get("total_actions");
            BigDecimal posts = (BigDecimal) map.get("posts");
            BigDecimal robot_posts = (BigDecimal) map.get("robot_posts");
            BigDecimal replies = (BigDecimal) map.get("replies");
            BigDecimal total_users = (BigDecimal) map.get("total_users");
            BigDecimal pv = (BigDecimal) map.get("pv");
            BigDecimal uv = (BigDecimal) map.get("uv");
            BigDecimal group_pv = (BigDecimal) map.get("group_pv");
            BigDecimal group_uv = (BigDecimal) map.get("group_uv");
            BigDecimal post_pv = (BigDecimal) map.get("post_pv");
            BigDecimal post_uv = (BigDecimal) map.get("post_uv");
            BigDecimal first_posts = (BigDecimal) map.get("first_posts");
            BigDecimal first_post_users = (BigDecimal) map.get("first_post_users");
            BigDecimal repeat_posts = (BigDecimal) map.get("repeat_posts");
            BigDecimal repeat_post_users = (BigDecimal) map.get("repeat_post_users");

            //
            sbContent.append("<tr>");
            sbContent.append("<td>" + i + "</td>");
            sbContent.append("<td>" + statdate + "</td>");
            sbContent.append("<td>" + total_actions + "</td>");
            sbContent.append("<td>" + posts + "</td>");
            sbContent.append("<td>" + robot_posts + "</td>");
            sbContent.append("<td>" + replies + "</td>");
            sbContent.append("<td>" + total_users + "</td>");
            sbContent.append("<td>" + pv + "</td>");
            sbContent.append("<td>" + uv + "</td>");
            sbContent.append("<td>" + group_pv + "</td>");
            sbContent.append("<td>" + group_uv + "</td>");
            sbContent.append("<td>" + post_pv + "</td>");
            sbContent.append("<td>" + post_uv + "</td>");
            sbContent.append("<td>" + first_post_users + "</td>");
            sbContent.append("<td>" + repeat_post_users + "</td>");
            sbContent.append("</tr>");
        }

        sbContent.append("</table>");
        sbContent.append("注：圈子列表PV，指圈子列表被浏览的次数");
        sbContent.append("<br/>");

        // ======= 生成图片 - begin
        double[] arrayGroupTotalActionss = new double[rowList.size()];
        double[] arrayGroupRepliess = new double[rowList.size()];
        //
        String[] rowKeysGroupTotalActionss = {"总发帖数"};
        String[] rowKeysGroupReplies = {"回复数"};

        String[] columnKeysGroupStatdate = new String[rowList.size()];

        for (int j = 0; j < rowList.size(); j++) {
            columnKeysGroupStatdate[j] = String.valueOf(rowList.get(j).get("statdate"))+"("+DateUtil.getWeekDay(String.valueOf(rowList.get(j).get("statdate")))+")" ;

            arrayGroupTotalActionss[j] = ((BigDecimal) rowList.get(j).get("total_actions")).doubleValue();
            arrayGroupRepliess[j] = ((BigDecimal) rowList.get(j).get("replies")).doubleValue();
        }
        //图片名称
        String chartTitleGroupTotalActions = "总发帖趋势";
        String chartTitleGroupReplies = "回复趋势";

        //X轴
        String categoryLabelGroupTotalActions = "日期";
        String categoryLabelGroupReplies = "日期";

        // y轴
        String valueLabelGroupTotalActions = "总发帖数";
        String valueLabelGroupReplies = "回复数";

        //数据集
        double[][] dataGroupTotalActions = new double[1][];
        double[][] dataGroupReplies = new double[1][];

        dataGroupTotalActions[0] = arrayGroupTotalActionss;
        dataGroupReplies[0] = arrayGroupRepliess;

        //总发帖数折线图
        JFreeChartUtil.buildLineChat(chartTitleGroupTotalActions, categoryLabelGroupTotalActions, valueLabelGroupTotalActions, rowKeysGroupTotalActionss, columnKeysGroupStatdate, dataGroupTotalActions,
                MailConstants.MAIL_CHART_PATH + "/img-group-totalactions.jpg");

        //回复数折线图
        JFreeChartUtil.buildLineChat(chartTitleGroupReplies, categoryLabelGroupReplies, valueLabelGroupReplies, rowKeysGroupReplies, columnKeysGroupStatdate, dataGroupReplies,
                MailConstants.MAIL_CHART_PATH + "/img-group-replies.jpg");
        // ======= 生成图片 - end

        //添加图片到邮件中
        sbContent.append("<h1>2、趋势统计</h1><br/>");
        sbContent.append("<table class=\"gridtable\">");

        sbContent.append("<tr>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"1\" align=\"center\">整体数据</td>" +
                "        <td style=\"background-color:#ff7f00\" colspan=\"2\" align=\"center\">总发帖数</td>" +
                "        <td style=\"background-color:#ff7f00\" colspan=\"2\" align=\"center\">回复数</td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td rowspan=\"1\">一周趋势</td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-group-totalactions.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-group-replies.jpg\"/></td>" +
                "    </tr>");

        sbContent.append("</table>");
        sbContent.append("<br/>");

        return sbContent.toString();
    }

    /**
     * 圈圈-分圈子信息
     *
     * @return
     */
    private String groupGroupStat() {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        //
        List<Map<String, Object>> rowlist = groupBaseStatService.queryGroupDayList(startDate, endDate);

        //获取圈子名称
        Map<String, String> mapGroup = dgGroupService.getGroupsMap();

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");
        sbContent
                .append("<tr><th>序号</th><th>圈子</th><th>总发帖数</th><th>主贴数</th><th>爬取数</th><th>回复数</th><th>发帖用户数</th><th>总浏览数</th><th>总浏览人数</th><th>圈子列表PV</th><th>圈子列表UV</th><th>帖子PV</th><th>帖子UV</th></tr>");
        int i = 0;
        for (Map<String, Object> map : rowlist) {
            i++;
            String groupId = (String) map.get("group_id");

            String groupName = "";

            if ("g".equals(groupId)) {
                groupName = "总计";
            } else {
                groupName = mapGroup.get(groupId);
                if (StringUtils.isBlank(groupName)) {
                    //  不存在/删除的圈子不予显示
                    continue;
                    //groupName = groupId;
                }
            }

            BigDecimal total_actions = (BigDecimal) map.get("total_actions");
            BigDecimal posts = (BigDecimal) map.get("posts");
            BigDecimal robot_posts = (BigDecimal) map.get("robot_posts");
            BigDecimal replies = (BigDecimal) map.get("replies");
            BigDecimal total_users = (BigDecimal) map.get("total_users");
            BigDecimal pv = (BigDecimal) map.get("pv");
            BigDecimal uv = (BigDecimal) map.get("uv");
            BigDecimal group_pv = (BigDecimal) map.get("group_pv");
            BigDecimal group_uv = (BigDecimal) map.get("group_uv");
            BigDecimal post_pv = (BigDecimal) map.get("post_pv");
            BigDecimal post_uv = (BigDecimal) map.get("post_uv");

            //
            sbContent.append("<tr>");
            sbContent.append("<td>" + i + "</td>");
            sbContent.append("<td>" + groupName + "</td>");
            sbContent.append("<td>" + total_actions + "</td>");
            sbContent.append("<td>" + posts + "</td>");
            sbContent.append("<td>" + robot_posts + "</td>");
            sbContent.append("<td>" + replies + "</td>");
            sbContent.append("<td>" + total_users + "</td>");
            sbContent.append("<td>" + pv + "</td>");
            sbContent.append("<td>" + uv + "</td>");
            sbContent.append("<td>" + group_pv + "</td>");
            sbContent.append("<td>" + group_uv + "</td>");
            sbContent.append("<td>" + post_pv + "</td>");
            sbContent.append("<td>" + post_uv + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");

        return sbContent.toString();
    }

    private String groupHourStat() {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String order = "hour";
        //
        List<Map<String, Object>> rowlst = commonHourStatService.queryCommonHourStatList(
                CommonHourConstants.COMMON_HOUR_TYPE_GROUP, startDate, endDate, order);

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");
        // sbContent.append("<tr><th>小时</th><th>发帖数</th><th>发帖人数</th></tr>");
        sbContent.append("<tr><th>小时</th><th>发帖数</th><th>图形展示</th></tr>");
        double[] arrayPosts = new double[24];
        double[] arrayUsers = new double[24];
        double[] arrayMoneys = new double[24];
        for (int i = 0; i < 24; i++) {
            boolean isOk = false;
            for (Map<String, Object> map : rowlst) {
                Integer hour = (Integer) map.get("hour");
                Integer posts = (Integer) map.get("nums1");
                Integer users = (Integer) map.get("nums2");
                //
                if (i == hour) {
                    //
                    arrayPosts[i] = posts.doubleValue();
                    arrayUsers[i] = users.doubleValue();
                    //
                    sbContent.append("<tr>");
                    sbContent.append("<td>" + hour + "</td>");
                    sbContent.append("<td>" + posts + "</td>");
                    if(i==0){
                        sbContent.append("<td rowspan=\"24\"><img width=\"950\" height=\"690\" style=\"margin:10px; padding:10px;\" src=\"cid:img-group-hour.jpg\"/></td>");
                    }
                    sbContent.append("</tr>");
                    isOk = true;
                    break;
                }
            }
            if (!isOk) {
                //
                arrayPosts[i] = 0;
                arrayUsers[i] = 0;
                arrayMoneys[i] = 0;
                //

                sbContent.append("<tr>");
                sbContent.append("<td>" + i + "</td>");
                sbContent.append("<td>" + 0 + "</td>");
                if(i==0){
                    sbContent.append("<td rowspan=\"24\"><img width=\"950\" height=\"690\" style=\"margin:10px; padding:10px;\" src=\"cid:img-group-hour.jpg\"/></td>");
                }
                sbContent.append("</tr>");
            }
        }

        // 生成图片 - begin
        // double[][] data = new double[2][];
        double[][] data = new double[1][];
        data[0] = arrayPosts;
        // data[1] = arrayUsers;
        //
        // String[] rowKeys = { "发帖数", "发帖人数" };
        String[] rowKeys = {"发帖量"};
        String[] columnKeys = new String[24];
        for (int i = 0; i < 24; i++) {
            columnKeys[i] = String.valueOf(i);
        }
        String chartTitle = "圈子小时发帖量分布图";
        String categoryLabel = "时间";
        String valueLabel = "发帖量";
        JFreeChartUtil.buildBarChat(chartTitle, categoryLabel, valueLabel, rowKeys, columnKeys, data,
                MailConstants.MAIL_CHART_PATH + "/img-group-hour.jpg");
        // 生成图片 - end

        sbContent.append("</table>");
        return sbContent.toString();
    }
}