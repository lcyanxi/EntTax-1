package com.douguo.dc.serverlog.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("serverlogDao")
public class ServerlogDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryListByType(String startDate, String endDate, String qtype) {
		String sql = "SELECT statdate,plat,qtype,time_seq,times FROM `dd_serverlog_stat` WHERE qtype= ? AND statdate BETWEEN ? AND ? ORDER BY statdate,plat,qtype,time_seq,times desc";
		Util.showSQL(sql, new Object[] { qtype, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { qtype, startDate, endDate });
		return rowlst;
	}
	
	public List<Map<String, Object>> queryListByPlat(String startDate, String endDate, String plat) {
		String sql = "SELECT nt.statdate,nt.plat,nt.qtype,nt.newseq as time_seq,sum(nt.times) as times "
					+ "FROM (" 
					+ "select statdate,plat,qtype,case when time_seq <= 100 then 100 "
													+ "when time_seq <=500 then 500 "
													+ "when time_seq <=1000 then 1000 "
													+ "else 1001 "
													+ "end newseq,"
													+ "times "
													+ "from dd_serverlog_stat) nt "
													+ "WHERE nt.plat= ? AND nt.statdate BETWEEN ? AND ? "
													+ "group by nt.statdate,nt.plat,nt.qtype,nt.newseq "
													+ "ORDER BY nt.statdate,nt.plat,nt.qtype,nt.newseq desc";
		Util.showSQL(sql, new Object[] { plat, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { plat, startDate, endDate });
		return rowlst;
	}
	
	public List<Map<String, Object>> querySumListByPlat(String startDate, String endDate, String plat) {
		String sql = "SELECT statdate,plat,qtype,sum(times) as ntimes FROM `dd_serverlog_stat` WHERE plat= ? AND statdate BETWEEN ? AND ? group by statdate,plat,qtype ORDER BY statdate,plat,qtype desc";
		Util.showSQL(sql, new Object[] { plat, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { plat, startDate, endDate });
		return rowlst;
	}
	
	public List<Map<String, Object>> queryList(String startDate, String endDate) {
		//String sql = "SELECT statdate,plat,qtype,time_seq,times FROM `dd_serverlog_stat` WHERE statdate BETWEEN ? AND ? ORDER BY statdate,plat,qtype,time_seq,times desc";
		String sql = "SELECT nt.statdate,nt.plat,nt.qtype,nt.newseq as time_seq,sum(nt.times) as times FROM (select statdate,plat,qtype,case when time_seq <= 100 then 100 when time_seq <=1000 then 1000 else 2000 end newseq,times from dd_serverlog_stat) nt WHERE nt.statdate BETWEEN ? AND ? group by nt.statdate,nt.plat,nt.qtype,nt.newseq ORDER BY nt.statdate,nt.plat,nt.qtype,nt.newseq desc";
		Util.showSQL(sql, new Object[] { startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate });
		return rowlst;
	}
}
