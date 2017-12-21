package com.douguo.dc.mail.dao;

import com.douguo.dc.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("/quizeRplayDao")
public class QuizeRplayDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String,Object>> queryquizeRplayList(String startDate, String endDate){
        String sql="select * from dd_quizerplay_stat where statdate BETWEEN ? AND ? ORDER BY statdate ASC ";
        Util.showSQL(sql, new Object[]{});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }
    public List<Map<String,Object>> queryCommonList(String sql,String startDate, String endDate){
        Util.showSQL(sql, new Object[]{});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }
}
