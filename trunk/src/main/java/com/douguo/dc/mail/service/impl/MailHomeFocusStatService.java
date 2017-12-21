package com.douguo.dc.mail.service.impl;

import com.douguo.dc.dish.service.DishService;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mail.service.SysMailSetService;
import com.douguo.dc.service.EventsService;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JFreeChartUtil;
import com.douguo.dc.util.JavaSendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 首页焦点图数据统计
 */
@Repository("mailHomeFocusDishStatService")
public class MailHomeFocusStatService {

    @Autowired
    private EventsService eventsService;

    @Autowired
    private SysMailSetService sysMailSetService;

    /**
     * 发送作品相关统计邮件
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
                subject = "首页焦点图数据日报_" + curDate;
                to = "zhangyaozhou@douguo.com,zhangjianfei@douguo.com";
            }

            // 1. 基本数据统计
            String dishBaseStat = baseStat(sysMailSet.getDesc());
            String dishBaseStat_Android = baseStat_Android(sysMailSet.getDesc());
            String dishBaseStat_Ios = baseStat_Ios(sysMailSet.getDesc());
            
            content.append("<h1>1、首页焦点图数据统计</h1><br/>");
            content.append(dishBaseStat);
            content.append("<h1>2、首页焦点图数据统计-安卓客户端</h1><br/>");
            content.append(dishBaseStat_Android);
            content.append("<h1>3、首页焦点图数据统计-IOS客户端</h1><br/>");
            content.append(dishBaseStat_Ios);
            content.append("注：受限于日志收集的策略，点击数据有一定误差，最简方式“点击数据 * 2”约等于实际点击数。");
            content.append("<br/><br/>");

            content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");
            
            String[] imgPaths = new String[2];
            imgPaths[0] = MailConstants.MAIL_CHART_PATH + "/img-focus-sum.jpg";
            imgPaths[1] = MailConstants.MAIL_CHART_PATH + "/img-focus-sum-first.jpg";
            
            javaSendMail.sendHtmlWithInnerImageEmail(subject, to, content.toString(), imgPaths);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 构造邮件内容
     * 数据汇总 / 数据趋势图
     * @return
     */
    private String baseStat(String nDays) {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, Integer.parseInt(nDays));
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        //
        List<Map<String, Object>> rowlist = eventsService.getCustomEventDetailList(startDate, endDate, "RECIPE_HOME_TOP_RECOMMEND_CLICKED");
        List<Integer> listApp = new ArrayList<Integer>();
        List<String> listStatTime = new ArrayList<String>();
        List<String> listDimensionName = new ArrayList<String>();

        //汇总数据
        Map<String, Integer> mapSum = new HashMap<String, Integer>();
        //Ios
        Map<String, Integer> mapIos = new HashMap<String, Integer>();
        //Android
        Map<String, Integer> mapAndroid = new HashMap<String, Integer>();
        for (Map<String, Object> map : rowlist) {
            Integer appId = (Integer) map.get("app_id");
            Date dStatTime = (Date) map.get("stat_time");
            String statTime = DateUtil.date2Str(dStatTime,"yyyy-MM-dd");
            String dimensionName = (String) map.get("dimension_name");
            Integer pv = (Integer) map.get("pv");

            if (!listApp.contains(appId)) {
                listApp.add(appId);
            }

            if (!listStatTime.contains(statTime)) {
                listStatTime.add(statTime);
            }

            if (!listDimensionName.contains(dimensionName)) {
                listDimensionName.add(dimensionName);
            }

            String key = statTime + "_" + dimensionName;

            //汇总
            if (mapSum.containsKey(key)) {
                Integer oldPv = mapSum.get(key);
                mapSum.put(key, (oldPv + pv));
            } else {
                mapSum.put(key, pv);
            }

            //Ios
            if (!mapIos.containsKey(key) && appId == 3) {
                mapIos.put(key, pv);
            }

            //Android
            if (!mapAndroid.containsKey(key) && appId == 4) {
                mapAndroid.put(key, pv);
            }
        }

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");

        // 构造 title
        sbContent.append("<tr><th>序号</th><th>日期</th>");
        
        //首页图片编号排序
        Collections.sort(listDimensionName);
        
