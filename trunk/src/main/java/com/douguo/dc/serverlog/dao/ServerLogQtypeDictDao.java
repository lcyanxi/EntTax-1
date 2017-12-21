package com.douguo.dc.serverlog.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.douguo.dc.serverlog.model.ServerLogQtypeDict;
import com.douguo.dc.util.Util;

@Repository("serverLogQtypeDictDao")
public class ServerLogQtypeDictDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String querySQL = "select * from dd_server_log_qtype_dict where id=?";
	private final String queryAllSQL = "SELECT * FROM dd_server_log_qtype_dict ";
	private final String insertSQL = "insert into dd_server_log_qtype_dict(id,qtype,qtype_name,service,create_time,qdesc) values (?,?,?,?,?,?)";
	private final String updateSQL = "update dd_server_log_qtype_dict set id=?,qtype=?,qtype_name=?,service=?,create_time=?,qdesc=? where id=?";

	public List<Map<String, Object>> queryListByService(String service) {
		String sql = "SELECT * FROM `dd_server_log_qtype_dict` WHERE service= ? ";
		Util.showSQL(sql, new Object[] { service });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { service });
		return rowlst;
	}

	public List<Map<String, Object>> queryList() {
		String sql = "SELECT * FROM `dd_server_log_qtype_dict` ORDER BY qtype";
		Util.showSQL(sql, new Object[] {});
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
		return rowlst;
	}

	public List<ServerLogQtypeDict> getServerLogQtypeDict(String qtypeDictId) {
		Object[] params = new Object[] { qtypeDictId };
		List<ServerLogQtypeDict> list = jdbcTemplate.query(querySQL, params, new ServerLogQtypeDictRowMapper());
		return list;
	}

	/**
	 * 查询qtype List
	 * 
	 * @return
	 */
	public List<ServerLogQtypeDict> queryAll() {
		List<ServerLogQtypeDict> list = jdbcTemplate.query(queryAllSQL, new ServerLogQtypeDictRowMapper());
		return list;
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public boolean insert(ServerLogQtypeDict qtypeDict) {
		int n = jdbcTemplate.update(insertSQL, qtypeDict.getId(), qtypeDict.getQtype(), qtypeDict.getQtypeName(),
				qtypeDict.getService(), qtypeDict.getCreateTime(), qtypeDict.getQdesc());
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
	public boolean update(ServerLogQtypeDict qtypeDict) {
		int n = jdbcTemplate.update(updateSQL, qtypeDict.getId(), qtypeDict.getQtype(), qtypeDict.getQtypeName(),
				qtypeDict.getService(), qtypeDict.getCreateTime(), qtypeDict.getQdesc(),qtypeDict.getId());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public class ServerLogQtypeDictRowMapper implements RowMapper<ServerLogQtypeDict> {

		@Override
		public ServerLogQtypeDict mapRow(ResultSet rs, int rowNum) throws SQLException {
			ServerLogQtypeDict fun = new ServerLogQtypeDict();
			fun.setId(rs.getInt("id"));
			fun.setQtype(rs.getString("qtype"));
			fun.setQtypeName(rs.getString("qtype_name"));
			fun.setService(rs.getString("service"));
			fun.setCreateTime(rs.getString("create_time"));
			fun.setQdesc(rs.getString("qdesc"));
			return fun;
		}
	}
}