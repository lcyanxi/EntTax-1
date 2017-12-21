package com.douguo.dc.serverlog.dao;

import com.douguo.dc.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("serverLogQtypeStatDao")
public class ServerLogQtypeStatDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryListByClient(String startDate, String endDate, String app) {
        String sql = "select * from dd_serverlog_qtype_stat where statdate between ? and ? and client=? order by statdate,qtype";
        Util.showSQL(sql, new Object[]{startDate, endDate, app});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate, app});
        return rowlst;
    }

    public List<Map<String, Object>> querySumListByClient(String startDate, String endDate, String... apps) {
        StringBuffer appSql = new StringBuffer("");
        for (int i = 0; i < apps.length; i++) {
            if (appSql.length() == 0) {
                appSql.append(apps[i]);
            } else {
                appSql.append("," + apps[i]);
            }
        }
        String sql = "select statdate,qtype,sum(pv) as pv,sum(uv) as uv from dd_serverlog_qtype_stat where statdate between ? and ? and client in (" + appSql.toString() + ") group by statdate,qtype order by statdate,qtype";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }
    
    public List<Map<String, Object>> querySumListByClientIgnoreqtype(String startDate, String endDate, String... apps) {
        StringBuffer appSql = new StringBuffer("");
        for (int i = 0; i < apps.length; i++) {
            if (appSql.length() == 0) {
                appSql.append(apps[i]);
            } else {
                appSql.append("," + apps[i]);
            }
        }
        String sql = "select statdate,sum(pv) as pv,sum(uv) as uv from dd_serverlog_qtype_stat where statdate between ? and ? and qtype='qtype_all' and client in (" + appSql.toString() + ") group by statdate order by statdate";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }
    
}
