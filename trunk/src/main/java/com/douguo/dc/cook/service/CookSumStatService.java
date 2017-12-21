package com.douguo.dc.cook.service;

import com.douguo.dc.cook.dao.CookSumStatDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("cookSumStatService")
public class CookSumStatService {

    @Autowired
    private CookSumStatDao cookSumStatDao;

    /**
     * 获取统计内容
     */
    public List<Map<String, Object>> queryCookSumStatList(String startDate, String endDate) {
        return cookSumStatDao.queryCookSumStatList(startDate, endDate) ;
    }
    
}