package com.douguo.dc.mail.dao;

import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lichang on 2017/8/14.
 */
@Repository("indexFlushDao")
public class IndexFlushDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 首页刷新总数据查询
     * @return
     */
    public List<Map<String,Object>> queryIndexFlush(String sql){

        String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        String startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        String endDate = DateUtil.getSpecifiedDayBefore(today, 0);

        Util.showSQL(sql, new Object[]{});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    /**
     * 查询一段时间内的
     * @param sql
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> queryIndexFlushSumList(String sql,String startDate, String endDate) {
        Util.showSQL(sql, new Object[]{});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }


    public List<Map<String,Object>>  queryMainIndexInterstTagsList(){
        String sql="select * from dd_main_index_interst_tags_stat order by value desc";
        Util.showSQL(sql, new Object[]{});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
        return rowlst;
    }



    public List<Map<String,Object>>  queryMainIndexInterstTagsNewUserList(String startDate, String endDate){
        String sql="select * from dd_main_index_interst_tags_new_user_stat  WHERE statdate BETWEEN ? AND ? ";
        Util.showSQL(sql, new Object[]{});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }


    public List<Map<String,Object>>  queryMainIndexInterstTagsTotalList(){
        String sql="select sum(value) as total from dd_main_index_interst_tags_new_user_stat";
        Util.showSQL(sql, new Object[]{});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
        return rowlst;
    }
}
