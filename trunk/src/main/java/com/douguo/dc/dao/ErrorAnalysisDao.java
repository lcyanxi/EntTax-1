package com.douguo.dc.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("ErrorAnalysisDao")
public class ErrorAnalysisDao {

	private JdbcTemplate	jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Map<String, Object>> getData(String appId, String startDate, String endDate, String time_type, String dimension_type, String dimension_name, String stat_key_id, String sort_type) {
		String format = "select * from stat_collection where app_id='%s' and stat_time between '%s' and '%s' and time_type='%s' and dimension_type='%s' and dimension_name='%s' and stat_key_id='%s' order by stat_time %s";
		String sql = String.format(format, appId, startDate, endDate, time_type, dimension_type, dimension_name, stat_key_id, sort_type);
		Util.showSQL(sql, new Object[] {});
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
		return rowlst;
	}
}
