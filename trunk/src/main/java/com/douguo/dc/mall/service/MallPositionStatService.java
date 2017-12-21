package com.douguo.dc.mall.service;

import com.douguo.dc.mall.dao.AppTuanDao;
import com.douguo.dc.mall.dao.MallPositionStatDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("mallPositionStatService")
public class MallPositionStatService {

    @Autowired
    private MallPositionStatDao mallPositionStatDao;

    public List<Map<String, Object>> queryList(String startDate, String endDate) {
        List<Map<String, Object>> keyRows = mallPositionStatDao.queryList(startDate, endDate);
        return keyRows;
    }

    public List<Map<String, Object>> queryListSumWithPageView(String startDate, String endDate, String order) {
        List<Map<String, Object>> keyRows = mallPositionStatDao.queryListSumWithPageView(startDate, endDate, order);
        return keyRows;
    }
}