package com.zyz.open.hiveadmin.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zyz.open.hiveadmin.dao.HiveAdminJobDao;
import com.zyz.open.hiveadmin.model.HiveAdminJob;

@Repository("hiveAdminJobService")
public class HiveAdminJobService {

	@Autowired
	private HiveAdminJobDao hiveAdminJobDao;

	public List<Map<String, Object>> queryListMapByUid(String uid) {
		List<Map<String, Object>> keyRows = hiveAdminJobDao.queryListMapByUid(uid);
		return keyRows;
	}

	public List<HiveAdminJob> queryListByUid(String uid) {
		List<HiveAdminJob> keyRows = hiveAdminJobDao.queryListByUid(uid);
		return keyRows;
	}

	public List<Map<String, Object>> queryFocusList(String type, String startDate, String endDate, String id) {
		List<Map<String, Object>> keyRows = hiveAdminJobDao.queryFocusList(type, startDate, endDate, id);
		return keyRows;
	}

	public List<Map<String, Object>> queryAdList(String startDate, String endDate, String adId) {
		List<Map<String, Object>> keyRows = hiveAdminJobDao.queryAdList(startDate, endDate, adId);
		return keyRows;
	}

	public HiveAdminJob getHiveAdminJobById(String id) {
		List<HiveAdminJob> list = hiveAdminJobDao.getHiveAdminJob(id);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<HiveAdminJob> queryAll() {
		return hiveAdminJobDao.queryAll();
	}

	/**
	 * 新增
	 * 
	 * @param hiveAdminJob
	 * @return
	 */
	public boolean insert(HiveAdminJob hiveAdminJob) {
		return hiveAdminJobDao.insert(hiveAdminJob);
	}

	/**
	 * 更新
	 * 
	 * @param hiveAdminJob
	 * @return
	 */
	public boolean update(HiveAdminJob hiveAdminJob) {
		return hiveAdminJobDao.update(hiveAdminJob);
	}
	
	/**
	 * 删除
	 * 
	 * @author JainfeiZhang
	 * @param id
	 */
	public void delete(String id){
		hiveAdminJobDao.deleteHiveAdminJob(id);
	}
}
