package com.douguo.dc.group.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.group.dao.GroupBaseStatDao;

@Repository("groupBaseStatService")
public class GroupBaseStatService {

	@Autowired
	private GroupBaseStatDao groupBaseStatDao;

	public List<Map<String, Object>> queryGroupDayList(String startDate, String endDate) {
		List<Map<String, Object>> keyRows = groupBaseStatDao.queryGroupDayList(startDate, endDate);
		return keyRows;
	}

	public List<Map<String, Object>> queryGroupDetailList(String groupId,String startDate, String endDate) {
		List<Map<String, Object>> keyRows = groupBaseStatDao.queryGroupDetailList(groupId,startDate, endDate);
		return keyRows;
	}
	
	public List<Map<String, Object>> queryGroupList(String startDate, String endDate) {
		List<Map<String, Object>> keyRows = groupBaseStatDao.queryGroupList(startDate, endDate);
		return keyRows;
	}
}