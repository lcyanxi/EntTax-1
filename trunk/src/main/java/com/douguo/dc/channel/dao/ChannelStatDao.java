package com.douguo.dc.channel.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("channelStatDao")
public class ChannelStatDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryChannelStatList(String startDate, String endDate, String order) {
		String sql = "SELECT * FROM dd_channel_stat WHERE statdate BETWEEN ? AND ? " + " ORDER BY statdate";

		if (StringUtils.isNotEmpty(order)) {
			sql += "," + order;
		}

		Util.showSQL(sql, new Object[] { startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate });
		return rowlst;
	}
}