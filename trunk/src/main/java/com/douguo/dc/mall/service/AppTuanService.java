package com.douguo.dc.mall.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.mall.dao.AppTuanDao;

@Repository("appTuanService")
public class AppTuanService {

	@Autowired
	private AppTuanDao appTuanDao;

	public List<Map<String, Object>> queryListSum(String startDate, String endDate, String appId) {
		List<Map<String, Object>> keyRows = appTuanDao.queryListSum(startDate, endDate);
		return keyRows;
	}

	public List<Map<String, Object>> queryList(String startDate, String endDate) {
		List<Map<String, Object>> keyRows = appTuanDao.queryList(startDate, endDate);
		return keyRows;
	}

	public List<Map<String, Object>> queryProductList(String startDate, String endDate, String order) {
		List<Map<String, Object>> keyRows = appTuanDao.queryProductList(startDate, endDate, order);
		return keyRows;
	}

	public List<Map<String, Object>> queryProductList(String startDate, String endDate, String goodsIds, String order) {
		List<Map<String, Object>> keyRows = appTuanDao.queryProductList(startDate, endDate, goodsIds, order);
		return keyRows;
	}

	/**
	 * 查询单品list
	 * 
	 * @param startDate
	 * @param endDate
	 * @param order
	 * @return
	 */
	public List<Map<String, Object>> querySingleProductList(String startDate, String endDate, String tuanId,
			String order) {
		List<Map<String, Object>> keyRows = appTuanDao.querySingleProductList(startDate, endDate, tuanId, order);
		return keyRows;
	}

	public List<Map<String, Object>> queryOrderSumList(String startDate, String endDate, String order) {
		List<Map<String, Object>> keyRows = appTuanDao.queryOrderSumList(startDate, endDate, order);
		return keyRows;
	}

	public List<Map<String, Object>> queryOrderPayTypeList(String startDate, String endDate, String order) {
		List<Map<String, Object>> keyRows = appTuanDao.queryOrderPayTypeList(startDate, endDate, order);
		return keyRows;
	}

	public List<Map<String, Object>> queryOrderSourceList(String startDate, String endDate, String order) {
		List<Map<String, Object>> keyRows = appTuanDao.queryOrderSourceList(startDate, endDate, order);
		return keyRows;
	}

	public List<Map<String, Object>> queryOrderTimeDistributeList(String startDate, String endDate, String order) {
		List<Map<String, Object>> keyRows = appTuanDao.queryOrderTimeDistributeList(startDate, endDate, order);
		return keyRows;
	}
}