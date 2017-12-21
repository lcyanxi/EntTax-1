package com.douguo.dc.datashow.dao;

import com.douguo.dc.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by lichang on 2017/8/22.
 */
@Repository
public class DataShowDao {
    @Autowired
    private JdbcTemplate dgDataJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String,Object>>  queryDimention(String startDate,String endDate){
        String sql="select dimention_type, dimention_value,dimention_name,DATE_FORMAT (statdate, \"%Y-%m-%d\") AS statdate" +
                " from dd_family_status_dimention " +
                "WHERE statdate BETWEEN ? AND ? ORDER BY dimention_type,dimention_value  ASC " ;
        Util.showSQL(sql, new Object[] { startDate, endDate });
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate });
        return rowlst;
    }

    public List<Map<String,Object>> queryDgData(){
        String sql="SELECT id ,code FROM dg_tj_map order by id desc limit 2";
        List<Map<String, Object>> rowlst = dgDataJdbcTemplate.queryForList(sql);
        return  rowlst;
    }

    /**
     * 每一秒取一次在线人数
     * @param statdate
     * @return
     */
    public List<Map<String,Object>> queryPersonNum(String statdate){
        String sql="SELECT num  FROM dd_person_num  WHERE statdate=?";
        Util.showSQL(sql, new Object[] { statdate });
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] {statdate });
        return rowlst;
    }
    /**
     * 插入实时在线人数
     * @param personNum
     * @param time
     * @return
     */
    public boolean inserPersonNum(int personNum,String time){
        String sql="insert into dd_person_num (num,statdate) values(%s,'%s')";
        sql = String.format(sql, personNum,time);
        try {
            this.jdbcTemplate.execute(sql);
            return true;
        } catch (Exception e) {
          e.printStackTrace();
        }
        return false;
    }


    /**
     * 删除指定日期的在线人数
     * @param startDate
     * @param endDate
     * @return
     */
    public boolean deletePersonNum(String startDate,String endDate){
        String sql="DELETE FROM dd_person_num WHERE  statdate >='"+startDate+"' AND  statdate <='"+endDate+"' ";
        System.out.println(sql);
        try {
            this.jdbcTemplate.execute(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



}
