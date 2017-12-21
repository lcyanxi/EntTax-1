package com.douguo.dc.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.douguo.dc.user.model.Function;

@Repository("functionDao")
public class FunctionDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String queryAllFunctionSQL = "SELECT id,function_id,function_name,uri,sort,visiable,type,type_id FROM tc_function ";

	private final String queryFunSQL = "select id,function_id,function_name,uri,sort,visiable,type,type_id from tc_function where function_id=?";
	private final String queryFunUriSQL = "select id,function_id,function_name,uri,sort,visiable,type,type_id from tc_function where uri=?";

	private final String insertFunSQL = "insert into tc_function (function_id,function_name,uri,type,type_id) values (?,?,?,?,?)";
	private final String updateFunSQL = "update tc_function set function_id=?,function_name=?,uri=?,type=?,type_id=? where function_id=?";

	/**
	 * 查询list
	 * 
	 * @param uid
	 * @return
	 */
	public List<Function> getFunctionByFunId(String functionId) {
		Object[] params = new Object[] { functionId };
		List<Function> list = jdbcTemplate.query(queryFunSQL, params, new FunctionRowMapper());
		return list;
	}

	public List<Function> getFunctionByUri(String uri) {
		Object[] params = new Object[] { uri };
		List<Function> list = jdbcTemplate.query(queryFunUriSQL, params, new FunctionRowMapper());
		return list;
	}

	/**
	 * 查询用权限List
	 * 
	 * @return
	 */
	public List<Function> queryAll() {
		List<Function> list = jdbcTemplate.query(queryAllFunctionSQL, new FunctionRowMapper());
		return list;
	}

	/**
	 * 新增
	 * 
	 * @param userid
	 * @param pass
	 * @return
	 */
	public boolean insert(Function fun) {
		int n = jdbcTemplate.update(insertFunSQL, fun.getFunctionId(), fun.getFunctionName(), fun.getUri(),
				fun.getType(), fun.getTypeId());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 更新
	 * 
	 * @param userid
	 * @param pass
	 * @return
	 */
	public boolean update(Function fun) {
		int n = jdbcTemplate.update(updateFunSQL, fun.getFunctionId(), fun.getFunctionName(), fun.getUri(),
				fun.getType(), fun.getTypeId(), fun.getFunctionId());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public class FunctionRowMapper implements RowMapper<Function> {

		@Override
		public Function mapRow(ResultSet rs, int rowNum) throws SQLException {
			Function fun = new Function();
			fun.setId(rs.getInt("id"));
			fun.setFunctionId(rs.getString("function_id"));
			fun.setFunctionName(rs.getString("function_name"));
			fun.setUri(rs.getString("uri"));
			fun.setSort(rs.getInt("sort"));
			fun.setVisiable(rs.getInt("visiable"));
			fun.setType(rs.getString("type"));
			fun.setTypeId(rs.getString("type_id"));
			return fun;
		}
	}

}
