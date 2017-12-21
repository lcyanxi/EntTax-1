package com.douguo.dc.userbehavior.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.userbehavior.dao.UserBehaviorDao;

@Repository("userBehaviorService")
public class UserBehaviorService {

	@Autowired
	private UserBehaviorDao userBehaviorDao;

	public List<Map<String, Object>> querySumListByQtype(String startDate, String endDate, String qtype) {
		List<Map<String, Object>> keyRows = userBehaviorDao.querySumListByQtype(startDate, endDate, qtype);
		return keyRows;
	}

	public List<Map<String, Object>> querySumList(String startDate, String endDate) {
		List<Map<String, Object>> keyRows = userBehaviorDao.querySumList(startDate, endDate);
		return keyRows;
	}

	public List<Map<String, Object>> queryKeywordList(String startDate, String endDate, String keyword) {
		List<Map<String, Object>> keyRows = userBehaviorDao.queryKeywordList(startDate, endDate, keyword);
		return keyRows;
	}

	public List<Map<String, Object>> queryKeywordListByQtype(String startDate, String endDate, String keyword,
			String qtype) {
		List<Map<String, Object>> keyRows = userBehaviorDao.queryKeywordListByQtype(startDate, endDate, keyword, qtype);
		return keyRows;
	}
}