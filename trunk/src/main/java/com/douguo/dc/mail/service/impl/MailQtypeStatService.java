package com.douguo.dc.mail.service.impl;

import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.serverlog.service.ServerLogQtypeSrctypeStatService;
import com.douguo.dc.serverlog.service.ServerLogQtypeStatService;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JFreeChartUtil;
import com.douguo.dc.util.JavaSendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Repository("mailQtypeStatService")
public class MailQtypeStatService {

    @Autowired
    private ServerLogQtypeStatService serverLogQtypeStatService;
    
    @Autowired
    private ServerLogQtypeSrctypeStatService serverLogQtypeSrctypeStatService ;
    
    //Qtype类型
    String[] qtypeArr = {
			    		"view_caipu_detail",
			    		"search_caipu",
			    		"view_caipu_zuopins",
			    		"view_dish_home",
			    		"view_caipu_caidan_detail",
			    		"action_fav_caipu",
			    		"view_activity_list",
			    		"view_zuopin_detail",
			            "live_class_detail",
			    		} ;
    
    String[] qtypeArrCh = {
    						"查看菜谱(50W)",
    						"搜索菜谱(40W)",
    						"查看菜谱作品(12W)",
    						"秀美食首页(7W)",
    						"查看菜单(6W)",
    						"菜谱收藏(11W)",
    						"查看活动列表(8W)",
    						"查看作品详情(6W)",
			                "查看课堂详情"
    						} ;
    
    //SrcType类型
    List<Map<String,String>> srctypeListConstants = getSrctype() ;
    
    List<String> qtypeList = new ArrayList<String>();
    
    //图片路径LIST
    List<String> imgUrlArr = new ArrayList<String>() ;
    List<String> imgUrlArrQtype = new ArrayList<String>() ;
    List<String> imgUrlArrQtypeSrctype = new ArrayList<String>() ;

