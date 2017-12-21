package com.douguo.dc.userbehavior.dao;

import com.douguo.dc.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("userActionsStatDao")
public class UserActionsStatDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryUserActionsStatList(String statType, String userType, String timeType, String startDate, String endDate) {
        String sql = "SELECT * FROM `dd_user_actions_stat` WHERE stat_type = ? and user_type = ? and time_type = ? AND statdate BETWEEN ? AND ? ORDER BY statdate";
        Util.showSQL(sql, new Object[]{statType, userType, timeType, startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{statType, userType, timeType, startDate, endDate});
        return rowlst;
    }
}