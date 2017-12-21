package com.douguo.dc.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.user.dao.UserMenuDao;

@Repository("userMenuService")
public class UserMenuService {

	@Autowired
	private UserMenuDao userMenuDao;

	public List<Map<String,Object>> queryUserMenu(String uid,String type) {
		List<Map<String,Object>> list = userMenuDao.queryUserMenu(uid, type);
		return list;
	}
	
	public List<Map<String,Object>> queryUserFunction(String uid){
		return userMenuDao.queryUserFunction(uid);
	}
}
