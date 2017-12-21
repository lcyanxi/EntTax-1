package com.douguo.uprofile.user.web;

import com.common.base.common.Page;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;
import com.douguo.uprofile.user.service.UserInfoService;
import com.douguo.uprofile.user.service.UserProfileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/userProfile/manage")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {
        String row_key = request.getParameter("rk");

        String table_name = "dhb_user_profile";
        String column_family = "stat";
        int version = 10;

        Map<String, String> up = null;

        if (row_key == null || "".equals(row_key)){
            up = new HashMap<String,String>();
            up.put("信息：","暂无数据");
        } else {
            up = userProfileService.getUserProfile(table_name,row_key,column_family,version);
        }

        model.put("up", up);

        return "/userProfile/userProfile_view";
    }

}