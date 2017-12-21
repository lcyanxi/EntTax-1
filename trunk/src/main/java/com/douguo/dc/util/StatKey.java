package com.douguo.dc.util;

public class StatKey {
	
	public final static String SessionCount="1";  //应用程序启动次数
	public final static String FailureCount="2";  //应用程序出错次数
	public final static String UserCount="3";  //使用用户数
	public final static String NewUserCount="4";  //新增用户数
	public final static String MeanSessionTime="5";  //应用使用时长
	public final static String LandscapeRatio="6";  //----垃圾数据-----
	public final static String MeanUploadTraffic="7";  //
	public final static String MeanDownloadTraffic="8";  //
	public final static String LastAccessUserCountJson="9";  //
	public final static String UserSessionCountJson="10";  //
	public final static String UserAccessDaysJson="11";  //
	public final static String UserFailureCountJson="12";  //
	public final static String LandscapeL="13";  //
	public final static String LandscapeP="14";  //
	public final static String PointCount="15";  //
	public final static String MeanTime="16";  //
	public final static String RedirectInfoJson="17";  //
	//用户-新用户-留存
	public final static String UserRetentionJson="18";  //
	public final static String SessionLengthJson="19";  //使用时长分布
	public final static String SessionJson="20";  //启动次数分布
	public final static String TerminalDevicesUV="21";
	public final static String TerminalDevicesPV="22";
	//电商-订单用户-周留存
	public final static String MallOrderUserWeekRetention="28";
	//电商-成交用户-周留存
	public final static String MallPayUserWeekRetention="29";
	
	//电商-订单-周新用户
	public final static String MallNewUserWeekOrder="34";
	//电商-成交单-周新用户
	public final static String MallNewUserWeekPay="35";

	//用户-新用户-月留存
	public final static String NewUserMonthRetention="38";

	//用户-月活跃用户-月留存
	public final static String ActiveUserMonthRetention="39";

	//用户-新用户-原始渠道-留存
	public final static String NewUserOriginalChannelRetention="17";

}
