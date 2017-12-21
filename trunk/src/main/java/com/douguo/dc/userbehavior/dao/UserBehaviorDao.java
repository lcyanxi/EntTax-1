package com.douguo.dc.userbehavior.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("userBehaviorDao")
public class UserBehaviorDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> querySumListByQtype(String startDate, String endDate, String qtype) {
		String sql = "SELECT keyword,sum(counts) as counts FROM `dd_user_behavior_stat` WHERE qtype = ? AND statdate BETWEEN ? AND ? GROUP BY keyword ORDER BY counts desc";
		Util.showSQL(sql, new Object[] { qtype, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { qtype, startDate, endDate });
		return rowlst;
	}

	public List<Map<String, Object>> querySumList(String startDate, String endDate) {
		String sql = "SELECT keyword,sum(counts) as counts FROM `dd_user_behavior_stat` WHERE statdate BETWEEN ? AND ? GROUP BY keyword ORDER BY counts desc";
		Util.showSQL(sql, new Object[] { startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate });
		return rowlst;
	}

	public List<Map<String, Object>> queryKeywordList(String startDate, String endDate, String keyword) {
		String sql = "SELECT statdate,keyword,sum(counts) as counts FROM `dd_user_behavior_stat` WHERE statdate BETWEEN ? AND ? and keyword = ? GROUP BY statdate,keyword ORDER BY statdate";
		Util.showSQL(sql, new Object[] { startDate, endDate, keyword });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate, keyword });
		return rowlst;
	}

	public List<Map<String, Object>> queryKeywordListByQtype(String startDate, String endDate, String keyword,
			String qtype) {
		String sql = "SELECT statdate,keyword,sum(counts) as counts FROM `dd_user_behavior_stat` WHERE statdate BETWEEN ? AND ? and keyword = ? and qtype = ? GROUP BY statdate,keyword ORDER BY statdate";
		Util.showSQL(sql, new Object[] { startDate, endDate, keyword, qtype });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate, keyword,
				qtype });
		return rowlst;
	}
}