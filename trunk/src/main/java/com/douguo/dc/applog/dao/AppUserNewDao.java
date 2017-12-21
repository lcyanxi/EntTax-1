package com.douguo.dc.applog.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("appUserNewDao")
public class AppUserNewDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryListByChannel(String startDate, String endDate, String channel) {
		String sql = "select dimension_name as channel,stat_value as uid,client,statdate from dd_app_collection_stat where statdate>=? and statdate<=? and time_type='TODAY' and dimension_type='CHANNEL' AND stat_key_id=4 ";
		List<String> listParm = new ArrayList<String>();
		listParm.add(startDate);
		listParm.add(endDate);

		if (StringUtils.isNotBlank(channel)) {
			sql += " AND dimension_name = ? ";
			listParm.add(channel);
		}

		sql += " order by statdate,client,uid desc;";

		Util.showSQL(sql, listParm.toArray());
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, listParm.toArray());
		return rowlst;
	}

	public List<Map<String, Object>> queryChannelList(String startDate, String endDate, String app) {
		String sql = "select dimension_name as channel,stat_value as uid,client,statdate from dd_app_collection_stat where statdate>=? and statdate<=? and time_type='TODAY' and dimension_type='CHANNEL' AND stat_key_id=4 ";
		List<String> listParm = new ArrayList<String>();
		listParm.add(startDate);
		listParm.add(endDate);

		if (StringUtils.isNotBlank(app)) {
			sql += " AND client = ? ";
			listParm.add(app);
		}

		sql += " order by statdate,client,uid desc;";

		Util.showSQL(sql, listParm.toArray());
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, listParm.toArray());
		return rowlst;
	}

	/**
	 * 获取某段时间内新增用户总量
	 * 
	 * @param startDate
	 * @param endDAte
	 * @param app
	 */
	public List<Map<String, Object>> getNewUserTotal(String startDate, String endDate, String... app) {
		String queryNewUserTotalSQL = "select t.statdate,sum(t.stat_value) as total_new_users from dd_app_collection_stat t "
				+ "	where t.stat_key_id=4 and t.statdate>=? and t.statdate<=? "
				+ " and t.time_type='TODAY' and t.dimension_type='ALL' and t.dimension_name='' ";
		//
		String sql = "";
		for (int i = 0; i < app.length; i++) {
			if (sql.equals("")) {
				sql = " t.client=" + app[i];
			} else {
				sql += " or t.client=" + app[i];
			}
		}
		
		queryNewUserTotalSQL += " and (" + sql + ") group by t.statdate";
		
		Util.showSQL(queryNewUserTotalSQL, new Object[] { startDate, endDate });
		
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryNewUserTotalSQL, new Object[] { startDate,
				endDate });

		return rowlst;
	}

	/**
	 * 获取某段时间内新增用户总量
	 * 
	 * @param startDate
	 * @param endDAte
	 * @param app
	 */
	public List<Map<String, Object>> getNewUserTotalForApp(String startDate, String endDate, String... app) {
		//
		String queryNewUserTotalSQL_app = "select t.statdate,t.client as app,t.stat_value as total_new_users from dd_app_collection_stat t "
				+ " where t.stat_key_id=4 and t.statdate>=? and t.statdate<=? "
				+ " and t.time_type='TODAY' and t.dimension_type='ALL' and t.dimension_name='' ";
		String sql = "";

		for (int i = 0; i < app.length; i++) {
			if (sql.equals("")) {
				sql = " t.client=" + app[i];
			} else {
				sql += " or t.client=" + app[i];
			}
		}
		//
		queryNewUserTotalSQL_app += " and (" + sql + ")";

		Util.showSQL(queryNewUserTotalSQL_app, new Object[] { startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryNewUserTotalSQL_app, new Object[] {
				startDate, endDate });
		return rowlst;
	}
}