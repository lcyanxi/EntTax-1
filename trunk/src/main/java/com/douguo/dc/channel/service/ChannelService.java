package com.douguo.dc.channel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.channel.dao.ChannelDao;
import com.douguo.dc.channel.dao.ChannelTypeDao;
import com.douguo.dc.channel.model.Channel;
import com.douguo.dc.channel.model.ChannelType;

@Repository("channelService")
public class ChannelService {

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private ChannelTypeDao channelTypeDao;

    public List<Channel> getChannelListByStatus(int status) {
        List<Channel> rowList = channelDao.queryChannel(status);

        List<ChannelType> listChannelType = channelTypeDao.queryAll();
        Map<Long, String> mapType = new HashMap<Long, String>();
        for (ChannelType cType : listChannelType) {
            mapType.put(Long.parseLong(String.valueOf(cType.getId())), cType.getTypeName());
        }

        for (Channel chl : rowList) {
            Long type1 = chl.getChannelType1();
            Long type2 = chl.getChannelType2();
            Long type3 = chl.getChannelType3();

            String channelTypeName1 = mapType.get(type1);
            String channelTypeName2 = mapType.get(type2);
            String channelTypeName3 = mapType.get(type3);
            chl.setChannelTypeName1(channelTypeName1);
            chl.setChannelTypeName2(channelTypeName2);
            chl.setChannelTypeName3(channelTypeName3);
        }

        if (null == rowList)
            return null;
        return rowList;
    }

    public Channel getChannel(String id) {
        List<Channel> list = channelDao.getChannel(id);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public int insertChannelDict(String code, String name) {
        return channelDao.insertChannelDict(code, name);
    }

    public boolean update(Channel channel) {
        return channelDao.update(channel);
    }

    public int deleteChannelDict(int id) {
        return channelDao.updateChannelStatus(id);
    }
}