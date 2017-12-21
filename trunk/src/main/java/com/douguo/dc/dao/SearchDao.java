package com.douguo.dc.dao;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("searchDao")
public class SearchDao {

	@Autowired
	private JdbcTemplate	jdbcTemplate;	
	
	public List<Map<String, Object>> selectSearchEmptyList(String searchDate,Integer clienttypeid) {
		String sql	= "SELECT * FROM `dd_search_empty_stat` WHERE searchdate= ? AND clienttypeid= ? ";
		Util.showSQL(sql, new Object[] { searchDate,clienttypeid });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { searchDate,clienttypeid });
		return rowlst;
	}
}
