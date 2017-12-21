package com.douguo.dc.user.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.user.model.Menu;

@Repository("menuDao")
public class MenuDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public boolean saveMenu(Menu menu) {
		String strEditSQL = "insert into tc_menu (id,parent_id,function_id,level,sort,visiable) values (?,?,?,?,?,?);";
		String strInsertSQL = "insert into tc_menu (parent_id,function_id,level,sort,visiable) values (?,?,?,?,?);";
		String strDelSQL = "delete from tc_menu where id=?";

		String menuId = menu.getId();

		String parentId = menu.getParentId();
		String functionId = menu.getFunctionId();
		String level = menu.getLevel();
		String sort = menu.getSort();
		String visiable = menu.getVisiable();
		int n = 0;
		if (null == menuId || "".equals(menuId)) {
			System.out.println("======insert menu ......");
			n = jdbcTemplate.update(strInsertSQL, parentId, functionId, level, sort, visiable);
		}else{
			jdbcTemplate.update(strDelSQL, menuId);
			n = jdbcTemplate.update(strEditSQL, menuId, parentId, functionId, level, sort, visiable);
			
		}
		
		return (n > 0) ? true : false;
	}

	/**
	 * 查询list
	 * 
	 * @param uid
	 * @return
	 */
	public List<Map<String, Object>> queryMenu() {
		String strMenuSQL = "select m.id,m.parent_id,m.function_id,m.level,m.sort,m.visiable,f.function_name from tc_menu m,tc_function f where f.function_id=m.function_id  order by f.type_id,level,sort;";

		return jdbcTemplate.queryForList(strMenuSQL);
	}

}
