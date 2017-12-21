package com.douguo.dc.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.dao.AizhanRankDao;

@Repository("aizhanRankService")
public class AizhanRankService {

	@Autowired
	private AizhanRankDao aizhanRankDao;
	
	public List<Map<String, Object>> queryListByType(String startDate, String endDate, String type) {
		List<Map<String, Object>> keyRows = aizhanRankDao.queryListByType(startDate, endDate, type);
		return keyRows;
	}
}
