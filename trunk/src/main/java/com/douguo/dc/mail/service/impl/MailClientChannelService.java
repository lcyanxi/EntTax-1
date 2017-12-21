package com.douguo.dc.mail.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douguo.dc.channel.model.Channel;
import com.douguo.dc.channel.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.applog.dao.AppUserNewDao;
import com.douguo.dc.applog.service.AppDauService;
import com.douguo.dc.applog.service.AppWauService;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mail.service.SysMailSetService;
import com.douguo.dc.mall.service.AppTuanService;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JavaSendMail;

@Repository("mailClientChannelService")
public class MailClientChannelService {

    @Autowired
    private AppDauService appDauService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private AppWauService appWauService;

    @Autowired
    private SysMailSetService sysMailSetService;

    @Autowired
    private AppUserNewDao appUserNewDao;

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
                mapChannel.put(strChannelCode,strChannelName);
            }

            // 1. 日活渠道TOP10数据
            String activeUserTop = activeUserTop(mapChannel);
            content.append("<h1>1、用户日活-渠道TOP10</h1><br/>");
            content.append(activeUserTop);
            content.append("<br/><br/>");

            // 2. 新增渠道TOP10数据
            String newUserTop = newUserTop(mapChannel);
            content.append("<h1>2、用户新增-渠道TOP10</h1><br/>");
            content.append(newUserTop);
            content.append("<br/><br/>");

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
//            SysMailSet sysMailSet = sysMailSetService
//                    .getSysMailSetByMailType(MailConstants.MAIL_TYPE_CLIENT_CHANNEL_SUM);

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
                mapChannel.put(strChannelCode,strChannelName);
            }

            // 1. 日活渠道TOP10数据
            String activeUserTop = activeUserTop(mapChannel);
            content.append("<h1>1、用户日活-渠道TOP10</h1><br/>");
            content.append(activeUserTop);
            content.append("<br/><br/>");

            // 2. 新增渠道TOP10数据
            String newUserTop = newUserTop(mapChannel);
            content.append("<h1>2、用户新增-渠道TOP10</h1><br/>");
            content.append(newUserTop);
            content.append("<br/><br/>");

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

    private String getAppName(String app) {
        if (app.equals("3")) {
            return "豆果美食(IOS)";
        } else if (app.equals("4")) {
            return "豆果美食(Android)";
        } else {
            return "未知";
        }
    }

    private String newUserTop(Map<String ,String> mapChannel) {
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
                sbContent.append("<h3>2.2、 Ios渠道TOP10</h4>");
                newapp = "3";
            } else if (app.equals("4")) {
                sbContent.append("<h3>2.3、 Android 市场渠道TOP10</h4>");
                newapp = "4";
            } else if (app.equals("4cpa")) {
                sbContent.append("<h3>2.4、 Android CPA渠道TOP10</h4>");
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
                        // 只取前100条数据
                        if (i > 100) {
                            break;
                        }
                    } else {
                        continue;
                    }
                } else if (app.equals("4cpa")) {
                    if (channel.indexOf("CPA") >= 0) {
                        i++;
                        // 只取前100条数据
                        if (i > 100) {
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
        //
        StringBuffer sumContent = new StringBuffer();
        sumContent.append("<h3>2.1 新增汇总</h2>");
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
        //
        sumContent.append(sbContent);

        return sumContent.toString();
    }

    private String activeUserTop(Map<String,String> mapChannel) {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        BigDecimal iosSum = new BigDecimal(0);
        BigDecimal androidSum = new BigDecimal(0);
        // 豆果美食，ios、android
        String[] apps = new String[]{"3", "4"};
        for (String app : apps) {
            List<Map<String, Object>> rowlist = appDauService.queryChannelList(startDate, endDate, app);
            int i = 0;
            // 构造表格
            sbContent.append("<table class=\"gridtable\">");
            sbContent.append("<tr><th>序号</th><th>APP</th><th>渠道</th><th>日活</th><th>占比</th></tr>");
            BigDecimal sumUids = new BigDecimal(0);
            for (Map<String, Object> map : rowlist) {
                BigDecimal uid = (BigDecimal) map.get("uid");
                sumUids = sumUids.add(uid);
            }

            //
            if (app.equals("3")) {
                iosSum = sumUids;
            } else if (app.equals("4")) {
                androidSum = sumUids;
            }

            for (Map<String, Object> map : rowlist) {
                i++;
                // 只取前10条数据
                if (i > 10) {
                    break;
                }

                String appName = getAppName(app);
                String channel = (String) map.get("channel");
                String channelName = mapChannel.get(channel);
                BigDecimal uid = (BigDecimal) map.get("uid");
                BigDecimal newUid = uid.multiply(new BigDecimal(100));
                BigDecimal rate = new BigDecimal(0);

                if (sumUids.longValue() != 0) {
                    rate = newUid.divide(sumUids, 2, BigDecimal.ROUND_HALF_UP);
                }

                //
                sbContent.append("<tr>");
                sbContent.append("<td>" + i + "</td>");
                sbContent.append("<td>" + appName + "</td>");
                sbContent.append("<td>" + channelName + "</td>");
                sbContent.append("<td>" + uid + "</td>");
                sbContent.append("<td>" + rate + "%</td>");
                sbContent.append("</tr>");
            }
            sbContent.append("</table>");
            sbContent.append("<br/>");
        }

        //
        List<Map<String, Object>> listIos = appWauService.queryListByApp(startDate, endDate, "3", "ALL");
        List<Map<String, Object>> listAndroid = appWauService.queryListByApp(startDate, endDate, "4", "ALL");
        Long iosWeekSum = new Long(0);
        Long androidWeekSum = new Long(0);
        if (listIos.size() > 0) {
            iosWeekSum = (Long) listIos.get(0).get("uid");
        }
        if (listAndroid.size() > 0) {
            androidWeekSum = (Long) listAndroid.get(0).get("uid");
        }

        StringBuffer sumContent = new StringBuffer();
        sumContent.append("<table class=\"gridtable\">");
        sumContent.append("<tr><th>序号</th><th>APP</th><th>活跃用户</th><th>周活跃</th></tr>");
        sumContent.append("<tr>");
        sumContent.append("<td>1</td>");
        sumContent.append("<td>IOS</td>");
        sumContent.append("<td>" + iosSum + "</td>");
        sumContent.append("<td>" + iosWeekSum + "</td>");
        sumContent.append("</tr>");
        sumContent.append("<tr>");
        sumContent.append("<td>2</td>");
        sumContent.append("<td>Android</td>");
        sumContent.append("<td>" + androidSum + "</td>");
        sumContent.append("<td>" + androidWeekSum + "</td>");
        sumContent.append("</tr>");
        sumContent.append("</table>");
        sumContent.append("<br/>");

        sumContent.append(sbContent);

        return sumContent.toString();
    }
}