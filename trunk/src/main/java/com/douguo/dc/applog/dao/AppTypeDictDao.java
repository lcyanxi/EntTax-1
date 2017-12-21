package com.douguo.dc.applog.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.douguo.dc.applog.model.AppTypeDict;
import com.douguo.dc.util.Util;

@Repository("appTypeDictDao")
public class AppTypeDictDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String querySQL = "select id,type,data,val,name,val_desc from dd_app_type_dict where id=?";
	private final String queryAllSQL = "SELECT id,type,data,val,name,val_desc FROM dd_app_type_dict ";
	private final String insertSQL = "insert into dd_app_type_dict(id,type,data,val,name,val_desc) values (?,?,?,?,?,?)";
	private final String updateSQL = "update dd_app_type_dict set id=?,type=?,data=?,val=?,name=?,val_desc=? where id=?";

	public List<Map<String, Object>> queryListByType(String type) {
		String sql = "SELECT type,data,val,name,val_desc FROM `dd_app_type_dict` WHERE type= ? ORDER BY type";
		Util.showSQL(sql, new Object[] { type });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { type });
		return rowlst;
	}

	public List<Map<String, Object>> queryList() {
		String sql = "SELECT type,data,val,name,val_desc FROM `dd_app_type_dict` ORDER BY type";
		Util.showSQL(sql, new Object[] {});
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
		return rowlst;
	}

	public List<AppTypeDict> getAppTypeDictById(String appTypeDictId) {
		Object[] params = new Object[] { appTypeDictId };
		List<AppTypeDict> list = jdbcTemplate.query(querySQL, params, new AppTypeDictRowMapper());
		return list;
	}

	/**
	 * 查询用权限List
	 * 
	 * @return
	 */
	public List<AppTypeDict> queryAll() {
		List<AppTypeDict> list = jdbcTemplate.query(queryAllSQL, new AppTypeDictRowMapper());
		return list;
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public boolean insert(AppTypeDict appTypeDict) {
		int n = jdbcTemplate.update(insertSQL, appTypeDict.getId(), appTypeDict.getType(), appTypeDict.getData(),
				appTypeDict.getVal(), appTypeDict.getName(), appTypeDict.getValDesc());
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
	public boolean update(AppTypeDict appTypeDict) {
		int n = jdbcTemplate.update(updateSQL, appTypeDict.getId(), appTypeDict.getType(), appTypeDict.getData(),
				appTypeDict.getVal(), appTypeDict.getName(), appTypeDict.getValDesc(),appTypeDict.getId());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public class AppTypeDictRowMapper implements RowMapper<AppTypeDict> {

		@Override
		public AppTypeDict mapRow(ResultSet rs, int rowNum) throws SQLException {
			AppTypeDict fun = new AppTypeDict();
			fun.setId(rs.getInt("id"));
			fun.setType(rs.getInt("type"));
			fun.setData(rs.getInt("data"));
			fun.setVal(rs.getInt("val"));
			fun.setName(rs.getString("Name"));
			fun.setValDesc(rs.getString("val_desc"));
			return fun;
		}
	}
}
