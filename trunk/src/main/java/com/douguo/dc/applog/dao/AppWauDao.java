package com.douguo.dc.applog.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("appWauDao")
public class AppWauDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryListByApp(String startDate, String endDate, String appId, String type) {
        String sql = "SELECT statdate,app,imei,euid,uid,logins FROM `dd_app_week_dau_stat` WHERE app= ? and type=? AND statdate BETWEEN ? AND ? ORDER BY statdate,app desc";
        Util.showSQL(sql, new Object[]{appId, type, startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{appId, type, startDate,
                endDate});
        return rowlst;
    }

    public List<Map<String, Object>> queryListByType(String startDate, String endDate, String type) {
        String sql = "SELECT statdate,app,imei,euid,uid,logins FROM `dd_app_week_dau_stat` WHERE type=? and statdate BETWEEN ? AND ? ORDER BY statdate,app desc";
        Util.showSQL(sql, new Object[]{type, startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{type, startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> querySumListByType(String startDate, String endDate, String type, String... apps) {
        StringBuffer sbApp = null;
        for (String app : apps) {
            if (sbApp == null) {
                sbApp = new StringBuffer(app);
            } else {
                sbApp.append("," + app);
            }
        }

        String sql = "SELECT statdate,sum(imei) as imei,sum(euid) as euid,sum(uid) as uid,sum(logins) as logins FROM `dd_app_week_dau_stat` WHERE type=? and statdate BETWEEN ? AND ? ";
        if (sbApp != null) {
            sql += "and app in (" + sbApp.toString() + ")";
        }

        sql += " group by statdate ORDER BY statdate,app desc";
        Util.showSQL(sql, new Object[]{type, startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{type, startDate, endDate});
        return rowlst;
    }
}