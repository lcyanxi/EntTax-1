package com.douguo.dc.mail.service.impl;

import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.dao.FamilyStatusDao;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JFreeChartUtil;
import com.douguo.dc.util.JavaSendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

@Repository("mailFamilyStatService")
public class MailFamilyStatService {

    @Autowired
    private FamilyStatusDao familyStatusDao;


    private static final int WIDTH = 590;//图片宽
    private static final int HIGH = 500;  //图片高
    private static final int TITLEFONTSIZE=24; //图片标题字体大小
    private static final int ITEMFONTSIZE=20;//图片内容字体大小

    /**
     * 发送家庭统计报表邮件
     *
     * @param sysMailSet
     * @return
     */
    public void sendFamilySumMail(SysMailSet sysMailSet) {

        JavaSendMail javaSendMail = new JavaSendMail();

        String currDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String subject = "";
        String toAddress = "";
        StringBuffer content = new StringBuffer("");
        content.append("<style type=\"text/css\"> table.gridtable { font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse; } table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #dedede; } table.gridtable td { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff; } </style>");
        System.out.println(toAddress);
        if (sysMailSet != null) {
            subject = sysMailSet.getSubject()+"_"+currDate;
            toAddress = sysMailSet.getMailTo();
            System.out.println(toAddress);
        } else {
            subject = "家庭数据报表统计_" + currDate;
            toAddress = "lichang@douguo.com";
        }


        //获取需统计天数
        String strDesc = sysMailSet.getDesc();
        Integer nDays = 7;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(strDesc)) {
            try {
                nDays = Integer.parseInt(strDesc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //1. 家庭汇总数据统计
        content.append("<h1>1.汇总数据报表</h1><br/>");
        content.append("<h1>1.1有效家庭汇总数据报表</h1><br/>");
        content.append(familyBaseStat(nDays));
        content.append("<br/><br/>");

        //2.性别统计百分比
        content.append(personPortrait());
        content.append("<br/><br/>");


        content.append("<br/><br/>");
        content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");


        String[] imgPaths = new String[10];
        imgPaths[0] = MailConstants.MAIL_CHART_PATH + "/img-sex-precentage-type.jpg";
        imgPaths[1] = MailConstants.MAIL_CHART_PATH + "/img-age-precentage-type.jpg";
        imgPaths[2] = MailConstants.MAIL_CHART_PATH + "/img-income-precentage-type.jpg";
        imgPaths[3] = MailConstants.MAIL_CHART_PATH + "/img-professional-precentage-type.jpg";
        imgPaths[4] = MailConstants.MAIL_CHART_PATH + "/img-avoid-precentage-type.jpg";
        imgPaths[5] = MailConstants.MAIL_CHART_PATH + "/img-chronicdisease-precentage-type.jpg";
        imgPaths[6] = MailConstants.MAIL_CHART_PATH + "/img-family-sum-total.jpg";
        imgPaths[7] = MailConstants.MAIL_CHART_PATH + "/img-new-add-family-sum-total.jpg";
        imgPaths[8] = MailConstants.MAIL_CHART_PATH + "/img-province-precent-type.jpg";
        imgPaths[9] = MailConstants.MAIL_CHART_PATH + "/img-city-precent-type.jpg";
        try {
            //发送邮件
            javaSendMail.sendHtmlWithInnerImageEmail(subject, toAddress, content.toString(), imgPaths);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建家庭邮件内容
     *
     * @return 家庭汇总数据表格
     */
    private String familyBaseStat(Integer beforeDays) {
        StringBuffer sbContent = new StringBuffer();

        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, beforeDays);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 0);
        List<Map<String, Object>> mapList = familyStatusDao.queryFamilyAllList(startDate,endDate);

        // 构造家庭报表统计表格
        sbContent.append("<table class=\"gridtable\">");

        // 构造 title
        sbContent.append("<tr>" +
                "<th>日期</th>" +
                "<th>有效家庭数</th>" +
                "<th>有效家庭成员数</th>" +
                "<th>关联菜谱数</th>" +
                "<th>平均家庭成员数</th>" +
                "<th>平均管理计划天数</th>" +
                "</tr>");

        //遍历查询结果
        for (int i=mapList.size()-1;i>=0;i--) {
            //取值
            int family_num =(int) mapList.get(i).get("family_num");
            int member_num = (int) mapList.get(i).get("member_num");
            int cook_num = (int) mapList.get(i).get("cook_num");
            double avg_family_members = (double) mapList.get(i).get("avg_family_members");
            double avg_manage_days = (double) mapList.get(i).get("avg_manage_days");
            String statdate = (String) mapList.get(i).get("statdate");
            String strWeek = DateUtil.getWeekDay(statdate);

            //1.填值到表格
            sbContent.append("<tr>");
            sbContent.append("<td>" + statdate + "(" + strWeek + ")</td>");
            sbContent.append("<td>" + family_num + "</td>");
            sbContent.append("<td>" + member_num + "</td>");
            sbContent.append("<td>" + cook_num + "</td>");
            sbContent.append("<td>" + avg_family_members + "</td>");
            sbContent.append("<td>" + avg_manage_days + "</td>");
            sbContent.append("</tr>");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");

        //构造家庭报表统计表格结束

        //构建家庭报表折线图 开始
        String[] rowKeysCooks = {"有效家庭数", "有效家庭成员数"};
        String[] columnKeysCooks = new String[mapList.size()];
        double[] arrayFamily_num = new double[mapList.size()];
        double[] arrayFamily_member_num = new double[mapList.size()];


        int index=0;
        for (int j = mapList.size()-1; j>=0; j--) {
            //获取日期 : 格式：2016-09-27(二)
            columnKeysCooks[index] = mapList.get(j).get("statdate") + "(" + DateUtil.getWeekDay(String.valueOf(mapList.get(j).get("statdate"))) + ")";

            int family_num = (int) mapList.get(j).get("family_num");
            arrayFamily_num[index] = family_num;

            int avg_family_members = (int) mapList.get(j).get("member_num");
            arrayFamily_member_num[index] = avg_family_members;

            index++;
        }

        //图片名称
        String chartTitleCooks = "有效家庭汇总折线图";

        //X轴
        String categoryLabelCooks = "日期";

        // y轴
        String valueLabelCooks = "人数";

        //数据集
        double[][] dataCooks = new double[2][];

        dataCooks[0] = arrayFamily_num;
        dataCooks[1] = arrayFamily_member_num;


        sbContent.append("<br/><br/>");
        sbContent.append("<h1>1.2有效家庭汇总数据折线图</h1><br/>");
        sbContent.append("<img width=\"590\" height=\"500\" src=\"cid:img-family-sum-total.jpg\" alt=\"\"/>");

        //家庭汇总折线图
        JFreeChartUtil.buildLineChat(chartTitleCooks, categoryLabelCooks, valueLabelCooks, rowKeysCooks, columnKeysCooks, dataCooks,
                MailConstants.MAIL_CHART_PATH + "/img-family-sum-total.jpg");
        //构建家庭报表折线图 结束

        //构建新增家庭数table  开始
        sbContent.append(newAddFamily(mapList));
        //构建新增家庭数table  结束

        return sbContent.toString();
    }


    /**
     * 新增家庭数
     *
     * @param list
     * @return
     */
    private StringBuffer newAddFamily(List<Map<String, Object>> list) {

        StringBuffer sbContent = new StringBuffer("");

        // 构造新增家庭报表统计表格  开始
        sbContent.append("<h1>1.3新增有效家庭数据表</h1><br/>");
        sbContent.append("<table class=\"gridtable\">");

        // 构造 title
        sbContent.append("<tr>" +
                "<th>日期</th>" +
                "<th>新增有效家庭数</th>" +
                "<th>新增有效家庭成员数</th>" +
                "</tr>");


        double[] arrayFamily_num = new double[list.size() - 1];
        double[] arrayMember_num = new double[list.size() - 1];
        String[] columnKeysCooks = new String[list.size() - 1];

        int index=0;
        for (int j = list.size()-1; j>0; j--) {
            double family_num , member_num ;

            String statdate = (String) list.get(j-1).get("statdate");
            columnKeysCooks[index] = statdate;

            family_num = ((int) list.get(j-1).get("family_num")) - ((int) list.get(j).get("family_num"));
            arrayFamily_num[index] = family_num;

            member_num = ((int) list.get(j-1).get("member_num")) - ((int) list.get(j).get("member_num"));
            arrayMember_num[index] = member_num;

            index++;

            //1.填值到表格
            sbContent.append("<tr>");
            sbContent.append("<td>" + statdate + "(" + DateUtil.getWeekDay(statdate) + ")</td>");
            sbContent.append("<td>" + (int) family_num + "</td>");
            sbContent.append("<td>" + (int) member_num + "</td>");
            sbContent.append("</tr>");
        }

        sbContent.append("</table>");
        sbContent.append("<br/>");
        sbContent.append("<br/><br/>");

        //构造新增家庭报表统计表格  结束


        //构建新增家庭报表折线图  开始
        //图片名称
        String chartTitleCooks = "新增有效家庭汇总折线图";

        //X轴
        String categoryLabelCooks = "日期";

        // y轴
        String valueLabelCooks = "人数";

        String[] rowKeysCooks = {"新增有效家庭数", "新增有效家庭成员数"};

        //数据集
        double[][] dataCooks = new double[2][];

        dataCooks[0] = arrayFamily_num;
        dataCooks[1] = arrayMember_num;


        sbContent.append("<br/><br/>");
        sbContent.append("<h1>1.4新增有效家庭汇总折线图</h1><br/>");
        sbContent.append("<img width=\"590\" height=\"500\" src=\"cid:img-new-add-family-sum-total.jpg\" alt=\"\"/>");

        //家庭汇总折线图
        JFreeChartUtil.buildLineChat(chartTitleCooks, categoryLabelCooks, valueLabelCooks, rowKeysCooks, columnKeysCooks, dataCooks,
                MailConstants.MAIL_CHART_PATH + "/img-new-add-family-sum-total.jpg");
        //构建家庭报表折线图 结束

        return sbContent;
    }

    /**
     * 人群画像饼图放入表格里
     *
     * @return string
     */
    private String personPortrait() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 1);

        StringBuffer sbContent = new StringBuffer();

        Map<String, Double> sexTemMap = new HashMap<>();
        Map<String, Double> ageTemMap = new HashMap<>();
        Map<String, Double> incomeTemMap = new HashMap<>();
        Map<String, Double> professionTemMap = new HashMap<>();
        Map<String, Double> chronicillnessTemMap = new HashMap<>();
        Map<String, Double> taboosTemMap = new HashMap<>();


        List<Map<String, Object>> list = familyStatusDao.queryByTypePrecentageList(startDate,endDate);

        String statdate="";
        for (Map<String,Object> map:list){
            Map<String,Double> tempMap=new HashMap<>();
            statdate=(String) map.get("statdate");
            String name = (String) map.get("dimention_name");
            String type= (String) map.get("dimention_type");
            int total =  (int)map.get("dimention_value");
            double dimention_value=total;

            if (type.equals("sex")){
                //类型转换
                sexTemMap.put(name, dimention_value);
            }else if (type.equals("age")){
                ageTemMap.put(name, dimention_value);
            }else if (type.equals("income")){
                incomeTemMap.put(name, dimention_value);
            }else if (type.equals("profession")){
                professionTemMap.put(name, dimention_value);
            }else if (type.equals("chronicillness")){
                chronicillnessTemMap.put(name, dimention_value);
            }else if (type.equals("taboos")){
                taboosTemMap.put(name, dimention_value);
            }
        }


        // 构造家庭报表统计表格
        sbContent.append("<h1>2、人群画像</h1><br/>");
        sbContent.append("<table class=\"gridtable\">");

        // 构造 title
        sbContent.append("<tr>" +
                "<th colspan=\"3\">人群画像_'"+statdate+"'</th>" +
                "</tr>");
        //1.填值到表格
        sbContent.append("<tr>");
        sbContent.append("<td>" + sexPrecentageStat(sexTemMap) + "</td>");
        sbContent.append("<td>" + agePrecentageStat(ageTemMap) + "</td>");
        sbContent.append("</tr>");

        sbContent.append("<tr>");
        sbContent.append("<td>" + incomePrecentageStat(incomeTemMap) + "</td>");
        sbContent.append("<td>" + professionalPrecentageStat(professionTemMap) + "</td>");
        sbContent.append("</tr>");

        sbContent.append("<tr>");
        sbContent.append("<td>" + avoidPrecentageStat(taboosTemMap) + "</td>");
        sbContent.append("<td>" + chronicdiseasePrecentageStat(chronicillnessTemMap) + "</td>");
        sbContent.append("</tr>");

        sbContent.append("<tr>");
        sbContent.append("<td colspan=\"3\">" +provinceBarStat(list) + "</td>");
        sbContent.append("</tr>");

        sbContent.append("<tr>");
        sbContent.append("<td colspan=\"3\">" +cityBarStat(list) + "</td>");
        sbContent.append("</tr>");

        sbContent.append("</table>");
        sbContent.append("<br/>");



        return sbContent.toString();
    }

    /**
     * 性别所占百分比
     *
     * @return 性别百分比饼状图
     */
    private StringBuffer sexPrecentageStat(Map<String, Double> sexTemMap) {

        StringBuffer sbContent = new StringBuffer();

        sbContent.append("<img width=\"490\" height=\"400\" src=\"cid:img-sex-precentage-type.jpg\" alt=\"\"/>");

        //生成图片
        String chartTitle = "性别";
        JFreeChartUtil.buildPieChat(chartTitle, sexTemMap, MailConstants.MAIL_CHART_PATH + "/img-sex-precentage-type.jpg", WIDTH, HIGH,TITLEFONTSIZE,ITEMFONTSIZE);
        //生成图片结束

        return sbContent;
    }

    /**
     * 年龄所占百分比统计
     *
     * @return 年龄百分比饼状图
     */
    private StringBuffer agePrecentageStat(Map<String, Double> ageTemMap ) {

        StringBuffer sbContent = new StringBuffer();

        sbContent.append("<img width=\"490\" height=\"400\" src=\"cid:img-age-precentage-type.jpg\" alt=\"\"/>");

        //生成图片
        String chartTitle = "年龄";
        JFreeChartUtil.buildPieChat(chartTitle, ageTemMap, MailConstants.MAIL_CHART_PATH + "/img-age-precentage-type.jpg", WIDTH, HIGH,TITLEFONTSIZE,ITEMFONTSIZE);
        //生成图片结束

        return sbContent;
    }


    /**
     * 收入所占百分比统计
     *
     * @return 收入百分比饼状图
     */
    private StringBuffer incomePrecentageStat(Map<String, Double> ageTemMap) {

        StringBuffer sbContent = new StringBuffer();
        sbContent.append("<img width=\"490\" height=\"400\" src=\"cid:img-income-precentage-type.jpg\" alt=\"\"/>");

        //生成图片
        String chartTitle = "收入";
        JFreeChartUtil.buildPieChat(chartTitle, ageTemMap, MailConstants.MAIL_CHART_PATH + "/img-income-precentage-type.jpg", WIDTH, HIGH,TITLEFONTSIZE,ITEMFONTSIZE);
        //生成图片结束

        return sbContent;
    }

    /**
     * 职业所占百分比统计
     *
     * @return 职业百分比饼状图
     */
    private StringBuffer professionalPrecentageStat(Map<String, Double> ageTemMap) {

        StringBuffer sbContent = new StringBuffer();

        sbContent.append("<img width=\"490\" height=\"400\" src=\"cid:img-professional-precentage-type.jpg\" alt=\"\"/>");

        //生成图片
        String chartTitle = "职业";
        JFreeChartUtil.buildPieChat(chartTitle, ageTemMap, MailConstants.MAIL_CHART_PATH + "/img-professional-precentage-type.jpg", WIDTH, HIGH,TITLEFONTSIZE,ITEMFONTSIZE);
        //生成图片结束

        return sbContent;
    }

    /**
     * 忌口所占百分比统计
     *
     * @return 忌口百分比饼状图
     */
    private StringBuffer avoidPrecentageStat(Map<String, Double> ageTemMap ) {

        StringBuffer sbContent = new StringBuffer();

        sbContent.append("<img width=\"490\" height=\"400\" src=\"cid:img-avoid-precentage-type.jpg\" alt=\"\"/>");

        //生成图片
        String chartTitle = "忌口";
        JFreeChartUtil.buildPieChat(chartTitle, ageTemMap, MailConstants.MAIL_CHART_PATH + "/img-avoid-precentage-type.jpg", WIDTH, HIGH,TITLEFONTSIZE,ITEMFONTSIZE);
        //生成图片结束

        return sbContent;
    }

    /**
     * 慢性病所占百分比统计
     *
     * @return 慢性病百分比饼状图
     */
    private StringBuffer chronicdiseasePrecentageStat(Map<String, Double> ageTemMap) {

        StringBuffer sbContent = new StringBuffer();

        sbContent.append("<img width=\"490\" height=\"400\" src=\"cid:img-chronicdisease-precentage-type.jpg\" alt=\"\"/>");

        //生成图片
        String chartTitle = "慢性病";
        JFreeChartUtil.buildPieChat(chartTitle, ageTemMap, MailConstants.MAIL_CHART_PATH + "/img-chronicdisease-precentage-type.jpg", WIDTH, HIGH,TITLEFONTSIZE,ITEMFONTSIZE);
        //生成图片结束

        return sbContent;
    }


    /**
     * 省份分布柱形图
     * @param countryList
     * @return
     */
    private StringBuffer provinceBarStat(List<Map<String,Object>> countryList){
        StringBuffer  sbContent=new StringBuffer();

        int size=35;
        List  countryNameList=new ArrayList();
        List  countryValueList=new ArrayList();

        for (Map<String,Object> map:countryList) {
            String name = (String) map.get("dimention_name");
            String type = (String) map.get("dimention_type");
            int value = (int) map.get("dimention_value");
            double tempvalue=(double) value;

            if (type.equals("province")){
                countryNameList.add(name);
                countryValueList.add(tempvalue);
            }
        }
        countryNameList=converSizeLists(countryNameList,size);
        countryValueList=converSizeLists(countryValueList,size);

        String[] columnKeysCooks = new String[size];
        double[] arrayCountry= new double[size];
        int temp=1;
        int index=0;
        for (int i=countryNameList.size()-1;temp<=size;i--){
            columnKeysCooks[index]=(String)countryNameList.get(i);
            arrayCountry[index]=(double)countryValueList.get(i);
            index++;
            temp++;
        }


        //首页分客户端总数据汇总统计柱形图 开始
        String[] rowKeysCooks = {"省份"};

        //图片名称
        String chartTitleCooks = "省份分布";

        //X轴
        String categoryLabelCooks = "名称";

        // y轴
        String valueLabelCooks = "数量";

        //数据集
        double[][] dataCooks = new double[1][];
        dataCooks[0] = arrayCountry;


        // 首页分客户端总数据汇总统计折线图表格
        sbContent.append("<img width=\"990\" height=\"500\" src=\"cid:img-province-precent-type.jpg\" alt=\"\"/>");

        //生成柱形图
        JFreeChartUtil.buildBarChat3(chartTitleCooks, categoryLabelCooks, valueLabelCooks, rowKeysCooks, columnKeysCooks, dataCooks,
                MailConstants.MAIL_CHART_PATH + "/img-province-precent-type.jpg");

        return sbContent;
    }


    /**
     * 城市分布柱形图
     * @param countryList
     * @return
     */
    private StringBuffer cityBarStat(List<Map<String,Object>> countryList){
        StringBuffer  sbContent=new StringBuffer();

        int size=50;
        List  countryNameList=new ArrayList();
        List  countryValueList=new ArrayList();

        for (Map<String,Object> map:countryList) {
            String name = (String) map.get("dimention_name");
            String type = (String) map.get("dimention_type");
            int value = (int) map.get("dimention_value");
            double tempvalue=(double) value;

            if (type.equals("city")){
                countryNameList.add(name);
                countryValueList.add(tempvalue);
            }
        }
        countryNameList=converSizeLists(countryNameList,size);
        countryValueList=converSizeLists(countryValueList,size);

        String[] columnKeysCooks = new String[size];
        double[] arrayCountry= new double[size];
        int temp=1;
        int index=0;
        for (int i=countryNameList.size()-1;temp<=size;i--){
            columnKeysCooks[index]=(String)countryNameList.get(i);
            arrayCountry[index]=(double)countryValueList.get(i);
            index++;
            temp++;
        }


        //首页分客户端总数据汇总统计柱形图 开始
        String[] rowKeysCooks = {"城市"};

        //图片名称
        String chartTitleCooks = "城市分布";

        //X轴
        String categoryLabelCooks = "名称";

        // y轴
        String valueLabelCooks = "数量";

        //数据集
        double[][] dataCooks = new double[1][];
        dataCooks[0] = arrayCountry;


        // 首页分客户端总数据汇总统计折线图表格
        sbContent.append("<img width=\"990\" height=\"500\" src=\"cid:img-city-precent-type.jpg\" alt=\"\"/>");

        //生成柱形图
        JFreeChartUtil.buildBarChat3(chartTitleCooks, categoryLabelCooks, valueLabelCooks, rowKeysCooks, columnKeysCooks, dataCooks,
                MailConstants.MAIL_CHART_PATH + "/img-city-precent-type.jpg");

        return sbContent;
    }

    /**
     *  取指定长度的数据
     * @param list
     * @param index
     * @return
     */
    private List converSizeLists(List list,int index){
        List templist=new ArrayList();
        if (list.size()>index){
            for (int i=0;i<index;i++){
                templist.add(list.get(i));
            }
            Collections.reverse(templist);//倒序取后指定个数数据
        }else {
            templist=list;
        }

        return  templist;
    }




}
