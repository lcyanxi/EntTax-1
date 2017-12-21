package com.douguo.dc.mail.service;

import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.mail.service.impl.*;
import com.douguo.dc.mail.service.impl.tmp.CommonMailPinyingService;
import com.douguo.dc.mail.service.impl.tmp.CommonMailStatService;
import com.douguo.dc.mail.service.impl.tmp.MailGaotieStatService;
import com.douguo.dc.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository("timeSendMailService")
public class TimeSendMailService {

    @Autowired
    private MailClientChannelService mailClientChannelService;

    @Autowired
    private MailClientChannelMonitorService mailClientChannelMonitorService;

    @Autowired
    private CommonMailStatService commonMailStatService;

    @Autowired
    private MailGroupStatService mailGroupStatSercie;

    @Autowired
    private MailMallStatService mailMallStatService;

    @Autowired
    private MailGaotieStatService mailGaotieStatService;

    @Autowired
    private MailMallPositionStatService mailMallPositionStatService;

    @Autowired
    private MailMallDailyTimeStatService mailOrderDailyHourStatService;

    @Autowired
    private SysMailSetService sysMailSetService;

    @Autowired
    private  MailDishStatService mailDishStatService;

    @Autowired
    private MailUserAllActionsStatService mailUserAllActionsStatService;

    @Autowired
    private MailUserDayAllActionsStatService mailUserDayAllActionsStatService;

    @Autowired
    private MailHomeFocusStatService mailHomeFocusStatService;

    @Autowired
    private MailCookSumStatService mailCookSumStatService;

    @Autowired
    private MailClientChannelNewUserService mailClientChannelNewUserService ;
    
    @Autowired
    private MailQtypeStatService mailQtypeStatService ;

    @Autowired
    private MailFamilyStatService mailFamilyStatService;

    @Autowired
    private MailIndexFlushStatService mailIndexFlushStatService;

    @Autowired
    private MailShowPersonNum mailShowPersonNum;

    @Autowired
    private MailOperationDepartmentStatService mailOperationDepartmentStatService;

    @Autowired
    private MailOperationBaseStatService mailOperationBaseStatService;

    @Autowired
    private MailOperationHudongStatService mailOperationHudongStatService;

    @Autowired
    private MailOperationDianshangStatService mailOperationDianshangStatService;

    @Autowired
    private MailOperationWeekStatService mailOperationWeekStatService;

    @Autowired
    private MailClickStatService mailClickStatService;

    @Autowired
    private  MailMainIndexInterstTagsService mailMainIndexInterstTagsService;

    @Autowired
    private  MailLiveClassStatService mailLiveClassStatService;


    @Autowired
    private  MailQuizeRplayStatService mailQuizeRplayStatService;

    @Autowired
    private MailLifeNumberStatService mailLifeNumberStatService;
    @Autowired
    private CommonMailPinyingService commonMailPinyingService;


    /**
     * 客户端统计&圈子统计
     */
    public void sendSumMail() {
//        mailGroupStatSercie.sendGroupSumMail();
//        mailClientChannelService.sendClientChannelSumMail();
    }

    public void sendOrderSumMail() {
//        mailMallStatService.sendOrderSumMail();
    }

