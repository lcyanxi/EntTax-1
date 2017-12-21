package com.douguo.dc.applog.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.applog.dao.AppDauDao;
import com.douguo.dc.dao.TrendsDao;
import com.douguo.dc.util.DateUtil;

@Repository("appDauService")
public class AppDauService {

    @Autowired
    private AppDauDao appDauDao;

    @Autowired
    private TrendsDao trendsDao;

    public List<Map<String, Object>> querySumListByApp(String startDate, String endDate, String appId) {
        List<Map<String, Object>> keyRows = appDauDao.querySumListByApp(startDate, endDate, appId);
        return keyRows;
    }

    public List<Map<String, Object>> querySumList(String startDate, String endDate) {
        List<Map<String, Object>> keyRows = appDauDao.querySumList(startDate, endDate);
        return keyRows;
    }

    public List<Map<String, Object>> querySumListAll(String startDate, String endDate) {
        List<Map<String, Object>> keyRows = appDauDao.querySumListAll(startDate, endDate);
        return keyRows;
    }

    /**
     * 指定app，按渠道汇总日活信息
     *
     * @param startDate
     * @param endDate
     * @param appId
     * @param channel
     * @param vers
     * @return
     */
    public List<Map<String, Object>> queryChannelListByApp(String startDate, String endDate, String appId,
                                                           String channel, String vers) {
        List<Map<String, Object>> keyRows = appDauDao.queryChannelListByApp(startDate, endDate, appId, channel, vers);
        return keyRows;
    }

    /**
     * 指定app，按渠道汇总日活质量
     *
     * @param startDate
     * @param endDate
     * @param appId
     * @param channel
     * @param vers
     * @return
     */
    public List<Map<String, Object>> queryChannelQualityListByApp(String startDate, String endDate, String appId,
                                                                  String channel) {
        List<Map<String, Object>> keyRows = appDauDao.queryChannelQualityListByApp(startDate, endDate, appId, channel);
        return keyRows;
    }

    /**
     * 指定app，按版本统计
     *
     * @param startDate
     * @param endDate
     * @param appId
     * @param channel
     * @param vers
     * @return
     */
    public List<Map<String, Object>> queryVersListByApp(String startDate, String endDate, String appId) {
        List<Map<String, Object>> keyRows = appDauDao.queryVersListByApp(startDate, endDate, appId);
        return keyRows;
    }

    /**
     * @param startDate
     * @param endDate
     * @param appId
     * @param channel
     * @param vers
     * @return
     */
    public String getUserRetentions(String appId, String endDate, String startDate, String page, String time_unit,
                                    String time_type, String dimension_type, String dimension_name) {
        int range = DateUtil.getDays(startDate, endDate);
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
                //String s_v = this.getPercentage(sv, total, 2);
                String s_v = "";//??????????????????????????????
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
     * 按渠道汇总日活信息
     *
     * @param startDate
     * @param endDate
     * @param app
     * @return
     */
    public List<Map<String, Object>> queryChannelList(String startDate, String endDate, String app) {
        List<Map<String, Object>> keyRows = appDauDao.queryChannelList(startDate, endDate, app);
        return keyRows;
    }

    /**
     * 获取某段时间内，某app的活跃用户总数
     *
     * @param startDate
     * @param endDate
     * @param app
     */
    public List<Map<String, Object>> queryActiveUserTotal(String startDate, String endDate, String... app) {
        return appDauDao.queryActiveUserTotal(startDate, endDate, app);
    }
}
