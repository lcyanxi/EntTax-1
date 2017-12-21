package com.douguo.dc.applog.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.douguo.dc.applog.model.AppLogClickDict;
import com.douguo.dc.util.Util;

@Repository("appLogClickDictDao")
public class AppLogClickDictDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String querySQL = "select id,page,view,position,title,title_desc from dd_app_log_click_dict where id=?";
	private final String queryAllSQL = "SELECT id,page,view,position,title,title_desc FROM dd_app_log_click_dict ";
	private final String insertSQL = "insert into dd_app_log_click_dict(id,page,view,position,title,title_desc) values (?,?,?,?,?,?)";
	private final String updateSQL = "update dd_app_log_click_dict set id=?,page=?,view=?,position=?,title=?,title_desc=? where id=?";

	public List<Map<String, Object>> queryListByPage(String page) {
		String sql = "SELECT page,view,position,title,title_desc FROM `dd_app_log_click_dict` WHERE page= ? ORDER BY view,position";
		Util.showSQL(sql, new Object[] { page });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { page });
		return rowlst;
	}

	public List<Map<String, Object>> queryList() {
		String sql = "SELECT page,view,position,title,title_desc FROM `dd_app_log_click_dict` ORDER BY page";
		Util.showSQL(sql, new Object[] {});
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
		return rowlst;
	}

	public List<AppLogClickDict> getAppLogClickDict(String appLogClickDictId) {
		Object[] params = new Object[] { appLogClickDictId };
		List<AppLogClickDict> list = jdbcTemplate.query(querySQL, params, new AppLogClickDictRowMapper());
		return list;
	}

	/**
	 * 查询用权限List
	 * 
	 * @return
	 */
	public List<AppLogClickDict> queryAll() {
		List<AppLogClickDict> list = jdbcTemplate.query(queryAllSQL, new AppLogClickDictRowMapper());
		return list;
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public boolean insert(AppLogClickDict appLogClickDict) {
		int n = jdbcTemplate.update(insertSQL, appLogClickDict.getId(), appLogClickDict.getPage(), appLogClickDict.getView(),
				appLogClickDict.getPosition(), appLogClickDict.getTitle(), appLogClickDict.getTitleDesc());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 更新
	 * 
	 * @return
	 */
	public boolean update(AppLogClickDict appLogClickDict) {
		int n = jdbcTemplate.update(updateSQL, appLogClickDict.getId(), appLogClickDict.getPage(), appLogClickDict.getView(),
				appLogClickDict.getPosition(), appLogClickDict.getTitle(), appLogClickDict.getTitleDesc(),appLogClickDict.getId());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public class AppLogClickDictRowMapper implements RowMapper<AppLogClickDict> {

		@Override
		public AppLogClickDict mapRow(ResultSet rs, int rowNum) throws SQLException {
			AppLogClickDict fun = new AppLogClickDict();
			fun.setId(rs.getInt("id"));
			fun.setPage(rs.getInt("page"));
			fun.setView(rs.getInt("view"));
			fun.setPosition(rs.getInt("position"));
			fun.setTitle(rs.getString("title"));
			fun.setTitleDesc(rs.getString("title_desc"));
			return fun;
		}
	}
}
