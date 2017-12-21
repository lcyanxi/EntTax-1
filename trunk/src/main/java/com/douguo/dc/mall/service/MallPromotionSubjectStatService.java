package com.douguo.dc.mall.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.mall.dao.MallPromotionSubjectStatDao;

@Repository("mallPromotionSubjectStatService")
public class MallPromotionSubjectStatService {

	@Autowired
	private MallPromotionSubjectStatDao mallPromotionSubjectStatDao;

	public List<Map<String, Object>> queryList(String startDate, String endDate) {
		List<Map<String, Object>> keyRows = mallPromotionSubjectStatDao.queryList(startDate, endDate);
		return keyRows;
	}
}