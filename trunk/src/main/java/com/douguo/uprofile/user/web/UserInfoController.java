package com.douguo.uprofile.user.web;

import com.common.base.common.Page;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;
import com.douguo.uprofile.user.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/userInfo/manage")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/queryJson")
    public void queryJson(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {

        //获取页面信息
        request.setCharacterEncoding("utf-8");
        String strPageSize = request.getParameter("rows"); // 取出每一页显示的行数
        String sidx = request.getParameter("sidx"); // 取出排序的项
        String sort = request.getParameter("sord"); // 取出排序方式：升序，降序
        String strPageNo = request.getParameter("page");
        //参数
        String user_id = request.getParameter("userId");
        String username = request.getParameter("userName");
        String nickname = request.getParameter("nickName");
        String sex = request.getParameter("sex");
        String mobile = request.getParameter("mobile");

        int pageSize = 20;
        if (StringUtils.isNotBlank(strPageSize)) { // 设置每一页显示行数的默认值
            pageSize = Integer.parseInt(strPageSize);
        }
        JQGridUtil t = new JQGridUtil();
        int pageNo = 1; // 当前显示的页数
        if (StringUtils.isNotBlank(strPageNo)) {
            pageNo = Integer.parseInt(strPageNo);
        }

        //列表页条件
        String queryCondition = "&" + request.getQueryString();

        System.out.println("user_id:"+user_id);
        System.out.println("username:"+username);
        System.out.println("nickname:"+nickname);

        Page page = null ;

        if(user_id==null && username==null && nickname==null){
            page = userInfoService.queryAllUsers(user_id, username, nickname, mobile, sex, pageNo, pageSize, sort);
        } else if ("".equals(user_id) && "".equals(username) && "".equals(nickname)) {
            page = userInfoService.queryAllUsers(user_id, username, nickname, mobile, sex, pageNo, pageSize, sort);
        } else {
            page = userInfoService.queryDesignatedUsers(user_id, username, nickname, mobile, sex, pageNo, pageSize, sort);
        }

        List<Map<String, Object>> list = (List<Map<String, Object>>) page.getResult();
        int records = page.getTotalCount();
        // 要显示的总的页数
        int totalPage = page.getTotalPageCount();
        String[][] arryFun = new String[list.size()][];

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> fun =  list.get(i);
            //查询条件“userId
            arryFun[i] = new String[]{String.valueOf(fun.get("user_id")),
                    "<a href='/userInfo/manage/show.do?user_id=" +String.valueOf(fun.get("user_id")) + queryCondition + "' title='查看用户信息' >"+String.valueOf(fun.get("username"))+"</a>",
                    "<a target='_blank' href='http://www.douguo.com/u/" + String.valueOf(fun.get("username")) + ".html' title='昵称' >" + String.valueOf(fun.get("nickname")) + "</a>",
                    String.valueOf(fun.get("sex")), String.valueOf(fun.get("mobile")), String.valueOf(fun.get("email")),
                    "<a target='_blank' href='http://acp.douguo.net/index.php/douxiaoguo/msgbox?' title='私信' >私信</a>",
                    "<a href='/userInfo/manage/preEdit.do?userId=" + String.valueOf(fun.get("user_id")) + queryCondition + "' title='修改' >修改</a>",
                    "<a href='/userInfo/manage/show.do?userId=" + String.valueOf(fun.get("user_id")) + queryCondition + "' title='查看' >查看</a>",
                    "<a href='/userInfo/manage/delete.do?userId=" + String.valueOf(fun.get("user_id")) + queryCondition + "' title='删除' >删除</a>"};
        }

        // 行数据
        List<Map> rows = new ArrayList<Map>();
        for (String[] axx : arryFun) {
            Map map = new HashMap();
            map.put("id", "1");
            map.put("cell", axx);

            rows.add(map);
        }

        // rows
        GridPager<Map> gridPager = new GridPager<Map>(pageNo, totalPage, records, rows); // 将表格显示的配置初始化给GridPager
        t.toJson(gridPager, response); // 发送json数据
    }

    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String appId = request.getParameter("appId");
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        model.put("globalAppid", appId);

        return "/userInfo/userInfo_manager";
    }

    /**
     * 修改页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/show")
    public String preEdit(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //
        String user_id = request.getParameter("user_id");

        if(user_id==null){
            user_id="unknow";
        }

        // 获取类型list
//        Page page = userInfoService.queryAllUsers("", "", "", "", "", 1, 20, "");

        Page page = userInfoService.queryDesignatedUsers(user_id, "", "", "", "", 1, 20, "");
        List<Map<String, Object>> list = (List<Map<String, Object>>) page.getResult();

        //用户信息Map
        Map<String, Object> user = null ;

        for(Map<String, Object> map : list){
            if(user_id.equals(String.valueOf(map.get("user_id")))){
                user = map ;
            }
        }

        if(user==null){
            user = new HashMap<>();
        }

        //传值
        model.put("user", user);

        return "/userInfo/userInfo_show";
    }
}