package com.douguo.dc.serverlog.dao;

import com.douguo.dc.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("serverLogQtypeSrctypeStatDao")
public class ServerLogQtypeSrctypeStatDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> querySumListByClientQtype(String startDate, String endDate, String... apps) {
        StringBuffer appSql = new StringBuffer("");
        for (int i = 0; i < apps.length; i++) {
            if (appSql.length() == 0) {
                appSql.append(apps[i]);
            } else {
                appSql.append("," + apps[i]);
            }
        }
        String sql = "select statdate,qtype,srctype,sum(pv) as pv,sum(uv) as uv ,srctype_name from dd_serverlog_qtype_srctype_stat where statdate between ? and ? and client in (" + appSql.toString() + ") and qtype is not null and srctype is not null group by statdate,qtype,srctype,srctype_name order by statdate ASC ,qtype ASC ,srctype DESC ";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }
}
