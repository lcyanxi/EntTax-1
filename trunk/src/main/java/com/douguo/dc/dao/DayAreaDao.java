package com.douguo.dc.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("dayAreaDao")
public class DayAreaDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryProvinceList(String startDate, String endDate, String type, Integer source) {
		String sql = "SELECT statdate,province,sum(num) as pnum,sum(usernum) as pusernum FROM `dd_day_area_stat` WHERE source= ? AND type = ? AND statdate BETWEEN ? AND ? group by statdate,province ORDER BY statdate,pnum desc";
		Util.showSQL(sql, new Object[] { source, type, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { source, type, startDate,
				endDate });
		return rowlst;
	}
	
	public List<Map<String, Object>> queryProvinceListLikeType(String startDate, String endDate, String type, Integer source) {
		String sql = "SELECT statdate,province,sum(num) as pnum,sum(usernum) as pusernum FROM `dd_day_area_stat` WHERE source= ? AND type like ? AND statdate BETWEEN ? AND ? group by statdate,province ORDER BY statdate,pnum desc";
		Util.showSQL(sql, new Object[] { source, "%" + type + "%", startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { source, "%"+type+"%", startDate,
				endDate });
		return rowlst;
	}
}
