package com.douguo.dc.group.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.douguo.dg.group.service.DgGroupService;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.douguo.dc.group.service.GroupBaseStatService;
import com.douguo.dc.mail.service.impl.MailGroupStatService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/group/base")
public class GroupBaseStatController {
    @Autowired
    private GroupBaseStatService groupBaseStatService;

    @Autowired
    private DgGroupService dgGroupService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                       HttpServletRequest request, HttpServletResponse response, ModelMap model) throws JSONException {
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String startDate = request.getParameter("startDate");
        if (null == startDate || startDate.equals("")) {
            startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        }
        String endDate = request.getParameter("endDate");
        if (null == endDate || endDate.equals("")) {
            endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        }

        String qtype = request.getParameter("qtype");
        String app = request.getParameter("app");

        if (null == qtype || "".equals(qtype)) {
            qtype = "";
        }

        model.put("globalAppid", appId);
        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("qtype", qtype);
        model.put("app", app);

        /** 获取app应用列表数据 */
        List<Map<String, Object>> rowlst;
        if (null == qtype || "".equals(qtype)) {
            rowlst = groupBaseStatService.queryGroupList(startDate, endDate);
        } else {
            rowlst = groupBaseStatService.queryGroupList(startDate, endDate);
        }

        // 图表数据 begin
        List<BigDecimal> seriesTotalActions = new ArrayList<BigDecimal>();
        List<BigDecimal> seriesPosts = new ArrayList<BigDecimal>();
        List<BigDecimal> seriesReplies = new ArrayList<BigDecimal>();
        List<BigDecimal> seriesTotalUsers = new ArrayList<BigDecimal>();
        List<BigDecimal> seriesPv = new ArrayList<BigDecimal>();
        List<BigDecimal> seriesUv = new ArrayList<BigDecimal>();
        List<Date> xAxis = new ArrayList<Date>();
        for (Map<String, Object> map : rowlst) {
            seriesTotalActions.add((BigDecimal) map.get("total_actions"));
            seriesPosts.add((BigDecimal) map.get("posts"));
            seriesReplies.add((BigDecimal) map.get("replies"));
            seriesTotalUsers.add((BigDecimal) map.get("total_users"));
            seriesPv.add((BigDecimal) map.get("pv"));
            seriesUv.add((BigDecimal) map.get("uv"));
            Date date = (Date) map.get("statdate");
            if (!xAxis.contains(date)) {
                xAxis.add(date);
            }
        }

        JSONArray jsonArray = new JSONArray();
        JSONObject objTotalActions = buildJson("总发帖数", "line", seriesTotalActions);
        JSONObject objPosts = buildJson("主帖数", "line", seriesPosts);
        JSONObject objReplies = buildJson("回复数", "line", seriesReplies);
        JSONObject objTotalUsers = buildJson("发帖用户数", "line", seriesTotalUsers);
        JSONObject objPv = buildJson("总浏览数", "line", seriesPv);
        JSONObject objUv = buildJson("总浏览人数", "line", seriesUv);
        jsonArray.put(objPv);
        jsonArray.put(objUv);
        jsonArray.put(objTotalActions);
        jsonArray.put(objPosts);
        jsonArray.put(objReplies);
        jsonArray.put(objTotalUsers);

        model.put("series", jsonArray.toString());
        model.put("xAxis", buildJson("", "category", xAxis).toString());
        // 图标数据 end

        model.put("rowlst", rowlst);
        return "/group_base/group_base_list";
    }

    private static JSONObject buildJson(String name, String type, List data) throws JSONException {
        JSONObject jsonObj = new JSONObject();

        if (StringUtils.isNotBlank(name)) {
            jsonObj.put("name", name);
        }
        jsonObj.put("type", type);
        jsonObj.put("data", data);
        return jsonObj;
    }

    @RequestMapping(value = "/daylist", method = RequestMethod.GET)
    public String dayList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                          HttpServletRequest request, HttpServletResponse response, ModelMap model) throws JSONException {
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String startDate = request.getParameter("startDate");
        if (null == startDate || startDate.equals("")) {
            startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        }
        String endDate = request.getParameter("endDate");
        if (null == endDate || endDate.equals("")) {
            endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        }

        String app = request.getParameter("app");
        String qtype = request.getParameter("qtype");
        String keyword = request.getParameter("keyword");

        if (null == qtype || "".equals(qtype)) {
            qtype = "";
        }

        model.put("globalAppid", appId);
        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("keyword", keyword);
        model.put("app", app);
        model.put("qtype", qtype);
        List<Map<String, Object>> rowlst;
        /** 获取app应用列表数据 */
        if (qtype.equals("")) {
            rowlst = groupBaseStatService.queryGroupDayList(startDate, endDate);
        } else {
            rowlst = groupBaseStatService.queryGroupDayList(startDate, endDate);
        }

        // 图表数据 begin
        Map<String, List<BigDecimal>> mapChart = new HashMap<String, List<BigDecimal>>();
        List<String> listGroup = new ArrayList<String>();
        Map<String, BigDecimal> mapTmp = new HashMap<String, BigDecimal>();
        Map<String, String> mapGroups = dgGroupService.getGroupsMap();
        //
//		List<BigDecimal> seriesTotalActions = new ArrayList<BigDecimal>();
//		List<BigDecimal> seriesPosts = new ArrayList<BigDecimal>();
//		List<BigDecimal> seriesReplies = new ArrayList<BigDecimal>();
//		List<BigDecimal> seriesTotalUsers = new ArrayList<BigDecimal>();
//		List<BigDecimal> seriesPv = new ArrayList<BigDecimal>();
//		List<BigDecimal> seriesUv = new ArrayList<BigDecimal>();
        List<Date> xAxis = new ArrayList<Date>();
        for (Map<String, Object> map : rowlst) {
            String groupId = (String) map.get("group_id");

            //String groupName = (String)MailGroupStatService.getName(groupId);
            String groupName = (String) mapGroups.get(groupId);
            if (StringUtils.isBlank(groupName)) {
                groupName = groupId;
            }
            map.put("group_name", groupName);
            Date statDate = (Date) map.get("statdate");
            BigDecimal bdTotalActions = (BigDecimal) map.get("total_actions");

//			seriesTotalActions.add(bdTotalActions);
//			seriesPosts.add((BigDecimal) map.get("posts"));
//			seriesReplies.add((BigDecimal) map.get("replies"));
//			seriesTotalUsers.add((BigDecimal) map.get("total_users"));
//			seriesPv.add((BigDecimal) map.get("pv"));
//			seriesUv.add((BigDecimal) map.get("uv"));

            //只取正常的分组名称&同时排除“总计”
            if (!"".equals(groupName) && !"g".equals(groupId)) {
                mapTmp.put(groupName + statDate, bdTotalActions);
                //
                if (!listGroup.contains(groupName)) {
                    listGroup.add(groupName);
                }
            }

            //
            Date date = (Date) map.get("statdate");
            if (!xAxis.contains(date)) {
                xAxis.add(date);
            }
        }

        //
        for (String group : listGroup) {
            //
            for (Date dDate : xAxis) {
                BigDecimal bdVal = mapTmp.get(group + dDate);
                if (bdVal == null) {
                    bdVal = new BigDecimal(0);
                }
                List<BigDecimal> list;
                if (mapChart.containsKey(group)) {
                    list = mapChart.get(group);
                } else {
                    list = new ArrayList<BigDecimal>();
                }
                list.add(bdVal);
                mapChart.put(group, list);
            }
        }
        //
        //
        JSONArray jsonArray = new JSONArray();
        for (Entry<String, List<BigDecimal>> entry : mapChart.entrySet()) {
            String key = entry.getKey();
            List<BigDecimal> value = entry.getValue();
            JSONObject obj1 = buildJson(key, "line", value);
            jsonArray.put(obj1);
        }

        //
        JSONObject selectJson = new JSONObject();
        for (int i = 4; i < listGroup.size(); i++) {
            selectJson.put(listGroup.get(i), false);
        }
        //
        JSONObject legendJson = new JSONObject();
        legendJson.put("data", listGroup);
//		legendJson.put("orient", "vertical");
        legendJson.put("selected", selectJson);
        legendJson.put("x", "left");
        legendJson.put("y", "top");
        //
//		JSONArray jsonArray = new JSONArray();
//		JSONObject objTotalActions = buildJson("总发帖数", "line", seriesTotalActions);
//		JSONObject objPosts = buildJson("主帖数", "line", seriesPosts);
//		JSONObject objReplies = buildJson("回复数", "line", seriesReplies);
//		JSONObject objTotalUsers = buildJson("发帖用户数", "line", seriesTotalUsers);
//		JSONObject objPv = buildJson("总浏览数", "line", seriesPv);
//		JSONObject objUv = buildJson("总浏览人数", "line", seriesUv);
//		jsonArray.put(objPv);
//		jsonArray.put(objUv);
//		jsonArray.put(objTotalActions);
//		jsonArray.put(objPosts);
//		jsonArray.put(objReplies);
//		jsonArray.put(objTotalUsers);


        model.put("series", jsonArray.toString());
        model.put("legend", legendJson.toString());
        model.put("xAxis", buildJson("", "category", xAxis).toString());
        // 图标数据 end

        model.put("rowlst", rowlst);
        return "/group_base/group_day_list";
    }

    @RequestMapping(value = "/detaillist", method = RequestMethod.GET)
    public String detailList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                             HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String startDate = request.getParameter("startDate");
        if (null == startDate || startDate.equals("")) {
            startDate = DateUtil.getSpecifiedDayBefore(today, 1);
        }
        String endDate = request.getParameter("endDate");
        if (null == endDate || endDate.equals("")) {
            endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        }

        String groupId = request.getParameter("groupId");
        String app = request.getParameter("app");
        String qtype = request.getParameter("qtype");
        String keyword = request.getParameter("keyword");

        if (null == qtype || "".equals(qtype)) {
            qtype = "";
        }

        model.put("globalAppid", appId);
        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("keyword", keyword);
        model.put("app", app);
        model.put("qtype", qtype);
        model.put("groupId", groupId);
        List<Map<String, Object>> rowlst;
        /** 获取app应用列表数据 */
        if (qtype.equals("")) {
            rowlst = groupBaseStatService.queryGroupDetailList(groupId, startDate, endDate);
        } else {
            rowlst = groupBaseStatService.queryGroupDetailList(groupId, startDate, endDate);
        }

        Map<String, String> mapGroups = dgGroupService.getGroupsMap();
        //处理圈圈名字
        for (Map<String, Object> map : rowlst) {
            String gId = (String) map.get("group_id");
            String groupName = (String) mapGroups.get(gId);
            if (StringUtils.isBlank(groupName)) {
                groupName = groupId;
            }
            map.put("group_name", groupName);
        }

        model.put("rowlst", rowlst);
        return "/group_base/group_detail_list";
    }
}