package com.douguo.dc.cookcollect.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("cookCollectDao")
public class CookCollectDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> querySumList(String startDate, String endDate) {
		String sql = "SELECT cook_name,sum(collects) as collects FROM `dd_cook_collect_stat` WHERE statdate BETWEEN ? AND ? GROUP BY cook_name ORDER BY collects desc";
		Util.showSQL(sql, new Object[] { startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate });
		return rowlst;
	}

	public List<Map<String, Object>> queryCookNameList(String startDate, String endDate, String cookName) {
		String sql = "SELECT statdate,cook_name,sum(collects) as collects FROM `dd_cook_collect_stat` WHERE statdate BETWEEN ? AND ? and cook_name = ? GROUP BY statdate,cook_name ORDER BY statdate";
		Util.showSQL(sql, new Object[] { startDate, endDate, cookName });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate, cookName });
		return rowlst;
	}
}