package com.douguo.dc.applog.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.douguo.dc.applog.service.AppWauService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/appwau")
public class AppWauController {
	@Autowired
	private AppWauService appWauService;

	@RequestMapping(value = "/waulist", method = RequestMethod.GET)
	public String wauList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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

		String app = request.getParameter("app");
		String tab = request.getParameter("tab");

		if (null == app || "".equals(app)) {
			app = "3";
		}

		if (null == tab || "".equals(tab)) {
			tab = "WAU";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("app", app);
		model.put("tab", tab);

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst;
		if (null == app || "".equals(app)) {
			rowlst = appWauService.queryListByType(startDate, endDate,"ALL");
		} else {
			rowlst = appWauService.queryListByType(startDate, endDate,"ALL");
			// rowlst = appDauService.querySumListByApp(startDate, endDate,
			// app);
		}

		List<Long> seriesIos = new ArrayList<Long>();
		List<Long> seriesAndroid = new ArrayList<Long>();
		List<Date> xAxis = new ArrayList<Date>();
		for (Map<String, Object> map : rowlst) {
			Integer nApp = (Integer) map.get("app");
			if (nApp == 3) {
				seriesIos.add((Long) map.get("uid"));
			} else if (nApp == 4) {
				seriesAndroid.add((Long) map.get("uid"));
			}
			Date date = (Date) map.get("statdate");
			if (!xAxis.contains(date)) {
				xAxis.add(date);
			}
		}

		JSONArray jsonArray = new JSONArray();
		JSONObject obj1 = buildJson("IOS周活数", "line", seriesIos);
		JSONObject obj2 = buildJson("Android周活数", "line", seriesAndroid);
		jsonArray.put(obj1);
		jsonArray.put(obj2);

		model.put("series", jsonArray.toString());
		model.put("xAxis", buildJson("", "category", xAxis).toString());
		model.put("rowlst", rowlst);
		return "/app_wau/wau_list";
	}

	public static void main(String[] args) {
		String name = "test";
		String type = "bar";
		List<String> data = new ArrayList<String>();
		data.add("12");
		data.add("15");
		data.add("16");
		data.add("17");
		try {
			JSONObject tmp = AppWauController.buildJson(name, type, data);
			JSONObject tmp2 = AppWauController.buildJson(name, type, data);
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(tmp);
			jsonArray.put(tmp2);
			System.out.println(jsonArray.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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