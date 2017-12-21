package com.douguo.dc.mail.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.dao.QuizeRplayDao;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JFreeChartUtil;
import com.douguo.dc.util.JavaSendMail;
import com.douguo.dc.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("/mailQuizeRplayStatService")
public class MailQuizeRplayStatService {
    @Autowired
    private QuizeRplayDao quizeRplayDao;

    List<String> imgUrlArr = new ArrayList<String>();

    public void sendMail(SysMailSet sysMailSet) {
        JavaSendMail javaSendMail = new JavaSendMail();

        String currDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String subject;
        String toAddress = "";
        StringBuffer content = new StringBuffer("");
        content.append("<style type=\"text/css\"> table.gridtable { font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse; } table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #dedede; } table.gridtable td { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff; } </style>");
        if (sysMailSet != null) {
            subject = sysMailSet.getSubject() + "_" + currDate;
            toAddress = sysMailSet.getMailTo();
        } else {
            subject = "问答类型报表统计_" + currDate;
            toAddress = "lichang@douguo.com";
        }

        String strConfig = StringUtils.trimToEmpty(sysMailSet.getConfig());
        JSONObject jsonConfig = JsonUtil.parseStrToJsonObj(strConfig);
        String strDesc = jsonConfig.getString("trace_days");
        Integer nDays = 7;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(strDesc)) {
            try {
                nDays = Integer.parseInt(strDesc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        content.append(queryQuizeRplayStat(nDays));

        String[] imgPaths = new String[imgUrlArr.size()];
        for (int i = 0; i < imgUrlArr.size(); i++) {
            imgPaths[i] = MailConstants.MAIL_CHART_PATH + "/" + imgUrlArr.get(i);
        }

        content.append("<br/><br/>");
        content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

        try {
            //发送邮件
            javaSendMail.sendHtmlWithInnerImageEmail(subject, toAddress, content.toString(), imgPaths);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String queryQuizeRplayStat(Integer beforeDays) {

        StringBuffer sbContent = new StringBuffer();

        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, beforeDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 0);


        List<Map<String, Object>> mapList = quizeRplayDao.queryquizeRplayList(startDate, endDate);


        // 构建问答数据表格开始'
        sbContent.append("<h1>1.1问答pv、uv数据</h1><br/>");
        sbContent.append("<table class=\"gridtable\">");
        // 构造 title

        sbContent.append("<tr>" +
                "<th colspan=\"7\">问答pv、uv数据</th>" +
                "</tr>");
        sbContent.append("<tr>" +
                "<td>日期</td>" +
                "<td>总PV</td>" +
                "<td>总UV</td>" +
               // "<td>问答圈PV</td>" +
                //"<td>问答圈UV</td>" +
               // "<td>首页问答iconPV</td>" +
               // "<td>首页问答iconUV</td>" +
                "</tr>");


        for (Map<String, Object> map : mapList) {
            Date statdate = (Date) map.get("statdate");
            Integer total_pv = (Integer) map.get("total_pv");
            Integer total_uv = (Integer) map.get("total_uv");
            Integer quizreplay_pv = (Integer) map.get("quizreplay_pv");
            Integer quizreplay_uv = (Integer) map.get("quizreplay_uv");
            Integer icon_pv = (Integer) map.get("icon_pv");
            Integer icon_uv = (Integer) map.get("icon_uv");

            //1.填值到表格
            sbContent.append("<tr>");
            sbContent.append("<td>" + statdate + "</td>");
            sbContent.append("<td>" + total_pv + "</td>");
            sbContent.append("<td>" + total_uv + "</td>");
            // sbContent.append("<td>" + quizreplay_pv + "</td>");
            //sbContent.append("<td>" + quizreplay_uv + "</td>");
            // sbContent.append("<td>" + icon_pv + "</td>");
            //sbContent.append("<td>" + icon_uv + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");

        String[] srctype = {"total_pv", "total_uv"};
        String[] srcName = {"总PV", "总UV"};

        // 构建问答数据表格开始'
        sbContent.append("<h1>1.2 问答pv、uv数据趋势图</h1><br/>");

        sbContent.append(buidLineChatTable(mapList,srctype,srcName));

        // 构建问答数据表格开始'
        sbContent.append("<h1>2.1问答详情数据</h1><br/>");
        sbContent.append("<table class=\"gridtable\">");
        // 构造 title

        sbContent.append("<tr>" +
                "<th colspan=\"11\">问答详情数据</th>" +
                "</tr>");
        sbContent.append("<tr>" +
                "<td>日期</td>" +
                "<td>总PV</td>" +
                "<td>总UV</td>" +
                "<td>提问数</td>" +
                "<td>提问总人数</td>" +
                "<td>回答数</td>" +
                "<td>回答总人数</td>" +
                "<td>问答浏览总数</td>" +
                "<td>问答浏览人总数</td>" +
                "<td>答案点赞总数</td>" +
                "<td>新增使用提问人数</td>" +
                "</tr>");
        for (Map<String, Object> map : mapList) {
            Date statdate = (Date) map.get("statdate");
            Integer total_pv = (Integer) map.get("total_pv");
            Integer total_uv = (Integer) map.get("total_uv");
            Integer post_num = (Integer) map.get("post_num");
            Integer post_person_num = (Integer) map.get("post_person_num");
            Integer reply_num = (Integer) map.get("reply_num");
            Integer reply_person_num = (Integer) map.get("reply_person_num");
            Integer view_group_quizreplay_num = (Integer) map.get("view_group_quizreplay_num");
            Integer view_group_quizreplay_person_num = (Integer) map.get("view_group_quizreplay_person_num");
            Integer like_num = (Integer) map.get("like_num");
            Integer new_post_num = (Integer) map.get("new_post_num");

            //1.填值到表格
            sbContent.append("<tr>");
            sbContent.append("<td>" + statdate + "</td>");
            sbContent.append("<td>" + total_pv + "</td>");
            sbContent.append("<td>" + total_uv + "</td>");
            sbContent.append("<td>" + post_num + "</td>");
            sbContent.append("<td>" + post_person_num + "</td>");
            sbContent.append("<td>" + reply_num + "</td>");
            sbContent.append("<td>" + reply_person_num + "</td>");
            sbContent.append("<td>" + view_group_quizreplay_num + "</td>");
            sbContent.append("<td>" + view_group_quizreplay_person_num + "</td>");
            sbContent.append("<td>" + like_num + "</td>");
            sbContent.append("<td>" + new_post_num + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");


        // 构建问答数据表格开始'
        sbContent.append("<h1>2.2问答数据</h1><br/>");


        sbContent.append("<table class=\"gridtable\">");
        // 构造 title
        sbContent.append("<tr>" +
                "<td>" + buildLineChatStat(mapList, "total_pv", "总PV") + "</td>" +
                "<td>" + buildLineChatStat(mapList, "total_uv", "总UV") + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>" + buildLineChatStat(mapList, "post_num", "提问数") + "</td>" +
                "<td>" + buildLineChatStat(mapList, "post_person_num", "提问总人数") + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>" + buildLineChatStat(mapList, "reply_num", "回答数") + "</td>" +
                "<td>" + buildLineChatStat(mapList, "reply_person_num", "回答总人数") + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>" + buildLineChatStat(mapList, "view_group_quizreplay_num", "问题浏览总数") + "</td>" +
                "<td>" + buildLineChatStat(mapList, "view_group_quizreplay_person_num", "问题浏览人总数") + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>" + buildLineChatStat(mapList, "like_num", "答案点赞总数") + "</td>" +
                "<td>" + buildLineChatStat(mapList, "new_post_num", "新增使用提问人数") + "</td>" +
                "</tr>");

        sbContent.append("</table>");
        sbContent.append("<br/>");

/*

        Map<String[],String[]> srcMap=new LinkedHashMap<>();
        String[] s1Type={"view_group_quizreplay_num","view_group_quizreplay_person_num"};
        String[] s1TypeName={"问答浏览总数","问答浏览人总数"};
        srcMap.put(s1Type,s1TypeName);
        String[] s2Type={"reply_num","reply_person_num"};
        String[] s2TypeName={"回答数","回答总人数"};
        srcMap.put(s2Type,s2TypeName);
        String[] s3Type={"post_num","post_person_num"};
        String[] s3TypeName={"提问数","提问总人数"};
        srcMap.put(s3Type,s3TypeName);

*/


        //sbContent.append("<h1>2.3折线图多条测试</h1><br/>");
        //sbContent.append(buidLineMoreChatTable(mapList,srcMap));


        return sbContent.toString();
    }

    /**
     * 单条折线图工具方法
     * @param srcMap
     * @param srctype
     * @param srcName
     * @return 返回折线图表格
     */
    private String buidLineChatTable(List<Map<String, Object>> srcMap, String[] srctype, String[] srcName) {
        StringBuffer sbContent = new StringBuffer();
        // 构建问答数据表格开始'
        sbContent.append("<table class=\"gridtable\">");
        sbContent.append("<tr>" +
                " <td style=\"background-color:#ff7f00\" rowspan=\"100\" align=\"center\">整体数据</td>" +
                " </tr>" +
                " <tr>");
        for (int i = 0; i < srctype.length; i++) {
            if (i % 2 != 0) {//i为奇数
                sbContent.append("<td colspan=\"2\">" + buildLineChatStat(srcMap, srctype[i], srcName[i]) + "</td>");
                sbContent.append("</tr>");
            } else {
                sbContent.append("<td colspan=\"2\">" + buildLineChatStat(srcMap, srctype[i], srcName[i]) + "</td>");
            }
        }

        sbContent.append("</table>");
        sbContent.append("<br/>");
        return sbContent.toString();

    }

    /**
     * 多条折线图表格生产
     * @param srcMap
     * @param map
     * @return 返回多条折线图表格
     */
    private String buidLineMoreChatTable(List<Map<String,Object>> srcMap,Map<String[],String[]> map){
        StringBuffer sbContent = new StringBuffer();
        // 构建问答数据表格开始'
        sbContent.append("<table class=\"gridtable\">");
        sbContent.append("<tr>" +
                " <td style=\"background-color:#ff7f00\" rowspan=\"100\" align=\"center\">整体数据</td>" +
                " </tr>" +
                " <tr>");
        int i=0;
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String[] key=(String[]) entry.getKey();
                String[] value =(String[]) entry.getValue();
                buildLineChatMoreStat(srcMap,key,value);
                if (i % 2 != 0) {//i为奇数
                    sbContent.append("<td colspan=\"2\">" + buildLineChatMoreStat(srcMap,key,value)+ "</td>");
                    sbContent.append("</tr>");
                } else {
                    sbContent.append("<td colspan=\"2\">" + buildLineChatMoreStat(srcMap,key,value)+ "</td>");
                }
                i++;
            }

        sbContent.append("</table>");
        sbContent.append("<br/>");
        return sbContent.toString();
    }

    /**
     * 构建单条折线图
     * @param srcMap
     * @param srctype
     * @param srcName
     * @return
     */
    private String buildLineChatStat(List<Map<String, Object>> srcMap, String srctype, String srcName) {
        StringBuffer sbContent = new StringBuffer();

        String[] columnSrctypeKeys = new String[srcMap.size()]; // X轴数据集
        double[] arraySrctypeUv = new double[srcMap.size()]; //图表数据集

        int index = 0;
        for (Map<String, Object> map : srcMap) {
            Date statdate = (Date) map.get("statdate");
            Integer value = (Integer) map.get(srctype);
            columnSrctypeKeys[index] = statdate.toString();
            arraySrctypeUv[index] = value;
            index++;
        }

        //图片名称
        String chartTitleSrctype = srcName + " 变化趋势图";

        //趋势折线图数据点说明
        String[] rowKeysSrctypeUv = {srcName};

        //X轴名称
        String categoryLabelSrctype = "日期";

        // y轴名称
        String valueLabelSrctype = "数量";

        //数据集
        double[][] dataSrctype = new double[1][];
        dataSrctype[0] = arraySrctypeUv;

        //图片完整名称
        String imgName = "img-" + srctype + "_-uv-stat.jpg";
        imgUrlArr.add(imgName);
        //菜谱数折线图
        //图表名称  X轴Lable  Y轴Lable  Y列值  X轴列值  数据集  图片路径
        JFreeChartUtil.buildLineChat(chartTitleSrctype, categoryLabelSrctype, valueLabelSrctype, rowKeysSrctypeUv, columnSrctypeKeys, dataSrctype,
                MailConstants.MAIL_CHART_PATH + "/" + imgName);

        sbContent.append("<td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"src=\"cid:" + imgName + "\"/></td>");

        return sbContent.toString();
    }

    /**
     * 多条折线图工具方法
     *
     * @param srcMap
     * @param srctype
     * @param srcName
     * @return
     */
    private String buildLineChatMoreStat(List<Map<String, Object>> srcMap, String[] srctype, String[] srcName) {
        StringBuffer sbContent = new StringBuffer();

        //String[] columnSrctypeKeys = new String[srcMap.size()]; // X轴数据集
        //double[] arraySrctypeUv = new double[srcMap.size()]; //图表数据集

        List<double[]> list = new ArrayList();
        String[] columnSrctypeKeys = null;
        for (int i = 0; i < srctype.length; i++) {
            double[] arraySrctypeUv = new double[srcMap.size()]; //图表数据集
            columnSrctypeKeys = new String[srcMap.size()]; // X轴数据集
            int index = 0;
            for (Map<String, Object> map : srcMap) {
                Date statdate = (Date) map.get("statdate");
                Integer value = (Integer) map.get(srctype[i]);
                columnSrctypeKeys[index] = statdate.toString();
                arraySrctypeUv[index] = value;
                index++;
            }
            list.add(arraySrctypeUv);
        }

        //图片名称
        String chartTitleSrctype = " 变化趋势图";

        //趋势折线图数据点说明
        // String[] rowKeysSrctypeUv = {srcName};

        //X轴名称
        String categoryLabelSrctype = "日期";

        // y轴名称
        String valueLabelSrctype = "数量";

        //数据集
        double[][] dataSrctype = new double[srctype.length][];
        for (int j = 0; j < list.size(); j++) {
            dataSrctype[j] = list.get(j);
        }

        //图片完整名称
        String imgName = "img-" + srctype + "_-uv-stat.jpg";
        imgUrlArr.add(imgName);
        //菜谱数折线图
        //图表名称  X轴Lable  Y轴Lable  Y列值  X轴列值  数据集  图片路径
        JFreeChartUtil.buildLineChat(chartTitleSrctype, categoryLabelSrctype, valueLabelSrctype, srcName, columnSrctypeKeys, dataSrctype,
                MailConstants.MAIL_CHART_PATH + "/" + imgName);

        sbContent.append("<td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"src=\"cid:" + imgName + "\"/></td>");

        return sbContent.toString();
    }


}
