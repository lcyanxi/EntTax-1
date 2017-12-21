package com.douguo.dc.service;

import com.douguo.dc.dao.TrendsDao;
import com.douguo.dc.model.RegisterUserModel;
import com.douguo.dc.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository("trendsService")
public class TrendsService {

    @Autowired
    private TrendsDao trendsDao;

    /**
     * 获取version_dic数据
     *
     * @param app_id
     * @return 版本json
     */
    public String getVersions(String app_id) {
        String format = "{\"total\":%s,\"datas\":[%s],\"result\":\"success\"}";
        List<Map<String, Object>> rowlst = this.trendsDao.queryVersions(app_id);
        if (null == rowlst) {
            String str = String.format(format, "0", "");
            return str;
        }

        int total = 0;
        StringBuilder datas = new StringBuilder();
        String formatData = "{\"is_shown\":true,\"name\":\"%s\",\"id\":\"%s\"}";
        for (Map<String, Object> map : rowlst) {
            String app_version = String.valueOf(map.get("app_version")).trim();
            if (null == app_version || app_version.trim().equals("")) {
                continue;
            }
            String str = String.format(formatData, app_version, app_version);
            datas.append(str).append(",");
            total++;
        }
        String datasStr = " ";
        if (0 != datas.length()) {
            datasStr = datas.substring(0, datas.length() - 1);
        }
        String ret = String.format(format, total, datasStr);
        return ret;
    }

    /**
     * 获取qudao_dic数据
     *
     * @return 渠道数据json
     */
    public String getChannels() {
        List<Map<String, Object>> rowlst = this.trendsDao.queryChannels();
        String format = "{\"total\":%s,\"data_groups\":[],\"datas\":[%s],\"result\":\"success\"}";
        if (null == rowlst) {
            String str = String.format(format, "0", "");
            return str;
        }

        int total = 0;
        StringBuilder datas = new StringBuilder();
        String formatData = "{\"is_shown\":true,\"name\":\"%s\",\"id\":\"%s\"}";
        for (Map<String, Object> map : rowlst) {
            String name = String.valueOf(map.get("channel_name")).trim();
            String id = String.valueOf(map.get("channel_code")).trim();
            if (null == name || null == id || id.trim().equals("")) {
                continue;
            }
            String str = String.format(formatData, name, id);
            datas.append(str).append(",");
            total++;
        }
        String datasStr = " ";
        if (0 != datas.length()) {
            datasStr = datas.substring(0, datas.length() - 1);
        }

        String ret = String.format(format, total, datasStr);
        return ret;
    }

