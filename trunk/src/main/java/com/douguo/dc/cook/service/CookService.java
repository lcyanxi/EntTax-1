package com.douguo.dc.cook.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.cook.dao.CookDao;

@Repository("cookService")
public class CookService {

	@Autowired
	private CookDao cookDao;

	public List<Map<String, Object>> queryCookLevelList(String startDate, String endDate) {
		List<Map<String, Object>> keyRows = cookDao.queryCookLevelList(startDate, endDate);
		return keyRows;
	}

	public List<Map<String, Object>> queryCookSumStatList(String startDate, String endDate) {
		List<Map<String, Object>> keyRows = cookDao.queryCookSumStatList(startDate, endDate);
		return keyRows;
	}
}