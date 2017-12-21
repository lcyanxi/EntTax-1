package com.douguo.dc.channel.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.douguo.dc.applog.service.AppUserNewService;
import com.douguo.dc.channel.model.Channel;
import com.douguo.dc.channel.model.ChannelType;
import com.douguo.dc.channel.service.ChannelService;
import com.douguo.dc.channel.service.ChannelSumStatService;
import com.douguo.dc.channel.service.ChannelTypeService;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;

@Controller
@RequestMapping("/channel/manage")
public class ChannelManageController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelSumStatService channelSumStatService;

    @Autowired
    private AppUserNewService appUserNewService;

    @Autowired
    private ChannelTypeService channelTypeService;

    @RequestMapping(value = "/queryJson")
    public void queryJson(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {

        request.setCharacterEncoding("utf-8");
        String rowsLimit = request.getParameter("rows"); // 取出每一页显示的行数
        String sidx = request.getParameter("sidx"); // 取出排序的项
        String sord = request.getParameter("sord"); // 取出排序方式：升序，降序
        String level = request.getParameter("level"); //
        //

        if (rowsLimit == null) // 设置每一页显示行数的默认值
        {
            rowsLimit = "10";
        }
        JQGridUtil t = new JQGridUtil();
        int nPage = 1; // 当前显示的页数
        int total = 1; // 要显示的总的页数，初始值为1

        if (StringUtils.isBlank(level)) {
            level = "1";
        }

        List<Channel> list = channelService.getChannelListByStatus(1);

        int records = list.size();

        total = records / Integer.parseInt(rowsLimit) + 1; // 计算总的页数
        String[][] arryFun = new String[list.size()][];

        for (int i = 0; i < list.size(); i++) {
            Channel fun = list.get(i);
            arryFun[i] = new String[]{String.valueOf(fun.getId()), fun.getChannelTypeName1(),
                    fun.getChannelTypeName2(), fun.getChannelTypeName3(), fun.getChannelName(), fun.getChannelCode(), fun.getChannelTag(),
                    fun.getUserName(), fun.getPrincipal(), fun.getPrincipalDep(), fun.getPrincipalContact(),
                    fun.getChannelCooperation(), fun.getPlanDesc(),
                    "<a href='/channel/manage/preEdit.do?id=" + fun.getId() + "' title='修改' >修改</a>"};
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
        GridPager<Map> gridPager = new GridPager<Map>(nPage, total, records, rows); // 将表格显示的配置初始化给GridPager
        t.toJson(gridPager, response); // 发送json数据
    }

    /**
     * 导出渠道基本信息 - excel
     *
     * @param request
     * @param response
     * @param model
     * @param appId
     * @param version
     * @return
     */
    @RequestMapping(value = "/exportBaseExcel")
    public ModelAndView exportBaseExcel(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //
        List<Channel> list = channelService.getChannelListByStatus(1);
        //
        model.put("list", list);
        return new ModelAndView(new ChannelBaseExcelView(), model);
    }

    /**
     * 导出渠道汇总信息 - excel
     *
     * @param request
     * @param response
     * @param model
     * @param appId
     * @param version
     * @return
     */
    @RequestMapping(value = "/exportSumExcel")
    public ModelAndView exportSumExcel(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //
        String startDate = request.getParameter("start_date"); //
        String endDate = request.getParameter("end_date"); //
        List<Map<String, Object>> list = channelSumStatService.queryChannelSumList(startDate, endDate, "3","4");
        List<Map<String, Object>> listTotal = appUserNewService.getNewUserTotal(startDate, endDate, "3", "4");
        List<Map<String, Object>> listAppTotal = appUserNewService.getNewUserTotalForApp(startDate, endDate, "3", "4");

        model.put("list", list);
        model.put("listTotal", listTotal);
        model.put("listAppTotal", listAppTotal);
        return new ModelAndView(new ChannelSumExcelView(), model);
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String manageChannel(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String appId = request.getParameter("appId");
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        model.put("globalAppid", appId);

        return "/channel/manage/channel_manager";
    }

    /**
     * 修改页面
     *
     * @param request
     * @param response
     * @param model
     * @param appId
     * @param version
     * @return
     */
    @RequestMapping(value = "/preEdit")
    public String preEdit(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //
        String id = request.getParameter("id");
        //
        // 获取类型list
        Channel channel = channelService.getChannel(id);
        model.put("channel", channel);

        return "/channel/manage/channel_manager_modify";
    }

    @RequestMapping(value = "/save")
    public String saveChannel(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");

        String channelName = (String) request.getParameter("channelName");
        String channelCode = (String) request.getParameter("channelCode");
        channelService.insertChannelDict(channelCode, channelName);
        List<Channel> channelList = channelService.getChannelListByStatus(1);
        model.put("channels", channelList);
        String appId = request.getParameter("appId");
        if (null == appId || appId.equals("")) {
            appId = "4";
        }
        model.put("globalAppid", appId);
        return "/channel/manage/channel_manager";
    }

    @RequestMapping(value = "/update")
    public String update(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");

        String id = request.getParameter("id");
        String channelCode = request.getParameter("channelCode");
        String channelName = request.getParameter("channelName");
        String channelTag = request.getParameter("channelTag");

        String principal = request.getParameter("principal");
        String userName = request.getParameter("userName");
        String principalDep = request.getParameter("principalDep");
        String principalContact = request.getParameter("principalContact");
        String planDesc = request.getParameter("planDesc");
        String channelCooperation = request.getParameter("channelCooperation");
        String channelPlat = request.getParameter("channelPlat");
        String channelType1 = request.getParameter("channelType1");
        String channelType2 = request.getParameter("channelType2");
        String channelType3 = request.getParameter("channelType3");

        if (id != null && id.trim().length() > 0) {
            Channel channel = channelService.getChannel(id);

            if (channel != null) {
                channel.setId(Integer.parseInt(id));
                //
                channel.setChannelCode(channelCode);
                channel.setChannelName(channelName);
                channel.setChannelTag(channelTag);
                channel.setPrincipal(principal);
                channel.setUserName(userName);
                channel.setPrincipalDep(principalDep);
                channel.setPrincipalContact(principalContact);
                channel.setPlanDesc(planDesc);
                //
                channel.setChannelCooperation(channelCooperation);
                if (StringUtils.isNotBlank(channelPlat)) {
                    channel.setChannelPlat(channelPlat);
                }

                if (StringUtils.isNotBlank(channelType1)) {
                    channel.setChannelType1(Long.parseLong(channelType1));
                }
                if (StringUtils.isNotBlank(channelType2)) {
                    channel.setChannelType2(Long.parseLong(channelType2));
                }
                if (StringUtils.isNotBlank(channelType3)) {
                    channel.setChannelType3(Long.parseLong(channelType3));
                }

                channelService.update(channel);
            }
        } else {
        }
        return "redirect:/channel/manage/index.do";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET, produces = "application/html; charset=UTF-8")
    public String deleteChannel(@RequestParam(value = "id", required = true) int id, HttpServletRequest request,
                                HttpServletResponse response, ModelMap model) {
        channelService.deleteChannelDict(id);
        String appId = request.getParameter("appId");
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        model.put("globalAppid", appId);
        return "/manage/channel";
    }

    @RequestMapping(value = "/channelTypeJson", method = RequestMethod.GET)
    public void channelTypeJson(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws JSONException {

		/* Get the params */
        // String sortname = request.getParameter("sortname");
        // String sortorder = request.getParameter("sortorder");
        // String rp = request.getParameter("rp");
        // String pageNo = request.getParameter("page");

        /** 获取查询条件 */
        // String qtype = request.getParameter("qtype");
        // String query = request.getParameter("query");

        // sortname = (sortname == null || sortname.equals("")) ? "event_code" :
        // sortname;
        // sortorder = (sortorder == null || sortorder.equals("")) ? "asc" :
        // sortorder;
        // int pagesize = (rp == null || rp.equals("")) ? 15 :
        // Integer.parseInt(rp);
        // int page_no = (pageNo == null || pageNo.equals("")) ? 1 :
        // Integer.parseInt(pageNo);
        // int startRow = (page_no - 1) * pagesize;

        // String ret = this.channelService.getEventsListByStatus(1, sortname,
        // sortorder, startRow, page_no, pagesize, qtype, query);

        // JSONObject jObject = JSONObject.;

        List<ChannelType> listCType1 = channelTypeService.queryListByLevel("1");
        List<ChannelType> listCType2 = channelTypeService.queryListByLevel("2");
        List<ChannelType> listCType3 = channelTypeService.queryListByLevel("3");

        // 构造渠道类型 json
        JSONArray channelTypeArray = new JSONArray();
        for (ChannelType cType1 : listCType1) {
            int id1 = cType1.getId();
            //
            JSONObject ObjChannelType1 = new JSONObject();
            ObjChannelType1.put("ch1_name", cType1.getTypeName());
            ObjChannelType1.put("ch1_id", cType1.getId());
            JSONArray channelTypeArray1 = new JSONArray();
            for (ChannelType cType2 : listCType2) {
                int id2 = cType2.getId();
                int parentId2 = cType2.getParentId();

                // 取2级 parentId 等于 1级的id
                if (id1 == parentId2) {
                    JSONObject objChannelType2 = new JSONObject();
                    JSONArray arryChannelType2 = new JSONArray();
                    objChannelType2.put("ch2_name", cType2.getTypeName());
                    objChannelType2.put("ch2_id", cType2.getId());
                    //
                    for (ChannelType cType3 : listCType3) {
                        int id3 = cType3.getId();
                        int parentId3 = cType3.getParentId();
                        // 取3级 parentId 等于 2级的id
                        if (id2 == parentId3) {
                            JSONObject objChannelType3 = new JSONObject();
                            objChannelType3.put("ch3_name", cType3.getTypeName());
                            objChannelType3.put("ch3_id", cType3.getId());
                            arryChannelType2.put(objChannelType3);
                        }
                    }// end for 3
                    objChannelType2.put("d", arryChannelType2);

                    channelTypeArray1.put(objChannelType2);
                }
            }// end for2
            //

            ObjChannelType1.put("c", channelTypeArray1);
            channelTypeArray.put(ObjChannelType1);
        }// end for1

        String ret = "[ "
                + "{\"p\":\"江西省\", \"c\":[ {\"ct\":\"南昌市\", \"d\":[ {\"dt\":\"西湖区\"}, {\"dt\":\"东湖区\"}, {\"dt\":\"高新区\"} ]}, {\"ct\":\"赣州市\", \"d\":[ {\"dt\":\"瑞金县\"} ]} ]},"
                + " {\"p\":\"北京\", \"c\":[ {\"ct\":\"东城区\"}, {\"ct\":\"西城区\"} ]},"
                + " {\"p\":\"河北省\", \"c\":[{\"ct\":\"石家庄\", \"d\":[ {\"dt\":\"长安区\"}, {\"dt\":\"桥东区\"}, {\"dt\":\"桥西区\"} ]},{\"ct\":\"唐山市\", \"d\":[ {\"dt\":\"滦南县\"}, {\"dt\":\"乐亭县\"}, {\"dt\":\"迁西县\"} ]} ]} "
                + "]";
        ret = channelTypeArray.toString();

        response.setContentType("application/json;charset=UTF-8");
        try {
            response.getWriter().write(ret);
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}