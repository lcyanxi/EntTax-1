package com.zyz.open.hiveadmin.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;
import com.zyz.open.hiveadmin.model.HiveAdminJob;

@Repository("hiveAdminJobDao")
public class HiveAdminJobDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String querySQL = "select * from dd_hiveadmin_job where id=?";
    private final String queryAllSQL = "SELECT * FROM dd_hiveadmin_job order by job_start_time desc";
    private final String insertSQL = "insert into dd_hiveadmin_job(job_name,uid,hql,hql_template,status,job_type,query_type,job_type_content,stat_begin_time,stat_end_time,job_start_time,update_time,create_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private final String updateSQL = "update dd_hiveadmin_job set uid=?,job_name=?,hql=?,hql_template=?,status=?,job_type=?,query_type=?,job_type_content=?,stat_begin_time=?,stat_end_time=?,job_start_time=?,update_time=? where id=?";
	

    public List<Map<String, Object>> queryListMapByUid(String uid) {
        String sql = "SELECT * FROM `dd_hiveadmin_job` WHERE uid= ? ORDER BY job_start_time desc,update_time desc";
        Util.showSQL(sql, new Object[]{uid});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{uid});
        return rowlst;
    }

    public List<HiveAdminJob> queryListByUid(String uid) {
        String sql = "SELECT * FROM `dd_hiveadmin_job` WHERE uid= ? ORDER BY update_time desc";
        Util.showSQL(sql, new Object[]{uid});
        List<HiveAdminJob> rowlst = jdbcTemplate.query(sql, new Object[]{uid}, new HiveAdminRowMapper());
        return rowlst;
    }

    public List<HiveAdminJob> getHiveAdminJob(String id) {
        Object[] params = new Object[]{id};
        List<HiveAdminJob> list = jdbcTemplate.query(querySQL, params, new HiveAdminRowMapper());
        return list;
    }
    
    public void deleteHiveAdminJob(String id) {
    	String deleteSQL = "delete from dd_hiveadmin_job where id=" +id ;
        Object[] params = new Object[]{id};
        jdbcTemplate.execute(deleteSQL);
    }

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

        Util.showSQL(sql, new Object[]{id, startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{id, startDate, endDate});
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
        Util.showSQL(sql, new Object[]{id, startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{id, startDate, endDate});
        return rowlst;
    }

    /**
     * 查询用权限List
     *
     * @return
     */
    public List<HiveAdminJob> queryAll() {
        List<HiveAdminJob> list = jdbcTemplate.query(queryAllSQL, new HiveAdminRowMapper());
        return list;
    }

    /**
     * 新增
     *
     * @return
     */
    public boolean insert(HiveAdminJob hiveAdminJob) {
        int n = jdbcTemplate.update(insertSQL, hiveAdminJob.getJobName(), hiveAdminJob.getUid(), hiveAdminJob.getHql(), hiveAdminJob.getHqlTemplate(),
                hiveAdminJob.getStatus(), hiveAdminJob.getJobType(), hiveAdminJob.getQueryType(),
                hiveAdminJob.getJobTypeContent(), hiveAdminJob.getStatBeginTime(), hiveAdminJob.getStatEndTime(),
                hiveAdminJob.getJobStartTime(), hiveAdminJob.getUpdateTime(), hiveAdminJob.getCreateTime());
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
    public boolean update(HiveAdminJob hiveAdminJob) {
        int n = jdbcTemplate.update(updateSQL, hiveAdminJob.getUid(), hiveAdminJob.getJobName(), hiveAdminJob.getHql(), hiveAdminJob.getHqlTemplate(),
                hiveAdminJob.getStatus(), hiveAdminJob.getJobType(), hiveAdminJob.getQueryType(),
                hiveAdminJob.getJobTypeContent(), hiveAdminJob.getStatBeginTime(), hiveAdminJob.getStatEndTime(),
                hiveAdminJob.getJobStartTime(), hiveAdminJob.getUpdateTime(), hiveAdminJob.getId());
        if (n > 0) {
            return true;
        } else {
            return false;
        }
    }

    public class HiveAdminRowMapper implements RowMapper<HiveAdminJob> {
        @Override
        public HiveAdminJob mapRow(ResultSet rs, int rowNum) throws SQLException {
            HiveAdminJob haJob = new HiveAdminJob();
            haJob.setId(rs.getInt("id"));
            haJob.setJobName(rs.getString("job_name"));
            haJob.setUid(rs.getString("uid"));
            haJob.setHql(rs.getString("hql"));
            haJob.setHqlTemplate(rs.getString("hql_template"));
            haJob.setStatus(rs.getInt("status"));
            haJob.setJobType(rs.getString("job_type"));
            haJob.setQueryType(rs.getString("query_type"));
            haJob.setJobTypeContent(rs.getString("job_type_content"));
            haJob.setStatBeginTime(rs.getString("stat_begin_time"));
            haJob.setStatEndTime(rs.getString("stat_end_time"));
            haJob.setJobStartTime(rs.getString("job_start_time"));
            haJob.setUpdateTime(rs.getString("update_time"));
            haJob.setCreateTime(rs.getString("create_time"));
            return haJob;
        }
    }
}