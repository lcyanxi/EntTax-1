package com.douguo.dc.mail.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("mailGaotieStatDao")
public class MailGaotieStatDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryList(String startDate, String endDate) {
		String sql = "SELECT statdate,pv,mobiles,use_conpon,total_conpon FROM `dd_tmp_gaotie_stat` WHERE statdate BETWEEN ? AND ? ORDER BY statdate";
		Util.showSQL(sql, new Object[] { startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate });
		return rowlst;
	}
}