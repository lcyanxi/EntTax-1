package com.douguo.dc.mall.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.mall.common.MallConstants;
import com.douguo.dc.util.Util;

@Repository("mallRepeatBuyDao")
public class MallRepeatBuyDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryListByRepeatType(String startDate, String endDate, String repeatType) {
		String sql = "SELECT statdate,stat_type,repeat_type,repeat_value FROM `dd_mall_repeat_buy_stat` WHERE repeat_type= ? AND statdate BETWEEN ? AND ? ORDER BY statdate,stat_type,repeat_type desc";
		Util.showSQL(sql, new Object[] { repeatType, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { repeatType, startDate, endDate });
		return rowlst;
	}
	
	public List<Map<String, Object>> queryListByStatType(String startDate, String endDate, String statType) {
		
		String sql = "";
		if(statType.equals(MallConstants.MALL_REPEAT_BUY_COMMON)){
			sql = "SELECT nt.id,nt.statdate,nt.stat_type,nt.rtype repeat_type,sum(nt.repeat_value) as repeat_value FROM (select id,statdate,stat_type,repeat_type as rtype,repeat_value from dd_mall_repeat_buy_stat) nt WHERE nt.stat_type= ? AND nt.statdate BETWEEN ? AND ? group by nt.statdate,nt.rtype ORDER BY nt.statdate,nt.id";
		}else{
			sql = "SELECT nt.id,nt.statdate,nt.stat_type,nt.rtype repeat_type,sum(nt.repeat_value) as repeat_value FROM (select id,statdate,stat_type,case when repeat_type > 5 then '5以上' else repeat_type end rtype,repeat_value from dd_mall_repeat_buy_stat) nt WHERE nt.stat_type= ? AND nt.statdate BETWEEN ? AND ? group by nt.statdate,nt.rtype ORDER BY nt.statdate,nt.id";
		}
		
		Util.showSQL(sql, new Object[] { statType, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { statType, startDate, endDate });
		return rowlst;
	}
}