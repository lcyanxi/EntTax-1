package com.douguo.dc.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("UserEngagementDao")
public class UserEngagementDao {

	private JdbcTemplate	jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private final String	querySessionLengthDistributionSQL	= "select time_type, stat_value from duration where app_id=? and stat_time=? and dimension_type=? and dimension_name=? and stat_key_id=? and time_type like ? order by time_type asc;";
	private final String	querySessionDistributionSQL	= "select time_type, stat_value from duration where app_id=? and stat_time=? and dimension_type=? and dimension_name=? and stat_key_id=? and time_type like ? order by time_type asc;";

	public List<Map<String, Object>> querySessionLength(String appId, String statdate, String dimension_type, String dimension_name, String statKeyId) {
		Util.showSQL(querySessionLengthDistributionSQL, new Object[] { appId, statdate, dimension_type, dimension_name, statKeyId, "SINGLE%" });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(querySessionLengthDistributionSQL, new Object[] { appId, statdate, dimension_type, dimension_name, statKeyId, "SINGLE%" });
		return rowlst;
	}

	public List<Map<String, Object>> querySessionLengthDaily(String appId, String statdate, String dimension_type, String dimension_name, String statKeyId) {
		Util.showSQL(querySessionLengthDistributionSQL, new Object[] { appId, statdate, dimension_type, dimension_name, statKeyId, "DAILY%" });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(querySessionLengthDistributionSQL, new Object[] { appId, statdate, dimension_type, dimension_name, statKeyId, "DAILY%" });
		return rowlst;
	}

	public List<Map<String, Object>> querySessionDaily(String appId, String statdate, String dimension_type, String dimension_name, String statKeyId) {
		Util.showSQL(querySessionDistributionSQL, new Object[] { appId, statdate, dimension_type, dimension_name, statKeyId, "LAUNCH%" });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(querySessionDistributionSQL, new Object[] { appId, statdate, dimension_type, dimension_name, statKeyId, "LAUNCH%" });
		return rowlst;
	}
}
