package com.douguo.dc.applog.web;

import com.douguo.dc.applog.service.AppMauService;
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
import java.util.Map.Entry;

@Controller
@RequestMapping("/appmau")
public class AppMauController {
	@Autowired
	private AppMauService appMauService;

	@RequestMapping(value = "/maulist", method = RequestMethod.GET)
	public String dauList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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
			tab = "MAU";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("app", app);
		model.put("tab", tab);

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst;

		if (null == app || "".equals(app)) {
			rowlst = appMauService.querySumList(startDate, endDate);
		} else {
			rowlst = appMauService.querySumList(startDate, endDate);
		}

		List<BigDecimal> seriesIos = new ArrayList<BigDecimal>();
		List<BigDecimal> seriesAndroid = new ArrayList<BigDecimal>();
		List<Date> xAxis = new ArrayList<Date>();

		for (Map<String, Object> map : rowlst) {
			Integer nApp = (Integer) map.get("app");

			if (nApp == 3) {
				seriesIos.add((BigDecimal) map.get("uid"));
			} else if (nApp == 4) {
				seriesAndroid.add((BigDecimal) map.get("uid"));
			}

			Date date = (Date) map.get("statdate");
			if (!xAxis.contains(date)) {
				xAxis.add(date);
			}
		}

		JSONArray jsonArray = new JSONArray();
		JSONObject obj1 = buildJson("IOS月活数", "line", seriesIos);
		JSONObject obj2 = buildJson("Android月活数", "line", seriesAndroid);
		jsonArray.put(obj1);
		jsonArray.put(obj2);

