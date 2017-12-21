package com.zyz.open.hiveadmin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("hiveAdminOldAdmDao")
public class HiveAdminOldAdmDao {
	
	@Autowired
	private JdbcTemplate oldDouguoDataJdbcTemplate ;
	
	   /**
     * 根据焦点图id，查询pv&点击数据
     *
     * @param id
     * @return
     */
    public List<Map<String, Object>> queryFocusList(String type, String startDate, String endDate, String id) {
        // String
        String sql = "";
        if (type.equals("INDEX")) {
            sql = "SELECT DATE(dwf.clicktime) as statdate,COUNT(dwf.id) as clicks,dpos.other_index_total as pv FROM douguo_data.dg_stat_adtimes_web_focus dwf left join douguo_data.dg_pv_other_stat dpos on DATE(dwf.clicktime)=dpos.statdate  WHERE dwf.ads_id=? and DATE(clicktime) BETWEEN ? AND ? GROUP BY DATE(clicktime)";
        } else if (type.equals("HUODONG")) {
            sql = "SELECT DATE(dwf.clicktime) as statdate,COUNT(dwf.id) as clicks,dpos.huodong_index_total as pv FROM douguo_data.dg_stat_adtimes_web_focus dwf left join douguo_data.dg_pv_huodong_stat dpos on DATE(dwf.clicktime)=dpos.statdate  WHERE dwf.ads_id=? and DATE(dwf.clicktime) BETWEEN ? AND ? GROUP BY DATE(dwf.clicktime)";
        } else {// 只显示点击数据
            sql = "SELECT DATE(clicktime) as statdate,COUNT(*) as clicks,0 as pv FROM douguo_data.dg_stat_adtimes_web_focus  WHERE ads_id=? and DATE(clicktime) BETWEEN ? AND ? GROUP BY DATE(clicktime)";
        }
        
    	if( null == oldDouguoDataJdbcTemplate ){
    		System.out.println("ERROR: template is null");
    	} 

        Util.showSQL(sql, new Object[]{id, startDate, endDate});
        List<Map<String, Object>> rowlst = oldDouguoDataJdbcTemplate.queryForList(sql, new Object[]{id, startDate, endDate});
        return rowlst;
    }

    /**
     * 根据广告id，查询pv&点击数据
     *
     * @param id
     * @return
     */
    public List<Map<String, Object>> queryAdList(String startDate, String endDate, String id) {
        // String
        String sql = "";
        sql = "SELECT DATE(dsaa.clicktime) as statdate,COUNT(dsaa.id) as clicks FROM douguo_data.dg_stat_adtimes_ads dsaa left join douguo_data.dg_pv_other_stat dpos on DATE(dsaa.clicktime)=dpos.statdate  WHERE dsaa.item_id=? and DATE(dsaa.clicktime) BETWEEN ? AND ? GROUP BY DATE(dsaa.clicktime)";
        // select date(clicktime),count(*) from dg_stat_adtimes_ads where
        // item_id=14 group by date(clicktime);
        
    	if( null == oldDouguoDataJdbcTemplate ){
    		System.out.println("ERROR: template is null");
    	} 
        
        Util.showSQL(sql, new Object[]{id, startDate, endDate});
        List<Map<String, Object>> rowlst = oldDouguoDataJdbcTemplate.queryForList(sql, new Object[]{id, startDate, endDate});
        return rowlst;
    }
	

}