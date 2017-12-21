package com.douguo.dc.taskmonitor.dao;

import com.douguo.dc.taskmonitor.model.Operator;
import com.douguo.dc.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by lichang on 2017/10/23.
 */
@Repository("taskMonitorDao")
public class TaskMonitorDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Map<String, Object>> queryAllList(String startDate, String endDate) {
        String sql = "select * from dd_yunying_operation_stat where date(statdate) BETWEEN ? AND ?";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> queryAllListGroupName(String startDate, String endDate) {
        String sql = "select * from dd_yunying_operation_stat where date(statdate) BETWEEN ? AND ? order by name,statdate desc";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    // following method are all used in mail
    public List<Map<String, Object>> queryAllListBase(String startDate, String endDate) {
        String sql = "select * from dd_yunying_base_operation_stat where date(statdate) BETWEEN ? AND ?";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> queryAllListHuodong(String startDate, String endDate) {
        String sql = "select * from dd_yunying_hudong_operation_stat where date(statdate) BETWEEN ? AND ?";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> queryAllListDianshang(String startDate, String endDate) {
        String sql = "select * from dd_yunying_dianshang_operation_stat where date(statdate) BETWEEN ? AND ?";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> queryAllListWeek(String startDate, String endDate) {
        String sql = "select * from dd_yunying_week_stat where date(statdate) BETWEEN ? AND ?";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }


    /**
     * 显示用户列表
     *
     * @return
     */
    public List<Map<String, Object>> queryOperatorList(String sql) {
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
        return rowlst;
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    public int deleteUserById(int id,String sql) {
        return jdbcTemplate.update(sql, id);
    }


    public int addUser(Operator operator,String insertSQL) {
        Object[] parms = new Object[]{operator.getUserid(), operator.getName(), operator.getGroup(), operator.getWorkdesc(),
                operator.getCooks(), operator.getDishs(), operator.getPosts(), operator.getQuestions(), operator.getCaidans(),
                operator.getCook_comments(), operator.getDish_comments(), operator.getDish_likes(), operator.getPost_replys(),
                operator.getQuestions_replys(), operator.getMall(), operator.getLive()
        };
        Util.showSQL(insertSQL, parms);
        return jdbcTemplate.update(insertSQL, parms);
    }




    public int updateUser(Operator operator,String updateSQL){
        Object[] parms = new Object[]{operator.getUserid(), operator.getName(), operator.getGroup(), operator.getWorkdesc(),
                operator.getCooks(), operator.getDishs(), operator.getPosts(), operator.getQuestions(), operator.getCaidans(),
                operator.getCook_comments(), operator.getDish_comments(), operator.getDish_likes(), operator.getPost_replys(),
                operator.getQuestions_replys(), operator.getMall(), operator.getLive(),operator.getId()
        };
        return jdbcTemplate.update(updateSQL,parms);
    }

}

