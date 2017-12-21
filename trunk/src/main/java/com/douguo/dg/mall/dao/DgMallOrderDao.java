package com.douguo.dg.mall.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("dgMallOrderDao")
public class DgMallOrderDao {
    @Autowired
    private JdbcTemplate mallJdbcTemplate;

    public List<Map<String, Object>> getOrderList(String createdate) {
        String sql = "SELECT * FROM `dg_order` WHERE date(createtime)>=? and date(createtime)<=?";
        Util.showSQL(sql, new Object[]{createdate, createdate});
        List<Map<String, Object>> rowlst = mallJdbcTemplate.queryForList(sql, new Object[]{createdate, createdate});
        return rowlst;
    }
}