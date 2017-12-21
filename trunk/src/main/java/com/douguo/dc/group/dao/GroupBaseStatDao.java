package com.douguo.dc.group.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("groupBaseStatDao")
public class GroupBaseStatDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryGroupDayList(String startDate, String endDate) {
		String sql = "SELECT statdate,group_id,sum(posts) as posts,sum(robot_posts) as robot_posts,sum(replies) as replies,sum(total_actions) as total_actions," +
				"		sum(total_users) as total_users,sum(pv) as pv,sum(uv) as uv,sum(group_pv) as group_pv,sum(group_uv) as group_uv," +
				"		sum(post_pv) as post_pv,sum(post_uv) as post_uv " +
				"	FROM `dd_group_base_stat` " +
				"	WHERE statdate BETWEEN ? AND ? " +
				"	GROUP BY statdate,group_id " +
				"	ORDER BY statdate,group_id";
		Util.showSQL(sql, new Object[] { startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate });
		
		return rowlst;
	}

	public List<Map<String, Object>> queryGroupDetailList(String groupId, String startDate, String endDate) {
		String sql = "SELECT statdate,group_id,sum(posts) as posts,sum(replies) as replies,sum(total_actions) as total_actions," +
				"		sum(total_users) as total_users,sum(pv) as pv,sum(uv) as uv,sum(group_pv) as group_pv,sum(group_uv) as group_uv," +
				"		sum(post_pv) as post_pv,sum(post_uv) as post_uv " +
				"	FROM `dd_group_base_stat` " +
				"	WHERE group_id=? and statdate BETWEEN ? AND ? " +
				"	GROUP BY statdate,group_id " +
				"	ORDER BY statdate,group_id";
		Util.showSQL(sql, new Object[] { groupId, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate
				.queryForList(sql, new Object[] { groupId, startDate, endDate });
		
		return rowlst;
	}

	public List<Map<String, Object>> queryGroupList(String startDate, String endDate) {
		String sql = "SELECT statdate,group_id,sum(posts) as posts,sum(robot_posts) as robot_posts,sum(replies) as replies,sum(total_actions) as total_actions," +
				"			sum(total_users) as total_users,sum(pv) as pv,sum(uv) as uv,sum(group_pv) as group_pv,sum(group_uv) as group_uv," +
				"			sum(post_pv) as post_pv,sum(post_uv) as post_uv,sum(first_posts) as first_posts,sum(first_post_users) as first_post_users,sum(repeat_posts) as repeat_posts," +
				"			sum(repeat_post_users) as repeat_post_users " +
				"		FROM `dd_group_base_stat` " +
				"		WHERE group_id='g' and statdate BETWEEN ? AND ? " +
				"		GROUP BY statdate " +
				"		ORDER BY statdate";
		Util.showSQL(sql, new Object[] { startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate });
		
		return rowlst;
	}
}