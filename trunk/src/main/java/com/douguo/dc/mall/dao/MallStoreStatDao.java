package com.douguo.dc.mall.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("mallStoreStatDao")
public class MallStoreStatDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryStoreSumList(String startDate, String endDate, String order) {
		if (StringUtils.isEmpty(order)) {
			order = "moneys";
		}

		String sql = "SELECT store_id,max(store_name) as store_name,max(goods) as goods,max(goods_sale) as goods_sale,sum(pays) as pays,sum(goods_sum) as goods_sum,sum(moneys) as moneys,sum(pv) as pv,sum(uv) as uv FROM `dd_mall_store_stat` WHERE statdate BETWEEN ? AND ? group by store_id " ;
		if (StringUtils.isNotEmpty(order)) {
			sql += " order by " + order + " desc";
		}
		
		Util.showSQL(sql, new Object[] { startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate });
		return rowlst;
	}

	public List<Map<String, Object>> querySingleStoreList(String startDate, String endDate, String storeId,
			String order) {
		if (StringUtils.isEmpty(order)) {
			order = "statdate";
		}

		String sql = "SELECT * FROM `dd_mall_store_stat` WHERE statdate BETWEEN ? AND ? and store_id = ?"
				+ " ORDER BY " + order ;

		if (StringUtils.isNotEmpty(order)) {
			sql += "," + order + " desc";
		}

		Util.showSQL(sql, new Object[] { startDate, endDate, storeId });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate, storeId });
		return rowlst;
	}
	
}