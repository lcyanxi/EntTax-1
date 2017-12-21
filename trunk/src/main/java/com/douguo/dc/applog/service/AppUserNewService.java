package com.douguo.dc.applog.service;

import java.math.BigDecimal;
import java.util.*;

import com.douguo.dc.util.BigDecimalUtil;
import com.douguo.dc.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.applog.dao.AppUserNewDao;

@Repository("appUserNewService")
public class AppUserNewService {

    @Autowired
    private AppUserNewDao appUserNewDao;

    /**
     * 按渠道查询新增
     *
     * @param startDate
     * @param endDate
     * @param appId
     * @param channel
     * @param vers
     * @return
     */
    public List<Map<String, Object>> queryListByChannel(String startDate, String endDate, String channel) {
        List<Map<String, Object>> keyRows = appUserNewDao.queryListByChannel(startDate, endDate, channel);
        return keyRows;
    }

    /**
     * 按渠道汇总日活信息
     *
     * @param startDate
     * @param endDate
     * @param appId
     * @param channel
     * @param vers
     * @return
     */
    public List<Map<String, Object>> queryChannelList(String startDate, String endDate, String app) {
        List<Map<String, Object>> keyRows = appUserNewDao.queryChannelList(startDate, endDate, app);
        return keyRows;
    }


    /**
     * 获取某段时间内新增用户总量
     *
     * @param startDate
     * @param endDAte
     * @param app
     */
    public List<Map<String, Object>> getNewUserTotal(String startDate, String endDate, String... app) {
        List<Map<String, Object>> keyRows = appUserNewDao.getNewUserTotal(startDate, endDate, app);
        return keyRows;

    }

    /**
     * 获取7日内累计新增用户
     */
    public List<Map<String, Object>> get7DayNewUserTotal(String startDate, String endDate, String... app) {
        String newStartDate = DateUtil.getSpecifiedDayBefore(startDate, 6);
        List<Map<String, Object>> keyRows = appUserNewDao.getNewUserTotal(newStartDate, endDate, app);

        Map<Date, BigDecimal> mapNewUser = new HashMap<Date, BigDecimal>();
        for (Map<String, Object> map : keyRows) {
            Date curDate = (Date) map.get("statdate");
            BigDecimal total_new_users = (BigDecimal) map.get("total_new_users");
            mapNewUser.put(curDate, total_new_users);
        }

        //构造新的7日汇总数据
        List<Map<String, Object>> newRows = appUserNewDao.getNewUserTotal(startDate, endDate, app);

        for (Map<String, Object> map : newRows) {
            Date curDate = (Date) map.get("statdate");
            BigDecimal newUsers = new BigDecimal(0);
            for (Date date : mapNewUser.keySet()) {

                String strBeforeDate = DateUtil.getSpecifiedDayBefore(DateUtil.date2Str(curDate, "yyyy-MM-dd"), 6);
                Date beforeDate = DateUtil.stringToDate(strBeforeDate, "yyyy-MM-dd");

//                if(!curDate.before(date) && !beforeDate.after(date)){
                if (date.getTime() >= beforeDate.getTime() && date.getTime() <= curDate.getTime()) {
                    newUsers = newUsers.add(mapNewUser.get(date));
                }
            }

            map.put("total_new_users", newUsers);
        }

        return newRows;

    }

    /**
     * 获取某段时间内新增用户总量
     *
     * @param startDate
     * @param endDAte
     * @param app
     */
    public List<Map<String, Object>> getNewUserTotalForApp(String startDate, String endDate, String... app) {
        List<Map<String, Object>> keyRows = appUserNewDao.getNewUserTotalForApp(startDate, endDate, app);
        return keyRows;

    }
}