    public void timeSendMail() {
        List<SysMailSet> list = sysMailSetService.queryTimeMail();
        int curHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int curMinute = Calendar.getInstance().get(Calendar.MINUTE);
        // 获得当前月份日期
        int curDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        //
        String sCurHour = String.valueOf(curHour) ;
        String sCurMinute = String.valueOf(curMinute); ;
        String curTime = null ;

        //格式化时间
        if(curHour<10){
        	sCurHour = "0" + sCurHour ;
        }
        if(curMinute<10){
        	sCurMinute = "0" + sCurMinute ;
        }
        curTime = sCurHour + ":" + sCurMinute ;

        for (SysMailSet sysMailSet : list) {
            String mailType = sysMailSet.getMailType();
            String sendType = sysMailSet.getSendType();

            String sendTime = sysMailSet.getSendTime();
            if (sendTime != null) {
                sendTime = sendTime.trim();
            }

            if (MailConstants.MAIL_SEND_TYPE_DAY.equals(sendType)) {// 按天
                if (curTime.equals(sendTime)) {
                    handleMailType(sysMailSet);
                }
            } else if (MailConstants.MAIL_SEND_TYPE_HOUR.equals(sendType)) {// 按小时
                // 电商日报-小时
                if (String.valueOf(curMinute).equals(sendTime)) {
                    handleMailType(sysMailSet);
                }
            } else if (MailConstants.MAIL_SEND_TYPE_WEEK.equals(sendType)) {// 按周
                String weekDay = DateUtil.getWeekDayEn(new Date()) + " " + curTime ;
                if (weekDay.equals(sendTime)){
                    handleMailType(sysMailSet);
                }

            } else if (MailConstants.MAIL_SEND_TYPE_MONTH.equals(sendType)) {// 按月
                String curMonthDay = curDay + " " + curTime;
                if (curMonthDay.equals(sendTime)){
                    handleMailType(sysMailSet);
                }

            } else if (MailConstants.MAIL_SEND_TYPE_CUSTOM.equals(sendType)){// 定制时间
                String curDate = DateUtil.getDate() + " " + curTime;
                if (curDate.equals(sendTime)){
                    handleMailType(sysMailSet);
                }
            }
        }

    }

