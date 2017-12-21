package com.zyz.open.hiveadmin.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;
import com.zyz.open.hiveadmin.model.HiveAdminJob;

/**
 * 无效DAO
 */
@Repository("hiveAdminAdmDao")
public class HiveAdminAdmDao {
	
	private JdbcTemplate jdbcTemplate;

	private final String querySQL = "select id,job_name,uid,hql,status,job_type,job_type_content,stat_begin_time,stat_end_time,job_start_time,update_time,create_time from dd_hiveadmin_job where id=?";
	private final String queryAllSQL = "SELECT id,job_name,username,hql,status,job_type,job_type_content,stat_begin_time,stat_end_time,job_start_time,update_time,create_time FROM dd_hiveadmin_job ";

	private final String queryOrderCodeSQL = "SELECT ads_id FROM (`dg_orderrecode`) WHERE `orderitem_id` = ? AND `flag` = '0'";
	private final String queryPlatPageSQL = "SELECT platpage FROM (`dg_ads`) WHERE `id` IN (?)";
	private final String queryPageSQL = "SELECT fieldname FROM (`dg_page`) WHERE id IN (?)";
	

	
	public List<Map<String, Object>> queryListMapByUid(String uid) {
		String sql = "SELECT * FROM `dd_hiveadmin_job` WHERE uid= ? ORDER BY update_time desc";
		Util.showSQL(sql, new Object[] { uid });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { uid });
		return rowlst;
	}


	/**
	 * 根据焦点图id，查询pv&点击数据
	 * 
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> queryFocusList(String type,String startDate, String endDate, String id) {
		//String 
		String sql = "";
		if(type.equals("INDEX")){
			sql = "SELECT DATE(dwf.clicktime) as statdate,COUNT(dwf.id) as clicks,dpos.other_index_total as pv FROM douguo_data.dg_stat_adtimes_web_focus dwf left join douguo_data.dg_pv_other_stat dpos on DATE(dwf.clicktime)=dpos.statdate  WHERE dwf.ads_id=? and DATE(clicktime) BETWEEN ? AND ? GROUP BY DATE(clicktime)";
		}else if(type.equals("HUODONG")){
			sql = "SELECT DATE(dwf.clicktime) as statdate,COUNT(dwf.id) as clicks,dpos.huodong_index_total as pv FROM douguo_data.dg_stat_adtimes_web_focus dwf left join douguo_data.dg_pv_huodong_stat dpos on DATE(dwf.clicktime)=dpos.statdate  WHERE dwf.ads_id=? and DATE(dwf.clicktime) BETWEEN ? AND ? GROUP BY DATE(dwf.clicktime)";
		}else{//只显示点击数据
			sql = "SELECT DATE(clicktime) as statdate,COUNT(*) as clicks,0 as pv FROM douguo_data.dg_stat_adtimes_web_focus  WHERE ads_id=? and DATE(clicktime) BETWEEN ? AND ? GROUP BY DATE(clicktime)";
		}
		
		Util.showSQL(sql, new Object[] { id, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { id, startDate, endDate });
		return rowlst;
	}
	
	/**
	 * 根据广告id，查询pv&点击数据
	 * 
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> queryAdList(String startDate, String endDate, String id) {
		//String 
		String sql = "";
		sql = "SELECT DATE(dsaa.clicktime) as statdate,COUNT(dsaa.id) as clicks FROM douguo_data.dg_stat_adtimes_ads dsaa left join douguo_data.dg_pv_other_stat dpos on DATE(dsaa.clicktime)=dpos.statdate  WHERE dsaa.item_id=? and DATE(dsaa.clicktime) BETWEEN ? AND ? GROUP BY DATE(dsaa.clicktime)";
		//select date(clicktime),count(*) from dg_stat_adtimes_ads where item_id=14 group by date(clicktime);
		Util.showSQL(sql, new Object[] { id, startDate, endDate });
		List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { id, startDate, endDate });
		return rowlst;
	}
	

}