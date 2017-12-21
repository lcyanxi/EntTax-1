package com.douguo.dc.mail.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.dao.ClickStatDao;
import com.douguo.dc.mail.model.ClickRate;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import static com.douguo.dc.util.DataFillZeroUtil.converList;
import static com.douguo.dc.util.DataFillZeroUtil.getNextDay;


/**
 * Created by lichang on 2017/10/24.
 */
@Repository("mailClickStatService")
public class MailClickStatService {

    @Autowired
    private ClickStatDao clickStatDao;

    /**
     * 点击数据汇总邮件
     *
     * @param sysMailSet
     */
    public void sendClickStatMail(SysMailSet sysMailSet) {
        JavaSendMail javaSendMail = new JavaSendMail();
        System.out.println(sysMailSet);

        String currDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String subject;
        String toAddress = "";
        StringBuffer content = new StringBuffer("");
        content.append("<style type=\"text/css\"> table.gridtable { font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse; } table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #dedede; } table.gridtable td { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff; } </style>");
        System.out.println(toAddress);
        if (sysMailSet != null) {
            subject = sysMailSet.getSubject() + "_" + currDate;
            toAddress = sysMailSet.getMailTo();
            System.out.println(toAddress);
        } else {
            subject = "首页类型报表统计_" + currDate;
            toAddress = "lichang@douguo.com";
        }


        //获取需要发送的顺序及编号
        String strConfig = StringUtils.trimToEmpty(sysMailSet.getConfig());
        JSONObject jsonConfig = JsonUtil.parseStrToJsonObj(strConfig);
        String config = jsonConfig.getString("type");
        String strDesc = jsonConfig.getString("trace_days");


        Integer nDays = 7;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(strDesc)) {
            try {
                nDays = Integer.parseInt(strDesc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //1. 家庭汇总数据统计
        content.append("<h1>首页推荐数据点击率报表</h1><br/>");
        content.append(queryClickStat(nDays));
        content.append("<br/><br/>");
        content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");


        String[] imgPaths = new String[8];
        imgPaths[0] = MailConstants.MAIL_CHART_PATH + "/img-click-kt-total.jpg";
        imgPaths[1] = MailConstants.MAIL_CHART_PATH + "/img-click-wz-total.jpg";
        imgPaths[2] = MailConstants.MAIL_CHART_PATH + "/img-click-cd-total.jpg";
        imgPaths[3] = MailConstants.MAIL_CHART_PATH + "/img-click-cp-total.jpg";
        imgPaths[4] = MailConstants.MAIL_CHART_PATH + "/img-click-tz-total.jpg";
        imgPaths[5] = MailConstants.MAIL_CHART_PATH + "/img-click-wd-total.jpg";
        imgPaths[6] = MailConstants.MAIL_CHART_PATH + "/img-click-sp-total.jpg";
        imgPaths[7] = MailConstants.MAIL_CHART_PATH + "/img-click-total-total.jpg";
        try {
            //发送邮件
            javaSendMail.sendHtmlWithInnerImageEmail(subject, toAddress, content.toString(), imgPaths);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String queryClickStat(int day) {
        StringBuffer sbContent = new StringBuffer();

        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, day);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 0);


        List<Map<String, Object>> list = clickStatDao.queryClickList(startDate, endDate);

        List<ClickRate> ktlist = new ArrayList();
        List<ClickRate> wzlist = new ArrayList();
        List<ClickRate> cdlist = new ArrayList();
        List<ClickRate> cplist = new ArrayList();
        List<ClickRate> tzlist = new ArrayList();
        List<ClickRate> wdlist = new ArrayList();
        List<ClickRate> splist = new ArrayList();

/*      qtype中文名字对应关系
        WHEN tp.qtype='view_caipu_detail' THEN '查看菜谱' \
        WHEN tp.qtype='view_caipu_caidan_detail' THEN '查看菜单' \
        WHEN tp.qtype='live_class_detail' THEN '查看课堂' \
        WHEN tp.qtype='view_group_postreplies' THEN '查看帖子' \
        WHEN tp.qtype='view_article' THEN '订阅号' \
        WHEN tp.qtype='view_tuan_detail' THEN '查看商品' \  废弃   已经不用了
        WHEN tp.qtype='view_group_quizreplay' THEN '查看问答' \
        WHEN tp.qtype='good_view_article' THEN '好物推荐' \*/

        String date = "";
        for (Map<String, Object> map : list) {
            Date statdate = (Date) map.get("statdate");
            String tempDate = DateUtil.dateToString(statdate, "yyyy-MM-dd");
            String qtype = (String) map.get("qtype");
            int click = (int) map.get("click");
            int exposure = (int) map.get("exposure");
            double click_rate = (double) map.get("click_rate");

            ClickRate kt = new ClickRate();
            kt.setStatdate(tempDate);
            kt.setClick(click);
            kt.setClick_rate(click_rate);
            kt.setQtype(qtype);
            kt.setExposure(exposure);

            if (qtype.equals("live_class_detail")) {
                ktlist.add(kt);
            }
            if (qtype.equals("view_article")) {
                wzlist.add(kt);
            }
            if (qtype.equals("view_caipu_caidan_detail")) {
                cdlist.add(kt);
            }
            if (qtype.equals("view_caipu_detail")) {
                cplist.add(kt);
            }
            if (qtype.equals("view_group_postreplies")) {
                tzlist.add(kt);
            }
            if (qtype.equals("view_group_quizreplay")) {
                wdlist.add(kt);
            }
            if (qtype.equals("good_view_article")) {
                splist.add(kt);
            }
        }

        //数据检查没有的用零填充

        int  temp=day+1;//数据是从当前时间的前一天开取的


        wzlist=converListObject(wzlist,temp,"view_article");
        cplist=converListObject(cplist,temp,"view_caipu_detail");
        splist=converListObject(splist,temp,"good_view_article");
        ktlist=converListObject(ktlist,temp,"live_class_detail");
        cdlist=converListObject(cdlist,temp,"view_caipu_caidan_detail");
        tzlist=converListObject(tzlist,temp,"view_group_postreplies");
        wdlist=converListObject(wdlist,temp,"view_group_quizreplay");

        //用于填充折线图数据开始

        String[] columnKeysCooks = new String[day];
        int indexdst=0;
        for (int i=day;i>0;i--){
            columnKeysCooks[indexdst]=getNextDay(i);
            indexdst++;
        }

        double[] arrayKtClick = new double[day];
        double[] arrayWzClick = new double[day];
        double[] arrayCdClick = new double[day];
        double[] arrayCpClick = new double[day];
        double[] arrayTzClick = new double[day];
        double[] arrayWdClick = new double[day];
        double[] arraySpClick = new double[day];
        double[] arraytoalClick=new double[day];




        sbContent.append("<h1>1.1首页总点击率折线图</h1><br/>");
        sbContent.append("<br/><br/>");
        // 构建点击率汇总数据表格开始
        sbContent.append("<img width=\"590\" height=\"500\" src=\"cid:img-click-total-total.jpg\" alt=\"\"/>");

        sbContent.append("<h1>1.2总点击率明细数据</h1><br/>");
        sbContent.append("<br/><br/>");
        // 构建总点击率汇总数据表格开始

        sbContent.append("<table class=\"gridtable\">");
        // 构造表头
        sbContent.append("<tr>" +
                "<th colspan=\"4\">点击率汇总数据</th>" +
                "</tr>");
        sbContent.append("<tr>" +
                "<th>日期</th>" +
                "<th>点击</th>" +
                "<th>曝光</th>" +
                "<th>点击率</th>");



        DecimalFormat df = new DecimalFormat("0.00");

        for(int i=0;i<day;i++){
            ClickRate wz=wzlist.get(i);
            ClickRate cp=cplist.get(i);
            ClickRate sp=splist.get(i);
            ClickRate kt=ktlist.get(i);
            ClickRate cd=cdlist.get(i);
            ClickRate tz=tzlist.get(i);
            ClickRate wd=wdlist.get(i);
            int total_click =(wz.getClick()+cp.getClick()+sp.getClick()+kt.getClick()+cd.getClick()+tz.getClick()+wd.getClick())*100;
            int total_exposure=wz.getExposure()+cp.getExposure()+sp.getExposure()+kt.getExposure()+cd.getExposure()+tz.getExposure()+wd.getExposure();
            String flag="0.0";
            if (total_exposure!=0){
                double total_click_rate=(double) total_click/(double) total_exposure;
                flag=df.format(total_click_rate);
            }

            sbContent.append("<tr>" +
                    "<td>"+wz.getStatdate()+"</td>" +
                    "<td>"+total_click+"</td><td>"+total_exposure+"</td><td>"+flag+"%</td>"+
                    "</tr>"
            );
            arraytoalClick[i]=Double.valueOf(flag);

        }

        sbContent.append("</table>");
        sbContent.append("<br/>");




        sbContent.append("<h1>2.1首页点击率折线图</h1><br/>");
        sbContent.append("<br/><br/>");
        // 构建点击率汇总数据表格开始
        sbContent.append("<table class=\"gridtable\">");
        // 构造 title

        sbContent.append("<tr>" +
                "<th colspan=\"2\">点击率汇总数据</th>" +
                "</tr>");

        sbContent.append("<tr>" +
                "<td><img width=\"590\" height=\"500\" src=\"cid:img-click-wz-total.jpg\" alt=\"\"/></td>" +
                "<td><img width=\"590\" height=\"500\" src=\"cid:img-click-cp-total.jpg\" alt=\"\"/></td>" +
                "</tr>");
        sbContent.append("<tr>" +
                "<td><img width=\"590\" height=\"500\" src=\"cid:img-click-sp-total.jpg\" alt=\"\"/></td>" +
                "<td><img width=\"590\" height=\"500\" src=\"cid:img-click-kt-total.jpg\" alt=\"\"/></td>" +

                "</tr>");
        sbContent.append("<tr>" +
                "<td><img width=\"590\" height=\"500\" src=\"cid:img-click-cd-total.jpg\" alt=\"\"/></td>" +
                "<td><img width=\"590\" height=\"500\" src=\"cid:img-click-tz-total.jpg\" alt=\"\"/></td>" +
                "</tr>");
        sbContent.append("<tr>" +
                "<td><img width=\"590\" height=\"500\" src=\"cid:img-click-wd-total.jpg\" alt=\"\"/></td>" +
                "<td></td>" +
                "</tr>");

        sbContent.append("</table>");
        sbContent.append("<br/>");

        sbContent.append("<h1>2.2点击率明细数据</h1><br/>");
        sbContent.append("<br/><br/>");
        // 构建点击率汇总数据表格开始

        // 构造表格
        sbContent.append("<table class=\"gridtable\">");
        // 构造表头

        sbContent.append("<tr>" +
                "<th colspan=\"22\">点击率汇总数据</th>" +
                "</tr>");
        sbContent.append("<tr>" +
                "<th rowspan=\"2\">日期</th>" +
                "<th colspan=\"3\">生活号</th>" +
                "<th colspan=\"3\">查看菜谱</th>" +
                "<th colspan=\"3\">好物推荐</th>" +
                "<th colspan=\"3\">查看课堂</th>" +
                "<th colspan=\"3\">查看菜单</th>" +
                "<th colspan=\"3\">查看帖子</th>" +
                "<th colspan=\"3\">查看问答</th>");
        sbContent.append("<tr>" +
                "<th width=40px>点击</th><th style=\"width:60px\">曝光</th><th style=\"width:60px\">点击率</th>" +
                "<th width=40px>点击</th><th style=\"width:60px\">曝光</th><th style=\"width:60px\">点击率</th>" +
                "<th width=40px>点击</th><th style=\"width:60px\">曝光</th><th style=\"width:60px\">点击率</th>" +
                "<th width=40px>点击</th><th style=\"width:60px\">曝光</th><th style=\"width:60px\">点击率</th>" +
                "<th width=40px>点击</th><th style=\"width:60px\">曝光</th><th style=\"width:60px\">点击率</th>" +
                "<th width=40px>点击</th><th style=\"width:60px\">曝光</th><th style=\"width:60px\">点击率</th>" +
                "<th width=40px>点击</th><th style=\"width:60px\">曝光</th><th style=\"width:60px\">点击率</th>"
        );

        for(int i=0;i<day;i++){
            ClickRate wz=wzlist.get(i);
            ClickRate cp=cplist.get(i);
            ClickRate sp=splist.get(i);
            ClickRate kt=ktlist.get(i);
            ClickRate cd=cdlist.get(i);
            ClickRate tz=tzlist.get(i);
            ClickRate wd=wdlist.get(i);
            sbContent.append("<tr>" +
                    "<td>"+wz.getStatdate()+"</td>" +
                    "<td>"+wz.getClick()+"</td><td>"+wz.getExposure()+"</td><td>"+wz.getClick_rate()+"%</td>" +
                    "<td>"+cp.getClick()+"</td><td>"+cp.getExposure()+"</td><td>"+cp.getClick_rate()+"%</td>" +
                    "<td>"+sp.getClick()+"</td><td>"+sp.getExposure()+"</td><td>"+sp.getClick_rate()+"%</td>" +
                    "<td>"+kt.getClick()+"</td><td>"+kt.getExposure()+"</td><td>"+kt.getClick_rate()+"%</td>" +
                    "<td>"+cd.getClick()+"</td><td>"+cd.getExposure()+"</td><td>"+cd.getClick_rate()+"%</td>" +
                    "<td>"+tz.getClick()+"</td><td>"+tz.getExposure()+"</td><td>"+tz.getClick_rate()+"%</td>" +
                    "<td>"+wd.getClick()+"</td><td>"+wd.getExposure()+"</td><td>"+wd.getClick_rate()+"%</td>"
            );

            double typekt=kt.getClick_rate();
            double typewz=wz.getClick_rate();
            double typecd=cd.getClick_rate();
            double typecp=cp.getClick_rate();
            double typetz=tz.getClick_rate();
            double typewd=wd.getClick_rate();
            double typesp=sp.getClick_rate();

            arrayKtClick[i]=typekt;
            arrayWzClick[i]=typewz;
            arrayCdClick[i]=typecd;
            arrayCpClick[i]=typecp;
            arrayTzClick[i]=typetz;
            arrayWdClick[i]=typewd;
            arraySpClick[i]=typesp;
        }

        sbContent.append("</table>");
        sbContent.append("<br/>");

        //构建点击率汇总数据折线图 开始
        String[] rowKeysKt = {"课堂"};
        String[] rowKeysWz = {"生活号"};   //由之前的文章更名为订阅号
        String[] rowKeysCd = {"菜单"};
        String[] rowKeysCp = {"菜谱"};
        String[] rowKeysTz = {"帖子"};
        String[] rowKeysWd = {"问答"};
        String[] rowKeysSp = {"好物推荐"};   //商品已经不使用了，为了代码重用，将它用于好物推荐
        String[] rowKeysTotal = {"总体"};

        //图片名称
        String chartTitleKt = "查看课堂";
        String chartTitleWz = "生活号";
        String chartTitleCd = "查看菜单";
        String chartTitleCp = "查看菜谱";
        String chartTitleTz = "查看帖子";
        String chartTitleWd = "查看问答";
        String chartTitleSp = "好物推荐";
        String chartTitleTotal="总体";
        //X轴
        String categoryLabelCooks = "日期";

        // y轴
        String valueLabelCooks = "数量";

        //数据集
        double[][] dataKt = new double[1][];
        dataKt[0] = arrayKtClick;
        double[][] dataWz = new double[1][];
        dataWz[0] = arrayWzClick;
        double[][] dataCd = new double[1][];
        dataCd[0] = arrayCdClick;
        double[][] dataCp = new double[1][];
        dataCp[0] = arrayCpClick;
        double[][] dataTz = new double[1][];
        dataTz[0] = arrayTzClick;
        double[][] dataWd = new double[1][];
        dataWd[0] = arrayWdClick;
        double[][] dataSp = new double[1][];
        dataSp[0] = arraySpClick;
        double[][] dataTotal=new double[1][];
        dataTotal[0]=arraytoalClick;


        //点击率汇总数据折线图
        JFreeChartUtil.buildLineChat(chartTitleKt, categoryLabelCooks, valueLabelCooks, rowKeysKt, columnKeysCooks, dataKt,
                MailConstants.MAIL_CHART_PATH + "/img-click-kt-total.jpg");
        JFreeChartUtil.buildLineChat(chartTitleWz, categoryLabelCooks, valueLabelCooks, rowKeysWz, columnKeysCooks, dataWz,
                MailConstants.MAIL_CHART_PATH + "/img-click-wz-total.jpg");
        JFreeChartUtil.buildLineChat(chartTitleCd, categoryLabelCooks, valueLabelCooks, rowKeysCd, columnKeysCooks, dataCd,
                MailConstants.MAIL_CHART_PATH + "/img-click-cd-total.jpg");
        JFreeChartUtil.buildLineChat(chartTitleCp, categoryLabelCooks, valueLabelCooks, rowKeysCp, columnKeysCooks, dataCp,
                MailConstants.MAIL_CHART_PATH + "/img-click-cp-total.jpg");
        JFreeChartUtil.buildLineChat(chartTitleTz, categoryLabelCooks, valueLabelCooks, rowKeysTz, columnKeysCooks, dataTz,
                MailConstants.MAIL_CHART_PATH + "/img-click-tz-total.jpg");
        JFreeChartUtil.buildLineChat(chartTitleWd, categoryLabelCooks, valueLabelCooks, rowKeysWd, columnKeysCooks, dataWd,
                MailConstants.MAIL_CHART_PATH + "/img-click-wd-total.jpg");
        JFreeChartUtil.buildLineChat(chartTitleSp, categoryLabelCooks, valueLabelCooks, rowKeysSp, columnKeysCooks, dataSp,
                MailConstants.MAIL_CHART_PATH + "/img-click-sp-total.jpg");
        JFreeChartUtil.buildLineChat(chartTitleTotal, categoryLabelCooks, valueLabelCooks, rowKeysTotal, columnKeysCooks, dataTotal,
                MailConstants.MAIL_CHART_PATH + "/img-click-total-total.jpg");

        //构建点击率汇总数据折线图 结束
        return sbContent.toString();
    }


    /**
     *  用于将遍历出来的数据将没有的日期的用零填充
     * @param list
     * @param day
     * @param qtype
     * @return
     */
    public List converListObject(List<ClickRate> list,int day,String qtype){
        List<ClickRate> dstList=new ArrayList<>();
        if (list.isEmpty()){    //判断传进来的值是否为空
            for (int index=1;index<day;index++){
                ClickRate cc=new ClickRate();
                cc.setClick(0);
                cc.setClick_rate(0.0);
                cc.setStatdate(getNextDay(index));
                cc.setExposure(0);
                cc.setQtype(qtype);
                dstList.add(cc);
            }
        } else {
            int i=1; //控制日期天数 与指定天数day作比较
            for (ClickRate clickRate:list){
                clickRate.getQtype();
                boolean temp=DataFillZeroUtil.compare_date(clickRate.getStatdate(),getNextDay(i));
                if(temp){
                    dstList.add(clickRate);
                    i++;
                }else {
                    while (!temp){
                        ClickRate cc=new ClickRate();
                        cc.setClick(0);
                        cc.setClick_rate(0.0);
                        cc.setStatdate(getNextDay(i));
                        cc.setExposure(0);
                        cc.setQtype(qtype);
                        i++;
                        dstList.add(cc);
                        temp=DataFillZeroUtil.compare_date(clickRate.getStatdate(),getNextDay(i));
                    }
                    dstList.add(clickRate);
                    i++;
                }

            }

           boolean temp=DataFillZeroUtil.compare_date(getNextDay(day),getNextDay(i)); //用于传进来的list遍历完之后的日期数据填充
            for (;!temp;){
                ClickRate cc=new ClickRate();
                cc.setClick(0);
                cc.setClick_rate(0.0);
                cc.setStatdate(getNextDay(i));
                cc.setExposure(0);
                cc.setQtype(qtype);
                i++;
                dstList.add(cc);
                temp=DataFillZeroUtil.compare_date(getNextDay(day),getNextDay(i));
            }
        }
        Collections.reverse(dstList);
        return dstList;
    }

}
