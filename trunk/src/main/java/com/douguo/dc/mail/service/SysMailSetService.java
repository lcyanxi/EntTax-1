package com.douguo.dc.mail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.mail.dao.SysMailSetDao;
import com.douguo.dc.mail.model.SysMailSet;

@Repository("sysMailSetService")
public class SysMailSetService {

	@Autowired
	private SysMailSetDao sysMailSetDao;

	public SysMailSet getSysMailSetByMailType(String mailType) {
		List<SysMailSet> list = sysMailSetDao.queryListByMailType(mailType);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public SysMailSet getSysMailSet(String id) {
		List<SysMailSet> list = sysMailSetDao.getSysMailSet(id);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<SysMailSet> queryAll() {
		return sysMailSetDao.queryAll();
	}
	
	public List<SysMailSet> queryTimeMail() {
		return sysMailSetDao.queryTimeMail();
	}

	/**
	 * 新增
	 * 
	 * @param fun
	 * @return
	 */
	public boolean insert(SysMailSet sysMailSet) {
		return sysMailSetDao.insert(sysMailSet);
	}

	/**
	 * 更新
	 * 
	 * @param fun
	 * @return
	 */
	public boolean update(SysMailSet sysMailSet) {
		return sysMailSetDao.update(sysMailSet);
	}
	
	/**
	 * 更新收件人
	 * 
	 * @return
	 */
	public boolean updateMailReceiver(String recriverStr, String id) {
		return sysMailSetDao.updateMailReceiver(recriverStr, id);
	}
	
}