		model.put("series", jsonArray.toString());
		model.put("xAxis", buildJson("", "category", xAxis).toString());
		model.put("rowlst", rowlst);
		return "/app_mau/mau_list";
	}

	@RequestMapping(value = "/maulistAll", method = RequestMethod.GET)
	public String mauListAll(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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

		String tab = request.getParameter("tab");

		if (null == tab || "".equals(tab)) {
			tab = "MAU_ALL";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("tab", tab);

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlstAll;

		rowlstAll = appMauService.querySumListAll(startDate, endDate);

		List<BigDecimal> seriesAll = new ArrayList<BigDecimal>();
		List<Date> xAxis = new ArrayList<Date>();

		for (Map<String, Object> map : rowlstAll) {
			seriesAll.add((BigDecimal) map.get("uid"));

			Date date = (Date) map.get("statdate");
			if (!xAxis.contains(date)) {
				xAxis.add(date);
			}
		}

		JSONArray jsonArray = new JSONArray();
		JSONObject obj = buildJson("客户端月活总数", "line", seriesAll);
		jsonArray.put(obj);

		model.put("series", jsonArray.toString());
		model.put("xAxis", buildJson("", "category", xAxis).toString());
		model.put("rowlstAll", rowlstAll);
		return "/app_mau/mau_list_all";
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

	@RequestMapping(value = "/maudetaillist", method = RequestMethod.GET)
	public String dauDetailListNew(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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
		String tab = request.getParameter("tab");
		String channel = request.getParameter("channel");
		String vers = request.getParameter("vers");

		if (null == app || "".equals(app)) {
			app = "3";
		}

		if (null == tab || "".equals(tab)) {
			tab = "CHANNEL";
		}

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst = appMauService.queryChannelQualityListByApp(startDate, endDate, app, channel);
		// ----构造图表数据 begin -------
		// List<BigDecimal> seriesIos = new ArrayList<BigDecimal>();
		// List<BigDecimal> seriesAndroid = new ArrayList<BigDecimal>();
		List<Date> xAxis = new ArrayList<Date>();
		Map<String, List<BigDecimal>> mapChart = new HashMap<String, List<BigDecimal>>();

		Map<String, BigDecimal> mapTmp = new HashMap<String, BigDecimal>();
		List<String> listChannel = new ArrayList<String>();
		//
		listChannel.add("zhuzhan");
		listChannel.add("qqkp");
		listChannel.add("baidu");
		listChannel.add("360box");
		listChannel.add("miui");
		listChannel.add("YDY");
		listChannel.add("hiapk");
		listChannel.add("wandoujia");
		listChannel.add("huawei");
		listChannel.add("oppo");
		//
		for (Map<String, Object> map : rowlst) {
			String strChannel = (String) map.get("channel");
			Date date = (Date) map.get("statdate");
			BigDecimal bdRs = (BigDecimal) map.get("rs");

			mapTmp.put(strChannel + date, bdRs);
			if (!listChannel.contains(strChannel) && listChannel.size() < 30) {
				listChannel.add(strChannel);
			}

			if (!xAxis.contains(date)) {
				xAxis.add(date);
			}
		}

		for (String chanl : listChannel) {
			//
			for (Date dDate : xAxis) {
				BigDecimal bdVal = mapTmp.get(chanl + dDate);
				if (bdVal == null) {
					bdVal = new BigDecimal(0);
				}
				List<BigDecimal> list;
				if (mapChart.containsKey(chanl)) {
					list = mapChart.get(chanl);
				} else {
					list = new ArrayList<BigDecimal>();
				}
				list.add(bdVal);
				mapChart.put(chanl, list);
			}
		}

		//
		JSONArray jsonArray = new JSONArray();
		for (Entry<String, List<BigDecimal>> entry : mapChart.entrySet()) {
			String key = entry.getKey();
			List<BigDecimal> value = entry.getValue();
			JSONObject obj1 = buildJson(key, "line", value);
			jsonArray.put(obj1);
		}
		JSONObject selectJson = new JSONObject();
		for (int i = 3; i < listChannel.size(); i++) {
			selectJson.put(listChannel.get(i), false);
		}

		JSONObject legendJson = new JSONObject();
		legendJson.put("data", listChannel);
		legendJson.put("orient", "vertical");
		legendJson.put("selected", selectJson);
		legendJson.put("x", "left");
		legendJson.put("y", "top");
		model.put("legend", legendJson.toString());
		model.put("series", jsonArray.toString());
		model.put("xAxis", buildJson("", "category", xAxis).toString());
		// ----构造图表数据 end -------
		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("app", app);
		model.put("tab", tab);

		model.put("rowlst", rowlst);
		return "/app_mau/mau_channel_list";
	}


	@RequestMapping(value = "/mauverlist", method = RequestMethod.GET)
	public String dauVerList(@RequestParam(value = "appId", required = false, defaultValue = "4") String appId,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws JSONException {
		if (null == appId || appId.equals("")) {
			appId = "4";
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

		String tab = request.getParameter("tab");

		if (null == tab || "".equals(tab)) {
			tab = "VER";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("tab", tab);

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst;
		rowlst = appMauService.queryVersListByApp(startDate, endDate, appId);

		List<String> listVer = new ArrayList<String>();
		List<String> listVerView = new ArrayList<String>();
		List<String> listDate = new ArrayList<String>();

		Map<String, List<Map<String, String>>> mapSer = new HashMap<String, List<Map<String, String>>>();
		for (Map<String, Object> map : rowlst) {
			String vers = (String) map.get("vers");
			Date date = (Date) map.get("statdate");
			String strDate = DateUtil.date2Str(date, "yyyy-MM-dd");
			//
			/*
			if (mapSer.containsKey(strDate)) {
				List<Map<String, String>> listTmp = mapSer.get(strDate);

				Map<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("name", (String) map.get("vers"));
				tmpMap.put("value", String.valueOf((BigDecimal) map.get("uid")));
				listTmp.add(tmpMap);

				mapSer.put(strDate, listTmp);
			} else {
				List<Map<String, String>> listTmp = new ArrayList<Map<String, String>>();
				
				Map<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("name", (String) map.get("vers"));
				tmpMap.put("value", String.valueOf((BigDecimal) map.get("uid")));
				listTmp.add(tmpMap);
				
				mapSer.put(strDate, listTmp);
			}
			*/
			List<Map<String, String>> listTmp  = mapSer.get(strDate);
			if(listTmp == null){
				listTmp = new ArrayList<Map<String, String>>();
			}
			//
			Map<String, String> tmpMap = new HashMap<String, String>();
			tmpMap.put("name", (String) map.get("vers"));
			tmpMap.put("value", String.valueOf((BigDecimal) map.get("uid")));
			if(listTmp.size() < 10){
				listTmp.add(tmpMap);
			}else{
				if(listTmp.size() == 10){
					Map<String, String> tMap = new HashMap<String, String>();
					tMap.put("name", "other");
					tMap.put("value", String.valueOf((BigDecimal) map.get("uid")));
					listTmp.add(tMap);
				}else{
					Map<String, String> tMap = listTmp.get(10);
					BigDecimal bdUid = (BigDecimal) map.get("uid");
					bdUid = bdUid.add(new BigDecimal(tMap.get("value")));
					tMap.put("value", String.valueOf(bdUid));
				}
			}
			
			
			mapSer.put(strDate, listTmp);
			
			if (!listDate.contains(strDate)) {
				listDate.add(strDate);
			}
			if (!listVer.contains(vers)) {
				listVer.add(vers);
			}
			
			if (!listVerView.contains(vers) && listVerView.size() <= 9) {
				listVerView.add(vers);
			}
		}

		// timeline
		JSONObject timelineJson = new JSONObject();
		timelineJson.put("data", listDate);

		// legend
		JSONObject legendJson = new JSONObject();
		legendJson.put("data", listVer);

		// JSONObject jsonSeries = new JSONObject("");
		JSONArray jsonArrayFirst = new JSONArray();
		JSONArray jsonArray = new JSONArray();
		int i = 0;
//		for (Entry<String, List<Map<String, String>>> entry : mapSer.entrySet()) {
		for(String verDate : listDate){
			i++;
//			List<Map<String, String>> ll = entry.getValue();
			List<Map<String, String>> ll = mapSer.get(verDate);

			JSONObject obj1 = buildJson("", "pie", ll);

			if (i == 1) {
				obj1.put("radius", "50%");
				jsonArrayFirst.put(obj1);
			} else {
				JSONObject jsonSer = new JSONObject();
				JSONArray jsonArraySer = new JSONArray();
				jsonArraySer.put(obj1);
				jsonSer.put("series", jsonArraySer);
				jsonArray.put(jsonSer);
			}
		}

		String strFirstSer = jsonArrayFirst.toString();
		String strSer = jsonArray.toString();
		model.put("timeline", timelineJson.toString());
		model.put("legend", legendJson.toString());
		model.put("firstSeries", strFirstSer);
		model.put("series", strSer.substring(1, strSer.length() - 1));
		model.put("rowlst", rowlst);
		return "/app_mau/mau_version_list";
	}
	
	@RequestMapping(value = "/mau7daylist", method = RequestMethod.GET)
	public String dau7Day(@RequestParam(value = "appId", required = false, defaultValue = "4") String appId,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws JSONException {
		if (null == appId || appId.equals("")) {
			appId = "4";
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

		String tab = request.getParameter("tab");

		if (null == tab || "".equals(tab)) {
			tab = "VER";
		}

		/** 获取新增用户数 */

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst;
		rowlst = appMauService.queryVersListByApp(startDate, endDate, appId);

		List<String> listVer = new ArrayList<String>();
		List<String> listVerView = new ArrayList<String>();
		List<String> listDate = new ArrayList<String>();
	
		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("tab", tab);
		model.put("rowlst", rowlst);
		return "/app_mau/mau_version_list";
	}
}