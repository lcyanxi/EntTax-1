package com.douguo.dc.mail.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.douguo.dc.datashow.dao.DataShowDao;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JavaSendMail;
import com.douguo.dc.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by lichang on 2017/9/11.
 */
@Repository("mailShowPersonNum")
public class MailShowPersonNum {

    @Autowired
    private DataShowDao dataShowDao;


    /**
     * 在线人数数据生成报表
     * @param sysMailSet
     */
    public void sendShowPersonNumMail(SysMailSet sysMailSet) {
        JavaSendMail javaSendMail = new JavaSendMail();

        String currDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String subject = "";
        String toAddress = "";
        StringBuffer content = new StringBuffer("");
        content.append("<style type=\"text/css\"> table.gridtable { font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse; } table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #dedede; } table.gridtable td { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff; } </style>");


        if (sysMailSet != null) {
            subject = sysMailSet.getSubject()+"_"+currDate;
            toAddress = sysMailSet.getMailTo();
        } else {
            subject = "在线人数数据监控_" + currDate;
            toAddress = "lichang@douguo.com";
        }


        //获得配置信息
        String strConfig = StringUtils.trimToEmpty(sysMailSet.getConfig());
        JSONObject jsonConfig = JsonUtil.parseStrToJsonObj(strConfig);



        //1. 在线人数数据监控
        content.append("<h1>1.在线人数数据监控</h1><br/>");
        content.append(insertPersonNum(jsonConfig));
        content.append("<br/><br/>");

        content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");


        String[] imgPaths = new String[0];
        try {
            //发送邮件
            javaSendMail.sendHtmlWithInnerImageEmail(subject, toAddress, content.toString(), imgPaths);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入每一秒生成的随机人数
     * @param
     * @param
     */
    public String insertPersonNum(JSONObject jsonConfig){
        StringBuffer sbContent = new StringBuffer();

        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 30);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 0);

        dataShowDao.deletePersonNum(startDate,endDate);


        // 构建插入在线人数数据表格开始
        sbContent.append("<table class=\"gridtable\">");
        // 构造 title

        sbContent.append("<tr>" +
                "<th colspan=\"2\">每小时在线总人数</th>" +
                "</tr>");
        sbContent.append("<tr>" +
                "<td>时间</td>" +
                "<td>人数</td>" +
                "</tr>");

        List<Map> list=createPersonNum(jsonConfig);
        long starTime=System.currentTimeMillis();
        if(!list.isEmpty()) {
            for (Map map : list) {
                int num = 0;
                Iterator iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String temp = (String) entry.getValue();
                    int personToalNum = Integer.parseInt(temp);
                    String dataTime = (String) entry.getKey();

                    //1.填值到表格
                    sbContent.append("<tr>");
                    sbContent.append("<td>" + dataTime + "</td>");
                    sbContent.append("<td>" + personToalNum + "</td>");
                    sbContent.append("</tr>");

                    List personNum = RandomPersonNum(personToalNum);//把拿到的分成3600个随机数

                    int index = 0;
                    String minute = "";
                    String second = "";
                    for (int i = 0; i < 60; i++) {   //把拿到的数据填入到每一秒里
                        for (int j = 0; j < 60; j++) {
                            if (i < 10) {
                                minute = ":0" + i;
                            } else {
                                minute = ":" + i;
                            }
                            if (j < 10) {
                                second = ":0" + j;
                            } else {
                                second = ":" + j;
                            }

                            String newDateTime = dataTime + minute + second;
                            int readom = (int) personNum.get(index);
                            num = num + readom;
                            dataShowDao.inserPersonNum(num, newDateTime);
                            index++;
                        }
                    }
                }
            }
        }else {
            System.out.println("---------上传的数据为空-----------------");
        }
        sbContent.append("</table>");
        sbContent.append("<br/>");
        long endTime=System.currentTimeMillis();
        System.out.println(endTime-starTime);

