package com.douguo.dc.mall.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.mall.dao.MallSourceStatDao;

@Repository("mallSourceStatService")
public class MallSourceStatService {

	@Autowired
	private MallSourceStatDao mallSourceStatDao;

	public List<Map<String, Object>> queryMallSourceList( String startDate, String endDate, String order) {
		List<Map<String, Object>> keyRows = mallSourceStatDao.queryMallSourceList(startDate, endDate, order);
		return keyRows;
	}
}