    /**
     * 发送Qtype相关统计邮件
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
                subject = "Qtype统计报表_" + curDate;
                to = "zhangyaozhou@douguo.com";
            }

            String strDesc = sysMailSet.getDesc();
            //默认为8
            Integer nDays = 8;
            if (org.apache.commons.lang3.StringUtils.isNotBlank(strDesc)) {
                try {
                    nDays = Integer.parseInt(strDesc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            
            // 1. Qtype汇总数据统计
            String qtypeStat = qtypeStat(nDays);
            content.append(qtypeStat);
            // 2. Qtype分类来源统计
            String qtypeClassificationStat = qtypeClassificationStat(nDays);
            content.append("<h1>3、Qtype分类来源统计</h1><br/>");
            content.append(qtypeClassificationStat);
            // emial tail
            content.append("<br/><br/>");
            content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

            if(imgUrlArrQtype.size()>0){
            	imgUrlArr.addAll(imgUrlArrQtype) ;
            }
            if(imgUrlArrQtypeSrctype.size()>0){
            	imgUrlArr.addAll(imgUrlArrQtypeSrctype) ;
            }
            String[] imgPaths = new String[imgUrlArr.size()];
            for(int i=0; i<imgUrlArr.size(); i++){
            	imgPaths[i] = MailConstants.MAIL_CHART_PATH + "/" +imgUrlArr.get(i) ;
            }
            
            javaSendMail.sendHtmlWithInnerImageEmail(subject, to, content.toString(), imgPaths);
            
            //清空图片路径list
            imgUrlArr.clear();
            imgUrlArrQtype.clear();
            imgUrlArrQtypeSrctype.clear();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
	/**
     * 构造邮件内容 1
     * @return
     */
    private String qtypeStat(Integer beforeDays) {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, beforeDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        
        //↓↓↓↓↓↓↓ Qtype不分类统计数据
        //构造表格，Qtype不分类统计--BEGIN
        List<Map<String, Object>> rowlistIgnoreqtype = serverLogQtypeStatService.querySumListByClientIgnoreqtype(startDate, endDate, "3", "4");
        
        // 图表数据List
        List<String> statdateIgnoreqtype = new ArrayList<String>() ;
        List<Integer> datePvIgnoreqtype = new ArrayList<Integer>() ;
        List<Float> datePerpvIgnoreqtype = new ArrayList<Float>() ;
    	
        sbContent.append("<h1>1、app汇总数据</h1><br/>");
        sbContent.append("<h1>1.1、app汇总-列表数据</h1>");
        
        sbContent.append("<table class=\"gridtable\">");
        sbContent.append("<tr><th>序号</th><th>日期</th><th>PV</th><th>人均PV</th></tr>");
        
        int tableIgnoreqtypei=0 ;
        
        for(Map<String, Object> map : rowlistIgnoreqtype){
        	tableIgnoreqtypei++ ;
        	
        	String statdate = (String) map.get("statdate").toString() ;
        	Integer uv =  ((BigDecimal)map.get("uv")).intValue();
        	Integer pv =  ((BigDecimal)map.get("pv")).intValue();
        	
        	//calculate the pv of perperson
        	float size = (float)pv/uv;
        	DecimalFormat df = new DecimalFormat("0.0");
        	float pvPer = Float.valueOf(df.format(size));
        	
        	if(!statdateIgnoreqtype.contains(statdate)){
        		statdateIgnoreqtype.add(statdate) ;
        	}
        	datePvIgnoreqtype.add(pv.intValue()) ;
        	datePerpvIgnoreqtype.add(pvPer) ;
        	
        	//get the week date of statdate, example: 2016-11-30>>2016-11-30(三)
            String statdateWithWeek = statdate + "(" + DateUtil.getWeekDay(statdate) + ")";
        	
        	sbContent.append("<tr><td>"+tableIgnoreqtypei+"</td><td>"+statdateWithWeek+"</td><td>"+pv+"</td><td>"+pvPer+"</td></tr>") ;
        }
        sbContent.append("</table></br>") ;
        //构造表格，Qtype不分类统计--END
        
        // ======= 生成图片 - start
        double[] appdataPvTemp = new double[datePvIgnoreqtype.size()];
        double[] appdataPerpvTemp = new double[datePerpvIgnoreqtype.size()];
        
        for(int tempi=0; tempi<datePvIgnoreqtype.size(); tempi++){
        	appdataPvTemp[tempi]=datePvIgnoreqtype.get(tempi);
        }
        for(int tempi=0; tempi<datePerpvIgnoreqtype.size(); tempi++){
        	appdataPerpvTemp[tempi]=datePerpvIgnoreqtype.get(tempi);
        }
       
        //图表数据点说明
        String[] appdatePvRowKeys = {"pv"};
        String[] appdatePerpvRowKeys = {"人均pv"};

        //日期数量（X）
        String[] appdateColumnKeysDate = new String[statdateIgnoreqtype.size()];
        
        //把构造图表的日期数据复制到趋势图中
        for(int m=0; m<statdateIgnoreqtype.size(); m++){
        	appdateColumnKeysDate[m] = statdateIgnoreqtype.get(m)+"("+DateUtil.getWeekDay(statdateIgnoreqtype.get(m))+")" ;
        }
        
        //图片名称
        String appdatePv = "app数据-pv";
        String appdatePerpv = "app数据-人均pv";

        //X轴名称
        String categoryLabel = "日期";

        //y轴名称
        String appdatePvYname = "PV";
        String appdatePerpvYname = "人均PV";

        //数据集
        double[][] appdataPv = new double[1][];
        double[][] appdataPerpv = new double[1][];

        appdataPv[0] = appdataPvTemp;
        appdataPerpv[0] = appdataPerpvTemp;
        
        JFreeChartUtil.buildLineChat(appdatePv, categoryLabel, appdatePvYname, appdatePvRowKeys, appdateColumnKeysDate, appdataPv,
        		MailConstants.MAIL_CHART_PATH + "/img-appdate-pv.jpg");
        JFreeChartUtil.buildLineChat(appdatePerpv, categoryLabel, appdatePerpvYname, appdatePerpvRowKeys, appdateColumnKeysDate, appdataPerpv,
        		MailConstants.MAIL_CHART_PATH + "/img-appdate-perpv.jpg");
        // ======= 生成图片 - end
        
        
        imgUrlArr.add("img-appdate-pv.jpg") ;
        imgUrlArr.add("img-appdate-perpv.jpg") ;

        //添加图片到邮件中
        sbContent.append("<h1>1.2、app汇总-趋势图</h1><br/>");
        sbContent.append("<table class=\"gridtable\">");

        sbContent.append("<tr>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"1\" align=\"center\">整体数据</td>" +
                "        <td style=\"background-color:#ff7f00\" colspan=\"2\" align=\"center\">app汇总-pv</td>" +
                "        <td style=\"background-color:#ff7f00\" colspan=\"2\" align=\"center\">app汇总-人均pv</td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"1\">趋势</td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-appdate-pv.jpg\"/></td>" +
                "        <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-appdate-perpv.jpg\"/></td>" +
                "    </tr>");

        sbContent.append("</table>");
        sbContent.append("<br/>");
        //构造图表=======================end
        //↑↑↑↑↑↑↑↑↑↑
        
        List<Map<String, Object>> rowlist = serverLogQtypeStatService.querySumListByClient(startDate, endDate, "3", "4");

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");

        for(String qtype:qtypeArr){
        	qtypeList.add(qtype);
        }
        
        // 构造 Table title
        sbContent.append("<h2>2、 Qtytpe汇总-列表数据</h2></br>");
        
        sbContent.append("<h2>2.1、 Qtytpe汇总-列表数据</h2>");
        sbContent.append("<tr><th>序号</th><th>日期</th>");
        for(int i=0; i<qtypeArr.length; i++){
        	sbContent.append("<th>"+qtypeArrCh[i]+"("+qtypeArr[i]+")"+"</th>") ;
        }
        sbContent.append("</tr>") ;
        
        //遍历rowlist 重新构造TABLE数据源用以改变TABLE显示方式
        List<String> statdateList = new ArrayList<String>() ;
        Map<String, Integer> mapUV = new HashMap<String, Integer>();
        for(Map<String, Object> map : rowlist){
        	String statdate = (String) map.get("statdate").toString() ;
        	String qtype = (String) map.get("qtype") ;
        	Integer uv =  ((BigDecimal)map.get("uv")).intValue();
        	if (!statdateList.contains(statdate)) {
        		statdateList.add(statdate);
            }
        	//构造key
        	String key = statdate + "_" + qtype;
        	//构造map: 
        	if(qtypeList.contains(qtype)){
        		mapUV.put(key, uv);
        	}
        }
        
        //构造TABLE BEGIN
        //Table序号
        int i = 0;
        //遍历时间list 以正确显示TABLE数据行数
        for(String stattime : statdateList){
        	i++;
        	sbContent.append("<tr>");
            sbContent.append("<td>" + i + "</td>");
            //get the week date of stattime, example: 2016-11-30>>2016-11-30(三)
            String stattimeWithWeek = stattime + "(" + DateUtil.getWeekDay(stattime) + ")";
            sbContent.append("<td>" + stattimeWithWeek + "</td>");
    		for(String qtypeTemp : qtypeArr){
    			Integer qtype_uv = (Integer) mapUV.get(stattime + "_" + qtypeTemp);
    			if(qtype_uv == null){
    				qtype_uv = 0 ;
    			}
    			sbContent.append("<td>" + qtype_uv + "</td>");
    		}
        	sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");
        //构造TABLE END
        
        // 生成图片 - begin
        //遍历所有Qtype
        for(String qtype : qtypeArrCh){
        	List<Map<String, Object>> qtypeListNew = new ArrayList<Map<String,Object>>() ;
        	
        	//图片名称en
        	String enName = null ;
        	for(int k=0; k<qtypeArrCh.length; k++){
        		if(qtype.equals(qtypeArrCh[k])){
        			enName = qtypeArr[k] ;
        		}
        	}
        	
        	//get值为对应Qtype的Map，放入list
        	for (Map<String, Object> map : rowlist) {
        		if(enName.equals(map.get("qtype"))){
        			qtypeListNew.add(map);
        		} 
        	}
        	
        	double[] arrayQtypeUv = new double[qtypeListNew.size()];
        	
        	String[] columnKeys = new String[qtypeListNew.size()];
        	
        	for (int j = 0; j < qtypeListNew.size(); j++) {
        		//获取日期 : 格式：2016-09-27(二)
        		String tempColumnKeys = String.valueOf(qtypeListNew.get(j).get("statdate")) ;
        		columnKeys[j] = tempColumnKeys + "(" + DateUtil.getWeekDay(tempColumnKeys) + ")";
        		arrayQtypeUv[j] = ((BigDecimal) qtypeListNew.get(j).get("uv")).intValue();
        	}
        	
        	//图片名称
        	String chartTitleCooks = qtype+" UV变化趋势图";
        	
        	//趋势折线图数据点说明
        	String[] rowKeysQtypeUv = {"UV数"};
        	
        	//X轴名称
        	String categoryLabelCooks = "日期";
        	
        	// y轴名称
        	String valueLabelCooks = "UV";
        	
        	//数据集
        	double[][] dataQtype = new double[1][];
        	dataQtype[0] = arrayQtypeUv;
        	
        	//图片完整名称
        	String imgName = "img-"+enName+"-uv-stat.jpg" ;
        	
        	//菜谱数折线图 
        	//图表名称  X轴Lable  Y轴Lable  Y列值  X轴列值  数据集  图片路径
        	JFreeChartUtil.buildLineChat(chartTitleCooks, categoryLabelCooks, valueLabelCooks, rowKeysQtypeUv, columnKeys, dataQtype,
        			MailConstants.MAIL_CHART_PATH + "/" +imgName);
        	
        	imgUrlArrQtype.add(imgName) ;
        	// 生成图片 - end
        }
        

        //添加图片到邮件中
        sbContent.append("<h2>2.2、 Qtype汇总-趋势图</h2><br/>");
        sbContent.append("<table class=\"gridtable\">");

        sbContent.append("<tr>" +
                "        <td style=\"background-color:#ff7f00\" rowspan=\"8\" align=\"center\">整体数据</td>" +
                "    </tr>"+
                "    <tr>" );
        
        for(int j=0; j<imgUrlArrQtype.size(); j++){
        	if(j % 2 != 0){//J为奇数
        		sbContent.append("<td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"src=\"cid:"+imgUrlArrQtype.get(j)+"\"/></td>" ) ;
        		sbContent.append("</tr>") ;
        	} else {
        		sbContent.append("<td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"src=\"cid:"+imgUrlArrQtype.get(j)+"\"/></td>" ) ;
        	}
        }
                
        sbContent.append("</table>");
        sbContent.append("<br/>");

        return sbContent.toString();
    }
    
    
    /**
     * 构造邮件内容 2
     * @return
     */
    private String qtypeClassificationStat(Integer beforeDays) {
    	
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, beforeDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        
        int i = 0 ; //二级标题序列号
        
        List<Map<String, Object>> rowlistSum = serverLogQtypeSrctypeStatService.querySumListByClientQtype(startDate, endDate, "3","4");
        
        for(String qtype : qtypeArr ){
        	
        	List<String> statdateList = new ArrayList<String>() ; //table日期
        	List<Map<String, Object>> rowlist = new ArrayList<Map<String,Object>>() ; // 存储单个Qtype的list集
        	Map<String, Integer> mapUV = new HashMap<String, Integer>() ; //TABLE数据map
        	List<String> srctypeList = new ArrayList<String>() ; // 存储Srctype的list
        	
        	i++ ;
        	
        	// 构造数据集
        	for(Map<String, Object> map : rowlistSum){
        		if(qtype.equals(map.get("qtype"))){
        			rowlist.add(map) ;
        		}
        	}
        	
        	//获取Qtype相对应的中文名称
        	String qtypeChName = "" ;
        	for(int k=0; k<qtypeArrCh.length; k++){
        		if(qtype.equals(qtypeArr[k])){
        			qtypeChName = qtypeArrCh[k] ;
        		}
        	}
        	
        	int tableOrder = 0 ; //table序号
        	
            for(Map<String, Object> map : rowlist){
            	String statdate = (String) map.get("statdate").toString() ;
            	String srctype = (String) map.get("srctype") ;
            	Integer uv =  ((BigDecimal)map.get("uv")).intValue();
            	if (!statdateList.contains(statdate)) {
            		statdateList.add(statdate) ;
                }
            	if (!srctypeList.contains(srctype)){
            		srctypeList.add(srctype) ;
            	}
            	//构造key
            	String key = statdate + qtype + srctype ;
            	//构造map: 
            	if(qtypeList.contains(qtype)){
            		mapUV.put(key, uv);
            	}
            }
        	
        	// 构造二级标题
        	sbContent.append("<h2>3."+i+"、"+qtypeChName+"来源统计</h2></br>") ;
        	
        	// 构造二级标题
        	sbContent.append("<h3>3."+i+".1、"+qtypeChName+"-汇总数据统计</h3><br>") ;
        	
        	// 构造表格
        	sbContent.append("<table class=\"gridtable\">");
        	
        	// 构造TABLEtitle
        	sbContent.append("<tr><th>序号</th><th>日期</th>");
            for(int k=0; k<srctypeList.size(); k++){
            	String mapSrctypeValue = "" ;
            	String mapSrctypeKey = srctypeList.get(k) ;
            	if("".equals(mapSrctypeKey) || mapSrctypeKey == null){
            		mapSrctypeKey = "unKnow" ;            	
            	}
            	for(Map<String,String> map:srctypeListConstants){
            		if(map.containsKey(mapSrctypeKey)){
            			mapSrctypeValue = map.get(mapSrctypeKey) ;
            		}
            	}
            	sbContent.append("<th>"+mapSrctypeValue+"("+mapSrctypeKey+")"+"</th>") ;
            }
            sbContent.append("</tr>") ;
        	
            // 构造数据
        	for(String stattime : statdateList){
        		tableOrder ++ ;
            	sbContent.append("<tr>");
                sbContent.append("<td>" + tableOrder + "</td>");
                
                //get the week day of stattime, like: 2016-10-16 >> 2016-10-16(二)
                String stattimeWithWeek = stattime + "(" + DateUtil.getWeekDay(stattime) + ")";
                
                sbContent.append("<td>" + stattimeWithWeek + "</td>");
        		for(String srctypeTemp : srctypeList){
        			Integer qtype_srctype_uv = (Integer) mapUV.get(stattime + qtype + srctypeTemp);
        			if(qtype_srctype_uv == null){
        				qtype_srctype_uv = 0 ;
        			}
        			sbContent.append("<td>" + qtype_srctype_uv + "</td>");
        		}
            	sbContent.append("</tr>");
            }
        	
        	sbContent.append("</table>");
        	sbContent.append("<br/>");
        	// 构造表格 END
        	
        	List<String> imgNameForTable = new ArrayList<String>() ;

        	// 构造趋势图BEGIN
        	for(String srctype : srctypeList){
        		
        		List<Map<String,Object>> specialSrctypeList = new ArrayList<Map<String,Object>>() ; //单个Srctype对应存储的数据集
        		
        		
        		for(Map<String,Object> map : rowlist){
        			if(srctype.equals(map.get("srctype"))){
        				specialSrctypeList.add(map) ;
        			}
        		}
        		
        		String[] columnSrctypeKeys = new String[specialSrctypeList.size()]; // X轴数据集
        		double[] arraySrctypeUv = new double[specialSrctypeList.size()]; //图表数据集

        		for (int j = 0; j < specialSrctypeList.size(); j++) {
        			arraySrctypeUv[j] = ((BigDecimal) specialSrctypeList.get(j).get("uv")).intValue();
        			//获取日期 : 格式：2016-09-27(二)
        			String tempColumnKeys = (specialSrctypeList.get(j).get("statdate")).toString() ;
        			columnSrctypeKeys[j] = tempColumnKeys + "(" + DateUtil.getWeekDay(tempColumnKeys) + ")";
        		}
        		
        		//图片名称
        		String mapSrctypeValue = "" ;
            	String mapSrctypeKey = srctype ;
            	if("".equals(srctype) || srctype==null){
            		mapSrctypeKey = "unKnow" ;            	
            	}
            	for(Map<String,String> map:srctypeListConstants){
            		if(map.containsKey(mapSrctypeKey)){
            			mapSrctypeValue = map.get(mapSrctypeKey) ;
            		}
            	}
        		String chartTitleSrctype = "来源："+mapSrctypeValue+"("+mapSrctypeKey+")"+" 变化趋势图";
        		
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
        		String imgName = "img-"+qtype+"_"+srctype+"-uv-stat.jpg" ;
        		
        		//菜谱数折线图 
        		//图表名称  X轴Lable  Y轴Lable  Y列值  X轴列值  数据集  图片路径
        		JFreeChartUtil.buildLineChat(chartTitleSrctype, categoryLabelSrctype, valueLabelSrctype, rowKeysSrctypeUv, columnSrctypeKeys, dataSrctype,
        				MailConstants.MAIL_CHART_PATH + "/" +imgName);
        		
        		imgUrlArrQtypeSrctype.add(imgName) ;
        		imgNameForTable.add(imgName) ;
        		
        		// 生成图片 - end
        	}
          //添加图片到邮件中
          sbContent.append("<h3>3."+i+".2、"+qtypeChName+"-趋势图</h3></br>") ;
          sbContent.append("<table class=\"gridtable\">");
          sbContent.append("<tr>" +
                  "        <td style=\"background-color:#ff7f00\" rowspan=\"100\" align=\"center\">整体数据</td>" +
                  "    </tr>"+
                  "    <tr>" );
          
          for(int j=0; j<imgNameForTable.size(); j++){
          	if(j % 2 != 0){//J为奇数
          		sbContent.append("<td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"src=\"cid:"+imgNameForTable.get(j)+"\"/></td>" ) ;
          		sbContent.append("</tr>") ;
          	} else {
          		sbContent.append("<td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"src=\"cid:"+imgNameForTable.get(j)+"\"/></td>" ) ;
          	}
          }
          sbContent.append("</table>");
          sbContent.append("<br/>");
          
        }

        return sbContent.toString();
    }
    
    
    public static List<Map<String,String>> getSrctype() {
	   
	   File file = null ;
		try {
			file = ResourceUtils.getFile("classpath:srctype.txt");
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}  
	   System.out.println(file);
	   
	   List<Map<String,String>> rowlst = new ArrayList<Map<String,String>>() ;
	   
   		//文件读取-begin
   		BufferedReader reader = null;
   		try {
   			reader = new BufferedReader(new FileReader(file));
   			String line = null;
   			int n = 0;
   			// 一次读入一行，直到读入null为文件结束
   			while ((line = reader.readLine()) != null) {
   				String[] arryLine = line.split("=");
   				Map<String, String> map = new HashMap<String, String>();
   				map.put(arryLine[0], arryLine[1]);
   				rowlst.add(map);
   			}
   		} catch (IOException e) {
   			e.printStackTrace();
   		} finally {
   			if (reader != null) {
   				try {
   					reader.close();
   				} catch (IOException e1) {
   				}
   			}
   		}
   		//文件读取-END
   		
		return rowlst;
    	
    }
    
    

}