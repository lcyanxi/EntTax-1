package com.douguo.dg.mall.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("dgGoodsDao")
public class DgGoodsDao {
	@Autowired
	private JdbcTemplate mallJdbcTemplate;

	public List<Map<String, Object>> getList(Integer id) {
		String sql = "SELECT * FROM `dg_goods` WHERE id = ? ";
		Util.showSQL(sql, new Object[] { id });
		List<Map<String, Object>> rowlst = mallJdbcTemplate.queryForList(sql, new Object[] {  id });
		return rowlst;
	}
	
	public List<Map<String, Object>> getAllList() {
		String sql = "SELECT * FROM `dg_goods`  ";
		Util.showSQL(sql, new Object[] { });
		List<Map<String, Object>> rowlst = mallJdbcTemplate.queryForList(sql, new Object[] { });
		return rowlst;
	}
	
	public List<Map<String, Object>> getCustomerGoodsList() {
		String sql = "select dg.*,dc.cate_name as f_cate_name,dcc.cate_name as cate_name,ds.store_name as store_name from dg_goods dg join dg_category dc on dg.f_category_id=dc.id join dg_category dcc on dg.category_id=dcc.id join dg_store ds on dg.user_id=ds.user_id";
		Util.showSQL(sql, new Object[] { });
		List<Map<String, Object>> rowlst = mallJdbcTemplate.queryForList(sql, new Object[] { });
		return rowlst;
	}
}