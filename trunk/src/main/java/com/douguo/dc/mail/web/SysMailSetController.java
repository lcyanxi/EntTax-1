package com.douguo.dc.mail.web;

import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mail.service.SysMailSetService;
import com.douguo.dc.mail.service.TimeSendMailService;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;
import com.zyz.open.hiveadmin.common.HiveAdminJobConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("/sysmailset")
public class SysMailSetController {

    @Autowired
    private SysMailSetService sysMailSetService;

    @Autowired
    private TimeSendMailService timeSendMailService;

    @RequestMapping(value = "/queryJson")
    public void queryJson(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {

        request.setCharacterEncoding("utf-8");
        String rowsLimit = request.getParameter("rows"); // 取出每一页显示的行数
        String sidx = request.getParameter("sidx"); // 取出排序的项
        String sord = request.getParameter("sord"); // 取出排序方式：升序，降序

        if (rowsLimit == null) // 设置每一页显示行数的默认值
        {
            rowsLimit = "10";
        }
        JQGridUtil t = new JQGridUtil();
        int nPage = 1; // 当前显示的页数
        int total = 1; // 要显示的总的页数，初始值为1

        List<SysMailSet> list = sysMailSetService.queryAll();

        // int records = data.length;
        int records = list.size();

        total = records / Integer.parseInt(rowsLimit) + 1; // 计算总的页数
        String[][] arryFun = new String[list.size()][];

        for (int i = 0; i < list.size(); i++) {
            SysMailSet fun = list.get(i);
            arryFun[i] = new String[]{String.valueOf(fun.getId()), fun.getMailType(),
                    fun.getSubject(), fun.getMailTo(),
                    fun.getSendType(), fun.getSendTime(), fun.getDesc(),
                    "<a href='/sysmailset/preEdit.do?id=" + fun.getId() + "' title='修改' >修改</a>",
                    "<a href='/sysmailset/testMail.do?id=" + fun.getId() + "' title='测试' >测试</a>",
                    "<a href='/sysmailset/resendMail.do?id=" + fun.getId() + "&mailTo=" + fun.getMailTo() + "' title='重发' >重发</a>"};
        }

        // 行数据
        List<Map> rows = new ArrayList<Map>();
        for (String[] axx : arryFun) {
            Map map = new HashMap();
            map.put("id", "1");
            map.put("cell", axx);

            rows.add(map);
        }

        // rows
        GridPager<Map> gridPager = new GridPager<Map>(nPage, total, records, rows); // 将表格显示的配置初始化给GridPager
        t.toJson(gridPager, response); // 发送json数据
    }

    /**
     * 管理页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/preList")
    public String preFun(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        return "/sys_set/sysMailSetManager";
    }

    /**
     * 删除离职人员邮箱
     *
     * @return
     */
    @RequestMapping(value = "/deleteReceiver")
    public String deleteReceiver(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

        String mailToDelete = request.getParameter("mailToDelete").trim();

        List<SysMailSet> list = sysMailSetService.queryAll();
        SysMailSet fun = null;
        //遍历每个功能
        for (int k = 0; k < list.size(); k++) {
            fun = list.get(k);
            String[] mailReceiverFromDb = convertStrToArray((fun.getMailTo()).toString().replaceAll(" ", ""));
            //对于每个功能的每一个邮箱遍历检查
            for (String temp : mailReceiverFromDb) {
                //邮箱值去空
                temp = temp.trim();
                //匹配删除
                if (temp.equals(mailToDelete)) {
                    List<String> tempMailToList = new ArrayList<String>(Arrays.asList(mailReceiverFromDb));
                    tempMailToList.remove(temp);
                    //获取ID号
                    String id = String.valueOf(fun.getId()).trim();
                    //得到去除后的邮箱地址
                    String tempMailReceiver = StringUtils.join(tempMailToList.toArray(), ",");
                    sysMailSetService.updateMailReceiver(tempMailReceiver, id);
                }
            }
        }

        return "/sys_set/sysMailSetManager";
    }

    //邮箱地址拆分
    private static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(",");
        return strArray;
    }

