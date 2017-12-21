package com.douguo.dc.serverlog.service;

import com.douguo.dc.serverlog.dao.ServerLogSrctypeDictDao;
import com.douguo.dc.serverlog.model.ServerLogSrctypeDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("serverLogSrctypeDictService")
public class ServerLogSrctypeDictService {
    @Autowired
    private ServerLogSrctypeDictDao serverLogSrctypeDictDao;

    public List querySrctypeList() {
        List<ServerLogSrctypeDict> list = new ArrayList<>();

        List<Map<String, Object>> rowList = serverLogSrctypeDictDao.querySrctypeList();

        for (Map<String, Object> map : rowList) {
            Integer id = (Integer) map.get("id");
            String srctype = (String) map.get("srctype");
            String srctypeName = (String) map.get("srctype_name");
            String service = (String) map.get("service");

            Date time = (Date) map.get("create_time");
            String createTime=time.toString();
            String updateTime=map.get("update_time").toString();
            String sdesc = (String) map.get("sdesc");

            ServerLogSrctypeDict serverLogSrctypeDict = new ServerLogSrctypeDict();
            serverLogSrctypeDict.setId(id);
            serverLogSrctypeDict.setSrctype(srctype);
            serverLogSrctypeDict.setSrctypeName(srctypeName);
            serverLogSrctypeDict.setService(service);
            serverLogSrctypeDict.setCreateTime(createTime);
            serverLogSrctypeDict.setUpdateTime(updateTime);
            serverLogSrctypeDict.setSdesc(sdesc);

            list.add(serverLogSrctypeDict);
        }

        return list;
    }


    public int addSrctype(ServerLogSrctypeDict serverLogSrctypeDict){
        return  serverLogSrctypeDictDao.addSrctype(serverLogSrctypeDict);
    }

    public int updateSrctype(ServerLogSrctypeDict serverLogSrctypeDict){
        return  serverLogSrctypeDictDao.updateSrctype(serverLogSrctypeDict);
    }

    public int deleteSrctype(int id){
        return  serverLogSrctypeDictDao.deleteSrctype(id);
    }
}
