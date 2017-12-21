package com.douguo.dc.channel.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.douguo.dc.channel.model.Channel;
import com.douguo.dc.util.Util;

@Repository("channelDao")
public class ChannelDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private final String querySQL = "select * from dd_app_channel_dict where id=?";
	private final String queryChannelDictSQL = "SELECT * FROM dd_app_channel_dict WHERE STATUS = ?;";
	private final String insertChannelDictSQL = "INSERT INTO dd_app_channel_dict (channel_code, channel_name, status ) VALUES (?, ?, 1)  ON DUPLICATE KEY UPDATE channel_name = ?, status=1 ;";
	private final String updateChannelStatusSQL = "UPDATE dd_app_channel_dict SET STATUS = 0 WHERE id = ? ;";
	private final String updateSQL = "update dd_app_channel_dict set channel_code=?,channel_name=?,user_name=?,principal=?,principal_dep=?,principal_contact=?,plan_desc=?,channel_cooperation=?,channel_plat=?,channel_type_1=?,channel_type_2=?,channel_type_3=?,channel_tag=? where id=?";

	public List<Map<String, Object>> queryChannelList(int status) {
		Util.showSQL(queryChannelDictSQL, new Object[] { status });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryChannelDictSQL, new Object[] { status });
		return rowlst;
	}
	
	public List<Channel> queryChannel(int status) {
		Util.showSQL(queryChannelDictSQL, new Object[] { status });
		List<Channel> rowlst = jdbcTemplate.query(queryChannelDictSQL, new Object[] { status },new ChannelRowMapper());
		return rowlst;
	}

	public List<Channel> getChannel(String id) {
		Object[] params = new Object[] { id };
		List<Channel> list = jdbcTemplate.query(querySQL, params, new ChannelRowMapper());
		return list;
	}

	public int insertChannelDict(String code, String name) {
		Util.showSQL(insertChannelDictSQL, new Object[] { code, name, name });
		return jdbcTemplate.update(insertChannelDictSQL, code, name, name);
	}

	/**
	 * 更新渠道状态
	 * 
	 * @param id
	 * @return
	 */

	public int updateChannelStatus(int id) {
		Util.showSQL(updateChannelStatusSQL, new Object[] { id });
		return jdbcTemplate.update(updateChannelStatusSQL, id);
	}

	/**
	 * 更新
	 * 
	 * @return
	 */
	public boolean update(Channel channel) {
		int n = jdbcTemplate.update(updateSQL, channel.getChannelCode(), channel.getChannelName(),channel.getUserName(),
				channel.getPrincipal(), channel.getPrincipalDep(), channel.getPrincipalContact(),
				channel.getPlanDesc(), channel.getChannelCooperation(), channel.getChannelPlat(),
				channel.getChannelType1(), channel.getChannelType2(), channel.getChannelType3(), channel.getChannelTag(),channel.getId());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public int getTotal(String tablename) {
		String totalSQL = "select count(*) from " + tablename;
		int total = 0;
		try {
			total = jdbcTemplate.queryForObject(totalSQL, java.lang.Integer.class);
		} catch (Exception e) {
			total = 0;
		}
		return total;
	}

	public class ChannelRowMapper implements RowMapper<Channel> {
		@Override
		public Channel mapRow(ResultSet rs, int rowNum) throws SQLException {
			Channel chanl = new Channel();
			chanl.setId(rs.getInt("id"));
			chanl.setChannelCode(rs.getString("channel_code"));
			chanl.setChannelName(rs.getString("channel_name"));
			chanl.setStatus(rs.getInt("status"));
			chanl.setUserName(rs.getString("user_name"));
			chanl.setPrincipal(rs.getString("principal"));
			chanl.setPrincipalDep(rs.getString("principal_dep"));
			chanl.setPrincipalContact(rs.getString("principal_contact"));
			chanl.setPlanDesc(rs.getString("plan_desc"));
			chanl.setChannelCooperation(rs.getString("channel_cooperation"));
			chanl.setChannelPlat(rs.getString("channel_plat"));
			chanl.setChannelType1(rs.getLong("channel_type_1"));
			chanl.setChannelType2(rs.getLong("channel_type_2"));
			chanl.setChannelType3(rs.getLong("channel_type_3"));
			chanl.setChannelTag(rs.getString("channel_tag"));
			return chanl;
		}
	}
}