        for (String dName : listDimensionName) {
            sbContent.append("<th>" + dName + "</th>");
        }

        sbContent.append("<th>总计</th>") ;
        sbContent.append("<th>总计(修正)</th>") ;
        sbContent.append("</tr>");

        //浏览总数totalCount
        int totalCount = 0 ;
        int focusAll = 0 ;
        
        int i = 0;

        //
        for (String stTime : listStatTime) {
            i++;
            //
            sbContent.append("<tr>");
            sbContent.append("<td>" + i + "</td>");
            sbContent.append("<td>" + stTime + "</td>");

            for (String dName : listDimensionName) {
                Integer nPv = mapSum.get(stTime + "_" + dName);
                if(nPv == null){
                    nPv = 0;
                }
                sbContent.append("<th>" + nPv + "</th>");
                totalCount = totalCount + nPv ;
                focusAll = focusAll + nPv ;
            }

            //添加总数到TABLE
            sbContent.append("<td>" + totalCount + "</td>") ;
            sbContent.append("<td>" + totalCount*2 + "</td>") ;
            totalCount = 0 ;

            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");
        

        //构造图表=======================start
        //总点击量Y轴数据
        double[] arrayFocus = new double[i];
        int tempFocus = 0 ;
        int tempCount = 0 ;
        for (String stTime : listStatTime) {
            for (String dName : listDimensionName) {
                Integer nPv = mapSum.get(stTime + "_" + dName);
                if(nPv == null){
                    nPv = 0;
                }
                tempFocus = tempFocus + nPv ;
            }
            arrayFocus[tempCount] = tempFocus ;
            tempCount++ ;
            tempFocus = 0 ; //置0，避免每天的点击量累加
        }
        
        //第一帧Y轴数据
        double[] arrayFocusFirst = new double[i];
        int tempFocusFirst = 0  ;
        int tempCountFirst = 0  ;
        String focusDnameNo = "1" ; //设置需要显示的帧号
        for (String stTime : listStatTime) {
            for (String dName : listDimensionName) {
                if(focusDnameNo.equals(dName)){
                	Integer nPv = mapSum.get(stTime + "_" + dName);
                	if(nPv == null){
                		nPv = 0;
                	}
                	tempFocusFirst = tempFocusFirst + nPv ;
                }
            }
            arrayFocusFirst[tempCountFirst] = tempFocusFirst ;
            tempCountFirst++ ;
            tempFocusFirst = 0 ; //置0，避免每天的点击量累加
        }
        
        //图表数据点说明
        String[] rowKeysFocus = {"全部点击数"};
        String[] rowKeysFocusFirst = {"第一帧点击数"};

        //日期数量（X）
        System.out.println("listStatTime.size()的值："+listStatTime.size());
        String[] columnKeysDate = new String[listStatTime.size()];
        
        //把构造图表的日期数据复制到趋势图中
        for(int m=0; m<listStatTime.size(); m++){
        	columnKeysDate[m] = listStatTime.get(m)+"("+DateUtil.getWeekDay(listStatTime.get(m))+")" ;
        }
        
        //图片名称
        String chartTitleFocus = "首页点击数趋势";
        String chartTitleFocusFirst = "第一帧点击数趋势";

        //X轴名称
        String categoryLabelFocus = "日期";
        String categoryLabelFocusFirst = "日期";

        //y轴名称
        String valueLabelFocus = "首页点击总数";
        String valueLabelFocusFirst = "第一帧点击数";

        //数据集
        double[][] dataFocus = new double[1][];
        double[][] dataFocusFirst = new double[1][];

        dataFocus[0] = arrayFocus;
        dataFocusFirst[0] = arrayFocusFirst;

        //全部点击数趋势图
        JFreeChartUtil.buildLineChat(chartTitleFocus, categoryLabelFocus, valueLabelFocus, rowKeysFocus, columnKeysDate, dataFocus,
        		MailConstants.MAIL_CHART_PATH + "/img-focus-sum.jpg");

        //第一帧点击数趋势图
        JFreeChartUtil.buildLineChat(chartTitleFocusFirst, categoryLabelFocusFirst, valueLabelFocusFirst, rowKeysFocusFirst, columnKeysDate, dataFocusFirst,
        		MailConstants.MAIL_CHART_PATH + "/img-focus-sum-first.jpg");
        // ======= 生成图片 - end

        //添加图片到邮件中
        sbContent.append("<h1>趋势统计</h1><br/>");
        sbContent.append("<table class=\"gridtable\">");

        sbContent.append("<tr>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"1\" align=\"center\">整体数据</td>" +
                "        <td style=\"background-color:#ff7f00\" colspan=\"2\" align=\"center\">总点击数</td>" +
                "        <td style=\"background-color:#ff7f00\" colspan=\"2\" align=\"center\">第一帧点击数</td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td rowspan=\"1\">一周趋势</td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-focus-sum.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-focus-sum-first.jpg\"/></td>" +
                "    </tr>");

        sbContent.append("</table>");
        sbContent.append("<br/>");
        //构造图表=======================end
     
        return sbContent.toString();
    }
    
    
    /**
     * 构造邮件内容-Android
     * @Date	2016-7-27
     * @author  Zhangjianfei
     * @return
     */
    private String baseStat_Android(String nDays) {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, Integer.valueOf(nDays));
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        //
        List<Map<String, Object>> rowlist = eventsService.getCustomEventDetailList_Android(startDate, endDate, "RECIPE_HOME_TOP_RECOMMEND_CLICKED");

