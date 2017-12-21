package com.douguo.dc.channel.web;

import com.douguo.dc.applog.service.AppUserNewService;
import com.douguo.dc.channel.service.ChannelService;
import com.douguo.dc.channel.service.ChannelSumStatService;
import com.douguo.dc.channel.service.ChannelTypeService;
import com.douguo.dc.util.DateUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/channel/monitor")
public class ChannelMonitorController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelSumStatService channelSumStatService;

    @Autowired
    private AppUserNewService appUserNewService;

    @Autowired
    private ChannelTypeService channelTypeService;

    @RequestMapping(value = "/list")
    public String list(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                       HttpServletRequest request, HttpServletResponse response, ModelMap model) throws JSONException, UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String startDate = request.getParameter("startDate");
        if (null == startDate || startDate.equals("")) {
            startDate = DateUtil.getSpecifiedDayBefore(today, 7);
        }
        String endDate = request.getParameter("endDate");
        if (null == endDate || endDate.equals("")) {
            endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        }

        String app = request.getParameter("app");
        String userName = request.getParameter("userName");
        if (org.apache.commons.lang.StringUtils.isNotBlank(userName)) {
            userName = java.net.URLDecoder.decode(userName);
        }
        String tab = request.getParameter("tab");

        if (null == app || "".equals(app)) {
            app = "3";
        }

        if (null == tab || "".equals(tab)) {
            tab = "DAU";
        }

        model.put("globalAppid", appId);
        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("userName", userName);
        model.put("app", app);
        model.put("tab", tab);

        /** 获取app应用列表数据 */
        List<Map<String, Object>> rowlst = channelSumStatService.queryChannelSum(startDate, endDate, userName, "", "", "", "", "", "3", "4");

        List<BigDecimal> seriesNewUser = new ArrayList<BigDecimal>();
        List<BigDecimal> seriesDaus = new ArrayList<BigDecimal>();
        List<String> xAxis = new ArrayList<String>();
        int i = 0;
        for (Map<String, Object> map : rowlst) {
            i++;
            if (i > 20) {
                continue;
            }
            String channel = (String) map.get("channel");
            BigDecimal bdNews = (BigDecimal) map.get("news");
            if (bdNews == null) {
                bdNews = new BigDecimal(0);
            }
            //
            BigDecimal bdDaus = (BigDecimal) map.get("daus");
            if (bdDaus == null) {
                bdDaus = new BigDecimal(0);
            }
            seriesNewUser.add(bdNews);
            seriesDaus.add(bdDaus);

            if (!xAxis.contains(channel)) {
                xAxis.add(channel);
            }
        }

        JSONArray jsonArray = new JSONArray();
        JSONObject obj1 = buildJson("新增", "bar", seriesNewUser);
        // JSONObject obj2 = buildJson("日活数", "bar", seriesDaus);
        jsonArray.put(obj1);
        // jsonArray.put(obj2);

        model.put("series", jsonArray.toString());
        model.put("xAxis", buildJson("", "category", xAxis).toString());
        model.put("rowlst", rowlst);
        return "/channel/monitor/channel_monitor_list";
    }

    private static JSONObject buildJson(String name, String type, List data) throws JSONException {
        JSONObject jsonObj = new JSONObject();

        if (org.apache.commons.lang.StringUtils.isNotBlank(name)) {
            jsonObj.put("name", name);
        }
        jsonObj.put("type", type);
        jsonObj.put("data", data);
        return jsonObj;
    }

}