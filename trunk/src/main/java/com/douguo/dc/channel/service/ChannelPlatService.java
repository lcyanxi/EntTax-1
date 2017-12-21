package com.douguo.dc.channel.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.channel.dao.ChannelPlatDao;
import com.douguo.dc.channel.model.ChannelPlat;

@Repository("ChannelPlatService")
public class ChannelPlatService {

	@Autowired
	private ChannelPlatDao channelPlatDao;

	public List<Map<String, Object>> queryList() {
		List<Map<String, Object>> keyRows = channelPlatDao.queryList();
		return keyRows;
	}

	public ChannelPlat getChannelPlat(String id) {
		List<ChannelPlat> list = channelPlatDao.getChannelPlat(id);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<ChannelPlat> queryAll() {
		return channelPlatDao.queryAll();
	}

	/**
	 * 新增
	 * 
	 * @param fun
	 * @return
	 */
	public boolean insert(ChannelPlat channelPlat) {
		return channelPlatDao.insert(channelPlat);
	}

	/**
	 * 更新
	 * 
	 * @param fun
	 * @return
	 */
	public boolean update(ChannelPlat channelPlat) {
		return channelPlatDao.update(channelPlat);
	}
}