package com.douguo.dc.applog.dao;

import com.douguo.dc.util.Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("appMauDao")
public class AppMauDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> querySumListByApp(String startDate, String endDate, String appId) {
        String sql = "SELECT statdate,app,sum(imei) as imei,sum(euid) as euid,sum(uid) as uid,sum(login_users) as logins FROM `dd_mau_stat` WHERE app= ? AND statdate BETWEEN ? AND ? GROUP BY statdate,app ORDER BY statdate,app desc";
        Util.showSQL(sql, new Object[]{appId, startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{appId, startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> querySumList(String startDate, String endDate) {
        String sql = "SELECT statdate,app,sum(imei) as imei,sum(euid) as euid,sum(uid) as uid,sum(login_users) as logins,round(avg(avg_login_times),1) as avglogins  FROM `dd_mau_stat` WHERE statdate BETWEEN ? AND ? GROUP BY statdate,app ORDER BY statdate,app desc";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> querySumListAll(String startDate, String endDate) {
        String sql = "SELECT statdate,'ALL' as app,sum(imei) as imei,sum(euid) as euid,sum(uid) as uid,sum(login_users) as logins,round(avg(avg_login_times),1) as avglogins FROM `dd_mau_stat` WHERE statdate BETWEEN ? AND ? GROUP BY statdate ORDER BY statdate asc";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> queryChannelListByApp(String startDate, String endDate, String appId,
                                                           String channel, String vers) {
        String sql = "SELECT statdate,app,channel,sum(imei) as imei,sum(euid) as euid,sum(uid) as uid,sum(logins) as logins FROM `dd_mau_stat` WHERE app= ? AND statdate BETWEEN ? AND ? ";
        List<String> listParm = new ArrayList<String>();
        listParm.add(appId);
        listParm.add(startDate);
        listParm.add(endDate);

        if (StringUtils.isNotBlank(channel)) {
            sql += " AND channel = ? ";
            listParm.add(channel);
        }

        if (StringUtils.isNotBlank(vers)) {
            sql += " AND vers = ? ";
            listParm.add(vers);
        }

        sql += " group by statdate,channel ORDER BY statdate,uid desc";

        Util.showSQL(sql, new Object[]{appId, startDate, endDate});
        // List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new
        // Object[] { appId, startDate, endDate });
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, listParm.toArray());
        return rowlst;
    }

    public List<Map<String, Object>> queryChannelList(String startDate, String endDate, String app) {
        String sql = "SELECT statdate,app,channel,sum(imei) as imei,sum(euid) as euid,sum(uid) as uid,sum(logins) as logins FROM `dd_mau_stat` WHERE statdate BETWEEN ? AND ? ";
        List<String> listParm = new ArrayList<String>();
        listParm.add(startDate);
        listParm.add(endDate);

        if (StringUtils.isNotBlank(app)) {
            sql += " AND app = ? ";
            listParm.add(app);
        }

        sql += " group by statdate,channel ORDER BY statdate,uid desc";

        Util.showSQL(sql, listParm.toArray());
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, listParm.toArray());
        return rowlst;
    }

    /**
     * 获取渠道质量list
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
        //String sql = "SELECT statdate,app,channel,sum(imei) as imei,sum(euid) as euid,sum(uid) as uid FROM `dd_app_dau_stat` WHERE app= ? AND statdate BETWEEN ? AND ? ";
        String sql = "select nt.*,(nt.cs/nt.uid) as rs "
                + "from ("
                + "		select dd.uid,dc.*,case when dd.uid >= 300 then "
                + "				(dishs * 10 + posts * 10 + replys * 10 + view_cooks*0.1 + pays * 100 ) "
                + "			else 0 "
                + "			end as cs "
                + " 	from ("
                + "			select * from dd_channel_stat where statdate BETWEEN ? AND ? "
                + "		) dc join ("
                + " 		select statdate,channel,sum(uid) as uid "
                + "				from dd_mau_stat where app = ? and statdate BETWEEN ? AND ? group by statdate,channel"
                + "		) dd on (dc.channel=dd.channel and dc.statdate=dd.statdate)"
                + ") nt ";
        List<String> listParm = new ArrayList<String>();
        listParm.add(startDate);
        listParm.add(endDate);
        listParm.add(appId);
        listParm.add(startDate);
        listParm.add(endDate);

        if (StringUtils.isNotBlank(channel)) {
            sql += " AND nt.channel = ? ";
            listParm.add(channel);
        }

        sql += "  ORDER BY nt.statdate,rs desc";

        Util.showSQL(sql, listParm.toArray());
        // List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new
        // Object[] { appId, startDate, endDate });
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, listParm.toArray());
        return rowlst;
    }

    public List<Map<String, Object>> queryVersListByApp(String startDate, String endDate, String appId) {
        String sql = "select statdate,app,vers,sum(uid) as uid "
                + "from dd_mau_stat "
                + "where statdate BETWEEN ? AND ? ";
        if (StringUtils.isNotBlank(appId)) {
            sql += "and app = ? ";
        }
        sql += "group by statdate,app,vers order by statdate,app,uid desc";
        List<String> listParm = new ArrayList<String>();
        //
        listParm.add(startDate);
        listParm.add(endDate);
        if (StringUtils.isNotBlank(appId)) {
            listParm.add(appId);
        }

        Util.showSQL(sql, listParm.toArray());
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, listParm.toArray());
        return rowlst;
    }

    /**
     * 获取某段时间内，某app的活跃用户总数
     *
     * @param startDate
     * @param endDate
     * @param app
     */
    public List<Map<String, Object>> queryActiveUserTotal(String startDate, String endDate, String... app) {
        String queryActiveUserTotalSQL = "SELECT statdate,sum(imei) as imei,sum(euid) as euid,sum(uid) as uid,sum(logins) as logins " +
                "FROM `dd_mau_stat` " +
                "WHERE statdate BETWEEN ? AND ? ";

        //
        String sql = "";
        for (int i = 0; i < app.length; i++) {
            if (sql.equals("")) {
                sql = " app=" + app[i];
            } else {
                sql += " or app=" + app[i];
            }
        }

        queryActiveUserTotalSQL += " and (" + sql + ")  GROUP BY statdate ORDER BY statdate ";

        Util.showSQL(queryActiveUserTotalSQL, new Object[]{startDate, endDate});

        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryActiveUserTotalSQL, new Object[]{startDate,
                endDate});

        return rowlst;
    }
}