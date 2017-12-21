package com.douguo.dc.user.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("userMenuDao")
public class UserMenuDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 查询用户list
	 * 
	 * @param uid
	 * @return
	 */
	public List<Map<String, Object>> queryUserMenu(String uid, String type) {
		String strMenuSQL = "select m.id,m.parent_id,m.function_id,m.level,m.sort,m.visiable,f.function_name,f.uri from tc_menu m,tc_function f where f.function_id=m.function_id";
		if (type != null && "menu".equals(type)) {
			strMenuSQL += " and m.visiable = 1 and f.function_id in(select function_id from tc_user_function where uid = '"
					+ uid + "')";
		}
		// strMenuSQL += " order by f.typeID,level,sort";
		strMenuSQL += " order by type_id,level,sort";

		List<Map<String, Object>> listMenu = jdbcTemplate.queryForList(strMenuSQL);

		return listMenu;
	}

	/**
	 * 查询用户list
	 * 
	 * @param uid
	 * @return
	 */
	public List<Map<String, Object>> queryUserFunction(String uid) {
		// String strUserFunctionSQL =
		// "select `uid`,`function_id` from `tc_user_function` where uid = ?";
		// @ TODO 可优化
		String strUserFunctionSQL = "SELECT id,uri,function_id,function_name,sort,type,type_id FROM tc_function where function_id in (select function_id from tc_user_function where uid = ?) order by type_id,sort ";
//		jdbcTemplate.

		List<Map<String, Object>> listUserFunction = jdbcTemplate.queryForList(strUserFunctionSQL, uid);

		return listUserFunction;
	}

}
