package com.douguo.dc.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.StatKey;
import com.douguo.dc.util.Util;

@Repository("eventsDao")
public class EventsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String eventsListSQL = "SELECT s.pv,s.uv,s.event_code,d.event_name FROM dd_event_stat s LEFT JOIN dd_event_dic d ON s.app_id=d.app_id and s.event_code=d.event_code WHERE s.stat_time=? and s.app_id=? and s.dimension_type=? and s.dimension_name=? ORDER BY pv DESC ";
    private final String queryVservionsSQL = "SELECT distinct t.app_version from dd_app_version_dict t where t.app_id=? and t.status='1' order by t.app_version asc;";
    private final String queryEventsSQL = "SELECT event_code,event_name FROM dd_event_dic where app_id=? and status='1' order by event_code asc";
    private final String queryNameSQL = "select event_name from dd_event_dic where app_id=? and event_code=? and status = 1";
    private final String eventListSQL = "SELECT pv, uv, stat_time FROM dd_event_stat where app_id=? and event_code=? and dimension_type=? and dimension_name=? and stat_time between ? and ? order by stat_time desc";
    private final String getPvUvSQL = "select stat_value,stat_time from stat_collection where stat_time between ? and ? and app_id=? and time_type='TODAY' and dimension_type=? and dimension_name=? and stat_key_id=? order by stat_time desc";

    public List<Map<String, Object>> getEventsListByDay(String appId, String dimensionType, String version, String date) {
        Util.showSQL(eventsListSQL, new Object[]{date, appId, dimensionType, version});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(eventsListSQL, new Object[]{date, appId, dimensionType, version});
        return rowlst;
    }

    public List<Map<String, Object>> queryVersions(String appId) {
        Util.showSQL(queryVservionsSQL, new Object[]{appId});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryVservionsSQL, new Object[]{appId});
        return rowlst;
    }

    public List<Map<String, Object>> queryEvents(String appId) {
        Util.showSQL(queryEventsSQL, new Object[]{appId});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryEventsSQL, new Object[]{appId});
        return rowlst;
    }

    public String getNameById(String appId, String id) {
        Util.showSQL(queryNameSQL, new Object[]{appId, id});
        Map<String, Object> map0;
        try {
            map0 = jdbcTemplate.queryForMap(queryNameSQL, new Object[]{appId, id});
        } catch (Exception e) {
            return "";
        }

        return map0.get("event_name").toString();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<Map<String, Object>> getEventsListById(String appId, String id, String dimensionType, String version, String startDate, String endDate) {
        Util.showSQL(eventListSQL, new Object[]{appId, id, dimensionType, version, startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(eventListSQL, new Object[]{appId, id, dimensionType, version, startDate, endDate});
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : rowlst) {
            Map map0 = new HashMap();
            map0.put("uv", map.get("uv"));
            map0.put("pv", map.get("pv"));
            map0.put("date", map.get("stat_time"));
            ret.add(map0);
        }
        return ret;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map getPv(String appId, String dimensionType, String version, String startDate, String endDate) {
        Util.showSQL(getPvUvSQL, new Object[]{startDate, endDate, appId, dimensionType, version, StatKey.SessionCount});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(getPvUvSQL, new Object[]{startDate, endDate, appId, dimensionType, version, StatKey.SessionCount});
        Map map0 = new HashMap();
        for (Map<String, Object> map : rowlst) {
            map0.put(map.get("stat_time"), map.get("stat_value").toString().replace("\n", ""));
        }
        return map0;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map getUv(String appId, String dimensionType, String version, String startDate, String endDate) {
        Util.showSQL(getPvUvSQL, new Object[]{startDate, endDate, appId, dimensionType, version, StatKey.UserCount});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(getPvUvSQL, new Object[]{startDate, endDate, appId, dimensionType, version, StatKey.UserCount});
        Map map0 = new HashMap();
        for (Map<String, Object> map : rowlst) {
            map0.put(map.get("stat_time"), map.get("stat_value").toString().replace("\n", ""));
        }
        return map0;
    }

    public List<Map<String, Object>> getCustomEventDetailList(String beginDate, String endDate, String eventCode) {
        String customEventDetailListSQL = "select app_id,stat_time,dimension_name,pv,uv " +
                "from dd_event_stat where  dimension_type='CUSTOM' and stat_time>=? and stat_time<=? and event_code=? and app_id in (3,4) " +
                "order by stat_time,dimension_name";
//        System.out.println("开始时间+结束时间"+beginDate+endDate);
        Util.showSQL(customEventDetailListSQL, new Object[]{beginDate, endDate, eventCode});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(customEventDetailListSQL, new Object[]{beginDate, endDate, eventCode});
        return rowlst;
    }
    
    /**
     * 安卓客户端Event事件统计数据
     * @Date	2016-7-26
     * @Author	ZhangJianFei	
     */
    public List<Map<String, Object>> getCustomEventDetailList_Android(String beginDate, String endDate, String eventCode) {
        String customEventDetailListSQL = "select app_id,stat_time,dimension_name,pv,uv " +
                "from dd_event_stat where  dimension_type='CUSTOM' and stat_time>=? and stat_time<=? and event_code=? and app_id = 3 " +
                "order by stat_time,dimension_name";
        Util.showSQL(customEventDetailListSQL, new Object[]{beginDate, endDate, eventCode});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(customEventDetailListSQL, new Object[]{beginDate, endDate, eventCode});
        return rowlst;
    }
    
    /**
     * IOS客户端Event事件统计数据
     * @Date	2016-7-26
     * @Author	ZhangJianFei	
     */
    public List<Map<String, Object>> getCustomEventDetailList_Ios(String beginDate, String endDate, String eventCode) {
        String customEventDetailListSQL = "select app_id,stat_time,dimension_name,pv,uv " +
                "from dd_event_stat where  dimension_type='CUSTOM' and stat_time>=? and stat_time<=? and event_code=? and app_id = 4 " +
                "order by stat_time,dimension_name";
        Util.showSQL(customEventDetailListSQL, new Object[]{beginDate, endDate, eventCode});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(customEventDetailListSQL, new Object[]{beginDate, endDate, eventCode});
        return rowlst;
    }
}
