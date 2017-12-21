package com.douguo.dc.channel.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.douguo.dc.channel.model.ChannelPlat;
import com.douguo.dc.util.Util;

@Repository("channelPlatDao")
public class ChannelPlatDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String querySQL = "select * from dd_channel_plat where id=?";
	private final String queryAllSQL = "SELECT * FROM dd_channel_plat order by sort";
	private final String insertSQL = "insert into dd_channel_plat(plat_name,plat_desc,sort,create_time) values (?,?,?,?)";
	private final String updateSQL = "update dd_channel_plat set plat_name=?,plat_desc=?,sort=?,create_time=? where id=?";

	public List<Map<String, Object>> queryList() {
		String sql = "SELECT * FROM `dd_channel_plat` ORDER BY sort";
		Util.showSQL(sql, new Object[] {});
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
		return rowlst;
	}

	public List<ChannelPlat> getChannelPlat(String id) {
		Object[] params = new Object[] { id };
		List<ChannelPlat> list = jdbcTemplate.query(querySQL, params, new ChannelPlatRowMapper());
		return list;
	}

	/**
	 * 查询qtype List
	 * 
	 * @return
	 */
	public List<ChannelPlat> queryAll() {
		List<ChannelPlat> list = jdbcTemplate.query(queryAllSQL, new ChannelPlatRowMapper());
		return list;
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public boolean insert(ChannelPlat channelPlat) {
		int n = jdbcTemplate.update(insertSQL, channelPlat.getPlatName(), channelPlat.getPlatDesc(),
				channelPlat.getSort(), channelPlat.getCreateTime());
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
	public boolean update(ChannelPlat channelPlat) {
		int n = jdbcTemplate.update(updateSQL, channelPlat.getPlatName(), channelPlat.getPlatDesc(),
				channelPlat.getSort(), channelPlat.getCreateTime(), channelPlat.getId());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public class ChannelPlatRowMapper implements RowMapper<ChannelPlat> {
		@Override
		public ChannelPlat mapRow(ResultSet rs, int rowNum) throws SQLException {
			ChannelPlat cType = new ChannelPlat();
			cType.setId(rs.getInt("id"));
			cType.setPlatName(rs.getString("plat_name"));
			cType.setPlatDesc(rs.getString("plat_desc"));
			cType.setSort(rs.getInt("sort"));
			cType.setCreateTime(rs.getString("create_time"));
			return cType;
		}
	}
}