    /**
     * 根据邮件类型，发送邮件
     *
     * @param sysMailSet
     */
    public void handleMailType(SysMailSet sysMailSet) {
        String mailType = sysMailSet.getMailType();

        // 高铁投放--临时
        if (MailConstants.MAIL_TYPE_GAOTIE_SUM.equals(mailType)) {
            mailGaotieStatService.sendGaotieStatMail();
        } else if (MailConstants.MAIL_TYPE_MALL_DAILY_TIME_SUM.equals(mailType)) {//小时电商报表
            //mailOrderDailyHourStatService.sendMail();
            mailOrderDailyHourStatService.sendMail(sysMailSet);
        } else if (MailConstants.MAIL_TYPE_COMMON_TIME_SUM.equals(mailType)) {//通用报表
            commonMailStatService.sendMail(sysMailSet);
        } else if (MailConstants.MAIL_TYPE_CLIENT_CHANNEL_SUM.equals(mailType)) {//客户端渠道
            mailClientChannelService.sendClientChannelSumMail(sysMailSet);
        } else if (MailConstants.MAIL_TYPE_CLIENT_CHANNEL_MONITOR.equals(mailType)) {//客户端渠道日监控报表
            mailClientChannelMonitorService.sendMail(sysMailSet);
        } else if (MailConstants.MAIL_TYPE_GROUP_BASE_SUM.equals(mailType)) {//圈子报表
            mailGroupStatSercie.sendGroupSumMail(sysMailSet);
        } else if (MailConstants.MAIL_TYPE_DG_MALL_SUM.equals(mailType)) {//电商报表
            mailMallStatService.sendOrderSumMail(sysMailSet);
        } else if (MailConstants.MAIL_TYPE_MALL_POSITION_SUM.equals(mailType)){//电商资源位报表
            mailMallPositionStatService.sendMail(sysMailSet);
        } else if (MailConstants.MAIL_TYPE_DISH_BASE_SUM.equals(mailType)){//作品日统计
            mailDishStatService.sendDishSumMail(sysMailSet);
        }else if (MailConstants.MAIL_TYPE_USER_7DAY_ACTIONS_SUM_MONITOR.equals(mailType)) {//用户网站行为监测-7DAY
            mailUserAllActionsStatService.sendUserAllActionsStatMail(sysMailSet);
        }else if (MailConstants.MAIL_TYPE_USER_DAY_ACTIONS_SUM_MONITOR.equals(mailType)) {//用户网站行为监测-DAY
            mailUserDayAllActionsStatService.sendUserAllActionsStatMail(sysMailSet);
        }else if (MailConstants.MAIL_TYPE_HOME_FOCUS_MONITOR.equals(mailType)) {//首页焦点图
            mailHomeFocusStatService.sendMail(sysMailSet);
        }else if (MailConstants.MAIL_TYPE_COOK_SUM.equals(mailType)) {//菜谱汇总统计
            mailCookSumStatService.sendMail(sysMailSet);
        }else if (MailConstants.MAIL_TYPE_CLIENT_CHANNEL_NEWUSER.equals(mailType)) {//客户端渠道新用户统计
        	mailClientChannelNewUserService.sendClientChannelSumMail(sysMailSet);
        }else if(MailConstants.MAIL_TYPE_FAMILY_MONITER.equals(mailType)){//家庭报表统计
             mailFamilyStatService.sendFamilySumMail(sysMailSet);
        }else if (MailConstants.MAIL_TYPE_INDEX_FLUSH_STAT.equals(mailType)){//首页类型统计报表
            mailIndexFlushStatService.sendIndexTypeSumMail(sysMailSet);
        }else if (MailConstants.MAIL_QTYPE_STAT.equals(mailType)) {//Qtype统计
        	mailQtypeStatService.sendMail(sysMailSet);
        }else if (MailConstants.MAIL_TYPE_SHOW_PERSON_NUM_STAT.equals(mailType)) {//每小时在线总人数数据监控
           mailShowPersonNum.sendShowPersonNumMail(sysMailSet);
        }
        // 运营操作统计相关
        else if (MailConstants.MAIL_TYPE_WORKING_OF_OPERATION_DEPARTMENT_STAT.equals(mailType)) {//运营工作任务统计-总计
            mailOperationDepartmentStatService.sendMail(sysMailSet);
        }
        else if (MailConstants.MAIL_TYPE_WORKING_OF_OPERATION_BASE_STAT.equals(mailType)) {//运营工作任务统计-基础
            mailOperationBaseStatService.sendMail(sysMailSet);
        }
        else if (MailConstants.MAIL_TYPE_WORKING_OF_OPERATION_HUDONG_STAT.equals(mailType)) {//运营工作任务统计-互动
            mailOperationHudongStatService.sendMail(sysMailSet);
        }
        else if (MailConstants.MAIL_TYPE_WORKING_OF_OPERATION_DIANSHANG_STAT.equals(mailType)) {//运营工作任务统计-电商
            mailOperationDianshangStatService.sendMail(sysMailSet);
        }
        else if (MailConstants.MAIL_TYPE_WORKING_OF_OPERATION_WEEK_STAT.equals(mailType)) {//运营工作任务统计-周统计
            mailOperationWeekStatService.sendMail(sysMailSet);
        }
        // 首页推荐相关
        else if (MailConstants.MAIL_TYPE_CLICK_RATE_STAT.equals(mailType)) {//首页推荐数据点击率
            mailClickStatService.sendClickStatMail(sysMailSet);
        }
        else if (MailConstants.MAIL_TYPE_INDEX_INTERST_TAGS_STAT.equals(mailType)) {//首页兴趣标签
            mailMainIndexInterstTagsService.sendMainIndexInterstTagsMail(sysMailSet);
        }
        else if (MailConstants.MAIL_TYPE_LIVE_CLASS_DAILY_TIME_SUM_STAT.equals(mailType)) {//豆果课堂_分时段销售数据
            mailLiveClassStatService.sendMail(sysMailSet);
        }

        else if (MailConstants.MAIL_TYPE_QUIZE_REPLAY_DAILY_TIME_SUM_STAT.equals(mailType)) {//问答每日数据
            mailQuizeRplayStatService.sendMail(sysMailSet);
        }
        // 文章相关-生活号
        else if (MailConstants.MAIL_TYPE_ARTICLES_LIFENUM_STAT.equals(mailType)) {//豆果课堂_分时段销售数据
            mailLifeNumberStatService.sendMail(sysMailSet);
        }
        // 拼音转换任务
        else if (MailConstants.  MAIL_TYPE_PING_YIN_CONVER_STAT.equals(mailType)) {//豆果课堂_分时段销售数据
            commonMailPinyingService.sendMail(sysMailSet);
        }
    }
}