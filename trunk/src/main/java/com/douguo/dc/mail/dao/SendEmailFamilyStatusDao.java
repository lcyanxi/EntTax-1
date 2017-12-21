package com.douguo.dc.mail.dao;

import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("sendFamilyStatusDao")
public class SendEmailFamilyStatusDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询家庭数据汇总统计
     * @return
     */
    public List<Map<String,Object>> queryFamilyAllList(String startDate,String endDate){
        String sql=" SELECT family_num , member_num, cook_num," +
                " avg_family_members , avg_manage_days ," +
                "DATE_FORMAT (statdate, \"%Y-%m-%d\" ) AS statdate FROM  dd_family_status   WHERE statdate BETWEEN ? AND ?  GROUP BY" +
                " DATE_FORMAT (statdate, \"%Y-%m-%d\" ) ORDER BY statdate DESC";
        Util.showSQL(sql, new Object[]{});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    /**
     * 百分比统计查询
     * @return
     */
    public List<Map<String,Object>> queryByTypePrecentageList(){

        String sql="select dimention_type, dimention_value,dimention_name,DATE_FORMAT (statdate, \"%Y-%m-%d\") AS statdate" +
                " from dd_family_status_dimention " +
                "WHERE DATE_FORMAT (statdate, \"%Y-%m-%d\") = DATE_SUB(curdate(),INTERVAL 1 DAY) ORDER BY dimention_type";
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
        return  rowlst;
    }

}
