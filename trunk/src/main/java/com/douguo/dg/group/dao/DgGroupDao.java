package com.douguo.dg.group.dao;

import com.douguo.dc.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("dgGroupDao")
public class DgGroupDao {
    @Autowired
    private JdbcTemplate groupJdbcTemplate;

    public List<Map<String, Object>> getGroupInfo(String gId) {
        String sql = "SELECT * FROM `dg_group` WHERE id = ? ";
        Util.showSQL(sql, new Object[]{gId});
        List<Map<String, Object>> rowlst = groupJdbcTemplate.queryForList(sql, new Object[]{gId});
        return rowlst;
    }

    public List<Map<String, Object>> getGroupList() {
        String sql = "SELECT * FROM `dg_group` where flag>=1 order by sortnum";
        Util.showSQL(sql, new Object[]{});
        List<Map<String, Object>> rowlst = groupJdbcTemplate.queryForList(sql, new Object[]{});
        return rowlst;
    }
}