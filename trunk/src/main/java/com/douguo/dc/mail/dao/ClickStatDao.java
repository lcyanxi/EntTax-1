package com.douguo.dc.mail.dao;

import com.douguo.dc.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by lichang on 2017/10/24.
 */
@Repository("clickStatDao")
public class ClickStatDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String,Object>> queryClickList(String startDate, String endDate){
        String sql="select * from dd_main_index_flush_click_explosure_stat where statdate BETWEEN ? AND ? ORDER BY statdate DESC ";
        Util.showSQL(sql, new Object[]{});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }
}
