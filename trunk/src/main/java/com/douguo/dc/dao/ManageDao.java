package com.douguo.dc.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("manageDao")
public class ManageDao {

	@Autowired
	private JdbcTemplate	jdbcTemplate;

	private final String	queryChannelDictSQL	= "SELECT * FROM dd_app_channel_dict WHERE STATUS = ?;";
	private final String	insertChannelDictSQL	= "INSERT INTO dd_app_channel_dict (channel_code, channel_name, status ) VALUES (?, ?, 1)  ON DUPLICATE KEY UPDATE channel_name = ?, status=1 ;";
	private final String	updateChannelDictSQL	= "UPDATE dd_app_channel_dict SET STATUS = 0 WHERE id = ? ;";

	private final String	queryAppsSQL		= "select id,name from dd_app where status=? order by id asc";

	public List<Map<String, Object>> queryChannelDict(int status) {
		Util.showSQL(queryChannelDictSQL, new Object[] { status });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryChannelDictSQL, new Object[] { status });
		return rowlst;
	}

	public int insertChannelDict(String code, String name) {
		Util.showSQL(insertChannelDictSQL, new Object[] { code, name, name });
		return jdbcTemplate.update(insertChannelDictSQL, code, name, name);
	}

	public int updateChannelDict(int id) {
		Util.showSQL(updateChannelDictSQL, new Object[] { id });
		return jdbcTemplate.update(updateChannelDictSQL, id);
	}

	public List<Map<String, Object>> queryEventsDictPage(int status, String sortname, String sortorder, int startRow, int pagesize, String qtype, String query) {
		String sql = "";
		if (null == qtype || null == query || qtype.trim().equals("") || query.trim().equals("")) {
			String format = "select * from dd_event_dic where status=%s order by %s %s limit %s,%s";
			sql = String.format(format, status, sortname, sortorder, startRow, pagesize);
		} else {
			/** 限定查询条件,即使用户更改客户端源代码改变查询条件也无效 */
			if (qtype.equals("id") || qtype.equals("event_name") || qtype.equals("event_code")) {
				String format = "select * from dd_event_dic where status=%s and %s like '%%%s%%' order by %s %s limit %s,%s";
				sql = String.format(format, status, qtype, query, sortname, sortorder, startRow, pagesize);
			}
		}
		if (sql.equals(""))
			return null;
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
		return rowlst;
	}

	public List<Map<String, Object>> queryAppDictPage(String sortname, String sortorder, int startRow, int pagesize, String qtype, String query) {
		String sql = "";
		if (sortname.equals("key")) {
			sortname = "`key`";
		}
		if (qtype.equals("key")) {
			qtype = "`key`";
		}
		if (null == qtype || null == query || qtype.trim().equals("") || query.trim().equals("")) {
			String format = "select * from dd_app where status='%s' order by %s %s limit %s,%s";
			sql = String.format(format, "NORMAL", sortname, sortorder, startRow, pagesize);
		} else {
			/** 限定查询条件,即使用户更改客户端源代码改变查询条件也无效 */
			if (qtype.equals("id") || qtype.equals("name") || qtype.equals("`key`")) {
				String format = "select * from dd_app where status='%s' and %s like '%%%s%%' order by %s %s limit %s,%s";
				sql = String.format(format, "NORMAL", qtype, query, sortname, sortorder, startRow, pagesize);
			}
		}
		if (sql.equals(""))
			return null;
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
		return rowlst;
	}

	public List<Map<String, Object>> queryVersionDictPage(int status, String sortname, String sortorder, int startRow, int pagesize, String qtype, String query) {
		String sql = "";
		if (null == qtype || null == query || qtype.trim().equals("") || query.trim().equals("")) {
			String format = "select * from dd_app_version_dict where status=%s order by %s %s limit %s,%s";
			sql = String.format(format, status, sortname, sortorder, startRow, pagesize);
		} else {
			/** 限定查询条件,即使用户更改客户端源代码改变查询条件也无效 */
			if (qtype.equals("id") || qtype.equals("app_version")) {
				String format = "select * from dd_app_version_dict where status=%s and %s like '%%%s%%' order by %s %s limit %s,%s";
				sql = String.format(format, status, qtype, query, sortname, sortorder, startRow, pagesize);
			}
		}
		if (sql.equals(""))
			return null;
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
		return rowlst;
	}

	public List<Map<String, Object>> queryAppsDict(String status) {
		Util.showSQL(queryAppsSQL, new Object[] { status });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryAppsSQL, new Object[] { status });
		return rowlst;
	}

	public int getTotal(String tablename) {
		String totalSQL = "select count(*) from " + tablename;
		int total = 0;
		try {
			total = jdbcTemplate.queryForObject(totalSQL, java.lang.Integer.class);
		} catch (Exception e) {
			total = 0;
		}
		return total;
	}

	public String insertEventDict(String event_name, String event_code, String app_id, String status) {
		String flag = "success";
		String sql = "insert into dd_event_dic (event_name, event_code, app_id, status ) values ('%s', '%s', %s, %s) ON DUPLICATE KEY UPDATE event_name='%s',event_code='%s',app_id=%s,status=%s";
		sql = String.format(sql, event_name, event_code, app_id, status, event_name, event_code, app_id, status);
		try {
			this.jdbcTemplate.execute(sql);
		} catch (Exception e) {
			flag = e.getMessage();
		}
		return flag;
	}

	public boolean deleteEventByIds(String ids) {
		boolean ret = false;
		String sql = "delete from dd_event_dic where id in (" + ids + ")";
		try {
			jdbcTemplate.execute(sql);
			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	public boolean deleteAppByIds(String ids) {
		boolean ret = false;
		String sql = "delete from dd_app where id in (" + ids + ")";
		try {
			jdbcTemplate.execute(sql);
			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	public String updateEventDict(String id, String event_name, String event_code, String app_id, String status) {
		String flag = "success";
		String sql = "update dd_event_dic set event_code='%s',event_name='%s',app_id=%s,status=%s where id=%s";
		sql = String.format(sql, event_code, event_name, app_id, status, id);
		try {
			this.jdbcTemplate.execute(sql);
		} catch (Exception e) {
			flag = e.getMessage() ;
		}
		return flag;
	}

	public String insertVersionDict(String app_id, String app_version, String status) {
		String flag = "success";
		String sql = "insert into dd_app_version_dict (app_id, app_version, status ) values ('%s', '%s', %s) ON DUPLICATE KEY UPDATE app_id='%s',app_version='%s',status=%s";
		sql = String.format(sql, app_id, app_version, status, app_id, app_version, status);
		try {
			this.jdbcTemplate.execute(sql);
		} catch (Exception e) {
			flag = e.getMessage();
		}
		return flag;
	}

	public boolean deleteVersionByIds(String ids) {
		boolean ret = false;
		String sql = "delete from dd_app_version_dict where id in (" + ids + ")";
		try {
			jdbcTemplate.execute(sql);
			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}

	public String updateVersionDict(String idStr, String app_id, String app_version, String status) {
		String flag = "success";
		String sql = "update dd_app_version_dict set app_id='%s',app_version='%s',status=%s where id=%s";
		sql = String.format(sql, app_id, app_version, status, idStr);
		try {
			this.jdbcTemplate.execute(sql);
		} catch (Exception e) {
			flag = e.getMessage();
		}
		return flag;
	}

	public String insertAppDict(String name, String key, String user_id) {
		String flag = "success";
		String sql = "insert into app (name, `key`, user_id, status ) values ('%s', '%s', %s, '%s') ON DUPLICATE KEY UPDATE name='%s',`key`='%s',user_id=%s,status='%s'";
		sql = String.format(sql, name, key, user_id, "NORMAL", name, key, user_id, "NORMAL");
		try {
			this.jdbcTemplate.execute(sql);
		} catch (Exception e) {
			flag = e.getMessage();
		}
		return flag;
	}

	public String updateAppDict(String idStr, String name, String key, String user_id) {
		String flag = "success";
		String sql = "update app set name='%s',`key`='%s',user_id=%s where id=%s";
		sql = String.format(sql, name, key, user_id, idStr);
		try {
			this.jdbcTemplate.execute(sql);
		} catch (Exception e) {
			flag = e.getMessage();
		}
		return flag;
	}
}
