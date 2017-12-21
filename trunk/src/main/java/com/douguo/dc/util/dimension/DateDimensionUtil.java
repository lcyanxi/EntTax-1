package com.douguo.dc.util.dimension;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateDimensionUtil {

    public static void main(String[] args) throws IOException {

        Date curDate = new Date();
        System.out.println(new Date());
        System.out.println("主键:" + DateDimensionUtil.date2Str(new Date(), "yyyy-MM-dd"));
        System.out.println("===========================================================");
        FileWriter fileWritter = new FileWriter("/Users/zyz/Desktop/dateDimension.log");


        for (int i = -1640; i < 3650; i++) {
            StringBuffer sbDateDesc = new StringBuffer();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, (cal.get(Calendar.DATE) + i));
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);//1月中的某天
            //一周第一天是否为星期天
            boolean isFirstSunday = (cal.getFirstDayOfWeek() == Calendar.SUNDAY);
            //获取周几
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);//1周中的某天
            if (isFirstSunday) {
                dayOfWeek = dayOfWeek - 1;
                if (dayOfWeek == 0) {
                    dayOfWeek = 7;
                }
            }

            //month
            month = month + 1;

            int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);//1年中的某天
            int dayOfWeekInMonth = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);//1个月中的第几周
            int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);//1个月中的第几周

            String strCurDate = getDate1(year, month, dayOfMonth);
            String strCurDateFull = getDate2(year, month, dayOfMonth);
            boolean debug = false;
            if (debug) {
                System.out.println("主键:" + strCurDate);
                System.out.println("主键:" + strCurDateFull);
                System.out.println("年:" + year);
                System.out.println("季度:" + getQuarter(month));
                System.out.println("月:" + month);
                //
                System.out.println("月-full:" + month + "月");
                System.out.println("星期:" + getWeek(dayOfWeek));
                System.out.println("季节:" + getSeason(month));
                System.out.println("节假日:" + getWorkDay(strCurDateFull, dayOfWeek));
                System.out.println("1周中第几天：" + dayOfWeek);
                //
                System.out.println("1月中第几天：" + dayOfMonth);
                System.out.println("1年中第几天：" + dayOfYear);
                System.out.println("1月中第几周：" + dayOfWeekInMonth);
                System.out.println("1年中第几周：" + weekOfYear);
            }
            //
            sbDateDesc.append(strCurDate);//主键
            sbDateDesc.append(",");
            sbDateDesc.append(strCurDateFull);//普通日期
            sbDateDesc.append(",");
            sbDateDesc.append(year);//年
            sbDateDesc.append(",");
            sbDateDesc.append(getQuarter(month)); //季度
            sbDateDesc.append(",");
            sbDateDesc.append(month); //月
            sbDateDesc.append(",");
            //
            sbDateDesc.append(month + "月"); //月-full
            sbDateDesc.append(",");
            sbDateDesc.append(getWeek(dayOfWeek));//星期几
            sbDateDesc.append(",");
            sbDateDesc.append(getSeason(month));//季节
            sbDateDesc.append(",");
            sbDateDesc.append(getWorkDay(strCurDateFull, dayOfWeek));//节假日
            sbDateDesc.append(",");
            sbDateDesc.append(dayOfWeek);//1周中第几天
            sbDateDesc.append(",");
            //
            sbDateDesc.append(dayOfMonth);//1月中第几天
            sbDateDesc.append(",");
            sbDateDesc.append(dayOfYear);//1年中第几天
            sbDateDesc.append(",");
            sbDateDesc.append(dayOfWeekInMonth); //一月中第几周
            sbDateDesc.append(",");
            sbDateDesc.append(weekOfYear);//1年中第几周
            //
//            System.out.println(sbDateDesc.toString());
            fileWritter.write(sbDateDesc.toString()+"\n");
        }
        fileWritter.close();

    }

    /**
     * 季节
     *
     * @param month
     * @return
     */
    private static String getSeason(int month) {
        if (month == 1 || month == 2 || month == 3) {
            return "春";
        } else if (month == 4 || month == 5 || month == 6) {
            return "夏";
        } else if (month == 7 || month == 8 || month == 9) {
            return "秋";
        } else if (month == 10 || month == 11 || month == 12) {
            return "冬";
        } else {
            return "未知";
        }
    }

    /**
     * 季度
     *
     * @param month
     * @return
     */
    private static String getQuarter(int month) {
        if (month == 1 || month == 2 || month == 3) {
            return "一季度";
        } else if (month == 4 || month == 5 || month == 6) {
            return "二季度";
        } else if (month == 7 || month == 8 || month == 9) {
            return "三季度";
        } else if (month == 10 || month == 11 || month == 12) {
            return "四季度";
        } else {
            return "未知季度";
        }
    }

    /**
     * 返回节假日类型
     *
     * @param month
     * @return
     */
    private static String getWorkDay(String curDate, int month) {
        if (month == 1 || month == 2 || month == 3 || month == 4 || month == 5) {
            return "工作日";
        } else {
            return "节假日";
        }
    }

    /**
     * 返回星期
     *
     * @param week
     * @return
     */
    private static String getWeek(int week) {
        if (week == 1) {
            return "星期一";
        } else if (week == 2) {
            return "星期二";
        } else if (week == 3) {
            return "星期三";
        } else if (week == 4) {
            return "星期四";
        } else if (week == 5) {
            return "星期五";
        } else if (week == 6) {
            return "星期六";
        } else if (week == 7) {
            return "星期日";
        } else {
            return "未知";
        }
    }

    private static String getDate1(int year, int month, int day) {
        String strMonth = String.valueOf(month);
        String strDay = String.valueOf(day);
        if (strMonth.length() < 2) {
            strMonth = "0" + strMonth;
        }
        if (strDay.length() < 2) {
            strDay = "0" + strDay;
        }
        return year + strMonth + strDay;
    }

    private static String getDate2(int year, int month, int day) {
        String strMonth = String.valueOf(month);
        String strDay = String.valueOf(day);
        if (strMonth.length() < 2) {
            strMonth = "0" + strMonth;
        }
        if (strDay.length() < 2) {
            strDay = "0" + strDay;
        }
        return year + "-" + strMonth + "-" + strDay;
    }

    public static String date2Str(Date date, String format) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