    /**
     * 获取用户留存率数据
     *
     * @param appId
     * @param endDate
     * @param startDate
     * @param page
     * @param time_unit
     * @param dimension_type
     * @param dimension_name
     * @return 用户留存率json数据
     */
    public String getUserRetentions(String appId, String endDate, String startDate, String page, String time_unit, String time_type, String dimension_type, String dimension_name) {
        int range = this.getDays(startDate, endDate);
        if (range < 0) {
            return null;
        }
        int totalStr = range + 1;

        String format = "{\"total\":%s,\"stats\":[%s],\"result\":\"success\"}";
        String formatStat = "{\"install_period\":\"%s\",\"retention_rate\":[%s],\"total_install\":%s}";
        StringBuilder rates = new StringBuilder();
        for (int i = 0; i <= range; i++) {
            Calendar ss = Calendar.getInstance();
            ss.setTime(DateUtil.stringToDate(startDate));
            ss.add(Calendar.DAY_OF_YEAR, i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String stattime = sdf.format(ss.getTime());

            /** 获取新增用户数 */
            String total = this.trendsDao.getDayNewUserCount(appId, "4", stattime, time_type, dimension_type, dimension_name);
            List<Map<String, Object>> rowlst = this.trendsDao.queryRetentions(appId, "18", stattime, dimension_type, dimension_name, page, time_unit);
            if (null == rowlst) {
                String str = String.format(formatStat, stattime, "", "");
                rates.append(str).append(",");
                continue;
            }

            StringBuilder values = new StringBuilder();
            for (Map<String, Object> map : rowlst) {
                String sv = String.valueOf(map.get("stat_value"));
                if (null == sv || sv.trim().equals("")) {
                    continue;
                }
                sv = sv.replaceAll("\n", "");
                String s_v = this.getPercentage(sv, total, 2);
                if (null == s_v || s_v.trim().equals("")) {
                    continue;
                }
                values.append(s_v).append(",");
            }
            String temp = " ";
            if (0 != values.length()) {
                temp = values.substring(0, values.length() - 1);
            }

            String rateStr = String.format(formatStat, stattime, temp, total);
            rates.append(rateStr).append(",");
        }
        String retention_rate = " ";
        if (0 != rates.length()) {
            retention_rate = rates.substring(0, rates.length() - 1);
        }

        String ret = String.format(format, totalStr, retention_rate);
        return ret;
    }


    /**
     * 获取用户留存率数据
     *
     * @param appId
     * @param endDate
     * @param startDate
     * @param page
     * @param time_unit
     * @param dimension_type
     * @param dimension_name
     * @return 用户留存率json数据
     */


    public String getUserRetentionsMonth(String appId, String endDate, String startDate, String page, String time_unit, String time_type, String dimension_type, String dimension_name) {

        int range = this.getMonths(startDate, endDate);
        if (range < 0) {
            return null;
        }
        int totalStr = range + 1;

        String format = "{\"total\":%s,\"stats\":[%s],\"result\":\"success\"}";
        String formatStat = "{\"install_period\":\"%s\",\"retention_rate\":[%s],\"total_install\":%s}";
        StringBuilder rates = new StringBuilder();
        for (int i = 0; i <= range; i++) {
            Calendar ss = Calendar.getInstance();
            ss.setTime(DateUtil.stringToDate(startDate));
            ss.add(Calendar.MONTH, i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String stattime = sdf.format(ss.getTime());

            /** 获取新增用户数 */
            String total = this.trendsDao.getDayNewUserCountMonth(appId, "4", stattime, time_type, dimension_type, dimension_name);
            List<Map<String, Object>> rowlst = this.trendsDao.queryRetentionsMonth(appId, "38", stattime, dimension_type, dimension_name, page, time_unit);
            if (null == rowlst) {
                String str = String.format(formatStat, stattime, "", "");
                rates.append(str).append(",");
                continue;
            }

            StringBuilder values = new StringBuilder();
            for (Map<String, Object> map : rowlst) {
                String sv = String.valueOf(map.get("stat_value"));
                if (null == sv || sv.trim().equals("")) {
                    continue;
                }
                sv = sv.replaceAll("\n", "");
                String s_v = this.getPercentage(sv, total, 2);
                if (null == s_v || s_v.trim().equals("")) {
                    continue;
                }
                values.append(s_v).append(",");
            }
            String temp = " ";
            if (0 != values.length()) {
                temp = values.substring(0, values.length() - 1);
            }

            String rateStr = String.format(formatStat, stattime, temp, total);
            rates.append(rateStr).append(",");
        }
        String retention_rate = " ";
        if (0 != rates.length()) {
            retention_rate = rates.substring(0, rates.length() - 1);
        }

        String ret = String.format(format, totalStr, retention_rate);
        return ret;
    }

    /**
     * 获取新增用户趋势
     *
     * @param appId
     //* @param statKeyId
     * @param endDate
     * @param startDate
     * @param time_unit
     * @param dimension_type
     * @param dimension_name
     * @return 新增用户趋势json
     */
    public String getNewUserTrends(String appId, String is_compared, String endDate, String startDate, String time_unit, String time_type, String dimension_type, String dimension_name) {
        int range = this.getDays(startDate, endDate);
        if (range < 0) {
            return null;
        }

        String formatStr = "{\"stats\":[{\"data\":[%s],\"name\":\"%s\"}],\"is_compared\":%s,\"dates\":[%s],\"result\":\"success\"}";
        String name = startDate + "至" + endDate;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mmdd = new SimpleDateFormat("MM-dd");
        Calendar ss = Calendar.getInstance();
        Date sd = DateUtil.stringToDate(startDate);

        StringBuilder data = new StringBuilder();
        StringBuilder dates = new StringBuilder();
        for (int i = 0; i <= range; i++) {
            ss.setTime(sd);
            ss.add(Calendar.DAY_OF_YEAR, i);

            String stattime = sdf.format(ss.getTime());
            String dateStr = mmdd.format(ss.getTime());

            /** 获取新增用户数 */
            String total = this.trendsDao.getDayNewUserCount(appId, "4", stattime, time_type, dimension_type, dimension_name);

            data.append(total).append(",");
            dates.append("\"" + dateStr + "\"").append(",");
        }
        String data_rate = " ";
        if (0 != data.length()) {
            data_rate = data.substring(0, data.length() - 1);
        }
        String dates_rate = " ";
        if (0 != dates.length()) {
            dates_rate = dates.substring(0, dates.length() - 1);
        }

        String ret = String.format(formatStr, data_rate, name, is_compared, dates_rate);
        return ret;
    }

    /**
     * 获取新增用户趋势 - week
     *
     * @param appId
     * @param endDate
     * @param startDate
     * @param time_unit
     * @param time_type
     * @param dimension_type
     * @param dimension_name
     * @return
     */
    public String getNewUserTrendsWeek(String appId, String endDate, String startDate, String time_unit, String time_type, String dimension_type, String dimension_name) {
        
    	String formatStr = "{\"stats\":[{\"data\":[%s],\"name\":\"%s\"}],\"is_compared\":false,\"dates\":[%s],\"result\":\"success\"}";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c_begin = Calendar.getInstance();
        Calendar c_end = Calendar.getInstance();
        try {
            c_begin.setTime(format.parse(startDate));
            c_end.setTime(format.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat mm_dd = new SimpleDateFormat("MM-dd");
        String sStr = "";
        String eStr = "";
        StringBuilder data = new StringBuilder();
        StringBuilder dates = new StringBuilder();
        Calendar temp = Calendar.getInstance();
        while (c_begin.before(c_end)) {
            if (c_begin.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                String stime = format.format(c_begin.getTime());

                temp.setTime(c_begin.getTime());
                temp.add(Calendar.DAY_OF_YEAR, 6);
                String etime = format.format(temp.getTime());
                if (sStr.equals("")) {
                    sStr = format.format(c_begin.getTime());
                }
                eStr = format.format(c_begin.getTime());
                String total = this.trendsDao.getPeriodNewUserCount(appId, "4", stime, etime, time_type, dimension_type, dimension_name);
                try {
                    data.append(total).append(",");
                    String dateStr = mm_dd.format(format.parse(eStr));
                    dates.append("\"" + dateStr + "\"").append(",");
                } catch (ParseException e) {
                }
            }
            c_begin.add(Calendar.DAY_OF_YEAR, 1);
        }
        String name = "";
        try {
            String s = mm_dd.format(format.parse(sStr));
            String e = mm_dd.format(format.parse(eStr));
            name = s + "至" + e;
        } catch (ParseException e) {
            name = sStr + "至" + eStr;
        }

        String data_rate = " ";
        if (0 != data.length()) {
            data_rate = data.substring(0, data.length() - 1);
        }
        String dates_rate = " ";
        if (0 != dates.length()) {
            dates_rate = dates.substring(0, dates.length() - 1);
        }

        String ret = String.format(formatStr, data_rate, name, dates_rate);
        
        System.out.println("ret:======================="+ret);
        
        return ret;
    }

    /**
     * 获取新增用户趋势 - month
     *
     * @param appId
     * @param endDate
     * @param startDate
     * @param time_unit
     * @param time_type
     * @param dimension_type
     * @param dimension_name
     * @return
     */
    public String getNewUserTrendsMonth(String appId, String endDate, String startDate, String time_unit, String time_type, String dimension_type, String dimension_name) {
        String formatStr = "{\"is_compared\":false,\"stats\":[{\"data\":[%s],\"name\":\"%s\"}],\"dates\":[%s],\"result\":\"success\"}";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c_begin = Calendar.getInstance();
        Calendar c_end = Calendar.getInstance();
        try {
            c_begin.setTime(format.parse(startDate));
            c_end.setTime(format.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat mm_dd = new SimpleDateFormat("MM-dd");
        String sStr = "";
        String eStr = "";
        StringBuilder data = new StringBuilder();
        StringBuilder dates = new StringBuilder();
        Calendar temp = Calendar.getInstance();
        while (c_begin.before(c_end)) {
            if (c_begin.get(Calendar.DAY_OF_MONTH) == 1) {
                if (sStr.equals("")) {
                    sStr = format.format(c_begin.getTime());
                }
                String stime = format.format(c_begin.getTime());
                c_begin.set(Calendar.DAY_OF_MONTH, c_begin.getActualMaximum(Calendar.DAY_OF_MONTH));
                String etime = format.format(temp.getTime());
                eStr = format.format(c_begin.getTime());

                String total = this.trendsDao.getPeriodNewUserCount(appId, "4", stime, etime, time_type, dimension_type, dimension_name);
                try {
                    data.append(total).append(",");
                    String dateStr = mm_dd.format(format.parse(stime));
                    dates.append("\"" + dateStr + "\"").append(",");
                } catch (ParseException e) {
                }
                c_begin.add(Calendar.DAY_OF_YEAR, 1);
            } else {
                c_begin.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
        String name = "";
        try {
            String s = mm_dd.format(format.parse(sStr));
            String e = mm_dd.format(format.parse(eStr));
            name = s + "至" + e;
        } catch (ParseException e) {
            name = sStr + "至" + eStr;
        }

        String data_rate = " ";
        if (0 != data.length()) {
            data_rate = data.substring(0, data.length() - 1);
        }
        String dates_rate = " ";
        if (0 != dates.length()) {
            dates_rate = dates.substring(0, dates.length() - 1);
        }

        String ret = String.format(formatStr, data_rate, name, dates_rate);
        return ret;
    }

    /**
     * 新增用户明细
     *
     * @param appId
     * @param endDate
     * @param startDate
     * @param page
     * @param time_unit
     * @param dimension_type
     * @param dimension_name
     * @return 新增用户明细json
     */
    public String getNewUserDetails(String appId, String endDate, String startDate, String page, String time_unit, String time_type, String dimension_type, String dimension_name) {
        if (null == page || page.trim().equals("")) {
            page = "1";
        }

        int range = this.getDays(startDate, endDate);
        if (range < 0) {
            return null;
        }

        String formatStr = "{\"total\":%s,\"stats\":[%s],\"result\":\"success\"}";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mmdd = new SimpleDateFormat("MM-dd");
        Calendar ss = Calendar.getInstance();
        Date sd = DateUtil.stringToDate(startDate);

        String total = String.valueOf(range + 1);
        StringBuilder data = new StringBuilder();
        String formatData = "{\"data\":\"%s\",\"rate\":\"%s %%\",\"date\":\"%s\"}";

        /** 分页 */
        int PAGE_SIZE = 500;
        int pageNum = Integer.valueOf(page);
        int i_init_start = range - (pageNum - 1) * PAGE_SIZE;
        int i_init_end = 0;
        if (i_init_start > PAGE_SIZE) {
            i_init_end = range - pageNum * PAGE_SIZE + 1;
        }

        for (int i = i_init_start; i >= i_init_end; i--) {
            ss.setTime(sd);
            ss.add(Calendar.DAY_OF_YEAR, i);
            String stattime = sdf.format(ss.getTime());
            String dateStr = mmdd.format(ss.getTime());

            /** 获取新增用户数 */
            String newUsersTotal = this.trendsDao.getDayNewUserCount(appId, "4", stattime, time_type, dimension_type, dimension_name);
            /** 获取活跃用户数 */
            String activeUsers = this.trendsDao.getDayActiveUserCount(appId, "3", stattime, time_type, dimension_type, dimension_name);
            String percentage = this.getPercentage(newUsersTotal, activeUsers, 2);
            if (null == percentage || percentage.trim().equals("")) {
                percentage = "0";
            }

            //String dataStr = String.format(formatData, newUsersTotal + "/" + activeUsers, percentage, dateStr);
            // 先屏蔽活跃用户
            String dataStr = String.format(formatData, newUsersTotal, percentage, dateStr);
            data.append(dataStr).append(",");
        }
        String data_rate = " ";
        if (0 != data.length()) {
            data_rate = data.substring(0, data.length() - 1);
        }

        String ret = String.format(formatStr, total, data_rate);
        return ret;
    }

    /**
     * 新增用户明细 - week
     *
     * @param appId
     * @param endDate
     * @param startDate
     * @param page
     * @param time_unit
     * @param time_type
     * @param dimension_type
     * @param dimension_name
     * @return
     */
    public String getNewUserDetailsWeek(String appId, String endDate, String startDate, String page, String time_unit, String time_type, String dimension_type, String dimension_name) {
        String formatStr = "{\"total\":%s,\"stats\":[%s],\"result\":\"success\"}";
        String formatData = "{\"data\":%s,\"date\":\"%s\",\"rate\":\"%s %%\"}";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c_begin = Calendar.getInstance();
        Calendar c_end = Calendar.getInstance();
        Calendar c_count = Calendar.getInstance();
        try {
            c_begin.setTime(format.parse(startDate));
            c_end.setTime(format.parse(endDate));
            c_count.setTime(format.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        /** 根据起止时间算出有多少周，即多少条数据 */
        int count = 0;
        while (c_count.after(c_begin)) {
            if (c_count.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                count++;
                c_count.add(Calendar.DAY_OF_YEAR, -7);
            } else {
                c_count.add(Calendar.DAY_OF_YEAR, -1);
            }
        }

        /** 分页 */
        if (null == page || page.trim().equals("")) {
            page = "1";
        }
        int pageNum = Integer.valueOf(page);
        int page2show = count - (pageNum - 1) * 20;

        SimpleDateFormat mm_dd = new SimpleDateFormat("MM-dd");
        StringBuilder data = new StringBuilder();
        Calendar temp = Calendar.getInstance();
        int index = count;
        int p = 1;
        while (c_end.after(c_begin)) {
            if (c_end.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                /** 只查询20条数据 */
                if (p > 20) {
                    break;
                }
                /** 不在当前显示周内，直接跳过 */
                if (index > page2show) {
                    index--;
                    c_end.add(Calendar.DAY_OF_YEAR, -7);
                    continue;
                }
                String stime = format.format(c_end.getTime());

                temp.setTime(c_end.getTime());
                temp.add(Calendar.DAY_OF_YEAR, 6);
                String etime = format.format(temp.getTime());

                /** 获取新增用户总数 */
                String newUsersTotal = this.trendsDao.getPeriodNewUserCount(appId, "4", stime, etime, time_type, dimension_type, dimension_name);
                
                /** 获取活跃用户数 */
                String activeUsers = this.trendsDao.getPeriodActiveUserCount(appId, "3", stime, etime, time_type, dimension_type, dimension_name);
                String percentage = this.getPercentage(newUsersTotal, activeUsers, 2);
                if (null == percentage || percentage.trim().equals("")) {
                    percentage = "0";
                }
                try {
                    String dateStrS = mm_dd.format(format.parse(stime));
                    String dateStrE = mm_dd.format(format.parse(etime));

                    String dataStr = String.format(formatData, "\"" + newUsersTotal + "/" + activeUsers + "\"", dateStrS + " ~ " + dateStrE, percentage);
                    data.append(dataStr).append(",");
                } catch (ParseException e) {
                }
                p++;
                c_end.add(Calendar.DAY_OF_YEAR, -7);
            } else {
                c_end.add(Calendar.DAY_OF_YEAR, -1);
            }
        }

        String data_rate = " ";
        if (0 != data.length()) {
            data_rate = data.substring(0, data.length() - 1);
        }

        String ret = String.format(formatStr, count, data_rate);
        return ret;
    }

    /**
     * 新增用户明细 - month
     *
     * @param appId
     * @param endDate
     * @param startDate
     * @param page
     * @param time_unit
     * @param time_type
     * @param dimension_type
     * @param dimension_name
     * @return
     */
    public String getNewUserDetailsMonth(String appId, String endDate, String startDate, String page, String time_unit, String time_type, String dimension_type, String dimension_name) {
        String formatStr = "{\"total\":%s,\"stats\":[%s],\"result\":\"success\"}";
        String formatData = "{\"data\":%s,\"date\":\"%s\",\"rate\":\"%s %%\"}";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c_begin = Calendar.getInstance();
        Calendar c_end = Calendar.getInstance();
        Calendar c_count = Calendar.getInstance();
        try {
            c_begin.setTime(format.parse(startDate));
            c_end.setTime(format.parse(endDate));
            c_count.setTime(format.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        /** 根据起止时间算出有多少月，即多少条数据 */
        int count = 0;
        while (c_count.after(c_begin)) {
            if (c_count.get(Calendar.DAY_OF_MONTH) == 1) {
                count++;
                c_count.add(Calendar.MONTH, -1);
            } else {
                c_count.add(Calendar.DAY_OF_YEAR, -1);
            }
        }

        /** 分页 */
        if (null == page || page.trim().equals("")) {
            page = "1";
        }
        int pageNum = Integer.valueOf(page);
        int page2show = count - (pageNum - 1) * 20;

        SimpleDateFormat mm_dd = new SimpleDateFormat("MM-dd");
        StringBuilder data = new StringBuilder();
        Calendar temp = Calendar.getInstance();
        int index = count;
        int p = 1;
        while (c_end.after(c_begin)) {
            if (c_end.get(Calendar.DAY_OF_MONTH) == 1) {
                /** 只查询20条数据 */
                if (p > 20) {
                    break;
                }
                /** 不在当前显示周内，直接跳过 */
                if (index > page2show) {
                    index--;
                    c_end.add(Calendar.MONTH, -1);
                    continue;
                }
                String stime = format.format(c_end.getTime());

                temp.setTime(c_end.getTime());
                temp.set(Calendar.DAY_OF_MONTH, c_end.getActualMaximum(Calendar.DAY_OF_MONTH));
                String etime = format.format(temp.getTime());

                /** 获取新增用户总数 */
                String newUsersTotal = this.trendsDao.getPeriodNewUserCount(appId, "4", stime, etime, time_type, dimension_type, dimension_name);
                /** 获取活跃用户数 */
                String activeUsers = this.trendsDao.getPeriodActiveUserCount(appId, "3", stime, etime, time_type, dimension_type, dimension_name);
                String percentage = this.getPercentage(newUsersTotal, activeUsers, 2);
                if (null == percentage || percentage.trim().equals("")) {
                    percentage = "--";
                }
                try {
                    String dateStrS = mm_dd.format(format.parse(stime));
                    // String dateStrE = mm_dd.format(format.parse(etime));
                    String dataStr = String.format(formatData, "\"" + newUsersTotal + "/" + activeUsers + "\"", dateStrS, percentage);
                    data.append(dataStr).append(",");
                } catch (ParseException e) {
                }
                p++;
                c_end.add(Calendar.MONTH, -1);
            } else {
                c_end.add(Calendar.DAY_OF_YEAR, -1);
            }
        }

        String data_rate = " ";
        if (0 != data.length()) {
            data_rate = data.substring(0, data.length() - 1);
        }

        String ret = String.format(formatStr, count, data_rate);
        return ret;
    }

    /**
     * 获取活跃用户趋势数据
     *
     * @param appId
     * @param endDate
     * @param startDate
     * @param time_unit
     * @param dimension_type
     * @param dimension_name
     * @return 活跃用户json
     */
    public String getActiveUserTrends(String appId, String is_compared, String endDate, String startDate, String time_unit, String time_type, String dimension_type, String dimension_name) {
        String statKeyId = "3";

        int range = this.getDays(startDate, endDate);
        if (range < 0) {
            return null;
        }

        String formatStr = "{\"stats\":[{\"data\":[%s],\"name\":\"%s\"}],\"is_compared\":%s,\"dates\":[%s],\"result\":\"success\"}";
        String name = startDate + "至" + endDate;

        StringBuilder data = new StringBuilder();
        StringBuilder dates = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mmdd = new SimpleDateFormat("MM-dd");
        Calendar ss = Calendar.getInstance();
        Date sd = DateUtil.stringToDate(startDate);
        for (int i = 0; i <= range; i++) {
            ss.setTime(sd);
            ss.add(Calendar.DAY_OF_YEAR, i);

            String stattime = sdf.format(ss.getTime());
            String dateStr = mmdd.format(ss.getTime());

            /** 获取活跃用户数 */
            String total = this.trendsDao.getDayActiveUserCount(appId, statKeyId, stattime, time_type, dimension_type, dimension_name);

            data.append(total).append(",");
            dates.append("\"" + dateStr + "\"").append(",");
        }
        String data_rate = " ";
        if (0 != data.length()) {
            data_rate = data.substring(0, data.length() - 1);
        }
        String dates_rate = " ";
        if (0 != dates.length()) {
            dates_rate = dates.substring(0, dates.length() - 1);
        }

        String ret = String.format(formatStr, data_rate, name, is_compared, dates_rate);
        return ret;
    }

    /**
     * 获取启动次数趋势数据
     *
     * @param appId
     * @param endDate
     * @param startDate
     * @param time_unit
     * @param dimension_type
     * @param dimension_name
     * @return 启动次数json
     */
    public String getLaunchesTrends(String appId, String is_compared, String endDate, String startDate, String time_unit, String time_type, String dimension_type, String dimension_name) {
        String statKeyId = "1";

        int range = this.getDays(startDate, endDate);
        if (range < 0) {
            return null;
        }

        String formatStr = "{\"stats\":[{\"data\":[%s],\"name\":\"%s\"}],\"is_compared\":%s,\"dates\":[%s],\"result\":\"success\"}";
        String name = startDate + "至" + endDate;

        StringBuilder data = new StringBuilder();
        StringBuilder dates = new StringBuilder();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mmdd = new SimpleDateFormat("MM-dd");
        Calendar ss = Calendar.getInstance();
        Date sd = DateUtil.stringToDate(startDate);
        for (int i = 0; i <= range; i++) {
            ss.setTime(sd);
            ss.add(Calendar.DAY_OF_YEAR, i);

            String stattime = sdf.format(ss.getTime());
            String dateStr = mmdd.format(ss.getTime());

            /** 获取启动次数 */
            String total = this.trendsDao.getDayLaunchesCount(appId, statKeyId, stattime, time_type, dimension_type, dimension_name);

            data.append(total).append(",");
            dates.append("\"" + dateStr + "\"").append(",");
        }
        String data_rate = " ";
        if (0 != data.length()) {
            data_rate = data.substring(0, data.length() - 1);
        }
        String dates_rate = " ";
        if (0 != dates.length()) {
            dates_rate = dates.substring(0, dates.length() - 1);
        }

        String ret = String.format(formatStr, data_rate, name, is_compared, dates_rate);
        return ret;
    }

    /**
     * 获取活跃用户明细
     *
     * @param appId
     * @param endDate
     * @param startDate
     * @param page
     * @param time_unit
     * @param dimension_type
     * @param dimension_name
     * @return 活跃用户明细json
     */
    public String getActiveUserDetails(String appId, String endDate, String startDate, String page, String time_unit, String time_type, String dimension_type, String dimension_name) {
        int range = this.getDays(startDate, endDate);
        if (range < 0) {
            return null;
        }

        if (null == page || page.trim().equals("")) {
            page = "1";
        }

        String formatStr = "{\"total\":%s,\"stats\":[%s],\"result\":\"success\"}";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mmdd = new SimpleDateFormat("MM-dd");
        Calendar ss = Calendar.getInstance();
        Date sd = DateUtil.stringToDate(startDate);

        String total = String.valueOf(range + 1);
        StringBuilder data = new StringBuilder();
        String formatData = "{\"data\":\"%s\",\"rate\":\"%s \",\"date\":\"%s\"}";

        /** 分页 */
        int pageNum = Integer.valueOf(page);
        int i_init_start = range - (pageNum - 1) * 20;
        int i_init_end = 0;
        if (i_init_start > 20) {
            i_init_end = range - pageNum * 20 + 1;
        }
        /** 获取活跃用户总数 */
        String activeUsersTotal = this.trendsDao.getPeriodActiveUserCount(appId, "3", startDate, endDate, time_type, dimension_type, dimension_name);
        for (int i = i_init_start; i >= i_init_end; i--) {
            ss.setTime(sd);
            ss.add(Calendar.DAY_OF_YEAR, i);
            String stattime = sdf.format(ss.getTime());
            String dateStr = mmdd.format(ss.getTime());

            /** 获取活跃用户数 */
            String activeUsers = this.trendsDao.getDayActiveUserCount(appId, "3", stattime, time_type, dimension_type, dimension_name);

            String percentage = this.getPercentage(activeUsers, activeUsersTotal, 2);
            if (null == percentage || percentage.trim().equals("")) {
                percentage = "0";
            }

            String dataStr = String.format(formatData, activeUsers, percentage, dateStr);
            data.append(dataStr).append(",");
        }
        String data_rate = " ";
        if (0 != data.length()) {
            data_rate = data.substring(0, data.length() - 1);
        }

        String ret = String.format(formatStr, total, data_rate);
        return ret;
    }

    /**
     * 获取启动次数明细
     *
     * @param appId
     * @param endDate
     * @param startDate
     * @param page
     * @param time_unit
     * @param dimension_type
     * @param dimension_name
     * @return 启动次数json
     */
    public String getLaunchesDetails(String appId, String endDate, String startDate, String page, String time_unit, String time_type, String dimension_type, String dimension_name) {
        int range = this.getDays(startDate, endDate);
        if (range < 0) {
            return null;
        }

        if (null == page || page.trim().equals("")) {
            page = "1";
        }

        String formatStr = "{\"total\":%s,\"stats\":[%s],\"result\":\"success\"}";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mmdd = new SimpleDateFormat("MM-dd");
        Calendar ss = Calendar.getInstance();
        Date sd = DateUtil.stringToDate(startDate);

        String total = String.valueOf(range + 1);
        StringBuilder data = new StringBuilder();
        String formatData = "{\"data\":\"%s\",\"rate\":\"%s \",\"date\":\"%s\"}";

        /** 分页 */
        int pageNum = Integer.valueOf(page);
        int i_init_start = range - (pageNum - 1) * 20;
        int i_init_end = 0;
        if (i_init_start > 20) {
            i_init_end = range - pageNum * 20 + 1;
        }
        /** 获取启动次数总数 */
        String launchesTotal = this.trendsDao.getPeriodLaunchesCount(appId, "1", startDate, endDate, time_type, dimension_type, dimension_name);
        for (int i = i_init_start; i >= i_init_end; i--) {
            ss.setTime(sd);
            ss.add(Calendar.DAY_OF_YEAR, i);
            String stattime = sdf.format(ss.getTime());
            String dateStr = mmdd.format(ss.getTime());

            /** 启动次数 */
            String launches = this.trendsDao.getDayLaunchesCount(appId, "1", stattime, time_type, dimension_type, dimension_name);

            String percentage = this.getPercentage(launches, launchesTotal, 2);
            if (null == percentage || percentage.trim().equals("")) {
                percentage = "0";
            }

            String dataStr = String.format(formatData, launches, percentage, dateStr);
            data.append(dataStr).append(",");
        }
        String data_rate = " ";
        if (0 != data.length()) {
            data_rate = data.substring(0, data.length() - 1);
        }

        String ret = String.format(formatStr, total, data_rate);
        return ret;
    }

    private String getPercentage(String sub, String total, int scale) {
        if (null == total || total.trim().equals("0") || total.trim().equals("") || null == sub || sub.trim().equals("0") || sub.trim().equals("")) {
            return "";
        }
        if (scale < 0) {
            scale = 2;
        }
        BigDecimal divisor = new BigDecimal(sub);
        BigDecimal dividend = new BigDecimal(total);
        double d = divisor.divide(dividend, scale + 2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;

        BigDecimal result = new BigDecimal(Double.toString(d));
        BigDecimal one = new BigDecimal("1");
        String ret = result.divide(one, scale, BigDecimal.ROUND_HALF_UP).toString();
        return ret;
    }

    private int getDays(String startDate, String endDate) {
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


    private int getMonths(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        /** 计算起止日期间的月数 */
        int range = 0;
        Calendar cs = Calendar.getInstance();
        Calendar ce = Calendar.getInstance();

        try {
            cs.setTime(sdf.parse(startDate));
            ce.setTime(sdf.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int startDateMonth = cs.get(Calendar.MONTH)+1;
        int endDateMonth = ce.get(Calendar.MONTH)+1;
        int startDateYear = cs.get(Calendar.YEAR);
        int endDateYear = ce.get(Calendar.YEAR);

        range = (endDateMonth-startDateMonth)+12*(endDateYear-startDateYear) ;
        return range;
    }

    /****************************/
    /**
     * 获取电商用户-周留存率数据
     *
     * @param appId
     * @param endDate
     * @param startDate
     * @param page
     * @param time_unit
     * @param dimension_type
     * @param dimension_name
     * @return 用户留存率json数据
     */
    public String getMallUserWeekRetentions(String appId, String endDate, String startDate, String page, String time_unit, String time_type, String dimension_type, String dimension_name) {
        int range = this.getDays(startDate, endDate);
        if (range < 0) {
            return null;
        }
        int totalStr = range + 1;

        String format = "{\"total\":%s,\"stats\":[%s],\"result\":\"success\"}";
        String formatStat = "{\"install_period\":\"%s\",\"retention_rate\":[%s],\"total_install\":%s}";
        StringBuilder rates = new StringBuilder();
        for (int i = 0; i <= range; i++) {
            Calendar ss = Calendar.getInstance();
            ss.setTime(DateUtil.stringToDate(startDate));
            ss.add(Calendar.DAY_OF_YEAR, i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String stattime = sdf.format(ss.getTime());

            /** 获取新增用户数 */
            //String total = this.trendsDao.getDayNewUserCount(appId, "4", stattime, time_type, dimension_type, dimension_name);
            String total = this.trendsDao.getDayNewUserCount("4", "34", stattime, time_type, dimension_type, dimension_name);
            //List<Map<String, Object>> rowlst = this.trendsDao.queryRetentions(appId, "18", stattime, dimension_type, dimension_name, page, time_unit);
            List<Map<String, Object>> rowlst = this.trendsDao.queryRetentions("4", "28", stattime, dimension_type, dimension_name, page, time_unit);
            if (null == rowlst) {
                String str = String.format(formatStat, stattime, "", "");
                rates.append(str).append(",");
                continue;
            }

            StringBuilder values = new StringBuilder();
            for (Map<String, Object> map : rowlst) {
                String sv = String.valueOf(map.get("stat_value"));
                if (null == sv || sv.trim().equals("")) {
                    continue;
                }
                sv = sv.replaceAll("\n", "");
                String s_v = this.getPercentage(sv, total, 2);
                if (null == s_v || s_v.trim().equals("")) {
                    continue;
                }
                values.append(s_v).append(",");
            }
            String temp = " ";
            if (0 != values.length()) {
                temp = values.substring(0, values.length() - 1);
            }

            String rateStr = String.format(formatStat, stattime, temp, total);
            rates.append(rateStr).append(",");
        }
        String retention_rate = " ";
        if (0 != rates.length()) {
            retention_rate = rates.substring(0, rates.length() - 1);
        }

        String ret = String.format(format, totalStr, retention_rate);
        
        //TODO
        System.out.println("ret in service==============: "+ret);
        
        return ret;
    }

    /**
     * 获取原始用户-留存率数据
     *
     * @param appId
     * @param endDate
     * @param startDate
     * @param page
     * @param time_unit
     * @param dimension_type
     * @param dimension_name
     * @return 用户留存率json数据
     */
    public String getOriginUserRetentions(String appId, String endDate, String startDate, String page, String time_unit, String time_type, String dimension_type, String dimension_name) {
        int range = this.getDays(startDate, endDate);
        if (range < 0) {
            return null;
        }
        int totalStr = range + 1;

        String format = "{\"total\":%s,\"stats\":[%s],\"result\":\"success\"}";
        String formatStat = "{\"install_period\":\"%s\",\"retention_rate\":[%s],\"total_install\":%s}";
        StringBuilder rates = new StringBuilder();
        for (int i = 0; i <= range; i++) {
            Calendar ss = Calendar.getInstance();
            ss.setTime(DateUtil.stringToDate(startDate));
            ss.add(Calendar.DAY_OF_YEAR, i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String stattime = sdf.format(ss.getTime());

            /** 获取新增用户数 */
            String total = this.trendsDao.getDayNewUserCount(appId, "4", stattime, time_type, dimension_type, dimension_name);
            List<Map<String, Object>> rowlst = this.trendsDao.queryRetentions(appId, "17", stattime, dimension_type, dimension_name, page, time_unit);
            if (null == rowlst) {
                String str = String.format(formatStat, stattime, "", "");
                rates.append(str).append(",");
                continue;
            }

            StringBuilder values = new StringBuilder();
            for (Map<String, Object> map : rowlst) {
                String sv = String.valueOf(map.get("stat_value"));
                if (null == sv || sv.trim().equals("")) {
                    continue;
                }
                sv = sv.replaceAll("\n", "");
                String s_v = this.getPercentage(sv, total, 2);
                if (null == s_v || s_v.trim().equals("")) {
                    continue;
                }
                values.append(s_v).append(",");
            }
            String temp = " ";
            if (0 != values.length()) {
                temp = values.substring(0, values.length() - 1);
            }

            String rateStr = String.format(formatStat, stattime, temp, total);
            rates.append(rateStr).append(",");
        }
        String retention_rate = " ";
        if (0 != rates.length()) {
            retention_rate = rates.substring(0, rates.length() - 1);
        }

        String ret = String.format(format, totalStr, retention_rate);
        return ret;
    }


    /**
     * 注册用户来源
     * @param startDate
     * @param endDate
     * @return
     */
    public Map queryRegisterUserList(String startDate,String endDate){

        String   sql ="select source, statdate,sum(num) as totalNum from dd_reg_user_stat WHERE  date(statdate) BETWEEN ? AND ? GROUP BY source, statdate ORDER BY  statdate ";

        Map tempMap=new HashMap();

        List  data=new ArrayList();
        List  xAxis=new ArrayList();
        List  nameAndValue=new ArrayList();
        List type0List=new ArrayList();
        List type3List=new ArrayList();
        List type4List=new ArrayList();


        List<Map<String,Object>> list=trendsDao.queryRegisterUserList(sql,startDate,endDate);

        if(list.isEmpty()){
            list=trendsDao.queryRegisterUserList(sql,"2015-08-08","2015-10-10");
        }

        String dateTime="";
        for (Map<String,Object> map:list){
            Date statdate=(Date) map.get("statdate");
            String time=DateUtil.dateToString(statdate,"yyyy-MM-dd");
            int source=(int)map.get("source");
            BigDecimal totalNum=(BigDecimal) map.get("totalNum");
            Integer total=totalNum.intValue();

            if (!dateTime.equals(time)){
                dateTime=time;
                xAxis.add("\""+dateTime+"\"");
                total=total+total;
            }

            RegisterUserModel userModel=new RegisterUserModel();
            userModel.setDateTime(dateTime);
            userModel.setTotal(total);
            if (source==0){
                userModel.setType(source+"");
                userModel.setName("网站");
                type0List.add(total);
            }if (source==3){
                userModel.setName("IOS豆果美食");
                userModel.setType(source+"");
                type3List.add(total);
            }if(source==4){
                userModel.setName("Android豆果美食");
                userModel.setType(source+"");
                type4List.add(total);
            }
            nameAndValue.add(userModel);
        }

        for (int i=0;i<type0List.size();i++){
            int sum =(int)type0List.get(i)+(int)type3List.get(i)+(int)type4List.get(i);
            data.add(sum);
        }

        tempMap.put("name",xAxis);
        tempMap.put("data",data);
        tempMap.put("wangzhan",type0List);
        tempMap.put("ios",type3List);
        tempMap.put("android",type4List);
        tempMap.put("nameAndValue",nameAndValue);


        return tempMap;
    }

    /**
     * 注册用户日统计
     * @param startDate
     * @param endDate
     * @return
     */
    public Map queryRegisterDayUserList(String startDate,String endDate){

        String   sql ="select source, statdate,sum(num) as totalNum from dd_reg_user_stat WHERE  date(statdate) BETWEEN ? AND ? GROUP BY source, statdate ORDER BY  statdate ";

        Map tempMap=new HashMap();

        List  data=new ArrayList();
        List  xAxis=new ArrayList();
        List  statdateList= new ArrayList();
        List  nameAndValue=new ArrayList();
        List type0List=new ArrayList();
        List type3List=new ArrayList();
        List type4List=new ArrayList();


        List<Map<String,Object>> list=trendsDao.queryRegisterUserList(sql,startDate,endDate);

        String dateTime="";
        for (Map<String,Object> map:list){
            Date statdate=(Date) map.get("statdate");
            String time=DateUtil.dateToString(statdate,"yyyy-MM-dd");
            BigDecimal totalNum=(BigDecimal) map.get("totalNum");
            Integer total=totalNum.intValue();
            int source=(int)map.get("source");

            if (!dateTime.equals(time)){
                dateTime=time;
                xAxis.add("\""+dateTime+"\"");
                statdateList.add(dateTime);
            }
            if (source==0){
                type0List.add(total);
            }if (source==3){
                type3List.add(total);
            }if(source==4){
                type4List.add(total);
            }
        }

        for (int i=0;i<type0List.size();i++){
            int sum =(int)type0List.get(i)+(int)type3List.get(i)+(int)type4List.get(i);
            RegisterUserModel userModel=new RegisterUserModel();
            userModel.setDateTime((String) statdateList.get(i));
            userModel.setTotal(sum);
            nameAndValue.add(userModel);
            data.add(sum);
        }

        tempMap.put("name",xAxis);
        tempMap.put("data",data);
        tempMap.put("nameAndValue",nameAndValue);

        return tempMap;
    }

    public Map queryRegisterWeekUserList(String startDate,String endDate){
        String sql = "select stat_year,stat_week, sum(num) as totalNum from dd_reg_user_stat WHERE  date(statdate)  BETWEEN ? AND ? GROUP BY stat_year,stat_week ORDER BY  stat_year,stat_week";

        List<Map<String,Object>> list= trendsDao.queryRegisterUserList(sql,startDate ,endDate);

        Map tempMap=new HashMap();

        List  data=new ArrayList();
        List  xAxis=new ArrayList();
        List  nameAndValue=new ArrayList();

        for (Map<String,Object> map:list){
            int week=(int) map.get("stat_week");
            int year=(int) map.get("stat_year");
            BigDecimal totalNum=(BigDecimal) map.get("totalNum");
            Integer total=totalNum.intValue();
            RegisterUserModel userModel=new RegisterUserModel();
            userModel.setDateTime(year+"-"+week);
            userModel.setTotal(total);

            xAxis.add("\""+year+"-"+week+"\"");
            data.add(total);
            nameAndValue.add(userModel);
        }
        tempMap.put("name",xAxis);
        tempMap.put("data",data);
        tempMap.put("nameAndValue",nameAndValue);
        return tempMap;
    }

    /**
     * 注册用户月统计
     * @param startDate
     * @param endDate
     * @return
     */
    public Map queryRegisterMonthUserList(String startDate,String endDate){

        String   sql ="select  stat_year,stat_month,sum(num) as totalNum from dd_reg_user_stat WHERE  date(statdate)  BETWEEN ? AND ? GROUP BY  stat_year,stat_month ORDER BY   stat_year,stat_month";

        List<Map<String,Object>> list= trendsDao.queryRegisterUserList(sql,startDate ,endDate);

        Map tempMap=new HashMap();

        List  data=new ArrayList();
        List  xAxis=new ArrayList();
        List  nameAndValue=new ArrayList();

        for (Map<String,Object> map:list){
            int year=(int) map.get("stat_year");
            int month=(int) map.get("stat_month");
            BigDecimal totalNum=(BigDecimal) map.get("totalNum");
            Integer total=totalNum.intValue();
            RegisterUserModel userModel=new RegisterUserModel();
            userModel.setDateTime(year+"-"+month);
            userModel.setTotal(total);

            xAxis.add("\""+year+"-"+month+"\"");
            data.add(total);
            nameAndValue.add(userModel);
        }
        tempMap.put("name",xAxis);
        tempMap.put("data",data);
        tempMap.put("nameAndValue",nameAndValue);
        return tempMap;
    }


}
