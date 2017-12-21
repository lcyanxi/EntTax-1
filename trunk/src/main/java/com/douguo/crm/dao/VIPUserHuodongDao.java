package com.douguo.crm.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.common.base.common.Page;
import com.douguo.crm.model.VIPHuodong;
import com.douguo.crm.model.VIPUser;
import com.douguo.crm.model.VIPUserHuodong;
import com.douguo.dc.util.Util;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Repository("vipUserHuodongDao")
public class VIPUserHuodongDao {

    private static final Log loger = LogFactory.getLog(VIPUserHuodongDao.class);

    @Autowired
    private JdbcTemplate dgDataJdbcTemplate;
    private final String queryUserHuodong = "SELECT * FROM dg_user_huodong WHERE user_id = ? ;" ;
    
    public class VIPUserHuodongRowMapper implements RowMapper<VIPUserHuodong> {
        @Override
        public VIPUserHuodong mapRow(ResultSet rs, int rowNum) throws SQLException {
            VIPUserHuodong vipUserHuodong = new VIPUserHuodong();
            vipUserHuodong.setId(rs.getInt("id"));
            vipUserHuodong.setUserId(rs.getInt("user_id"));
            vipUserHuodong.setUserName(rs.getString("username"));
            vipUserHuodong.setNickName(rs.getString("nickname"));
            vipUserHuodong.setContent(rs.getString("content"));
            vipUserHuodong.setSex(rs.getString("sex"));
            vipUserHuodong.setFlag(rs.getInt("flag"));
            
            return vipUserHuodong;
        }
    }
    
    //获取特定用户的活动内容
    public List<?> getVipUserHuodong(int userid){
    	Object[] parms = new Object[]{userid};
        List<VIPUserHuodong> list = dgDataJdbcTemplate.query(queryUserHuodong, parms, new VIPUserHuodongRowMapper());
        if(0 == list.size()){
        	return new ArrayList<String>() ;
        } else {
	        VIPUserHuodong vipUserHuodong = list.get(0) ;
	        String JsonString = vipUserHuodong.getContent() ;
	        VIPHuodong[] vipHuodong = JSON.parseObject(JsonString, new TypeReference<VIPHuodong[]>(){}) ;
	        List<?> huodongList = Arrays.asList(vipHuodong) ;
	        return huodongList ;
        }
    }
    
}