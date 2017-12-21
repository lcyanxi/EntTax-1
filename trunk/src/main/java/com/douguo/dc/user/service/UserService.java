package com.douguo.dc.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.user.dao.UserDao;
import com.douguo.dc.user.model.User;

@Repository("userService")
public class UserService {

	@Autowired
	private UserDao userDao;

	public User getUser(String uid) {
		List<User> list = userDao.getUserByUid(uid);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<User> queryUserList(String uid) {
		return userDao.queryUserList(uid);
	}

	public boolean updateUserPass(String userid, String pass) {
		return userDao.updateUserPass(userid, pass);
	}

	/**
	 * 新增用户
	 * 
	 * @param user
	 * @return
	 */
	public boolean insert(User user) {
		return userDao.insert(user);
	}

	/**
	 * 更新用户
	 * 
	 * @param user
	 * @return
	 */
	public boolean update(User user) {
		return userDao.update(user);
	}

	/**
	 * 删除用户
	 * 
	 * @param uid
	 * @return
	 */
	public boolean del(String uid) {
		return userDao.del(uid);
	}
}
