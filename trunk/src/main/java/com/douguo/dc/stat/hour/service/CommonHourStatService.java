package com.douguo.dc.stat.hour.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.stat.hour.dao.CommonHourStatDao;

@Repository("commonHourStatService")
public class CommonHourStatService {

	@Autowired
	private CommonHourStatDao commonHourStatDao;

	public List<Map<String, Object>> queryCommonHourStatList(String type, String startDate, String endDate, String order) {
		List<Map<String, Object>> keyRows = commonHourStatDao.queryCommonHourStatList(type, startDate, endDate, order);
		return keyRows;
	}
}