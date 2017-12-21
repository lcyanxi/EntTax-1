package com.zyz.open.hiveadmin.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zyz.open.hiveadmin.dao.HiveAdminJobDao;
import com.zyz.open.hiveadmin.dao.HiveAdminOldAdmDao;
import com.zyz.open.hiveadmin.model.HiveAdminJob;

@Repository("hiveAdminJobOldAdmService")
public class HiveAdminJobOldAdmService {

	@Autowired
	private HiveAdminOldAdmDao hiveAdminOldAdmDao;

	public List<Map<String, Object>> queryFocusList(String type,String startDate, String endDate, String id) {
		return hiveAdminOldAdmDao.queryFocusList(type, startDate, endDate, id) ;
	}
	
	public List<Map<String, Object>> queryAdList(String startDate, String endDate, String id) {
		return hiveAdminOldAdmDao.queryAdList(startDate, endDate, id) ;
	}
	
}
