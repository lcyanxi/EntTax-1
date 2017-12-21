package com.douguo.dc.channel.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.douguo.dc.channel.model.ChannelType;
import com.douguo.dc.util.Util;

@Repository("channelTypeDao")
public class ChannelTypeDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String querySQL = "select * from dd_channel_type where id=?";
	private final String queryAllSQL = "SELECT * FROM dd_channel_type ";
	private final String insertSQL = "insert into dd_channel_type(parent_id,type_name,type_desc,level,sort,create_time) values (?,?,?,?,?,?)";
	private final String updateSQL = "update dd_channel_type set parent_id=?,type_name=?,type_desc=?,level=?,sort=?,create_time=? where id=?";

	public List<ChannelType> queryListByLevel(String level) {
		String sql = "SELECT * FROM `dd_channel_type` WHERE level = ? order by level,parent_id,sort";
		Util.showSQL(sql, new Object[] { level });
		List<ChannelType> rowlst = jdbcTemplate.query(sql, new Object[] { level },new ChannelTypeRowMapper());
		return rowlst;
	}

	public List<Map<String, Object>> queryList() {
		String sql = "SELECT * FROM `dd_channel_type` ORDER BY level,sort";
		Util.showSQL(sql, new Object[] {});
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
		return rowlst;
	}

	public List<ChannelType> getChannelType(String channelTypeId) {
		Object[] params = new Object[] { channelTypeId };
		List<ChannelType> list = jdbcTemplate.query(querySQL, params, new ChannelTypeRowMapper());
		return list;
	}

	/**
	 * 查询qtype List
	 * 
	 * @return
	 */
	public List<ChannelType> queryAll() {
		List<ChannelType> list = jdbcTemplate.query(queryAllSQL, new ChannelTypeRowMapper());
		return list;
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public boolean insert(ChannelType channelType) {
		int n = jdbcTemplate.update(insertSQL, channelType.getParentId(), channelType.getTypeName(), channelType.getTypeDesc(),
				channelType.getLevel(), channelType.getSort(), channelType.getCreateTime());
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
	public boolean update(ChannelType channelType) {
		int n = jdbcTemplate.update(updateSQL, channelType.getParentId(), channelType.getTypeName(), channelType.getTypeDesc(),
				channelType.getLevel(), channelType.getSort(), channelType.getCreateTime(), channelType.getId());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public class ChannelTypeRowMapper implements RowMapper<ChannelType> {

		@Override
		public ChannelType mapRow(ResultSet rs, int rowNum) throws SQLException {
			ChannelType cType = new ChannelType();
			cType.setId(rs.getInt("id"));
			cType.setParentId(rs.getInt("parent_id"));
			cType.setTypeName(rs.getString("type_name"));
			cType.setTypeDesc(rs.getString("type_desc"));
			cType.setLevel(rs.getInt("level"));
			cType.setSort(rs.getInt("sort"));
			cType.setCreateTime(rs.getString("create_time"));
			return cType;
		}
	}
}