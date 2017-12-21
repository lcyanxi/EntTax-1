package com.douguo.crm.service;

import com.common.base.common.Page;
import com.douguo.crm.dao.VIPUserDao;
import com.douguo.crm.dao.VIPUserHuodongDao;
import com.douguo.crm.dao.VIPUserHuodongDao.VIPUserHuodongRowMapper;
import com.douguo.crm.model.VIPUser;
import com.douguo.crm.model.VIPUserHuodong;
import com.douguo.dc.util.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("vipUserHuodongService")
public class VIPUserHuodongService {

    @Autowired
    private VIPUserHuodongDao vipUserHuodongDao;
    
    public List<?> getVipUserHuodong(int userid){
    	return vipUserHuodongDao.getVipUserHuodong(userid) ;
    }

}