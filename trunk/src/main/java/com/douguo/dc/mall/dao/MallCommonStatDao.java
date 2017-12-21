package com.douguo.dc.mall.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("mallCommonStatDao")
public class MallCommonStatDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryList(String statType, String startDate, String endDate) {
		String sql = "SELECT statdate,stat_value,orders,pays,moneys,income FROM `dd_mall_common_stat` WHERE stat_type = ? and statdate BETWEEN ? AND ? ORDER BY statdate";
		Util.showSQL(sql, new Object[] { statType, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate
				.queryForList(sql, new Object[] { statType, startDate, endDate });
		return rowlst;
	}

	public List<Map<String, Object>> queryList(String statType, String statValue, String startDate, String endDate) {
		String sql = "SELECT statdate,stat_value,orders,pays,moneys,income FROM `dd_mall_common_stat` WHERE stat_type = ? and stat_value = ? and statdate BETWEEN ? AND ? ORDER BY statdate";
		Util.showSQL(sql, new Object[] { statType, statValue, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { statType, statValue,
				startDate, endDate });
		return rowlst;
	}
}