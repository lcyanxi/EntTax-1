package com.douguo.dc.mail.common;

public class MailConstants {

    // 邮件类型
    // 自动
    public static final String MAIL_TYPE_TIME_SEND = "MAIL_TYPE_TIME_SEND";

    // 电商统计数据
    public static final String MAIL_TYPE_DG_MALL_SUM = "MAIL_TYPE_DG_MALL_SUM";
    // 电商-资源位统计数据
    public static final String MAIL_TYPE_MALL_POSITION_SUM = "MAIL_TYPE_MALL_POSITION_SUM";
    // 客户端渠道数据
    public static final String MAIL_TYPE_CLIENT_CHANNEL_SUM = "MAIL_TYPE_CLIENT_CHANNEL_SUM";
    // 客户端渠道日监控数据
    public static final String MAIL_TYPE_CLIENT_CHANNEL_MONITOR = "MAIL_TYPE_CLIENT_CHANNEL_MONITOR";
    // 圈子统计数据
    public static final String MAIL_TYPE_GROUP_BASE_SUM = "MAIL_TYPE_GROUP_BASE_SUM";
    // 高铁统计数据-tmp1
    public static final String MAIL_TYPE_GAOTIE_SUM = "MAIL_TYPE_GAOTIE_SUM";
    // 电商-每日时段订单统计
    public static final String MAIL_TYPE_MALL_DAILY_TIME_SUM = "MAIL_TYPE_MALL_DAILY_TIME_SUM";
    //common 临时性质的邮件
    public static final String MAIL_TYPE_COMMON_TIME_SUM = "MAIL_TYPE_COMMON_TIME_SUM";
    // 作品日监控
    public static final String MAIL_TYPE_DISH_BASE_SUM = "MAIL_TYPE_DISH_BASE_SUM";
    // 用户网站行为监测-7DAY
    public static final String MAIL_TYPE_USER_7DAY_ACTIONS_SUM_MONITOR = "MAIL_TYPE_USER_7DAY_ACTIONS_SUM_MONITOR";
    // 用户网站行为监测-DAY
    public static final String MAIL_TYPE_USER_DAY_ACTIONS_SUM_MONITOR = "MAIL_TYPE_USER_DAY_ACTIONS_SUM_MONITOR";
    // 首页焦点图
    public static final String MAIL_TYPE_HOME_FOCUS_MONITOR = "MAIL_TYPE_HOME_FOCUS_MONITOR";
    //菜谱汇总统计
    public static final String MAIL_TYPE_COOK_SUM = "MAIL_TYPE_COOK_SUM";
    //客户端渠道新用户统计
    public static final String MAIL_TYPE_CLIENT_CHANNEL_NEWUSER = "MAIL_TYPE_CLIENT_CHANNEL_NEWUSER" ;
    //Qtype统计
    public static final String MAIL_QTYPE_STAT = "MAIL_QTYPE_STAT" ;
    //家庭邮件报表统计
    public  static final String MAIL_TYPE_FAMILY_MONITER="MAIL_TYPE_FAMILY_MONITER";

    //首页刷新邮件报表
    public static final String MAIL_TYPE_INDEX_FLUSH_STAT="MAIL_TYPE_INDEX_FLUSH_STAT";

    //每小时在线总人数数据监控
    public static final String MAIL_TYPE_SHOW_PERSON_NUM_STAT="MAIL_TYPE_SHOW_PERSON_NUM_STAT";

    //首页推荐数据点击率报表
    public static final String MAIL_TYPE_CLICK_RATE_STAT="MAIL_TYPE_CLICK_RATE_STAT";

    //运营部门工作统计-总计指标
    public static final String MAIL_TYPE_WORKING_OF_OPERATION_DEPARTMENT_STAT = "MAIL_TYPE_WORKING_OF_OPERATION_DEPARTMENT_STAT";
    //运营部门工作统计-基本指标
    public static final String MAIL_TYPE_WORKING_OF_OPERATION_BASE_STAT = "MAIL_TYPE_WORKING_OF_OPERATION_BASE_STAT";
    //运营部门工作统计-互动指标
    public static final String MAIL_TYPE_WORKING_OF_OPERATION_HUDONG_STAT = "MAIL_TYPE_WORKING_OF_OPERATION_HUDONG_STAT";
    //运营部门工作统计-电商指标
    public static final String MAIL_TYPE_WORKING_OF_OPERATION_DIANSHANG_STAT = "MAIL_TYPE_WORKING_OF_OPERATION_DIANSHANG_STAT";
    //运营部门工作统计-周统计指标
    public static final String MAIL_TYPE_WORKING_OF_OPERATION_WEEK_STAT = "MAIL_TYPE_WORKING_OF_OPERATION_WEEK_STAT";
    //首页兴趣标签统计
    public static final String MAIL_TYPE_INDEX_INTERST_TAGS_STAT = "MAIL_TYPE_INDEX_INTERST_TAGS_STAT";
    //豆果课堂_分时段销售数据
    public static final String MAIL_TYPE_LIVE_CLASS_DAILY_TIME_SUM_STAT = "MAIL_TYPE_LIVE_CLASS_DAILY_TIME_SUM_STAT";

    //问答每日数据报表
    public static final String MAIL_TYPE_QUIZE_REPLAY_DAILY_TIME_SUM_STAT = "MAIL_TYPE_QUIZE_REPLAY_DAILY_TIME_SUM_STAT";

    //文章-生活号
    public static final String MAIL_TYPE_ARTICLES_LIFENUM_STAT = "MAIL_TYPE_ARTICLES_LIFENUM_STAT";
    //拼音转换任务
    public static final String MAIL_TYPE_PING_YIN_CONVER_STAT = "MAIL_TYPE_PING_YIN_CONVER_STAT";

    // 邮件发送类型
    public static final String MAIL_SEND_TYPE_HOUR = "hour";// 按小时
    public static final String MAIL_SEND_TYPE_DAY = "day";// 按天
    public static final String MAIL_SEND_TYPE_WEEK = "week";// 按周
    public static final String MAIL_SEND_TYPE_MONTH = "month";// 按月
    public static final String MAIL_SEND_TYPE_CUSTOM = "custom";// 定制
    public static final String MAIL_SEND_TYPE_ABANDONED = "abandoned";// 废弃


    // public static final String MAIL_CHART_PATH = "/Users/zjf/Documents/douguo";
     public static final String MAIL_CHART_PATH="C:/Users/Administrator/Desktop/img";
    //public static final String MAIL_CHART_PATH = "/usr/local/goldmine/tomcat/default/webapps/ROOT/hiveadmin/data";
    //在线人数数据线上地址
    //public static final String MAIL_PERSON_DATA_PATH="D:/personData.txt";
    public static final String MAIL_PERSON_DATA_PATH = "/usr/local/goldmine/tomcat/default/webapps/ROOT/hiveadmin/data/personData.txt";
    public static final int PAGE_LIMIT_50 = 50;
    public static final int PAGE_LIMIT_500 = 500;
}