package com.douguo.dc.mail.service.impl;

import com.douguo.dc.applog.dao.AppUserNewDao;
import com.douguo.dc.applog.service.AppDauService;
import com.douguo.dc.applog.service.AppUserNewService;
import com.douguo.dc.channel.model.Channel;
import com.douguo.dc.channel.service.ChannelService;
import com.douguo.dc.channel.service.ChannelSumStatService;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mail.service.SysMailSetService;
import com.douguo.dc.util.BigDecimalUtil;
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

@Repository("mailClientChannelNewUserService")
public class MailClientChannelNewUserService {


    @Autowired
    private AppDauService appDauService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private SysMailSetService sysMailSetService;

    @Autowired
    private AppUserNewDao appUserNewDao;

    @Autowired
    private AppUserNewService appUserNewService;

    @Autowired
    private ChannelSumStatService channelSumStatService;

    public void sendClientChannelSumMail_old() {
        JavaSendMail javaSendMail = new JavaSendMail();
        try {
            SysMailSet sysMailSet = sysMailSetService
                    .getSysMailSetByMailType(MailConstants.MAIL_TYPE_CLIENT_CHANNEL_SUM);

            String curDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
            String subject = "";
            String to = "";
            StringBuffer content = new StringBuffer("");
            content.append("<style type=\"text/css\"> table.gridtable { font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse; } table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #dedede; } table.gridtable td { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff; } </style>");

            if (sysMailSet != null) {
                subject = sysMailSet.getSubject() + "_" + curDate;
                to = sysMailSet.getMailTo();
            } else {
                subject = "豆果客户端渠道数据日报_" + curDate;
                to = "zhangyaozhou@douguo.com";
            }


            List<Channel> listChannel = channelService.getChannelListByStatus(1);
            Map<String, String> mapChannel = new HashMap<String, String>();
            for (Channel channel : listChannel) {
                String strChannelCode = channel.getChannelCode();
                String strChannelName = channel.getChannelName();
//
                mapChannel.put(strChannelCode, strChannelName);
            }

            // 1. 日活渠道TOP10数据
//            String activeUserTop = activeUserTop(mapChannel);
//            content.append("<h1>1、用户日活-渠道TOP10</h1><br/>");
//            content.append(activeUserTop);
//            content.append("<br/><br/>");

            // 2. 新增渠道TOP10数据
            String newUserTop = newUserTop(mapChannel);
            content.append("<h1>2、用户新增-渠道TOP10</h1><br/>");
            content.append(newUserTop);
            content.append("<br/><br/>");

            content.append("注：公司有严格的数据管理规定，数据仅限邮件收件人范围知悉，不得传播<br/>");
            content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

            javaSendMail.sendHtmlEmail(subject, to, content.toString());
            // String[] imgPaths = new String[] { CHART_ORDER_TIME_PATH +
            // "/img-0.jpg" };
            // javaSendMail.sendHtmlWithInnerImageEmail(subject, to,
            // content.toString(), "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendClientChannelSumMail(SysMailSet sysMailSet) {
        JavaSendMail javaSendMail = new JavaSendMail();
        try {
            String curDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
            String subject = "";
            String to = "";
            StringBuffer content = new StringBuffer("");
            content.append("<style type=\"text/css\"> table.gridtable { font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse; } table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #dedede; } table.gridtable td { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff; } </style>");

            if (sysMailSet != null) {
                subject = sysMailSet.getSubject() + "_" + curDate;
                to = sysMailSet.getMailTo();
            } else {
                subject = "豆果客户端渠道数据日报_" + curDate;
                to = "zhangyaozhou@douguo.com, zhangjianfei@douguo.com";
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

            List<Channel> listChannel = channelService.getChannelListByStatus(1);
            Map<String, String> mapChannel = new HashMap<String, String>();
            for (Channel channel : listChannel) {
                String strChannelCode = channel.getChannelCode();
                String strChannelName = channel.getChannelName();
                mapChannel.put(strChannelCode, strChannelName);
            }

            // 1. 新增渠道TOP10数据
            String newUserTop = newUserTop(mapChannel);
            content.append("<h1>1、用户新增-渠道TOP10</h1><br/>");
            content.append(newUserTop);
            content.append("<br/><br/>");

            // 2. 整体数据
            String strChannelWholeStat = channelWholeStat(nDays);
            content.append("<h1>2、整体数据</h1><br/>");
            content.append(strChannelWholeStat);
            content.append("<br/><br/>");

            // 3. 分类渠道新增分布
            String strChannelTypeStat = channelTypeStat();
            content.append("<h1>3、分类渠道新增分布</h1><br/>");
            content.append(strChannelTypeStat);
            content.append("<br/><br/>");

            // 4. TOP20渠道新增分布
            String top20 = newUserTop20();
            content.append("<h1>4、新增TOP20渠道分布</h1><br/>");
            content.append(top20);
            content.append("<br/><br/>");

            // 5. 线上TOP10新增分布
            String onlineTop10 = onlineTop10();
            content.append("<h1>5、线上新增TOP10分布</h1><br/>");
            content.append(onlineTop10);
            content.append("<br/><br/>");

            // 6. 线下TOP10新增分布
            String offlineTop10 = offlineTop10();
            content.append("<h1>6、线下新增TOP10分布</h1><br/>");
            content.append(offlineTop10);
            content.append("<br/><br/>");

            content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

            String[] imgPaths = new String[2];
            imgPaths[0] = MailConstants.MAIL_CHART_PATH + "/img-channel-newuser.jpg";
            imgPaths[1] = MailConstants.MAIL_CHART_PATH + "/img-channel-newuser-type2.jpg";

            javaSendMail.sendHtmlWithInnerImageEmail(subject, to, content.toString(), imgPaths);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getAppName(String app) {
        if (app.equals("3")) {
            return "豆果美食(IOS)";
        } else if (app.equals("4")) {
            return "豆果美食(Android)";
        } else {
            return "未知";
        }
    }

    private String newUserTop(Map<String, String> mapChannel) {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        BigDecimal iosSum = new BigDecimal(0);
        BigDecimal androidSum = new BigDecimal(0);
        // 豆果美食，ios、android
        String[] apps = new String[]{"3", "4", "4cpa"};
        for (String app : apps) {
            String newapp = "";
            int i = 0;
            // 构造表格
            if (app.equals("3")) {
                sbContent.append("<h3>1.2、 Ios渠道TOP10</h4>");
                newapp = "3";
            } else if (app.equals("4")) {
                sbContent.append("<h3>1.3、 Android 市场渠道TOP10</h4>");
                newapp = "4";
            } else if (app.equals("4cpa")) {
                sbContent.append("<h3>1.4、 Android CPA渠道TOP10</h4>");
                newapp = "4";
            }

            List<Map<String, Object>> rowlist = appUserNewDao.queryChannelList(startDate, endDate, newapp);

            sbContent.append("<table class=\"gridtable\">");
            sbContent.append("<tr><th>序号</th><th>APP</th><th>渠道</th><th>新用户</th><th>占比</th></tr>");
            BigDecimal sumUids = new BigDecimal(0);
            Integer nTop10Uids = 0;
            for (Map<String, Object> map : rowlist) {
                Integer uid = (Integer) map.get("uid");
                BigDecimal buid = new BigDecimal(uid);
                sumUids = sumUids.add(buid);
            }
            //
            if (app.equals("3")) {
                iosSum = sumUids;
            } else if (app.equals("4")) {
                androidSum = sumUids;
            }

            for (Map<String, Object> map : rowlist) {

                String appName = getAppName(newapp);
                String channel = (String) map.get("channel");
                String channelName = mapChannel.get(channel);
                Integer uid = (Integer) map.get("uid");
                BigDecimal newUid = new BigDecimal(uid * 100);
                BigDecimal rate = new BigDecimal(0);

                if (app.equals("3")) {

                } else if (app.equals("4")) {
                    if (channel.indexOf("CPA") < 0) {
                        i++;
                        // 只取前10条数据
                        if (i > 100) {
                            break;
                        }
                    } else {
                        continue;
                    }
                } else if (app.equals("4cpa")) {
                    if (channel.indexOf("CPA") >= 0) {
                        i++;
                        // 只取前10条数据
                        if (i > 10) {
                            break;
                        }
                    } else {
                        continue;
                    }
                }

                if (sumUids.longValue() != 0) {
                    rate = newUid.divide(sumUids, 2, BigDecimal.ROUND_HALF_UP);
                }
                nTop10Uids += uid;
                //
                sbContent.append("<tr>");
                sbContent.append("<td>" + i + "</td>");
                sbContent.append("<td>" + appName + "</td>");
                sbContent.append("<td>" + channelName + "</td>");
                sbContent.append("<td>" + uid + "</td>");
                sbContent.append("<td>" + rate + "%</td>");
                sbContent.append("</tr>");
            }

            // top10 rate
            BigDecimal top10Rate = new BigDecimal(0);
            BigDecimal top10Uids = new BigDecimal(0);
            top10Uids = new BigDecimal(nTop10Uids * 100);
            if (sumUids.longValue() != 0) {
                top10Rate = top10Uids.divide(sumUids, 2, BigDecimal.ROUND_HALF_UP);
            }
            sbContent.append("<tr>");
            sbContent.append("<td>总计</td>");
            sbContent.append("<td></td>");
            sbContent.append("<td></td>");
            sbContent.append("<td>" + nTop10Uids + "</td>");
            sbContent.append("<td>" + top10Rate + "%</td>");
            sbContent.append("</tr>");
            sbContent.append("</table>");
            sbContent.append("<br/>");
        }
        StringBuffer sumContent = new StringBuffer();
        sumContent.append("<h3>1.1 新增汇总</h2>");
        sumContent.append("<table class=\"gridtable\">");
        sumContent.append("<tr><th>序号</th><th>APP</th><th>新用户</th></tr>");
        sumContent.append("<tr>");
        sumContent.append("<td>1</td>");
        sumContent.append("<td>IOS</td>");
        sumContent.append("<td>" + iosSum + "</td>");
        sumContent.append("</tr>");
        sumContent.append("<tr>");
        sumContent.append("<td>2</td>");
        sumContent.append("<td>Android</td>");
        sumContent.append("<td>" + androidSum + "</td>");
        sumContent.append("</tr>");
        sumContent.append("</table>");
        sumContent.append("<br/>");

        sumContent.append(sbContent);

        return sumContent.toString();
    }


    /**
     * 渠道整体数据
     *
     * @return
     */
    private String channelWholeStat(Integer beforeDays) {
        StringBuffer sbContent = new StringBuffer();

        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, beforeDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);

        String chainDate = DateUtil.getSpecifiedDayBefore(today, 2);
        String sameDate = DateUtil.getSpecifiedDayBefore(today, 8);

        //获取数据
        List<Map<String, Object>> listNewUserTotal = appUserNewService.getNewUserTotal(startDate, endDate, "3", "4");
        List<Map<String, Object>> listActiveUserTotal = appDauService.queryActiveUserTotal(startDate, endDate, "3", "4");

        //
        Map<Date, BigDecimal> mapNewUser = new HashMap<Date, BigDecimal>();
        for (Map<String, Object> map : listNewUserTotal) {
            Date statDate = (Date) map.get("statdate");
            BigDecimal bdNewUsers = (BigDecimal) map.get("total_new_users");
            mapNewUser.put(statDate, bdNewUsers);
        }

        Map<Date, BigDecimal> mapActiveUser = new HashMap<Date, BigDecimal>();
        for (Map<String, Object> map : listActiveUserTotal) {
            Date statDate = (Date) map.get("statdate");
            BigDecimal bdUids = (BigDecimal) map.get("uid");
            mapActiveUser.put(statDate, bdUids);
        }

        //新增用户数、环比、同比
        BigDecimal bdCurNewUser = mapNewUser.get(DateUtil.stringToDate(endDate));
        if (bdCurNewUser == null) {
            bdCurNewUser = new BigDecimal(0);
        }

        BigDecimal bdNewUser_Chain = mapNewUser.get(DateUtil.stringToDate(chainDate));

        if (bdNewUser_Chain == null) {
            bdNewUser_Chain = new BigDecimal(0);
        }

        BigDecimal bdNewUser_Same = mapNewUser.get(DateUtil.stringToDate(sameDate));
        if (bdNewUser_Same == null) {
            bdNewUser_Same = new BigDecimal(0);
        }

        //新用户-环比
        BigDecimal dbNewUser_ChainRate = BigDecimalUtil.getPercentage(bdCurNewUser.subtract(bdNewUser_Chain), bdNewUser_Chain, 2);
        String strNewUser_ChainRateIcon = getTrendIcon(dbNewUser_ChainRate);

        //新用户-同比
        BigDecimal dbNewUser_SameRate = BigDecimalUtil.getPercentage(bdCurNewUser.subtract(bdNewUser_Same), bdNewUser_Same, 2);
        String strNewUser_SameRateIcon = getTrendIcon(dbNewUser_SameRate);

        //表头
        sbContent.append("<br/>");
        sbContent.append("<table class=\"gridtable\">");
        sbContent.append("<tr>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"2\">整体数据</td>" +
                "        <td style=\"background-color:#ff7f00\" colspan=\"2\">新增激活用户</td>" +
                "        </tr>" +
                "        <tr>" +
                "        <td style=\"background-color:#ff7f00\" colspan=\"2\">" + BigDecimalUtil.format(bdCurNewUser, 0) + "</td>" +
                "        </tr>" +
                "        <tr>" +
                "        <td rowspan=\"4\">一周趋势</td>" +
                "        <td align=\"center\">" +
                "            <div style=\"margin-left:47%\" class=\"" + strNewUser_ChainRateIcon + "\"></div>" +
                "        </td>" +
                "        <td>" +
                "            <div style=\"margin-left:40%\" class=\"" + strNewUser_SameRateIcon + "\"></div>" +
                "        </td>" +
                "        </tr>" +
                "        <tr>" +
                "        <td class='" + getTrendColor(dbNewUser_ChainRate) + "'>" + dbNewUser_ChainRate + "%</td>" +
                "        <td class='" + getTrendColor(dbNewUser_SameRate) + "'>" + dbNewUser_SameRate + "%</td>" +
                "        </tr>" +
                "        <tr>" +
                "        <td>环比</td>" +
                "        <td>同比</td>" +
                "        </tr>" +
                "        <tr>" +
                "        <td colspan=\"2\"><img width=\"490\" height=\"350\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-channel-newuser.jpg\"/></td>" +
                "        </tr>");

        sbContent.append("<tr>" +
                "        <td style=\"background-color:#ffb570\">日期</td>" +
                "        <td style=\"background-color:#ffb570\" colspan=\"2\">新增用户</td>" +
                "    </tr>");
        //详细数据
        for (Map<String, Object> map : listActiveUserTotal) {
            Date statdate = (Date) map.get("statdate");
            String strWeek = DateUtil.getWeekDay(statdate);
            //从map中获取
            BigDecimal bdNewUsers = (BigDecimal) mapNewUser.get(statdate);
            BigDecimal dbUids = (BigDecimal) mapActiveUser.get(statdate);

            sbContent.append(" <tr>" +
                    "        <td nowrap='nowrap'>" + statdate + "(" + strWeek + ")</td>" +
                    "        <td colspan=\"2\">" + BigDecimalUtil.format(bdNewUsers, 0) + "</td>" +
                    "    </tr>");
        }
        //表格结束
        sbContent.append("</table>");
        sbContent.append("<br/>");

        // ======= 生成图片 - begin
        double[] arrayNewUser = new double[listNewUserTotal.size()];
        //
        String[] rowKeysNewUser = {"新增用户数"};
        String[] columnKeysNewUser = new String[listNewUserTotal.size()];
        for (int i = 0; i < listNewUserTotal.size(); i++) {
            columnKeysNewUser[i] = String.valueOf(listNewUserTotal.get(i).get("statdate")) + "(" + DateUtil.getWeekDay(String.valueOf(listNewUserTotal.get(i).get("statdate"))) + ")";
            arrayNewUser[i] = ((BigDecimal) listNewUserTotal.get(i).get("total_new_users")).doubleValue();
        }
        String chartTitleNewUser = "新增用户趋势";
        String categoryLabelNewUser = "日期";
        String valueLabelNewUser = "用户数";
        double[][] dataNewUser = new double[1][];
        dataNewUser[0] = arrayNewUser;

        JFreeChartUtil.buildLineChat(chartTitleNewUser, categoryLabelNewUser, valueLabelNewUser, rowKeysNewUser, columnKeysNewUser, dataNewUser,
                MailConstants.MAIL_CHART_PATH + "/img-channel-newuser.jpg");
        // ======= 生成图片 - end

        return sbContent.toString();
    }


    private String channelTypeStat() {
        StringBuffer sbContent = new StringBuffer();

        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 8);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);

        String curDate = DateUtil.getSpecifiedDayBefore(today, 1);

        String chainDate = DateUtil.getSpecifiedDayBefore(today, 2);
        String sameDate = DateUtil.getSpecifiedDayBefore(today, 8);

        Map<String, Double> mapChartDat = new HashMap<String, Double>();

        //获取数据
        List<Map<String, Object>> listNewUserTotal = appUserNewService.getNewUserTotal(curDate, curDate, "3", "4");
        List<Map<String, Object>> listChannelType1Cur = channelSumStatService.queryChannelListType1(curDate, curDate, "3", "4");
        List<Map<String, Object>> listChannelType1 = channelSumStatService.queryChannelListType1(startDate, endDate, "3", "4");

        //
        Map<Date, BigDecimal> mapNewUserTotal = new HashMap<Date, BigDecimal>();
        for (Map<String, Object> map : listNewUserTotal) {
            Date statDate = (Date) map.get("statdate");
            BigDecimal bdNewUsers = (BigDecimal) map.get("total_new_users");
            mapNewUserTotal.put(statDate, bdNewUsers);
        }

        //当天总激活用户数
        BigDecimal bdCurNewUserTotal = mapNewUserTotal.get(DateUtil.stringToDate(curDate, "yyyy-MM-dd"));

        Map<String, BigDecimal> mapNewUserChannelType1 = new HashMap<String, BigDecimal>();
        for (Map<String, Object> map : listChannelType1) {

            Date statDate = (Date) map.get("statdate");
            Long strType1 = (Long) map.get("type1");
            BigDecimal bdUids = (BigDecimal) map.get("uid");
            mapNewUserChannelType1.put(DateUtil.date2Str(statDate, "yyyy-MM-dd") + strType1, bdUids);
        }

        //表头
        sbContent.append("<table class=\"gridtable\">");
        sbContent.append("<tr>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"2\">分类渠道新增用户分布</td>" +
                "        <td style=\"background-color:#ff7f00\" colspan=\"3\">新增激活用户</td>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"2\">新增用户占比</td>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"2\">渠道类别</td>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"2\">新增用户</td>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"2\">新增用户占比</td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td style=\"background-color:#ff7f00\">新增激活用户</td>" +
                "        <td style=\"background-color:#ff7f00\">环比</td>" +
                "        <td style=\"background-color:#ff7f00\">同比</td>" +
                "    </tr>");

        //构造线上、线下数据
        for (Map<String, Object> map : listChannelType1Cur) {
            Date statDate = (Date) map.get("statdate");
            Long lType1 = (Long) map.get("type1");
            String strType1_Name = (String) map.get("type1_name");
            if (StringUtils.isBlank(strType1_Name)) {
                strType1_Name = "未知";
            }
            //新增用户数、环比、同比
            BigDecimal bdCurNewUserType1 = (BigDecimal) map.get("uid");
            BigDecimal bdNewUserType1_Chain = mapNewUserChannelType1.get(chainDate + lType1);

            if (bdNewUserType1_Chain == null) {
                bdNewUserType1_Chain = new BigDecimal(0);
            }

            BigDecimal bdNewUserType1_Same = mapNewUserChannelType1.get(sameDate + lType1);
            if (bdNewUserType1_Same == null) {
                bdNewUserType1_Same = new BigDecimal(0);
            }

            //新用户-环比
            BigDecimal dbNewUser_ChainRate = BigDecimalUtil.getPercentage(bdCurNewUserType1.subtract(bdNewUserType1_Chain), bdNewUserType1_Chain, 2);
            String strNewUser_ChainRateIcon = getTrendIcon(dbNewUser_ChainRate);

            //新用户-同比
            BigDecimal dbNewUser_SameRate = BigDecimalUtil.getPercentage(bdCurNewUserType1.subtract(bdNewUserType1_Same), bdNewUserType1_Same, 2);
            String strNewUser_SameRateIcon = getTrendIcon(dbNewUser_SameRate);

            List<Map<String, Object>> listChannelType2 = channelSumStatService.queryChannelListType2(curDate, curDate, String.valueOf(lType1), "3", "4");

            int nRowSpan = listChannelType2.size();

            BigDecimal channelType1Rate = BigDecimalUtil.getPercentage(bdCurNewUserType1, bdCurNewUserTotal, 2);


            for (int i = 0; i < listChannelType2.size(); i++) {
                Map<String, Object> mapType2 = listChannelType2.get(i);
                Long channelType2 = (Long) mapType2.get("type2");
                String channelType2_Name = (String) mapType2.get("type2_name");
                BigDecimal bdUidsType2 = (BigDecimal) mapType2.get("uid");

                BigDecimal channelType2Rate = BigDecimalUtil.getPercentage(bdUidsType2, bdCurNewUserTotal, 2);

                //填充图形需要的数据
                mapChartDat.put(channelType2_Name, bdUidsType2.doubleValue());
                //
                if (i == 0) {
                    sbContent.append("<tr>" +
                            "        <td rowspan=\"" + nRowSpan + "\">" + strType1_Name + "</td>" +
                            "        <td rowspan=\"" + nRowSpan + "\">" + BigDecimalUtil.format(bdCurNewUserType1, 0) + "</td>" +
                            "        <td class='" + getTrendColor(dbNewUser_ChainRate) + "' rowspan=\"" + nRowSpan + "\">" + dbNewUser_ChainRate + "%</td>" +
                            "        <td class='" + getTrendColor(dbNewUser_SameRate) + "' rowspan=\"" + nRowSpan + "\">" + dbNewUser_SameRate + "%</td>" +
                            "        <td rowspan=\"" + nRowSpan + "\">" + channelType1Rate + "%</td>" +
                            "        <td>" + channelType2_Name + "</td>" +
                            "        <td>" + BigDecimalUtil.format(bdUidsType2, 0) + "</td>" +
                            "        <td>" + channelType2Rate + "%</td>" +
                            "    </tr>");
                } else {
                    sbContent.append("<tr>" +
                            "        <td>" + channelType2_Name + "</td>" +
                            "        <td>" + BigDecimalUtil.format(bdUidsType2, 0) + "</td>" +
                            "        <td>" + channelType2Rate + "%</td>" +
                            "    </tr>");
                }
            }
        }

        sbContent.append(
                "    <tr>" +
                        "        <td colspan=\"8\"><img width=\"590\" height=\"500\" src=\"cid:img-channel-newuser-type2.jpg\" alt=\"\"/></td>" +
                        "    </tr>");

        //表格结束
        sbContent.append("</table>");
        sbContent.append("<br/>");

        // ======= 生成图片 - begin
        String chartTitleNewUser = "渠道分类新增分布";
        JFreeChartUtil.buildPieChat(chartTitleNewUser, mapChartDat, MailConstants.MAIL_CHART_PATH + "/img-channel-newuser-type2.jpg", 590, 500);
        // ======= 生成图片 - end

        return sbContent.toString();
    }

    private String newUserTop20() {
        StringBuffer sbContent = new StringBuffer();

        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 8);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);

