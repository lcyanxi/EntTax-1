package com.douguo.dc.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.douguo.dc.user.model.UserFunction;
import com.douguo.dc.util.Util;
import com.zyz.open.hiveadmin.dao.HiveAdminJobDao.HiveAdminRowMapper;
import com.zyz.open.hiveadmin.model.HiveAdminJob;

@Repository("userFunctionDao")
public class UserFunctionDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String insertUserFunSQL = "insert into tc_user_function (uid,function_id) values (?,?)";
	private final String delUserFunSQL = "delete from tc_user_function where uid=?;";

	/**
	 * 新增
	 * 
	 * @param userid
	 * @param pass
	 * @return
	 */
	public boolean batchInsert(List<UserFunction> listFun) {
		List<Object[]> list = new ArrayList<Object[]>();

		for (UserFunction uf : listFun) {
			Object[] obj = new Object[2];
			obj[0] = uf.getUid();
			obj[1] = uf.getFunctionId();

			list.add(obj);
		}
		int[] n = jdbcTemplate.batchUpdate(insertUserFunSQL, list);
		for (int rs : n) {
			if (rs <= 0) {
				return false;
			}
		}
		return true;
	}

	public boolean delByUid(String uid) {
		int n = jdbcTemplate.update(delUserFunSQL, uid);
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean addUserFun(String param, String param_add){
		List<Object[]> list = new ArrayList<Object[]>();
		
		Object[] obj = new Object[3];
		obj[0] = param ;
		obj[1] = param_add ;
		obj[2] = param ;
		list.add(obj);
		
		String addUserFunSql = "insert into tc_user_function (uid,function_id)  "
				+ "select uid," + param_add + " from tc_user_function where function_id="+param+" and "
				+ "uid not in (select uid from tc_user_function where function_id="+param_add+ " group by uid )" ;
		
		try {
			jdbcTemplate.update(addUserFunSql) ;
			return true;
		} catch (DataAccessException e) {
			e.printStackTrace();
			return false ;
		}
	}
	
	/**
	 * 根据uid查找function
	 * @param uid
	 * @return
	 */
	public List<UserFunction> queryUserFunctionByUid(String uid) {
        String sql = "SELECT * FROM `tc_user_function` WHERE uid= ?";
        Util.showSQL(sql, new Object[]{uid});
        List<UserFunction> rowlst = jdbcTemplate.query(sql, new Object[]{uid}, new UserFunctionRowMapper());
        return rowlst;
    }
	
	public class UserFunctionRowMapper implements RowMapper<UserFunction> {
        @Override
        public UserFunction mapRow(ResultSet rs, int rowNum) throws SQLException {
        	UserFunction uf = new UserFunction();
            uf.setId(rs.getInt("id"));
            uf.setUid(rs.getString("uid"));
            uf.setFunctionId(rs.getString("function_id"));
            return uf;
        }
    }

}
