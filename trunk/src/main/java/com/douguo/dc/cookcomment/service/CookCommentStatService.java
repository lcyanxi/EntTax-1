package com.douguo.dc.cookcomment.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.cookcomment.dao.CookCommentStatDao;

@Repository("cookCommentStatService")
public class CookCommentStatService {

	@Autowired
	private CookCommentStatDao cookCommentStatDao;

	public List<Map<String, Object>> queryList( String startDate, String endDate, String order) {
		List<Map<String, Object>> keyRows = cookCommentStatDao.queryList(startDate, endDate, order);
		return keyRows;
	}
}