package com.douguo.uprofile.user.service;

import com.douguo.uprofile.user.dao.UserProfileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service("userProfileService")
public class UserProfileService {

    @Autowired
    private UserProfileDao userProfileDao;

    public Map<String, String> getUserProfile (String table_name, String row_key, String column_family, int version) throws IOException {
        Map<String, String> sstr = userProfileDao.getUserProfile(table_name, row_key, column_family, version);
        return sstr;
    }

}