package com.douguo.dc.spider.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.douguo.dc.spider.model.SpiderIp;
import com.douguo.dc.util.Util;

@Repository("spiderIpDao")
public class SpiderIpDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String querySQL = "select * from dd_spider_ip where id=?";
	private final String queryAllSQL = "SELECT * FROM dd_spider_ip ";
	private final String insertSQL = "insert into dd_spider_ip(ip,spider,ip_desc,create_time) values (?,?,?,?)";
	private final String updateSQL = "update dd_spider_ip set ip=?,spider=?,ip_desc=?,create_time=? where id=?";

	public List<Map<String, Object>> queryListBySpider(String spider) {
		String sql = "SELECT * FROM `dd_spider_ip` WHERE spider = ? ";
		Util.showSQL(sql, new Object[] { spider });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { spider });
		return rowlst;
	}

	public List<Map<String, Object>> queryList() {
		String sql = "SELECT * FROM `dd_spider_ip` ORDER BY spider";
		Util.showSQL(sql, new Object[] {});
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
		return rowlst;
	}

	public List<SpiderIp> getSpiderIp(String spiderIpId) {
		Object[] params = new Object[] { spiderIpId };
		List<SpiderIp> list = jdbcTemplate.query(querySQL, params, new SpiderIpRowMapper());
		return list;
	}

	/**
	 * 查询qtype List
	 * 
	 * @return
	 */
	public List<SpiderIp> queryAll() {
		List<SpiderIp> list = jdbcTemplate.query(queryAllSQL, new SpiderIpRowMapper());
		return list;
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public boolean insert(SpiderIp spiderIp) {
		int n = jdbcTemplate.update(insertSQL, spiderIp.getIp(), spiderIp.getSpider(),
				spiderIp.getIpDesc(), spiderIp.getCreateTime());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 更新
	 * 
	 * @return
	 */
	public boolean update(SpiderIp spiderIp) {
		int n = jdbcTemplate.update(updateSQL, spiderIp.getIp(), spiderIp.getSpider(),
				spiderIp.getIpDesc(), spiderIp.getCreateTime(), spiderIp.getId());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public class SpiderIpRowMapper implements RowMapper<SpiderIp> {

		@Override
		public SpiderIp mapRow(ResultSet rs, int rowNum) throws SQLException {
			SpiderIp fun = new SpiderIp();
			fun.setId(rs.getInt("id"));
			fun.setIp(rs.getString("ip"));
			fun.setSpider(rs.getString("spider"));
			fun.setIpDesc(rs.getString("ip_desc"));
			fun.setCreateTime(rs.getString("create_time"));
			return fun;
		}
	}
}