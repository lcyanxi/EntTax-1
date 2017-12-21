package com.douguo.dc.serverlog.dao;

import com.douguo.dc.serverlog.model.ServerLogSrctypeDict;
import com.douguo.dc.taskmonitor.model.Operator;
import com.douguo.dc.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("serverLogSrctypeDictDao")
public class ServerLogSrctypeDictDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> querySrctypeList() {
        String sql = "SELECT * FROM `dd_server_log_srctype_dict` ORDER BY srctype";
        Util.showSQL(sql, new Object[]{});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
        return rowlst;
    }


    public int addSrctype(ServerLogSrctypeDict srctypeDict) {
        String sql = "insert into dd_server_log_srctype_dict(srctype,srctype_name,service,create_time,update_time,sdesc)VALUES (?,?,?,?,?,?)";
        Object[] parms = new Object[]{srctypeDict.getSrctype(), srctypeDict.getSrctypeName(), srctypeDict.getService(), new Date(),new Date(),
                srctypeDict.getSdesc()
        };
        Util.showSQL(sql, parms);
        return jdbcTemplate.update(sql, parms);
    }

    public int updateSrctype(ServerLogSrctypeDict srctypeDict) {
        String sql = "update dd_server_log_srctype_dict SET srctype =?,srctype_name=?,service=?,update_time=?,sdesc=?  where id=?";
        Object[] parms = new Object[]{srctypeDict.getSrctype(), srctypeDict.getSrctypeName(), srctypeDict.getService(), new Date(), srctypeDict.getSdesc(),
                srctypeDict.getId()
        };
        Util.showSQL(sql, parms);
        return jdbcTemplate.update(sql, parms);
    }

    public int deleteSrctype(int id) {
        String sql = "DELETE FROM  dd_server_log_srctype_dict WHERE id=?";
        return jdbcTemplate.update(sql, id);
    }
}
