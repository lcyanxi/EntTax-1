package com.douguo.crm.service;

import com.common.base.common.Page;
import com.douguo.crm.dao.VIPUserNewDao;
import com.douguo.crm.model.VIPUser;
import com.douguo.crm.model.VIPUserNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("vipUserNewService")
public class VIPUserNewService {

    @Autowired
    private VIPUserNewDao vipUserNewDao;

    public List<Map<String, Object>> queryVipUserNewList() {
        List<Map<String, Object>> rowList = vipUserNewDao.queryVipUserNewListAll();

        return rowList;
    }

    public List<VIPUserNew> queryVipUserNew(String userId, String userName, String nickName, String tagId, String principal) {
        Page page = vipUserNewDao.queryVipUserNew(userId, userName, nickName, tagId, principal, 1, 9999999, "");
        if (page.getResult() != null) {
            List<VIPUserNew> rowList = (List<VIPUserNew>) page.getResult();
            for (VIPUserNew vipUser : rowList) {
                vipUser.setTagName(getTagName(vipUser.getTagId()));
            }
            return rowList;
        } else {
            return null;
        }
    }

    public Page queryVipUserNew(String userId, String userName, String nickName, String tagId, String principal, int pageNo, int pageSize, String sort) {
        Page page = vipUserNewDao.queryVipUserNew(userId, userName, nickName, tagId, principal, pageNo, pageSize, sort);
        List<VIPUserNew> rowList = (List<VIPUserNew>) page.getResult();
        for (VIPUserNew vipUser : rowList) {
            vipUser.setTagName(getTagName(vipUser.getTagId()));
        }
        return page;
    }

    private String getTagName(int tagId) {
        if (tagId == 0) {
            return "未分类";
        } else if (tagId == 1) {
            return "核心用户";
        } else if (tagId == 2) {
            return "菜谱达人";
        } else if (tagId == 3) {
            return "圈圈达人";
        } else if (tagId == 4) {
            return "马甲";
        } else if (tagId == 5) {
            return "黑名单";
        } else {
            return "未分类";
        }
    }

    public VIPUserNew getVipUserNew(String id) {
        List<VIPUserNew> list = vipUserNewDao.getVipUserNew(id);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public VIPUserNew getVipUserNewByUID(String userId) {
        List<VIPUserNew> list = vipUserNewDao.getVipUserNewByUID(userId);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public int insertVipUserNew(VIPUserNew vipUserNew) {
        return vipUserNewDao.insertVipUserNew(vipUserNew);
    }

    public boolean update(VIPUserNew vipUserNew) {
        return vipUserNewDao.update(vipUserNew);
    }

    public int delete(Integer id) {
        return vipUserNewDao.delete(id);
    }
}