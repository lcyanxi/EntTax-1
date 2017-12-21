package com.douguo.uprofile.user.service;

import com.common.base.common.Page;
import com.douguo.uprofile.user.dao.UserInfoDao;
import com.douguo.uprofile.user.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

//@Repository("userInfoService")
@Service("userInfoService")
public class UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    public Page queryAllUsers(String userId, String userName, String nickName, String mobile, String sex, int pageNo, int pageSize, String sort) {
        Page page = userInfoDao.queryAllUsers(userId, userName, nickName, mobile, sex, pageNo, pageSize, sort);
        List<UserInfo> rowList = (List<UserInfo>) page.getResult();
        page.setData(rowList);
        return page;
    }

    public Page queryDesignatedUsers(String userId, String userName, String nickName, String mobile, String sex, int pageNo, int pageSize, String sort) {
        Page page = userInfoDao.queryDesignatedUsers(userId, userName, nickName, mobile, sex, pageNo, pageSize, sort);
        List<UserInfo> rowList = (List<UserInfo>) page.getResult();
        page.setData(rowList);
        return page;
    }

    public Page queryLikeUsers(String args, int pageNo, int pageSize, String sort) {
        Page page = userInfoDao.queryLikeUsers(args, pageNo, pageSize, sort);
        List<UserInfo> rowList = (List<UserInfo>) page.getResult();
        page.setData(rowList);
        return page;
    }
}