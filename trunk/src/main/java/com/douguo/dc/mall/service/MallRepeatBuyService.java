package com.douguo.dc.mall.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.mall.dao.MallRepeatBuyDao;

@Repository("mallRepeatBuyService")
public class MallRepeatBuyService {
	@Autowired
	private MallRepeatBuyDao mallRepeatBuyDao;
	
	public List<Map<String, Object>> queryListByRepeatType(String startDate, String endDate, String repeatType) {
		List<Map<String, Object>> keyRows = mallRepeatBuyDao.queryListByRepeatType(startDate, endDate, repeatType);
		return keyRows;
	}
	
	public List<Map<String, Object>> queryListByStatType(String startDate, String endDate, String statType) {
		List<Map<String, Object>> keyRows = mallRepeatBuyDao.queryListByStatType(startDate, endDate, statType);
		return keyRows;
	}
}