        String curDate = DateUtil.getSpecifiedDayBefore(today, 1);

        String chainDate = DateUtil.getSpecifiedDayBefore(today, 2);
        String sameDate = DateUtil.getSpecifiedDayBefore(today, 8);

        Map<String, Double> mapChartDat = new HashMap<String, Double>();


        // 获取数据
        // channel new user
        List<Map<String, Object>> listChannelCur = channelSumStatService.queryChannelSumList(curDate, curDate, "3", "4");
        List<Map<String, Object>> listChannelChain = channelSumStatService.queryChannelSumList(chainDate, chainDate, "3", "4");
        List<Map<String, Object>> listChannelSame = channelSumStatService.queryChannelSumList(sameDate, sameDate, "3", "4");

        //total new user
        List<Map<String, Object>> listTotal = appUserNewService.getNewUserTotal(curDate, curDate, "3", "4");

        //当天数据
        Map<String, BigDecimal> mapTotal = new HashMap<String, BigDecimal>();

        for (Map<String, Object> totObj : listTotal) {
            Date statDate = (Date) totObj.get("statdate");
            BigDecimal totalNewUsers = (BigDecimal) totObj.get("total_new_users");
            mapTotal.put(DateUtil.date2Str(statDate, "yyyy-MM-dd"), totalNewUsers);
        }

