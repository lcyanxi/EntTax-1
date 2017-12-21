package com.douguo.dg.user.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("dgUserDao")
public class DgUserDao {
    @Autowired
    private JdbcTemplate dgJdbcTemplate;

    public List<Map<String, Object>> getUserByNickName(String nickName) {
        String sql = "SELECT * FROM `dg_user` WHERE nickname = ? ";
        Util.showSQL(sql, new Object[]{nickName});
        List<Map<String, Object>> rowlst = dgJdbcTemplate.queryForList(sql, new Object[]{nickName});
        return rowlst;
    }

    public List<Map<String, Object>> getUserByUserName(String userName) {
        String sql = "SELECT * FROM `dg_user` WHERE username = ? ";
        Util.showSQL(sql, new Object[]{userName});
        List<Map<String, Object>> rowlst = dgJdbcTemplate.queryForList(sql, new Object[]{userName});
        return rowlst;
    }

    public List<Map<String, Object>> getUserByUserId(String userId) {
        String sql = "SELECT * FROM `dg_user` WHERE user_id = ? ";
        Util.showSQL(sql, new Object[]{userId});
        List<Map<String, Object>> rowlst = dgJdbcTemplate.queryForList(sql, new Object[]{userId});
        return rowlst;
    }
}