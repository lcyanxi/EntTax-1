package com.douguo.dc.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("overviewDao")
public class OverviewDao {

	@Autowired
	private JdbcTemplate	jdbcTemplate;

	private final String	day2daySQL			= "SELECT stat_time,stat_value FROM stat_collection WHERE app_id=? and stat_key_id =? " + "AND time_type = ? AND dimension_type =?  AND stat_time  BETWEEN ? AND  ? ORDER BY stat_time ASC;";
	private final String	hour2hourSQL		= "SELECT hour,stat_value FROM stat_collection WHERE app_id=? and stat_key_id =? " + " AND time_type = \"HOUR\" AND dimension_type =?  " + " AND stat_time = ? " + " AND HOUR BETWEEN \"0\" AND  \"23\" ORDER BY HOUR ASC;";
	private final String	topByDimSQL			= "SELECT stat_time,dimension_name,stat_value FROM stat_collection " + "WHERE app_id=? and  time_type = ? AND dimension_type = ? AND stat_key_id=? AND " + " stat_time BETWEEN ? AND ? ORDER BY stat_time DESC;";
	private final String	valueInDaySQL		= "SELECT stat_key_id,stat_value FROM stat_collection" + " WHERE app_id=? and time_type = ? AND dimension_type =?  AND stat_time  = ? ;";
	private final String	queryQudaoDicSQL	= "SELECT qudao_code, qudao_name FROM qudao_dic WHERE STATUS = ?;";

	private final String	queryApps			= "select distinct t.id,t.name,t.key,t.user_id,t.status from dd_app t where t.status=? order by t.id;";

	public List<Map<String, Object>> queryAppModel(String status) {
		Util.showSQL(queryApps, new Object[] { status });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryApps, new Object[] { status });
		return rowlst;
	}

	public List<Map<String, Object>> queryQudaoDic(int status) {
		Util.showSQL(queryQudaoDicSQL, new Object[] { status });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryQudaoDicSQL, new Object[] { status });
		return rowlst;
	}

	public List<Map<String, Object>> queryDay2Day(String appId, String stats, String time_type, String dimension_type, String startDate, String endDate) {
		Util.showSQL(day2daySQL, new Object[] { appId, stats, time_type, dimension_type, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(day2daySQL, new Object[] { appId, stats, time_type, dimension_type, startDate, endDate });
		return rowlst;
	}

	public List<Map<String, Object>> queryHour2Hour(String appId, String stats, String dimension_type, String date) {
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(hour2hourSQL, new Object[] { appId, stats, dimension_type, date });
		Util.showSQL(hour2hourSQL, new Object[] { appId, stats, dimension_type, date });
		return rowlst;
	}

	public List<Map<String, Object>> queryTopByDim(String appId, String time_type, String dim, String stats, String startDate, String endDate) {
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(topByDimSQL, new Object[] { appId, time_type, dim, stats, startDate, endDate });
		Util.showSQL(topByDimSQL, new Object[] { appId, time_type, dim, stats, startDate, endDate });
		return rowlst;
	}

	public List<Map<String, Object>> queryValueInDay(String appId, String time_type, String dimension_type, String date) {
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(valueInDaySQL, new Object[] { appId, time_type, dimension_type, date });
		Util.showSQL(valueInDaySQL, new Object[] { appId, time_type, dimension_type, date });
		return rowlst;
	}
}
