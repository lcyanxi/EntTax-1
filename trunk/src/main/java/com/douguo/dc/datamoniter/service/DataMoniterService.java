package com.douguo.dc.datamoniter.service;

import com.douguo.dc.datamoniter.dao.DataMoniterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("dataMoniterService")
public class DataMoniterService {

	@Autowired
	private DataMoniterDao dataMoniterDao;

	public List<Map<String, Object>> queryList(String startDate, String endDate) {
		List<Map<String, Object>> keyRows = dataMoniterDao.queryList(startDate, endDate);
		return keyRows;
	}

}
