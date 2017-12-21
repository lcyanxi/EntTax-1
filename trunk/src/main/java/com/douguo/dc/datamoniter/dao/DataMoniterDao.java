package com.douguo.dc.datamoniter.dao;

import com.douguo.dc.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>数据监控DAO接口，监控<strong>索引、数据表</strong>数据是否正常</p>
 * @author zhangjianfei
 * @since 1.0
 * @version 1.0
 */
@Repository("dataMoniterDao")
public class DataMoniterDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询所有的数据类型数据
     * @param startDate
     * @param endDate
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> queryList(String startDate, String endDate) {
        String sql = "SELECT DATE(statdate) as statdate, type, name, docs FROM `dd_data_monitor_stat` "
                + " WHERE DATE(statdate) BETWEEN ? AND ? order by statdate ASC limit 1000";

        Util.showSQL(sql, new Object[] { startDate, endDate });
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[] { startDate, endDate });
        return rowlst;
    }

}
