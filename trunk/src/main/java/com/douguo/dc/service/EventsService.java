package com.douguo.dc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.dao.EventsDao;
import com.douguo.dc.util.JsonUtil;

@Repository("eventsService")
public class EventsService {

    @Autowired
    private EventsDao eventsDao;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<Map<String, Object>> getEventsListByDay(String appId, String version, String date) {
        String dimensionType = "";
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        if ("".equals(version)) {
            dimensionType = "ALL";
        } else {
            dimensionType = "VERSION";
        }
        List<Map<String, Object>> daoList = eventsDao.getEventsListByDay(appId, dimensionType, version, date);
        for (Map<String, Object> map : daoList) {
            Map map0 = new HashMap();
            map0.put("pv", map.get("pv"));
            map0.put("uv", map.get("uv"));
            map0.put("display_name", map.get("event_name"));
            map0.put("name", map.get("event_code"));
            map0.put("id", map.get("event_code"));
            ret.add(map0);
        }
        return ret;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<Map<String, Object>> queryVersions(String appId) {
        List<Map<String, Object>> daoList = eventsDao.queryVersions(appId);
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : daoList) {
            Map map0 = new HashMap();
            map0.put("name", map.get("app_version"));
            map0.put("id", map.get("app_version"));
            map0.put("is_shown", "true");
            ret.add(map0);
        }
        return ret;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<Map<String, Object>> queryEvents(String appId) {
        List<Map<String, Object>> daoList = eventsDao.queryEvents(appId);
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : daoList) {
            Map map0 = new HashMap();
            map0.put("name", map.get("event_code") + "(" + map.get("event_name") + ")");
            map0.put("id", map.get("event_code"));
            ret.add(map0);
        }
        return ret;
    }

