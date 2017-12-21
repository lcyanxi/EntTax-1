package com.douguo.dc.article.lifenumber.dao;

import com.douguo.dc.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("lifeNumberDao")
public class LifeNumberDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryLifeNumberList(String startDate, String endDate) {
		String sql = "SELECT articles,articles_user_upload,articles_upload,articles_grab,quality_articles,all_users," +
				"apply_all_users,invite_all_users,article_views_pv,article_views_uv,article_user_follows," +
				"article_user_favs,article_user_cmmts,date(statdate) as statdate " +
				"FROM `dd_life_number_stat` WHERE date(statdate) BETWEEN ? AND ? ORDER BY date(statdate)";
		Util.showSQL(sql, new Object[] { startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate });
		return rowlst;
	}

}