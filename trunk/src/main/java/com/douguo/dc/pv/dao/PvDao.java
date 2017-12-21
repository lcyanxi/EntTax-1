package com.douguo.dc.pv.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("pvDao")
public class PvDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> querySumListByType(String startDate, String endDate, String type) {
		String sql = "SELECT statdate,type,sum(pv) as pv,sum(uv) as uv,sum(ip) as ip FROM `dd_pv_sum_stat` WHERE type= ? AND statdate BETWEEN ? AND ? GROUP BY statdate,type ORDER BY statdate,type desc";
		Util.showSQL(sql, new Object[] { type, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { type, startDate, endDate });
		return rowlst;
	}
	
	public List<Map<String, Object>> querySumList(String startDate, String endDate) {
		String sql = "SELECT statdate,type,sum(pv) as pv,sum(uv) as uv,sum(ip) as ip FROM `dd_pv_sum_stat` WHERE statdate BETWEEN ? AND ? GROUP BY statdate,type ORDER BY statdate,type desc";
		Util.showSQL(sql, new Object[] { startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate });
		return rowlst;
	}
}