package com.douguo.dc.pv.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.pv.dao.PvDao;

@Repository("pvService")
public class PvService {

	@Autowired
	private PvDao pvDao;
	
	public List<Map<String, Object>> querySumListByType(String startDate, String endDate, String type) {
		List<Map<String, Object>> keyRows = pvDao.querySumListByType(startDate, endDate, type);
		return keyRows;
	}
	
	public List<Map<String, Object>> querySumList(String startDate, String endDate) {
		List<Map<String, Object>> keyRows = pvDao.querySumList(startDate, endDate);
		return keyRows;
	}
}