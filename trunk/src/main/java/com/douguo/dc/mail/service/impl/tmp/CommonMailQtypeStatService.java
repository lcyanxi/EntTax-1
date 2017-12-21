package com.douguo.dc.mail.service.impl.tmp;

import com.alibaba.fastjson.JSONObject;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.serverlog.dao.ServerLogSrctypeDictDao;
import com.douguo.dc.serverlog.service.ServerLogQtypeSrctypeStatService;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JFreeChartUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository("commonMailQtypeStatService")
public class CommonMailQtypeStatService {


    @Autowired
    private ServerLogQtypeSrctypeStatService serverLogQtypeSrctypeStatService;

    @Autowired
    private ServerLogSrctypeDictDao serverLogSrctypeDictDao;

    /**
     * 构造邮件内容 2
     *
     * @return
     */
    public Map qtypeClassificationStat(JSONObject jsonConfig) {

        Map dstMap = new HashMap();

        String strDesc = jsonConfig.getString("trace_days");
        String qtypeArrStr = jsonConfig.getString("qtypeStr");
        String qtypeArrChStr = jsonConfig.getString("qtypeCh");
        String srcPosition = jsonConfig.getString("position");


        String[] qtypeArr = qtypeArrStr.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        String[] qtypeArrCh = qtypeArrChStr.split(",");

        Integer nDays = 9;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(strDesc)) {
            try {
                nDays = Integer.parseInt(strDesc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String position = "3";
        if (org.apache.commons.lang3.StringUtils.isNotBlank(srcPosition)) {
            position = srcPosition;
        }


        //图片路径LIST
        List<String> imgUrlArr = new ArrayList<String>();
        List<String> qtypeList = new ArrayList<String>();

        for (String qtype : qtypeArr) {
            qtypeList.add(qtype);
        }


        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, nDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);

        int i = 0; //二级标题序列号

        List<Map<String, Object>> rowlistSum = serverLogQtypeSrctypeStatService.querySumListByClientQtype(startDate, endDate, "3", "4");

        for (String qtype : qtypeArr) {

            List<String> statdateList = new ArrayList<String>(); //table日期
            List<Map<String, Object>> rowlist = new ArrayList<Map<String, Object>>(); // 存储单个Qtype的list集
            Map<String, Integer> mapUV = new HashMap<String, Integer>(); //TABLE数据map
            List<String> srctypeList = new ArrayList<String>(); // 存储Srctype的list

            i++;

            // 构造数据集
            for (Map<String, Object> map : rowlistSum) {
                if (qtype.equals(map.get("qtype"))) {
                    rowlist.add(map);
                }
            }

            //获取Qtype相对应的中文名称
            String qtypeChName = "";
            for (int k = 0; k < qtypeArrCh.length; k++) {
                if (qtype.equals(qtypeArr[k])) {
                    qtypeChName = qtypeArrCh[k];
                }
            }

            int tableOrder = 0; //table序号

            for (Map<String, Object> map : rowlist) {
                String statdate = (String) map.get("statdate").toString();
                String srctype = (String) map.get("srctype");
                String srctype_name = (String) map.get("srctype_name");
                srctype = srctype_name + "(" + srctype + ")";
                Integer uv = ((BigDecimal) map.get("uv")).intValue();

                if (!statdateList.contains(statdate)) {
                    statdateList.add(statdate);
                }
                if (!srctypeList.contains(srctype)) {
                    srctypeList.add(srctype);
                }

                //构造key
                String key = statdate + qtype + srctype;
                //构造map:
                if (qtypeList.contains(qtype)) {
                    mapUV.put(key, uv);
                }
            }

            // 构造二级标题
            sbContent.append("<h2>" + position + ".1" + qtypeChName + "-数据统计</h2></br>");

            // 构造表格
            sbContent.append("<table class=\"gridtable\">");

            // 构造TABLEtitle
            sbContent.append("<tr><th>序号</th><th>日期</th>");
            for (int k = 0; k < srctypeList.size(); k++) {
                String mapSrctypeKey = srctypeList.get(k);
                sbContent.append("<th>" + mapSrctypeKey + "</th>");
            }
            sbContent.append("</tr>");

            // 构造数据
            for (String stattime : statdateList) {
                tableOrder++;
                sbContent.append("<tr>");
                sbContent.append("<td>" + tableOrder + "</td>");

                //get the week day of stattime, like: 2016-10-16 >> 2016-10-16(二)
                String stattimeWithWeek = stattime + "(" + DateUtil.getWeekDay(stattime) + ")";

                sbContent.append("<td>" + stattimeWithWeek + "</td>");

                for (String srctypeTemp : srctypeList) {
                    Integer qtype_srctype_uv = (Integer) mapUV.get(stattime + qtype + srctypeTemp);
                    if (qtype_srctype_uv == null) {
                        qtype_srctype_uv = 0;
                    }
                    sbContent.append("<td>" + qtype_srctype_uv + "</td>");
                }
                sbContent.append("</tr>");
            }

            sbContent.append("</table>");
            sbContent.append("<br/>");
            // 构造表格 END

            List<String> imgNameForTable = new ArrayList<String>();

            int temp = 1;

            // 构造趋势图BEGIN
            for (String srctype : srctypeList) {

                List<Map<String, Object>> specialSrctypeList = new ArrayList<Map<String, Object>>(); //单个Srctype对应存储的数据集

                for (Map<String, Object> map : rowlist) {
                    String tempSrctype = (String) map.get("srctype");
                    String tempSrctype_name = (String) map.get("srctype_name");
                    tempSrctype = tempSrctype_name + "(" + tempSrctype + ")";

                    if (srctype.equals(tempSrctype)) {
                        specialSrctypeList.add(map);
                    }
                }

                String[] columnSrctypeKeys = new String[specialSrctypeList.size()]; // X轴数据集
                double[] arraySrctypeUv = new double[specialSrctypeList.size()]; //图表数据集

                for (int j = 0; j < specialSrctypeList.size(); j++) {
                    arraySrctypeUv[j] = ((BigDecimal) specialSrctypeList.get(j).get("uv")).intValue();
                    //获取日期 : 格式：2016-09-27(二)
                    String tempColumnKeys = (specialSrctypeList.get(j).get("statdate")).toString();
                    columnSrctypeKeys[j] = tempColumnKeys + "(" + DateUtil.getWeekDay(tempColumnKeys) + ")";
                }


                //图片名称
                String mapSrctypeValue = "";
                String mapSrctypeKey = srctype;
                String chartTitleSrctype = "来源：" + mapSrctypeKey + " 变化趋势图";

                //趋势折线图数据点说明
                String[] rowKeysSrctypeUv = {"UV数"};

                //X轴名称
                String categoryLabelSrctype = "日期";

                // y轴名称
                String valueLabelSrctype = "UV";

                //数据集
                double[][] dataSrctype = new double[1][];
                dataSrctype[0] = arraySrctypeUv;

                //图片完整名称
                String imgName = "img-" + qtype + "_" + temp + "-uv-stat.jpg";
                temp++;
                //菜谱数折线图
                //图表名称  X轴Lable  Y轴Lable  Y列值  X轴列值  数据集  图片路径
                JFreeChartUtil.buildLineChat(chartTitleSrctype, categoryLabelSrctype, valueLabelSrctype, rowKeysSrctypeUv, columnSrctypeKeys, dataSrctype,
                        MailConstants.MAIL_CHART_PATH + "/" + imgName);

                imgUrlArr.add(imgName);
                imgNameForTable.add(imgName);

                // 生成图片 - end
            }
            //添加图片到邮件中
            sbContent.append("<h2>" + position + ".2" + qtypeChName + "-趋势图</h2></br>");
            sbContent.append("<table class=\"gridtable\">");
            sbContent.append("<tr>" +
                    "        <td style=\"background-color:#ff7f00\" rowspan=\"100\" align=\"center\">整体数据</td>" +
                    "    </tr>" +
                    "    <tr>");

            for (int j = 0; j < imgNameForTable.size(); j++) {
                if (j % 2 != 0) {//J为奇数
                    sbContent.append("<td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"src=\"cid:" + imgNameForTable.get(j) + "\"/></td>");
                    sbContent.append("</tr>");
                } else {
                    sbContent.append("<td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"src=\"cid:" + imgNameForTable.get(j) + "\"/></td>");
                }
            }
            sbContent.append("</table>");
            sbContent.append("<br/>");
        }

        dstMap.put("sbContent", sbContent.toString());
        dstMap.put("imgUrlArr", imgUrlArr);

        return dstMap;
    }

    /**
     * srcQtype通用邮件报表按uv降序
     *
     * @param jsonConfig
     * @return
     */
    public Map qtypeClassificationStatOderByNum(JSONObject jsonConfig) {

        Map dstMap = new HashMap();

        String strDesc = jsonConfig.getString("trace_days");
        String qtypeArrStr = jsonConfig.getString("qtypeStr");
        String qtypeArrChStr = jsonConfig.getString("qtypeCh");
        String srcPosition = jsonConfig.getString("position");


        String[] qtypeArr = qtypeArrStr.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        String[] qtypeArrCh = qtypeArrChStr.split(",");

        Integer nDays = 9;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(strDesc)) {
            try {
                nDays = Integer.parseInt(strDesc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String position = "3";
        if (org.apache.commons.lang3.StringUtils.isNotBlank(srcPosition)) {
            position = srcPosition;
        }


        //图片路径LIST
        List<String> imgUrlArr = new ArrayList<String>();
        List<String> qtypeList = new ArrayList<String>();

        for (String qtype : qtypeArr) {
            qtypeList.add(qtype);
        }


        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, nDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);

        int i = 0; //二级标题序列号

        List<Map<String, Object>> rowlistSum = serverLogQtypeSrctypeStatService.querySumListByClientQtype(startDate, endDate, "3", "4");
        List<Map<String, Object>> srctypeNameList = serverLogSrctypeDictDao.querySrctypeList();//srctype对应的srctypeName


        for (String qtype : qtypeArr) {

            List<String> statdateList = new ArrayList<String>(); //table日期
            List<Map<String, Object>> rowlist = new ArrayList<Map<String, Object>>(); // 存储单个Qtype的list集
            Map<String, Integer> mapUV = new HashMap<String, Integer>(); //TABLE数据map

            i++;

            // 构造数据集
            for (Map<String, Object> map : rowlistSum) {
                if (qtype.equals(map.get("qtype"))) {
                    rowlist.add(map);
                }
            }

            //获取Qtype相对应的中文名称
            String qtypeChName = "";
            for (int k = 0; k < qtypeArrCh.length; k++) {
                if (qtype.equals(qtypeArr[k])) {
                    qtypeChName = qtypeArrCh[k];
                }
            }

            List<String> srctypeList = sortSrcQtypeUv(rowlist, srctypeNameList, endDate);//存储Srctype的list

            int tableOrder = 0; //table序号

            for (Map<String, Object> map : rowlist) {
                String statdate = (String) map.get("statdate").toString();
                String srctype = (String) map.get("srctype");
                String srctype_name = (String) srctypeName(srctypeNameList).get(srctype);
                srctype = srctype_name + "(" + srctype + ")";
                Integer uv = ((BigDecimal) map.get("uv")).intValue();

                if (!statdateList.contains(statdate)) {
                    statdateList.add(statdate);
                }
                //构造key
                String key = statdate + qtype + srctype;
                //构造map:
                if (qtypeList.contains(qtype)) {
                    mapUV.put(key, uv);
                }
            }

            // 构造二级标题
            sbContent.append("<h2>" + position + ".1" + qtypeChName + "-数据统计</h2></br>");

            // 构造表格
            sbContent.append("<table class=\"gridtable\">");

            // 构造TABLEtitle
            sbContent.append("<tr><th>序号</th><th>日期</th>");
            for (int k = 0; k < srctypeList.size(); k++) {
                String mapSrctypeKey = srctypeList.get(k);
                sbContent.append("<th>" + mapSrctypeKey + "</th>");
            }
            sbContent.append("</tr>");

            // 构造数据
            for (String stattime : statdateList) {
                tableOrder++;
                sbContent.append("<tr>");
                sbContent.append("<td>" + tableOrder + "</td>");

                //get the week day of stattime, like: 2016-10-16 >> 2016-10-16(二)
                String stattimeWithWeek = stattime + "(" + DateUtil.getWeekDay(stattime) + ")";

                sbContent.append("<td>" + stattimeWithWeek + "</td>");

                for (String srctypeTemp : srctypeList) {
                    Integer qtype_srctype_uv = (Integer) mapUV.get(stattime + qtype + srctypeTemp);
                    if (qtype_srctype_uv == null) {
                        qtype_srctype_uv = 0;
                    }
                    sbContent.append("<td>" + qtype_srctype_uv + "</td>");
                }
                sbContent.append("</tr>");
            }

            sbContent.append("</table>");
            sbContent.append("<br/>");
            // 构造表格 END

            List<String> imgNameForTable = new ArrayList<String>();

            int temp = 1;
            // 构造趋势图BEGIN
            for (String srctype : srctypeList) {

                List<Map<String, Object>> specialSrctypeList = new ArrayList<Map<String, Object>>(); //单个Srctype对应存储的数据集

                for (Map<String, Object> map : rowlist) {
                    String tempSrctype = (String) map.get("srctype");
                    String tempSrctype_name = (String) srctypeName(srctypeNameList).get(tempSrctype);
                    tempSrctype = tempSrctype_name + "(" + tempSrctype + ")";

                    if (srctype.equals(tempSrctype)) {
                        specialSrctypeList.add(map);
                    }
                }

                String[] columnSrctypeKeys = new String[specialSrctypeList.size()]; // X轴数据集
                double[] arraySrctypeUv = new double[specialSrctypeList.size()]; //图表数据集

                for (int j = 0; j < specialSrctypeList.size(); j++) {
                    arraySrctypeUv[j] = ((BigDecimal) specialSrctypeList.get(j).get("uv")).intValue();
                    //获取日期 : 格式：2016-09-27(二)
                    String tempColumnKeys = (specialSrctypeList.get(j).get("statdate")).toString();
                    columnSrctypeKeys[j] = tempColumnKeys + "(" + DateUtil.getWeekDay(tempColumnKeys) + ")";
                }


                //图片名称
                String mapSrctypeKey = srctype;
                String chartTitleSrctype = "来源：" + mapSrctypeKey + " 变化趋势图";

                //趋势折线图数据点说明
                String[] rowKeysSrctypeUv = {"UV数"};

                //X轴名称
                String categoryLabelSrctype = "日期";

                // y轴名称
                String valueLabelSrctype = "UV";

                //数据集
                double[][] dataSrctype = new double[1][];
                dataSrctype[0] = arraySrctypeUv;

                //图片完整名称
                String imgName = "img-" + qtype + "_" + temp + "-uv-stat.jpg";
                temp++;
                //菜谱数折线图
                //图表名称  X轴Lable  Y轴Lable  Y列值  X轴列值  数据集  图片路径
                JFreeChartUtil.buildLineChat(chartTitleSrctype, categoryLabelSrctype, valueLabelSrctype, rowKeysSrctypeUv, columnSrctypeKeys, dataSrctype,
                        MailConstants.MAIL_CHART_PATH + "/" + imgName);

                imgUrlArr.add(imgName);
                imgNameForTable.add(imgName);

                // 生成图片 - end
            }
            //添加图片到邮件中
            sbContent.append("<h2>" + position + ".2" + qtypeChName + "-趋势图</h2></br>");
            sbContent.append("<table class=\"gridtable\">");
            sbContent.append("<tr>" +
                    "        <td style=\"background-color:#ff7f00\" rowspan=\"100\" align=\"center\">整体数据</td>" +
                    "    </tr>" +
                    "    <tr>");

            for (int j = 0; j < imgNameForTable.size(); j++) {
                if (j % 2 != 0) {//J为奇数
                    sbContent.append("<td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"src=\"cid:" + imgNameForTable.get(j) + "\"/></td>");
                    sbContent.append("</tr>");
                } else {
                    sbContent.append("<td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"src=\"cid:" + imgNameForTable.get(j) + "\"/></td>");
                }
            }
            sbContent.append("</table>");
            sbContent.append("<br/>");
        }

        dstMap.put("sbContent", sbContent.toString());
        dstMap.put("imgUrlArr", imgUrlArr);

        return dstMap;
    }


    /**
     * 按srcQtype的uv进行排序
     *
     * @param srcList
     * @param endDate
     * @return
     */
    private List sortSrcQtypeUv(List<Map<String, Object>> srcList, List<Map<String, Object>> srctypeNameList, String endDate) {

        List sortSrcQtype = new ArrayList<>();
        Map<BigDecimal, String> treeMap = new TreeMap<>();

        for (Map<String, Object> map : srcList) { //以昨天为基准按uv进行排序
            if (map.get("statdate").toString().equals(endDate)) {
                String srctype = (String) map.get("srctype");
                String srctype_name = (String) srctypeName(srctypeNameList).get(srctype);
                srctype = srctype_name + "(" + srctype + ")";
                treeMap.put((BigDecimal) map.get("uv"), srctype);
            }
        }

        Iterator iter = treeMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object value = entry.getValue();
            if (!sortSrcQtype.contains(value)) {
                sortSrcQtype.add(value);
            }
        }

        Collections.reverse(sortSrcQtype);
        return sortSrcQtype;
    }

    /**
     * 获取srctype和srctypeName的对应关系
     * @param list
     * @return
     */
    private Map srctypeName(List<Map<String, Object>> list) {
        Map map = new HashMap();
        for (Map<String, Object> map1 : list) {
            String srctype = (String) map1.get("srctype");
            String srctypeName = (String) map1.get("srctype_name");
            map.put(srctype, srctypeName);
        }
        return map;
    }

}
