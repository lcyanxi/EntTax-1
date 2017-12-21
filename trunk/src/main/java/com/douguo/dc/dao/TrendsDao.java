package com.douguo.dc.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("trendsDao")
public class TrendsDao {

	@Autowired
	private JdbcTemplate dgJdbcTemplate;

	private JdbcTemplate	jdbcTemplate;

	private final String	queryRetentionsSQL				= "select distinct t.time_type,t.stat_value from dd_app_user_retained_stat t where t.client=? and t.stat_key_id=? and t.statdate=? and t.dimension_type=? and binary t.dimension_name=? order by t.time_type asc;";
	private final String	queryRetentionsMonthSQL			= "select distinct t.time_type,t.stat_value from dd_app_user_retained_stat t where t.client=? and t.stat_key_id=? and DATE_FORMAT(date(t.statdate),'%Y-%m')=? and t.dimension_type=? order by t.time_type asc;";
	private final String	queryChannelsSQL				= "select distinct t.channel_code,t.channel_name from dd_app_channel_dict t where t.status='1';";
	private final String	queryVservionsSQL				= "select distinct t.app_version from dd_app_version_dict t where t.app_id=? and t.status='1' order by t.app_version asc;";
	private final String	queryNewUserTotalSQL			= "select t.stat_value from dd_app_collection_stat t where t.client=? and t.stat_key_id=? and t.statdate=? and t.time_type=? and t.dimension_type=? and binary t.dimension_name=?;";
	private final String	queryNewUserTotalMonthSQL		= "select sum(t.stat_value) as stat_value from dd_app_collection_stat t where t.client=? and t.stat_key_id=? and DATE_FORMAT(date(t.statdate),'%Y-%m')=? and t.time_type=? and t.dimension_type=?;";
	private final String	queryPeriodNewUserTotalSQL		= "select sum(cast(t.stat_value as signed)) from dd_app_collection_stat t where t.client=? and t.stat_key_id=? and t.statdate between ? and ? and t.time_type=? and t.dimension_type=? and t.dimension_name=?;";
	private final String	queryActiveUserTotalSQL			= "select t.stat_value from dd_app_collection_stat t where t.client=? and t.stat_key_id=? and t.statdate=? and t.time_type=? and t.dimension_type=? and t.dimension_name=?;";
	private final String	queryPeriodActiveUserTotalSQL	= "select sum(cast(t.stat_value as signed)) from dd_app_collection_stat t where t.client=? and t.stat_key_id=? and t.statdate between ? and ? and t.time_type=? and t.dimension_type=? and t.dimension_name=?;";
	private final String	queryLaunchesTotalSQL			= "select t.stat_value from dd_app_collection_stat t where t.client=? and t.stat_key_id=? and t.statdate=? and t.time_type=? and t.dimension_type=? and t.dimension_name=?;";
	private final String	queryPeriodLaunchesTotalSQL		= "select sum(cast(t.stat_value as signed)) from dd_app_collection_stat t where t.client=? and t.stat_key_id=? and t.statdate between ? and ? and t.time_type=? and t.dimension_type=? and t.dimension_name=?;";


	private final String    queryRegisterUserSQL            ="select date(registerdate) as  statdate,count(*) as totalNum from dg_userdetail WHERE  date(registerdate) BETWEEN ? AND ? GROUP BY statdate ORDER BY  statdate DESC ";

