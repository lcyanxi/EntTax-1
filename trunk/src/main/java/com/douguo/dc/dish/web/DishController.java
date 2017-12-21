package com.douguo.dc.dish.web;

import com.douguo.dc.dish.service.DishService;
import com.douguo.dc.util.DateUtil;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    private Map<Integer, String> getSource() {

        Map<Integer, String> sourceMap = new HashMap<Integer, String>();
        sourceMap.put(0, "网站");
        sourceMap.put(3, "IOS豆果美食");
        sourceMap.put(4, "Andriod豆果美食");
        return sourceMap;
    }


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
            startDate = DateUtil.getSpecifiedDayBefore(today, 6);
        }
        String endDate = request.getParameter("endDate");
        if (null == endDate || endDate.equals("")) {
            endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        }

        String type = request.getParameter("type");
        if (StringUtils.isBlank(type)) {
            type = "source";
        }

        model.put("globalAppid", appId);
        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("type", type);

        /** 获取app应用列表数据 */
        Map<Integer, String> sourceMap = this.getSource();
//		Map<String, String> dishNameMap = this.getLevelName();
        // provinceMap.get("11");
        List<Map<String, Object>> rowlst = dishService.queryDishListBySource(startDate, endDate);
        //作品数
        Map<String, String> rowMapDishs = new HashMap<String, String>();
        //人数
        Map<String, String> rowMapUsers = new HashMap<String, String>();
        //tagName转换
        Map<String, String> rowMapName = new HashMap<String, String>();
        Set<String> tableLine = new LinkedHashSet<String>();
        Set<String> tableColumn = new LinkedHashSet<String>();
        //作品总计
        Integer dishsSum = 0;
        //用户数总计
        Integer usersSum = 0;
        String tmpDate = "";
        for (Map<String, Object> map : rowlst) {

            Integer source = (Integer) map.get("source");
            String sourceName = (String) sourceMap.get(source);

            String statdate = map.get("statdate").toString();
            Integer dishs = (Integer) map.get("dishs");
            Integer uids = (Integer) map.get("uids");
            if (dishs == null) {
                dishs = 0;
            }

            if (uids == null) {
                uids = 0;
            }

            tableLine.add(statdate);
            tableColumn.add(String.valueOf(source));
            rowMapName.put(String.valueOf(source), sourceName);

            if (type.equals("source")) {
                rowMapDishs.put(String.valueOf(source) + '-' + statdate, String.valueOf(dishs));

                if ("".equals(tmpDate)) {
                    dishsSum += dishs;
                } else if (tmpDate.equals(statdate)) {
                    dishsSum += dishs;
                } else {
                    dishsSum = dishs;
                }
                rowMapDishs.put("总计" + '-' + statdate, String.valueOf(dishsSum));
                tmpDate = statdate;
                request.getSession().setAttribute("rowMapDishs", rowMapDishs);
            } else {
                rowMapUsers.put(String.valueOf(source) + '-' + statdate, String.valueOf(uids));
                if ("".equals(tmpDate)) {
                    usersSum += uids;
                } else if (tmpDate.equals(statdate)) {
                    usersSum += uids;
                } else {
                    usersSum = uids;
                }
                rowMapUsers.put("总计" + '-' + statdate, String.valueOf(usersSum));
                tmpDate = statdate;
                request.getSession().setAttribute("rowMapUsers", rowMapUsers);
            }
        }
        //
        tableColumn.add("总计");

        request.getSession().setAttribute("rowMapName", rowMapName);
        request.getSession().setAttribute("tableLine", tableLine);
        request.getSession().setAttribute("tableColumn", tableColumn);

        //图表begin
        List<Integer> seriesWeb = new ArrayList<Integer>();
        List<Integer> seriesAndroid = new ArrayList<Integer>();
        List<Integer> seriesIos = new ArrayList<Integer>();
        //
        List<Date> xAxis = new ArrayList<Date>();
        for (Map<String, Object> map : rowlst) {
            Integer source = (Integer) map.get("source");
            if (source.equals(0)) {
                seriesWeb.add((Integer) map.get("dishs"));
            } else if (source.equals(3)) {
                seriesIos.add((Integer) map.get("dishs"));
            } else if (source.equals(4)) {
                seriesAndroid.add((Integer) map.get("dishs"));
            }
            Date date = (Date) map.get("statdate");
            if (!xAxis.contains(date)) {
                xAxis.add(date);
            }
        }

        JSONArray jsonArray = new JSONArray();
        JSONObject objWeb = buildJson("网站", "line", seriesWeb);
        JSONObject objAndroid = buildJson("Android豆果美食", "line", seriesAndroid);
        JSONObject objIos = buildJson("IOS豆果美食", "line", seriesIos);
        jsonArray.put(objWeb);
        jsonArray.put(objAndroid);
        jsonArray.put(objIos);

        model.put("series", jsonArray.toString());
        model.put("xAxis", buildJson("", "category", xAxis).toString());
        model.put("rowlst", rowlst);
        //图表end

        if (type.equals("source")) {
            return "/dish/dish_list";
        } else {
            return "/dish/dish_user_list";
        }
    }

    /**
     * 作品周统计
     *
     * @param appId
     * @param request
     * @param response
     * @param model
     * @return
     * @throws JSONException
     */
    @RequestMapping(value = "/weeklist", method = RequestMethod.GET)
    public String weeklist(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                           HttpServletRequest request, HttpServletResponse response, ModelMap model) throws JSONException {
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

        String type = request.getParameter("type");

        if (null == type || "".equals(type)) {
            type = "week";
        }

        model.put("globalAppid", appId);
        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("type", type);

        /** 获取type应用列表数据 */
        List<Map<String, Object>> rowlst = dishService.queryDishListByWeek(startDate, endDate);

        List<BigDecimal> seriesWeek = new ArrayList<BigDecimal>();
        List<BigDecimal> seriesWww = new ArrayList<BigDecimal>();
        List<String> xAxis = new ArrayList<String>();
        for (Map<String, Object> map : rowlst) {
            seriesWeek.add((BigDecimal) map.get("dishs"));
            Integer statyear = (Integer) map.get("stat_year");
            Integer statWeek = (Integer) map.get("stat_week");
            String strDate = statyear + "-" + statWeek;
            if (!xAxis.contains(strDate)) {
                xAxis.add(strDate);
            }
        }

        JSONArray jsonArray = new JSONArray();
        JSONObject obj1 = buildJson("周作品数", "line", seriesWeek);
        jsonArray.put(obj1);

        model.put("series", jsonArray.toString());
        model.put("xAxis", buildJson("", "category", xAxis).toString());
        model.put("rowlst", rowlst);
        return "/dish/dish_week_list";
    }

    /**
     * 作品周统计
     *
     * @param appId
     * @param request
     * @param response
     * @param model
     * @return
     * @throws JSONException
     */
    @RequestMapping(value = "/monthlist", method = RequestMethod.GET)
    public String monthlist(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                           HttpServletRequest request, HttpServletResponse response, ModelMap model) throws JSONException {
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

        String type = request.getParameter("type");

        if (null == type || "".equals(type)) {
            type = "month";
        }

        model.put("globalAppid", appId);
        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("type", type);

        /** 获取type应用列表数据 */
        List<Map<String, Object>> rowlst = dishService.queryDishListByMonth(startDate, endDate);

        List<BigDecimal> seriesWeek = new ArrayList<BigDecimal>();
        List<BigDecimal> seriesWww = new ArrayList<BigDecimal>();
        List<String> xAxis = new ArrayList<String>();
        for (Map<String, Object> map : rowlst) {
            seriesWeek.add((BigDecimal) map.get("dishs"));
            Integer statyear = (Integer) map.get("stat_year");
            Integer statMonth = (Integer) map.get("stat_month");
            String strDate = statyear + "-" + statMonth;
            if (!xAxis.contains(strDate)) {
                xAxis.add(strDate);
            }
        }

        JSONArray jsonArray = new JSONArray();
        JSONObject obj1 = buildJson("月作品数", "line", seriesWeek);
        jsonArray.put(obj1);

        model.put("series", jsonArray.toString());
        model.put("xAxis", buildJson("", "category", xAxis).toString());
        model.put("rowlst", rowlst);
        return "/dish/dish_month_list";
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
}