package com.douguo.dc.dish.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.dish.dao.DishDao;

@Repository("dishService")
public class DishService {

    @Autowired
    private DishDao dishDao;

    public List<Map<String, Object>> queryDishTagList(String startDate, String endDate) {
        List<Map<String, Object>> keyRows = dishDao.queryDishTagList(startDate, endDate);
        return keyRows;
    }

    public List<Map<String, Object>> queryDishListBySource(String startDate, String endDate) {
        List<Map<String, Object>> keyRows = dishDao.queryDishListBySource(startDate, endDate);
        return keyRows;
    }

    public List<Map<String, Object>> queryDishListByWeek(String startDate, String endDate) {
        List<Map<String, Object>> keyRows = dishDao.queryDishListByWeek(startDate, endDate);
        return keyRows;
    }

    public List<Map<String, Object>> queryDishListByMonth(String startDate, String endDate) {
        List<Map<String, Object>> keyRows = dishDao.queryDishListByMonth(startDate, endDate);
        return keyRows;
    }

    /**
     * 作品相关统计
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String,Object>> queryDishSumList(String startDate,String endDate){
        List<Map<String, Object>> keyRows = dishDao.queryDishSumList(startDate, endDate);
        return keyRows;
    }
}