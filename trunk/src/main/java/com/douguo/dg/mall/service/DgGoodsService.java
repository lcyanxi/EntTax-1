package com.douguo.dg.mall.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dg.mall.dao.DgGoodsDao;

@Repository("dgGoodsService")
public class DgGoodsService {

	@Autowired
	private DgGoodsDao dgGoodsDao;

	public List<Map<String, Object>> getList(Integer id) {
		List<Map<String, Object>> keyRows = dgGoodsDao.getList(id);
		return keyRows;
	}
	
	public List<Map<String, Object>> getAllList() {
		List<Map<String, Object>> keyRows = dgGoodsDao.getAllList();
		return keyRows;
	}
	
	public List<Map<String, Object>> getCustomerGoodsList() {
		List<Map<String, Object>> keyRows = dgGoodsDao.getCustomerGoodsList();
		return keyRows;
	}
}