        return sbContent.toString();

    }


    /**
     * 在线人数随机数 误差小于100
     * @param total
     * @return
     */
    private List  RandomPersonNum(int total){
        List list=null;
        boolean flag=true;
        int min=0;
        int size=0;
        int max=0;
        if (total<30000){
            min=2;
            size=1;
            max=20;
        }
        else if (total>30000&&total<50000){
            min=5;
            size=1;
            max=30;
        }else if (total>50000&&total<100000){
            min=10;
            size=2;
            max=40;
        }else if (total>100000){
            min=10;
            size=3;
            max=50;
        }
        while (flag){
            Random random = new Random();
            list =new ArrayList();
            int temp=0;
            for(int i=0;i<3600;i++){
                int num=random.nextInt(max)%(max-10+1) + min;
                temp = temp+num;
                list.add(num);
            }
            int count=temp-total;
            if (Math.abs(count)<100){
                flag=false;
            }
            if(count>1000){
                max= max-size;
            }if (count<-1000){
                max=max+size;
            }
        }
        return list;
    }


    private List<Map> createPersonNum(JSONObject jsonConfig){

        List<Map> indexList=new ArrayList<>();

        String strDesc = jsonConfig.getString("trace_days");
        Integer nDays = 30;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(strDesc)) {
            try {
                nDays = Integer.parseInt(strDesc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        JSONArray files = jsonConfig.getJSONArray("files");
        String numFilePath=null;
        String[] percentNum =null;
        Iterator it = files.iterator();
        int tableTitleRank = 0;
        while (it.hasNext()) {
            tableTitleRank++;
            JSONObject jsonObj = (JSONObject) it.next();
            numFilePath = jsonObj.getString("file_path");
            String percentFilePath=jsonObj.getString("percentFilePath");
            percentNum=percentFilePath.split(",");
            String title = jsonObj.getString("title");
            if (title == null) {
                title = "数据报表 " + String.valueOf(tableTitleRank);
            }
        }


        System.out.println(percentNum[0]+percentNum[6]);

        //拿到一个月的时间日期
        List dateTime=new ArrayList();
        for (int i=0;i<nDays;i++){
            dateTime.add(DateUtil.getNextDay(-i));
        }

        //获取一个周的在线人数的占比
        List<Map<String,String>>  percentOneList=createPercentPerson(percentNum[0]);
        List<Map<String,String>>  percentTwoList=createPercentPerson(percentNum[1]);
        List<Map<String,String>> percentThreeList=createPercentPerson(percentNum[2]);
        List<Map<String,String>> percentFourList=createPercentPerson(percentNum[3]);
        List<Map<String,String>> percentFiveList=createPercentPerson(percentNum[4]);
        List<Map<String,String>> percentSixList=createPercentPerson(percentNum[5]);
        List<Map<String,String>> percentServenList=createPercentPerson(percentNum[6]);




        //获取在线人数总数
        List<Map<String,String>> list;
        File file = new File(numFilePath);
        try{
            list=new ArrayList();
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                Map<String,String> map=new LinkedHashMap<>();
                String[] strArray = s.split("\\s+"); //拆分字符为"," ,然后把结果交给数组strArray
                map.put(strArray[0],strArray[1]);
                list.add(map);
            }
            br.close();
        }catch(Exception e){
            list=null;
            e.printStackTrace();
        }

        //获取在线人数每小时的总人数
        for (Map<String,String> map:list){
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String  key = (String)entry.getKey();
                String temp=key.replaceAll("/", "-");
                if (DateUtil.isContents(dateTime,temp)){   //获取一个月的数据
                    String indexNum=(String)entry.getValue();
                    int temNum=Integer.parseInt(indexNum);
                    int weekDay=DateUtil.getWeekDayEn(DateUtil.stringToDate(temp));
                    switch (weekDay){
                        case 0:
                            indexList.add(showPersonNum(percentServenList,temNum,temp));
                            break;
                        case 1:
                            indexList.add(showPersonNum(percentOneList,temNum,temp));
                            break;
                        case 2:
                            indexList.add(showPersonNum(percentTwoList,temNum,temp));
                            break;
                        case 3:
                            indexList.add(showPersonNum(percentThreeList,temNum,temp));
                            break;
                        case 4:
                            indexList.add(showPersonNum(percentFourList,temNum,temp));
                            break;
                        case 5:
                            indexList.add(showPersonNum(percentFiveList,temNum,temp));
                            break;
                        case 6:
                            indexList.add(showPersonNum(percentSixList,temNum,temp));
                            break;
                    }
                }

            }

        }

    return  indexList;

    }


    /**
     * 获取一周内每小时的占比
     * @param fileName
     * @return
     */
    private List  createPercentPerson(String fileName){
        List list;
        File file = new File(fileName);
        try{
            list=new ArrayList();
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                Map<String,String> map=new LinkedHashMap<>();
                String[] strArray = s.split("\\s+"); //拆分字符为"," ,然后把结果交给数组strArray
                map.put(strArray[0],strArray[1]);
                list.add(map);
            }
            br.close();
        }catch(Exception e){
            list=null;
            e.printStackTrace();
        }

        return list;
    }


    /**
     * 获取每小时在线总人数
     * @param list
     * @param num
     * @param date
     * @return
     */
    private Map<String,String> showPersonNum(List<Map<String,String>> list,int num,String date){

        DecimalFormat df = new DecimalFormat("#");

        Map<String,String> tempMap=new LinkedHashMap<>();
        for (Map<String,String> map:list){
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String  key = (String)entry.getKey();
                String value=(String)entry.getValue();
                double totalNum=Double.valueOf(value)*num;
                df.format(totalNum);
                tempMap.put(date+" "+key,df.format(totalNum));
            }
        }
        return tempMap;
    }

}
