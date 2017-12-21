package com.douguo.dc.serverlog.service;

import com.douguo.dc.serverlog.dao.ServerLogQtypeStatDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("serverLogQtypeStatService")
public class ServerLogQtypeStatService {

	@Autowired
	private ServerLogQtypeStatDao serverLogQtypeStatDao;
	
	public List<Map<String, Object>> queryListByClient(String startDate, String endDate, String app) {
		List<Map<String, Object>> keyRows = serverLogQtypeStatDao.queryListByClient(startDate, endDate, app);
		return keyRows;
	}
	

	public List<Map<String, Object>> querySumListByClient(String startDate, String endDate, String... app) {
		List<Map<String, Object>> keyRows = serverLogQtypeStatDao.querySumListByClient(startDate, endDate, app);
		return keyRows;
	}
	

	public List<Map<String, Object>> querySumListByClientIgnoreqtype(String startDate, String endDate, String... app) {
		List<Map<String, Object>> keyRows = serverLogQtypeStatDao.querySumListByClientIgnoreqtype(startDate, endDate, app);
		return keyRows;
	}
	
}
