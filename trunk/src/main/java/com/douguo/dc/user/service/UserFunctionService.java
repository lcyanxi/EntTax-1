package com.douguo.dc.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.user.dao.UserFunctionDao;
import com.douguo.dc.user.model.UserFunction;

@Repository("userFunctionService")
public class UserFunctionService {

	@Autowired
	private UserFunctionDao userFunctionDao;

	/**
	 * 删除
	 * 
	 * @param fun
	 * @return
	 */
	public boolean delByUid(String uid) {
		return userFunctionDao.delByUid(uid);
	}

	/**
	 * 新增
	 * 
	 * @param fun
	 * @return
	 */
	public boolean batchInsert(List<UserFunction> list) {
		return userFunctionDao.batchInsert(list);
	}
	
	/**
	 * 权限统一授权
	 * @param param
	 * @param param_add
	 * @return
	 */
	public boolean addUserFun(String param, String param_add){
		return userFunctionDao.addUserFun(param, param_add) ;
	}
	
	/**
	 * 根据uid查询用户 
	 * 
	 * @param uid
	 * @return
	 */
	public List<UserFunction> queryUserFunctionByUid(String uid){
		return userFunctionDao.queryUserFunctionByUid(uid) ;
	}
}
