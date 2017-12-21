package com.douguo.dc.applog.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("appClickDao")
public class AppClickDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 老版app type log 查询
	 * 
	 * @param startDate
	 * @param endDate
	 * @param type
	 * @param data1
	 * @return
	 */
	public List<Map<String, Object>> queryList(String startDate, String endDate, String type, String data1) {
		String sql = "SELECT statdate,type,data1,sum(clicks) as clicks,sum(uv) as uvs " + " FROM `dd_app_click_stat` "
				+ " WHERE statdate BETWEEN ? AND ? and type = ? ";

		if (data1 != null && !"".equals(data1)) {
			sql += " and data1 = ? group by statdate,type,data1 order by data1,statdate ";
			Util.showSQL(sql, new Object[] { startDate, endDate, type, data1 });
			List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate, type,
					data1 });
			return rowlst;
		} else {
			sql += " group by statdate,type,data1 order by data1,statdate ";
			Util.showSQL(sql, new Object[] { startDate, endDate, type });
			List<Map<String, Object>> rowlst = jdbcTemplate
					.queryForList(sql, new Object[] { startDate, endDate, type });
			return rowlst;
		}

	}

	/**
	 * 老版app type log 查询
	 * 
	 * @param startDate
	 * @param endDate
	 * @param type
	 * @param data1
	 * @return
	 */
	public List<Map<String, Object>> queryListData3(String startDate, String endDate, String type, String data1) {
		String sql = "SELECT statdate,type,data3,data1,sum(clicks) as clicks,sum(uv) as uvs "
				+ " FROM `dd_app_click_stat` " + " WHERE statdate BETWEEN ? AND ? and type = ? ";

		if (data1 != null && !"".equals(data1)) {
			sql += " and data1 = ? group by statdate,type,data3,data1 order by data3,statdate";
			Util.showSQL(sql, new Object[] { startDate, endDate, type, data1 });
			List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate, type,
					data1 });
			return rowlst;
		} else {
			sql += " group by statdate,type,data3,data1 order by data3,statdate";
			Util.showSQL(sql, new Object[] { startDate, endDate, type });
			List<Map<String, Object>> rowlst = jdbcTemplate
					.queryForList(sql, new Object[] { startDate, endDate, type });
			return rowlst;
		}

	}

	// 新版app type log 查询 从530版本开始的type=1001的日志，取data4进行解析

	/**
	 * 查询页面-点击数据;
	 * 
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param view
	 * @return
	 */
	public List<Map<String, Object>> queryPVList(String startDate, String endDate, String page) {
		String sql = "SELECT statdate,page,view,sum(clicks) as clicks,sum(uv) as uvs "
				+ " FROM `dd_app_log_click_pv_stat` " + " WHERE statdate BETWEEN ? AND ? and page = ? "
				+ " group by statdate,page,view order by page,view,statdate";

		Util.showSQL(sql, new Object[] { startDate, endDate, page });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate, page });
		return rowlst;

	}

	/**
	 * 查询页面-view-点击数据;
	 * 
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param view
	 * @return
	 */
	public List<Map<String, Object>> queryPVList(String startDate, String endDate, String page, String view) {
		String sql = "SELECT statdate,page,view,sum(clicks) as clicks,sum(uv) as uvs "
				+ " FROM `dd_app_log_click_pv_stat` " + " WHERE statdate BETWEEN ? AND ? and page = ? and view = ? "
				+ " group by statdate,page,view order by page,view,statdate";

		Util.showSQL(sql, new Object[] { startDate, endDate, page, view });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql,
				new Object[] { startDate, endDate, page, view });
		return rowlst;

	}

	/**
	 * 查询页面-位置-点击数据
	 * 
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param view
	 * @param position
	 * @return
	 */
	public List<Map<String, Object>> queryPVPoList(String startDate, String endDate, String page, String view) {
		String sql = "SELECT statdate,page,view,position,sum(clicks) as clicks,sum(uv) as uvs "
				+ " FROM `dd_app_log_click_pvpo_stat` " + " WHERE statdate BETWEEN ? AND ? and page = ? and view = ? "
				+ " group by statdate,page,view,position order by page,view,position,statdate ";

		Util.showSQL(sql, new Object[] { startDate, endDate, page, view });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql,
				new Object[] { startDate, endDate, page, view });
		return rowlst;

	}

	/**
	 * 查询页面-位置-点击数据
	 * 
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param view
	 * @param position
	 * @return
	 */
	public List<Map<String, Object>> queryPVPoList(String startDate, String endDate, String page, String view,
			String position) {
		String sql = "SELECT statdate,page,view,position,sum(clicks) as clicks,sum(uv) as uvs "
				+ " FROM `dd_app_log_click_pvpo_stat` "
				+ " WHERE statdate BETWEEN ? AND ? and page = ? and view = ? and position = ?"
				+ " group by statdate,page,view,position order by page,view,position,statdate ";

		Util.showSQL(sql, new Object[] { startDate, endDate, page, view, position });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate, page,
				view, position });
		return rowlst;

	}
}
