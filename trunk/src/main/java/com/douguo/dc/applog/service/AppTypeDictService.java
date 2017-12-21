package com.douguo.dc.applog.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.applog.dao.AppTypeDictDao;
import com.douguo.dc.applog.model.AppTypeDict;
import com.douguo.dc.user.model.Function;

@Repository("appTypeDictService")
public class AppTypeDictService {

	@Autowired
	private AppTypeDictDao appTypeDictDao;

	public List<Map<String, Object>> queryListByType(String type) {
		List<Map<String, Object>> keyRows = appTypeDictDao.queryListByType(type);
		return keyRows;
	}

	public List<Map<String, Object>> queryList() {
		List<Map<String, Object>> keyRows = appTypeDictDao.queryList();
		return keyRows;
	}

	public AppTypeDict getAppTypeDictById(String appTypeDictId) {
		List<AppTypeDict> list = appTypeDictDao.getAppTypeDictById(appTypeDictId);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<AppTypeDict> queryAll() {
		return appTypeDictDao.queryAll();
	}

	/**
	 * 新增
	 * 
	 * @param fun
	 * @return
	 */
	public boolean insert(AppTypeDict appTypeDict) {
		return appTypeDictDao.insert(appTypeDict);
	}

	/**
	 * 更新
	 * 
	 * @param fun
	 * @return
	 */
	public boolean update(AppTypeDict appTypeDict) {
		return appTypeDictDao.update(appTypeDict);
	}
}