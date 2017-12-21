package com.douguo.dc.mail.dao;

import com.douguo.dc.util.Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("/liveClassDao")
public class LiveClassDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<Map<String, Object>> queryList(String statType, String statValue, String startDate, String endDate) {
        String sql = "SELECT statdate,stat_value,orders,pays,moneys,income FROM `dd_live_class_common_stat` WHERE stat_type = ? and stat_value = ? and statdate BETWEEN ? AND ? ORDER BY statdate";
        System.out.println(sql);
        Util.showSQL(sql, new Object[] { statType, statValue, startDate, endDate });
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { statType, statValue,
                startDate, endDate });
        return rowlst;
    }

    public List<Map<String, Object>> queryProductList(String startDate, String endDate, String order) {
        if (StringUtils.isEmpty(order)) {
            order = "clicks";
        }

        String sql = "SELECT class_id,max(class_name) as class_name,sum(clicks) as clicks,sum(uids) as uids,sum(orders) as orders," +
                " sum(pays) as pays,sum(pay_uids) as pay_uids,sum(sub_class_num) as goods,sum(moneys) as moneys " +
                " FROM `dd_live_class_stat` WHERE statdate BETWEEN ? AND ? group by class_id ";
        if (StringUtils.isNotEmpty(order)) {
            sql += " order by " + order + " desc";
        }

        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }
}
