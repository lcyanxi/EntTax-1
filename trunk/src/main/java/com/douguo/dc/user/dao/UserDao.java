package com.douguo.dc.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.douguo.dc.user.model.User;
import com.douguo.dc.user.utils.MD5Util;

@Repository("userDao")
public class UserDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	// insert into usersinfo (userID,pass,username) values (?,password(?),?)";
	private final String insertUserSQL = "INSERT INTO tc_user(uid,pass,username) values (?,?,?)";
	private final String updateUserSQL = "update tc_user set uid=?,username=? where uid=? ";
	private final String delUserSQL = "delete from tc_user where uid=?";
	private final String getUserSQL = "SELECT id,uid,pass,type,group_id,username FROM tc_user where uid=? ";
	private final String updateUserPassSQL = "update tc_user set pass=? where uid=?";

	/**
	 * 查询用户list
	 * 
	 * @param uid
	 * @return
	 */
	public List<User> getUserByUid(String uid) {
		Object[] params = new Object[] { uid };
		List<User> listUser = jdbcTemplate.query(getUserSQL, params, new UserRowMapper());
		System.out.println("listUser:" + listUser);
		return listUser;
	}

	/**
	 * 查询用户list
	 * 
	 * @param uid
	 * @return
	 */
	public List<User> queryUserList(String uid) {

		String selSql = "SELECT id,uid,username,pass FROM tc_user ";
		List<User> listUser = null;
		if (uid != null && !uid.equals("")) { // 是否要查询某个用户

			selSql += " where uid like ?";
			Object[] params = new Object[] { "%" + uid + "%" };
			listUser = jdbcTemplate.query(selSql, params, new UserRowMapper());
		} else {
			Object[] params = new Object[] {};
			listUser = jdbcTemplate.query(selSql, params, new UserRowMapper());
		}

		// Object[] params = new Object[] { uid };
		// List<User> listUser = jdbcTemplate.query(selSql, params, new
		// UserRowMapper());
		return listUser;
	}

	public class UserRowMapper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setUid(rs.getString("uid"));
			user.setUsername(rs.getString("username"));
			user.setPass(rs.getString("pass"));
			return user;
		}
	}

	/**
	 * 更新密码
	 * 
	 * @param userid
	 * @param pass
	 * @return
	 */
	public boolean updateUserPass(String uid, String pass) {
		int n = jdbcTemplate.update(updateUserPassSQL, MD5Util.MD5(pass), uid);
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 新增用户
	 * 
	 * @param userid
	 * @param pass
	 * @return
	 */
	public boolean insert(User user) {
		int n = jdbcTemplate.update(insertUserSQL, user.getUid(), MD5Util.MD5(user.getPass()), user.getUsername());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 更新用户
	 * 
	 * @param userid
	 * @param pass
	 * @return
	 */
	public boolean update(User user) {
		int n = jdbcTemplate.update(updateUserSQL, user.getUid(), user.getUsername(), user.getUid());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 更新用户
	 * 
	 * @param userid
	 * @param pass
	 * @return
	 */
	public boolean del(String uid) {
		int n = jdbcTemplate.update(delUserSQL, uid);
		String del_uf_sql = "delete from userfunctions where userID=?"; // 删除userfunctions表中关联信息
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

}
