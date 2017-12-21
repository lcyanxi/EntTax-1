package com.douguo.dc.stat.hour.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("commonHourStatDao")
public class CommonHourStatDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryCommonHourStatList(String type, String startDate, String endDate, String order) {
		String sql = "SELECT * FROM dd_common_hour_stat WHERE type = ? and statdate BETWEEN ? AND ? "
				+ " ORDER BY statdate";

		if (StringUtils.isNotEmpty(order)) {
			sql += "," + order;
		}

		Util.showSQL(sql, new Object[] { type, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { type, startDate, endDate });
		return rowlst;
	}
}