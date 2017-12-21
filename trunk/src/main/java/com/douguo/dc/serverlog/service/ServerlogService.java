package com.douguo.dc.serverlog.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.serverlog.dao.ServerlogDao;

@Repository("serverlogService")
public class ServerlogService {

	@Autowired
	private ServerlogDao serverlogDao;
	
	public List<Map<String, Object>> queryListByType(String startDate, String endDate, String qtype) {
		List<Map<String, Object>> keyRows = serverlogDao.queryListByType(startDate, endDate, qtype);
		return keyRows;
	}
	
	public List<Map<String, Object>> queryListByPlat(String startDate, String endDate, String plat) {
		List<Map<String, Object>> keyRows = serverlogDao.queryListByPlat(startDate, endDate, plat);
		return keyRows;
	}
	
	public List<Map<String, Object>> querySumListByPlat(String startDate, String endDate, String plat) {
		List<Map<String, Object>> keyRows = serverlogDao.querySumListByPlat(startDate, endDate, plat);
		return keyRows;
	}
	
	public List<Map<String, Object>> queryList(String startDate, String endDate) {
		List<Map<String, Object>> keyRows = serverlogDao.queryList(startDate, endDate);
		return keyRows;
	}
}
