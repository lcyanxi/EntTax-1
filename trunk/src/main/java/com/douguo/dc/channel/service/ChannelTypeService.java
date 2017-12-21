package com.douguo.dc.channel.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.channel.dao.ChannelTypeDao;
import com.douguo.dc.channel.model.ChannelType;

@Repository("channelTypeService")
public class ChannelTypeService {

	@Autowired
	private ChannelTypeDao channelTypeDao;

	public List<ChannelType> queryListByLevel(String level) {
		List<ChannelType> keyRows = channelTypeDao.queryListByLevel(level);
		return keyRows;
	}

	public List<Map<String, Object>> queryList() {
		List<Map<String, Object>> keyRows = channelTypeDao.queryList();
		return keyRows;
	}

	public ChannelType getChannelType(String channelTypeId) {
		List<ChannelType> list = channelTypeDao.getChannelType(channelTypeId);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<ChannelType> queryAll() {
		return channelTypeDao.queryAll();
	}

	/**
	 * 新增
	 * 
	 * @param fun
	 * @return
	 */
	public boolean insert(ChannelType channelType) {
		return channelTypeDao.insert(channelType);
	}

	/**
	 * 更新
	 * 
	 * @param fun
	 * @return
	 */
	public boolean update(ChannelType channelType) {
		return channelTypeDao.update(channelType);
	}
}