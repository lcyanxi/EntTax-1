package com.douguo.dc.datamoniter.web;

import com.douguo.dc.datamoniter.model.DataResult;
import com.douguo.dc.datamoniter.service.DataMoniterService;
import com.douguo.dc.datamoniter.util.DgEsUtils;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JavaSendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.UnknownHostException;
import java.util.*;

@Controller
@RequestMapping("/datamoniter")
public class DataMoniterController {

	@Autowired
	private DataMoniterService dataMoniterService;

    /**
     * 管理页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/preList")
    public String preList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws UnknownHostException {

        // get all indices
        DgEsUtils.startupClient("192.168.1.181", "dg_es_cluster", 9300);
//        DgEsUtils.startupClient("192.168.1.235", "dg_es1_cluster", 9300);
        Set<String> set = DgEsUtils.getAllIndices();

        List<DataResult> mapList = new ArrayList<DataResult>();

        for (String index : set){
            //获取索引信息
            String historyDateCounts = DgEsUtils.getHistoryDateCounts(index);
            String clusterStatus = DgEsUtils.getClusterStatus(index);
            String lastUpdateDateAndDoc = DgEsUtils.getLastUpdateTimeAndDocs(index);
            String indexCreatedDate = DgEsUtils.getIndexCretedFormatTime(index);

            String lastUpdateDate = null;
            String lastUpdateDoc = null;
            //分解信息字符串gen
            if ("unknown".equals(lastUpdateDateAndDoc)){
                lastUpdateDate = "未知更新时间";
                lastUpdateDoc = "未知更新数量";
            } else {
                String[] lastUpdateDateAndDocArr = lastUpdateDateAndDoc.split("~");
                lastUpdateDate = lastUpdateDateAndDocArr[0];
                lastUpdateDoc = lastUpdateDateAndDocArr[1];
            }

            //构造信息model
            DataResult dataResult = new DataResult();
            dataResult.setType("Elastic Search");
            dataResult.setIndex(index);
            dataResult.setClusterStatus(clusterStatus);
            dataResult.setHistoryDateCounts(historyDateCounts);
            dataResult.setLastUpdateDate(lastUpdateDate);
            dataResult.setLastUpdateDocs(lastUpdateDoc);
            dataResult.setIndexCreatedDate(indexCreatedDate);

            //构建昨日日期，用于比较最后更新时间是否正常
            String compareDateBefor = DateUtil.getSpecifiedDayBefore(DateUtil.getDate(), 1);

            //设置索引数据健康状态flag
            // 0-无标色:索引正常，数据量大于100000并且更新时间为昨日
            // 1-red:数量不足100000并且更新日期非昨日
            // 2-yellow:数量不足100000或者更新日期非昨日
            if ( Integer.valueOf(clusterStatus) > 100000 && compareDateBefor.equals(lastUpdateDate) ){
                dataResult.setIsNormal(0);
            }
            else if ( Integer.valueOf(clusterStatus) <= 100000 || !(compareDateBefor.equals(lastUpdateDate)) ){
                dataResult.setIsNormal(2);
            }
            else if ( Integer.valueOf(clusterStatus) <= 100000 && !(compareDateBefor.equals(lastUpdateDate)) ) {
                dataResult.setIsNormal(1);
            }
            else {
                dataResult.setIsNormal(0);
            }
            mapList.add(dataResult);
        }

        //销毁client对象
        DgEsUtils.shutDownClient();

        //对结果集进行排序
        Collections.sort(mapList, new Comparator<DataResult>() {
            @Override
            public int compare(DataResult o1, DataResult o2) {
                String o1s = o1.getIndex();
                String o2s = o2.getIndex();
                if (o1 == null || o2 == null){
                    return -1;
                }
                if(o1s == null || o2s == null){
                    return -1;
                }
                if(o1s.length() > o2s.length()){
                    return 1;
                }
                if(o1s.length() < o2s.length()){
                    return -1;
                }
                if(o1s.compareTo(o2s) > 0){
                    return 1;
                }
                if(o1s.compareTo(o2s) < 0){
                    return -1;
                }
                if(o1s.compareTo(o2s) == 0){
                    return 0;
                }
                return 0;
            }
        });

        model.put("mapList", mapList);

        return "/monitor/monitor_data_manager";
    }

    //not used
    private static String getDataMoniterMailContent(List<Map<String, Object>> rowlist){

        StringBuffer sbContent = new StringBuffer();

        //开始构造HTML邮件体
        sbContent.append("<style type=\"text/css\"> " +
                        "table.gridtable { " +
                            "font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; " +
                            "border-color: #666666; border-collapse: collapse; } " +
                        "table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; " +
                            "background-color: #dedede; } table.gridtable td { border-width: 1px; padding: 8px; border-style: " +
                            "solid; border-color: #666666; background-color: #ffffff; } </style>");
        // 构造表格
        sbContent.append("<table class=\"gridtable\">");
        // 构造 title
        sbContent.append("<tr><th>序号</th><th>数据类型</th><th>数据来源</th><th>数据量</th></tr>");

        int i = 0;

        //
        for (Map<String, Object> map : rowlist) {
            i++;

            Date statdate = (Date) map.get("statdate");
//            String strWeek = DateUtil.getWeekDay(statdate);
            String type = (String) map.get("type");
            String name = (String) map.get("name");
            Integer docs = (Integer) map.get("docs");

            //
            sbContent.append("<tr>");
            sbContent.append("<td>" + i + "</td>");
//            sbContent.append("<td>" + statdate + "(" + strWeek + ")</td>");
            sbContent.append("<td>" + type + "</td>");
            sbContent.append("<td>" + name + "</td>");
            sbContent.append("<td>" + docs + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");
        sbContent.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

        return sbContent.toString();
    }

    //not used
    private static void sendDataMoniterMail(String mailContent, String recivers, String subject) throws Exception {

        String curDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String to = recivers.toString();

        JavaSendMail javaSendMail = new JavaSendMail();
        javaSendMail.sendHtmlEmail(subject, recivers, mailContent);

    }


}