        List<Integer> listApp = new ArrayList<Integer>();
        List<String> listStatTime = new ArrayList<String>();
        List<String> listDimensionName = new ArrayList<String>();

        //汇总数据
        Map<String, Integer> mapSum = new HashMap<String, Integer>();
        //Ios
        Map<String, Integer> mapIos = new HashMap<String, Integer>();
        //Android
        Map<String, Integer> mapAndroid = new HashMap<String, Integer>();
        for (Map<String, Object> map : rowlist) {
            Integer appId = (Integer) map.get("app_id");
            Date dStatTime = (Date) map.get("stat_time");
            String statTime = DateUtil.date2Str(dStatTime,"yyyy-MM-dd");
            String dimensionName = (String) map.get("dimension_name");
            Integer pv = (Integer) map.get("pv");

            if (!listApp.contains(appId)) {
                listApp.add(appId);
            }

            if (!listStatTime.contains(statTime)) {
                listStatTime.add(statTime);
            }

            if (!listDimensionName.contains(dimensionName)) {
                listDimensionName.add(dimensionName);
            }

            String key = statTime + "_" + dimensionName;

            //汇总
            if (mapSum.containsKey(key)) {
                Integer oldPv = mapSum.get(key);
                mapSum.put(key, (oldPv + pv));
            } else {
                mapSum.put(key, pv);
            }

            //Ios
            if (!mapIos.containsKey(key) && appId == 3) {
                mapIos.put(key, pv);
            }

            //Android
            if (!mapAndroid.containsKey(key) && appId == 4) {
                mapAndroid.put(key, pv);
            }
        }

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");

        // 构造 title
        //sbContent.append("<tr><th>序号</th><th>日期</th><th>作品数</th><th>作品人数</th><th>菜谱作品数</th><th>菜谱作品人数</th><th>作品总赞数</th><th>作品总赞人数</th></tr>");
        sbContent.append("<tr><th>序号</th><th>日期</th>");
        
        Collections.sort(listDimensionName);
        
        for (String dName : listDimensionName) {
            sbContent.append("<th>" + dName + "</th>");
        }

        /*
        Append:total Count
        @author:    ZhangJianfei
        @date:      7/22/2016 14:00
         */
        sbContent.append("<th>总计</th>") ;
        sbContent.append("<th>总计(修正)</th>") ;
        sbContent.append("</tr>");

        //浏览总数totalCount
        int totalCount = 0 ;

        int i = 0;