	public List<Map<String, Object>> queryRetentions(String appId, String statKeyId, String statdate, String dimension_type, String dimension_name, String page, String time_unit) {
		Util.showSQL(queryRetentionsSQL, new Object[] { appId, statKeyId, statdate, dimension_type, dimension_name });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryRetentionsSQL, new Object[] { appId, statKeyId, statdate, dimension_type, dimension_name });
		return rowlst;
	}

	public List<Map<String, Object>> queryRetentionsMonth(String appId, String statKeyId, String statdate, String dimension_type, String dimension_name, String page, String time_unit) {
		Util.showSQL(queryRetentionsMonthSQL, new Object[] { appId, statKeyId, statdate, dimension_type, dimension_name });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryRetentionsMonthSQL, new Object[] { appId, statKeyId, statdate, dimension_type });
		return rowlst;
	}
	public List<Map<String, Object>> queryChannels() {
		Util.showSQL(queryChannelsSQL, new Object[] {});
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryChannelsSQL, new Object[] {});
		return rowlst;
	}

	public List<Map<String, Object>> queryVersions(String client) {
		Util.showSQL(queryVservionsSQL, new Object[] { client });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryVservionsSQL, new Object[] { client });
		return rowlst;
	}

	/**
	 * 获取新增用户数 注意：当dimension_type="CHANNELID"需要将值转为CHANNEL, dd_app_collection_stat表中dimension_type字段存储的是CHANNEL
	 * 
	 * @param statdate
	 * @param dimension_type
	 * @param dimension_name
	 * @return
	 */
	public String getDayNewUserCount(String appId, String statKeyId, String statdate, String time_type, String dimension_type, String dimension_name) {
		if (dimension_type.equals("CHANNELID")) {
			dimension_type = "CHANNEL";
		}
		if (null == statKeyId || statKeyId.trim().equals("")) {
			statKeyId = "4";
		}
		Util.showSQL(queryNewUserTotalSQL, new Object[] { appId, statKeyId, statdate, time_type, dimension_type, dimension_name });
		String total = "0";
		try {
			total = jdbcTemplate.queryForObject(queryNewUserTotalSQL, new Object[] { appId, statKeyId, statdate, time_type, dimension_type, dimension_name }, java.lang.String.class);
		} catch (Exception e) {
		}
		if (null == total) {
			total = "0";
		} else {
			total = total.replaceAll("\n", "");
			if (total.trim().equals("")) {
				total = "0";
			}
		}
		return total;
	}


	/**
	 * 获取月新增用户数 注意：当dimension_type="CHANNELID"需要将值转为CHANNEL, dd_app_collection_stat表中dimension_type字段存储的是CHANNEL
	 *
	 * @param statdate
	 * @param dimension_type
	 * @param dimension_name
	 * @return
	 */
	public String getDayNewUserCountMonth(String appId, String statKeyId, String statdate, String time_type, String dimension_type, String dimension_name) {
		if (dimension_type.equals("CHANNELID")) {
			dimension_type = "CHANNEL";
		}
		if (null == statKeyId || statKeyId.trim().equals("")) {
			statKeyId = "4";
		}
		Util.showSQL(queryNewUserTotalMonthSQL, new Object[] { appId, statKeyId, statdate, time_type, dimension_type, dimension_name });
		String total = "0";
		try {
			total = jdbcTemplate.queryForObject(queryNewUserTotalMonthSQL, new Object[] { appId, statKeyId, statdate, time_type, dimension_type}, java.lang.String.class);
		} catch (Exception e) {
		}
		if (null == total) {
			total = "0";
		} else {
			total = total.replaceAll("\n", "");
			if (total.trim().equals("")) {
				total = "0";
			}
		}
		return total;
	}

	/**
	 * 获取一段时间内新增用户数总数 注意：当dimension_type="CHANNELID"需要将值转为CHANNEL, dd_app_collection_stat表中dimension_type字段存储的是CHANNEL
	 * 
	 * @param statdate
	 * @param dimension_type
	 * @param dimension_name
	 * @return
	 */
	public String getPeriodNewUserCount(String appId, String statKeyId, String starttime, String endtime, String time_type, String dimension_type, String dimension_name) {
		if (dimension_type.equals("CHANNELID")) {
			dimension_type = "CHANNEL";
		}
		if (null == statKeyId || statKeyId.trim().equals("")) {
			statKeyId = "4";
		}
		Util.showSQL(queryPeriodNewUserTotalSQL, new Object[] { appId, statKeyId, starttime, endtime, time_type, dimension_type, dimension_name });
		String total = "0";
		try {
			total = jdbcTemplate.queryForObject(queryPeriodNewUserTotalSQL, new Object[] { appId, statKeyId, starttime, endtime, time_type, dimension_type, dimension_name }, java.lang.String.class);
		} catch (Exception e) {
		}
		if (null == total) {
			total = "0";
		} else {
			total = total.replaceAll("\n", "");
			if (total.trim().equals("")) {
				total = "0";
			}
		}
		return total;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 获取活跃用户数 注意：当dimension_type="CHANNELID"需要将值转为CHANNEL, dd_app_collection_stat表中dimension_type字段存储的是CHANNEL
	 * 
	 * @param appId
	 * @param statKeyId
	 * @param stattime
	 * @param dimension_type
	 * @param dimension_name
	 * @return
	 */
	public String getDayActiveUserCount(String appId, String statKeyId, String stattime, String time_type, String dimension_type, String dimension_name) {
		if (dimension_type.equals("CHANNELID")) {
			dimension_type = "CHANNEL";
		}
		if (null == statKeyId || statKeyId.trim().equals("")) {
			statKeyId = "3";
		}
		Util.showSQL(queryActiveUserTotalSQL, new Object[] { appId, statKeyId, stattime, time_type, dimension_type, dimension_name });
		String total = "0";
		try {
			total = jdbcTemplate.queryForObject(queryActiveUserTotalSQL, new Object[] { appId, statKeyId, stattime, time_type, dimension_type, dimension_name }, java.lang.String.class);
		} catch (Exception e) {
		}
		if (null == total) {
			total = "0";
		} else {
			total = total.replaceAll("\n", "");
			if (total.trim().equals("")) {
				total = "0";
			}
		}
		return total;
	}

	/**
	 * 获取启动次数 注意：当dimension_type="CHANNELID"需要将值转为CHANNEL, dd_app_collection_stat表中dimension_type字段存储的是CHANNEL
	 * 
	 * @param appId
	 * @param statKeyId
	 * @param stattime
	 * @param dimension_type
	 * @param dimension_name
	 * @return
	 */
	public String getDayLaunchesCount(String appId, String statKeyId, String stattime, String time_type, String dimension_type, String dimension_name) {
		if (dimension_type.equals("CHANNELID")) {
			dimension_type = "CHANNEL";
		}
		if (null == statKeyId || statKeyId.trim().equals("")) {
			statKeyId = "1";
		}
		Util.showSQL(queryLaunchesTotalSQL, new Object[] { appId, statKeyId, stattime, time_type, dimension_type, dimension_name });
		String total = "0";
		try {
			total = jdbcTemplate.queryForObject(queryLaunchesTotalSQL, new Object[] { appId, statKeyId, stattime, time_type, dimension_type, dimension_name }, java.lang.String.class);
		} catch (Exception e) {
		}
		if (null == total) {
			total = "0";
		} else {
			total = total.replaceAll("\n", "");
			if (total.trim().equals("")) {
				total = "0";
			}
		}
		return total;
	}

	/**
	 * 获取一段时间内启动次数总数 注意：当dimension_type="CHANNELID"需要将值转为CHANNEL, dd_app_collection_stat表中dimension_type字段存储的是CHANNEL
	 * 
	 * @param appId
	 * @param statKeyId
	 * @param stattime
	 * @param dimension_type
	 * @param dimension_name
	 * @return
	 */
	public String getPeriodLaunchesCount(String appId, String statKeyId, String starttime, String endtime, String time_type, String dimension_type, String dimension_name) {
		if (dimension_type.equals("CHANNELID")) {
			dimension_type = "CHANNEL";
		}
		if (null == statKeyId || statKeyId.trim().equals("")) {
			statKeyId = "1";
		}
		Util.showSQL(queryPeriodLaunchesTotalSQL, new Object[] { appId, statKeyId, starttime, endtime, time_type, dimension_type, dimension_name });
		String total = "0";
		try {
			total = jdbcTemplate.queryForObject(queryPeriodLaunchesTotalSQL, new Object[] { appId, statKeyId, starttime, endtime, time_type, dimension_type, dimension_name }, java.lang.String.class);
		} catch (Exception e) {
		}
		if (null == total) {
			total = "0";
		} else {
			total = total.replaceAll("\n", "");
			if (total.trim().equals("")) {
				total = "0";
			}
		}
		return total;
	}

	/**
	 * 获取一段时间内活跃用户数总数 注意：当dimension_type="CHANNELID"需要将值转为CHANNEL, dd_app_collection_stat表中dimension_type字段存储的是CHANNEL
	 * 
	 * @param appId
	 * @param statKeyId
	 * @param stattime
	 * @param dimension_type
	 * @param dimension_name
	 * @return
	 */
	public String getPeriodActiveUserCount(String appId, String statKeyId, String starttime, String endtime, String time_type, String dimension_type, String dimension_name) {
		if (dimension_type.equals("CHANNELID")) {
			dimension_type = "CHANNEL";
		}
		if (null == statKeyId || statKeyId.trim().equals("")) {
			statKeyId = "3";
		}
		Util.showSQL(queryPeriodActiveUserTotalSQL, new Object[] { appId, statKeyId, starttime, endtime, time_type, dimension_type, dimension_name });
		String total = "0";
		try {
			total = jdbcTemplate.queryForObject(queryPeriodActiveUserTotalSQL, new Object[] { appId, statKeyId, starttime, endtime, time_type, dimension_type, dimension_name }, java.lang.String.class);
		} catch (Exception e) {
		}
		if (null == total) {
			total = "0";
		} else {
			total = total.replaceAll("\n", "");
			if (total.trim().equals("")) {
				total = "0";
			}
		}
		return total;
	}


	/**
	 * 查询新增注册用户
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Map<String,Object>>  queryRegisterUserList(String sql ,String startDate,String endDate){
		Util.showSQL(sql, new Object[] { startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate });
		return rowlst;
	}

}
