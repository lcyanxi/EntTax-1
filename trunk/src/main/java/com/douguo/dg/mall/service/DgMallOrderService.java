package com.douguo.dg.mall.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dg.mall.dao.DgMallOrderDao;

@Repository("dgMallOrderService")
public class DgMallOrderService {

	@Autowired
	private DgMallOrderDao dgMallOrderDao;

	public List<Map<String, Object>> getOrderList(String createdate) {
		List<Map<String, Object>> keyRows = dgMallOrderDao.getOrderList(createdate);
		return keyRows;
	}
}