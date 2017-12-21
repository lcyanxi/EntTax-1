package com.douguo.dc.applog.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.applog.dao.AppLogClickDictDao;
import com.douguo.dc.applog.model.AppLogClickDict;

@Repository("appLogClickDictService")
public class AppLogClickDictService {

	@Autowired
	private AppLogClickDictDao appLogClickDictDao;

	public List<Map<String, Object>> queryListByPage(String page) {
		List<Map<String, Object>> keyRows = appLogClickDictDao.queryListByPage(page);
		return keyRows;
	}

	public List<Map<String, Object>> queryList() {
		List<Map<String, Object>> keyRows = appLogClickDictDao.queryList();
		return keyRows;
	}

	public AppLogClickDict getAppLogClickDict(String appLogClickDictId) {
		List<AppLogClickDict> list = appLogClickDictDao.getAppLogClickDict(appLogClickDictId);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<AppLogClickDict> queryAll() {
		return appLogClickDictDao.queryAll();
	}

	/**
	 * 新增
	 * 
	 * @param fun
	 * @return
	 */
	public boolean insert(AppLogClickDict appLogClickDict) {
		return appLogClickDictDao.insert(appLogClickDict);
	}

	/**
	 * 更新
	 * 
	 * @param fun
	 * @return
	 */
	public boolean update(AppLogClickDict appLogClickDict) {
		return appLogClickDictDao.update(appLogClickDict);
	}
}