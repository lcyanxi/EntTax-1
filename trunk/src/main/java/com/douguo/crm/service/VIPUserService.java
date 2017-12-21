package com.douguo.crm.service;

import com.common.base.common.Page;
import com.douguo.crm.dao.VIPUserDao;
import com.douguo.crm.model.VIPUser;
import com.douguo.dc.util.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("vipUserService")
public class VIPUserService {

    @Autowired
    private VIPUserDao vipUserDao;

    public List<Map<String, Object>> queryVipUserList() {
        List<Map<String, Object>> rowList = vipUserDao.queryVipUserList();

        return rowList;
    }

    public List<VIPUser> queryVipUser(String userId, String userName, String nickName, String province, String city, String tagId, String principal,String hasChild,String domain) {
        Page page = vipUserDao.queryVipUser(userId, userName, nickName, province, city, tagId, principal,hasChild,domain, 1, 9999999, "");
        if (page.getResult() != null) {

            List<VIPUser> rowList = (List<VIPUser>) page.getResult();

            //更新tag名称
            for (VIPUser vipUser : rowList) {
                vipUser.setTagName(getTagName(vipUser.getTagId()));
            }
            return rowList;
        } else {
            return null;
        }
    }

    public Page queryVipUser(String userId, String userName, String nickName, String province, String city, String tagId, String principal,String hasChild,String domain, int pageNo, int pageSize, String sort) {
        Page page = vipUserDao.queryVipUser(userId, userName, nickName, province, city, tagId, principal,hasChild,domain, pageNo, pageSize, sort);
        List<VIPUser> rowList = (List<VIPUser>) page.getResult();
        //更新tag名称
        for (VIPUser vipUser : rowList) {
            vipUser.setTagName(getTagName(vipUser.getTagId()));
        }
        page.setData(rowList);
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

    public VIPUser getVipUser(String id) {
        List<VIPUser> list = vipUserDao.getVipUser(id);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public VIPUser getVipUserByUID(String userId) {
        List<VIPUser> list = vipUserDao.getVipUserByUID(userId);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public int insertVipUser(VIPUser vipUser) {
        return vipUserDao.insertVipUser(vipUser);
    }

    public boolean update(VIPUser vipUser) {
        return vipUserDao.update(vipUser);
    }
    
    public int deleteVipUser(String id){
    	return vipUserDao.deleteVipUser(id) ;
    }
    

    //public int deleteVipUser(int id) {
    //    return vipUserDao.updateChannelStatus(id);
    //}
}