        //
        for (String stTime : listStatTime) {
            i++;
            //
            sbContent.append("<tr>");
            sbContent.append("<td>" + i + "</td>");
            sbContent.append("<td>" + stTime + "</td>");

            for (String dName : listDimensionName) {
                Integer nPv = mapSum.get(stTime + "_" + dName);
                if(nPv == null){
                    nPv = 0;
                }
                sbContent.append("<th>" + nPv + "</th>");
                totalCount = totalCount + nPv ;
            }

            //添加总数到TABLE
            //@Zhangjianfei
            sbContent.append("<td>" + totalCount + "</td>") ;
            sbContent.append("<td>" + totalCount*2 + "</td>") ;
            totalCount = 0 ;

            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");

        return sbContent.toString();
    }
    
    /**
     * 构造邮件内容-Ios
     * @Date	2016-7-27
     * @author  Zhangjianfei
     * @return
     */
    private String baseStat_Ios(String nDays) {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, Integer.valueOf(nDays));
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        //
        List<Map<String, Object>> rowlist = eventsService.getCustomEventDetailList_Ios(startDate, endDate, "RECIPE_HOME_TOP_RECOMMEND_CLICKED");
        
        //Test
        System.out.println("rowList的长度："+rowlist.size());

        List<Integer> listApp = new ArrayList<Integer>();
        List<String> listStatTime = new ArrayList<String>();
        List<String> listDimensionName = new ArrayList<String>();

        //汇总数据
        Map<String, Integer> mapSum = new HashMap<String, Integer>();
        //Ios
        Map<String, Integer> mapIos = new HashMap<String, Integer>();
        //Android
        Map<String, Integer> mapAndroid = new HashMap<String, Integer>();
        for (Map<String, Object> map : rowlist) {
            Integer appId = (Integer) map.get("app_id");
            Date dStatTime = (Date) map.get("stat_time");
            String statTime = DateUtil.date2Str(dStatTime,"yyyy-MM-dd");
            String dimensionName = (String) map.get("dimension_name");
            Integer pv = (Integer) map.get("pv");

            if (!listApp.contains(appId)) {
                listApp.add(appId);
            }

            if (!listStatTime.contains(statTime)) {
                listStatTime.add(statTime);
            }

            if (!listDimensionName.contains(dimensionName)) {
                listDimensionName.add(dimensionName);
            }

            String key = statTime + "_" + dimensionName;

            //汇总
            if (mapSum.containsKey(key)) {
                Integer oldPv = mapSum.get(key);
                mapSum.put(key, (oldPv + pv));
            } else {
                mapSum.put(key, pv);
            }

            //Ios
            if (!mapIos.containsKey(key) && appId == 3) {
                mapIos.put(key, pv);
            }

            //Android
            if (!mapAndroid.containsKey(key) && appId == 4) {
                mapAndroid.put(key, pv);
            }
        }

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");

        // 构造 title
        //sbContent.append("<tr><th>序号</th><th>日期</th><th>作品数</th><th>作品人数</th><th>菜谱作品数</th><th>菜谱作品人数</th><th>作品总赞数</th><th>作品总赞人数</th></tr>");
        sbContent.append("<tr><th>序号</th><th>日期</th>");
        
        Collections.sort(listDimensionName);
        
        for (String dName : listDimensionName) {
            sbContent.append("<th>" + dName + "</th>");
        }

        /*
        Append:total Count
        @author:    ZhangJianfei
        @date:      7/22/2016 14:00
         */
        sbContent.append("<th>总计</th>") ;
        sbContent.append("<th>总计(修正)</th>") ;
        sbContent.append("</tr>");

        //浏览总数totalCount
        int totalCount = 0 ;

        int i = 0;

        //
        for (String stTime : listStatTime) {
            i++;
            //
            sbContent.append("<tr>");
            sbContent.append("<td>" + i + "</td>");
            sbContent.append("<td>" + stTime + "</td>");

            for (String dName : listDimensionName) {
                Integer nPv = mapSum.get(stTime + "_" + dName);
                if(nPv == null){
                    nPv = 0;
                }
                sbContent.append("<th>" + nPv + "</th>");
                totalCount = totalCount + nPv ;
            }

            //添加总数到TABLE
            //@Zhangjianfei
            sbContent.append("<td>" + totalCount + "</td>") ;
            sbContent.append("<td>" + totalCount*2 + "</td>") ;
            totalCount = 0 ;

            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");

        return sbContent.toString();
    }
}