    public String getNameById(String appId, String id) {
        return eventsDao.getNameById(appId, id);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public String getEventsCount(String appId, String id, String version, String startDate, String endDate) {
        String dimensionType;
        if (version == null || version.equals("")) {
            dimensionType = "ALL";
        } else {
            dimensionType = "VERSION";
        }
        List<Map<String, Object>> daoList = eventsDao.getEventsListById(appId, id, dimensionType, version, startDate,
                endDate);
        Collections.reverse(daoList);
        List<String> dates = new ArrayList<String>();
        List<Integer> data = new ArrayList<Integer>();
        List<Map> stats = new ArrayList<Map>();
        Map stat = new HashMap();
        for (Map<String, Object> map : daoList) {
            dates.add(map.get("date").toString().substring(5));
            data.add(new Integer(map.get("pv").toString()));
        }
        stat.put("data", data);
        stat.put("name", id + "(" + this.getNameById(appId, id) + ")");
        stats.add(stat);
        return JsonUtil.getChartJson(dates, stats, false, "success", -1);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public String getEventsUv(String appId, String id, String version, String startDate, String endDate) {
        String dimensionType;
        if (version == null || version.equals("")) {
            dimensionType = "ALL";
        } else {
            dimensionType = "VERSION";
        }
        List<Map<String, Object>> daoList = eventsDao.getEventsListById(appId, id, dimensionType, version, startDate,
                endDate);
        Collections.reverse(daoList);
        List<String> dates = new ArrayList<String>();
        List<Integer> data = new ArrayList<Integer>();
        List<Map> stats = new ArrayList<Map>();
        Map stat = new HashMap();
        for (Map<String, Object> map : daoList) {
            dates.add(map.get("date").toString().substring(5));
            data.add(new Integer(map.get("uv").toString()));
        }
        stat.put("data", data);
        stat.put("name", id + "(" + this.getNameById(appId, id) + ")");
        stats.add(stat);
        return JsonUtil.getChartJson(dates, stats, false, "success", -1);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public String getEventsPvRadio(String appId, String id, String version, String startDate, String endDate) {
        String dimensionType;
        if (version == null || version.equals("")) {
            dimensionType = "ALL";
        } else {
            dimensionType = "VERSION";
        }
        List<Map<String, Object>> eventList = eventsDao.getEventsListById(appId, id, dimensionType, version, startDate,
                endDate);
        Map mapPv = eventsDao.getPv(appId, dimensionType, version, startDate, endDate);
        Collections.reverse(eventList);
        List<String> dates = new ArrayList<String>();
        List<Float> data = new ArrayList<Float>();
        List<Map> stats = new ArrayList<Map>();
        Map stat = new HashMap();
        for (Map<String, Object> map : eventList) {
            dates.add(map.get("date").toString().substring(5));
            float radio;
            try {
                float eventcount = (Integer) map.get("pv");
                int pv = Integer.valueOf(mapPv.get(map.get("date")).toString());
                radio = eventcount / pv;
            } catch (Exception e) {
                e.printStackTrace();
                radio = 0.00f;
            }
            data.add(radio);
        }
        stat.put("data", data);
        stat.put("name", id + "(" + this.getNameById(appId, id) + ")");
        stats.add(stat);
        return JsonUtil.getChartJson(dates, stats, false, "success", -1);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public String getEventsUvRadio(String appId, String id, String version, String startDate, String endDate) {
        String dimensionType;
        if (version == null || version.equals("")) {
            dimensionType = "ALL";
        } else {
            dimensionType = "VERSION";
        }
        List<Map<String, Object>> eventList = eventsDao.getEventsListById(appId, id, dimensionType, version, startDate,
                endDate);
        Map mapUv = eventsDao.getUv(appId, dimensionType, version, startDate, endDate);
        Collections.reverse(eventList);
        List<String> dates = new ArrayList<String>();
        List<Float> data = new ArrayList<Float>();
        List<Map> stats = new ArrayList<Map>();
        Map stat = new HashMap();
        for (Map<String, Object> map : eventList) {
            dates.add(map.get("date").toString().substring(5));
            float radio;
            try {
                float eventcount = (Integer) map.get("uv");
                int uv = Integer.valueOf(mapUv.get(map.get("date")).toString());
                radio = eventcount / uv;
            } catch (Exception e) {
                e.printStackTrace();
                radio = 0.00f;
            }
            data.add(radio);
        }
        stat.put("data", data);
        stat.put("name", id + "(" + this.getNameById(appId, id) + ")");
        stats.add(stat);
        return JsonUtil.getChartJson(dates, stats, false, "success", -1);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public String getEventsTable(String appId, String id, String version, String startDate, String endDate) {
        String dimensionType;
        if (version == null || version.equals("")) {
            dimensionType = "ALL";
        } else {
            dimensionType = "VERSION";
        }
        List<Map<String, Object>> eventList = eventsDao.getEventsListById(appId, id, dimensionType, version, startDate,
                endDate);
        Map mapUv = eventsDao.getUv(appId, dimensionType, version, startDate, endDate);
        Map mapPv = eventsDao.getPv(appId, dimensionType, version, startDate, endDate);
        List<Map> stats = new ArrayList<Map>();
        for (Map<String, Object> map : eventList) {
            Map stat = new HashMap();
            float radioPv;
            float radioUv;
            try {
                float eventcount = (Integer) map.get("pv");
                float eventuser = (Integer) map.get("uv");
                int uv = Integer.valueOf(mapUv.get(map.get("date")).toString());
                int pv = Integer.valueOf(mapPv.get(map.get("date")).toString());
                radioPv = eventcount / pv;
                radioUv = eventuser / uv;
                stat.put("count", eventcount);
                stat.put("device", eventuser);
                stat.put("count_per_launch", radioPv);
                stat.put("duration", radioUv);
                stat.put("date", map.get("date").toString());
            } catch (Exception e) {
                e.printStackTrace();
                radioPv = 0.00f;
                radioUv = 0.00f;
            }
            stats.add(stat);

        }
        return JsonUtil.getTableJson(stats, stats.size(), "success");
    }

    public List<Map<String, Object>> getCustomEventDetailList(String beginDate, String endDate, String eventCode) {
        return eventsDao.getCustomEventDetailList(beginDate, endDate, eventCode);
    }
    
    public List<Map<String, Object>> getCustomEventDetailList_Android(String beginDate, String endDate, String eventCode) {
        return eventsDao.getCustomEventDetailList_Android(beginDate, endDate, eventCode);
    }
    
    public List<Map<String, Object>> getCustomEventDetailList_Ios(String beginDate, String endDate, String eventCode) {
        return eventsDao.getCustomEventDetailList_Ios(beginDate, endDate, eventCode);
    }

}
