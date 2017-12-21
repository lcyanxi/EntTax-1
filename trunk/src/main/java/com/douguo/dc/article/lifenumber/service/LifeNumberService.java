package com.douguo.dc.article.lifenumber.service;

import com.douguo.dc.article.lifenumber.dao.LifeNumberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("lifeNumberService")
public class LifeNumberService {

    @Autowired
    private LifeNumberDao lifeNumberDao;

    public List<Map<String, Object>> queryLifeNumberList(String startDate, String endDate) {
        List<Map<String, Object>> rowlst = lifeNumberDao.queryLifeNumberList(startDate, endDate);
        return rowlst;
    }

}