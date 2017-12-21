package com.douguo.dc.mail.service.impl.tmp;

import com.alibaba.fastjson.JSONObject;
import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JavaSendMail;
import com.douguo.dc.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

import static com.douguo.dc.util.PinyinUtil.ToFirstChar;
import static com.douguo.dc.util.PinyinUtil.ToPinyin;

@Repository("commonMailPinyingService")
public class CommonMailPinyingService {
    public void sendMail(SysMailSet sysMailSet) {
        if (sysMailSet != null) {
            String strConfig = StringUtils.trimToEmpty(sysMailSet.getConfig());
            JSONObject jsonConfig = JsonUtil.parseStrToJsonObj(strConfig);

            StringBuffer content = new StringBuffer("");

            String curDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
            String subject = sysMailSet.getSubject() + "_" + curDate;
            String to = sysMailSet.getMailTo();
            try {
                content.append("<style type=\"text/css\"> table.gridtable { font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse; } table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #dedede; } table.gridtable td { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff; } </style>");
                if (org.apache.commons.lang3.StringUtils.isNotBlank(jsonConfig.getString("local"))) {
                    content.append("拼音联想词转换成功");
                    content.append("数据生成地址：" + pingyinBuild(jsonConfig));
                } else {
                    content.append("请输入要转换的数据地址");
                }
                JavaSendMail.sendHtmlEmail(subject, to, content.toString());
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

    public String pingyinBuild(JSONObject jsonConfig) {
        String local = (String) jsonConfig.get("local");
        String toLocal=(String) jsonConfig.get("toLocal");
        if (!org.apache.commons.lang3.StringUtils.isNotBlank(jsonConfig.getString("toLocal"))){
            toLocal= MailConstants.MAIL_CHART_PATH+"/result_conver_pinyin.log";
        }
        File file = new File(local);
        List<Map> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                Map map = new HashMap();
                String[] strArray = s.split(","); //拆分字符为"," ,然后把结果交给数组strArray
                String str = strArray[0].replaceAll("\\s*", "");
                map.put(str, strArray[1]);
                list.add(map);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return converKeyword(list,toLocal);
    }

    private String converKeyword(List<Map> tempList,String tolocal) {
        File file = new File(tolocal);
        FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(file);
            writer = new BufferedWriter(fw);

            for (Map map : tempList) {
                Iterator iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    String newStr = new String(key.getBytes(), "UTF-8");
                    String pinying = ToPinyin(newStr);
                    String firstChar = ToFirstChar(newStr);
                    writer.write(key + "," + value + "," + pinying + "," + firstChar);
                    writer.newLine();//换行
                }
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tolocal;
    }

}
