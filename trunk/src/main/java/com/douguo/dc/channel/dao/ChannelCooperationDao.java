package com.douguo.dc.channel.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.douguo.dc.channel.model.ChannelCooperation;
import com.douguo.dc.util.Util;

@Repository("channelCooperationDao")
public class ChannelCooperationDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String querySQL = "select * from dd_channel_cooperation where id=?";
	private final String queryAllSQL = "SELECT * FROM dd_channel_cooperation order by sort";
	private final String insertSQL = "insert into dd_channel_cooperation(cop_name,cop_desc,sort,create_time) values (?,?,?,?)";
	private final String updateSQL = "update dd_channel_cooperation set cop_name=?,cop_desc=?,sort=?,create_time=? where id=?";

	public List<Map<String, Object>> queryList() {
		String sql = "SELECT * FROM `dd_channel_cooperation` ORDER BY sort";
		Util.showSQL(sql, new Object[] {});
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
		return rowlst;
	}

	public List<ChannelCooperation> getChannelCooperation(String id) {
		Object[] params = new Object[] { id };
		List<ChannelCooperation> list = jdbcTemplate.query(querySQL, params, new ChannelCooperationRowMapper());
		return list;
	}

	/**
	 * 查询qtype List
	 * 
	 * @return
	 */
	public List<ChannelCooperation> queryAll() {
		List<ChannelCooperation> list = jdbcTemplate.query(queryAllSQL, new ChannelCooperationRowMapper());
		return list;
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public boolean insert(ChannelCooperation channelCop) {
		int n = jdbcTemplate.update(insertSQL, channelCop.getCopName(), channelCop.getCopDesc(),
				channelCop.getSort(), channelCop.getCreateTime());
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
	public boolean update(ChannelCooperation channelCop) {
		int n = jdbcTemplate.update(updateSQL, channelCop.getCopName(), channelCop.getCopDesc(),
				channelCop.getSort(), channelCop.getCreateTime(), channelCop.getId());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public class ChannelCooperationRowMapper implements RowMapper<ChannelCooperation> {
		@Override
		public ChannelCooperation mapRow(ResultSet rs, int rowNum) throws SQLException {
			ChannelCooperation cType = new ChannelCooperation();
			cType.setId(rs.getInt("id"));
			cType.setCopName(rs.getString("cop_name"));
			cType.setCopDesc(rs.getString("cop_desc"));
			cType.setSort(rs.getInt("sort"));
			cType.setCreateTime(rs.getString("create_time"));
			return cType;
		}
	}
}