        //环比数据
        Map<String, Integer> mapChannelChainNewUser = new HashMap<String, Integer>();
        Map<String, Integer> mapChannelChainRank = new HashMap<String, Integer>();
        //
        for (int i = 0; i < listChannelChain.size(); i++) {
            Map<String, Object> map = listChannelChain.get(i);
            String channelCode = (String) map.get("channel");
            Integer news = (Integer) map.get("news");
            if (news == null) {
                news = new Integer(0);
            }
            mapChannelChainNewUser.put(channelCode, news);
            if (!mapChannelChainRank.containsKey(channelCode)) {
                mapChannelChainRank.put(channelCode, i + 1);
            }
        }

        //同比数据
        Map<String, Integer> mapChannelSameNewuser = new HashMap<String, Integer>();
        for (Map<String, Object> map : listChannelSame) {
            String channelCode = (String) map.get("channel");
            Integer news = (Integer) map.get("news");
            if (news == null) {
                news = new Integer(0);
            }
            mapChannelSameNewuser.put(channelCode, news);
        }

        //表头
        sbContent.append("<table class=\"gridtable\">");
        sbContent.append("<tr>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"2\">排名</td>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"2\" colspan=\"3\">渠道名称</td>" +
                "        <td style=\"background-color:#ff7f00\" colspan=\"3\">新增用户</td>" +
                //"        <td style=\"background-color:#ff7f00\" rowspan=\"2\">活跃用户</td>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"2\">新用户占比</td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td style=\"background-color:#ff7f00\">新增用户</td>" +
                "        <td style=\"background-color:#ff7f00\">环比</td>" +
                "        <td style=\"background-color:#ff7f00\">同比</td>" +
                "    </tr>");

