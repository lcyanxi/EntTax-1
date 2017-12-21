package com.douguo.dc.mall.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.mall.dao.MallCommonStatDao;

@Repository("mallCommonStatService")
public class MallCommonStatService {

	@Autowired
	private MallCommonStatDao mallCommonStatDao;

	public List<Map<String, Object>> queryList(String statType, String startDate, String endDate) {
		List<Map<String, Object>> keyRows = mallCommonStatDao.queryList(statType, startDate, endDate);
		return keyRows;
	}

	public List<Map<String, Object>> queryList(String statType, String statValue, String startDate, String endDate) {
		List<Map<String, Object>> keyRows = mallCommonStatDao.queryList(statType, statValue, startDate, endDate);
		return keyRows;
	}
}