package com.douguo.dc.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataFillZeroUtil {

    /**
     * 解决查出的数据某一天没有数据  用0填充
     * @param map
     * @return
     */
    public static List<Double> converList(Map map, int size){
        List list=new ArrayList();
        if (map.isEmpty()){  //如果查出的集合为空，将数据全部用0填充
            for (int i=0;i<size;i++){
                list.add(0.0);
            }
        }else {
            int index=1;
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key =(String) entry.getKey();
                boolean tempDate= compare_date(key,getNextDay(index));//比较日期是否相等
                if (tempDate){
                    list.add(entry.getValue());
                    index++;
                }else {
                    while (!tempDate){//防止连续几天都没有数据的情况
                        list.add(0.0);
                        index++;
                        tempDate= compare_date(key,getNextDay(index));
                    }
                    list.add(entry.getValue());
                    index++;
                }
            }

            boolean tempDate= compare_date(getNextDay(size),getNextDay(index));//假如我要7天的数据，查出只有前4天的数据， 后面三天用0填充
            for (;!tempDate;){
                list.add(0.0);
                index++;
                tempDate= compare_date(getNextDay(size),getNextDay(index));
            }
        }
        Collections.reverse(list);
        return list;
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
     * 判断两个日期是否相等
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static boolean compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return false;
            } else if (dt1.getTime() < dt2.getTime()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception exception) {
            System.out.println("日期转换错误");
            exception.printStackTrace();
        }
        return false;
    }


}
