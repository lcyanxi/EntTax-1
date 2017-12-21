package com.douguo.dc.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.StatKey;
import com.douguo.dc.util.Util;

@Repository("TerminalDao")
public class TerminalDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private final String queryDevicesSQL = "select * from stat_collection where app_id=? and stat_time=? and time_type=? and dimension_type=? and stat_key_id=? order by stat_value*1 desc limit ?";

	public List<Map<String, Object>> getDevicesChartData(String appId, String statDate, String type,
			String dimension_type, String stat_key_id) {
		Util.showSQL(queryDevicesSQL, new Object[] { appId, statDate, type, dimension_type, stat_key_id, 10 });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryDevicesSQL, new Object[] { appId, statDate,
				type, dimension_type, stat_key_id, 10 });
		return rowlst;
	}

	public List<Map<String, Object>> getDevicesTableData(int startRow, int pagesize, String appId, String statDate,
			String time_type, String dimension_type, String stat_key_id) {
		String sql = "";
		if (stat_key_id.equals(StatKey.TerminalDevicesUV)) {
			sql = "select * from stat_collection where app_id='%s' and stat_time='%s' and time_type='%s' and dimension_type='%s' and stat_key_id='%s' order by stat_value*1 desc limit %s,%s";
			sql = String.format(sql, appId, statDate, time_type, dimension_type, stat_key_id, startRow, pagesize);
		} else if (stat_key_id.equals(StatKey.TerminalDevicesPV)) {
			sql = "select * from stat_collection where app_id='%s' and stat_time='%s' and time_type='%s' and dimension_type='%s' and stat_key_id='%s' order by stat_value*1 desc";
			sql = String.format(sql, appId, statDate, time_type, dimension_type, stat_key_id);
		} else {
			return null;
		}
		System.out.println(sql);
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
		return rowlst;
	}

	public String getSumVal(String appId, String statDate, String time_type, String dimension_type, String stat_key_id) {
		String sumval = "0";
		try {
			String sql = "select sum(stat_value) as value  from stat_collection where app_id='%s' and stat_time='%s' and time_type='%s' and dimension_type='%s' and stat_key_id='%s'";
			sql = String.format(sql, appId, statDate, time_type, dimension_type, stat_key_id);
			sumval = jdbcTemplate.queryForObject(sql, java.lang.String.class);
		} catch (Exception e) {
		}
		if (null == sumval) {
			sumval = "0";
		} else {
			sumval = sumval.replaceAll("\n", "");
			if (sumval.trim().equals("")) {
				sumval = "0";
			}
		}
		return sumval;
	}

	public String getTotal(String appId, String statDate, String time_type, String dimension_type, String stat_key_id) {
		String total = "0";
		try {
			String sql = "select count(*) as value from stat_collection where app_id='%s' and stat_time='%s' and time_type='%s' and dimension_type='%s' and stat_key_id='%s'";
			sql = String.format(sql, appId, statDate, time_type, dimension_type, stat_key_id);
			total = jdbcTemplate.queryForObject(sql, java.lang.String.class);
		} catch (Exception e) {
		}
		if (null == total) {
			total = "0";
		} else {
			total = total.replaceAll("\n", "");
			if (total.trim().equals("")) {
				total = "0";
			}
		}
		return total;
	}

	public String getLocationName(String nm) {
		String name = nm;
		try {
			String sql = "select name from location_code where code = ?";
			name = jdbcTemplate.queryForObject(sql, new Object[] { nm }, String.class);
		} catch (Exception e) {
		}
		return name;
	}
}
