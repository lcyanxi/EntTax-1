package com.douguo.dc.mall.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("mallPromotionSubjectStatDao")
public class MallPromotionSubjectStatDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryList( String startDate, String endDate) {
		String sql = "SELECT * FROM `dd_mall_promotion_subject_stat` WHERE statdate BETWEEN ? AND ? ORDER BY subject_id,statdate";
		Util.showSQL(sql, new Object[] { startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate
				.queryForList(sql, new Object[] { startDate, endDate });
		return rowlst;
	}
}