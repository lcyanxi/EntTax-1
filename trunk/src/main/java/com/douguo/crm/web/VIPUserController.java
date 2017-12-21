package com.douguo.crm.web;

import com.common.base.common.Page;
import com.douguo.crm.model.VIPUser;
import com.douguo.crm.service.VIPUserHuodongService;
import com.douguo.crm.service.VIPUserService;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;
import com.douguo.dc.util.DateUtil;
import com.douguo.dg.user.service.DgUserService;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/vipuser/manage")
public class VIPUserController {

    @Autowired
    private VIPUserService vipUserService;
    
    @Autowired
    private VIPUserHuodongService vipUserHuodongService ;

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
        //
        String sex = request.getParameter("sex");
        String hasChild = request.getParameter("hasChild");
        //String[] arryDomain = request.getParameterValues("domain");
        String strDomain = request.getParameter("domain");
        String[] arryDomain = null;
        if (strDomain != null) {
            arryDomain = strDomain.split(",");
        }
        String domain = getDomain(arryDomain);


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
        
        Page page = vipUserService.queryVipUser(userId, userName, nickName, province, city, tagId, principal, hasChild, domain, pageNo, pageSize, sidx + " " + sord);
        List<VIPUser> list = (List<VIPUser>) page.getResult();
        int records = page.getTotalCount();
        // 要显示的总的页数
        int totalPage = page.getTotalPageCount();
        String[][] arryFun = new String[list.size()][];

        for (int i = 0; i < list.size(); i++) {
            VIPUser fun = list.get(i);
          //查询条件“userId
            arryFun[i] = new String[]{String.valueOf(fun.getId()), String.valueOf(fun.getUserId()) , 
                    "<a href='/vipuser/manage/showVipHuodong.do?id=" + fun.getId() + "&userId=" + fun.getUserId() + queryCondition + "' title='查看用户信息' >"+fun.getUserName()+"</a>",
                    "<img width='36px' height='36px' src='http://tx1.douguo.net/" + fun.getHeadIcon() + "' >",
                    "<a target='_blank' href='http://www.douguo.com/u/" + fun.getUserName() + ".html' title='昵称' >" + fun.getNickName() + "</a>",
                    fun.getRealName(), fun.getAddress(), fun.getMobile(), fun.getCountry(),
                    fun.getProvince(), fun.getCity(), fun.getWeiboUrl(), fun.getTagName(), fun.getQqGroup(),
                    fun.getPrincipal(), fun.getHasChild(), fun.getCreatetime(),
                    "<a target='_blank' href='http://acp.douguo.net/index.php/douxiaoguo/msgbox?' title='私信' >私信</a>",
                    "<a href='/vipuser/manage/preEdit.do?id=" + fun.getId() + queryCondition + "' title='修改' >修改</a>",
                    "<a href='/vipuser/manage/showVipHuodong.do?id=" + fun.getId() + "&userId=" + fun.getUserId() + queryCondition + "' title='查看' >查看</a>",
                    "<a href='/vipuser/manage/deleteVipUser.do?id=" + fun.getId() + queryCondition + "' title='删除' >删除</a>"};
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
     * 导出渠道基本信息 - excel
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
        String hasChild = request.getParameter("hasChild");
        String[] arryDomain = request.getParameterValues("domain");
        String domain = getDomain(arryDomain);

        List<VIPUser> list = vipUserService.queryVipUser(userId, userName, nickName, province, city, tagId, principal, hasChild, domain);
        //
        model.put("list", list);
        return new ModelAndView(new VipUserExcelView(), model);
    }

    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String appId = request.getParameter("appId");
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        model.put("globalAppid", appId);

        return "/vip_user/vip_user_manager";
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
        //queryCondition = queryCondition.replaceAll("&id="+id,"");
        //
        // 获取类型list
        VIPUser vipUser = vipUserService.getVipUser(id);

        // 擅长领域
        String domain = vipUser.getDomain();
        if (StringUtils.isBlank(domain)) {
            domain = "0";
        }
        StringBuffer strDomain = new StringBuffer(Integer.toBinaryString(Integer.parseInt(domain)));

