package com.douguo.dc.spider.service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.spider.dao.SpiderIpDao;
import com.douguo.dc.spider.model.SpiderIp;

@Repository("spiderIpService")
public class SpiderIpService {

    @Autowired
    private SpiderIpDao spiderIpDao;

    public List<Map<String, Object>> queryListBySpider(String spider) {
        List<Map<String, Object>> keyRows = spiderIpDao.queryListBySpider(spider);
        return keyRows;
    }

    public List<Map<String, Object>> queryList() {
        List<Map<String, Object>> keyRows = spiderIpDao.queryList();
        return keyRows;
    }

    public SpiderIp getSpiderIp(String spiderIpId) {
        List<SpiderIp> list = spiderIpDao.getSpiderIp(spiderIpId);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<SpiderIp> queryAll() {
        return spiderIpDao.queryAll();
    }

    /**
     * 新增
     *
     * @param fun
     * @return
     */
    public boolean insert(SpiderIp spiderIp) {

        Pattern p = Pattern.compile("([0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)\\*$");
        Matcher m = p.matcher(spiderIp.getIp());
        if (m.matches()) {
            try {
                String ip = m.group(1);
                for (int i = 1; i < 255; i++) {
                    spiderIp.setIp(ip + i);
                    spiderIpDao.insert(spiderIp);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return spiderIpDao.insert(spiderIp);
        }
    }

    /**
     * 更新
     *
     * @param fun
     * @return
     */
    public boolean update(SpiderIp spiderIp) {
        return spiderIpDao.update(spiderIp);
    }
}