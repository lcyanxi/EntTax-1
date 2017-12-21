package com.douguo.dc.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.dao.DayAreaDao;

@Repository("dayAreaService")
public class DayAreaService {

	@Autowired
	private DayAreaDao dayAreaDao;

	public List<Map<String, Object>> queryProvinceList(String startDate, String endDate, String type, Integer source) {
		List<Map<String, Object>> keyRows = dayAreaDao.queryProvinceList(startDate, endDate, type, source);
		return keyRows;
	}
	
	public List<Map<String, Object>> queryProvinceListLikeType(String startDate, String endDate, String type, Integer source) {
		List<Map<String, Object>> keyRows = dayAreaDao.queryProvinceListLikeType(startDate, endDate, type, source);
		return keyRows;
	}
}
