package com.douguo.dc.channel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douguo.dc.channel.model.Channel;
import com.douguo.dc.channel.model.ChannelType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.channel.dao.ChannelSumStatDao;
import com.douguo.dc.channel.dao.ChannelTypeDao;

@Repository("channelSumStatService")
public class ChannelSumStatService {

    @Autowired
    private ChannelSumStatDao channelSumStatDao;

    @Autowired
    private ChannelTypeDao channelTypeDao;

    public List<Map<String, Object>> queryChannelSumList(String startDate, String endDate, String... app) {
        List<Map<String, Object>> rowList = channelSumStatDao.queryChannelSumList(startDate, endDate, app);

        //渠道类别名称转换
        List<ChannelType> listChannelType = channelTypeDao.queryAll();
        Map<Long, String> mapType = new HashMap<Long, String>();
        for (ChannelType cType : listChannelType) {
            mapType.put(Long.parseLong(String.valueOf(cType.getId())), cType.getTypeName());
        }

        for (Map<String, Object> chl : rowList) {
            Long type1 = (Long) chl.get("channel_type_1");
            String channelTypeName1 = mapType.get(type1);
            chl.put("type1_name", channelTypeName1);
            //
            Long type2 = (Long) chl.get("channel_type_2");
            String channelTypeName2 = mapType.get(type2);
            chl.put("type2_name", channelTypeName2);
            //
            Long type3 = (Long) chl.get("channel_type_3");
            String channelTypeName3 = mapType.get(type3);
            chl.put("type3_name", channelTypeName3);

        }

        return rowList;
    }

    public List<Map<String, Object>> queryChannelSumListForChannelType1(String startDate, String endDate, String channelType1, String... app) {
        List<Map<String, Object>> rowList = channelSumStatDao.queryChannelSumList(startDate, endDate, channelType1, app);
        return rowList;
    }


    public List<Map<String, Object>> queryChannelListType1(String startDate, String endDate, String... app) {
        List<Map<String, Object>> rowList = channelSumStatDao.queryChannelListType1(startDate, endDate, app);

        //渠道类别名称转换
        List<ChannelType> listChannelType = channelTypeDao.queryAll();
        Map<Long, String> mapType = new HashMap<Long, String>();
        for (ChannelType cType : listChannelType) {
            mapType.put(Long.parseLong(String.valueOf(cType.getId())), cType.getTypeName());
        }

        for (Map<String, Object> chl : rowList) {
            Long type1 = (Long) chl.get("type1");
            String channelTypeName1 = mapType.get(type1);
            chl.put("type1_name", channelTypeName1);
        }

        return rowList;
    }

    public List<Map<String, Object>> queryChannelListType2(String startDate, String endDate, String channelType1, String... app) {
        List<Map<String, Object>> rowList = channelSumStatDao.queryChannelListType2(startDate, endDate, channelType1, app);
        //渠道类别名称转换
        List<ChannelType> listChannelType = channelTypeDao.queryAll();
        Map<Long, String> mapType = new HashMap<Long, String>();
        for (ChannelType cType : listChannelType) {
            mapType.put(Long.parseLong(String.valueOf(cType.getId())), cType.getTypeName());
        }

        for (Map<String, Object> chl : rowList) {
            Long type2 = (Long) chl.get("type2");
            String channelTypeName2 = mapType.get(type2);
            if(StringUtils.isBlank(channelTypeName2)){
                channelTypeName2 = "未知";
            }
            chl.put("type2_name", channelTypeName2);
        }
        return rowList;
    }

    /**
     * 渠道汇总监控-按渠道汇总
     *
     * @param startDate
     * @param endDate
     * @param userName
     * @param channelPlat
     * @param channelType1
     * @param channelType2
     * @param channelType3
     * @param channelTag
     * @param app
     * @return
     */
    public List<Map<String, Object>> queryChannelSum(String startDate, String endDate, String userName, String channelPlat, String channelType1, String channelType2, String channelType3, String channelTag, String... app) {
        return channelSumStatDao.queryChannelSum(startDate, endDate, userName, channelPlat, channelType1, channelType2, channelType3, channelTag, app);
    }
}