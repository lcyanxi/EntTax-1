package com.douguo.dc.applog.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.applog.dao.AppWauDao;

@Repository("appWauService")
public class AppWauService {

    @Autowired
    private AppWauDao appWauDao;

    public List<Map<String, Object>> queryListByApp(String startDate, String endDate, String appId, String type) {
        List<Map<String, Object>> keyRows = appWauDao.queryListByApp(startDate, endDate, appId, type);
        return keyRows;
    }

    public List<Map<String, Object>> queryListByType(String startDate, String endDate, String type) {
        List<Map<String, Object>> keyRows = appWauDao.queryListByType(startDate, endDate, type);
        return keyRows;
    }

    public List<Map<String, Object>> querySumListByType(String startDate, String endDate, String type, String... apps) {
        List<Map<String, Object>> keyRows = appWauDao.querySumListByType(startDate, endDate, type, apps);
        return keyRows;
    }
}