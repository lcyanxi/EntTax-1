package com.douguo.dc.channel.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.channel.dao.ChannelStatDao;

@Repository("channelStatService")
public class ChannelStatService {

	@Autowired
	private ChannelStatDao channelStatDao;

	public List<Map<String, Object>> queryChannelStatList(String startDate, String endDate, String order) {
		List<Map<String, Object>> keyRows = channelStatDao.queryChannelStatList(startDate, endDate, order);
		return keyRows;
	}
}