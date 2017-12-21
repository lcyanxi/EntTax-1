package com.douguo.dc.cook.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("cookDao")
public class CookDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryCookLevelList(String startDate, String endDate) {
		String sql = "SELECT statdate,level,cooks,users FROM `dd_cook_level_stat` WHERE statdate BETWEEN ? AND ? ORDER BY statdate,level";
		Util.showSQL(sql, new Object[] { startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate });
		return rowlst;
	}

    public List<Map<String, Object>> queryCookSumStatList(String startDate, String endDate) {
        String sql = "SELECT * FROM `dd_cook_sum_stat` WHERE statdate BETWEEN ? AND ? ORDER BY statdate";
        Util.showSQL(sql, new Object[] { startDate, endDate });
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate });
        return rowlst;
    }
}