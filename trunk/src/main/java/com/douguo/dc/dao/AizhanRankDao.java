package com.douguo.dc.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("aizhanRankDao")
public class AizhanRankDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryListByType(String startDate, String endDate, String type) {
		String sql = "SELECT statdate,keyword,querys,rank,dgrank FROM `dd_aizhan_rank_stat` WHERE type= ? AND statdate BETWEEN ? AND ? ORDER BY statdate,querys desc";
		Util.showSQL(sql, new Object[] { type, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { type, startDate, endDate });
		return rowlst;
	}
}
