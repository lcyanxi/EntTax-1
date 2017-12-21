package com.douguo.dc.mail.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mail.service.SysMailSetService;
import com.douguo.dc.mail.service.impl.tmp.CommonMailQtypeStatService;
import com.douguo.dc.mall.common.MallConstants;
import com.douguo.dc.mall.service.AppTuanService;
import com.douguo.dc.mall.service.MallRepeatBuyService;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JFreeChartUtil;
import com.douguo.dc.util.JavaSendMail;
import com.douguo.dc.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.recycler.Recycler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("mailMallStatService")
public class MailMallStatService {

    @Autowired
    private AppTuanService appTuanService;

    @Autowired
    private MallRepeatBuyService mallRepeatBuyService;
    @Autowired
    private CommonMailQtypeStatService commonMailQtypeStatService;

    @Autowired
    private SysMailSetService sysMailSetService;

    public void sendOrderSumMail(SysMailSet sysMailSet) {
        JavaSendMail javaSendMail = new JavaSendMail();
        try {
//			SysMailSet sysMailSet = sysMailSetService.getSysMailSetByMailType(MailConstants.MAIL_TYPE_DG_MALL_SUM);

            String strConfig = StringUtils.trimToEmpty(sysMailSet.getConfig());
            JSONObject jsonConfig = JsonUtil.parseStrToJsonObj(strConfig);
            String curDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
            String subject = "";
            String to = "";
            StringBuffer content = new StringBuffer("");
            content.append("<style type=\"text/css\"> table.gridtable { font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse; } table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #dedede; } table.gridtable td { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff; } </style>");

            if (sysMailSet != null) {
                subject = sysMailSet.getSubject() + "_" + curDate;
                to = sysMailSet.getMailTo();
            } else {
                subject = "豆果严选销售日报_" + curDate;
                to = "wyx@douguo.com,zhangyaozhou@douguo.com";
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

            // 1. 订单汇总数据
            String orderSum = orderSum(nDays);
            content.append("<h1>1、订单汇总</h1><br/>");
            content.append(orderSum);
            content.append("<br/><br/>");

            // 2. 成交量前20商品详情
            String top20Product = top20Product();
            content.append("<h1>2、商品TOP20数据</h1><br/>");
            content.append(top20Product);
            content.append("<br/><br/>");

            // 3. 订单支付渠道信息
            String orderPayType = orderPayType();
            content.append("<h1>3、订单支付渠道数据</h1><br/>");
            content.append(orderPayType);
            content.append("<br/><br/>");

            // 4. 订单支付渠道信息
            String source = orderSource();
            content.append("<h1>4、订单来源渠道数据</h1><br/>");
            content.append(source);
            content.append("<br/><br/>");

            // 5. 订单重复购买率
            String mall30dayRepeatBuy = mall30dayRepeatBuy();
            content.append("<h1>5、订单重复购买率</h1><br/>");
            content.append(mall30dayRepeatBuy);
            content.append("<br/><br/>");

            // 6. 订单下单时间分布
            String orderTime = orderTimeDistribute();
            content.append("<h1>6、订单下单&支付时间分布</h1><br/>");
            content.append(orderTime);
            content.append("<br/>");
            if (org.apache.commons.lang3.StringUtils.isNotBlank(jsonConfig.getString("qtypeStr")) && org.apache.commons.lang3.StringUtils.isNotBlank(jsonConfig.getString("qtypeCh"))) {
                Map targetMap = commonMailQtypeStatService.qtypeClassificationStatOderByNum(jsonConfig);
                content.append(targetMap.get("sbContent"));
                List imgUrlArr = (List) targetMap.get("imgUrlArr");
                imgUrlArr.add("img-0.jpg");
                imgUrlArr.add("img-pv.jpg");
                imgUrlArr.add("img-uv.jpg");
                imgUrlArr.add("img-pays.jpg");
                imgUrlArr.add("img-Moneys.jpg");
                String[] imgPaths = new String[imgUrlArr.size()];
                for (int i = 0; i < imgUrlArr.size(); i++) {
                    imgPaths[i] = MailConstants.MAIL_CHART_PATH + "/" + imgUrlArr.get(i);
                }
                javaSendMail.sendHtmlWithInnerImageEmail(subject, to, content.toString(), imgPaths);
            } else {
                JavaSendMail.sendHtmlEmail(subject, to, content.toString());
            }
            content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 订单来源统计
     *
     * @return
     */
    private String orderSource() {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String order = "moneys";
        //
        List<Map<String, Object>> rowlst = appTuanService.queryOrderSourceList(startDate, endDate, order);

        // 构造表格

        sbContent.append("<table class=\"gridtable\">");
        sbContent.append("<tr><th>订单来源</th><th>成交单数</th><th>成交金额(元)</th></tr>");
        for (Map<String, Object> map : rowlst) {
            Integer nSource = (Integer) map.get("source");
            String source = getSource(nSource);
            BigDecimal dPays = (BigDecimal) map.get("pays");
            BigDecimal dMoneys = (BigDecimal) map.get("moneys");
            //
            try {
                dMoneys = dMoneys.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
            } catch (Exception e) {
                e.printStackTrace();
                dMoneys = new BigDecimal(0);
            }
            sbContent.append("<tr>");
            sbContent.append("<td>" + source + "</td>");
            sbContent.append("<td>" + dPays + "</td>");
            sbContent.append("<td>" + dMoneys + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        return sbContent.toString();
    }

    private String orderPayType() {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String order = "moneys";
        List<Map<String, Object>> rowlst = appTuanService.queryOrderPayTypeList(startDate, endDate, order);

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");
        sbContent.append("<tr><th>支付渠道</th><th>成交单数</th><th>成交金额(元)</th></tr>");
        for (Map<String, Object> map : rowlst) {
            Integer nPayType = (Integer) map.get("pay_type");
            String payType = getPayType(nPayType);
            BigDecimal dPays = (BigDecimal) map.get("pays");
            BigDecimal dMoneys = (BigDecimal) map.get("moneys");
            try {
                dMoneys = dMoneys.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
            } catch (Exception e) {
                e.printStackTrace();
                dMoneys = new BigDecimal(0);
            }
            sbContent.append("<tr>");
            sbContent.append("<td>" + payType + "</td>");
            sbContent.append("<td>" + dPays + "</td>");
            sbContent.append("<td>" + dMoneys + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        return sbContent.toString();
    }

    private String getPayType(Integer payType) {
        if (payType == 1) {
            return "支付宝";
        } else if (payType == 2) {
            return "微信";
        } else if (payType == 3) {
            return "银联";
        } else if (payType == 4) {
            return "微信-M站";
        } else {
            return "未知";
        }
    }

    private String getSource(Integer source) {
        if (source == 0) {
            return "网站";
        } else if (source == 815) {
            return "WAP";
        } else if (source == 3) {
            return "IOS客户端";
        } else if (source == 4) {
            return "Android客户端";
        } else {
            return String.valueOf(source);
        }
    }

    private String mall30dayRepeatBuy() {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        //
        List<Map<String, Object>> rowlst = mallRepeatBuyService.queryListByStatType(startDate, endDate,
                MallConstants.MALL_REPEAT_BUY_COMMON);

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");
        sbContent.append("<tr><th>复购率类别</th><th>人数</th><th>复购率</th></tr>");

        // 总人数
        BigDecimal valueAll = new BigDecimal(0);
        for (Map<String, Object> map : rowlst) {
            String rType = (String) map.get("repeat_type");
            if ("0".equals(rType)) {
                valueAll = (BigDecimal) map.get("repeat_value");
            }
        }

        for (Map<String, Object> map : rowlst) {

            String rType = (String) map.get("repeat_type");
            if (rType.equals("0")) {
                continue;
            }
            BigDecimal repeatValue = (BigDecimal) map.get("repeat_value");
            BigDecimal vRate = new BigDecimal(0);

            if (valueAll.intValue() != 0) {
                BigDecimal newRepeatValue = repeatValue.multiply(new BigDecimal(100));
                vRate = newRepeatValue.divide(valueAll, BigDecimal.ROUND_HALF_UP);
            }
            //

            sbContent.append("<tr>");
            sbContent.append("<td>" + rType + "天</td>");
            sbContent.append("<td>" + repeatValue + "</td>");
            sbContent.append("<td>" + vRate + "%</td>");
            sbContent.append("</tr>");
        }
        // 总计
        sbContent.append("<tr>");
        sbContent.append("<td>总计</td>");
        sbContent.append("<td>" + valueAll + "</td>");
        sbContent.append("<td>100%</td>");
        sbContent.append("</tr>");

        sbContent.append("</table>");
        return sbContent.toString();
    }

    /**
     * 订单汇总
     *
     * @return
     */
    private String orderSum(Integer beforeDays) {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, beforeDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String order = "";
        //
        List<Map<String, Object>> rowlst = appTuanService.queryOrderSumList(startDate, endDate, order);
        // 构造表格

        sbContent.append("<table class=\"gridtable\">");
        sbContent
                .append("<tr><th>统计日期</th><th>列表pv</th><th>列表uv</th><th>严选tab_uv</th>" +
                        "<th>商品浏览pv</th><th>商品浏览uv</th><th>订单数</th><th>当日新用户数</th><th>成交单数</th><th>成交人数</th><th>成交转化率</th><th>成交率</th><th>成交金额(元)</th><th>客单价(元)</th><th>补贴金额(元)</th><th>优惠券(元)</th><th>非补贴-成交单</th><th>非补贴-SKU</th><th>非补贴-成交金额(元)</th></tr>");
        for (Map<String, Object> map : rowlst) {
            Date statDate = (Date) map.get("statdate");
            String strWeekStatDate = DateUtil.getWeekDay(statDate);
            Long listPv = (Long) map.get("list_pv");
            Integer listUv = (Integer) map.get("list_uv");
            Integer taun_uv = (Integer) map.get("taun_uv");
            Long detailPv = (Long) map.get("detail_pv");
            Integer detailUv = (Integer) map.get("detail_uv");
            Integer orders = (Integer) map.get("orders");
            Integer pays = (Integer) map.get("pays");
            BigDecimal dMoneys = (BigDecimal) map.get("moneys");
            Integer orderUV = (Integer) map.get("order_uv");
            BigDecimal dIncome = (BigDecimal) map.get("income");
            BigDecimal dConpons = (BigDecimal) map.get("conpons");
            BigDecimal dSubsidys = (BigDecimal) map.get("subsidys");
            Integer nComPays = (Integer) map.get("com_pays");
            Integer nComSku = (Integer) map.get("com_sku");
            Integer newUserNum = (Integer) map.get("new_user_num");
            BigDecimal dComMoneys = (BigDecimal) map.get("com_moneys");
            BigDecimal payRate = new BigDecimal(0);
            BigDecimal conversionRate = new BigDecimal(0);// 成交单转化率
            BigDecimal dCustomerPrice = new BigDecimal(0);//客单价

            if (0 != pays) {
                BigDecimal bdPays = new BigDecimal(pays * 100);
                BigDecimal bdOrders = new BigDecimal(orders);
                payRate = bdPays.divide(bdOrders, BigDecimal.ROUND_HALF_UP);
            }
            //

            if (0 != detailUv.intValue()) {
                BigDecimal bdPays = new BigDecimal(pays * 100);
                //
                BigDecimal bdDetailUv = new BigDecimal(detailUv);
                conversionRate = bdPays.divide(bdDetailUv, 2, BigDecimal.ROUND_HALF_UP);
            }

            //
            try {
                dMoneys = dMoneys.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
            } catch (Exception e) {
                e.printStackTrace();
                dMoneys = new BigDecimal(0);
            }
            //
            try {
                dConpons = dConpons.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
            } catch (Exception e) {
                e.printStackTrace();
                dConpons = new BigDecimal(0);
            }
            //
            try {
                dSubsidys = dSubsidys.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
            } catch (Exception e) {
                e.printStackTrace();
                dSubsidys = new BigDecimal(0);
            }
            //
            //
            if (0 != orderUV) {
                BigDecimal bdOrderUV = new BigDecimal(orderUV);
                //
                dCustomerPrice = dMoneys.divide(bdOrderUV, 2, BigDecimal.ROUND_HALF_UP);
            }

            //
            try {
                dComMoneys = dComMoneys.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
            } catch (Exception e) {
                e.printStackTrace();
                dComMoneys = new BigDecimal(0);
            }

            sbContent.append("<tr>");
            sbContent.append("<td>" + statDate + "(" + strWeekStatDate + ")</td>");
            sbContent.append("<td>" + listPv + "</td>");
            sbContent.append("<td>" + listUv + "</td>");
            sbContent.append("<td>" + taun_uv + "</td>");
            sbContent.append("<td>" + detailPv + "</td>");
            sbContent.append("<td>" + detailUv + "</td>");
            sbContent.append("<td>" + orders + "</td>");
            sbContent.append("<td>" + newUserNum + "</td>");
            sbContent.append("<td>" + pays + "</td>");
            sbContent.append("<td>" + orderUV + "</td>");
            sbContent.append("<td>" + conversionRate + "%</td>");
            sbContent.append("<td>" + payRate + "%</td>");
            sbContent.append("<td>" + dMoneys + "</td>");
            sbContent.append("<td>" + dCustomerPrice + "</td>");
            sbContent.append("<td>" + dSubsidys + "</td>");
            sbContent.append("<td>" + dConpons + "</td>");
            sbContent.append("<td>" + nComPays + "</td>");
            sbContent.append("<td>" + nComSku + "</td>");
            sbContent.append("<td>" + dComMoneys + "</td>");
            // sbContent.append("<td>" + dIncome + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");

        /*
        //构造趋势图表======Start
        //Data for X-axis
        List<String> xAxisDateList = new ArrayList<String>() ;
        for (Map<String, Object> map : rowlst) {
            Date statDate = (Date) map.get("statdate");
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
            String str=sdf.format(statDate);
            xAxisDateList.add(str) ;

        }
        String[] xAxisDate = new String[xAxisDateList.size()] ;
        for(int i=0; i<xAxisDateList.size(); i++){

        	xAxisDate[i] = xAxisDateList.get(i) ;
        }
        
        //Date for Y-axis-优食汇列表PV
		List<Long> yAxisDateListPv = new ArrayList<Long> () ;
        for (Map<String, Object> map : rowlst) {
        	Long listPv = (Long) map.get("list_pv");
            yAxisDateListPv.add(listPv) ;
        }
        double[] yAxisDatePvTemp = new double[yAxisDateListPv.size()] ;
        double[][] yAxisDatePv = new double[1][] ;
        for(int i=0; i<yAxisDateListPv.size(); i++){
        	yAxisDatePvTemp[i] = yAxisDateListPv.get(i) ;
        }
        yAxisDatePv[0] = yAxisDatePvTemp ;
        
        //Date for Y-axis-优食汇列表UV
        List<Integer> yAxisDateListUv = new ArrayList<Integer> () ;
        for (Map<String, Object> map : rowlst) {
        	Integer listUv = (Integer) map.get("list_uv");
            yAxisDateListUv.add(listUv) ;
        }
        double[] yAxisDateUvTemp = new double[yAxisDateListUv.size()] ;
        double[][] yAxisDateUv = new double[1][] ;
        for(int i=0; i<yAxisDateListUv.size(); i++){
        	yAxisDateUvTemp[i] = yAxisDateListUv.get(i) ;
        }
        yAxisDateUv[0] = yAxisDateUvTemp ;
        
        //Date for Y-axis-成交单数
        List<Integer> yAxisDateListPays = new ArrayList<Integer> () ;
        for (Map<String, Object> map : rowlst) {
        	Integer pays = (Integer) map.get("pays");
        	yAxisDateListPays.add(pays) ;
        }
        double[] yAxisDatePaysTemp = new double[yAxisDateListPays.size()] ;
        double[][] yAxisDatePays = new double[1][] ;
        for(int i=0; i<yAxisDateListPays.size(); i++){
        	yAxisDatePaysTemp[i] = yAxisDateListPays.get(i) ;
        }
        yAxisDatePays[0] = yAxisDatePaysTemp ;
        
        //Date for Y-axis-成交金额
        List<BigDecimal> yAxisDateListMoneys = new ArrayList<BigDecimal> () ;
        for (Map<String, Object> map : rowlst) {
        	BigDecimal moneys = (BigDecimal) map.get("moneys");
        	yAxisDateListMoneys.add(moneys) ;
        }
        double[] yAxisDateMoneysTemp = new double[yAxisDateListMoneys.size()] ;
        double[][] yAxisDateMoneys = new double[1][] ;
        for(int i=0; i<yAxisDateListMoneys.size(); i++){
        	DecimalFormat df=new DecimalFormat("0.00");
            Double dbTemp = new Double(df.format(yAxisDateListMoneys.get(i)).toString());
        	yAxisDateMoneysTemp[i] = dbTemp ;
        }
        yAxisDateMoneys[0] = yAxisDateMoneysTemp ;
        
        //图表数据点说明
        String[] rowKeysPv = {"列表PV"};
        String[] rowKeysUv = {"列表UV"};
        String[] rowKeysPays = {"成交单数"};
        String[] rowKeysMoneys = {"成交金额"};
        
        //图片名称
        String chartTitlePv = "列表PV变化趋势";
        String chartTitleUv = "列表UV变化趋势";
        String chartTitlePays = "成交单数变化趋势";
        String chartTitleMoneys = "成交金额变化趋势";

        //X轴名称
        String nameXaxis = "日期";

        //y轴名称
        String nameYaxisPv = "列表PV数量";
        String nameYaxisUv = "列表UV数量";
        String nameYaxisPays = "成交单数";
        String nameYaxisMoneys = "成交金额";
        
        //生成趋势图片 
        //参数顺序：图片名称 X轴名称 Y轴名称 图表数据点说明 X轴数据 Y轴数据
        JFreeChartUtil.buildLineChat(chartTitlePv, nameXaxis, nameYaxisPv, rowKeysPv, xAxisDate, yAxisDatePv,
        		MailConstants.MAIL_CHART_PATH +"/img-pv.jpg");
        JFreeChartUtil.buildLineChat(chartTitleUv, nameXaxis, nameYaxisUv, rowKeysUv, xAxisDate, yAxisDateUv,
        		MailConstants.MAIL_CHART_PATH +"/img-uv.jpg");
        JFreeChartUtil.buildLineChat(chartTitlePays, nameXaxis, nameYaxisPays, rowKeysPays, xAxisDate, yAxisDatePays,
        		MailConstants.MAIL_CHART_PATH +"/img-pays.jpg");	
        JFreeChartUtil.buildLineChat(chartTitleMoneys, nameXaxis, nameYaxisMoneys, rowKeysMoneys, xAxisDate, yAxisDateMoneys,
        		MailConstants.MAIL_CHART_PATH +"/img-Moneys.jpg");
        //构造图表=========END
        */

        //构造趋势图表======Start
        //Data for X-axis

        //refactor code
        int nSize = rowlst.size();
        String[] xAxisDate = new String[nSize];
        double[] yAxisDatePvTemp = new double[nSize];
        double[][] yAxisDatePv = new double[1][];
        double[] yAxisDateUvTemp = new double[nSize];
        double[][] yAxisDateUv = new double[1][];
        double[] yAxisDatePaysTemp = new double[nSize];
        double[][] yAxisDatePays = new double[1][];
        double[] yAxisDateMoneysTemp = new double[nSize];
        double[][] yAxisDateMoneys = new double[1][];

        for (int i = 0; i < rowlst.size(); i++) {
            Map<String, Object> map = rowlst.get(i);

            //日期
            Date statDate = (Date) map.get("statdate");
            String strDate = DateUtil.date2Str(statDate);
            xAxisDate[i] = strDate + "(" + DateUtil.getWeekDay(statDate) + ")";

            //
            //Date for Y-axis-列表PV
            Long listPv = (Long) map.get("list_pv");
            yAxisDatePvTemp[i] = listPv;

            //Date for Y-axis-列表UV
            Integer listUv = (Integer) map.get("list_uv");
            yAxisDateUvTemp[i] = listUv;

            //Date for Y-axis-成交单数
            Integer pays = (Integer) map.get("pays");
            yAxisDatePaysTemp[i] = pays;

            //Date for Y-axis-成交金额
            BigDecimal moneys = (BigDecimal) map.get("moneys");
            yAxisDateMoneysTemp[i] = moneys.doubleValue()/100;
        }

        yAxisDatePv[0] = yAxisDatePvTemp;
        yAxisDateUv[0] = yAxisDateUvTemp;
        yAxisDatePays[0] = yAxisDatePaysTemp;
        yAxisDateMoneys[0] = yAxisDateMoneysTemp;

        //图表数据点说明
        String[] rowKeysPv = {"列表PV"};
        String[] rowKeysUv = {"列表UV"};
        String[] rowKeysPays = {"成交单数"};
        String[] rowKeysMoneys = {"成交金额"};

        //图片名称
        String chartTitlePv = "列表PV变化趋势";
        String chartTitleUv = "列表UV变化趋势";
        String chartTitlePays = "成交单数变化趋势";
        String chartTitleMoneys = "成交金额变化趋势";

        //X轴名称
        String nameXaxis = "日期";

        //y轴名称
        String nameYaxisPv = "列表PV数量";
        String nameYaxisUv = "列表UV数量";
        String nameYaxisPays = "成交单数";
        String nameYaxisMoneys = "成交金额";

        //生成趋势图片
        //参数顺序：图片名称 X轴名称 Y轴名称 图表数据点说明 X轴数据 Y轴数据
        JFreeChartUtil.buildLineChat(chartTitlePv, nameXaxis, nameYaxisPv, rowKeysPv, xAxisDate, yAxisDatePv,
                MailConstants.MAIL_CHART_PATH + "/img-pv.jpg");
        JFreeChartUtil.buildLineChat(chartTitleUv, nameXaxis, nameYaxisUv, rowKeysUv, xAxisDate, yAxisDateUv,
                MailConstants.MAIL_CHART_PATH + "/img-uv.jpg");
        JFreeChartUtil.buildLineChat(chartTitlePays, nameXaxis, nameYaxisPays, rowKeysPays, xAxisDate, yAxisDatePays,
                MailConstants.MAIL_CHART_PATH + "/img-pays.jpg");
        JFreeChartUtil.buildLineChat(chartTitleMoneys, nameXaxis, nameYaxisMoneys, rowKeysMoneys, xAxisDate, yAxisDateMoneys,
                MailConstants.MAIL_CHART_PATH + "/img-Moneys.jpg");
        //构造图表=========END

        //将图片添加至邮件中
        sbContent.append("<br/>");
        sbContent.append("<h1>趋势统计</h1><br/>");
        sbContent.append("<table class=\"gridtable\">");
        sbContent.append("<tr>" +
                "           <td style=\"background-color:#ff7f00\" colspan=\"2\" align=\"center\">列表PV数量变化趋势图</td>" +
                "           <td style=\"background-color:#ff7f00\" colspan=\"2\" align=\"center\">列表UV数量变化趋势图</td>" +
                "         </tr>" +
                "         <tr>" +
                "           <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-pv.jpg\"/></td>" +
                "           <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-uv.jpg\"/></td>" +
                "         </tr>" +
                "         <tr>" +
                "           <td style=\"background-color:#ff7f00\" colspan=\"2\" align=\"center\">成交单数变化趋势图</td>" +
                "           <td style=\"background-color:#ff7f00\" colspan=\"2\" align=\"center\">成交金额变化趋势图</td>" +
                "         </tr>" +
                "         <tr>" +
                "           <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-pays.jpg\"/></td>" +
                "           <td colspan=\"2\"><img width=\"550\" height=\"390\" style=\"margin:10px; padding:10px;\"" +
                "                             src=\"cid:img-Moneys.jpg\"/></td>" +
                "         </tr>");

        sbContent.append("</table>");
        sbContent.append("<br/>");

        return sbContent.toString();
    }


    private String orderTimeDistribute() {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String order = "hour";
        //
        List<Map<String, Object>> rowlst = appTuanService.queryOrderTimeDistributeList(startDate, endDate, order);

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");
        sbContent.append("<tr><th>小时</th><th>订单数</th><th>成交单数</th><th>成交金额(元)</th></tr>");
        double[] arrayOrders = new double[24];
        double[] arrayPays = new double[24];
        double[] arrayMoneys = new double[24];
        for (int i = 0; i < 24; i++) {
            boolean isOk = false;
            for (Map<String, Object> map : rowlst) {
                Integer hour = (Integer) map.get("hour");
                Integer orders = (Integer) map.get("orders");
                Integer pays = (Integer) map.get("pays");
                BigDecimal dMoneys = (BigDecimal) map.get("moneys");
                //
                try {
                    dMoneys = dMoneys.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
                } catch (Exception e) {
                    e.printStackTrace();
                    dMoneys = new BigDecimal(0);
                }
                if (i == hour) {
                    //
                    arrayOrders[i] = orders.doubleValue();
                    arrayPays[i] = pays.doubleValue();
                    arrayMoneys[i] = dMoneys.doubleValue();
                    //
                    sbContent.append("<tr>");
                    sbContent.append("<td>" + hour + "</td>");
                    sbContent.append("<td>" + orders + "</td>");
                    sbContent.append("<td>" + pays + "</td>");
                    sbContent.append("<td>" + dMoneys + "</td>");
                    sbContent.append("</tr>");
                    isOk = true;
                    break;
                }
            }
            if (!isOk) {
                //
                arrayOrders[i] = 0;
                arrayPays[i] = 0;
                arrayMoneys[i] = 0;
                //

                sbContent.append("<tr>");
                sbContent.append("<td>" + i + "</td>");
                sbContent.append("<td>" + 0 + "</td>");
                sbContent.append("<td>" + 0 + "</td>");
                sbContent.append("<td>" + new BigDecimal(0) + "</td>");
                sbContent.append("</tr>");
            }
        }

        // 生成图片 - begin
        // double[][] data = new double[][] { { 1230, 1110, 1120, 2332 }, { 720,
        // 750, 860, 23 }, { 830, 780, 700, 23 } };
        double[][] data = new double[2][];
        data[0] = arrayOrders;
        data[1] = arrayPays;
        // data[2]=arrayMoneys;
        //
        // String[] rowKeys = { "下单数", "成交单数", "交易额" };
        String[] rowKeys = {"下单数", "成交单数"};
        String[] columnKeys = new String[24];
        for (int i = 0; i < 24; i++) {
            columnKeys[i] = String.valueOf(i);
        }
        String chartTitle = "电商小时销量分布图";
        String categoryLabel = "时间";
        String valueLabel = "销量";
        JFreeChartUtil.buildBarChat(chartTitle, categoryLabel, valueLabel, rowKeys, columnKeys, data,
                MailConstants.MAIL_CHART_PATH + "/img-0.jpg");
        // 生成图片 - end

        sbContent.append("</table>");
        return sbContent.toString();
    }

    private String top20Product() {
        StringBuffer sbContent = new StringBuffer();
        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String order = "pays";
        //
        List<Map<String, Object>> rowlst = appTuanService.queryProductList(startDate, endDate, order);
        System.out.println("rowlst:" + rowlst.size());
        int i = 0;

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");
        sbContent
                .append("<tr><th>商品id</th><th>商品名称</th><th>浏览数</th><th>UV数</th><th>成交单数</th><th>商品数</th><th>商品单价</th><th>成交转化率</th><th>总金额(元)</th></tr>");
        for (Map<String, Object> map : rowlst) {
            i++;
            // 只取前20条数据
            if (i > 20) {
                break;
            }

            Long tuanId = (Long) map.get("tuan_id");
            String tuanName = (String) map.get("tuan_name");
            BigDecimal clicks = (BigDecimal) map.get("clicks");
            BigDecimal uids = (BigDecimal) map.get("uids");
            BigDecimal orders = (BigDecimal) map.get("orders");
            BigDecimal pays = (BigDecimal) map.get("pays");
            BigDecimal goods = (BigDecimal) map.get("goods");
            BigDecimal moneys = (BigDecimal) map.get("moneys");
            BigDecimal orderRate = new BigDecimal(0);
            BigDecimal payConversionRate = new BigDecimal(0);//订单成交转化率
            BigDecimal payRate = new BigDecimal(0);//订单成交率
            BigDecimal productPrice = new BigDecimal(0);//商品成交均价
            BigDecimal productConversionRate = new BigDecimal(0);//商品成交转化率

            //
            if (0 != uids.intValue()) {
                BigDecimal bdOrders = orders.multiply(new BigDecimal(100));
                orderRate = bdOrders.divide(uids, 2, BigDecimal.ROUND_HALF_UP);
            }
            //
            if (0 != uids.intValue()) {
                BigDecimal bdPays = pays.multiply(new BigDecimal(100));
                payConversionRate = bdPays.divide(uids, 2, BigDecimal.ROUND_HALF_UP);
            }
            //
            if (0 != orders.intValue()) {
                BigDecimal bdPays = pays.multiply(new BigDecimal(100));
                payRate = bdPays.divide(orders, BigDecimal.ROUND_HALF_UP);
            }
            //
            if (0 != uids.intValue()) {
                BigDecimal bdGoods = pays.multiply(new BigDecimal(100));
                productConversionRate = bdGoods.divide(uids, 2, BigDecimal.ROUND_HALF_UP);
            }
            //
//			map.put("orderRate", orderRate);
//			map.put("payRate", payRate);
            //

            try {
                moneys = moneys.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
            } catch (Exception e) {
                e.printStackTrace();
                moneys = new BigDecimal(0);
            }

            if (0 != goods.intValue()) {
                //
                productPrice = moneys.divide(goods, 2, BigDecimal.ROUND_HALF_UP);
            }

            //
            sbContent.append("<tr>");
            sbContent.append("<td>" + tuanId + "</td>");
            sbContent.append("<td>" + tuanName + "</td>");
            sbContent.append("<td>" + clicks + "</td>");
            sbContent.append("<td>" + uids + "</td>");
            sbContent.append("<td>" + pays + "</td>");
            sbContent.append("<td>" + goods + "</td>");
            sbContent.append("<td>" + productPrice + "</td>");
            sbContent.append("<td>" + productConversionRate + "%</td>");
            sbContent.append("<td>" + moneys + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");

        return sbContent.toString();
    }
}