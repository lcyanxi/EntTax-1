package com.douguo.dc.channel.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.channel.dao.ChannelCooperationDao;
import com.douguo.dc.channel.model.ChannelCooperation;

@Repository("channelCooperationService")
public class ChannelCooperationService {

	@Autowired
	private ChannelCooperationDao channelCooperationDao;

	public List<Map<String, Object>> queryList() {
		List<Map<String, Object>> keyRows = channelCooperationDao.queryList();
		return keyRows;
	}

	public ChannelCooperation getChannelCooperation(String id) {
		List<ChannelCooperation> list = channelCooperationDao.getChannelCooperation(id);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<ChannelCooperation> queryAll() {
		return channelCooperationDao.queryAll();
	}

	/**
	 * 新增
	 * 
	 * @param fun
	 * @return
	 */
	public boolean insert(ChannelCooperation channelCooperation) {
		return channelCooperationDao.insert(channelCooperation);
	}

	/**
	 * 更新
	 * 
	 * @param fun
	 * @return
	 */
	public boolean update(ChannelCooperation channelCooperation) {
		return channelCooperationDao.update(channelCooperation);
	}
}