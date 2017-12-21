package com.douguo.dc.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateUtil {

	/**
	 * 获得当前日期
	 *
	 * @return
	 */
	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay, int n) {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - n);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay, int n) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + n);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
	}

	/**
	 * 将字符串转为日期类型
	 * 
	 * @param date
	 * @return
	 */
	public static Date stringToDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	/**
	 * 将字符串转为日期类型
	 * 
	 * @param date
	 * @return
	 */
	public static Date stringToDate(String date,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * 将日期转为字符串
	 * 
	 * @param
	 * @return
	 */
	public static String dateToString(Date date, String format) {
        return date2Str(date,format);
	}

	public static String timestamp2Str(Timestamp time, String format) {
		Date date = null;
		if (null != time) {
			date = new Date(time.getTime());
		}
		return date2Str(date, format);
	}

    public static String date2Str(Date date) {
        return date2Str(date,"yyyy-MM-dd");
    }

	public static String date2Str(Date date, String format) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static int getDays(String startDate, String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		/** 计算起止日期间的天数 */
		int range = 0;
		try {
			long to = sdf.parse(endDate).getTime();
			long from = sdf.parse(startDate).getTime();
			range = (int) ((to - from) / (1000 * 60 * 60 * 24));
		} catch (Exception e) {
			e.printStackTrace();
			range = 0;
		}
		return range;
	}
	
	/**
	 * 计算2个日期的时间差
	 * @author JainfeiZhang
	 * @param sDate1
	 * @param sDate2
	 * @return
	 * @throws ParseException
	 */
    public static int getBetweenDay(String sDate1, String sDate2) throws ParseException {  
    	Date date1 = null;  
    	Date date2 = null;
        SimpleDateFormat formater = new SimpleDateFormat();  
        formater.applyPattern("yyyy-MM-dd");  
        date1 = formater.parse(sDate1);  
        date2 = formater.parse(sDate2);  
        Calendar d1 = new GregorianCalendar();  
        d1.setTime(date1);  
        Calendar d2 = new GregorianCalendar();  
        d2.setTime(date2);  
        int days = d2.get(Calendar.DAY_OF_YEAR)- d1.get(Calendar.DAY_OF_YEAR);  
        int y2 = d2.get(Calendar.YEAR);  
        if (d1.get(Calendar.YEAR) != y2) {  
            do {  
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);  
                d1.add(Calendar.YEAR, 1);  
            } while (d1.get(Calendar.YEAR) != y2);  
        }  
        return days;  
    }  

	
	/**
	 * 通过日期(String) 获取是周几
	 * @author JainfeiZhang
	 * @param sDate
	 * @return
	 */
	public static String getWeekDay(String sDate){
		return getWeekDay(stringToDate(sDate));
	}

	/**
	 * 通过日期(Data) 获取是周几(CH)
	 * @param date
	 * @return
	 */
	public static String getWeekDay(Date date){
		String[] weekDaysName = { "日", "一", "二", "三", "四", "五", "六" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDaysName[intWeek];
	}

	/**
	 * 通过日期(Data) 获取是周几(EN)
	 * @param date
	 * @return
	 */
	public static int getWeekDayEn(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return intWeek;
	}


	/**
	 * 获取当前时间之前或之后几小时 hour
	 * @param hour
	 * @return
	 */
	public static String getTimeByHour(int hour) {

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);

		return new SimpleDateFormat("yyyy-MM-dd HH").format(calendar.getTime());
	}

	/**
	 * 获取当前时间之前或之后几小时 hour
	 * @param hour
	 * @return
	 */
	public static String getTimeByHourInt(int hour) {

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);
		return new SimpleDateFormat("mm:ss").format(calendar.getTime());
	}

	/**
	 *   获取当前时间之前或之后几分钟 minute
	 * @param minute
	 * @return
	 */
	public static String getTimeByMinute(int minute) {

		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.MINUTE, minute);

		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

	}



	/**
	 * 比较日期大小，并返回boolean
	 * @param dateCompareStr
	 * @param dateToCompareStr
     * @return
     */
	public static boolean compareDate(String dateCompareStr, String dateToCompareStr){
		Date dateCompare = stringToDate(dateCompareStr);
		Date dateToCompare = stringToDate(dateToCompareStr);
		if (dateCompare.getTime() > dateToCompare.getTime()){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断两个日期是否相等
	 * @param DATE1
	 * @param DATE2
	 * @return
	 */
	public static boolean compare_date(String DATE1, String DATE2) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				return false;
			} else if (dt1.getTime() < dt2.getTime()) {
				return false;
			} else {
				return true;
			}

	}


	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(String s){
		String res;
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}

    /*
     * 将时间戳转换为指定的时间格式
     * 列如 "yyyy-MM-dd HH:mm:ss"、 "yyyy-MM-dd"
     */
    public static String stampToDesignatedDate(String s, String dataFormat){
        String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dataFormat);
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

	/*
     * 将时间转换为时间戳
     */
	public static String dateToStamp(String s) throws ParseException{
		String res;
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}

	/**
	 * 获得指定日期
	 * @param index
	 * @return
	 */
	public static String getNextDay(int index) {
		Date date=new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -index);
		date = calendar.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");//设置日期格式
		String currentDate=df.format(date);
		return currentDate;
	}

	/**
	 * 判断一个日期是否在一个集合里
	 * @param list
	 * @param src
	 * @return
	 */
	public  static   boolean isContents(List list, String src) {
		try {
			for (int i =0;i<list.size();i++){
				String temp=(String) list.get(i);
				if(DateUtil.compare_date(temp,src)){
					return true;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return false;
	}

}