    /**
     * 修改页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/preEdit")
    public String preEdit(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //
        String id = request.getParameter("id");
        SysMailSet sysMailSet = sysMailSetService.getSysMailSet(id);
        model.put("sysMailSet", sysMailSet);
        return "/sys_set/sysMailSetModify";
    }

    @RequestMapping(value = "/insert")
    public String insert(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");

        String mailType = request.getParameter("mailType");
        String subject = request.getParameter("subject");
        String mailTo = request.getParameter("mailTo");
        String sendType = request.getParameter("sendType");
        String sendDate = request.getParameter("sendDate");

        String sendTime_day = request.getParameter("sendTime_day");
        String sendTime_week_part = request.getParameter("sendTime_week");
        String sendTime_month_part = request.getParameter("sendTime_month");
        String sendTime_custom = request.getParameter("sendTime_custom");

        String sendTime_week_exactTime = request.getParameter("sendTime_week_exactTime");
        String sendTime_month_exactTime = request.getParameter("sendTime_month_exactTime");

        // 合并时间单位 周： 2 12:00 ； 月：15 12:00
        String sendTime_week = sendTime_week_part + " " + sendTime_week_exactTime;
        String sendTime_month = sendTime_month_part + " " + sendTime_month_exactTime;

        String sendTime = null;

        if (MailConstants.MAIL_SEND_TYPE_DAY.equals(sendType)) {
            sendTime = sendTime_day;
        } else if (MailConstants.MAIL_SEND_TYPE_WEEK.equals(sendType)) {
            sendTime = sendTime_week;
        } else if (MailConstants.MAIL_SEND_TYPE_MONTH.equals(sendType)) {
            sendTime = sendTime_month;
        } else if (MailConstants.MAIL_SEND_TYPE_CUSTOM.equals(sendType)) {
            sendTime = sendTime_custom;
        }

        String desc = request.getParameter("desc");
        //
        SysMailSet sysMailSet = new SysMailSet();
        sysMailSet.setMailType(mailType);
        sysMailSet.setMailTo(mailTo);
        sysMailSet.setSubject(subject);
        sysMailSet.setSendType(sendType);
        sysMailSet.setSendDate(sendDate);
        sysMailSet.setSendTime(sendTime);
        sysMailSet.setDesc(desc);

        sysMailSetService.insert(sysMailSet);
        return "/sys_set/sysMailSetManager";
    }

    @RequestMapping(value = "/update")
    public String update(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");

        String id = request.getParameter("id");
        String mailType = request.getParameter("mailType");
        String subject = request.getParameter("subject");
        String mailTo = request.getParameter("mailTo");
        String sendType = request.getParameter("sendType");
        String sendDate = request.getParameter("sendDate");

        String sendTime = null;

        String sendTime_day = request.getParameter("sendTime_day");
        String sendTime_week_part = request.getParameter("sendTime_week");
        String sendTime_week_exactTime = request.getParameter("sendTime_week_exactTime");
        String sendTime_month_part = request.getParameter("sendTime_month");
        String sendTime_month_exactTime = request.getParameter("sendTime_month_exactTime");
        String sendTime_custom = request.getParameter("sendTime_custom");

        // 合并时间单位 周： 2 12:00 ； 月：15 12:00
        String sendTime_week = sendTime_week_part + " " + sendTime_week_exactTime;
        String sendTime_month = sendTime_month_part + " " + sendTime_month_exactTime;

        if (MailConstants.MAIL_SEND_TYPE_DAY.equals(sendType)) {
            sendTime = sendTime_day;
        } else if (MailConstants.MAIL_SEND_TYPE_WEEK.equals(sendType)) {
            sendTime = sendTime_week;
        } else if (MailConstants.MAIL_SEND_TYPE_MONTH.equals(sendType)) {
            sendTime = sendTime_month;
        } else if (MailConstants.MAIL_SEND_TYPE_CUSTOM.equals(sendType)) {
            sendTime = sendTime_custom;
        }

        String desc = request.getParameter("desc");
        String mailConfig = request.getParameter("config");

        if (id != null && id.trim().length() > 0) {
            SysMailSet sysMailSet = sysMailSetService.getSysMailSet(id);

            if (sysMailSet != null) {
                sysMailSet.setMailType(mailType);
                sysMailSet.setMailTo(mailTo);
                sysMailSet.setSubject(subject);
                sysMailSet.setSendType(sendType);
                sysMailSet.setSendDate(sendDate);
                if (StringUtils.isNotEmpty(sendTime)) {
                    sysMailSet.setSendTime(sendTime);
                }

                sysMailSet.setDesc(desc);
                sysMailSet.setConfig(mailConfig);

                sysMailSetService.update(sysMailSet);
            }
        }
        return "/sys_set/sysMailSetManager";
    }

    /**
     * 测试能否立即发送邮件
     *
     * @param request
     * @param response
     * @param model
     * @return
     * @author JianfeiZhang
     */
    @RequestMapping(value = "/testMail")
    public String testMail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String id = request.getParameter("id");
        SysMailSet sysMailSet = sysMailSetService.getSysMailSet(id);

        //发送测试人
        sysMailSet.setMailTo(HiveAdminJobConstants.TEST_CHECK_MAIL_LIST);
        timeSendMailService.handleMailType(sysMailSet);
        return "/sys_set/sysMailSetManager";
    }

    /**
     * 测试能否立即发送邮件
             *
             * @param request
        * @param response
        * @param model
        * @return
        * @author JianfeiZhang
        */
@RequestMapping(value = "/resendMail")
public String resendMail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String id = request.getParameter("id");
        SysMailSet sysMailSet = sysMailSetService.getSysMailSet(id);
        String mailTo = request.getParameter("mailTo");
        //发送测试人
        sysMailSet.setMailTo(mailTo);
        timeSendMailService.handleMailType(sysMailSet);
        return "/sys_set/sysMailSetManager";
        }

        }
