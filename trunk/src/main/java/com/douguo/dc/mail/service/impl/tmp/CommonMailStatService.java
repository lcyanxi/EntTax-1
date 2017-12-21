package com.douguo.dc.mail.service.impl.tmp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.douguo.crm.model.VIPUser;
import com.douguo.crm.model.VIPUserNew;
import com.douguo.crm.service.VIPUserNewService;
import com.douguo.crm.service.VIPUserService;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JavaSendMail;
import com.douguo.dc.util.JsonUtil;
import com.douguo.dg.user.service.DgUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * 临时性-发送临时邮件
 *
 * @author zyz
 */
@Repository("commonMailStatService")
public class CommonMailStatService {

    @Autowired
    MailSubwayStatService mailSubwayStatService;

    @Autowired
    DgUserService dgUserService;

    @Autowired
    VIPUserService vipUserService;

    @Autowired
    VIPUserNewService vipUserNewService;

    @Autowired
    CommonMailQtypeStatService commonMailQtypeStatService;

    /**
     * @param sysMailSet
     */
    public void sendMail_old(SysMailSet sysMailSet) {
        if (sysMailSet != null) {
            String strConfig = StringUtils.trimToEmpty(sysMailSet.getConfig());
            JSONObject jsonConfig = JsonUtil.parseStrToJsonObj(strConfig);

            String strDesc = StringUtils.trimToEmpty(sysMailSet.getDesc());//

            if (StringUtils.isNotBlank(strDesc)) {
                if (strDesc.equals("subway")) {
                    mailSubwayStatService.sendMail(sysMailSet);
                } else {
                    String curDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
                    String subject = sysMailSet.getSubject() + "_" + curDate;
                    String to = sysMailSet.getMailTo();
//                    String mailContent = buildMailContent(strDesc);
                    String mailContent = "";
                    try {
                        JavaSendMail.sendHtmlEmail(subject, to, mailContent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            try {
                JavaSendMail.sendTextEmail("邮件发送失败", "zhangyaozhou@douguo.com", "");
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * @param sysMailSet
     */
    public void sendMail(SysMailSet sysMailSet) {
        JavaSendMail javaSendMail = new JavaSendMail();
        if (sysMailSet != null) {
            String strConfig = StringUtils.trimToEmpty(sysMailSet.getConfig());
            JSONObject jsonConfig = JsonUtil.parseStrToJsonObj(strConfig);

            StringBuffer content = new StringBuffer("");

            String curDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
            String subject = sysMailSet.getSubject() + "_" + curDate;
            String to = sysMailSet.getMailTo();
            try {
                content.append("<style type=\"text/css\"> table.gridtable { font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse; } table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #dedede; } table.gridtable td { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff; } </style>");
                content.append(buildMailContent(jsonConfig));
                if (org.apache.commons.lang3.StringUtils.isNotBlank(jsonConfig.getString("qtypeStr")) && org.apache.commons.lang3.StringUtils.isNotBlank(jsonConfig.getString("qtypeCh"))) {
                    Map targetMap = commonMailQtypeStatService.qtypeClassificationStatOderByNum(jsonConfig);
                    content.append(targetMap.get("sbContent"));
                    List imgUrlArr = (List) targetMap.get("imgUrlArr");
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
        } else {
            try {
                JavaSendMail.sendTextEmail("邮件发送失败", "zhangyaozhou@douguo.com", "");
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    /**
     * 构建邮件体
     *
     * @param
     * @return
     */
    private String buildMailContent(JSONObject jsonConfig) {

        StringBuffer content = new StringBuffer("");
        StringBuffer mailContentTmp = new StringBuffer("");
        try {
            // 增加样式
            JSONArray files = jsonConfig.getJSONArray("files");
            //获得要展示的天数
            String strDesc = jsonConfig.getString("trace_days");

            Integer nDays = 9;
            if (org.apache.commons.lang3.StringUtils.isNotBlank(strDesc)) {
                try {
                    nDays = Integer.parseInt(strDesc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Iterator it = files.iterator();
            int tableTitleRank = 0;
            while (it.hasNext()) {
                tableTitleRank++;
                JSONObject jsonObj = (JSONObject) it.next();
                String filePath = jsonObj.getString("file_path");
                String title = jsonObj.getString("title");
                String viewStyle = jsonObj.getString("view_style");
                String filterDate = jsonObj.getString("is_filter_date");
                boolean isFilterDate = true;
                if (StringUtils.isNotBlank(filterDate)) {
                    if ("false".equals(filterDate)) {
                        isFilterDate = false;
                    }
                }

                if (title == null) {
                    title = "数据报表 " + String.valueOf(tableTitleRank);
                }

                mailContentTmp.append("<h2>" + title + "</h2>");

                //直接展示
                if (viewStyle == null) {
                    mailContentTmp.append(getFileContent(filePath) + "<br/>");
                } else if (viewStyle.equals("table")) {//表格展示
                    mailContentTmp.append(getFileContentForTable(filePath, nDays, isFilterDate));
                } else if (viewStyle.equals("import_crm_vip_user")) {
                    mailContentTmp.append(importCrmVipUser(filePath));
                } else if (viewStyle.equals("import_crm_vip_user_new")) {
                    mailContentTmp.append(importCrmVipUserNew(filePath));
                }
            }

            //content.append("<h1>数据统计</h1><br/>");
            content.append(mailContentTmp.toString());
            content.append("<br/><br/>");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return content.toString();
    }


    /**
     * 构建邮件体
     *
     * @param
     * @return
     */
    private String buildMailContent_old(String strDesc) {
        // (废弃)格式：/opt/DATA/goldmine/live_class/data/data_class.log#table@title1&/opt/DATA/goldmine/live_class/data/data_class2.log#table@title2
        //             表格以 & 分割
        //             每个表格 @ 标记标题, # 标记形成类型（table：表格）
        // 格式：{"path":"C:\\Users\\Administrator\\Desktop\\work\\dataTemp.log","title":"测试数据标题1","viewStyle":"table"};
        //      {"path":"C:\\Users\\Administrator\\Desktop\\work\\index_focus_2017-04-04.log","title":"测试数据标题2","viewStyle":"table"}
        StringBuffer content = new StringBuffer("");
        StringBuffer mailContentTmp = new StringBuffer("");
        try {
            // 增加样式
            content.append("<style type=\"text/css\"> table.gridtable { font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse; } table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #dedede; } table.gridtable td { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff; } </style>");
            // 分割多个报表 JSON
            String[] arryDescDatas = strDesc.split(";");
            int tableTitleRank = 0;
            for (String strJsonData : arryDescDatas) {
                tableTitleRank += 1;
                JSONObject descJson = JsonUtil.parseStrToJsonObj(strJsonData);
                String path = descJson.getString("path");
                String title = descJson.getString("title");
                String viewStyle = descJson.getString("viewStyle");
                if (title == null) {
                    title = "数据报表 " + Integer.toString(tableTitleRank);
                }
                if (path == null) {
                    path = "";
                }
                if (viewStyle == null) {
                    viewStyle = "table";
                }
                mailContentTmp.append("<h2>" + title + "</h2>");
                if (viewStyle.equals("table")) {
                    mailContentTmp.append(getFileContentForTable(path, 8,true));
                } else if (viewStyle.equals("import_crm_vip_user")) {
                    mailContentTmp.append(importCrmVipUser(path));
                } else if (viewStyle.equals("import_crm_vip_user_new")) {
                    mailContentTmp.append(importCrmVipUserNew(path));
                }
            }


//            // 分割多个报表
//            String[]  arryDescDatas = strDesc.split("&");
//            for (String strDataRoute : arryDescDatas){
//                String arraySingleTable[] = strDataRoute.split("@");
//                String tableTitle = "" ;
//                String tableDesc = "" ;
//
//                // 邮件内容增加分表标题
//                if (arraySingleTable.length == 1){
//                    //没有标题，采用默认
//                    tableTitle = "数据报表" ;
//                    tableDesc = arraySingleTable[0] ;
//                } else if (arraySingleTable.length ==2){
//                    tableTitle = arraySingleTable[1] ;
//                    tableDesc = arraySingleTable[0] ;
//                }
//                mailContentTmp.append("<h2>"+ tableTitle +"</h2>") ;
//
//                // 邮件增加数据
//                String[] arryDesc = tableDesc.split("#");
//                if (arryDesc.length == 1) {//直接展示
//                    // 获取邮件内容
//                    String filePath = arryDesc[0];
//                    mailContentTmp.append(getFileContent(filePath) + "<br/>") ;
//                } else if (arryDesc.length == 2) {//表格展示
//                    String filePath = arryDesc[0];
//                    String viewStyle = arryDesc[1];
//                    if (viewStyle.equals("table")) {
//                        mailContentTmp.append(getFileContentForTable(filePath));
//                    } else if (viewStyle.equals("import_crm_vip_user")) {
//                        mailContentTmp.append(importCrmVipUser(filePath));
//                    } else if (viewStyle.equals("import_crm_vip_user_new")) {
//                        mailContentTmp.append(importCrmVipUserNew(filePath));
//                    }
//                }
//
//            }

            content.append("<h1>数据统计</h1><br/>");
            content.append(mailContentTmp.toString());
            content.append("<br/><br/>");
            content.append("<h1> Powered by - 豆果DC数据团队 </h1><br/>");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return content.toString();
    }

    /**
     * 导入达人信息
     *
     * @param fileName
     * @return
     */
    private String importCrmVipUser(String fileName) {
        StringBuffer sbContent = new StringBuffer();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String uid = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((uid = reader.readLine()) != null) {
                // 显示行号
                line++;
                //获取昵称or豆果id
                if (StringUtils.isNotBlank(uid)) {
                    //判断是否真实用户
                    Map<String, Object> dgUser = dgUserService.getUserByUserName(uid);

                    if (dgUser != null) {
                        Integer userId = (Integer) dgUser.get("user_id");
                        String userName = (String) dgUser.get("username");
                        String nickName = (String) dgUser.get("nickname");
                        String headIcon = (String) dgUser.get("headicon");
                        //判断达人库是否已有此用户
                        VIPUser vUser = vipUserService.getVipUserByUID(String.valueOf(userId));
                        if (vUser == null) {
                            VIPUser vipUser = new VIPUser();
                            vipUser.setUserId(userId);
                            vipUser.setUserName(userName);
                            vipUser.setNickName(nickName);
                            vipUser.setHeadIcon(headIcon);
                            vipUser.setCreatetime(DateUtil.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
                            //插入达人
                            vipUserService.insertVipUser(vipUser);
                        }
                    }
                }
            }
            reader.close();
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
        return "导入达人信息完成";
    }

    /**
     * 导入新达人信息
     *
     * @param fileName
     * @return
     */
    private String importCrmVipUserNew(String fileName) {
        StringBuffer sbContent = new StringBuffer();
        String curDate = DateUtil.getSpecifiedDayBefore(DateUtil.date2Str(new Date(), "yyyy-MM-dd"), 1);

        File file = new File(fileName + "_" + curDate + ".log");

        BufferedReader reader = null;
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String strTmp = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((strTmp = reader.readLine()) != null) {
                // 显示行号
                line++;
                //获取昵称or豆果id
                if (StringUtils.isNotBlank(strTmp)) {
                    String[] arryTmp = strTmp.split("\u0001");
                    String uid = "";
                    String tagId = "0";
                    if (arryTmp.length >= 2) {
                        uid = arryTmp[0];
                        tagId = arryTmp[1];
                    }
                    //判断是否真实用户104312622d  ^A
                    Map<String, Object> dgUser = dgUserService.getUserByUserId(uid);

                    if (dgUser != null) {
                        Integer userId = (Integer) dgUser.get("user_id");
                        String userName = (String) dgUser.get("username");
                        String nickName = (String) dgUser.get("nickname");
                        String headIcon = (String) dgUser.get("headicon");
                        //判断达人库是否已有此用户
                        VIPUserNew vUser = vipUserNewService.getVipUserNewByUID(String.valueOf(userId));
                        if (vUser == null) {
                            VIPUserNew vipUserNew = new VIPUserNew();
                            vipUserNew.setUserId(userId);
                            vipUserNew.setUserName(userName);
                            vipUserNew.setNickName(nickName);
                            vipUserNew.setHeadIcon(headIcon);
                            vipUserNew.setTagId(Integer.parseInt(tagId));
                            vipUserNew.setCreatetime(DateUtil.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
                            //插入新达人
                            vipUserNewService.insertVipUserNew(vipUserNew);
                        }
                    }
                }
            }
            reader.close();
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
        return "导入新达人信息完成";
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    private static String getFileContent(String fileName) {
        StringBuffer sbContent = new StringBuffer();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                // System.out.println("line " + line + ": " + tempString);
                line++;
                sbContent.append(tempString + "<br/>");
            }
            reader.close();
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
        return sbContent.toString();
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    private static String getFileContentForTable(String fileName, int day, boolean isFilterDate) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }

        List list = new ArrayList();
        for (int i = 0; i < day; i++) {
            list.add(DateUtil.getNextDay(i));
        }

        StringBuffer sbContent = new StringBuffer();
        File file = new File(fileName);
        BufferedReader reader = null;
        // 构造表格
        sbContent.append("<table class=\"gridtable\">");

        int lineNum = 1;
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                //分隔行
                String[] arryLine = tempString.split(",");
                if (line == 1) {
                    sbContent.append("<tr>");
                    sbContent.append("<th>序号</th>");
                    for (String value : arryLine) {
                        sbContent.append("<th>");
                        sbContent.append(value);
                        sbContent.append("</th>");
                    }
                    sbContent.append("</tr>");
                } else {

                    try {

                        String target = arryLine[0];

                        // 默认需要过滤日期
                        if (isFilterDate) {
                            if (target.length() == 8) {
                                target = target.substring(0, 4) + "-" + target.substring(4, 6) + "-" + target.substring(6, 8);
                            }
                            String temp = target.replaceAll("/", "-");
                            if (!(target.isEmpty()) && isContents(list, temp)) {
                                sbContent.append("<tr>");
                                sbContent.append("<td>" + lineNum + "</td>");
                                for (String value : arryLine) {
                                    sbContent.append("<td>");
                                    sbContent.append(value);
                                    sbContent.append("</td>");
                                }
                                sbContent.append("</tr>");
                                lineNum++;
                            }
                        } else {//配置false，不需要过滤
                            sbContent.append("<tr>");
                            sbContent.append("<td>" + lineNum + "</td>");
                            for (String value : arryLine) {
                                sbContent.append("<td>");
                                sbContent.append(value);
                                sbContent.append("</td>");
                            }
                            sbContent.append("</tr>");
                            lineNum++;
                        }
                    } catch (Exception e) {

                        sbContent.append("<tr>");
                        sbContent.append("<td>" + lineNum + "</td>");
                        for (String value : arryLine) {
                            sbContent.append("<td>");
                            sbContent.append(value);
                            sbContent.append("</td>");
                        }
                        sbContent.append("</tr>");
                        lineNum++;
                    }

                }
                line++;
            }
            reader.close();
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
        sbContent.append("</table>");
        return sbContent.toString();
    }

/*    private static String getFileContentForTable(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }
        StringBuffer sbContent = new StringBuffer();
        File file = new File(fileName);
        BufferedReader reader = null;
        // 构造表格
        sbContent.append("<table class=\"gridtable\">");

        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                //分隔行
                String[] arryLine = tempString.split(",");
                if (line == 1) {
                    sbContent.append("<tr>");
                    sbContent.append("<th>序号</th>");
                    for (String value : arryLine) {
                        sbContent.append("<th>");
                        sbContent.append(value);
                        sbContent.append("</th>");
                    }
                    sbContent.append("</tr>");
                } else {
                    sbContent.append("<tr>");
                    sbContent.append("<td>" + (line - 1) + "</td>");
                    for (String value : arryLine) {
                        sbContent.append("<td>");
                        sbContent.append(value);
                        sbContent.append("</td>");
                    }
                    sbContent.append("</tr>");
                }
                line++;
            }
            reader.close();
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
        sbContent.append("</table>");
        return sbContent.toString();
    }*/

    /**
     * 比较一个日期是否在一个集合里
     *
     * @param list
     * @param src
     * @return
     */
    private static boolean isContents(List list, String src) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            String temp = (String) list.get(i);
            if (DateUtil.compare_date(temp, src)) {
                return true;
            }
        }
        return false;
    }
}