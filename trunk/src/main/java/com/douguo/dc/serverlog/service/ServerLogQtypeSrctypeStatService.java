package com.douguo.dc.serverlog.service;

import com.douguo.dc.serverlog.dao.ServerLogQtypeSrctypeStatDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("serverLogQtypeSrctypeStatService")
public class ServerLogQtypeSrctypeStatService {

	@Autowired
	private ServerLogQtypeSrctypeStatDao serverLogQtypeSrctypeStatDao;
	
	public List<Map<String, Object>> querySumListByClientQtype(String startDate, String endDate, String... apps) {
		List<Map<String, Object>> keyRows = serverLogQtypeSrctypeStatDao.querySumListByClientQtype(startDate, endDate, apps);
		return keyRows;
	}
	
}