        Integer top20NewUsers = new Integer(0);
        Integer top20ActiveUsers = new Integer(0);

        BigDecimal totalNewUsers = mapTotal.get(curDate);
        if (totalNewUsers == null) {
            totalNewUsers = new BigDecimal(0);
        }

        for (int i = 0; i < listChannelCur.size(); i++) {
            //只取前20个渠道
            if (i >= 20) {
                continue;
            }

            Map<String, Object> channel = listChannelCur.get(i);
            if (channel != null) {
                Date statdate = (Date) channel.get("statdate");
                BigDecimal daus = (BigDecimal) channel.get("daus");

                if (daus == null) {
                    daus = new BigDecimal("0");
                }
                Integer nNewUserCur = (Integer) channel.get("news");
                if (nNewUserCur == null) {
                    nNewUserCur = new Integer(0);
                }
                BigDecimal bdNewUserCur = new BigDecimal(nNewUserCur);


                String userlast1 = (String) channel.get("userlast1");
                String userlast7 = (String) channel.get("userlast7");
                String channelName = (String) channel.get("channel_name");
                String channelCode = (String) channel.get("channel");

                //环比
                Integer nChainNewUser = mapChannelChainNewUser.get(channelCode);
                if (nChainNewUser == null) {
                    nChainNewUser = new Integer(0);
                }
                BigDecimal bdChainNewUser = new BigDecimal(nChainNewUser);
                BigDecimal dbNewUser_ChainRate = BigDecimalUtil.getPercentage(bdNewUserCur.subtract(bdChainNewUser), bdChainNewUser, 2);

                //同比
                Integer nSameNewUser = mapChannelSameNewuser.get(channelCode);
                if (nSameNewUser == null) {
                    nSameNewUser = new Integer(0);
                }
                BigDecimal bdSameNewUser = new BigDecimal(nSameNewUser);
                BigDecimal dbNewUser_SameRate = BigDecimalUtil.getPercentage(bdNewUserCur.subtract(bdSameNewUser), bdSameNewUser, 2);

                BigDecimal bdChannelNewUserRate = BigDecimalUtil.getPercentage(new BigDecimal(nNewUserCur), totalNewUsers, 2);

                //
                int curRank = i + 1;
                Integer chainRank = mapChannelChainRank.get(channelCode);
                if (chainRank == null) {
                    chainRank = 0;
                }
                BigDecimal bdRank = new BigDecimal(chainRank - curRank);
                String strRank = bdRank.toString();
                if (bdRank.doubleValue() == 0) {
                    strRank = "";
                }
                top20NewUsers += nNewUserCur;
                top20ActiveUsers += daus.intValue();
                sbContent.append("<tr>" +
                        "        <td>" + curRank + "</td>" +
                        "        <td>" + channelName + "</td>" +
                        "        <td>" + getTrendRankNew(curRank, chainRank, 20) + "</td>" +
                        "        <td class='" + getTrendColor(bdRank) + "' >" + strRank + "</td>" +
                        "        <td>" + BigDecimalUtil.format(new BigDecimal(nNewUserCur), 0) + "</td>" +
                        "        <td class='" + getTrendColor(dbNewUser_ChainRate) + "' >" + dbNewUser_ChainRate + "%</td>" +
                        "        <td class='" + getTrendColor(dbNewUser_SameRate) + "' > " + dbNewUser_SameRate + "%</td>" +
                        //"        <td>" + BigDecimalUtil.format(daus, 0) + "</td>" +
                        "        <td>" + bdChannelNewUserRate + "%</td>" +
                        "    </tr>");
            }
        }
        //表格结束
        sbContent.append("<tr>" +
                "<td colspan=\"4\">总计</td>" +
                "<td>" + BigDecimalUtil.format(new BigDecimal(top20NewUsers), 0) + "</td>" +
                "<td>-</td>" +
                "<td>-</td>" +
                //"<td>" + BigDecimalUtil.format(new BigDecimal(top20ActiveUsers), 0) + "</td>" +
                "<td>" + BigDecimalUtil.getPercentage(new BigDecimal(top20NewUsers), totalNewUsers, 2) + "%</td>" +
                "</tr>");
        sbContent.append("</table>");
        sbContent.append("<br/>");

