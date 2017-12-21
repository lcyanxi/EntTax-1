package com.douguo.dc.mall.service;

import com.douguo.dc.mall.dao.MallStoreStatDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("mallStoreStatService")
public class MallStoreStatService {

	@Autowired
	private MallStoreStatDao mallStoreStatDao;

	public List<Map<String, Object>> queryStoreSumList(String startDate, String endDate, String order) {
		List<Map<String, Object>> keyRows = mallStoreStatDao.queryStoreSumList(startDate, endDate, order);

		return keyRows;
	}

	/**
	 * 查询单个商家list
	 * 
	 * @param startDate
	 * @param endDate
	 * @param order
	 * @return
	 */
	public List<Map<String, Object>> querySingleStoreSumList(String startDate, String endDate, String storeId,
			String order) {
		List<Map<String, Object>> keyRows = mallStoreStatDao.querySingleStoreList(startDate, endDate, storeId, order);
		return keyRows;
	}
}