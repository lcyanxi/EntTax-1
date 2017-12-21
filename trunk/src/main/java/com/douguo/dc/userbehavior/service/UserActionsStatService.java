package com.douguo.dc.userbehavior.service;

import com.douguo.dc.applog.service.AppDauService;
import com.douguo.dc.applog.service.AppUserNewService;
import com.douguo.dc.applog.service.AppWauService;
import com.douguo.dc.userbehavior.common.UserBehaviorConstants;
import com.douguo.dc.userbehavior.dao.UserActionsStatDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userActionsStatService")
public class UserActionsStatService {

    @Autowired
    private UserActionsStatDao userActionsStatDao;

    @Autowired
    private AppDauService appDauService;

    @Autowired
    private AppWauService appWauService;

    @Autowired
    private AppUserNewService appUserNewService;


    /**
     * 获取7天汇总数据
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> queryAll7DayList(String startDate, String endDate) {
        List<Map<String, Object>> keyRows = userActionsStatDao.queryUserActionsStatList(UserBehaviorConstants.StatType.ALL.type, UserBehaviorConstants.UserType.ALL.type, UserBehaviorConstants.TimeType.DAY7.type, startDate, endDate);

        //处理周活跃数据
        Map<Date, Map<String, Object>> mapAppWau = new HashMap<Date, Map<String, Object>>();

        List<Map<String, Object>> appWauList = appWauService.querySumListByType(startDate, endDate, "ALL", "3", "4");

        for (Map<String, Object> map : appWauList) {
            Date statdate = (Date) map.get("statdate");
            mapAppWau.put(statdate, map);
        }

        //处理7天新增用户
        Map<Date, Map<String, Object>> mapAppUserNew = new HashMap<Date, Map<String, Object>>();

        List<Map<String, Object>> appUserNew = appUserNewService.get7DayNewUserTotal(startDate, endDate, "3", "4");

        for (Map<String, Object> map : appUserNew) {
            Date statdate = (Date) map.get("statdate");
            mapAppUserNew.put(statdate, map);
        }

        //重新构造list
        for (Map<String, Object> map : keyRows) {
            Date statdate = (Date) map.get("statdate");

            Map<String, Object> tmpMapWau = mapAppWau.get(statdate);
            try {
                BigDecimal active_users = (BigDecimal) tmpMapWau.get("uid");
                map.put("active_users", active_users);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Map<String, Object> tmpMapNewUser = mapAppUserNew.get(statdate);
            try{
                BigDecimal new_active_users = (BigDecimal) tmpMapNewUser.get("total_new_users");
                map.put("new_active_users", new_active_users);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return keyRows;
    }

    public List<Map<String, Object>> queryNewRegUser7DayList(String startDate, String endDate) {
        List<Map<String, Object>> keyRows = userActionsStatDao.queryUserActionsStatList(UserBehaviorConstants.StatType.ALL.type, UserBehaviorConstants.UserType.NEW_REG_USER.type, UserBehaviorConstants.TimeType.DAY7.type, startDate, endDate);
        return keyRows;
    }

    public List<Map<String, Object>> queryNewRegUserDayList(String startDate, String endDate) {
        List<Map<String, Object>> keyRows = userActionsStatDao.queryUserActionsStatList(UserBehaviorConstants.StatType.ALL.type, UserBehaviorConstants.UserType.NEW_REG_USER.type, UserBehaviorConstants.TimeType.DAY.type, startDate, endDate);
        return keyRows;
    }

    /**
     * 获取按日汇总数据
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> queryAllDayList(String startDate, String endDate) {
        List<Map<String, Object>> keyRows = userActionsStatDao.queryUserActionsStatList(UserBehaviorConstants.StatType.ALL.type, UserBehaviorConstants.UserType.ALL.type, UserBehaviorConstants.TimeType.DAY.type, startDate, endDate);

        //处理活跃数据
        Map<Date, Map<String, Object>> mapAppDau = new HashMap<Date, Map<String, Object>>();

        List<Map<String, Object>> appWauList = appDauService.queryActiveUserTotal(startDate, endDate, "3", "4");

        for (Map<String, Object> map : appWauList) {
            Date statdate = (Date) map.get("statdate");
            mapAppDau.put(statdate, map);
        }

        //处理新增用户
        Map<Date, Map<String, Object>> mapAppUserNew = new HashMap<Date, Map<String, Object>>();

        List<Map<String, Object>> appUserNew = appUserNewService.getNewUserTotal(startDate, endDate, "3", "4");

        for (Map<String, Object> map : appUserNew) {
            Date statdate = (Date) map.get("statdate");
            mapAppUserNew.put(statdate, map);
        }


        //重新构造list
        for (Map<String, Object> map : keyRows) {
            Date statdate = (Date) map.get("statdate");

            Map<String, Object> tmpMapDau = mapAppDau.get(statdate);
            try {
                BigDecimal active_users = (BigDecimal) tmpMapDau.get("uid");
                map.put("active_users", active_users);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Map<String, Object> tmpMapNewUser = mapAppUserNew.get(statdate);
            BigDecimal new_active_users=new BigDecimal(0);
            try{
                new_active_users = (BigDecimal) tmpMapNewUser.get("total_new_users");
                map.put("new_active_users", new_active_users);
            }catch (Exception e){
                map.put("new_active_users", new_active_users);
                e.printStackTrace();
            }


        }

        return keyRows;
    }
}