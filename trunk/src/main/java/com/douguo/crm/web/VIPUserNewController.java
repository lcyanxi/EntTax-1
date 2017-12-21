package com.douguo.crm.web;

import com.common.base.common.Page;
import com.douguo.crm.model.VIPUser;
import com.douguo.crm.model.VIPUserNew;
import com.douguo.crm.service.VIPUserNewService;
import com.douguo.crm.service.VIPUserService;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;
import com.douguo.dc.util.DateUtil;
import com.douguo.dg.user.service.DgUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("/vipusernew/manage")
public class VIPUserNewController {

    @Autowired
    private VIPUserNewService vipUserNewService;

    @Autowired
    private VIPUserService vipUserService;

    @Autowired
    private DgUserService dgUserService;

    @RequestMapping(value = "/queryJson")
    public void queryJson(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {

        request.setCharacterEncoding("utf-8");
        String strPageSize = request.getParameter("rows"); // 取出每一页显示的行数
        String sidx = request.getParameter("sidx"); // 取出排序的项
        String sord = request.getParameter("sord"); // 取出排序方式：升序，降序
        String level = request.getParameter("level"); //
        String strPageNo = request.getParameter("page");
        //参数
        String userId = request.getParameter("userId");
        String userName = request.getParameter("userName");
        String nickName = request.getParameter("nickName");
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String tagId = request.getParameter("tagId");
        String principal = request.getParameter("principal");

        int pageSize = 20;
        if (StringUtils.isNotBlank(strPageSize)) { // 设置每一页显示行数的默认值
            pageSize = Integer.parseInt(strPageSize);
        }
        JQGridUtil t = new JQGridUtil();
        int pageNo = 1; // 当前显示的页数
        if (StringUtils.isNotBlank(strPageNo)) {
            pageNo = Integer.parseInt(strPageNo);
        }

        if (StringUtils.isBlank(level)) {
            level = "1";
        }

        //列表页条件
        String queryCondition = "&" + request.getQueryString();

        Page page = vipUserNewService.queryVipUserNew(userId, userName, nickName, tagId, principal, pageNo, pageSize, sidx + " " + sord);
        List<VIPUserNew> list = (List<VIPUserNew>) page.getResult();
        int records = page.getTotalCount();

        // 要显示的总的页数
        int totalPage = page.getTotalPageCount();

        String[][] arryFun = new String[list.size()][];

        for (int i = 0; i < list.size(); i++) {
            VIPUserNew fun = list.get(i);
            arryFun[i] = new String[]{String.valueOf(fun.getId()), String.valueOf(fun.getUserId()), fun.getUserName(),
                    "<img width='36px' height='36px' src='http://tx1.douguo.net/" + fun.getHeadIcon() + "' >",
                    "<a target='_blank' href='http://www.douguo.com/u/" + fun.getUserName() + ".html' title='昵称' >" + fun.getNickName() + "</a>",
                    fun.getTagName(), fun.getPrincipal(), fun.getCreatetime(),
                    "<a href='/vipusernew/manage/preEdit.do?id=" + fun.getId() + queryCondition + "' title='修改' >修改</a>",
                    "<a target='_blank' href='http://acp.douguo.net/index.php/douxiaoguo/msgbox?' title='私信' >私信</a>",
                    "<a href='/vipusernew/manage/insertVipUser.do?vipUserNewId=" + fun.getId() + "' title='入库' >入库</a>"
            };
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

    /**
     * 导出基本信息 - excel
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/exportExcel")
    public ModelAndView exportExcel(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //
        //参数
        String userId = request.getParameter("userId");
        String userName = request.getParameter("userName");
        String nickName = request.getParameter("nickName");
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String tagId = request.getParameter("tagId");
        String principal = request.getParameter("principal");

        List<VIPUserNew> list = vipUserNewService.queryVipUserNew(userId, userName, nickName, tagId, principal);
        //
        model.put("list", list);
        return new ModelAndView(new VipUserNewExcelView(), model);
    }

    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String appId = request.getParameter("appId");
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        model.put("globalAppid", appId);
        return "/vip_user_new/vip_user_new_manager";
    }

    /**
     * 修改页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/preEdit")
    public String preEdit(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //
        String id = request.getParameter("id");
        String queryCondition = request.getQueryString();
        //
        // 获取类型list
        VIPUserNew vipUserNew = vipUserNewService.getVipUserNew(id);
        model.put("vipUserNew", vipUserNew);
        model.put("queryCondition", queryCondition);


        return "/vip_user_new/vip_user_new_modify";
    }

    @RequestMapping(value = "/save")
    public String save(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");

        //获取昵称or豆果id
        String uname = (String) request.getParameter("uname");

        //判断是否真实用户
        Map<String, Object> dgUser = dgUserService.getUserByNickName(uname);
        if (dgUser == null) {
            //用豆果id去取
            dgUser = dgUserService.getUserByUserName(uname);
        }

        if (dgUser != null) {
            Integer userId = (Integer) dgUser.get("user_id");
            String userName = (String) dgUser.get("username");
            String nickName = (String) dgUser.get("nickname");
            String headIcon = (String) dgUser.get("headicon");
            //判断达人库是否已有此用户
            VIPUserNew vUser = vipUserNewService.getVipUserNewByUID(String.valueOf(userId));
            if (vUser == null) {
                VIPUserNew vipUserNew = new VIPUserNew();
                vipUserNew.setUserId(userId);
                vipUserNew.setUserName(userName);
                vipUserNew.setNickName(nickName);
                vipUserNew.setHeadIcon(headIcon);
                vipUserNew.setCreatetime(DateUtil.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
                //插入新达人
                vipUserNewService.insertVipUserNew(vipUserNew);
            }
        }
        //
        List<VIPUserNew> vipUserNewList = vipUserNewService.queryVipUserNew("", "", "", "", "");
        model.put("vipUserNewList", vipUserNewList);
        String appId = request.getParameter("appId");
        if (null == appId || appId.equals("")) {
            appId = "4";
        }
        model.put("globalAppid", appId);
        return "/vip_user_new/vip_user_new_manager";
    }

    @RequestMapping(value = "/insertVipUser")
    public String insertVipUser(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");

        //新用户id
        String vipUserNewId = (String) request.getParameter("vipUserNewId");

        //判断是否真实用户
        VIPUserNew vuserNew = vipUserNewService.getVipUserNew(vipUserNewId);

        if (vuserNew != null) {
            Integer userId = vuserNew.getUserId();
            String userName = vuserNew.getUserName();
            String nickName = vuserNew.getNickName();
            String headIcon = vuserNew.getHeadIcon();
            Integer tagId = vuserNew.getTagId();
            String principal = vuserNew.getPrincipal();
            String createtime = vuserNew.getCreatetime();
            int nValue = 0;
            //判断达人库是否已有此用户
            VIPUser vipUser = vipUserService.getVipUserByUID(String.valueOf(userId));
            if (vipUser == null) {
                vipUser = new VIPUser();
                vipUser.setUserId(userId);
                vipUser.setUserName(userName);
                vipUser.setNickName(nickName);
                vipUser.setHeadIcon(headIcon);
                vipUser.setTagId(tagId);
                vipUser.setPrincipal(principal);
                vipUser.setCreatetime(createtime);
                //插入新达人
                nValue = vipUserService.insertVipUser(vipUser);
            }
            //删除本地用户
            if (nValue > 0) {
                vipUserNewService.delete(Integer.parseInt(vipUserNewId));
            }

        }
        //
        List<VIPUserNew> vipUserNewList = vipUserNewService.queryVipUserNew("", "", "", "", "");
        model.put("vipUserNewList", vipUserNewList);
        String appId = request.getParameter("appId");
        if (null == appId || appId.equals("")) {
            appId = "4";
        }
        model.put("globalAppid", appId);
        return "/vip_user_new/vip_user_new_manager";
    }

    @RequestMapping(value = "/update")
    public String update(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");

        String id = request.getParameter("id");
        String userId = request.getParameter("userId");

        String tagId = request.getParameter("tagId");
        String principal = request.getParameter("principal");
        String userDesc = request.getParameter("userDesc");
        String queryCondition = request.getParameter("queryCondition");

        if (id != null && id.trim().length() > 0) {
            VIPUserNew vipUser = vipUserNewService.getVipUserNew(id);

            if (vipUser != null) {
                vipUser.setTagId(Integer.parseInt(tagId));
                vipUser.setPrincipal(principal);
                //
                vipUserNewService.update(vipUser);
            }
        } else {
        }

        model.put("queryCondition", queryCondition);
        String[] arryQuery = queryCondition.split("&");
        for (String qy : arryQuery) {
            if (StringUtils.isNotBlank(qy)) {
                String[] arryKV = qy.split("=");
                if (arryKV.length == 2) {
                    model.put(arryKV[0], arryKV[1]);
                }
            }
        }
//        return "redirect:/vipusernew/manage/index.do";
        return "/vip_user_new/vip_user_new_manager";
    }

//    @RequestMapping(value = "/delete", method = RequestMethod.GET, produces = "application/html; charset=UTF-8")
//    public String deleteChannel(@RequestParam(value = "id", required = true) int id, HttpServletRequest request,
//                                HttpServletResponse response, ModelMap model) {
//        vipUserNewService.deleteChannelDict(id);
//        String appId = request.getParameter("appId");
//        if (null == appId || appId.equals("")) {
//            appId = "1";
//        }
//        model.put("globalAppid", appId);
//        return "/manage/vipuser";
//    }
}