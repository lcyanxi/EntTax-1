package com.douguo.dc.cookcollect.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.cookcollect.dao.CookCollectDao;

@Repository("cookCollectService")
public class CookCollectService {

	@Autowired
	private CookCollectDao cookCollectDao;

	public List<Map<String, Object>> querySumList(String startDate, String endDate) {
		List<Map<String, Object>> keyRows = cookCollectDao.querySumList(startDate, endDate);
		return keyRows;
	}

	public List<Map<String, Object>> queryCookNameList(String startDate, String endDate, String cookName) {
		List<Map<String, Object>> keyRows = cookCollectDao.queryCookNameList(startDate, endDate, cookName);
		return keyRows;
	}
}