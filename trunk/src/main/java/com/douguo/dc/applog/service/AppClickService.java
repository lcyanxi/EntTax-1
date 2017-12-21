package com.douguo.dc.applog.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.applog.dao.AppClickDao;

@Repository("appClickService")
public class AppClickService {

	@Autowired
	private AppClickDao appClickDao;

	public List<Map<String, Object>> queryList(String startDate, String endDate, String type, String data1) {
		List<Map<String, Object>> keyRows = appClickDao.queryList(startDate, endDate, type, data1);
		return keyRows;
	}

	public List<Map<String, Object>> queryListData3(String startDate, String endDate, String type, String data1) {
		List<Map<String, Object>> keyRows = appClickDao.queryListData3(startDate, endDate, type, data1);
		return keyRows;
	}

	// 新版app type log 查询 从530版本开始的type=1001的日志，取data4进行解析

	public List<Map<String, Object>> queryPVList(String startDate, String endDate, String page) {
		List<Map<String, Object>> keyRows = appClickDao.queryPVList(startDate, endDate, page);
		return keyRows;
	}
	
	public List<Map<String, Object>> queryPVList(String startDate, String endDate, String page, String view) {
		List<Map<String, Object>> keyRows = appClickDao.queryPVList(startDate, endDate, page, view);
		return keyRows;
	}

	public List<Map<String, Object>> queryPVPoList(String startDate, String endDate, String page, String view) {
		List<Map<String, Object>> keyRows = appClickDao.queryPVPoList(startDate, endDate, page, view);
		return keyRows;
	}
	
	public List<Map<String, Object>> queryPVPoList(String startDate, String endDate, String page, String view,
			String position) {
		List<Map<String, Object>> keyRows = appClickDao.queryPVPoList(startDate, endDate, page, view, position);
		return keyRows;
	}
}