        return sbContent.toString();
    }

    /**
     * 线上新增top10
     *
     * @return
     */
    private String onlineTop10() {
        return top10("1");
    }

    /**
     * 线下新增top10
     *
     * @return
     */
    private String offlineTop10() {
        return top10("2");
    }

    /**
     * 线上新增top10
     *
     * @return
     */
    private String top10(String channelType1) {
        StringBuffer sbContent = new StringBuffer();

        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 8);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);

        String curDate = DateUtil.getSpecifiedDayBefore(today, 1);

        String chainDate = DateUtil.getSpecifiedDayBefore(today, 2);
        String sameDate = DateUtil.getSpecifiedDayBefore(today, 8);

        Map<String, Double> mapChartDat = new HashMap<String, Double>();

        // 获取数据
        // channel new user
        List<Map<String, Object>> listChannelCur = channelSumStatService.queryChannelSumListForChannelType1(curDate, curDate, channelType1, "3", "4");
        List<Map<String, Object>> listChannelChain = channelSumStatService.queryChannelSumListForChannelType1(chainDate, chainDate, channelType1, "3", "4");
        List<Map<String, Object>> listChannelSame = channelSumStatService.queryChannelSumListForChannelType1(sameDate, sameDate, channelType1, "3", "4");

        //total new user
        List<Map<String, Object>> listTotal = appUserNewService.getNewUserTotal(curDate, curDate, "3", "4");

        //当天数据
        Map<String, BigDecimal> mapTotal = new HashMap<String, BigDecimal>();

        for (Map<String, Object> totObj : listTotal) {
            Date statDate = (Date) totObj.get("statdate");
            BigDecimal totalNewUsers = (BigDecimal) totObj.get("total_new_users");
            mapTotal.put(DateUtil.date2Str(statDate, "yyyy-MM-dd"), totalNewUsers);
        }

        //环比数据
        Map<String, Integer> mapChannelChainNewUser = new HashMap<String, Integer>();
        Map<String, Integer> mapChannelChainRank = new HashMap<String, Integer>();
        //
        for (int i = 0; i < listChannelChain.size(); i++) {
            Map<String, Object> map = listChannelChain.get(i);
            String channelCode = (String) map.get("channel");
            Integer news = (Integer) map.get("news");
            if (news == null) {
                news = new Integer(0);
            }
            mapChannelChainNewUser.put(channelCode, news);
            mapChannelChainRank.put(channelCode, i + 1);
        }

        //同比数据
        Map<String, Integer> mapChannelSameNewuser = new HashMap<String, Integer>();
        for (Map<String, Object> map : listChannelSame) {
            String channelCode = (String) map.get("channel");
            Integer news = (Integer) map.get("news");
            if (news == null) {
                news = new Integer(0);
            }
            mapChannelSameNewuser.put(channelCode, news);
        }

        //表头
        sbContent.append("<table class=\"gridtable\">");
        sbContent.append("<tr>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"2\">排名</td>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"2\" colspan=\"3\">渠道名称</td>" +
                "        <td style=\"background-color:#ff7f00\" colspan=\"3\">新增用户</td>" +
                //"        <td style=\"background-color:#ff7f00\" rowspan=\"2\">活跃用户</td>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"2\">新用户占比</td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td style=\"background-color:#ff7f00\">新增用户</td>" +
                "        <td style=\"background-color:#ff7f00\">环比</td>" +
                "        <td style=\"background-color:#ff7f00\">同比</td>" +
                "    </tr>");

        Integer top20NewUsers = new Integer(0);
        Integer top20ActiveUsers = new Integer(0);

        BigDecimal totalNewUsers = mapTotal.get(curDate);
        if (totalNewUsers == null) {
            totalNewUsers = new BigDecimal(0);
        }

        for (int i = 0; i < listChannelCur.size(); i++) {
            //只取前10个渠道
            if (i >= 10) {
                continue;
            }

            Map<String, Object> channel = listChannelCur.get(i);
            if (channel != null) {
                Date statdate = (Date) channel.get("statdate");
                BigDecimal daus = (BigDecimal) channel.get("daus");

                if (daus == null) {
                    daus = new BigDecimal("0");
                }
                Integer nNewUserCur = (Integer) channel.get("news");
                if (nNewUserCur == null) {
                    nNewUserCur = new Integer(0);
                }
                BigDecimal bdNewUserCur = new BigDecimal(nNewUserCur);


                String userlast1 = (String) channel.get("userlast1");
                String userlast7 = (String) channel.get("userlast7");
                String channelName = (String) channel.get("channel_name");
                String channelCode = (String) channel.get("channel");

                //环比
                Integer nChainNewUser = mapChannelChainNewUser.get(channelCode);
                if (nChainNewUser == null) {
                    nChainNewUser = new Integer(0);
                }
                BigDecimal bdChainNewUser = new BigDecimal(nChainNewUser);
                BigDecimal dbNewUser_ChainRate = BigDecimalUtil.getPercentage(bdNewUserCur.subtract(bdChainNewUser), bdChainNewUser, 2);

                //同比
                Integer nSameNewUser = mapChannelSameNewuser.get(channelCode);
                if (nSameNewUser == null) {
                    nSameNewUser = new Integer(0);
                }
                BigDecimal bdSameNewUser = new BigDecimal(nSameNewUser);
                BigDecimal dbNewUser_SameRate = BigDecimalUtil.getPercentage(bdNewUserCur.subtract(bdSameNewUser), bdSameNewUser, 2);

                BigDecimal bdChannelNewUserRate = BigDecimalUtil.getPercentage(new BigDecimal(nNewUserCur), totalNewUsers, 2);

                //
                int curRank = i + 1;
                Integer chainRank = mapChannelChainRank.get(channelCode);
                if (chainRank == null) {
                    chainRank = Integer.parseInt("100");
                }
                BigDecimal bdRank = new BigDecimal(chainRank - curRank);
                String strRank = bdRank.toString();
                if (bdRank.doubleValue() == 0) {
                    strRank = "";
                }
                top20NewUsers += nNewUserCur;
                top20ActiveUsers += daus.intValue();
                sbContent.append("<tr>" +
                        "        <td>" + curRank + "</td>" +
                        "        <td>" + channelName + "</td>" +
                        "        <td>" + getTrendRankNew(curRank, chainRank, 10) + "</td>" +
                        "        <td class='" + getTrendColor(bdRank) + "' >" + strRank + "</td>" +
                        "        <td>" + BigDecimalUtil.format(new BigDecimal(nNewUserCur), 0) + "</td>" +
                        "        <td class='" + getTrendColor(dbNewUser_ChainRate) + "' >" + dbNewUser_ChainRate + "%</td>" +
                        "        <td class='" + getTrendColor(dbNewUser_SameRate) + "' > " + dbNewUser_SameRate + "%</td>" +
                        //"        <td>" + BigDecimalUtil.format(daus, 0) + "</td>" +
                        "        <td>" + bdChannelNewUserRate + "%</td>" +
                        "    </tr>");
            }
        }
        //表格结束
        sbContent.append("<tr>" +
                "<td colspan=\"4\">总计</td>" +
                "<td>" + BigDecimalUtil.format(new BigDecimal(top20NewUsers), 0) + "</td>" +
                "<td>-</td>" +
                "<td>-</td>" +
                //"<td>" + BigDecimalUtil.format(new BigDecimal(top20ActiveUsers), 0) + "</td>" +
                "<td>" + BigDecimalUtil.getPercentage(new BigDecimal(top20NewUsers), totalNewUsers, 2) + "%</td>" +
                "</tr>");
        sbContent.append("</table>");
        sbContent.append("<br/>");

        return sbContent.toString();
    }

    /**
     * @param bdRate
     * @return
     */
    private String getTrendIcon(BigDecimal bdRate) {
        if (bdRate.signum() == 1) {
            return "arrow-up";
        } else if (bdRate.signum() == -1) {
            return "arrow-down";
        } else {
            return "";
        }
    }

    /**
     * @param bdRate
     * @return
     */
    private String getTrendColor(BigDecimal bdRate) {
        if (bdRate.signum() == 1) {
            return "riseColor";
        } else if (bdRate.signum() == -1) {
            return "reduceColor";
        } else {
            return "";
        }
    }

    /**
     * @param curRank
     * @param chainRank
     * @return
     */
    private String getTrendRank(Integer curRank, Integer chainRank, Integer base) {
        String rt = "";
        if (chainRank > base) {
            rt = "new";
        } else if ((chainRank - curRank) == 0) {
            rt = "=";
        } else {
            rt = "";
        }

        return rt;
    }

    /**
     * @param curRank
     * @param chainRank
     * @return
     */
    private String getTrendRankNew(Integer curRank, Integer chainRank, Integer base) {
        String rt = "";
        if (chainRank > base) {
            rt = "new";
        } else if ((chainRank - curRank) == 0) {
            rt = "〓";
        } else if ((chainRank - curRank) > 0) {
            rt = "<div style=\"margin-left:10%\" class=\"arrow-up\"></div>";
        } else if ((chainRank - curRank) < 0) {
            rt = "<div style=\"margin-left:10%\" class=\"arrow-down\"></div>";
        } else {
            rt = "";
        }

        return rt;
    }


}