        while (strDomain.length() < 10) {
            strDomain.insert(0, "0");
        }

        String domain1 = strDomain.substring(9, 10);
        String domain2 = strDomain.substring(8, 9);
        String domain3 = strDomain.substring(7, 8);
        String domain4 = strDomain.substring(6, 7);
        String domain5 = strDomain.substring(5, 6);
        String domain6 = strDomain.substring(4, 5);
        String domain7 = strDomain.substring(3, 4);
        String domain8 = strDomain.substring(2, 3);
        //
        model.put("domain1", domain1);
        model.put("domain2", domain2);
        model.put("domain3", domain3);
        model.put("domain4", domain4);
        model.put("domain5", domain5);
        model.put("domain6", domain6);
        model.put("domain7", domain7);
        model.put("domain8", domain8);
        model.put("queryCondition", queryCondition);
        model.put("vipUser", vipUser);
        
        //获得VIP用户的活动数据，传入vip_user_modify.jsp
        List vipHuodongList = vipUserHuodongService.getVipUserHuodong(vipUser.getUserId()) ;
        model.put("vipUserHuodong", vipHuodongList) ;

        return "/vip_user/vip_user_modify";
    }
    
    
    /**
     * 修改页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/showVipHuodong")
    public String showVipHuodong(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String id = request.getParameter("id");
        String queryCondition = request.getQueryString();
        //queryCondition = queryCondition.replaceAll("&id="+id,"");
        
        // 获取类型list
        VIPUser vipUser = vipUserService.getVipUser(id);

        // 擅长领域
        String domain = vipUser.getDomain();
        if (StringUtils.isBlank(domain)) {
            domain = "0";
        }
        StringBuffer strDomain = new StringBuffer(Integer.toBinaryString(Integer.parseInt(domain)));

        while (strDomain.length() < 10) {
            strDomain.insert(0, "0");
        }

        String domain1 = strDomain.substring(9, 10);
        String domain2 = strDomain.substring(8, 9);
        String domain3 = strDomain.substring(7, 8);
        String domain4 = strDomain.substring(6, 7);
        String domain5 = strDomain.substring(5, 6);
        String domain6 = strDomain.substring(4, 5);
        String domain7 = strDomain.substring(3, 4);
        String domain8 = strDomain.substring(2, 3);
        //
        model.put("domain1", domain1);
        model.put("domain2", domain2);
        model.put("domain3", domain3);
        model.put("domain4", domain4);
        model.put("domain5", domain5);
        model.put("domain6", domain6);
        model.put("domain7", domain7);
        model.put("domain8", domain8);
        model.put("queryCondition", queryCondition);
        model.put("vipUser", vipUser);
        
        //获得VIP用户的活动数据，传入vip_user_show.jsp
        List vipHuodongList = vipUserHuodongService.getVipUserHuodong(vipUser.getUserId()) ;
        model.put("vipUserHuodong", vipHuodongList) ;

        return "/vip_user/vip_user_show";
    }
    
    /**
     * 删除VIP用户
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/deleteVipUser")
    public String deleteVipUser(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String id = request.getParameter("id");
    	vipUserService.deleteVipUser(id) ;
        return "redirect:/vipuser/manage/index.do" ;
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
            VIPUser vUser = vipUserService.getVipUserByUID(String.valueOf(userId));
            if (vUser == null) {
                VIPUser vipUser = new VIPUser();
                vipUser.setUserId(userId);
                vipUser.setUserName(userName);
                vipUser.setNickName(nickName);
                vipUser.setHeadIcon(headIcon);
                vipUser.setCreatetime(DateUtil.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
                //插入新达人
                vipUserService.insertVipUser(vipUser);
            }
        }
        //
        List<VIPUser> vipUserList = vipUserService.queryVipUser("", "", "", "", "", "", "", "", "");
        model.put("vipUserList", vipUserList);
        String appId = request.getParameter("appId");
        if (null == appId || appId.equals("")) {
            appId = "4";
        }
        model.put("globalAppid", appId);
        return "/vip_user/vip_user_manager";
    }

    @RequestMapping(value = "/update")
    public String update(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");

        String id = request.getParameter("id");
        String userId = request.getParameter("userId");
//        String username = request.getParameter("username");
//        String headicon = request.getParameter("headicon");
//        String nickname = request.getParameter("nickname");

        String realName = request.getParameter("realName");
        String adress = request.getParameter("address");
        String mobile = request.getParameter("mobile");
        String country = request.getParameter("country");
        String province = request.getParameter("province");
        String city = request.getParameter("city");

        String weiboUrl = request.getParameter("weiboUrl");
        String tagId = request.getParameter("tagId");
        String tagName = request.getParameter("tagName");
        String qqGroup = request.getParameter("qqGroup");
        String principal = request.getParameter("principal");

        //
        String sex = request.getParameter("sex");
        String qq = request.getParameter("qq");
        String webchat = request.getParameter("webchat");
        String hasChild = request.getParameter("hasChild");
        String userDesc = request.getParameter("userDesc");

        //处理擅长领域
        String[] arryDomain = request.getParameterValues("domain");
        String domain = getDomain(arryDomain);

        String queryCondition = request.getParameter("queryCondition");

        if (id != null && id.trim().length() > 0) {
            VIPUser vipUser = vipUserService.getVipUser(id);

            if (vipUser != null) {
                vipUser.setRealName(realName);
                vipUser.setAddress(adress);
                vipUser.setMobile(mobile);
                vipUser.setCountry(country);
                vipUser.setProvince(province);
                vipUser.setCity(city);
                //
                vipUser.setWeiboUrl(weiboUrl);
                vipUser.setTagId(Integer.parseInt(tagId));
                vipUser.setQqGroup(qqGroup);
                vipUser.setPrincipal(principal);
                //
                vipUser.setQq(qq);
                vipUser.setSex(sex);
                vipUser.setWebchat(webchat);
                vipUser.setHasChild(hasChild);
                vipUser.setDomain(domain);
                vipUser.setUserDesc(userDesc);
                //
                vipUserService.update(vipUser);
            }
        } else {
        }

        model.put("queryCondition", queryCondition);
        String[] arryQuery = queryCondition.split("&");
        for (String qy : arryQuery) {
            if (org.apache.commons.lang.StringUtils.isNotBlank(qy)) {
                String[] arryKV = qy.split("=");
                if (arryKV.length == 2) {
                    model.put(arryKV[0], arryKV[1]);
                    if (arryKV[0].equals("domain")) {
                        //处理擅长领域
                        String[] arryDons = arryKV[1].split(",");
                        for (String don : arryDons) {
                            model.put("domain" + don, 1);
                        }
                    }
                }
            }
        }

//        return "redirect:/vipuser/manage/index.do";
        return "/vip_user/vip_user_manager";
    }
    
    

    private String getDomain(String[] arryDomain) {
        StringBuffer sbBinary = new StringBuffer("0000000000");
        int nbLength = sbBinary.length();
        if (arryDomain != null) {
            for (String v : arryDomain) {
                if (StringUtils.isNotBlank(v)) {
                    Integer i = Integer.parseInt(v);
                    sbBinary.replace(nbLength - i, nbLength - i + 1, "1");
                }
            }
        }

        // 生成十进制擅长领域
        return String.valueOf(Integer.valueOf(sbBinary.toString(), 2));
    }

//    @RequestMapping(value = "/delete", method = RequestMethod.GET, produces = "application/html; charset=UTF-8")
//    public String deleteChannel(@RequestParam(value = "id", required = true) int id, HttpServletRequest request,
//                                HttpServletResponse response, ModelMap model) {
//        vipUserService.deleteChannelDict(id);
//        String appId = request.getParameter("appId");
//        if (null == appId || appId.equals("")) {
//            appId = "1";
//        }
//        model.put("globalAppid", appId);
//        return "/manage/vipuser";
//    }

//    @RequestMapping(value = "/channelTypeJson", method = RequestMethod.GET)
//    public void channelTypeJson(HttpServletRequest request, HttpServletResponse response, ModelMap model)
//            throws JSONException {
//
//		/* Get the params */
//        // String sortname = request.getParameter("sortname");
//        // String sortorder = request.getParameter("sortorder");
//        // String rp = request.getParameter("rp");
//        // String pageNo = request.getParameter("page");
//
//        /** 获取查询条件 */
//        // String qtype = request.getParameter("qtype");
//        // String query = request.getParameter("query");
//
//        // sortname = (sortname == null || sortname.equals("")) ? "event_code" :
//        // sortname;
//        // sortorder = (sortorder == null || sortorder.equals("")) ? "asc" :
//        // sortorder;
//        // int pagesize = (rp == null || rp.equals("")) ? 15 :
//        // Integer.parseInt(rp);
//        // int page_no = (pageNo == null || pageNo.equals("")) ? 1 :
//        // Integer.parseInt(pageNo);
//        // int startRow = (page_no - 1) * pagesize;
//
//        // String ret = this.vipUserService.getEventsListByStatus(1, sortname,
//        // sortorder, startRow, page_no, pagesize, qtype, query);
//
//        // JSONObject jObject = JSONObject.;
//
//        List<ChannelType> listCType1 = channelTypeService.queryListByLevel("1");
//        List<ChannelType> listCType2 = channelTypeService.queryListByLevel("2");
//        List<ChannelType> listCType3 = channelTypeService.queryListByLevel("3");
//
//        // 构造渠道类型 json
//        JSONArray channelTypeArray = new JSONArray();
//        for (ChannelType cType1 : listCType1) {
//            int id1 = cType1.getId();
//            //
//            JSONObject ObjChannelType1 = new JSONObject();
//            ObjChannelType1.put("ch1_name", cType1.getTypeName());
//            ObjChannelType1.put("ch1_id", cType1.getId());
//            JSONArray channelTypeArray1 = new JSONArray();
//            for (ChannelType cType2 : listCType2) {
//                int id2 = cType2.getId();
//                int parentId2 = cType2.getParentId();
//
//                // 取2级 parentId 等于 1级的id
//                if (id1 == parentId2) {
//                    JSONObject objChannelType2 = new JSONObject();
//                    JSONArray arryChannelType2 = new JSONArray();
//                    objChannelType2.put("ch2_name", cType2.getTypeName());
//                    objChannelType2.put("ch2_id", cType2.getId());
//                    //
//                    for (ChannelType cType3 : listCType3) {
//                        int id3 = cType3.getId();
//                        int parentId3 = cType3.getParentId();
//                        // 取3级 parentId 等于 2级的id
//                        if (id2 == parentId3) {
//                            JSONObject objChannelType3 = new JSONObject();
//                            objChannelType3.put("ch3_name", cType3.getTypeName());
//                            objChannelType3.put("ch3_id", cType3.getId());
//                            arryChannelType2.put(objChannelType3);
//                        }
//                    }// end for 3
//                    objChannelType2.put("d", arryChannelType2);
//
//                    channelTypeArray1.put(objChannelType2);
//                }
//            }// end for2
//            //
//
//            ObjChannelType1.put("c", channelTypeArray1);
//            channelTypeArray.put(ObjChannelType1);
//        }// end for1
//
//        String ret = "[ "
//                + "{\"p\":\"江西省\", \"c\":[ {\"ct\":\"南昌市\", \"d\":[ {\"dt\":\"西湖区\"}, {\"dt\":\"东湖区\"}, {\"dt\":\"高新区\"} ]}, {\"ct\":\"赣州市\", \"d\":[ {\"dt\":\"瑞金县\"} ]} ]},"
//                + " {\"p\":\"北京\", \"c\":[ {\"ct\":\"东城区\"}, {\"ct\":\"西城区\"} ]},"
//                + " {\"p\":\"河北省\", \"c\":[{\"ct\":\"石家庄\", \"d\":[ {\"dt\":\"长安区\"}, {\"dt\":\"桥东区\"}, {\"dt\":\"桥西区\"} ]},{\"ct\":\"唐山市\", \"d\":[ {\"dt\":\"滦南县\"}, {\"dt\":\"乐亭县\"}, {\"dt\":\"迁西县\"} ]} ]} "
//                + "]";
//        ret = channelTypeArray.toString();
//
//        response.setContentType("application/json;charset=UTF-8");
//        try {
//            response.getWriter().write(ret);
//            response.flushBuffer();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}