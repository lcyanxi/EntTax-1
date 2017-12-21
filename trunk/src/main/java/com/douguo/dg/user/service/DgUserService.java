package com.douguo.dg.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dg.user.dao.DgUserDao;

@Repository("dgUserService")
public class DgUserService {

	@Autowired
	private DgUserDao dgUserDao;

	public Map<String, Object> getUserByNickName(String nickName) {
		List<Map<String, Object>> keyRows = dgUserDao.getUserByNickName(nickName);
		if(keyRows.size() > 0){
			return keyRows.get(0);
		}else{
			return null;
		}
	}

	public Map<String, Object> getUserByUserName(String userName) {
		List<Map<String, Object>> keyRows = dgUserDao.getUserByUserName(userName);
		if(keyRows.size() > 0){
			return keyRows.get(0);
		}else{
			return null;
		}
	}

	public Map<String, Object> getUserByUserId(String userId) {
		List<Map<String, Object>> keyRows = dgUserDao.getUserByUserId(userId);
		if(keyRows.size() > 0){
			return keyRows.get(0);
		}else{
			return null;
		}
	}
}